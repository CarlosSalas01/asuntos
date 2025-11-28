/**
 * Script de prueba para verificar datos SIA en la base de datos
 * Ejecutar: node test-sia-data.js
 */

import administradorDataSource from "./src/config/administradorDataSource.js";

async function testSiaData() {
  try {
    console.log("🔍 Verificando datos SIA en la base de datos...\n");

    const pool = administradorDataSource.getDataSource();

    // 1. Contar asuntos tipo K
    const countQuery = `
      SELECT COUNT(*) as total 
      FROM controlasuntospendientesnew.asunto 
      WHERE tipoasunto = 'K'
    `;

    const countResult = await pool.query(countQuery);
    console.log("📊 Total de asuntos SIA (tipo K):", countResult.rows[0].total);

    if (parseInt(countResult.rows[0].total) === 0) {
      console.log("\n❌ No hay asuntos tipo 'K' en la base de datos");
      console.log(
        "💡 Verifica que existan registros con tipoasunto='K' en la tabla asunto\n"
      );
      return;
    }

    // 2. Obtener algunos asuntos de muestra
    const sampleQuery = `
      SELECT 
        idasunto,
        idconsecutivo,
        nocontrol,
        LEFT(descripcion, 50) as descripcion,
        estatustexto,
        fechaingreso,
        fechaatender,
        estatus
      FROM controlasuntospendientesnew.asunto 
      WHERE tipoasunto = 'K'
      ORDER BY idconsecutivo DESC
      LIMIT 5
    `;

    const sampleResult = await pool.query(sampleQuery);
    console.log("\n📋 Muestra de asuntos SIA:");
    console.table(sampleResult.rows);

    // 3. Verificar responsables
    const firstAsunto = sampleResult.rows[0];
    if (firstAsunto) {
      const respQuery = `
        SELECT 
          r.idresponsable,
          r.idasunto,
          r.idarea,
          r.estatus,
          r.avance,
          a.nombre as area_nombre
        FROM controlasuntospendientesnew.responsable r
        LEFT JOIN controlasuntospendientesnew.area a ON r.idarea = a.idarea
        WHERE r.idasunto = $1
        AND r.estatus <> 'C'
      `;

      const respResult = await pool.query(respQuery, [firstAsunto.idasunto]);
      console.log(`\n👥 Responsables del asunto ${firstAsunto.idconsecutivo}:`);
      console.table(respResult.rows);
    }

    // 4. Verificar áreas disponibles
    const areasQuery = `
      SELECT idarea, nombre, siglas, nivel
      FROM controlasuntospendientesnew.area
      WHERE nivel >= 2
      ORDER BY nivel, nombre
      LIMIT 10
    `;

    const areasResult = await pool.query(areasQuery);
    console.log("\n🏢 Áreas disponibles (primeras 10):");
    console.table(areasResult.rows);

    console.log("\n✅ Verificación completada");
  } catch (error) {
    console.error("❌ Error en la verificación:", error);
  } finally {
    process.exit(0);
  }
}

testSiaData();
