/**
 * AdministradorReportes - Equivalente a AdministradorReportes.java
 * Maneja la generación de reportes y consultas complejas
 */

import AsuntoDAO from "../dao/AsuntoDAO.js";
import AreaDAO from "../dao/AreaDAO.js";

class AdministradorReportes {
  constructor(areas = null) {
    this.areas = areas;
    this.asuntoDAO = new AsuntoDAO(areas);
    this.areaDAO = new AreaDAO();
  }

  /**
   * Obtiene reporte por área específica - Equivalente a obtenReportePorArea() del Java original
   * Genera el desglose detallado de asuntos por tipo para un área específica
   */
  async obtenReportePorArea(idarea, filtro) {
    try {
      // Tipos de asunto según el sistema original Java
      const tiposAsunto = [
        { codigo: "K", nombre: "SIA" },
        { codigo: "M", nombre: "COMISIONES" },
        { codigo: "C", nombre: "CORREOS" },
        { codigo: "A", nombre: "ACUERDOS" },
      ];

      // Obtener información del área
      const areaData = await this.areaDAO.getArea(idarea);
      if (!areaData) {
        return [];
      }

      const resumen = [];

      // Procesar cada tipo de asunto
      for (const tipo of tiposAsunto) {
        const detallesTipo = await this.obtenerResumenPorTipo(
          idarea,
          tipo,
          filtro
        );
        resumen.push(detallesTipo);
      }

      // Agregar reuniones pendientes de registrar acuerdos (caso especial)
      const reuniones = await this.obtenerResumenReuniones(idarea, filtro);
      resumen.push(reuniones);

      // Construir resultado final
      const reporteArea = {
        area: areaData,
        resumen: resumen,
      };

      console.log(`✅ Reporte generado para ${areaData.siglas}:`, {
        tiposAsunto: resumen.length,
        totalPendientes: resumen.reduce(
          (sum, item) =>
            sum + item.vencidos_d + item.porvencer_d + item.pendactivos_d,
          0
        ),
      });

      return [reporteArea];
    } catch (error) {
      console.error("Error en obtenReportePorArea:", error);
      throw error;
    }
  }

  /**
   * Obtiene resumen para un tipo específico de asunto
   * Equivalente a las consultas individuales en AdministradorReportes.java
   */
  async obtenerResumenPorTipo(idarea, tipo, filtro) {
    try {
      // Configurar filtro para el área específica
      const filtroArea = {
        ...filtro,
        idarea: idarea,
      };

      // Obtener cantidades por estado
      const vencidos = await this.asuntoDAO.cantidadAsuntosVencidosDirectos(
        tipo.codigo,
        idarea,
        filtroArea
      );
      const porVencer = await this.asuntoDAO.cantidadAsuntosPorVencerDirectos(
        tipo.codigo,
        idarea,
        filtroArea
      );
      const activos = await this.asuntoDAO.cantidadAsuntosPendActivosDirectos(
        tipo.codigo,
        idarea,
        filtroArea
      );

      return {
        tipoasunto: tipo.nombre,
        tipoAbreviado: tipo.codigo,
        vencidos_d: vencidos.cantidad || 0,
        porvencer_d: porVencer.cantidad || 0,
        pendactivos_d: activos.cantidad || 0,
      };
    } catch (error) {
      console.error(
        `Error obteniendo resumen para tipo ${tipo.nombre}:`,
        error
      );
      return {
        tipoasunto: tipo.nombre,
        tipoAbreviado: tipo.codigo,
        vencidos_d: 0,
        porvencer_d: 0,
        pendactivos_d: 0,
      };
    }
  }

  /**
   * Obtiene resumen específico para reuniones
   * Equivalente al caso especial "R" en AdministradorReportes.java
   */
  async obtenerResumenReuniones(idarea, filtro) {
    try {
      // Configurar filtro para el área específica
      const filtroArea = {
        ...filtro,
        idarea: idarea,
      };

      const reunionesSinAcuerdos =
        await this.asuntoDAO.cantidadReunionesSinAcuerdosDirectos(filtroArea);

      return {
        tipoasunto: "REUNIONES PENDIENTES DE REGISTRAR ACUERDOS",
        tipoAbreviado: "R",
        vencidos_d: reunionesSinAcuerdos.cantidad || 0, // Las reuniones se muestran como "vencidas"
        porvencer_d: 0, // Las reuniones no tienen "por vencer"
        pendactivos_d: 0, // Las reuniones no tienen "activos"
      };
    } catch (error) {
      console.error("Error obteniendo resumen de reuniones:", error);
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
   * Genera totales para reporte diario
   * Equivalente a generaTotalesReporteDiario() del Java original
   */
  async generaTotalesReporteDiario(filtro) {
    try {
      console.log("📊 Generando totales para reporte diario...");

      const tiposAsunto = [
        { codigo: "K", nombre: "SIA" },
        { codigo: "M", nombre: "COMISIONES" },
        { codigo: "C", nombre: "CORREOS" },
        { codigo: "A", nombre: "ACUERDOS" },
        { codigo: "R", nombre: "REUNIONES PENDIENTES DE REGISTRAR ACUERDOS" },
      ];

      const resumenTotal = [];

      for (const tipo of tiposAsunto) {
        if (tipo.codigo === "R") {
          // Caso especial para reuniones
          const reuniones = await this.asuntoDAO.reunionesSinAcuerdo(
            new Date().getFullYear()
          );
          resumenTotal.push({
            tipoasunto: tipo.nombre,
            tipoAbreviado: tipo.codigo,
            vencidos_d: reuniones,
            porvencer_d: 0,
            pendactivos_d: 0,
          });
        } else {
          // Tipos normales de asunto
          // Aquí implementarías las consultas totales por tipo
          resumenTotal.push({
            tipoasunto: tipo.nombre,
            tipoAbreviado: tipo.codigo,
            vencidos_d: 0,
            porvencer_d: 0,
            pendactivos_d: 0,
          });
        }
      }

      return {
        area: { nombre: "TOTALES" },
        resumen: resumenTotal,
      };
    } catch (error) {
      console.error("Error en generaTotalesReporteDiario:", error);
      throw error;
    }
  }

  /**
   * Obtiene reporte para múltiples áreas
   * Equivalente a obtenReporteArea() del Java original
   */
  async obtenReporteArea(subareas, filtro) {
    try {
      const reporteFinal = [];

      for (const area of subareas) {
        const idarea = area.idarea || area.id;
        const reporteArea = await this.obtenReportePorArea(idarea, filtro);

        if (reporteArea && reporteArea.length > 0) {
          reporteFinal.push(reporteArea[0]);
        }
      }

      return reporteFinal;
    } catch (error) {
      console.error("Error en obtenReporteArea:", error);
      throw error;
    }
  }
}

export default AdministradorReportes;
