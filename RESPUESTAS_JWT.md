# 🔐 Respuestas Detalladas sobre JWT - Sistema de Tokens

---

## **1. ¿Para qué es la configuración de la clave secreta?**

### **La Clave Secreta (JWT Secret)**

```javascript
// En datosGlobales.js
const getJWTConfig = () => {
  return {
    secret: process.env.JWT_SECRET || "asuntos_ugma_secret_key_2024",
    expiresIn: process.env.JWT_EXPIRES_IN || "8h",
  };
};
```

### **¿Para qué sirve?**

La **clave secreta** es como una "llave maestra" que se usa para:

#### **A) FIRMAR el token (cuando se crea)**

```javascript
// Cuando el usuario hace login
const token = jwt.sign(payload, jwtConfig.secret, { expiresIn: "8h" });
```

**Analogía:** Es como un sello oficial del gobierno. Solo quien tiene el sello oficial puede crear documentos válidos.

#### **B) VERIFICAR el token (cuando llega una petición)**

```javascript
// Cuando el usuario hace una petición
const decoded = jwt.verify(token, jwtConfig.secret);
```

**Analogía:** Es como verificar que un billete es auténtico usando una lámpara especial. Solo con la lámpara correcta puedes validarlo.

---

### **¿Por qué es importante?**

#### **🔒 Seguridad Criptográfica**

```
┌─────────────────────────────────────────────────────────┐
│  CREACIÓN DEL TOKEN (FIRMA)                             │
├─────────────────────────────────────────────────────────┤
│                                                          │
│  Datos del usuario (payload)                            │
│  + Clave Secreta                                        │
│  ────────────────────────                               │
│  │ Algoritmo HMAC-SHA256 │                              │
│  ────────────────────────                               │
│         ↓                                                │
│  Token firmado (nadie puede modificarlo)                │
│                                                          │
└─────────────────────────────────────────────────────────┘
```

**Ejemplo práctico:**

```javascript
// Payload original
const payload = {
  idusuario: 123,
  username: "jperez",
  role: "A", // Administrador
};

// Alguien malicioso intenta cambiar el rol
// Modifica el token para poner role: "S" (SuperAdmin)
// ❌ PERO al verificar:

jwt.verify(tokenModificado, jwtConfig.secret);
// ❌ ERROR: "invalid signature"
// La firma no coincide porque se modificó el contenido
```

**Sin la clave secreta correcta:**

- ❌ No puedes crear tokens válidos
- ❌ No puedes modificar tokens existentes sin que se detecte
- ❌ Los tokens falsos son rechazados inmediatamente

---

### **¿Qué pasa si alguien conoce la clave secreta?**

⚠️ **PELIGRO:** Si un atacante obtiene tu clave secreta, puede:

- ✅ Crear tokens válidos para cualquier usuario
- ✅ Hacerse pasar por administradores
- ✅ Acceder a todo el sistema

**Por eso:**

1. ✅ Nunca subirla a Git
2. ✅ Guardarla en archivo `.env` (ignorado por Git)
3. ✅ Usar claves largas y complejas
4. ✅ Cambiarla periódicamente en producción

---

## **2. ¿De dónde obtenemos usuarioBean?**

### **Flujo completo:**

```javascript
// 1. Usuario envía credenciales desde el frontend
const response = await fetch("/api/auth/login", {
  method: "POST",
  body: JSON.stringify({
    username: "jperez",
    password: "mi_contraseña_segura",
  }),
});

// ──────────────────────────────────────────────────

// 2. Backend recibe las credenciales en authController.js
const login = async (req, res, next) => {
  const { username, password } = req.body;

  // 3. Crea objeto para buscar en BD
  const datosLogin = {
    usuario: username, // "jperez"
    contrasenia: password, // "mi_contraseña_segura"
  };

  // 4. Busca el usuario en la base de datos PostgreSQL
  const usuarioBean = await administraUsuariosAreas.buscaUsuario(datosLogin);
  //    ↑
  //    AQUÍ obtenemos el usuarioBean
};
```

---

### **¿Qué hace `buscaUsuario()`?**

