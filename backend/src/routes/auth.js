import express from "express";
import * as authController from "../controllers/authController.js";

const router = express.Router();

// POST /api/auth/login - Iniciar sesión
router.post("/login", authController.login);

// POST /api/auth/logout - Cerrar sesión
router.post("/logout", authController.logout);

// GET /api/auth/profile - Obtener perfil del usuario (requiere autenticación)
router.get("/profile", authController.verifyToken, authController.getProfile);

export default router;
