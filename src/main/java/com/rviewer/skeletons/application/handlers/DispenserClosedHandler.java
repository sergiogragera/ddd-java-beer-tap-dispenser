package com.rviewer.skeletons.application.handlers;

import com.rviewer.skeletons.domain.events.DispenserClosedEvent;
import com.rviewer.skeletons.domain.models.Dispenser;
import com.rviewer.skeletons.domain.models.Usage;
import com.rviewer.skeletons.domain.persistence.UsageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class DispenserClosedHandler {
  Logger logger = LoggerFactory.getLogger(DispenserClosedHandler.class);

  @Autowired UsageRepository usageRepository;

  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void archiveUsage(DispenserClosedEvent dispenserClosed) {
    final Dispenser dispenser = dispenserClosed.getDispenser();
    logger.info(
        String.format(
            "Dispenser %s closed at %s", dispenser.getId(), dispenserClosed.getCreatedAt()));
    Usage usage = new Usage(dispenser);
    usageRepository.save(usage);
    logger.info(
        String.format(
            "Archive closed usage %s with spent of %s", usage.getId(), usage.getTotalSpent()));
  }
}
