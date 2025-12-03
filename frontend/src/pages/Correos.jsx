import React from "react";
import { useTheme } from "../context/ThemeContext.jsx";

const Correos = () => {
  const { isDarkMode } = useTheme();

  return (
    <div className={`Correos ${isDarkMode ? "dark" : "light"} `}>
      <div className="rounded-full bg-gradient-to-br from-blue-950 to-teal-950 text-white font-bold py-2 px-6 mb-4 w-fit ml-0">
        <h1 className="text-lg font-bold my-0">Correos</h1>
      </div>
      <div className="p-8 text-center min-h-full">
        <p className="text-lg text-zinc-400 font-bold">
          Contenido de la página Correos
        </p>
      </div>
    </div>
  );
};

export default Correos;
