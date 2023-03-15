package com.rviewer.skeletons.domain.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.rviewer.skeletons.domain.dtos.DispenserRequest;
import com.rviewer.skeletons.domain.models.Dispenser;

@Repository
public interface DispenserRepository {

  Optional<Dispenser> save(DispenserRequest dispenser);

  Optional<Dispenser> findById(int id);
}
