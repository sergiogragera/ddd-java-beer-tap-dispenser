package com.rviewer.skeletons.infrastructure.persistence.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dispenser")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class DispenserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date openedAt;

    @Column
    private float flowVolume;

    public DispenserEntity(float flowVolume) {
        this.flowVolume = flowVolume;
    }
}
