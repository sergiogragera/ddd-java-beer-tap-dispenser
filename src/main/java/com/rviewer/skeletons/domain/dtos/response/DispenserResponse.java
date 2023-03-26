package com.rviewer.skeletons.domain.dtos.response;

import java.util.UUID;

import com.rviewer.skeletons.domain.dtos.request.DispenserRequest;

import lombok.Getter;

@Getter
public class DispenserResponse extends DispenserRequest {
  private UUID id;

  public DispenserResponse(UUID id, float flowVolume) {
    super(flowVolume);
    this.id = id;
  }
}
