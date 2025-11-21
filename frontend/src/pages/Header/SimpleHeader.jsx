import { useState } from "react";
import { Link } from "react-router-dom";
import modalService from "../../services/modalService.js";
import {
  InegiLogo,
  SunIcon,
  MoonIcon,
} from "../../components/icons/CustomIcons.jsx";
import { useTheme } from "../../context/ThemeContext.jsx";

function SimpleHeader({ user, onLogout }) {
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const { isDarkMode, toggleDarkMode } = useTheme();

  // Manejar logout con modal de confirmación
  const handleLogout = async () => {
    try {
      const result = await modalService.showConfirmation(
        "¿Cerrar sesión?",
        "¿Estás seguro de que quieres cerrar tu sesión?",
        "Sí, cerrar sesión",
        "Cancelar"
      );

      if (result.isConfirmed) {
        // Logout
        onLogout();

        modalService.showSuccess(
          "¡Sesión cerrada!",
          "Has cerrado sesión exitosamente",
          1500
        );
      }
    } catch (error) {
      console.error("Error en logout:", error);
      onLogout();
    }
  };

  return (
    <header className="bg-gradient-to-br from-blue-950 to-teal-950 text-white py-4 shadow-md">
      <div className="px-5 flex justify-between items-center relative">
        {/* Logo - Lado izquierdo */}
        <div className="py-3">
          <h1 className="text-xl font-bold">
            <Link to="/" className="text-white">
              <InegiLogo className="h-9 w-auto filter brightness-0 invert" />
            </Link>
          </h1>
        </div>

        {/* Controles del usuario - Lado derecho */}
        <div className="py-3 flex items-center gap-3">
          {/* Toggle de modo oscuro */}
          <button
            onClick={toggleDarkMode}
            className="relative inline-flex items-center justify-center w-10 h-10 rounded-full bg-white/10 hover:bg-white/20 transition-colors duration-200 focus:outline-none focus:ring-2 focus:ring-white/30"
            title={
              isDarkMode ? "Cambiar a modo claro" : "Cambiar a modo oscuro"
            }
          >
            <div className="relative w-5 h-5">
              <SunIcon
                className={`absolute inset-0 w-5 h-5 text-yellow-300 transition-all duration-300 transform ${
                  isDarkMode
                    ? "opacity-0 rotate-90 scale-75"
                    : "opacity-100 rotate-0 scale-100"
                }`}
              />
              <MoonIcon
                className={`absolute inset-0 w-5 h-5 text-blue-200 transition-all duration-300 transform ${
                  isDarkMode
                    ? "opacity-100 rotate-0 scale-100"
                    : "opacity-0 -rotate-90 scale-75"
                }`}
              />
            </div>
          </button>

          {/* User Dropdown */}
          <div className="relative inline-block">
            <button
              onClick={() => setIsDropdownOpen(!isDropdownOpen)}
              className="flex items-center gap-2 bg-white/5 hover:bg-white/20 border-0 rounded-md py-2 px-3 text-white cursor-pointer transition-colors"
            >
              <div className="w-8 h-8 bg-blue-900 rounded-full flex items-center justify-center text-sm font-semibold text-white aspect-square overflow-hidden border-2 border-white/20">
                {user?.nombre?.charAt(0).toUpperCase() || "U"}
              </div>
              <span className="text-sm text-gray-200">
                {user?.rolDescripcion || user?.role || "Usuario"}
              </span>
              <svg
                className={`w-4 h-4 transition-transform duration-200 ${
                  isDropdownOpen ? "rotate-180" : ""
                }`}
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M19 9l-7 7-7-7"
                />
              </svg>
            </button>
          </div>
        </div>

        {/* Dropdown Menu */}
        {isDropdownOpen && (
          <>
            <div className="absolute top-full right-5 mt-2 w-60 bg-white dark:bg-gray-800 rounded-lg shadow-xl border border-gray-200 dark:border-gray-700 z-50 overflow-hidden transition-colors duration-300">
              <div className="flex items-center gap-3 p-4">
                <div className="w-12 h-12 bg-blue-900 rounded-full flex items-center justify-center text-base font-semibold text-white aspect-square overflow-hidden border-2 border-gray-500 flex-shrink-0">
                  {user?.nombre?.charAt(0).toUpperCase() || "U"}
                </div>
                <div>
                  <div className="text-sm font-semibold text-gray-900 dark:text-white">
                    {user?.nombre || "Usuario"}
                  </div>
                  <div className="text-xs text-gray-500 dark:text-gray-400">
                    {user?.role === "admin" ? "Administrador" : "Usuario"}
                  </div>
                </div>
              </div>

              <hr className="border-t border-gray-200 dark:border-gray-600 m-0" />

              <button
                onClick={handleLogout}
                className="flex items-center justify-center gap-3 w-full py-3 px-4 border-0 bg-transparent text-red-600 dark:text-red-400 text-sm cursor-pointer hover:bg-red-50 dark:hover:bg-red-900/20 transition-colors"
              >
                <svg
                  className="w-4 h-4"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"
                  />
                </svg>
                Cerrar sesión
              </button>
            </div>

            {/* Overlay */}
            <div
              className="fixed inset-0 z-40"
              onClick={() => setIsDropdownOpen(false)}
            ></div>
          </>
        )}
      </div>
    </header>
  );
}

export default SimpleHeader;
