package com.ProjectSpace.database.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "links_projeto")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_link")
    private UUID linkId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_projeto", nullable = false)
    private ProjetoEntity projeto;

    private String titulo;

    @Column(nullable = false)
    private String url;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Override
    public final boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (objeto == null || classeEfetiva(this) != classeEfetiva(objeto)) {
            return false;
        }
        LinkEntity outro = (LinkEntity) objeto;
        return linkId != null && Objects.equals(linkId, outro.linkId);
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
