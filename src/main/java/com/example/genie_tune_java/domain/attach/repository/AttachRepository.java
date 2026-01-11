package com.example.genie_tune_java.domain.attach.repository;

import com.example.genie_tune_java.domain.attach.entity.Attach;
import com.example.genie_tune_java.domain.attach.entity.AttachTargetType;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface AttachRepository extends CrudRepository<Attach, Long> {


  List<Attach> findByAttachTargetTypeInAndTargetIdIn(List<AttachTargetType> targetTypes, List<Long> memberIds);
}
