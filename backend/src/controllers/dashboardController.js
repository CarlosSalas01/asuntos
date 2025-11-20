import axios from "axios";
import datosGlobales from "../config/datosGlobales.js";
import administradorDataSource from "../config/administradorDataSource.js";

/**
 * Obtiene reuniones sin acuerdos desde PostgreSQL
 * Query específico para contar reuniones sin acuerdos del área 2
 * que no tienen acciones registradas y están pendientes
 */
async function obtenerReunionesSinAcuerdos(fechaIni, fechaFin, usuario) {
  try {
    // Query específico solicitado: reuniones del área 2 sin acciones registradas
    const sqlReunionesSA = `
      SELECT count(*) as cantidad 
      FROM controlasuntospendientesnew.asunto 
      WHERE idarea = 2 
        AND tipoasunto = 'R' 
        AND NOT (idasunto IN (
          SELECT idasunto 
          FROM controlasuntospendientesnew.accion 
          GROUP BY idasunto
        )) 
        AND estatus = 'P'
    `;

    const result = await administradorDataSource.executeQuery(sqlReunionesSA);

    const cantidad = parseInt(result.rows[0]?.cantidad) || 0;
    return cantidad;
  } catch (error) {
    console.error("Error obteniendo reuniones sin acuerdos:", error);
    return 0;
  }
}

/**
 * Obtiene totales desde la API externa de SOLR (como el sistema original)
 * Para ser usado por EstadisticasCards.jsx
 * NOTA: Las reuniones sin acuerdos se obtienen de PostgreSQL con query específico
 */
export const getTotalesAPIExterna = async (req, res) => {
  const { fechaInicio = "2025-01-01", fechaFin = "2025-11-11" } = req.query;

  try {
    // Construir URL de la API externa con parámetros de fecha
    const parametrosFecha = encodeURIComponent(
      JSON.stringify({
        fAsTot1: fechaInicio,
        fAsTot2: fechaFin,
      })
    );

    const urlAPIExterna = `http://localhost:3002/asuntos_api/getdatos/${parametrosFecha}`;

    // Consultar API externa con timeout más largo y manejo de errores mejorado
    const response = await axios.get(urlAPIExterna, {
      timeout: 8000,
      headers: {
        Accept: "application/json",
        "User-Agent": "NodeJS-Backend",
      },
    });

    if (
      !response.data ||
      !response.data.resumenAsunto ||
      !response.data.resumenAsunto[0]
    ) {
      throw new Error("Respuesta inválida de API externa");
    }

    const datosExternos = response.data.resumenAsunto[0];

    // Obtener reuniones sin acuerdos desde PostgreSQL (query específico)
    const reunionesSA = await obtenerReunionesSinAcuerdos(
      fechaInicio,
      fechaFin,
      null
    );

    // Adaptar al formato esperado por EstadisticasCards.jsx
    const totalesParaComponente = {
      fuente: "API_Externa_SOLR",
      periodo: { fechaInicio, fechaFin },
      totales: {
        totalGral: datosExternos.atendidos + datosExternos.pendientes,
        totalAtendidos: datosExternos.atendidos,
        totalPendientes: datosExternos.pendientes,
        totalReuniones: reunionesSA, // Usar valor de PostgreSQL en lugar de API
      },
      timestamp: new Date(),
    };

    res.json(totalesParaComponente);
  } catch (error) {
    console.error("Error en getTotalesAPIExterna:", error.message);

    // Fallback con datos vacíos (mejor que datos hardcodeados)
    const fallback = {
      fuente: "API_Externa_SOLR_Fallback",
      error: `Error conectando con API externa: ${error.message}`,
      periodo: { fechaInicio, fechaFin },
      totales: {
        totalGral: 0,
        totalAtendidos: 0,
        totalPendientes: 0,
        totalReuniones: 0,
      },
      timestamp: new Date(),
    };

    res.status(503).json(fallback); // 503 Service Unavailable es más apropiado
  }
};

/**
 * Obtiene totales SOLO desde nuestra base de datos PostgreSQL
 * Para comparar contra la API externa de SOLR
 */
