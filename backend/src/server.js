//Express es para crear el servidor y manejar rutas
import express from "express";
//Cors es para permitir solicitudes desde otros dominios, por ejemplo, desde el frontend
import cors from "cors";
//Helmet ayuda a proteger la app configurando cabeceras HTTP (en este caso, lo usamos para seguridad básica)
import helmet from "helmet";
//Morgan es para logging de peticiones HTTP (aquí usamos el formato 'combined' que es para logs detallados)
import morgan from "morgan";
//Dotenv es para cargar variables de entorno desde un archivo .env
import dotenv from "dotenv";

import authRoutes from "./routes/auth.js";
import dashboardRoutes from "./routes/dashboardRoutes.js";
import consultaGeneralRoutes from "./routes/consultaGeneralRoutes.js";
import administradorDataSource from "./config/administradorDataSource.js";
import datosGlobales from "./config/datosGlobales.js";

// Configuración de variables de entorno. Las variables de entorno vienen siendo cargadas desde un archivo .env en el root del proyecto
dotenv.config();

const app = express();
const PORT = process.env.PORT || 9001;

// Middlewares
app.use(helmet()); // Seguridad
app.use(cors()); // CORS
app.use(morgan("combined")); // Logging
app.use(express.json()); // Parser JSON
app.use(express.urlencoded({ extended: true })); // Parser URL

// Rutas
app.get("/", (req, res) => {
  res.json({
    message: "API del Sistema de Asuntos",
    version: datosGlobales.VERSION,
    versionOriginal: datosGlobales.VERSION_ORIGINAL,
    versionMigracion: datosGlobales.VERSION_MIGRACION,
    status: "active",
    migration: "Java/JSP → Node.js/React",
  });
});

app.use("/api/auth", authRoutes);
app.use("/api/dashboard", dashboardRoutes);
app.use("/api/busqueda-general", consultaGeneralRoutes);

// Manejo de rutas no encontradas
app.use("*", (req, res) => {
  res.status(404).json({
    error: "Ruta no encontrada",
    message: `La ruta ${req.originalUrl} no existe`,
  });
});

// Manejo de errores no capturados
process.on("uncaughtException", (error) => {
  console.error("❌ Error no capturado:", error);
});

process.on("unhandledRejection", (reason, promise) => {
  console.error("❌ Promesa rechazada:", reason);
});

app.listen(PORT, async () => {
  console.log(`🚀 Servidor corriendo en puerto ${PORT}`);
  console.log(`📍 URL: http://localhost:${PORT}`);

  // Inicializar conexión a base de datos
  try {
    console.log(`🔌 Inicializando conexión a base de datos...`);
    await administradorDataSource.testConnection();
    console.log(`✅ Conexión a base de datos establecida`);
  } catch (error) {
    console.warn(
      `⚠️  Advertencia: No se pudo establecer conexión a BD:`,
      error.message
    );
    console.log(`🔄 El sistema funcionará con usuarios de prueba`);
  }
});
