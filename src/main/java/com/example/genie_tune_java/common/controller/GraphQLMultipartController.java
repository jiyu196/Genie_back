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

    // 1. operations 파싱 ex) operationsJson = '{"query": "mutation...", "variables": {"file": null}}'
    String operationsJson = request.getParameter("operations");
    log.info("Operations JSON: {}", operationsJson);

    // 2. 프론트에서 보내준 JSON 문자열을 Map 형태로 파싱한다.
//     operations = {
//       "query": "mutation($file: Upload!) { uploadFile(file: $file) {...} }",
//       "variables": {"file": null},
//      "operationName": "UploadFile"
//     }
    Map<String, Object> operations = objectMapper.readValue(operationsJson, Map.class);
    log.info("Operations 파싱 완료: {}", operations);

    //3. Map 형태에서 key 값을 map으로 저장해놓은 파일 뭉치들을(JSON 형태) 꺼낸다. ex) '{"0": ["variables.file"]}'
    String mapJson = request.getParameter("map");
    log.info("Map JSON: {}", mapJson);

    //4. JSON 형태의 파일 뭉치를 실제 꺼낼 수 있는 Map 형태로 변환한다. fileMap = {"0": ["variables.file"] }
    Map<String, List<String>> fileMap = objectMapper.readValue(mapJson, Map.class);
    log.info("File Map 파싱 완료: {}", fileMap);

    // 5. variables 가져오기 ex) variables = {"file": null} (프론트에서 RequestDTO에 넣어주는 값)
    Map<String, Object> variables = (Map<String, Object>) operations.get("variables");
    if (variables == null) {
      variables = new HashMap<>();
      log.warn("Variables가 null이어서 새로 생성");
    }
    log.info("초기 Variables: {}", variables);

    // 6. map 으로 들어온 파일 뭉치들을 variables에 매핑 (appoloclient에서 formData를 처리 못해서 백엔드에서 맵핑)
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

    // 7. 원래 요청이었던 GraphQL 실행
    String query = (String) operations.get("query");
    String operationName = (String) operations.get("operationName");

    log.info("GraphQL 쿼리 실행 준비");
    log.info("Query: {}", query);
    log.info("Operation Name: {}", operationName);

    //8. 파일이 담겨져있는 객체로 GraphQL 요청 직접 생성
    DefaultExecutionGraphQlRequest graphQlRequest = new DefaultExecutionGraphQlRequest(
            query,
            operationName,
            variables,
            null,
            UUID.randomUUID().toString(),
            null
    );

    log.info("GraphQL 실행 중...");

    //9. 그래프 QL 직접 실행 (동기 방식으로 block 사용하는 듯)
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
  // map -> variables, path -> 파일 경로, value -> 실제 파일 value
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