export const getTotalesPostgreSQL = async (req, res) => {
  try {
    const { fechaInicio = "2025-01-01", fechaFin = "2025-11-11" } = req.query;

    console.log("Consultando desde BD local...");

    // Consultas directas a PostgreSQL
    const queryAtendidos = `
      SELECT COUNT(*) as total
      FROM controlasuntospendientesnew.asunto 
      WHERE estatus = 'A'
      AND SUBSTRING(fechaatender,1,8) >= REPLACE('${fechaInicio}', '-', '')
      AND SUBSTRING(fechaatender,1,8) <= REPLACE('${fechaFin}', '-', '')
    `;

    const queryPendientes = `
      SELECT COUNT(*) as total
      FROM controlasuntospendientesnew.asunto 
      WHERE estatus = 'P'
      AND SUBSTRING(fechaatender,1,8) >= REPLACE('${fechaInicio}', '-', '')
      AND SUBSTRING(fechaatender,1,8) <= REPLACE('${fechaFin}', '-', '')
    `;

    const queryReunionesSA = `
      SELECT COUNT(*) as total
      FROM controlasuntospendientesnew.rep_union 
      WHERE (acuerdo IS NULL OR acuerdo = '' OR TRIM(acuerdo) = '')
    `;

    const [atendidosResult, pendientesResult, reunionesResult] =
      await Promise.all([
        administradorDataSource.executeQuery(queryAtendidos),
        administradorDataSource.executeQuery(queryPendientes),
        administradorDataSource.executeQuery(queryReunionesSA),
      ]);

    const totalAtendidos = parseInt(atendidosResult.rows[0]?.total) || 0;
    const totalPendientes = parseInt(pendientesResult.rows[0]?.total) || 0;
    const totalReuniones = parseInt(reunionesResult.rows[0]?.total) || 0;
    const totalGral = totalAtendidos + totalPendientes;

    const resultado = {
      fuente: "PostgreSQL",
      periodo: { fechaInicio, fechaFin },
      totales: {
        totalGral,
        totalAtendidos,
        totalPendientes,
        totalReuniones,
      },
    };

    res.json(resultado);
  } catch (error) {
    console.error("Error en getTotalesPostgreSQL:", error);
    res.status(500).json({
      error: "Error obteniendo totales desde PostgreSQL",
      details: error.message,
    });
  }
};

/**
 * Obtiene el resumen de asuntos para el dashboard
 */
export const getResumenInicio = async (req, res) => {
  try {
    const { tipo = "0", otroAnio, idAdjunta = "1" } = req.query;
    const usuario = req.user; // Del middleware de autenticación

    if (tipo === "0") {
      // Obtener datos del resumen general REAL
      const resultado = await obtenerDatosResumen(otroAnio, idAdjunta, usuario);
      res.json(resultado);
    } else if (tipo === "1") {
      // Obtener detalles pendientes por área REAL
      const idarea = req.query.idarea;
      const detalles = await obtenerDetallesPendientes(idarea);
      res.json(detalles);
    }
  } catch (error) {
    console.error("❌ Error en getResumenInicio:", error);
    res.status(500).json({ error: "Error interno del servidor" });
  }
};

/**
 * Obtiene totales generales - Equivalente a ResponsableDAO.obtenResumenIni() del sistema original Java
 * Este método replica exactamente la lógica del servlet original para obtener estadísticas generales
 */
async function obtenerTotalesGenerales(fechaIni, fechaFin, usuario) {
  try {
    console.log("🔍 === MÉTODO CORRECTO: obtenerTotalesGenerales ===");
    console.log(`📅 Consultando totales del ${fechaIni} al ${fechaFin}...`);

    // SQL EXACTO del método Java original: ResponsableDAO.obtenResumenIni()
    const sqlTotalesGenerales = `
      SELECT 
        (SELECT count(*) FROM controlasuntospendientesnew.asunto 
         WHERE estatus='A' AND fechaatendertexto >= $1 AND fechaatendertexto <= $2) as atendidos, 
        (SELECT count(*) FROM controlasuntospendientesnew.asunto 
         WHERE estatus='P' AND tipoasunto<>'R' AND fechaingreso >= $1 AND fechaingreso <= $2) as pendientes
    `;

    console.log("📊 Ejecutando SQL de totales generales...");
    const resultTotales = await administradorDataSource.executeQuery(
      sqlTotalesGenerales,
      [fechaIni, fechaFin]
    );

    const totales = resultTotales.rows[0] || { atendidos: 0, pendientes: 0 };

    console.log("✅ Totales obtenidos desde PostgreSQL:", {
      atendidos: totales.atendidos,
      pendientes: totales.pendientes,
      totalGral: parseInt(totales.atendidos) + parseInt(totales.pendientes),
    });

    return {
      totalAtendidos: parseInt(totales.atendidos) || 0,
      totalPendientes: parseInt(totales.pendientes) || 0,
      totalGral:
        (parseInt(totales.atendidos) || 0) +
        (parseInt(totales.pendientes) || 0),
    };
  } catch (error) {
    console.error("❌ Error en obtenerTotalesGenerales:", error);
    console.log("🔄 Retornando totales por defecto como fallback");
    return {
      totalAtendidos: 0,
      totalPendientes: 0,
      totalGral: 0,
    };
  }
}

