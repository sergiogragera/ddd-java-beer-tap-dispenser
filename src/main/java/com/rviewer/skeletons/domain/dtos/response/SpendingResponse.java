package com.rviewer.skeletons.domain.dtos.response;

import java.util.List;

import lombok.Getter;

@Getter
public class SpendingResponse {
  private final List<UsageResponse> usages;

  public SpendingResponse(List<UsageResponse> usages) {
    this.usages = usages;
  }

  public float getAmount() {
    return usages.stream()
        .map(UsageResponse::getTotalSpent)
        .reduce(0f, (total, usageSpend) -> Float.sum(total, usageSpend));
  }
}
