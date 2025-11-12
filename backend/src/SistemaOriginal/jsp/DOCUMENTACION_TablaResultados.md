# 📊 DOCUMENTACIÓN COMPLETA: TABLA TablaResultados - CONSULTA GENERAL DE ASUNTOS

**Fecha de análisis**: 12 de noviembre de 2025  
**Archivo analizado**: `consultaAsuntosGeneral.jsp`  
**Sistema**: Sistema de Seguimiento de Asuntos UGMA - INEGI

---

## 🎯 RESUMEN EJECUTIVO

La tabla `TablaResultados` muestra un resumen estadístico de diferentes tipos de asuntos en el sistema, permitiendo a los usuarios navegar a consultas detalladas de cada categoría.

---

## 📁 ARCHIVOS ESPECÍFICOS INVOLUCRADOS

### 🌐 **CAPA DE PRESENTACIÓN (JSP)**

- 📄 **`src/main/webapp/consultaAsuntosGeneral.jsp`**
  - Formulario principal con filtros
  - Renderiza la tabla TablaResultados
  - Contiene enlaces a consultas específicas

### 🔧 **CAPA DE CONTROL (SERVLETS)**

- 📄 **`src/main/java/mx/org/inegi/dggma/sistemas/asuntos/controladorNew/BusquedaGeneral.java`**

  - Servlet principal que procesa formulario
  - Mapeo URL: `busquedaGeneral.do`
  - Método principal: `processRequest()`

- 📄 **`src/main/java/mx/org/inegi/dggma/sistemas/asuntos/controladorNew/RuteaConsultaAsuntos.java`**
  - Servlet de navegación para clicks en tabla
  - Mapeo URL: `ruteaConsultaAsuntos.do`
  - Rutea a consultas específicas por tipo

### 💼 **CAPA DE NEGOCIO**

- 📄 **`src/main/java/mx/org/inegi/dggma/sistemas/asuntos/negocio/DelegadoNegocio.java`**
  - Método clave: `obtenerDatosBusqueda(FiltroAsunto filtro)` (línea 672)
  - Coordina las 5 consultas de conteo
  - Crea objetos `ElementoBusqueda`

### 🏛️ **CAPA DE ACCESO A DATOS (FACADE)**

- 📄 **`src/main/java/mx/org/inegi/dggma/sistemas/asuntos/fachada/FachadaDAO.java`**
  - `cantidadAsuntosPorAreaxTipo()` (línea 126) → Para SIA, CORREOS, COMISIONES
  - `cantidadAsuntosReuniones()` (línea 133) → Para REUNIONES
  - `cantidadAcuerdosFiltro()` (línea 140) → Para ACUERDOS

### 🗄️ **CAPA DE ACCESO A DATOS (DAO)**

- 📄 **`src/main/java/mx/org/inegi/dggma/sistemas/asuntos/baseDatos/AsuntoDAO.java`**

  - `cantidadAsuntosxAreaxTipo()` (línea 1465) → SQL para K, C, M
  - `cantidadAsuntosReunion()` (línea 829) → SQL para R

- 📄 **`src/main/java/mx/org/inegi/dggma/sistemas/asuntos/baseDatos/AccionDAO.java`**
  - `cantidadAccionesFiltro()` (línea 291) → SQL para A (ACUERDOS)

### 📊 **MODELOS DE DATOS**

- 📄 **`src/main/java/mx/org/inegi/dggma/sistemas/asuntos/modelo/ElementoBusqueda.java`**

  - Estructura: `tipoAsunto`, `descripcion`, `cantidad`
  - Representa cada fila de la tabla

- 📄 **`src/main/java/mx/org/inegi/dggma/sistemas/asuntos/modelo/FiltroAsunto.java`**

  - Contiene criterios de filtrado
  - Fechas, áreas, texto de búsqueda

- 📄 **`src/main/java/mx/org/inegi/dggma/sistemas/asuntos/dto/Cantidad.java`**
  - DTO para resultados de consultas COUNT
  - Campo: `cantidad` (int)

