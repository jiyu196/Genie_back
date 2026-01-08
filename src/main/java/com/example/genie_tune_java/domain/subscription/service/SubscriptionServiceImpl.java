package com.example.genie_tune_java.domain.subscription.service;

import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.product.entity.Product;
import com.example.genie_tune_java.domain.subscription.dto.GetSubscriptionResponseDTO;
import com.example.genie_tune_java.domain.subscription.dto.SubscriptionRegisterInputDTO;
import com.example.genie_tune_java.domain.subscription.entity.Subscription;
import com.example.genie_tune_java.domain.subscription.mapper.SubscriptionMapper;
import com.example.genie_tune_java.domain.subscription.repository.SubscriptionRepository;
import com.example.genie_tune_java.security.dto.JWTPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class SubscriptionServiceImpl implements SubscriptionService {
  private final SubscriptionRepository subscriptionRepository;
  private final SubscriptionMapper subscriptionMapper;

  @Override
  @Transactional
  public Subscription registerSubscription(SubscriptionRegisterInputDTO dto) {
    Subscription subscription = subscriptionMapper.toSuccessSubscriptionEntity(dto);
    subscription.activate();
    subscriptionRepository.save(subscription);
    return subscription;
  }

  @Override
  @Transactional(readOnly = true)
  public GetSubscriptionResponseDTO getSubscription() {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    JWTPrincipal jwtPrincipal = (JWTPrincipal) authentication.getPrincipal();

    Long memberId = jwtPrincipal.getMemberId();

    Subscription subscription = subscriptionRepository.findActiveSubscriptionWithProduct(memberId).orElseThrow(() -> new GlobalException(ErrorCode.SUBSCRIPTION_NOTFOUND));

    Product selectedProduct = subscription.getProduct();
    return new GetSubscriptionResponseDTO(selectedProduct.getDisplayName(), selectedProduct.getProductGrade().name(), selectedProduct.getSubscriptionCycle().name(),
            subscription.getStartDate(), subscription.getEndDate(), selectedProduct.getMaxServiceAccessIdCount(), subscription.getSubscriptionStatus().name());
  }
}
