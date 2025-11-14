import React, { useState, memo } from "react";
import { useTheme } from "../context/ThemeContext";
import { useDashboardAPIExterna } from "../hooks/useDashboardAPIExterna";
import GeneralStats from "../components/dashboard/EstadisticasCards";
import AreaCard from "../components/dashboard/AreaCard";
import PendientesModal from "../components/dashboard/PendientesModal";

const Home = memo(() => {
  const { isDarkMode } = useTheme();
  const [modalData, setModalData] = useState(null);
  const [showModal, setShowModal] = useState(false);

  // Memoizar usuario para evitar re-renderizados innecesarios
  const [user] = useState(() => {
    try {
      return JSON.parse(localStorage.getItem("user") || "{}");
    } catch {
      return {};
    }
  });

  const {
    loading,
    error,
    dashboardData,
    consultarPendientes,
    formatearNumero,
  } = useDashboardAPIExterna(user);

  const handleConsultarPendientes = async (idarea) => {
    const data = await consultarPendientes(idarea);
    setModalData(data);
    setShowModal(true);
  };

  if (loading) {
    return (
      <div className="flex flex-col justify-center items-center min-h-96 p-8">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mb-4"></div>
        <p className="text-gray-600 dark:text-gray-300">
          Cargando dashboard...
        </p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="p-8 text-center">
        <div className="bg-red-100 dark:bg-red-900 border border-red-400 text-red-700 dark:text-red-200 px-6 py-4 rounded-lg">
          <h3 className="font-bold mb-2">Error en el Dashboard</h3>
          <p className="mb-4">{error}</p>
          <button
            onClick={() => window.location.reload()}
            className="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700 transition-colors"
          >
            Reintentar
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="p-8 min-h-full">
      <div className="max-w-7xl mx-auto">
        {/* Encabezado con fecha */}
        {dashboardData.fechaHora && (
          <div className="mb-8 text-center">
            <p className="text-lg text-gray-600 dark:text-gray-300">
              <strong>Fecha de corte:</strong> {dashboardData.fechaHora}
            </p>
          </div>
        )}

        {/* Estadísticas Generales */}
        <GeneralStats
          totales={dashboardData.totales}
          formatearNumero={formatearNumero}
        />

        {/* Tarjetas por Área */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          {dashboardData.areas.map((area) => (
            <AreaCard
              key={area.id}
              area={area}
              onConsultarPendientes={handleConsultarPendientes}
              formatearNumero={formatearNumero}
            />
          ))}
        </div>

        {/* Modal para Pendientes */}
        <PendientesModal
          isOpen={showModal}
          onClose={() => setShowModal(false)}
          modalData={modalData}
        />
      </div>
    </div>
  );
});

Home.displayName = "Home";

export default Home;
