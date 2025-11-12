# 🚀 GUÍA DE MIGRACIÓN: JSP/Servlet → Node.js/React/Tailwind

**Sistema**: Sistema de Seguimiento de Asuntos UGMA - INEGI  
**Migración**: Java/JSP → Node.js/React/Tailwind CSS  
**Fecha**: 12 de noviembre de 2025

---

## 🎯 COMPONENTE PRIORITARIO: TablaResultados

### 📋 **CONTEXTO TÉCNICO ACTUAL**

**Flujo original identificado**:
```
consultaAsuntosGeneral.jsp → busquedaGeneral.do → DelegadoNegocio → FachadaDAO → PostgreSQL
```

**Datos que maneja**:
```javascript
// Estructura de cada elemento de búsqueda
{
  tipoAsunto: "K|C|M|R|A",      // SIA|CORREOS|COMISIONES|REUNIONES|ACUERDOS
  descripcion: "SIA",            // Nombre mostrado
  cantidad: 145                  // Número de registros
}
```

---

## 🔧 **INSTRUCCIONES PRECISAS PARA LA IA**

### 1️⃣ **COMPONENTE REACT: ConsultaAsuntosGeneral**

**Crear**: `src/components/ConsultaAsuntosGeneral.jsx`

```jsx
// REQUERIMIENTO: Formulario de filtros + Tabla de resultados
// BASADO EN: consultaAsuntosGeneral.jsp (líneas 41-83)

const ConsultaAsuntosGeneral = () => {
  const [filtros, setFiltros] = useState({
    fechas: '',           // Tipo de fecha (ingreso, atención, etc.)
    fecha1: '',           // Fecha inicio (formato: DD/MM/YYYY)
    fecha2: '',           // Fecha fin (formato: DD/MM/YYYY)
    areaFiltro: 0,        // ID del área (0 = todas)
    texto: ''             // Búsqueda libre
  });
  
  const [resultadosBusqueda, setResultadosBusqueda] = useState([]);
  
  // LÓGICA: Reemplaza el servlet BusquedaGeneral.java
  // ENDPOINT: POST /api/busqueda-general
}
```

**Estilos Tailwind requeridos**:
- Formulario: `form-control` → `border border-gray-300 rounded px-3 py-2`
- Tabla: `table table-bordered table-striped` → `min-w-full border-collapse border border-gray-300`
- Botón: `btn btn-outline-primary` → `bg-blue-500 hover:bg-blue-700 text-white px-4 py-2 rounded`

### 2️⃣ **COMPONENTE: TablaResultados**

**Crear**: `src/components/TablaResultados.jsx`

```jsx
// REQUERIMIENTO: Tabla que muestra 5 tipos de asuntos con sus cantidades
// BASADO EN: JSP líneas 96-102 + DelegadoNegocio.java línea 672

const TablaResultados = ({ datos, onClickTipo }) => {
  // DATOS ESPERADOS: Array de 5 elementos fijos:
  // [
  //   { tipoAsunto: 'K', descripcion: 'SIA', cantidad: 145 },
  //   { tipoAsunto: 'C', descripcion: 'CORREOS', cantidad: 289 },
  //   { tipoAsunto: 'M', descripcion: 'COMISIONES', cantidad: 67 },
  //   { tipoAsunto: 'R', descripcion: 'REUNIONES', cantidad: 34 },
  //   { tipoAsunto: 'A', descripcion: 'ACUERDOS', cantidad: 512 }
  // ]
  
  return (
    <table className="min-w-full border-collapse border border-gray-300">
      <thead>
        <tr className="bg-gray-100">
          <td className="border border-gray-300 px-4 py-2">Tipo Asunto</td>
          <td className="border border-gray-300 px-4 py-2">Registros</td>
        </tr>
      </thead>
      <tbody>
        {datos.map((item) => (
          <tr key={item.tipoAsunto}>
            <td className="border border-gray-300 px-4 py-2">{item.descripcion}</td>
            <td className="border border-gray-300 px-4 py-2 text-center">
              <button 
                onClick={() => onClickTipo(item.tipoAsunto)}
                className="text-blue-600 hover:text-blue-800 underline"
              >
                {item.cantidad}
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};
```

