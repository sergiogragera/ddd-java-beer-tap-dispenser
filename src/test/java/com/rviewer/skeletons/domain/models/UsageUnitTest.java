package com.rviewer.skeletons.domain.models;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

public class UsageUnitTest {
  @Test
  public void itShouldThrowIllegalArgumentExceptionWhenFlowVolumeArgIsNotPositiveNumber() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          final var dispenser = new Dispenser(BigDecimal.valueOf(0.5));
          new Usage(dispenser);
        });
  }

  @Test
  public void itShouldReturnUsage() {
    final var dispenser = new Dispenser(BigDecimal.valueOf(0.5));
    dispenser.open(Optional.of(LocalDateTime.now()));
    dispenser.close(Optional.of(LocalDateTime.now()));

    final var usage = new Usage(dispenser);

    assertNotNull(usage);
    assertNotNull(dispenser.getId());
  }
}
