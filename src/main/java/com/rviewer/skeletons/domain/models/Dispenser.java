package com.rviewer.skeletons.domain.models;

import java.time.LocalDateTime;

import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyClosedException;
import com.rviewer.skeletons.domain.exceptions.DispenserAlreadyOpenedException;
import com.rviewer.skeletons.domain.exceptions.InvalidArgumentException;
import com.rviewer.skeletons.domain.models.valueobjects.Id;
import com.rviewer.skeletons.domain.models.valueobjects.Status;

import lombok.Getter;

@Getter
public class Dispenser {
    private Id id;
    private LocalDateTime createdAt = LocalDateTime.now();
    private float flowVolume;
    private Status status = new Status();

    public Dispenser(int id, float flowVolume) {
        this(id, flowVolume, null, null);
    }

    public Dispenser(int id, float flowVolume, Status status) {
        this(id, flowVolume, status, null);
    }

    public Dispenser(int id, float flowVolume, Status status, LocalDateTime createdAt) {
        this.id = new Id(id);
        if (status != null) {
            this.status = status;
        }
        if (flowVolume <= 0) {
            throw new InvalidArgumentException("invalid flow volume: must be greater than zero");
        }
        this.flowVolume = flowVolume;
        if (createdAt != null) {
            this.createdAt = createdAt;
        }
    }

    public void open(LocalDateTime date) {
        if (date != null) {
            if (this.status.isOpened()) {
                throw new DispenserAlreadyOpenedException();
            }
            this.status = new Status(date);
        }
    }

    public void close(LocalDateTime date) {
        if (date != null) {
            if (!this.status.isOpened()) {
                throw new DispenserAlreadyClosedException();
            }
            this.status = new Status(this.status.getOpenedAt(), date);
        }
    }

    public float getLitersDispensed() {
        return this.status.getSecondsOpened() * this.flowVolume;
    }
}
