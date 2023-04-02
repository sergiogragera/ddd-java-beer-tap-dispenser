package com.rviewer.skeletons.domain.models.valueobjects;

import com.rviewer.skeletons.domain.exceptions.InvalidArgumentException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Status {
  @Column private LocalDateTime openedAt;

  @Column private LocalDateTime closedAt;

  public Status() {}

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

  public long getSecondsOpened() {
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
