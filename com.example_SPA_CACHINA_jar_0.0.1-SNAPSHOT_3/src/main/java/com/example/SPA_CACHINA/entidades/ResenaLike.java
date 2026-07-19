package com.example.SPA_CACHINA.entidades;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "resena_likes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"resena_id", "usuario_id"})
})
public class ResenaLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "resena_id", nullable = false)
    private Long resenaId;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    public ResenaLike() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResenaId() {
        return resenaId;
    }

    public void setResenaId(Long resenaId) {
        this.resenaId = resenaId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}
