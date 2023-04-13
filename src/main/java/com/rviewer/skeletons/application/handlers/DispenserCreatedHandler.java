package com.rviewer.skeletons.application.handlers;

import com.rviewer.skeletons.domain.events.DispenserCreatedEvent;
import com.rviewer.skeletons.domain.models.Dispenser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DispenserCreatedHandler {
  Logger logger = LoggerFactory.getLogger(DispenserCreatedHandler.class);

  @EventListener
  public void archiveUsage(DispenserCreatedEvent dispenserClosed) {
    final Dispenser dispenser = dispenserClosed.getDispenser();
    logger.info(
        String.format(
            "Dispenser %s created at %s", dispenser.getId(), dispenserClosed.getCreatedAt()));
  }
}
