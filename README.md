# ☕ Sistema de Gestión Empresarial | Backend Java + Spring Boot

> **API REST profesional con arquitectura escalable, seguridad JWT y documentación completa.**  
> Construido desde cero siguiendo principios SOLID, Clean Architecture y mejores prácticas de la industria.

[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=spring)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?logo=mysql)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?logo=docker)](https://www.docker.com/)

---

## 🎯 ¿Qué hace especial a este proyecto?

Este proyecto **NO es otro CRUD tutorial**. Es una API REST empresarial construida desde cero con:

**✅ YA IMPLEMENTADO:**
- **🏗️ Arquitectura profesional** en capas con separación clara de responsabilidades
- **🗃️ Modelo de datos robusto** con validaciones de dominio y soft delete
- **📋 DTOs bien diseñados** que desacoplan el dominio del contrato de la API
- **🔍 Query methods personalizados** con Spring Data JPA
- **✅ Validaciones declarativas** con Bean Validation

**🚧 EN DESARROLLO (esta semana):**
- **🔧 Service layer** con lógica de negocio y reglas de validación
- **🌐 REST Controllers** con endpoints completos (CRUD + búsquedas)
- **⚠️ Exception handling** global con respuestas consistentes
- **🧪 Tests unitarios** para garantizar calidad del código

**🔜 PRÓXIMAMENTE:**
- **🔐 Autenticación JWT** con roles y permisos
- **📊 Lógica de negocio avanzada**: transacciones, validaciones cruzadas, cálculo de totales
- **🐳 Dockerización completa** - levanta todo el stack con un comando
- **📖 Documentación interactiva** con Swagger/OpenAPI
- **✅ Testing completo** - unitarios, integración y cobertura >70%
- **⚡ Optimizaciones** con caché, paginación y filtros avanzados

**Decisión técnica clave:** Elegí Spring Boot porque es el estándar de facto en empresas, con un ecosistema maduro que permite enfocarse en la lógica de negocio sin reinventar la rueda. La arquitectura en capas facilita testing aislado y escalabilidad horizontal.

---

## 📚 Documentación Técnica

Este proyecto cuenta con documentación técnica completa para desarrolladores:

- **[🏛️ Arquitectura del Sistema](docs/architecture_doc.md)** - Diseño en capas, patrones de diseño, flujo de datos
- **[🗃️ Modelo de Base de Datos](docs/database_doc.md)** - Esquema de BD, tablas, índices, convenciones
- **[🌐 API Reference](docs/api_doc.md)** - Documentación completa de endpoints REST

> 💡 **Para nuevos desarrolladores:** Comienza leyendo la Arquitectura, luego el Modelo de Datos, y finalmente la API Reference.

---

## 🚀 Stack Tecnológico

### Core
- **Java 17** (LTS) - características modernas como Records, Pattern Matching
- **Spring Boot 3.x** - framework empresarial de referencia
- **Spring Security + JWT** - autenticación stateless
- **Spring Data JPA** - abstracción elegante sobre Hibernate

### Persistencia
- **MySQL 8.0** - base de datos principal ✅ CONFIGURADO
- **H2** - para testing rápido 🔜 FASE 1
- **Flyway** - migraciones versionadas 🔜 FASE 3

### DevOps & Tools
- **Maven** - gestión de dependencias ✅ CONFIGURADO
- **Docker + Docker Compose** - entorno consistente 🔜 FASE 3
- **Swagger UI** - documentación viva de la API 🔜 FASE 3
- **JUnit 5 + Mockito** - suite completa de testing 🔜 FASE 1

---

## 🏛️ Arquitectura

```
src/main/java/
├── controller/     # Endpoints REST - validación de entrada
├── service/        # Lógica de negocio - casos de uso
├── repository/     # Acceso a datos - persistencia
├── model/          # Entidades JPA - dominio
├── dto/            # Data Transfer Objects - contratos
├── security/       # JWT, filtros, configuración
├── exception/      # Manejo global de errores
└── config/         # Beans, CORS, Swagger
```

**Principios aplicados:**
- **SOLID**: cada clase tiene una responsabilidad única
- **DRY**: código reutilizable sin duplicación
- **Fail-fast**: validaciones tempranas con mensajes claros
- **DTOs**: nunca exponemos entidades directamente

---

## 📦 Entidades del Dominio

### Producto ✅ IMPLEMENTADO
- Control de inventario con stock mínimo
- Categorización flexible
- Validaciones de negocio en el dominio
- Soft delete para auditoría
- Timestamps automáticos de creación/actualización

### Cliente 🔜 PRÓXIMAMENTE
- Gestión de contactos y direcciones
- Historial de compras
- Sistema de crédito y límites

### Venta 🔜 PRÓXIMAMENTE
- Transacciones ACID completas
- Validación automática de stock
- Cálculo de totales con descuentos
- Estados: PENDIENTE → CONFIRMADA → ENTREGADA

---

## ⚡ Endpoints Principales

> **📝 NOTA:** Los endpoints se irán habilitando progresivamente conforme avance el desarrollo.

### Productos 🚧 EN DESARROLLO
```http
GET    /api/productos                           # Listar todos los productos activos
GET    /api/productos/{id}                      # Ver detalle de un producto
POST   /api/productos                           # Crear nuevo producto
PUT    /api/productos/{id}                      # Actualizar producto existente
DELETE /api/productos/{id}                      # Eliminar (soft delete) producto
GET    /api/productos/buscar?nombre=laptop      # Búsqueda por nombre
GET    /api/productos/bajo-stock                # Productos que necesitan reposición
```

### Autenticación 🔜 FASE 3
```http
POST   /api/auth/register     # Registro de usuario
POST   /api/auth/login        # Login (devuelve JWT)
POST   /api/auth/refresh      # Renovar token
```

### Clientes 🔜 FASE 2
```http
GET    /api/clientes              # Listar todos
POST   /api/clientes              # Crear
PUT    /api/clientes/{id}         # Actualizar
GET    /api/clientes/{id}/ventas  # Historial de compras
```

### Ventas 🔜 FASE 2
```http
POST   /api/ventas                # Crear venta (valida stock automáticamente)
GET    /api/ventas/{id}           # Ver detalle con items
PUT    /api/ventas/{id}/estado    # Cambiar estado
GET    /api/ventas/reporte        # Ventas por período
```

**Documentación completa:** Swagger UI estará disponible en `http://localhost:8080/swagger-ui.html` (Fase 3)

---

## 🐳 Instalación y Ejecución

### Estado Actual: Opción 2 (Manual) ✅ FUNCIONAL

```bash
# Requisitos: Java 17, MySQL 8.0, Maven 3.8+

# 1. Clonar el repositorio
git clone https://github.com/SebastianJamardo/sistema-gestion.git
cd sistema-gestion

# 2. Configurar base de datos
mysql -u root -p
CREATE DATABASE gestion_db;

# 3. Configurar application.properties
# Edita src/main/resources/application.properties con tus credenciales:
spring.datasource.url=jdbc:mysql://localhost:3306/gestion_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password

# 4. Compilar y ejecutar
mvn clean install
mvn spring-boot:run

# La aplicación estará disponible en http://localhost:8080
```

### Opción con IDE (IntelliJ/Eclipse) ✅ FUNCIONAL
1. Importar como proyecto Maven
2. Configurar MySQL (ver paso 2 arriba)
3. Actualizar application.properties con tus credenciales
4. Run `SistemaGestionApplication.java`

### Opción 1: Docker 🔜 FASE 3
```bash
# Próximamente disponible
docker-compose up -d
```

---

## 🧪 Testing

> **Estado:** Tests se implementarán al finalizar la Fase 1 (esta semana)

```bash
# Ejecutar todos los tests (próximamente)
mvn test

# Ver cobertura (próximamente)
mvn jacoco:report
# Abrir: target/site/jacoco/index.html
```

**Plan de testing:**
- 🔜 Unitarios para ProductoService (lógica de negocio aislada)
- 🔜 Tests de validaciones en DTOs
- 🔜 Tests de query methods en Repository (con H2 en memoria)
- 🔜 Fase 2: Tests de integración completos
- 🔜 Fase 3: Tests E2E con Testcontainers

**Objetivo de cobertura:** >70% para Fase 1, >80% para Fase 3

---

## 🔒 Seguridad

> **Fase actual:** Configuración básica de Spring Security (password autogenerado)  
> **Fase 3:** Sistema completo de autenticación y autorización

**Implementado:**
- ✅ Spring Security configurado (modo desarrollo)
- ✅ Validación de entrada con Bean Validation
- ✅ Manejo seguro de datos sensibles (BigDecimal para precios)

**Próximamente (Fase 3):**
- 🔜 JWT con expiración - tokens de 24h con refresh
- 🔜 Bcrypt para passwords - salt automático
- 🔜 CORS configurado - solo dominios autorizados
- 🔜 Roles y permisos - USER, ADMIN, MANAGER
- 🔜 Rate limiting - protección contra brute force

---

## 📈 Roadmap

> **🚧 PROYECTO EN CONSTRUCCIÓN ACTIVA** - Este README documenta la visión completa del sistema. El desarrollo sigue un enfoque iterativo con entregas funcionales progresivas.

### 🔄 Fase 1: MVP Funcional (EN PROGRESO - 65%)
- [x] Arquitectura base en capas profesional
- [x] Conexión MySQL y configuración de entorno
- [x] Entidad Producto con lógica de dominio
- [x] Repository con query methods personalizados
- [x] DTOs con validaciones robustas (Request/Response)
- [ ] Service con lógica de negocio completa
- [ ] Controller REST con todos los endpoints
- [ ] Manejo global de excepciones
- [ ] Testing básico (unitarios para Service)
- [ ] Documentación de endpoints con Postman

**Fecha estimada de completado:** 2 de diciembre 2024

### 🔜 Fase 2: Expansión del Dominio (Próximo)
- [ ] Entidades Cliente y Venta
- [ ] Relaciones entre entidades (@OneToMany, @ManyToMany)
- [ ] Transacciones con @Transactional
- [ ] Validaciones de negocio cruzadas
- [ ] Tests de integración con repositorios
- [ ] Paginación y ordenamiento

**Fecha estimada:** Diciembre 2024

### 🔜 Fase 3: Profesionalización
- [ ] Autenticación JWT con Spring Security
- [ ] Sistema de roles y permisos (USER, ADMIN)
- [ ] Swagger/OpenAPI para documentación interactiva
- [ ] Docker + Docker Compose
- [ ] Tests >70% cobertura
- [ ] Manejo avanzado de errores

**Fecha estimada:** Enero 2025

### 🔜 Fase 4: Features Avanzadas
- [ ] Caché con Redis
- [ ] Paginación avanzada con filtros dinámicos
- [ ] Reportes PDF/Excel
- [ ] Métricas con Actuator
- [ ] CI/CD con GitHub Actions
- [ ] Deploy en Railway/AWS

**Fecha estimada:** Febrero 2025

---

## 🧠 Decisiones Técnicas Documentadas

### ¿Por qué JWT en lugar de sesiones?
**Decisión:** Autenticación stateless con JWT.  
**Razón:** Permite escalado horizontal sin sticky sessions. El backend no almacena estado, delegando la verificación a la firma del token.  
**Trade-off:** Los tokens no se pueden invalidar hasta que expiren (solución: refresh tokens + lista negra en Redis).

### ¿Por qué DTOs separados?
**Decisión:** Nunca exponer entidades JPA directamente.  
**Razón:** Desacopla el modelo de dominio del contrato de la API. Evita lazy loading exceptions, controla qué campos se serializan y facilita versionado de la API.

### ¿Por qué Flyway?
**Decisión:** Migraciones versionadas en lugar de `ddl-auto=update`.  
**Razón:** Reproducibilidad en cualquier entorno. Las migraciones son código, se revisan en PR y se ejecutan automáticamente. Zero downtime deployments.

---

## 🤝 Contribuciones

Este es un proyecto de aprendizaje **abierto a feedback**. Si ves algo mejorable:

1. Abre un Issue explicando el problema
2. Propón una solución en un PR
3. Asegúrate de que los tests pasen

---

## 📞 Contacto

**Sebastián Jamardo**  
Desarrollador Backend | Java + Spring Boot  

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue?logo=linkedin)](https://www.linkedin.com/in/sebastianjamardo/)
[![GitHub](https://img.shields.io/badge/GitHub-Follow-black?logo=github)](https://github.com/SebastianJamardo)
[![Email](https://img.shields.io/badge/Email-Contact-red?logo=gmail)](mailto:tu-email@ejemplo.com)

---

## 📄 Licencia

MIT License - siéntete libre de usar este código para aprender.

---

**⭐ Si este proyecto te resulta útil para aprender, dale una estrella en GitHub!**

**📊 Estado del proyecto:** 🚧 En desarrollo activo - Fase 1 (65% completado)

*Última actualización: 25 de noviembre 2024*