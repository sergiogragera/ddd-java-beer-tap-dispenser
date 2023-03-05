package com.rviewer.skeletons.domain.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import com.rviewer.skeletons.domain.exceptions.InvalidArgumentException;
import com.rviewer.skeletons.domain.exceptions.InvalidDateRangeException;

public class TapUnitTest {
    @Test
    public void withoutDatesShouldReturnClosedTap() {
      final var dispenser = new Dispenser(1, 0.5f);
      final var tap = new Tap(1, dispenser);
      assertNotNull(tap);
      assertEquals(1, tap.getId());
      assertEquals(dispenser, tap.getDispenser());
      assertFalse(tap.isOpened());
      assertEquals(0, tap.getLitersDispensed());
    }

    @Test
    public void withOpenDateShouldReturnOpenedTap() {
      final var dispenser = new Dispenser(1, 0.5f);
      final var tap = new Tap(1, dispenser, LocalDateTime.now().minus(10, ChronoUnit.SECONDS));
      assertNotNull(tap);
      assertEquals(1, tap.getId());
      assertEquals(dispenser, tap.getDispenser());
      assertTrue(tap.isOpened());
      assertTrue(tap.getLitersDispensed() > 0);
    }

    @Test
    public void withOpenAndCloseDateShouldReturnClosedTap() {
      final var dispenser = new Dispenser(1, 0.5f);
      final var tap = new Tap(1, dispenser, LocalDateTime.now().minus(10, ChronoUnit.SECONDS),  LocalDateTime.now());
      assertNotNull(tap);
      assertEquals(1, tap.getId());
      assertEquals(dispenser, tap.getDispenser());
      assertFalse(tap.isOpened());
      assertEquals(5, tap.getLitersDispensed());
    }

    @Test
    public void withCloseBeforeOpenDateShouldThrowInvalidArgumentException() {
      final var dispenser = new Dispenser(1, 0.5f);

      Exception exception =
        assertThrows(
            InvalidArgumentException.class,
            () -> {
                new Tap(1, dispenser, LocalDateTime.now(), LocalDateTime.now().minus(10, ChronoUnit.SECONDS));
            });

        var expectedMessage = "close date before open";
        var actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
