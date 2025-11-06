import React from "react";
import { useTheme } from "../context/ThemeContext.jsx";

const Reportes = () => {
  const { isDarkMode } = useTheme();

  return (
    <div
      className={`p-8 text-center min-h-full ${isDarkMode ? "dark" : "light"}`}
    >
      <h1 className="text-2xl font-bold dark:text-gray-300">Reportes</h1>
      <p className="mt-4 text-lg text-gray-600 dark:text-gray-400">
        Aquí puedes ver los reportes generados.
      </p>
    </div>
  );
};

export default Reportes;
