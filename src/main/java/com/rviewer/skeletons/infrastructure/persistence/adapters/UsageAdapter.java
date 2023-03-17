package com.rviewer.skeletons.infrastructure.persistence.adapters;

import com.rviewer.skeletons.domain.dtos.UsageResponse;
import com.rviewer.skeletons.domain.models.Dispenser;
import com.rviewer.skeletons.infrastructure.persistence.entities.UsageEntity;
import java.util.Optional;

public class UsageAdapter {

  public static UsageResponse fromEntityToModel(UsageEntity usageEntity) {
    return new UsageResponse(
        DateAdapter.convertToLocalDateTimeViaInstant(usageEntity.getOpenedAt()),
        DateAdapter.convertToLocalDateTimeViaInstant(usageEntity.getClosedAt()),
        usageEntity.getFlowVolume(),
        usageEntity.getTotalSpent());
  }

  public static Optional<UsageEntity> fromModelToEntity(Optional<Dispenser> optionalDispenser) {
    if (optionalDispenser.isPresent()) {
      Dispenser dispenser = optionalDispenser.get();

      UsageEntity usageEntity = new UsageEntity(dispenser.getId().getValue());
      usageEntity.setOpenedAt(
          DateAdapter.convertToDateViaInstant(dispenser.getStatus().getOpenedAt()));
      usageEntity.setClosedAt(
          DateAdapter.convertToDateViaInstant(dispenser.getStatus().getClosedAt()));
      usageEntity.setFlowVolume(dispenser.getFlowVolume());
      usageEntity.setTotalSpent(dispenser.getTotalSpent());
      return Optional.of(usageEntity);
    }

    return Optional.empty();
  }
}
