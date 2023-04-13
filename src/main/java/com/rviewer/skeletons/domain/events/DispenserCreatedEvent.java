package com.rviewer.skeletons.domain.events;

import com.rviewer.skeletons.domain.models.Dispenser;

public class DispenserCreatedEvent extends DispenserEvent {

  public DispenserCreatedEvent(Dispenser dispenser) {
    super(dispenser);
  }
}
