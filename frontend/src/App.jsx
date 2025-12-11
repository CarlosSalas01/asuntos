import React, { useState, useEffect } from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
  useLocation,
} from "react-router-dom";

import Header from "./pages/Header/Header.jsx";
import SimpleHeader from "./pages/Header/SimpleHeader.jsx";
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
import Roles from "./pages/Roles/Roles.jsx";

import { ThemeProvider, useTheme } from "./context/ThemeContext";
import "./App.css";

// Componente interno que usa el contexto
function AppContent() {
  const location = useLocation();
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // Rutas donde se usa SimpleHeader en lugar del Header completo
  const rutasConSimpleHeader = ["/roles"];

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

  //Sirve para cerrar sesión
  const handleLogout = () => {
    // Eliminar token y datos del usuario del almacenamiento local
    localStorage.removeItem("authToken");
    // Eliminar datos del usuario y rol
    localStorage.removeItem("userData");
    localStorage.removeItem("userRole");
    // Actualizar estado de autenticación a false
    setIsAuthenticated(false);
    // Limpiar datos del usuario en el estado
    setUser(null);
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-lg">Cargando...</div>
      </div>
    );
  }

  const isSIA = location.pathname === "/sia";
  const isCORREOS = location.pathname === "/correos";
  const isREUINIONES = location.pathname === "/reuniones";
  const isACUERDOS = location.pathname === "/acuerdos";
  const isCOMISIONES = location.pathname === "/comisiones";
  const isCONVENIOS = location.pathname === "/convenios";
  const isCONSULTA = location.pathname === "/consulta-general";

  return (
    <div className="App min-h-screen bg-gradient-to-br from-gray-50 to-blue-50 dark:from-gray-900 dark:to-slate-800 transition-colors duration-300">
      {!isAuthenticated ? (
        <Routes>
          <Route path="/login" element={<Login onLogin={handleLogin} />} />
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      ) : (
        <>
          {rutasConSimpleHeader.includes(location.pathname) ? (
            <SimpleHeader user={user} onLogout={handleLogout} />
          ) : (
            <Header user={user} onLogout={handleLogout} />
          )}
          <main
            className={
              `main-content
               ${isSIA ? " sia-full-width" : ""}` +
              `${isCORREOS ? " correos-full-width" : ""}` +
              `${isREUINIONES ? " reuniones-full-width" : ""}` +
              `${isACUERDOS ? " acuerdos-full-width" : ""}` +
              `${isCOMISIONES ? " comisiones-full-width" : ""}` +
              `${isCONVENIOS ? " convenios-full-width" : ""}` +
              `${isCONSULTA ? " consulta-full-width" : ""}`
            }
          >
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/login" element={<Navigate to="/" replace />} />
              <Route path="/sia" element={<SIA className="sia-full-width" />} />
              <Route
                path="/correos"
                element={<Correos className="correos-full-width" />}
              />
              <Route
                path="/reuniones"
                element={<Reuniones className="reuniones-full-width" />}
              />
              <Route
                path="/acuerdos"
                element={<Acuerdos className="acuerdos-full-width" />}
              />
              <Route
                path="/comisiones"
                element={<Comisiones className="comisiones-full-width" />}
              />
              <Route
                path="/convenios"
                element={<Convenios className="convenios-full-width" />}
              />
              <Route
                path="/consulta-general"
                element={<ConsultaGral className="consulta-full-width" />}
              />

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
              <Route path="/consulta-acuerdos" element={<ConsultaAcuerdos />} />
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
              <Route path="/convenios-captura" element={<ConveniosCaptura />} />
              <Route path="/reuniones-captura" element={<ReunionesCaptura />} />
              <Route path="/roles" element={<Roles />} />
              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </main>
        </>
      )}
    </div>
  );
}

function App() {
  return (
    <ThemeProvider>
      <Router>
        <AppContent />
      </Router>
    </ThemeProvider>
  );
}

export default App;
