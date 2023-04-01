package com.rviewer.skeletons.domain.dtos.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;

@Getter
public class SpendingResponse {
  private final List<UsageResponse> usages;

  public SpendingResponse(List<UsageResponse> usages) {
    this.usages = usages;
  }

  public BigDecimal getAmount() {
    return usages.stream()
        .map(UsageResponse::getTotalSpent)
        .reduce(BigDecimal.ZERO, (total, usageSpend) -> total.add(usageSpend));
  }
}
