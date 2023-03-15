package com.rviewer.skeletons.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import com.rviewer.skeletons.domain.dtos.DispenserRequest;
import com.rviewer.skeletons.domain.persistence.DispenserRepository;
import com.rviewer.skeletons.infrastructure.persistence.entities.DispenserEntity;

@AutoConfigureTestEntityManager
@DataJpaTest(
    includeFilters =
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
public class DispenserRepositoryIntegrationTest {

  @Autowired private TestEntityManager entityManager;
  @Autowired private DispenserRepository dispenserEntityRepository;

  @Test
  void itShouldSaveDispenser() {
    final var dispenser = dispenserEntityRepository.save(new DispenserRequest(0.4f));
    assertTrue(dispenser.isPresent());

    final var dispenserEntity =
        entityManager.find(DispenserEntity.class, dispenser.get().getId().getValue());
    assertEquals(0.4f, dispenserEntity.getFlowVolume());
  }

  @Test
  void itShouldFindDispneserById() {
    final var dispenserEntity = entityManager.persist(new DispenserEntity(0.5f));

    final var dispenser = dispenserEntityRepository.findById(dispenserEntity.getId());
    assertTrue(dispenser.isPresent());
    assertEquals(dispenserEntity.getFlowVolume(), dispenser.get().getFlowVolume());
  }
}
