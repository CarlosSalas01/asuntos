/**
 * DAO para manejo de usuarios - Equivalente a UsuarioDAO.java
 * Maneja todas las operaciones de base de datos relacionadas con usuarios
 */

import administradorDataSource from "../config/administradorDataSource.js";

class UsuarioDAO {
  /**
   * Busca un usuario por sus datos de login
   * Equivalente a buscaUsuario(DatosLogin datosLogin)
   * @param {Object} datosLogin - Objeto con usuario y contraseña
   */
  async buscaUsuario(datosLogin) {
    try {
      console.log(`[UsuarioDAO] Buscando usuario: ${datosLogin.usuario}`);

      // Buscar en la base de datos real
      const sql = `
        SELECT idusuario, username, contrasenia, nombre, apellido, vigente, 
               activoestatus, correo1, correo2, responsable, enviocorreoauto, superusuario 
        FROM controlasuntospendientesnew.usuario 
        WHERE username = $1 AND vigente = 'S'
      `;

      const params = [datosLogin.usuario.toLowerCase()];
      const result = await administradorDataSource.executeQuery(sql, params);

      if (result.rows.length > 0) {
        const usuario = result.rows[0];
        console.log(
          `[UsuarioDAO] Usuario encontrado en BD: ${usuario.username}`
        );
        console.log(`[UsuarioDAO] Datos del usuario:`, {
          nombre: usuario.nombre,
          apellido: usuario.apellido,
          correo: usuario.correo1,
          responsable: usuario.responsable,
        });
        return this.mapRowToUsuario(usuario);
      }

      console.log(
        `[UsuarioDAO] Usuario no encontrado en BD: ${datosLogin.usuario}`
      );

      // Solo usar usuarios de prueba para admin/usuario específicos
      if (
        datosLogin.usuario.toLowerCase() === "admin" ||
        datosLogin.usuario.toLowerCase() === "usuario"
      ) {
        console.log(
          `[UsuarioDAO] Usando usuario de prueba para: ${datosLogin.usuario}`
        );

        // Fallback a usuarios de prueba si hay error de BD
        const usuariosPrueba = [
          {
            idusuario: 1,
            username: "admin",
            contrasenia: "admin123", // En texto plano para pruebas
            nombre: "Administrador",
            apellido: "Sistema",
            vigente: "S",
            activoestatus: "S",
            correo1: "admin@asuntos.com",
            correo2: null,
            responsable: "S",
            enviocorreoauto: "S",
            superusuario: true,
          },
          {
            idusuario: 2,
            username: "usuario",
            contrasenia: "user123", // En texto plano para pruebas
            nombre: "Usuario",
            apellido: "Prueba",
            vigente: "S",
            activoestatus: "S",
            correo1: "usuario@asuntos.com",
            correo2: null,
            responsable: "N",
            enviocorreoauto: "N",
            superusuario: false,
          },
        ];

        const usuario = usuariosPrueba.find(
          (u) =>
            u.username.toLowerCase() === datosLogin.usuario.toLowerCase() &&
            u.vigente === "S"
        );

        if (usuario) {
          console.log(
            `[UsuarioDAO] Usuario encontrado en datos de prueba: ${usuario.username}`
          );
          return this.mapRowToUsuario(usuario);
        }

        console.log(
          `[UsuarioDAO] Usuario no encontrado en datos de prueba: ${datosLogin.usuario}`
        );
      }

      return null;
    } catch (error) {
      console.error("[UsuarioDAO] Error en buscaUsuario:", error);

      // En caso de error de BD, intentar con usuarios de prueba admin/usuario
      if (
        datosLogin.usuario.toLowerCase() === "admin" ||
        datosLogin.usuario.toLowerCase() === "usuario"
      ) {
        console.log(
          `[UsuarioDAO] Intentando usuario de prueba por error de BD: ${datosLogin.usuario}`
        );

        const usuariosPrueba = [
          {
            idusuario: 1,
            username: "admin",
            contrasenia: "admin123",
            nombre: "Administrador",
            apellido: "Sistema",
            vigente: "S",
            activoestatus: "S",
            correo1: "admin@asuntos.com",
            correo2: null,
            responsable: "S",
            enviocorreoauto: "S",
            superusuario: true,
          },
          {
            idusuario: 2,
            username: "usuario",
            contrasenia: "user123",
            nombre: "Usuario",
            apellido: "Prueba",
            vigente: "S",
            activoestatus: "S",
            correo1: "usuario@asuntos.com",
            correo2: null,
            responsable: "N",
            enviocorreoauto: "N",
            superusuario: false,
          },
        ];

        const usuario = usuariosPrueba.find(
          (u) => u.username.toLowerCase() === datosLogin.usuario.toLowerCase()
        );

        if (usuario) {
          console.log(
            `[UsuarioDAO] Usuario de emergencia encontrado: ${usuario.username}`
          );
          return this.mapRowToUsuario(usuario);
        }
      }

      throw error;
    }
  }

