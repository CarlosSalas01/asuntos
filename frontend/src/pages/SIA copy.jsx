import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useTheme } from "../context/ThemeContext";
import siaService from "../services/siaService";
import FormularioFiltros from "../components/consulta/FormularioFiltros";
import TablaResultados from "../components/consulta/TablaResultados";

/**
 * Componente SIA (Sistema Integral de Asuntos)
 * Equivalente a consultaAsuntosGeneral.jsp + ExportarSIAHTML.java
 * Permite realizar búsquedas de asuntos SIA con filtros y exportar resultados
 */
const SIA = () => {
  const { isDarkMode } = useTheme();
  const navigate = useNavigate();

  // Estados
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [resultados, setResultados] = useState([]);
  const [total, setTotal] = useState(0);
  const [mostrarResultados, setMostrarResultados] = useState(false);
  const [filtrosActuales, setFiltrosActuales] = useState(null);

  /**
   * Carga inicial de datos al montar el componente
   */
  useEffect(() => {
    // Ejecutar búsqueda automática con filtros por defecto
    const filtrosIniciales = {
      fechas: "envio",
      fecha1: "", // Sin filtro de fecha
      fecha2: "",
      areaFiltro: "0", // Todas las áreas
      texto: "",
    };

    handleBuscar(filtrosIniciales);
  }, []); // Solo se ejecuta al montar

  /**
   * Maneja la búsqueda de asuntos SIA
   */
  const handleBuscar = async (filtros) => {
    console.log("🔍 Filtros recibidos del formulario:", filtros);

    setLoading(true);
    setError(null);
    setMostrarResultados(false);

    try {
      // Transformar filtros del formulario al formato que espera el backend
      const filtrosTransformados = {
        tipoFecha: mapearTipoFecha(filtros.fechas),
        fechaInicio: convertirFechaAYYYYMMDD(filtros.fecha1),
        fechaFinal: convertirFechaAYYYYMMDD(filtros.fecha2),
        estatusAsunto: "T", // Todos por defecto
        estatusResp: "T", // Todos por defecto
        idarea: parseInt(filtros.areaFiltro) || 0,
        clasifica: "T",
        presidencia: "T",
        urgente: "T",
        texto: filtros.texto || "",
        id: "",
        porcentajeAvance: "",
        offset: 0,
      };

      console.log(
        "🔄 Filtros transformados para backend:",
        filtrosTransformados
      );

      const respuesta = await siaService.buscarAsuntosSIA(
        filtrosTransformados,
        false
      );

      console.log("✅ Resultados recibidos:", respuesta);

      setResultados(respuesta.data || []);
      setTotal(respuesta.total || 0);
      setFiltrosActuales(filtrosTransformados);
      setMostrarResultados(true);
    } catch (error) {
      console.error("❌ Error en búsqueda:", error);
      setError(
        error.response?.data?.message ||
          error.message ||
          "Error al buscar asuntos"
      );
      setResultados([]);
      setTotal(0);
      setMostrarResultados(false);
    } finally {
      setLoading(false);
    }
  };

  /**
   * Mapea tipo de fecha del formulario al formato backend
   */
  const mapearTipoFecha = (tipoFecha) => {
    const mapeo = {
      ingreso: "envio",
      atencion: "atencion",
      envio: "envio",
    };
    return mapeo[tipoFecha] || "envio";
  };

  /**
   * Convierte fecha de DD/MM/YYYY o YYYY-MM-DD a YYYYMMDD
   */
  const convertirFechaAYYYYMMDD = (fecha) => {
    if (!fecha) return "";

    // Si es formato YYYY-MM-DD (del input date)
    if (fecha.includes("-")) {
      return fecha.replace(/-/g, "");
    }

    // Si es formato DD/MM/YYYY
    if (fecha.includes("/")) {
      const partes = fecha.split("/");
      return `${partes[2]}${partes[1].padStart(2, "0")}${partes[0].padStart(
        2,
        "0"
      )}`;
    }

    return fecha;
  };

  /**
   * Exporta resultados a CSV
   */
  const handleExportarCSV = async () => {
    if (!filtrosActuales) {
      alert("Debe realizar una búsqueda antes de exportar");
      return;
    }

    try {
      setLoading(true);
      await siaService.exportarSIAcsv(filtrosActuales);
    } catch (error) {
      console.error("❌ Error exportando CSV:", error);
      setError("Error al exportar CSV");
    } finally {
      setLoading(false);
    }
  };

  /**
   * Exporta resultados a HTML
   */
  const handleExportarHTML = async () => {
    if (!filtrosActuales) {
      alert("Debe realizar una búsqueda antes de exportar");
      return;
    }

    try {
      setLoading(true);
      await siaService.exportarSIAhtml(filtrosActuales);
    } catch (error) {
      console.error("❌ Error exportando HTML:", error);
      setError("Error al exportar HTML");
    } finally {
      setLoading(false);
    }
  };

  /**
   * Maneja el clic en un asunto
   */
  const handleClickAsunto = (asunto) => {
    console.log("🚀 Clic en asunto:", asunto);
    // TODO: Navegar a detalle del asunto
    alert(`Ver detalle del asunto ${asunto.idconsecutivo}`);
  };

  /**
   * Formatea fecha de YYYYMMDD a DD/MM/YYYY
   */
  const formatearFecha = (fecha) => {
    if (!fecha || fecha.length < 8) return "";

    const anio = fecha.substring(0, 4);
    const mes = fecha.substring(4, 6);
    const dia = fecha.substring(6, 8);

    return `${dia}/${mes}/${anio}`;
  };

  return (
    <div className={`SIA min-h-full ${isDarkMode ? "dark" : "light"}`}>
      <div className="p-6 space-y-6">
        {/* Header de la página */}
        <div className="mb-6">
          <h1
            className={`text-3xl font-bold ${
              isDarkMode ? "text-white" : "text-gray-900"
            }`}
          >
            Sistema Integral de Asuntos (SIA)
          </h1>
          <p
            className={`mt-2 text-sm ${
              isDarkMode ? "text-gray-400" : "text-gray-600"
            }`}
          >
            Consulta y gestión de asuntos. Utiliza los filtros para buscar
            información específica.
          </p>
        </div>

        {/* Formulario de filtros */}
        <FormularioFiltros
          onBuscar={handleBuscar}
          areas={[]} // TODO: Cargar áreas del usuario
          loading={loading}
        />

        {/* Mensajes de error */}
        {error && (
          <div
            className={`p-4 rounded-md ${
              isDarkMode
                ? "bg-red-900/50 border border-red-700"
                : "bg-red-50 border border-red-200"
            }`}
          >
            <div className="flex">
              <div className="flex-shrink-0">
                <svg
                  className="h-5 w-5 text-red-400"
                  viewBox="0 0 20 20"
                  fill="currentColor"
                >
                  <path
                    fillRule="evenodd"
                    d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z"
                    clipRule="evenodd"
                  />
                </svg>
              </div>
              <div className="ml-3">
                <h3
                  className={`text-sm font-medium ${
                    isDarkMode ? "text-red-300" : "text-red-800"
                  }`}
                >
                  Error
                </h3>
                <div
                  className={`mt-2 text-sm ${
                    isDarkMode ? "text-red-400" : "text-red-700"
                  }`}
                >
                  {error}
                </div>
              </div>
            </div>
          </div>
        )}

        {/* Resultados */}
        {mostrarResultados && (
          <div
            className={`p-6 rounded-lg shadow-md ${
              isDarkMode ? "bg-gray-800" : "bg-white"
            }`}
          >
            {/* Header con botones de exportación */}
            <div className="flex justify-between items-center mb-4">
              <h2
                className={`text-xl font-bold ${
                  isDarkMode ? "text-white" : "text-gray-900"
                }`}
              >
                Resultados de la búsqueda ({total} asuntos encontrados)
              </h2>

              <div className="flex gap-2">
                <button
                  onClick={handleExportarCSV}
                  disabled={loading}
                  className="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 disabled:bg-gray-400 disabled:cursor-not-allowed"
                >
                  📥 Exportar CSV
                </button>

                <button
                  onClick={handleExportarHTML}
                  disabled={loading}
                  className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:bg-gray-400 disabled:cursor-not-allowed"
                >
                  📄 Exportar HTML
                </button>
              </div>
            </div>

            {/* Tabla de resultados - Formato idéntico a exportaSIA.jsp */}
            {resultados.length > 0 ? (
              <div className="overflow-x-auto">
                <table
                  className={`min-w-full border ${
                    isDarkMode ? "border-gray-700" : "border-gray-300"
                  }`}
                >
                  <thead
                    className={isDarkMode ? "bg-green-700" : "bg-green-600"}
                  >
                    <tr>
                      <th className="px-3 py-2 text-left text-xs font-semibold text-white border border-gray-400">
                        Consec.
                      </th>
                      <th className="px-3 py-2 text-left text-xs font-semibold text-white border border-gray-400">
                        ID Asunto
                      </th>
                      <th className="px-3 py-2 text-left text-xs font-semibold text-white border border-gray-400">
                        Turno
                      </th>
                      <th className="px-3 py-2 text-left text-xs font-semibold text-white border border-gray-400">
                        Asunto
                      </th>
                      <th className="px-3 py-2 text-left text-xs font-semibold text-white border border-gray-400">
                        Instrucción
                      </th>
                      <th className="px-3 py-2 text-left text-xs font-semibold text-white border border-gray-400">
                        Fecha envío
                      </th>
                      <th className="px-3 py-2 text-left text-xs font-semibold text-white border border-gray-400">
                        Fecha vencimiento
                      </th>
                      <th className="px-3 py-2 text-left text-xs font-semibold text-white border border-gray-400">
                        Última reprogramación
                      </th>
                      <th className="px-3 py-2 text-left text-xs font-semibold text-white border border-gray-400">
                        Destinatarios
                      </th>
                      <th className="px-3 py-2 text-left text-xs font-semibold text-white border border-gray-400">
                        Estatus Resp.
                      </th>
                      <th className="px-3 py-2 text-left text-xs font-semibold text-white border border-gray-400">
                        Avance
                      </th>
                      <th className="px-3 py-2 text-left text-xs font-semibold text-white border border-gray-400">
                        Último Avance
                      </th>
                      <th className="px-3 py-2 text-left text-xs font-semibold text-white border border-gray-400">
                        Fecha de atención
                      </th>
                      <th className="px-3 py-2 text-left text-xs font-semibold text-white border border-gray-400">
                        Días Proceso/Atención
                      </th>
                      <th className="px-3 py-2 text-left text-xs font-semibold text-white border border-gray-400">
                        Días retraso
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {resultados.map((asunto, index) => {
                      const consecutivo = index + 1;
                      const responsables = asunto.responsables || [];
                      const numResponsables = responsables.length || 1;

                      // Si no hay responsables, mostrar una fila vacía
                      if (responsables.length === 0) {
                        return (
                          <tr
                            key={asunto.idasunto || index}
                            className={
                              isDarkMode
                                ? "bg-gray-800 hover:bg-gray-700"
                                : "bg-white hover:bg-gray-50"
                            }
                          >
                            <td
                              className={`px-3 py-2 text-sm text-center border ${
                                isDarkMode
                                  ? "border-gray-700 text-gray-300"
                                  : "border-gray-300 text-gray-900"
                              }`}
                            >
                              {consecutivo}
                            </td>
                            <td
                              className={`px-3 py-2 text-sm border ${
                                isDarkMode
                                  ? "border-gray-700 text-gray-300"
                                  : "border-gray-300 text-gray-900"
                              }`}
                            >
                              {asunto.idconsecutivo}
                            </td>
                            <td
                              className={`px-3 py-2 text-sm border ${
                                isDarkMode
                                  ? "border-gray-700 text-gray-300"
                                  : "border-gray-300 text-gray-900"
                              }`}
                            >
                              {asunto.nocontrol}
                            </td>
                            <td
                              className={`px-3 py-2 text-sm border ${
                                isDarkMode
                                  ? "border-gray-700 text-gray-300"
                                  : "border-gray-300 text-gray-900"
                              }`}
                            >
                              {asunto.descripcion}
                            </td>
                            <td
                              className={`px-3 py-2 text-sm border ${
                                isDarkMode
                                  ? "border-gray-700 text-gray-300"
                                  : "border-gray-300 text-gray-900"
                              }`}
                            >
                              {asunto.estatustexto}
                            </td>
                            <td
                              className={`px-3 py-2 text-sm border ${
                                isDarkMode
                                  ? "border-gray-700 text-gray-300"
                                  : "border-gray-300 text-gray-900"
                              }`}
                            >
                              {formatearFecha(asunto.fechaingreso)}
                            </td>
                            <td
                              className={`px-3 py-2 text-sm border ${
                                isDarkMode
                                  ? "border-gray-700 text-gray-300"
                                  : "border-gray-300 text-gray-900"
                              }`}
                            >
                              {formatearFecha(asunto.fechaatender)}
                            </td>
                            <td
                              className={`px-3 py-2 text-sm border ${
                                isDarkMode
                                  ? "border-gray-700 text-gray-300"
                                  : "border-gray-300 text-gray-900"
                              }`}
                            >
                              {asunto.fechaUltimaReprogramacion || ""}
                            </td>
                            <td
                              colSpan="7"
                              className={`px-3 py-2 text-sm text-center border ${
                                isDarkMode
                                  ? "border-gray-700 text-gray-500"
                                  : "border-gray-300 text-gray-500"
                              }`}
                            >
                              Sin responsables asignados
                            </td>
                          </tr>
                        );
                      }

                      // Renderizar fila(s) con responsables
                      return responsables.map((responsable, respIndex) => (
                        <tr
                          key={`${asunto.idasunto}-${respIndex}`}
                          className={
                            isDarkMode
                              ? "bg-gray-800 hover:bg-gray-700"
                              : "even:bg-gray-50 hover:bg-gray-100"
                          }
                        >
                          {/* Celdas con rowspan solo en la primera fila */}
                          {respIndex === 0 && (
                            <>
                              <td
                                rowSpan={numResponsables}
                                className={`px-3 py-2 text-sm text-center border ${
                                  isDarkMode
                                    ? "border-gray-700 text-gray-300"
                                    : "border-gray-300 text-gray-900"
                                }`}
                              >
                                {consecutivo}
                              </td>
                              <td
                                rowSpan={numResponsables}
                                className={`px-3 py-2 text-sm border ${
                                  isDarkMode
                                    ? "border-gray-700 text-gray-300"
                                    : "border-gray-300 text-gray-900"
                                }`}
                              >
                                {asunto.idconsecutivo}
                              </td>
                              <td
                                rowSpan={numResponsables}
                                className={`px-3 py-2 text-sm border ${
                                  isDarkMode
                                    ? "border-gray-700 text-gray-300"
                                    : "border-gray-300 text-gray-900"
                                }`}
                              >
                                '{asunto.nocontrol}
                              </td>
                              <td
                                rowSpan={numResponsables}
                                className={`px-3 py-2 text-sm border ${
                                  isDarkMode
                                    ? "border-gray-700 text-gray-300"
                                    : "border-gray-300 text-gray-900"
                                }`}
                              >
                                {asunto.descripcion}
                              </td>
                              <td
                                rowSpan={numResponsables}
                                className={`px-3 py-2 text-sm border ${
                                  isDarkMode
                                    ? "border-gray-700 text-gray-300"
                                    : "border-gray-300 text-gray-900"
                                }`}
                              >
                                {asunto.estatustexto}
                              </td>
                              <td
                                rowSpan={numResponsables}
                                className={`px-3 py-2 text-sm border ${
                                  isDarkMode
                                    ? "border-gray-700 text-gray-300"
                                    : "border-gray-300 text-gray-900"
                                }`}
                              >
                                {formatearFecha(asunto.fechaingreso)}
                              </td>
                              <td
                                rowSpan={numResponsables}
                                className={`px-3 py-2 text-sm border ${
                                  isDarkMode
                                    ? "border-gray-700 text-gray-300"
                                    : "border-gray-300 text-gray-900"
                                }`}
                              >
                                {formatearFecha(asunto.fechaatender)}
                              </td>
                              <td
                                rowSpan={numResponsables}
                                className={`px-3 py-2 text-sm border ${
                                  isDarkMode
                                    ? "border-gray-700 text-gray-300"
                                    : "border-gray-300 text-gray-900"
                                }`}
                              >
                                {asunto.fechaUltimaReprogramacion || ""}
                              </td>
                            </>
                          )}

                          {/* Datos del responsable - se repiten para cada responsable */}
                          <td
                            className={`px-3 py-2 text-sm border ${
                              isDarkMode
                                ? "border-gray-700 text-gray-300"
                                : "border-gray-300 text-gray-900"
                            }`}
                          >
                            {responsable.area?.nivel === 2
                              ? responsable.area?.siglas
                              : responsable.area?.nombre}
                          </td>
                          <td
                            className={`px-3 py-2 text-sm border ${
                              isDarkMode
                                ? "border-gray-700 text-gray-300"
                                : "border-gray-300 text-gray-900"
                            }`}
                          >
                            {responsable.datos?.estatus === "P" && "PENDIENTE"}
                            {responsable.datos?.estatus === "A" && "ATENDIDO"}
                            {responsable.datos?.estatus === "C" && "CANCELADO"}
                          </td>
                          <td
                            className={`px-3 py-2 text-sm border ${
                              isDarkMode
                                ? "border-gray-700 text-gray-300"
                                : "border-gray-300 text-gray-900"
                            }`}
                          >
                            {responsable.datos?.avance || 0} %
                          </td>
                          <td
                            className={`px-3 py-2 text-sm border ${
                              isDarkMode
                                ? "border-gray-700 text-gray-300"
                                : "border-gray-300 text-gray-900"
                            }`}
                          >
                            {responsable.ultimoAvanceGlobal || ""}
                          </td>
                          <td
                            className={`px-3 py-2 text-sm border ${
                              isDarkMode
                                ? "border-gray-700 text-gray-300"
                                : "border-gray-300 text-gray-900"
                            }`}
                          >
                            {formatearFecha(responsable.datos?.fechaatencion)}
                          </td>
                          <td
                            className={`px-3 py-2 text-sm border ${
                              isDarkMode
                                ? "border-gray-700 text-gray-300"
                                : "border-gray-300 text-gray-900"
                            }`}
                          >
                            {responsable.datos?.diasatencion || 0}
                          </td>
                          <td
                            className={`px-3 py-2 text-sm border ${
                              isDarkMode
                                ? "border-gray-700 text-gray-300"
                                : "border-gray-300 text-gray-900"
                            }`}
                          >
                            {responsable.datos?.diasretraso || 0}
                          </td>
                        </tr>
                      ));
                    })}
                  </tbody>
                </table>
              </div>
            ) : (
              <div
                className={`text-center py-12 ${
                  isDarkMode ? "text-gray-400" : "text-gray-500"
                }`}
              >
                <p className="text-lg">
                  No existen asuntos SIA para este usuario con dichas
                  características.
                </p>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default SIA;
