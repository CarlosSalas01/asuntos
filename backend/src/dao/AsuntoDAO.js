import administradorDataSource from "../config/administradorDataSource.js";
import AsuntoBean from "../models/AsuntoBean.js";

class AsuntoDAO {
  /**
   * @param {Object} filtros - Filtros opcionales: { search, limit, offset, tipoAsunto }
   * @param {string} tipoAsunto - Tipo de asunto: 'K' (SIA), 'C' (Correos), 'R' (Reuniones), 'A' (Acuerdos), 'M' (Comisiones)
   */
  async obtenerAsuntosPorTipo(filtros = {}, tipoAsunto) {
    const { search = "", limit = 50, offset = 0 } = filtros;

    // Validar tipo de asunto
    const tiposValidos = ["K", "C", "R", "A", "M"];
    const tipo = tiposValidos.includes(tipoAsunto) ? tipoAsunto : "K";

    // 1. Construir query dinámico para asuntos
    const params = [tipo];
    const whereParts = [`a.tipoasunto = $1`];

    if (search.trim()) {
      params.push(`%${search.trim()}%`);
      whereParts.push(`a.descripcion ILIKE $${params.length}`);
    }

    const whereClause = `WHERE ${whereParts.join(" AND ")}`;

    params.push(limit);
    params.push(offset);

    const sqlAsuntos = `
      SELECT *
      FROM controlasuntospendientesnew.asunto a
      ${whereClause}
      ORDER BY a.idconsecutivo DESC
      LIMIT $${params.length - 1} OFFSET $${params.length}
    `;

    const resultAsuntos = await administradorDataSource.executeQuery(
      sqlAsuntos,
      params
    );
    const asuntos = resultAsuntos.rows;

    // 2. Para cada asunto, obtener sus responsables (nivel 2)
    for (let asunto of asuntos) {
      const sqlResponsables = `
        SELECT 
          r.idresponsable,
          r.idarea, 
          a.siglas, 
          r.estatus, 
          r.avance, 
          r.fechaatencion,
          r.delegado
        FROM controlasuntospendientesnew.responsable r 
        INNER JOIN controlasuntospendientesnew.area a ON r.idarea = a.idarea AND a.nivel = 2
        WHERE r.idasunto = $1
      `;
      const resultResp = await administradorDataSource.executeQuery(
        sqlResponsables,
        [asunto.idasunto]
      );
      asunto.responsables = resultResp.rows;
    }

    return asuntos;
  }
  constructor(areas = null) {
    this.areas = areas;
  }

  async cantidadAsuntosxAreaxTipo(filtro, tipo) {
    try {
      const query = `
        SELECT COUNT(*) as cantidad
        FROM controlasuntospendientesnew.asunto a
        LEFT JOIN controlasuntospendientesnew.responsable r ON a.idasunto = r.idasunto
        WHERE r.idarea = $1 
          AND a.tipoasunto = $2 
          AND a.estatus = $3
          AND r.estatus = 'A'
          AND a.fechaingreso >= $4 
          AND a.fechaingreso <= $5
      `;

      const result = await administradorDataSource.executeQuery(query, [
        filtro.idarea,
        tipo,
        filtro.estatusAsunto || "P",
        filtro.fechaInicio,
        filtro.fechaFinal,
      ]);

      return { cantidad: parseInt(result.rows[0]?.cantidad) || 0 };
    } catch (error) {
      console.error("Error en cantidadAsuntosxAreaxTipo:", error);
      return { cantidad: 0 };
    }
  }

