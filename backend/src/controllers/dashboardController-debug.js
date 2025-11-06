/**
 * Controlador del Dashboard - Versión de Debug
 * Version simplificada para identificar problemas paso a paso
 */

export const getResumenInicio = async (req, res) => {
  console.log("🔍 === DEBUG: getResumenInicio llamado ===");
  console.log("📊 Query params:", req.query);

  try {
    const { tipo = "0", otroAnio, idAdjunta = "1" } = req.query;

    console.log("📋 Parámetros parseados:", { tipo, otroAnio, idAdjunta });

    // Respuesta de prueba simple
    const respuesta = [
      {
        fechaHora: "Miércoles, 6 de noviembre de 2024 a las 10:30 horas",
        atendidosTodos: 150,
        pendientesTodos: 75,
        totalGral: 225,
        reunionesSA: 12,
        // Datos de área de prueba
        atendidos: 45,
        pendientes: 30,
        vencidos: 5,
        porvencer: 10,
        sinvencer: 15,
        siglas: "TEST",
        idarea: "1",
      },
    ];

    console.log("✅ Enviando respuesta de prueba:", respuesta);
    res.json(respuesta);
  } catch (error) {
    console.error("❌ Error en getResumenInicio:", error);
    res.status(500).json({
      error: "Error interno del servidor",
      message: error.message,
      stack: process.env.NODE_ENV === "development" ? error.stack : undefined,
    });
  }
};

export const getAreaSuperior = async (req, res) => {
  console.log("🔍 === DEBUG: getAreaSuperior llamado ===");
  console.log("📊 Query params:", req.query);

  try {
    const { idarea, nivel } = req.query;
    console.log("✅ Retornando idarea:", idarea);
    res.json(idarea);
  } catch (error) {
    console.error("❌ Error en getAreaSuperior:", error);
    res.status(500).json({
      error: "Error interno del servidor",
      message: error.message,
    });
  }
};
