package com.example.genie_tune_java.common.exception;

import lombok.Getter;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  // ==== 이메일 인증 관련 ====
  CODE_EXPIRED(HttpStatus.BAD_REQUEST, "인증 코드가 만료되었습니다. 다시 요청해주세요."),
  CODE_INVALID(HttpStatus.BAD_REQUEST, "인증 코드가 올바르지 않습니다."),
  EMAIL_DUPLICATED(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
  BIZ_NUMBER_DUPLICATED(HttpStatus.CONFLICT, "이미 사용 중인 사업자등록번호입니다."),
  ORGANIZATION_NAME_DUPLICATED(HttpStatus.CONFLICT, "이미 가입하신 단체입니다."),

  // ==== JWT 관련 ====
  TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT 토큰이 만료되었습니다."),
  TOKEN_MALFORMED(HttpStatus.UNAUTHORIZED, "JWT 토큰이 손상되었습니다."),
  TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),
  TOKEN_UNKNOWN(HttpStatus.UNAUTHORIZED, "지원하지 않는 JWT 형식입니다."),

  // ==== 공통 ====
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
  FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
  REQUIRED_FIELD_MISSING(HttpStatus.BAD_REQUEST, "필수 데이터가 누락되었습니다."),

  // ==== 회원관리 ====
  MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
  MEMBER_PASSWORD_INVALID(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
  MEMBER_DISABLED(HttpStatus.LOCKED, "잠긴 회원입니다. 관리자에게 문의하세요."),
  MEMBER_DELETED(HttpStatus.FORBIDDEN, "삭제된 회원입니다."),
  TERMS_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 약관입니다."),

  // ==== 사업자 API 조회 관련 ====
  BUSINESS_NUMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사업자 상태정보가 유효하지 않습니다."),
  BAD_JSON_REQUEST(HttpStatus.BAD_REQUEST, "값을 잘못 입력하셨습니다."),
  REQUEST_DATA_MALFORMED(HttpStatus.LENGTH_REQUIRED, "필수 입력값이 누락되었습니다."),
  TOO_LARGE_REQUEST(HttpStatus.PAYLOAD_TOO_LARGE, "요청 사업자 번호가 100개 이상입니다."),
  INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러입니다."),
  HTTP_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "국세청 서버 에러입니다. 잠시 후에 다시 시도하여주시기 바랍니다.");
  private final HttpStatus status;
  private final String message;

  ErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
