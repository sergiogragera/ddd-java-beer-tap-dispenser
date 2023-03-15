package com.rviewer.skeletons.domain.models.valueobjects;

public class Id {
  private int value;

  public Id(int value) {
    if (value < 1) {
      throw new IllegalArgumentException("invalid id: must be positive number");
    }

    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
