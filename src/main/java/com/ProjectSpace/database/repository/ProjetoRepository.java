package com.ProjectSpace.database.repository;

import com.ProjectSpace.database.model.ProjetoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjetoRepository extends JpaRepository<ProjetoEntity, UUID> {

    List<ProjetoEntity> findAllByUsuario_UsuarioId(UUID usuarioId);
}
