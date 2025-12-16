package com.example.genie_tune_java.api.nts.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class BusinessStatusRequestDTO {
  private final List<String> b_no;

  public BusinessStatusRequestDTO(String bizNumber) {
    this.b_no = List.of(bizNumber);
  }
}
