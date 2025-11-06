import * as asuntosService from "../services/asuntosService.js";

// Obtener todos los asuntos
export const obtenerAsuntos = async (req, res, next) => {
  try {
    const asuntos = await asuntosService.obtenerTodosLosAsuntos();
    res.json({
      success: true,
      data: asuntos,
      count: asuntos.length,
    });
  } catch (error) {
    next(error);
  }
};

// Obtener un asunto por ID
export const obtenerAsuntoPorId = async (req, res, next) => {
  try {
    const { id } = req.params;
    const asunto = await asuntosService.obtenerAsuntoPorId(id);

    if (!asunto) {
      return res.status(404).json({
        success: false,
        message: "Asunto no encontrado",
      });
    }

    res.json({
      success: true,
      data: asunto,
    });
  } catch (error) {
    next(error);
  }
};

// Crear un nuevo asunto
export const crearAsunto = async (req, res, next) => {
  try {
    const nuevoAsunto = await asuntosService.crearAsunto(req.body);
    res.status(201).json({
      success: true,
      message: "Asunto creado exitosamente",
      data: nuevoAsunto,
    });
  } catch (error) {
    next(error);
  }
};

// Actualizar un asunto
export const actualizarAsunto = async (req, res, next) => {
  try {
    const { id } = req.params;
    const asuntoActualizado = await asuntosService.actualizarAsunto(
      id,
      req.body
    );

    if (!asuntoActualizado) {
      return res.status(404).json({
        success: false,
        message: "Asunto no encontrado",
      });
    }

    res.json({
      success: true,
      message: "Asunto actualizado exitosamente",
      data: asuntoActualizado,
    });
  } catch (error) {
    next(error);
  }
};

// Eliminar un asunto
export const eliminarAsunto = async (req, res, next) => {
  try {
    const { id } = req.params;
    const eliminado = await asuntosService.eliminarAsunto(id);

    if (!eliminado) {
      return res.status(404).json({
        success: false,
        message: "Asunto no encontrado",
      });
    }

    res.json({
      success: true,
      message: "Asunto eliminado exitosamente",
    });
  } catch (error) {
    next(error);
  }
};