/**
 * Obtiene datos del resumen desde APIs externas y base de datos
 */
async function obtenerDatosResumen(otroAnio, idAdjunta, usuario) {
  try {
    // Calcular fechas
    const hoy = new Date();
    const anioActual = hoy.getFullYear().toString();
    const anioConsulta = otroAnio || anioActual;

    const fechaIni = `${anioConsulta}-01-01`;
    const fechaFin = otroAnio ? `${anioConsulta}-12-31` : formatearFecha(hoy);
    const fechaHora = formatearFechaHora(new Date());

    // URLs de las APIs (usando configuración global)
    const urlResumen =
      "http://localhost:3002/asuntos_api/getdatos/%7B%22resumen%22%3A%22true%22%7D";
    const paramsFechas = `%7B%22fAsTot1%22%3A%22${fechaIni}%22%2C%22fAsTot2%22%3A%22${fechaFin}%22%7D`;
    const urlTotales = `http://localhost:3002/asuntos_api/getdatos/${paramsFechas}`;

    // Obtener datos del API REST SOLR
    const [resumenResponse, reunionesSADirecta] = await Promise.all([
      axios.get(urlResumen), // API REST para datos por área
      obtenerReunionesSinAcuerdos(fechaIni, fechaFin, usuario), // SQL directa para reuniones SA
    ]);

    console.log(`🌐 Respuesta de API SOLR tipo:`, typeof resumenResponse.data);
    console.log(`🌐 Es array:`, Array.isArray(resumenResponse.data));
    console.log(
      `🌐 Contenido (primeros 2):`,
      resumenResponse.data?.slice(0, 2)
    );

    // El servicio SOLR devuelve directamente un array, no un objeto con propiedad resumenInicio
    const elementosAreas = Array.isArray(resumenResponse.data)
      ? resumenResponse.data
      : resumenResponse.data.resumenInicio || [];

    console.log(
      `🌐 Elementos de áreas extraídos: ${elementosAreas.length} elementos`
    );
    let totalAtendidosCalculado = 0;
    let totalPendientesCalculado = 0;

    elementosAreas.forEach((area) => {
      totalAtendidosCalculado += parseInt(area.atendidos) || 0;
      totalPendientesCalculado +=
        parseInt(area.pendientes) || parseInt(area.totalPend) || 0;
    });

    const atendidosTotal = totalAtendidosCalculado;
    const pendientesTotal = totalPendientesCalculado;
    const totalGeneral = atendidosTotal + pendientesTotal;
    const reunionesSA = reunionesSADirecta;

    // Construir resultado con totales correctos del servlet Java original
    const resultado = [
      {
        fechaHora,
        atendidosTodos: atendidosTotal,
        pendientesTodos: pendientesTotal,
        totalGral: totalGeneral, // Total general calculado correctamente
        reunionesSA,
      },
    ];

    // Procesar datos por área
    const idAdjuntaInt = parseInt(idAdjunta);

    console.log(
      `📊 idAdjunta recibido: "${idAdjunta}" (tipo: ${typeof idAdjunta})`
    );
    console.log(`📊 Áreas totales en API externa: ${elementosAreas.length}`);

    if (idAdjunta !== "0" && idAdjunta !== "1") {
      // Filtrar por área específica
      console.log(`🔍 Filtrando por área específica: ${idAdjuntaInt}`);
      const areaEspecifica = elementosAreas.find(
        (area) => area.idarea === idAdjuntaInt
      );
      if (areaEspecifica) {
        resultado.push(formatearDatosArea(areaEspecifica));
        console.log(`✅ Área específica encontrada: ${areaEspecifica.siglas}`);
      } else {
        console.log(`⚠️ Área ${idAdjuntaInt} NO encontrada en API externa`);
      }
    } else {
      // Incluir todas las áreas
      console.log(
        `📋 Incluyendo TODAS las áreas (${elementosAreas.length} áreas)`
      );
      elementosAreas.forEach((area) => {
        resultado.push(formatearDatosArea(area));
      });
    }

    console.log(
      `✅ Resultado final: ${resultado.length} elementos (1 total + ${
        resultado.length - 1
      } áreas)`
    );
    return resultado;
  } catch (error) {
    console.error("Error obteniendo datos del resumen:", error);
    throw error;
  }
}

