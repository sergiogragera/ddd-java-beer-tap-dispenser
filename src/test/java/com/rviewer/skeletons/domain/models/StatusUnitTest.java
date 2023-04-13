package com.rviewer.skeletons.domain.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.Test;

public class StatusUnitTest {
  final LocalDateTime now = LocalDateTime.now();
  final LocalDateTime secondsAgo = now.minusSeconds(15);
  final LocalDateTime minutesAgo = now.minusMinutes(15);

  @Test
  public void itShouldThrowIllegalArgumentExceptionWhenDispenserIsNotClosed() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new Status(now, secondsAgo);
        });
  }

  @Test
  public void itShouldReturnNotOpenedStatus() {
    final var status = new Status();
    assertFalse(status.isOpened());
    assertFalse(status.isClosedAfter(now));
    assertFalse(status.isOpenedAfter(now));
    assertEquals(0, status.getSecondsOpened());
  }

  @Test
  public void itShouldReturnOpenedStatus() {
    final var status = new Status(secondsAgo);
    assertTrue(status.isOpened());
    assertTrue(status.getSecondsOpened() >= 15);
    assertFalse(status.isClosedAfter(now));
    assertTrue(status.isOpenedAfter(minutesAgo));
    assertFalse(status.isOpenedAfter(now));
  }

  @Test
  public void itShouldReturnClosedStatus() {
    final var status = new Status(secondsAgo, now);
    assertFalse(status.isOpened());
    assertEquals(15, status.getSecondsOpened());
    assertTrue(status.isClosedAfter(secondsAgo));
    assertTrue(status.isOpenedAfter(minutesAgo));
    assertFalse(status.isOpenedAfter(now));
    assertFalse(status.isClosedAfter(now));
  }
}
