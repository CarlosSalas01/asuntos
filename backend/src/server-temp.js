import express from "express";
import cors from "cors";

const app = express();
const PORT = 5006;

// Middlewares básicos
app.use(cors());
app.use(express.json());

// Ruta de prueba
app.get("/", (req, res) => {
  res.json({ message: "Servidor temporal funcionando", port: PORT });
});

// Ruta de login simplificada
app.post("/api/auth/login", (req, res) => {
  console.log("🔐 [LOGIN TEMP] Petición recibida:", req.body);

  const { username, password } = req.body;

  if (!username || !password) {
    return res.status(400).json({
      success: false,
      message: "Username y password son requeridos",
    });
  }

  // Login simplificado - acepta cualquier credencial
  res.json({
    success: true,
    message: "Login exitoso (temporal)",
    token: "temp-token-12345",
    user: {
      id: 1,
      username: username,
      nombre: "Usuario Temporal",
      role: "admin",
    },
  });
});

// Ruta de dashboard con datos completos para AreaCard
app.get("/api/resumen-inicio", (req, res) => {
  console.log("📊 [DASHBOARD TEMP] Petición recibida");

  // Datos simulados que coinciden con la estructura real del SOLR
  const mockData = [
    {
      fechaHora: "jueves, 6 de noviembre de 2025, 02:12 p.m. horas",
      atendidosTodos: 414,
      pendientesTodos: 40,
      totalGral: 454,
      reunionesSA: 1,
    },
    {
      atendidos: 0,
      atendidosArea: "0",
      enviados: 0,
      idarea: "192",
      nivel: 0,
      pendactivos: 0,
      pendientes: 0,
      pendientesAntes: 0,
      reunionesSA: 0,
      siglas: "DATG",
      vencidos: 0,
      porvencer: 0,
      sinvencer: 0,
      totalPend: 0,
      id: "192",
      porcentajes: { vencidos: 0, porvencer: 0, sinvencer: 0 },
    },
    {
      atendidos: 112,
      atendidosArea: "112",
      enviados: 0,
      idarea: "129",
      nivel: 0,
      pendactivos: 0,
      pendientes: 8,
      pendientesAntes: 0,
      reunionesSA: 0,
      siglas: "DGAIGAT",
      vencidos: 2,
      porvencer: 0,
      sinvencer: 6,
      totalPend: 8,
      id: "129",
      porcentajes: { vencidos: 25, porvencer: 0, sinvencer: 75 },
    },
    {
      atendidos: 86,
      atendidosArea: "86",
      enviados: 0,
      idarea: "2",
      nivel: 0,
      pendactivos: 0,
      pendientes: 12,
      pendientesAntes: 0,
      reunionesSA: 0,
      siglas: "DGAIGB",
      vencidos: 1,
      porvencer: 0,
      sinvencer: 11,
      totalPend: 12,
      id: "2",
      porcentajes: { vencidos: 8.33, porvencer: 0, sinvencer: 91.67 },
    },
    {
      atendidos: 74,
      atendidosArea: "74",
      enviados: 0,
      idarea: "47",
      nivel: 0,
      pendactivos: 0,
      pendientes: 10,
      pendientesAntes: 0,
      reunionesSA: 0,
      siglas: "DGAIIG",
      vencidos: 9,
      porvencer: 0,
      sinvencer: 1,
      totalPend: 10,
      id: "47",
      porcentajes: { vencidos: 90, porvencer: 0, sinvencer: 10 },
    },
    {
      atendidos: 57,
      atendidosArea: "57",
      enviados: 0,
      idarea: "97",
      nivel: 0,
      pendactivos: 0,
      pendientes: 2,
      pendientesAntes: 0,
      reunionesSA: 0,
      siglas: "DGARNMA",
      vencidos: 2,
      porvencer: 0,
      sinvencer: 0,
      totalPend: 2,
      id: "97",
      porcentajes: { vencidos: 100, porvencer: 0, sinvencer: 0 },
    },
    {
      atendidos: 85,
      atendidosArea: "85",
      enviados: 0,
      idarea: "164",
      nivel: 0,
      pendactivos: 0,
      pendientes: 8,
      pendientesAntes: 0,
      reunionesSA: 0,
      siglas: "DMG",
      vencidos: 3,
      porvencer: 1,
      sinvencer: 4,
      totalPend: 8,
      id: "164",
      porcentajes: { vencidos: 37.5, porvencer: 12.5, sinvencer: 50 },
    },
  ];

  res.json(mockData);
});

// Manejo de errores
app.use((err, req, res, next) => {
  console.error("❌ Error:", err);
  res.status(500).json({ error: "Error interno del servidor" });
});

// Iniciar servidor
app.listen(PORT, () => {
  console.log(`🚀 Servidor temporal corriendo en puerto ${PORT}`);
  console.log(`📍 URL: http://localhost:${PORT}`);
  console.log(`🔧 Modo: TEMPORAL (sin BD)`);
});

// Manejo de señales
process.on("SIGINT", () => {
  console.log("\n🛑 Deteniendo servidor temporal...");
  process.exit(0);
});

process.on("uncaughtException", (error) => {
  console.error("❌ Error no capturado:", error);
});

process.on("unhandledRejection", (reason, promise) => {
  console.error("❌ Promesa rechazada:", reason);
});
