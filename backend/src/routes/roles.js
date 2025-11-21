import express from "express";
import {
  obtenerRolesUsuario,
  seleccionarRol,
} from "../controllers/rolesController.js";
import { verifyToken } from "../controllers/authController.js";

const router = express.Router();

// Todas las rutas requieren autenticación
router.use(verifyToken);

// GET /api/roles - Obtener todos los roles/permisos del usuario
router.get("/", obtenerRolesUsuario);

// POST /api/roles/seleccionar - Seleccionar un rol específico
router.post("/seleccionar", seleccionarRol);

export default router;
