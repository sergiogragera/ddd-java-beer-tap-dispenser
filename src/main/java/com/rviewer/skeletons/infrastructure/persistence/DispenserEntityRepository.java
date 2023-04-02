package com.rviewer.skeletons.infrastructure.persistence;

import com.rviewer.skeletons.domain.models.Dispenser;
import com.rviewer.skeletons.domain.persistence.DispenserRepository;
import com.rviewer.skeletons.infrastructure.persistence.jpa.DispenserEntityJpaRepository;
import java.util.Optional;
import java.util.UUID;
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
  public Dispenser save(Dispenser dispenser) {
    return dispenserRepository.save(dispenser);
  }

  @Override
  public Optional<Dispenser> findById(UUID id) {
    return dispenserRepository.findById(id);
  }
}
