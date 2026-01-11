package com.example.genie_tune_java.api.nts.service;

import com.example.genie_tune_java.api.nts.NTSBusinessAPIClient;
import com.example.genie_tune_java.api.nts.dto.status.BusinessStatusDataDTO;
import com.example.genie_tune_java.api.nts.dto.status.BusinessStatusResponseDTO;
import com.example.genie_tune_java.api.nts.dto.validation.BusinessValidationOutputData;
import com.example.genie_tune_java.api.nts.dto.validation.BusinessValidationResponseDTO;
import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.member.dto.register.biz_check.BusinessValidationCheckRequestDTO;
import com.example.genie_tune_java.domain.member.dto.register.biz_check.BusinessValidationCheckResponseDTO;
import com.example.genie_tune_java.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class BusinessNumberCheckService {
  private final NTSBusinessAPIClient ntsClient;
  private final MemberRepository memberRepository;

  public BusinessValidationCheckResponseDTO checkBusinessValidation(BusinessValidationCheckRequestDTO dto) {

    //0. 중복 체크 기존 사업자 등록번호 조회 불가
    if(memberRepository.existsByBizNumber(dto.bizNumber())) {
      throw new GlobalException(ErrorCode.BIZ_NUMBER_DUPLICATED);
    }

    //1.api 호출 및 결과 값 반환
    BusinessValidationResponseDTO bvrDto = ntsClient.checkValidation(dto);

    checkDataValidation(bvrDto.statusCode());

    //2. API 응답 DTO -> 필요한 정보인 data(리스트 타입) 필드 접근
    List<BusinessValidationOutputData> listData = bvrDto.data();
    //3. 리스트 형태의 data 내 필드 접근 (반환은 list 길이 1개짜리)
    BusinessValidationOutputData realData= listData.get(0);

    boolean validation = realData.validationCode().equals("01");

    return new BusinessValidationCheckResponseDTO(realData.bizNumber(), validation, realData.resultMessage(), realData.status().getBStt(), realData.status().getBSttCd());

  }

  private void checkDataValidation(String statusCode) {
    switch (statusCode) {
      case "OK" -> {log.info("통과!");} // OK면 그냥 통과
      case "BAD_JSON_REQUEST" ->
              throw new GlobalException(ErrorCode.BAD_JSON_REQUEST);

      case "REQUEST_DATA_MALFORMED" ->
              throw new GlobalException(ErrorCode.REQUEST_DATA_MALFORMED);

      case "TOO_LARGE_REQUEST" ->
              throw new GlobalException(ErrorCode.TOO_LARGE_REQUEST);

      case "INTERNAL_ERROR" ->
              throw new GlobalException(ErrorCode.INTERNAL_ERROR);

      default -> {
        log.info(statusCode); throw new GlobalException(ErrorCode.HTTP_ERROR);
      }

    }
  }

}
