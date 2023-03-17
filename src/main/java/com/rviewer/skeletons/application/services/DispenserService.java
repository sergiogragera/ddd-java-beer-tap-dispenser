package com.rviewer.skeletons.application.services;

import com.rviewer.skeletons.domain.dtos.DispenserRequest;
import com.rviewer.skeletons.domain.dtos.DispenserResponse;
import com.rviewer.skeletons.domain.dtos.SpendingResponse;
import com.rviewer.skeletons.domain.dtos.UsageResponse;
import com.rviewer.skeletons.domain.models.Dispenser;
import com.rviewer.skeletons.domain.models.valueobjects.Status;
import com.rviewer.skeletons.domain.persistence.DispenserRepository;
import com.rviewer.skeletons.domain.persistence.UsageRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DispenserService {
  @Autowired DispenserRepository dispenserRepository;
  @Autowired UsageRepository usageRepository;

  public DispenserResponse create(DispenserRequest dispenserRequest) {
    Optional<Dispenser> dispenserSaved =
        dispenserRepository.save(new Dispenser(dispenserRequest.getFlowVolume()));
    return dispenserSaved
        .map(
            dispenser ->
                new DispenserResponse(dispenser.getId().getValue(), dispenser.getFlowVolume()))
        .orElseThrow(() -> new RuntimeException("dispenser can't be created"));
  }

  public void open(int id, LocalDateTime date) {
    Optional<Dispenser> dispenserById = dispenserRepository.findById(id);
    dispenserById.ifPresentOrElse(
        dispenser -> {
          dispenser.open(date);
          dispenserRepository.save(dispenser);
        },
        () -> {
          throw new RuntimeException();
        });
  }

  public void close(int id, LocalDateTime date) {
    Optional<Dispenser> dispenserById = dispenserRepository.findById(id);
    dispenserById.ifPresentOrElse(
        dispenser -> {
          dispenser.close(date);
          dispenserRepository.save(dispenser);
          usageRepository.save(dispenser);
        },
        () -> {
          throw new RuntimeException();
        });
  }

  public SpendingResponse getUsages(int id) {
    SpendingResponse spending = new SpendingResponse();

    Optional<Dispenser> dispenserById = this.dispenserRepository.findById(id);
    dispenserById.ifPresentOrElse(
        dispenser -> {
          List<UsageResponse> usages = usageRepository.findByDispenserId(id);
          if (dispenser.getStatus().isOpened()) {
            Status dispenserStatus = dispenser.getStatus();
            usages.add(
                new UsageResponse(
                    dispenserStatus.getOpenedAt(),
                    null,
                    dispenser.getFlowVolume(),
                    dispenser.getTotalSpent()));
          }
          spending.getUsages().addAll(usages);
        },
        () -> {
          throw new RuntimeException("dispenser not found");
        });

    return spending;
  }
}
