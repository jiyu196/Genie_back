package com.example.genie_tune_java.domain.word_rule.mapper;

import com.example.genie_tune_java.domain.word_rule.dto.GetWordRulesResponseDTO;
import com.example.genie_tune_java.domain.word_rule.entity.WordRule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WordRuleMapper {

  GetWordRulesResponseDTO toListDTO(WordRule wordRule);
}
