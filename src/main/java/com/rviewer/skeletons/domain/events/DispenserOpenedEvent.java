package com.rviewer.skeletons.domain.events;

import com.rviewer.skeletons.domain.models.Dispenser;

public class DispenserOpenedEvent extends DispenserEvent {

  public DispenserOpenedEvent(Dispenser dispenser) {
    super(dispenser);
  }
}
