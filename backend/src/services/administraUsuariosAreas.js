/**
 * Capa de negocio para administración de usuarios y áreas
 * Equivalente a AdministraUsuariosAreas.java
 */

import usuarioDAO from "../dao/usuarioDAO.js";
import permisoDAO from "../dao/permisoDAO.js";
import bcrypt from "bcryptjs";
import jwt from "jsonwebtoken";
import datosGlobales from "../config/datosGlobales.js";

class AdministraUsuariosAreas {
  /**
   * Busca y autentica un usuario
   * Equivalente a buscaUsuario(DatosLogin datosLogin)
   * @param {Object} datosLogin - Objeto con usuario y contraseña
   */
  async buscaUsuario(datosLogin) {
    try {
      // Validar que se proporcionen usuario y contraseña
      if (!datosLogin.usuario || !datosLogin.contrasenia) {
        throw new Error("Usuario y contraseña son requeridos");
      }

      // Buscar usuario en la base de datos
      const usuarioDTO = await usuarioDAO.buscaUsuario(datosLogin);

      if (!usuarioDTO) {
        console.log(
          `[AUTH] Usuario no encontrado en BD: ${datosLogin.usuario}`
        );
        return null; // Usuario no encontrado
      }

      console.log(
        `[AUTH] Usuario encontrado: ${usuarioDTO.username} - ${usuarioDTO.nombre} ${usuarioDTO.apellido}`
      );
      console.log(
        `[AUTH] Entorno: ${
          datosGlobales.ESTADO_PRODUCCION ? "PRODUCCIÓN" : "DESARROLLO"
        }`
      );

      // Verificar contraseña
      const passwordValida = await this.verificarPassword(
        datosLogin.contrasenia,
        usuarioDTO.contrasenia
      );

      if (!passwordValida) {
        console.log(
          `[AUTH] Contraseña inválida para usuario: ${datosLogin.usuario}`
        );
        return null; // Contraseña incorrecta
      }

      console.log(
        `[AUTH] ✅ Autenticación exitosa para: ${usuarioDTO.username}`
      );

      // Convertir DTO a Bean (equivalente al mapeo en Java)
      const usuarioBean = this.convertirDTOaBean(usuarioDTO);

      // Cargar permisos del usuario
      await this.cargaPermisosUsuarios(usuarioBean);

      return usuarioBean;
    } catch (error) {
      console.error("Error en buscaUsuario:", error);
      throw error;
    }
  }

  /**
   * Verifica la contraseña del usuario
   * @param {string} passwordInput - Contraseña introducida
   * @param {string} passwordDB - Contraseña almacenada en BD
   */
  async verificarPassword(passwordInput, passwordDB) {
    try {
      // En DESARROLLO: Permitir cualquier contraseña para facilitar pruebas
      if (!datosGlobales.ESTADO_PRODUCCION) {
        console.log(
          `[AUTH-DEV] Modo desarrollo: Aceptando cualquier contraseña para usuario`
        );
        return true;
      }

      // En PRODUCCIÓN: Validar contraseñas reales
      if (datosGlobales.AUTENTICACION_LDAP) {
        // TODO: Implementar autenticación LDAP/Directorio Activo
        console.log(`[AUTH-PROD] Validación LDAP no implementada aún`);
        return false;
      }

      // Validación de contraseñas en BD
      if (passwordDB.startsWith("$2")) {
        // Contraseña hasheada con bcrypt
        return await bcrypt.compare(passwordInput, passwordDB);
      } else {
        // Contraseñas legacy (texto plano)
        return passwordInput === passwordDB;
      }
    } catch (error) {
      console.error("Error verificando contraseña:", error);
      return false;
    }
  }

  /**
   * Hashea una contraseña
   * @param {string} password - Contraseña a hashear
   */
  async hashearPassword(password) {
    const saltRounds = 10;
    return await bcrypt.hash(password, saltRounds);
  }

  /**
   * Convierte UsuarioDTO a UsuarioBean (equivalente al mapeo en Java)
   * @param {Object} usuarioDTO - DTO del usuario
   */
  convertirDTOaBean(usuarioDTO) {
    return {
      datos: {
        idusuario: usuarioDTO.idusuario,
        username: usuarioDTO.username,
        nombre: usuarioDTO.nombre,
        apellido: usuarioDTO.apellido,
        nombreCompleto: `${usuarioDTO.nombre} ${usuarioDTO.apellido}`,
        vigente: usuarioDTO.vigente,
        activoestatus: usuarioDTO.activoestatus,
        correo1: usuarioDTO.correo1,
        correo2: usuarioDTO.correo2,
        responsable: usuarioDTO.responsable,
        enviocorreoauto: usuarioDTO.enviocorreoauto,
        superusuario: usuarioDTO.superusuario,
      },
      permisos: [],
      permisoActual: null,
    };
  }

