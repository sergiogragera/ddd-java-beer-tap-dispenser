package com.rviewer.skeletons.domain.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyClosedException;
import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyOpenedException;
import com.rviewer.skeletons.domain.exceptions.DispenserNotOpenYetException;
import com.rviewer.skeletons.domain.exceptions.InvalidArgumentException;

public class TapUnitTest {
    @Test
    public void buildTapShouldThrowIllegalArgumentExceptionWheIdArgIsNotPositiveNumber () {
      assertThrows(
        IllegalArgumentException.class,
          () -> {
            new Tap(0, null);
          });
    }

    @Test
    public void buildTapShouldThrowIllegalArgumentExceptionWhenNullDispenserArg () {
      assertThrows(
        NullPointerException.class,
          () -> {
            new Tap(1, null);
          });
    }

    @Test
    public void buildTapShouldReturnClosedTapWhenNullDatesArgs() {
      final var dispenser = new Dispenser(1, 0.5f);
      final var tap = new Tap(1, dispenser);
      assertNotNull(tap);
      assertEquals(1, tap.getId().getValue());
      assertEquals(dispenser, tap.getDispenser());
      assertFalse(tap.isOpened());
      assertEquals(0, tap.getLitersDispensed());
    }

    @Test
    public void buildTapShouldReturnOpenedTapWhenOpenDateArg() {
      final var dispenser = new Dispenser(1, 0.5f);
      final var tap = new Tap(1, dispenser, LocalDateTime.now().minus(10, ChronoUnit.SECONDS));
      assertNotNull(tap);
      assertEquals(1, tap.getId().getValue());
      assertEquals(dispenser, tap.getDispenser());
      assertTrue(tap.isOpened());
      assertTrue(tap.getLitersDispensed() > 0);
    }

    @Test
    public void buildTapShouldReturnClosedTapWhenOpenDateAndCloseDateArgs() {
      final var dispenser = new Dispenser(1, 0.5f);
      final var tap = new Tap(1, dispenser, LocalDateTime.now().minus(10, ChronoUnit.SECONDS),  LocalDateTime.now());
      assertNotNull(tap);
      assertEquals(1, tap.getId().getValue());
      assertEquals(dispenser, tap.getDispenser());
      assertFalse(tap.isOpened());
      assertEquals(5, tap.getLitersDispensed());
    }

    @Test
    public void buildTapShouldThrowInvalidArgumentExceptionWhenCloseDateArgBeforeOpenDateArg() {
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

    @Test
    public void openTapShouldThrowAlreadyOpenedExceptionWhenOpenedAtIsNotNull () {
      final var dispenser = new Dispenser(1, 0.5f);
      final var openedTap = new Tap(1, dispenser, LocalDateTime.now());

      assertThrows(
        DispenserAlreadyOpenedException.class,
          () -> {
            openedTap.open(LocalDateTime.now());
          });
    }

    @Test
    public void openTaShouldReturnTapWithSameStatusWhenNullDateArg () {
      final var dispenser = new Dispenser(1, 0.5f);
      final var tap = new Tap(1, dispenser);
      final var openedTap = new Tap(1, dispenser, LocalDateTime.now());
      final var closedTap = new Tap(1, dispenser, LocalDateTime.now().minus(10, ChronoUnit.SECONDS), LocalDateTime.now());
      
      assertFalse(tap.isOpened());
      assertTrue(openedTap.isOpened());
      assertFalse(closedTap.isOpened());
      
      tap.open(null);
      openedTap.open(null);
      closedTap.open(null);

      assertFalse(tap.isOpened());
      assertTrue(openedTap.isOpened());
      assertFalse(closedTap.isOpened());
    }

    @Test
    public void closeTapShouldThrowDispenserNotOpenYetExceptionWhenOpenedAtIsNull () {
      final var dispenser = new Dispenser(1, 0.5f);
      final var tap = new Tap(1, dispenser);

      assertThrows(
        DispenserNotOpenYetException.class,
          () -> {
            tap.close(LocalDateTime.now());
          });
    }
    
    @Test
    public void closeTapShouldThrowDispenserAlreadyClosedExceptionWhenClosedAtIsNotNull () {
      final var dispenser = new Dispenser(1, 0.5f);
      final var closedTap = new Tap(1, dispenser, LocalDateTime.now().minus(10, ChronoUnit.SECONDS), LocalDateTime.now());

      assertThrows(
        DispenserAlreadyClosedException.class,
          () -> {
            closedTap.close(LocalDateTime.now());
          });
    }

    @Test
    public void closeTapShouldReturnTapWithSameStatusWhenNullDateArg() {
      final var dispenser = new Dispenser(1, 0.5f);
      final var tap = new Tap(1, dispenser);
      final var openedTap = new Tap(1, dispenser, LocalDateTime.now());
      final var closedTap = new Tap(1, dispenser, LocalDateTime.now().minus(10, ChronoUnit.SECONDS), LocalDateTime.now());
      
      assertFalse(tap.isOpened());
      assertTrue(openedTap.isOpened());
      assertFalse(closedTap.isOpened());
      
      tap.close(null);
      openedTap.close(null);
      closedTap.close(null);

      assertFalse(tap.isOpened());
      assertTrue(openedTap.isOpened());
      assertFalse(closedTap.isOpened());
    }

    @Test
    public void calculeTapLitersSpendShouldReturnPositiveNumberWhenTapIsOpened() {
      final var dispenser = new Dispenser(1, 0.5f);
      final var openedTap = new Tap(1, dispenser, LocalDateTime.now().minus(10, ChronoUnit.SECONDS));
      
      assertTrue(openedTap.isOpened());
      assertTrue(openedTap.getLitersDispensed() > 0);
    }

    @ParameterizedTest
    @CsvSource({"0.3,0", "0.5,10", "0.2,2", "0.01,25"})
    public void calculeTapLitersSpendShouldReturnExactLitersWhenTapIsClosed(float flowVolume, int seconds) {
      final var dispenser = new Dispenser(1, flowVolume);
      final var closedTap = new Tap(1, dispenser, LocalDateTime.now().minus(seconds, ChronoUnit.SECONDS), LocalDateTime.now());
      
      assertFalse(closedTap.isOpened());
      assertEquals(flowVolume * seconds, closedTap.getLitersDispensed());
    }
}
