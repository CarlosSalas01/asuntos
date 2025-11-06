/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.negocio;


import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaDAO;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import mx.org.inegi.dggma.sistemas.asuntos.datosglobales.DatosGlobales;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AreaDTO;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AsuntoBean;
import mx.org.inegi.dggma.sistemas.asuntos.dto.InstruccionDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.UsuarioDTO;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaUsuarioArea;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.*;
import org.apache.commons.mail.SimpleEmail;


/**
 *
 * @author joseluis
 */
public class AdministradorCorreo implements Serializable {
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////  ENVIO ASUNTO        ////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
    String servCorreo="w-appintrasmtp.inegi.gob.mx";
    
    public String enviaCorreoAsunto(AsuntoBean asuntoDto, List<UsuarioDTO> destinatarios, String subject) throws Exception {
        
        String turno="";
        if (asuntoDto.getTipoasunto().equals("K")) {
          turno = "<tr> <td bgcolor=\"#EBEBEB\" class=\"celdaEspecial\">" +
                  "        <div align=\"right\"><strong><font face=\"Arial, Helvetica, sans-serif\">Turno</font></strong></div></td>" +
                  "        <td class=\"celdaEspecial\">"+asuntoDto.getNocontrol()+"</td></tr>";
        }
        String mensaje = "<strong>Sistema de Seguimiento de Asuntos de la UGMA</strong>\n" +
                "\n" +
                "Usted tiene un <strong>"+
                (asuntoDto.getTipoasunto().equals("K") ?  " TURNO SIA ":" CORREO ") + "</strong>"+   
                (asuntoDto.getUrgente().equals("S") ?  "<strong> URGENTE </strong>":"")+
                (asuntoDto.getPresidencia().equals("P") || asuntoDto.getPresidencia().equals("S") ? " DE PRESIDENCIA ":"")+
                " con Folio "+ asuntoDto.getIdconsecutivo()  + "\n" +
                "\n" +
                "<table width=\"80%\" border=\"1\" align=\"center\" cellpadding=\"5\" cellspacing=\"0\" bgcolor=\"#FFFFFF\" style='font-family:Arial, Helvetica, sans-serif; font-size:12px' >" +
                turno+
                "<tr> <td bgcolor=\"#EBEBEB\" class=\"celdaEspecial\">" +
                "        <div align=\"right\"><strong><font face=\"Arial, Helvetica, sans-serif\">Asunto</font></strong></div></td>" +
                "        <td class=\"celdaEspecial\">"+asuntoDto.getDescripcionFormatoHTML()+"</td></tr>"+
                "<tr><td bgcolor=\"#EBEBEB\" class=\"celdaEspecial\">"+
                "    <div align=\"right\"><font face=\"Arial, Helvetica, sans-serif\"><strong>Fecha"+
                "     para atenci&oacute;n</strong></font></div></td>"+
                "    <td class=\"celdaEspecial\">"+ asuntoDto.getFechaatenderFormatoTexto() +"</td></tr>"+
                "<tr><td bgcolor=\"#EBEBEB\" class=\"celdaEspecial\" rowspan=\" "+ asuntoDto.getResponsables().size()  +"\" >"+
                "    <div align=\"right\"><strong><font face=\"Arial, Helvetica, sans-serif\">Responsables:</font></strong></div></td>"+
                "";
                boolean bandera=false;
                for(ResponsableBean responsable:asuntoDto.getResponsables()){
                    if (bandera) mensaje+="<tr>";
                    mensaje += "<td class=\"celdaEspecial\">"+ responsable.getArea().getSiglas() +"</td></tr>";
                    bandera = true;
                }
                
                if (asuntoDto.getTipoasunto().equals("R")) {
                    mensaje +=  "<tr><td bgcolor=\"#EBEBEB\" class=\"celdaEspecial\" rowspan=\""+ asuntoDto.getCorresponsables().size() +"\">"+
                                "    <div align=\"right\"><strong><font face=\"Arial, Helvetica, sans-serif\">Corresponsables:</font></strong></div></td>";
                    bandera=false;
                    for(AreaBean corresponsable:asuntoDto.getCorresponsables()){
                        if (bandera) mensaje+="<tr>";
                        mensaje += "<tr><td class=\"celdaEspecial\">"+corresponsable.getDatos().getSiglas()+"</td</tr>";
                        bandera = true;
                    }
                }

        mensaje += "</table>\n\n" +
                   "Para más detalles, <a href ='"+ DatosGlobales.getRutaSistema() +"consultaAsunto.do?idasunto="+ asuntoDto.getIdasunto() +"&modo="+asuntoDto.getTipoasunto()+"'>Acceder al Sistema de Asuntos</a>\n\n"+ 
                   "<br><br><font size = \"-1\"face=\"Arial, Helvetica, sans-serif\">Éste es un correo informativo, por lo que no es necesario responderlo.<br>";

        String mensajeHtml = getTraduccionHTML(mensaje);
        mensajeHtml="<html><body>"+mensajeHtml+"</body><html>";

        MimeMultipart mimemultipart = new MimeMultipart();
        mimemultipart.setSubType("alternative");

        MimeBodyPart msgText = new MimeBodyPart();
        msgText.setText(mensaje);

        mimemultipart.addBodyPart(msgText);
        MimeMultipart htmlContent = new MimeMultipart();
        htmlContent.setSubType("related");
        MimeBodyPart mimebodypart = new MimeBodyPart();
        mimemultipart.addBodyPart(mimebodypart);
        mimebodypart.setContent(htmlContent);
        MimeBodyPart msgHtml = new MimeBodyPart();
        msgHtml.setContent(mensajeHtml, "text/html");

        htmlContent.addBodyPart(msgHtml);

        SimpleEmail email = new SimpleEmail();
        email.setHostName(servCorreo);
        
        //Destinatarios
        for (UsuarioDTO destino:destinatarios){
          email.addTo(destino.getCorreo1(),destino.getNombre());
        }
        
        email.setFrom("asuntos.pendientes@inegi.org.mx", "Sistema de Asuntos Pendientes");
        email.setSubject(subject);
        email.setContent(mimemultipart);
        if (DatosGlobales.ESTADO_PRODUCCION){
            email.send();
        }
        return mensaje;
    }
    
