/**
 * Controlador para pruebas de sistema
 * Incluye endpoints para verificar conexiones y estado del sistema
 */

import administradorDataSource from "../config/administradorDataSource.js";
import datosGlobales from "../config/datosGlobales.js";

/**
 * Endpoint para verificar el estado del sistema
 */
const healthCheck = async (req, res) => {
  try {
    const dbConfig = datosGlobales.getDatabaseConfig();

    res.json({
      success: true,
      message: "Sistema funcionando correctamente",
      timestamp: new Date().toISOString(),
      version: datosGlobales.VERSION,
      environment: datosGlobales.ESTADO_PRODUCCION
        ? "production"
        : "development",
      database: {
        host: dbConfig.host,
        port: dbConfig.port,
        database: dbConfig.database,
        user: dbConfig.user,
      },
      urls: {
        sistema: datosGlobales.getRutaSistema(),
        archivos: datosGlobales.getRuta(),
      },
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: "Error en health check",
      error: error.message,
    });
  }
};

/**
 * Endpoint para probar la conexión a la base de datos
 */
const testDatabase = async (req, res) => {
  try {
    // Probar conexión básica
    const pool = administradorDataSource.getDataSource();
    const client = await pool.connect();

    try {
      // Test básico
      const basicTest = await client.query(
        "SELECT 1 as test_connection, NOW() as timestamp"
      );

      // Test de schema
      let schemaTest = null;
      try {
        schemaTest = await client.query(
          "SELECT COUNT(*) as total_usuarios FROM controlasuntospendientesnew.usuario"
        );
      } catch (schemaError) {
        console.log("[TEST-DB] ⚠️  Schema no accesible:", schemaError.message);
      }

      // Test de usuario específico
      let userTest = null;
      if (schemaTest) {
        try {
          userTest = await client.query(
            "SELECT username, nombre, apellido FROM controlasuntospendientesnew.usuario WHERE vigente = 'S' LIMIT 3"
          );
        } catch (userError) {
          console.log(
            "[TEST-DB] ⚠️  Error consultando usuarios:",
            userError.message
          );
        }
      }

      client.release();

      res.json({
        success: true,
        message: "Prueba de base de datos completada",
        results: {
          basicConnection: {
            success: true,
            data: basicTest.rows[0],
          },
          schemaAccess: {
            success: schemaTest !== null,
            totalUsers: schemaTest ? schemaTest.rows[0].total_usuarios : null,
            error: schemaTest === null ? "Schema no accesible" : null,
          },
          userQuery: {
            success: userTest !== null,
            usersFound: userTest ? userTest.rows.length : 0,
            sampleUsers: userTest ? userTest.rows : null,
          },
        },
      });
    } catch (queryError) {
      client.release();
      throw queryError;
    }
  } catch (error) {
    console.error("[TEST-DB] ❌ Error en prueba de base de datos:", error);

    res.status(500).json({
      success: false,
      message: "Error probando conexión a base de datos",
      error: error.message,
      details: {
        code: error.code,
        host: datosGlobales.getDatabaseConfig().host,
        port: datosGlobales.getDatabaseConfig().port,
        database: datosGlobales.getDatabaseConfig().database,
      },
    });
  }
};

/**
 * Endpoint para obtener estadísticas del pool de conexiones
 */
const getPoolStats = async (req, res) => {
  try {
    const stats = administradorDataSource.getPoolStats();

    res.json({
      success: true,
      message: "Estadísticas del pool de conexiones",
      stats: stats || "Pool no inicializado",
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: "Error obteniendo estadísticas del pool",
      error: error.message,
    });
  }
};

/**
 * Endpoint para listar usuarios disponibles para login
 */
const listAvailableUsers = async (req, res) => {
  try {
    console.log("[LIST-USERS] Consultando usuarios disponibles...");

    const sql = `
      SELECT idusuario, username, nombre, apellido, correo1, correo2, 
             responsable, superusuario, vigente, activoestatus
      FROM controlasuntospendientesnew.usuario 
      WHERE vigente = 'S' 
      ORDER BY nombre, apellido
    `;

    const result = await administradorDataSource.executeQuery(sql);

    console.log(
      `[LIST-USERS] ✅ Encontrados ${result.rows.length} usuarios vigentes`
    );

    // Formatear usuarios para mostrar información útil
    const usersFormatted = result.rows.map((user) => ({
      id: user.idusuario,
      username: user.username,
      nombre: `${user.nombre} ${user.apellido}`.trim(),
      correo: user.correo1 || user.correo2 || "Sin correo",
      esResponsable: user.responsable === "S",
      esSuperUsuario: user.superusuario === true || user.superusuario === "S",
      estatus: user.activoestatus,
    }));

    res.json({
      success: true,
      message: `${result.rows.length} usuarios disponibles para login (TODOS los usuarios vigentes)`,
      totalUsers: result.rows.length,
      users: usersFormatted,
      instructions: {
        login: "Usa el campo 'username' para hacer login",
        password: "🔓 MODO DESARROLLO: Puedes usar cualquier contraseña",
        note: "Solo se muestran usuarios vigentes (vigente = 'S'). En producción se validarán contraseñas reales.",
        environment: "DESARROLLO - Sin validación de contraseña",
      },
    });
  } catch (error) {
    console.error("[LIST-USERS] ❌ Error listando usuarios:", error);

    res.status(500).json({
      success: false,
      message: "Error consultando usuarios disponibles",
      error: error.message,
    });
  }
};

export { healthCheck, testDatabase, getPoolStats, listAvailableUsers };
