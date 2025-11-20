import React, { useState, useEffect } from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";

import Header from "./pages/Header/Header.jsx";
import Home from "./components/dashboard/Home";
import Asuntos from "./pages/Asuntos";
import Login from "./pages/Login/Login.jsx";
import SIA from "./pages/SIA";
import Reuniones from "./pages/Reuniones";
import Correos from "./pages/Correos";
import Comisiones from "./pages/Comisiones";
import Convenios from "./pages/Convenios";
import ConsultaGral from "./pages/ConsultaGral";
import ConsultaAsuntosGeneral from "./pages/ConsultaAsuntosGeneral";
import ConsultaSIA from "./pages/ConsultaSIA";
import ConsultaCorreos from "./pages/ConsultaCorreos";
import ConsultaComisiones from "./pages/ConsultaComisiones";
import ConsultaReuniones from "./pages/ConsultaReuniones";
import ConsultaAcuerdos from "./pages/ConsultaAcuerdos";
import Reportes from "./pages/Reportes";
import Acuerdos from "./pages/Acuerdos";
import AsuntosSIA from "./pages/Captura/AsuntosSIA.jsx";
import CorreosCaptura from "./pages/Captura/CorreosCaptura.jsx";
import ConveniosCaptura from "./pages/Captura/ConveniosCaptura.jsx";
import ReunionesCaptura from "./pages/Captura/ReunionesCaptura.jsx";
import AtenYPend from "./pages/Reportes/AtenYPend.jsx";
import Diario from "./pages/Reportes/Diario.jsx";
import Reasignacion from "./pages/Reportes/Reasignacion.jsx";
import ResponsReportes from "./pages/Reportes/ResponsReportes.jsx";
import SemYMen from "./pages/Reportes/SemYMen.jsx";

import { ThemeProvider, useTheme } from "./context/ThemeContext";
import "./App.css";

// Componente interno que usa el contexto
function AppContent() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Verificar si hay un token guardado al cargar la aplicación
    const token = localStorage.getItem("authToken");
    const userData = localStorage.getItem("userData");

    if (token && userData) {
      setIsAuthenticated(true);
      setUser(JSON.parse(userData));
    }
    setLoading(false);
  }, []);

  const handleLogin = (username, role, userData) => {
    setIsAuthenticated(true);
    setUser(userData);
  };

  const handleLogout = () => {
    localStorage.removeItem("authToken");
    localStorage.removeItem("userData");
    localStorage.removeItem("userRole");
    setIsAuthenticated(false);
    setUser(null);
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-lg">Cargando...</div>
      </div>
    );
  }

  return (
    <Router>
      <div className="App min-h-screen bg-gradient-to-br from-gray-50 to-blue-50 dark:from-gray-900 dark:to-slate-800 transition-colors duration-300">
        {!isAuthenticated ? (
          <Routes>
            <Route path="/login" element={<Login onLogin={handleLogin} />} />
            <Route path="*" element={<Navigate to="/login" replace />} />
          </Routes>
        ) : (
          <>
            <Header user={user} onLogout={handleLogout} />
            <main className="main-content">
              <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Navigate to="/" replace />} />
                <Route path="/sia" element={<SIA />} />
                <Route path="/reuniones" element={<Reuniones />} />
                <Route path="/correos" element={<Correos />} />
                <Route path="/comisiones" element={<Comisiones />} />
                <Route path="/convenios" element={<Convenios />} />
                <Route path="/acuerdos" element={<Acuerdos />} />
                <Route path="/consulta-general" element={<ConsultaGral />} />

                {/* Nueva consulta general con TablaResultados */}
                <Route
                  path="/consulta-asuntos-general"
                  element={<ConsultaAsuntosGeneral />}
                />

                {/* Rutas específicas de navegación desde TablaResultados */}
                <Route path="/consulta-sia" element={<ConsultaSIA />} />
                <Route path="/consulta-correos" element={<ConsultaCorreos />} />
                <Route
                  path="/consulta-comisiones"
                  element={<ConsultaComisiones />}
                />
                <Route
                  path="/consulta-reuniones"
                  element={<ConsultaReuniones />}
                />
                <Route
                  path="/consulta-acuerdos"
                  element={<ConsultaAcuerdos />}
                />

                <Route path="/reportes" element={<Reportes />} />
                <Route path="/asuntos" element={<Asuntos />} />
                <Route path="/asuntos-sia" element={<AsuntosSIA />} />
                <Route path="/correos-captura" element={<CorreosCaptura />} />
                <Route path="/atendidos-y-pendientes" element={<AtenYPend />} />
                <Route path="/diario" element={<Diario />} />
                <Route path="/reasignacion" element={<Reasignacion />} />
                <Route
                  path="/responsabilidad-reportes"
                  element={<ResponsReportes />}
                />
                <Route path="/semanal-mensual" element={<SemYMen />} />
                <Route
                  path="/convenios-captura"
                  element={<ConveniosCaptura />}
                />
                <Route
                  path="/reuniones-captura"
                  element={<ReunionesCaptura />}
                />
                <Route path="*" element={<Navigate to="/" replace />} />
              </Routes>
            </main>
          </>
        )}
      </div>
    </Router>
  );
}

function App() {
  return (
    <ThemeProvider>
      <AppContent />
    </ThemeProvider>
  );
}

export default App;
