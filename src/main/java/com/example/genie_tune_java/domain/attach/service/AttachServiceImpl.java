package com.example.genie_tune_java.domain.attach.service;

import com.example.genie_tune_java.api.gpt.dto.DownloadImageDTO;
import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.common.util.S3Util;
import com.example.genie_tune_java.domain.attach.dto.AttachRegisterDTO;
import com.example.genie_tune_java.domain.attach.dto.AttachRequestDTO;
import com.example.genie_tune_java.domain.attach.dto.AttachResponseDTO;
import com.example.genie_tune_java.domain.attach.entity.Attach;
import com.example.genie_tune_java.domain.attach.entity.TargetType;
import com.example.genie_tune_java.domain.attach.mapper.AttachMapper;
import com.example.genie_tune_java.domain.attach.repository.AttachRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class AttachServiceImpl implements AttachService {

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Value("${cloud.aws.region}")
  private String region;

  private final AttachRepository attachRepository;
  private final AttachMapper attachMapper;
  private final S3Util s3Util;
  private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 15MB , 그림 용량 체크하고 조정 필요
  //test시 1.7MB 정도 나옴
  private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp", "pdf");

  // 1. 일반 파일 업로드 방식
  @Override
  public AttachResponseDTO upload(AttachRequestDTO dto, MultipartFile file) {
    // 1. 사용자가 등록한 File이 우리가 정해놓은 규칙에 맞는지 check
    validateFile(file);

    // 2. 원본 파일명
    String originName = file.getOriginalFilename();
    // 3. 파일 확장자 추출
    String extension = extractExtension(file.getOriginalFilename());
    // 4. S3 저장용 파일명 (UUID + 확장자)
    String fileName = UUID.randomUUID() + "." + extension;
    // 5. s3 Key 생성 (일종의 path)
    String key = generateKey(dto.targetType(), dto.targetId(), fileName);

    // 6. S3 업로드
    try {
      // S3Util을 이용해서 실제로 업로드
      s3Util.upload(file.getBytes(), key, file.getContentType());

    } catch (Exception e) {
      log.error("S3 실제 에러 원인: ", e);
      throw new GlobalException(ErrorCode.S3_UPLOAD_FAILED);
    }
    //7. Attach 객체로 저장을 위한, DTO 신규 생성
    AttachRegisterDTO registerDTO = new AttachRegisterDTO(
            dto.targetType(), dto.targetId(), key, fileName, originName, extension,
            file.getContentType(), file.getSize()
    );
    Attach attach = attachRepository.save(attachMapper.toAttachForRegister(registerDTO));

    return attachMapper.toAttachResponseDTO(attach, key);
  }

  // 2. GPT가 보내주는 이미지를 저장하는 로직

  @Override
  public AttachResponseDTO uploadGeneratedImage(AttachRequestDTO dto, DownloadImageDTO imageDto) {

    //1. 확장자 추출 -> GPT는 기본적으로 image/png나 image/webp로 준다고 한다 webp는 jpg로 변경
    String extension = imageDto.contentType().contains("png") ? "png" : (imageDto.contentType().contains("webp") ? "webp" : "jpg");
    //2. GPT Image는 originName이 존재하지 않음
    String originName = "WebtoonResult" + dto.targetId() + "." + extension;
    //3. fileName 지정 (UUID + 확장자)
    String fileName = UUID.randomUUID() + "." + extension;
    // 4. s3 Key 생성 (일종의 path)
    String key = generateKey(dto.targetType(), dto.targetId(), fileName);

    try {
      // S3Util을 이용해서 실제로 업로드
      s3Util.upload(imageDto.bytes(), key, imageDto.contentType());

    } catch (Exception e) {
      log.error("S3 실제 에러 원인: ", e);
      throw new GlobalException(ErrorCode.S3_UPLOAD_FAILED);
    }

    AttachRegisterDTO registerDTO = new AttachRegisterDTO(
            dto.targetType(), dto.targetId(), key, fileName, originName, extension,
            imageDto.contentType(), imageDto.fileSize()
    );
    Attach attach = attachRepository.save(attachMapper.toAttachForRegister(registerDTO));

    return attachMapper.toAttachResponseDTO(attach, buildFileUrl(key));
  }


  // 검증 로직

  private void validateFile(MultipartFile file) {

    if (file == null || file.isEmpty()) {
      throw new GlobalException(ErrorCode.FILE_NOT_FOUND);
    }

    if (file.getSize() > MAX_FILE_SIZE) {
      throw new GlobalException(ErrorCode.FILE_SIZE_EXCEEDED);
    }

    String contentType = file.getContentType();
    // image나 스캔본인 pdf만 허용
    if (contentType == null || (!contentType.startsWith("image/") && !contentType.equals("application/pdf"))) {
      throw new GlobalException(ErrorCode.INVALID_CONTENT_TYPE);
    }

    String extension = extractExtension(file.getOriginalFilename());
    if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
      throw new GlobalException(ErrorCode.INVALID_FILE_EXTENSION);
    }
  }

  //S3 key 생성 규칙
  //genie_tune/{targetType}/{targetId}/{uuid}.{ext}
  private String generateKey(TargetType targetType, Long targetId, String fileName) {
    return String.format("genie_tune/%s/%d/%s", targetType.name().toLowerCase(), targetId, fileName);
  }

  // 파일 확장자 추출 (.txt, .jpg 등)
  private String extractExtension(String filename) {
    if (filename == null || !filename.contains(".")) {
      return "";
    }
    return filename.substring(filename.lastIndexOf('.') + 1);
  }

  // S3 URL 조립
  private String buildFileUrl(String s3Key) {
    if (s3Key == null) return null;
    return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, s3Key);
  }

}
