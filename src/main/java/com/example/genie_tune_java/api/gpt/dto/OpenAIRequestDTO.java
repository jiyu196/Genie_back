package com.example.genie_tune_java.api.gpt.dto;

import java.util.List;

public record OpenAIRequestDTO(
  String model, List<Message> messages
) {
  // 이 중첩 record의 코드는 이 Message는 RequestDTO에만 사용 된다고 명시하는 느낌 (분리해도 상관 x, 다만 유지보수할 때 파일 여러개 봐야해서 헷갈릴 수 있음)
  public record Message(String role, String content) {}
  // 팩토리 메서드로 볼 수 있다. 이 객체를 생성할 때 new 쭉쭉 길게 만드는 걸 팩토리 메서드 하나로 대행하고 밖에서는 OpenAIRequestDTO.of(args1, args2, args3) 으로 호출하면 된다.
  public static OpenAIRequestDTO of(String model, String roel, String content) {
    return new OpenAIRequestDTO(model, List.of(new Message(roel, content)));
  }
}
