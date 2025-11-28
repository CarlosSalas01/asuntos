/**
 * Rutas de Asuntos
 *
 * ENDPOINTS ACTIVOS:
// Comentarios de endpoints SIA eliminados
 *
 * ENDPOINTS EN DESARROLLO:
 * - CRUD básico (GET, POST, PUT, DELETE)
 */

import express from "express";
import * as asuntosController from "../controllers/asuntosController.js";

const router = express.Router();

// ============================================================================
// CRUD BÁSICO - EN DESARROLLO
// ============================================================================

// GET /api/asuntos - Obtener todos los asuntos
router.get("/", asuntosController.obtenerAsuntos);

// GET /api/asuntos/:id - Obtener un asunto específico
router.get("/:id", asuntosController.obtenerAsuntoPorId);

// POST /api/asuntos - Crear un nuevo asunto
router.post("/", asuntosController.crearAsunto);

// PUT /api/asuntos/:id - Actualizar un asunto
router.put("/:id", asuntosController.actualizarAsunto);

// DELETE /api/asuntos/:id - Eliminar un asunto
router.delete("/:id", asuntosController.eliminarAsunto);

export default router;
