package com.rviewer.skeletons.infrastructure.persistence.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usage")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UsageEntity {
  @Id private int id;

  @Temporal(TemporalType.TIMESTAMP)
  @Column
  private Date openedAt;

  @Temporal(TemporalType.TIMESTAMP)
  @Column
  private Date closedAt;

  @Column private float flowVolume;

  @Column private float totalSpent;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "dispenser_id", nullable = false)
  private DispenserEntity dispenser;

  public UsageEntity(int id, DispenserEntity dispenser) {
    this.id = id;
    this.dispenser = dispenser;
  }
}
