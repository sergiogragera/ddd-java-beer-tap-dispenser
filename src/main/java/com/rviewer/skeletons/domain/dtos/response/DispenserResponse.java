package com.rviewer.skeletons.domain.dtos.response;

import java.math.BigDecimal;
import java.util.UUID;

import com.rviewer.skeletons.domain.dtos.request.DispenserRequest;

import lombok.Getter;

@Getter
public class DispenserResponse extends DispenserRequest {
  private UUID id;

  public DispenserResponse(UUID id, BigDecimal flowVolume) {
    super(flowVolume);
    this.id = id;
  }
}
