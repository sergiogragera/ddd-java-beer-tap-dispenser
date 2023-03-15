package com.rviewer.skeletons.domain.models.valueobjects;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.rviewer.skeletons.domain.exceptions.InvalidArgumentException;

import lombok.Getter;

public class Status {
  @Getter private LocalDateTime openedAt;
  private LocalDateTime closedAt;

  public Status() {
    this(null);
  }

  public Status(LocalDateTime openedAt) {
    this(openedAt, null);
  }

  public Status(LocalDateTime openedAt, LocalDateTime closedAt) {
    this.openedAt = openedAt;

    if (closedAt != null) {
      if (openedAt == null || !closedAt.isAfter(this.openedAt)) {
        throw new InvalidArgumentException("close date before open");
      }
      this.closedAt = closedAt;
    }
  }

  public boolean isOpened() {
    return this.openedAt != null && this.closedAt == null;
  }

  public float getSecondsOpened() {
    if (this.openedAt != null) {
      LocalDateTime start = this.openedAt;
      LocalDateTime end = LocalDateTime.now();
      if (this.closedAt != null) {
        end = this.closedAt;
      }
      return start.until(end, ChronoUnit.SECONDS);
    }
    return 0;
  }
}
