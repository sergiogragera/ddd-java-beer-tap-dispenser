package com.rviewer.skeletons.domain.exceptions;

public class DispenserClosedAfterOpenException extends UnsupportedOperationException {
  public DispenserClosedAfterOpenException() {
    super("Dispenser closed after new open date");
  }
}
