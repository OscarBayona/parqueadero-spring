# Parking Management API

Sistema de gestión de parqueaderos construido con **Java 21**, **Spring Boot 3.4.4**, **Spring Security**, **PostgreSQL**, y **JWT** para autenticación.

---

## Características
- Registro y autenticación de usuarios
- Gestión de parqueaderos por parte de administradores y socios
- Registro de entradas y salidas de vehículos
- Historial de vehículos con cálculo de tarifas
- Envío de correos simulados a socios
- Estadísticas y reportes personalizados

---

## 📂 Estructura del Proyecto
```bash
parqueadero/
├── parking-service/            # Servicio principal
│   ├── controller/             # Controladores REST
│   ├── dto/                    # Clases de transferencia de datos (DTOs)
│   ├── entity/                 # Entidades JPA (User, Parking, Vehicle, etc.)
│   ├── exception/              # Manejo de Excepciones
│   ├── repository/             # Interfaces de repositorio (JPARepository)
│   ├── service/                # Lógica de negocio
│   ├── config/                 # Seguridad, JWT, inicializadores
│   └── resources/              # application.properties
├── email-service/             # Microservicio de envío de correos (simulado)
└── README.md
```

---

## 📊 Modelo Relacional

### users
- `id`, `email`, `password`, `role`, `active`, `created_at`, `updated_at`

### parkings
- `id`, `name`, `capacity`, `hourly_rate`, `address`, (FK) `owner_id`, `active`, `created_at`, `updated_at`

### vehicles
- `id`, `license_plate`, (FK) `parking_id`, `entry_date_time`, `status`

### vehicle_history
- `id`, `license_plate`, (FK) `parking_id`, `entry_date_time`, `exit_date_time`, `total_cost`, `total_hours`

---

## 📘 Documentación de Endpoints

### 🔐 Autenticación
| Método | Endpoint           | Descripción |
|--------|---------------------|-------------|
| POST   | `/auth/login`       | Iniciar sesión y obtener token JWT |
| POST   | `/auth/logout`      | Cerrar sesión (token se invalida) |

### 👤 Usuarios
| Método | Endpoint                      | Descripción |
|--------|-------------------------------|-------------|
| POST   | `/api/user/register`          | Registro de usuario sin autenticación |
| POST   | `/api/user/create-socio`      | Crear socio (solo ADMIN) |
| GET    | `/api/user`                   | Obtener lista de todos los usuarios (solo ADMIN) |

### 🏢 Parqueaderos
| Método | Endpoint                         | Descripción |
|--------|----------------------------------|-------------|
| POST   | `/api/parkings`                  | Crear un nuevo parqueadero (solo ADMIN) |
| GET    | `/api/parkings`                  | Listar todos los parqueaderos (solo ADMIN) |
| PUT    | `/api/parkings/{id}`             | Actualizar un parqueadero (solo ADMIN) |
| DELETE | `/api/parkings/{id}`             | Eliminar un parqueadero (solo ADMIN) |
| GET    | `/api/parkings/my`               | Listar parqueaderos del socio autenticado |
| GET    | `/api/parkings/{id}/my-vehicles` | Ver vehículos actualmente parqueados de un parqueadero del socio |
| GET    | `/api/parkings/{id}/vehicles/{vehicleId}` | Ver detalle de un vehículo en un parqueadero (socio propietario) |

### 🚗 Vehículos
| Método | Endpoint              | Descripción |
|--------|------------------------|-------------|
| POST   | `/api/vehicles/entry`  | Registrar entrada de vehículo |
| POST   | `/api/vehicles/exit`   | Registrar salida de vehículo (retorna ID del historial) |

### 📈 Estadísticas / Reportes
| Método | Endpoint                                      | Descripción |
|--------|-----------------------------------------------|-------------|
| GET    | `/api/vehicles/top-global`                    | Top 10 placas más registradas en todos los parqueaderos (ADMIN y SOCIO) |
| GET    | `/api/vehicles/top/{parkingId}`               | Top 10 placas más registradas en un parqueadero (ADMIN y SOCIO) |
| GET    | `/api/vehicles/first-time/{parkingId}`        | Vehículos actualmente parqueados que están por primera vez en ese parqueadero |
| GET    | `/api/parkings/{id}/revenue`                  | Ganancias de un parqueadero (hoy, semana, mes, año) (SOCIO) |
| GET    | `/api/socios/top-week`                        | Top 3 socios con más ingresos de vehículos esta semana (ADMIN) |
| GET    | `/api/parkings/top-revenue`                   | Top 3 parqueaderos con mayor ganancia esta semana (ADMIN) |

### 📧 Email
| Método | Endpoint              | Descripción |
|--------|------------------------|-------------|
| POST   | `/api/email/send`      | Enviar email a un socio (solo ADMIN) |

---

## 🌐 Variables de entorno Postman
- `base_url`: URL base para todos los endpoints, ej: `http://localhost:8080`
- `token`: Se puede actualizar automáticamente en el test de login con:
```javascript
let jsonData = pm.response.json();
pm.environment.set("token", jsonData.token);
```

---

## ⚙️ Seguridad
- JWT con roles `ADMIN`, `SOCIO`
- Endpoints protegidos con `@PreAuthorize` o `hasRole()`

---

## 👨‍💻 Autor
**Oscar Ivan Bayona Diaz**  
Repositorio: [github.com/OscarBayona/parqueadero-spring](https://github.com/OscarBayona/parqueadero-spring)
