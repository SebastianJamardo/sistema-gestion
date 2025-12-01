package com.sebastianjamardo.sistema_gestion.model.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;
import org.springframework.cglib.core.Local;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name= "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo;

    @Column(length = 50)
    private String categoria;

    @Column(name ="Activo", nullable = false)
    private boolean activo = true;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        fechaActualizacion = LocalDateTime.now();
    }

    // Métodos de negocio
    public boolean tieneStockDisponible(Integer cantidad) {
        return this.stock >= cantidad;
    }

    public void reducirStock(Integer cantidad) {
        if (!tieneStockDisponible(cantidad)) {
            throw new IllegalStateException("Stock insuficiente");
        }
        this.stock -= cantidad;
    }

    public boolean necesitaReposicion() {
        return this.stock <= this.stockMinimo;
    }
}

