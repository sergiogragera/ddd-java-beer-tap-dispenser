package com.rviewer.skeletons.domain.dtos.response;

import java.time.LocalDateTime;

import com.rviewer.skeletons.domain.models.Dispenser;
import com.rviewer.skeletons.domain.models.Usage;
import lombok.Getter;

@Getter
public class UsageResponse {
  private LocalDateTime openedAt;
  private LocalDateTime closedAt;
  private float flowVolume;
  private float totalSpent;

  public UsageResponse(Usage usage) {
    this.openedAt = usage.getStatus().getOpenedAt();
    this.closedAt = usage.getStatus().getClosedAt();
    this.flowVolume = usage.getFlowVolume();
    this.totalSpent = usage.getTotalSpent();
  }

  public UsageResponse(Dispenser dispenser) {
    this.openedAt = dispenser.getStatus().getOpenedAt();
    this.closedAt = dispenser.getStatus().getClosedAt();
    this.flowVolume = dispenser.getFlowVolume();
    this.totalSpent = dispenser.getTotalSpent();
  }
}
