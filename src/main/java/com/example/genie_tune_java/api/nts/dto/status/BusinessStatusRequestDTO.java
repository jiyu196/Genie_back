package com.example.genie_tune_java.api.nts.dto.status;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
@Getter
public class BusinessStatusRequestDTO {
  @JsonProperty("b_no")
  private final List<String> bNo;

  public BusinessStatusRequestDTO(String bizNumber) {
    this.bNo = List.of(bizNumber);
  }
}
