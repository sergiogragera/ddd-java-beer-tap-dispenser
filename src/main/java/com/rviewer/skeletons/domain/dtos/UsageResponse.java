package com.rviewer.skeletons.domain.dtos;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsageResponse {
  private LocalDateTime openedAt;
  private LocalDateTime closedAt;
  private float flowVolume;
  private float totalSpend;
}
