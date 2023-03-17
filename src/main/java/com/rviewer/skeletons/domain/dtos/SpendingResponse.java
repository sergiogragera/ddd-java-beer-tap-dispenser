package com.rviewer.skeletons.domain.dtos;

import java.util.ArrayList;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SpendingResponse {
  private ArrayList<UsageResponse> usages = new ArrayList<>();

  public float getAmount() {
    return usages.stream().map(UsageResponse::getFlowVolume).reduce(0f, Float::sum);
  }
}
