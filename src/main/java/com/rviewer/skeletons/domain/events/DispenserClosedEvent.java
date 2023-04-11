package com.rviewer.skeletons.domain.events;

import com.rviewer.skeletons.domain.models.Dispenser;
import lombok.Getter;

@Getter
public class DispenserClosedEvent {
  private Dispenser dispenser;

  public DispenserClosedEvent(Dispenser dispenser) {
    this.dispenser = dispenser;
  }
}
