import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useTheme } from "../context/ThemeContext";
import useConsultaGeneral from "../hooks/useConsultaGeneral";
import FormularioFiltros from "../components/consulta/FormularioFiltros";
import TablaResultados from "../components/consulta/TablaResultados";

/**
 * Componente SIA (Sistema Integral de Asuntos)
 * Equivalente a consultaAsuntosGeneral.jsp
 * Permite realizar búsquedas generales de asuntos con filtros
 */
const SIA = () => {
  const { isDarkMode } = useTheme();
  const navigate = useNavigate();

  // Hook personalizado para manejar la consulta general
  const {
    loading,
    error,
    resultadosBusqueda,
    areas,
    realizarBusqueda,
    cargarAreas,
    navegarAConsultaEspecifica,
  } = useConsultaGeneral();

  const [mostrarResultados, setMostrarResultados] = useState(false);

  // Cargar áreas al montar el componente
  useEffect(() => {
    cargarAreas();
  }, [cargarAreas]);

  /**
   * Maneja la búsqueda desde el formulario
   */
  const handleBuscar = async (filtros) => {
    console.log("🔍 Iniciando búsqueda con filtros:", filtros);

    try {
      await realizarBusqueda(filtros);
      setMostrarResultados(true);
    } catch (error) {
      console.error("❌ Error en búsqueda:", error);
      setMostrarResultados(false);
    }
  };

  /**
   * Maneja el clic en un tipo de asunto de la tabla
   */
  const handleClickTipoAsunto = (tipoAsunto, resultado) => {
    console.log(`🚀 Clic en tipo asunto: ${tipoAsunto}`, resultado);

    // Obtener ruta de navegación
    const ruta = navegarAConsultaEspecifica(tipoAsunto);

    if (ruta) {
      // Por ahora, mostrar alerta (más tarde implementar navegación)
      alert(
        `Navegando a consulta específica de ${resultado.descripcion}\nRuta: ${ruta}\nRegistros: ${resultado.cantidad}`
      );

      // TODO: Implementar navegación real cuando estén los componentes específicos
      // navigate(ruta, {
      //   state: {
      //     tipoAsunto,
      //     descripcion: resultado.descripcion,
      //     filtrosOriginales: filtros
      //   }
      // });
    }
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
            Consulta general de asuntos del sistema. Utiliza los filtros para
            buscar información específica.
          </p>
        </div>
      </div>
    </div>
  );
};

export default SIA;
