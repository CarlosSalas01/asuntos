<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <head>
        <%@ taglib uri="/WEB-INF/tlds/superSelect.tld" prefix="s"%>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="styles/hojaEstilos.css" type="text/css">
<!--        <link rel="stylesheet" href="styles/Estilo.css" type="text/css">-->
        <link rel="stylesheet" href="styles/consultas.css" type="text/css">
        <link rel="stylesheet" href="styles/jquery-ui.css" />
        <script src="js/jquery-3.5.0.min.js"></script>
        <script src="assets/js/bootstrap.min.js"></script>
        <script language="JavaScript" src="js/jquery-ui.js"></script>
        <script language="JavaScript" src="js/date-picker.js"></script>
        <script language="JavaScript" src="js/funcionesConsulta.js"></script>
        <script language="JavaScript" src="js/dialogos.js"></script>
        <script language="JavaScript" src="js/control.js"></script>
        <script language="JavaScript" src="js/jquery.form.js"></script>
    </head>
      <jsp:include page="encabezado.jsp" flush="true" />
      <br/><br/>
      <c:if test="${reunionConsulta != null}">
<%--        <table width="100%" class="tablaDeta">
            <tr style="background: #01DF01"><td align="center" colspan="2">
                    <strong><font style="color: #FFFFFF;font-size: 16px">A C U E R D O S&nbsp;&nbsp;&nbsp;P O R&nbsp;&nbsp;&nbsp;R E U N I &Oacute; N</font></strong>
            </td></tr> 
            <tr><td>&nbsp;</td></tr>                    
            <c:if test="${reunionConsulta != null}">
            <tr><td colspan="2">
                    <strong>ReuniÃ³n:</strong>${reunionConsulta.idconsecutivo}
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <strong>Fecha:</strong>${reunionConsulta.fechaingresoFormatoTexto}
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <strong>Tema:</strong>${reunionConsulta.descripcion}
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="consultaReunion.do?recarga=S#id${reunionConsulta.idasunto}">Regresar a Reuniones</a>
               </td></tr>
               </c:if>
        </table>--%>
        <div class="row alert alert-success" role="alert">
            <div class="col-12 text-center">A C U E R D O S&nbsp;&nbsp;&nbsp;P O R&nbsp;&nbsp;&nbsp;R E U N I Ó N</div>
        </div>
        <div class="row alert alert-secondary" role="alert">
            <div class="col-2">Reunión: <span class="badge bg-light text-dark">${reunionConsulta.idconsecutivo}</span></div>
            <div class="col-2"><b>Fecha: </b><span class="badge bg-light text-dark">${reunionConsulta.fechaingresoFormatoTexto}</span></div>
            <div class="col-5"><b>Tema: </b><span class="badge bg-light text-dark">${reunionConsulta.descripcion}</span></div>
            <div class="col-2">
                <a href="consultaReunion.do?pagActual=${pagActual}&totalR=${totalR}&recarga=S#id${reunionConsulta.idasunto}">
                    <h6><span class="badge bg-primary">Regresar a Reuniones</span></h6>
                </a>
            </div>
        </div>
       </c:if>
       <c:if test="${reunionConsulta == null}">
          <jsp:include page="filtros/filtroAcuerdos.jsp" flush="true" />                            
          <jsp:include page="jspConsulta/paginadorAcuerdos.jsp" flush="true" />                              
       </c:if>     
       <c:if test="${not empty acuerdosConsulta}">
           <div class="table-responsive">
            <table width="100%" class="table table-bordered tablaPeq tableR" id="tab_principal">
                <thead class="thead-scrollable">
                <tr class="encTablas">
                    <th>Reuni&oacute;n</th>
                    <%--<th>Num.<br>acuerdo</th> --%>
                    <th>Acuerdo</th>
                    <%--<th>Clasifi<br>caci&oacute;n</th> --%>
                    <th>Descripci&oacute;n</th>
                    <th>Fecha<br>env&iacute;o</th>
                    <th>Fecha<br>vencimiento</th>
                    <th>Anexos</th>
                    
                    <th></th>  
                    <th>Responsables</th>
                    
                    <c:if test="${usuario.permisoActual.areaBean.datos.nivel > 2}">
                        <th>Asignado</th>    
                        <th>Instruc<br>ci&oacute;n</th>
                    </c:if>
                    <th>Estatus<br/>Responsable</th>
                    <th>Avance</th>
                    <th>Fecha de<br/> atenci&oacute;n</th>
                    <th>D&iacute;as<br/>Proceso/<br/>Atenci&oacute;n</th>
                    <th>D&iacute;as retraso</th>                  
                </tr>
            </thead>
            <tbody>
                <c:set var="fondo" value="#FFFFFF"/>
                <c:set var="pendiente" value ="#FFBF00"/>
                <c:set var="atendido" value ="#088A08"/>
                
                 <c:forEach var="acuerdo" items="${acuerdosConsulta}">
                    <c:choose>
                        <c:when test="${fondo == '#f2f3f5'}">
                            <c:set var="fondo" value="#FFFFFF"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="fondo" value="#f2f3f5"/>
                        </c:otherwise>
                 </c:choose>
                     <tr style="background: ${fondo}">
                        <td rowspan="${acuerdo.noResponsables}"><a name="id${acuerdo.idAccion}"/><a href="consultaReuniondAcuerdos.do?idReunion=${acuerdo.idAsunto}">${acuerdo.idAsuntoConsecutivo}</a>
                            <br /><a href="generaCaratulaReunionPDF.do?idasunto=${acuerdo.idAsunto}"><img src="img/adobe_acrobat.png" width="20" border="0"/></a>
                        </td>
                        <%--<td rowspan="${acuerdo.noResponsables}">${acuerdo.idconsecutivo}</td> --%>
                        <td rowspan="${acuerdo.noResponsables}">
                            <b>${acuerdo.idconsecutivo}</b><br><br>
                            <c:if test="${acuerdo.estatus == 'P'}">
                                <c:if test="${acuerdo.estatusAcuerdo == 0}"><img src="imagen/bandera_verde.png" width="20" height="20"/></c:if>
                                <c:if test="${acuerdo.estatusAcuerdo == 1}"><img src="imagen/bandera_amarilla.png" width="20" height="20"/></c:if>
                                <c:if test="${acuerdo.estatusAcuerdo == 2}"><img src="imagen/bandera_roja.png" width="20" height="20"/></c:if>
                            </c:if>
                            <br>
                            <div id="estatusA${acuerdo.idAsunto}">
                            <c:if test="${acuerdo.estatus == 'P' && (usuario.permisoActual.areaBean.datos.nivel < 3 || vistaUsuario.capturaAsuntos)}">
                                <font style="color:#FFBF00">PENDIENTE</font>
                            </c:if>
                            <c:if test="${acuerdo.estatus == 'P' && usuario.permisoActual.areaBean.datos.nivel > 2}">
                                <br>
                                <a class="LinkAmarillo" href="#${acuerdo.idAsunto}" onClick="muestraAvancesAcuerdo('${acuerdo.idAsuntoConsecutivo}',${acuerdo.idAccion},${usuario.permisoActual.areaBean.datos.idarea},${usuario.permisoActual.areaBean.datos.nivel})" >
                                    <font style="color:${pendiente}">PENDIENTE</font>
                                </a>
                            </c:if>
                            <c:if test="${vistaUsuario.capturaAsuntos && acuerdo.estatus == 'A'}">
                                <font style="color:#088A08">ATENDIDO</font>
                            </c:if>
                            <c:if test="${usuario.permisoActual.areaBean.datos.nivel == 2 && acuerdo.estatus == 'A'}">
                                <font style="color:#088A08">ATENDIDO</font>
                            </c:if>
                            </div>
                        <%--<td rowspan="${acuerdo.noResponsables}">
                            <c:if test="${acuerdo.realizada == 'I'}">Interno</c:if>
                            <c:if test="${acuerdo.realizada == 'E'}">Externo</c:if>
                        </td>--%>
                        <c:if test="${acuerdo.estatus == 'A' && usuario.permisoActual.areaBean.datos.nivel == 3}">
                            <a class="LinkVerde" href="#${acuerdo.idAsuntoConsecutivo}" onClick="muestraAtendidoNiv3Acuerdos('${acuerdo.idAccion}', '${acuerdo.idAsuntoConsecutivo}','${usuario.permisoActual.areaBean.datos.dependede}')" >
                                <font style="color:${atendido}">ATENDIDO</font>
                            </a>
                        </c:if>
                        <c:if test="${acuerdo.estatus == 'A' && usuario.permisoActual.areaBean.datos.nivel > 3}">
                            <a class="LinkVerde" href="#${acuerdo.idAsuntoConsecutivo}" onClick="muestraAtendidoNiv4y5Acuerdos('${acuerdo.idAccion}', '${acuerdo.idAsuntoConsecutivo}','${usuario.permisoActual.areaBean.datos.idarea}','${usuario.permisoActual.areaBean.datos.nivel}')" >
                                <font style="color:${atendido}">ATENDIDO</font>
                            </a>
                        </c:if>
                        <br/>
                        <a href="#${acuerdo.idAsunto}" data-bs-toggle="modal" data-bs-target="#avancesAcTodos" onClick="resumenAvancesAcuerdoTodos(${acuerdo.idAccion},${acuerdo.idAsuntoConsecutivo})"><img src="imagen/avances.png" border="0" width="20" title="Mostrar todos los avances" alt="Mostrar todos los avances" /></a>
                            <c:if test="${usuario.permisoActual.datos.rol == 'A'}">
                                <a href="#${acuerdo.idAccion}" data-bs-toggle="modal" data-bs-target="#act_seguimAc" onClick="actividadesSeguimientoAcuerdos(${acuerdo.idAccion},${acuerdo.idconsecutivo},${acuerdo.idAsuntoConsecutivo},'0')"><img src="imagen/seg_actividades.png" border="0" width="20" title="Actividades seguimiento" alt="Actividades seguimiento" /></a>
                            </c:if>
                        </td>
                        <td rowspan="${acuerdo.noResponsables}">${acuerdo.descripcionFormatoHTML}</td>
                        <td rowspan="${acuerdo.noResponsables}">${acuerdo.fechaenvioFormatoTexto}</td>
                        <td rowspan="${acuerdo.noResponsables}">
                                <%--<c:if test="${acuerdo.prioridad == 'S'}"><div class="urgente">URGENTE</div><br/></c:if> 
                                <c:if test="${acuerdo.prioridad == '1'}"><div class="prioridad1">PRIORIDAD 1</div><br/></c:if> 
                                <c:if test="${acuerdo.prioridad == '2'}"><div class="prioridad2">PRIORIDAD 2</div><br/></c:if> 
                                <c:if test="${acuerdo.prioridad == 'A'}"><div class="admvo">ADMINISTRATIVO</div><br/></c:if>
                                --%>
                               <c:if test="${acuerdo.prioridad == 'S'}"><span class="badge bg-danger">URGENTE</span><br/></c:if> 
                               <c:if test="${acuerdo.prioridad == '1'}"><span class="badge bg-warning">PRIORIDAD 1</span><br/></c:if> 
                               <c:if test="${acuerdo.prioridad == '2'}"><span class="badge atNormal">ATENCIÓN NORMAL</span><br/></c:if>
                               <c:if test="${acuerdo.prioridad == 'A'}"><span class="badge bg-primary">ADMINISTRATIVO</span><br/></c:if> 
                                <div id="fechaAtender${acuerdo.idAccion}">
                                <c:choose>
                                    <c:when test="${usuario.permisoActual.datos.rol == 'A' && acuerdo.estatus == 'P'}">
                                        <a href="#${acuerdo.idAccion}" data-bs-toggle="modal" data-bs-target="#reprogramaAcModal" onClick="reprogramaAcuerdo(${acuerdo.idAccion},${acuerdo.idconsecutivo},${acuerdo.idAsuntoConsecutivo},'${vistaUsuario.capturaAsuntos}','${acuerdo.estatus}' );">${acuerdo.fechaoriginalFormatoTexto}</a> 
                                    </c:when>
                                    <c:when test="${usuario.permisoActual.datos.rol != 'A' || acuerdo.estatus == 'A'}">
                                        ${acuerdo.fechaoriginalFormatoTexto}
                                    </c:when>
                                </c:choose>  
                                <br>        
                                </div>        

                                <div id="consultaReprograma${acuerdo.idAccion}">  
                                    <c:if test="${acuerdo.reprogramado}">
                                    <br/> 
                                    <!--<div class="reprograma" onClick="muestraPopup('muestraProgramacionesAcuerdo.do?idacuerdo=${acuerdo.idAccion}')"> <u>Reprogramaciones</u></div>    -->
                                    <a class="reprograma" href="#${acuerdo.idAccion}" data-bs-toggle="modal" data-bs-target="#reprogramacionesACModal" onClick="muestraReprogramaAc('${acuerdo.idAccion}','${acuerdo.idAsuntoConsecutivo}','${vistaUsuario.capturaAsuntos}','${acuerdo.estatus}')" >
                                    	<font style="color:${reprograma}">Reprogramaciones</font>
                                    </a>
                                    </c:if> 
                                </div>
                        </td>
                        <td rowspan="${acuerdo.noResponsables}">
                          <c:if test="${acuerdo.anexos != null}">   
                            <c:forEach var="anexoAsunto" items="${acuerdo.anexos}">
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
                                    <img src="${imagen}" alt="${anexoAsunto.nombrearchivo}" title="${anexoAsunto.nombrearchivo}" border="0" width="20" height="20"/></a><c:if test="${vistaUsuario.capturaAsuntos}">
                                    &nbsp;&nbsp;<a href="eliminaAnexo.do?idAnexo=${anexoAsunto.idanexo}&modulo=K" onClick="return confirmar(this.form)"><img src="img/eliminar_doc.png" title="Eliminar anexo" border="0" width="20" height="20"/></c:if>
                                    <!--&nbsp;&nbsp;<a href="agregarAnexo.jsp?idAnexo=${anexoAsunto.idanexo}&modulo=K" onClick="return confirmar(this.form)"><img src="img/edit-add-3.ico" title="Agregar anexo" border="0" width="15" height="15"/></a>-->
                                </a><br>
                                
                            </c:forEach>
                          </c:if>       
                        </td>

                        <td rowspan="${acuerdo.noResponsables}">
                            <c:if test="${vistaUsuario.delegaAsuntos}">
                             <%--<a href="editaAcuerdo.do?idasunto=${acuerdo.idAsunto}&idacuerdo=${acuerdo.idAccion}"><img src="imagen/Editar-asunto.png" border="0" /></a><br>--%>&nbsp;
                            </c:if>
                            <c:if test="${vistaUsuario.capturaAsuntos}">
                                <a href="editaAcuerdo.do?idasunto=${acuerdo.idAsunto}&idacuerdo=${acuerdo.idAccion}"><img src="imagen/editar.png" width="20" border="0" /></a><br>
                                <a href="eliminaAcuerdo.do?idasunto=${acuerdo.idAsunto}&idacuerdo=${acuerdo.idAccion}"><img src="imagen/eliminar.png" width="20" border="0" /></a>
                            </c:if> 
                        </td>
                       
                        <c:set var="bandera" value="false" />      
                        <c:forEach var="responsable" items="${acuerdo.responsables}">
                                <c:if test="${bandera == true}">
                                    <tr>
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
                                               <a  href="consultaAcuerdoAsignado.do?idacuerdo=${acuerdo.idAccion}&idResponsable=${responsable.area.idarea}" title="${responsable.area.nombre}" class="delegado">${nomArea}</a> 
                                            </c:otherwise>       
                                            </c:choose>
                                            
                                            <c:if test="${responsable.datos.delegado == 'S'}">
                                                <a id="a1" href="#" onMouseOver="return atributoTitleAcuerdo('${responsable.area.idarea}',${acuerdo.idAccion}, this)">
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
				<c:set var="ide" value="${acuerdo.idAsunto}${responsable.area.idarea}"/>
                                    <c:choose>
                                        <c:when test="${vistaUsuario.capturaAsuntos && responsable.datos.avance == 99 && responsable.datos.estatus == 'P'}">
                                            <div id="estatus${acuerdo.idAccion}${responsable.area.idarea}">
                                                <a class="LinkAmarillo" href="#${acuerdo.idAccion}" data-bs-toggle="modal" data-bs-target="#asignaAtendidoACModal" onClick="cambiaAtendido(${acuerdo.idAsunto},'${acuerdo.idAccion}',${responsable.area.idarea},'','A');" >
                                                    <font style="color:#FFBF00">PENDIENTE</font>
                                                </a>
                                            </div>
                                        </c:when>    
                                        <%-- Antes <c:when test="${vistaUsuario.capturaAsuntos && responsable.datos.estatus == 'A' || usuario.permisoActual.areaBean.datos.nivel > 2}"> 
                                        <c:when test="${(vistaUsuario.capturaAsuntos && responsable.datos.estatus == 'A') || (usuario.permisoActual.areaBean.datos.nivel > 2 && responsable.datos.estatus == 'A')}">--%>
                                        <c:when test="${(vistaUsuario.capturaAsuntos && responsable.datos.estatus == 'A')}">
                                            <div id="${acuerdo.idAccion}${responsable.area.idarea}"><font style="color:#088A08"><a href="javascript:void(0)" onClick="cambiaEstatus('${acuerdo.idAsunto}','${acuerdo.idAccion}','${responsable.area.idarea}')">ATENDIDO</a></font></div>
                                        </c:when>
                                        <c:when test="${responsable.datos.estatus == 'C'}">                                                 
                                            <font style="color:#FA5858">CANCELADO</font>
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
                                <%--<c:if test="${acuerdo.estatus == 'A' && usuario.permisoActual.areaBean.datos.nivel == 2}">
                                    <a class="LinkVerde" href="#${acuerdo.idAsuntoConsecutivo}" onClick="muestraAtendidoNiv3Acuerdos('${acuerdo.idAccion}', '${acuerdo.idAsuntoConsecutivo}','${usuario.permisoActual.areaBean.datos.idarea}',${responsable.area.idarea})" >
                                        <font style="color:${atendido}">ATENDIDO</font>
                                    </a>
                                </c:if>--%>
                                </td>
                                
                                <td>
                                    <%--<c:choose>
                                        <c:when test="${responsable.area.idarea == usuario.permisoActual.areaBean.datos.idarea || usuario.permisoActual.datos.rol == 'A'}"> --%>
                                            <%-- <a href="consultaCapturaAvanceAcuerdo.do?idasunto=${acuerdo.idAsunto}&idacuerdo=${acuerdo.idAccion}&idResponsable=${responsable.area.idarea}&idRSuperior=0#id${asunto.idasunto}" title="${responsable.ultimoAvance}">${responsable.datos.avance} %</a> --%>
                                                <c:choose>
                                                <c:when test="${responsable.datos.estatus != 'C'}">
                                                    <a href="consultaCapturaAvanceAcuerdo.do?idasunto=${acuerdo.idAsunto}&idacuerdo=${acuerdo.idAccion}&idResponsable=${responsable.area.idarea}&idRSuperior=0&idRTabla=${responsable.datos.idresponsable}" title="${responsable.ultimoAvance}"><div id="av${acuerdo.idAccion}${responsable.area.idarea}">${responsable.datos.avance} %</div></a>
                                                </c:when>    
                                                <c:when test="${responsable.datos.estatus == 'C' && responsable.datos.avance > 0}">
                                                    <a href="consultaAvancesCanceladosAcuerdo.do?idasunto=${acuerdo.idAsunto}&idacuerdo=${acuerdo.idAccion}&idResponsable=${responsable.area.idarea}&idRSuperior=0&idRTabla=${responsable.datos.idresponsable}">${responsable.datos.avance} %</a>
                                                </c:when>                                        
                                                <c:when test="${responsable.datos.estatus == 'C' && responsable.datos.avance == 0}">
                                                    ${responsable.datos.avance} %
                                                </c:when>                                            
                                                </c:choose>
                                        <%--</c:when>
                                        <c:otherwise>
                                            ${responsable.datos.avance} %  
                                        </c:otherwise> 
                                    </c:choose>    --%>
                                   <a href="#${acuerdo.idAsunto}" data-bs-toggle="modal" data-bs-target="#avancesAcuerdoModal" onClick="resumenAvancesAcuerdo(${acuerdo.idAsunto},${acuerdo.idAccion},${responsable.area.idarea},${acuerdo.idAsuntoConsecutivo})"><img src="img/arrow.gif" border="0" title="Resumen avances" alt="Resumen avances" /></a>
                                   
                                </td>
                                
                                <td><div id="fechaA${acuerdo.idAccion}${responsable.area.idarea}">${responsable.fechaatencionFormatoTexto}</div></td>
                                <td><div id="diasA${acuerdo.idAccion}${responsable.area.idarea}"><c:if test="${responsable.datos.diasatencion > 0}">${responsable.datos.diasatencion}</c:if></div></td>
                                <td><div id="diasR${acuerdo.idAccion}${responsable.area.idarea}"><c:if test="${responsable.datos.diasretraso > 0}">${responsable.datos.diasretraso}</c:if></div></td>
                                </tr>
                                <c:set var="bandera" value="true"/>
                        </c:forEach>
                        
    
                </c:forEach>
            </tbody>
          </table>
          </div>

          <c:if test="${reunionConsulta == null}">
            <ul id="menu" class="container__menu container__menu--hidden"></ul>
            <jsp:include page="jspConsulta/paginadorAcuerdos.jsp" flush="true" />                              
          </c:if>
          <jsp:include page="dialogoAcuerdo.jsp" flush="true" />      
       </c:if>
       <c:if test="${empty acuerdosConsulta}">
           No existen acuerdos asignados al área para esta reuni&oacute;n.
       </c:if> 
        <%--<c:if test="${reunionConsulta != null && vistaUsuario.capturarAcuerdos &&
                                        ((reunionConsulta.responsable.datos.idarea == usuario.permisoActual.areaBean.datos.idarea) || (usuario.permisoActual.datos.rol == 'A'))}">--%>
        <c:if test="${reunionConsulta != null && vistaUsuario.capturarAcuerdos && (usuario.permisoActual.datos.rol == 'A')}">
            <form action="iniciaCapturaAcuerdos.do" method="post">
                <input type="hidden" name="idasunto" value="${reunionConsulta.idasunto}"/>
                <input type="hidden" name="pagina" value="${pagActual}"/>
                <input type="submit" value="Capturar más acuerdos"/>
            </form>  
        </c:if>    

