package com.ProjectSpace.database.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tarefas")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TarefaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_tarefa")
    private UUID tarefaId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_projeto", nullable = false)
    private ProjetoEntity projeto;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String prioridade;

    @Column(name = "data_limite")
    private LocalDate dataLimite;

    @Column(name = "concluida_em")
    private LocalDateTime concluidaEm;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void preencherDataDeCriacao() {
        if (criadoEm == null) {
            criadoEm = LocalDateTime.now();
        }
    }

    @Override
    public final boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (objeto == null || classeEfetiva(this) != classeEfetiva(objeto)) {
            return false;
        }
        TarefaEntity outro = (TarefaEntity) objeto;
        return tarefaId != null && Objects.equals(tarefaId, outro.tarefaId);
    }

    @Override
    public final int hashCode() {
        return classeEfetiva(this).hashCode();
    }

    private static Class<?> classeEfetiva(Object objeto) {
        if (objeto instanceof HibernateProxy proxy) {
            return proxy.getHibernateLazyInitializer().getPersistentClass();
        }
        return objeto.getClass();
    }
}
