package com.rviewer.skeletons.domain.exceptions;

public class DispenserOpenedAfterCloseException extends UnsupportedOperationException {
  public DispenserOpenedAfterCloseException() {
    super("Dispenser opened after new close date");
  }
}