    public String enviaCorreoAcuerdo(AccionBean acuerdo, List<UsuarioDTO> destinatarios, String idreunion) throws Exception {
        String mensaje = "<strong>Sistema de Seguimiento de Asuntos de la UGMA</strong>\n" +
                "\n<br>" +
                "Usted tiene un <strong>ACUERDO </strong>"+   
                " con número de Reunión <b>"+ idreunion  + "</b>\n" +
                "\n<br>" +
                "<table width=\"80%\" border=\"1\" align=\"center\" cellpadding=\"5\" cellspacing=\"0\" bgcolor=\"#FFFFFF\" style='font-family:Arial, Helvetica, sans-serif; font-size:12px' >" +
                "<tr><td bgcolor=\"#EBEBEB\" class=\"celdaEspecial\">" +
                "        <div align=\"right\"><strong><font face=\"Arial, Helvetica, sans-serif\">Asunto</font></strong></div></td>" +
                "        <td class=\"celdaEspecial\">"+acuerdo.getDescripcion()+"</td></tr>"+
                "<tr><td bgcolor=\"#EBEBEB\" class=\"celdaEspecial\">"+
                "    <div align=\"right\"><font face=\"Arial, Helvetica, sans-serif\"><strong>Fecha"+
                "     para atenci&oacute;n</strong></font></div></td>"+
                "    <td class=\"celdaEspecial\">"+ acuerdo.getFechaatenderFormatoTexto() +"</td></tr>";

        mensaje += "</table>\n\n" +
                   "Para m�s detalles, <a href ='"+ DatosGlobales.getRutaSistema() +"consultaAsunto.do?idasunto="+ idreunion +"&modo=R'>Acceder al Sistema de Asuntos</a>\n\n"+ 
                   "<br><br><font size = \"-1\"face=\"Arial, Helvetica, sans-serif\">Éste es un correo informativo, por lo que no es necesario responderlo.<br>";

        String mensajeHtml = getTraduccionHTML(mensaje);
        mensajeHtml="<html><body>"+mensajeHtml+"</body><html>";

        MimeMultipart mimemultipart = new MimeMultipart();
        mimemultipart.setSubType("alternative");

        MimeBodyPart msgText = new MimeBodyPart();
        msgText.setText(mensaje);

        mimemultipart.addBodyPart(msgText);
        MimeMultipart htmlContent = new MimeMultipart();
        htmlContent.setSubType("related");
        MimeBodyPart mimebodypart = new MimeBodyPart();
        mimemultipart.addBodyPart(mimebodypart);
        mimebodypart.setContent(htmlContent);
        MimeBodyPart msgHtml = new MimeBodyPart();
        msgHtml.setContent(mensajeHtml, "text/html");

        htmlContent.addBodyPart(msgHtml);

        SimpleEmail email = new SimpleEmail();
        email.setHostName(servCorreo);

        //Responsables y con permisos
        for (UsuarioDTO destino:destinatarios){
          email.addTo(destino.getCorreo1(),destino.getNombre());
        }

        email.setFrom("asuntos.pendientes@inegi.org.mx", "Sistema de Asuntos Pendientes");
        email.setSubject("%Nuevo Asunto");
        email.setContent(mimemultipart);
        if (DatosGlobales.ESTADO_PRODUCCION){
            email.send();
        }
        return mensaje;

    }
     public String enviaCorreoUrgenteAsuntoELIMINAR(AsuntoBean asuntoDto, List<UsuarioDTO> destinatarios) throws Exception {
        String turno="";
        if (asuntoDto.getTipoasunto().equals("K")) {
          turno = "<tr> <td bgcolor=\"#EBEBEB\" class=\"celdaEspecial\">" +
                  "        <div align=\"right\"><strong><font face=\"Arial, Helvetica, sans-serif\">Turno</font></strong></div></td>" +
                  "        <td class=\"celdaEspecial\">"+asuntoDto.getNocontrol()+"</td></tr>";
        }
        
        String mensaje = "<strong>Sistema de Seguimiento de Asuntos de la UGMA</strong>\n" +
                "\n" +
                "Usted tiene un <strong>"+ (asuntoDto.getTipoasunto().equals("K") ?  " TURNO SIA ":" CORREO ") + 
                " URGENTE </strong> "+
                " con Folio "+ asuntoDto.getIdconsecutivo()  + "\n" +
                "\n" +
                "<table width=\"80%\" border=\"1\" align=\"center\" cellpadding=\"5\" cellspacing=\"0\" bgcolor=\"#FFFFFF\" style='font-family:Arial, Helvetica, sans-serif; font-size:12px'>" +
                turno+
                "<tr> <td bgcolor=\"#EBEBEB\" class=\"celdaEspecial\">" +
                "        <div align=\"right\"><strong><font face=\"Arial, Helvetica, sans-serif\">Asunto</font></strong></div></td>" +
                "        <td class=\"celdaEspecial\">"+asuntoDto.getDescripcionFormatoHTML()+"</td></tr>"+
                "<tr><td bgcolor=\"#EBEBEB\" class=\"celdaEspecial\">"+
                "    <div align=\"right\"><font face=\"Arial, Helvetica, sans-serif\"><strong>Fecha"+
                "     para atenci&oacute;n</strong></font></div></td>"+
                "    <td class=\"celdaEspecial\">"+ asuntoDto.getFechaatenderFormatoTexto() +"</td></tr>"+
                "<tr><td bgcolor=\"#EBEBEB\" class=\"celdaEspecial\" rowspan=\" "+ asuntoDto.getResponsables().size()  +"\" >"+
                "    <div align=\"right\"><strong><font face=\"Arial, Helvetica, sans-serif\">Responsables:</font></strong></div></td>"+
                "";
                boolean bandera=false;
                for(ResponsableBean responsable:asuntoDto.getResponsables()){
                    if (bandera) mensaje+="<tr>";
                    mensaje += "<td class=\"celdaEspecial\">"+ responsable.getArea().getSiglas() +"</td></tr>";
                    bandera = true;
                }
                
                if (asuntoDto.getTipoasunto().equals("R")) {
                    mensaje +=  "<tr><td bgcolor=\"#EBEBEB\" class=\"celdaEspecial\" rowspan=\""+ asuntoDto.getCorresponsables().size() +"\">"+
                                "    <div align=\"right\"><strong><font face=\"Arial, Helvetica, sans-serif\">Corresponsables:</font></strong></div></td>";
                    bandera=false;
                    for(AreaBean corresponsable:asuntoDto.getCorresponsables()){
                        if (bandera) mensaje+="<tr>";
                        mensaje += "<tr><td class=\"celdaEspecial\">"+corresponsable.getDatos().getSiglas()+"</td</tr>";
                        bandera = true;
                    }
                }

        mensaje += "</table>\n\n" +
                   "Para más detalles <a href ='"+ DatosGlobales.getRutaSistema() +"consultaAsunto.do?idasunto="+ asuntoDto.getIdasunto() +"&modo="+asuntoDto.getTipoasunto()+"'>Acceder al Sistema de Asuntos</a>\n\n"+ 
                   "<br><br><font size = \"-1\"face=\"Arial, Helvetica, sans-serif\">Éste es un correo informativo, por lo que no es necesario responderlo.<br>";

        String mensajeHtml = getTraduccionHTML(mensaje);
        mensajeHtml="<html><body>"+mensajeHtml+"</body><html>";


        MimeMultipart mimemultipart = new MimeMultipart();
        mimemultipart.setSubType("alternative");

        MimeBodyPart msgText = new MimeBodyPart();
        msgText.setText(mensaje);

        mimemultipart.addBodyPart(msgText);
        MimeMultipart htmlContent = new MimeMultipart();
        htmlContent.setSubType("related");
        MimeBodyPart mimebodypart = new MimeBodyPart();
        mimemultipart.addBodyPart(mimebodypart);
        mimebodypart.setContent(htmlContent);
        MimeBodyPart msgHtml = new MimeBodyPart();
        msgHtml.setContent(mensajeHtml, "text/html");

        htmlContent.addBodyPart(msgHtml);

        SimpleEmail email = new SimpleEmail();
        email.setHostName(servCorreo);

        //Responsables y con permisos
        for (UsuarioDTO destino:destinatarios){
          email.addTo(destino.getCorreo1(),destino.getNombre());
        }

        email.setFrom("asuntos.pendientes@inegi.org.mx", "Sistema de Asuntos Pendientes");
        email.setSubject("%Asunto Urgente");
        email.setContent(mimemultipart);
        if (DatosGlobales.ESTADO_PRODUCCION){
            email.send();
        }
        return mensaje;

    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////  ENVIO REPORTE DIARIO   ////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String enviaCorreoRepoteDiario(List<ReporteArea> reporte, List<UsuarioDTO> destinatarios, String fecha1, String fecha2) throws Exception {
        
        String tabla = formaTablaDiaria(reporte,fecha1,fecha2);

        String mensajeHtml = getTraduccionHTML(tabla);
        mensajeHtml="<html><body>"+mensajeHtml+"</body><html>";


        MimeMultipart mimemultipart = new MimeMultipart();
        mimemultipart.setSubType("alternative");

        MimeBodyPart msgText = new MimeBodyPart();
        msgText.setText(tabla);

        mimemultipart.addBodyPart(msgText);
        MimeMultipart htmlContent = new MimeMultipart();
        htmlContent.setSubType("related");
        MimeBodyPart mimebodypart = new MimeBodyPart();
        mimemultipart.addBodyPart(mimebodypart);
        mimebodypart.setContent(htmlContent);
        MimeBodyPart msgHtml = new MimeBodyPart();
        msgHtml.setContent(mensajeHtml, "text/html");

        htmlContent.addBodyPart(msgHtml);

        SimpleEmail email = new SimpleEmail();
        email.setHostName(servCorreo); // 10.1.32.15
              
        //Destinatarios
        for (UsuarioDTO destino:destinatarios){
          email.addTo(destino.getCorreo1(),destino.getNombre());
        }
        
        email.setFrom("asuntos.pendientes@inegi.org.mx", "Sistema de Asuntos Pendientes");
        email.setSubject("%Reporte Diario "+Utiles.getFecha()+" a las "+Utiles.getHora() );
        email.setContent(mimemultipart);
        if (DatosGlobales.ESTADO_PRODUCCION){
           email.send();
        }
        return tabla;

    }

    ///////////////////////////////////////////
    /////  CORREO DIARIO MENSUAL
    ///////////////////////////////////////////
    
    public String enviaCorreoReporteDiarioSemanalMensual(List<ResumenAreaSM> resumenSemana, List<ResumenAreaSM> resumenMes,  List<ReporteArea> diario, List<ResumenAreaSM> repSemanal, String semana, List<UsuarioDTO> destinatarios, List<ResumenAreaSM> mensual, String delMes, String fecha1, String fecha2, int nivel) throws Exception {
        String tablaUnida="<table width='100%' align='center'><tr><td valign='top' align='center' width='33%'>";

        String tablaDia = formaTablaDiaria(diario,DatosGlobales.anioBase(),Utiles.getFechaDMA());
        String tablaSemana = formaTablaSemana(resumenSemana, repSemanal, semana, nivel);
        String tablaMens = formaTablaMensual(resumenMes, mensual, delMes, fecha1, fecha2, nivel);
        
        tablaUnida += tablaDia;
        tablaUnida += "</td><td valign='top' align='center' width='33%'>";
        tablaUnida += tablaSemana;
        tablaUnida += "</td><td valign='top' align='center' width='33%'>";
        tablaUnida += tablaMens;
        tablaUnida += "</td></tr></table>";
            
        tablaUnida += "<table><tr><td>Éste es un correo informativo, por lo que no es necesario responderlo.</td></tr></table>";
                    
        String mensajeHtml = getTraduccionHTML(tablaUnida);
        mensajeHtml="<html><body>"+mensajeHtml+"</body><html>";
        MimeMultipart mimemultipart = new MimeMultipart();
        mimemultipart.setSubType("alternative");
        MimeBodyPart msgText = new MimeBodyPart();
        msgText.setText(tablaUnida);
        mimemultipart.addBodyPart(msgText);
        MimeMultipart htmlContent = new MimeMultipart();
        htmlContent.setSubType("related");
        MimeBodyPart mimebodypart = new MimeBodyPart();
        mimemultipart.addBodyPart(mimebodypart);
        mimebodypart.setContent(htmlContent);
        MimeBodyPart msgHtml = new MimeBodyPart();
        msgHtml.setContent(mensajeHtml, "text/html");
        htmlContent.addBodyPart(msgHtml);
        SimpleEmail email = new SimpleEmail();
        email.setHostName(servCorreo);
       //Responsables y con permisos
        for (UsuarioDTO destino:destinatarios){
          email.addTo(destino.getCorreo1(),destino.getNombre());
        }
            
        email.setFrom("asuntos.pendientes@inegi.org.mx", "Sistema de Asuntos Pendientes");
        email.setSubject("%Reporte Diario/Mensual "+Utiles.getFecha()+" a las "+Utiles.getHora());
        email.setContent(mimemultipart);
        if (DatosGlobales.ESTADO_PRODUCCION){
            email.send();
        }
        return tablaUnida;
    }
    
