# 🐟 La Cachina Fish — Sistema Web de Gestión

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen?style=for-the-badge&logo=springboot" />
  <img src="https://img.shields.io/badge/MySQL-Cloud-blue?style=for-the-badge&logo=mysql" />
  <img src="https://img.shields.io/badge/Desplegado%20en-Render-46E3B7?style=for-the-badge&logo=render" />
</p>

Sistema web desarrollado para la gestión integral de pedidos, reservas y atención al cliente de la cevichería **La Cachina Fish**. Incluye panel de administración, módulo de pedidos online, carrito de compras, reclamos, sugerencias y APIs REST.

---

## 🌐 Demo en vivo

🔗 [https://cachinafish.onrender.com](https://cachinafish.onrender.com)

> ⚠️ El servidor puede tardar unos segundos en despertar si estuvo inactivo (instancia gratuita en Render).

---

## 📋 Tabla de contenidos

- [Descripción](#-descripción)
- [Tecnologías](#-tecnologías-utilizadas)
- [Arquitectura](#-arquitectura)
- [Funcionalidades](#-funcionalidades)
- [Estructura del proyecto](#-estructura-del-proyecto)
- [APIs REST](#-apis-rest)
- [Base de datos](#-base-de-datos)
- [Seguridad](#-seguridad)
- [Variables de entorno](#-variables-de-entorno)
- [Instalación local](#-instalación-local)
- [Despliegue cloud](#-despliegue-cloud)
- [Autores](#-autores)

---

## 📖 Descripción

**La Cachina Fish** es una aplicación web full-stack construida con Spring Boot y Thymeleaf que permite a los clientes realizar pedidos online, hacer reservas de mesa, enviar sugerencias y reclamaciones. Los administradores cuentan con un panel de control para gestionar platos, usuarios, pedidos y toda la actividad del negocio.

---

## 🛠️ Tecnologías utilizadas

| Capa | Tecnología |
|------|-----------|
| Lenguaje | Java 17 |
| Framework principal | Spring Boot 3.3.4 |
| Seguridad | Spring Security |
| Persistencia | Spring Data JPA + Hibernate |
| Motor de plantillas | Thymeleaf |
| Base de datos | MySQL (local) / Aiven MySQL Cloud |
| Utilidades | Lombok, Spring Mail |
| Build | Maven |
| Control de versiones | Git + GitHub |
| Despliegue | Render (backend) |

---

## 🏗️ Arquitectura

El sistema sigue el patrón **MVC (Model – View – Controller)**:

```
src/main/java/com/example/SPA_CACHINA/
│
├── configuraciones/        # Spring Security y configuración de usuario
├── controladores/          # Controllers MVC + API REST
│   └── ApiPruebas/         # Endpoints REST (contactos, login, pedidos, platos...)
├── entidades/              # Modelos JPA (Usuario, Plato, Pedido, Reserva...)
├── repositorios/           # Interfaces DAO (Spring Data JPA)
├── servicios/              # Lógica de negocio
└── locale/                 # DTOs, configuración Web, modelos auxiliares

src/main/resources/
├── templates/              # Vistas Thymeleaf (.html)
│   └── vistas/             # Fragmentos reutilizables (navbar, footer, carrusel...)
└── static/imagenes/        # Imágenes de los platos
```

---

## ✅ Funcionalidades

### 👤 Usuarios
- Registro e inicio de sesión con control de roles (`ADMIN` / `USER`)
- Gestión de usuarios (listado, cambio de rol) desde el panel admin

### 🍽️ Platos
- Listado del menú con imágenes y precios
- CRUD completo de platos (solo administrador)

### 🛒 Carrito y Pedidos
- Carrito de compras interactivo
- Realización de pedidos online
- Confirmación y detalle de pedidos
- Gestión y edición de pedidos (admin)

### 📅 Reservas
- Formulario de reserva de mesa
- Gestión de reservas desde el panel admin

### 📩 Atención al cliente
- Formulario de **reclamos**
- Formulario de **sugerencias**
- Formulario de **contacto**
- Gestión y edición desde el panel admin

### 🔐 Panel administrador
- Página exclusiva para administradores
- Control total de platos, pedidos, usuarios, reclamos, sugerencias y reservas

---

## 🔌 APIs REST

Los siguientes endpoints REST están disponibles bajo `/api/`:

| Recurso | Ruta base |
|---------|-----------|
| Login | `/api/login` |
| Usuarios | `/api/usuarios` |
| Platos | `/api/platos` |
| Pedidos | `/api/pedidos` |
| Reservas | `/api/reservas` |
| Contactos | `/api/contactos` |
| Reclamaciones | `/api/reclamaciones` |
| Sugerencias | `/api/sugerencias` |

---

## 🗄️ Base de datos

La persistencia fue implementada con **Spring Data JPA** + **Hibernate** sobre **MySQL**.

**Entidades principales:**

- `Usuario` — datos de autenticación y roles
- `Plato` — menú del restaurante
- `Pedido` + `PedidoDetalle` — cabecera y líneas del pedido
- `Reservas` — reservas de mesa
- `Contactos` — mensajes de contacto
- `Reclamaciones` — libro de reclamaciones
- `Sugerencias` — sugerencias de clientes

La base de datos en producción está alojada en **Aiven MySQL Cloud**.

---

## 🔒 Seguridad

Spring Security implementa:

- Autenticación basada en formulario (login/logout)
- Control de roles (`ROLE_ADMIN`, `ROLE_USER`)
- Cifrado de contraseñas con **BCrypt**
- Protección **CSRF**
- Sesiones protegidas
- Acceso a rutas restringido según rol

---

## ⚙️ Variables de entorno

Para ejecutar el proyecto en producción, configurar las siguientes variables de entorno:

| Variable | Descripción | Valor por defecto |
|----------|-------------|-------------------|
| `PORT` | Puerto del servidor | `8080` |
| `DB_URL` | URL de conexión MySQL | `jdbc:mysql://localhost:3306/spa_cachinafish` |
| `DB_USERNAME` | Usuario de base de datos | `root` |
| `DB_PASSWORD` | Contraseña de base de datos | `ROOT` |
| `SECURITY_USER` | Usuario admin temporal | `dave` |
| `SECURITY_PASSWORD` | Contraseña admin temporal | `123` |
| `SECURITY_ROLE` | Rol del usuario admin | `ADMIN` |
| `MAIL_HOST` | Host SMTP | `smtp.gmail.com` |
| `MAIL_PORT` | Puerto SMTP | `587` |
| `MAIL_USERNAME` | Correo remitente | — |
| `MAIL_PASSWORD` | Contraseña de correo | — |

---

## 🚀 Instalación local

### Requisitos previos

- Java 17+
- Maven 3.8+
- MySQL 8+

### Pasos

```bash
# 1. Clonar el repositorio
git clone https://github.com/tu-usuario/cachina-fish.git
cd cachina-fish

# 2. Crear la base de datos en MySQL
CREATE DATABASE spa_cachinafish;

# 3. Configurar las credenciales en application.properties
#    o exportar variables de entorno:
export DB_URL=jdbc:mysql://localhost:3306/spa_cachinafish?serverTimezone=UTC
export DB_USERNAME=tu_usuario
export DB_PASSWORD=tu_contraseña

# 4. Compilar y ejecutar
./mvnw spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`.

---

## ☁️ Despliegue cloud

| Componente | Plataforma | URL / Servicio |
|-----------|-----------|----------------|
| Backend (JAR) | Render | [cachinafish.onrender.com](https://cachinafish.onrender.com) |
| Base de datos | Aiven MySQL Cloud | Configurado vía variables de entorno |

---

## 👨‍💻 Autores

**GRUPO 4 — Proyecto Integrador II**  
Universidad Tecnológica del Perú  
Carrera: Ingeniería de Sistemas

---

<p align="center">
  Hecho con ❤️ por el Grupo 4 · UTP · 2026
</p>
