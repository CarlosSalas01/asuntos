import React, { useEffect, useState } from "react";
import apiService from "../services/apiService.js";
import { RightArrow } from "../components/icons/CustomIcons.jsx";
import ScrollButton from "../components/ScrollButton.jsx";

// Este componente tiene esta variable porque puede recibir clases CSS externas. En este caso, la llamada es desde App.jsx
const SIA = ({ className = "" }) => {
  const [asuntos, setAsuntos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filtroGeneral, setFiltroGeneral] = useState("");
  const [filtroEstatus, setFiltroEstatus] = useState("");
  const [mostrarFiltros, setMostrarFiltros] = useState(true);

  // Estados de paginación
  const [paginaActual, setPaginaActual] = useState(1);
  const [registrosPorPagina, setRegistrosPorPagina] = useState(10);

  const cargarAsuntos = async (search = "") => {
    try {
      setLoading(true);
      const filtros = search ? { search } : {};
      const data = await apiService.obtenerAsuntosSIA(filtros);
      setAsuntos(data);
    } catch (err) {
      console.error("Error cargando asuntos SIA:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    cargarAsuntos();
  }, []);

  // Filtrar asuntos en tiempo real
  const asuntosFiltrados = asuntos.filter((asunto) => {
    // Filtro por estatus del asunto
    if (filtroEstatus && asunto.estatus !== filtroEstatus) {
      return false;
    }

    // Filtro general (búsqueda local adicional)
    if (!filtroGeneral.trim()) return true;
    const busqueda = filtroGeneral.trim().toLowerCase();

    // Buscar en campos del asunto
    const coincideAsunto =
      String(asunto.idasunto ?? "")
        .toLowerCase()
        .includes(busqueda) ||
      String(asunto.idconsecutivo ?? "")
        .toLowerCase()
        .includes(busqueda) ||
      String(asunto.descripcion ?? "")
        .toLowerCase()
        .includes(busqueda) ||
      String(asunto.fechaingreso ?? "")
        .toLowerCase()
        .includes(busqueda);

    // Buscar en responsables
    const coincideResponsable = (asunto.responsables || []).some((r) =>
      String(r.siglas ?? "")
        .toLowerCase()
        .includes(busqueda)
    );

    return coincideAsunto || coincideResponsable;
  });

  // Cálculos de paginación
  const totalRegistros = asuntosFiltrados.length;
  const totalPaginas = Math.ceil(totalRegistros / registrosPorPagina);
  const indiceInicio = (paginaActual - 1) * registrosPorPagina;
  const indiceFin = indiceInicio + registrosPorPagina;
  const asuntosPaginados = asuntosFiltrados.slice(indiceInicio, indiceFin);

  // Resetear a página 1 cuando cambian los filtros o registros por página
  useEffect(() => {
    setPaginaActual(1);
  }, [filtroGeneral, filtroEstatus, registrosPorPagina]);

  const cambiarPagina = (nuevaPagina) => {
    if (nuevaPagina >= 1 && nuevaPagina <= totalPaginas) {
      setPaginaActual(nuevaPagina);
    }
  };

  const getEstatusLabel = (estatus) => {
    switch (estatus) {
      case "A":
        return "Atendido";
      case "P":
        return "Pendiente";
      default:
        return estatus || "";
    }
  };

  // Formatear fecha de YYYYMMDD a DD/MM/YYYY
  const formatearFecha = (fecha) => {
    if (!fecha || fecha.length !== 8) return fecha || "";
    const anio = fecha.substring(0, 4);
    const mes = fecha.substring(4, 6);
    const dia = fecha.substring(6, 8);
    return `${dia}/${mes}/${anio}`;
  };

  if (loading) return <div>Cargando...</div>;
  return (
    <div className={`h-full w-full p-3 overflow-auto relative ${className}`}>
      <div className="rounded-full bg-gradient-to-br from-blue-950 to-teal-950 text-white font-bold py-2 px-6 mb-4 w-fit ml-0">
        <h1 className="text-lg font-bold my-0">Sistema Integral de Asuntos</h1>
      </div>
      <div className="my-7"></div>
      <div className="mt-11">
        <h1
          className="font-bold my-2 text-slate-700 dark:text-slate-200 text-lg cursor-pointer select-none flex items-center w-fit"
          onClick={() => setMostrarFiltros(!mostrarFiltros)}
        >
          Filtros
          <RightArrow
            className={`inline-block ml-2 h-5 w-5 text-gray-500 transition-transform duration-300 ease-in-out ${
              mostrarFiltros ? "rotate-90" : "rotate-0"
            }`}
          />
        </h1>
        <div
          className={`flex gap-11 flex-wrap bg-gradient-to-br from-slate-200 to-slate-100 dark:from-slate-800 dark:to-slate-700 p-4 rounded-md transition-all duration-300 ease-in-out overflow-hidden ${
            mostrarFiltros
              ? "max-h-96 opacity-100 mt-2"
              : "max-h-0 opacity-0 p-0 border-0 mt-0"
          }`}
          id="divFiltros"
        >
          <div className="flex flex-col flex-1 min-w-[200px]">
            <label className="font-medium text-slate-700 dark:text-slate-200 mb-1 ">
              Búsqueda general
            </label>
            <input
              type="text"
              id="general"
              placeholder="Ingresa texto"
              className="py-2 px-2 rounded-md shadow-sm text-slate-800 dark:bg-gray-300 placeholder:text-slate-600"
              value={filtroGeneral}
              onChange={(e) => setFiltroGeneral(e.target.value)}
            />
          </div>
          <div className="flex flex-col flex-1 min-w-[200px]">
            <label className="font-medium text-slate-700 dark:text-slate-200 mb-1">
              Estatus
            </label>
            <select
              className="py-2 px-2 rounded-md shadow-sm text-slate-800 dark:bg-gray-300"
              value={filtroEstatus}
              onChange={(e) => setFiltroEstatus(e.target.value)}
            >
              <option value="">Todos</option>
              <option value="A">Activo</option>
              <option value="P">Pendiente</option>
            </select>
          </div>
        </div>
        <hr
          className={`border-slate-500 transition-all duration-300 ${
            mostrarFiltros ? "mt-6" : "mt-0 mb-0"
          }`}
        />
      </div>
      <div className="my-7"></div>

      {/* Controles de paginación superior */}
      <div className="flex flex-wrap items-center justify-between gap-4 mb-4">
        <div className="flex items-center gap-2">
          <label className="text-slate-700 dark:text-slate-200 text-sm">
            Mostrar
          </label>
          <select
            className="py-1 px-2 rounded-md shadow-sm dark:text-slate-200  text-slate-700 border dark:border-gray-400 border-gray-300 dark:bg-slate-700 bg-white"
            value={registrosPorPagina}
            onChange={(e) => setRegistrosPorPagina(Number(e.target.value))}
          >
            <option value={10}>10</option>
            <option value={25}>25</option>
            <option value={50}>50</option>
          </select>
          <label className="text-slate-700 dark:text-slate-200 text-sm">
            registros
          </label>
        </div>
        <div className="text-slate-600 dark:text-slate-300 text-sm">
          Mostrando {totalRegistros === 0 ? 0 : indiceInicio + 1} -{" "}
          {Math.min(indiceFin, totalRegistros)} de {totalRegistros} registros
        </div>
      </div>

      <div className="rounded-sm shadow-sm overflow-hidden">
        <table className="w-full border border-gray-300 table-fixed">
          <thead className="bg-gradient-to-br from-blue-950 to-teal-950 text-white py-4 shadow-md rounded-md">
            <tr>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-24">
                Asunto
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-28">
                Clasificación
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-62">
                Descripción
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-62">
                Instrucción
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-28">
                Fecha de envío
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-28">
                Fecha de vencimiento
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-32">
                Destinatarios
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-32">
                Estatus responsable
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-20">
                Avance
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-24">
                Fecha de atención
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-32">
                Observaciones
              </th>
            </tr>
          </thead>
          <tbody>
            {asuntosPaginados.map((asunto) => {
              const responsables = asunto.responsables || [];
              const numResp = Math.max(1, responsables.length);
              console.log("fechaingreso:", asunto.fechaingreso);

              return (
                <React.Fragment key={asunto.idasunto}>
                  <tr>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1"
                    >
                      {asunto.idconsecutivo}
                      <br />
                      <span className="text-xs">
                        {getEstatusLabel(asunto.estatus)}
                      </span>
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1"
                    >
                      {asunto.presidencia}
                      <br />
                      {asunto.nocontrol}
                      <br />
                      {asunto.estatuscorto}
                      <br />
                      {asunto.fuente}
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 p-2"
                    >
                      {asunto.descripcion}
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 p-2"
                    >
                      {asunto.estatustexto}
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1"
                    >
                      {formatearFecha(asunto.fechaingreso)}
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1"
                    >
                      {asunto.urgente}
                      <br />
                      {formatearFecha(asunto.fechaatender)}
                    </td>
                    {/* Columnas del primer responsable (sin rowSpan) */}
                    <td className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1">
                      {responsables[0]?.siglas || ""}
                    </td>
                    <td className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1">
                      {getEstatusLabel(responsables[0]?.estatus)}
                    </td>
                    <td className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1">
                      {responsables[0]?.avance
                        ? `${responsables[0].avance}%`
                        : ""}
                    </td>
                    <td className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1">
                      {responsables[0]?.fechaatencion ?? ""}
                    </td>
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 p-2"
                    >
                      {asunto.observaciones}
                    </td>
                  </tr>

                  {/* Filas adicionales por cada responsable restante */}
                  {responsables.slice(1).map((resp, idx) => (
                    <tr key={`${asunto.idasunto}-resp-${idx}`}>
                      <td className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1">
                        {resp.siglas}
                      </td>
                      <td className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1">
                        {getEstatusLabel(resp.estatus)}
                      </td>
                      <td className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1">
                        {resp.avance ?? ""}
                      </td>
                      <td className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1">
                        {resp.fechaatencion ?? ""}
                      </td>
                    </tr>
                  ))}
                </React.Fragment>
              );
            })}
          </tbody>
        </table>
      </div>

      {/* Controles de paginación inferior */}
      {totalPaginas > 1 && (
        <div className="flex flex-wrap items-center justify-center gap-2 mt-7">
          <button
            onClick={() => cambiarPagina(1)}
            disabled={paginaActual === 1}
            className="px-3 py-1 rounded-md bg-slate-200 dark:bg-slate-700 text-slate-700 dark:text-slate-200 disabled:opacity-50 disabled:cursor-not-allowed hover:bg-slate-300 dark:hover:bg-slate-600 transition-colors"
          >
            «
          </button>
          <button
            onClick={() => cambiarPagina(paginaActual - 1)}
            disabled={paginaActual === 1}
            className="px-3 py-1 rounded-md bg-slate-200 dark:bg-slate-700 text-slate-700 dark:text-slate-200 disabled:opacity-50 disabled:cursor-not-allowed hover:bg-slate-300 dark:hover:bg-slate-600 transition-colors"
          >
            ‹
          </button>

          {/* Números de página */}
          {(() => {
            const paginas = [];
            const maxVisible = 5;
            let inicio = Math.max(1, paginaActual - Math.floor(maxVisible / 2));
            let fin = Math.min(totalPaginas, inicio + maxVisible - 1);

            if (fin - inicio + 1 < maxVisible) {
              inicio = Math.max(1, fin - maxVisible + 1);
            }

            for (let i = inicio; i <= fin; i++) {
              paginas.push(
                <button
                  key={i}
                  onClick={() => cambiarPagina(i)}
                  className={`px-3 py-1 rounded-md transition-colors ${
                    i === paginaActual
                      ? "bg-gradient-to-br from-blue-900 to-teal-900 text-white hover:from-teal-800 hover:to-blue-900"
                      : "bg-slate-200 dark:bg-slate-700 text-slate-700 dark:text-slate-200 hover:bg-slate-300 dark:hover:bg-slate-600"
                  }`}
                >
                  {i}
                </button>
              );
            }
            return paginas;
          })()}

          <button
            onClick={() => cambiarPagina(paginaActual + 1)}
            disabled={paginaActual === totalPaginas}
            className="px-3 py-1 rounded-md bg-slate-200 dark:bg-slate-700 text-slate-700 dark:text-slate-200 disabled:opacity-50 disabled:cursor-not-allowed hover:bg-slate-300 dark:hover:bg-slate-600 transition-colors"
          >
            ›
          </button>
          <button
            onClick={() => cambiarPagina(totalPaginas)}
            disabled={paginaActual === totalPaginas}
            className="px-3 py-1 rounded-md bg-slate-200 dark:bg-slate-700 text-slate-700 dark:text-slate-200 disabled:opacity-50 disabled:cursor-not-allowed hover:bg-slate-300 dark:hover:bg-slate-600 transition-colors"
          >
            »
          </button>
        </div>
      )}

      {/* Botón flotante de scroll */}
      <ScrollButton />
    </div>
  );
};

export default SIA;
