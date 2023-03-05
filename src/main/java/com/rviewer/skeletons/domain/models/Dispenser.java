package com.rviewer.skeletons.domain.models;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class Dispenser {
    private Id id;
    private LocalDate createdAt = LocalDate.now();
    private float flowVolume;

    public Dispenser(int id, float flowVolume) {
        this(id, flowVolume, null);
    }

    public Dispenser(int id, float flowVolume, LocalDate createdAt) {
        this.id = new Id(id);
        if (flowVolume <= 0) {
            throw new RuntimeException();
        }
        this.flowVolume = flowVolume;
        if (createdAt != null) {
            this.createdAt = createdAt;
        }
    }
}
