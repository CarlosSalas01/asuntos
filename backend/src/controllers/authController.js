import administraUsuariosAreas from "../services/administraUsuariosAreas.js";
import datosGlobales from "../config/datosGlobales.js";

// Verificar token JWT - Usa administraUsuariosAreas
const verifyToken = (req, res, next) => {
  const token = req.headers.authorization?.split(" ")[1];

  if (!token) {
    return res.status(401).json({
      success: false,
      message: "Token de acceso requerido",
    });
  }

  try {
    const decoded = administraUsuariosAreas.validarToken(token);
    if (!decoded) {
      return res.status(401).json({
        success: false,
        message: "Token inválido o expirado",
      });
    }
    req.user = decoded;
    next();
  } catch (error) {
    return res.status(401).json({
      success: false,
      message: "Token inválido o expirado",
    });
  }
};

// Login de usuario - Equivalente a Login.java
const login = async (req, res, next) => {
  try {
    const { username, password } = req.body;

    console.log(`🔐 [LOGIN] Intento de login para usuario: ${username}`);

    // Validación básica
    if (!username || !password) {
      return res.status(400).json({
        success: false,
        message: "Username y password son requeridos",
      });
    }

    // Crear objeto DatosLogin (equivalente al Java)
    const datosLogin = {
      usuario: username,
      contrasenia: password,
    };

    // Autenticar usuario usando AdministraUsuariosAreas
    const usuarioBean = await administraUsuariosAreas.buscaUsuario(datosLogin);

    if (!usuarioBean) {
      console.log(
        `❌ [LOGIN] Usuario no encontrado o credenciales inválidas: ${username}`
      );
      return res.status(401).json({
        success: false,
        message: "Credenciales inválidas",
      });
    }

    // Generar token JWT
    const token = administraUsuariosAreas.generarToken(usuarioBean);

    console.log(
      `✅ [LOGIN] Login exitoso para usuario: ${username} (${usuarioBean.permisoActual.rol})`
    );

    // Respuesta exitosa
    res.json({
      success: true,
      message: "Login exitoso",
      token,
      user: {
        id: usuarioBean.datos.idusuario,
        username: usuarioBean.datos.username,
        nombre: usuarioBean.datos.nombreCompleto,
        role: usuarioBean.permisoActual.rol,
        email: usuarioBean.datos.correo1,
        idArea: usuarioBean.permisoActual.idarea || 1,
        areaActual: usuarioBean.permisoActual.descripcion || "Sistema",
        nivel: usuarioBean.datos.superusuario ? 1 : 2,
      },
    });
  } catch (error) {
    console.error("❌ [LOGIN] Error en autenticación:", error);
    next(error);
  }
};

// Logout de usuario
const logout = async (req, res, next) => {
  try {
    console.log(`🚪 [LOGOUT] Usuario ${req.user?.username} cerró sesión`);

    res.json({
      success: true,
      message: "Logout exitoso",
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: "Error en logout",
    });
  }
};

// Obtener perfil del usuario actual
const getProfile = async (req, res, next) => {
  try {
    console.log(
      `👤 [PROFILE] Solicitando perfil para usuario: ${req.user?.username}`
    );

    res.json({
      success: true,
      data: {
        id: req.user.id,
        username: req.user.username,
        role: req.user.role,
        idArea: req.user.idArea,
        areaActual: req.user.areaActual,
      },
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: "Error obteniendo perfil",
    });
  }
};

// Exportar funciones para ES modules
export { login, logout, getProfile, verifyToken };
