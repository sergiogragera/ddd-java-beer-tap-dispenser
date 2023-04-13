package com.rviewer.skeletons.application.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyClosedException;
import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyOpenedException;
import com.rviewer.skeletons.domain.models.Dispenser;
import com.rviewer.skeletons.domain.persistence.DispenserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StatusServiceUnitTest {

  @InjectMocks private StatusService service;

  @Mock private DispenserRepository dispenserRepository;

  @Test
  void itShouldThrowDispenserAlreadyOpenedExceptionWhenOpen() {
    final var dispenser = new Dispenser(BigDecimal.valueOf(0.5));
    dispenser.open(LocalDateTime.now());
    when(dispenserRepository.findById(dispenser.getId())).thenReturn(Optional.of(dispenser));

    Assertions.assertThrows(
        DispenserAlreadyOpenedException.class,
        () -> service.open(dispenser.getId(), Optional.of(LocalDateTime.now())));
    verify(dispenserRepository, times(1)).findById(any(UUID.class));
    verifyNoMoreInteractions(dispenserRepository);
  }

  @Test
  void itShouldSaveOpenedDispenserWhenOpen() {
    final var dispenser = new Dispenser(BigDecimal.valueOf(0.5));
    when(dispenserRepository.findById(dispenser.getId())).thenReturn(Optional.of(dispenser));

    service.open(dispenser.getId(), Optional.of(LocalDateTime.now()));
    verify(dispenserRepository, times(1)).save(any(Dispenser.class));
    verifyNoMoreInteractions(dispenserRepository);
  }

  @Test
  void itShouldThrowDispenserAlreadyClosedExceptionWhenClose() {
    final var dispenser = new Dispenser(BigDecimal.valueOf(0.5));
    when(dispenserRepository.findById(dispenser.getId())).thenReturn(Optional.of(dispenser));

    Assertions.assertThrows(
        DispenserAlreadyClosedException.class,
        () -> service.close(dispenser.getId(), Optional.of(LocalDateTime.now())));
    verify(dispenserRepository, times(1)).findById(any(UUID.class));
    verifyNoMoreInteractions(dispenserRepository);
  }

  @Test
  void itShouldSaveClosedDispenserWhenClose() {
    final var dispenser = new Dispenser(BigDecimal.valueOf(0.5));
    dispenser.open(LocalDateTime.now());
    when(dispenserRepository.findById(dispenser.getId())).thenReturn(Optional.of(dispenser));

    service.close(dispenser.getId(), Optional.of(LocalDateTime.now()));
    verify(dispenserRepository, times(1)).save(any(Dispenser.class));
    verifyNoMoreInteractions(dispenserRepository);
  }
}
