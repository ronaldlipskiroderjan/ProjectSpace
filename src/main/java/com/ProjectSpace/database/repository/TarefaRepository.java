package com.ProjectSpace.database.repository;

import com.ProjectSpace.database.model.TarefaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TarefaRepository extends JpaRepository<TarefaEntity, UUID> {

    List<TarefaEntity> findAllByProjeto_ProjetoId(UUID projetoId);
}