  /**
   * Carga los permisos del usuario
   * Equivalente a cargaPermisosUsuarios(UsuarioBean usuarioBean)
   * @param {Object} usuarioBean - Bean del usuario
   */
  async cargaPermisosUsuarios(usuarioBean) {
    try {
      console.log(
        `[AdministraUsuariosAreas] Cargando permisos para usuario: ${usuarioBean.datos.username}`
      );

      // Intentar cargar permisos reales de la base de datos
      try {
        const permisos = await permisoDAO.permisosUsuario(
          usuarioBean.datos.idusuario
        );

        if (permisos && permisos.length > 0) {
          // Mapear permisos de BD al formato del Bean
          usuarioBean.permisos = permisos.map((p) => ({
            idpermiso: p.idpermiso,
            idarea: p.idarea,
            rol: this.mapearRolBD(p.rol),
            rolOriginal: p.rol,
            descripcion: this.obtenerDescripcionRol(p.rol),
            activo: true,
          }));

          // El permiso actual es el de mayor jerarquía
          const rolPrincipal = await permisoDAO.getRolPrincipalUsuario(
            usuarioBean.datos.idusuario
          );
          usuarioBean.permisoActual =
            usuarioBean.permisos.find((p) => p.rolOriginal === rolPrincipal) ||
            usuarioBean.permisos[0];

          console.log(
            `[AdministraUsuariosAreas] Permisos cargados de BD. Rol principal: ${rolPrincipal}`
          );
          return;
        }
      } catch (permisoError) {
        console.warn(
          `[AdministraUsuariosAreas] Error cargando permisos de BD:`,
          permisoError.message
        );
      }

      // Fallback: Asignar rol básico basado en superusuario
      let rol = "usuario";
      if (usuarioBean.datos.superusuario) {
        rol = "admin";
      }

      const permiso = {
        rol: rol,
        descripcion: rol === "admin" ? "Administrador" : "Usuario",
        activo: true,
      };

      usuarioBean.permisos = [permiso];
      usuarioBean.permisoActual = permiso;

      console.log(
        `[AdministraUsuariosAreas] Usando permisos por defecto. Rol: ${rol}`
      );
    } catch (error) {
      console.error("Error cargando permisos:", error);
      throw error;
    }
  }

  /**
   * Mapea el rol de la BD al formato interno
   * @param {string} rolBD - Rol de la base de datos ('A', 'E', 'C', 'U')
   */
  mapearRolBD(rolBD) {
    const mapeoRoles = {
      A: "admin", // Administrador
      E: "ejecutivo", // Ejecutivo
      C: "consulta", // Consulta
      U: "usuario", // Usuario
    };

    return mapeoRoles[rolBD] || "usuario";
  }

  /**
   * Obtiene la descripción amigable del rol
   * @param {string} rolBD - Rol de la base de datos
   */
  obtenerDescripcionRol(rolBD) {
    const descripciones = {
      A: "Administrador",
      E: "Ejecutivo",
      C: "Consulta",
      U: "Usuario",
    };

    return descripciones[rolBD] || "Usuario";
  }

  /**
   * Genera token JWT para el usuario autenticado
   * @param {Object} usuarioBean - Bean del usuario
   */
  generarToken(usuarioBean) {
    const jwtConfig = datosGlobales.getJWTConfig();

    const payload = {
      idusuario: usuarioBean.datos.idusuario,
      username: usuarioBean.datos.username,
      nombre: usuarioBean.datos.nombreCompleto,
      role: usuarioBean.permisoActual.rol,
      superusuario: usuarioBean.datos.superusuario,
    };

    return jwt.sign(payload, jwtConfig.secret, {
      expiresIn: jwtConfig.expiresIn,
    });
  }

  /**
   * Valida token JWT
   * @param {string} token - Token a validar
   */
  validarToken(token) {
    try {
      const jwtConfig = datosGlobales.getJWTConfig();
      return jwt.verify(token, jwtConfig.secret);
    } catch (error) {
      console.error("Error validando token:", error);
      return null;
    }
  }

  /**
   * Obtiene usuarios administradores
   */
  async obtenUsuariosAdministradores() {
    try {
      return await usuarioDAO.obtenUsuariosAdministradores();
    } catch (error) {
      console.error("Error obteniendo administradores:", error);
      throw error;
    }
  }

  /**
   * Busca usuario por ID
   * @param {number} idusuario - ID del usuario
   */
  async buscaUsuarioPorId(idusuario) {
    try {
      const usuarioDTO = await usuarioDAO.buscaUsuarioPorId(idusuario);
      if (usuarioDTO) {
        return this.convertirDTOaBean(usuarioDTO);
      }
      return null;
    } catch (error) {
      console.error("Error buscando usuario por ID:", error);
      throw error;
    }
  }
}

export default new AdministraUsuariosAreas();
