package com.rviewer.skeletons.domain.persistence;

import com.rviewer.skeletons.domain.models.Dispenser;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface DispenserRepository {

  Dispenser save(Dispenser dispenser);

  Optional<Dispenser> findById(UUID id);
}
