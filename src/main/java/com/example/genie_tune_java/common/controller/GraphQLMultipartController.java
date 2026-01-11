package com.example.genie_tune_java.common.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.graphql.ExecutionGraphQlResponse;
import org.springframework.graphql.ExecutionGraphQlService;
import org.springframework.graphql.support.DefaultExecutionGraphQlRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Log4j2
public class GraphQLMultipartController {

  private final ExecutionGraphQlService graphQlService;
  private final ObjectMapper objectMapper;

  public GraphQLMultipartController(ExecutionGraphQlService graphQlService, ObjectMapper objectMapper) {
    this.graphQlService = graphQlService;
    this.objectMapper = objectMapper;
  }

  @PostMapping(value = "/graphql-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Map<String, Object> handleMultipart(MultipartHttpServletRequest request) throws Exception {

    log.info("========== Multipart GraphQL 요청 시작 ==========");

    // operations 파싱
    String operationsJson = request.getParameter("operations");
    log.info("Operations JSON: {}", operationsJson);

    Map<String, Object> operations = objectMapper.readValue(operationsJson, Map.class);
    log.info("Operations 파싱 완료: {}", operations);

    // map 파싱
    String mapJson = request.getParameter("map");
    log.info("Map JSON: {}", mapJson);

    Map<String, List<String>> fileMap = objectMapper.readValue(mapJson, Map.class);
    log.info("File Map 파싱 완료: {}", fileMap);

    // variables 가져오기
    Map<String, Object> variables = (Map<String, Object>) operations.get("variables");
    if (variables == null) {
      variables = new HashMap<>();
      log.warn("Variables가 null이어서 새로 생성");
    }
    log.info("초기 Variables: {}", variables);

    // 파일들을 variables에 매핑
    log.info("파일 매핑 시작...");
    for (Map.Entry<String, List<String>> entry : fileMap.entrySet()) {
      String fileKey = entry.getKey();
      List<String> paths = entry.getValue();
      log.info("파일 키: {}, 경로: {}", fileKey, paths);

      MultipartFile file = request.getFile(fileKey);
      if (file != null) {
        log.info("파일 발견: {}, 크기: {}, 타입: {}",
                file.getOriginalFilename(), file.getSize(), file.getContentType());

        for (String path : paths) {
          String cleanPath = path.replace("variables.", "");
          log.info("파일을 {} 경로에 설정 중...", cleanPath);
          setNestedValue(variables, cleanPath, file);
        }
      } else {
        log.warn("파일 키 {}에 대한 파일을 찾을 수 없음", fileKey);
      }
    }

    log.info("파일 매핑 후 Variables: {}", variables);

    // GraphQL 실행
    String query = (String) operations.get("query");
    String operationName = (String) operations.get("operationName");

    log.info("GraphQL 쿼리 실행 준비");
    log.info("Query: {}", query);
    log.info("Operation Name: {}", operationName);

    DefaultExecutionGraphQlRequest graphQlRequest = new DefaultExecutionGraphQlRequest(
            query,
            operationName,
            variables,
            null,
            UUID.randomUUID().toString(),
            null
    );

    log.info("GraphQL 실행 중...");
    ExecutionGraphQlResponse response = graphQlService.execute(graphQlRequest).block();
    Object responseData = response.getData();
    log.info("GraphQL 실행 완료");
    log.info("Response Data: {}", responseData);
    log.info("Response Errors: {}", response.getErrors());

    Map<String, Object> result = new HashMap<>();
    result.put("data", responseData);
    result.put("errors", response.getErrors());

    log.info("========== Multipart GraphQL 요청 종료 ==========");

    return result;
  }

  private void setNestedValue(Map<String, Object> map, String path, Object value) {
    log.debug("setNestedValue 시작 - path: {}, value type: {}", path, value.getClass().getSimpleName());

    String[] keys = path.split("\\.");
    Object current = map;

    for (int i = 0; i < keys.length - 1; i++) {
      String key = keys[i];
      log.debug("중첩 키 처리: {}", key);

      if (current instanceof Map) {
        Map<String, Object> currentMap = (Map<String, Object>) current;
        current = currentMap.computeIfAbsent(key, k -> {
          log.debug("새로운 Map 생성: {}", k);
          return new HashMap<String, Object>();
        });
      }
    }

    if (current instanceof Map) {
      Map<String, Object> currentMap = (Map<String, Object>) current;
      String finalKey = keys[keys.length - 1];
      log.debug("최종 키에 값 설정: {}", finalKey);
      currentMap.put(finalKey, value);
    }
  }
}