    ///////////////////////////////////////////
    /////  CORREO DIARIO MENSUAL
    ///////////////////////////////////////////
    
    public String enviaCorreoReporteDiarioMensual(List<ResumenAreaSM> resumenMes, List<ReporteArea> diario, List<UsuarioDTO> destinatarios, List<ResumenAreaSM> mensual, String delMes, String fecha1, String fecha2, int nivel) throws Exception {
        String tablaUnida="<table width='100%' align='center'><tr><td valign='top' align='center' width='33%'>";
        String tablaDia = formaTablaDiaria(diario,DatosGlobales.anioBase(),Utiles.getFechaDMA());
        
        String tablaMens = formaTablaMensual(resumenMes, mensual, delMes, fecha1, fecha2, nivel);

        tablaUnida += tablaDia;
        tablaUnida += "</td><td valign='top' align='center' width='33%'>";
        tablaUnida += tablaMens;
        tablaUnida += "</td></tr></table>";
            
        String mensajeHtml = getTraduccionHTML(tablaUnida);
        mensajeHtml="<html><body>"+mensajeHtml+"</body><html>";
        MimeMultipart mimemultipart = new MimeMultipart();
        mimemultipart.setSubType("alternative");
        MimeBodyPart msgText = new MimeBodyPart();
        msgText.setText(tablaUnida);
        mimemultipart.addBodyPart(msgText);
        MimeMultipart htmlContent = new MimeMultipart();
        htmlContent.setSubType("related");
        MimeBodyPart mimebodypart = new MimeBodyPart();
        mimemultipart.addBodyPart(mimebodypart);
        mimebodypart.setContent(htmlContent);
        MimeBodyPart msgHtml = new MimeBodyPart();
        msgHtml.setContent(mensajeHtml, "text/html");
        htmlContent.addBodyPart(msgHtml);
        SimpleEmail email = new SimpleEmail();
        email.setHostName(servCorreo);

        
        //Responsables y con permisos
        for (UsuarioDTO destino:destinatarios){
          email.addTo(destino.getCorreo1(),destino.getNombre());
        }
            
        email.setFrom("asuntos.pendientes@inegi.org.mx", "Sistema de Asuntos Pendientes");
        email.setSubject("%Reporte Diario/Mensual "+Utiles.getFecha()+" a las "+Utiles.getHora());
        email.setContent(mimemultipart);
        if (DatosGlobales.ESTADO_PRODUCCION){
            email.send();
        }
        return tablaUnida;
    }
    
    ///////////////////////////////////////////
    /////  CORREO DIARIO SEMANAL
    ///////////////////////////////////////////
   
    public String enviaCorreoReporteDiarioSemanal2(List<ResumenAreaSM> resumen,List<ReporteArea> diario, List<ResumenAreaSM> rep, List<UsuarioDTO> destinatarios, String semana, String fecha1, String fecha2, int nivel) throws Exception {
        String tablaUnida = "<table width='80%' align='center'><tr><td valign='top' align='center' colspan='2'>";
//        String tablaResumen = formaTablaResumen()+"</td></tr>";
//        tablaUnida+=tablaResumen;
        tablaUnida+="<tr><td valign='top' align='center' width='50%'>";
        String tablaDia = formaTablaDiaria(diario,DatosGlobales.anioBase(),Utiles.getFechaDMA());
        String tablaSemana = formaTablaSemana(resumen,rep, semana, nivel);
        tablaUnida+=tablaDia;
        tablaUnida+="</td><td valign='top' align='center' width='50%'>";
        tablaUnida+=tablaSemana;
        tablaUnida+="</td></tr></table>";
        
        String mensajeHtml = getTraduccionHTML(tablaUnida);
        mensajeHtml="<html><body>"+mensajeHtml+"</body><html>";
        MimeMultipart mimemultipart = new MimeMultipart();
        mimemultipart.setSubType("alternative");
        MimeBodyPart msgText = new MimeBodyPart();
        msgText.setText(tablaUnida);
        mimemultipart.addBodyPart(msgText);
        MimeMultipart htmlContent = new MimeMultipart();
        htmlContent.setSubType("related");
        MimeBodyPart mimebodypart = new MimeBodyPart();
        mimemultipart.addBodyPart(mimebodypart);
        mimebodypart.setContent(htmlContent);
        MimeBodyPart msgHtml = new MimeBodyPart();
        msgHtml.setContent(mensajeHtml, "text/html");
        htmlContent.addBodyPart(msgHtml);
        SimpleEmail email = new SimpleEmail();
        email.setHostName(servCorreo);

        
        //Responsables y con permisos
        for (UsuarioDTO destino:destinatarios){
          email.addTo(destino.getCorreo1(),destino.getNombre());
        }
        
        email.setFrom("asuntos.pendientes@inegi.org.mx", "Sistema de Asuntos Pendientes");
        email.setSubject("%Reporte Diario/Semanal "+Utiles.getFecha()+" a las "+Utiles.getHora());
        email.setContent(mimemultipart);
        if (DatosGlobales.ESTADO_PRODUCCION){
          email.send();
        }
        return tablaUnida;
        
    }

    public String enviaCorreoUrgenteAsuntoReasignaYaNoEnviar(AsuntoBean asuntoDto, List<UsuarioDTO> destinatarios) throws Exception {
        String turno="";
        if (asuntoDto.getTipoasunto().equals("K")) {
          turno = "<tr> <td bgcolor=\"#EBEBEB\" class=\"celdaEspecial\">" +
                  "        <div align=\"right\"><strong><font face=\"Arial, Helvetica, sans-serif\">Turno</font></strong></div></td>" +
                  "        <td class=\"celdaEspecial\">"+asuntoDto.getNocontrol()+"</td></tr>";
        }
        
        String mensaje = "<strong>Sistema de Seguimiento de Asuntos de la UGMA</strong>\n" +
                "\n" +
                "Usted tiene un <strong>"+ (asuntoDto.getTipoasunto().equals("K") ?  " TURNO SIA ":" CORREO ") + 
                " URGENTE </strong> "+ (asuntoDto.getPresidencia().equals("P") || asuntoDto.getPresidencia().equals("S") ? " DE PRESIDENCIA ":"")+
                " con Folio "+ asuntoDto.getIdconsecutivo()  + "\n" +
                "\n" +
                "<table width=\"80%\" border=\"1\" align=\"center\" cellpadding=\"5\" cellspacing=\"0\" bgcolor=\"#FFFFFF\" style='font-family:Arial, Helvetica, sans-serif; font-size:12px'>" +
                turno+
                "<tr> <td bgcolor=\"#EBEBEB\" class=\"celdaEspecial\">" +
                "        <div align=\"right\"><strong><font face=\"Arial, Helvetica, sans-serif\">Asunto</font></strong></div></td>" +
                "        <td class=\"celdaEspecial\">"+asuntoDto.getDescripcionFormatoHTML()+"</td></tr>"+
                "<tr><td bgcolor=\"#EBEBEB\" class=\"celdaEspecial\">"+
                "    <div align=\"right\"><font face=\"Arial, Helvetica, sans-serif\"><strong>Fecha"+
                "     para atenci&oacute;n</strong></font></div></td>"+
                "    <td class=\"celdaEspecial\">"+ asuntoDto.getFechaatenderFormatoTexto() +"</td></tr>"+
                "<tr><td bgcolor=\"#EBEBEB\" class=\"celdaEspecial\" rowspan=\" "+ asuntoDto.getResponsables().size()  +"\" >"+
                "    <div align=\"right\"><strong><font face=\"Arial, Helvetica, sans-serif\">Responsables:</font></strong></div></td>"+
                "";
                boolean bandera=false;
                for(ResponsableBean responsable:asuntoDto.getResponsables()){
                    if (bandera) mensaje+="<tr>";
                    mensaje += "<td class=\"celdaEspecial\">"+  responsable.getArea().getSiglas() +"</td></tr>";
                    bandera = true;
                }
                
                if (asuntoDto.getTipoasunto().equals("R")) {
                    mensaje +=  "<tr><td bgcolor=\"#EBEBEB\" class=\"celdaEspecial\" rowspan=\""+ asuntoDto.getCorresponsables().size() +"\">"+
                                "    <div align=\"right\"><strong><font face=\"Arial, Helvetica, sans-serif\">Corresponsables:</font></strong></div></td>";
                    bandera=false;
                    for(AreaBean corresponsable:asuntoDto.getCorresponsables()){
                        if (bandera) mensaje+="<tr>";
                        mensaje += "<tr><td class=\"celdaEspecial\">"+corresponsable.getDatos().getSiglas()+"</td</tr>";
                        bandera = true;
                    }
                }

        mensaje += "</table>\n\n" +
                   "Para más detalles <a href ='"+ DatosGlobales.getRutaSistema() +"consultaAsunto.do?idasunto="+ asuntoDto.getIdasunto() +"&modo="+asuntoDto.getTipoasunto()+"'>Acceder al Sistema de Asuntos</a>\n\n"+ 
                   "<br><br><font size = \"-1\"face=\"Arial, Helvetica, sans-serif\">Éste es un correo informativo, por lo que no es necesario responderlo.<br>";

        String mensajeHtml = getTraduccionHTML(mensaje);
        mensajeHtml="<html><body>"+mensajeHtml+"</body><html>";


        MimeMultipart mimemultipart = new MimeMultipart();
        mimemultipart.setSubType("alternative");

        MimeBodyPart msgText = new MimeBodyPart();
        msgText.setText(mensaje);

        mimemultipart.addBodyPart(msgText);
        MimeMultipart htmlContent = new MimeMultipart();
        htmlContent.setSubType("related");
        MimeBodyPart mimebodypart = new MimeBodyPart();
        mimemultipart.addBodyPart(mimebodypart);
        mimebodypart.setContent(htmlContent);
        MimeBodyPart msgHtml = new MimeBodyPart();
        msgHtml.setContent(mensajeHtml, "text/html");

        htmlContent.addBodyPart(msgHtml);

        SimpleEmail email = new SimpleEmail();
        email.setHostName(servCorreo);
         
        for (UsuarioDTO destino:destinatarios){
          email.addTo(destino.getCorreo1(),destino.getNombre());
        }

        email.setFrom("asuntos.pendientes@inegi.org.mx", "Sistema de Asuntos Pendientes");
        email.setSubject("%Asunto Urgente");
        email.setContent(mimemultipart);
        if (DatosGlobales.ESTADO_PRODUCCION){
            email.send();
        }
        return mensaje;

    }
    
