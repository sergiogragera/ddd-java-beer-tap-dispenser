package com.rviewer.skeletons.infrastructure.controllers;

import com.rviewer.skeletons.application.services.DispenserService;
import com.rviewer.skeletons.domain.dtos.DispenserRequest;
import com.rviewer.skeletons.domain.dtos.DispenserResponse;
import com.rviewer.skeletons.domain.dtos.SpendingResponse;
import com.rviewer.skeletons.domain.dtos.StatusRequest;
import com.rviewer.skeletons.infrastructure.persistence.adapters.DateAdapter;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/dispenser")
public class DispenserController {

  @Autowired private DispenserService dispenserService;

  @PostMapping
  public DispenserResponse createDispenser(@RequestBody DispenserRequest dispenserRequest) {
    return dispenserService.create(dispenserRequest);
  }

  @PutMapping("/{id}/status")
  public SpendingResponse updateStatusDispenser(@PathVariable int id, @RequestBody StatusRequest statusRequest) {
    if (statusRequest.getStatus().toString().equals("open")) {
        dispenserService.open(id, statusRequest.getUpdatedAt());
    }
    else {
        dispenserService.close(id, statusRequest.getUpdatedAt());
    }
    return dispenserService.getUsages(id);
  }
}
