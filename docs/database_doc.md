# 🗃️ Modelo de Datos y Esquema de Base de Datos

> **Sistema de Gestión Empresarial - Documentación de Base de Datos**

## Tabla de Contenidos
- [Visión General](#visión-general)
- [Diagrama ER](#diagrama-er)
- [Tablas](#tablas)
- [Índices](#índices)
- [Convenciones](#convenciones)

---

## Visión General

### Motor de Base de Datos
- **MySQL 8.0**
- **Character Set:** utf8mb4
- **Collation:** utf8mb4_unicode_ci

### Configuración de Conexión
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gestion_db
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

---

## Diagrama ER (Entity-Relationship)

### Fase 1 (Actual)

```
┌─────────────────┐
│   PRODUCTOS     │
├─────────────────┤
│ id (PK)         │
│ nombre          │
│ descripcion     │
│ precio          │
│ stock           │
│ stock_minimo    │
│ categoria       │
│ activo          │
│ fecha_creacion  │
│ fecha_actualizacion │
└─────────────────┘
```

### Fase 2 (Próximo)

```
┌─────────────────┐         ┌─────────────────┐
│   CLIENTES      │         │     VENTAS      │
├─────────────────┤         ├─────────────────┤
│ id (PK)         │◄────────│ id (PK)         │
│ nombre          │    1:N  │ cliente_id (FK) │
│ email           │         │ fecha           │
│ telefono        │         │ total           │
│ direccion       │         │ estado          │
│ activo          │         │ activo          │
└─────────────────┘         └────────┬────────┘
                                     │
                                     │ N:M
                                     │
┌─────────────────┐         ┌────────▼────────┐
│   PRODUCTOS     │         │  VENTA_ITEMS    │
├─────────────────┤         ├─────────────────┤
│ id (PK)         │◄────────│ id (PK)         │
│ nombre          │    1:N  │ venta_id (FK)   │
│ precio          │         │ producto_id (FK)│
│ stock           │         │ cantidad        │
│ ...             │         │ precio_unitario │
└─────────────────┘         └─────────────────┘
```

---

## Tablas

### 1. productos ✅ IMPLEMENTADA

Almacena información de los productos del inventario.

#### Estructura

| Columna | Tipo | Constraints | Descripción |
|---------|------|-------------|-------------|
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Identificador único |
| `nombre` | VARCHAR(100) | NOT NULL | Nombre del producto |
| `descripcion` | VARCHAR(500) | NULL | Descripción detallada |
| `precio` | DECIMAL(10,2) | NOT NULL | Precio unitario |
| `stock` | INT | NOT NULL | Cantidad en inventario |
| `stock_minimo` | INT | NOT NULL | Stock mínimo para alerta |
| `categoria` | VARCHAR(50) | NULL | Categoría del producto |
| `activo` | BOOLEAN | NOT NULL, DEFAULT TRUE | Soft delete flag |
| `fecha_creacion` | DATETIME | NOT NULL | Timestamp de creación |
| `fecha_actualizacion` | DATETIME | NOT NULL | Timestamp de última modificación |

#### SQL de Creación

```sql
CREATE TABLE productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500),
    precio DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL,
    stock_minimo INT NOT NULL,
    categoria VARCHAR(50),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion DATETIME NOT NULL,
    fecha_actualizacion DATETIME NOT NULL,
    
    INDEX idx_nombre (nombre),
    INDEX idx_categoria (categoria),
    INDEX idx_activo (activo),
    INDEX idx_bajo_stock (stock, stock_minimo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

#### Reglas de Negocio

1. **Soft Delete**: Los productos nunca se eliminan físicamente. Se marca `activo = false`.
2. **Stock Mínimo**: Cuando `stock <= stock_minimo`, el producto necesita reposición.
3. **Precio**: Siempre es positivo (validado en capa de aplicación).
4. **Auditoría**: Las fechas se gestionan automáticamente con `@PrePersist` y `@PreUpdate`.

#### Ejemplos de Datos

```sql
INSERT INTO productos (nombre, descripcion, precio, stock, stock_minimo, categoria, activo, fecha_creacion, fecha_actualizacion)
VALUES 
    ('Laptop Dell Inspiron', '15.6 pulgadas, 8GB RAM, 256GB SSD', 1500.00, 10, 5, 'Electrónica', TRUE, NOW(), NOW()),
    ('Mouse Logitech', 'Inalámbrico, ergonómico', 25.50, 50, 20, 'Accesorios', TRUE, NOW(), NOW()),
    ('Teclado Mecánico', 'RGB, switches Cherry MX', 120.00, 3, 10, 'Accesorios', TRUE, NOW(), NOW());
```

---

### 2. clientes 🔜 FASE 2

Almacena información de los clientes.

#### Estructura Planeada

| Columna | Tipo | Constraints | Descripción |
|---------|------|-------------|-------------|
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Identificador único |
| `nombre` | VARCHAR(100) | NOT NULL | Nombre completo |
| `email` | VARCHAR(100) | UNIQUE | Correo electrónico |
| `telefono` | VARCHAR(20) | NULL | Teléfono de contacto |
| `direccion` | VARCHAR(200) | NULL | Dirección física |
| `limite_credito` | DECIMAL(10,2) | DEFAULT 0 | Límite de crédito disponible |
| `credito_usado` | DECIMAL(10,2) | DEFAULT 0 | Crédito actualmente en uso |
| `activo` | BOOLEAN | NOT NULL, DEFAULT TRUE | Soft delete flag |
| `fecha_creacion` | DATETIME | NOT NULL | Timestamp de creación |
| `fecha_actualizacion` | DATETIME | NOT NULL | Timestamp de última modificación |

---

### 3. ventas 🔜 FASE 2

Almacena las transacciones de venta.

#### Estructura Planeada

| Columna | Tipo | Constraints | Descripción |
|---------|------|-------------|-------------|
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Identificador único |
| `cliente_id` | BIGINT | FOREIGN KEY → clientes(id) | Cliente que realizó la compra |
| `fecha` | DATETIME | NOT NULL | Fecha y hora de la venta |
| `total` | DECIMAL(10,2) | NOT NULL | Total de la venta |
| `estado` | VARCHAR(20) | NOT NULL | PENDIENTE, CONFIRMADA, ENTREGADA, CANCELADA |
| `activo` | BOOLEAN | NOT NULL, DEFAULT TRUE | Soft delete flag |

---

### 4. venta_items 🔜 FASE 2

Tabla intermedia que relaciona ventas con productos (N:M).

#### Estructura Planeada

| Columna | Tipo | Constraints | Descripción |
|---------|------|-------------|-------------|
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Identificador único |
| `venta_id` | BIGINT | FOREIGN KEY → ventas(id) | Venta asociada |
| `producto_id` | BIGINT | FOREIGN KEY → productos(id) | Producto vendido |
| `cantidad` | INT | NOT NULL | Cantidad vendida |
| `precio_unitario` | DECIMAL(10,2) | NOT NULL | Precio al momento de la venta |

**Nota:** Se guarda `precio_unitario` en la venta para mantener histórico. Si el precio del producto cambia después, las ventas pasadas mantienen el precio original.

---

## Índices

### productos (Actuales)

```sql
-- Índice en nombre para búsquedas rápidas
CREATE INDEX idx_nombre ON productos(nombre);

-- Índice en categoría para filtrado
CREATE INDEX idx_categoria ON productos(categoria);

-- Índice en activo para filtrar eliminados
CREATE INDEX idx_activo ON productos(activo);

-- Índice compuesto para detectar productos con bajo stock
CREATE INDEX idx_bajo_stock ON productos(stock, stock_minimo);
```

### Justificación de Índices

**idx_nombre:**
- Query frecuente: `SELECT * FROM productos WHERE nombre LIKE '%laptop%'`
- Sin índice: Full table scan
- Con índice: Búsqueda optimizada

**idx_categoria:**
- Query frecuente: `SELECT * FROM productos WHERE categoria = 'Electrónica'`
- Permite filtrado rápido por categoría

**idx_activo:**
- Query frecuente: `SELECT * FROM productos WHERE activo = true`
- Evita devolver productos eliminados

**idx_bajo_stock:**
- Query frecuente: `SELECT * FROM productos WHERE stock <= stock_minimo`
- Optimiza la búsqueda de productos que necesitan reposición

---

## Convenciones de Nomenclatura

### Tablas
- **Plural, snake_case**: `productos`, `clientes`, `venta_items`
- **Descriptivas**: El nombre debe indicar claramente qué almacena

### Columnas
- **snake_case**: `stock_minimo`, `fecha_creacion`
- **Sufijos comunes**:
  - `_id` para foreign keys: `cliente_id`, `producto_id`
  - `fecha_*` para timestamps: `fecha_creacion`, `fecha_actualizacion`
  
### Primary Keys
- **Siempre `id`**: Todas las tablas tienen un campo `id` como PK
- **Tipo BIGINT**: Permite hasta 9,223,372,036,854,775,807 registros
- **AUTO_INCREMENT**: Generado automáticamente por MySQL

### Foreign Keys
- **Formato `<tabla>_id`**: `cliente_id`, `producto_id`
- **ON DELETE RESTRICT**: No permitir eliminar si hay referencias
- **ON UPDATE CASCADE**: Si el ID cambia (raro), actualizar referencias

---

## Soft Delete

### ¿Por qué Soft Delete?

En lugar de:
```sql
DELETE FROM productos WHERE id = 5;
```

Hacemos:
```sql
UPDATE productos SET activo = false WHERE id = 5;
```

**Ventajas:**
1. **Auditoría**: Mantenemos histórico completo
2. **Recuperación**: Podemos "deshacer" eliminaciones
3. **Referencias**: Las ventas pasadas siguen teniendo el producto_id válido
4. **Reportes históricos**: Podemos analizar productos descontinuados

**Cómo se implementa:**

Todas las queries filtran por `activo = true`:
```java
@Query("SELECT p FROM Producto p WHERE p.activo = true")
List<Producto> findAll();
```

---

## Transacciones

### Aislamiento
```properties
spring.jpa.properties.hibernate.connection.isolation=2
```
**Nivel 2 = READ_COMMITTED**: Balance entre rendimiento y consistencia.

### Comportamiento de JPA

**Auto-commit deshabilitado** en métodos con `@Transactional`:
```java
@Transactional
public void crearVenta(VentaRequest request) {
    // Todo esto es atómico:
    Venta venta = ventaRepository.save(nuevaVenta);
    for (Item item : request.getItems()) {
        producto.reducirStock(item.getCantidad());
        productoRepository.save(producto);
    }
    // Si algo falla, se hace ROLLBACK completo
}
```

---

## Migraciones (Fase 3 - Flyway)

### Estructura de Migraciones

```
src/main/resources/db/migration/
├── V1__crear_tabla_productos.sql
├── V2__crear_tabla_clientes.sql
├── V3__crear_tabla_ventas.sql
└── V4__agregar_indices_productos.sql
```

### Ejemplo: V1__crear_tabla_productos.sql

```sql
-- Migración: Crear tabla productos
-- Autor: Sebastián Jamardo
-- Fecha: 2024-11-25

CREATE TABLE productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500),
    precio DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL,
    stock_minimo INT NOT NULL,
    categoria VARCHAR(50),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion DATETIME NOT NULL,
    fecha_actualizacion DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices
CREATE INDEX idx_nombre ON productos(nombre);
CREATE INDEX idx_categoria ON productos(categoria);
CREATE INDEX idx_activo ON productos(activo);
```

**Ventaja:** Cada cambio en la BD está versionado y es reproducible en cualquier entorno.

---

## Backup y Recuperación

### Backup Completo (Manual)

```bash
mysqldump -u root -p gestion_db > backup_$(date +%Y%m%d).sql
```

### Restauración

```bash
mysql -u root -p gestion_db < backup_20241125.sql
```

### Backup Incremental (Producción)

Configurar backups automáticos diarios:
```bash
0 2 * * * /usr/bin/mysqldump -u root -p[PASSWORD] gestion_db | gzip > /backups/gestion_$(date +\%Y\%m\%d).sql.gz
```

---

## Monitoreo y Performance

### Queries Lentos

Habilitar slow query log:
```sql
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 2; -- queries > 2 segundos
```

### Análisis de Queries

```sql
EXPLAIN SELECT * FROM productos WHERE nombre LIKE '%laptop%';
```

Buscar:
- **type = ALL**: Full table scan (malo)
- **type = ref/range**: Usando índices (bueno)

---

## Escalabilidad

### Connection Pooling (HikariCP)

```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000
```

### Read Replicas (Futuro)

Configurar réplicas de lectura para separar:
- **Master**: Escrituras (INSERT, UPDATE, DELETE)
- **Replicas**: Lecturas (SELECT)

---

## Referencias

- [MySQL 8.0 Documentation](https://dev.mysql.com/doc/refman/8.0/en/)
- [JPA/Hibernate Best Practices](https://vladmihalcea.com/tutorials/hibernate/)
- [Database Naming Conventions](https://www.sqlshack.com/learn-sql-naming-conventions/)

---

**Última actualización:** 25 de noviembre 2024  
**Autor:** Sebastián Jamardo