/**
 * Formatea los datos de un área - Basado en el código original ResumenInicio.java
 */
function formatearDatosArea(area) {
  // CORRECCIÓN: Usar exactamente los nombres de campos del API REST original
  // El servicio SOLR devuelve: porVencer, sinVencer (camelCase)
  const atendidos = parseInt(area.atendidos) || 0;
  const pendientes = parseInt(area.totalPend) || parseInt(area.pendientes) || 0;
  const vencidos = parseInt(area.vencidos) || 0;
  const porvencer = parseInt(area.porVencer) || parseInt(area.por_vencer) || 0;
  const sinvencer = parseInt(area.sinVencer) || parseInt(area.sin_vencer) || 0;

  return {
    // Campos principales - EXACTOS del API REST (sin modificaciones)
    atendidos: atendidos,
    atendidosArea: atendidos.toString(),
    enviados: 0, // Campo del sistema original
    idarea: area.idarea?.toString() || "0",
    nivel: 0,
    pendactivos: 0,
    pendientes: pendientes, // Total pendientes del área
    pendientesAntes: 0,
    reunionesSA: 0, // Las reuniones sin acuerdo van en los totales generales
    siglas: area.siglas || "N/A",

    // CRÍTICO: Usar exactamente los valores del API REST sin modificar
    vencidos: vencidos, // área.vencidos del API REST
    porvencer: porvencer, // área.por_vencer del API REST
    sinvencer: sinvencer, // área.sin_vencer del API REST

    // Campos adicionales para compatibilidad con el frontend
    totalPend: pendientes,
    id: area.idarea?.toString() || "0", // Para onConsultarPendientes

    // Porcentajes calculados correctamente
    porcentajes: {
      vencidos: pendientes > 0 ? (vencidos / pendientes) * 100 : 0,
      porvencer: pendientes > 0 ? (porvencer / pendientes) * 100 : 0,
      sinvencer: pendientes > 0 ? (sinvencer / pendientes) * 100 : 0,
    },
  };
}

/**
 * Obtiene detalles de pendientes por área - EXACTO del servlet Java original (tipo="1")
 * Equivale a AdministradorReportes.obtenReportePorArea() + FachadaUsuarioArea.buscaArea()
 * Usa EXCLUSIVAMENTE consultas SQL directas a PostgreSQL (NO APIs externas)
 */
/**
 * Obtiene detalles de pendientes por área - CORREGIDO con consultas específicas
 * Usa las consultas exactas proporcionadas para cada tipo de asunto
 */
async function obtenerDetallesPendientes(idarea) {
  try {
    console.log(
      `🔍 === DETALLES PENDIENTES ÁREA ${idarea} (CONSULTAS ESPECÍFICAS CORREGIDAS) ===`
    );

    // Importar AsuntoDAO dinámicamente
    const { default: AsuntoDAO } = await import("../dao/AsuntoDAO.js");

    // Fechas basadas en DatosGlobales.anioBase() y Utiles.getFechaHoy() del servlet original
    const hoy = new Date();
    const anioActual = hoy.getFullYear().toString();
    const fechaIni = `${anioActual}-01-01`; // DatosGlobales.anioBase()
    const fechaFin = formatearFecha(hoy); // Utiles.getFechaHoy()

    console.log(`📅 Período de consulta: ${fechaIni} al ${fechaFin}`);

    // 1. OBTENER DATOS DEL ÁREA desde PostgreSQL
    const areaQuery = `
      SELECT idarea, nombre, siglas, nivel 
      FROM controlasuntospendientesnew.area 
      WHERE idarea = $1
    `;

    const areaResult = await administradorDataSource.executeQuery(areaQuery, [
      idarea,
    ]);
    const areaData = areaResult.rows[0];

    if (!areaData) {
      console.log(`❌ Área ${idarea} no encontrada en base de datos`);
      return [];
    }

    console.log(`📊 Área encontrada: ${areaData.siglas} (${areaData.nombre})`);

    // 2. USAR ASUNTODAO CON CONSULTAS ESPECÍFICAS CORREGIDAS
    const asuntoDAO = new AsuntoDAO();
    const resumenTipos = await asuntoDAO.obtenerDetallesPendientesPorArea(
      idarea,
      fechaIni,
      fechaFin
    );

    // 3. CONSTRUIR RESPUESTA EN FORMATO DEL SERVLET ORIGINAL
    const reportePorArea = [
      {
        area: {
          idarea: parseInt(idarea),
          nombre: areaData.nombre,
          siglas: areaData.siglas,
          nivel: areaData.nivel,
        },
        resumen: resumenTipos,
      },
    ];

    console.log(`✅ Detalles específicos obtenidos para ${areaData.siglas}:`, {
      area: areaData.siglas,
      tiposAsunto: resumenTipos.length,
      totalItems: resumenTipos.reduce(
        (sum, tipo) =>
          sum + tipo.vencidos_d + tipo.porvencer_d + tipo.pendactivos_d,
        0
      ),
    });

    return reportePorArea;
  } catch (error) {
    console.error(
      "❌ Error obteniendo detalles pendientes con consultas específicas:",
      error
    );
    throw error;
  }
}

