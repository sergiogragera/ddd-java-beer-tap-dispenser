package com.rviewer.skeletons.infrastructure.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rviewer.skeletons.application.services.StatusService;
import com.rviewer.skeletons.domain.dtos.request.StatusRequest;
import com.rviewer.skeletons.domain.dtos.request.StatusRequest.Status;
import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyClosedException;
import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyOpenedException;
import com.rviewer.skeletons.domain.exceptions.DispenserClosedAfterOpenException;
import com.rviewer.skeletons.domain.exceptions.DispenserNotFoundException;
import com.rviewer.skeletons.domain.exceptions.DispenserOpenedAfterCloseException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = StatusController.class)
public class StatusControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private StatusService service;

  @Test
  public void itShouldReturnNotFoundWhenDispenserNotExists() throws Exception {
    final var status = new StatusRequest(Status.open, LocalDateTime.now());
    doThrow(DispenserNotFoundException.class).when(service).open(any(), any());

    mockMvc
        .perform(
            put(String.format("/dispenser/%s/status", UUID.randomUUID()))
                .content(asJsonString(status))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  public void itShouldReturnConflictWhenDispenserAlreadyOpened() throws Exception {
    final var status = new StatusRequest(Status.open, LocalDateTime.now());
    doThrow(DispenserAlreadyOpenedException.class).when(service).open(any(), any());

    mockMvc
        .perform(
            put(String.format("/dispenser/%s/status", UUID.randomUUID()))
                .content(asJsonString(status))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict());
  }

  @Test
  public void itShouldReturnConflictWhenDispenserAlreadyClosed() throws Exception {
    final var status = new StatusRequest(Status.close, LocalDateTime.now());
    doThrow(DispenserAlreadyClosedException.class).when(service).close(any(), any());

    mockMvc
        .perform(
            put(String.format("/dispenser/%s/status", UUID.randomUUID()))
                .content(asJsonString(status))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict());
  }

  @Test
  public void itShouldReturnConflictWhenDispenserClosedAfterOpen() throws Exception {
    final var status = new StatusRequest(Status.open, LocalDateTime.now());
    doThrow(DispenserClosedAfterOpenException.class).when(service).open(any(), any());

    mockMvc
        .perform(
            put(String.format("/dispenser/%s/status", UUID.randomUUID()))
                .content(asJsonString(status))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict());
  }

  @Test
  public void itShouldReturnConflictWhenDispenserOpenedAfterClose() throws Exception {
    final var status = new StatusRequest(Status.open, LocalDateTime.now());
    doThrow(DispenserOpenedAfterCloseException.class).when(service).open(any(), any());

    mockMvc
        .perform(
            put(String.format("/dispenser/%s/status", UUID.randomUUID()))
                .content(asJsonString(status))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict());
  }

  @Test
  public void itShouldReturnOkWhenOpenDispenser() throws Exception {
    final var status = new StatusRequest(Status.open, LocalDateTime.now());
    doNothing().when(service).open(any(), any());

    mockMvc
        .perform(
            put(String.format("/dispenser/%s/status", UUID.randomUUID()))
                .content(asJsonString(status))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isAccepted());
  }

  @Test
  public void itShouldReturnOkWhenCloseDispenser() throws Exception {
    final var status = new StatusRequest(Status.close, LocalDateTime.now());
    doNothing().when(service).close(any(), any());

    mockMvc
        .perform(
            put(String.format("/dispenser/%s/status", UUID.randomUUID()))
                .content(asJsonString(status))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isAccepted());
  }

  private static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
