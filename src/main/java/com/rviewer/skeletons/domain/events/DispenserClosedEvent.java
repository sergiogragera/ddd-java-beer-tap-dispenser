package com.rviewer.skeletons.domain.events;

import com.rviewer.skeletons.domain.models.Dispenser;

public class DispenserClosedEvent extends DispenserEvent {

  public DispenserClosedEvent(Dispenser dispenser) {
    super(dispenser);
  }
}
