package com.rviewer.skeletons.application.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.rviewer.skeletons.domain.dtos.request.DispenserRequest;
import com.rviewer.skeletons.domain.models.Dispenser;
import com.rviewer.skeletons.domain.persistence.DispenserRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DispenserServiceUnitTest {

  @InjectMocks private DispenserService service;

  @Mock private DispenserRepository dispenserRepository;

  @Test
  void itShouldCreateDispenser() {
    final var dispenser = new Dispenser(BigDecimal.valueOf(0.5));
    when(dispenserRepository.save(any(Dispenser.class))).thenReturn(dispenser);

    final var dispenserResponse = service.create(new DispenserRequest(dispenser.getFlowVolume()));
    assertNotNull(dispenserResponse.getId());
    assertEquals(dispenser.getFlowVolume(), dispenserResponse.getFlowVolume());
  }
}
