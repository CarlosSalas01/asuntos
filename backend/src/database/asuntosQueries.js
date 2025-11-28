/**
 * Implementa las 3 consultas específicas según MAPEO_ARCHIVOS_MIGRACION.md
 *
 * ESTRUCTURA EXACTA:
 * - contarAsuntos(tipo, filtros) - para K, C, M (AsuntoDAO.cantidadAsuntosxAreaxTipo línea 1465)
 * - contarReuniones(filtros) - específico para R (AsuntoDAO.cantidadAsuntosReunion línea 829)
 * - contarAcuerdos(filtros) - específico para A (AccionDAO.cantidadAccionesFiltro línea 291)
 */

import administradorDataSource from "../config/administradorDataSource.js";

/**
 * Cuenta asuntos por tipo (K=SIA, C=CORREOS, M=COMISIONES)
 * @param {string} tipo
 * @param {Object} filtros
 * @returns {Promise<number>}
 */
export const contarAsuntos = async (tipo, filtros) => {
  const pool = administradorDataSource.getPool();

  let query = `
    SELECT COUNT(*) as cantidad
    FROM controlasuntospendientesnew.asunto a
    WHERE a.tipoasunto = $1
  `;

  const params = [tipo];
  let paramIndex = 2;

  // Aplicar filtros de fecha
  if (filtros.fecha1 && filtros.fecha2) {
    const campoFecha = obtenerCampoFecha(filtros.fechas || "fechaingreso");
    query += ` AND ${campoFecha} BETWEEN $${paramIndex} AND $${paramIndex + 1}`;
    params.push(filtros.fecha1, filtros.fecha2);
    paramIndex += 2;
  }

  // Filtro por área - buscar en responsables
  if (filtros.areaFiltro && parseInt(filtros.areaFiltro) > 0) {
    query += ` AND EXISTS (
      SELECT 1 FROM controlasuntospendientesnew.responsable r 
      WHERE r.idasunto = a.idasunto AND r.idarea = $${paramIndex}
    )`;
    params.push(parseInt(filtros.areaFiltro));
    paramIndex++;
  }

  // Filtro por texto en descripción
  if (filtros.texto && filtros.texto.trim()) {
    query += ` AND LOWER(a.asunto) ILIKE LOWER($${paramIndex})`;
    params.push(`%${filtros.texto.trim()}%`);
    paramIndex++;
  }

  try {
    console.log(`Contando asuntos tipo ${tipo}:`, { query, params });
    const result = await pool.query(query, params);
    const cantidad = parseInt(result.rows[0].cantidad) || 0;
    console.log(`${tipo}: ${cantidad} asuntos`);
    return cantidad;
  } catch (error) {
    console.error(`Error contando asuntos tipo ${tipo}:`, error);
    return 0;
  }
};

/**
 * Cuenta reuniones - Lógica específica para tipo R
 * Equivalente a AsuntoDAO.cantidadAsuntosReunion() línea 829
 * @param {Object} filtros - Filtros de búsqueda
 * @returns {Promise<number>} Cantidad de reuniones
 */
export const contarReuniones = async (filtros) => {
  const pool = administradorDataSource.getPool();

  let query = `
    SELECT COUNT(*) as cantidad
    FROM controlasuntospendientesnew.asunto a
    WHERE a.tipoasunto = 'R'
  `;

  const params = [];
  let paramIndex = 1;

  // Aplicar filtros de fecha
  if (filtros.fecha1 && filtros.fecha2) {
    const campoFecha = obtenerCampoFecha(filtros.fechas || "fechaingreso");
    query += ` AND ${campoFecha} BETWEEN $${paramIndex} AND $${paramIndex + 1}`;
    params.push(filtros.fecha1, filtros.fecha2);
    paramIndex += 2;
  }

  // Filtro por área - buscar en responsables
  if (filtros.areaFiltro && parseInt(filtros.areaFiltro) > 0) {
    query += ` AND EXISTS (
      SELECT 1 FROM controlasuntospendientesnew.responsable r 
      WHERE r.idasunto = a.idasunto AND r.idarea = $${paramIndex}
    )`;
    params.push(parseInt(filtros.areaFiltro));
    paramIndex++;
  }

  // Filtro por texto en descripción
  if (filtros.texto && filtros.texto.trim()) {
    query += ` AND LOWER(a.asunto) ILIKE LOWER($${paramIndex})`;
    params.push(`%${filtros.texto.trim()}%`);
    paramIndex++;
  }

  try {
    console.log(`📅 Contando reuniones:`, { query, params });
    const result = await pool.query(query, params);
    const cantidad = parseInt(result.rows[0].cantidad) || 0;
    console.log(`✅ REUNIONES: ${cantidad}`);
    return cantidad;
  } catch (error) {
    console.error("❌ Error contando reuniones:", error);
    return 0;
  }
};

