package com.rviewer.skeletons.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rviewer.skeletons.domain.models.Dispenser;
import com.rviewer.skeletons.domain.models.Usage;
import com.rviewer.skeletons.domain.persistence.UsageRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
public class UsageRepositoryIntegrationTest {

  @Autowired private TestEntityManager entityManager;
  @Autowired private UsageRepository usageRepository;

  private Dispenser dispenser;

  @BeforeEach
  void setUp() {
    final var dispenser = new Dispenser(BigDecimal.valueOf(0.5));
    this.dispenser = entityManager.merge(dispenser);
  }

  @Test
  void itShouldSaveUsage() {
    dispenser.open(LocalDateTime.now());
    dispenser.close(LocalDateTime.now());

    final var savedUsage = usageRepository.save(new Usage(dispenser));
    assertNotNull(savedUsage);

    final var foundUsage = entityManager.find(Usage.class, savedUsage.getId());
    assertEquals(this.dispenser, foundUsage.getDispenser());
    assertEquals(BigDecimal.valueOf(0.5), foundUsage.getFlowVolume());
    assertNotNull(foundUsage.getStatus().getOpenedAt());
    assertNotNull(foundUsage.getStatus().getClosedAt());
  }

  @Test
  void itShouldFindOneUsageByDispenserId() {
    dispenser.open(LocalDateTime.now());
    dispenser.close(LocalDateTime.now());
    final var savedUsage = entityManager.merge(new Usage(dispenser));

    final var foundUsages = usageRepository.findByDispenserId(this.dispenser.getId());
    assertTrue(foundUsages.size() == 1);
    assertEquals(savedUsage, foundUsages.get(0));
  }

  @ParameterizedTest
  @ValueSource(ints = {2, 4, 10})
  void itShouldFindMultipleUsagesByDispenserId(int usages) {
    for (int i = 0; i < usages; i++) {
      dispenser.open(LocalDateTime.now());
      dispenser.close(LocalDateTime.now());
      entityManager.merge(new Usage(dispenser));
    }

    final var foundUsages = usageRepository.findByDispenserId(this.dispenser.getId());
    assertTrue(foundUsages.size() == usages);
  }
}
