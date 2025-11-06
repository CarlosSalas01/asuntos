// JavaScript Document
$(function () {
    var hoy = new Date();
    var anioHoy = hoy.getFullYear();
    $("#informa").hide();
    var idAdjunta=$("#areaActual").val();
    var nivel = $("#nivel").val();
    if($("#nivel").val() > 2) {
        idAdjunta = obtenAdjunta(idAdjunta, nivel);
    } else idAdjunta=$("#idArea").val();
    resumen(null,idAdjunta, nivel);
});


function inicio() {
    location.reload();
}
function resumenTest(anio) {
    var total = 0, totalAnter = 0, porcAtendidos = 0, porcPendientes = 0, porcAtendidosAnter = 0, porcPendientesAnter = 0;
    var totalGral = 0, totalAtendidos = 0, totalAtendidosGral = 0, totalPendientes = 0, reunionesSA = 0, totalAtendidosResumen = 0;
    var totalAtendidosGralAnter = 0, totalPendientesAnter = 0, reunionesSAanter = 0;
    var fechaHora = "";
    var hoy = new Date();
    var anioHoy = hoy.getFullYear();
    var anioCompara;
    if (anio === null || anio === anioHoy)
        anioCompara = anioHoy;
    else
        anioCompara = "";
    $("#resumenTotales").hide();
    $("#cuadroInicio").html("");
    $("#cuadroResumen").html("");

    totalAtendidos = 100; //miguel angel delgado
    totalAtendidosGral = 100;
    totalPendientes = 100;
    totalGral = totalAtendidos + totalPendientes;
    /*for (var i = 0; i < 5; i++) { // este era el procedimiento anterior
        totalAtendidos = 100;
        totalAtendidosGral = 100;
        totalPendientes = 100;
        totalGral = totalAtendidos + totalPendientes;
        fechaHora = "lunes 11 de octubre de 2021, 10:57 horas";
        totalAtendidosResumen = 100;//data[i].atendidosArea;
        $("#fechaHra").html("Fecha de corte: <b>" + fechaHora + "</b>");
        reunionesSA = 100;
        total = (100 + 100);
        totalAnter = 100 + 100;
        porcAtendidos = (100 * 100) / total;
        porcPendientes = (100 * 100) / total;

        //porcAtendidosAnter = (100 * 100) / totalAnter;
        //porcPendientesAnter = (100 * 100) / totalAnter;

        if (anioCompara === anioHoy){
            $("#cuadroResumen").append(cuadroActual(i, "siglas "+i, porcAtendidos.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,'), porcPendientes.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,'), totalAtendidosResumen, totalPendientes, 100, 100, 100, "idarea"));
        } else {
            $("#cuadroResumen").append(unCuadro(i, "siglas", 30.5.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,'), 30.5.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,'), 90, 90));
            totalGral += totalAnter;
            totalAtendidosGral += totalAtendidosResumen;
            totalPendientes += 1;
            reunionesSA += 1;
        }
        total = 0, porcAtendidos = 0, porcPendientes = 0;
    }*/
    $("#resumenTotales").show();
    $("#totalGral").html(totalGral.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
    $("#totalAtendidos").html(totalAtendidosGral.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
    $("#totalPendientes").html(totalPendientes.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
    $("#totalReuniones").html(reunionesSA.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
    $("#informa").show();
    
/*    $("#anios").html("");
    for (var i = anioHoy; i >= 2010; i--) {
        if (anio === i)
            $("#anios").append("<button type='button' class='btn btn-primary' onclick=resumen(" + i + ")>" + i + "</button> ");
        else {
            if (anioCompara === i)
                $("#anios").append("<button type='button' class='btn btn-primary' onclick=resumen(" + i + ")>" + i + "</button> ");
            else
                $("#anios").append("<button type='button' class='btn btn-outline-secondary' onclick=resumen(" + i + ")>" + i + "</button>&nbsp;");
        }
    }*/
    $("#leyendo").hide();
}

function estaArea(idarea, idAdjunta) {
    return idarea.idarea === idAdjunta;
}

function resumen(anio, idAdjunta, nivel) {
    var total = 0, totalAnter = 0, porcAtendidos = 0, porcPendientes = 0, porcAtendidosAnter = 0, porcPendientesAnter = 0;
    var totalGral = 0, totalAtendidos = 0, totalAtendidosGral = 0, totalPendientes = 0, reunionesSA = 0, totalAtendidosResumen = 0;
    var totalAtendidosGralAnter = 0, totalPendientesAnter = 0, reunionesSAanter = 0;
    var atendidosArea=0, pendientesArea = 0;
    var fechaHora = "";
    var hoy = new Date();
    var anioHoy = hoy.getFullYear();
    var anioCompara;
    if (anio === null || anio === anioHoy)
        anioCompara = anioHoy;
    else
        anioCompara = "";
    $("#resumenTotales").hide();
    $("#cuadroInicio").html("");
    $("#cuadroResumen").html("");
    //var arreglo = [];
    $("#leyendo").show();
    $.ajax({
        type: 'GET',
        url: 'resumenInicio.do',
        dataType: "json",
        data: {tipo: "0", otroAnio: anio, idAdjunta:idAdjunta},
        success: function (data) {
            //console.log(data);
            var numAreas = data.length;
            if (data !== 'fail') {
                totalAtendidos = data[0].atendidos;
                totalAtendidosGral = data[0].atendidosTodos;
                totalPendientes = data[0].pendientesTodos; //pendientes
                totalGral = totalAtendidosGral+totalPendientes; //totalAtendidos + totalPendientes;
                fechaHora = data[0].fechaHora;
                reunionesSA = 0; //data[0].reunionesSA;
                for (var i = 1; i < data.length; i++) {
                    totalAtendidos = data[i].atendidos;
                    totalAtendidosResumen = data[i].atendidosArea; //atendidosArea
                    $("#fechaHra").html("Fecha de corte: <b>" + fechaHora + "</b>");
                    //if(anio === anioHoy ) $("#fechaHra").html("Fecha de corte: <b>"+fechaHora+"</b>");
                    //else $("#fechaHra").html("");
                    //reunionesSA = data[0].reunionesSA;
                    total = (data[i].atendidos + data[i].pendientes);
                    totalAnter = data[i].atendidosArea + data[i].pendientes;
                    atendidosArea = data[i].atendidosArea;
                    pendientesArea = data[i].pendientes;
                    porcAtendidos = (data[i].atendidos * 100) / total;
                    porcPendientes = (data[i].pendientes * 100) / total;
                    porcAtendidosAnter = (data[i].atendidosArea * 100) / totalAnter;
                    porcPendientesAnter = (data[i].pendientesAntes * 100) / totalAnter;
                    //arreglo.push([i,data[i].siglas,porcAtendidos.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,'),porcPendientes.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,'),data[i].atendidos,data[i].pendientes]);
                    if (anioCompara == anioHoy) {
                        $("#cuadroResumen").append(cuadroActual(i, data[i].siglas, totalAtendidosResumen, pendientesArea, data[i].vencidos, data[i].porvencer, data[i].sinvencer, data[i].idarea, numAreas));
                    } else {
                        //totalAtendidosGralAnter=0, totalPendientesAnter=0, reunionesSAanter=0;
                        $("#cuadroResumen").append(unCuadro(i, data[i].siglas, porcAtendidosAnter.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,'), porcPendientesAnter.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,'), data[i].atendidosArea, data[i].pendientesAntes)); //totalAtendidos,totalPendientes
                        //totalGral += totalAnter;
                        totalAtendidosGral += totalAtendidosResumen;
                        //totalPendientes += data[i].pendientesAntes;
                        reunionesSA += data[i].reunionesSA;
                    }
                    /*totalGral += total;
                     totalAtendidos += data[i].atendidos;
                     totalPendientes += data[i].pendientes;*/
                    total = 0, porcAtendidos = 0, porcPendientes = 0;
                }
                
                if(nivel === "" || nivel === "1") {
                    $("#totalGral").html(totalGral.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
                    $("#totalAtendidos").html(totalAtendidosGral.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
                    $("#totalPendientes").html(totalPendientes.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
                    $("#totalReuniones").html(reunionesSA.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
                } else {
                    $("#totalGral").html(parseInt(atendidosArea) + parseInt(pendientesArea));
                    $("#totalAtendidos").html(atendidosArea);
                    $("#totalPendientes").html(pendientesArea);
                    $("#totalReuniones").html(0);
                }
                $("#resumenTotales").show();
                $("#informa").show();
                /*$("#anios").show();
                 $("#fechaHra").show();*/
            }

        }, error: function (request, status, error) {
            alert("Sesión agotada, se reiniciará el sistema");
            location.href = 'logout.do';
        }, complete: function (xhr, status) {
            $("#leyendo").hide();
        }
    });
    $("#anios").html("");
    /*for (var i = anioHoy; i >= 2010; i--) {
        if (anio == i)
            $("#anios").append("<button type='button' class='btn btn-primary' onclick=resumen(" + i + ")>" + i + "</button> ");
        else {
            if (anioCompara == i)
                $("#anios").append("<button type='button' class='btn btn-primary' onclick=resumen(" + i + ")>" + i + "</button> ");
            else
                $("#anios").append("<button type='button' class='btn btn-outline-secondary' onclick=resumen(" + i + ")>" + i + "</button> ");
        }
    }*/
}
function resumenDelRespaldoOriginal(anio) {
    var total = 0, totalAnter = 0, porcAtendidos = 0, porcPendientes = 0, porcAtendidosAnter = 0, porcPendientesAnter = 0;
    var totalGral = 0, totalAtendidos = 0, totalAtendidosGral = 0, totalPendientes = 0, reunionesSA = 0, totalAtendidosResumen = 0;
    var totalAtendidosGralAnter = 0, totalPendientesAnter = 0, reunionesSAanter = 0;
    var fechaHora = "";
    var hoy = new Date();
    var anioHoy = hoy.getFullYear();
    var anioCompara;
    if (anio === null || anio === anioHoy)
        anioCompara = anioHoy;
    else
        anioCompara = "";
    $("#resumenTotales").hide();
    $("#cuadroInicio").html("");
    $("#cuadroResumen").html("");
    //var arreglo = [];
    $("#leyendo").show();
    $.ajax({
        type: 'GET',
        url: 'resumenInicio.do',
        dataType: "json",
        data: {tipo: "0", otroAnio: anio},
        success: function (data) {
            if (data !== 'fail') {
                totalAtendidos = data[0].atendidos;
                totalAtendidosGral = data[0].atendidos;
                totalPendientes = data[0].pendientes;
                totalGral = totalAtendidos + totalPendientes;
                for (var i = 0; i < data.length; i++) {
                    totalAtendidos = data[i].atendidos;
                    fechaHora = data[0].fechaHora;
                    totalAtendidosResumen = data[i].atendidosArea;
                    $("#fechaHra").html("Fecha de corte: <b>" + fechaHora + "</b>");
                    //if(anio === anioHoy ) $("#fechaHra").html("Fecha de corte: <b>"+fechaHora+"</b>");
                    //else $("#fechaHra").html("");
                    reunionesSA = data[0].reunionesSA;
                    total = (data[i].atendidos + data[i].pendientes);
                    totalAnter = data[i].atendidosArea + data[i].pendientes;
                    porcAtendidos = (data[i].atendidos * 100) / total;
                    porcPendientes = (data[i].pendientes * 100) / total;

                    porcAtendidosAnter = (data[i].atendidosArea * 100) / totalAnter;
                    porcPendientesAnter = (data[i].pendientesAntes * 100) / totalAnter;

                    //arreglo.push([i,data[i].siglas,porcAtendidos.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,'),porcPendientes.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,'),data[i].atendidos,data[i].pendientes]);
                    if (anioCompara === anioHoy)
                        $("#cuadroResumen").append(cuadroActual(i, data[i].siglas, porcAtendidos.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,'), porcPendientes.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,'), totalAtendidosResumen, totalPendientes, data[i].vencidos, data[i].porvencer, data[i].pendactivos, data[i].idarea));
                    else {
                        //totalAtendidosGralAnter=0, totalPendientesAnter=0, reunionesSAanter=0;
                        $("#cuadroResumen").append(unCuadro(i, data[i].siglas, porcAtendidosAnter.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,'), porcPendientesAnter.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,'), data[i].atendidosArea, data[i].pendientesAntes)); //totalAtendidos,totalPendientes
                        totalGral += totalAnter;
                        totalAtendidosGral += totalAtendidosResumen;
                        totalPendientes += data[i].pendientesAntes;
                        reunionesSA += data[i].reunionesSA;
                    }
                    /*totalGral += total;
                     totalAtendidos += data[i].atendidos;
                     totalPendientes += data[i].pendientes;*/
                    total = 0, porcAtendidos = 0, porcPendientes = 0;
                }
                $("#resumenTotales").show();
                $("#totalGral").html(totalGral.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
                $("#totalAtendidos").html(totalAtendidosGral.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
                $("#totalPendientes").html(totalPendientes.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
                $("#totalReuniones").html(reunionesSA.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
                $("#informa").show();
                /*$("#anios").show();
                 $("#fechaHra").show();*/
            }

        }, error: function (request, status, error) {
            alert("Sesión agotada, se reiniciará el sistema");
            location.href = 'logout.do';
        }, complete: function (xhr, status) {
            $("#leyendo").hide();
        }
    });
    $("#anios").html("");
    for (var i = anioHoy; i >= 2010; i--) {
        if (anio === i)
            $("#anios").append("<button type='button' class='btn btn-primary' onclick=resumen(" + i + ")>" + i + "</button>");
        else {
            if (anioCompara === i)
                $("#anios").append("<button type='button' class='btn btn-primary' onclick=resumen(" + i + ")>" + i + "</button>");
            else
                $("#anios").append("<button type='button' class='btn btn-light' onclick=resumen(" + i + ")>" + i + "</button>");
        }
    }
}
function habilitaCaptura(habilita) {
    var parametros = new Object();
    parametros["habilita"] = habilita;
    $.ajax({
        type: 'GET',
        url: '../habilitaCapturaConvenios.do',
        dataType: "json",
        data: parametros,
        success: function (data) {
            $("#oculto").append(data);
        }, error: function (request, status, error) {
            alert("Sesión agotada, se reiniciará el sistema");
            location.href = 'logout.do';
        }
    });
}
function unCuadro(numero, dga, pctAt, pctPe, numAt, numPe) {
    
    var first;
    if (numero === 0 || numero === 3)
        first = "col-md-offset-1";
    var cuadro = '<div class="col-md-4">' +
            '<div class="card text-center">'+
            '<div class="x_panel tile fixed_height_250 overflow_hidden">' +
            '<div class="x_title">' +
            '<h2>' + dga + '</h2>' +
            '<div class="clearfix"></div>' +
            '</div>' +
            '<div class="x_content">' +
            '<div class="widget_summary">' +
            '<div class="w_left w_25" id="masStr0"><span>Atendidos</span></div>' +
            '<div class="w_center w_55">' +
            '<div class="progress">' +
            '<div class="progress-bar bg-green" role="progressbar" style="width: ' + pctAt + '%">' + pctAt + '%</div>' +
            '</div>' +
            '</div>' +
            '<div class="w_right w_20" id="masCnt0"><span>' + numAt + '</span></div>' +
            '<div class="clearfix"></div>' +
            '</div>' +
            '<div class="widget_summary">' +
            '<div class="w_left w_25" id="masStr1"><span>Pendientes</span></div>' +
            '<div class="w_center w_55">' +
            '<div class="progress">' +
            '<div class="progress-bar bg-red" role="progressbar" style="color: #000000; width: ' + pctPe + '%">' + pctPe + '%</div>' +
            '</div>' +
            '</div>' +
            '<div class="w_right w_20" id="masCnt1"><span>' + numPe + '</span></div>' +
            '<div class="clearfix"></div>' +
            '</div>'+
            '</div>';
    '</div>' +
            '</div>' +
            '</div>';
    return cuadro;
 
}
function cuadroActual(numero, dga, numAt, numPe, vencidos, porvencer, activos, idarea, numAreas) {
    var pctVencidos = 0, pctPorVencer = 0, pctActivos = 0;
    var total = (vencidos + porvencer + activos); //data[i].vencidos, data[i].porvencer, data[i].pendactivos
    pctVencidos = (vencidos * 100) / total;
    pctPorVencer = (porvencer * 100) / total;
    pctActivos = (activos * 100) / total;
    if (isNaN(pctVencidos)) pctVencidos=0.0;
    if (isNaN(pctPorVencer)) pctPorVencer=0.0;
    if (isNaN(pctActivos)) pctActivos=0.0;
    var liga = "";
    if (idarea === $("#areaActual").val() || $("#permisoActual").val() === 'A' || $("#permisoActual").val() === 'E') {
        liga = " onclick= 'consultaPendientes("+idarea+")' data-bs-target='#pendientes' ";
    }
    var cuadro = "";
    if(numAreas === 2) {
        cuadro = '<div class="col-md-4"></div>';
    }
    cuadro += '<div class="col-md-4">'+
    '    <div class="card text-center">'+
    '      <div class="card-header">' + dga + '</div>'+
    '      <div class="card-body">'+
    '          <h5 class="card-title">'+
    '            <button type="button" class="btn btn-primary" ' + liga + ' title="Consultar pendientes" data-bs-toggle="modal" data-bs-placement="top">'+
    '                Total pendientes <span class="badge bg-light text-primary">' + numPe + '</span>'+ // total
    '            </button>'+
    '          </h5>'+
    '        <div class="progress">'+
    '          <div class="progress-bar bg-danger" role="progressbar" style="width: ' + pctVencidos + '%">' + pctVencidos.toFixed(1).replace(/(\d)(?=(\d{3})+\.)/g, '$1,') + '%</div>'+
    '          <div class="progress-bar bg-warning" role="progressbar" style="width: ' + pctPorVencer + '%">' + pctPorVencer.toFixed(1).replace(/(\d)(?=(\d{3})+\.)/g, '$1,') + '%</div>'+
    '          <div class="progress-bar bg-primary" role="progressbar" style="width: ' + pctActivos + '%">' + pctActivos.toFixed(1).replace(/(\d)(?=(\d{3})+\.)/g, '$1,') + '%</div>'+
    '        </div><br>'+
    '        <div id="desgloce">'+
    '        <button type="button" class="btn btn-danger btn-sm">' + vencidos + '</button> Vencidos&nbsp;'+
    '        <button type="button" class="btn btn-warning btn-sm">' + porvencer + '</button> Por vencer&nbsp;'+
    '        <button type="button" class="btn btn-primary btn-sm">' + activos + '</button> Sin vencer'+
    '        </div><br>'+
    '        <button type="button" class="btn btn-success" data-placement="top" >'+
    '            Atendidos <span class="badge bg-light text-success">' + numAt + '</span>'+
    '        </button>'+
    '      </div>'+
    '    </div>'+
    '</div>';
    return cuadro;
}

function consultaPendientes(idarea) {

    var tabla = "";
    $("#headPend").empty();
    $("#desglocePend").empty();
    var totalFila = 0, totalVenc = 0, totalPorVenc = 0, totalAct = 0, totalTotal = 0;
    var resumen, area;
    $.ajax({
        type: 'GET',
        url: 'resumenInicio.do',
        dataType: "json",
        data: {tipo: "1", idarea: idarea},
        success: function (data) {
            if (data != 'fail') {
                $("#headPend").append("<tr><td colspan='5' align='center'><b>" + data[0].area.siglas + "</b></td></tr>" +
                        "<tr><td colspan='5' align='center'><b>PENDIENTES</b></td></tr>" +
                        "<tr style='font-weight:bold; text-align:center'><td>CONCEPTO</td><td>VENCIDOS</td><td>POR VENCER</td><td>ACTIVOS</td><td>TOTAL</td></tr>");
                for (var i = 0; i < data[0].resumen.length; i++) {
                    resumen = data[0].resumen[i];
                    area = data[0].area;
                    totalFila = (resumen.vencidos_d + resumen.porvencer_d + resumen.pendactivos_d);
                    if (resumen.tipoasunto != 'REUNIONES PENDIENTES DE REGISTRAR ACUERDOS') {
                        tabla += "<tr align='center'><td>" + resumen.tipoasunto + "</td>" +
                                "<td><a href='consultaAsuntosFiltro.do?areaFiltro=" + area.idarea + "&modo=" + resumen.tipoAbreviado + "&estatusFechaA=VE&estatusResponsable=P'>" + resumen.vencidos_d + "</a></td>" +
                                "<td><a href='consultaAsuntosFiltro.do?areaFiltro=" + area.idarea + "&modo=" + resumen.tipoAbreviado + "&estatusFechaA=AV&estatusResponsable=P'>" + resumen.porvencer_d + "</td>" +
                                "<td><a href='consultaAsuntosFiltro.do?areaFiltro=" + area.idarea + "&modo=" + resumen.tipoAbreviado + "&estatusFechaA=AC&estatusResponsable=P'>" + resumen.pendactivos_d + "</td>" +
                                "<td>" + totalFila + "</td>";
                        tabla += "<tr>";
                    }
                    if (resumen.tipoasunto == 'REUNIONES PENDIENTES DE REGISTRAR ACUERDOS') {
                        tabla += "<tr align='center'><td>" + resumen.tipoasunto + "</td>" +
                                "<td colspan='3'><a href='consultaAsuntosFiltro.do?estatusReunion=RS&areaFiltro=" + area.idarea + "&modo=" + resumen.tipoAbreviado + "'>" + resumen.vencidos_d + "</td>" +
                                "<td>" + resumen.vencidos_d + "</td></tr>";
                    }
                    totalVenc += resumen.vencidos_d;
                    totalPorVenc += resumen.porvencer_d;
                    totalAct += resumen.pendactivos_d;
                }
                tabla += "<tr class='resaltar' align='center'><td>Total</td><td>" + totalVenc + "</td><td>" + totalPorVenc + "</td><td>" + totalAct + "</td><td>" + (totalVenc + totalPorVenc + totalAct) + "</td></tr>";
                $("#desglocePend").append(tabla);
            }
        }, error: function (request, status, error) {
            alert("Sesión agotada, se reiniciará el sistema");
            location.href = 'logout.do';
        }, complete: function (xhr, status) {

        }
    });
}

function obtenAdjunta(idarea, nivel) {
    var idAdjunta;
    $.ajax({
        async:false,
        type: 'GET',
        url: 'obtenAreaSuperior.do',
        dataType: "json",
        data: {idarea:idarea, nivel:nivel},
        success: function (data) {
            idAdjunta = data;
        }, error: function (request, status, error) {
            alert("Sesión agotada, se reiniciará el sistema");
            location.href = 'logout.do';
        }, complete: function (xhr, status) {
            
        }
    });
    return idAdjunta;
}