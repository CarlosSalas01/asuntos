/**
 * Middleware de autenticación JWT
 * Verifica tokens JWT para proteger rutas
 */

import jwt from "jsonwebtoken";
import datosGlobales from "../config/datosGlobales.js";

/**
 * Middleware para verificar token JWT
 * @param {Object} req - Request object
 * @param {Object} res - Response object
 * @param {Function} next - Next function
 */
export const authenticateToken = (req, res, next) => {
  try {
    // Obtener token del header Authorization
    const authHeader = req.headers["authorization"];
    const token = authHeader && authHeader.split(" ")[1]; // Bearer TOKEN

    if (!token) {
      return res.status(401).json({
        success: false,
        message: "Token de acceso requerido",
      });
    }

    // Verificar token
    const jwtConfig = datosGlobales.getJWTConfig();

    jwt.verify(token, jwtConfig.secret, (err, user) => {
      if (err) {
        if (err.name === "TokenExpiredError") {
          return res.status(401).json({
            success: false,
            message: "Token expirado",
          });
        }

        return res.status(403).json({
          success: false,
          message: "Token inválido",
        });
      }

      // Agregar usuario a la request
      req.user = user;
      next();
    });
  } catch (error) {
    console.error("Error en authenticateToken:", error);
    res.status(500).json({
      success: false,
      message: "Error interno del servidor",
    });
  }
};

/**
 * Middleware para verificar roles específicos
 * @param {Array} roles - Array de roles permitidos
 */
export const requireRole = (roles) => {
  return (req, res, next) => {
    if (!req.user) {
      return res.status(401).json({
        success: false,
        message: "Usuario no autenticado",
      });
    }

    if (!roles.includes(req.user.role)) {
      return res.status(403).json({
        success: false,
        message: "No tiene permisos para acceder a este recurso",
      });
    }

    next();
  };
};

/**
 * Middleware opcional que intenta verificar token pero no bloquea si no existe
 * @param {Object} req - Request object
 * @param {Object} res - Response object
 * @param {Function} next - Next function
 */
export const optionalAuth = (req, res, next) => {
  try {
    const authHeader = req.headers["authorization"];
    const token = authHeader && authHeader.split(" ")[1];

    if (token) {
      const jwtConfig = datosGlobales.getJWTConfig();

      jwt.verify(token, jwtConfig.secret, (err, user) => {
        if (!err) {
          req.user = user;
        }
        // Continúa independientemente del resultado
        next();
      });
    } else {
      next();
    }
  } catch (error) {
    console.error("Error en optionalAuth:", error);
    next(); // Continúa aunque haya error
  }
};
