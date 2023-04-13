package com.rviewer.skeletons.application.services;

import com.rviewer.skeletons.domain.dtos.response.SpendingResponse;
import com.rviewer.skeletons.domain.dtos.response.UsageResponse;
import com.rviewer.skeletons.domain.exceptions.DispenserNotFoundException;
import com.rviewer.skeletons.domain.models.Dispenser;
import com.rviewer.skeletons.domain.models.Usage;
import com.rviewer.skeletons.domain.persistence.DispenserRepository;
import com.rviewer.skeletons.domain.persistence.UsageRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpendingService {
  @Autowired DispenserRepository dispenserRepository;
  @Autowired UsageRepository usageRepository;

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
