package com.example.genie_tune_java.common.config;

import com.example.genie_tune_java.api.port_one.service.PortOneAuthService;
import com.example.genie_tune_java.domain.word_rule.dto.GetWordRulesResponseDTO;
import com.example.genie_tune_java.domain.word_rule.service.WordRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
@RequiredArgsConstructor
public class InitRunner {
  private final PortOneAuthService portOneAuthService;
  private final WordRuleService wordRuleService;
  private final WordRuleCache wordRuleCache;

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationReady() {
    log.info("Spring이 처음 시작될 때 사전에 실행하는 메서드 모음");

    try{
      // 1. 결제 토큰 사전 발급
      String token = portOneAuthService.getAccessToken();
      log.info("결제 Access Token 발급 {}", token);

      //2. 사전 db 금칙어 목록 모음
      List<GetWordRulesResponseDTO> wordList = wordRuleService.getWordRules();

      // 캐싱 저장
      wordRuleCache.load(wordList);

      //앞에 10개만 확인해보자
      wordList.stream().limit(10).forEach(log::info);

    } catch (Exception e){
      log.error("==== 시스템 초기 시작 메서드 오류 ==== ", e);
    }
  }
}