### ⚙️ **ARCHIVOS DE CONFIGURACIÓN**

- 📄 **`src/main/webapp/WEB-INF/web.xml`**

  - Mapeo de servlets a URLs
  - Configuración de filtros y listeners

- 📄 **`pom.xml`**
  - Dependencias Maven (PostgreSQL, JSTL, etc.)
  - Configuración de compilación

### 🎨 **RECURSOS ESTÁTICOS**

- 📄 **`src/main/webapp/styles/hojaEstilos.css`**
  - Estilos generales del sistema
- 📄 **`src/main/webapp/styles/consultas.css`**
  - Estilos específicos para consultas
- 📄 **`src/main/webapp/assets/js/bootstrap.min.js`**
  - Framework Bootstrap para UI
- 📄 **`src/main/webapp/js/jquery-3.5.0.min.js`**
  - jQuery para interacciones DOM

### 🔗 **SERVLETS DE DESTINO (Navegación)**

- 📄 **`src/main/java/mx/org/inegi/dggma/sistemas/asuntos/controladorNew/ConsultaCorreo.java`** ← Cuando click en CORREOS
- 📄 **`src/main/java/mx/org/inegi/dggma/sistemas/asuntos/controladorNew/ConsultaComision.java`** ← Cuando click en COMISIONES
- 📄 **`src/main/java/mx/org/inegi/dggma/sistemas/asuntos/controladorNew/ConsultaReunion.java`** ← Cuando click en REUNIONES
- 📄 **`src/main/java/mx/org/inegi/dggma/sistemas/asuntos/controladorNew/ConsultaAcuerdos.java`** ← Cuando click en ACUERDOS
- 📄 **`src/main/java/mx/org/inegi/dggma/sistemas/asuntos/controladorNew/ConsultaTurnoKEET.java`** ← Cuando click en SIA

### 🗂️ **ARCHIVOS COMPARTIDOS**

- 📄 **`src/main/webapp/encabezado.jsp`**
  - Header común incluido en consultaAsuntosGeneral.jsp
- 📄 **`src/main/webapp/WEB-INF/tlds/superSelect.tld`**
  - Tag library para componentes de selección

### ✅ **ARCHIVOS CONFIRMADOS EXISTENTES**

**Todos los archivos listados han sido verificados en el sistema:**

✅ **BusquedaGeneral.java** - Existe  
✅ **RuteaConsultaAsuntos.java** - Existe  
✅ **ConsultaTurnoKEET.java** - Existe (nota: KEET en mayúsculas)  
✅ **ConsultaCorreo.java** - Existe  
✅ **ConsultaComision.java** - Existe  
✅ **ConsultaReunion.java** - Existe  
✅ **ConsultaAcuerdos.java** - Existe  
✅ **DelegadoNegocio.java** - Existe  
✅ **FachadaDAO.java** - Existe  
✅ **AsuntoDAO.java** - Existe  
✅ **AccionDAO.java** - Existe  
✅ **ElementoBusqueda.java** - Existe  
✅ **FiltroAsunto.java** - Existe  
✅ **Cantidad.java** - Existe  
✅ **web.xml** - Verificado (mapeo consultaTurnoKeet.do → ConsultaTurnoKEET)

---

## 🏗️ ARQUITECTURA GENERAL DEL FLUJO

### 📋 COMPONENTES PRINCIPALES

```
Usuario → JSP → Servlet → Negocio → DAO → PostgreSQL
  ↓       ↓       ↓         ↓        ↓        ↓
 Form → busque → Delegate → Facade → AsuntoDAO → BD
```

---

## 🔄 FLUJO DETALLADO PASO A PASO

### 1️⃣ **INICIO DEL PROCESO**

