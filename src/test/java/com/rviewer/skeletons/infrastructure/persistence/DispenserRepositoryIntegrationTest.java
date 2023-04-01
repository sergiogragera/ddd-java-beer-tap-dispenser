package com.rviewer.skeletons.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rviewer.skeletons.domain.models.Dispenser;
import com.rviewer.skeletons.domain.persistence.DispenserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

@AutoConfigureTestEntityManager
@DataJpaTest(
    includeFilters =
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
public class DispenserRepositoryIntegrationTest {

  @Autowired private TestEntityManager entityManager;
  @Autowired private DispenserRepository dispenserEntityRepository;

  @Test
  void itShouldSaveDispenser() {
    final var dispenser = dispenserEntityRepository.save(new Dispenser(BigDecimal.valueOf(0.5)));
    assertNotNull(dispenser);

    final var dispenserEntity = entityManager.find(Dispenser.class, dispenser.getId());
    assertEquals(BigDecimal.valueOf(0.5), dispenserEntity.getFlowVolume());
  }

  @Test
  void itShouldSaveOpenedDispenser() {
    final var now = LocalDateTime.now();

    var dispenserEntity = entityManager.merge(new Dispenser(BigDecimal.valueOf(0.5)));

    dispenserEntity.open(Optional.of(now));
    final var dispenser = dispenserEntityRepository.save(dispenserEntity);
    assertNotNull(dispenser);

    dispenserEntity = entityManager.find(Dispenser.class, dispenser.getId());
    assertEquals(BigDecimal.valueOf(0.5), dispenserEntity.getFlowVolume());
    assertEquals(now, dispenserEntity.getStatus().getOpenedAt());
  }

  @Test
  void itShouldSaveClosedDispenser() {
    var dispenserEntity = new Dispenser(BigDecimal.valueOf(0.5));
    dispenserEntity.open(Optional.of(LocalDateTime.now()));
    dispenserEntity = entityManager.merge(dispenserEntity);

    dispenserEntity.close(Optional.of(LocalDateTime.now()));

    final var dispenser = dispenserEntityRepository.save(dispenserEntity);
    assertNotNull(dispenser);

    dispenserEntity = entityManager.find(Dispenser.class, dispenser.getId());
    assertEquals(BigDecimal.valueOf(0.5), dispenserEntity.getFlowVolume());
    assertFalse(dispenserEntity.isOpened());
  }

  @Test
  void itShouldFindDispenserById() {
    final var dispenserEntity = entityManager.merge(new Dispenser(BigDecimal.valueOf(0.5)));

    final var dispenser = dispenserEntityRepository.findById(dispenserEntity.getId());
    assertTrue(dispenser.isPresent());
    assertEquals(dispenserEntity.getFlowVolume(), dispenser.get().getFlowVolume());
  }
}
