# Microservicio de Usuarios - Plaza de Comidas

## 📋 Descripción

Microservicio de gestión de usuarios para el sistema Plaza de Comidas. Este servicio es responsable de la administración de usuarios con diferentes roles (Administrador, Propietario, Empleado, Cliente).

## 🏗️ Arquitectura

Este proyecto implementa **Arquitectura Hexagonal (Puertos y Adaptadores)** con las siguientes capas:

### **Domain (Dominio)**
Contiene la lógica de negocio pura, independiente de frameworks.

- **api**: Puertos de entrada (interfaces de casos de uso)
  - `IUsuarioServicePort`
- **spi**: Puertos de salida (interfaces de persistencia)
  - `IUsuarioPersistencePort`
- **model**: Modelos del dominio
  - `Usuario`
- **usecase**: Implementación de casos de uso
  - `UsuarioUseCase`
- **exception**: Excepciones del dominio
  - `DomainException`

### **Application (Aplicación)**
Coordina las operaciones entre la capa de presentación y el dominio.

- **dto**: Data Transfer Objects
  - `UsuarioRequestDto`
  - `UsuarioResponseDto`
- **handler**: Manejadores de operaciones
  - `IUsuarioHandler`
  - `UsuarioHandler`
- **mapper**: Mappers con MapStruct
  - `IUsuarioRequestMapper`
  - `IUsuarioResponseMapper`

### **Infrastructure (Infraestructura)**
Contiene los adaptadores de entrada y salida.

- **input/rest**: Controladores REST
  - `UsuarioRestController`
- **out/jpa**: Adaptadores de persistencia JPA
  - `entity/UsuarioEntity`
  - `repository/IUsuarioRepository`
  - `adapter/UsuarioJpaAdapter`
  - `mapper/IUsuarioEntityMapper`
- **configuration**: Configuración de beans
  - `BeanConfiguration`
- **documentation**: Configuración OpenAPI
  - `OpenApiConfiguration`
- **exception**: Excepciones de infraestructura
- **exceptionhandler**: Manejador global de excepciones

## 🚀 Tecnologías

- **Java 11**
- **Spring Boot 2.7.3**
- **Spring Data JPA**
- **MySQL**
- **Lombok**
- **MapStruct 1.5.2**
- **SpringDoc OpenAPI 1.6.11** (Swagger)
- **Gradle 7.5**

## ⚙️ Configuración

### Base de Datos

Configurar en `application.yml`:

```yaml
   spring:
      datasource:
    url: jdbc:mysql://localhost:3306/plazoleta-usuarios
    username: tu_usuario
    password: tu_password
  jpa:
    hibernate:
      ddl-auto: update
```

## 📊 Modelo de Datos

### Usuario
- `id`: Long (PK, auto-generado)
- `nombre`: String (obligatorio)
- `apellido`: String (obligatorio)
- `documentoIdentidad`: Integer (obligatorio, único)
- `celular`: String (obligatorio)
- `fechaNacimiento`: Date (obligatorio)
- `correo`: String (obligatorio, único)
- `clave`: String (obligatorio)
- `idRol`: Long (obligatorio)
- `activo`: Boolean

### Roles
1. Administrador
2. Propietario
3. Empleado
4. Cliente

## 🔌 API Endpoints

### **Usuarios**

Base URL: `/api/v1/usuarios`

#### Crear Usuario
```http
POST /api/v1/usuarios/
Content-Type: application/json

{
  "nombre": "Juan",
  "apellido": "Pérez",
  "documentoIdentidad": 12345678,
  "celular": "+573001234567",
  "fechaNacimiento": "1990-01-15",
  "correo": "juan.perez@example.com",
  "clave": "password123",
  "idRol": 4
}
```

**Respuestas:**
- `201 Created`: Usuario creado exitosamente
- `400 Bad Request`: Datos inválidos
- `409 Conflict`: El usuario ya existe

#### Obtener Todos los Usuarios
```http
GET /api/v1/usuarios/
```

**Respuestas:**
- `200 OK`: Lista de usuarios
- `404 Not Found`: No hay usuarios

## 📝 Reglas de Negocio

### Validaciones de Usuario

1. **Mayoría de Edad**: El usuario debe ser mayor de 18 años
2. **Correo Único**: No puede haber dos usuarios con el mismo correo
3. **Documento Único**: No puede haber dos usuarios con el mismo documento
4. **Formato de Celular**: Debe tener entre 10 y 13 dígitos
5. **Usuario Activo**: Al crear un usuario, se activa automáticamente

## 🏃 Ejecución

### Gradle

```bash
# Compilar
./gradlew build

# Ejecutar
./gradlew bootRun

# Tests
./gradlew test

# Reporte de cobertura
./gradlew jacocoTestReport
```

### JAR

```bash
java -jar build/libs/plazoleta-usuarios-0.0.1-SNAPSHOT.jar
```

## 📖 Documentación API (Swagger)

Una vez ejecutado el proyecto, acceder a:

```
http://localhost:8080/swagger-ui.html
```

## 🧪 Testing

Las pruebas unitarias deben seguir estos principios:
- **Cada caso de uso debe tener su test**
- **Usar Mockito para mocks**
- **JUnit 5** para las pruebas
- Cobertura mínima: 80%

Ejemplo:

```java
@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {
    
    @Mock
    private IUsuarioPersistencePort persistencePort;
    
    @InjectMocks
    private UsuarioUseCase useCase;
    
    @Test
    void guardarUsuario_cuandoEsMayorDeEdad_debeGuardarExitosamente() {
        // Arrange
        Usuario usuario = crearUsuarioValido();
        
        // Act & Assert
        assertDoesNotThrow(() -> useCase.guardarUsuario(usuario));
        verify(persistencePort).guardarUsuario(usuario);
    }
}
```

## 🔒 Seguridad

**Nota**: En producción se debe:
- Encriptar contraseñas (BCrypt)
- Implementar JWT para autenticación
- Usar HTTPS
- Validar permisos por rol

## 📦 Estructura de Paquetes

```
com.pragma.powerup
├── application
│   ├── dto
│   │   ├── request
│   │   └── response
│   ├── handler
│   │   └── impl
│   └── mapper
├── domain
│   ├── api
│   ├── exception
│   ├── model
│   ├── spi
│   └── usecase
├── infrastructure
│   ├── configuration
│   ├── documentation
│   ├── exception
│   ├── exceptionhandler
│   ├── input
│   │   └── rest
│   └── out
│       └── jpa
│           ├── adapter
│           ├── entity
│           ├── mapper
│           └── repository
└── PowerUpApplication.java
```

## 🤝 Contribución

1. Seguir los principios SOLID
2. Mantener la separación de capas
3. No incluir lógica de negocio en controladores
4. Usar DTOs para comunicación entre capas
5. Documentar endpoints con OpenAPI annotations
6. Incluir pruebas unitarias para cada caso de uso

## 👨‍💻 Autor

Pragma - Plaza de Comidas

## 📄 Licencia

Este proyecto es parte del sistema Plaza de Comidas.
