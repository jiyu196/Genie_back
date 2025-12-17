package com.example.genie_tune_java.api.nts;

import com.example.genie_tune_java.api.nts.dto.BusinessStatusResponseDTO;
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

  @Test
  void testGetNTSClient() {
    Assertions.assertNotNull(client);
    log.info(client);
  }

  @Test
  void testGetNTSBusinessAPIClient() {
    BusinessStatusResponseDTO responseDTO = client.checkStatus("4008266479");
    Assertions.assertNotNull(responseDTO);
    log.info(responseDTO.statusCode());
    log.info(responseDTO.matchCnt());
    log.info(responseDTO.requestCnt());
    responseDTO.data().forEach(log::info);
  }
}
