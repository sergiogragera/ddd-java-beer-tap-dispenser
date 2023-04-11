package com.rviewer.skeletons.application.services;

import com.rviewer.skeletons.domain.exceptions.DispenserNotFoundException;
import com.rviewer.skeletons.domain.models.Dispenser;
import com.rviewer.skeletons.domain.models.Usage;
import com.rviewer.skeletons.domain.persistence.DispenserRepository;
import com.rviewer.skeletons.domain.persistence.UsageRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusService {
  @Autowired DispenserRepository dispenserRepository;
  @Autowired UsageRepository usageRepository;

  public void open(UUID id, Optional<LocalDateTime> updatedAt) {
    Dispenser dispenser =
        dispenserRepository.findById(id).orElseThrow(() -> new DispenserNotFoundException());
    dispenser.open(updatedAt);
    dispenserRepository.save(dispenser);
  }

  @Transactional
  public void close(UUID id, Optional<LocalDateTime> updatedAt) {
    Dispenser dispenser =
        dispenserRepository.findById(id).orElseThrow(() -> new DispenserNotFoundException());
    Usage usage = dispenser.close(updatedAt);
    dispenserRepository.save(dispenser);
    // TODO: move this op to event domain
    usageRepository.save(usage);
  }
}