/**
 * Cuenta acuerdos - Consulta tabla accion
 * Equivalente a AccionDAO.cantidadAccionesFiltro() línea 291
 * @param {Object} filtros - Filtros de búsqueda
 * @returns {Promise<number>} Cantidad de acuerdos
 */
export const contarAcuerdos = async (filtros) => {
  const pool = administradorDataSource.getPool();

  let query = `
    SELECT COUNT(*) as cantidad
    FROM controlasuntospendientesnew.accion a
    INNER JOIN controlasuntospendientesnew.asunto ast ON a.idasunto = ast.idasunto
    WHERE 1=1
  `;

  const params = [];
  let paramIndex = 1;

  // Aplicar filtros de fecha - usar fechaaccion para acuerdos
  if (filtros.fecha1 && filtros.fecha2) {
    // Para acuerdos siempre usar fechaaccion según AccionDAO
    query += ` AND a.fechaaccion BETWEEN $${paramIndex} AND $${paramIndex + 1}`;
    params.push(filtros.fecha1, filtros.fecha2);
    paramIndex += 2;
  }

  // Filtro por área - buscar en responsables de accion
  if (filtros.areaFiltro && parseInt(filtros.areaFiltro) > 0) {
    query += ` AND EXISTS (
      SELECT 1 FROM controlasuntospendientesnew.responsable r 
      WHERE r.idaccion = a.idaccion AND r.idarea = $${paramIndex}
    )`;
    params.push(parseInt(filtros.areaFiltro));
    paramIndex++;
  }

  // Filtro por texto en descripción
  if (filtros.texto && filtros.texto.trim()) {
    query += ` AND (
      LOWER(a.descripcion) ILIKE LOWER($${paramIndex}) OR 
      LOWER(ast.asunto) ILIKE LOWER($${paramIndex})
    )`;
    params.push(`%${filtros.texto.trim()}%`);
    paramIndex++;
  }

  try {
    console.log(`📋 Contando acuerdos:`, { query, params });
    const result = await pool.query(query, params);
    const cantidad = parseInt(result.rows[0].cantidad) || 0;
    console.log(`✅ ACUERDOS: ${cantidad}`);
    return cantidad;
  } catch (error) {
    console.error("❌ Error contando acuerdos:", error);
    return 0;
  }
};

/**
 * Determina el campo de fecha según el tipo de filtro
 * @param {string} tipoFecha - Tipo de fecha (fechaingreso, fechaatencion, etc.)
 * @returns {string} Campo de fecha SQL
 */
const obtenerCampoFecha = (tipoFecha) => {
  switch (tipoFecha) {
    case "fechaingreso":
      return "a.fechaingreso";
    case "fechaatencion":
      return "a.fechaatencion";
    case "fechaenvio":
      return "a.fechaenvio";
    default:
      return "a.fechaingreso";
  }
};

/**
 * Función helper para formatear fechas
 * @param {string} fecha - Fecha en formato DD/MM/YYYY
 * @returns {string} Fecha en formato YYYYMMDD
 */
export const formatearFecha = (fecha) => {
  if (!fecha) return null;

  // Si ya está en formato YYYYMMDD, devolver como está
  if (/^\d{8}$/.test(fecha)) {
    return fecha;
  }

  // Si está en formato DD/MM/YYYY, convertir
  if (/^\d{2}\/\d{2}\/\d{4}$/.test(fecha)) {
    const partes = fecha.split("/");
    return `${partes[2]}${partes[1].padStart(2, "0")}${partes[0].padStart(
      2,
      "0"
    )}`;
  }

  // Si está en formato YYYY-MM-DD, convertir
  if (/^\d{4}-\d{2}-\d{2}$/.test(fecha)) {
    return fecha.replace(/-/g, "");
  }

  return null;
};
