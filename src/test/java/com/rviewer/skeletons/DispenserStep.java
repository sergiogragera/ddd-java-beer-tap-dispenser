package com.rviewer.skeletons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.type.TypeReference;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest
public class DispenserStep extends AbstractSteps {

  private static final String TEST_DISPENSER = "{\"flow_volume\": 0.5}";

  private static final String OPEN_STATUS = "{\"status\": \"open\"}";

  private static final String CLOSE_STATUS = "{\"status\": \"close\"}";

  private static final TypeReference<Map<String, Object>> RESOURCE_TYPE =
      new TypeReference<Map<String, Object>>() {};

  @Given("^a dispenser exists$")
  public void aDispenserExists() throws Throwable {
    createDispenser();
  }

  private void createDispenser() throws Exception {
    post("/dispenser", TEST_DISPENSER);
  }

  @When("^the user creates a dispenser$")
  public void theUserCreateADispenser() throws Throwable {
    createDispenser();
  }

  @And("^the user open a dispenser( (\\d+) seconds? ago)?$")
  public void theUserOpenADispenser(Integer secondsAgo) throws Throwable {
    String status = OPEN_STATUS;
    if (secondsAgo != null) {
      final var now = LocalDateTime.now();
      status =
          String.format(
              "{\"status\": \"open\", \"updated_at\": \"%s\"}",
              now.minusSeconds(secondsAgo.longValue()));
    }

    put("/dispenser/{id}/status", status, getCreatedId());
  }

  @And("^the user close a dispenser( (\\d+) seconds? ago)?$")
  public void theUserCloseADispenser(Integer secondsAgo) throws Throwable {
    String status = CLOSE_STATUS;
    if (secondsAgo != null) {
      final var now = LocalDateTime.now();
      status =
          String.format(
              "{\"status\": \"close\", \"updated_at\": \"%s\"}",
              now.minusSeconds(secondsAgo.longValue()));
    }

    put("/dispenser/{id}/status", status, getCreatedId());
  }

  private Object getCreatedId() throws Exception {
    return getLastPostContentAs(RESOURCE_TYPE).get("id");
  }

  @And("^the dispenser is successfully created$")
  public void theDispenserIsSuccessfullyCreated() {
    assertEquals(200, getLastPostResponse().getStatus());
  }

  @And("^the dispenser is successfully opened$")
  public void theDispenserIsSuccessfullyOpened() {
    assertEquals(202, getLastPutResponse().getStatus());
  }

  @And("^the dispenser is successfully closed$")
  public void theDispenserIsSuccessfullyClosed() {
    assertEquals(202, getLastPutResponse().getStatus());
  }

  @When("^the user gets the created dispenser spendings$")
  public void theUserRetrievesDispenserSpendings() throws Throwable {
    get("/dispenser/{id}/spendings", getCreatedId());
  }

  @Then("^the user receives status code of (\\d+)$")
  public void theUserReceivesStatusCodeOf(int statusCode) throws Throwable {
    assertEquals(statusCode, getLastStatusCode());
  }

  @And("^the retrieved dispenser is correct$")
  public void theRetrievedDispenserIsCorrect() throws Throwable {
    assertDispenserResourcesMatch(
        getLastPostContentAs(RESOURCE_TYPE), getLastGetContentAs(RESOURCE_TYPE));
  }

  @And("^the total amount is (equals to|greater than|less than) (\\d+|\\d+\\.\\d+)?$")
  public void theTotalAmountIs(String equality, BigDecimal amount) throws Throwable {
    switch (equality) {
      case "less than":
        assertTrue(
            amount.compareTo(
                    new BigDecimal(
                        String.valueOf(getLastGetContentAs(RESOURCE_TYPE).get("amount"))))
                > 0);
        break;
      case "greater than":
        assertTrue(
            amount.compareTo(
                    new BigDecimal(
                        String.valueOf(getLastGetContentAs(RESOURCE_TYPE).get("amount"))))
                < 0);
        break;
      case "equals to":
        assertTrue(
            amount.compareTo(
                    new BigDecimal(
                        String.valueOf(getLastGetContentAs(RESOURCE_TYPE).get("amount"))))
                == 0);
        break;
    }
  }

  @And("^exists (\\d+) usages?$")
  public void existsUsages(int times) throws Throwable {
    final var usages = (List) getLastGetContentAs(RESOURCE_TYPE).get("usages");
    assertEquals(times, usages.size());
  }

  private static void assertDispenserResourcesMatch(
      Map<String, Object> expected, Map<String, Object> actual) {
    assertEquals(expected.size(), actual.size());

    for (String key : expected.keySet()) {
      assertEquals(expected.get(key), actual.get(key));
    }
  }
}
