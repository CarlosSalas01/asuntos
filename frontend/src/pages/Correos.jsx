import React from "react";
import { useTheme } from "../context/ThemeContext.jsx";

const Correos = () => {
  const { isDarkMode } = useTheme();

  return (
    <div className={`Correos ${isDarkMode ? "dark" : "light"}`}>
      <div className="p-8 text-center min-h-full">
        <p className="text-lg text-zinc-400 font-bold">
          Contenido de la página Correos
        </p>
      </div>
    </div>
  );
};

export default Correos;
