package com.ProjectSpace.database.repository;

import com.ProjectSpace.database.model.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LinkRepository extends JpaRepository<LinkEntity, UUID> {

    List<LinkEntity> findAllByProjetoId(UUID projetoId);
}
