package com.sebastianjamardo.sistema_gestion.repository;

import com.sebastianjamardo.sistema_gestion.model.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Query methods de Spring Data
    List<Producto> findByActivoTrue();

    Optional<Producto> findByIdAndActivoTrue(Long id);

    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);

    List<Producto> findByCategoriaAndActivoTrue(String categoria);

    @Query("SELECT p FROM Producto p WHERE p.stock <= p.stockMinimo AND p.activo = true")
        List<Producto> findProductosConBajoStock();

    boolean existsByNombreIgnoreCaseAndActivoTrue(String nombre);
}