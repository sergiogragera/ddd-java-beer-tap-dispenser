package com.rviewer.skeletons.application.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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

import com.rviewer.skeletons.domain.dtos.request.DispenserRequest;
import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyClosedException;
import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyOpenedException;
import com.rviewer.skeletons.domain.exceptions.DispenserNotFoundException;
import com.rviewer.skeletons.domain.models.Dispenser;
import com.rviewer.skeletons.domain.models.Usage;
import com.rviewer.skeletons.domain.persistence.DispenserRepository;
import com.rviewer.skeletons.domain.persistence.UsageRepository;

@ExtendWith(MockitoExtension.class)
public class DispenserServiceUnitTest {

  @InjectMocks private DispenserService service;

  @Mock private DispenserRepository dispenserRepository;

  @Mock private UsageRepository usageRepository;

  @Test
  void itShouldCreateDispenser() {
    final var dispenser = new Dispenser(BigDecimal.valueOf(0.5));
    when(dispenserRepository.save(any(Dispenser.class))).thenReturn(dispenser);

    final var dispenserResponse = service.create(new DispenserRequest(dispenser.getFlowVolume()));
    assertNotNull(dispenserResponse.getId());
    assertEquals(dispenser.getFlowVolume(), dispenserResponse.getFlowVolume());
  }

  @Test
  void isShouldThrowDispenserAlreadyOpenedExceptionWhenOpen() {
    final var dispenser = new Dispenser(BigDecimal.valueOf(0.5));
    dispenser.open(Optional.of(LocalDateTime.now()));
    when(dispenserRepository.findById(dispenser.getId())).thenReturn(Optional.of(dispenser));

    Assertions.assertThrows(
        DispenserAlreadyOpenedException.class,
        () -> service.open(dispenser.getId(), Optional.of(LocalDateTime.now())));
    verify(dispenserRepository, times(1)).findById(any(UUID.class));
    verifyNoMoreInteractions(dispenserRepository);
  }

  @Test
  void isShouldSaveOpenedDispenserWhenOpen() {
    final var dispenser = new Dispenser(BigDecimal.valueOf(0.5));
    when(dispenserRepository.findById(dispenser.getId())).thenReturn(Optional.of(dispenser));

    service.open(dispenser.getId(), Optional.of(LocalDateTime.now()));
    verify(dispenserRepository, times(1)).save(any(Dispenser.class));
    verifyNoMoreInteractions(dispenserRepository);
  }

  @Test
  void isShouldThrowDispenserAlreadyClosedExceptionWhenClose() {
    final var dispenser = new Dispenser(BigDecimal.valueOf(0.5));
    when(dispenserRepository.findById(dispenser.getId())).thenReturn(Optional.of(dispenser));

    Assertions.assertThrows(
        DispenserAlreadyClosedException.class,
        () -> service.close(dispenser.getId(), Optional.of(LocalDateTime.now())));
    verify(dispenserRepository, times(1)).findById(any(UUID.class));
    verifyNoMoreInteractions(dispenserRepository);
  }

  @Test
  void isShouldSaveClosedDispenserWhenClose() {
    final var dispenser = new Dispenser(BigDecimal.valueOf(0.5));
    dispenser.open(Optional.of(LocalDateTime.now()));
    when(dispenserRepository.findById(dispenser.getId())).thenReturn(Optional.of(dispenser));

    service.close(dispenser.getId(), Optional.of(LocalDateTime.now()));
    verify(dispenserRepository, times(1)).save(any(Dispenser.class));
    verifyNoMoreInteractions(dispenserRepository);
    verify(usageRepository, times(1)).save(any(Usage.class));
    verifyNoMoreInteractions(usageRepository);
  }
}
