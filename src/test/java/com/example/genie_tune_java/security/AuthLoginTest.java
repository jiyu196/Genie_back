package com.example.genie_tune_java.security;

import com.example.genie_tune_java.security.service.AuthService;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;

@SpringBootTest
@Log4j2
@AutoConfigureGraphQlTester
public class AuthLoginTest {

  @Autowired
  private AuthService authService;
  @Autowired
  private GraphQlTester graphQlTester;

  @Test
  public void envTest() {
    graphQlTester.document("""
      mutation {
        login(input:{
          email:"test@test.com"
          password:"1234"
        }) {
          email
        }
      }
    """).execute()
            .errors().verify();
  }
}