/**
 * Obtiene detalles de un tipo específico de asunto - SQL directo
 * Equivale a las consultas SQL del AdministradorReportes original
 */
async function obtenerDetallesTipoAsunto(
  idarea,
  codigoTipo,
  nombreTipo,
  fechaIni,
  fechaFin
) {
  try {
    console.log(
      `� Consultando tipo ${codigoTipo} (${nombreTipo}) para área ${idarea}...`
    );

    // SQL para obtener detalles por tipo de asunto y estado de fechas
    // Basado en las consultas del AdministradorReportes.java original
    const sqlDetallesTipo = `
      SELECT 
        -- Vencidos: asuntos pendientes cuya fecha límite ya pasó
        (SELECT count(*) 
         FROM controlasuntospendientesnew.asunto a
         LEFT JOIN controlasuntospendientesnew.responsable r ON a.idasunto = r.idasunto
         WHERE r.idarea = $1 
           AND a.tipoasunto = $2 
           AND a.estatus = 'P'
           AND r.estatus = 'A'
           AND a.fechalimite < CURRENT_DATE
           AND a.fechaingreso >= $3 
           AND a.fechaingreso <= $4) as vencidos,
           
        -- Por vencer: asuntos pendientes próximos a vencer (próximos 7 días)
        (SELECT count(*) 
         FROM controlasuntospendientesnew.asunto a
         LEFT JOIN controlasuntospendientesnew.responsable r ON a.idasunto = r.idasunto
         WHERE r.idarea = $1 
           AND a.tipoasunto = $2 
           AND a.estatus = 'P'
           AND r.estatus = 'A'
           AND a.fechalimite >= CURRENT_DATE 
           AND a.fechalimite <= (CURRENT_DATE + INTERVAL '7 days')
           AND a.fechaingreso >= $3 
           AND a.fechaingreso <= $4) as porvencer,
           
        -- Activos: asuntos pendientes con tiempo suficiente
        (SELECT count(*) 
         FROM controlasuntospendientesnew.asunto a
         LEFT JOIN controlasuntospendientesnew.responsable r ON a.idasunto = r.idasunto
         WHERE r.idarea = $1 
           AND a.tipoasunto = $2 
           AND a.estatus = 'P'
           AND r.estatus = 'A'
           AND a.fechalimite > (CURRENT_DATE + INTERVAL '7 days')
           AND a.fechaingreso >= $3 
           AND a.fechaingreso <= $4) as activos
    `;

    const result = await administradorDataSource.executeQuery(sqlDetallesTipo, [
      idarea,
      codigoTipo,
      fechaIni,
      fechaFin,
    ]);

    const datos = result.rows[0] || { vencidos: 0, porvencer: 0, activos: 0 };

    const detalles = {
      tipoasunto: nombreTipo,
      tipoAbreviado: codigoTipo,
      vencidos_d: parseInt(datos.vencidos) || 0,
      porvencer_d: parseInt(datos.porvencer) || 0,
      pendactivos_d: parseInt(datos.activos) || 0,
    };

    console.log(`✅ ${nombreTipo}:`, detalles);
    return detalles;
  } catch (error) {
    console.error(`❌ Error consultando tipo ${nombreTipo}:`, error);
    return {
      tipoasunto: nombreTipo,
      tipoAbreviado: codigoTipo,
      vencidos_d: 0,
      porvencer_d: 0,
      pendactivos_d: 0,
    };
  }
}

