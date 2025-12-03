import express from "express";
import { verifyToken } from "../controllers/authController.js";
import AsuntoDAO from "../dao/AsuntoDAO.js";

const router = express.Router();

// TEMPORALMENTE DESACTIVADO: Todas las rutas requieren autenticación
// router.use(verifyToken);

router.get("/asuntos", async (req, res) => {
  const dao = new AsuntoDAO();
  try {
    const asuntos = await dao.obtenerAsuntosTablaSIA();
    res.json(asuntos);
  } catch (error) {
    res.status(500).json({ error: "Error al obtener asuntos" });
  }
});

export default router;
