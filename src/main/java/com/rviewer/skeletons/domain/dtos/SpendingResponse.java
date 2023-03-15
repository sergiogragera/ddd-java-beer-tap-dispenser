package com.rviewer.skeletons.domain.dtos;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SpendingResponse {
  private List<UsageResponse> usages = Arrays.asList();

  public float getAmount() {
    return usages.stream().map(UsageResponse::getFlowVolume).reduce(0f, Float::sum);
  }
}
