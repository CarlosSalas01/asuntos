import express from "express";
import { verifyToken } from "../controllers/authController.js";
import AsuntoDAO from "../dao/AsuntoDAO.js";

const router = express.Router();

// TEMPORALMENTE DESACTIVADO: Todas las rutas requieren autenticación
// router.use(verifyToken);

/**
 * GET /api/sia/asuntos
 * Obtiene asuntos de tipo SIA (K)
 * Query params: search, limit, offset
 */
router.get("/asuntos", async (req, res) => {
  const dao = new AsuntoDAO();
  try {
    const filtros = {
      search: req.query.search || "",
      limit: Math.min(parseInt(req.query.limit || "50", 10), 200),
      offset: parseInt(req.query.offset || "0", 10),
    };

    const asuntos = await dao.obtenerAsuntosPorTipo(filtros, "K");
    res.json(asuntos);
  } catch (error) {
    console.error("Error en /api/sia/asuntos:", error);
    res.status(500).json({ error: "Error al obtener asuntos SIA" });
  }
});

/**
 * GET /api/sia/asuntos/:tipoAsunto
 * Obtiene asuntos por tipo específico
 * Tipos válidos: K (SIA), C (Correos), R (Reuniones), A (Acuerdos), M (Comisiones)
 * Query params: search, limit, offset
 */
router.get("/asuntos/tipo/:tipoAsunto", async (req, res) => {
  const dao = new AsuntoDAO();
  try {
    const { tipoAsunto } = req.params;
    const tiposValidos = ["K", "C", "R", "A", "M"];

    if (!tiposValidos.includes(tipoAsunto.toUpperCase())) {
      return res.status(400).json({
        error: "Tipo de asunto inválido",
        tiposValidos: tiposValidos,
        descripcion: {
          K: "SIA",
          C: "Correos",
          R: "Reuniones",
          A: "Acuerdos",
          M: "Comisiones",
        },
      });
    }

    const filtros = {
      search: req.query.search || "",
      limit: Math.min(parseInt(req.query.limit || "50", 10), 200),
      offset: parseInt(req.query.offset || "0", 10),
    };

    const asuntos = await dao.obtenerAsuntosPorTipo(
      filtros,
      tipoAsunto.toUpperCase()
    );
    res.json(asuntos);
  } catch (error) {
    console.error(
      `Error en /api/sia/asuntos/tipo/${req.params.tipoAsunto}:`,
      error
    );
    res.status(500).json({ error: "Error al obtener asuntos" });
  }
});

export default router;
