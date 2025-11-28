import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useTheme } from "../../context/ThemeContext";
import axios from "axios";

const Roles = () => {
  const { isDarkMode } = useTheme();
  const navigate = useNavigate();
  const [roles, setRoles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [usuario, setUsuario] = useState(null);
  const [permisoActual, setPermisoActual] = useState(null);

  useEffect(() => {
    cargarRoles();
  }, []);

  const cargarRoles = async () => {
    try {
      setLoading(true);
      const token = localStorage.getItem("authToken");

      if (!token) {
        navigate("/login");
        return;
      }

      const response = await axios.get("http://localhost:9003/api/roles", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.data.success) {
        setUsuario(response.data.data.usuario);
        setPermisoActual(response.data.data.permisoActual);
        setRoles(response.data.data.permisos);

        // DEBUG: Ver qué datos están llegando
        console.log("Permisos recibidos:", response.data.data.permisos);
        response.data.data.permisos.forEach((p, idx) => {
          console.log(`Permiso ${idx}:`, {
            descripcion: p.descripcion,
            rolDescripcion: p.rolDescripcion,
            area: p.area,
            areaNombre: p.area?.nombre,
          });
        });

        // Si solo tiene un permiso, no necesita seleccionar, mostrar mensaje informativo
        // pero NO redirigir automáticamente para evitar confusión
        if (response.data.data.permisos.length === 1) {
          console.log("Usuario tiene un solo permiso asignado");
        }
      }
    } catch (err) {
      console.error("Error cargando roles:", err);
      setError("Error al cargar los permisos del usuario");

      // Si el error es de autenticación, redirigir al login
      if (err.response?.status === 401) {
        localStorage.removeItem("authToken");
        localStorage.removeItem("userData");
        navigate("/login");
      }
    } finally {
      setLoading(false);
    }
  };

  const seleccionarRol = async (idPermiso) => {
    try {
      const token = localStorage.getItem("authToken");

      const response = await axios.post(
        "http://localhost:9003/api/roles/seleccionar",
        { idPermiso },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.data.success) {
        // Actualizar token JWT con el nuevo rol
        localStorage.setItem("authToken", response.data.token);

        // Actualizar datos completos del usuario
        localStorage.setItem("userData", JSON.stringify(response.data.user));
        localStorage.setItem("userRole", response.data.user.role);

        // Guardar datos de sesión completos (equivalente a sesion.setAttribute en Java)
        if (response.data.session) {
          localStorage.setItem(
            "sessionData",
            JSON.stringify(response.data.session)
          );
          localStorage.setItem("idAreaSel", response.data.session.idAreaSel);
        }

        console.log(
          `✅ Rol actualizado: ${response.data.user.areaActual} (${response.data.user.role})`
        );

        // Redirigir al dashboard (equivalente a inicio.jsp)
        navigate("/");

        // Forzar recarga para que todos los componentes usen el nuevo rol
        window.location.reload();
      }
    } catch (err) {
      console.error("Error seleccionando rol:", err);
      setError(
        err.response?.data?.message || "Error al seleccionar el permiso"
      );
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div
          className={`text-lg ${isDarkMode ? "text-white" : "text-gray-900"}`}
        >
          Cargando permisos...
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <p className="text-red-600 dark:text-red-400 text-lg mb-4">{error}</p>
          <button
            onClick={() => navigate("/login")}
            className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg transition-colors"
          >
            Volver al login
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen flex items-center justify-center p-8">
      <div className="max-w-3xl w-full">
        <div className="text-center mb-8">
          <h1
            className={`text-3xl font-bold mb-4 ${
              isDarkMode ? "text-white" : "text-gray-900"
            }`}
          >
            Sistema de Seguimiento de Asuntos de la UGMA
          </h1>
          {usuario && (
            <p
              className={`text-lg mb-2 ${
                isDarkMode ? "text-gray-300" : "text-gray-700"
              }`}
            >
              Bienvenido(a), <strong>{usuario.nombre}</strong>
            </p>
          )}
          <p
            className={`text-sm ${
              isDarkMode ? "text-gray-400" : "text-gray-600"
            }`}
          >
            {roles.length === 1
              ? "Tiene asignado un permiso en este sistema:"
              : "Tiene asignado más de un permiso en este sistema, seleccione uno de los siguientes:"}
          </p>
        </div>

        <div className="bg-white dark:bg-gray-800 rounded-lg shadow-xl overflow-hidden border border-gray-200 dark:border-gray-700">
          <div className="bg-gradient-to-br from-blue-950 to-teal-950 text-white py-4 px-6">
            <h2 className="text-xl font-semibold text-center">
              Selección de permiso
            </h2>
          </div>

          <div className="divide-y divide-gray-200 dark:divide-gray-700">
            {roles.length === 0 ? (
              <div className="p-8 text-center">
                <p className={isDarkMode ? "text-gray-400" : "text-gray-600"}>
                  No hay permisos asignados
                </p>
              </div>
            ) : (
              roles.map((permiso) => (
                <div
                  key={permiso.idpermiso}
                  className={`p-6 hover:bg-gray-50 dark:hover:bg-gray-700/50 transition-colors cursor-pointer ${
                    permiso.esPermisoActual
                      ? "bg-blue-50 dark:bg-blue-900/20 border-l-4 border-blue-600"
                      : ""
                  }`}
                  onClick={() => seleccionarRol(permiso.idpermiso)}
                >
                  <div className="flex items-center justify-between">
                    <div className="flex-1">
                      <h3
                        className={`text-lg font-semibold mb-1 ${
                          isDarkMode ? "text-white" : "text-gray-900"
                        }`}
                      >
                        {permiso.area?.nombre || permiso.descripcion}
                      </h3>
                      <p
                        className={`text-sm ${
                          isDarkMode ? "text-gray-400" : "text-gray-600"
                        }`}
                      >
                        Rol:{" "}
                        <span className="font-medium">
                          {permiso.rolDescripcion || permiso.rol}
                        </span>
                        {permiso.area?.siglas && (
                          <span className="ml-4">
                            Siglas:{" "}
                            <span className="font-medium">
                              {permiso.area.siglas}
                            </span>
                          </span>
                        )}
                      </p>
                      {permiso.esPermisoActual && (
                        <span className="inline-block mt-2 px-3 py-1 bg-blue-600 text-white text-xs font-semibold rounded-full">
                          Permiso actual
                        </span>
                      )}
                    </div>
                    <div className="ml-4">
                      <svg
                        className={`w-6 h-6 ${
                          isDarkMode ? "text-gray-400" : "text-gray-600"
                        }`}
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                      >
                        <path
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          strokeWidth={2}
                          d="M9 5l7 7-7 7"
                        />
                      </svg>
                    </div>
                  </div>
                </div>
              ))
            )}
          </div>
        </div>

        {/* <div className="mt-6 text-center">
          <button
            onClick={() => navigate("/")}
            className={`text-sm ${
              isDarkMode
                ? "text-gray-400 hover:text-white"
                : "text-gray-600 hover:text-gray-900"
            } transition-colors`}
          >
            Volver al dashboard
          </button>
        </div> */}
      </div>
    </div>
  );
};

export default Roles;
