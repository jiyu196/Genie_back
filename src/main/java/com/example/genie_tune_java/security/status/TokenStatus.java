package com.example.genie_tune_java.security.status;

import lombok.Getter;

@Getter
public enum TokenStatus {
  VALID, EXPIRED, MALFORMED, INVALID, UNSUPPORTED;
}
