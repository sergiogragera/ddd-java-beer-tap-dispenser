package com.rviewer.skeletons.domain.models;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class UsageUnitTest {
  @Test
  public void itShouldThrowIllegalArgumentExceptionWhenDispenserIsNotClosed() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          final var dispenser = new Dispenser(BigDecimal.valueOf(0.5));
          new Usage(dispenser);
        });
  }

  @Test
  public void itShouldThrowIllegalArgumentExceptionWhenDispenserIsOpened() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          final var dispenser = new Dispenser(BigDecimal.valueOf(0.5));
          dispenser.open(LocalDateTime.now());
          new Usage(dispenser);
        });
  }

  @Test
  public void itShouldReturnUsage() {
    final var dispenser = new Dispenser(BigDecimal.valueOf(0.5));
    dispenser.open(LocalDateTime.now());
    dispenser.close(LocalDateTime.now());

    final var usage = new Usage(dispenser);

    assertNotNull(usage);
    assertFalse(usage.getStatus().isOpened());
    assertNotNull(usage.getTotalSpent());
  }
}
