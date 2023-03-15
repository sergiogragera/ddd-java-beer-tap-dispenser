package com.rviewer.skeletons.domain.dtos;

import lombok.Getter;

@Getter
public class DispenserResponse extends DispenserRequest {
  private int id;

  public DispenserResponse(int id, float flowVolume) {
    super(flowVolume);
    this.id = id;
  }
}
