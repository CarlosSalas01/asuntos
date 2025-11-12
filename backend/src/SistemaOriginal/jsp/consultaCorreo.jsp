        <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <%@ taglib uri="/WEB-INF/tlds/superSelect.tld" prefix="s"%>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>${nomSistema}</title>
        <link rel="stylesheet" href="styles/hojaEstilos.css" type="text/css">
        <!--<link rel="stylesheet" href="styles/Estilo.css" type="text/css">-->
        <link rel="stylesheet" href="styles/consultas.css" type="text/css">
        <link rel="stylesheet" href="styles/jquery-ui.css" />
        
        <script src="js/jquery-3.5.0.min.js"></script>
        <script src="assets/js/bootstrap.min.js"></script>
                
	<script language="JavaScript" src="js/date-picker.js"></script>
        <script language="JavaScript" src="js/jquery-ui.js"></script>
        <script language="JavaScript" src="js/funcionesConsulta.js"></script>
        <script language="JavaScript" src="js/dialogos.js"></script> 
        <script language="JavaScript" src="js/control.js"></script>
        <script language="JavaScript" src="js/jquery.form.js"></script>

      <jsp:include page="encabezado.jsp" flush="true" />  
      <jsp:include page="filtros/filtroAsuntos.jsp" flush="true" />      
      <c:if test="${not empty asuntosConsulta}">
         <jsp:include page="jspConsulta/paginadorCorreo.jsp" flush="true" />
         <div class="table-responsive" id="contenedor_tabla">
         <table width="100%" class="table table-bordered tablaPeq tableR" id="tab_principal">
            <thead class="thead-scrollable">
            <tr class="encTablas">
            <th>Asunto</th>
            <th>Clasifi<br>caci&oacute;n</th> 
            <th>Remitente</th>
            <th>Descripci&oacute;n</th>
            <th>Fecha<br>env&iacute;o</th>
            <th>Fecha<br>vencimiento</th>
            <th>Anexos</th>

            <c:if test="${vistaUsuario.capturaAsuntos}">
              <th></th>
            </c:if>

            <th>Destinatarios</th>
            <c:if test="${usuario.permisoActual.areaBean.datos.nivel > 2}">
              <th>Asignado</th>   
              <th>Instruc<br>ci&oacute;n</th>
            </c:if>

            <th>Estatus<br>Responsable</th>
            <th>Avance</th>
            <th>Fecha<br>atenci&oacute;n</th>
            <th>D&iacute;as<br/>Proceso/<br/>Atenci&oacute;n</th>
            <th>D&iacute;as<br>retraso</th>
            <c:if test="${vistaUsuario.capturaAsuntos}">
               <th>Observaciones</th>    
            </c:if>
           </tr>
           </thead>
            <tbody>
                <c:set var="fondo" value="#FFFFFF"/>
                <c:set var="pendiente" value ="#FFBF00"/>
                <c:set var="atendido" value ="#088A08"/>
                
                <c:forEach var="asunto" items="${asuntosConsulta}">
                    <c:choose>
                        <c:when test="${fondo == '#f2f3f5'}"><!--ECF2F2-->
                            <c:set var="fondo" value="#FFFFFF"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="fondo" value="#f2f3f5"/>
                        </c:otherwise>
                    </c:choose>    
                    <tr style="background: ${fondo}">
                        <td rowspan="${asunto.noResponsables}">
                            <b>${asunto.idconsecutivo}</b><br><br>
                            <div id="estatusA${asunto.idasunto}">
                                <c:if test="${asunto.estatus == 'P' && (usuario.permisoActual.areaBean.datos.nivel < 3 || vistaUsuario.capturaAsuntos)}">
                                    <c:choose>
                                        <c:when test="${asunto.estatusAsunto == 0}"><img src="imagen/bandera_verde.png" width="20" height="20"/></c:when>
                                        <c:when test="${asunto.estatusAsunto == 1}"><img src="imagen/bandera_amarilla.png" width="20" height="20"/></c:when>
                                        <c:when test="${asunto.estatusAsunto == 2}"><img src="imagen/bandera_roja.png" width="20" height="20"/></c:when>
                                    </c:choose>
                                    <br>
                                    <font style="color:${pendiente}">PENDIENTE</font>
                                </c:if>
                                <c:if test="${asunto.estatus == 'P' && usuario.permisoActual.areaBean.datos.nivel > 2}">
                                    <c:choose>
                                        <c:when test="${asunto.estatusAsunto == 0}"><img src="imagen/bandera_verde.png" width="20" height="20"/></c:when>
                                        <c:when test="${asunto.estatusAsunto == 1}"><img src="imagen/bandera_amarilla.png" width="20" height="20"/></c:when>
                                        <c:when test="${asunto.estatusAsunto == 2}"><img src="imagen/bandera_roja.png" width="20" height="20"/></c:when>
                                    </c:choose>
                                    <br>
                                    <a class="LinkAmarillo" href="#${asunto.idasunto}" onClick="muestraAvances('${asunto.idasunto}',${usuario.permisoActual.areaBean.datos.idarea},${usuario.permisoActual.areaBean.datos.nivel},${asunto.idconsecutivo})" >
                                    	<font style="color:${pendiente}">PENDIENTE</font>
                                    </a>
                                </c:if>
                                <c:if test="${vistaUsuario.capturaAsuntos && asunto.estatus == 'A'}">
                                    <font style="color:#088A08">ATENDIDO</font>
                                </c:if>
                                <c:if test="${usuario.permisoActual.areaBean.datos.nivel == 2 && asunto.estatus == 'A'}">
                                    <font style="color:#088A08">ATENDIDO</font>
                                </c:if>
                                <c:if test="${asunto.estatus == 'A' && usuario.permisoActual.areaBean.datos.nivel == 3}">
                                	<a class="LinkVerde" href="#${asunto.idasunto}" onClick="muestraAtendidoNiv3('${asunto.idconsecutivo}', '${asunto.idasunto}','${usuario.permisoActual.areaBean.datos.dependede}','C')" >
                                    	<font style="color:${atendido}">ATENDIDO</font>
                                    </a>
                                </c:if>
                                <c:if test="${asunto.estatus == 'A' && usuario.permisoActual.areaBean.datos.nivel > 3}">
                                	<a class="LinkVerde" href="#${asunto.idasunto}" onClick="muestraAtendidoNiv4y5('${asunto.idconsecutivo}', '${asunto.idasunto}','${usuario.permisoActual.areaBean.datos.idarea}','${usuario.permisoActual.areaBean.datos.nivel}','C')" >
                                    	<font style="color:${atendido}">ATENDIDO</font>
                                    </a>
                                </c:if>
                            </div>
                            <br><br>
           		    <a href="#${asunto.idasunto}" data-bs-toggle="modal" data-bs-target="#avances" onClick="resumenAvancesTodos(${asunto.idasunto},${asunto.idconsecutivo})"><img src="imagen/avances.png" border="0" width="20" title="Mostrar todos los avances" alt="Mostrar todos los avances" /></a>
                            
                            <c:if test="${usuario.permisoActual.datos.rol == 'A'}">
                                <a href="#${asunto.idasunto}" data-bs-toggle="modal" data-bs-target="#act_seguim" onClick="actividadesSeguimiento(${asunto.idasunto},${asunto.idconsecutivo}, '0')"><img src="imagen/seg_actividades.png" border="0" width="20" title="Actividades seguimiento" alt="Actividades seguimiento" /></a>
                            </c:if>
                            &nbsp;<a href="#${asunto.idasunto}" data-bs-toggle="modal" data-bs-target="#resumen_delega" onClick="resumenDelega(${asunto.idasunto},${asunto.idconsecutivo})"><img src="imagen/asignaciones.png" border="0" width="20" title="Resumen Asignaciones" alt="Resumen Asignaciones" /></a>    
                            
                        </td>
                        <td rowspan="${asunto.noResponsables}">
                            <c:if test="${asunto.presidencia == 'P'}"><b>Presidencia</b><br><br></c:if>
                            <c:if test="${asunto.fuente == 'I'}">Interno</c:if>
                            <c:if test="${asunto.fuente == 'E'}">Externo</c:if>    
                        </td>
                        <td rowspan="${asunto.noResponsables}">${asunto.remitente.datos.nombre}</td> <!-- se asigno directamente al remitente no al área -->
                        <td rowspan="${asunto.noResponsables}">
                            <a name="id${asunto.idasunto}"/></a>
	                    ${asunto.descripcionFormatoHTML}
                        </td>
                        <td rowspan="${asunto.noResponsables}">${asunto.fechaingresoFormatoTexto}</td>
                        <%--Fecha atender --%>
                        <td rowspan="${asunto.noResponsables}" aling="center">
                               <c:if test="${asunto.urgente == 'S'}"><span class="badge bg-danger">URGENTE</span><br/></c:if> 
                               <c:if test="${asunto.urgente == '1'}"><span class="badge bg-warning">PRIORIDAD 1</span><br/></c:if> 
                               <c:if test="${asunto.urgente == '2'}"><span class="badge atNormal">ATENCIÓN NORMAL</span><br/></c:if>
                               <c:if test="${asunto.urgente == 'A'}"><span class="badge bg-primary">ADMINISTRATIVO</span><br/></c:if> 
                                <div id="fechaAtender${asunto.idasunto}">
                                <c:choose>
                                    <c:when test="${usuario.permisoActual.datos.rol == 'A' && asunto.estatus == 'P'}">
                                        <a href="#${asunto.idasunto}" data-bs-toggle="modal" data-bs-target="#reprograModal" onClick="reprogramaAsunto(${asunto.idasunto},${asunto.idconsecutivo});">${asunto.fechaoriginalFormatoTexto}</a> 
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

                                <div id="consultaReprograma${asunto.idasunto}">  
                                    <c:if test="${asunto.reprogramado == true}">
                                    <br/> 
                                    <!--<div class="reprograma" onClick="muestraPopup('muestraProgramaciones.do?idasunto=${asunto.idasunto}')"> <u>Reprogramaciones</u></div>-->
                                    <a class="reprograma" href="#${asunto.idasunto}" data-bs-toggle="modal" data-bs-target="#reprogramacionesModal" onClick="muestraReprograma('${asunto.idasunto}','${asunto.idconsecutivo}','${vistaUsuario.capturaAsuntos}','${asunto.estatus}')" >
                                    	<font style="color:${reprograma}">Reprogramaciones</font>
							        </a>
                                    </c:if> 
                                </div>  
                            
                        </td>
                          <td rowspan="${asunto.noResponsables}">
                              <c:if test = "${asunto.anexos != null}">
                                <c:forEach var="anexoAsunto" items="${asunto.anexos}">
                                    <c:choose>
                                        <c:when test="${anexoAsunto.tipoDocumento == 1}">
                                            <c:set var="imagen" value="img/word.png"/>
                                        </c:when>
                                        <c:when test="${anexoAsunto.tipoDocumento == 2}">
                                            <c:set var="imagen" value="img/excel.png"/>
                                        </c:when>
                                        <c:when test="${anexoAsunto.tipoDocumento == 3}">
                                            <c:set var="imagen" value="img/powerpoint.png"/>
                                        </c:when>
                                        <c:when test="${anexoAsunto.tipoDocumento == 4}">
                                            <c:set var="imagen" value="img/adobe_acrobat.png"/>
                                        </c:when>
                                        <c:when test="${anexoAsunto.tipoDocumento == 5}">
                                            <c:set var="imagen" value="img/mail.png"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="imagen" value="img/documentos.png"/> 
                                        </c:otherwise>
                                    </c:choose>
                                    <a href="anexos/${anexoAsunto.nombrearchivo}?idAnexo=${anexoAsunto.idanexo}" target="_blank">
                                    <img src="${imagen}" alt="${anexoAsunto.nombrearchivo}" title="${anexoAsunto.nombrearchivo}" border="0" width="20" height="20"/>
                                    </a>
                                </c:forEach>
                              </c:if>  
                            </td>
                        
                        <c:if test="${vistaUsuario.capturaAsuntos}">
                        <td rowspan="${asunto.noResponsables}">
                            <c:set var="visible" value=""/>
                            <c:if test="${asunto.estatus == 'A'}"><c:set var="visible" value="display: none"/></c:if>
                            <div id="herramientas${asunto.idasunto}">
                                <a href="editaAsunto.do?idasunto=${asunto.idasunto}&tipo=1"><img src="imagen/editar.png" width="20" border="0" title="Editar" alt="Editar"/></a><br>
                                <a href="eliminaAsunto.do?idasunto=${asunto.idasunto}"><img src="imagen/eliminar.png" width="20" border="0" title="Eliminar" alt="Eliminar"/></a><br>
                                <a href="configuraEnvioCorreo.do?idasunto=${asunto.idasunto}"><img src="imagen/correo.png" width="20" border="0" title="Envia correo" alt="Envia correo"/></a><br>
                            </div>
                        </td>
                        </c:if>
                        
                        <c:set var="bandera" value="false" />      
                      
                        <c:forEach var="responsable" items="${asunto.responsables}">
                                <c:if test="${bandera == true}">
                                <tr style="background: ${fondo}">
                                </c:if>
                                <td>
                                    <c:if test="${responsable.area.nivel == 2}"><c:set var="nomArea" value="${responsable.area.siglas}"/></c:if>                                      
                                    <c:if test="${responsable.area.nivel > 2}"><c:set var="nomArea" value="${responsable.area.nombre}"/></c:if>                                      
                                                                         
                                    <c:choose>
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
                                    </c:choose>
                                 </td>
                                <c:if test="${(responsable.area.nivel > 2)}">
                                  <td>${responsable.fechaasignadoFormato}</td>
                                  <td>${responsable.datos.instruccion}</td>
                                </c:if>
                                <td>
                                    <c:choose>
                                        <c:when test="${vistaUsuario.capturaAsuntos && responsable.datos.avance == 99 && responsable.datos.estatus == 'P'}">
                                            <div id="estatus${asunto.idasunto}${responsable.area.idarea}">
                                                    <a class="LinkAmarillo" href="#${asunto.idasunto}" data-bs-toggle="modal" data-bs-target="#asignaAtendidoModal" onClick="cambiaAtendido(${asunto.idasunto},'${asunto.idconsecutivo}',${responsable.area.idarea},'${asunto.fechaatenderFormatoTexto}');" >
                                                    <font style="color:${pendiente}">PENDIENTE</font>
                                                  </a>
                                            </div>        
                                         </c:when>            
                                         <c:when test="${vistaUsuario.capturaAsuntos && responsable.datos.estatus == 'A'}">      
                                            <div id="estatus${asunto.idasunto}${responsable.area.idarea}"> 
                                                  <a class="LinkVerde" href="#${asunto.idasunto}" data-bs-toggle="modal" data-bs-target="#asuntoAtendidoModal" onClick="cambiaPendiente(${asunto.idasunto},'${asunto.idconsecutivo}',${responsable.area.idarea},'${responsable.fechaatencionFormatoTexto}','<c:out value="${responsable.datos.comentario}"/>','${asunto.fechaatenderFormatoTexto}','C');" >
                                                    <font style="color:${atendido}">ATENDIDO</font>
                                                  </a>
                                            </div>
                                        </c:when>
                                        <%-- se modifico la condición !vistaUsuario.capturaAsuntos && asunto.estatus == 'A' &&responsable.area.nivel == 2 !> --%>
					<c:when test="${!vistaUsuario.capturaAsuntos && responsable.datos.estatus == 'A' && responsable.area.nivel == 2}"> 
                                            <div id="estatus${asunto.idasunto}${responsable.area.idarea}"> 
                                                  <a class="LinkVerde" href="#${asunto.idasunto}" onClick="muestraAtendido('${asunto.idasunto}','${asunto.idconsecutivo}', '${responsable.fechaatencionFormatoTexto}','<c:out value="${responsable.datos.comentario}"/>','C',${responsable.area.idarea} )" >
                                                    <font style="color:${atendido}">ATENDIDO</font>
                                                  </a>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
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
                                        </c:otherwise>        
                                    </c:choose>
                                        
                                </td>
                                <td>
                                   <div id="avance${asunto.idasunto}${responsable.area.idarea}" >
                                    <%--<c:choose>
                                        <c:when test="${responsable.area.idarea == usuario.permisoActual.areaBean.datos.idarea || usuario.permisoActual.datos.rol == 'A'}">
                                            <%--<a href="consultaCapturaAvance.do?idasunto=${asunto.idasunto}&idResponsable=${responsable.area.idarea}&idRSuperior=0#id${asunto.idasunto}" title="${responsable.ultimoAvance}">${responsable.datos.avance} %</a> --%>
                                            <c:choose>
                                            <c:when test="${responsable.datos.estatus != 'C'}">
                                                <a href="consultaCapturaAvance.do?idasunto=${asunto.idasunto}&idResponsable=${responsable.area.idarea}&idRSuperior=0&idRTabla=${responsable.datos.idresponsable}" title="${responsable.ultimoAvance}">${responsable.datos.avance} %</a>
                                            </c:when>    
                                            <c:when test="${responsable.datos.estatus == 'C' && responsable.datos.avance > 0}">
                                                <a href="consultaAvancesCancelados.do?idasunto=${asunto.idasunto}&idResponsable=${responsable.area.idarea}&idRTabla=${responsable.datos.idresponsable}&idRSuperior=0">${responsable.datos.avance} %</a>
                                            </c:when>                                        
                                            <c:when test="${responsable.datos.estatus == 'C' && responsable.datos.avance == 0}">
                                                ${responsable.datos.avance} %
                                            </c:when>                                            
                                            </c:choose>    
                                    <%--    </c:when>                                            
                                        <c:otherwise>
                                            ${responsable.datos.avance} % 
                                        </c:otherwise>
                                    </c:choose> --%>
                                    &nbsp;
                                    <!--<img src="img/arrow.gif" title="Resumen avances" alt="Resumen avances" onClick="muestraPopup('resumenAvances.do?idasunto=${asunto.idasunto}&idarea=${responsable.area.idarea}')"/>-->
                                    <a href="#${asunto.idasunto}" onClick="resumenAvances(${asunto.idasunto},${responsable.area.idarea},${asunto.idconsecutivo})"><img src="img/arrow.gif" border="0" title="Resumen avances" alt="Resumen avances" /></a>
                                   </div>     
                                </td>
                                <td><div id ="fechaA${asunto.idasunto}${responsable.area.idarea}">${responsable.fechaatencionFormatoTexto}</div></td>
                                <td><div id ="diasA${asunto.idasunto}${responsable.area.idarea}"><c:if test="${responsable.datos.diasatencion > 0}">${responsable.datos.diasatencion}</c:if></div></td>
                                <td><div id ="diasR${asunto.idasunto}${responsable.area.idarea}"><c:if test="${responsable.datos.diasretraso > 0}">${responsable.datos.diasretraso}</c:if></div></td>
                                <c:if test="${vistaUsuario.capturaAsuntos && bandera == false}">
                                   <td rowspan="${asunto.noResponsables}">${asunto.observacionesFormatoHTML}</td>            
                                </c:if>
                                </tr>
                                <c:set var="bandera" value="true"/>
                        </c:forEach>
                </c:forEach>
                </form>
            </tbody>
        </table>
        </div>
        <ul id="menu" class="container__menu container__menu--hidden"></ul>
       <jsp:include page="jspConsulta/paginadorCorreo.jsp" flush="true" />
       
       </c:if>
       <c:if test="${empty asuntosConsulta}">
           No existen correos para este usuario con dichas características.
       </c:if> 
      
     <jsp:include page="dialogos.jsp" flush="true" />
        <div class="modal fade" id="avances" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                      <h4 class="modal-title">Desgloce de avances</h4>
                      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <div id="asuntoStr"></div><br/><br/>
                            <table class="table table-bordered table-hover table-striped tablaPeq" cellspacing="3"  >
                            <thead id="headAvances">
                            </thead>
                            <tbody id="resumenAvancesHist"></tbody>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-outline-danger" data-bs-dismiss="modal">Cerrar</button>
                    </div>
                </div>
            </div>
        </div>
           
        <div class="modal fade" id="act_seguim" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                      <h4 class="modal-title">Actividades de seguimiento</h4>
                      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div>
                            <table>
                                <thead>
                                    <tr><td>Asunto</th><td>Filtrar área:</th></tr>
                                </thead>
                            <tbody id="headDial">
                                <tr><td><input type="text" size="10" readonly="readonly" id="id_cons"/></td>
                                <td><select id="selArea"></select></td></tr>
                            </tbody>
                            </table>
                        </div>
                        <div id="asuntoStr2"></div>
                        <table align="center" width="100%" class="table table-bordered table-hover table-striped tablaPeq" >
                        <thead>
                            <tr><td>Fecha</td><td>Anexos</td><td>Descripción</td><td>Área</td><td>Realizó</td><td>Editar</td><td>Eliminar</td></tr>
                        </thead>
                        <tbody id="actividadesSeguim">
                        </tbody>
                        </table>

                        <br>
                        <div id="agregaActiv">
                            <form name="formaAnexo" id="formaAnexo" method="post" enctype="multipart/form-data" accept-charset="UTF-8">
                                <input type="hidden" name="tipo" id="tipo" value="grabarActividad">
                                <input type="hidden" name="idasunto" id="idasunto">
                                <input type="hidden" name="asuntoOacuerdo" id="asuntoOacuerdo" value="asunto">
                                <input type="hidden" name="altaEdita" id="altaEdita" value="A">
                                <input type="hidden" name="idactividadE" id="idactividadE">
                                <table align="center" width="100%" class="table table-bordered tablaPeq">
                                    <thead>
                                        <tr><td>Fecha</td><td>Área</td><td>Descripción</td></tr>
                                    </thead>
                                    <tbody>
                                        <tr><td><input type="text" name="fechaAct" id="fechaAct" size="15" readonly="readonly" data-uk-datepicker="{format:'DD/MM/YYYY'}"></td>
                                            <!--<td><input type="text" name="descripAct" id="descripAct" size="40"></td>-->
                                            <td><select id="areaResp" name="areaResp"></select></td>
                                            <td><textarea name="descripAct" id="descripAct" rows="4" cols="50"></textarea></td>
                                        </tr>
                                        <tr><td>Anexo</td><td colspan="2"><input type="file" name="archAct" id="archAct"></td></tr>
                                    </tbody>
                                </table>
                                <br>
                                <button type="button" class="btn btn-outline-primary" id="guardarAct" onClick="subeArchivo()" data-bs-dismiss="modal">Guardar</button>
                            </form>	
                        </div>
            
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-outline-danger" data-bs-dismiss="modal">Cerrar</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="resumen_delega" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                      <h4 class="modal-title">Árbol de asignación</h4>
                      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <div id="asuntoStr"></div><br/><br/>
                            <table class="table table-bordered table-hover table-striped tablaPeq" cellspacing="3"  >
                            <thead id="headAvances">
                            </thead>
                            <tbody id="resumenDelega"></tbody>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-outline-danger" data-bs-dismiss="modal">Cerrar</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="reprograModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                      <h4 class="modal-title">Reprogramación</h4>
                      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form name="frmReprograma">
                            Asunto <input name="idAR" id="idAR" size="10" disabled=""/><br><br>
                            Fecha reprogramaci&oacute;n:<br>
                            <input type="text" name="fecharep" id="fecharep" size="15" readonly="readonly" data-uk-datepicker="{format:'DD/MM/YYYY'}">
                            <!--<input type="text" name="fecharep" id="fecharep" readonly="1" size="15"> 
                            <a href="javascript:show_calendar('frmReprograma.fecharep');" onMouseOver="window.status='Date Picker';return true;"
                                onmouseout="window.status='';return true;"><img src="img/show-calendar.gif" width=24 height=22 border=0 /></a>-->
                            <br><br>
                            &Aacute;rea solicitante:</br>
                            <select name="areasol" id="areasol" style="width: 250px"></select>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-outline-primary" onclick="reprograma()" data-bs-dismiss="modal">Reprograma</button>
                        <button class="btn btn-outline-danger" data-bs-dismiss="modal">Cerrar</button>
                    </div>
                </div>
            </div>
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

        <div class="modal fade" id="asignaAtendidoModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                      <h4 class="modal-title">Asigna atendido</h4>
                      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form name="frmAtencion">
                            <div class="row g-2" style="row-gap: 10px;">
                                <div class="col-2">Asunto:</div>
                                <div class="col-3">
                                  <input name="idAA" id="idAP" size="10" disabled="" class="form-control"/>
                                </div>
                            </div>
                            <div class="row g-2" style="row-gap: 10px;">
                                <div class="col-6">Fecha de atención:<br/>
                                    <input name="fechaatencion" id="fechaatencion" type="text" readonly="readonly" class="form-control" data-uk-datepicker="{format:'DD/MM/YYYY'}" />
                                </div>
                            </div>
                            <div class="row g-2" style="row-gap: 10px;">
                                <div class="col-12">Captura comentarios:<br/>
                                    <textarea name="comen" id="comen" cols="35" rows="8" class="form-control"></textarea><br />
                                </div>
                            </div>
                        </form>
        <!--<form name="frmAtencion">
            Asunto <input name="idAP" id="idAP" size="10" disabled=""/><br>
            Fecha:<br>
            <input type="text" name="fechaatencion" id="fechaatencion" readonly="1" size="15"> 
            <a href="javascript:show_calendar('frmAtencion.fechaatencion');" onMouseOver="window.status='Date Picker';return true;"
               onmouseout="window.status='';return true;"><img src="img/show-calendar.gif" width=24 height=22 border=0 /></a>
            <br>
            Captura comentarios:
            <textarea name="comen" id="comen" cols="35" rows="8"></textarea>
        </form>-->
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-outline-primary" onclick="asignaAtendido()" data-bs-dismiss="modal">Atendido</button>
                        <button class="btn btn-outline-danger" data-bs-dismiss="modal">Cerrar</button>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="modal fade" id="asuntoAtendidoModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                      <h4 class="modal-title">Asunto atendido</h4>
                      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="row g-2" style="row-gap: 10px;">
                            <div class="col-2">Asunto:</div>
                            <div class="col-3">
                              <input name="idAA" id="idAA" size="10" disabled="" class="form-control"/>
                            </div>
                        </div>
                        <div class="row g-2" style="row-gap: 10px;">
                            <div class="col-6">Fecha de atención:<br/>
                                <input name="fechaatendido" id="fechaatendido" type="text" disabled class="form-control" />
                            </div>
                        </div>
                        <div class="row g-2" style="row-gap: 10px;">
                            <div class="col-12">Comentario de atención:<br/>
                                <textarea name="comentarioAtendido" id="comentarioAtendido" cols="35" rows="8" disabled="" class="form-control"></textarea><br />
                            </div>
                        </div>
                        <div class="row g-2" style="row-gap: 10px;">
                            <div class="col-12">Anexos:<br/>
                                <div id="anexos"></div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-outline-primary" onclick="asignaPendiente()" data-bs-dismiss="modal">Asigna pendiente</button>
                        <button class="btn btn-outline-danger" data-bs-dismiss="modal">Cerrar</button>
                    </div>
                </div>
            </div>
        </div>
        
       <div class="modal fade" id="cuadroAtPendientes" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                      <h4 class="modal-title">Cuadro resumen (correos)</h4>
                      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                           <div class="col-12">
                              <table width="60%" class="table table-bordered tablaPeq">
                                  <tr class="encTablas text-center text-white"><td>Área</td><td>Vencidos</td><td>Por vencer</td><td>Atendidos</td></tr>
                                  <c:forEach var="resumen" items="${resumen}">
                                   <tr>
                                       <td align="center">${resumen.siglas}</td>
                                       <td align="center">${resumen.vencidos}</td>
                                       <td align="center">${resumen.porvencer}</td>
                                       <td align="center">${resumen.atendidos}</td>
                                   </tr>
                                  </c:forEach>
                              </table>
                           </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-outline-danger" data-bs-dismiss="modal">Cerrar</button>
                    </div>
                </div>
            </div>
        </div>