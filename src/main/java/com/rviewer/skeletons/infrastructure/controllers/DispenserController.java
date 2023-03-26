package com.rviewer.skeletons.infrastructure.controllers;

import com.rviewer.skeletons.application.services.DispenserService;
import com.rviewer.skeletons.domain.dtos.request.DispenserRequest;
import com.rviewer.skeletons.domain.dtos.request.StatusRequest;
import com.rviewer.skeletons.domain.dtos.response.DispenserResponse;
import com.rviewer.skeletons.domain.dtos.response.SpendingResponse;
import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyClosedException;
import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyOpenedException;
import com.rviewer.skeletons.domain.exceptions.DispenserNotFoundException;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/dispenser")
public class DispenserController {

  @Autowired private DispenserService dispenserService;

  @PostMapping
  public DispenserResponse createDispenser(@Valid @RequestBody DispenserRequest dispenserRequest) {
    return dispenserService.create(dispenserRequest);
  }

  @PutMapping("/{id}/status")
  public void updateStatusDispenser(
      @PathVariable UUID id, @Valid @RequestBody StatusRequest statusRequest) {
    try {
      if (statusRequest.getStatus().toString().equals("open")) {
        dispenserService.open(id, statusRequest.getUpdatedAt());
      } else {
        dispenserService.close(id, statusRequest.getUpdatedAt());
      }
    } catch (DispenserAlreadyOpenedException | DispenserAlreadyClosedException ex) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage(), ex);
    } catch (DispenserNotFoundException ex) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "Requested dispenser does not exist", ex);
    }
  }

  @GetMapping("/{id}/spendings")
  public SpendingResponse getSpendingsDispenser(@PathVariable UUID id) {
    try {
      return dispenserService.getSpendings(id);
    } catch (DispenserNotFoundException ex) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "Requested dispenser does not exist", ex);
    }
  }
}
