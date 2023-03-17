package com.rviewer.skeletons.infrastructure.persistence.adapters;

import com.rviewer.skeletons.domain.models.Dispenser;
import com.rviewer.skeletons.domain.models.valueobjects.Status;
import com.rviewer.skeletons.infrastructure.persistence.entities.DispenserEntity;
import java.util.Optional;

public class DispenserAdapter {

  public static Optional<Dispenser> fromEntityToModel(
      Optional<DispenserEntity> optionalDispenserEntity) {
    return optionalDispenserEntity.map(
        (dispenserEntity) -> {
          Status status =
              new Status(
                  DateAdapter.convertToLocalDateTimeViaInstant(dispenserEntity.getOpenedAt()));
          return new Dispenser(dispenserEntity.getId(), dispenserEntity.getFlowVolume(), status);
        });
  }

  public static Optional<DispenserEntity> fromModelToEntity(Optional<Dispenser> optionalDispenser) {
    return optionalDispenser.map(
        (dispenser) -> {
          DispenserEntity dispenserEntity =
              new DispenserEntity(dispenser.getId().getValue(), dispenser.getFlowVolume());
          if (dispenser.getStatus().isOpened()) {
            dispenserEntity.setOpenedAt(
                DateAdapter.convertToDateViaInstant(dispenser.getStatus().getOpenedAt()));
          }
          return dispenserEntity;
        });
  }
}