<div class="modal fade" id="avancesAcTodos" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
              <h4 class="modal-title">Desgloce de avances</h4>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <div id="reunionStr"></div><br/><br/>
                    <table class="table table-bordered table-hover table-striped tablaPeq" cellspacing="3"  >
                    <thead id="headAvances">
                        <tr>
                            <th>Fecha</th><th>Porcentaje</th><th>Descripción</th><th>Realizó</th>
                        </tr>
                    </thead>
                    <tbody id="resumenAvancesAc"></tbody>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-outline-danger" data-bs-dismiss="modal">Cerrar</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="avancesAcuerdoModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
              <h4 class="modal-title">Desgloce de avances</h4>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <div id="reunionStrAc"></div><br/><br/>
                    <table class="table table-bordered table-hover table-striped tablaPeq" cellspacing="3"  >
                    <thead id="headAvances">
                        <tr>
                            <th>Fecha</th><th>Porcentaje</th><th>Descripción</th><th>Realizó</th>
                        </tr>
                    </thead>
                    <tbody id="resumenAvancesAcuerdo"></tbody>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-outline-danger" data-bs-dismiss="modal">Cerrar</button>
            </div>
        </div>
    </div>
</div>
           
<div class="modal fade" id="act_seguimAc" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
     <div class="modal-dialog modal-lg">
         <div class="modal-content">
             <div class="modal-header">
               <h4 class="modal-title">Actividades de seguimiento (acuerdos)</h4>
               <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
             </div>
             <div class="modal-body">
                 
        <div id="asuntoStr2"></div>
        <div>
            <table>
                <thead>
                    <tr><td>Asunto</td><td>Acuerdo</td><td>Filtrar área:</td></tr>
                </thead>
                <tbody id="headDial">
                    <tr><td><input type="text" size="10" readonly="readonly" id="id_cons"/></td>
	                    <td><input type="text" size="10" readonly="readonly" id="acuerdo"/></td>
                    <td><select id="selArea"></select></td></tr>
                </tbody>
            </table>
        </div>
        <table align="center" width="100%" class="table table-bordered table-hover table-striped tablaPeq" >
        <thead>
            <tr><td>Fecha</td><td>Anexos</td><td>Descripción</td><td>Área</td><td>Realizó</td><td>Editar</td><td>Eliminar</td></tr>
        </thead>
        <tbody id="actividadesSeguimAcuerdos">
        </tbody>
        </table>
        <br>
        <div id="agregaActiv">
            <form name="formaAnexo" id="formaAnexo" method="post" enctype="multipart/form-data" accept-charset="UTF-8">
                <input type="hidden" name="tipo" id="tipo" value="grabarActividad">
                <input type="hidden" name="idaccion" id="idaccion">
                <input type="hidden" name="asuntoOacuerdo" id="asuntoOacuerdo" value="acuerdo">
                <input type="hidden" name="altaEdita" id="altaEdita" value="A">
                <input type="hidden" name="idactividadE" id="idactividadE">
                <table align="center" width="100%" id="reprogramacionesAc2" class="table table-bordered table-hover table-striped tablaPeq">
                    <thead>
                        <tr><td>Fecha</td><td>Área</td><td>Descripción</td></tr>
                    </thead>
                    <tbody>
                        <tr><td><input type="text" name="fechaAct" id="fechaAct" size="15" readonly="readonly" data-uk-datepicker="{format:'DD/MM/YYYY'}"></td>
                             <td><select id="areaResp" name="areaResp"></select></td>
                            <td><textarea name="descripAct" id="descripAct" rows="4" cols="50"></textarea></td></tr>
                        <tr><td>Anexo</td><td colspan="2"><input type="file" name="archAct" id="archAct"></td></tr>
                    </tbody>
                </table>
                <br>
                <button type="button" id="guardarAct" onClick="subeArchivo()" data-bs-dismiss="modal">Guardar</button>
            </form>	
        </div>

             </div>
             <div class="modal-footer">
                 <button class="btn btn-outline-danger" data-bs-dismiss="modal">Cerrar</button>
             </div>
         </div>
     </div>
 </div>
           
