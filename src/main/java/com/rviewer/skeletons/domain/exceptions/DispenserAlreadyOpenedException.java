package com.rviewer.skeletons.domain.exceptions;

public class DispenserAlreadyOpenedException extends UnsupportedOperationException {
  public DispenserAlreadyOpenedException() {
    super("Dispenser is already opened");
  }
}