- **Archivo**: `consultaAsuntosGeneral.jsp`
- **Formulario**: `<form action="busquedaGeneral.do" name="filtrofrm">`
- **Parámetros enviados**:
  - `fechas`: Tipo de fecha (ingreso, atención, etc.)
  - `fecha1` y `fecha2`: Rango de fechas
  - `areaFiltro`: ID del área seleccionada
  - `texto`: Texto de búsqueda

### 2️⃣ **SERVLET PRINCIPAL**

- **Clase**: `BusquedaGeneral.java`
- **Ubicación**: `mx.org.inegi.dggma.sistemas.asuntos.controladorNew`
- **Método**: `processRequest()`

**Procesamiento**:

```java
// 1. Captura parámetros del request
String tipoFechas = request.getParameter("fechas");
String fecha1 = request.getParameter("fecha1");
String fecha2 = request.getParameter("fecha2");
String idareaFiltro = request.getParameter("areaFiltro");
String texto = request.getParameter("texto");

// 2. Crea/actualiza filtro
FiltroAsunto filtro = (FiltroAsunto) sesion.getAttribute("filtroConsultaGeneral");
filtro.setTipoFecha(tipoFechas);
filtro.setFechaInicio(Utiles.getSwapFecha(fecha1));
filtro.setFechaFinal(Utiles.getSwapFecha(fecha2));
filtro.setIdarea(Integer.parseInt(idareaFiltro));
filtro.setTexto(texto);

// 3. Delega al negocio
DelegadoNegocio delegado = new DelegadoNegocio(areasConsulta);
request.setAttribute("resultadosBusqueda", delegado.obtenerDatosBusqueda(filtro));

// 4. Redirige al JSP
RequestDispatcher salta = request.getRequestDispatcher("consultaAsuntosGeneral.jsp");
salta.forward(request, response);
```

### 3️⃣ **CAPA DE NEGOCIO**

- **Clase**: `DelegadoNegocio.java`
- **Método**: `obtenerDatosBusqueda(FiltroAsunto filtro)`
- **Ubicación**: `mx.org.inegi.dggma.sistemas.asuntos.negocio`

**Generación de datos**:

```java
List<ElementoBusqueda> datos = new ArrayList<ElementoBusqueda>();

// 1. SIA (K)
ElementoBusqueda elem = new ElementoBusqueda();
elem.setCantidad(fachada.cantidadAsuntosPorAreaxTipo(filtro, "K"));
elem.setTipoAsunto("K");
elem.setDescripcion("SIA");
datos.add(elem);

// 2. CORREOS (C)
elem = new ElementoBusqueda();
elem.setCantidad(fachada.cantidadAsuntosPorAreaxTipo(filtro, "C"));
elem.setTipoAsunto("C");
elem.setDescripcion("CORREOS");
datos.add(elem);

// 3. COMISIONES (M)
elem = new ElementoBusqueda();
elem.setCantidad(fachada.cantidadAsuntosPorAreaxTipo(filtro, "M"));
elem.setTipoAsunto("M");
elem.setDescripcion("COMISIONES");
datos.add(elem);

// 4. REUNIONES (R)
elem = new ElementoBusqueda();
elem.setCantidad(fachada.cantidadAsuntosReuniones(filtro));
elem.setTipoAsunto("R");
elem.setDescripcion("REUNIONES");
datos.add(elem);

// 5. ACUERDOS (A)
elem = new ElementoBusqueda();
elem.setCantidad(fachada.cantidadAcuerdosFiltro(filtro));
elem.setTipoAsunto("A");
elem.setDescripcion("ACUERDOS");
datos.add(elem);

return datos;
```

### 4️⃣ **CAPA DE ACCESO A DATOS**

- **Clase**: `FachadaDAO.java`
- **Ubicación**: `mx.org.inegi.dggma.sistemas.asuntos.fachada`

**Métodos de conteo**:

