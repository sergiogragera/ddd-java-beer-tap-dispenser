package com.rviewer.skeletons.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rviewer.skeletons.domain.models.Dispenser;
import com.rviewer.skeletons.domain.models.valueobjects.Status;
import com.rviewer.skeletons.domain.persistence.DispenserRepository;
import com.rviewer.skeletons.infrastructure.persistence.entities.DispenserEntity;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
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
    final var dispenser = dispenserEntityRepository.save(new Dispenser(0.4f));
    assertTrue(dispenser.isPresent());

    final var dispenserEntity =
        entityManager.find(DispenserEntity.class, dispenser.get().getId().getValue());
    assertEquals(0.4f, dispenserEntity.getFlowVolume());
  }

  @Test
  void itShouldSaveOpenedDispenser() {
    var dispenserEntity = entityManager.persist(new DispenserEntity(0.5f));

    final var status = new Status(LocalDateTime.now());
    final var dispenser =
        dispenserEntityRepository.save(new Dispenser(dispenserEntity.getId(), 0.4f, status));
    assertTrue(dispenser.isPresent());

    Date openedAt =
        Date.from(
            dispenser.get().getStatus().getOpenedAt().atZone(ZoneId.systemDefault()).toInstant());

    dispenserEntity = entityManager.find(DispenserEntity.class, dispenser.get().getId().getValue());
    assertEquals(0.4f, dispenserEntity.getFlowVolume());
    assertEquals(openedAt, dispenserEntity.getOpenedAt());
  }

  @Test
  void itShouldSaveClosedDispenser() {
    var dispenserEntity = new DispenserEntity(0.5f);
    dispenserEntity.setOpenedAt(new Date());
    dispenserEntity = entityManager.persist(dispenserEntity);

    final var status = new Status(LocalDateTime.now(), LocalDateTime.now().plusSeconds(10));
    final var dispenser =
        dispenserEntityRepository.save(new Dispenser(dispenserEntity.getId(), 0.4f, status));
    assertTrue(dispenser.isPresent());

    dispenserEntity = entityManager.find(DispenserEntity.class, dispenser.get().getId().getValue());
    assertEquals(0.4f, dispenserEntity.getFlowVolume());
    assertNull(dispenserEntity.getOpenedAt());
  }

  @Test
  void itShouldFindDispenserById() {
    final var dispenserEntity = entityManager.persist(new DispenserEntity(0.5f));

    final var dispenser = dispenserEntityRepository.findById(dispenserEntity.getId());
    assertTrue(dispenser.isPresent());
    assertEquals(dispenserEntity.getFlowVolume(), dispenser.get().getFlowVolume());
  }
}
