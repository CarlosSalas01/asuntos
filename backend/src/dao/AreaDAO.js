/**
 * AreaDAO - Equivalente a AreaDAO.java
 * Maneja las consultas relacionadas con áreas
 */

import administradorDataSource from "../config/administradorDataSource.js";

class AreaDAO {
  constructor() {}

  /**
   * Obtiene un área por su ID
   * Equivalente a getArea() del Java original
   */
  async getArea(idarea) {
    try {
      const query = `
        SELECT idarea, nombre, siglas, nivel, dependede, idresponsable, 
               fechacreacion, estatus, descripcion
        FROM controlasuntospendientesnew.area 
        WHERE idarea = $1
      `;

      const result = await administradorDataSource.executeQuery(query, [
        idarea,
      ]);

      if (result.rows.length > 0) {
        return result.rows[0];
      }

      return null;
    } catch (error) {
      console.error("Error en getArea:", error);
      return null;
    }
  }

  /**
   * Obtiene áreas dependientes de un área superior
   * Equivalente a areasDependientes() del Java original
   */
  async areasDependientes(dependeD, ordenPor = "nombre") {
    try {
      let orderByClause = "";
      switch (ordenPor.toLowerCase()) {
        case "nivel":
          orderByClause = "ORDER BY nivel, nombre";
          break;
        case "siglas":
          orderByClause = "ORDER BY siglas";
          break;
        default:
          orderByClause = "ORDER BY nombre";
      }

      const query = `
        SELECT idarea, nombre, siglas, nivel, dependede, idresponsable, 
               fechacreacion, estatus, descripcion
        FROM controlasuntospendientesnew.area 
        WHERE dependede = $1 AND estatus = 'A'
        ${orderByClause}
      `;

      const result = await administradorDataSource.executeQuery(query, [
        dependeD,
      ]);
      return result.rows;
    } catch (error) {
      console.error("Error en areasDependientes:", error);
      return [];
    }
  }

  /**
   * Obtiene áreas de un subnivel específico
   * Equivalente a areasSubNivel() del Java original
   */
  async areasSubNivel(dependeD) {
    try {
      const query = `
        SELECT idarea, nombre, siglas, nivel, dependede, idresponsable, 
               fechacreacion, estatus, descripcion
        FROM controlasuntospendientesnew.area 
        WHERE nivel > (SELECT nivel FROM controlasuntospendientesnew.area WHERE idarea = $1)
          AND estatus = 'A'
        ORDER BY nivel, nombre
      `;

      const result = await administradorDataSource.executeQuery(query, [
        dependeD,
      ]);
      return result.rows;
    } catch (error) {
      console.error("Error en areasSubNivel:", error);
      return [];
    }
  }

  /**
   * Obtiene áreas responsables por nivel
   * Equivalente a areasResponsablesxNivel() del Java original
   */
  async areasResponsablesxNivel(nivel) {
    try {
      const query = `
        SELECT idarea, nombre, siglas, nivel, dependede, idresponsable, 
               fechacreacion, estatus, descripcion
        FROM controlasuntospendientesnew.area 
        WHERE nivel = $1 AND estatus = 'A'
        ORDER BY nombre
      `;

      const result = await administradorDataSource.executeQuery(query, [nivel]);
      return result.rows;
    } catch (error) {
      console.error("Error en areasResponsablesxNivel:", error);
      return [];
    }
  }

  /**
   * Obtiene todas las áreas activas
   * Equivalente a obtenTodasAreas() del Java original
   */
  async obtenTodasAreas() {
    try {
      const query = `
        SELECT idarea, nombre, siglas, nivel, dependede, idresponsable, 
               fechacreacion, estatus, descripcion
        FROM controlasuntospendientesnew.area 
        WHERE estatus = 'A'
        ORDER BY nivel, nombre
      `;

      const result = await administradorDataSource.executeQuery(query, []);
      return result.rows;
    } catch (error) {
      console.error("Error en obtenTodasAreas:", error);
      return [];
    }
  }

  /**
   * Obtiene el área superior (padre) de un área
   * Equivalente a buscaAreaSuperior() del Java original
   */
  async buscaAreaSuperior(idarea) {
    try {
      const query = `
        SELECT p.idarea, p.nombre, p.siglas, p.nivel, p.dependede, p.idresponsable, 
               p.fechacreacion, p.estatus, p.descripcion
        FROM controlasuntospendientesnew.area a
        INNER JOIN controlasuntospendientesnew.area p ON a.dependede = p.idarea
        WHERE a.idarea = $1 AND p.estatus = 'A'
      `;

      const result = await administradorDataSource.executeQuery(query, [
        idarea,
      ]);

      if (result.rows.length > 0) {
        return result.rows[0];
      }

      return null;
    } catch (error) {
      console.error("Error en buscaAreaSuperior:", error);
      return null;
    }
  }

  /**
   * Obtiene áreas de nivel 0 y 1 (áreas principales)
   * Equivalente a areasResponsablesNivel0y1() del Java original
   */
  async areasResponsablesNivel0y1() {
    try {
      const query = `
        SELECT idarea, nombre, siglas, nivel, dependede, idresponsable, 
               fechacreacion, estatus, descripcion
        FROM controlasuntospendientesnew.area 
        WHERE nivel IN (0, 1) AND estatus = 'A'
        ORDER BY nivel, nombre
      `;

      const result = await administradorDataSource.executeQuery(query, []);
      return result.rows;
    } catch (error) {
      console.error("Error en areasResponsablesNivel0y1:", error);
      return [];
    }
  }

  /**
   * Busca áreas por criterio de búsqueda
   */
  async buscarAreas(criterio) {
    try {
      const query = `
        SELECT idarea, nombre, siglas, nivel, dependede, idresponsable, 
               fechacreacion, estatus, descripcion
        FROM controlasuntospendientesnew.area 
        WHERE (LOWER(nombre) LIKE LOWER($1) OR LOWER(siglas) LIKE LOWER($1))
          AND estatus = 'A'
        ORDER BY nivel, nombre
      `;

      const result = await administradorDataSource.executeQuery(query, [
        `%${criterio}%`,
      ]);
      return result.rows;
    } catch (error) {
      console.error("Error en buscarAreas:", error);
      return [];
    }
  }
}

export default AreaDAO;
