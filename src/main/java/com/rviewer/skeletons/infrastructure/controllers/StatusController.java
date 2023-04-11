package com.rviewer.skeletons.infrastructure.controllers;

import com.rviewer.skeletons.application.services.StatusService;
import com.rviewer.skeletons.domain.dtos.request.StatusRequest;
import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyClosedException;
import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyOpenedException;
import com.rviewer.skeletons.domain.exceptions.DispenserClosedAfterOpenException;
import com.rviewer.skeletons.domain.exceptions.DispenserNotFoundException;
import com.rviewer.skeletons.domain.exceptions.DispenserOpenedAfterCloseException;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/dispenser/{id}/status")
public class StatusController {

  @Autowired private StatusService statusService;

  @PutMapping
  @ResponseStatus(code = HttpStatus.ACCEPTED)
  public void updateStatusDispenser(
      @PathVariable UUID id, @Valid @RequestBody StatusRequest statusRequest) {
    try {
      if (statusRequest.getStatus().toString().equals("open")) {
        statusService.open(id, Optional.ofNullable(statusRequest.getUpdatedAt()));
      } else {
        statusService.close(id, Optional.ofNullable(statusRequest.getUpdatedAt()));
      }
    } catch (DispenserAlreadyOpenedException
        | DispenserAlreadyClosedException
        | DispenserClosedAfterOpenException
        | DispenserOpenedAfterCloseException ex) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage(), ex);
    } catch (DispenserNotFoundException ex) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "Requested dispenser does not exist", ex);
    }
  }
}