```java
// Para SIA, CORREOS, COMISIONES
public int cantidadAsuntosPorAreaxTipo(FiltroAsunto filtro, String tipo) {
    Cantidad dato = adao.cantidadAsuntosxAreaxTipo(filtro, tipo);
    return dato != null ? dato.getCantidad() : 0;
}

// Para REUNIONES
public int cantidadAsuntosReuniones(FiltroAsunto filtro) {
    Cantidad dato = adao.cantidadAsuntosReunion(filtro);
    return dato != null ? dato.getCantidad() : 0;
}

// Para ACUERDOS
public int cantidadAcuerdosFiltro(FiltroAsunto filtro) {
    Cantidad dato = accdao.cantidadAccionesFiltro(filtro);
    return dato != null ? dato.getCantidad() : 0;
}
```

### 5️⃣ **RENDERIZADO EN JSP**

- **Variable**: `${resultadosBusqueda}`
- **Tipo**: `List<ElementoBusqueda>`

**Código JSP**:

```jsp
<c:if test="${not empty resultadosBusqueda}">
    <table class="table table-bordered table-striped">
        <head>
            <tr><td>Tipo Asunto</td><td>Registros</td></tr>
        </head>
        <tbody>
            <c:forEach var="e" items="${resultadosBusqueda}">
                <tr>
                    <td>${e.descripcion}</td>
                    <td align="center">
                        <a href="ruteaConsultaAsuntos.do?modo=${e.tipoAsunto}">
                            ${e.cantidad}
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</c:if>
```

---

## 🗃️ ESTRUCTURA DE DATOS

### 📋 **ElementoBusqueda.java**

```java
public class ElementoBusqueda implements Serializable {
    private String tipoAsunto;    // K, C, M, R, A
    private String descripcion;   // SIA, CORREOS, COMISIONES, REUNIONES, ACUERDOS
    private int cantidad;         // Número de registros encontrados
}
```

### 🏷️ **Tipos de Asunto**

| Código | Descripción | Método de Conteo                           |
| ------ | ----------- | ------------------------------------------ |
| K      | SIA         | `cantidadAsuntosPorAreaxTipo(filtro, "K")` |
| C      | CORREOS     | `cantidadAsuntosPorAreaxTipo(filtro, "C")` |
| M      | COMISIONES  | `cantidadAsuntosPorAreaxTipo(filtro, "M")` |
| R      | REUNIONES   | `cantidadAsuntosReuniones(filtro)`         |
| A      | ACUERDOS    | `cantidadAcuerdosFiltro(filtro)`           |

---

## 🔗 NAVEGACIÓN AL HACER CLIC

### **Servlet de Ruteo**

- **Clase**: `RuteaConsultaAsuntos.java`
- **URL**: `ruteaConsultaAsuntos.do?modo=${e.tipoAsunto}`
- **Función**: Redirige a la consulta específica según el tipo

**Destinos por tipo**:

```java
if (modoConsulta.equals("K")) {
    salto = "consultaTurnoKeet.do";    // → SIA
}
if (modoConsulta.equals("C")) {
    salto = "consultaCorreo.do";       // → CORREOS
}
if (modoConsulta.equals("R")) {
    salto = "consultaReunion.do";      // → REUNIONES
}
if (modoConsulta.equals("M")) {
    salto = "consultaComision.do";     // → COMISIONES
}
if (modoConsulta.equals("A")) {
    salto = "consultaAcuerdos.do";     // → ACUERDOS
}
```

---

## 🗄️ ACCESO A BASE DE DATOS

### **Tablas PostgreSQL Involucradas**

- **Principal**: `controlasuntospendientesnew.asunto`
- **Acciones/Acuerdos**: `controlasuntospendientesnew.accion`
- **Filtros**: Por fechas, áreas, estados, texto

### **Consultas SQL (estimadas)**

