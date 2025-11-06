import React from "react";
import { useTheme } from "../context/ThemeContext.jsx";

const Reuniones = () => {
  const { isDarkMode } = useTheme();

  return (
    <div
      className={`p-8 text-center min-h-full ${isDarkMode ? "dark" : "light"}`}
    >
      <p className="text-lg text-zinc-400 font-bold">
        Contenido de la página Reuniones
      </p>
    </div>
  );
};

export default Reuniones;
