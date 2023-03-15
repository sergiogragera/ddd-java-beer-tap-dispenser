package com.rviewer.skeletons.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DispenserRequest {
  @JsonProperty("flow_volume")
  private float flowVolume;
}
