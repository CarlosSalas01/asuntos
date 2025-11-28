import AsuntoDAO from "../dao/AsuntoDAO.js";
import AreaDAO from "../dao/AreaDAO.js";
import AdministradorReportes from "../services/AdministradorReportes.js";
import administradorDataSource from "../config/administradorDataSource.js";

class FachadaDAO {
  constructor(areas = null) {
    // Initialize DAOs and services
    this.asuntoDAO = new AsuntoDAO();
    this.areaDAO = new AreaDAO();
    this.administradorReportes = new AdministradorReportes();
    this.areas = areas; // AreasConsulta equivalent
  }

  /**
   * Obtiene el último ID de asunto
   * @returns {Promise<number>} El ID máximo de asunto
   */
  async ultimoidAsunto() {
    try {
      return await this.asuntoDAO.getMaxIdAsunto();
    } catch (error) {
      console.error("Error en ultimoidAsunto:", error);
      return -1;
    }
  }

  /**
   * Busca asunto asi gnado por responsable superior y ID asunto
   * @param {number} idRSuperior - ID del responsable superior
   * @param {number} idasunto - ID del asunto
   * @returns {Promise<Object|null>} El asunto encontrado o null
   */
  async buscaAsuntoAsignado(idRSuperior, idasunto) {
    try {
      return await this.asuntoDAO.buscarAsuntoAsignado(idRSuperior, idasunto);
    } catch (error) {
      console.error("Error en buscaAsuntoAsignado:", error);
      throw error;
    }
  }

  /**
   * Busca asuntos por área y tipo - Keet
   * @param {Object} filtro - Filtro de búsqueda
   * @returns {Promise<Array>} Lista de asuntos
   */
  async buscarAsuntosPorAreaKeet(filtro) {
    try {
      return await this.asuntoDAO.buscarAsuntosPorAreaxTipo(filtro, "K");
    } catch (error) {
      console.error("Error en buscarAsuntosPorAreaKeet:", error);
      throw error;
    }
  }

  /**
   * Busca asuntos por área y tipo - Correo
   * @param {Object} filtro - Filtro de búsqueda
   * @returns {Promise<Array>} Lista de asuntos
   */
  async buscarAsuntosPorAreaCorreo(filtro) {
    try {
      return await this.asuntoDAO.buscarAsuntosPorAreaxTipo(filtro, "C");
    } catch (error) {
      console.error("Error en buscarAsuntosPorAreaCorreo:", error);
      throw error;
    }
  }

  /**
   * Busca asuntos por área y tipo - Comisiones
   * @param {Object} filtro - Filtro de búsqueda
   * @returns {Promise<Array>} Lista de asuntos
   */
  async buscarAsuntosPorAreaComisiones(filtro) {
    try {
      return await this.asuntoDAO.buscarAsuntosPorAreaxTipo(filtro, "M");
    } catch (error) {
      console.error("Error en buscarAsuntosPorAreaComisiones:", error);
      throw error;
    }
  }

  /**
   * Busca asuntos por área y tipo - Convenio
   * @param {Object} filtro - Filtro de búsqueda
   * @returns {Promise<Array>} Lista de asuntos
   */
  async buscarAsuntosPorAreaConvenio(filtro) {
    try {
      return await this.asuntoDAO.buscarAsuntosPorAreaxTipo(filtro, "V");
    } catch (error) {
      console.error("Error en buscarAsuntosPorAreaConvenio:", error);
      throw error;
    }
  }

  /**
   * Busca asuntos por área y tipo - Reuniones
   * @param {Object} filtro - Filtro de búsqueda
   * @returns {Promise<Array>} Lista de asuntos
   */
  async buscarAsuntosPorAreaReuniones(filtro) {
    try {
      return await this.asuntoDAO.buscarAsuntosReunion(filtro);
    } catch (error) {
      console.error("Error en buscarAsuntosPorAreaReuniones:", error);
      throw error;
    }
  }

  /**
   * Obtiene la cantidad de asuntos por área y tipo
   * @param {Object} filtro - Filtro de búsqueda
   * @param {string} tipo - Tipo de asunto
   * @returns {Promise<number>} Cantidad de asuntos
   */
  async cantidadAsuntosPorAreaxTipo(filtro, tipo) {
    try {
      const resultado = await this.asuntoDAO.cantidadAsuntosxAreaxTipo(
        filtro,
        tipo
      );
      return resultado ? resultado.cantidad : 0;
    } catch (error) {
      console.error("Error en cantidadAsuntosPorAreaxTipo:", error);
      throw error;
    }
  }

