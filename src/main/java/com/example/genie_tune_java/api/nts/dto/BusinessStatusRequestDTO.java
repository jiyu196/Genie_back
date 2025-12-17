package com.example.genie_tune_java.api.nts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class BusinessStatusRequestDTO {
  @JsonProperty("b_no")
  private final List<String> bNo;

  public BusinessStatusRequestDTO(String bizNumber) {
    this.bNo = List.of(bizNumber);
  }
}
