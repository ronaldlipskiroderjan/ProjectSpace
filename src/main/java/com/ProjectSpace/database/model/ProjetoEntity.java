package com.ProjectSpace.database.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjetoEntity {

    @Id
    private UUID projetoId;

    private UUID usuarioId;

    private String titulo;

    private String prioridade;

    private String status;

    private Set<TagEntity> tags = new HashSet<>();
}
