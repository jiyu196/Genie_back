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
import com.example.genie_tune_java.domain.register_request.dto.RegisterRequestCheckDTO;
import com.example.genie_tune_java.domain.register_request.dto.RegisterRequestDTO;
import com.example.genie_tune_java.domain.register_request.entity.RegisterRequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessNumberCheckService {
  private final NTSBusinessAPIClient ntsClient;

  public RegisterRequestDTO checkBusinessStatus(RegisterRequestCheckDTO dto) {
    //1. 사업자 등록번호 꺼내기
    String bizNumber = dto.bizNumber();
    //2. 외부에서 받은 dto의 필드를 꺼내서 API 호출용 DTO 생성 및 결과 값 반환
    BusinessStatusResponseDTO responseDTO = ntsClient.checkStatus(bizNumber);

    //3. 추가로 필요한 값 꺼내기 & null Check
    BusinessStatusDataDTO data = responseDTO.data().get(0);

    if(!StringUtils.hasText(data.getBStt()) || !StringUtils.hasText(data.getBSttCd())) {
      throw new GlobalException(ErrorCode.BUSINESS_NUMBER_NOT_FOUND);
    }

    RegisterRequestStatus status = "01".equals(data.getBSttCd()) ? RegisterRequestStatus.PENDING : RegisterRequestStatus.REJECTED;

    //4. API 반환 값을 Entity 등록용 RegisterRequestDTO로 반환

    return new RegisterRequestDTO(bizNumber, data.getBStt(), data.getBSttCd(), status);
  }

  public BusinessValidationCheckResponseDTO  checkBusinessValidation(BusinessValidationCheckRequestDTO dto) {

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
      case "OK" -> {} // OK면 그냥 통과
      case "BAD_JSON_REQUEST" ->
              throw new GlobalException(ErrorCode.BAD_JSON_REQUEST);

      case "REQUEST_DATA_MALFORMED" ->
              throw new GlobalException(ErrorCode.REQUEST_DATA_MALFORMED);

      case "TOO_LARGE_REQUEST" ->
              throw new GlobalException(ErrorCode.TOO_LARGE_REQUEST);

      case "INTERNAL_ERROR" ->
              throw new GlobalException(ErrorCode.INTERNAL_ERROR);

      default ->
              throw new GlobalException(ErrorCode.HTTP_ERROR);

    }
  }

}
