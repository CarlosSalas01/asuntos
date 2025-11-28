/**
 * Rutas del Dashboard - Equivalente a resumenInicio.do
 */

import express from "express";
import {
  getResumenInicio,
  getAreaSuperior,
  getDetallesPendientes,
  getTotalesPostgreSQL,
  getTotalesAPIExterna,
} from "../controllers/dashboardController.js";
import { authenticateToken } from "../middleware/authMiddleware.js";

const router = express.Router();

router.get("/resumen-inicio", getResumenInicio);
router.get("/area-superior", getAreaSuperior);
router.get("/pendientes-detalle", getDetallesPendientes);
router.get("/totales-postgresql", getTotalesPostgreSQL);
router.get("/totales", getTotalesAPIExterna);

export default router;
