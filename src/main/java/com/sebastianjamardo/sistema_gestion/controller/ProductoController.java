package com.sebastianjamardo.sistema_gestion.controller;

import com.sebastianjamardo.sistema_gestion.dto.request.ProductoRequest;
import com.sebastianjamardo.sistema_gestion.dto.response.ProductoResponse;
import com.sebastianjamardo.sistema_gestion.model.entity.Producto;
import com.sebastianjamardo.sistema_gestion.service.ProductoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService services;

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        ProductoResponse response = services.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long id) {
        ProductoResponse response = services.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> ListarTodos() {
        List<ProductoResponse> productos = services.listarTodos();
        return ResponseEntity.ok(productos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(
        @PathVariable long id,
        @Valid @RequestBody ProductoRequest request) {
        ProductoResponse response = services.actualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/limpiar-todo")
    public ResponseEntity<String> limpiarTodo() {
        services.limpiarTodo();  // ← Delegamos al Service
        return ResponseEntity.ok("✅ Base de datos limpia");
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoResponse>> buscarPorNombre (@RequestParam String nombre) {
        List<ProductoResponse> productos = services.buscarPorNombre(nombre);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/bajo-stock")
    public ResponseEntity<List<ProductoResponse>> obtenerProductosConBajoStock() {
        List<ProductoResponse> productos = services.obtenerProductosConBajoStock();
        return ResponseEntity.ok(productos);    
    }
}
