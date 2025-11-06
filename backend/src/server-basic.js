// Servidor ultra mínimo sin imports externos
import http from "http";

const server = http.createServer((req, res) => {
  res.writeHead(200, { "Content-Type": "application/json" });
  res.end(
    JSON.stringify({
      message: "Servidor HTTP básico funcionando",
      url: req.url,
      method: req.method,
    })
  );
});

const PORT = 5008;
server.listen(PORT, () => {
  console.log(`🚀 Servidor HTTP básico corriendo en puerto ${PORT}`);
});

// Mantener el proceso vivo
process.on("SIGINT", () => {
  console.log("Cerrando servidor...");
  server.close(() => {
    process.exit(0);
  });
});
