import express from "express";
import { verifyToken } from "../controllers/authController.js";
import AsuntoDAO from "../dao/AsuntoDAO.js";

const router = express.Router();

// Todas las rutas requieren autenticación
router.use(verifyToken);

router.get("/asuntos", async (req, res) => {
  // Recoge los filtros desde query
  const filtro = {
    idarea: req.query.idarea ? parseInt(req.query.idarea) : 0,
    fechaInicio: req.query.fechaInicio,
    fechaFinal: req.query.fechaFin,
    // Puedes agregar más filtros aquí si los necesitas
    offset: 0, // Puedes ajustar esto según paginación
    limitAll: false,
  };
  const tipo = req.query.tipoasunto || "K"; // Puedes ajustar el tipo por defecto
  const dao = new AsuntoDAO();
  try {
    const asuntos = await dao.buscarAsuntosPorAreaxTipo(filtro, tipo);
    res.json(asuntos);
  } catch (error) {
    res.status(500).json({ error: "Error al obtener asuntos" });
  }
});

export default router;
