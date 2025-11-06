// Servidor mínimo para debug
import express from "express";

const app = express();
const PORT = 5007;

app.get("/", (req, res) => {
  res.json({ message: "Servidor mínimo funcionando" });
});

app.get("/api/test", (req, res) => {
  res.json({ status: "OK", timestamp: new Date().toISOString() });
});

app.listen(PORT, () => {
  console.log(`🚀 Servidor mínimo corriendo en puerto ${PORT}`);
  console.log(`📍 URL: http://localhost:${PORT}`);
});