  /**
   * Obtiene la cantidad de asuntos - reuniones
   * @param {Object} filtro - Filtro de búsqueda
   * @returns {Promise<number>} Cantidad de reuniones
   */
  async cantidadAsuntosReuniones(filtro) {
    try {
      const resultado = await this.asuntoDAO.cantidadAsuntosReunion(filtro);
      return resultado ? resultado.cantidad : 0;
    } catch (error) {
      console.error("Error en cantidadAsuntosReuniones:", error);
      throw error;
    }
  }

  /**
   * Obtiene la cantidad de reuniones sin acuerdos directos
   * @param {number|Object} param - ID de área o filtro
   * @returns {Promise<number>} Cantidad de reuniones sin acuerdos
   */
  async cantidadReunionesSinAcuerdosDirectos(param) {
    try {
      let resultado;
      if (typeof param === "number") {
        // Si es un número, es un ID de área
        resultado = await this.asuntoDAO.cantidadReunionesSinAcuerdosDirectos(
          param
        );
      } else {
        // Si es un objeto, es un filtro
        resultado = await this.asuntoDAO.cantidadReunionesSinAcuerdosDirectos(
          param
        );
      }
      return resultado ? resultado.cantidad : 0;
    } catch (error) {
      console.error("Error en cantidadReunionesSinAcuerdosDirectos:", error);
      throw error;
    }
  }

  /**
   * Obtiene la cantidad de reuniones sin acuerdos
   * @param {Object} filtro - Filtro de búsqueda
   * @returns {Promise<number>} Cantidad de reuniones sin acuerdos
   */
  async cantidadReunionesSinAcuerdos(filtro) {
    try {
      const resultado = await this.asuntoDAO.cantidadReunionesSinAcuerdos(
        filtro
      );
      return resultado ? resultado.cantidad : 0;
    } catch (error) {
      console.error("Error en cantidadReunionesSinAcuerdos:", error);
      throw error;
    }
  }

  /**
   * Busca reuniones sin acuerdos
   * @param {Object} filtro - Filtro de búsqueda
   * @returns {Promise<Array>} Lista de reuniones sin acuerdos
   */
  async buscarReunionesSinAcuerdos(filtro) {
    try {
      return await this.asuntoDAO.buscarAsuntosSAccionesxArea();
    } catch (error) {
      console.error("Error en buscarReunionesSinAcuerdos:", error);
      throw error;
    }
  }

  /**
   * Obtiene reuniones sin acuerdo por año
   * @param {string} anio - Año de consulta
   * @returns {Promise<number>} Cantidad de reuniones sin acuerdo
   */
  async reunionesSinAcuerdo(anio) {
    try {
      return await this.asuntoDAO.reunionesSinAcuerdo(anio);
    } catch (error) {
      console.error("Error en reunionesSinAcuerdo:", error);
      throw error;
    }
  }

  /**
   * Busca asunto por clave primaria
   * @param {number} pk - ID del asunto
   * @returns {Promise<Object|null>} El asunto encontrado o null
   */
  async buscaAsuntoPorLlavePrimaria(pk) {
    try {
      return await this.asuntoDAO.findByPrimaryKey(pk);
    } catch (error) {
      console.error("Error en buscaAsuntoPorLlavePrimaria:", error);
      throw error;
    }
  }

  /**
   * Obtiene área por ID
   * @param {number} idarea - ID del área
   * @returns {Promise<Object|null>} El área encontrada o null
   */
  async nombreArea(idarea) {
    try {
      return await this.areaDAO.getArea(idarea);
    } catch (error) {
      console.error("Error en nombreArea:", error);
      throw error;
    }
  }

  /**
   * Obtiene el área superior
   * @param {number} idarea - ID del área
   * @param {number} nivel - Nivel superior
   * @returns {Promise<number>} ID del área superior
   */
  async obtenAreaSuperior(idarea, nivel) {
    try {
      return await this.areaDAO.obtenAreaSuperior(idarea, nivel);
    } catch (error) {
      console.error("Error en obtenAreaSuperior:", error);
      throw error;
    }
  }

  /**
   * Obtiene resumen de área
   * @param {number} idarea - ID del área
   * @param {string} tipo - Tipo de asunto
   * @param {Object} filtro - Filtro de búsqueda
   * @returns {Promise<Object>} Resumen del área
   */
  async obtenResumenArea(idarea, tipo, filtro) {
    try {
      return await this.administradorReportes.obtenResumenArea(
        idarea,
        tipo,
        filtro
      );
    } catch (error) {
      console.error("Error en obtenResumenArea:", error);
      throw error;
    }
  }

