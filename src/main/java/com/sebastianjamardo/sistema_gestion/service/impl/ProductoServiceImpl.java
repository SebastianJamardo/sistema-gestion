package com.sebastianjamardo.sistema_gestion.service.impl;

import com.sebastianjamardo.sistema_gestion.exception.DuplicateResourceException;
import com.sebastianjamardo.sistema_gestion.exception.ResourceNotFoundException;
import com.sebastianjamardo.sistema_gestion.service.ProductoService;
import com.sebastianjamardo.sistema_gestion.repository.ProductoRepository;
import com.sebastianjamardo.sistema_gestion.model.entity.Producto;
import com.sebastianjamardo.sistema_gestion.dto.request.ProductoRequest;
import com.sebastianjamardo.sistema_gestion.dto.response.ProductoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository repository;

    @Override
    public ProductoResponse crear(ProductoRequest request) {
        // Validar que no exista un producto con ese nombre
        if(repository.existsByNombreIgnoreCaseAndActivoTrue(request.getNombre())) {
            throw new DuplicateResourceException("Ya existe un producto con el nombre: " + request.getNombre());
        }

        // 2. Convertir Request DTO → Entity
        Producto producto = Producto.builder()
                .nombre(request.getNombre())
                .description(request.getDescripcion())
                .precio(request.getPrecio())
                .stock(request.getStock())
                .stockMinimo(request.getStockMinimo())
                .categoria(request.getCategoria())
                .activo(true)
                .build();

        // 3. Guardar en base de datos
        Producto guardado = repository.save(producto);

        // 4. Convertir Entity → Response DTO
        return convertirAResponse(guardado);
    }

    private ProductoResponse convertirAResponse(Producto producto) {
        return ProductoResponse.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescription())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .stockMinimo(producto.getStockMinimo())
                .categoria(producto.getCategoria())
                .necesitaReposicion(producto.necesitaReposicion())  // ← Campo calculado
                .fechaCreacion(producto.getFechaCreacion())
                .fechaActualizacion(producto.getFechaActualizacion())
                .build();
    }

    @Override
    public ProductoResponse obtenerPorId(Long id) {
        Producto producto = repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                   "Producto con ID " + id + " no encontrado"
                ));

        return convertirAResponse(producto);
    }

    @Override
    public List<ProductoResponse> listarTodos() {
        return repository.findByActivoTrue()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        // 1. Verificar que existe
        Producto producto = repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto con ID " + id + " no encontrado"
                ));

        // 2. Actualizar camps
        producto.setNombre(request.getNombre());
        producto.setDescription(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setStockMinimo(request.getStockMinimo());
        producto.setCategoria(request.getCategoria());

        // 3. Guardar (JPA hace UPDATE automáticamente)
        Producto actualizado = repository.save(producto);

        // 4. Convertir y devolver
        return convertirAResponse(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto con Id " + id + " no encontrado"));

        producto.setActivo(false);
        repository.save(producto);
    }

    @Override
    public List<ProductoResponse> buscarPorNombre(String nombre) {
        return repository.findByNombreContainingIgnoreCaseAndActivoTrue(nombre)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    public List<ProductoResponse> obtenerProductosConBajoStock() {
        return repository.findProductosConBajoStock()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    public void limpiarTodo() {
        repository.deleteAll();
    }
}


