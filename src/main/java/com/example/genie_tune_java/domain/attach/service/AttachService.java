package com.example.genie_tune_java.domain.attach.service;

import com.example.genie_tune_java.api.gpt.dto.DownloadImageDTO;
import com.example.genie_tune_java.domain.attach.dto.AttachRequestDTO;
import com.example.genie_tune_java.domain.attach.dto.AttachResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface AttachService {
  AttachResponseDTO upload(AttachRequestDTO dto, MultipartFile file);
  public AttachResponseDTO uploadGeneratedImage(AttachRequestDTO dto, DownloadImageDTO imageDto);
  String buildFileUrl(String s3Key);
}
