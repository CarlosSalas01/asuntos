<head>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/superSelect.tld" prefix="s"%>
<link rel="stylesheet" href="styles/hojaEstilos.css" type="text/css"/>
<link rel="stylesheet" href="styles/consultas.css" type="text/css"/>
<link type="text/css" href="calendario/ui.all.css" rel="stylesheet" />
<script type="text/javascript" src="js/jquery-3.5.0.min.js"></script>
<script src="assets/js/bootstrap.min.js"></script>
<script type="text/javascript" src="calendario/ui.core.js"></script>
<script type="text/javascript" src="calendario/ui.datepicker.js"></script>
<script type="text/javascript" src="assets/js/jquery-ui-1.10.4.custom.js"></script>

<script language="javascript">
	$(function() {
            //$("#resultado").hide();
		$('#fecha1').datepicker({
			changeMonth: true,
			changeYear: true
		});
		$('#fecha2').datepicker({
			changeMonth: true,
			changeYear: true
		});
	});
function validaFechas() {
	var fec1 = document.getElementById('fecha1').value.substring(6,10)+document.getElementById('fecha1').value.substring(3,5)+document.getElementById('fecha1').value.substring(0,2);
	var fec2 = document.getElementById('fecha2').value.substring(6,10)+document.getElementById('fecha2').value.substring(3,5)+document.getElementById('fecha2').value.substring(0,2);
	if(parseInt(fec1) > parseInt(fec2)) {
		alert("Fecha inicio del asunto debe menor que fecha de término");
		document.getElementById('fecha1').focus();
		return false;
	} else {
            //$("#resultado").css("display", "block");
            $("#resultado").show();
            return true;
        }
}
</script>
</head>
<jsp:include page="encabezado.jsp" flush="true" />
   <form action="busquedaGeneral.do" name="filtrofrm">
        <div class="row alert alert-success" role="alert">
            <div class="col-12 text-center">Consulta general de asuntos</div>
        </div>
       <div class="row">
        <div class="row justify-content-md-center">
            <div class="col-6">
           <div class="row">
               <div class="col-2">
                   Tipo de fecha: 
                   <s:superSelect name="fechas" indice="${filtroConsultaGeneral.tipoFecha}" elementos="${filtroConsultaGeneral.lfechas}" />    
               </div>
                   <div class="col-2">desde: <input type="text" class="form-control" name="fecha1" id="fecha1" value="${filtroConsultaGeneral.fechaInicioFormat}" size="15" readonly="readonly"></div>
               <div class="col-2">a: <input type="text" class="form-control" name="fecha2" id="fecha2" value="${filtroConsultaGeneral.fechaFinalFormat}" size="15" readonly="readonly"></div>
           </div>
           <div class="row col-12">&nbsp;</div>
           <div class="row">
                <div class="col-1">&Aacute;rea:</div>
                <div class="col-5">
                    <select name="areaFiltro" id="areaFiltro" class="form-select">
		       <option value="0">Todas...</option>
                       <c:forEach items="${areasConsulta.areasResponsables}" var="area"> 
                            <c:choose>
                                <c:when test="${area.idarea == filtroConsulta.idarea}">
                                    <c:set var="sel" value="selected"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="sel" value=""/>
                                </c:otherwise>
                            </c:choose> 
                           <option value="${area.idarea}" ${sel}>${area.nombre}</option>
                       </c:forEach>
                </select>
                </div>
               <div class="col-2">Busca información</div>
               <div class="col-2"><input type="text" name="texto" class="form-control" id="texto" value="${filtroConsulta.texto}"></div>
               <div class="col-1"><button type="submit" class="btn btn-outline-primary" onclick="return validaFechas()">Consultar</button></div>
            </div>
            </div>
        </div>
       </div>
       <div class="row col-12">&nbsp;</div>
       <c:if test="${not empty resultadosBusqueda}">
        <div class="row justify-content-md-center">
            <div class="col-4" id="resultado">
                <div class="card">
                  <div class="card-header">
                    Resultados:
                  </div>
                    <div class="card-body">
                        <table class="table table-bordered table-striped">
                            <head>
                            <tr><td>Tipo Asunto</td><td>Registros</td></tr>
                            </head>
                            <tbody>
                             <c:forEach var="e" items="${resultadosBusqueda}">
                                 <tr><td>${e.descripcion}</td>
                                     <td align="center"><a href="ruteaConsultaAsuntos.do?modo=${e.tipoAsunto}">${e.cantidad}</a></td>
                                 </tr>
                             </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            
            </div>
        </div>
       </c:if>
<%--
      <table width="100%" class="tablaDeta">
    	<tr>
            <td><h2>Consulta General con fecha de 
                    <s:superSelect name="fechas" indice="${filtroConsultaGeneral.tipoFecha}" elementos="${filtroConsultaGeneral.lfechas}" />    
                    desde
                    <input type="text" name="fecha1" id="fecha1" value="01/01/${anioHoy}" size="15" readonly="readonly">
                    &nbsp;&nbsp;&nbsp;a:
                    <input type="text" name="fecha2" id="fecha2" value="${filtroConsultaGeneral.fechaFinalFormat}" size="15" readonly="readonly">
                </h2>
            </td>
            <td aling="right">
                <img src="imagen/deshacer.gif" onClick="document.filtrofrm.action='reiniciaConsultaGeneral.do';document.filtrofrm.submit();" title="Reiniciar filtro"/>
            </td>
        </tr>
        <tr><td colspan="2">
             &Aacute;rea: <select name="areaFiltro" id="areaFiltro" class="combo">
		       <option value="0">Todas...</option>
                       <c:forEach items="${areasConsulta.areasResponsables}" var="area"> 
                            <c:choose>
                                <c:when test="${area.idarea == filtroConsulta.idarea}">
                                    <c:set var="sel" value="selected"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="sel" value=""/>
                                </c:otherwise>
                            </c:choose> 
                           <option value="${area.idarea}" ${sel}>${area.nombre}</option>
                       </c:forEach>
                </select>  
          &nbsp;&nbsp;
          Busca informaci&oacute;n: <input type="text" size="70" name="texto" id="texto" value="${filtroConsulta.texto}">
          &nbsp;&nbsp;
          <input type="submit" value="Consultar" onclick="return validaFechas()">&nbsp;&nbsp; 
        </td></tr>
        </table>--%>
   </form>
<%--
   <br/><br/>
   <c:if test="${not empty resultadosBusqueda}">
     Resultados:
        <table id="TablaResultados" border="1" style="font-size:11px; border-collapse:collapse;" bgcolor="#EAEAEA">
            <tr><td>No. Encontrados</td><td>Tipo Asunto</td></tr>
            <c:forEach var="e" items="${resultadosBusqueda}">
                <tr><td>${e.descripcion}</td>
                    <td><a href="ruteaConsultaAsuntos.do?modo=${e.tipoAsunto}">${e.cantidad}</a></td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
--%>