### 3️⃣ **API ENDPOINT: /api/busqueda-general**

**Crear**: `src/routes/busquedaGeneral.js`

```javascript
// REQUERIMIENTO: Reemplaza BusquedaGeneral.java
// LÓGICA: Recibe filtros → Ejecuta 5 consultas COUNT → Devuelve resultados

app.post('/api/busqueda-general', async (req, res) => {
  const { fechas, fecha1, fecha2, areaFiltro, texto } = req.body;
  
  try {
    // CONSULTAS BASADAS EN: DelegadoNegocio.java líneas 677-701
    const resultados = [
      {
        tipoAsunto: 'K',
        descripcion: 'SIA',
        cantidad: await contarAsuntos('K', filtros) // AsuntoDAO línea 1465
      },
      {
        tipoAsunto: 'C', 
        descripcion: 'CORREOS',
        cantidad: await contarAsuntos('C', filtros)
      },
      {
        tipoAsunto: 'M',
        descripcion: 'COMISIONES', 
        cantidad: await contarAsuntos('M', filtros)
      },
      {
        tipoAsunto: 'R',
        descripcion: 'REUNIONES',
        cantidad: await contarReuniones(filtros) // AsuntoDAO línea 829
      },
      {
        tipoAsunto: 'A',
        descripcion: 'ACUERDOS', 
        cantidad: await contarAcuerdos(filtros) // AccionDAO línea 291
      }
    ];
    
    res.json(resultados);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});
```

### 4️⃣ **CONSULTAS SQL PARA Node.js**

**Crear**: `src/database/asuntosQueries.js`

```javascript
// BASADO EN: AsuntoDAO.java líneas 1465, 829 + AccionDAO.java línea 291

const contarAsuntos = async (tipo, filtros) => {
  // BASADO EN: AsuntoDAO.cantidadAsuntosxAreaxTipo()
  const query = `
    SELECT COUNT(*) as cantidad 
    FROM controlasuntospendientesnew.asunto 
    WHERE tipoasunto = $1
      AND fechaingreso BETWEEN $2 AND $3
      ${filtros.areaFiltro > 0 ? 'AND idarea = $4' : ''}
      ${filtros.texto ? 'AND (asunto ILIKE $5 OR descripcion ILIKE $5)' : ''}
  `;
  
  // PARÁMETROS: [tipo, fecha1, fecha2, areaFiltro?, texto?]
  const result = await db.query(query, params);
  return result.rows[0].cantidad;
};

const contarReuniones = async (filtros) => {
  // BASADO EN: AsuntoDAO.cantidadAsuntosReunion()
  const query = `
    SELECT COUNT(*) as cantidad 
    FROM controlasuntospendientesnew.asunto 
    WHERE tipoasunto = 'R'
      AND fechaingreso BETWEEN $1 AND $2
      ${filtros.areaFiltro > 0 ? 'AND idarea = $3' : ''}
  `;
  
  const result = await db.query(query, params);
  return result.rows[0].cantidad;
};

const contarAcuerdos = async (filtros) => {
  // BASADO EN: AccionDAO.cantidadAccionesFiltro()
  const query = `
    SELECT COUNT(*) as cantidad 
    FROM controlasuntospendientesnew.accion 
    WHERE fechaaccion BETWEEN $1 AND $2
      ${filtros.areaFiltro > 0 ? 'AND idarea = $3' : ''}
  `;
  
  const result = await db.query(query, params);
  return result.rows[0].cantidad;
};
```

### 5️⃣ **NAVEGACIÓN: Ruteo a consultas específicas**

**REQUERIMIENTO**: Cuando usuario hace click en número de la tabla

