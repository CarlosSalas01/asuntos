/**
 * Rutas del Dashboard - Versión Debug
 */

import express from "express";
import {
  getResumenInicio,
  getAreaSuperior,
} from "../controllers/dashboardController-debug.js";

const router = express.Router();

console.log("📋 Cargando rutas de dashboard debug...");

// Endpoint de debug simplificado
router.get("/resumen-inicio", (req, res, next) => {
  console.log("🎯 Ruta /resumen-inicio interceptada");
  getResumenInicio(req, res).catch(next);
});

router.get("/area-superior", (req, res, next) => {
  console.log("🎯 Ruta /area-superior interceptada");
  getAreaSuperior(req, res).catch(next);
});

console.log("✅ Rutas de dashboard debug cargadas");

export default router;
