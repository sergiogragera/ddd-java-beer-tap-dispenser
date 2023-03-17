package com.rviewer.skeletons.infrastructure.persistence;

import com.rviewer.skeletons.domain.dtos.UsageResponse;
import com.rviewer.skeletons.domain.models.Dispenser;
import com.rviewer.skeletons.domain.persistence.UsageRepository;
import com.rviewer.skeletons.infrastructure.persistence.adapters.UsageAdapter;
import com.rviewer.skeletons.infrastructure.persistence.entities.UsageEntity;
import com.rviewer.skeletons.infrastructure.persistence.jpa.UsageEntityJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
  public Optional<Dispenser> save(Dispenser dispenser) {
    if (!dispenser.getStatus().isOpened()) {
      Optional<UsageEntity> usageEntity =
          UsageAdapter.fromModelToEntity(Optional.ofNullable(dispenser));
      if (usageEntity.isPresent()) {
        usageRepository.save(usageEntity.get());
        return Optional.of(dispenser);
      }
    }
    return Optional.empty();
  }

  @Override
  public List<UsageResponse> findByDispenserId(int id) {
    List<UsageEntity> usages = usageRepository.findByDispenser_Id(id);
    return usages.stream()
        .map((usage) -> UsageAdapter.fromEntityToModel(usage))
        .collect(Collectors.toList());
  }
}
