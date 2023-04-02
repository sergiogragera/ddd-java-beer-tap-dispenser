package com.rviewer.skeletons.application.services;

import com.rviewer.skeletons.domain.dtos.request.DispenserRequest;
import com.rviewer.skeletons.domain.dtos.response.DispenserResponse;
import com.rviewer.skeletons.domain.dtos.response.SpendingResponse;
import com.rviewer.skeletons.domain.dtos.response.UsageResponse;
import com.rviewer.skeletons.domain.exceptions.DispenserNotFoundException;
import com.rviewer.skeletons.domain.models.Dispenser;
import com.rviewer.skeletons.domain.models.Usage;
import com.rviewer.skeletons.domain.persistence.DispenserRepository;
import com.rviewer.skeletons.domain.persistence.UsageRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DispenserService {
  @Autowired DispenserRepository dispenserRepository;
  @Autowired UsageRepository usageRepository;

  public DispenserResponse create(DispenserRequest dispenserRequest) {
    Dispenser dispenser = dispenserRepository.save(new Dispenser(dispenserRequest.getFlowVolume()));
    return new DispenserResponse(dispenser.getId(), dispenser.getFlowVolume());
  }

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
    usageRepository.save(usage);
  }

  public SpendingResponse getSpendings(UUID id) {
    List<UsageResponse> usagesResponse = new ArrayList<>();

    Dispenser dispenser =
        this.dispenserRepository.findById(id).orElseThrow(() -> new DispenserNotFoundException());
    List<Usage> usages = usageRepository.findByDispenserId(id);
    usages.stream()
        .map((usage) -> new UsageResponse(usage))
        .collect(Collectors.toCollection(() -> usagesResponse));
    if (dispenser.isOpened()) {
      usagesResponse.add(new UsageResponse(dispenser));
    }

    return new SpendingResponse(usagesResponse);
  }
}