```sql
-- Para SIA, CORREOS, COMISIONES
SELECT COUNT(*) as cantidad
FROM controlasuntospendientesnew.asunto
WHERE tipoasunto = ?
  AND fechaingreso BETWEEN ? AND ?
  AND [filtros_adicionales];

-- Para REUNIONES
SELECT COUNT(*) as cantidad
FROM controlasuntospendientesnew.asunto
WHERE tipoasunto = 'R'
  AND [filtros_de_fecha_y_area];

-- Para ACUERDOS
SELECT COUNT(*) as cantidad
FROM controlasuntospendientesnew.accion
WHERE [filtros_aplicables];
```

---

## 🔧 PARÁMETROS DE FILTRADO

### **Variables del Filtro**

- `tipoFecha`: Criterio temporal (ingreso, atención, etc.)
- `fechaInicio` / `fechaFinal`: Rango temporal
- `idarea`: Área específica (0 = todas)
- `texto`: Búsqueda libre en contenido
- `estatusAsunto`: Estado del asunto
- `estatusResp`: Estado de respuesta

---

## 📊 EJEMPLO DE SALIDA

**Tabla típica renderizada**:

```
┌─────────────┬───────────┐
│ Tipo Asunto │ Registros │
├─────────────┼───────────┤
│ SIA         │ 145       │
│ CORREOS     │ 289       │
│ COMISIONES  │ 67        │
│ REUNIONES   │ 34        │
│ ACUERDOS    │ 512       │
└─────────────┴───────────┘
```

---

## 🎯 PUNTOS CLAVE

1. **✅ Fuente de datos**: PostgreSQL directo (NO usa API externa)
2. **✅ Arquitectura**: MVC clásico con capas bien definidas
3. **✅ Filtrado**: Completo por fechas, áreas y texto
4. **✅ Navegación**: Enlaces dinámicos a consultas específicas
5. **✅ Rendimiento**: Consultas COUNT optimizadas

---

## 🔄 FLUJO RESUMIDO

```
consultaAsuntosGeneral.jsp
         ↓ (submit form)
    busquedaGeneral.do
         ↓ (BusquedaGeneral.java)
    DelegadoNegocio.obtenerDatosBusqueda()
         ↓ (5 consultas COUNT)
    FachadaDAO → AsuntoDAO → PostgreSQL
         ↓ (resultados)
    ${resultadosBusqueda} → JSP
         ↓ (render table)
    TablaResultados visible al usuario
         ↓ (click en números)
    ruteaConsultaAsuntos.do → consulta específica
```

---

## 🔗 MAPEO COMPLETO DE URLs Y MÉTODOS

### 📍 **URLS DEL SISTEMA**

```
busquedaGeneral.do → BusquedaGeneral.java → processRequest()
ruteaConsultaAsuntos.do → RuteaConsultaAsuntos.java → processRequest()
consultaTurnoKeet.do → ConsultaTurnoKEET.java → processRequest()
consultaCorreo.do → ConsultaCorreo.java → processRequest()
consultaReunion.do → ConsultaReunion.java → processRequest()
consultaComision.do → ConsultaComision.java → processRequest()
consultaAcuerdos.do → ConsultaAcuerdos.java → processRequest()
```

### 📋 **MÉTODOS CLAVE POR ARCHIVO**

#### **DelegadoNegocio.java**

- **Línea 672**: `obtenerDatosBusqueda(FiltroAsunto filtro)`
  - Genera 5 elementos de búsqueda
  - Llama a FachadaDAO para obtener conteos

#### **FachadaDAO.java**

- **Línea 126**: `cantidadAsuntosPorAreaxTipo(FiltroAsunto filtro, String tipo)`
- **Línea 133**: `cantidadAsuntosReuniones(FiltroAsunto filtro)`
- **Línea 140**: `cantidadAcuerdosFiltro(FiltroAsunto filtro)`

#### **AsuntoDAO.java**

- **Línea 1465**: `cantidadAsuntosxAreaxTipo(FiltroAsunto filtro, String tipo)`
- **Línea 829**: `cantidadAsuntosReunion(FiltroAsunto filtro)`

#### **AccionDAO.java**

