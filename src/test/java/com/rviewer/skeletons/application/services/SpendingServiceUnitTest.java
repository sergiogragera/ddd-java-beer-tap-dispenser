package com.rviewer.skeletons.application.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.rviewer.skeletons.domain.exceptions.DispenserNotFoundException;
import com.rviewer.skeletons.domain.models.Dispenser;
import com.rviewer.skeletons.domain.models.Usage;
import com.rviewer.skeletons.domain.persistence.DispenserRepository;
import com.rviewer.skeletons.domain.persistence.UsageRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SpendingServiceUnitTest {
  final LocalDateTime now = LocalDateTime.now();
  final LocalDateTime secondsAgo = now.minusSeconds(15);
  final LocalDateTime minutesAgo = now.minusMinutes(15);

  @InjectMocks private SpendingService service;

  @Mock private DispenserRepository dispenserRepository;
  @Mock private UsageRepository usageRepository;

  @Test
  void itShouldThrowDispenserNotFoundExceptionWhenDispenserNotFound() {
    final var dispenserId = UUID.randomUUID();
    when(dispenserRepository.findById(dispenserId)).thenReturn(Optional.empty());

    Assertions.assertThrows(
        DispenserNotFoundException.class, () -> service.getSpendings(dispenserId));
    verify(dispenserRepository, times(1)).findById(any(UUID.class));
    verifyNoMoreInteractions(dispenserRepository);
    verifyNoMoreInteractions(usageRepository);
  }

  @Test
  void itShouldReturnDispenserUsagesEmptyWhenDispenserNotOpened() {
    final var dispenser = new Dispenser(BigDecimal.valueOf(0.5));
    when(dispenserRepository.findById(dispenser.getId())).thenReturn(Optional.of(dispenser));
    when(usageRepository.findByDispenserId(dispenser.getId())).thenReturn(Collections.emptyList());

    final var response = service.getSpendings(dispenser.getId());
    assertEquals(0, response.getUsages().size());
    assertTrue(response.getAmount().compareTo(BigDecimal.ZERO) == 0);
    verify(dispenserRepository, times(1)).findById(dispenser.getId());
    verifyNoMoreInteractions(dispenserRepository);
    verifyNoMoreInteractions(usageRepository);
  }

  @Test
  void itShouldReturnDispenserUsagesWhenDispenserOpened() {
    final var dispenser = new Dispenser(BigDecimal.valueOf(0.5));
    dispenser.open(secondsAgo);
    when(dispenserRepository.findById(dispenser.getId())).thenReturn(Optional.of(dispenser));
    when(usageRepository.findByDispenserId(dispenser.getId())).thenReturn(Collections.emptyList());

    final var response = service.getSpendings(dispenser.getId());
    assertEquals(1, response.getUsages().size());
    assertTrue(response.getAmount().compareTo(BigDecimal.ZERO) > 0);
    verify(dispenserRepository, times(1)).findById(dispenser.getId());
    verifyNoMoreInteractions(dispenserRepository);
    verify(usageRepository, times(1)).findByDispenserId(dispenser.getId());
    verifyNoMoreInteractions(usageRepository);
  }

  @Test
  void itShouldReturnDispenserUsagesWhenDispenserHasClosedUsages() {
    final var dispenser = new Dispenser(BigDecimal.valueOf(0.5));
    dispenser.open(secondsAgo);
    dispenser.close(now);
    final var usage = new Usage(dispenser);
    dispenser.open(LocalDateTime.now());

    when(dispenserRepository.findById(dispenser.getId())).thenReturn(Optional.of(dispenser));
    when(usageRepository.findByDispenserId(dispenser.getId())).thenReturn(Arrays.asList(usage));

    final var response = service.getSpendings(dispenser.getId());
    assertEquals(2, response.getUsages().size());
    assertTrue(response.getAmount().compareTo(BigDecimal.ZERO) > 0);
    verify(dispenserRepository, times(1)).findById(dispenser.getId());
    verifyNoMoreInteractions(dispenserRepository);
    verify(usageRepository, times(1)).findByDispenserId(dispenser.getId());
    verifyNoMoreInteractions(usageRepository);
  }
}
