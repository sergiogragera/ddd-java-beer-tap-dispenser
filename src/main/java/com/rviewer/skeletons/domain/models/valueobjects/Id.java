package com.rviewer.skeletons.domain.models.valueobjects;

public class Id {
  private int value;

  public Id(int value) {
    if (value < 0) {
      throw new IllegalArgumentException("invalid id: must be greater or queal than zero");
    }

    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
