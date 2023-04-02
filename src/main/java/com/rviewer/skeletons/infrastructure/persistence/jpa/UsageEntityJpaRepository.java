package com.rviewer.skeletons.infrastructure.persistence.jpa;

import com.rviewer.skeletons.domain.models.Usage;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsageEntityJpaRepository extends JpaRepository<Usage, UUID> {
  List<Usage> findByDispenser_Id(UUID id);
}
