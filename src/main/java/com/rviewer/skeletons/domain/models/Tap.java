package com.rviewer.skeletons.domain.models;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import lombok.Getter;

public class Tap {
    @Getter
    private int id;
    private Optional<LocalDateTime> openedAt = Optional.ofNullable(null);
    private Optional<LocalDateTime> closedAt = Optional.ofNullable(null);
    @Getter
    private Dispenser dispenser;

    public Tap(int id, Dispenser dispenser) {
        this(id, dispenser, null, null);
    }

    public Tap(int id, Dispenser dispenser, LocalDateTime openedAt) {
        this(id, dispenser, openedAt, null);
    }

    public Tap(int id,Dispenser dispenser, LocalDateTime openedAt, LocalDateTime closedAt) {
        this.id = id;
        if (dispenser == null) {
            throw new RuntimeException();
        }
        this.dispenser = dispenser;

        this.open(Optional.ofNullable(openedAt));
        this.close(Optional.ofNullable(closedAt));
    }

    public boolean isOpened() {
        return this.openedAt.isPresent() && !this.closedAt.isPresent();
    }

    public void open(Optional<LocalDateTime> date) {
        if (date.isPresent()) {
            if (this.openedAt.isPresent()) {
                throw new RuntimeException();
            }
            this.openedAt = date;
        }
    }

    public void close(Optional<LocalDateTime> date) {
        if (date.isPresent()) {
            if (this.closedAt.isPresent()) {
                throw new RuntimeException();
            }
            else if (!this.openedAt.isPresent()) {
                throw new RuntimeException();
            }
            else if (!date.get().isAfter(this.openedAt.get())) {
                throw new RuntimeException();
            }
            this.closedAt = date;
        }
    }

    public float getLitersDispensed() {
        if (this.openedAt.isPresent()) {
            LocalDateTime start = this.openedAt.get();
            LocalDateTime end = LocalDateTime.now();
            if (this.closedAt.isPresent()) {
                end = this.closedAt.get();
            }
            return start.until(end, ChronoUnit.SECONDS) * this.dispenser.getFlowVolume();
        }
        return 0;
    }
}
