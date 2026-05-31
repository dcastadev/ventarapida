# VentaRápida - Sistema de Punto de Venta

Sistema de punto de venta para equipos tecnológicos desarrollado con Spring Boot y SQL Server.

## 🚀 Características

- Gestión de productos y categorías
- Administración de clientes
- Registro de ventas
- Generación de reportes PDF
- Sistema de autenticación con roles (ADMIN y VENDEDOR)
- Interfaz moderna con Bootstrap 5

## 🛠️ Tecnologías

- **Backend**: Spring Boot 3.2.5
- **Base de datos**: SQL Server 2022
- **Frontend**: Thymeleaf, Bootstrap 5, Bootstrap Icons
- **Seguridad**: Spring Security con BCrypt
- **Reportes**: JasperReports
- **Java**: 21

## 📋 Requisitos previos

- Java 21 o superior
- Maven 3.9+
- Docker (para SQL Server)

## 🔧 Instalación y configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/TU_USUARIO/ventarapida.git
cd ventarapida
```

### 2. Iniciar SQL Server con Docker

```bash
docker run -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=YourStrong@Passw0rd" \
  -p 1433:1433 --name sqlserver-ventarapida \
  -d mcr.microsoft.com/mssql/server:2022-latest
```

### 3. Crear la base de datos

Espera 15 segundos para que SQL Server inicie, luego ejecuta:

```bash
# Copiar el script SQL al contenedor
docker cp VentaRapidaDB.sql sqlserver-ventarapida:/tmp/VentaRapidaDB.sql

# Crear la base de datos
docker exec -it sqlserver-ventarapida /opt/mssql-tools18/bin/sqlcmd \
  -S localhost -U sa -P "YourStrong@Passw0rd" -C \
  -Q "CREATE DATABASE VentaRapidaDB"

# Ejecutar el script
docker exec -it sqlserver-ventarapida /opt/mssql-tools18/bin/sqlcmd \
  -S localhost -U sa -P "YourStrong@Passw0rd" -d VentaRapidaDB -C \
  -i /tmp/VentaRapidaDB.sql
```

### 4. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8080/ventarapida**

## 🔐 Credenciales de acceso

### Usuario Administrador
- **Usuario**: `admin`
- **Contraseña**: `password`

### Usuario Vendedor
- **Usuario**: `vendedor`
- **Contraseña**: `vendedor123`

## 📁 Estructura del proyecto

```
ventarapida/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── pe/edu/cibertec/ventarapida/
│   │   │       ├── config/          # Configuración de seguridad
│   │   │       ├── controller/      # Controladores MVC
│   │   │       ├── dto/             # Data Transfer Objects
│   │   │       ├── entity/          # Entidades JPA
│   │   │       ├── repository/      # Repositorios JPA
│   │   │       └── service/         # Lógica de negocio
│   │   └── resources/
│   │       ├── templates/           # Vistas Thymeleaf
│   │       ├── static/              # CSS, JS, imágenes
│   │       └── application.properties
│   └── test/
├── VentaRapidaDB.sql               # Script de base de datos
└── pom.xml
```

## 🎯 Funcionalidades por rol

### Administrador (ADMIN)
- ✅ Gestión completa de productos
- ✅ Gestión de categorías
- ✅ Gestión de clientes
- ✅ Registro de ventas
- ✅ Generación de reportes PDF
- ✅ Consultas de ventas

### Vendedor (VENDEDOR)
- ✅ Visualización de productos
- ✅ Gestión de clientes
- ✅ Registro de ventas
- ✅ Consultas de ventas

## 🐳 Comandos útiles de Docker

```bash
# Iniciar el contenedor
docker start sqlserver-ventarapida

# Detener el contenedor
docker stop sqlserver-ventarapida

# Ver logs del contenedor
docker logs sqlserver-ventarapida

# Acceder a SQL Server
docker exec -it sqlserver-ventarapida /opt/mssql-tools18/bin/sqlcmd \
  -S localhost -U sa -P "YourStrong@Passw0rd" -C
```

## 📝 Configuración

El archivo `application.properties` contiene la configuración de la aplicación:

- **Puerto**: 8080
- **Context path**: /ventarapida
- **Base de datos**: VentaRapidaDB en localhost:1433
- **Usuario BD**: sa
- **Contraseña BD**: YourStrong@Passw0rd

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor, abre un issue primero para discutir los cambios que te gustaría realizar.

## 📄 Licencia

Este proyecto es de código abierto y está disponible para fines educativos.

## 👥 Autores

- Proyecto desarrollado para el curso de Lenguaje de Programación II - CIBERTEC

## 📞 Soporte

Si tienes problemas con la instalación o ejecución del proyecto, por favor abre un issue en GitHub.
