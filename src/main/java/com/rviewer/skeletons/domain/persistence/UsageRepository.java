package com.rviewer.skeletons.domain.persistence;

import com.rviewer.skeletons.domain.models.Usage;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface UsageRepository {
  Usage save(Usage usage);

  List<Usage> findByDispenserId(UUID id);
}
