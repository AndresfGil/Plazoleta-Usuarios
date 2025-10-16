# Tests Unitarios - Microservicio de Usuarios

## 📋 Estructura de Tests

### **1. Tests de Dominio (Domain)**
- **UsuarioUseCaseTest**: Tests del caso de uso principal
  - ✅ Validación de edad (mayor de 18 años)
  - ✅ Encriptación de contraseña
  - ✅ Activación automática del usuario
  - ✅ Manejo de excepciones

### **2. Tests de Aplicación (Application)**
- **UsuarioHandlerTest**: Tests del handler de aplicación
  - ✅ Mapeo correcto de DTOs
  - ✅ Llamadas al servicio del dominio
  - ✅ Manejo de datos nulos

### **3. Tests de Infraestructura (Infrastructure)**
- **UsuarioJpaAdapterTest**: Tests del adaptador JPA
  - ✅ Mapeo entre dominio y entidades
  - ✅ Persistencia en repositorio
  - ✅ Transformación de datos

- **PasswordEncoderAdapterTest**: Tests del adaptador de encriptación
  - ✅ Encriptación de contraseñas
  - ✅ Manejo de valores nulos/vacíos
  - ✅ Integración con BCrypt

### **4. Tests de Controlador (Controller)**
- **UsuarioRestControllerTest**: Tests del controlador REST
  - ✅ Validaciones de entrada (400 Bad Request)
  - ✅ Creación exitosa (201 Created)
  - ✅ Manejo de JSON inválido

- **UsuarioRestControllerIntegrationTest**: Test de integración
  - ✅ Flujo completo end-to-end
  - ✅ Integración entre todas las capas

## 🧪 Cobertura de Tests

### **Casos Cubiertos:**

#### **✅ Casos Exitosos:**
- Usuario mayor de 18 años → Guardado exitoso
- Usuario exactamente 18 años → Guardado exitoso
- Encriptación de contraseña → BCrypt aplicado
- Activación automática → Usuario activo por defecto

#### **❌ Casos de Error:**
- Usuario menor de 18 años → DomainException
- Fecha de nacimiento nula → DomainException
- Campos obligatorios faltantes → 400 Bad Request
- Formato de correo inválido → 400 Bad Request
- Formato de celular inválido → 400 Bad Request
- ID de rol nulo → 400 Bad Request

#### **🔧 Casos Técnicos:**
- Mapeo correcto entre capas
- Llamadas a dependencias mockeadas
- Manejo de valores nulos
- Integración con frameworks

