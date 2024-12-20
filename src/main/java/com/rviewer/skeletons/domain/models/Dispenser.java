package com.rviewer.skeletons.domain.models;

import com.rviewer.skeletons.domain.events.DispenserClosedEvent;
import com.rviewer.skeletons.domain.events.DispenserCreatedEvent;
import com.rviewer.skeletons.domain.events.DispenserOpenedEvent;
import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyClosedException;
import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyOpenedException;
import com.rviewer.skeletons.domain.exceptions.DispenserClosedAfterOpenException;
import com.rviewer.skeletons.domain.exceptions.DispenserOpenedAfterCloseException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Type;
import org.springframework.data.domain.AbstractAggregateRoot;

@Entity
@Table(name = "dispenser")
@Getter
public class Dispenser extends AbstractAggregateRoot<Dispenser> {
  public static final BigDecimal PRICE_REFERENCE = BigDecimal.valueOf(12.25);

  @Id
  @GeneratedValue
  @Type(type = "org.hibernate.type.UUIDCharType")
  private UUID id = UUID.randomUUID();

  @Column(nullable = false, precision = 10, scale = 6)
  private BigDecimal flowVolume;

  @Embedded private Status status = new Status();

  protected Dispenser() {}

  public Dispenser(BigDecimal flowVolume) {
    if (flowVolume.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("flow volume must be greater than zero");
    }
    this.flowVolume = flowVolume;
    registerEvent(new DispenserCreatedEvent(this));
  }

  public BigDecimal getFlowVolume() {
    return this.flowVolume;
  }

  public boolean isOpened() {
    return this.status.isOpened();
  }

  public void open(LocalDateTime openDate) {
    if (openDate == null) {
      throw new NullPointerException("open date must not be null");
    }
    if (this.isOpened()) {
      throw new DispenserAlreadyOpenedException();
    } else if (this.status.isClosedAfter(openDate)) {
      throw new DispenserClosedAfterOpenException();
    }
    this.status = new Status(openDate);
    registerEvent(new DispenserOpenedEvent(this));
  }

  public void close(LocalDateTime closeDate) {
    if (closeDate == null) {
      throw new NullPointerException("close date must not be null");
    }
    if (!this.isOpened()) {
      throw new DispenserAlreadyClosedException();
    } else if (this.status.isOpenedAfter(closeDate)) {
      throw new DispenserOpenedAfterCloseException();
    }
    this.status = new Status(this.status.getOpenedAt(), closeDate);
    registerEvent(new DispenserClosedEvent(this));
  }

  public BigDecimal getLitersDispensed() {
    return BigDecimal.valueOf(this.status.getSecondsOpened()).multiply(this.flowVolume);
  }

  public BigDecimal getTotalSpent() {
    return this.getLitersDispensed().multiply(PRICE_REFERENCE);
  }
}
