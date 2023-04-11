package com.rviewer.skeletons.infrastructure.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rviewer.skeletons.application.services.SpendingService;
import com.rviewer.skeletons.domain.dtos.response.SpendingResponse;
import com.rviewer.skeletons.domain.dtos.response.UsageResponse;
import com.rviewer.skeletons.domain.models.Dispenser;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = SpendingController.class)
public class SpendingControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private SpendingService service;

  @Test
  public void itShouldReturnSpendings() throws Exception {
    final var id = UUID.randomUUID();
    final var usage = new UsageResponse(new Dispenser(BigDecimal.valueOf(0.4)));
    final var spendings = new SpendingResponse(List.of(usage));
    when(service.getSpendings(id)).thenReturn(spendings);

    mockMvc
        .perform(get(String.format("/dispenser/%s/spendings", id)))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.amount").exists());
  }
}
