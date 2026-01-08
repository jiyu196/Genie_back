package com.example.genie_tune_java.security.util;

import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
@Log4j2
public class AESUtil {
  // ServiceAccessKey를 암호화해서 저장하여 MyPage에서 복호화 해서 보여주는 역할을 위해 별도로 DB에 저장하기 위해 설정하는 값

  private final IvParameterSpec ivSpec;
  private final SecretKeySpec secretKey;

  public AESUtil(@Value("${security.aes.iv}") String iv, @Value("${security.aes.secret}") String secret) {
    log.info("주입된 iv: {}", iv);
    log.info("iv의 길이: {}",iv.length());
    this.ivSpec = createIvParameterSpec(iv);
    this.secretKey = createKeySpec(secret); //
  }

  //SecretKey 길이 상관없이 32바이트로 만들어주면서 이를 토대로 KeySpec까지 생성
  private static SecretKeySpec createKeySpec(String secretKey) {
    try {
      MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
      byte[] keyBytes = sha256.digest(secretKey.getBytes(StandardCharsets.UTF_8));

      return new SecretKeySpec(keyBytes, "AES"); // 정확히 32 bytes

    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("AES 키 생성 실패", e);
    }
  }

  private static IvParameterSpec createIvParameterSpec(String iv) {
    return new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
  }

  private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

  public String encrypt(String plainText) {
    try {
      Cipher cipher = Cipher.getInstance(ALGORITHM);

      cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
      byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

      return Base64.getEncoder().encodeToString(encrypted);
    } catch (Exception e) {
      throw new GlobalException(ErrorCode.SERVICE_ACCESS_KEY_ENCRYPT_FAILED); // 별도 에러코드 만들기
    }
  }

  public String decrypt(String cipherText) {
    try {
      Cipher cipher = Cipher.getInstance(ALGORITHM);

      cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
      byte[] decoded = Base64.getDecoder().decode(cipherText);

      return new String(cipher.doFinal(decoded), StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new GlobalException(ErrorCode.SERVICE_ACCESS_KEY_DECRYPT_FAILED);
    }
  }

}
