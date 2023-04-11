package com.rviewer.skeletons.domain.listeners;

import com.rviewer.skeletons.domain.events.DispenserClosedEvent;
import com.rviewer.skeletons.domain.models.Usage;
import com.rviewer.skeletons.domain.persistence.UsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class DispenserClosedListener {
  @Autowired UsageRepository usageRepository;

  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void archiveUsage(DispenserClosedEvent dispenserClosed) {
    Usage usage = new Usage(dispenserClosed.getDispenser());
    usageRepository.save(usage);
  }
}
