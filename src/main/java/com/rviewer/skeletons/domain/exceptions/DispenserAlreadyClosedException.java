package com.rviewer.skeletons.domain.exceptions;

public class DispenserAlreadyClosedException extends UnsupportedOperationException {
  public DispenserAlreadyClosedException() {
    super("Dispenser is already closed");
  }
}
