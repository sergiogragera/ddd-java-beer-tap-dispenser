package com.rviewer.skeletons.infrastructure.persistence;

import com.rviewer.skeletons.domain.dtos.DispenserRequest;
import com.rviewer.skeletons.domain.models.Dispenser;
import com.rviewer.skeletons.domain.persistence.DispenserRepository;
import com.rviewer.skeletons.infrastructure.persistence.entities.DispenserEntity;
import com.rviewer.skeletons.infrastructure.persistence.jpa.DispenserEntityJpaRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DispenserEntityRepository implements DispenserRepository {
  private final DispenserEntityJpaRepository dispenserRepository;

  @Autowired
  public DispenserEntityRepository(DispenserEntityJpaRepository dispenserEntityJpaRepository) {
    this.dispenserRepository = dispenserEntityJpaRepository;
  }

  @Override
  public Optional<Dispenser> save(DispenserRequest dispenser) {
    DispenserEntity dispenserEntity = new DispenserEntity(dispenser.getFlowVolume());
    dispenserEntity = dispenserRepository.save(dispenserEntity);

    return Optional.of(new Dispenser(dispenserEntity.getId(), dispenserEntity.getFlowVolume()));
  }

  @Override
  public Optional<Dispenser> findById(int id) {
    Optional<DispenserEntity> entity = dispenserRepository.findById(id);
    return Optional.of(new Dispenser(entity.get().getId(), entity.get().getFlowVolume()));
  }
}
