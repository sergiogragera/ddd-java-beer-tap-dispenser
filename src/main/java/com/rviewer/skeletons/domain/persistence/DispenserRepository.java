package com.rviewer.skeletons.domain.persistence;

import com.rviewer.skeletons.domain.dtos.DispenserRequest;
import com.rviewer.skeletons.domain.models.Dispenser;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface DispenserRepository {

  Optional<Dispenser> save(DispenserRequest dispenser);

  Optional<Dispenser> findById(int id);
}
