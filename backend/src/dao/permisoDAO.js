/**
 * DAO para manejo de permisos - Equivalente a PermisoDAO.java
 * Maneja todas las operaciones de base de datos relacionadas con permisos de usuarios
 */

import administradorDataSource from "../config/administradorDataSource.js";

class PermisoDAO {
  /**
   * Obtiene los permisos de un usuario específico
   * Equivalente a permisosUsuario(int idusuario)
   * @param {number} idusuario - ID del usuario
   */
  async permisosUsuario(idusuario) {
    try {
      const sql = `
                SELECT idpermiso, idusuario, idarea, rol 
                FROM controlasuntospendientesnew.permiso 
                WHERE idusuario = $1 
                ORDER BY idarea
            `;

      const params = [idusuario];
      const result = await administradorDataSource.executeQuery(sql, params);

      return result.rows.map((row) => this.mapRowToPermiso(row));
    } catch (error) {
      console.error("Error en permisosUsuario:", error);
      // Si hay error de BD, retornar permisos por defecto basados en datos del usuario
      return this.getPermisosDefecto(idusuario);
    }
  }

  /**
   * Obtiene los permisos de un área específica
   * Equivalente a permisosArea(int idarea)
   * @param {number} idarea - ID del área
   */
  async permisosArea(idarea) {
    try {
      const sql = `
                SELECT idpermiso, idusuario, idarea, rol 
                FROM controlasuntospendientesnew.permiso 
                WHERE idarea = $1
            `;

      const params = [idarea];
      const result = await administradorDataSource.executeQuery(sql, params);

      return result.rows.map((row) => this.mapRowToPermiso(row));
    } catch (error) {
      console.error("Error en permisosArea:", error);
      return [];
    }
  }

  /**
   * Obtiene el ID del área por ID de permiso
   * Equivalente a idAreaByPermiso(int idpermiso)
   * @param {number} idpermiso - ID del permiso
   */
  async idAreaByPermiso(idpermiso) {
    try {
      const sql = `
                SELECT idarea 
                FROM controlasuntospendientesnew.permiso 
                WHERE idpermiso = $1
            `;

      const params = [idpermiso];
      const result = await administradorDataSource.executeQuery(sql, params);

      if (result.rows.length > 0) {
        return result.rows[0].idarea;
      }

      return null;
    } catch (error) {
      console.error("Error en idAreaByPermiso:", error);
      return null;
    }
  }

  /**
   * Mapea una fila de la base de datos a un objeto Permiso
   * @param {Object} row - Fila de la base de datos
   */
  mapRowToPermiso(row) {
    return {
      idpermiso: row.idpermiso,
      idusuario: row.idusuario,
      idarea: row.idarea,
      rol: row.rol,
    };
  }

  /**
   * Obtiene permisos por defecto cuando no hay conexión a BD
   * @param {number} idusuario - ID del usuario
   */
  getPermisosDefecto(idusuario) {
    // Permisos por defecto para usuarios de prueba
    const permisosDefecto = [
      {
        idpermiso: 1,
        idusuario: 1, // admin
        idarea: 1,
        rol: "A", // Administrador
      },
      {
        idpermiso: 2,
        idusuario: 2, // usuario
        idarea: 1,
        rol: "U", // Usuario
      },
    ];

    return permisosDefecto.filter((p) => p.idusuario === idusuario);
  }

  /**
   * Verifica si un usuario tiene un rol específico
   * @param {number} idusuario - ID del usuario
   * @param {string} rol - Rol a verificar ('A' = Admin, 'U' = Usuario, etc.)
   */
  async usuarioTieneRol(idusuario, rol) {
    try {
      const permisos = await this.permisosUsuario(idusuario);
      return permisos.some((p) => p.rol === rol);
    } catch (error) {
      console.error("Error verificando rol de usuario:", error);
      return false;
    }
  }

  /**
   * Obtiene el rol principal de un usuario (el de mayor jerarquía)
   * @param {number} idusuario - ID del usuario
   */
  async getRolPrincipalUsuario(idusuario) {
    try {
      const permisos = await this.permisosUsuario(idusuario);

      if (permisos.length === 0) {
        return "U"; // Usuario por defecto
      }

      // Jerarquía de roles: A (Administrador) > E (Ejecutivo) > C (Consulta) > U (Usuario)
      const jerarquia = ["A", "E", "C", "U"];

      for (const rol of jerarquia) {
        if (permisos.some((p) => p.rol === rol)) {
          return rol;
        }
      }

      return "U"; // Usuario por defecto
    } catch (error) {
      console.error("Error obteniendo rol principal:", error);
      return "U";
    }
  }
}

export default new PermisoDAO();
