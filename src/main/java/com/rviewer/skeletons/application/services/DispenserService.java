package com.rviewer.skeletons.application.services;

import com.rviewer.skeletons.domain.dtos.request.DispenserRequest;
import com.rviewer.skeletons.domain.dtos.response.DispenserResponse;
import com.rviewer.skeletons.domain.dtos.response.SpendingResponse;
import com.rviewer.skeletons.domain.dtos.response.UsageResponse;
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
    Optional<Dispenser> dispenserById = dispenserRepository.findById(id);
    dispenserById.ifPresentOrElse(
        dispenser -> {
          dispenser.open(updatedAt);
          dispenserRepository.save(dispenser);
        },
        () -> {
          throw new RuntimeException();
        });
  }

  @Transactional
  public void close(UUID id, Optional<LocalDateTime> updatedAt) {
    Optional<Dispenser> dispenserById = dispenserRepository.findById(id);
    dispenserById.ifPresentOrElse(
        dispenser -> {
          Usage usage = dispenser.close(updatedAt);
          dispenserRepository.save(dispenser);
          usageRepository.save(usage);
        },
        () -> {
          throw new RuntimeException();
        });
  }

  public SpendingResponse getUsages(UUID id) {
    List<UsageResponse> usagesResponse = new ArrayList<>();

    Optional<Dispenser> dispenserById = this.dispenserRepository.findById(id);
    dispenserById.ifPresentOrElse(
        dispenser -> {
          List<Usage> usages = usageRepository.findByDispenserId(id);
          usages.stream()
              .map((usage) -> new UsageResponse(usage))
              .collect(Collectors.toCollection(() -> usagesResponse));
          if (dispenser.isOpened()) {
            usagesResponse.add(new UsageResponse(dispenser));
          }
        },
        () -> {
          throw new RuntimeException("dispenser not found");
        });

    return new SpendingResponse(usagesResponse);
  }
}
