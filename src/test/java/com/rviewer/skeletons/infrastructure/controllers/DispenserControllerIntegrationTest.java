package com.rviewer.skeletons.infrastructure.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rviewer.skeletons.application.services.DispenserService;
import com.rviewer.skeletons.domain.dtos.request.DispenserRequest;
import com.rviewer.skeletons.domain.dtos.request.StatusRequest;
import com.rviewer.skeletons.domain.dtos.request.StatusRequest.Status;
import com.rviewer.skeletons.domain.dtos.response.DispenserResponse;
import com.rviewer.skeletons.domain.dtos.response.SpendingResponse;
import com.rviewer.skeletons.domain.dtos.response.UsageResponse;
import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyClosedException;
import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyOpenedException;
import com.rviewer.skeletons.domain.exceptions.DispenserClosedAfterOpenException;
import com.rviewer.skeletons.domain.exceptions.DispenserNotFoundException;
import com.rviewer.skeletons.domain.models.Dispenser;

@SpringBootTest
@AutoConfigureMockMvc
public class DispenserControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private DispenserService service;

  @ParameterizedTest
  @ValueSource(floats = {-1, -0.01f, 0.00f, 0})
  public void itShouldReturnBadRequestWhenFlowVolumeIsNotPositive(float flowVolume)
      throws Exception {
    final var dispenser = new DispenserRequest(flowVolume);

    mockMvc
        .perform(
            post("/dispenser")
                .content(asJsonString(dispenser))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void itShouldCreateDispenser() throws Exception {
    final var dispenser = new DispenserRequest(0.4f);
    when(service.create(any(DispenserRequest.class)))
        .thenReturn(new DispenserResponse(UUID.randomUUID(), dispenser.getFlowVolume()));

    mockMvc
        .perform(
            post("/dispenser")
                .content(asJsonString(dispenser))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
  }

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
  public void itShouldReturnOkWhenOpenDispenser() throws Exception {
    final var status = new StatusRequest(Status.open, LocalDateTime.now());
    doNothing().when(service).open(any(), any());

    mockMvc
        .perform(
            put(String.format("/dispenser/%s/status", UUID.randomUUID()))
                .content(asJsonString(status))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
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
        .andExpect(status().isOk());
  }

  @Test
  public void itShouldReturnSpendings() throws Exception {
    final var id = UUID.randomUUID();
    final var usage = new UsageResponse(new Dispenser(0.4f));
    final var spendings = new SpendingResponse(List.of(usage));
    when(service.getSpendings(id)).thenReturn(spendings);

    mockMvc
        .perform(get(String.format("/dispenser/%s/spendings", id)))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.amount").exists());
  }

  private static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
