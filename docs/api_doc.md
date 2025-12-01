# 🌐 Documentación de API REST

> **Sistema de Gestión Empresarial - API Reference**

## Tabla de Contenidos
- [Información General](#información-general)
- [Autenticación](#autenticación)
- [Códigos de Estado](#códigos-de-estado)
- [Endpoints](#endpoints)
- [Modelos de Datos](#modelos-de-datos)
- [Ejemplos de Uso](#ejemplos-de-uso)

---

## Información General

### Base URL
```
http://localhost:8080/api
```

### Content-Type
Todas las requests y responses usan JSON:
```
Content-Type: application/json
```

### Versionado
Actualmente en **v1** (implícito). Futuro versionado: `/api/v1/productos`

---

## Autenticación

### Fase 1 (Actual): Spring Security Básico
```http
Authorization: Basic base64(username:password)
```

### Fase 3 (Próximo): JWT
```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## Códigos de Estado HTTP

| Código | Significado | Uso |
|--------|-------------|-----|
| `200` | OK | Request exitoso (GET, PUT) |
| `201` | Created | Recurso creado exitosamente (POST) |
| `204` | No Content | Request exitoso sin contenido (DELETE) |
| `400` | Bad Request | Validación fallida |
| `401` | Unauthorized | No autenticado |
| `403` | Forbidden | No autorizado (autenticado pero sin permisos) |
| `404` | Not Found | Recurso no encontrado |
| `409` | Conflict | Conflicto (ej: nombre duplicado) |
| `500` | Internal Server Error | Error del servidor |

---

## Endpoints

### Productos 🚧 EN DESARROLLO

#### Listar todos los productos activos

```http
GET /api/productos
```

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "nombre": "Laptop Dell Inspiron",
    "descripcion": "15.6 pulgadas, 8GB RAM",
    "precio": 1500.00,
    "stock": 10,
    "stockMinimo": 5,
    "categoria": "Electrónica",
    "necesitaReposicion": false,
    "fechaCreacion": "2024-11-25T10:00:00",
    "fechaActualizacion": "2024-11-25T10:00:00"
  },
  {
    "id": 2,
    "nombre": "Mouse Logitech",
    "descripcion": "Inalámbrico, ergonómico",
    "precio": 25.50,
    "stock": 3,
    "stockMinimo": 20,
    "categoria": "Accesorios",
    "necesitaReposicion": true,
    "fechaCreacion": "2024-11-24T14:30:00",
    "fechaActualizacion": "2024-11-25T09:15:00"
  }
]
```

---

#### Obtener un producto por ID

```http
GET /api/productos/{id}
```

**Path Parameters:**
- `id` (Long, required): ID del producto

**Response `200 OK`:**
```json
{
  "id": 1,
  "nombre": "Laptop Dell Inspiron",
  "descripcion": "15.6 pulgadas, 8GB RAM",
  "precio": 1500.00,
  "stock": 10,
  "stockMinimo": 5,
  "categoria": "Electrónica",
  "necesitaReposicion": false,
  "fechaCreacion": "2024-11-25T10:00:00",
  "fechaActualizacion": "2024-11-25T10:00:00"
}
```

**Response `404 Not Found`:**
```json
{
  "timestamp": "2024-11-25T14:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Producto con ID 999 no encontrado",
  "path": "/api/productos/999"
}
```

---

#### Crear un producto

```http
POST /api/productos
```

**Request Body:**
```json
{
  "nombre": "Teclado Mecánico",
  "descripcion": "RGB, switches Cherry MX",
  "precio": 120.00,
  "stock": 15,
  "stockMinimo": 10,
  "categoria": "Accesorios"
}
```

**Validaciones:**
- `nombre`: Obligatorio, 3-100 caracteres
- `descripcion`: Opcional, máximo 500 caracteres
- `precio`: Obligatorio, mayor a 0.01, máximo 2 decimales
- `stock`: Obligatorio, no negativo
- `stockMinimo`: Obligatorio, no negativo
- `categoria`: Opcional, máximo 50 caracteres

**Response `201 Created`:**
```json
{
  "id": 3,
  "nombre": "Teclado Mecánico",
  "descripcion": "RGB, switches Cherry MX",
  "precio": 120.00,
  "stock": 15,
  "stockMinimo": 10,
  "categoria": "Accesorios",
  "necesitaReposicion": false,
  "fechaCreacion": "2024-11-25T14:45:00",
  "fechaActualizacion": "2024-11-25T14:45:00"
}
```

**Response `400 Bad Request` (validación fallida):**
```json
{
  "timestamp": "2024-11-25T14:30:00",
  "status": 400,
  "error": "Bad Request",
  "errors": [
    {
      "field": "nombre",
      "message": "El nombre es obligatorio"
    },
    {
      "field": "precio",
      "message": "El precio debe ser mayor a 0"
    }
  ],
  "path": "/api/productos"
}
```

**Response `409 Conflict` (nombre duplicado):**
```json
{
  "timestamp": "2024-11-25T14:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "Ya existe un producto con el nombre 'Laptop Dell Inspiron'",
  "path": "/api/productos"
}
```

---

#### Actualizar un producto

```http
PUT /api/productos/{id}
```

**Path Parameters:**
- `id` (Long, required): ID del producto a actualizar

**Request Body:**
```json
{
  "nombre": "Laptop Dell Inspiron 15",
  "descripcion": "15.6 pulgadas, 16GB RAM, 512GB SSD",
  "precio": 1800.00,
  "stock": 8,
  "stockMinimo": 5,
  "categoria": "Electrónica"
}
```

**Response `200 OK`:**
```json
{
  "id": 1,
  "nombre": "Laptop Dell Inspiron 15",
  "descripcion": "15.6 pulgadas, 16GB RAM, 512GB SSD",
  "precio": 1800.00,
  "stock": 8,
  "stockMinimo": 5,
  "categoria": "Electrónica",
  "necesitaReposicion": false,
  "fechaCreacion": "2024-11-25T10:00:00",
  "fechaActualizacion": "2024-11-25T15:00:00"
}
```

**Response `404 Not Found`:**
```json
{
  "timestamp": "2024-11-25T15:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Producto con ID 999 no encontrado",
  "path": "/api/productos/999"
}
```

---

#### Eliminar un producto (Soft Delete)

```http
DELETE /api/productos/{id}
```

**Path Parameters:**
- `id` (Long, required): ID del producto a eliminar

**Response `204 No Content`:**
Sin contenido. El producto se marcó como `activo = false`.

**Response `404 Not Found`:**
```json
{
  "timestamp": "2024-11-25T15:10:00",
  "status": 404,
  "error": "Not Found",
  "message": "Producto con ID 999 no encontrado",
  "path": "/api/productos/999"
}
```

**Nota:** El producto NO se elimina físicamente. Solo se marca como inactivo y no aparecerá en futuras consultas.

---

#### Buscar productos por nombre

```http
GET /api/productos/buscar?nombre={query}
```

**Query Parameters:**
- `nombre` (String, required): Texto a buscar (case-insensitive)

**Ejemplo:**
```http
GET /api/productos/buscar?nombre=laptop
```

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "nombre": "Laptop Dell Inspiron",
    "descripcion": "...",
    "precio": 1500.00,
    "stock": 10,
    "stockMinimo": 5,
    "categoria": "Electrónica",
    "necesitaReposicion": false,
    "fechaCreacion": "2024-11-25T10:00:00",
    "fechaActualizacion": "2024-11-25T10:00:00"
  },
  {
    "id": 4,
    "nombre": "Laptop HP Pavilion",
    "descripcion": "...",
    "precio": 1200.00,
    "stock": 5,
    "stockMinimo": 3,
    "categoria": "Electrónica",
    "necesitaReposicion": false,
    "fechaCreacion": "2024-11-24T12:00:00",
    "fechaActualizacion": "2024-11-24T12:00:00"
  }
]
```

---

#### Productos con bajo stock

```http
GET /api/productos/bajo-stock
```

Devuelve productos donde `stock <= stockMinimo`.

**Response `200 OK`:**
```json
[
  {
    "id": 2,
    "nombre": "Mouse Logitech",
    "descripcion": "Inalámbrico, ergonómico",
    "precio": 25.50,
    "stock": 3,
    "stockMinimo": 20,
    "categoria": "Accesorios",
    "necesitaReposicion": true,
    "fechaCreacion": "2024-11-24T14:30:00",
    "fechaActualizacion": "2024-11-25T09:15:00"
  }
]
```

**Uso:** Alertar al administrador sobre productos que necesitan reposición.

---

## Modelos de Datos

### ProductoRequest (Request DTO)

```json
{
  "nombre": "string (3-100 chars, required)",
  "descripcion": "string (max 500 chars, optional)",
  "precio": "decimal (>0.01, 2 decimals, required)",
  "stock": "integer (>=0, required)",
  "stockMinimo": "integer (>=0, required)",
  "categoria": "string (max 50 chars, optional)"
}
```

### ProductoResponse (Response DTO)

```json
{
  "id": "long",
  "nombre": "string",
  "descripcion": "string",
  "precio": "decimal",
  "stock": "integer",
  "stockMinimo": "integer",
  "categoria": "string",
  "necesitaReposicion": "boolean",
  "fechaCreacion": "datetime (ISO 8601)",
  "fechaActualizacion": "datetime (ISO 8601)"
}
```

### ErrorResponse

```json
{
  "timestamp": "datetime (ISO 8601)",
  "status": "integer (HTTP status code)",
  "error": "string (error type)",
  "message": "string (error description)",
  "path": "string (request path)"
}
```

### ValidationErrorResponse

```json
{
  "timestamp": "datetime (ISO 8601)",
  "status": 400,
  "error": "Bad Request",
  "errors": [
    {
      "field": "string (field name)",
      "message": "string (validation message)"
    }
  ],
  "path": "string (request path)"
}
```

---

## Ejemplos de Uso

### cURL

#### Crear un producto
```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Monitor LG 24 pulgadas",
    "descripcion": "Full HD, IPS",
    "precio": 250.00,
    "stock": 20,
    "stockMinimo": 10,
    "categoria": "Electrónica"
  }'
```

#### Listar productos
```bash
curl -X GET http://localhost:8080/api/productos
```

#### Obtener producto por ID
```bash
curl -X GET http://localhost:8080/api/productos/1
```

#### Actualizar producto
```bash
curl -X PUT http://localhost:8080/api/productos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Monitor LG 27 pulgadas",
    "descripcion": "4K, IPS, HDR",
    "precio": 400.00,
    "stock": 15,
    "stockMinimo": 8,
    "categoria": "Electrónica"
  }'
```

#### Eliminar producto
```bash
curl -X DELETE http://localhost:8080/api/productos/1
```

---

### JavaScript (Fetch API)

```javascript
// Crear producto
const crearProducto = async () => {
  const response = await fetch('http://localhost:8080/api/productos', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      nombre: 'Auriculares Sony',
      descripcion: 'Noise cancelling, Bluetooth',
      precio: 150.00,
      stock: 12,
      stockMinimo: 5,
      categoria: 'Audio'
    })
  });
  
  if (response.ok) {
    const producto = await response.json();
    console.log('Producto creado:', producto);
  } else {
    const error = await response.json();
    console.error('Error:', error);
  }
};