    public String enviaCorreoConveniosAviso(List<UsuarioDTO> destinatarios, String dia) throws Exception {
        
        String mensajeHtml="Estimados todas y todos, buenos días. \n\n",subj="";
        if(dia.equals("primero")) {
            subj="Inicia ";
            mensajeHtml += "Se informa que a partir de hoy y en un periódo de 10 días naturales, estará habilitado "
                + "el módulo de captura de convenios dentro del Sistema de Seguimiento de Asuntos de la DGGMMA.";
        } else {
            subj = "Termina ";
            mensajeHtml += "Se informa que el día de hoy se cierra "
                + "el módulo de captura de convenios dentro del Sistema de Seguimiento de Asuntos de la DGGMMA.";
        }
        mensajeHtml += "\n\nSaludos cordiales y en caso de dudas favor de comunicarse con Norma Angélica Rivas u Osvaldo Luévano.";
        mensajeHtml="<html><body><table width='80%' align='center'><tr><td style='font-family:Arial, Helvetica, sans-serif; font-size:12px'>"+getTraduccionHTML(mensajeHtml)+"</td></tr></table></body></html>";
        MimeMultipart mimemultipart = new MimeMultipart();
        mimemultipart.setSubType("alternative");
        MimeBodyPart msgText = new MimeBodyPart();
        msgText.setText(mensajeHtml);
        mimemultipart.addBodyPart(msgText);
        MimeMultipart htmlContent = new MimeMultipart();
        htmlContent.setSubType("related");
        MimeBodyPart mimebodypart = new MimeBodyPart();
        mimemultipart.addBodyPart(mimebodypart);
        mimebodypart.setContent(htmlContent);
        MimeBodyPart msgHtml = new MimeBodyPart();
        msgHtml.setContent(mensajeHtml, "text/html");
        htmlContent.addBodyPart(msgHtml);
        SimpleEmail email = new SimpleEmail();
        email.setHostName(servCorreo);

        //Responsables y con permisos
        /*for (UsuarioDTO destino:destinatarios){
          email.addTo(destino.getCorreo1(),destino.getNombre());
        }*/

        email.addTo("fabian.garcia@inegi.org.mx");
        /*email.addTo("ricardo.serna@inegi.org.mx");
        email.addTo("jacqueline.nino@inegi.org.mx");*/
        
        email.setFrom("asuntos.pendientes@inegi.org.mx", "Sistema de Asuntos Pendientes");
        email.setSubject("%"+subj+" periodo de captura de convenios");
        email.setContent(mimemultipart);
        email.send();
        return mensajeHtml;
    }    
    
    private String getTraduccionHTML(String mensaje){
        String html=mensaje;

        // ACENTOS
        html=html.replace("á","&aacute;");
        html=html.replace("é","&eacute;");
        html=html.replace("í","&iacute;");
        html=html.replace("ó","&oacute;");
        html=html.replace("ú","&uacute;");

        html=html.replace("Á","&Aacute;");
        html=html.replace("É","&Eacute;");
        html=html.replace("Í","&Iacute;");
        html=html.replace("Ó","&Oacute;");
        html=html.replace("Ú","&Uacute;");

        // ACENTOS INVERSOS
        html=html.replace("à","&aacute;");
        html=html.replace("è","&eacute;");
        html=html.replace("ì","&iacute;");
        html=html.replace("ò","&oacute;");
        html=html.replace("ù","&uacute;");

        html=html.replace("À","&Aacute;");
        html=html.replace("È","&Eacute;");
        html=html.replace("Ì","&Iacute;");
        html=html.replace("Ò","&Oacute;");
        html=html.replace("Ù","&Uacute;");

        // ñ
        html=html.replace("ñ", "&ntilde;");
        html=html.replace("Ñ", "&Ntilde;");

        // enter
        html=html.replace("\n", "<br>");
        return html;
    }

