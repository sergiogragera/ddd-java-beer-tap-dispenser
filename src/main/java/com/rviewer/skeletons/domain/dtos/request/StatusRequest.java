package com.rviewer.skeletons.domain.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
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

  @NotNull private Status status;

  @JsonProperty("updated_at")
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime updatedAt;
}
