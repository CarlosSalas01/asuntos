// Este archivo se encarga de manejar las operaciones relacionadas con los roles y
// permisos de los usuarios.

import administraUsuariosAreas from "../services/administraUsuariosAreas.js";
import permisoDAO from "../dao/permisoDAO.js";
import { log } from "console";

export const obtenerRolesUsuario = async (req, res) => {
  try {
    const userId = req.user.idusuario; // Del token JWT decodificado

    console.log(`[ROLES] Obteniendo roles para usuario ID: ${userId}`);

    // Obtener información completa del usuario con sus permisos
    const usuarioBean = await administraUsuariosAreas.obtenerUsuarioPorId(
      userId
    );

    if (!usuarioBean) {
      return res.status(404).json({
        success: false,
        message: "Usuario no encontrado",
      });
    }

    // FORZAR recarga de permisos desde BD
    console.log("[ROLES] Forzando recarga de permisos desde BD...");
    await administraUsuariosAreas.cargaPermisosUsuarios(usuarioBean);
    console.log(
      "[ROLES] Permisos recargados. Total:",
      usuarioBean.permisos?.length
    );

    // Retornar todos los permisos del usuario (como ${usuario.permisos} en JSP)
    const permisos = usuarioBean.permisos.map((p) => ({
      idpermiso: p.datos.idpermiso,
      idarea: p.datos.idarea,
      rol: p.datos.rol, // Rol original de BD: 'A', 'R', 'RA', 'C', 'E', 'V'
      rolDescripcion: p.rolDescripcion, // "Responsable", "Administrador", etc.
      descripcion: p.descripcion, // Nombre del área o descripción del permiso
      area: p.areaBean?.datos || null, // Datos completos del área
      activo: p.activo,
      esPermisoActual:
        p.datos.idpermiso === usuarioBean.permisoActual?.datos?.idpermiso,
    }));

    // DEBUG: Verificar qué datos se están enviando
    console.log(
      "[ROLES] Permisos a enviar:",
      JSON.stringify(permisos, null, 2)
    );

    res.json({
      success: true,
      data: {
        usuario: {
          id: usuarioBean.datos.idusuario,
          username: usuarioBean.datos.username,
          nombre: usuarioBean.datos.nombre,
        },
        permisoActual: usuarioBean.permisoActual
          ? {
              idpermiso: usuarioBean.permisoActual.datos.idpermiso,
              idarea: usuarioBean.permisoActual.datos.idarea,
              rol: usuarioBean.permisoActual.datos.rol,
              descripcion: usuarioBean.permisoActual.descripcion,
            }
          : null,
        permisos,
      },
    });
  } catch (error) {
    console.error("[ROLES] Error obteniendo roles:", error);
    res.status(500).json({
      success: false,
      message: "Error obteniendo roles del usuario",
      error: error.message,
    });
  }
};

/**
 * Selecciona un rol/permiso específico para el usuario
 *
 * FLUJO COMPLETO (equivalente a SeleccionRol.java + IniciaSesion.java):
 * 1. Valida que el idPermiso pertenezca al usuario
 * 2. Asigna el permiso actual (usuario.setPermisoActual)
 * 3. Carga áreas de consulta y captura (delegado.obtenAreasConsultas/Captura)
 * 4. Registra en bitácora el cambio de rol
 * 5. Genera nuevo JWT con el rol actualizado
 * 6. Retorna configuración completa de sesión
 */