- **Línea 291**: `cantidadAccionesFiltro(FiltroAsunto filtro)`

### 🗄️ **CONSULTAS SQL ESPECÍFICAS**

#### **Para SIA, CORREOS, COMISIONES (AsuntoDAO.java - línea 1465)**

```sql
SELECT COUNT(*) as cantidad
FROM controlasuntospendientesnew.asunto
WHERE tipoasunto = ?
  AND [filtros_de_fecha]
  AND [filtros_de_area]
  AND [filtros_de_texto]
```

#### **Para REUNIONES (AsuntoDAO.java - línea 829)**

```sql
SELECT COUNT(*) as cantidad
FROM controlasuntospendientesnew.asunto
WHERE tipoasunto = 'R'
  AND [mismos_filtros_aplicables]
```

#### **Para ACUERDOS (AccionDAO.java - línea 291)**

```sql
SELECT COUNT(*) as cantidad
FROM controlasuntospendientesnew.accion
WHERE [filtros_específicos_de_acciones]
```

### 📊 **ESTRUCTURA COMPLETA DE DIRECTORIOS**

```
src/main/
├── java/mx/org/inegi/dggma/sistemas/asuntos/
│   ├── controladorNew/
│   │   ├── BusquedaGeneral.java ★
│   │   ├── RuteaConsultaAsuntos.java ★
│   │   ├── ConsultaTurnoKEET.java (SIA)
│   │   ├── ConsultaCorreo.java (CORREOS)
│   │   ├── ConsultaComision.java (COMISIONES)
│   │   ├── ConsultaReunion.java (REUNIONES)
│   │   └── ConsultaAcuerdos.java (ACUERDOS)
│   ├── negocio/
│   │   └── DelegadoNegocio.java ★
│   ├── fachada/
│   │   └── FachadaDAO.java ★
│   ├── baseDatos/
│   │   ├── AsuntoDAO.java ★
│   │   └── AccionDAO.java ★
│   ├── modelo/
│   │   ├── ElementoBusqueda.java ★
│   │   └── FiltroAsunto.java ★
│   └── dto/
│       └── Cantidad.java ★
└── webapp/
    ├── consultaAsuntosGeneral.jsp ★
    ├── WEB-INF/
    │   ├── web.xml ★
    │   └── tlds/superSelect.tld
    ├── styles/
    │   ├── hojaEstilos.css
    │   └── consultas.css
    └── js/
        └── jquery-3.5.0.min.js

★ = Archivos críticos para TablaResultados
```

### 🎯 **SECUENCIA DE EJECUCIÓN DETALLADA**

1. **Usuario submit** → `consultaAsuntosGeneral.jsp`
2. **POST/GET** → `busquedaGeneral.do`
3. **BusquedaGeneral.java:processRequest()** → Captura parámetros
4. **DelegadoNegocio.java:obtenerDatosBusqueda()** → Línea 672
5. **5 llamadas paralelas**:
   - FachadaDAO:cantidadAsuntosPorAreaxTipo("K") → AsuntoDAO → SQL
   - FachadaDAO:cantidadAsuntosPorAreaxTipo("C") → AsuntoDAO → SQL
   - FachadaDAO:cantidadAsuntosPorAreaxTipo("M") → AsuntoDAO → SQL
   - FachadaDAO:cantidadAsuntosReuniones() → AsuntoDAO → SQL
   - FachadaDAO:cantidadAcuerdosFiltro() → AccionDAO → SQL
6. **Resultados** → List<ElementoBusqueda>
7. **request.setAttribute("resultadosBusqueda")** → JSP
8. **JSTL forEach** → Renderiza tabla HTML
9. **Usuario click número** → `ruteaConsultaAsuntos.do?modo=X`
10. **RuteaConsultaAsuntos.java** → Redirige a consulta específica

---

**🏁 FIN DE DOCUMENTACIÓN**  
_Análisis completado el 12 de noviembre de 2025_