    private String formaTablaDiaria(List<ReporteArea> reporte, String fecha1, String fecha2 ) {
        String titulo="<p align='center' style='font-family:Arial, Helvetica, sans-serif; font-size:12px; font-weight:bold'><strong>Reporte de Asuntos Pendientes al "+Utiles.getFecha()+"  a las  "+Utiles.getHora()+"</p>";
        String mensaje="";
        String tabla= "";
        int sia_tt_vencidos=0,sia_tt_porvencer=0,sia_tt_activos=0;
        int correos_tt_vencidos=0,correos_tt_porvencer=0,correos_tt_activos=0;
        int com_tt_vencidos=0, com_tt_porvencer=0,com_tt_activos=0;
        int ac_tt_vencidos=0, ac_tt_porvencer=0,ac_tt_activos=0;
        int re_tt_vencidos=0;
        for (ReporteArea ra:reporte){
            AreaDTO area = ra.getArea();
            String nomarea = area.getNivel() == 2 ? area.getSiglas():area.getNombre();
            if(nomarea!="TOTALES") {
                tabla = "<table width=\"30%\" border=\"1\" align=\"center\" cellpadding=\"5\" cellspacing=\"0\" bgcolor=\"#FFFFFF\" style='font-family:Arial, Helvetica, sans-serif; font-size:10px'>" +
                               "<tr><td bgcolor=\"#EBEBEB\" colspan=\"5\" align=\"center\"><strong>"+ nomarea +
                               "</strong></td></tr>"+
                               "<tr><td bgcolor=\"#EBEBEB\" colspan=\"5\" align=\"center\"><strong>PENDIENTES</strong></td></tr>"+
                               "<tr><td bgcolor=\"#EBEBEB\" align=\"center\"><strong>CONCEPTO</strong></td><td align=\"center\" bgcolor=\"#EBEBEB\"><strong>VENCIDOS</strong></td><td bgcolor=\"#EBEBEB\" align=\"center\"><strong>POR VENCER</strong></td><td bgcolor=\"#EBEBEB\" align=\"center\"><strong>ACTIVOS</strong></td><td bgcolor=\"#EBEBEB\" align=\"center\"><strong>TOTAL</strong></td>"+
                               "</tr>";
                int totalVencidos=0,totalPorVencer=0,totalPendientes=0;
                int totalRenglon=0,total_tot=0;
                for (ResumenArea rma : ra.getResumen()) {
                    //System.out.println("ACTIVOS "+rma.getTipoasunto()+" - "+rma.getPendactivos_d());
                    if (!rma.getTipoasunto().equals("REUNIONES PENDIENTES DE REGISTRAR ACUERDOS")) {
                       totalRenglon=rma.getVencidos_d()+rma.getPorvencer_d()+rma.getPendactivos_d();
                        tabla+= "<tr><td align=\"center\">"+rma.getTipoasunto()+"</td>" +
                              "<td align=\"center\"><a href='"+ DatosGlobales.getRutaSistema() +"consultaAsuntosFiltro.do?areaFiltro="+ra.getArea().getIdarea()+"&reporte=D&modo="+rma.getTipoAbreviado()+"&estatusFechaA=VE&estatusResponsable=P&fechas=envio&fecha1="+fecha1+"&fecha2="+fecha2+"'>"+rma.getVencidos_d()+"</a></td>" +
                              "<td align=\"center\"><a href='"+ DatosGlobales.getRutaSistema() +"consultaAsuntosFiltro.do?areaFiltro="+ra.getArea().getIdarea()+"&reporte=D&modo="+rma.getTipoAbreviado()+"&estatusFechaA=AV&estatusResponsable=P&fechas=envio&fecha1="+fecha1+"&fecha2="+fecha2+"'>"+rma.getPorvencer_d()+"</a>"+
                              "</td><td align=\"center\"><a href='"+ DatosGlobales.getRutaSistema() +"consultaAsuntosFiltro.do?areaFiltro="+ra.getArea().getIdarea()+"&reporte=D&modo="+rma.getTipoAbreviado()+"&estatusFechaA=AC&estatusResponsable=P&fechas=envio&fecha1="+fecha1+"&fecha2="+fecha2+"'>"+rma.getPendactivos_d()+"</a>"+
                              "</td><td align=\"center\">"+totalRenglon+"</td></tr>";
                   } /*else {
                       totalRenglon=rma.getVencidos_d();
                       tabla+= "<tr><td align=\"center\">"+rma.getTipoasunto()+"</td>" 
                              + "<td colspan=\"3\" align=\"center\">"
                              + "<a href='"+ DatosGlobales.getRutaSistema() +"consultaAsuntosFiltro.do?areaFiltro="+ra.getArea().getIdarea()+"&reporte=D&modo="+rma.getTipoAbreviado()+"&estatusReunion=RS&fechas=envio&fecha1="+fecha1+"&fecha2="+fecha2+"'>"+rma.getVencidos_d()+"</a>"
                              + "</td>"
                              + "<td align=\"center\">"
                              + "<a href='"+ DatosGlobales.getRutaSistema() +"consultaAsuntosFiltro.do?areaFiltro="+ra.getArea().getIdarea()+"&reporte=D&modo="+rma.getTipoAbreviado()+"&estatusReunion=RS&fechas=envio&fecha1="+fecha1+"&fecha2="+fecha2+"'>"+rma.getVencidos_d()+"</a>"
                              + "</td>" 
                              + "</tr>"; 
                    }*/
                   if (!rma.getTipoasunto().equals("REUNIONES PENDIENTES DE REGISTRAR ACUERDOS")) {
                        totalVencidos+=rma.getVencidos_d();
                        totalPorVencer+=rma.getPorvencer_d();
                        totalPendientes+=rma.getPendactivos_d();
                        total_tot+=totalRenglon;
                    }
                   
// Cuadro resumen
                    if(rma.getTipoasunto().equals("SIA")) {
                        sia_tt_vencidos+=rma.getVencidos_d();
                        sia_tt_porvencer+=rma.getPorvencer_d();
                        sia_tt_activos+=rma.getPendactivos_d();
                    }
                    if(rma.getTipoasunto().equals("COMISIONES")) {
                        com_tt_vencidos+=rma.getVencidos_d();
                        com_tt_porvencer+=rma.getPorvencer_d();
                        com_tt_activos+=rma.getPendactivos_d();
                    }
                    if(rma.getTipoasunto().equals("CORREOS")) {
                        correos_tt_vencidos+=rma.getVencidos_d();
                        correos_tt_porvencer+=rma.getPorvencer_d();
                        correos_tt_activos+=rma.getPendactivos_d();
                    }
                    if(rma.getTipoasunto().equals("ACUERDOS")) {
                        ac_tt_vencidos+=rma.getVencidos_d();
                        ac_tt_porvencer+=rma.getPorvencer_d();
                        ac_tt_activos+=rma.getPendactivos_d();
                    }
                    /*if (rma.getTipoasunto().equals("REUNIONES PENDIENTES DE REGISTRAR ACUERDOS")) {
                        re_tt_vencidos+=rma.getVencidos_d();
                    }*/
                   
                }
                tabla+= "<tr class=\"resaltar\"><td align=\"center\">TOTAL</td><td align=\"center\">"+totalVencidos+
                        "</td><td align=\"center\">"+totalPorVencer+"</td><td align=\"center\">"+ totalPendientes+
                        "</td><td align=\"center\">"+total_tot+"</td></tr>"+
                        "</table>";
                mensaje+=tabla+"<br>";
            }
        }
        int sia_total = sia_tt_vencidos+sia_tt_porvencer+sia_tt_activos;
        int com_total = com_tt_vencidos+com_tt_porvencer+com_tt_activos;
        int correos_total = correos_tt_vencidos+correos_tt_porvencer+correos_tt_activos;
        int ac_total = ac_tt_vencidos+ac_tt_porvencer+ac_tt_activos;
        // RESUMEN
        String tablaIni = "<table width=\"30%\" border=\"1\" align=\"center\" cellpadding=\"5\" cellspacing=\"0\" bgcolor=\"#FFFFFF\" style='font-family:Arial, Helvetica, sans-serif; font-size:10px'>" +
            "<tr><td bgcolor=\"#EBEBEB\" colspan=\"5\" align=\"center\"><strong>TOTALES</strong></td></tr>"+
            "<tr><td bgcolor=\"#EBEBEB\" colspan=\"5\" align=\"center\"><strong>PENDIENTES</strong></td></tr>"+
            "<tr><td bgcolor=\"#EBEBEB\" align=\"center\"><strong>CONCEPTO</strong></td><td align=\"center\" bgcolor=\"#EBEBEB\"><strong>VENCIDOS</strong></td><td bgcolor=\"#EBEBEB\" align=\"center\"><strong>POR VENCER</strong></td><td bgcolor=\"#EBEBEB\" align=\"center\"><strong>ACTIVOS</strong></td><td bgcolor=\"#EBEBEB\" align=\"center\"><strong>TOTAL</strong></td>"+
            "</tr>";
        tablaIni+= "<tr><td align=\"center\">SIA</td>"+
              "<td align=\"center\"><a href='"+ DatosGlobales.getRutaSistema() +"consultaAsuntosFiltro.do?areaFiltro=0&reporte=D&modo=K&estatusFechaA=VE&estatusResponsable=P&fechas=envio&fecha1="+fecha1+"&fecha2="+fecha2+"'>"+sia_tt_vencidos+"</a></td>" +
              "<td align=\"center\"><a href='"+ DatosGlobales.getRutaSistema() +"consultaAsuntosFiltro.do?areaFiltro=0&reporte=D&modo=K&estatusFechaA=AV&estatusResponsable=P&fechas=envio&fecha1="+fecha1+"&fecha2="+fecha2+"'>"+sia_tt_porvencer+"</a>"+
              "</td><td align=\"center\"><a href='"+ DatosGlobales.getRutaSistema() +"consultaAsuntosFiltro.do?areaFiltro=0&reporte=D&modo=K&estatusFechaA=AC&estatusResponsable=P&fechas=envio&fecha1="+fecha1+"&fecha2="+fecha2+"'>"+sia_tt_activos+"</a>"+
              "</td><td align=\"center\">"+sia_total+"</td></tr>";
        tablaIni+= "<tr><td align=\"center\">COMISIONES</td>"+
              "<td align=\"center\">"+com_tt_vencidos+"</a></td>" +
              "<td align=\"center\">"+com_tt_porvencer+"</a></td>"+
              "<td align=\"center\">"+com_tt_activos+"</a>"+
              "</td><td align=\"center\">"+com_total+"</td></tr>";
        tablaIni+= "<tr><td align=\"center\">CORREOS</td>"+
              "<td align=\"center\"><a href='"+ DatosGlobales.getRutaSistema() +"consultaAsuntosFiltro.do?areaFiltro=0&reporte=D&modo=C&estatusFechaA=VE&estatusResponsable=P&fechas=envio&fecha1="+fecha1+"&fecha2="+fecha2+"'>"+correos_tt_vencidos+"</a></td>" +
              "<td align=\"center\"><a href='"+ DatosGlobales.getRutaSistema() +"consultaAsuntosFiltro.do?areaFiltro=0&reporte=D&modo=C&estatusFechaA=VE&estatusResponsable=P&fechas=envio&fecha1="+fecha1+"&fecha2="+fecha2+"'>"+correos_tt_porvencer+"</a></td>" +
              "<td align=\"center\"><a href='"+ DatosGlobales.getRutaSistema() +"consultaAsuntosFiltro.do?areaFiltro=0&reporte=D&modo=C&estatusFechaA=VE&estatusResponsable=P&fechas=envio&fecha1="+fecha1+"&fecha2="+fecha2+"'>"+correos_tt_activos+"</a></td>" +
              "</td><td align=\"center\">"+correos_total+"</td></tr>";
        tablaIni+= "<tr><td align=\"center\">ACUERDOS</td>"+
              "<td align=\"center\"><a href='"+ DatosGlobales.getRutaSistema() +"consultaAsuntosFiltro.do?areaFiltro=0&reporte=D&modo=A&estatusFechaA=VE&estatusResponsable=P&fechas=envio&fecha1="+fecha1+"&fecha2="+fecha2+"'>"+ac_tt_vencidos+"</a></td>" +
              "<td align=\"center\"><a href='"+ DatosGlobales.getRutaSistema() +"consultaAsuntosFiltro.do?areaFiltro=0&reporte=D&modo=A&estatusFechaA=VE&estatusResponsable=P&fechas=envio&fecha1="+fecha1+"&fecha2="+fecha2+"'>"+ac_tt_porvencer+"</a></td>" +
              "<td align=\"center\"><a href='"+ DatosGlobales.getRutaSistema() +"consultaAsuntosFiltro.do?areaFiltro=0&reporte=D&modo=A&estatusFechaA=VE&estatusResponsable=P&fechas=envio&fecha1="+fecha1+"&fecha2="+fecha2+"'>"+ac_tt_activos+"</a></td>" +
              "</td><td align=\"center\">"+ac_total+"</td></tr>";
        /*tablaIni+= "<tr><td align=\"center\">REUNIONES PENDIENTES DE REGISTRAR ACUERDOS</td>" +
               "<td colspan=\"3\" align=\"center\"><a href='"+ DatosGlobales.getRutaSistema() +"consultaAsuntosFiltro.do?areaFiltro=0&reporte=D&modo=R&estatusReunion=RS&fechas=envio&fecha1="+fecha1+"&fecha2="+fecha2+"'>"+re_tt_vencidos+"</a></td>"+
               "<td align=\"center\">"+re_tt_vencidos+"</td></tr>";
        */
            int tot_vencidos=sia_tt_vencidos+com_tt_vencidos+correos_tt_vencidos+ac_tt_vencidos+re_tt_vencidos;
            int tot_porvencer=sia_tt_porvencer+com_tt_porvencer+correos_tt_porvencer+ac_tt_porvencer;
            int tot_activos=sia_tt_activos+com_tt_activos+correos_tt_activos+ac_tt_activos;
            int total_tot=tot_vencidos+tot_porvencer+tot_activos;
        tablaIni+= "<tr class=\"resaltar\"><td align=\"center\">TOTAL</td><td align=\"center\">"+tot_vencidos+
                "</td><td align=\"center\">"+tot_porvencer+"</td><td align=\"center\">"+ tot_activos +
                "</td><td align=\"center\">"+total_tot+"</td></tr>"+
                "</table><br>";
// Fin cuadro resumen

        //mensaje+=tabla;
        mensaje=titulo+tablaIni+mensaje;
        return mensaje;
    }
    
