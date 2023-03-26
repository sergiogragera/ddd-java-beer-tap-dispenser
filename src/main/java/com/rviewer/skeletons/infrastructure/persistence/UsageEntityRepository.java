package com.rviewer.skeletons.infrastructure.persistence;

import com.rviewer.skeletons.domain.models.Usage;
import com.rviewer.skeletons.domain.models.valueobjects.Status;
import com.rviewer.skeletons.domain.persistence.UsageRepository;
import com.rviewer.skeletons.infrastructure.persistence.jpa.UsageEntityJpaRepository;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UsageEntityRepository implements UsageRepository {
  private final UsageEntityJpaRepository usageRepository;

  @Autowired
  public UsageEntityRepository(UsageEntityJpaRepository dispenserEntityJpaRepository) {
    this.usageRepository = dispenserEntityJpaRepository;
  }

  @Override
  public Usage save(Usage usage) {
    return usageRepository.save(usage);
  }

  @Override
  public List<Usage> findByDispenserId(UUID id) {
    return usageRepository.findByDispenser_Id(id);
  }
}
