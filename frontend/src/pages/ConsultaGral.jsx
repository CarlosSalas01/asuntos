import React from "react";
import { useTheme } from "../context/ThemeContext.jsx";

const ConsultaGral = ({ className }) => {
  const { isDarkMode } = useTheme();

  return (
    <div className={`h-full w-full p-3 overflow-hidden ${className}`}>
      <div className="rounded-full bg-gradient-to-br from-blue-950 to-teal-950 text-white font-bold py-2 px-6 mb-4 w-fit ml-0">
        <h1 className="text-lg font-bold my-0">Consulta general de asuntos</h1>
      </div>
      <div className="my-7"></div>
    </div>
  );
};

export default ConsultaGral;
