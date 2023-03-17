package com.rviewer.skeletons.infrastructure.persistence.jpa;

import com.rviewer.skeletons.infrastructure.persistence.entities.UsageEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsageEntityJpaRepository extends JpaRepository<UsageEntity, Integer> {
  List<UsageEntity> findByDispenser_Id(int id);
}
