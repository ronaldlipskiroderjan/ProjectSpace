package com.ProjectSpace.database.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tecnologias")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TecnologiaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_tecnologia")
    private UUID tecnologiaId;

    @Column(nullable = false)
    private String nome;

    private String categoria;

    @Column(name = "icone_url")
    private String iconeUrl;

    @Builder.Default
    @ManyToMany(mappedBy = "tecnologias")
    private Set<ProjetoEntity> projetos = new HashSet<>();

    @Override
    public final boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (objeto == null || classeEfetiva(this) != classeEfetiva(objeto)) {
            return false;
        }
        TecnologiaEntity outro = (TecnologiaEntity) objeto;
        return tecnologiaId != null && Objects.equals(tecnologiaId, outro.tecnologiaId);
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
