/**
 * Administrador de DataSource - Equivalente a AdministradorDataSource.java
 * Maneja la conexión a PostgreSQL con pool de conexiones
 */

import { Pool } from "pg";
import datosGlobales from "./datosGlobales.js";

class AdministradorDataSource {
  constructor() {
    this.pool = null;
  }

  /**
   * Obtiene el pool de conexiones a la base de datos
   * Equivalente al método getDataSource() de Java
   */
  getDataSource() {
    if (!this.pool) {
      const dbConfig = datosGlobales.getDatabaseConfig();

      this.pool = new Pool({
        host: dbConfig.host,
        port: dbConfig.port,
        database: dbConfig.database,
        user: dbConfig.user,
        password: dbConfig.password,
        ssl: dbConfig.ssl,
        // Configuración del pool equivalente a BasicDataSource
        max: dbConfig.max, // MaxTotal
        min: dbConfig.min, // MinIdle
        idleTimeoutMillis: dbConfig.idle, // TimeBetweenEvictionRunsMillis
        acquireTimeoutMillis: dbConfig.acquire, // MaxWaitMillis
        createTimeoutMillis: 8000,
        destroyTimeoutMillis: 5000,
        reapIntervalMillis: 1000,
        createRetryIntervalMillis: 200,
        // Configuración de validación (equivalente a validationQuery)
        keepAlive: true,
        keepAliveInitialDelayMillis: 10000,
      });

      // Manejo de eventos del pool
      this.pool.on("connect", (client) => {
        console.log("Nueva conexión establecida a PostgreSQL");
      });

      this.pool.on("error", (err, client) => {
        console.error("Error en el pool de conexiones PostgreSQL:", err);
      });

      this.pool.on("acquire", (client) => {
        console.log("Conexión adquirida del pool");
      });

      this.pool.on("remove", (client) => {
        console.log("Conexión removida del pool");
      });

      // Test de conectividad inicial
      this.testConnection();
    }

    return this.pool;
  }

  /**
   * Prueba la conectividad con la base de datos
   * Equivalente a la validationQuery "select 1 as uno;"
   */
  async testConnection() {
    try {
      // Asegurar que el pool esté inicializado
      if (!this.pool) {
        this.getDataSource();
      }

      const client = await this.pool.connect();
      const result = await client.query("SELECT 1 as uno");
      console.log("✅ Conexión a PostgreSQL establecida correctamente");
      console.log(
        "📊 Base de datos:",
        datosGlobales.getDatabaseConfig().database
      );
      console.log(
        "🏠 Host:",
        datosGlobales.getDatabaseConfig().host +
          ":" +
          datosGlobales.getDatabaseConfig().port
      );

      // Probar también que podemos acceder al schema controlasuntospendientesnew
      try {
        const testSchema = await client.query(
          "SELECT COUNT(*) FROM controlasuntospendientesnew.usuario LIMIT 1"
        );
        console.log(
          "✅ Schema controlasuntospendientesnew accesible - Usuarios encontrados:",
          testSchema.rows[0].count
        );
      } catch (schemaError) {
        console.warn(
          "⚠️  Advertencia: No se pudo acceder al schema controlasuntospendientesnew:",
          schemaError.message
        );
        console.warn(
          "🔍 Esto puede ser normal si la base de datos no está disponible aún. Usando usuarios de prueba."
        );
      }

      client.release();
      return true;
    } catch (error) {
      console.error("❌ Error al conectar con PostgreSQL:", error.message);
      console.error("🔍 Configuración de conexión:", {
        host: datosGlobales.getDatabaseConfig().host,
        port: datosGlobales.getDatabaseConfig().port,
        database: datosGlobales.getDatabaseConfig().database,
        user: datosGlobales.getDatabaseConfig().user,
      });
      console.log("🔄 Continuando con usuarios de prueba...");
      return false;
    }
  }

  /**
   * Ejecuta una consulta SQL
   * @param {string} query - Consulta SQL
   * @param {array} params - Parámetros de la consulta
   */
  async executeQuery(query, params = []) {
    // Asegurar que el pool esté inicializado
    if (!this.pool) {
      this.getDataSource();
    }

    const client = await this.pool.connect();
    try {
      const result = await client.query(query, params);
      return result;
    } catch (error) {
      console.error("Error ejecutando consulta:", error);
      throw error;
    } finally {
      client.release();
    }
  }

  /**
   * Cierra el pool de conexiones
   */
  async close() {
    if (this.pool) {
      await this.pool.end();
      console.log("Pool de conexiones cerrado");
    }
  }

  /**
   * Obtiene estadísticas del pool
   */
  getPoolStats() {
    if (this.pool) {
      return {
        totalCount: this.pool.totalCount,
        idleCount: this.pool.idleCount,
        waitingCount: this.pool.waitingCount,
      };
    }
    return null;
  }
}

// Instancia singleton
const administradorDataSource = new AdministradorDataSource();

export default administradorDataSource;
