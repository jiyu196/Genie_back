package com.example.genie_tune_java.security.util;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@auth.isActive(principal) and @auth.isApproved(principal) and @auth.isNormalUser(principal)")
public @interface IsMemberUser {
}
