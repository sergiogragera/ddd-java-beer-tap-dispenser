package com.rviewer.skeletons.domain.models;

import com.rviewer.skeletons.domain.models.valueobjects.Status;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Getter;

@Entity
@Table(name = "usage")
@Getter
public class Usage {
  @Id
  @GeneratedValue
  @Type(type = "org.hibernate.type.UUIDCharType")
  private UUID id;

  @Column(nullable = false, precision = 10, scale = 6)
  private BigDecimal flowVolume;

  @Column(nullable = false, precision = 10, scale = 6)
  private BigDecimal totalSpent;

  @Embedded private Status status;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "dispenser_id", nullable = false)
  private Dispenser dispenser;

  protected Usage() {}

  protected Usage(Dispenser dispenser) {
    this.dispenser = dispenser;
    if (dispenser.isOpened()) {
      throw new IllegalArgumentException("dispenser must be closed");
    }
    this.status = dispenser.getStatus();
    this.flowVolume = dispenser.getFlowVolume();
    this.totalSpent = dispenser.getTotalSpent();
  }
}
