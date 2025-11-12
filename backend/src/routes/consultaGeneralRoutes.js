import express from "express";
import consultaGeneralController from "../controllers/consultaGeneralController.js";

const router = express.Router();

// POST /api/busqueda-general
// Ruta exacta según MAPEO_ARCHIVOS_MIGRACION.md - equivalente a busquedaGeneral.do
router.post("/", consultaGeneralController.obtenerDatosBusqueda);

// GET /api/busqueda-general/areas - para obtener áreas disponibles
router.get("/areas", consultaGeneralController.obtenerAreasConsulta);

export default router;