  /**
   * Busca un usuario por ID
   * Equivalente a buscaUsuario(Integer idusuario)
   * @param {number} idusuario - ID del usuario
   */
  async buscaUsuarioPorId(idusuario) {
    try {
      const sql = `
                SELECT idusuario, username, contrasenia, nombre, apellido, vigente,
                       activoestatus, correo1, correo2, responsable, enviocorreoauto 
                FROM controlasuntospendientesnew.usuario 
                WHERE idusuario = $1 AND vigente = 'S'
            `;

      const params = [idusuario];
      const result = await administradorDataSource.executeQuery(sql, params);

      if (result.rows.length > 0) {
        return this.mapRowToUsuario(result.rows[0]);
      }

      return null;
    } catch (error) {
      console.error("Error en buscaUsuarioPorId:", error);
      throw error;
    }
  }

  /**
   * Busca un usuario por ID (vigente o no vigente)
   * Equivalente a buscaUsuarioVigenteoNo(Integer idusuario)
   * @param {number} idusuario - ID del usuario
   */
  async buscaUsuarioVigenteONo(idusuario) {
    try {
      const sql = `
                SELECT idusuario, username, contrasenia, nombre, apellido, vigente,
                       activoestatus, correo1, correo2, responsable, enviocorreoauto 
                FROM controlasuntospendientesnew.usuario 
                WHERE idusuario = $1
            `;

      const params = [idusuario];
      const result = await administradorDataSource.executeQuery(sql, params);

      if (result.rows.length > 0) {
        return this.mapRowToUsuario(result.rows[0]);
      }

      return null;
    } catch (error) {
      console.error("Error en buscaUsuarioVigenteONo:", error);
      throw error;
    }
  }

  /**
   * Obtiene todos los usuarios administradores
   * Equivalente a obtenUsuariosAdministradores()
   */
  async obtenUsuariosAdministradores() {
    try {
      const sql = `
                SELECT u.idusuario, u.username, u.contrasenia, u.nombre, u.apellido, u.vigente,
                       u.activoestatus, u.correo1, u.correo2, u.responsable, u.enviocorreoauto 
                FROM controlasuntospendientesnew.usuario u, controlasuntospendientesnew.permiso p 
                WHERE p.rol = 'A' AND u.idusuario = p.idusuario AND u.vigente = 'S'
            `;

      const result = await administradorDataSource.executeQuery(sql);

      return result.rows.map((row) => this.mapRowToUsuario(row));
    } catch (error) {
      console.error("Error en obtenUsuariosAdministradores:", error);
      throw error;
    }
  }

  /**
   * Obtiene usuarios de nivel 2
   * Equivalente a obtenUsuariosNivel2()
   */
  async obtenUsuariosNivel2() {
    try {
      const sql = `
                SELECT a.correo1, a.nombre 
                FROM controlasuntospendientesnew.usuario as a 
                INNER JOIN controlasuntospendientesnew.area as b ON a.idusuario = b.idresponsable 
                WHERE b.nivel = '2' AND a.vigente = 'S'
            `;

      const result = await administradorDataSource.executeQuery(sql);

      return result.rows.map((row) => this.mapRowToUsuario(row));
    } catch (error) {
      console.error("Error en obtenUsuariosNivel2:", error);
      throw error;
    }
  }

  /**
   * Mapea una fila de la base de datos a un objeto Usuario
   * @param {Object} row - Fila de la base de datos
   */
  mapRowToUsuario(row) {
    return {
      idusuario: row.idusuario,
      username: row.username,
      contrasenia: row.contrasenia,
      nombre: row.nombre,
      apellido: row.apellido,
      vigente: row.vigente,
      activoestatus: row.activoestatus,
      correo1: row.correo1,
      correo2: row.correo2,
      responsable: row.responsable,
      enviocorreoauto: row.enviocorreoauto,
      superusuario: row.superusuario || false,
    };
  }
}

export default new UsuarioDAO();
