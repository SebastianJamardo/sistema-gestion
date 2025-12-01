# 🏛️ Arquitectura del Sistema

> **Sistema de Gestión Empresarial - Backend API REST**

## Tabla de Contenidos
- [Visión General](#visión-general)
- [Arquitectura en Capas](#arquitectura-en-capas)
- [Flujo de Datos](#flujo-de-datos)
- [Patrones de Diseño](#patrones-de-diseño)
- [Tecnologías](#tecnologías)

---

## Visión General

Este sistema sigue una **arquitectura en capas (Layered Architecture)** con separación clara de responsabilidades. El objetivo es crear un sistema mantenible, testeable y escalable.

### Principios Arquitectónicos

1. **Separación de Responsabilidades** - Cada capa tiene un propósito único
2. **Inversión de Dependencias** - Las capas superiores no dependen de implementaciones concretas
3. **Clean Architecture** - Lógica de negocio independiente de frameworks
4. **API First** - El contrato de la API es estable e independiente del modelo de dominio

---

## Arquitectura en Capas

```
┌─────────────────────────────────────────────────────────┐
│                    CLIENTE (Frontend)                    │
│            (Web, Mobile, Postman, etc.)                 │
└─────────────────────────────────────────────────────────┘
                          ↓ HTTP/REST
┌─────────────────────────────────────────────────────────┐
│                  CONTROLLER LAYER                        │
│  • Recibe requests HTTP                                 │
│  • Valida entrada (Bean Validation)                     │
│  • Delega al Service                                    │
│  • Retorna Response HTTP                                │
│                                                          │
│  Clases: ProductoController, ClienteController          │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│                   SERVICE LAYER                          │
│  • Lógica de negocio                                    │
│  • Validaciones complejas                               │
│  • Orquestación entre repositorios                      │
│  • Conversión DTO ↔ Entity                              │
│  • Transacciones                                        │
│                                                          │
│  Clases: ProductoService, ClienteService                │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│                 REPOSITORY LAYER                         │
│  • Acceso a datos                                       │
│  • Queries a la base de datos                           │
│  • Abstracción de persistencia                          │
│                                                          │
│  Interfaces: ProductoRepository, ClienteRepository      │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│                   DATABASE (MySQL)                       │
│  • Persistencia de datos                                │
│  • Transacciones ACID                                   │
└─────────────────────────────────────────────────────────┘
```

---

## Detalle de Cada Capa

### 1. Controller Layer (Presentación)

**Responsabilidades:**
- Exponer endpoints REST
- Validar formato de entrada (JSON válido, tipos correctos)
- Mapear excepciones a códigos HTTP
- NO contiene lógica de negocio

**Ejemplo:**
```java
@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    
    @Autowired
    private ProductoService service;
    
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(
        @Valid @RequestBody ProductoRequest request
    ) {
        ProductoResponse response = service.crear(request);
        return ResponseEntity.status(201).body(response);
    }
}
```

**Anotaciones clave:**
- `@RestController` - Define un controlador REST
- `@RequestMapping` - Mapea la URL base
- `@Valid` - Activa validaciones del DTO
- `ResponseEntity` - Control completo sobre la respuesta HTTP

---

### 2. Service Layer (Lógica de Negocio)

**Responsabilidades:**
- Implementar reglas de negocio
- Validaciones complejas (cruzadas, dependientes del contexto)
- Coordinar múltiples repositorios si es necesario
- Gestionar transacciones
- Convertir entre DTOs y Entidades

**Ejemplo:**
```java
@Service
public class ProductoService {
    
    @Autowired
    private ProductoRepository repository;
    
    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        // 1. Validar reglas de negocio
        validarNombreUnico(request.getNombre());
        
        // 2. Convertir DTO → Entidad
        Producto producto = convertirAEntidad(request);
        
        // 3. Guardar
        Producto guardado = repository.save(producto);
        
        // 4. Convertir Entidad → Response
        return convertirAResponse(guardado);
    }
}
```

**Anotaciones clave:**
- `@Service` - Define un servicio de Spring
- `@Transactional` - Gestiona transacciones automáticamente
- `@Autowired` - Inyección de dependencias

---

### 3. Repository Layer (Acceso a Datos)

**Responsabilidades:**
- Abstracción del acceso a la base de datos
- Queries personalizados
- NO contiene lógica de negocio

**Ejemplo:**
```java
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Query methods automáticos
    List<Producto> findByActivoTrue();
    
    // Queries personalizados con JPQL
    @Query("SELECT p FROM Producto p WHERE p.stock <= p.stockMinimo")
    List<Producto> findProductosConBajoStock();
}
```

**Ventajas:**
- Spring genera la implementación automáticamente
- Código declarativo (qué, no cómo)
- Fácil de testear con mocks

---

### 4. Model/Domain Layer (Entidades)

**Responsabilidades:**
- Representar el dominio del negocio
- Lógica de dominio (métodos que operan sobre la entidad misma)
- Validaciones a nivel de dominio

**Ejemplo:**
```java
@Entity
@Table(name = "productos")
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private Integer stock;
    
    // Lógica de dominio
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
```

**Principio clave:** La entidad "sabe" cómo manejarse a sí misma.

---

## Flujo de Datos Completo

### Ejemplo: Crear un Producto

```
1. Cliente envía POST /api/productos
   {
     "nombre": "Laptop Dell",
     "precio": 1500.00,
     "stock": 10,
     "stockMinimo": 5
   }

2. ProductoController recibe el request
   - @Valid valida el ProductoRequest
   - Delega a ProductoService

3. ProductoService aplica lógica de negocio
   - Valida que no exista un producto con ese nombre
   - Convierte ProductoRequest → Producto (entidad)
   - Setea valores por defecto (activo = true, fechas)

4. ProductoRepository guarda en la base de datos
   - JPA genera: INSERT INTO productos (...)
   - MySQL asigna ID auto-incremental

5. ProductoService recibe la entidad guardada
   - Convierte Producto → ProductoResponse
   - Calcula campos derivados (necesitaReposicion)

6. ProductoController retorna 201 CREATED
   {
     "id": 5,
     "nombre": "Laptop Dell",
     "precio": 1500.00,
     "stock": 10,
     "stockMinimo": 5,
     "necesitaReposicion": false,
     "fechaCreacion": "2024-11-25T14:30:00"
   }
```

---

## Patrones de Diseño Implementados

### 1. Repository Pattern
**Problema:** Acoplar lógica de negocio con acceso a datos hace el código difícil de testear y mantener.

**Solución:** Abstraer el acceso a datos en interfaces (repositories).

**Beneficio:** 
- Lógica de negocio independiente de la base de datos
- Fácil de testear con mocks
- Puedes cambiar la BD sin tocar el Service

---

### 2. DTO Pattern (Data Transfer Object)
**Problema:** Exponer entidades directamente causa:
- Lazy loading exceptions
- Exposición de datos internos
- Acoplamiento del contrato API con el modelo de dominio

**Solución:** DTOs separados para entrada (Request) y salida (Response).

**Beneficio:**
- Contrato API estable
- Control sobre qué expones
- Versionado fácil

---

### 3. Dependency Injection
**Problema:** Crear objetos manualmente causa alto acoplamiento.

**Solución:** Spring gestiona la creación e inyección de dependencias.

**Beneficio:**
- Bajo acoplamiento
- Fácil de testear
- Código más limpio

---

### 4. Builder Pattern
**Problema:** Constructores con muchos parámetros son difíciles de leer y mantener.

**Solución:** Pattern Builder con Lombok `@Builder`.

```java
Producto p = Producto.builder()
    .nombre("Laptop")
    .precio(new BigDecimal("1500"))
    .stock(10)
    .build();
```

---

## Tecnologías por Capa

### Controller Layer
- Spring Web (`@RestController`, `@RequestMapping`)
- Bean Validation (`@Valid`, `@NotBlank`)
- Jackson (serialización JSON automática)

### Service Layer
- Spring Core (`@Service`, `@Autowired`)
- Spring Transaction (`@Transactional`)

### Repository Layer
- Spring Data JPA (`JpaRepository`)
- Hibernate (implementación de JPA)

### Model Layer
- JPA/Hibernate (`@Entity`, `@Column`)
- Lombok (`@Getter`, `@Setter`, `@Builder`)

### Database
- MySQL 8.0
- HikariCP (connection pool)

---

## Escalabilidad Futura

### Horizontal Scaling
- Stateless API (JWT en Fase 3)
- Sin sesiones en memoria
- Puedes levantar múltiples instancias detrás de un load balancer

### Vertical Scaling
- Connection pooling configurado (HikariCP)
- Queries optimizados con índices
- Caché en Fase 3 (Redis)

### Microservicios (Fase 4)
La arquitectura actual facilita dividir en microservicios:
- ProductoService → Microservicio de Inventario
- ClienteService → Microservicio de CRM
- VentaService → Microservicio de Ventas

---

## Diagrama de Componentes

```
┌────────────────────────────────────────────────────────┐
│                   Spring Boot Application               │
│                                                          │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐   │
│  │ Controllers │  │  Services   │  │ Repositories│   │
│  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘   │
│         │                 │                 │          │
│         └────────┬────────┴────────┬────────┘          │
│                  │                 │                   │
│         ┌────────▼─────────────────▼────────┐          │
│         │      Spring Application Context   │          │
│         │    (Dependency Injection Container)│          │
│         └───────────────────────────────────┘          │
│                                                          │
│  ┌──────────────────────────────────────────────────┐  │
│  │           Spring Data JPA / Hibernate            │  │
│  └─────────────────────┬────────────────────────────┘  │
└────────────────────────┼───────────────────────────────┘
                         │
                         ▼
              ┌──────────────────┐
              │   MySQL Database │
              └──────────────────┘
```

---

## Próximos Pasos Arquitectónicos

### Fase 2: Relaciones entre Entidades
- `@OneToMany` Cliente → Ventas
- `@ManyToOne` Venta → Cliente
- `@ManyToMany` Venta → Productos (con tabla intermedia)

### Fase 3: Seguridad
- Capa de seguridad con Spring Security
- JWT Filter intercepta requests
- AuthenticationManager valida credenciales

### Fase 4: Observabilidad
- Actuator endpoints para métricas
- Logging estructurado
- Health checks

---

## Referencias

- [Spring Framework Documentation](https://spring.io/projects/spring-framework)
- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Domain-Driven Design](https://martinfowler.com/bliki/DomainDrivenDesign.html)

---

**Última actualización:** 25 de noviembre 2024  
**Autor:** Sebastián Jamardo