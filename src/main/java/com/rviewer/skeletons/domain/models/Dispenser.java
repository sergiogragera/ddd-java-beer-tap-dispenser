package com.rviewer.skeletons.domain.models;

import java.time.LocalDateTime;

import com.rviewer.skeletons.domain.exceptions.InvalidArgumentException;

import lombok.Getter;

@Getter
public class Dispenser {
    private Id id;
    private LocalDateTime createdAt = LocalDateTime.now();
    private float flowVolume;

    public Dispenser(int id, float flowVolume) {
        this(id, flowVolume, null);
    }

    public Dispenser(int id, float flowVolume, LocalDateTime createdAt) {
        this.id = new Id(id);
        if (flowVolume <= 0) {
            throw new InvalidArgumentException("invalid flow volume: must be greater than zero");
        }
        this.flowVolume = flowVolume;
        if (createdAt != null) {
            this.createdAt = createdAt;
        }
    }
}