  async buscarAsuntosPorAreaxTipo(filtro, tipo) {
    let sql = `SELECT * FROM controlasuntospendientesnew.asunto AS a WHERE tipoasunto = $1`;
    let params = [tipo];
    let paramIndex = 2;
    // Filtros de área
    if (filtro.idarea > 0) {
      sql += ` AND idasunto IN (SELECT idasunto FROM controlasuntospendientesnew.responsable WHERE `;
      if (filtro.idareaDelegada > 0) {
        sql += `idarea = $${paramIndex++}`;
        params.push(filtro.idareaDelegada);
        if (filtro.estatusResp && filtro.estatusResp !== "T") {
          sql += ` AND estatus = $${paramIndex++}`;
          params.push(filtro.estatusResp);
        }
        sql += ` AND asignadopor = $${paramIndex++} AND estatus <> 'C'`;
        params.push(filtro.idarea);
      } else {
        sql += `idarea = $${paramIndex++}`;
        params.push(filtro.idarea);
        if (filtro.estatusResp && filtro.estatusResp !== "T") {
          sql += ` AND estatus = $${paramIndex++}`;
          params.push(filtro.estatusResp);
        }
      }
      sql += `)`;
    } else if (filtro.areasConsulta && filtro.areasConsulta.length > 0) {
      sql += ` AND idasunto IN (SELECT idasunto FROM controlasuntospendientesnew.responsable WHERE (`;
      filtro.areasConsulta.forEach((area, idx) => {
        if (filtro.estatusResp && filtro.estatusResp !== "T") {
          sql += `(idarea = $${paramIndex++} AND estatus = $${paramIndex++}) OR `;
          params.push(area.idarea, filtro.estatusResp);
        } else {
          sql += `idarea = $${paramIndex++} OR `;
          params.push(area.idarea);
        }
      });
      sql = sql.slice(0, -4) + `))`;
    }

    // Filtros de fecha
    if (filtro.tipoFecha === "atencion") {
      sql += ` AND substring(fechaatencion,1,8) >= $${paramIndex++} AND substring(fechaatencion,1,8) <= $${paramIndex++}`;
      params.push(filtro.fechaInicio, filtro.fechaFinal);
    }
    if (filtro.tipoFecha === "asignado") {
      sql += ` AND substring(fechaasignado,1,8) >= $${paramIndex++} AND substring(fechaasignado,1,8) <= $${paramIndex++}`;
      params.push(filtro.fechaInicio, filtro.fechaFinal);
    }

    // Porcentaje de avance
    if (filtro.porcentajeAvance) {
      sql += ` AND avance = $${paramIndex++}`;
      params.push(parseInt(filtro.porcentajeAvance));
    }

    // Orden y paginación
    sql += ` ORDER BY idarea, idconsecutivo DESC`;
    if (!filtro.limitAll) {
      sql += ` LIMIT 50 OFFSET $${paramIndex++}`;
      params.push(filtro.offset);
    }

    // Ejecuta la consulta
    const result = await administradorDataSource.executeQuery(sql, params);

    // Mapeo y procesamiento extra como en Java
    const asuntos = result.rows.map((row) => {
      const asunto = new AsuntoBean(row);
      // Procesa descripción HTML
      asunto.descripcionFormatoHTML = asunto.getDescripcionFormatoHTML?.();
      // Procesa fechas formateadas
      asunto.fechaingresoFormatoTexto = asunto.getFechaingresoFormatoTexto?.();
      asunto.fechaoriginalFormatoTexto =
        asunto.getFechaoriginalFormatoTexto?.();
      // Puedes agregar más procesamiento aquí si lo necesitas
      return asunto;
    });

    return asuntos;
  }

  async cantidadAsuntosVencidosDirectos(tipo, idarea, filtro) {
    try {
      const query = `
        SELECT COUNT(*) as cantidad
        FROM controlasuntospendientesnew.asunto a
        LEFT JOIN controlasuntospendientesnew.responsable r ON a.idasunto = r.idasunto
        WHERE r.idarea = $1 
          AND a.tipoasunto = $2 
          AND a.estatus = 'P'
          AND r.estatus = 'A'
          AND a.fechalimite < CURRENT_DATE
          AND a.fechaingreso >= $3 
          AND a.fechaingreso <= $4
      `;

      const result = await administradorDataSource.executeQuery(query, [
        idarea,
        tipo,
        filtro.fechaInicio,
        filtro.fechaFinal,
      ]);

      return { cantidad: parseInt(result.rows[0]?.cantidad) || 0 };
    } catch (error) {
      console.error("Error en cantidadAsuntosVencidosDirectos:", error);
      return { cantidad: 0 };
    }
  }

  async cantidadAsuntosPorVencerDirectos(tipo, idarea, filtro) {
    try {
      const query = `
        SELECT COUNT(*) as cantidad
        FROM controlasuntospendientesnew.asunto a
        LEFT JOIN controlasuntospendientesnew.responsable r ON a.idasunto = r.idasunto
        WHERE r.idarea = $1 
          AND a.tipoasunto = $2 
          AND a.estatus = 'P'
          AND r.estatus = 'A'
          AND a.fechalimite >= CURRENT_DATE 
          AND a.fechalimite <= (CURRENT_DATE + INTERVAL '7 days')
          AND a.fechaingreso >= $3 
          AND a.fechaingreso <= $4
      `;

      const result = await administradorDataSource.executeQuery(query, [
        idarea,
        tipo,
        filtro.fechaInicio,
        filtro.fechaFinal,
      ]);

      return { cantidad: parseInt(result.rows[0]?.cantidad) || 0 };
    } catch (error) {
      console.error("Error en cantidadAsuntosPorVencerDirectos:", error);
      return { cantidad: 0 };
    }
  }

