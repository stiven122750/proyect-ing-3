# 📌 Proyecto: Sistema de Gestión de Productos, Clientes y Órdenes

Este es un proyecto **Spring Boot** diseñado para gestionar información comercial básica: **Productos**, **Clientes**, **Categorías** y **Órdenes**.  
El objetivo principal es demostrar la implementación de una API REST con **lógica de negocio real**, pruebas unitarias y automatización CI/CD usando Jenkins.

---

## 🧩 Arquitectura del Proyecto

- **Backend:** Spring Boot 3
- **Dependencias principales:**
  - Spring Web
  - Spring Data JPA
  - H2 Database (configurable a otros motores)
  - JUnit 5 + Mockito para pruebas
- **Build System:** Gradle
- **CI/CD:** Jenkins (Pipeline con ejecución automática de pruebas)

---

## 🚀 Funcionalidades

El sistema permite administrar:

| Entidad | Funciones |
|--------|-----------|
| **Producto** | Crear, actualizar, listar, obtener por ID y eliminar |
| **Cliente** | Crear, actualizar, listar, obtener por ID y eliminar |
| **Categoría** | CRUD completo |
| **Órdenes** | Crear, listar, obtener por ID y eliminar |

---

## 📌 Reglas de Negocio

### 🔹 Productos
- El **precio debe ser positivo**
- El **nombre debe ser único**
- Al crear una orden, el **stock se reduce**

### 🔹 Clientes
- **No se pueden eliminar** si tienen órdenes asociadas

### 🔹 Órdenes
- La **cantidad debe ser mayor a 0**
- El producto debe tener **stock suficiente**
- Un cliente **no puede tener más de 5 órdenes activas**

💡 Las reglas están implementadas en el nivel de *Service* para mantener la integridad del negocio.

---


## 🧪 Pruebas Unitarias

Se implementan usando:

✔️ JUnit 5  
✔️ Mockito (mocks de repositorios)

Validan:

- Flujo correcto de negocio
- Manejo de excepciones
- Reglas de negocio aplicadas

📍 Reportes generados en:

build/reports/tests/test/index.html
build/test-results/test/*.xml


Integrables en Jenkins automáticamente ✔️

---

## 🔁 CI/CD con Jenkins

Incluye un `Jenkinsfile` que:

1. Descarga el código desde GitHub
2. Ejecuta las pruebas con Gradle
3. Publica reportes JUnit en Jenkins

> Preparado para ampliarse con JaCoCo, SonarQube, Docker, despliegues, etc.

---

## ▶️ Ejecución local

```sh
./gradlew bootRun

Aplicación disponible en:

👉 http://localhost:8080

📌 Estado del Proyecto

✔️ CRUD completo
✔️ Pruebas unitarias implementadas
✔️ Pipeline en Jenkins operativo
✔️ Lógica de negocio real aplicada


✨ Autor

👤 Steven Cardona, Johan Peña, Andrés Mejía
Proyecto académico para prácticas backend con Java + CI/CD

