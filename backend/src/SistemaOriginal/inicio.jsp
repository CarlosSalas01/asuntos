<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8;">
        <meta name="viewport" content="width=device-width">
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <title>Sistema de Seguimiento de asuntos de la UGMA</title>
        <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <%@taglib uri="/WEB-INF/tlds/superSelect.tld" prefix="s"%>
        <link href="assets/css/custom.css" rel="stylesheet">
        <link rel="stylesheet" href="styles/consultas.css" type="text/css">
        <link rel="stylesheet" href="styles/EstilosNew.css" type="text/css">
        <!--<link rel="stylesheet" href="styles/Estilo.css" type="text/css">-->
        <link rel="stylesheet" href="styles/jquery-ui.css" />
        <link href="assets/css/bootstrap.min.css" rel="stylesheet" >
        <!--<link href="assets/font-awesome/css/font-awesome.min.css" rel="stylesheet">
        <link href="assets/font-awesome/web-fonts-with-css/css/fontawesome-all.css" rel="stylesheet" type="text/css">-->
        <link href="assets/css/menu.css" type="text/css" rel="stylesheet" />
        <script src="js/jquery-3.5.0.min.js"></script>
        <script type="text/javascript" src="assets/js/jquery-ui-1.10.4.custom.js"></script>
        <script src="assets/js/bootstrap.min.js"></script>
        <script language="JavaScript" src="js/cierreConvenio.js"></script>
        <script language="JavaScript" src="js/inicio.js"></script>
        <script>
            window.dataLayer = window.dataLayer || [];
            function gtag() {
                dataLayer.push(arguments);
            }
            gtag('js', new Date());
            gtag('config', 'G-JCRTD25YCK');
        </script>
        <style>
            .fuenteSmall{
                font-size:9px;
            }
            #footer {
                padding-top:5px;
                position: fixed;
                bottom: 0%;
                height: 30px;
                width: 100%;
                background: #002A5C;
                z-index: 1000;
            }
            .divportada{
                background: url(img/portada.jpg) no-repeat;
                width:100%;
                height:85%;
                background-size:cover;
            }
        </style>
    </head>
    <body>
        <jsp:include page="encabezado.jsp" />
        <br>
        <%--<c:if test="${usuario.permisoActual.datos.rol == 'R' && usuario.permisoActual.areaBean.datos.nivel > 2}">
            <div class="divportada">
        </c:if>--%>
        <input type="hidden" id="permisoActual" value="${usuario.permisoActual.datos.rol}"/>    
        <input type="hidden" id="areaActual" value="${usuario.permisoActual.areaBean.datos.idarea}"/>
        <input type="hidden" id="areasCaptura" value="${areasCaptura}"/>
        <input type="hidden" id="nivel" value="${usuario.permisoActual.areaBean.datos.nivel}"/>
        <input type="hidden" id="idArea" value="${usuario.permisoActual.datos.idarea}"/>
        <input type="hidden" id="rol" value="${usuario.permisoActual.datos.rol}"/>

        <div class="container-fluid">
            <div id="contenedor">

                <div class="col-md-10 offset-md-1"> <!--Es para centrar datos iniciales-->
                    <div class="row" style="margin-top: 50px;">
                        <<div class="col-md-12 col-sm-12 text-center">
                            <div id="leyendo"><img src="img/leyendo.gif" width="40px;"></div>
                        </div>
                        <div id="informa">
                            <div class="col-md-1 col-sm-1"></div>
                            <div class="col-md-10 col-sm-10" id="fechaHra"></div>
                            <div class="col-md-12">&nbsp;</div>
                            <div class="col-md-1 col-sm-1"></div>
                            <div class="col-md-10 col-sm-12" id="anios"></div>
                        </div>
                    </div>
                    <div class="row" id="resumenTotales">
                        <div class="col-md-10 col-sm-12 col-md-offset-1">
                            <div class="row tile_count">
                                <div class="col-md-3 col-sm-5 col-xs-7 tile_stats_count">
                                    <span class="count_top"><i class="fa fa-list-alt"></i> Total de asuntos </span>
                                    <div class="count" id="totalGral"></div>
                                </div>

                                <div class="col-md-3 col-sm-5 col-xs-7 tile_stats_count">
                                    <span class="count_top"><i class="fa fa-check-circle"></i> Asuntos atendidos</span>
                                    <div class="count green" id="totalAtendidos"></div>
                                </div>

                                <div class="col-md-3 col-sm-5 col-xs-7 tile_stats_count">
                                    <span class="count_top"><i class="fa fa-exclamation-circle"></i> Asuntos pendientes</span>
                                    <div class="count red" id="totalPendientes"></div>
                                </div>
                                <div class="col-md-3 col-sm-5 col-xs-7 tile_stats_count">
                                    <span class="count_top"><i class="fa fa-exclamation-circle"></i> Reuniones sin acuerdos</span>
                                    <div class="count" id="totalReuniones"></div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row" id="cuadroResumen" style="row-gap: 20px; margin-top: 20px;"></div>
                </div>


            </div>
        </div>
        <%--<c:if test="${usuario.permisoActual.datos.rol == 'R' || usuario.permisoActual.areaBean.datos.nivel > 2}">
        </div>
    </c:if>--%>
        <div class="modal fade" id="pendientes" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title">Asuntos pendientes</h4>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <table class="table table-bordered table-hover tablaPeq" cellspacing="3"  >
                                <thead id="headPend">
                                </thead>
                                <tbody id="desglocePend"></tbody>
                            </table>
                        </div>

                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-default" data-bs-dismiss="modal">Cerrar</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="text-center" id="footer"><p style="color: white;">Derechos Reservados &copy; INEGI | ${version}</p></div>
    </body>
</html>
