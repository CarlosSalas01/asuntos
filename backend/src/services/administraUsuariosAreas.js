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
   * Solo incluye propiedades necesarias para el sistema
   *
   * Estructura del usuarioBean:
   * {
   *   datos: {
   *     idusuario: number,
   *     username: string,
   *     nombreCompleto: string,
   *     superusuario: boolean
   *   },
   *   permisos: Array,
   *   permisoActual: {
   *     idarea: number,
   *     rol: string,
   *     descripcion: string
   *   }
   * }
   *
   * @param {Object} usuarioDTO - DTO del usuario
   */
  convertirDTOaBean(usuarioDTO) {
    return {
      datos: {
        idusuario: usuarioDTO.idusuario,
        username: usuarioDTO.username,
        nombre: usuarioDTO.nombre,
        apellido: usuarioDTO.apellido,
        superusuario: usuarioDTO.superusuario,
      },
      permisos: [],
      permisoActual: null,
    };
  }

  /**
   * Carga los permisos del usuario
   * Equivalente a FachadaUsuarioArea.cargaPermisosUsuarios() del Java original
   *
   * FLUJO:
   * 1. PermisoDAO.permisosUsuario(idusuario) → Lista de permisos de la BD
   * 2. Para cada permiso:
   *    - Si tiene idarea > 0 → AreaDAO.getArea(idarea) → Cargar datos del área
   *    - Construir descripción completa: rol + área
   * 3. usuarioBean.setPermisos(listaCompleta)
   * 4. Asignar permisoActual (último permiso como temporal)
   *
   * @param {Object} usuarioBean - Bean del usuario
   */
  async cargaPermisosUsuarios(usuarioBean) {
    try {
      console.log(
        `[AdministraUsuariosAreas] Cargando permisos para usuario: ${usuarioBean.datos.username}`
      );

      // Intentar cargar permisos reales de la base de datos
      try {
        const permisosDTO = await permisoDAO.permisosUsuario(
          usuarioBean.datos.idusuario
        );

        if (permisosDTO && permisosDTO.length > 0) {
          const AreaDAO = (await import("../dao/AreaDAO.js")).default;
          const areaDAO = new AreaDAO();

          // Construir PermisoBean completo para cada permiso (como en Java)
          const permisosCompletos = [];

          for (const permisoDTO of permisosDTO) {
            const permisoBean = {
              datos: {
                idpermiso: permisoDTO.idpermiso,
                idusuario: permisoDTO.idusuario,
                idarea: permisoDTO.idarea,
                rol: permisoDTO.rol, // Rol original de BD: 'A', 'RA', 'C', 'E', 'V'
              },
              areaBean: null,
              descripcion: "", // Se construirá después
              activo: true,
            };

            // Si tiene área asignada, cargar datos del área
            if (permisoDTO.idarea > 0) {
              console.log(
                `[DEBUG] Cargando área ${permisoDTO.idarea} para permiso ${permisoDTO.idpermiso}`
              );
              const areaDTO = await areaDAO.getArea(permisoDTO.idarea);
              console.log(`[DEBUG] AreaDTO recibido:`, areaDTO);

              if (areaDTO) {
                permisoBean.areaBean = {
                  datos: {
                    idarea: areaDTO.idarea,
                    nombre: areaDTO.nombre,
                    siglas: areaDTO.siglas,
                    nivel: areaDTO.nivel,
                    dependede: areaDTO.dependede,
                    idresponsable: areaDTO.idresponsable,
                    activa: areaDTO.activa,
                  },
                };
                console.log(
                  `[DEBUG] areaBean construido:`,
                  permisoBean.areaBean
                );
              } else {
                console.log(
                  `[DEBUG] No se encontró área con ID ${permisoDTO.idarea}`
                );
              }
            }

            // Construir descripción completa (como en PermisoBean.getDescripcion())
            permisoBean.descripcion = this.construirDescripcionPermiso(
              permisoDTO.rol,
              permisoBean.areaBean?.datos
            );

            // Descripción del rol sin el área (para mostrar por separado en frontend)
            permisoBean.rolDescripcion = this.obtenerDescripcionRol(
              permisoDTO.rol
            );

            permisosCompletos.push(permisoBean);
          }

          usuarioBean.permisos = permisosCompletos;

          // El permiso actual es el último de la lista (como en Java original)
          // Esto es temporal hasta que el usuario seleccione uno en /roles
          if (permisosCompletos.length > 0) {
            usuarioBean.permisoActual =
              permisosCompletos[permisosCompletos.length - 1];
          }

          console.log(
            `[AdministraUsuariosAreas] ✅ ${permisosCompletos.length} permisos cargados de BD`
          );
          console.log(
            `[AdministraUsuariosAreas] Permisos: ${permisosCompletos
              .map(
                (p) =>
                  `${p.datos.rol}@${p.areaBean?.datos?.siglas || "GENERAL"}`
              )
              .join(", ")}`
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
      let rol = "U";
      if (usuarioBean.datos.superusuario) {
        rol = "A";
      }

      const permisoFallback = {
        datos: {
          idpermiso: 0,
          idusuario: usuarioBean.datos.idusuario,
          idarea: 0,
          rol: rol,
        },
        areaBean: null,
        descripcion: this.construirDescripcionPermiso(rol, null),
        activo: true,
      };

      usuarioBean.permisos = [permisoFallback];
      usuarioBean.permisoActual = permisoFallback;

      console.log(
        `[AdministraUsuariosAreas] Usando permiso por defecto. Rol: ${rol}`
      );
    } catch (error) {
      console.error("Error cargando permisos:", error);
      throw error;
    }
  }

  /**
   * Construye la descripción completa del permiso
   * Equivalente a PermisoBean.getDescripcion() del Java original
   *
   * Ejemplos:
   * - 'A' + null → "Administrador"
   * - 'A' + {siglas: 'DGAIIG'} → "Administrador (DGAIIG)"
   * - 'R' + {siglas: 'DGAIIG'} → "Responsable (DGAIIG)"
   * - 'RA' + {siglas: 'DMG'} → "Responsable Administrador (DMG)"
   * - 'C' + {siglas: 'DMG'} → "Consulta (DMG)"
   * - 'V' + null → "Convenios"
   *
   * @param {string} rol - Rol del permiso ('A', 'R', 'RA', 'C', 'E', 'V', 'U')
   * @param {Object} areaData - Datos del área (opcional)
   */
  construirDescripcionPermiso(rol, areaData) {
    let descripcionRol = "";

    // Mapeo de roles según el sistema original
    switch (rol) {
      case "A":
        descripcionRol = "Administrador";
        break;
      case "R":
        descripcionRol = "Responsable";
        break;
      case "RA":
        descripcionRol = "Responsable Administrador";
        break;
      case "C":
        descripcionRol = "Consulta";
        break;
      case "E":
        descripcionRol = "Ejecutivo";
        break;
      case "V":
        descripcionRol = "Convenios";
        break;
      case "U":
        descripcionRol = "Usuario";
        break;
      default:
        descripcionRol = "Usuario";
    }

    // Si tiene área asignada, agregar las siglas
    if (areaData && areaData.siglas) {
      return `${descripcionRol} (${areaData.siglas})`;
    }

    return descripcionRol;
  }

  /**
   * Obtiene la descripción amigable del rol
   * @param {string} rolBD - Rol de la base de datos ('A', 'R', 'RA', 'C', 'E', 'V', 'U')
   */
  obtenerDescripcionRol(rolBD) {
    const descripciones = {
      A: "Administrador",
      R: "Responsable",
      RA: "Responsable Administrador",
      C: "Consulta",
      E: "Ejecutivo",
      V: "Convenios",
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
      role: usuarioBean.permisoActual?.datos?.rol || "U", // Rol original de BD
      idarea: usuarioBean.permisoActual?.datos?.idarea || 0,
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

  /**
   * Obtiene usuario por ID con todos sus permisos cargados
   * Usado para selección de roles
   * @param {number} idusuario - ID del usuario
   */
  async obtenerUsuarioPorId(idusuario) {
    try {
      const usuarioDTO = await usuarioDAO.buscaUsuarioPorId(idusuario);
      if (!usuarioDTO) {
        return null;
      }

      const usuarioBean = this.convertirDTOaBean(usuarioDTO);
      await this.cargaPermisosUsuarios(usuarioBean);

      return usuarioBean;
    } catch (error) {
      console.error("Error obteniendo usuario por ID:", error);
      throw error;
    }
  }
}

export default new AdministraUsuariosAreas();
