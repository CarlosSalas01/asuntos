import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App.jsx";
import "./index.css";

// Suprimir errores de extensiones del navegador
window.addEventListener("error", (e) => {
  if (e.filename && e.filename.includes("extension://")) {
    e.stopImmediatePropagation();
    e.preventDefault();
    return false;
  }
});

window.addEventListener("unhandledrejection", (e) => {
  if (e.reason && e.reason.stack && e.reason.stack.includes("contentScript")) {
    e.stopImmediatePropagation();
    e.preventDefault();
    return false;
  }
});

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
