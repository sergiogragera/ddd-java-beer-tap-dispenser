package com.rviewer.skeletons.domain.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyClosedException;
import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyOpenedException;
import com.rviewer.skeletons.domain.exceptions.InvalidArgumentException;
import com.rviewer.skeletons.domain.models.valueobjects.Status;

public class DispenserUnitTest {
    @Test
    public void itShouldThrowIllegalArgumentExceptionWheIdArgIsNotPositiveNumber () {
      assertThrows(
        IllegalArgumentException.class,
          () -> {
            new Dispenser(0, 0.5f);
          });
    }

    @ParameterizedTest
    @ValueSource(floats = { -1f, 0 })
    public void itShouldThrowIllegalArgumentExceptionWhenFlowVolumeArgIsNotPositiveNumber (float flowVolume) {
      assertThrows(
        InvalidArgumentException.class,
          () -> {
            new Dispenser(1, flowVolume);
          });
    }

    @Test
    public void itShouldReturnDispenser() {
      final var dispenser = new Dispenser(1, 0.5f);
      
      assertNotNull(dispenser);
      assertEquals(1, dispenser.getId().getValue());
      assertEquals(0.5f, dispenser.getFlowVolume());
      assertNotNull(dispenser.getCreatedAt());
    }

    @Test
    public void itShouldReturnDispenserCreatedAtWhenCreatedAtArg() {
      final var now = LocalDateTime.now();
      final var dispenser = new Dispenser(1, 0.5f, null, now);
      
      assertNotNull(dispenser);
      assertEquals(1, dispenser.getId().getValue());
      assertEquals(now, dispenser.getCreatedAt());
    }

    @Test
    public void itShouldReturnDispenserStatusOpenedAtWhenStatusOpenedArg() {
      final var now = LocalDateTime.now();
      final var dispenser = new Dispenser(1, 0.5f, new Status(now));
      
      assertNotNull(dispenser);
      assertEquals(1, dispenser.getId().getValue());
      assertEquals(now, dispenser.getStatus().getOpenedAt());
    }

    @Test
    public void itShouldReturnDispenserStatusOpenedAtWhenStatusClosedArg() {
      final var now = LocalDateTime.now();
      final var secondsAgo = now.minusSeconds(15);
      final var dispenser = new Dispenser(1, 0.5f, new Status(secondsAgo, now));
      
      assertNotNull(dispenser);
      assertEquals(1, dispenser.getId().getValue());
      assertEquals(15, dispenser.getStatus().getSecondsOpened());
    }

    @Test
    public void itShouldReturnDispenserStatusOpenedAtWhenOpen() {
      final var now = LocalDateTime.now();
      final var dispenser = new Dispenser(1, 0.5f);

      dispenser.open(now);
      
      assertNotNull(dispenser);
      assertEquals(1, dispenser.getId().getValue());
      assertEquals(now, dispenser.getStatus().getOpenedAt());
    }

    @Test
    public void itShouldReturnDispenserStatusClosedAtWhenOpenAndClose() {
      final var now = LocalDateTime.now();
      final var secondsAgo = now.minusSeconds(15);
      final var dispenser = new Dispenser(1, 0.5f);

      dispenser.open(secondsAgo);
      dispenser.close(now);
      
      assertNotNull(dispenser);
      assertEquals(1, dispenser.getId().getValue());
      assertEquals(15, dispenser.getStatus().getSecondsOpened());
    }

    @Test
    public void itShouldThrowDispenserAlreadyOpenedExceptionWhenOpenAnOpenedDispenser () {
      assertThrows(
        DispenserAlreadyOpenedException.class,
          () -> {
            var dispenser = new Dispenser(1, 0.5f, new Status(LocalDateTime.now()));
            dispenser.open(LocalDateTime.now());
          });
    }

    @Test
    public void itShouldThrowDispenserAlreadyClosedExceptionWhenCloseAClosedDispenser () {
      assertThrows(
        DispenserAlreadyClosedException.class,
          () -> {
            final var now = LocalDateTime.now();
            final var secondsAgo = now.minusSeconds(15);
            var dispenser = new Dispenser(1, 0.5f, new Status(secondsAgo, now));
            dispenser.close(LocalDateTime.now());
          });
    }

    @Test
    public void itShouldGetLitersGreaterThanZeroWhenOpenedDispenser () {
      final var secondsAgo =  LocalDateTime.now().minusSeconds(10);
      final var dispenser = new Dispenser(1, 0.5f, new Status(secondsAgo));

      assertNotNull(dispenser);
      assertEquals(1, dispenser.getId().getValue());
      assertTrue(dispenser.getLitersDispensed() >= 5);

      final var minutesAgo =  LocalDateTime.now().minusMinutes(10);
      final var dispenserOpenedBefore = new Dispenser(2, 0.5f, new Status(minutesAgo));

      assertNotNull(dispenserOpenedBefore);
      assertEquals(2, dispenserOpenedBefore.getId().getValue());
      assertTrue(dispenserOpenedBefore.getLitersDispensed() >= 50);
      assertTrue(dispenserOpenedBefore.getLitersDispensed() > dispenser.getLitersDispensed());
    }

    @Test
    public void itShouldGetKnownLitersWhenClosedDispenser () {
      final var now = LocalDateTime.now();
      final var secondsAgo = now.minusSeconds(10);
      final var dispenser = new Dispenser(1, 0.5f, new Status(secondsAgo, now));

      assertNotNull(dispenser);
      assertEquals(1, dispenser.getId().getValue());
      assertEquals(5, dispenser.getLitersDispensed());
    }
}