    private String formaTablaSemana(List<ResumenAreaSM> resumen, List<ResumenAreaSM> repSemanal, String semana, int nivel){
        String tablaSemana="", tablaRes="";
        int tot_enviados=0,tot_pendientes=0,tot_atendidos=0;
        int sia_tt_enviados=0,sia_tt_pendientes=0,sia_tt_atendidos=0;
        int com_tt_enviados=0,com_tt_pendientes=0,com_tt_atendidos=0;
        int cor_tt_enviados=0,cor_tt_pendientes=0,cor_tt_atendidos=0;
        int acu_tt_enviados=0,acu_tt_pendientes=0,acu_tt_atendidos=0;
        int reu_tt_pendientes=0;
        int tt_enviados=0,tt_pendientes=0,tt_atendidos=0;
        String area=repSemanal.get(0).getSiglas();
        int idarea=repSemanal.get(0).getIdarea();
        tablaSemana = "<table width='80%' align='center' cellpadding='5' style='font-family:Arial, Helvetica, sans-serif; font-size:10px'>" +
                "<tr><td align='center' style='font-family:Arial, Helvetica, sans-serif; font-size:12px; font-weight:bold'>"+semana+"</td></tr></table>";
        int totPe=0,totEn=0,totAt=0;
        tablaRes += "<table width='80%' border='1' align='center' cellpadding='5' cellspacing='0' style='font-family:Arial, Helvetica, sans-serif; font-size:10px'>" +
                "<tr><td bgcolor='#EBEBEB' colspan='4' align='center'><b>TOTALES</b></td></tr>"+
                "<tr><td bgcolor='#EBEBEB' align='center'><b>CONCEPTO</b></td><td bgcolor='#EBEBEB' align='center'><b>PENDIENTES</b></td><td align='center' bgcolor='#EBEBEB'><b>ENVIADOS</b></td><td bgcolor='#EBEBEB' align='center'><b>ATENDIDOS</b></td></tr>" ;
                for (ResumenAreaSM res : resumen){
                    if(res.getTipoasunto().equals("REUNIONES PENDIENTES DE REGISTRAR ACUERDOS")) {
                        tablaRes+="<tr><td align=\"center\">"+res.getTipoasunto()+"</td>"
                            +"<td align=\"center\" colspan='3'>"+res.getPendientes()+"</td></tr>";
                    } else {
                    tablaRes+="<tr><td align=\"center\">"+res.getTipoasunto()+"</td>"
                            +"<td align=\"center\">"+res.getPendientes()+"</td>"
                            +"<td align=\"center\">"+res.getEnviados()+"</td>"
                            +"<td align=\"center\">"+res.getAtendidos()+"</td></tr>";
                    }
                    totPe += res.getPendientes();
                    totEn += res.getEnviados();
                    totAt += res.getAtendidos();
                }
            tablaRes += "<tr><td align=\"center\"><strong>TOTAL</strong></td><td align=\"center\"><strong>"+totPe+"</strong></td>"
                    + "<td align=\"center\"><strong>"+totEn+"</strong></td>"
                    + "<td align=\"center\"><strong>"+totAt+"</strong></td></tr>";
            tablaRes +="</table>";
        
        tablaSemana += tablaRes+"<br><br>";
            
        tablaSemana += "<table width='80%' border='1' align='center' cellpadding='5' cellspacing='0' style='font-family:Arial, Helvetica, sans-serif; font-size:10px'>" +
                           "<tr><td bgcolor='#EBEBEB' colspan='4' align='center'><b>"+area+"</b></td></tr>"+
                           "<tr><td bgcolor='#EBEBEB' align='center'><b>CONCEPTO</b></td><td bgcolor='#EBEBEB' align='center'><b>PENDIENTES</b></td><td align='center' bgcolor='#EBEBEB'><b>ENVIADOS</b></td><td bgcolor='#EBEBEB' align='center'><b>ATENDIDOS</b></td></tr>";
        
        for(int i = 0; i < repSemanal.size(); i++) {
            ResumenAreaSM res = (ResumenAreaSM) repSemanal.get(i);
            tt_enviados+=res.getEnviados();
            tt_pendientes+=res.getPendientes();
            tt_atendidos+=res.getAtendidos();
            if(res.getTipoasunto().equals("SIA")) {
                sia_tt_enviados+=res.getEnviados();
                sia_tt_pendientes+=res.getPendientes();
                sia_tt_atendidos+=res.getAtendidos();
            }
            if(res.getTipoasunto().equals("COMISIONES")) {
                com_tt_enviados+=res.getEnviados();
                com_tt_pendientes+=res.getPendientes();
                com_tt_atendidos+=res.getAtendidos();
            }
            if(res.getTipoasunto().equals("CORREOS")) {
                cor_tt_enviados+=res.getEnviados();
                cor_tt_pendientes+=res.getPendientes();
                cor_tt_atendidos+=res.getAtendidos();
            }
            if(res.getTipoasunto().equals("ACUERDOS")) {
                acu_tt_enviados+=res.getEnviados();
                acu_tt_pendientes+=res.getPendientes();
                acu_tt_atendidos+=res.getAtendidos();
            }
            if(res.getTipoasunto().equals("REUNIONES")) {
                reu_tt_pendientes+=res.getPendientes();
            }
            if(idarea == res.getIdarea()) {
                if(!res.getTipoasunto().equals("REUNIONES")) {
                    tablaSemana+="<tr><td align='center'>"+res.getTipoasunto()+"</td><td align='center'>"+res.getPendientes()+"</td><td align='center'>"+res.getEnviados()+"</td><td align='center'>"+res.getAtendidos()+"</td></tr>";
                } else tablaSemana+="<tr><td align='center'>REUNIONES PENDIENTES DE REGISTRAR ACUERDOS</td><td align='center' colspan='3'>"+res.getPendientes()+"</td></tr>";
                tot_enviados+=res.getEnviados();
                tot_pendientes+=res.getPendientes();
                tot_atendidos+=res.getAtendidos(); 
                if(res.getNivel() == 2) area=res.getSiglas();
                else area=res.getNombre();
                idarea=res.getIdarea();
            } else {
                if(res.getNivel() == 2) area=res.getSiglas();
                else area=res.getNombre();
                idarea=res.getIdarea();
                tablaSemana+="<tr><td align='center'>TOTAL</td><td align='center'>"+tot_pendientes+"</td>" +
                             "<td align='center'>"+tot_enviados+"</td>"+
                             "<td align='center'>"+tot_atendidos+"</td></tr></table>";
                tablaSemana+="<table><tr><td>&nbsp;</td></tr></table>";
                tablaSemana+="<table width='80%' border='1' align='center' cellpadding='5' cellspacing='0' style='font-family:Arial, Helvetica, sans-serif; font-size:10px'>"+
                          "<tr><td bgcolor='#EBEBEB' colspan='4' align='center'><b>"+area+"</b></td></tr>"+
                          "<tr><td bgcolor='#EBEBEB' align='center'><b>CONCEPTO</b></td><td bgcolor='#EBEBEB' align='center'><b>PENDIENTES</b></td><td align='center' bgcolor='#EBEBEB'><b>ENVIADOS</b></td><td bgcolor='#EBEBEB' align='center'><b>ATENDIDOS</b></td></tr>";
                if(!res.getTipoasunto().equals("REUNIONES")) {
                    tablaSemana+="<tr><td align='center'>"+res.getTipoasunto()+"</td><td align='center'>"+res.getPendientes()+"</td><td align='center'>"+res.getEnviados()+"</td><td align='center'>"+res.getAtendidos()+"</td></tr>";
                } else tablaSemana+="<tr><td align='center'>REUNIONES PENDIENTES DE REGISTRAR ACUERDOS</td><td align='center' colspan='3'>"+res.getPendientes()+"</td></tr>";
                    tot_enviados=res.getEnviados();
                tot_pendientes=res.getPendientes();
                tot_atendidos=res.getAtendidos();
            }
        }
        tablaSemana+="<tr><td align='center'>TOTAL</td><td align='center'>"+tot_pendientes+"</td><td align='center'>"+tot_enviados+"</td><td align='center'>"+tot_atendidos+"</td></tr></table>";
        tablaSemana+="<table><tr><td>&nbsp;</td></tr></table>";
/*        if(nivel == 2) { // Osvaldo pidi� que se eliminara
        tablaSemana+="<table width='80%' border='1' align='center' cellpadding='5' cellspacing='0' style='font-family:Arial, Helvetica, sans-serif; font-size:10px'>"+
                  "<tr><td bgcolor='#EBEBEB' colspan='4' align='center'><b>TOTAL</b></td></tr>"+
                  "<tr><td bgcolor='#EBEBEB' align='center'><b>CONCEPTO</b></td><td bgcolor='#EBEBEB' align='center'><b>PENDIENTES</b></td><td align='center' bgcolor='#EBEBEB'><b>ENVIADOS</b></td><td bgcolor='#EBEBEB' align='center'><b>ATENDIDOS</b></td></tr>"+
                  "<tr><td align='center'>SIA</td><td align='center'>"+sia_tt_pendientes+"</td><td align='center'>"+sia_tt_enviados+"</td><td align='center'>"+sia_tt_atendidos+"</td></tr>"+
                  "<tr><td align='center'>COMISIONES</td><td align='center'>"+com_tt_pendientes+"</td><td align='center'>"+com_tt_enviados+"</td><td align='center'>"+com_tt_atendidos+"</td></tr>"+
                  "<tr><td align='center'>CORREOS</td><td align='center'>"+cor_tt_pendientes+"</td><td align='center'>"+cor_tt_enviados+"</td><td align='center'>"+cor_tt_atendidos+"</td></tr>"+
                  "<tr><td align='center'>ACUERDOS</td><td align='center'>"+acu_tt_pendientes+"</td><td align='center'>"+acu_tt_enviados+"</td><td align='center'>"+acu_tt_atendidos+"</td></tr>"+
                  "<tr><td align='center'>REUNIONES PENDIENTES DE REGISTRAR ACUERDOS</td><td align='center'>"+reu_tt_pendientes+"</td><td colspan='2'>&nbsp;</td></tr>"+
                  "<tr><td align='center'>TOTAL dele</td><td align='center'>"+tt_pendientes+"</td><td align='center'>"+tt_enviados+"</td><td align='center'>"+tt_atendidos+"</td></tr>";
        tablaSemana+="</table>";
        }*/
        //tablaSemana+="<table><tr><td>�ste es un correo informativo, por lo que no es necesario responderlo.</td></tr></table>";
        return tablaSemana;
    }

