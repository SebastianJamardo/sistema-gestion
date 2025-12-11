package com.sebastianjamardo.sistema_gestion.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private Integer stockMinimo;
    private String categoria;
    private Boolean necesitaReposicion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
