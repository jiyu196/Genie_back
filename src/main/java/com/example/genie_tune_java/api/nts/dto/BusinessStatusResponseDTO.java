package com.example.genie_tune_java.api.nts.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class BusinessStatusResponseDTO {
  private String status_code;
  private int match_cnt;
  private int request_cnt;
  private List<BusinessStatusDataDTO> data;
}