```javascript
// BASADO EN: RuteaConsultaAsuntos.java
const manejarClickTipo = (tipoAsunto) => {
  const rutas = {
    'K': '/consulta-sia',      // ConsultaTurnoKEET.java
    'C': '/consulta-correos',  // ConsultaCorreo.java  
    'M': '/consulta-comisiones', // ConsultaComision.java
    'R': '/consulta-reuniones',  // ConsultaReunion.java
    'A': '/consulta-acuerdos'    // ConsultaAcuerdos.java
  };
  
  navigate(rutas[tipoAsunto]);
};
```

---

## 🎯 **INSTRUCCIONES ESPECÍFICAS PARA LA IA**

### ✅ **LO QUE DEBE HACER**:

1. **Crear exactamente 5 elementos** en la tabla (no más, no menos)
2. **Usar los códigos exactos**: K, C, M, R, A
3. **Mantener las descripciones**: SIA, CORREOS, COMISIONES, REUNIONES, ACUERDOS
4. **Implementar 3 consultas SQL diferentes**:
   - `contarAsuntos()` para K, C, M
   - `contarReuniones()` para R  
   - `contarAcuerdos()` para A
5. **Filtros obligatorios**: fechas, área, texto libre
6. **Links clickeables** en los números que naveguen a rutas específicas

### ❌ **LO QUE NO DEBE HACER**:

1. **No usar** SOLR ni APIs externas (datos vienen directo de PostgreSQL)
2. **No crear** consultas genéricas (cada tipo tiene lógica específica)
3. **No omitir** el filtrado por fechas y áreas
4. **No hardcodear** números (deben venir de BD real)

### 🗄️ **TABLAS DE BASE DE DATOS**:

- **Principal**: `controlasuntospendientesnew.asunto`
- **Acuerdos**: `controlasuntospendientesnew.accion`  
- **Campos clave**: `tipoasunto`, `fechaingreso`, `idarea`, `asunto`, `descripcion`

### 📊 **DATOS DE REFERENCIA** (Nov 2025):
```javascript
// Estos son los números reales actuales para validar
const datosPrueba = [
  { tipoAsunto: 'K', descripcion: 'SIA', cantidad: 145 },
  { tipoAsunto: 'C', descripcion: 'CORREOS', cantidad: 289 },
  { tipoAsunto: 'M', descripcion: 'COMISIONES', cantidad: 67 },
  { tipoAsunto: 'R', descripcion: 'REUNIONES', cantidad: 34 },
  { tipoAsunto: 'A', descripcion: 'ACUERDOS', cantidad: 512 }
];
```

---

## 🔧 **CONFIGURACIÓN DE DESARROLLO**

### **Package.json dependencies**:
```json
{
  "react-router-dom": "^6.0.0",
  "tailwindcss": "^3.0.0", 
  "pg": "^8.0.0",
  "express": "^4.0.0"
}
```

### **Estructura de carpetas sugerida**:
```
src/
├── components/
│   ├── ConsultaAsuntosGeneral.jsx
│   └── TablaResultados.jsx
├── database/
│   └── asuntosQueries.js
├── routes/
│   └── busquedaGeneral.js
└── pages/
    ├── ConsultaSIA.jsx
    ├── ConsultaCorreos.jsx
    ├── ConsultaComisiones.jsx
    ├── ConsultaReuniones.jsx
    └── ConsultaAcuerdos.jsx
```

---

**🎯 RESULTADO ESPERADO**: Una tabla funcional que muestre exactamente los mismos datos que el sistema JSP original, con filtrado por fechas/áreas, y navegación a consultas específicas por tipo de asunto.

**📚 DOCUMENTACIÓN DE REFERENCIA**: Ver `DOCUMENTACION_TablaResultados.md` para arquitectura completa del sistema original.

---

*Guía creada basándose en análisis completo del sistema JSP/Servlet original*