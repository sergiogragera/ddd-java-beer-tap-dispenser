package com.rviewer.skeletons;

import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/acceptance")
@WebAppConfiguration
public class DispenserAcceptanceTest {}
