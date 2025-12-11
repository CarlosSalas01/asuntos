import administradorDataSource from "../config/administradorDataSource.js";
import {
  contarAsuntos,
  contarReuniones,
  contarAcuerdos,
  formatearFecha,
} from "../database/asuntosQueries.js";

/**
 * Controlador para la consulta general de asuntos
 * Equivalente a BusquedaGeneral.java líneas 22-45
 * Basado en DelegadoNegocio.buscarAsuntos() líneas 672-701
 */

/**
 * POST /api/busqueda-general
 * Obtiene el conteo de asuntos por tipo según los filtros aplicados
 * Equivalente al servlet BusquedaGeneral.java
 * @param {Object} req - Request object
 * @param {Object} res - Response object
 */
export const obtenerDatosBusqueda = async (req, res) => {
  try {
    console.log("🔍 === POST /api/busqueda-general ===");
    console.log("� Body recibido:", req.body);

    // Extraer parámetros de filtro del body (POST)
    const {
      fechas = "fechaingreso", // Tipo de fecha
      fecha1, // Fecha inicio
      fecha2, // Fecha fin
      areaFiltro = "0", // ID del área (0 = todas)
      texto = "", // Texto de búsqueda
    } = req.body;

    console.log("📝 Filtros aplicados:", {
      fechas,
      fecha1,
      fecha2,
      areaFiltro,
      texto: texto ? "***" : "Sin texto",
    });

    // Construir filtros para las consultas - formato exacto del sistema original
    const filtros = {
      fechas, // Tipo de fecha (fechaingreso, fechaatencion)
      fecha1: fecha1 ? formatearFecha(fecha1) : null,
      fecha2: fecha2 ? formatearFecha(fecha2) : null,
      areaFiltro: areaFiltro,
      texto: texto ? texto.trim() : "",
    };

    console.log("🔧 Filtros procesados:", filtros);

    // Ejecutar las 3 consultas específicas según DelegadoNegocio.buscarAsuntos()
    const resultados = await Promise.all([
      contarAsuntos("K", filtros), // SIA
      contarAsuntos("C", filtros), // CORREOS
      contarAsuntos("M", filtros), // COMISIONES
      contarReuniones(filtros), // REUNIONES (específico)
      contarAcuerdos(filtros), // ACUERDOS (tabla accion)
    ]);

    // Formatear respuesta exactamente como ElementoBusqueda del sistema original
    const resultadosBusqueda = [
      { tipoAsunto: "K", descripcion: "SIA", cantidad: resultados[0] },
      { tipoAsunto: "C", descripcion: "CORREOS", cantidad: resultados[1] },
      { tipoAsunto: "M", descripcion: "COMISIONES", cantidad: resultados[2] },
      { tipoAsunto: "R", descripcion: "REUNIONES", cantidad: resultados[3] },
      { tipoAsunto: "A", descripcion: "ACUERDOS", cantidad: resultados[4] },
    ];

    // Respuesta en formato compatible con el sistema original
    res.json(resultadosBusqueda);
  } catch (error) {
    console.error("Error en consulta general:", error);
    res.status(500).json({
      success: false,
      error: "Error interno del servidor",
      message: error.message,
    });
  }
};

/**
 * Obtiene las áreas disponibles para consulta
 */
export const obtenerAreasConsulta = async (req, res) => {
  const pool = administradorDataSource.getPool();

  try {
    const query = `
      SELECT DISTINCT a.idarea, a.nombre
      FROM controlasuntospendientesnew.area a
      INNER JOIN controlasuntospendientesnew.responsable r ON a.idarea = r.idarea
      ORDER BY a.nombre
    `;

    const result = await pool.query(query);

    res.json({
      success: true,
      areas: result.rows,
    });
  } catch (error) {
    console.error("❌ Error obteniendo áreas:", error);
    res.status(500).json({
      success: false,
      error: "Error obteniendo áreas",
    });
  }
};

export default {
  obtenerDatosBusqueda,
  obtenerAreasConsulta,
};
