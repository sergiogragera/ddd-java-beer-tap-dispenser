package com.rviewer.skeletons.infrastructure.controllers;

import com.rviewer.skeletons.application.services.SpendingService;
import com.rviewer.skeletons.domain.dtos.response.SpendingResponse;
import com.rviewer.skeletons.domain.exceptions.DispenserNotFoundException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/dispenser/{id}/spendings")
public class SpendingController {

  @Autowired private SpendingService spendingService;

  @GetMapping
  public SpendingResponse getSpendingsDispenser(@PathVariable UUID id) {
    try {
      return spendingService.getSpendings(id);
    } catch (DispenserNotFoundException ex) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "Requested dispenser does not exist", ex);
    }
  }
}
