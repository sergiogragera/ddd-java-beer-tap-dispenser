package com.rviewer.skeletons.infrastructure.persistence.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rviewer.skeletons.domain.models.Usage;

@Repository
public interface UsageEntityJpaRepository extends JpaRepository<Usage, UUID> {
  List<Usage> findByDispenser_Id(UUID id);
}
