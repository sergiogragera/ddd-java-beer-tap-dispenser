package com.rviewer.skeletons;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = {"pretty"},
    features = "src/test/resources/acceptance")
@WebAppConfiguration
public class DispenserAcceptanceTest {}