  /**
   * Obtiene resumen de asuntos por área
   * @param {number} idarea - ID del área
   * @param {string} tipo - Tipo de asunto
   * @param {Object} filtro - Filtro de búsqueda
   * @returns {Promise<Object>} Resumen de asuntos del área
   */
  async obtenResumenAsuntosArea(idarea, tipo, filtro) {
    try {
      return await this.administradorReportes.obtenResumenAsuntosArea(
        idarea,
        tipo,
        filtro
      );
    } catch (error) {
      console.error("Error en obtenResumenAsuntosArea:", error);
      throw error;
    }
  }

  /**
   * Obtiene resumen de inicio
   * @param {string} fecha1 - Fecha inicio
   * @param {string} fecha2 - Fecha fin
   * @param {Array} areas - Lista de áreas
   * @returns {Promise<Array>} Resumen de inicio
   */
  async obtenResumenInicio(fecha1, fecha2, areas) {
    try {
      return await this.administradorReportes.obtenResumenInicio(
        fecha1,
        fecha2,
        areas
      );
    } catch (error) {
      console.error("Error en obtenResumenInicio:", error);
      throw error;
    }
  }

  /**
   * Obtiene resumen de acuerdos de inicio
   * @param {string} fecha1 - Fecha inicio
   * @param {string} fecha2 - Fecha fin
   * @param {Array} areas - Lista de áreas
   * @returns {Promise<Array>} Resumen de acuerdos de inicio
   */
  async obtenResumenAcInicio(fecha1, fecha2, areas) {
    try {
      return await this.administradorReportes.obtenResumenAcInicio(
        fecha1,
        fecha2,
        areas
      );
    } catch (error) {
      console.error("Error en obtenResumenAcInicio:", error);
      throw error;
    }
  }

  /**
   * Obtiene resumen inicial general
   * @param {string} fecha1 - Fecha inicio
   * @param {string} fecha2 - Fecha fin
   * @returns {Promise<Object>} Resumen inicial
   */
  async obtenResumenIni(fecha1, fecha2) {
    try {
      return await this.administradorReportes.obtenResumenIni(fecha1, fecha2);
    } catch (error) {
      console.error("Error en obtenResumenIni:", error);
      throw error;
    }
  }

  /**
   * Método de resumen semanal mensual para totales
   * @param {string} tipo - Tipo de asunto
   * @param {string} fecha1 - Fecha inicio
   * @param {string} fecha2 - Fecha fin
   * @returns {Promise<Object>} Resumen semanal mensual
   */
  async resumenSemanalMensTots(tipo, fecha1, fecha2) {
    try {
      return await this.administradorReportes.resumenSemanalMensTots(
        tipo,
        fecha1,
        fecha2
      );
    } catch (error) {
      console.error("Error en resumenSemanalMensTots:", error);
      throw error;
    }
  }

  /**
   * Método de resumen semanal mensual para acuerdos totales
   * @param {string} fecha1 - Fecha inicio
   * @param {string} fecha2 - Fecha fin
   * @returns {Promise<Object>} Resumen semanal mensual de acuerdos
   */
  async resumenSemanalMensAcuerdosTots(fecha1, fecha2) {
    try {
      return await this.administradorReportes.resumenSemanalMensAcuerdosTots(
        fecha1,
        fecha2
      );
    } catch (error) {
      console.error("Error en resumenSemanalMensAcuerdosTots:", error);
      throw error;
    }
  }

  /**
   * Método de resumen semanal mensual para reuniones totales
   * @param {string} fecha2 - Fecha fin
   * @returns {Promise<Object>} Resumen semanal mensual de reuniones
   */
  async resumenSemanalMensReunionesTots(fecha2) {
    try {
      return await this.administradorReportes.resumenSemanalMensReunionesTots(
        fecha2
      );
    } catch (error) {
      console.error("Error en resumenSemanalMensReunionesTots:", error);
      throw error;
    }
  }

  /**
   * Cierra la conexión a la base de datos
   */
  async close() {
    try {
      // Cerrar el pool de conexiones si está disponible
      if (
        administradorDataSource &&
        typeof administradorDataSource.close === "function"
      ) {
        await administradorDataSource.close();
      }
    } catch (error) {
      console.error("Error cerrando conexiones:", error);
    }
  }
}

export default FachadaDAO;
