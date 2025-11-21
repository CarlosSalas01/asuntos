<%@page contentType="text/html"%>
<%@page pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>${nomSistema}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link rel="stylesheet" href="styles/Estilo.css" type="text/css">
        <link href="assets/css/bootstrap.min.css" rel="stylesheet" >
        <link href="assets/css/menu.css" type="text/css" rel="stylesheet" />
        <link rel="stylesheet" href="assets/css/all.css" /></link>
        <script src="js/jquery-3.5.0.min.js"></script>
        <script type="text/javascript" src="assets/js/jquery-ui-1.10.4.custom.js"></script>
        <script src="assets/js/bootstrap.min.js"></script>
        <script>
        window.dataLayer = window.dataLayer || [];
        function gtag(){dataLayer.push(arguments);}
        gtag('js', new Date());
        gtag('config', 'G-JCRTD25YCK');
        
        
        
        </script>
    </head>
    <body>
    <c:if test="${multipermisos != null}">
        
    <div id="nav" data-spy="affix" data-offset-top="1">
        <nav class="network-nav navbar navbar-static-top" role="navigation">
            <div class="container-fluid">
                <div class="navbar-header">
                    <img class="menu-brand" align="center" src="img/INEGI_k.png" width="150">
                </div>
                <!--<div class="collapse navbar-collapse center-block" id="bs-example-navbar-collapse-1">-->
                <div>
                    <ul class="nav navbar-nav navbar-right" id="main-menu-items" style="font-size: 20px; color: #f8f7f7; margin-top: 20px;">
                        Sistema de Seguimiento de asuntos de la UGMA
                    </ul>
                </div>
            </div>
        </nav>
        <!--<nav class="navbar-custom navbar navbar-inverse navbar-static-top top-nav" id="nav" role="navigation"></nav>-->
    </div>
    
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
      <div class="container-fluid justify-content-start">
        <div class="col-1"><a class="navbar-brand" href="#"><i class="fa-solid fa-house"></i></a></div>
        <!--<div class="col-1"><a href="#"><img src="img/adobe_acrobat.png" title="Consultar manual de usuario"></a></div>-->
      </div>
    </nav>

        <br>
        <div class="float-end cerrarSesion">
            <i class="fas fa-user"></i> ${usuario.nombreCompleto} | <a href="logout.do">Cerrar sesi&oacute;n</a>
        </div>
    </c:if>
    <c:if test="${multipermisos == null}">
	    <jsp:include page="encabezado.jsp" />
    </c:if>
    <br>
    
    <div class="row justify-content-center">
        <br>
        <div class="col-5">
             <div class="card">
                <div class="card-header">
                    <h5>Tiene asignado más de un permiso en este sistema, seleccione uno de los siguientes:</h5>
                </div>
               <div class="card-body">
                   
                    <div class="list-group">
                      <a class="list-group-item list-group-item-action active text-lg-center">
                        Selección de permiso
                      </a>
                        <c:if test="${usuario.datos.responsable == 'S'}">
                            <c:forEach var="permiso" items="${usuario.permisos}">
                           
                                 <c:if test="${permiso.areaBean.datos.dependede == areaDepende}">
                                   <a href="seleccionRol.do?idPermiso=${permiso.datos.idpermiso}" class="list-group-item list-group-item-action">${permiso.descripcion}</a> 
                                   <a href="otroNivelRol.do?areaDepende=${permiso.areaBean.datos.idarea}"><img src="img/arrow.gif" width="20" alt="Subáreas" border="0" /></a>  
                                 </c:if>
                                 
                                 <c:if test="${permiso.datos.idarea == 0 && areaDepende == 1 }">  
                                    ${permiso.datos.idpermiso}
                                    <a href="seleccionRol.do?idPermiso=${permiso.datos.idpermiso}" class="list-group-item list-group-item-action">${permiso.descripcion}</a> 
                                 </c:if>  
                            </c:forEach>
                        </c:if>
                        
                        <c:if test="${usuario.datos.responsable == 'N'}">   
                         <c:forEach var="permiso" items="${usuario.permisos}">
                           
                              <a href="seleccionRol.do?idPermiso=${permiso.datos.idpermiso}" class="list-group-item list-group-item-action">${permiso.descripcion}</a> 
                            
                              
                         
                         </c:forEach>
                        </c:if>
                    </div>                   
               </div>
             </div>
            
        </div>
        
    </div>
    
</body>
</html>
