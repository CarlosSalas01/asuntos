/**
 * AsuntoDAO - Equivalente a AsuntoDAO.java
 * Maneja las consultas relacionadas con asuntos
 */

import administradorDataSource from "../config/administradorDataSource.js";

class AsuntoDAO {
  constructor(areas = null) {
    this.areas = areas;
  }

  /**
   * Obtiene la cantidad de asuntos por área y tipo
   * Equivalente a cantidadAsuntosxAreaxTipo() del Java original
   */
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

  /**
   * Obtiene la cantidad de asuntos vencidos directos
   * Equivalente a cantidadAsuntosVencidosDirectos() del Java original
   */
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

  /**
   * Obtiene la cantidad de asuntos por vencer directos
   * Equivalente a cantidadAsuntosPorVencerDirectos() del Java original
   */
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
}

export default AsuntoDAO;
