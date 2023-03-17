package com.rviewer.skeletons.domain.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusRequest {
  public enum Status {
    open,
    close
  }

  private Status status;
  @JsonProperty("updated_at")
  private LocalDateTime updatedAt;
}
