package com.rviewer.skeletons.infrastructure.persistence;

import com.rviewer.skeletons.domain.models.Dispenser;
import com.rviewer.skeletons.domain.persistence.DispenserRepository;
import com.rviewer.skeletons.infrastructure.persistence.adapters.DispenserAdapter;
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
  public Optional<Dispenser> save(Dispenser dispenser) {
    Optional<DispenserEntity> optionalDispenserEntity =
        DispenserAdapter.fromModelToEntity(Optional.ofNullable(dispenser));
    if (optionalDispenserEntity.isPresent()) {
      DispenserEntity dispenserEntity = dispenserRepository.save(optionalDispenserEntity.get());
      return DispenserAdapter.fromEntityToModel(Optional.ofNullable(dispenserEntity));
    }
    return Optional.empty();
  }

  @Override
  public Optional<Dispenser> findById(int id) {
    Optional<DispenserEntity> dispenserEntity = dispenserRepository.findById(id);
    return DispenserAdapter.fromEntityToModel(dispenserEntity);
  }
}
