package com.example.genie_tune_java.domain.webtoon.service;

import com.example.genie_tune_java.common.dto.ServiceAccessIdPrincipal;
import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.attach.entity.Attach;
import com.example.genie_tune_java.domain.attach.entity.AttachTargetType;
import com.example.genie_tune_java.domain.attach.repository.AttachRepository;
import com.example.genie_tune_java.domain.attach.service.AttachService;
import com.example.genie_tune_java.domain.service_access.entity.ServiceAccess;
import com.example.genie_tune_java.domain.service_access.repository.ServiceAccessRepository;
import com.example.genie_tune_java.domain.webtoon.dto.page.WebtoonGroupResponseDTO;
import com.example.genie_tune_java.domain.webtoon.dto.WebtoonRegisterDTO;
import com.example.genie_tune_java.domain.webtoon.dto.page.WebtoonPageRequestDTO;
import com.example.genie_tune_java.domain.webtoon.dto.page.WebtoonPageResponseDTO;
import com.example.genie_tune_java.domain.webtoon.entity.Webtoon;
import com.example.genie_tune_java.domain.webtoon.mapper.WebtoonMapper;
import com.example.genie_tune_java.domain.webtoon.repository.WebtoonRepository;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class WebtoonServiceImpl implements WebtoonService {

  private final WebtoonRepository webtoonRepository;
  private final WebtoonMapper webtoonMapper;
  private final ServiceAccessRepository serviceAccessRepository;
  private final AttachService attachService;
  private final AttachRepository attachRepository;

  @Override
  @Transactional
  public Webtoon register(WebtoonRegisterDTO dto) {
    Webtoon webtoon = webtoonMapper.toEntityForRegister(dto);
    return webtoonRepository.save(webtoon);
  }

  @Override
  @Transactional
  public List<WebtoonGroupResponseDTO> getWebtoonGallery(ServiceAccess serviceAccess) {

    //1. Service Access Id -> Prompt Id -> Webtoon Id 연결연결해서 한 serviceAccessId가 만든 webtoonList 전부 가져옴
    List<Webtoon> webtoonList = webtoonRepository.findAllByServiceAccessId(serviceAccess.getId());

    //2. Attach에서 가져오기 위한 webtoonIds 모아놓은 list
    List<Long> webtoonIds = webtoonList.stream().map(Webtoon::getId).toList();

    //3. Attach 관련 정보를 미리 Map형태로 저장시키기
    // MAP<WEBTOON_ID, S3Key> 형태로 미리 저장
    Map<Long, String> attachMap = attachRepository.findByAttachTargetTypeInAndTargetIdIn(List.of(AttachTargetType.WEBTOON), webtoonIds).stream()
            .collect(Collectors.toMap(
                    Attach::getTargetId,
                    Attach::getS3Key
            ));

    //4. webtoonGroupId(UUID) 기준으로 그룹화
    Map<String, List<Webtoon>> groupedWebtoon = webtoonList.stream()
            .collect(Collectors.groupingBy(
                    Webtoon::getWebtoonGroupId, // key 값 -> 개별 Webtoon 객체의 필드 중에서 어떤 값을 기준으로 Grouping을 할 것인가?
                    LinkedHashMap::new, // GroupingBy의 본질은 key 값을 기분으로 분류하는 것이다. 그래서 Map의 형태가 되어야만 한다. 데이터가 들어온 순서를 기억하기 위해 -> Linked
                    Collectors.toList() // Map안에 들어갈 값은 어떠한 형태인가?
            ));
    //5. 만든 Map을 -> entrySet으로 바꿔서 -> 개별 맵핑 -> WebToonGroupResponseDTO를 최종적으로 반환하는 것이 목표
    return groupedWebtoon.entrySet().stream()
            .map((Map.Entry<String, List<Webtoon>> entry) -> {

              String groupId = entry.getKey();        // WebtoonGroupId(UUID)
              List<Webtoon> cuts = entry.getValue();  // 웹툰 컷들 리스트

              // 4-1. 내부 레코드(WebtoonCutDTO) 리스트 만들기
              List<WebtoonGroupResponseDTO.WebtoonCutDTO> cutDTOs = cuts.stream()
                      .map((Webtoon w) -> {
                        // attachService를 사용하여 실제 S3 URL 빌드
                        String url = attachService.buildFileUrl(attachMap.get(w.getId()));
                        return new WebtoonGroupResponseDTO.WebtoonCutDTO(url);
                      })
                      .toList();

              // 4-2. 최종 상위 레코드 생성
              return new WebtoonGroupResponseDTO(
                      groupId,
                      cuts.get(0).getTitle(), // 그룹의 첫 번째 컷 제목을 대표 제목으로 사용
                      cutDTOs
              );
            })
            .toList(); // 최종적으로 List<WebtoonGroupResponseDTO>가 됨
  }
  @Override
  @Transactional(readOnly = true)
  public WebtoonPageResponseDTO getWebtoonGalleryPage(int page, int size, List<WebtoonGroupResponseDTO> content) {
    // 1. 페이지 인덱스 보정 (1-based -> 0-based)
    int pageIndex = Math.max(0, page - 1);
    //2. Pageable 객체
    Pageable pageable = PageRequest.of(pageIndex, size);

    // 3. subList를 위한 start, end 계산 (이게 있어야 진짜 페이징이 됨)
    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), content.size());

    // 3. 인덱스 범위 방어 처리 후 자르기
    List<WebtoonGroupResponseDTO> pagedList = (start >= content.size())
            ? List.of()
            : content.subList(start, end);

    // 4. PageImpl 객체에 담기 (여기서 totalPages 등이 자동 계산됨)
    Page<WebtoonGroupResponseDTO> pageResult = new PageImpl<>(pagedList, pageable, content.size());

    // 5. 질문자님이 원하시는 DTO 형식으로 반환
    return new WebtoonPageResponseDTO(
            pageResult.getContent(),          // 페이징된 데이터
            pageResult.getTotalPages(),       // 전체 페이지 수
            pageResult.getTotalElements(),    // 전체 데이터 개수
            pageResult.getNumber() + 1,       // 현재 페이지 (1부터 시작하도록)
            pageResult.isFirst(),             // 첫 페이지 여부
            pageResult.isLast()               // 마지막 페이지 여부
    );
  }


}