  /**
   * Obtiene la cantidad de asuntos pendientes activos directos
   * Equivalente a cantidadAsuntosPendActivosDirectos() del Java original
   */
  async cantidadAsuntosPendActivosDirectos(tipo, idarea, filtro) {
    try {
      const query = `
        SELECT COUNT(*) as cantidad
        FROM controlasuntospendientesnew.asunto a
        LEFT JOIN controlasuntospendientesnew.responsable r ON a.idasunto = r.idasunto
        WHERE r.idarea = $1 
          AND a.tipoasunto = $2 
          AND a.estatus = 'P'
          AND r.estatus = 'A'
          AND a.fechalimite > (CURRENT_DATE + INTERVAL '7 days')
          AND a.fechaingreso >= $3 
          AND a.fechaingreso <= $4
      `;

      const result = await administradorDataSource.executeQuery(query, [
        idarea,
        tipo,
        filtro.fechaInicio,
        filtro.fechaFinal,
      ]);

      return { cantidad: parseInt(result.rows[0]?.cantidad) || 0 };
    } catch (error) {
      console.error("Error en cantidadAsuntosPendActivosDirectos:", error);
      return { cantidad: 0 };
    }
  }

  /**
   * Obtiene la cantidad de asuntos atendidos
   * Equivalente a cantidadAsuntosAtendidos() del Java original
   */
  async cantidadAsuntosAtendidos(tipo, idarea, filtro) {
    try {
      const query = `
        SELECT COUNT(*) as cantidad
        FROM controlasuntospendientesnew.asunto a
        LEFT JOIN controlasuntospendientesnew.responsable r ON a.idasunto = r.idasunto
        WHERE r.idarea = $1 
          AND a.tipoasunto = $2 
          AND a.estatus = 'A'
          AND r.estatus = 'A'
          AND a.fechaingreso >= $3 
          AND a.fechaingreso <= $4
      `;

      const result = await administradorDataSource.executeQuery(query, [
        idarea,
        tipo,
        filtro.fechaInicio,
        filtro.fechaFinal,
      ]);

      return { cantidad: parseInt(result.rows[0]?.cantidad) || 0 };
    } catch (error) {
      console.error("Error en cantidadAsuntosAtendidos:", error);
      return { cantidad: 0 };
    }
  }

  /**
   * Obtiene la cantidad de reuniones sin acuerdos directos
   * Equivalente a cantidadReunionesSinAcuerdosDirectos() del Java original
   */
  async cantidadReunionesSinAcuerdosDirectos(filtro) {
    try {
      const query = `
        SELECT COUNT(*) as cantidad
        FROM controlasuntospendientesnew.asunto a
        LEFT JOIN controlasuntospendientesnew.responsable r ON a.idasunto = r.idasunto
        WHERE r.idarea = $1 
          AND a.tipoasunto = 'R'
          AND a.estatus != 'A'
          AND r.estatus = 'A'
          AND a.fechaingreso >= $2 
          AND a.fechaingreso <= $3
      `;

      const result = await administradorDataSource.executeQuery(query, [
        filtro.idarea,
        filtro.fechaInicio,
        filtro.fechaFinal,
      ]);

      return { cantidad: parseInt(result.rows[0]?.cantidad) || 0 };
    } catch (error) {
      console.error("Error en cantidadReunionesSinAcuerdosDirectos:", error);
      return { cantidad: 0 };
    }
  }

  /**
   * Cuenta reuniones sin acuerdo para un año específico
   * Equivalente a reunionesSinAcuerdo() del Java original
   */
  async reunionesSinAcuerdo(anio) {
    try {
      const query = `
        SELECT COUNT(*) as cantidad
        FROM controlasuntospendientesnew.asunto 
        WHERE tipoasunto = 'R' 
          AND estatus != 'A' 
          AND EXTRACT(YEAR FROM fechaingreso) = $1
      `;

      const result = await administradorDataSource.executeQuery(query, [anio]);
      return parseInt(result.rows[0]?.cantidad) || 0;
    } catch (error) {
      console.error("Error en reunionesSinAcuerdo:", error);
      return 0;
    }
  }

