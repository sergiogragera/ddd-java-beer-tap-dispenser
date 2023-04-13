package com.rviewer.skeletons.application.services;

import com.rviewer.skeletons.domain.exceptions.DispenserNotFoundException;
import com.rviewer.skeletons.domain.models.Dispenser;
import com.rviewer.skeletons.domain.persistence.DispenserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusService {
  @Autowired DispenserRepository dispenserRepository;

  public void open(UUID id, Optional<LocalDateTime> updatedAt) {
    Dispenser dispenser =
        dispenserRepository.findById(id).orElseThrow(() -> new DispenserNotFoundException());
    dispenser.open(updatedAt.orElse(LocalDateTime.now()));
    dispenserRepository.save(dispenser);
  }

  @Transactional
  public void close(UUID id, Optional<LocalDateTime> updatedAt) {
    Dispenser dispenser =
        dispenserRepository.findById(id).orElseThrow(() -> new DispenserNotFoundException());
    dispenser.close(updatedAt.orElse(LocalDateTime.now()));
    dispenserRepository.save(dispenser);
  }
}
