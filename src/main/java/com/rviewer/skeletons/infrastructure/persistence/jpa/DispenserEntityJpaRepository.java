package com.rviewer.skeletons.infrastructure.persistence.jpa;

import com.rviewer.skeletons.domain.models.Dispenser;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DispenserEntityJpaRepository extends JpaRepository<Dispenser, UUID> {}
