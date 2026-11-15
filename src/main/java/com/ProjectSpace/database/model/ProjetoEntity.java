package com.ProjectSpace.database.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "projetos")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjetoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_projeto")
    private UUID projetoId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String prioridade;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    private Integer progresso;

    @Column(name = "repositorio_url")
    private String repositorioUrl;

    @Column(name = "demo_url")
    private String demoUrl;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Builder.Default
    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TarefaEntity> tarefas = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LinkEntity> links = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AnotacaoEntity> anotacoes = new HashSet<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "projeto_tags",
            joinColumns = @JoinColumn(name = "id_projeto"),
            inverseJoinColumns = @JoinColumn(name = "id_tag")
    )
    private Set<TagEntity> tags = new HashSet<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "projeto_tecnologias",
            joinColumns = @JoinColumn(name = "id_projeto"),
            inverseJoinColumns = @JoinColumn(name = "id_tecnologia")
    )
    private Set<TecnologiaEntity> tecnologias = new HashSet<>();

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
        ProjetoEntity outro = (ProjetoEntity) objeto;
        return projetoId != null && Objects.equals(projetoId, outro.projetoId);
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
