/**
 * Script para probar endpoints del dashboard
 */

import fetch from "node-fetch";

async function testDashboard() {
  try {
    console.log("🧪 Probando endpoint /api/resumen-inicio...");

    const response = await fetch("http://localhost:5004/api/resumen-inicio");

    console.log("📊 Status:", response.status);
    console.log("📋 Headers:", Object.fromEntries(response.headers));

    if (response.ok) {
      const data = await response.json();
      console.log("✅ Respuesta exitosa:");
      console.log(JSON.stringify(data, null, 2));
    } else {
      const errorText = await response.text();
      console.log("❌ Error en respuesta:", errorText);
    }
  } catch (error) {
    console.error("💥 Error en petición:", error.message);
  }
}

testDashboard();
