/**
 * Controller de Asuntos - Maneja operaciones CRUD y consultas de asuntos
 *
 * FUNCIONALIDADES:
 * - Consultas generales de asuntos
 * - Consultas específicas de SIA (Sistema Integral de Asuntos)
 * - Exportación de datos (HTML/CSV)
 * - CRUD de asuntos (en desarrollo)
 */

import AsuntoDAO from "../dao/AsuntoDAO.js";

const asuntoDAO = new AsuntoDAO();

// Obtener todos los asuntos
export const obtenerAsuntos = async (req, res, next) => {
  try {
    // TODO: Implementar método en AsuntoDAO para obtener todos los asuntos
    res.status(501).json({
      success: false,
      message:
        "Endpoint en desarrollo. Use /api/dashboard o /api/consulta-general para consultas de asuntos.",
      info: "Este endpoint será implementado cuando se requiera funcionalidad CRUD completa de asuntos.",
    });
  } catch (error) {
    next(error);
  }
};

// Obtener un asunto por ID
export const obtenerAsuntoPorId = async (req, res, next) => {
  try {
    const { id } = req.params;

    // TODO: Implementar método en AsuntoDAO
    res.status(501).json({
      success: false,
      message:
        "Endpoint en desarrollo. Use /api/consulta-general para búsquedas específicas.",
      info: "Este endpoint será implementado cuando se requiera funcionalidad CRUD completa de asuntos.",
    });
  } catch (error) {
    next(error);
  }
};

// Crear un nuevo asunto
export const crearAsunto = async (req, res, next) => {
  try {
    // TODO: Implementar método de creación en AsuntoDAO
    res.status(501).json({
      success: false,
      message: "Endpoint en desarrollo.",
      info: "La creación de asuntos se implementará según los requerimientos del negocio.",
    });
  } catch (error) {
    next(error);
  }
};

// Actualizar un asunto
export const actualizarAsunto = async (req, res, next) => {
  try {
    const { id } = req.params;

    // TODO: Implementar método de actualización en AsuntoDAO
    res.status(501).json({
      success: false,
      message: "Endpoint en desarrollo.",
      info: "La actualización de asuntos se implementará según los requerimientos del negocio.",
    });
  } catch (error) {
    next(error);
  }
};

// Eliminar un asunto
export const eliminarAsunto = async (req, res, next) => {
  try {
    const { id } = req.params;

    // TODO: Implementar método de eliminación en AsuntoDAO
    res.status(501).json({
      success: false,
      message: "Endpoint en desarrollo.",
      info: "La eliminación de asuntos se implementará según los requerimientos del negocio.",
    });
  } catch (error) {
    next(error);
  }
};
