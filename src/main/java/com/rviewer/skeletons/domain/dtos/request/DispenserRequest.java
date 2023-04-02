package com.rviewer.skeletons.domain.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DispenserRequest {
  @Positive
  @JsonProperty("flow_volume")
  private BigDecimal flowVolume;
}
