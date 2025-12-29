package com.example.genie_tune_java.terms;

import com.example.genie_tune_java.domain.terms.dto.GetTermsRequestDTO;
import com.example.genie_tune_java.domain.terms.dto.GetTermsResponseDTO;
import com.example.genie_tune_java.domain.terms.service.TermsService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class TermsTest {
  @Autowired
  private TermsService termsService;

  @Test
  public void getTerm() {
    GetTermsResponseDTO dto = termsService.getTerm(new GetTermsRequestDTO("SERVICE"));
    Assertions.assertNotNull(dto);
    log.info(dto.toString());
  }
}
