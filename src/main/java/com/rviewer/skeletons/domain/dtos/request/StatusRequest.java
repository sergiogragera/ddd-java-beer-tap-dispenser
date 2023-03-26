package com.rviewer.skeletons.domain.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.constraints.NotNull;

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

  @NotNull
  private Status status;

  @JsonProperty("updated_at")
  private Optional<LocalDateTime> updatedAt;
}
