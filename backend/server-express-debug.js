/**
 * Servidor Express Mínimo para Debug de Dashboard
 */

import express from "express";
import cors from "cors";
import dashboardDebugRoutes from "./src/routes/dashboardRoutes-debug.js";

const app = express();
const PORT = process.env.PORT || 5008;

console.log("🚀 Iniciando servidor Express debug...");

// Middlewares básicos
app.use(
  cors({
    origin: true,
    credentials: true,
  })
);

app.use(express.json());

// Rutas de debug
console.log("🔧 Configurando rutas de debug...");
app.use("/api", dashboardDebugRoutes);

// Ruta de prueba básica
app.get("/test", (req, res) => {
  console.log("✅ Ruta /test funcionando");
  res.json({ message: "Servidor funcionando correctamente" });
});

// Manejo de errores
app.use((err, req, res, next) => {
  console.error("❌ Error capturado:", err);
  res.status(500).json({
    error: "Error interno del servidor",
    message: err.message,
  });
});

// Iniciar servidor
const server = app.listen(PORT, () => {
  console.log(`🟢 Servidor debug corriendo en puerto ${PORT}`);
  console.log(`📍 Endpoints disponibles:`);
  console.log(`   - GET http://localhost:${PORT}/test`);
  console.log(`   - GET http://localhost:${PORT}/api/resumen-inicio`);
  console.log(`   - GET http://localhost:${PORT}/api/area-superior`);
});

// Manejo de cierre limpio
process.on("SIGINT", () => {
  console.log("\n🛑 Recibido SIGINT, cerrando servidor...");
  server.close(() => {
    console.log("✅ Servidor cerrado limpiamente");
    process.exit(0);
  });
});

process.on("SIGTERM", () => {
  console.log("\n🛑 Recibido SIGTERM, cerrando servidor...");
  server.close(() => {
    console.log("✅ Servidor cerrado limpiamente");
    process.exit(0);
  });
});

export default app;
