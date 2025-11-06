import React, { useState, useEffect } from "react";

function Asuntos() {
  const [asuntos, setAsuntos] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Simulamos la carga de datos
    setTimeout(() => {
      setAsuntos([
        {
          id: 1,
          titulo: "Asunto de Ejemplo 1",
          descripcion: "Descripción del primer asunto de ejemplo",
          estado: "Activo",
          fecha: "2024-01-15",
        },
        {
          id: 2,
          titulo: "Asunto de Ejemplo 2",
          descripcion: "Descripción del segundo asunto de ejemplo",
          estado: "Pendiente",
          fecha: "2024-01-16",
        },
        {
          id: 3,
          titulo: "Asunto de Ejemplo 3",
          descripcion: "Descripción del tercer asunto de ejemplo",
          estado: "Cerrado",
          fecha: "2024-01-17",
        },
      ]);
      setLoading(false);
    }, 1000);
  }, []);

  const getEstadoColor = (estado) => {
    switch (estado) {
      case "Activo":
        return "#10b981";
      case "Pendiente":
        return "#f59e0b";
      case "Cerrado":
        return "#6b7280";
      default:
        return "#6b7280";
    }
  };

  if (loading) {
    return (
      <div style={loadingStyle}>
        <p>Cargando asuntos...</p>
      </div>
    );
  }

  return (
    <div style={containerStyle}>
      <div style={headerStyle}>
        <h1 style={titleStyle}>Gestión de Asuntos</h1>
        <button style={buttonStyle}>+ Nuevo Asunto</button>
      </div>

      <div style={tableContainerStyle}>
        <table style={tableStyle}>
          <thead>
            <tr style={headerRowStyle}>
              <th style={thStyle}>ID</th>
              <th style={thStyle}>Título</th>
              <th style={thStyle}>Descripción</th>
              <th style={thStyle}>Estado</th>
              <th style={thStyle}>Fecha</th>
              <th style={thStyle}>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {asuntos.map((asunto) => (
              <tr key={asunto.id} style={rowStyle}>
                <td style={tdStyle}>{asunto.id}</td>
                <td style={tdStyle}>{asunto.titulo}</td>
                <td style={tdStyle}>{asunto.descripcion}</td>
                <td style={tdStyle}>
                  <span
                    style={{
                      ...estadoStyle,
                      backgroundColor: getEstadoColor(asunto.estado),
                    }}
                  >
                    {asunto.estado}
                  </span>
                </td>
                <td style={tdStyle}>{asunto.fecha}</td>
                <td style={tdStyle}>
                  <button style={actionButtonStyle}>Ver</button>
                  <button style={{ ...actionButtonStyle, ...editButtonStyle }}>
                    Editar
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

const containerStyle = {
  padding: "2rem",
};

const headerStyle = {
  display: "flex",
  justifyContent: "space-between",
  alignItems: "center",
  marginBottom: "2rem",
};

const titleStyle = {
  fontSize: "2rem",
  color: "#1f2937",
};

const buttonStyle = {
  backgroundColor: "#2563eb",
  color: "white",
  border: "none",
  padding: "0.75rem 1.5rem",
  borderRadius: "6px",
  cursor: "pointer",
  fontSize: "1rem",
  fontWeight: "500",
};

const loadingStyle = {
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
  height: "200px",
  fontSize: "1.2rem",
  color: "#6b7280",
};

const tableContainerStyle = {
  backgroundColor: "white",
  borderRadius: "8px",
  boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)",
  overflow: "hidden",
};

const tableStyle = {
  width: "100%",
  borderCollapse: "collapse",
};

const headerRowStyle = {
  backgroundColor: "#f9fafb",
};

const thStyle = {
  padding: "1rem",
  textAlign: "left",
  fontWeight: "600",
  color: "#374151",
  borderBottom: "1px solid #e5e7eb",
};

const rowStyle = {
  borderBottom: "1px solid #e5e7eb",
};

const tdStyle = {
  padding: "1rem",
  color: "#6b7280",
};

const estadoStyle = {
  padding: "0.25rem 0.75rem",
  borderRadius: "9999px",
  fontSize: "0.875rem",
  fontWeight: "500",
  color: "white",
};

const actionButtonStyle = {
  backgroundColor: "#6b7280",
  color: "white",
  border: "none",
  padding: "0.5rem 1rem",
  borderRadius: "4px",
  cursor: "pointer",
  fontSize: "0.875rem",
  marginRight: "0.5rem",
};

const editButtonStyle = {
  backgroundColor: "#2563eb",
};

export default Asuntos;
