<head>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <%@ taglib uri="/WEB-INF/tlds/superSelect.tld" prefix="s"%>
    <link rel="stylesheet" href="styles/hojaEstilos.css" type="text/css">
    <link rel="stylesheet" href="styles/consultas.css" type="text/css">
    <script src="js/jquery-3.5.0.min.js"></script>
    <script src="assets/js/bootstrap.min.js"></script>
    <script language="JavaScript" src="js/funcionesConsulta.js"></script>
    <link rel="stylesheet" href="styles/jquery-ui.css" />
    <script language="JavaScript" src="js/date-picker.js"></script>
    <script language="JavaScript" src="js/jquery-ui.js"></script>
    <script language="JavaScript" src="js/control.js"></script>
    <script>
        $(function () {
            $('#fecha1').datepicker({
                changeMonth: true,
                changeYear: true
            });
            $('#fecha2').datepicker({
                changeMonth: true,
                changeYear: true
            });

            $(".reunionSA").on("click", function () {
                return confirm("Procede el registro sin acuerdos?");
            });           
        });
    </script>
</head>
<jsp:include page="encabezado.jsp" flush="true" /> 
<jsp:include page="filtros/filtroReuniones.jsp" flush="true" />       
<c:if test="${not empty asuntosConsulta}">
    <jsp:include page="jspConsulta\paginadorReuniones.jsp" flush="true" />   
    <div class="table-responsive" id="contenedor_tabla">
        <table width="100%" class="table table-bordered tablaPeq tableR" id="tab_principal">
            <thead class="thead-scrollable">
                <tr class="encTablas">
                    <th>Id</th>
                    <th>Total<br>Acuerdos</th>
                    <th>Atendido<br>sin acuerdos</th>
                    <th>Fecha</th>
                    <th>Tema</th>
                    <th>Objetivo</th>
                    <th>Lugar</th>
                    <th>Horario</th>
                    <th>Respon-<br>sable</th>
                    <th>Corres-<br>ponsables</th>
                    <th>Asistentes</th>
                    <th>Anexos</th>
                        <c:if test="${vistaUsuario.capturaAsuntos}">
                        <th></th>
                        </c:if>
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
                        <td width="1%"><a name="id${asunto.idasunto}"/>${asunto.idconsecutivo}
                            <a href="generaCaratulaReunionPDF.do?idasunto=${asunto.idasunto}" target="_blank"> <img src="img/adobe_acrobat.png" width="20" border="0"/></a>
                            <br><br>
                            <c:if test = "${asunto.transversal}">
                                <br><br><div class="trans">TRANSVERSAL</div>
                            </c:if>
                        </td>
                        <c:if test="${asunto.estatus == 'A'}">
                            <td class="text-center">
                                <c:if test = "${usuario.permisoActual.datos.rol == 'A'}">
                                    <a href="iniciaCapturaAcuerdos.do?idasunto=${asunto.idasunto}&pagina=${paginador.paginaActual}&cambiaEstatus"><h6><span class="badge bg-primary">Captura Acuerdos</span></h6></a>
                                </c:if>
                            </td>
                            <td class="text-center">
                                <div class="alert alert-secondary" role="alert">Atendida sin acuerdos</div>
                            </td>
                        </c:if>
                        <c:if test="${asunto.estatus != 'A'}">
                            <td width="1%">
                                <%--<c:if test="${asunto.accionesRealizadas <= 0}"><div class="urgente">SIN ACUERDOS</div></c:if> --%>
                                <c:if test="${asunto.accionesRealizadas > 0}"><a href="consultaAcuerdosAsunto.do?idasunto=${asunto.idasunto}&pagina=${paginador.paginaActual}&totalR=${paginador.totalR}"><h6><span class="badge bg-secondary">${asunto.accionesRealizadas}</span></h6></a></c:if>

                                <%--<c:if test="${asunto.accionesRealizadas <= 0 && vistaUsuario.capturarAcuerdos && ((asunto.responsable.datos.idarea == usuario.permisoActual.areaBean.datos.idarea) || (usuario.permisoActual.datos.rol == 'A'))}">--%>
                                <c:if test="${asunto.accionesRealizadas <= 0 && vistaUsuario.capturarAcuerdos && (usuario.permisoActual.datos.rol == 'A') && (asunto.estatus == 'P') }">
                                    <c:if test = "${asunto.transversal && usuario.permisoActual.datos.rol == 'A'}">
                                        <a href="iniciaCapturaAcuerdos.do?idasunto=${asunto.idasunto}&pagina=${paginador.paginaActual}"><h6><span class="badge bg-primary">Captura Acuerdos</span></h6></a>
                                    </c:if>
                                    <c:if test = "${!asunto.transversal}">
                                        <a href="iniciaCapturaAcuerdos.do?idasunto=${asunto.idasunto}&pagina=${paginador.paginaActual}"><h6><span class="badge bg-primary">Captura Acuerdos</span></h6></a>
                                    </c:if>
                                </c:if>
                            </td>
                            <td><%--<c:if test="${asunto.accionesRealizadas <= 0 && 
                                              vistaUsuario.capturarAcuerdos &&
                                              ((asunto.responsable.datos.idarea == usuario.permisoActual.areaBean.datos.idarea) || (usuario.permisoActual.datos.rol == 'A'))}">--%>

                                <c:if test="${asunto.accionesRealizadas <= 0 && vistaUsuario.capturarAcuerdos && usuario.permisoActual.datos.rol == 'A' && (asunto.estatus == 'P')}">
                                      <a href="grabaReunionSAcuerdos.do?modu=R&idasunto=${asunto.idasunto}&idarea=${asunto.idarea}&idusuario=${usuario.permisoActual.datos.idusuario}" class="reunionSA"><h6><span class="badge bg-primary">Atender sin acuerdos</span></h6></a>
                                </c:if>
                                <c:if test="${asunto.accionesRealizadas <= 0}">&nbsp;</c:if>
                            </td>
                        </c:if>
                        <td width="5%">${asunto.fechaingresoFormatoTexto}</td>
                        <td width="1%">
                            <%--<c:if test="${usuario.permisoActual.datos.rol == 'A'}">
                                <a href="editaInfo.do?modulo=R&campo=Descripcion&idasunto=${asunto.idasunto}&descrip=${asunto.descripcion}" target="_blank" onClick="window.open(this.href, this.target, 'width=500,height=200'); return false;window.close();">${asunto.descripcion}</a>
                            </c:if>
                            <c:if test="${usuario.permisoActual.datos.rol != 'A'}"> --%>
                            ${asunto.descripcionFormatoHTML}

                        </td> 
                        <td width="30%">
                            <%--    <c:if test="${usuario.permisoActual.datos.rol == 'A'}">
                                                                <a href="editaInfo.do?modulo=R&campo=Tema&idasunto=${asunto.idasunto}&descrip=${asunto.estatustexto}" target="_blank" onClick="window.open(this.href, this.target, 'width=500,height=200'); return false;window.close();">${asunto.estatustexto}</a>
                            </c:if>
                                <c:if test="${usuario.permisoActual.datos.rol != 'A'}"> --%>
                            ${asunto.estatustexto}


                        </td>

                        <td width="5%">${asunto.lugar}</td> 
                        <td width="5%">${asunto.informesunidad}</td>
                        <td>
                            <c:if test="${asunto.responsable.datos.nivel == 2}">${asunto.responsable.datos.siglas}</c:if>                                      
                            <c:if test="${asunto.responsable.datos.nivel > 2}">${asunto.responsable.datos.nombre}</c:if>              
                            </td>
                            <td width="5%">
                            <c:forEach var="corresponsable" items="${asunto.corresponsables}">
                                ${corresponsable.datos.siglas}<br>
                            </c:forEach>
                        </td>
                        <td width="15%">${asunto.asistentesFormatoHTML}</td>
                        <td >
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
                                        <c:when test="${anexoAsunto.tipoDocumento == 6}">
                                            <c:set var="imagen" value="img/zip.png"/>
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
                            <td width="5%"> <a href="editaAsunto.do?idasunto=${asunto.idasunto}&tipo=2"><img src="imagen/editar.png" width="20" border="0" /></a>
                                <a href="eliminaAsunto.do?idasunto=${asunto.idasunto}"><img src="imagen/eliminar.png" width="20" border="0" /></a>
                            </td>
                        </c:if>

                    </c:forEach>
            </tbody>
        </table>
    </div>
    <ul id="menu" class="container__menu container__menu--hidden"></ul>
    <jsp:include page="jspConsulta\paginadorReuniones.jsp" flush="true" />        
</c:if>
<c:if test="${empty asuntosConsulta}">
    No existen Reuniones para este usuario con dichas características.
</c:if> 

