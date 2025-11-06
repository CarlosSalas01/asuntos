/**
 * Componente de estadísticas generales del dashboard
 */

import React from "react";
import {
  InfoIcon,
  CheckIcon,
  TotalIcon,
  GroupIcon,
} from "../icons/CustomIcons";

const StatsCard = ({ icon, title, value, iconColor, formatearNumero }) => {
  return (
    <div className="group relative bg-slate-100 dark:bg-gray-800 p-6 rounded-xl shadow-lg border border-slate-300 dark:border-gray-700 hover:shadow-xl transition-all duration-300 transform hover:-translate-y-1 overflow-hidden">
      {/* Ícono en la esquina inferior derecha */}
      <div
        className={`absolute -right-4 -bottom-4 -rotate-6 size-28 group-hover:-rotate-12 group-hover:scale-125 transition-transform opacity-35 ${iconColor}`}
      >
        {React.cloneElement(icon, { className: "w-full h-full" })}
      </div>

      {/* Layout vertical como en la imagen */}
      <div className="relative z-10">
        <p className="text-gray-700 dark:text-gray-200 font-medium mb-4 text-left">
          {title}
        </p>
        <p className={`text-5xl font-bold ${iconColor} text-left`}>
          {formatearNumero(value)}
        </p>
      </div>
    </div>
  );
};

const GeneralStats = ({ totales, formatearNumero }) => {
  const stats = [
    {
      title: "Total de asuntos",
      value: totales.totalGral,
      iconColor: "text-sky-600 dark:text-sky-400",
      icon: <TotalIcon className="w-10 h-10" />,
    },
    {
      title: "Asuntos atendidos",
      value: totales.totalAtendidos,
      iconColor: "text-green-700 dark:text-green-500",
      icon: <CheckIcon className="w-15 h-15" />,
    },
    {
      title: "Asuntos pendientes",
      value: totales.totalPendientes,
      iconColor: "text-yellow-600 dark:text-yellow-500",
      icon: <InfoIcon className="w-15 h-15" />,
    },
    {
      title: "Reuniones sin acuerdos",
      value: totales.totalReuniones,
      iconColor: "text-indigo-700 dark:text-indigo-500",
      icon: <GroupIcon className="w-9 h-9" />,
    },
  ];

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-12">
      {stats.map((stat, index) => (
        <StatsCard
          key={index}
          icon={stat.icon}
          title={stat.title}
          value={stat.value}
          iconColor={stat.iconColor}
          formatearNumero={formatearNumero}
        />
      ))}
    </div>
  );
};

export default GeneralStats;
