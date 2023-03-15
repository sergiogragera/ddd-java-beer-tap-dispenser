package com.rviewer.skeletons.application.services;

import com.rviewer.skeletons.domain.dtos.DispenserRequest;
import com.rviewer.skeletons.domain.dtos.DispenserResponse;
import com.rviewer.skeletons.domain.dtos.SpendingResponse;
import com.rviewer.skeletons.domain.dtos.UsageResponse;
import com.rviewer.skeletons.domain.models.Dispenser;
import com.rviewer.skeletons.domain.models.valueobjects.Status;
import com.rviewer.skeletons.domain.persistence.DispenserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DispenserService {
  @Autowired DispenserRepository dispenserRepository;

  public DispenserResponse create(DispenserRequest dispenserRequest) {
    Optional<Dispenser> dispenserSaved = dispenserRepository.save(dispenserRequest);
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
          // TODO: find last closed usage date equals zero
          dispenser.open(date);
          // TODO: save usage
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
          // TODO: save usage
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
          // TODO: findUsagesByDispenser
          if (dispenser.getStatus().isOpened()) {
            Status dispenserStatus = dispenser.getStatus();
            spending
                .getUsages()
                .add(
                    new UsageResponse(
                        dispenserStatus.getOpenedAt(),
                        dispenserStatus
                            .getOpenedAt()
                            .plusSeconds((long) dispenserStatus.getSecondsOpened()),
                        dispenser.getFlowVolume(),
                        dispenser.getLitersDispensed()
                            * 12 // FIXME: add getAmount in domain aggregate
                        ));
          }
        },
        () -> {
          throw new RuntimeException("dispenser not found");
        });

    return spending;
  }
}
