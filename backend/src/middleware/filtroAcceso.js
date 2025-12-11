/**
 * Intercepta todas las peticiones para verificar autenticación
 * Este archivo funciona como un filtro global
 * que intercepta todas las peticiones entrantes y verifica si el usuario está autenticado antes de permitir
 * el acceso a las rutas protegidas.
 */

import administraUsuariosAreas from "../services/administraUsuariosAreas.js";

const filtroAcceso = async (req, res, next) => {
  try {
    // Rutas que no requieren autenticación
    const rutasPublicas = ["/api/auth/login", "/api/auth/logout"];

    // Si es una ruta pública, continuar sin verificación
    if (rutasPublicas.includes(req.path)) {
      return next();
    }

    // Verificar si hay token en headers
    const authHeader = req.headers.authorization;

    if (!authHeader) {
      return res.status(401).json({
        success: false,
        message: "Token de acceso requerido",
      });
      console.log("Token de acceso requerido");
    }

    // Extraer token del header "Bearer TOKEN"
    const token = authHeader.split(" ")[1];

    if (!token) {
      return res.status(401).json({
        success: false,
        message: "Formato de token inválido",
      });
    }

    // Validar token
    const usuario = administraUsuariosAreas.validarToken(token);

    if (!usuario) {
      return res.status(401).json({
        success: false,
        message: "Token inválido o expirado",
      });
    }

    // Agregar usuario a la request para uso posterior
    req.usuario = usuario;
    req.sesion = {
      usuario: usuario,
      logueado: true,
    };

    // Continuar con la siguiente función
    next();
  } catch (error) {
    console.error("Error en filtro de acceso:", error);
    res.status(500).json({
      success: false,
      message: "Error interno del servidor",
    });
  }
};

/**
 * Middleware específico para verificar rol de administrador
 */
const verificarAdmin = (req, res, next) => {
  if (!req.usuario || req.usuario.role !== "admin") {
    return res.status(403).json({
      success: false,
      message: "Acceso denegado. Se requieren permisos de administrador",
    });
  }
  next();
};

/**
 * Middleware para logging de accesos (equivalente a doBeforeProcessing)
 */
const logAcceso = (req, res, next) => {
  const timestamp = new Date().toISOString();
  const ip = req.ip || req.connection.remoteAddress;
  const userAgent = req.get("User-Agent");

  console.log(
    `[${timestamp}] ${req.method} ${req.path} - IP: ${ip} - User-Agent: ${userAgent}`
  );

  if (req.usuario) {
    console.log(
      `[${timestamp}] Usuario autenticado: ${req.usuario.username} (${req.usuario.role})`
    );
  }

  next();
};

const manejoErrores = (error, req, res, next) => {
  const timestamp = new Date().toISOString();
  console.error(`[${timestamp}] Error en ${req.method} ${req.path}:`, error);

  // Si ya se envió una respuesta, pasar al siguiente handler
  if (res.headersSent) {
    return next(error);
  }

  // Determinar código de estado
  let statusCode = 500;
  let message = "Error interno del servidor";

  if (error.name === "ValidationError") {
    statusCode = 400;
    message = "Datos de entrada inválidos";
  } else if (error.name === "UnauthorizedError") {
    statusCode = 401;
    message = "No autorizado";
  } else if (error.name === "ForbiddenError") {
    statusCode = 403;
    message = "Acceso denegado";
  }

  res.status(statusCode).json({
    success: false,
    message: message,
    ...(process.env.NODE_ENV === "development" && { stack: error.stack }),
  });
};

export { filtroAcceso, verificarAdmin, logAcceso, manejoErrores };
