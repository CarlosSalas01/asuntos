/**
 * Rutas para endpoints del sistema
 * Incluye health check y pruebas de conectividad
 */

import express from "express";
import {
  healthCheck,
  testDatabase,
  getPoolStats,
  listAvailableUsers,
} from "../controllers/systemController.js";

const router = express.Router();

// Health check del sistema
router.get("/health", healthCheck);

// Prueba de conexión a base de datos
router.get("/test-database", testDatabase);

// Estadísticas del pool de conexiones
router.get("/pool-stats", getPoolStats);

// Listar usuarios disponibles para login
router.get("/users", listAvailableUsers);

export default router;
