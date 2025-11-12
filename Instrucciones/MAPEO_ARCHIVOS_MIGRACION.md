# 🗂️ MAPEO EXACTO DE ARCHIVOS: JSP → REACT

## 📋 **CORRESPONDENCIA DIRECTA**

### 🎯 **COMPONENTE PRINCIPAL: ConsultaAsuntosGeneral**

| **Archivo Original** | **Archivo React** | **Líneas Clave** | **Función** |
|---------------------|-------------------|------------------|-------------|
| `consultaAsuntosGeneral.jsp` | `ConsultaAsuntosGeneral.jsx` | 41-83 | Formulario de filtros |
| `consultaAsuntosGeneral.jsp` | `TablaResultados.jsx` | 96-102 | Tabla de resultados |
| `BusquedaGeneral.java` | `/api/busqueda-general` | 22-45 | Servlet principal |
| `DelegadoNegocio.java` | `asuntosQueries.js` | 672-701 | Lógica de negocio |

### 🗄️ **CAPA DE DATOS**

| **Archivo Original** | **Archivo React** | **Método Específico** | **Query SQL** |
|---------------------|-------------------|----------------------|---------------|
| `AsuntoDAO.java` | `contarAsuntos()` | `cantidadAsuntosxAreaxTipo()` línea 1465 | COUNT para K,C,M |
| `AsuntoDAO.java` | `contarReuniones()` | `cantidadAsuntosReunion()` línea 829 | COUNT para R |
| `AccionDAO.java` | `contarAcuerdos()` | `cantidadAccionesFiltro()` línea 291 | COUNT para A |
| `FachadaDAO.java` | `database/connection.js` | `getConnection()` línea 15 | Conexión DB |

### 🧭 **NAVEGACIÓN Y RUTEO**

| **Servlet Original** | **Componente React** | **Ruta** | **Tipo Asunto** |
|---------------------|---------------------|-----------|-----------------|
| `ConsultaTurnoKEET.java` | `ConsultaSIA.jsx` | `/consulta-sia` | K (SIA) |
| `ConsultaCorreo.java` | `ConsultaCorreos.jsx` | `/consulta-correos` | C (CORREOS) |
| `ConsultaComision.java` | `ConsultaComisiones.jsx` | `/consulta-comisiones` | M (COMISIONES) |
| `ConsultaReunion.java` | `ConsultaReuniones.jsx` | `/consulta-reuniones` | R (REUNIONES) |
| `ConsultaAcuerdos.java` | `ConsultaAcuerdos.jsx` | `/consulta-acuerdos` | A (ACUERDOS) |
| `RuteaConsultaAsuntos.java` | `React Router` | Todas las rutas | Ruteo general |

### 🎨 **ELEMENTOS UI/UX**

| **JSP/Bootstrap** | **React/Tailwind** | **Elemento** |
|------------------|-------------------|--------------|
| `class="form-control"` | `className="border border-gray-300 rounded px-3 py-2"` | Inputs |
| `class="table table-bordered table-striped"` | `className="min-w-full border-collapse border border-gray-300"` | Tabla |
| `class="btn btn-outline-primary"` | `className="bg-blue-500 hover:bg-blue-700 text-white px-4 py-2 rounded"` | Botones |
| `onclick="javascript:abre()"`| `onClick={() => onClickTipo(item.tipoAsunto)}` | Eventos click |

### 📊 **DATOS Y FILTROS**

| **Campo JSP** | **Campo React** | **Tipo** | **Validación** |
|---------------|----------------|----------|----------------|
| `fechas` (select) | `filtros.fechas` | string | Requerido |
| `fecha1` (input) | `filtros.fecha1` | date | DD/MM/YYYY |
| `fecha2` (input) | `filtros.fecha2` | date | DD/MM/YYYY |
| `areaFiltro` (select) | `filtros.areaFiltro` | number | 0 = todas |
| `texto` (input) | `filtros.texto` | string | Opcional |

---

## 🔍 **REFERENCIAS ESPECÍFICAS POR ARCHIVO**

### **consultaAsuntosGeneral.jsp → ConsultaAsuntosGeneral.jsx**

