package com.rviewer.skeletons.domain.models;

import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyClosedException;
import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyOpenedException;
import com.rviewer.skeletons.domain.exceptions.DispenserClosedAfterOpenException;
import com.rviewer.skeletons.domain.exceptions.InvalidArgumentException;
import com.rviewer.skeletons.domain.models.valueobjects.Status;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Getter;

@Entity
@Table(name = "dispenser")
@Getter
public class Dispenser {
  private final float PRICE_REFERENCE = 12.25f;

  @Id
  @GeneratedValue
  @Type(type = "org.hibernate.type.UUIDCharType")
  private UUID id = UUID.randomUUID();

  @Column(nullable = false, precision = 10, scale = 6)
  private BigDecimal flowVolume;

  @Embedded private Status status = new Status();

  protected Dispenser() {}

  public Dispenser(float flowVolume) {
    if (flowVolume <= 0) {
      throw new InvalidArgumentException("invalid flow volume: must be greater than zero");
    }
    this.flowVolume = new BigDecimal(flowVolume).setScale(6, RoundingMode.HALF_UP);
  }

  public float getFlowVolume() {
    return this.flowVolume.floatValue();
  }

  public boolean isOpened() {
    return this.status.isOpened();
  }

  public void open(Optional<LocalDateTime> date) {
    LocalDateTime openDate = date.orElse(LocalDateTime.now());
    if (this.isOpened()) {
      throw new DispenserAlreadyOpenedException();
    } else if (this.isClosedAfter(openDate)) {
      throw new DispenserClosedAfterOpenException();
    }
    this.status = new Status(openDate);
  }

  private boolean isClosedAfter(LocalDateTime date) {
    return this.status.getClosedAt() != null && this.status.getClosedAt().isAfter(date);
  }

  public Usage close(Optional<LocalDateTime> date) {
    if (!this.isOpened()) {
      throw new DispenserAlreadyClosedException();
    }
    this.status = new Status(this.status.getOpenedAt(), date.orElse(LocalDateTime.now()));
    return new Usage(this);
  }

  public float getLitersDispensed() {
    return BigDecimal.valueOf(this.status.getSecondsOpened())
        .multiply(this.flowVolume)
        .floatValue();
  }

  public float getTotalSpent() {
    return this.getLitersDispensed() * PRICE_REFERENCE;
  }
}