/**
 * Obtiene detalles específicos de reuniones sin acuerdos - SQL directo
 * Equivale a la consulta especial para tipo "R" en el AdministradorReportes
 */
async function obtenerDetallesReuniones(idarea, fechaIni, fechaFin) {
  try {
    console.log(`📊 Consultando reuniones sin acuerdos para área ${idarea}...`);

    // SQL específico para reuniones pendientes de registrar acuerdos
    const sqlReuniones = `
      SELECT count(*) as cantidad
      FROM controlasuntospendientesnew.asunto a
      LEFT JOIN controlasuntospendientesnew.responsable r ON a.idasunto = r.idasunto
      WHERE r.idarea = $1 
        AND a.tipoasunto = 'R'
        AND a.estatus != 'A'
        AND r.estatus = 'A'
        AND a.fechaingreso >= $2 
        AND a.fechaingreso <= $3
    `;

    const result = await administradorDataSource.executeQuery(sqlReuniones, [
      idarea,
      fechaIni,
      fechaFin,
    ]);

    const cantidad = parseInt(result.rows[0]?.cantidad) || 0;

    const detallesReuniones = {
      tipoasunto: "REUNIONES PENDIENTES DE REGISTRAR ACUERDOS",
      tipoAbreviado: "R",
      vencidos_d: cantidad, // Las reuniones sin acuerdos se muestran como "vencidas"
      porvencer_d: 0, // Las reuniones no tienen "por vencer"
      pendactivos_d: 0, // Las reuniones no tienen "activos"
    };

    console.log(`✅ Reuniones sin acuerdos:`, detallesReuniones);
    return detallesReuniones;
  } catch (error) {
    console.error(`❌ Error consultando reuniones sin acuerdos:`, error);
    return {
      tipoasunto: "REUNIONES PENDIENTES DE REGISTRAR ACUERDOS",
      tipoAbreviado: "R",
      vencidos_d: 0,
      porvencer_d: 0,
      pendactivos_d: 0,
    };
  }
}

/**
 * Obtiene información del área superior
 */
export const getAreaSuperior = async (req, res) => {
  console.log("🔍 === GET AREA SUPERIOR ===");
  console.log("📊 Query params:", req.query);

  try {
    const { idarea, nivel } = req.query;
    console.log(`✅ Retornando área superior para ${idarea}`);
    res.json(idarea);
  } catch (error) {
    console.error("❌ Error obteniendo área superior:", error);
    res.status(500).json({ error: "Error interno del servidor" });
  }
};

/**
 * Utilidades de formateo
 */
function formatearFecha(fecha) {
  const año = fecha.getFullYear();
  const mes = String(fecha.getMonth() + 1).padStart(2, "0");
  const dia = String(fecha.getDate()).padStart(2, "0");
  return `${año}-${mes}-${dia}`;
}

function formatearFechaHora(fecha) {
  const opciones = {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
    timeZone: "America/Mexico_City",
  };

  return fecha.toLocaleDateString("es-MX", opciones) + " horas";
}

/**
 * Configuración de datos globales - Equivalente a DatosGlobales.anioBase()
 */
const datosGlobalesConfig = {
  anioBase: () => {
    // Por defecto, usar el inicio del año actual
    // TODO: Configurar según las reglas de negocio específicas
    const año = new Date().getFullYear();
    return `${año}-01-01`;
  },
};

/**
 * GET /api/dashboard/pendientes-detalle
 * Obtiene el detalle de pendientes por tipo para un área específica
 * @param {Object} req - Request object con query params: idarea, fechaInicio, fechaFin
 * @param {Object} res - Response object
 */
export async function getDetallesPendientes(req, res) {
  try {
    const { idarea, fechaInicio, fechaFin } = req.query;

    if (!idarea) {
      return res.status(400).json({
        success: false,
        message: "El parámetro idarea es requerido",
      });
    }

    const detalles = await obtenerDetallesPendientes(parseInt(idarea));

    res.json({
      success: true,
      data: detalles,
    });
  } catch (error) {
    console.error("Error en getDetallesPendientes:", error);
    res.status(500).json({
      success: false,
      message: "Error interno del servidor",
      error: error.message,
    });
  }
}
