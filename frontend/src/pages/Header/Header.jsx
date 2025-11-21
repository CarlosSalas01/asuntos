import { useState, useEffect, useRef } from "react";
import { Link } from "react-router-dom";
import modalService from "../../services/modalService.js";
import {
  InegiLogo,
  SunIcon,
  MoonIcon,
  ReunionesIcon,
  AsuntosIcon,
  CorreosIcon,
  ConveniosIcon,
  UsersIcon,
  ListaIcon,
  ScheduleIcon,
  ReasignacionIcon,
  PendientesIcon,
} from "../../components/icons/CustomIcons.jsx";
import { useTheme } from "../../context/ThemeContext.jsx";

function Header({ user, onLogout }) {
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const [isCapturaMenuOpen, setIsCapturaMenuOpen] = useState(false);
  const [isReportesMenuOpen, setIsReportesMenuOpen] = useState(false);
  const [dropdownPosition, setDropdownPosition] = useState("right-0");
  const reportesButtonRef = useRef(null);
  const reportesDropdownRef = useRef(null);
  const { isDarkMode, toggleDarkMode } = useTheme();

  // Función para calcular la posición del dropdown
  useEffect(() => {
    if (
      isReportesMenuOpen &&
      reportesButtonRef.current &&
      reportesDropdownRef.current
    ) {
      const buttonRect = reportesButtonRef.current.getBoundingClientRect();
      const dropdownWidth = 224; // w-56 = 14rem = 224px
      const viewportWidth = window.innerWidth;
      const spaceOnRight = viewportWidth - buttonRect.right;
      const spaceOnLeft = buttonRect.left;

      // Si no hay suficiente espacio a la derecha pero sí a la izquierda
      if (spaceOnRight < dropdownWidth && spaceOnLeft >= dropdownWidth) {
        setDropdownPosition("right-0"); // Alinear a la derecha del botón
      }
      // Si no hay espacio en ningún lado, centrar
      else if (spaceOnRight < dropdownWidth && spaceOnLeft < dropdownWidth) {
        setDropdownPosition("left-1/2 transform -translate-x-1/2");
      }
      // Por defecto, alinear a la izquierda
      else {
        setDropdownPosition("left-0");
      }
    }
  }, [isReportesMenuOpen]);

  // Listener para recalcular posición al redimensionar ventana
  useEffect(() => {
    const handleResize = () => {
      if (isReportesMenuOpen) {
        // Forzar recálculo de posición
        setIsReportesMenuOpen(false);
        setTimeout(() => setIsReportesMenuOpen(true), 10);
      }
    };

    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, [isReportesMenuOpen]);

  // const handleLogout = () => {
  //   setIsDropdownOpen(false);
  //   onLogout();
  // };

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

        {/* Dropdown Menu - Posicionado al final del div contenedor */}
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
                    {user?.rolDescripcion || user?.role || "Usuario"}
                  </div>
                </div>
              </div>

              <hr className="border-t border-gray-200 dark:border-gray-600 m-0" />

              {/* Mostrar "Cambiar rol" solo si tiene múltiples permisos */}
              {localStorage.getItem("totalPermisos") > 1 && (
                <>
                  <Link to="/roles">
                    <button className="flex items-center justify-center gap-3 w-full py-3 px-4 border-1 bg-transparent text-gray-600 dark:text-gray-400 text-sm cursor-pointer hover:bg-gray-50 dark:hover:bg-gray-950/25 transition-colors">
                      Cambiar rol
                    </button>
                  </Link>
                  <hr className="border-t border-gray-200 dark:border-gray-600 m-0" />
                </>
              )}

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
      <hr className="border-white/30 py-2" />
      <div className="px-5 flex justify-end">
        <nav>
          <ul className="flex list-none gap-8 m-0 p-0">
            <li className="relative">
              <button
                onClick={() => setIsCapturaMenuOpen(!isCapturaMenuOpen)}
                className="flex items-center gap-1 text-white py-2 px-4 rounded hover:bg-white/20 transition-colors focus:outline-none focus:ring-2 focus:ring-white/30"
              >
                Captura
                <svg
                  className={`w-4 h-4 transition-transform duration-200 ${
                    isCapturaMenuOpen ? "rotate-180" : ""
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
              {isCapturaMenuOpen && (
                <>
                  <div className="absolute top-full left-0 mt-2 w-56 bg-white dark:bg-gray-800 rounded-lg shadow-xl border border-gray-200 dark:border-gray-700 z-50 overflow-hidden">
                    <div className="py-2">
                      <Link
                        to="/asuntos-sia"
                        className="flex items-center px-4 py-3 text-gray-700 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors no-underline"
                        onClick={() => setIsCapturaMenuOpen(false)}
                      >
                        <AsuntosIcon className="w-5 h-5 mr-3 text-blue-500" />
                        <div>
                          <div className="font-medium">Asuntos SIA</div>
                        </div>
                      </Link>

                      <Link
                        to="/correos-captura"
                        className="flex items-center px-4 py-3 text-gray-700 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors no-underline"
                        onClick={() => setIsCapturaMenuOpen(false)}
                      >
                        <CorreosIcon className="w-5 h-5 mr-3 text-blue-500" />
                        <div>
                          <div className="font-medium">Correos</div>
                        </div>
                      </Link>

                      <Link
                        to="/reuniones-captura"
                        className="flex items-center px-4 py-3 text-gray-700 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors no-underline"
                        onClick={() => setIsCapturaMenuOpen(false)}
                      >
                        <ReunionesIcon className="w-5 h-5 mr-3 text-blue-500" />
                        <div>
                          <div className="font-medium">Reuniones</div>
                        </div>
                      </Link>
                      <Link
                        to="/convenios-captura"
                        className="flex items-center px-4 py-3 text-gray-700 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors no-underline"
                        onClick={() => setIsCapturaMenuOpen(false)}
                      >
                        <ConveniosIcon className="w-5 h-5 mr-3 text-blue-500" />
                        <div>
                          <div className="font-medium">Convenios</div>
                        </div>
                      </Link>
                    </div>
                  </div>

                  {/* Overlay para cerrar el menú */}
                  <div
                    className="fixed inset-0 z-40"
                    onClick={() => setIsCapturaMenuOpen(false)}
                  ></div>
                </>
              )}
            </li>
            <li className="relative pt-2">
              <Link
                to="/sia"
                className="text-white no-underline py-2 px-4 rounded hover:bg-white/20 transition-colors"
              >
                SIA
              </Link>
            </li>
            <li className="relative pt-2">
              <Link
                to="/correos"
                className="text-white no-underline py-2 px-4 rounded hover:bg-white/20 transition-colors"
              >
                Correos
              </Link>
            </li>
            <li className="relative pt-2">
              <Link
                to="/reuniones"
                className="text-white no-underline py-2 px-4 rounded hover:bg-white/20 transition-colors"
              >
                Reuniones
              </Link>
            </li>
            <li className="relative pt-2">
              <Link
                to="/acuerdos"
                className="text-white no-underline py-2 px-4 rounded hover:bg-white/20 transition-colors"
              >
                Acuerdos
              </Link>
            </li>
            <li className="relative pt-2">
              <Link
                to="/comisiones"
                className="text-white no-underline py-2 px-4 rounded hover:bg-white/20 transition-colors"
              >
                Comisiones
              </Link>
            </li>
            <li className="relative pt-2">
              <Link
                to="/convenios"
                className="text-white no-underline py-2 px-4 rounded hover:bg-white/20 transition-colors"
              >
                Convenios
              </Link>
            </li>
            <li className="relative pt-2">
              <Link
                to="/consulta-general"
                className="text-white no-underline py-2 px-4 rounded hover:bg-white/20 transition-colors"
              >
                Consulta general
              </Link>
            </li>
            <li className="relative">
              <button
                ref={reportesButtonRef}
                onClick={() => setIsReportesMenuOpen(!isReportesMenuOpen)}
                className="flex items-center gap-1 text-white py-2 px-4 rounded hover:bg-white/20 transition-colors focus:outline-none focus:ring-2 focus:ring-white/30"
              >
                Reportes
                <svg
                  className={`w-4 h-4 transition-transform duration-200 ${
                    isReportesMenuOpen ? "rotate-180" : ""
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
              {isReportesMenuOpen && (
                <>
                  {/* Overlay invisible para cerrar dropdown */}
                  <div
                    className="fixed inset-0 z-40"
                    onClick={() => setIsReportesMenuOpen(false)}
                  ></div>

                  <div
                    ref={reportesDropdownRef}
                    className={`absolute top-full mt-2 w-56 max-w-[90vw] bg-white dark:bg-gray-800 rounded-lg shadow-xl border border-gray-200 dark:border-gray-700 z-50 overflow-hidden ${dropdownPosition}`}
                  >
                    <div className="py-2">
                      <Link
                        to="/diario"
                        className="flex items-center px-4 py-3 text-gray-700 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors no-underline"
                        onClick={() => setIsReportesMenuOpen(false)}
                      >
                        <ListaIcon className="w-5 h-5 mr-3 text-blue-500" />
                        <div>
                          <div className="font-medium">Diario</div>
                        </div>
                      </Link>

                      <Link
                        to="/reasignacion"
                        className="flex items-center px-4 py-3 text-gray-700 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors no-underline"
                        onClick={() => setIsReportesMenuOpen(false)}
                      >
                        <ReasignacionIcon className="w-5 h-5 mr-3 text-blue-500" />
                        <div>
                          <div className="font-medium">Reasignación</div>
                        </div>
                      </Link>

                      <Link
                        to="/semanal-mensual"
                        className="flex items-center px-4 py-3 text-gray-700 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors no-underline"
                        onClick={() => setIsReportesMenuOpen(false)}
                      >
                        <ScheduleIcon className="w-5 h-5 mr-3 text-blue-500" />
                        <div>
                          <div className="font-medium">Semanal y mensual</div>
                        </div>
                      </Link>
                      <Link
                        to="/atendidos-y-pendientes"
                        className="flex items-center px-4 py-3 text-gray-700 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors no-underline"
                        onClick={() => setIsReportesMenuOpen(false)}
                      >
                        <PendientesIcon className="w-5 h-5 mr-3 text-blue-500" />
                        <div>
                          <div className="font-medium">
                            Atendidos pendientes
                          </div>
                        </div>
                      </Link>
                      <Link
                        to="/responsabilidad-reportes"
                        className="flex items-center px-4 py-3 text-gray-700 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors no-underline"
                        onClick={() => setIsReportesMenuOpen(false)}
                      >
                        <UsersIcon className="w-5 h-5 mr-3 text-blue-500" />
                        <div>
                          <div className="font-medium">Por responsable</div>
                        </div>
                      </Link>
                    </div>
                  </div>

                  {/* Overlay para cerrar el menú */}
                  <div
                    className="fixed inset-0 z-40"
                    onClick={() => setIsReportesMenuOpen(false)}
                  ></div>
                </>
              )}
            </li>
          </ul>
        </nav>
      </div>
    </header>
  );
}

export default Header;