// Listar productos con bajo stock
const productosBajoStock = async () => {
  const response = await fetch('http://localhost:8080/api/productos/bajo-stock');
  const productos = await response.json();
  
  productos.forEach(p => {
    console.log(`⚠️ ${p.nombre}: ${p.stock} unidades (mínimo: ${p.stockMinimo})`);
  });
};
```

---

### Postman Collection

Importa esta colección en Postman:

```json
{
  "info": {
    "name": "Sistema Gestión - API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Productos",
      "item": [
        {
          "name": "Listar productos",
          "request": {
            "method": "GET",
            "url": "{{baseUrl}}/productos"
          }
        },
        {
          "name": "Crear producto",
          "request": {
            "method": "POST",
            "url": "{{baseUrl}}/productos",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"nombre\": \"Producto Test\",\n  \"precio\": 100.00,\n  \"stock\": 10,\n  \"stockMinimo\": 5\n}"
            }
          }
        }
      ]
    }
  ],
  "variable": [
    {
      "key": "baseUrl",
      "value": "http://localhost:8080/api"
    }
  ]
}
```

---

## Rate Limiting (Fase 3)

```http
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 95
X-RateLimit-Reset: 1638360000
```

- **Límite:** 100 requests por hora por IP
- **Headers de respuesta** indican límites y resets

---

## Paginación (Fase 2)

```http
GET /api/productos?page=0&size=20&sort=nombre,asc
```

**Response:**
```json
{
  "content": [...],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": true,
      "unsorted": false
    }
  },
  "totalPages": 5,
  "totalElements": 100,
  "last": false,
  "first": true
}
```

---

## Swagger UI (Fase 3)

Documentación interactiva disponible en:
```
http://localhost:8080/swagger-ui.html
```

---

## Versionado de API (Futuro)

### Estrategia: URL Versioning

```
/api/v1/productos  (actual)
/api/v2/productos  (futuro)
```

**Compatibilidad:** v1 se mantendrá hasta que todos los clientes migren a v2.

---

## Referencias

- [REST API Best Practices](https://www.restapitutorial.com/)
- [HTTP Status Codes](https://httpstatuses.com/)
- [JSON API Specification](https://jsonapi.org/)

---

**Última actualización:** 25 de noviembre 2024  
**Autor:** Sebastián Jamardo