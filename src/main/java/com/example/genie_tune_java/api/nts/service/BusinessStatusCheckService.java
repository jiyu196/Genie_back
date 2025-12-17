package com.example.genie_tune_java.api.nts.service;

import com.example.genie_tune_java.api.nts.NTSBusinessAPIClient;
import com.example.genie_tune_java.api.nts.dto.BusinessStatusDataDTO;
import com.example.genie_tune_java.api.nts.dto.BusinessStatusResponseDTO;
import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.member.entity.RegisterStatus;
import com.example.genie_tune_java.domain.register_request.dto.RegisterRequestCheckDTO;
import com.example.genie_tune_java.domain.register_request.dto.RegisterRequestDTO;
import com.example.genie_tune_java.domain.register_request.entity.RegisterRequestStatus;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class BusinessStatusCheckService {
  private final NTSBusinessAPIClient ntsClient;

  public RegisterRequestDTO checkBusinessStatus(RegisterRequestCheckDTO dto) {
    //1. 사업자 등록번호 꺼내기
    String bizNumber = dto.bizNumber();
    //2. 외부에서 받은 dto의 필드를 꺼내서 API 호출용 DTO 생성 및 결과 값 반환
    BusinessStatusResponseDTO responseDTO = ntsClient.checkStatus(bizNumber);

    //3.

    //3. 추가로 필요한 값 꺼내기 & null Check
    BusinessStatusDataDTO data = responseDTO.data().get(0);

    if(!StringUtils.hasText(data.getBStt()) || !StringUtils.hasText(data.getBSttCd())) {
      throw new GlobalException(ErrorCode.BUSINESS_NUMBER_NOT_FOUND);
    }

    RegisterRequestStatus status = "01".equals(data.getBSttCd()) ? RegisterRequestStatus.PENDING : RegisterRequestStatus.REJECTED;

    //4. API 반환 값을 Entity 등록용 RegisterRequestDTO로 반환

    return new RegisterRequestDTO(bizNumber, data.getBStt(), data.getBSttCd(), status);
  }

}
