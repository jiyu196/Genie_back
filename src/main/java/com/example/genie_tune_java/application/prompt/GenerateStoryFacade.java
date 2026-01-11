package com.example.genie_tune_java.application.prompt;

import com.example.genie_tune_java.api.gpt.dto.DownloadImageDTO;
import com.example.genie_tune_java.api.gpt.dto.OpenAIRequestDTO;
import com.example.genie_tune_java.api.gpt.dto.OpenAIResponseDTO;
import com.example.genie_tune_java.api.gpt.service.DownloadImageService;
import com.example.genie_tune_java.api.gpt.service.OpenAIService;
import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.attach.dto.AttachRequestDTO;
import com.example.genie_tune_java.domain.attach.dto.AttachResponseDTO;
import com.example.genie_tune_java.domain.attach.entity.AttachTargetType;
import com.example.genie_tune_java.domain.attach.service.AttachService;
import com.example.genie_tune_java.domain.prompt.dto.GenerateStoryRequestDTO;
import com.example.genie_tune_java.domain.prompt.dto.GenerateStoryResponseDTO;
import com.example.genie_tune_java.domain.prompt.entity.Prompt;
import com.example.genie_tune_java.domain.prompt.service.PromptService;
import com.example.genie_tune_java.domain.webtoon.dto.WebtoonRegisterDTO;
import com.example.genie_tune_java.domain.webtoon.entity.Webtoon;
import com.example.genie_tune_java.domain.webtoon.entity.WebtoonStatus;
import com.example.genie_tune_java.domain.webtoon.service.WebtoonService;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Log4j2
public class GenerateStoryFacade {

  private final PromptService promptService;
  private final OpenAIService openAIService;
  private final DownloadImageService downloadImageService;
  private final AttachService attachService;
  private final WebtoonService webtoonService;

  @Transactional
  public GenerateStoryResponseDTO generateWebtoon(GenerateStoryRequestDTO dto, DataFetchingEnvironment env) {
    //1. 파이썬에 집어넣을 DTO 생성
    OpenAIRequestDTO openAIRequestDTO = promptService.checkWord(dto, env);
    //2. 파이썬에서 보내준 결과값
    OpenAIResponseDTO openAIResponseDTO = openAIService.requestPythonProcessing(openAIRequestDTO);
    // 3. null check (혹시나 imageUrl이 없으면 실패임)
    if(openAIResponseDTO.imageUrl() == null || openAIResponseDTO.imageUrl().isEmpty()) {
      log.error("AI 이미지 생성 실패 : {} ", openAIResponseDTO.errorMessage());
      throw new GlobalException(ErrorCode.IMAGE_GENERATION_FAILED);
    }
    log.info("image URL: {}", openAIResponseDTO.imageUrl());

    // 4. 프롬프트 최종 저장 (엔티티 메서드로 활용)
    Prompt prompt = promptService.finalSave(openAIResponseDTO);
    String title = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 웹툰";
    // 5. WebToon 객체 생성
    Webtoon webtoon = webtoonService.register(new WebtoonRegisterDTO(title, prompt, WebtoonStatus.COMPLETED));
    // 6. webtoon 이미지 파일 가져오기
    DownloadImageDTO imageDTO = downloadImageService.download(openAIResponseDTO.imageUrl());

    // 7. 메서드 호출을 위해 MultipartFile 형태로 변환 WEBTOON 객체의 Pk 주입 -> (현재 1L로 적혀있는 것 수정 필요)
    AttachResponseDTO attachResponseDTO = attachService.uploadGeneratedImage(new AttachRequestDTO(AttachTargetType.WEBTOON, webtoon.getId()), imageDTO);
    log.info("프론트에 내려주는 주소: {}", attachResponseDTO.fileUrl());
    return new GenerateStoryResponseDTO(
            openAIResponseDTO.originalContent(), openAIResponseDTO.refinedContent(), openAIResponseDTO.revisedPrompt(),
            attachResponseDTO.fileUrl(), openAIResponseDTO.errorMessage());
  }
}
