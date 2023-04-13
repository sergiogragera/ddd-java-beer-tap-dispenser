package com.rviewer.skeletons.infrastructure.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rviewer.skeletons.application.services.DispenserService;
import com.rviewer.skeletons.domain.dtos.request.DispenserRequest;
import com.rviewer.skeletons.domain.dtos.response.DispenserResponse;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = DispenserController.class)
public class DispenserControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private DispenserService service;

  @ParameterizedTest
  @ValueSource(strings = {"-1", "-0.01", "0.00", "0"})
  public void itShouldReturnBadRequestWhenFlowVolumeIsNotPositive(BigDecimal flowVolume)
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
    final var dispenser = new DispenserRequest(BigDecimal.valueOf(0.4));
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

  private static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
