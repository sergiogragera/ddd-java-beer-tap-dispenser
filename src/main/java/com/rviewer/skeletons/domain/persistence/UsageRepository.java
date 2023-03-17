package com.rviewer.skeletons.domain.persistence;

import com.rviewer.skeletons.domain.dtos.UsageResponse;
import com.rviewer.skeletons.domain.models.Dispenser;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UsageRepository {
  Optional<Dispenser> save(Dispenser dispenser);

  List<UsageResponse> findByDispenserId(int id);
}
