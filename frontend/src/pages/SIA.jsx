import React, { useEffect, useState } from "react";
import axios from "axios";
import { useTheme } from "../context/ThemeContext.jsx";
import { RightArrow } from "../components/icons/CustomIcons.jsx";

const SIA = ({ className = "" }) => {
  const { isDarkMode } = useTheme();
  const [asuntos, setAsuntos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filtroGeneral, setFiltroGeneral] = useState("");
  const [filtroEstatus, setFiltroEstatus] = useState("");
  const [mostrarFiltros, setMostrarFiltros] = useState(false);

  const [idarea, setIdarea] = useState("");
  const [fechaInicio, setFechaInicio] = useState("2025-01-01");
  const [fechaFin, setFechaFin] = useState("2025-12-31");

  useEffect(() => {
    // const token = localStorage.getItem("token");
    axios
      .get("/api/sia/asuntos")
      .then((res) => {
        setAsuntos(res.data);
        setLoading(false);
      })
      .catch((err) => {
        console.error(err);
        setLoading(false);
      });
  }, []);

  // Filtrar asuntos en tiempo real (sin buscar en descripción)
  const asuntosFiltrados = asuntos.filter((asunto) => {
    // Filtro por estatus
    if (filtroEstatus && asunto.estatus !== filtroEstatus) {
      return false;
    }

    // Filtro general
    if (!filtroGeneral.trim()) return true;
    const busqueda = filtroGeneral.trim().toLowerCase();

    const coincide =
      String(asunto.idasunto ?? "")
        .toLowerCase()
        .includes(busqueda) ||
      String(asunto.fechaingreso ?? "")
        .toLowerCase()
        .includes(busqueda) ||
      String(asunto.idconsecutivo ?? "")
        .toLowerCase()
        .includes(busqueda) ||
      String(asunto.siglas ?? "")
        .toLowerCase()
        .includes(busqueda) ||
      String(asunto.estatus ?? "")
        .toLowerCase()
        .includes(busqueda) ||
      String(asunto.delegado ?? "")
        .toLowerCase()
        .includes(busqueda);

    return coincide;
  });

  console.log(
    "Filtro:",
    filtroGeneral,
    "Total:",
    asuntos.length,
    "Filtrados:",
    asuntosFiltrados.length
  );

  if (loading) return <div>Cargando...</div>;
  return (
    <div className={`h-full w-full p-3 overflow-hidden ${className}`}>
      <div className="rounded-full bg-gradient-to-br from-blue-950 to-teal-950 text-white font-bold py-2 px-6 mb-4 w-fit ml-0">
        <h1 className="text-lg font-bold my-0">Sistema Integral de Asuntos</h1>
      </div>
      <div className="my-7"></div>
      <div>
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
          className={`flex gap-11 flex-wrap dark:bg-slate-800 bg-slate-200 border-2 border-dashed dark:border-slate-600 border-slate-400 p-4 rounded-md transition-all duration-300 ease-in-out overflow-hidden ${
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
              id="general"
              placeholder="Ingresa texto"
              className="py-2 px-2 rounded-md shadow-sm text-slate-700"
              value={filtroGeneral}
              onChange={(e) => setFiltroGeneral(e.target.value)}
            />
          </div>
          {/* <div className="flex flex-col flex-1 min-w-[200px]">
            <label className="font-medium text-slate-700 dark:text-slate-200 mb-1">
              Fecha Inicio:
            </label>
            <input type="date" className="py-1 px-2 rounded-md shadow-sm" />
          </div>
          <div className="flex flex-col flex-1 min-w-[200px]">
            <label className="font-medium text-slate-700 dark:text-slate-200 mb-1">
              Fecha Fin:
            </label>
            <input type="date" className="py-1 px-2 rounded-md shadow-sm" />
          </div> */}

          <div className="flex flex-col flex-1 min-w-[200px]">
            <label className="font-medium text-slate-700 dark:text-slate-200 mb-1">
              Estatus
            </label>
            <select
              className="py-2 px-2 rounded-md shadow-sm text-slate-700"
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
      <div className="rounded-sm shadow-sm overflow-hidden">
        <table className="w-full border border-gray-300 table-fixed">
          <thead className="bg-gradient-to-br from-blue-950 to-teal-950 text-white py-4 shadow-md rounded-md">
            <tr>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-24">
                ID Asunto
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-28">
                Fecha Ingreso
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-28">
                Consecutivo
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-24">
                Siglas
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-20">
                Estatus
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700 w-24">
                Delegado
              </th>
              <th className="px-3 py-2 text-base text-center font-semibold text-white border border-gray-600 dark:border-gray-700">
                Descripción
              </th>
            </tr>
          </thead>
          <tbody>
            {asuntosFiltrados.map((asunto, index) => (
              <tr key={`${asunto.idasunto}-${asunto.siglas}-${index}`}>
                <td className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1">
                  {asunto.idasunto}
                </td>
                <td className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1">
                  {asunto.fechaingreso}
                </td>
                <td className="text-center dark:bg-slate-700  bg-gray-200 border border-gray-300 dark:border-gray-800 px-1">
                  {asunto.idconsecutivo}
                </td>
                <td className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1">
                  {asunto.siglas}
                </td>
                <td className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1">
                  {asunto.estatus}
                </td>
                <td className="text-center dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 px-1">
                  {asunto.delegado}
                </td>
                <td className="text-justify dark:bg-slate-700 bg-gray-200 border border-gray-300 dark:border-gray-800 p-2">
                  {asunto.descripcion}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default SIA;
