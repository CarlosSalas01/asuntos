# Sistema Asuntos

Sistema de gestión integral para el manejo de asuntos y casos, construido con React en el frontend y Node.js en el backend.

## 🚀 Estructura del Proyecto

```
asuntos_new/
├── frontend/          # Aplicación React
│   ├── src/
│   │   ├── components/   # Componentes reutilizables
│   │   ├── pages/        # Páginas de la aplicación
│   │   ├── hooks/        # Custom hooks
│   │   ├── services/     # Servicios para API calls
│   │   └── types/        # Definiciones de tipos
│   ├── package.json
│   └── vite.config.js
├── backend/           # API REST con Node.js
│   ├── src/
│   │   ├── controllers/  # Controladores
│   │   ├── routes/       # Rutas de la API
│   │   ├── services/     # Lógica de negocio
│   │   ├── middleware/   # Middlewares
│   │   ├── models/       # Modelos de datos
│   │   └── config/       # Configuraciones
│   └── package.json
└── README.md
```

## 🛠️ Tecnologías Utilizadas

### Frontend

- **React 18** - Librería de interfaz de usuario
- **Vite** - Build tool y dev server
- **React Router DOM** - Enrutamiento
- **Axios** - Cliente HTTP
- **JavaScript (ES6+)** - Lenguaje de programación

### Backend

- **Node.js** - Entorno de ejecución
- **Express** - Framework web
- **JavaScript (ES6+)** - Lenguaje de programación
- **CORS** - Cross-Origin Resource Sharing
- **Helmet** - Seguridad HTTP
- **Morgan** - Logging de peticiones

## 📋 Prerrequisitos

- Node.js (versión 16 o superior)
- npm o yarn

## 🚀 Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd asuntos_new
```

### 2. Instalar todas las dependencias

```bash
npm run install:all
```

Este comando instalará las dependencias del proyecto raíz, frontend y backend automáticamente.

### 3. Configurar el Backend

Crear archivo de variables de entorno:

```bash
cd backend
cp .env.example .env
```

Editar el archivo `.env` con tus configuraciones:

```env
NODE_ENV=development
PORT=8000
JWT_SECRET=tu_jwt_secret_aqui
CORS_ORIGIN=http://localhost:3000
```

## 🎯 Ejecutar la Aplicación

### Desarrollo (Recomendado)

#### Ejecutar Frontend y Backend simultáneamente

```bash
npm run dev
```

- Backend estará disponible en: http://localhost:8000
- Frontend estará disponible en: http://localhost:3000

#### Ejecutar por separado (opcional)

**Backend (Terminal 1)**

```bash
npm run dev:backend
```

**Frontend (Terminal 2)**

```bash
npm run dev:frontend
```

### Producción

#### Construir Frontend

```bash
npm run build
```

#### Ejecutar Backend

```bash
npm start
```

## 📝 Scripts Disponibles

| Script                 | Descripción                                      |
| ---------------------- | ------------------------------------------------ |
| `npm run dev`          | Ejecuta frontend y backend simultáneamente       |
| `npm run dev:frontend` | Solo ejecuta el frontend                         |
| `npm run dev:backend`  | Solo ejecuta el backend                          |
| `npm run install:all`  | Instala dependencias en raíz, frontend y backend |
| `npm run build`        | Construye el frontend para producción            |
| `npm start`            | Ejecuta el backend en modo producción            |

## 📡 API Endpoints

### Asuntos

| Método | Endpoint           | Descripción                  |
| ------ | ------------------ | ---------------------------- |
| GET    | `/api/asuntos`     | Obtener todos los asuntos    |
| GET    | `/api/asuntos/:id` | Obtener un asunto específico |
| POST   | `/api/asuntos`     | Crear un nuevo asunto        |
| PUT    | `/api/asuntos/:id` | Actualizar un asunto         |
| DELETE | `/api/asuntos/:id` | Eliminar un asunto           |

### Ejemplo de Asunto

```json
{
  "id": 1,
  "titulo": "Asunto de Ejemplo",
  "descripcion": "Descripción del asunto",
  "estado": "Activo",
  "fecha": "2024-01-15",
  "fechaCreacion": "2024-01-15T10:00:00Z",
  "fechaActualizacion": "2024-01-15T10:00:00Z"
}
```

## 🎨 Características

### Frontend

- ✅ Interfaz moderna y responsiva
- ✅ Navegación con React Router
- ✅ Gestión de estado local con hooks
- ✅ Comunicación con API mediante Axios
- ✅ Componentes reutilizables

### Backend

- ✅ API REST completa
- ✅ Middleware de seguridad
- ✅ Manejo de errores centralizado
- ✅ Logging de peticiones
- ✅ CORS configurado
- ✅ Estructura modular

## 🔧 Próximas Mejoras

- [ ] Integración con base de datos (PostgreSQL/MongoDB)
- [ ] Autenticación y autorización
- [ ] Validación de datos
- [ ] Tests unitarios e integración
- [ ] Documentación de API con Swagger
- [ ] Despliegue con Docker
- [ ] CI/CD pipeline

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📝 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## 👥 Autores

- Tu Nombre - _Desarrollo inicial_

## 📞 Soporte

Si tienes alguna pregunta o necesitas ayuda, por favor abre un issue en el repositorio.