  // ===== NUEVOS MÉTODOS ESPECÍFICOS PARA MODAL PENDIENTES =====

  /**
   * Obtiene cantidad de SIA (Solicitudes de Información) pendientes - Tipo K
   */
  async obtenerSIAPendientes(idarea, fechaInicio, fechaFin) {
    try {
      const query = `
        SELECT 
          COUNT(CASE 
            WHEN asunto.estatus = 'P' 
            AND SUBSTRING(asunto.fechaatender,1,8) <= TO_CHAR(CURRENT_DATE, 'YYYYMMDD') 
            THEN 1 
          END) as vencidos,
          COUNT(CASE 
            WHEN asunto.estatus = 'P' 
            AND SUBSTRING(asunto.fechaatender,1,8) > TO_CHAR(CURRENT_DATE, 'YYYYMMDD') 
            AND SUBSTRING(asunto.fechaatender,1,8) <= TO_CHAR(CURRENT_DATE + INTERVAL '7 days', 'YYYYMMDD') 
            THEN 1 
          END) as porvencer,
          COUNT(CASE 
            WHEN asunto.estatus = 'P' 
            AND (asunto.fechaatender IS NULL 
                 OR SUBSTRING(asunto.fechaatender,1,8) > TO_CHAR(CURRENT_DATE + INTERVAL '7 days', 'YYYYMMDD')) 
            THEN 1 
          END) as activos
        FROM controlasuntospendientesnew.asunto asunto
        INNER JOIN controlasuntospendientesnew.responsable resp ON asunto.idasunto = resp.idasunto
        INNER JOIN controlasuntospendientesnew.area area ON resp.idarea = area.idarea
        WHERE asunto.tipoasunto = 'K' 
          AND area.idarea = $1 
          AND resp.estatus = 'P'
      `;

      const result = await administradorDataSource.executeQuery(query, [
        idarea,
      ]);
      const datos = result.rows[0] || { vencidos: 0, porvencer: 0, activos: 0 };

      return {
        vencidos_d: parseInt(datos.vencidos) || 0,
        porvencer_d: parseInt(datos.porvencer) || 0,
        pendactivos_d: parseInt(datos.activos) || 0,
      };
    } catch (error) {
      console.error("Error en obtenerSIAPendientes:", error);
      return { vencidos_d: 0, porvencer_d: 0, pendactivos_d: 0 };
    }
  }

  /**
   * Obtiene cantidad de COMISIONES pendientes - Tipo M
   */
  async obtenerComisionesPendientes(idarea, fechaInicio, fechaFin) {
    try {
      const query = `
        SELECT 
          COUNT(CASE 
            WHEN asunto.estatus = 'P' 
            AND SUBSTRING(asunto.fechaatender,1,8) <= TO_CHAR(CURRENT_DATE, 'YYYYMMDD') 
            THEN 1 
          END) as vencidos,
          COUNT(CASE 
            WHEN asunto.estatus = 'P' 
            AND SUBSTRING(asunto.fechaatender,1,8) > TO_CHAR(CURRENT_DATE, 'YYYYMMDD') 
            AND SUBSTRING(asunto.fechaatender,1,8) <= TO_CHAR(CURRENT_DATE + INTERVAL '7 days', 'YYYYMMDD') 
            THEN 1 
          END) as porvencer,
          COUNT(CASE 
            WHEN asunto.estatus = 'P' 
            AND (asunto.fechaatender IS NULL 
                 OR SUBSTRING(asunto.fechaatender,1,8) > TO_CHAR(CURRENT_DATE + INTERVAL '7 days', 'YYYYMMDD')) 
            THEN 1 
          END) as activos
        FROM controlasuntospendientesnew.asunto asunto
        INNER JOIN controlasuntospendientesnew.responsable resp ON asunto.idasunto = resp.idasunto
        INNER JOIN controlasuntospendientesnew.area area ON resp.idarea = area.idarea
        WHERE asunto.tipoasunto = 'M' 
          AND area.idarea = $1 
          AND resp.estatus = 'P'
      `;

      const result = await administradorDataSource.executeQuery(query, [
        idarea,
      ]);
      const datos = result.rows[0] || { vencidos: 0, porvencer: 0, activos: 0 };

      return {
        vencidos_d: parseInt(datos.vencidos) || 0,
        porvencer_d: parseInt(datos.porvencer) || 0,
        pendactivos_d: parseInt(datos.activos) || 0,
      };
    } catch (error) {
      console.error("Error en obtenerComisionesPendientes:", error);
      return { vencidos_d: 0, porvencer_d: 0, pendactivos_d: 0 };
    }
  }

