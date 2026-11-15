package com.ProjectSpace.database.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_usuario")
    private UUID usuarioId;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Column(name = "foto_perfil")
    private String fotoPerfil;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Builder.Default
    @OneToMany(
            mappedBy = "usuario",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<ProjetoEntity> projetos = new HashSet<>();

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuarios_roles",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_role")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    @PrePersist
    void preencherDataDeCriacao() {
        if (criadoEm == null) {
            criadoEm = LocalDateTime.now();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public @Nullable String getPassword() {
        return senhaHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public final boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (objeto == null || classeEfetiva(this) != classeEfetiva(objeto)) {
            return false;
        }
        UsuarioEntity outro = (UsuarioEntity) objeto;
        return usuarioId != null && Objects.equals(usuarioId, outro.usuarioId);
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
