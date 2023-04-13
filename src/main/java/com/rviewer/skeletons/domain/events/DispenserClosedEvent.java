package com.rviewer.skeletons.domain.events;

import java.time.LocalDateTime;

import com.rviewer.skeletons.domain.models.Dispenser;

import lombok.Getter;

@Getter
public class DispenserClosedEvent {
  private Dispenser dispenser;

  private LocalDateTime createdAt;

  public DispenserClosedEvent(Dispenser dispenser) {
    this.dispenser = dispenser;
    this.createdAt = LocalDateTime.now();
  }
}
