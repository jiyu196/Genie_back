package com.example.genie_tune_java.domain.prompt.service;

import com.example.genie_tune_java.api.gpt.dto.OpenAIRequestDTO;
import com.example.genie_tune_java.api.gpt.dto.OpenAIResponseDTO;
import com.example.genie_tune_java.common.config.WordRuleCache;
import com.example.genie_tune_java.common.dto.ServiceAccessIdPrincipal;
import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.prompt.dto.GenerateStoryRequestDTO;
import com.example.genie_tune_java.domain.prompt.dto.WordFilterResult;
import com.example.genie_tune_java.domain.prompt.dto.register.PreRegisterPromptDTO;
import com.example.genie_tune_java.domain.prompt.dto.register.RegisterPromptLogDTO;
import com.example.genie_tune_java.domain.prompt.entity.Prompt;
import com.example.genie_tune_java.domain.prompt.entity.PromptLog;
import com.example.genie_tune_java.domain.prompt.entity.PromptStatus;
import com.example.genie_tune_java.domain.prompt.entity.SourceType;
import com.example.genie_tune_java.domain.prompt.mapper.PromptLogMapper;
import com.example.genie_tune_java.domain.prompt.mapper.PromptMapper;
import com.example.genie_tune_java.domain.prompt.repository.PromptLogRepository;
import com.example.genie_tune_java.domain.prompt.repository.PromptRepository;
import com.example.genie_tune_java.domain.service_access.entity.ServiceAccess;
import com.example.genie_tune_java.domain.service_access.repository.ServiceAccessRepository;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Log4j2
public class PromptServiceImpl implements PromptService {
  private final PromptRepository promptRepository;
  private final ServiceAccessRepository serviceAccessRepository;
  private final PromptLogRepository promptLogRepository;
  private final PromptMapper promptMapper;
  private final PromptLogMapper promptLogMapper;
  private final WordRuleCache wordRuleCache;

  @Transactional
  public OpenAIRequestDTO checkWord (GenerateStoryRequestDTO dto, DataFetchingEnvironment env) {

    ServiceAccessIdPrincipal principal = env.getGraphQlContext().get(ServiceAccessIdPrincipal.class);

    if(principal == null) {
      throw new GlobalException(ErrorCode.GRAPHQL_INTERCEPTOR_ERROR);
    }

    String accessId = principal.getAccessId();
    //1. 받아온 문장 정보를 개별 list로 저장
    List<String> characterDescription = dto.accessIdCharacter();
    List<String> originalContentList = dto.originalContent();

    //2. cashing 시켜놓은 Map<String, GetWordRuleResponseDTO> 에 있는 Key값과 비교하여 대체 시켜버림 cashing method 확인
    // WordFilterResult는 -> forbiddenWord, cleanWord, isSlang, reason 4개 필드 존재
    List<WordFilterResult> characterFilterResults = characterDescription.stream().map(wordRuleCache::filterWord).toList();
    List<WordFilterResult> originalFilterResult = originalContentList.stream().map(wordRuleCache::filterWord).toList();

    //3. prompt 일부 저장(별도 DTO 생성) // 저 아래의 boolean 중 하나라도 true면 전체 true로 간주 (prompt 저장시)
    boolean isSlangCharacter = hasSlangWord(characterFilterResults);
    boolean isSlangContent = hasSlangWord(originalFilterResult);

    log.info("캐릭터 묘사에 slang 여부 : {} 원본 content 내에 slang 여부 : {}", isSlangCharacter, isSlangContent);

    //4. 문자열 리스트 -> 하나의 문장 결합
    String character = characterFilterResults.stream().map(WordFilterResult::resolveWord).collect(Collectors.joining(" "));
    String original = originalFilterResult.stream().map(WordFilterResult::resolveWord).collect(Collectors.joining(" "));
    log.info("캐릭터 묘사: {}", character);
    log.info("원본 프롬프트: {}", original);

    //5. Prompt 먼저 저장 및 저장한 Prompt 반환
    ServiceAccess serviceAccess = serviceAccessRepository.findByAccessId(accessId).orElseThrow(() -> new GlobalException(ErrorCode.SERVICE_ACCESS_NOTFOUND));
    PreRegisterPromptDTO promptDTO = new PreRegisterPromptDTO(serviceAccess, original, PromptStatus.WAITING, isSlangCharacter);
    Prompt prompt = promptRepository.save(promptMapper.toEntityForRegister(promptDTO));

    //5-1. Character Result를 전부 담은 List 생성
    List<PromptLog> characterLogs = characterFilterResults.stream().map(result -> {
      RegisterPromptLogDTO logDTO = new RegisterPromptLogDTO(
        prompt, result.reason(), null, result.originalWord(), result.filteredWord(), SourceType.CHARACTER_DESCRIPTION);
      return promptLogMapper.toEntityForRegister(logDTO);
    }).toList();
    //5-2. original Result를 전부 담은 List 생성
    List<PromptLog> originalLogs = originalFilterResult.stream().map(result -> {
      RegisterPromptLogDTO logDTO = new RegisterPromptLogDTO(
              prompt, result.reason(), null, result.originalWord(), result.filteredWord(), SourceType.ORIGINAL_CONTENT);
      return promptLogMapper.toEntityForRegister(logDTO);
    }).toList();

    //6. prompt 기반 prompt Log 저장 (단어 단위로 기록, 별도 DTO) SLANG 여부와 상관없이 저장 신규 array를 만들어 한 번에 저장

    List<PromptLog> allLogs = Stream.concat(originalLogs.stream(), characterLogs.stream()).toList();

    promptLogRepository.saveAll(allLogs);

    //7. OpenAIRequestDTO 객체 생성 Python에 보낼 때는, SlangContent가 있었는지 여부만 담아서 보냄

    return new OpenAIRequestDTO(accessId, original, isSlangContent, character);
  }

  private boolean hasSlangWord (List<WordFilterResult> wordFilterResults) {
    return wordFilterResults.stream().anyMatch(WordFilterResult::isSlang);
  }

  @Transactional
  public Prompt finalSave(OpenAIResponseDTO dto) {
    Prompt savedPrompt = promptRepository.findByOriginalContentAndServiceAccess(dto.originalContent(), dto.accessId()).orElseThrow(() -> new GlobalException(ErrorCode.PROMPT_NOT_FOUND));
    savedPrompt.update(dto.filteredContent(), dto.refinedContent(), dto.revisedPrompt(), PromptStatus.APPROVED);
    return savedPrompt;
  }

}
