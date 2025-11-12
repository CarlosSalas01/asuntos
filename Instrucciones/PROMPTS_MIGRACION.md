# 📝 PROMPTS LISTOS PARA LA IA - MIGRACIÓN REACT

## 🎯 **PROMPT 1: Componente Principal**

```
Necesito que crees el componente React ConsultaAsuntosGeneral.jsx basándote en esta especificación:

REQUISITOS:
- Formulario con 4 filtros: fechas (select), fecha1 (input date), fecha2 (input date), areaFiltro (select), texto (input text)
- Tabla TablaResultados que muestre exactamente 5 filas con estos datos:
  * SIA (código K) 
  * CORREOS (código C)
  * COMISIONES (código M) 
  * REUNIONES (código R)
  * ACUERDOS (código A)
- Al hacer click en los números, navegar a rutas específicas
- Usar Tailwind CSS para estilos
- Integrar con API endpoint POST /api/busqueda-general

DATOS ESPERADOS DEL API:
```json
[
  { "tipoAsunto": "K", "descripcion": "SIA", "cantidad": 145 },
  { "tipoAsunto": "C", "descripcion": "CORREOS", "cantidad": 289 },
  { "tipoAsunto": "M", "descripcion": "COMISIONES", "cantidad": 67 },
  { "tipoAsunto": "R", "descripcion": "REUNIONES", "cantidad": 34 },
  { "tipoAsunto": "A", "descripcion": "ACUERDOS", "cantidad": 512 }
]
```

NAVEGACIÓN:
- K → /consulta-sia
- C → /consulta-correos  
- M → /consulta-comisiones
- R → /consulta-reuniones
- A → /consulta-acuerdos

La tabla debe ser responsive y mostrar "Tipo Asunto" y "Registros" como headers.
```

---

## 🎯 **PROMPT 2: API Backend**

```
Crea el endpoint Node.js/Express para /api/busqueda-general que:

FUNCIONALIDAD:
- Reciba filtros por POST: fechas, fecha1, fecha2, areaFiltro, texto
- Ejecute exactamente 5 consultas SQL diferentes a PostgreSQL:
  1. COUNT para SIA (tipoasunto = 'K')
  2. COUNT para CORREOS (tipoasunto = 'C') 
  3. COUNT para COMISIONES (tipoasunto = 'M')
  4. COUNT para REUNIONES (tipoasunto = 'R') - tabla asunto
  5. COUNT para ACUERDOS - tabla accion

CONSULTAS SQL BASE:
```sql
-- Para K, C, M:
SELECT COUNT(*) FROM controlasuntospendientesnew.asunto 
WHERE tipoasunto = ? AND fechaingreso BETWEEN ? AND ?

-- Para R (reuniones):
SELECT COUNT(*) FROM controlasuntospendientesnew.asunto 
WHERE tipoasunto = 'R' AND fechaingreso BETWEEN ? AND ?

-- Para A (acuerdos):
SELECT COUNT(*) FROM controlasuntospendientesnew.accion 
WHERE fechaaccion BETWEEN ? AND ?
```

FILTROS ADICIONALES:
- Si areaFiltro > 0: agregar AND idarea = ?
- Si texto existe: agregar AND (asunto ILIKE %?% OR descripcion ILIKE %?%)

RESPUESTA JSON:
Array de 5 objetos con tipoAsunto, descripcion, cantidad

Usa pg library para PostgreSQL.
```

---

## 🎯 **PROMPT 3: Consultas SQL Optimizadas**

```
Crea el archivo de consultas SQL para Node.js que maneje los 3 tipos diferentes:

ARCHIVO: src/database/asuntosQueries.js

FUNCIONES REQUERIDAS:
1. contarAsuntos(tipo, filtros) - para K, C, M
2. contarReuniones(filtros) - específico para R  
3. contarAcuerdos(filtros) - específico para A

BASE DE DATOS:
- Schema: controlasuntospendientesnew
- Tablas principales: asunto, accion
- Campos clave: tipoasunto, fechaingreso, fechaaccion, idarea, asunto, descripcion

FILTROS A APLICAR:
- Fechas obligatorias (rango)
- Área opcional (si > 0)
- Texto opcional (búsqueda ILIKE en asunto y descripcion)

Debe manejar parámetros dinámicos y prepared statements para seguridad.
Cada función debe retornar solo el número (cantidad).
```

