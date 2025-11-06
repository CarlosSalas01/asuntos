import React, { createContext, useContext, useState, useEffect } from "react";

const ThemeContext = createContext();

export const useTheme = () => {
  const context = useContext(ThemeContext);
  if (!context) {
    throw new Error("useTheme must be used within a ThemeProvider");
  }
  return context;
};

export const ThemeProvider = ({ children }) => {
  const [isDarkMode, setIsDarkMode] = useState(() => {
    // Verificar si hay un tema guardado en localStorage
    const savedTheme = localStorage.getItem("theme");
    if (savedTheme) {
      return savedTheme === "dark";
    }
    // Si no hay tema guardado, usar la preferencia del sistema
    return window.matchMedia("(prefers-color-scheme: dark)").matches;
  });

  // Aplicar el tema al cargar el componente
  useEffect(() => {
    if (isDarkMode) {
      document.documentElement.classList.add("dark");
    } else {
      document.documentElement.classList.remove("dark");
    }

    // Guardar la preferencia en localStorage
    localStorage.setItem("theme", isDarkMode ? "dark" : "light");
    console.log(
      "[Theme] Saved to localStorage:",
      isDarkMode ? "dark" : "light"
    );
  }, [isDarkMode]);

  // Función para alternar el tema
  const toggleDarkMode = () => {
    console.log(
      "[Theme] Toggle clicked - Current mode:",
      isDarkMode ? "dark" : "light"
    );
    setIsDarkMode((prevMode) => {
      const newMode = !prevMode;
      return newMode;
    });
  };

  const value = {
    isDarkMode,
    toggleDarkMode,
  };

  return (
    <ThemeContext.Provider value={value}>{children}</ThemeContext.Provider>
  );
};

export default ThemeProvider;
