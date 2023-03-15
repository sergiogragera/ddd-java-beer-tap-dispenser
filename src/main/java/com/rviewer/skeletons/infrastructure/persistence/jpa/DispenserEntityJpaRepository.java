package com.rviewer.skeletons.infrastructure.persistence.jpa;

import com.rviewer.skeletons.infrastructure.persistence.entities.DispenserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DispenserEntityJpaRepository extends JpaRepository<DispenserEntity, Integer> {

}