    private String formaTablaMensual(List<ResumenAreaSM> resumenMensual, List<ResumenAreaSM> mensual, String delMes, String fecha1, String fecha2, int nivel){
        String tablaMens="", tablaRes="";
        int tot_enviados=0,tot_pendientes=0,tot_atendidos=0;
        int sia_tt_enviados=0,sia_tt_pendientes=0,sia_tt_atendidos=0;
        int com_tt_enviados=0,com_tt_pendientes=0,com_tt_atendidos=0;
        int cor_tt_enviados=0,cor_tt_pendientes=0,cor_tt_atendidos=0;
        int acu_tt_enviados=0,acu_tt_pendientes=0,acu_tt_atendidos=0;
        int reu_tt_pendientes=0;
        int tt_enviados=0,tt_pendientes=0,tt_atendidos=0;
        String area=mensual.get(0).getSiglas();
        int idarea=mensual.get(0).getIdarea();
        tablaMens = "<table width='80%' align='center' cellpadding='5' style='font-family:Arial, Helvetica, sans-serif; font-size:10px'>" +
                "<tr><td align='center' style='font-family:Arial, Helvetica, sans-serif; font-size:12px; font-weight:bold'>"+delMes+"</td></tr></table>";
        
        int totPe=0,totEn=0,totAt=0;
        tablaRes += "<table width='80%' border='1' align='center' cellpadding='5' cellspacing='0' style='font-family:Arial, Helvetica, sans-serif; font-size:10px'>" +
                "<tr><td bgcolor='#EBEBEB' colspan='4' align='center'><b>TOTALES</b></td></tr>"+
                "<tr><td bgcolor='#EBEBEB' align='center'><b>CONCEPTO</b></td><td bgcolor='#EBEBEB' align='center'><b>PENDIENTES</b></td><td align='center' bgcolor='#EBEBEB'><b>ENVIADOS</b></td><td bgcolor='#EBEBEB' align='center'><b>ATENDIDOS</b></td></tr>" ;
                for (ResumenAreaSM res : resumenMensual){
                if(res.getTipoasunto().equals("REUNIONES PENDIENTES DE REGISTRAR ACUERDOS")) {
                    tablaRes+="<tr><td align=\"center\">"+res.getTipoasunto()+"</td>"
                        +"<td align=\"center\" colspan='3'>"+res.getPendientes()+"</td></tr>";
                } else {
                tablaRes+="<tr><td align=\"center\">"+res.getTipoasunto()+"</td>"
                        +"<td align=\"center\">"+res.getPendientes()+"</td>"
                        +"<td align=\"center\">"+res.getEnviados()+"</td>"
                        +"<td align=\"center\">"+res.getAtendidos()+"</td></tr>";
                }
               totPe += res.getPendientes();
               totEn += res.getEnviados();
               totAt += res.getAtendidos();
            }
            tablaRes += "<tr><td align=\"center\"><strong>TOTAL</strong></td><td align=\"center\"><strong>"+totPe+"</strong></td>"
                    + "<td align=\"center\"><strong>"+totEn+"</strong></td>"
                    + "<td align=\"center\"><strong>"+totAt+"</strong></td></tr>";
            tablaRes +="</table>";
        
        tablaMens += tablaRes+"<br><br>";
        
        tablaMens += "<table width='80%' border='1' align='center' cellpadding='5' cellspacing='0' style='font-family:Arial, Helvetica, sans-serif; font-size:10px'>" +
                           "<tr><td bgcolor='#EBEBEB' colspan='4' align='center'><b>"+area+"</b></td></tr>"+
                           "<tr><td bgcolor='#EBEBEB' align='center'><b>CONCEPTO</b></td><td bgcolor='#EBEBEB' align='center'><b>PENDIENTES</b></td><td align='center' bgcolor='#EBEBEB'><b>ENVIADOS</b></td><td bgcolor='#EBEBEB' align='center'><b>ATENDIDOS</b></td></tr>";
        
        for(int i = 0; i < mensual.size(); i++) {
            ResumenAreaSM res = (ResumenAreaSM) mensual.get(i);
            tt_enviados+=res.getEnviados();
            tt_pendientes+=res.getPendientes();
            tt_atendidos+=res.getAtendidos();
            if(res.getTipoasunto().equals("SIA")) {
                sia_tt_enviados+=res.getEnviados();
                sia_tt_pendientes+=res.getPendientes();
                sia_tt_atendidos+=res.getAtendidos();
            }
            if(res.getTipoasunto().equals("COMISIONES")) {
                com_tt_enviados+=res.getEnviados();
                com_tt_pendientes+=res.getPendientes();
                com_tt_atendidos+=res.getAtendidos();
            }
            if(res.getTipoasunto().equals("CORREOS")) {
                cor_tt_enviados+=res.getEnviados();
                cor_tt_pendientes+=res.getPendientes();
                cor_tt_atendidos+=res.getAtendidos();
            }
            if(res.getTipoasunto().equals("ACUERDOS")) {
                acu_tt_enviados+=res.getEnviados();
                acu_tt_pendientes+=res.getPendientes();
                acu_tt_atendidos+=res.getAtendidos();
            }
            if(res.getTipoasunto().equals("REUNIONES")) {
                reu_tt_pendientes+=res.getPendientes();
            }
            if(idarea == res.getIdarea()) {
                if(!res.getTipoasunto().equals("REUNIONES")) {
                    tablaMens+="<tr><td align='center'>"+res.getTipoasunto()+"</td><td align='center'>"+res.getPendientes()+"</td><td align='center'>"+res.getEnviados()+"</td><td align='center'>"+res.getAtendidos()+"</td></tr>";
                } else tablaMens+="<tr><td align='center'>REUNIONES PENDIENTES DE REGISTRAR ACUERDOS</td><td align='center' colspan='2'>"+res.getPendientes()+"</td></tr>";
                tot_enviados+=res.getEnviados();
                tot_pendientes+=res.getPendientes();
                tot_atendidos+=res.getAtendidos();
                if(res.getNivel() == 2) area=res.getSiglas();
                else area=res.getNombre();
                idarea=res.getIdarea();
            } else {
                if(res.getNivel() == 2) area=res.getSiglas();
                else area=res.getNombre();
                idarea=res.getIdarea();
                tablaMens+="<tr><td align='center'>TOTAL</td><td align='center'>"+tot_pendientes+"</td>" +
                             "<td align='center'>"+tot_enviados+"</td>"+
                             "<td align='center'>"+tot_atendidos+"</td></tr></table>";
                tablaMens+="<table><tr><td>&nbsp;</td></tr></table>";
                tablaMens+="<table width='80%' border='1' align='center' cellpadding='5' cellspacing='0' style='font-family:Arial, Helvetica, sans-serif; font-size:10px'>"+
                          "<tr><td bgcolor='#EBEBEB' colspan='4' align='center'><b>"+area+"</b></td></tr>"+
                          "<tr><td bgcolor='#EBEBEB' align='center'><b>CONCEPTO</b></td><td bgcolor='#EBEBEB' align='center'><b>PENDIENTES</b></td><td align='center' bgcolor='#EBEBEB'><b>ENVIADOS</b></td><td bgcolor='#EBEBEB' align='center'><b>ATENDIDOS</b></td></tr>";
                if(!res.getTipoasunto().equals("REUNIONES")) {
                    tablaMens+="<tr><td align='center'>"+res.getTipoasunto()+"</td><td align='center'>"+res.getPendientes()+"</td><td align='center'>"+res.getEnviados()+"</td><td align='center'>"+res.getAtendidos()+"</td></tr>";
                } else tablaMens+="<tr><td align='center'>REUNIONES PENDIENTES DE REGISTRAR ACUERDOS</td><td align='center' colspan='2'>"+res.getPendientes()+"</td></tr>";
                tot_enviados=res.getEnviados();
                tot_pendientes=res.getPendientes();
                tot_atendidos=res.getAtendidos();
            }
        }
        tablaMens+="<tr><td align='center'>TOTAL</td><td align='center'>"+tot_pendientes+"</td><td align='center'>"+tot_enviados+"</td><td align='center'>"+tot_atendidos+"</td></tr></table>";
        tablaMens+="<table><tr><td>&nbsp;</td></tr></table>";
        /*if(nivel == 2) {
        tablaMens+="<table width='80%' border='1' align='center' cellpadding='5' cellspacing='0' style='font-family:Arial, Helvetica, sans-serif; font-size:10px'>"+
                  "<tr><td bgcolor='#EBEBEB' colspan='4' align='center'><b>TOTAL</b></td></tr>"+
                  "<tr><td bgcolor='#EBEBEB' align='center'><b>CONCEPTO</b></td><td bgcolor='#EBEBEB' align='center'><b>PENDIENTES</b></td><td align='center' bgcolor='#EBEBEB'><b>ENVIADOS</b></td><td bgcolor='#EBEBEB' align='center'><b>ATENDIDOS</b></td></tr>"+
                  "<tr><td align='center'>SIA</td><td align='center'>"+sia_tt_pendientes+"</td><td align='center'>"+sia_tt_enviados+"</td><td align='center'>"+sia_tt_atendidos+"</td></tr>"+
                  "<tr><td align='center'>COMISIONES</td><td align='center'>"+com_tt_pendientes+"</td><td align='center'>"+com_tt_enviados+"</td><td align='center'>"+com_tt_atendidos+"</td></tr>"+
                  "<tr><td align='center'>CORREOS</td><td align='center'>"+cor_tt_pendientes+"</td><td align='center'>"+cor_tt_enviados+"</td><td align='center'>"+cor_tt_atendidos+"</td></tr>"+
                  "<tr><td align='center'>ACUERDOS</td><td align='center'>"+acu_tt_pendientes+"</td><td align='center'>"+acu_tt_enviados+"</td><td align='center'>"+acu_tt_atendidos+"</td></tr>"+
                  "<tr><td align='center'>REUNIONES PENDIENTES DE REGISTRAR ACUERDOS</td><td align='center'>"+reu_tt_pendientes+"</td><td colspan='2'>&nbsp;</td></tr>"+
                  "<tr><td align='center'>TOTAL borrar</td><td align='center'>"+tt_pendientes+"</td><td align='center'>"+tt_enviados+"</td><td align='center'>"+tt_atendidos+"</td></tr>";
        //tablaMens+="<tr><td colspan=5>�ste es un correo informativo, por lo que no es necesario responderlo.</td></tr>";
        tablaMens+="</table>";
        }*/
        return tablaMens;
    }
    private String formaTablaMensualOld(List<ReporteAreaSM> mensual, String delMes, String fecha1, String fecha2){
        String tablaMens="";
        int total1_m=0,total2_m=0,total3_m=0;
        tablaMens +="<table width='50%' align='center' cellpadding='5' style='font-family:Arial, Helvetica, sans-serif; font-size:10px'>" 
                  + "<td align='center' style='font-family:Arial, Helvetica, sans-serif; font-size:12px; font-weight:bold'>"+delMes+"</td></tr></table>";
        for (ReporteAreaSM ra_m:mensual){
            AreaDTO area_m = ra_m.getArea();
            String nomarea_m = area_m.getNivel() == 2 ? area_m.getSiglas():area_m.getNombre();
            tablaMens += "<table width=\"50%\" border=\"1\" align=\"center\" cellpadding=\"5\" cellspacing=\"0\" bgcolor=\"#FFFFFF\" style='font-family:Arial, Helvetica, sans-serif; font-size:10px'>" +
                        "<tr><td bgcolor=\"#EBEBEB\" colspan=\"4\" align=\"center\"><strong>"+ nomarea_m + "</strong></td></tr>"+
                        "<tr><td bgcolor=\"#EBEBEB\" align=\"center\"<b>CONCEPTO</b></td><td bgcolor=\"#EBEBEB\" align=\"center\"><b>PENDIENTES</b></td><td bgcolor=\"#EBEBEB\" align=\"center\"><b>ENVIADOS</b></td><td bgcolor=\"#EBEBEB\" align=\"center\"><b>ATENDIDOS</b></td></tr>";
            for (ResumenAreaSM rma_m : ra_m.getResumen()) {
                tablaMens+= "<tr><td align=\"center\">"+rma_m.getTipoasunto()+"</td>"
                        + "<td align=\"center\"><a href='"+ DatosGlobales.getRutaSistema() +"consultaAsuntosFiltro.do?areaFiltro="+ra_m.getArea().getIdarea()+"&reporte=D&modo="+rma_m.getTipoAbreviado()+"&fecha1="+fecha1+"&fecha2="+fecha2+"&estatusA=P&fechas=envio'>"+rma_m.getPendientes()+"</a></td>" 
                        + "<td align=\"center\"><a href='"+ DatosGlobales.getRutaSistema() +"consultaAsuntosFiltro.do?areaFiltro="+ra_m.getArea().getIdarea()+"&reporte=D&modo="+rma_m.getTipoAbreviado()+"&fecha1="+fecha1+"&fecha2="+fecha2+"&fechas=envio'>"+rma_m.getEnviados()+"</a></td>"
                        + "<td align=\"center\"><a href='"+ DatosGlobales.getRutaSistema() +"consultaAsuntosFiltro.do?areaFiltro="+ra_m.getArea().getIdarea()+"&reporte=D&modo="+rma_m.getTipoAbreviado()+"&fecha1="+fecha1+"&fecha2="+fecha2+"&estatusA=A&fechas=envio'>"+rma_m.getAtendidos()+"</a></td></tr>";
                total1_m+=rma_m.getEnviados();
                total2_m+=rma_m.getPendientes();
                total3_m+=rma_m.getAtendidos();
            }
            tablaMens+= "<tr class=\"resaltar\"><td align=\"center\">TOTAL</td></td>"+
                    "<td align=\"center\">"+total2_m+"</td><td align=\"center\">"+total1_m+"<td align=\"center\">"+ total3_m+"</td></tr>"+
                    "</table>";
            tablaMens+="<table><tr><td>&nbsp;</td></tr></table>";
            total1_m=0;
            total2_m=0;
            total3_m=0;
        }
        return tablaMens;
    }
}
