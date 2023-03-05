package com.rviewer.skeletons.domain.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.rviewer.skeletons.domain.exceptions.InvalidArgumentException;

public class DispenserUnitTest {
    @Test
    public void buildDispenserShouldThrowIllegalArgumentExceptionWheIdArgIsNotPositiveNumber () {
      assertThrows(
        IllegalArgumentException.class,
          () -> {
            new Dispenser(0, 0.5f);
          });
    }

    @ParameterizedTest
    @ValueSource(floats = { -1f, 0 })
    public void buildDispenserShouldThrowIllegalArgumentExceptionWhenNullFlowVolumeArgIsNotPositiveNumber (float flowVolume) {
      assertThrows(
        InvalidArgumentException.class,
          () -> {
            new Dispenser(1, flowVolume);
          });
    }

    @Test
    public void buildDispenserShouldReturnDispenser() {
      final var dispenser = new Dispenser(1, 0.5f);
      
      assertNotNull(dispenser);
      assertEquals(1, dispenser.getId().getValue());
      assertEquals(0.5f, dispenser.getFlowVolume());
      assertNotNull(dispenser.getCreatedAt());
    }

    @Test
    public void buildDispenserShouldReturnDispenserCreatedAtWhenCreatedAtArg() {
      final var createdAt = LocalDateTime.now();
      final var dispenser = new Dispenser(1, 0.5f, createdAt);
      
      assertNotNull(dispenser);
      assertEquals(1, dispenser.getId().getValue());
      assertEquals(0.5f, dispenser.getFlowVolume());
      assertEquals(createdAt, dispenser.getCreatedAt());
    }
}
