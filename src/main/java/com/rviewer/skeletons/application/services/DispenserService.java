package com.rviewer.skeletons.application.services;

import com.rviewer.skeletons.domain.dtos.request.DispenserRequest;
import com.rviewer.skeletons.domain.dtos.response.DispenserResponse;
import com.rviewer.skeletons.domain.models.Dispenser;
import com.rviewer.skeletons.domain.persistence.DispenserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DispenserService {
  @Autowired DispenserRepository dispenserRepository;

  public DispenserResponse create(DispenserRequest dispenserRequest) {
    Dispenser dispenser = dispenserRepository.save(new Dispenser(dispenserRequest.getFlowVolume()));
    return new DispenserResponse(dispenser.getId(), dispenser.getFlowVolume());
  }
}
