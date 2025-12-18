package com.example.genie_tune_java.api.nts;

import com.example.genie_tune_java.api.nts.dto.status.BusinessStatusResponseDTO;
import com.example.genie_tune_java.api.nts.dto.validation.BusinessValidationResponseDTO;
import com.example.genie_tune_java.api.nts.service.BusinessNumberCheckService;
import com.example.genie_tune_java.domain.member.dto.BusinessValidationCheckRequestDTO;
import com.example.genie_tune_java.domain.member.dto.BusinessValidationCheckResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class NTSClientTest {
  @Autowired
  private NTSBusinessAPIClient client;
  @Autowired
  private BusinessNumberCheckService service;
  @Test
  void testGetNTSClient() {
    Assertions.assertNotNull(client);
    log.info(client);
  }

  @Test
  void testGetNTSBusinessStatusAPIClient() {
    BusinessStatusResponseDTO responseDTO = client.checkStatus("4008266479");
    Assertions.assertNotNull(responseDTO);
    log.info(responseDTO.statusCode());
    log.info(responseDTO.matchCnt());
    log.info(responseDTO.requestCnt());
    responseDTO.data().forEach(log::info);
  }

  @Test
  void testGetNTSBusinessValidationAPIClient() {
    BusinessValidationCheckResponseDTO dto = service.checkBusinessValidation(new BusinessValidationCheckRequestDTO("7580402880", "정사랑","20230426"));
    Assertions.assertNotNull(dto);
    log.info(dto);
  }
}