```javascript
// MIGRAR ESTAS LÍNEAS ESPECÍFICAS:

// Línea 41-48: Formulario de filtros
<form name="busquedaGeneral" action="busquedaGeneral.do" method="post">

// Línea 62-65: Select de fechas  
<select name="fechas" class="form-control">
  <option value="fechaingreso">Fecha ingreso</option>
  <option value="fechaatencion">Fecha atención</option>
</select>

// Línea 96-102: Tabla de resultados
<table class="table table-bordered table-striped">
  <tr><td>Tipo Asunto</td><td>Registros</td></tr>
  // Loop de 5 elementos
</table>
```

### **BusquedaGeneral.java → /api/busqueda-general**

```javascript
// MIGRAR ESTA LÓGICA (líneas 22-45):

// Recepción de parámetros
String fechas = request.getParameter("fechas");
String fecha1 = request.getParameter("fecha1");
String fecha2 = request.getParameter("fecha2");
String areaFiltro = request.getParameter("areaFiltro");
String texto = request.getParameter("texto");

// Llamada a DelegadoNegocio
DelegadoNegocio dn = new DelegadoNegocio();
ArrayList<ElementoBusqueda> listaElementos = dn.buscarAsuntos(filtros);

// Respuesta
request.setAttribute("listaElementos", listaElementos);
```

### **DelegadoNegocio.java → asuntosQueries.js**

```javascript
// MIGRAR MÉTODO buscarAsuntos() (líneas 672-701):

public ArrayList<ElementoBusqueda> buscarAsuntos(FiltroAsunto filtro) {
    ArrayList<ElementoBusqueda> lista = new ArrayList<>();
    
    // SIA
    ElementoBusqueda eb1 = new ElementoBusqueda();
    eb1.setTipoAsunto("K");
    eb1.setDescripcion("SIA");
    eb1.setCantidad(fDao.cantidadAsuntosxAreaxTipo("K", filtro));
    
    // CORREOS  
    ElementoBusqueda eb2 = new ElementoBusqueda();
    eb2.setTipoAsunto("C");
    eb2.setDescripcion("CORREOS");
    eb2.setCantidad(fDao.cantidadAsuntosxAreaxTipo("C", filtro));
    
    // ... continúa para M, R, A
    
    return lista;
}
```

---

## 🎯 **INSTRUCCIONES PARA LA IA**

### **PASO 1**: Lee estos archivos originales para entender la lógica
- `consultaAsuntosGeneral.jsp` (líneas 41-102)
- `BusquedaGeneral.java` (líneas 22-45)  
- `DelegadoNegocio.java` (líneas 672-701)

### **PASO 2**: Replica exactamente esta estructura:
1. **Formulario** con 5 campos (fechas, fecha1, fecha2, areaFiltro, texto)
2. **Tabla** con exactamente 5 filas fijas (K,C,M,R,A)
3. **API** que ejecute 5 consultas COUNT diferentes
4. **Navegación** a 5 rutas específicas

### **PASO 3**: Usa estos datos de prueba para validar:
```javascript
const datosEsperados = [
  { tipoAsunto: 'K', descripcion: 'SIA', cantidad: 145 },
  { tipoAsunto: 'C', descripcion: 'CORREOS', cantidad: 289 },
  { tipoAsunto: 'M', descripcion: 'COMISIONES', cantidad: 67 },
  { tipoAsunto: 'R', descripcion: 'REUNIONES', cantidad: 34 },
  { tipoAsunto: 'A', descripcion: 'ACUERDOS', cantidad: 512 }
];
```

### **PASO 4**: Verifica que cada número sea clickeable y navegue correctamente

---

## ⚠️ **PUNTOS CRÍTICOS**

1. **NO CAMBIES** los códigos K,C,M,R,A - son fijos en el sistema
2. **NO USES** consultas genéricas - cada tipo tiene lógica específica  
3. **SÍ MANTÉN** el orden exacto: SIA, CORREOS, COMISIONES, REUNIONES, ACUERDOS
4. **SÍ APLICA** todos los filtros de fecha, área y texto
5. **SÍ CONECTA** directamente con PostgreSQL (no APIs externas)

---

**📚 DOCUMENTACIÓN COMPLETA**: Ver `DOCUMENTACION_TablaResultados.md` para arquitectura detallada

*Mapeo creado basándose en análisis línea por línea del sistema original*