/**
 * Rutas del Dashboard - Equivalente a resumenInicio.do
 */

import express from "express";
import {
  getResumenInicio,
  getAreaSuperior,
  getDetallesPendientes,
} from "../controllers/dashboardController.js";
import { authenticateToken } from "../middleware/authMiddleware.js";

const router = express.Router();

/**
 * GET /api/resumen-inicio
 * Obtiene el resumen del dashboard principal
 * Query params: tipo, otroAnio, idAdjunta, idarea
 */
// Temporalmente sin autenticación para pruebas
router.get("/resumen-inicio", getResumenInicio);

/**
 * GET /api/area-superior
 * Obtiene el área superior de una área dada
 * Query params: idarea, nivel
 */
// Temporalmente sin autenticación para pruebas
router.get("/area-superior", getAreaSuperior);

/**
 * GET /api/dashboard/pendientes-detalle
 * Obtiene el detalle de pendientes por tipo para un área específica
 * Query params: idarea, fechaInicio, fechaFin
 */
// Temporalmente sin autenticación para pruebas
router.get("/pendientes-detalle", getDetallesPendientes);

export default router;
