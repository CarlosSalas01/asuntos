import React, { useState, useEffect } from "react";
import { useTheme } from "../../context/ThemeContext";

/**
 * Componente FormularioFiltros
 * Equivalente al formulario en consultaAsuntosGeneral.jsp
 * Permite filtrar por tipo de fecha, rango de fechas, área y texto
 */
const FormularioFiltros = ({
  onBuscar,
  areas = [],
  loading = false,
  className = "",
}) => {
  const { isDarkMode } = useTheme();

  // Estado del formulario
  const [filtros, setFiltros] = useState({
    fechas: "ingreso", // Tipo de fecha por defecto
    fecha1: "", // Fecha inicio
    fecha2: "", // Fecha fin
    areaFiltro: "0", // Área (0 = todas)
    texto: "", // Texto de búsqueda
  });

  const [errores, setErrores] = useState({});

  // Opciones de tipo de fecha
  const tiposFecha = [
    { value: "ingreso", label: "Fecha de ingreso" },
    { value: "atencion", label: "Fecha de atención" },
    { value: "envio", label: "Fecha de envío" },
  ];

  // Configurar fechas por defecto al montar el componente
  useEffect(() => {
    const hoy = new Date();
    const inicioAno = new Date(hoy.getFullYear(), 0, 1);

    setFiltros((prev) => ({
      ...prev,
      fecha1: formatearFechaInput(inicioAno),
      fecha2: formatearFechaInput(hoy),
    }));
  }, []);

  /**
   * Maneja cambios en los campos del formulario
   */
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFiltros((prev) => ({
      ...prev,
      [name]: value,
    }));

    // Limpiar error del campo modificado
    if (errores[name]) {
      setErrores((prev) => ({
        ...prev,
        [name]: null,
      }));
    }
  };

  /**
   * Valida las fechas del formulario
   */
  const validarFechas = () => {
    const erroresNuevos = {};

    if (!filtros.fecha1) {
      erroresNuevos.fecha1 = "La fecha de inicio es requerida";
    }

    if (!filtros.fecha2) {
      erroresNuevos.fecha2 = "La fecha de fin es requerida";
    }

    if (filtros.fecha1 && filtros.fecha2) {
      const fechaInicio = new Date(filtros.fecha1);
      const fechaFin = new Date(filtros.fecha2);

      if (fechaInicio > fechaFin) {
        erroresNuevos.fecha1 =
          "La fecha de inicio debe ser menor que la fecha de término";
      }
    }

    return erroresNuevos;
  };

  /**
   * Maneja el envío del formulario
   */
  const handleSubmit = (e) => {
    e.preventDefault();

    const nuevosErrores = validarFechas();

    if (Object.keys(nuevosErrores).length > 0) {
      setErrores(nuevosErrores);
      return;
    }

    // Convertir fechas al formato esperado por el backend (DD/MM/YYYY)
    const filtrosParaEnvio = {
      ...filtros,
      fecha1: convertirFechaParaBackend(filtros.fecha1),
      fecha2: convertirFechaParaBackend(filtros.fecha2),
    };

    console.log("📝 Enviando filtros:", filtrosParaEnvio);
    onBuscar(filtrosParaEnvio);
  };

  /**
   * Resetea el formulario
   */
  const handleReset = () => {
    const hoy = new Date();
    const inicioAno = new Date(hoy.getFullYear(), 0, 1);

    setFiltros({
      fechas: "ingreso",
      fecha1: formatearFechaInput(inicioAno),
      fecha2: formatearFechaInput(hoy),
      areaFiltro: "0",
      texto: "",
    });

    setErrores({});
  };

  return (
    <div className={className}>
      <div
        className={`rounded-lg border shadow-sm ${
          isDarkMode
            ? "bg-gray-800 border-gray-700"
            : "bg-white border-gray-200"
        }`}
      >
        {/* Header */}
        <div
          className={`px-6 py-4 border-b ${
            isDarkMode
              ? "border-gray-700 bg-gray-750"
              : "border-gray-200 bg-green-50"
          }`}
        >
          <h2
            className={`text-lg font-semibold text-center ${
              isDarkMode ? "text-white" : "text-green-800"
            }`}
          >
            🔍 Consulta general de asuntos
          </h2>
        </div>

        {/* Formulario */}
        <form onSubmit={handleSubmit} className="p-6">
          <div className="space-y-6">
            {/* Primera fila: Tipo de fecha y rango de fechas */}
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4 items-end">
              {/* Tipo de fecha */}
              <div>
                <label
                  className={`block text-sm font-medium mb-2 ${
                    isDarkMode ? "text-gray-300" : "text-gray-700"
                  }`}
                >
                  Tipo de fecha:
                </label>
                <select
                  name="fechas"
                  value={filtros.fechas}
                  onChange={handleChange}
                  className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 transition-colors ${
                    isDarkMode
                      ? "bg-gray-700 border-gray-600 text-white focus:ring-blue-500 focus:border-blue-500"
                      : "bg-white border-gray-300 text-gray-900 focus:ring-blue-500 focus:border-blue-500"
                  }`}
                >
                  {tiposFecha.map((tipo) => (
                    <option key={tipo.value} value={tipo.value}>
                      {tipo.label}
                    </option>
                  ))}
                </select>
              </div>

              {/* Fecha inicio */}
              <div>
                <label
                  className={`block text-sm font-medium mb-2 ${
                    isDarkMode ? "text-gray-300" : "text-gray-700"
                  }`}
                >
                  Desde:
                </label>
                <input
                  type="date"
                  name="fecha1"
                  value={filtros.fecha1}
                  onChange={handleChange}
                  className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 transition-colors ${
                    errores.fecha1
                      ? "border-red-500 focus:ring-red-500"
                      : isDarkMode
                      ? "bg-gray-700 border-gray-600 text-white focus:ring-blue-500 focus:border-blue-500"
                      : "bg-white border-gray-300 text-gray-900 focus:ring-blue-500 focus:border-blue-500"
                  }`}
                />
                {errores.fecha1 && (
                  <p className="mt-1 text-sm text-red-500">{errores.fecha1}</p>
                )}
              </div>

              {/* Fecha fin */}
              <div>
                <label
                  className={`block text-sm font-medium mb-2 ${
                    isDarkMode ? "text-gray-300" : "text-gray-700"
                  }`}
                >
                  Hasta:
                </label>
                <input
                  type="date"
                  name="fecha2"
                  value={filtros.fecha2}
                  onChange={handleChange}
                  className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 transition-colors ${
                    errores.fecha2
                      ? "border-red-500 focus:ring-red-500"
                      : isDarkMode
                      ? "bg-gray-700 border-gray-600 text-white focus:ring-blue-500 focus:border-blue-500"
                      : "bg-white border-gray-300 text-gray-900 focus:ring-blue-500 focus:border-blue-500"
                  }`}
                />
                {errores.fecha2 && (
                  <p className="mt-1 text-sm text-red-500">{errores.fecha2}</p>
                )}
              </div>

              {/* Botón Consultar */}
              <div>
                <button
                  type="submit"
                  disabled={loading}
                  className={`w-full px-4 py-2 rounded-md font-medium transition-colors focus:outline-none focus:ring-2 focus:ring-offset-2 ${
                    loading
                      ? "bg-gray-400 cursor-not-allowed"
                      : isDarkMode
                      ? "bg-blue-600 hover:bg-blue-700 focus:ring-blue-500 text-white"
                      : "bg-blue-600 hover:bg-blue-700 focus:ring-blue-500 text-white"
                  }`}
                >
                  {loading ? (
                    <div className="flex items-center justify-center">
                      <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                      Consultando...
                    </div>
                  ) : (
                    "Consultar"
                  )}
                </button>
              </div>
            </div>

            {/* Segunda fila: Área y texto de búsqueda */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 items-end">
              {/* Área */}
              <div>
                <label
                  className={`block text-sm font-medium mb-2 ${
                    isDarkMode ? "text-gray-300" : "text-gray-700"
                  }`}
                >
                  Área:
                </label>
                <select
                  name="areaFiltro"
                  value={filtros.areaFiltro}
                  onChange={handleChange}
                  className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 transition-colors ${
                    isDarkMode
                      ? "bg-gray-700 border-gray-600 text-white focus:ring-blue-500 focus:border-blue-500"
                      : "bg-white border-gray-300 text-gray-900 focus:ring-blue-500 focus:border-blue-500"
                  }`}
                >
                  <option value="0">Todas...</option>
                  {areas.map((area) => (
                    <option key={area.idarea} value={area.idarea}>
                      {area.nombre}
                    </option>
                  ))}
                </select>
              </div>

              {/* Texto de búsqueda */}
              <div>
                <label
                  className={`block text-sm font-medium mb-2 ${
                    isDarkMode ? "text-gray-300" : "text-gray-700"
                  }`}
                >
                  Buscar información:
                </label>
                <input
                  type="text"
                  name="texto"
                  value={filtros.texto}
                  onChange={handleChange}
                  placeholder="Ingresa texto a buscar..."
                  className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 transition-colors ${
                    isDarkMode
                      ? "bg-gray-700 border-gray-600 text-white placeholder-gray-400 focus:ring-blue-500 focus:border-blue-500"
                      : "bg-white border-gray-300 text-gray-900 placeholder-gray-500 focus:ring-blue-500 focus:border-blue-500"
                  }`}
                />
              </div>

              {/* Botón Reset */}
              <div>
                <button
                  type="button"
                  onClick={handleReset}
                  className={`w-full px-4 py-2 rounded-md font-medium transition-colors focus:outline-none focus:ring-2 focus:ring-offset-2 ${
                    isDarkMode
                      ? "bg-gray-600 hover:bg-gray-700 focus:ring-gray-500 text-white"
                      : "bg-gray-500 hover:bg-gray-600 focus:ring-gray-500 text-white"
                  }`}
                >
                  🔄 Reiniciar
                </button>
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};

/**
 * Formatea fecha para input type="date" (YYYY-MM-DD)
 */
const formatearFechaInput = (fecha) => {
  if (!fecha) return "";

  const d = new Date(fecha);
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, "0");
  const day = String(d.getDate()).padStart(2, "0");

  return `${year}-${month}-${day}`;
};

/**
 * Convierte fecha de YYYY-MM-DD a DD/MM/YYYY para el backend
 */
const convertirFechaParaBackend = (fechaInput) => {
  if (!fechaInput) return "";

  const [year, month, day] = fechaInput.split("-");
  return `${day}/${month}/${year}`;
};

export default FormularioFiltros;