<div class="modal fade" id="reprogramacionesACModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
     <div class="modal-dialog">
         <div class="modal-content">
             <div class="modal-header">
               <h4 class="modal-title">Reprogramaciones</h4>
               <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
             </div>
             <div class="modal-body">
                 <div id="reunion"></div>
                 <table align="center" width="100%" id="reprogramacionesAc" class="table table-bordered table-striped tablaPeq">
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

<div class="modal fade" id="reprogramaAcModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <input type="hidden" id="permisoReg">
            <input type="hidden" id="estatusReg">
            <div class="modal-header">
              <h4 class="modal-title">Reprogramación</h4>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form name="frmReprogramaAcuerdo">
                   Acuerdo <input name="idAC" id="idAC" size="10" disabled=""/>
                   <br><br>
                   Fecha reprogramaci&oacute;n:<br>
                   <input type="text" name="fecharepAc" id="fecharepAc" readonly="1" size="15" data-uk-datepicker="{format:'DD/MM/YYYY'}">
                   <br><br>
                   &Aacute;rea solicitante:</br>
                   <select name="areasolAC" id="areasolAC" style="width: 250px"></select>
               </form>
            </div>
            <div class="modal-footer">
                <button class="btn btn-outline-primary" onclick="reprogramaAC()" data-bs-dismiss="modal">Reprograma</button>
                <button class="btn btn-outline-danger" data-bs-dismiss="modal">Cerrar</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="cuadroAtPendientes" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
 <div class="modal-dialog">
     <div class="modal-content">
         <div class="modal-header">
           <h4 class="modal-title">Cuadro resumen (acuerdos)</h4>
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
        
<div class="modal fade" id="asignaAtendidoACModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
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
            </div>
            <div class="modal-footer">
                <button class="btn btn-outline-primary" onclick="asignaAtendidoAC()" data-bs-dismiss="modal">Atendido</button>
                <button class="btn btn-outline-danger" data-bs-dismiss="modal">Cerrar</button>
            </div>
        </div>
    </div>
</div>