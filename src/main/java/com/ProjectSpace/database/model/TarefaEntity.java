package com.ProjectSpace.database.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TarefaEntity {

    @Id
    private UUID tarefaId;

    private UUID projetoId;

    private String titulo;

    private String prioridade;

    private String status;

    private LocalDate dataLimite;
}
