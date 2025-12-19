package com.example.genie_tune_java.api.nts.dto.validation;

import com.example.genie_tune_java.domain.member.dto.register.biz_check.BusinessValidationCheckRequestDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class BusinessValidationRequestDTO {

  @JsonProperty("businesses")
  private final List<BusinessValidationInputData> businessInputData;

  public BusinessValidationRequestDTO(BusinessValidationCheckRequestDTO dto) {
    this.businessInputData = List.of(
            new BusinessValidationInputData(
                  dto.bizNumber(), dto.representativeName(), dto.openingDate(),
                    "", "", "", "", "", "")

    );
  }
}
