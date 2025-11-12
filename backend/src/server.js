import express from "express";
import cors from "cors";
import helmet from "helmet";
import morgan from "morgan";
import dotenv from "dotenv";

import authRoutes from "./routes/auth.js";
import dashboardRoutes from "./routes/dashboardRoutes.js";
import consultaGeneralRoutes from "./routes/consultaGeneralRoutes.js";
import administradorDataSource from "./config/administradorDataSource.js";

// Configuración de variables de entorno
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
    version: "1.0.0",
    status: "active",
  });
});

// Endpoint de prueba para dashboard - COMENTADO para usar rutas reales
/*
app.get("/api/resumen-inicio", (req, res) => {
  console.log("🔍 Endpoint /api/resumen-inicio llamado con query:", req.query);

  try {
    // Respuesta de prueba para verificar conectividad
    const respuestaPrueba = [
      {
        fechaHora: "Miércoles, 6 de noviembre de 2024 a las 10:30 horas",
        atendidosTodos: 150,
        pendientesTodos: 75,
        totalGral: 225,
        reunionesSA: 12,
      },
    ];

    console.log("✅ Enviando respuesta de prueba");
    res.json(respuestaPrueba);
  } catch (error) {
    console.error("❌ Error en endpoint de prueba:", error);
    res.status(500).json({
      error: "Error interno del servidor",
      message: error.message,
    });
  }
});
*/

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
