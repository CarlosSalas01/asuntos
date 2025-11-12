<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" href="styles/hojaEstilos.css" type="text/css">
<link rel="stylesheet" href="styles/consultas.css" type="text/css">
<link rel="stylesheet" href="styles/jquery-ui.css" />
<script src="js/jquery-3.5.0.min.js"></script>
<script src="assets/js/bootstrap.min.js"></script>
<script language="JavaScript" src="js/date-picker.js"></script>
<script language="JavaScript" src="js/jquery-ui.js"></script>
<script src="assets/js/jquery.fileDownload.js" type="text/javascript"></script>
<script language="JavaScript" src="js/funcionesConsulta.js"></script>
<script language="JavaScript" src="js/dialogos.js"></script> 
<script language="JavaScript" src="js/control.js"></script>
<script language="JavaScript" src="js/jquery.form.js"></script>
    </head>
    <body >
      <jsp:include page="encabezado.jsp" flush="true" />  
     <jsp:include page="filtros/filtroComision.jsp" flush="true" />
     <c:if test="${not empty asuntosConsulta}">
        <jsp:include page="jspConsulta/paginadorComisiones.jsp" flush="true" />
        <div class="table-responsive" id="contenedor_tabla">
          <table width="100%" class="table table-bordered tablaPeq tableR" id="tab_principal">
              <thead class="thead-scrollable">
              <tr class="encTablas">
                <th>Asunto</th>
                <th>Comisi&oacute;n</th>
                <th>Comisionados</th>
                <th>Objetivo</th>
                <th>Lugar</th>
                <th>Inicio</th>
                <th>Fin</th>
                <th>Fecha<br>vencimiento</th>
                <th>Anexos</th>
                 <c:if test="${vistaUsuario.capturaAsuntos}">
                   <th>Observaciones</th>     
                </c:if>

                <th>Responsable</th>
                <c:if test="${usuario.permisoActual.areaBean.datos.nivel > 2}">
                <th>Asignado</th>
                <th>Instruc.</th>
                </c:if>
                <th>Estatus<br>Resp.</th>
                <th>Avance</th>
                <th>Fecha<br>atención</th>
                <th>D&iacute;as<br>atenci&oacute;n</th>
                <th>D&iacute;as<br>retraso</th>
                <%--<c:if test="${vistaUsuario.capturaAsuntos}">
                <th></th>
                </c:if>--%>
            </tr>
            </thead>
            <tbody>
                <c:set var="fondo" value="#FFFFFF"/>
                <c:set var="pendiente" value ="#FFBF00"/>
                <c:set var="atendido" value ="#088A08"/>
                
                <c:forEach var="asunto" items="${asuntosConsulta}">
                    <c:choose>
                        <c:when test="${fondo == '#f2f3f5'}">
                            <c:set var="fondo" value="#FFFFFF"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="fondo" value="#f2f3f5"/>
                        </c:otherwise>
                 </c:choose>    
                 <tr style="background: ${fondo}">
                        
                        <td>
                            <b>${asunto.idconsecutivo}</b><br><br>
                            <c:if test="${asunto.estatus == 'P'}">
                                <c:if test="${asunto.estatusAsunto == 0}"><img src="imagen/Bandera-Verde.png" width="20" height="20"/></c:if>
                                <c:if test="${asunto.estatusAsunto == 1}"><img src="imagen/Bandera-Amarilla.png" width="20" height="20"/></c:if>
                                <c:if test="${asunto.estatusAsunto == 2}"><img src="imagen/Bandera-Roja.png" width="20" height="20"/></c:if>
                            </c:if>
                            <br>
                            <c:if test="${asunto.estatus == 'P'}">
                                <font style="color:#FFBF00">PENDIENTE</font>
                            </c:if>
                            <c:if test="${asunto.estatus == 'A'}">
                                <font style="color:#088A08">ATENDIDO</font>
                            </c:if>
                            <%--<br><br>
                            <a href="#${asunto.idasunto}" onClick="resumenAvancesTodos(${asunto.idasunto},${asunto.idconsecutivo})"><img src="imagen/Mostrar tabla.png" border="0" width="15" title="Mostrar todos los avances" alt="Mostrar todos los avances" /></a>
                            <c:if test="${usuario.permisoActual.datos.rol == 'A'}">
                                <a href="#${asunto.idasunto}" onClick="actividadesSeguimiento(${asunto.idasunto},${asunto.idconsecutivo}, '0')"><img src="imagen/Insertar_ACorresponsable.png" border="0" width="15" title="Actividades seguimiento" alt="Actividades seguimiento" /></a>
                            </c:if>--%>
                        </td>
                        <td >${asunto.nocontrol}</td>
                        <td width="30%">
                           ${asunto.asistentesFormatoHTML}
                        </td> 
                        <td>
	                    ${asunto.descripcion}
                        </td> 
                        <td >${asunto.lugar}</td> 
                        
                        <td>${asunto.fechaingresoFormatoTexto}</td>
                        
                        <td >${asunto.fechadescargaFormatoTexto}</td>
                        <td>
                        
                        
                                <div id="fechaAtender${asunto.idasunto}">
                                <c:choose>
                                    <c:when test="${usuario.permisoActual.datos.rol == 'A' && asunto.estatus == 'P'}">
                                        <a href="#${asunto.idasunto}" onClick="reprogramaAsunto(${asunto.idasunto},${asunto.idconsecutivo});"> ${asunto.fechaoriginalFormatoTexto}</a> 
                                    </c:when>
                                    <c:when test="${usuario.permisoActual.datos.rol != 'A' || asunto.estatus == 'A'}">
                                        ${asunto.fechaoriginalFormatoTexto} 
                                    </c:when>
                                    <c:otherwise>
                                        Error
                                        ${usuario.permisoActual.datos.rol}
                                        ${asunto.estatus}
                                    </c:otherwise>    
                                </c:choose>  
                                <br>        
                                </div>
                        
                        <!--
                        
                                <c:choose>
                                    <c:when test="${usuario.permisoActual.datos.rol == 'A' && asunto.estatus == 'P'}">
                                        <a href="#${asunto.idasunto}" onClick="reprogramaAsunto(${asunto.idasunto},${asunto.idconsecutivo});">${asunto.fechaoriginalFormatoTexto}</a> 
                                    </c:when>
                                    <c:when test="${usuario.permisoActual.datos.rol != 'A' || asunto.estatus == 'A'}">
                                        ${asunto.fechaoriginalFormatoTexto}
                                    </c:when>
                                </c:choose>  
                                <br>        
                                </div>        
