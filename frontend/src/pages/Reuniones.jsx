import React, { useEffect, useState } from "react";
import { RightArrow } from "../components/icons/CustomIcons.jsx";
import ScrollButton from "../components/ScrollButton.jsx";
import Paginacion, {
  PaginacionNavegacion,
  usePaginacion,
} from "../components/Paginacion.jsx";
import apiService from "../services/apiService.js";

const Reuniones = ({ className = "" }) => {
  const [reuniones, setReuniones] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filtroGeneral, setFiltroGeneral] = useState("");
  const [filtroEstatus, setFiltroEstatus] = useState("");
  const [mostrarFiltros, setMostrarFiltros] = useState(true);

  const cargarReuniones = async (search = "") => {
    try {
      setLoading(true);
      const filtros = search ? { search } : {};
      const data = await apiService.obtenerReuniones(filtros);
      setReuniones(data);
    } catch (err) {
      console.error("Error cargando reuniones:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    cargarReuniones();
  }, []);

  // Filtrar reuniones en tiempo real
  const reunionesFiltradas = reuniones.filter((reunion) => {
    if (filtroEstatus && reunion.estatus !== filtroEstatus) {
      return false;
    }

    if (!filtroGeneral.trim()) return true;
    const busqueda = filtroGeneral.trim().toLowerCase();

    const coincideReunion =
      String(reunion.idasunto ?? "")
        .toLowerCase()
        .includes(busqueda) ||
      String(reunion.idconsecutivo ?? "")
        .toLowerCase()
        .includes(busqueda) ||
      String(reunion.descripcion ?? "")
        .toLowerCase()
        .includes(busqueda) ||
      String(reunion.fechaingreso ?? "")
        .toLowerCase()
        .includes(busqueda);

    const coincideResponsable = (reunion.responsables || []).some((r) =>
      String(r.siglas ?? "")
        .toLowerCase()
        .includes(busqueda)
    );

    return coincideReunion || coincideResponsable;
  });

  // Hook de paginación
  const {
    paginaActual,
    setPaginaActual,
    registrosPorPagina,
    setRegistrosPorPagina,
    totalRegistros,
    totalPaginas,
    indiceInicio,
    indiceFin,
    datosPaginados: reunionesPaginadas,
  } = usePaginacion(reunionesFiltradas);

  useEffect(() => {
    setPaginaActual(1);
  }, [filtroGeneral, filtroEstatus]);

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

  if (loading) return <div>Cargando...</div>;

  return (
    <div className={`h-full w-full p-3 overflow-auto relative ${className}`}>
      <div className="rounded-full bg-gradient-to-br from-blue-950 to-teal-950 text-white font-bold py-2 px-6 mb-4 w-fit ml-0">
        <h1 className="text-lg font-bold my-0">Reuniones</h1>
      </div>
      <div className="my-7"></div>

      {/* Sección de Filtros */}
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
            <label className="font-medium text-slate-700 dark:text-slate-200 mb-1">
              Búsqueda general
            </label>
            <input
              type="text"
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
              <option value="A">Atendido</option>
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
      <Paginacion
        paginaActual={paginaActual}
        totalPaginas={totalPaginas}
        onCambiarPagina={setPaginaActual}
        totalRegistros={totalRegistros}
        registrosPorPagina={registrosPorPagina}
        onCambiarRegistrosPorPagina={setRegistrosPorPagina}
        indiceInicio={indiceInicio}
        indiceFin={indiceFin}
      />

      <div className="rounded-sm shadow-sm overflow-hidden">
        <table className="w-full border border-gray-300 table-fixed">
          <thead className="bg-gradient-to-br from-blue-950 to-teal-950 text-white py-4 shadow-md rounded-md">
            <tr>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-24">
                ID
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-28">
                Total de acuerdos
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-32">
                Atendido sin acuerdos
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-32">
                Fecha
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-28">
                Tema
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-28">
                Objetivo
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-32">
                Lugar
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-32">
                Horario
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-32">
                Responsable
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-32">
                Corresponsables
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-32">
                Asistentes
              </th>
              {/* <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-32">
                Anexos
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-32">
                Acciones
              </th> */}
            </tr>
          </thead>
          <tbody>
            {reunionesPaginadas.map((reunion) => {
              const responsables = reunion.responsables || [];
              const numResp = Math.max(1, responsables.length);

              return (
                <React.Fragment key={reunion.idasunto}>
                  <tr>
                    {/* 1. ID */}
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1"
                    >
                      {reunion.idconsecutivo}
                      <br />
                      <span className="text-xs">
                        {getEstatusLabel(reunion.estatus)}
                      </span>
                    </td>
                    {/* 2. Total Acuerdos (pendiente integrar conteo) */}
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1"
                    >
                      {reunion.accionesRealizadas || "-"}
                    </td>
                    {/* 3. Atendido sin acuerdos */}
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1"
                    >
                      {reunion.estatus === "A" && !reunion.accionesRealizadas
                        ? "Atendida sin acuerdos"
                        : "-"}
                    </td>
                    {/* 4. Fecha */}
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1"
                    >
                      {reunion.fechaingreso}
                    </td>
                    {/* 5. Tema */}
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 p-2"
                    >
                      {reunion.descripcion}
                    </td>
                    {/* 6. Objetivo */}
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 p-2"
                    >
                      {reunion.estatustexto}
                    </td>
                    {/* 7. Lugar */}
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1"
                    >
                      {reunion.lugar || "-"}
                    </td>
                    {/* 8. Horario */}
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1"
                    >
                      {reunion.informesunidad || "-"}
                    </td>
                    {/* 9. Responsable (primer responsable) */}
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1"
                    >
                      {responsables[0]?.siglas || "-"}
                    </td>
                    {/* 10. Corresponsables (responsables restantes) */}
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1"
                    >
                      {responsables
                        .slice(1)
                        .map((r) => r.siglas)
                        .join(", ") || "-"}
                    </td>
                    {/* 11. Asistentes */}
                    <td
                      rowSpan={numResp}
                      className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 p-2"
                    >
                      {reunion.asistentes || "-"}
                    </td>
                  </tr>
                </React.Fragment>
              );
            })}
          </tbody>
        </table>
      </div>

      {/* Controles de paginación inferior */}
      <PaginacionNavegacion
        paginaActual={paginaActual}
        totalPaginas={totalPaginas}
        onCambiarPagina={setPaginaActual}
      />

      {/* Botón flotante de scroll */}
      <ScrollButton />
    </div>
  );
};

export default Reuniones;
