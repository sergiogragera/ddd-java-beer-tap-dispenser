package com.rviewer.skeletons.domain.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyClosedException;
import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyOpenedException;
import com.rviewer.skeletons.domain.exceptions.DispenserClosedAfterOpenException;
import com.rviewer.skeletons.domain.exceptions.DispenserOpenedAfterCloseException;
import com.rviewer.skeletons.domain.exceptions.InvalidArgumentException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DispenserUnitTest {

  @ParameterizedTest
  @ValueSource(strings = {"-1", "-0.01", "0.00", "0"})
  public void itShouldThrowIllegalArgumentExceptionWhenFlowVolumeArgIsNotPositiveNumber(
      BigDecimal flowVolume) {
    assertThrows(
        InvalidArgumentException.class,
        () -> {
          new Dispenser(flowVolume);
        });
  }

  @Test
  public void itShouldReturnDispenser() {
    final var dispenser = new Dispenser(BigDecimal.valueOf(0.5));

    assertNotNull(dispenser);
    assertEquals(BigDecimal.valueOf(0.5), dispenser.getFlowVolume());
    assertNotNull(dispenser.getStatus());
  }

  @Test
  public void itShouldReturnOpenDispenserWhenOpen() {
    final var now = LocalDateTime.now();
    final var dispenser = new Dispenser(BigDecimal.valueOf(0.5));

    dispenser.open(Optional.of(now));

    assertNotNull(dispenser);
    assertTrue(dispenser.isOpened());
    assertEquals(now, dispenser.getStatus().getOpenedAt());
  }

  @Test
  public void itShouldReturnClosedStatusWhenOpenAndClose() {
    final var now = LocalDateTime.now();
    final var secondsAgo = now.minusSeconds(15);
    final var dispenser = new Dispenser(BigDecimal.valueOf(0.5));

    dispenser.open(Optional.of(secondsAgo));
    dispenser.close(Optional.of(now));

    assertNotNull(dispenser);
    assertEquals(secondsAgo, dispenser.getStatus().getOpenedAt());
    assertEquals(now, dispenser.getStatus().getClosedAt());
    assertFalse(dispenser.getStatus().isOpened());
  }

  @Test
  public void itShouldThrowDispenserAlreadyOpenedExceptionWhenOpenAnOpenedDispenser() {
    assertThrows(
        DispenserAlreadyOpenedException.class,
        () -> {
          var dispenser = new Dispenser(BigDecimal.valueOf(0.5));

          dispenser.open(Optional.of(LocalDateTime.now()));
          dispenser.open(Optional.of(LocalDateTime.now()));
        });
  }

  @Test
  public void itShouldThrowDispenserAlreadyClosedExceptionWhenCloseAClosedDispenser() {
    assertThrows(
        DispenserAlreadyClosedException.class,
        () -> {
          final var now = LocalDateTime.now();
          final var secondsAgo = now.minusSeconds(15);

          var dispenser = new Dispenser(BigDecimal.valueOf(0.5));

          dispenser.open(Optional.of(secondsAgo));
          dispenser.close(Optional.of(now));
          dispenser.close(Optional.of(now));
        });
  }

  @Test
  public void itShouldThrowDispenserClosedAfterOpenExceptionWhenOpenBeforeAClosedDispenser() {
    assertThrows(
        DispenserClosedAfterOpenException.class,
        () -> {
          final var now = LocalDateTime.now();
          final var secondsAgo = now.minusSeconds(15);
          final var minuteAgo = now.minusSeconds(1);

          var dispenser = new Dispenser(BigDecimal.valueOf(0.5));

          dispenser.open(Optional.of(secondsAgo));
          dispenser.close(Optional.of(now));
          dispenser.open(Optional.of(minuteAgo));
        });
  }

  @Test
  public void itShouldThrowDispenserOpenedAfterCloseExceptionWhenCloseBeforeAOpenedDispenser() {
    assertThrows(
        DispenserOpenedAfterCloseException.class,
        () -> {
          final var now = LocalDateTime.now();
          final var secondsAgo = now.minusSeconds(15);

          var dispenser = new Dispenser(BigDecimal.valueOf(0.5));

          dispenser.open(Optional.of(now));
          dispenser.close(Optional.of(secondsAgo));
        });
  }

  @Test
  public void itShouldGetLitersGreaterThanZeroWhenOpenedDispenser() {
    final var secondsAgo = LocalDateTime.now().minusSeconds(10);
    final var dispenser = new Dispenser(BigDecimal.valueOf(0.5));

    dispenser.open(Optional.of(secondsAgo));

    assertNotNull(dispenser);
    assertTrue(dispenser.getLitersDispensed().compareTo(BigDecimal.valueOf(5)) >= 0);

    final var minutesAgo = LocalDateTime.now().minusMinutes(10);
    final var dispenserOpenedBefore = new Dispenser(BigDecimal.valueOf(0.5));

    dispenserOpenedBefore.open(Optional.of(minutesAgo));

    assertNotNull(dispenserOpenedBefore);
    assertTrue(dispenserOpenedBefore.getLitersDispensed().compareTo(BigDecimal.valueOf(50)) >= 0);
    assertTrue(
        dispenserOpenedBefore.getLitersDispensed().compareTo(dispenser.getLitersDispensed()) > 0);
  }

  @Test
  public void itShouldGetKnownLitersWhenClosedDispenser() {
    final var now = LocalDateTime.now();
    final var secondsDispensed = 10;
    final var secondsAgo = now.minusSeconds(secondsDispensed);
    final var flowVolume = 0.66;
    final var dispenser = new Dispenser(BigDecimal.valueOf(flowVolume));

    dispenser.open(Optional.of(secondsAgo));
    dispenser.close(Optional.of(now));

    assertNotNull(dispenser);
    assertEquals(
        BigDecimal.valueOf(flowVolume).multiply(BigDecimal.valueOf(secondsDispensed)),
        dispenser.getLitersDispensed());
  }
}