-->
                                <div id="consultaReprograma${asunto.idasunto}">  
                                    <c:if test="${asunto.reprogramado == true}">
                                    <br/> 
                                    <%--<div class="reprograma" onClick="muestraPopup('muestraProgramaciones.do?idasunto=${asunto.idasunto}')"> <u>Reprogramaciones</u></div>
                                	<a class="reprograma" href="#${asunto.idasunto}" onClick="muestraReprograma('${asunto.idasunto}','${asunto.idconsecutivo}','${vistaUsuario.capturaAsuntos}','${asunto.estatus}')" >
                                            <font style="color:${reprograma}">Reprogramaciones</font>
                                        </a>--%>
                                        <a class="reprograma" href="#${asunto.idasunto}" data-bs-toggle="modal" data-bs-target="#reprogramacionesModal" onClick="muestraReprograma(${asunto.idasunto},${asunto.idconsecutivo},'${vistaUsuario.capturaAsuntos}','${asunto.estatus}')" >
                                            <font style="color:${reprograma}">Reprogramaciones</font>
                                        </a>
                                    </c:if> 
                                </div>  
                        </td>
                        
                        <td>
                          <c:if test="${asunto.anexos != null}">   
                             <c:forEach var="anexoAsunto" items="${asunto.anexos}">
                                    <c:choose>
                                        <c:when test="${anexoAsunto.tipoDocumento == 1}">
                                        <c:set var="imagen" value="img/ic_Word.png"/>
                                        </c:when>
                                        <c:when test="${anexoAsunto.tipoDocumento == 2}">
                                        <c:set var="imagen" value="img/Ic_Xlsx.png"/>
                                        </c:when>
                                        <c:when test="${anexoAsunto.tipoDocumento == 3}">
                                        <c:set var="imagen" value="img/Ic_Ppt.png"/>
                                        </c:when>
                                        <c:when test="${anexoAsunto.tipoDocumento == 4}">
                                        <c:set var="imagen" value="img/adobe.gif"/>
                                        </c:when>
                                        <c:when test="${anexoAsunto.tipoDocumento == 5}">
                                        <c:set var="imagen" value="img/Ic_Correo.png"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="imagen" value="img/documento.gif"/> 
                                        </c:otherwise>
                                    </c:choose>
                                    <a href="anexos/${anexoAsunto.nombrearchivo}?idAnexo=${anexoAsunto.idanexo}" target="_blank">
                                    <img src="${imagen}" alt="${anexoAsunto.nombrearchivo}" title="${anexoAsunto.nombrearchivo}" border="0" width="15" height="15"/>
                                    </a>
                                </c:forEach>  
                           </c:if>       
                        </td>
                        
                        <c:if test="${vistaUsuario.capturaAsuntos}">
                        <td>${asunto.observacionesFormatoHTML}</td>     
                        </c:if>
                        
                        <c:set var="bandera" value="false" />      
                        <c:forEach var="responsable" items="${asunto.responsables}">
                            <c:if test="${bandera == true}">
                                <tr style="background: ${fondo}">
                            </c:if>
                            <td>
                                    <c:if test="${responsable.area.nivel == 2}"><c:set var="nomArea" value="${responsable.area.siglas}"/></c:if>                                      
                                    <c:if test="${responsable.area.nivel > 2}"><c:set var="nomArea" value="${responsable.area.nombre}"/></c:if>                                      
                                                                         
                                    <%--<c:choose>
                                        <c:when test="${responsable.area.idarea == usuario.permisoActual.areaBean.datos.idarea  || (responsable.datos.delegado == 'S' && vistaUsuario.consultarTodo) || usuario.permisoActual.datos.rol == 'A' }">
                                            <c:choose>
                                            <c:when test="${responsable.datos.estatus == 'A' && responsable.datos.delegado != 'S'}">
                                                <div class="propietario">${nomArea}</div>  
                                            </c:when>
                                            <c:otherwise>
                                               <a  href="consultaAsuntoAsignado.do?idasunto=${asunto.idasunto}&idResponsable=${responsable.area.idarea}" title="${responsable.area.nombre}" class="delegado">${nomArea}</a> 
                                            </c:otherwise>       
                                            </c:choose>
                                            <c:if test="${responsable.datos.delegado == 'S'}">
                                                <a id="a1" href="#" onMouseOver="return atributoTitle('${responsable.area.idarea}',${asunto.idasunto},this)">
                                                    <img src="img/arrow.gif" alt="Consulta asignaciones" border="0" />
                                                </a>
                                            </c:if>
                                        </c:when>
                                        <c:otherwise>
                                            <c:if test="${responsable.area.nivel == 2}"><a class="noLink" href="#" title="${responsable.area.nombre}" class="estilo">${responsable.area.siglas}</c:if>                                      
                                            <c:if test="${responsable.area.nivel > 2}">${responsable.area.nombre}</c:if>                                      
                                        </c:otherwise>        
                                    </c:choose>--%>
                                    <div class="propietario">${nomArea}</div>
                                </td>
                                <c:if test="${responsable.area.nivel > 2}">
                                  <td>${responsable.fechaasignadoFormato}</td>
                                  <td>${responsable.datos.instruccion}</td>
                                </c:if>

                            <td>
                                <c:choose>
                                    <c:when test="${responsable.datos.estatus == 'P'}">
                                        <font style="color:#FFBF00">PENDIENTE</font>
                                    </c:when>    
                                    <c:when test="${responsable.datos.estatus == 'A'}">
                                        <font style="color:#088A08">ATENDIDO</font>
                                    </c:when>
                                    <c:when test="${responsable.datos.estatus == 'C'}">                                                 
                                        <font style="color:#FA5858">CANCELADO</font>
                                    </c:when>       
                                </c:choose>

                            </td>
                            <td>
                               <c:choose>
                                    <c:when test="${responsable.area.idarea == usuario.permisoActual.areaBean.datos.idarea || usuario.permisoActual.datos.rol == 'A'}">
                                      <%-- <a href="consultaCapturaAvance.do?idasunto=${asunto.idasunto}&idResponsable=${responsable.area.idarea}&idRSuperior=0">${responsable.datos.avance} %</a> --%>
                                      <c:choose>
                                            <c:when test="${responsable.datos.estatus != 'C'}">
                                                <a href="consultaCapturaAvance.do?idasunto=${asunto.idasunto}&idResponsable=${responsable.area.idarea}&idRSuperior=0&idRTabla=${responsable.datos.idresponsable}&tipo=M" title="${responsable.ultimoAvance}">${responsable.datos.avance} %</a>
                                            </c:when>    
                                            <c:when test="${responsable.datos.estatus == 'C' && responsable.datos.avance > 0}">
                                                <a href="consultaAvancesCancelados.do?idasunto=${asunto.idasunto}&idResponsable=${responsable.area.idarea}&idRTabla=${responsable.datos.idresponsable}&idRSuperior=0&tipo=M">${responsable.datos.avance} %</a>
                                            </c:when>                                        
                                            <c:when test="${responsable.datos.estatus == 'C' && responsable.datos.avance == 0}">
                                                ${responsable.datos.avance} %
                                            </c:when>                                            
                                      </c:choose>  
                                    </c:when>
                                    <c:otherwise>
                                        ${responsable.datos.avance} %  
                                    </c:otherwise>
                                </c:choose>              
                            </td>            
                            <td>${responsable.fechaatencionFormatoTexto}</td>
                            <td><c:if test="${responsable.datos.diasatencion > 0}">${responsable.datos.diasatencion}</c:if></td>
                            <td><c:if test="${responsable.datos.diasretraso > 0}">${responsable.datos.diasretraso}</c:if></td>
                             
                            
                            <c:set var="bandera" value="true"/>
                        </c:forEach>
                        <%--<c:if test="${vistaUsuario.capturaAsuntos}">
                            <td width="5%"> <a href="editaAsunto.do?idasunto=${asunto.idasunto}&tipo=3"><img src="imagen/Editar-asunto.png" border="0" /></a>
                                <a href="eliminaAsunto.do?idasunto=${asunto.idasunto}"><img src="imagen/Eliminar-asunto.png" border="0" /></a>
                            </td>
                        </c:if>--%>
                        </tr>    
                </c:forEach>
            </tbody>
        </table>
    </div>
                
       <div class="modal fade" id="reprogramacionesModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                      <h4 class="modal-title">Reprogramaciones</h4>
                      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div id="asunto"></div>
                        <table align="center" width="100%" id="reprogramacionesD" class="table table-bordered table-striped tablaPeq">
                        <thead>
                            <tr><td>Fecha <br/>solicitud</td>
                                <td>Fecha <br/>reprogramada</td>
                                <td>Área</td>
                            </tr>
                        </thead>
                        <tbody>
                        </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-outline-danger" data-bs-dismiss="modal">Cerrar</button>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="jspConsulta/paginadorComisiones.jsp" flush="true" />
        
       </c:if>
       <c:if test="${empty asuntosConsulta}">
           No existen comisiones para este usuario con dichas características.
       </c:if> 
       <jsp:include page="dialogos.jsp" flush="true" />
    </body>
</html>
