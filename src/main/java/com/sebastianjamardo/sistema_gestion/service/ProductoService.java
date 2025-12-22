package com.sebastianjamardo.sistema_gestion.service;

import com.sebastianjamardo.sistema_gestion.dto.request.ProductoRequest;
import com.sebastianjamardo.sistema_gestion.dto.response.ProductoResponse;

import java.util.List;

public interface ProductoService {

    ProductoResponse crear(ProductoRequest request);

    ProductoResponse obtenerPorId(Long id);

    List<ProductoResponse> listarTodos();

    ProductoResponse actualizar(Long id, ProductoRequest request);

    void eliminar(Long id);

    List<ProductoResponse> buscarPorNombre(String nombre);

    List<ProductoResponse> obtenerProductosConBajoStock();

    void limpiarTodo();
}