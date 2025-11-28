<%-- 
    Document   : consultaKEET
    Created on : 4/11/2012, 10:07:02 AM
    Author     : jacqueline
--%>


<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>${nomSistema}</title>
    </head>
    <body>
      <h6>SIA</h6>
      <c:if test="${not empty asuntosConsulta}">
         <table width="100%" border="1" cellpadding="2" cellspacing="0" class="tablaDeta">       

             <tr>
             	<th>Consec.</th>
                <th>ID Asunto</th>
                <%--<th>Estatus</th> --%>
                <%--<th>Pres.</th> --%>
                <th>Turno</th>
                <%--<th>Clasificaci&oacute;n</th> --%>
                <th>Asunto</th>
                <th>Instrucci&oacute;n</th>
                <%--<th>Urgente</th>--%>
                <th>Fecha envío</th>
                <th>Fecha vencimiento</th>
                <th>Ultima reprogramación</th>
                <%--<th>Observaciones</th>--%>
                <th>Destinatarios</th>
                <th>Estatus Resp.</th>
                <th>Avance</th>
                <th>Último Avance</th>
                <th>Fecha de atenci&oacute;n</th>
                <th>D&iacute;as Proceso/Atenci&oacute;n</th>
                <th>D&iacute;as retraso</th>
                
            </tr>
            <tbody>
	            <c:set var="cons" value="${0}"/>
                <c:forEach var="asunto" items="${asuntosConsulta}">
	                <c:set var="cons" value="${cons + 1}" />
                    <tr>
	                    <td rowspan="${asunto.noResponsables}" align="center">${cons}</td>
                        <%--IDAsunto --%>
                        <td rowspan="${asunto.noResponsables}">${asunto.idconsecutivo}</td>
                        <%--Estatus
                        <td rowspan="${asunto.noResponsables}">
                            <c:if test="${asunto.estatus == 'P'}">PENDIENTE</c:if>
                            <c:if test="${asunto.estatus == 'A'}">ATENDIDO</c:if>
                        </td> --%>
                        <%--Presidencia 
                        <td rowspan="${asunto.noResponsables}">
                            <c:if test="${asunto.presidencia == 'P'}">
                                <b>Presidencia</b><br>C/Resp.<br><br>
                            </c:if>
                            <c:if test="${asunto.presidencia == 'N'}">
                                <b>Presidencia</b><br>S/Resp.<br><br>
                            </c:if>
                        </td>--%>
                        <%--Turno --%>
                       <td rowspan="${asunto.noResponsables}">'${asunto.nocontrol}</td>
                       <%--Clasificacion
                       <td rowspan="${asunto.noResponsables}">
                            <c:if test="${asunto.fuente == 'I'}">Interno</c:if>
                            <c:if test="${asunto.fuente == 'E'}">Externo</c:if>    
                       </td>--%> 
                       <%--Asunto --%>
                        <td rowspan="${asunto.noResponsables}">
                            ${asunto.descripcionFormatoHTML}
                        </td>
                        <%--Instrucción --%>
                        <td rowspan="${asunto.noResponsables}">
                            ${asunto.estatustexto}
                        </td>

                        <%--Urgente 
                        <td rowspan="${asunto.noResponsables}">${asunto.urgente}</td>--%>
                        <%--Fecha envio --%>
                        <td rowspan="${asunto.noResponsables}">${asunto.fechaingresoFormatoTexto}</td>
                        <%--Fecha atender --%>
                        <td rowspan="${asunto.noResponsables}">${asunto.fechaoriginalFormatoTexto}</td>
                        <%--Fecha ultima reprogramacion --%>
                        <td rowspan="${asunto.noResponsables}">${asunto.fechaUltimaReprogramacion}</td>
                       <%--Observaciones 
                       <td rowspan="${asunto.noResponsables}">${asunto.observaciones}</td>--%>                                  
                        
                        <c:set var="bandera" value="false" />      
                        <c:forEach var="responsable" items="${asunto.responsables}">
                                <c:if test="${bandera == true}">
                                  <tr>
                                </c:if>
                                <%--Nombre Responsable --%>     
                                <td>
                                    <c:if test="${responsable.area.nivel == 2}">${responsable.area.siglas}</c:if>                                      
                                    <c:if test="${responsable.area.nivel > 2}">${responsable.area.nombre}</c:if>                                      
                                </td>
                                <%--Estatus Responsable --%>     
                                <td>
                                    <c:choose>
                                        <c:when test="${responsable.datos.estatus == 'P'}">
                                          PENDIENTE
                                        </c:when>    
                                        <c:when test="${responsable.datos.estatus == 'A'}">
                                           ATENDIDO
                                        </c:when>
                                        <c:when test="${responsable.datos.estatus == 'C'}">                                                 
                                           CANCELADO
                                        </c:when>       
                                    </c:choose>
                                </td>
                                <%--Avance --%>     
                                <td>${responsable.datos.avance} %</td>
                                <%--Ultimo avance --%>    
                                <td>${responsable.ultimoAvanceGlobal}</td>
                                <%--Fecha de atención --%>
                                <td>${responsable.fechaatencionFormatoTexto}</td>
                                <%--Dias de atención--%>
                                <td>${responsable.datos.diasatencion}</td>
                                <%--Dias de retraso--%>    
                                <td>${responsable.datos.diasretraso}</td>
                                </tr>
                                <c:set var="bandera" value="true"/>
                        </c:forEach>
                </c:forEach>
              
            </tbody>
        </table>
       </c:if>
       <c:if test="${empty asuntosConsulta}">
           No existen asuntos SIA para este usuario con dichas características.
       </c:if> 
    </body>
</html>
