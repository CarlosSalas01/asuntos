import { useTheme } from "../context/ThemeContext";

const SIA = () => {
  const { isDarkMode } = useTheme();

  return (
    <div
      className={`SIA ${
        isDarkMode ? "dark" : "light"
      } <div className="p-8 text-center min-h-full">`}
    >
      <div className="p-8 text-center min-h-full">
        <p className="text-lg text-zinc-400 font-bold">
          Contenido de la página SIA
        </p>
      </div>
    </div>
  );
};

export default SIA;