export const seleccionarRol = async (req, res) => {
  try {
    const { idPermiso } = req.body;
    const userId = req.user.idusuario; // Del token JWT

    console.log(
      `[ROLES] Usuario ${userId} seleccionando permiso: ${idPermiso}`
    );

    // Validar que se proporcione idPermiso
    if (!idPermiso) {
      return res.status(400).json({
        success: false,
        message: "El ID del permiso es requerido",
      });
    }

    // Obtener usuario con sus permisos
    const usuarioBean = await administraUsuariosAreas.obtenerUsuarioPorId(
      userId
    );

    if (!usuarioBean) {
      return res.status(404).json({
        success: false,
        message: "Usuario no encontrado",
      });
    }

    // Validar que el permiso sea válido para este usuario
    const permisoValido = usuarioBean.permisos.find(
      (p) => p.datos.idpermiso === parseInt(idPermiso)
    );

    if (!permisoValido) {
      return res.status(403).json({
        success: false,
        message: "Permiso no válido para este usuario",
      });
    }

    // ========== FLUJO DE IniciaSesion.java ==========

    // 1. Asignar permiso actual (equivalente a delegado.asignaRolPermisos)
    usuarioBean.permisoActual = permisoValido;
    console.log(
      `[ROLES] Permiso actual asignado: ${permisoValido.descripcion} (${permisoValido.datos.rol})`
    );

    // 2. Obtener ID del área seleccionada
    const idAreaSel =
      permisoValido.datos.idarea ||
      (await permisoDAO.idAreaByPermiso(idPermiso));
    console.log(`[ROLES] Área seleccionada: ${idAreaSel}`);

    // 3. Obtener áreas de consulta y captura
    // (En el sistema original: delegado.obtenAreasConsultas / obtenAreasCaptura)
    // Por ahora, se incluyen en el token y se cargarán dinámicamente en frontend
    // TODO: Implementar obtenAreasConsultas si es necesario para permisos granulares

    // 4. Registrar en bitácora
    // (En el sistema original: BitacoraDAO.grabaBitacora)
    console.log(
      `[ROLES] Bitácora: ${usuarioBean.datos.nombre} cambió a rol ${permisoValido.datos.rol} en área ${idAreaSel}`
    );
    // TODO: Implementar registro en tabla de bitácora si existe

    // 5. Generar nuevo token JWT con el rol actualizado
    const token = administraUsuariosAreas.generarToken(usuarioBean);

    // 6. Preparar datos de sesión (equivalente a sesion.setAttribute)
    const sessionData = {
      usuario: {
        id: usuarioBean.datos.idusuario,
        username: usuarioBean.datos.username,
        nombre: usuarioBean.datos.nombre,
        superusuario: usuarioBean.datos.superusuario,
      },
      permisoActual: {
        idpermiso: permisoValido.datos.idpermiso,
        idarea: permisoValido.datos.idarea || idAreaSel,
        rol: permisoValido.datos.rol,
        descripcion: permisoValido.descripcion,
      },
      vistaUsuario: {
        // Equivalente a PermisosAreasUsuario en Java
        esAdmin: permisoValido.datos.rol === "A",
        esResponsable: permisoValido.datos.rol === "R",
        esResponsableAdmin: permisoValido.datos.rol === "RA",
        esEjecutivo: permisoValido.datos.rol === "E",
        esConsulta: permisoValido.datos.rol === "C",
        esConvenios: permisoValido.datos.rol === "V",
        esUsuario: permisoValido.datos.rol === "U",
      },
      idAreaSel: idAreaSel,
    };

    console.log(
      `[ROLES] ✅ Rol seleccionado exitosamente: ${permisoValido.datos.rol}`
    );

    // Respuesta con el nuevo token y configuración de sesión
    res.json({
      success: true,
      message: `Rol seleccionado: ${permisoValido.descripcion}`,
      token,
      user: {
        id: usuarioBean.datos.idusuario,
        username: usuarioBean.datos.username,
        nombre: usuarioBean.datos.nombre,
        role: permisoValido.datos.rol,
        rolDescripcion: permisoValido.rolDescripcion,
        idArea: permisoValido.datos.idarea || idAreaSel,
        areaActual: permisoValido.descripcion,
        nivel: usuarioBean.datos.superusuario ? 1 : 2,
      },
      session: sessionData, // Datos completos de sesión para el frontend
    });
  } catch (error) {
    console.error("[ROLES] Error seleccionando rol:", error);
    res.status(500).json({
      success: false,
      message: "Error al seleccionar el rol",
      error: error.message,
    });
  }
};