  /**
   * Obtiene cantidad de CORREOS pendientes - Tipo C
   */
  async obtenerCorreosPendientes(idarea, fechaInicio, fechaFin) {
    try {
      const query = `
        SELECT 
          COUNT(CASE 
            WHEN asunto.estatus = 'P' 
            AND SUBSTRING(asunto.fechaatender,1,8) <= TO_CHAR(CURRENT_DATE, 'YYYYMMDD') 
            THEN 1 
          END) as vencidos,
          COUNT(CASE 
            WHEN asunto.estatus = 'P' 
            AND SUBSTRING(asunto.fechaatender,1,8) > TO_CHAR(CURRENT_DATE, 'YYYYMMDD') 
            AND SUBSTRING(asunto.fechaatender,1,8) <= TO_CHAR(CURRENT_DATE + INTERVAL '7 days', 'YYYYMMDD') 
            THEN 1 
          END) as porvencer,
          COUNT(CASE 
            WHEN asunto.estatus = 'P' 
            AND (asunto.fechaatender IS NULL 
                 OR SUBSTRING(asunto.fechaatender,1,8) > TO_CHAR(CURRENT_DATE + INTERVAL '7 days', 'YYYYMMDD')) 
            THEN 1 
          END) as activos
        FROM controlasuntospendientesnew.asunto asunto
        INNER JOIN controlasuntospendientesnew.responsable resp ON asunto.idasunto = resp.idasunto
        INNER JOIN controlasuntospendientesnew.area area ON resp.idarea = area.idarea
        WHERE asunto.tipoasunto = 'C' 
          AND area.idarea = $1 
          AND resp.estatus = 'P'
      `;

      const result = await administradorDataSource.executeQuery(query, [
        idarea,
      ]);
      const datos = result.rows[0] || { vencidos: 0, porvencer: 0, activos: 0 };

      return {
        vencidos_d: parseInt(datos.vencidos) || 0,
        porvencer_d: parseInt(datos.porvencer) || 0,
        pendactivos_d: parseInt(datos.activos) || 0,
      };
    } catch (error) {
      console.error("Error en obtenerCorreosPendientes:", error);
      return { vencidos_d: 0, porvencer_d: 0, pendactivos_d: 0 };
    }
  }

  /**
   * Obtiene cantidad de ACUERDOS pendientes - Tipo A (tabla accion, no asunto)
   */
  async obtenerAcuerdosPendientes(idarea, fechaInicio, fechaFin) {
    try {
      const query = `
        SELECT 
          COUNT(CASE 
            WHEN accion.estatus = 'P' 
            AND SUBSTRING(accion.acuerdo_fecha,1,8) <= TO_CHAR(CURRENT_DATE, 'YYYYMMDD') 
            THEN 1 
          END) as vencidos,
          COUNT(CASE 
            WHEN accion.estatus = 'P' 
            AND SUBSTRING(accion.acuerdo_fecha,1,8) > TO_CHAR(CURRENT_DATE, 'YYYYMMDD') 
            AND SUBSTRING(accion.acuerdo_fecha,1,8) <= TO_CHAR(CURRENT_DATE + INTERVAL '7 days', 'YYYYMMDD') 
            THEN 1 
          END) as porvencer,
          COUNT(CASE 
            WHEN accion.estatus = 'P' 
            AND (accion.acuerdo_fecha IS NULL 
                 OR SUBSTRING(accion.acuerdo_fecha,1,8) > TO_CHAR(CURRENT_DATE + INTERVAL '7 days', 'YYYYMMDD')) 
            THEN 1 
          END) as activos
        FROM controlasuntospendientesnew.accion accion
        INNER JOIN controlasuntospendientesnew.responsable resp ON accion.idaccion = resp.idaccion
        INNER JOIN controlasuntospendientesnew.area area ON resp.idarea = area.idarea
        WHERE (accion.activoestatus <> 'CNV' OR accion.activoestatus IS NULL)
          AND area.idarea = $1 
          AND resp.estatus = 'P'
      `;

      const result = await administradorDataSource.executeQuery(query, [
        idarea,
      ]);
      const datos = result.rows[0] || { vencidos: 0, porvencer: 0, activos: 0 };

      return {
        vencidos_d: parseInt(datos.vencidos) || 0,
        porvencer_d: parseInt(datos.porvencer) || 0,
        pendactivos_d: parseInt(datos.activos) || 0,
      };
    } catch (error) {
      console.error("Error en obtenerAcuerdosPendientes:", error);
      return { vencidos_d: 0, porvencer_d: 0, pendactivos_d: 0 };
    }
  }