---

## 🎯 **PROMPT 4: Componente TablaResultados**

```
Crea el componente TablaResultados.jsx que:

PROPS:
- datos: Array de 5 elementos con tipoAsunto, descripcion, cantidad
- onClickTipo: Función callback para manejar clicks

DISEÑO TAILWIND:
- Tabla con bordes completos
- Header con fondo gris claro
- Números clickeables (azul, hover más oscuro)
- Responsive design
- Dos columnas: "Tipo Asunto" y "Registros"

COMPORTAMIENTO:
- Los números deben ser links clickeables
- Al hacer click, ejecutar onClickTipo(tipoAsunto)
- Mostrar loading state mientras se cargan datos
- Manejar estado vacío/error

DATOS FIJOS ESPERADOS:
1. SIA - código K
2. CORREOS - código C  
3. COMISIONES - código M
4. REUNIONES - código R
5. ACUERDOS - código A

La tabla siempre debe mostrar exactamente estas 5 filas en este orden.
```

---

## 🎯 **PROMPT 5: Configuración de Rutas**

```
Configura React Router para manejar la navegación desde TablaResultados:

RUTAS REQUERIDAS:
- /consulta-sia (para tipoAsunto K)
- /consulta-correos (para tipoAsunto C)
- /consulta-comisiones (para tipoAsunto M) 
- /consulta-reuniones (para tipoAsunto R)
- /consulta-acuerdos (para tipoAsunto A)

FUNCIONALIDAD:
- Cada ruta debe pasar el tipoAsunto como parámetro
- Mantener filtros de búsqueda en el estado global
- Breadcrumb navigation de regreso a búsqueda general

COMPONENTES A CREAR:
- ConsultaSIA.jsx
- ConsultaCorreos.jsx
- ConsultaComisiones.jsx  
- ConsultaReuniones.jsx
- ConsultaAcuerdos.jsx

Cada uno debe mostrar tabla detallada del tipo específico con paginación.
Usar el mismo diseño Tailwind CSS que TablaResultados.
```

---

## 🎯 **PROMPT 6: Testing & Validación**

```
Crea tests para validar que la migración funciona correctamente:

TESTS REQUERIDOS:
1. API endpoint responde con exactamente 5 elementos
2. Cada elemento tiene tipoAsunto, descripcion, cantidad
3. Las consultas SQL retornan números válidos
4. Navegación funciona correctamente
5. Filtros se aplican bien

DATOS DE PRUEBA (usar estos números para validar):
- SIA: ~145 registros
- CORREOS: ~289 registros  
- COMISIONES: ~67 registros
- REUNIONES: ~34 registros
- ACUERDOS: ~512 registros

CASOS EDGE:
- Sin filtros (todos los datos)
- Fechas muy específicas (rango pequeño)
- Área específica
- Búsqueda de texto
- Combinación de todos los filtros

Usa Jest y React Testing Library.
Incluye tests de integración con base de datos real.
```

---

## 📋 **CHECKLIST FINAL PARA LA IA**

Cuando termines la implementación, verifica que:

- [ ] ✅ Tabla muestra exactamente 5 filas siempre
- [ ] ✅ API endpoint funciona con filtros
- [ ] ✅ Consultas SQL usan 3 funciones diferentes  
- [ ] ✅ Navegación entre rutas funciona
- [ ] ✅ Estilos Tailwind aplicados correctamente
- [ ] ✅ Manejo de estados loading/error
- [ ] ✅ Filtros se envían correctamente al backend
- [ ] ✅ Números clickeables y funcionales
- [ ] ✅ Responsive design
- [ ] ✅ Validación de datos del API

**PRIORIDAD**: Enfócate primero en que la tabla TablaResultados funcione con datos reales de la BD, después optimiza el resto.

---

*Prompts creados basándose en análisis completo del sistema JSP original*