```javascript
// En administraUsuariosAreas.js
async buscaUsuario(datosLogin) {
  // 1. Va a la base de datos PostgreSQL
  const usuarioDTO = await usuarioDAO.buscaUsuario(
    datosLogin.usuario,
    datosLogin.contrasenia
  );

  // 2. Si el usuario existe y la contraseña es correcta
  if (usuarioDTO) {
    // 3. Busca los permisos del usuario
    const permisos = await permisoDAO.buscaPermisosUsuario(usuarioDTO.idusuario);

    // 4. Busca las áreas del usuario
    const areas = await areaDAO.buscaAreasUsuario(usuarioDTO.idusuario);

    // 5. Construye el usuarioBean completo
    const usuarioBean = {
      datos: {
        idusuario: usuarioDTO.idusuario,
        username: usuarioDTO.username,
        nombre: usuarioDTO.nombre,
        apellido: usuarioDTO.apellido,
        nombreCompleto: `${usuarioDTO.nombre} ${usuarioDTO.apellido}`,
        superusuario: usuarioDTO.superusuario,
        // ... más datos
      },
      permisos: permisos,  // Lista de permisos del usuario
      areas: areas,        // Lista de áreas donde tiene acceso
      permisoActual: permisos[0],  // Permiso principal
    };

    return usuarioBean;
  }

  return null;  // Usuario no encontrado o contraseña incorrecta
}
```

---

### **Estructura del usuarioBean:**

```javascript
usuarioBean = {
  datos: {
    idusuario: 123,
    username: "jperez",
    nombre: "Juan",
    apellido: "Pérez",
    nombreCompleto: "Juan Pérez",
    superusuario: true,
    email: "jperez@example.com",
  },
  permisos: [
    {
      datos: {
        idpermiso: 1,
        idusuario: 123,
        idarea: 5,
        rol: "A", // Administrador
      },
      descripcion: "Dirección General",
      rolDescripcion: "Administrador",
    },
  ],
  areas: [{ idarea: 5, siglas: "DGGMA", nombre: "Dirección General" }],
  permisoActual: {
    datos: { idpermiso: 1, idusuario: 123, idarea: 5, rol: "A" },
    descripcion: "Dirección General",
    rolDescripcion: "Administrador",
  },
};
```

**En resumen:** `usuarioBean` es un objeto que contiene TODA la información del usuario que acaba de hacer login, obtenida de la base de datos PostgreSQL.

---

## **3. Los datos dentro de payload, ¿por qué tantos?, ¿son todos necesarios?**

### **Payload actual:**

```javascript
const payload = {
  idusuario: usuarioBean.datos.idusuario, // ID único del usuario
  username: usuarioBean.datos.username, // Nombre de usuario
  nombre: usuarioBean.datos.nombreCompleto, // Nombre completo
  role: usuarioBean.permisoActual?.datos?.rol, // Rol (A, U, R)
  idarea: usuarioBean.permisoActual?.datos?.idarea, // Área actual
  superusuario: usuarioBean.datos.superusuario, // ¿Es superusuario?
};
```

---

### **¿Son todos necesarios?**

#### **✅ MÍNIMOS NECESARIOS (imprescindibles):**

```javascript
const payloadMinimo = {
  idusuario: 123, // ← Para identificar al usuario de forma única
  role: "A", // ← Para control de permisos
};
```

Con solo estos 2 campos, el sistema **podría funcionar**, pero tendrías que:

- ❌ Ir a la BD en cada petición para obtener el nombre
- ❌ Ir a la BD para obtener el área
- ❌ Hacer más consultas = más lento

---

#### **✅ RECOMENDADOS (mejoran el rendimiento):**

```javascript
const payloadOptimizado = {
  idusuario: 123, // ✅ NECESARIO - Identificación única
  username: "jperez", // ✅ ÚTIL - Mostrar en logs y auditoría
  nombre: "Juan Pérez", // ✅ ÚTIL - Mostrar en UI sin consultar BD
  role: "A", // ✅ NECESARIO - Control de acceso
  idarea: 5, // ✅ ÚTIL - Filtrar datos por área
  superusuario: true, // ✅ ÚTIL - Permisos especiales
};
```

---

### **¿Qué NO incluir en el payload?**

#### **❌ Datos sensibles:**

```javascript
// ❌ NUNCA incluir:
const payloadMALO = {
  idusuario: 123,
  password: "mi_contraseña",        // ❌ PELIGRO!
  numeroTarjeta: "1234-5678",       // ❌ PELIGRO!
  sueldo: 50000,                    // ❌ Dato sensible
  permisos: [...array gigante...]   // ❌ Token muy grande
};
```

**Razón:** El token JWT se puede **decodificar** (aunque no modificar). Cualquiera con el token puede ver su contenido:

```javascript
// Cualquiera puede hacer esto en su navegador:
const partes = token.split(".");
const payload = JSON.parse(atob(partes[1]));
console.log(payload); // Ve todos los datos
```

---

### **¿Cuáles usar en tu caso?**

Para tu sistema de asuntos, el payload actual es **óptimo**:

```javascript
const payload = {
  idusuario: usuarioBean.datos.idusuario, // ✅ Para saber quién es
  username: usuarioBean.datos.username, // ✅ Para logs y auditoría
  nombre: usuarioBean.datos.nombreCompleto, // ✅ Para mostrar en UI
  role: usuarioBean.permisoActual?.datos?.rol, // ✅ Para control de acceso
  idarea: usuarioBean.permisoActual?.datos?.idarea, // ✅ Para filtros
  superusuario: usuarioBean.datos.superusuario, // ✅ Para permisos especiales
};
```

**Ventajas:**

- ✅ Token pequeño (no excede límites)
- ✅ Suficiente info para la mayoría de operaciones
- ✅ No necesitas consultar BD en cada petición
- ✅ No incluye datos sensibles

---

## **4. ¿A qué te refieres con "petición a una ruta protegida"?**

### **Rutas Públicas vs Rutas Protegidas**

#### **🌐 Rutas PÚBLICAS (sin autenticación):**

```javascript
// Cualquiera puede acceder - NO requiere token
app.post("/api/auth/login", login); // ✅ Login
app.get("/api/public/info", getPublicInfo); // ✅ Información pública
```

**Ejemplo de petición:**

```javascript
// Frontend - No necesita token
fetch("http://localhost:9001/api/auth/login", {
  method: "POST",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify({ username: "jperez", password: "1234" }),
});
```

---

#### **🔒 Rutas PROTEGIDAS (requieren autenticación):**

```javascript
// Solo usuarios autenticados pueden acceder - REQUIERE token
router.get(
  "/permisos/:idUsuario",
  verifyToken, // ← Middleware que valida el token
  rolesController.obtenerPermisos
);
```

**Ejemplo de petición:**

