package com.ProjectSpace.database.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_role")
    private UUID roleId;

    @Column(unique = true, nullable = false)
    private String nome;

    @Builder.Default
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<UsuarioEntity> usuarios = new HashSet<>();

    @Override
    public @Nullable String getAuthority() {
        return nome;
    }

    @Override
    public final boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (objeto == null || classeEfetiva(this) != classeEfetiva(objeto)) {
            return false;
        }
        RoleEntity outro = (RoleEntity) objeto;
        return roleId != null && Objects.equals(roleId, outro.roleId);
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
