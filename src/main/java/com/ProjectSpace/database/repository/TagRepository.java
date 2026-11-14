package com.ProjectSpace.database.repository;

import com.ProjectSpace.database.model.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TagRepository extends JpaRepository<TagEntity, UUID> {

    Optional<TagEntity> findByNomeIgnoreCase(String nome);
}
