package com.example.genie_tune_java.common.exception;

import lombok.Getter;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpMethod;
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

  // ==== Service Access Id 발급 이후 DB 저장 관련
  SERVICE_ACCESS_KEY_ENCRYPT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Service Access Key 발급 과정에서 문제가 생겼습니다."),
  SERVICE_ACCESS_KEY_DECRYPT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Service Access Key 인증 과정에서 문제가 생겼습니다."),

  // ==== 공통 ====
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
  FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
  REQUIRED_FIELD_MISSING(HttpStatus.BAD_REQUEST, "필수 데이터가 누락되었습니다."),

  // ==== 회원관리 ====
  MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
  MEMBER_PASSWORD_INVALID(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
  SAME_OLD_PASSWORD(HttpStatus.FORBIDDEN, "변경 전 비밀번호와 동일합니다."),
  MEMBER_DISABLED(HttpStatus.LOCKED, "잠긴 회원입니다. 관리자에게 문의하세요."),
  MEMBER_DELETED(HttpStatus.FORBIDDEN, "삭제된 회원입니다."),
  TERMS_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 약관입니다."),
  DELETE_MESSAGE_INVALID(HttpStatus.BAD_REQUEST, "삭제요청 메세지가 누락되었습니다."),

  // ==== 사업자 API 조회 관련 ====
  BUSINESS_NUMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사업자 상태정보가 유효하지 않습니다."),
  BAD_JSON_REQUEST(HttpStatus.BAD_REQUEST, "값을 잘못 입력하셨습니다."),
  REQUEST_DATA_MALFORMED(HttpStatus.LENGTH_REQUIRED, "필수 입력값이 누락되었습니다."),
  TOO_LARGE_REQUEST(HttpStatus.PAYLOAD_TOO_LARGE, "요청 사업자 번호가 100개 이상입니다."),
  INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러입니다."),
  HTTP_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "국세청 서버 에러입니다. 잠시 후에 다시 시도하여주시기 바랍니다."),

  // ==== 상품 관련 ====
  PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "선택하신 상품은 존재하지 않습니다."),
  PRODUCT_MAX_PROMPT_DAILY_COUNT_NOT_FOUNT(HttpStatus.NOT_FOUND, "프롬프트 일일 최대 사용량을 찾을 수 없습니다."),

  //==== 주문 관련 ====
  ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문 내역을 찾을 수 없습니다."),
  ORDER_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "주문 권한이 없습니다."),

  //==== 결제 관련 ====
  PAY_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 내역을 찾을 수 없습니다."),
  PORTONE_AUTH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "포트원 결제를 위한 토큰 발급이 실패하였습니다."),
  PAYMENT_PRE_REGISTER_INVALID_REQUEST_ERROR(HttpStatus.BAD_REQUEST, "입력정보가 올바르지 않습니다."),
  PAYMENT_PRE_REGISTER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "포트원 인증정보가 올바르지 않습니다"),
  PAYMENT_PRE_REGISTER_FORBIDDEN_ERROR(HttpStatus.FORBIDDEN, "포트원 결제 요청이 거절되었습니다."),
  PAYMENT_PRE_REGISTER_ALREADY_PAID_ERROR(HttpStatus.CONFLICT, "이미 결제가 완료된 결제입니다."),
  PAYMENT_GET_INVALID_REQUEST_ERROR(HttpStatus.BAD_REQUEST, "요청된 입력 정보가 유효하지 않습니다."),
  PAYMENT_GET_UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED, "인증정보가 올바르지 않습니다."),
  PAYMENT_GET_FORBIDDEN_ERROR(HttpStatus.FORBIDDEN, "요청이 거절되었습니다."),
  PAYMENT_GET_PAYMENT_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "결제 건이 존재하지 않습니다."),

  //==== GraphQLInterceptor ==== 관련
  GRAPHQL_INTERCEPTOR_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GraphQLInterceptor 에서 내부 에러가 터졌습니다."),

  // ==== 결제 수단 관련 ====
  PAYMETHOD_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 수단을 찾을 수 없습니다."),

  //==== Service Access 관련 ====
  SERVICE_ACCESS_REQUIRED(HttpStatus.UNAUTHORIZED , "Service Access ID가 필요합니다. "),
  SERVICE_ACCESS_RELOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "Service Access로 다시 로그인 하여 주시기 바랍니다."),
  SERVICE_ACCESS_NOTFOUND(HttpStatus.BAD_REQUEST, "ServiceAccess ID가 존재하지 않습니다."),
  SERVICE_ACCESS_INVALID(HttpStatus.UNAUTHORIZED, "ServiceAccess key가 일치하지 않습니다."),
  PAYMENT_REQUIRED(HttpStatus.PAYMENT_REQUIRED, "구독이 만료되었습니다. 재 결제가 필요합니다."),
  SERVICE_ACCESS_MALFORMED(HttpStatus.UNAUTHORIZED, "Service Access 검증에 실패하였습니다."),
  SERVICE_ACCESS_NOT_ALLOWED(HttpStatus.UNAUTHORIZED, "Service Access ID가 활성화 상태가 아닙니다."),

  //==== Subscription 관련 ====
  SUBSCRIPTION_EXPIRED(HttpStatus.UNAUTHORIZED, "구독이 만료되었습니다."),
  SUBSCRIPTION_NOTFOUND(HttpStatus.NOT_FOUND, "구독 내역이 없습니다."),

  //==== 파이썬 호출시 발생 에러 ====
  OPENAI_PYTHON_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "웹툰 제작 서버가 불안정합니다. 다시 시도하여 주시기 바랍니다." ),
  IMAGE_GENERATION_FAILED(HttpStatus.FORBIDDEN, "정책에 위배된 프롬프트 입니다."),

  //==== 프롬프트 관련 ====
  PROMPT_NOT_FOUND(HttpStatus.NOT_FOUND, "저장된 프롬프트를 찾을 수 없습니다."),

  //==== 첨부파일, S3 관련 ====
  ATTACH_NOT_FOUND(HttpStatus.NOT_FOUND, "저장된 첨부파일을 찾을 수 없습니다."),
  FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "업로드 된 파일이 없습니다."),
  INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST , "유효하지 않은 파일 확장자 입니다."),
  INVALID_CONTENT_TYPE(HttpStatus.BAD_REQUEST,"허용되지 않은 파일입니다."),
  FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "파일 용량 제한을 초과하였습니다."),
  IMAGE_GET_FAILED(HttpStatus.BAD_REQUEST, "OPEN API에서 image를 가져오는 것을 실패하였습니다."),
  S3_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "첨부파일 업로드에 실패하였습니다."),
  S3_GET_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "첨부파일 가져오기에 실패하였습니다."),

  //==== Daily Usage ====
  DAILY_USAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "현재 accessId의 사용량 정보를 가져올 수 없습니다."),
  DAILY_USAGE_EXCEEDED(HttpStatus.BAD_REQUEST, "일일 토큰 사용량을 초과하였습니다.");
  private final HttpStatus status;
  private final String message;

  ErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
