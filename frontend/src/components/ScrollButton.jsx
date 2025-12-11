import React, { useEffect, useState } from "react";
import { RightArrow } from "./icons/CustomIcons.jsx";

/**
 * Botón flotante de scroll que permite ir al inicio o al final de la página.
 * Se muestra automáticamente cuando el usuario hace scroll.
 *
 * @param {string} containerSelector - Selector CSS del contenedor con scroll (default: ".main-content")
 * @param {number} showAfterPx - Cantidad de pixeles de scroll necesarios para mostrar el botón (default: 100)
 */
const ScrollButton = ({
  containerSelector = ".main-content",
  showAfterPx = 100,
}) => {
  const [scrollPercent, setScrollPercent] = useState(0);
  const [mostrarBoton, setMostrarBoton] = useState(false);

  // Función para obtener el contenedor con scroll
  const getScrollContainer = () => {
    const container = document.querySelector(containerSelector);
    if (container && container.scrollHeight > container.clientHeight) {
      return container;
    }
    return document.documentElement;
  };

  // Detectar posición del scroll
  useEffect(() => {
    const handleScroll = () => {
      const container = getScrollContainer();
      const scrollTop =
        container === document.documentElement
          ? window.scrollY
          : container.scrollTop;
      const scrollHeight = container.scrollHeight;
      const clientHeight =
        container === document.documentElement
          ? window.innerHeight
          : container.clientHeight;

      const maxScroll = scrollHeight - clientHeight;
      const percent = maxScroll > 0 ? (scrollTop / maxScroll) * 100 : 0;
      setScrollPercent(percent);

      // Mostrar el botón solo cuando el usuario ha hecho scroll
      setMostrarBoton(scrollTop > showAfterPx);
    };

    // Escuchar scroll en window con capture para detectar scroll en cualquier elemento
    window.addEventListener("scroll", handleScroll, true);
    handleScroll(); // Calcular posición inicial

    return () => window.removeEventListener("scroll", handleScroll, true);
  }, [containerSelector, showAfterPx]);

  // Función para scroll arriba/abajo
  const handleScrollClick = () => {
    const container = getScrollContainer();

    if (scrollPercent <= 50) {
      // Ir al fondo
      if (container === document.documentElement) {
        window.scrollTo({
          top: document.body.scrollHeight,
          behavior: "smooth",
        });
      } else {
        container.scrollTo({ top: container.scrollHeight, behavior: "smooth" });
      }
    } else {
      // Ir arriba
      if (container === document.documentElement) {
        window.scrollTo({ top: 0, behavior: "smooth" });
      } else {
        container.scrollTo({ top: 0, behavior: "smooth" });
      }
    }
  };

  return (
    <button
      onClick={handleScrollClick}
      className={`fixed bottom-6 right-6 z-50 w-12 h-12 rounded-full bg-gradient-to-br from-blue-900 to-teal-900 text-white shadow-lg hover:shadow-xl hover:scale-110 transition-all duration-300 flex items-center justify-center ${
        mostrarBoton
          ? "opacity-100 translate-y-0"
          : "opacity-0 translate-y-10 pointer-events-none"
      }`}
      title={scrollPercent <= 50 ? "Ir al fondo" : "Ir arriba"}
    >
      <RightArrow
        className={`w-6 h-6 transition-transform duration-300 ${
          scrollPercent <= 50 ? "rotate-90" : "-rotate-90"
        }`}
      />
    </button>
  );
};

export default ScrollButton;