  /**
   * Obtiene cantidad de REUNIONES PENDIENTES DE REGISTRAR ACUERDOS - Tipo R
   */
  async obtenerReunionesPendientes(idarea, fechaInicio, fechaFin) {
    try {
      const query = `
        SELECT 
          COUNT(CASE 
            WHEN asunto.estatus = 'P' 
            AND SUBSTRING(asunto.fechaatender,1,8) <= TO_CHAR(CURRENT_DATE, 'YYYYMMDD') 
            THEN 1 
          END) as vencidos,
          COUNT(CASE 
            WHEN asunto.estatus = 'P' 
            AND SUBSTRING(asunto.fechaatender,1,8) > TO_CHAR(CURRENT_DATE, 'YYYYMMDD') 
            AND SUBSTRING(asunto.fechaatender,1,8) <= TO_CHAR(CURRENT_DATE + INTERVAL '7 days', 'YYYYMMDD') 
            THEN 1 
          END) as porvencer,
          COUNT(CASE 
            WHEN asunto.estatus = 'P' 
            AND (asunto.fechaatender IS NULL 
                 OR SUBSTRING(asunto.fechaatender,1,8) > TO_CHAR(CURRENT_DATE + INTERVAL '7 days', 'YYYYMMDD')) 
            THEN 1 
          END) as activos
        FROM controlasuntospendientesnew.asunto asunto
        INNER JOIN controlasuntospendientesnew.responsable resp ON asunto.idasunto = resp.idasunto
        INNER JOIN controlasuntospendientesnew.area area ON resp.idarea = area.idarea
        WHERE asunto.tipoasunto = 'R' 
          AND area.idarea = $1 
          AND resp.estatus = 'P'
      `;

      const result = await administradorDataSource.executeQuery(query, [
        idarea,
      ]);
      const datos = result.rows[0] || { vencidos: 0, porvencer: 0, activos: 0 };

      return {
        vencidos_d: parseInt(datos.vencidos) || 0,
        porvencer_d: parseInt(datos.porvencer) || 0,
        pendactivos_d: parseInt(datos.activos) || 0,
      };
    } catch (error) {
      console.error("Error en obtenerReunionesPendientes:", error);
      return { vencidos_d: 0, porvencer_d: 0, pendactivos_d: 0 };
    }
  }

  /**
   * Método principal para obtener todos los detalles de pendientes por área
   * Usa las consultas específicas correctas para cada tipo
   */
  async obtenerDetallesPendientesPorArea(idarea, fechaInicio, fechaFin) {
    try {
      console.log(
        `🔍 === DETALLES PENDIENTES ÁREA ${idarea} (CONSULTAS ESPECÍFICAS) ===`
      );

      const [sia, comisiones, correos, acuerdos, reuniones] = await Promise.all(
        [
          this.obtenerSIAPendientes(idarea, fechaInicio, fechaFin),
          this.obtenerComisionesPendientes(idarea, fechaInicio, fechaFin),
          this.obtenerCorreosPendientes(idarea, fechaInicio, fechaFin),
          this.obtenerAcuerdosPendientes(idarea, fechaInicio, fechaFin),
          this.obtenerReunionesPendientes(idarea, fechaInicio, fechaFin),
        ]
      );

      const resultado = [
        {
          tipoasunto: "SIA",
          tipoAbreviado: "K",
          ...sia,
        },
        {
          tipoasunto: "COMISIONES",
          tipoAbreviado: "M",
          ...comisiones,
        },
        {
          tipoasunto: "CORREOS",
          tipoAbreviado: "C",
          ...correos,
        },
        {
          tipoasunto: "ACUERDOS",
          tipoAbreviado: "A",
          ...acuerdos,
        },
        {
          tipoasunto: "REUNIONES PENDIENTES DE REGISTRAR ACUERDOS",
          tipoAbreviado: "R",
          ...reuniones,
        },
      ];

      console.log("✅ Detalles específicos obtenidos:", resultado);
      return resultado;
    } catch (error) {
      console.error("Error en obtenerDetallesPendientesPorArea:", error);
      return [];
    }
  }
}

export default AsuntoDAO;
