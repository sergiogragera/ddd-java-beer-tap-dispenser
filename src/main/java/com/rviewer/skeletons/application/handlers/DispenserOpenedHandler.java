package com.rviewer.skeletons.application.handlers;

import com.rviewer.skeletons.domain.events.DispenserOpenedEvent;
import com.rviewer.skeletons.domain.models.Dispenser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DispenserOpenedHandler {
  Logger logger = LoggerFactory.getLogger(DispenserOpenedHandler.class);

  @EventListener
  public void archiveUsage(DispenserOpenedEvent dispenserClosed) {
    final Dispenser dispenser = dispenserClosed.getDispenser();
    logger.info(
        String.format(
            "Dispenser %s opened at %s", dispenser.getId(), dispenserClosed.getCreatedAt()));
  }
}