```javascript
// Frontend - NECESITA el token
const token = localStorage.getItem("token"); // Token guardado al hacer login

fetch("http://localhost:9001/api/roles/permisos/123", {
  method: "GET",
  headers: {
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`, // ← AQUÍ va el token
  },
});
```

---

### **¿Qué pasa en una ruta protegida?**

```javascript
// 1. Usuario hace petición
GET /api/roles/permisos/123
Headers: {
  Authorization: "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}

// ──────────────────────────────────────────────────

// 2. Express recibe la petición
router.get("/permisos/:idUsuario", verifyToken, rolesController.obtenerPermisos);
                                    ↑
                                    │
// 3. PRIMERO ejecuta el middleware verifyToken
const verifyToken = (req, res, next) => {
  const token = req.headers.authorization?.split(" ")[1];

  if (!token) {
    return res.status(401).json({ message: "Token requerido" });
  }

  const decoded = jwt.verify(token, jwtConfig.secret);
  req.user = decoded;  // Guarda los datos del usuario
  next();  // ✅ Permite continuar
};

// ──────────────────────────────────────────────────

// 4. Si el token es válido, ENTONCES ejecuta el controller
rolesController.obtenerPermisos = (req, res) => {
  // Ahora puedes usar req.user
  console.log(req.user.idusuario);  // 123
  console.log(req.user.role);       // "A"

  // Procesar la petición...
};
```

---

### **Ejemplo completo del flujo:**

```
Usuario → Frontend → Backend

┌──────────────────────────────────────────────────────┐
│ 1. Usuario hace login                                │
│    POST /api/auth/login                              │
│    { username: "jperez", password: "1234" }          │
└────────────────────┬─────────────────────────────────┘
                     ↓
┌──────────────────────────────────────────────────────┐
│ 2. Backend valida y retorna token                    │
│    { token: "eyJhbGci...", user: {...} }             │
└────────────────────┬─────────────────────────────────┘
                     ↓
┌──────────────────────────────────────────────────────┐
│ 3. Frontend guarda el token                          │
│    localStorage.setItem('token', token)              │
└──────────────────────────────────────────────────────┘

        ⏰ Pasa el tiempo...

┌──────────────────────────────────────────────────────┐
│ 4. Usuario quiere ver sus permisos (RUTA PROTEGIDA) │
│    GET /api/roles/permisos/123                       │
│    Headers: { Authorization: "Bearer eyJhbGci..." }  │
└────────────────────┬─────────────────────────────────┘
                     ↓
┌──────────────────────────────────────────────────────┐
│ 5. Middleware verifyToken valida el token            │
│    ✅ Token válido → req.user = { idusuario: 123 }   │
│    ❌ Token inválido → 401 Unauthorized              │
└────────────────────┬─────────────────────────────────┘
                     ↓
┌──────────────────────────────────────────────────────┐
│ 6. Controller procesa la petición                    │
│    Retorna los permisos del usuario                  │
└──────────────────────────────────────────────────────┘
```

---

## **5. ¿Cómo funcionan los parámetros de authenticateToken?**

```javascript
export const authenticateToken = (req, res, next) => {
  // ...código...
};
```

### **Parámetros explicados:**

#### **`req` (Request - Petición)**

Es un objeto que contiene TODA la información de la petición HTTP:

```javascript
req = {
  method: "GET", // Método HTTP
  url: "/api/roles/permisos/123", // URL solicitada
  params: { idUsuario: "123" }, // Parámetros de la URL
  query: { page: 1, limit: 10 }, // Query strings (?page=1&limit=10)
  body: { nombre: "Juan" }, // Datos enviados en POST/PUT
  headers: {
    // Cabeceras HTTP
    authorization: "Bearer eyJhbGci...",
    "content-type": "application/json",
    "user-agent": "Mozilla/5.0...",
  },
  user: undefined, // ← Lo agregamos nosotros en el middleware
};
```

---

#### **`res` (Response - Respuesta)**

Es un objeto para ENVIAR la respuesta al cliente:

```javascript
// Enviar respuesta exitosa
res.json({ success: true, data: [...] });

// Enviar error
res.status(401).json({ success: false, message: "No autorizado" });

// Enviar diferentes códigos de estado
res.status(200).json(...)  // OK
res.status(201).json(...)  // Created
res.status(400).json(...)  // Bad Request
res.status(401).json(...)  // Unauthorized
res.status(403).json(...)  // Forbidden
res.status(404).json(...)  // Not Found
res.status(500).json(...)  // Internal Server Error
```

---

#### **`next` (Siguiente función)**

Es una función que dice "continúa con el siguiente middleware o controller":

```javascript
// Caso 1: Token válido → continuar
const authenticateToken = (req, res, next) => {
  const token = obtenerToken(req);

  if (tokenValido(token)) {
    req.user = decoded;
    next(); // ✅ "Todo bien, continúa"
  }
};

// Caso 2: Token inválido → NO continuar
const authenticateToken = (req, res, next) => {
  const token = obtenerToken(req);

  if (!token) {
    return res.status(401).json({ message: "Token requerido" });
    // ❌ NO llama next(), la petición termina aquí
  }
};
```

---

### **Flujo visual:**

```
Petición → Middleware 1 → Middleware 2 → Controller → Respuesta
           ↓              ↓               ↓
           next()         next()          res.json()
```

**Ejemplo completo:**

```javascript
// Ruta con múltiples middlewares
router.get(
  "/admin/usuarios",
  authenticateToken, // ← Middleware 1: Valida token
  requireRole(["A"]), // ← Middleware 2: Valida rol de administrador
  getUsuarios // ← Controller: Obtiene los usuarios
);

// ──────────────────────────────────────────────────

// Middleware 1
const authenticateToken = (req, res, next) => {
  const token = req.headers.authorization?.split(" ")[1];

  if (!token) {
    return res.status(401).json({ message: "Token requerido" });
    // ❌ STOP - No llama next()
  }

  const decoded = jwt.verify(token, secret);
  req.user = decoded;
  next(); // ✅ Continúa al Middleware 2
};

// Middleware 2
const requireRole = (roles) => {
  return (req, res, next) => {
    if (!roles.includes(req.user.role)) {
      return res.status(403).json({ message: "Sin permisos" });
      // ❌ STOP - No llama next()
    }
    next(); // ✅ Continúa al Controller
  };
};

// Controller
const getUsuarios = async (req, res) => {
  const usuarios = await Usuario.find();
  res.json({ success: true, data: usuarios });
  // ✅ Envía respuesta - FIN
};
```

---

## **6. ¿Por qué usamos `authHeader && authHeader.split(" ")[1]`?**

### **Formato estándar de Authorization:**

El header `Authorization` sigue un formato específico:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
               ↑      ↑
               │      └─ Token JWT
               └─ Tipo de autenticación
```

---

### **Desglose del código:**

```javascript
const authHeader = req.headers["authorization"];
// authHeader = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

const token = authHeader && authHeader.split(" ")[1];
```

---

### **Paso a paso:**

#### **1. `req.headers["authorization"]`**

Obtiene el valor del header Authorization:

```javascript
// Si el cliente envió el header:
authHeader = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";

// Si NO envió el header:
authHeader = undefined;
```

---

#### **2. `authHeader &&`**

Es un **operador de cortocircuito**:

```javascript
// Si authHeader existe (no es null/undefined)
authHeader && authHeader.split(" "); // ✅ Ejecuta split()

// Si authHeader NO existe
undefined && authHeader.split(" "); // ❌ Retorna undefined (no ejecuta split)
```

**¿Por qué?** Para evitar este error:

```javascript
// Sin el &&
const token = authHeader.split(" ")[1];
// ❌ ERROR: Cannot read property 'split' of undefined
```

---

#### **3. `.split(" ")[1]`**

Divide el string por espacios y toma la segunda parte:

```javascript
const authHeader = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";

authHeader.split(" ");
// Retorna: ["Bearer", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."]
//           ↑ [0]     ↑ [1]

authHeader.split(" ")[1];
// Retorna: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
// ✅ Solo el token, sin "Bearer"
```

---

### **Casos de uso:**

```javascript
// ✅ Caso 1: Header correcto
const authHeader = "Bearer eyJhbGci...";
const token = authHeader && authHeader.split(" ")[1];
// token = "eyJhbGci..."

// ✅ Caso 2: Sin header
const authHeader = undefined;
const token = authHeader && authHeader.split(" ")[1];
// token = undefined

// ✅ Caso 3: Header sin "Bearer"
const authHeader = "eyJhbGci...";
const token = authHeader && authHeader.split(" ")[1];
// token = undefined (porque split retorna solo 1 elemento)

// ❌ Caso 4: Header malformado
const authHeader = "InvalidFormat";
const token = authHeader && authHeader.split(" ")[1];
// token = undefined
```

---

### **¿Por qué usar el formato "Bearer"?**

Es el **estándar RFC 6750** para tokens JWT:

```
Authorization: <tipo> <credenciales>
```

Tipos comunes:

- `Bearer` - Para tokens JWT (el más común)
- `Basic` - Para usuario:contraseña en Base64
- `Digest` - Para autenticación digest
- `AWS4-HMAC-SHA256` - Para AWS

---

### **Código completo explicado:**

```javascript
export const authenticateToken = (req, res, next) => {
  try {
    // 1. Obtener header Authorization
    const authHeader = req.headers["authorization"];
    // Ejemplo: "Bearer eyJhbGci..."

    // 2. Extraer solo el token (sin "Bearer")
    const token = authHeader && authHeader.split(" ")[1];
    // token = "eyJhbGci..."

    // 3. Validar que existe el token
    if (!token) {
      return res.status(401).json({
        success: false,
        message: "Token de acceso requerido",
      });
    }

    // 4. Verificar el token con la clave secreta
    const jwtConfig = datosGlobales.getJWTConfig();
    jwt.verify(token, jwtConfig.secret, (err, user) => {
      if (err) {
        // Token inválido o expirado
        return res.status(401).json({
          success: false,
          message: "Token inválido",
        });
      }

      // 5. Token válido - Guardar datos del usuario
      req.user = user;

      // 6. Continuar con el siguiente middleware/controller
      next();
    });
  } catch (error) {
    console.error("Error en authenticateToken:", error);
    res.status(500).json({
      success: false,
      message: "Error interno del servidor",
    });
  }
};
```

---

## 🎯 Resumen Ejecutivo

| Pregunta                | Respuesta Corta                                                    |
| ----------------------- | ------------------------------------------------------------------ |
| **1. Clave secreta**    | Firma y valida tokens. Sin ella, no hay seguridad.                 |
| **2. usuarioBean**      | Se obtiene de la BD después de validar usuario/contraseña en login |
| **3. Datos en payload** | Los mínimos necesarios: id, role. Los demás son para rendimiento   |
| **4. Ruta protegida**   | Endpoint que requiere token válido para acceder                    |
| **5. Parámetros**       | req=petición, res=respuesta, next=continuar                        |
| **6. split(" ")[1]**    | Extrae el token de "Bearer TOKEN" → solo "TOKEN"                   |

---

**¿Más dudas?** ¡Pregunta lo que necesites! 🚀
