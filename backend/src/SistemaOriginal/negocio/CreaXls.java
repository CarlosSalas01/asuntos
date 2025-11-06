/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.negocio;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AccionBean;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AreaBean;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AsuntoBean;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.ResponsableBean;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author RICARDO.SERNA
 */
public class CreaXls {
    
    String strEstatus="",strPres="", strIntExt="", strPriori="", strDiasAt="", strDiasRet="";
    String strEstatusR="";
    int merge1=0, merge2=0, rowspan=0;
    
    public void XlsSia(OutputStream out, List<AsuntoBean> asuntos) {
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("Hoja1");
            sheet.setColumnWidth(1, 3000);
            sheet.setColumnWidth(2, 15000);
            sheet.setColumnWidth(3, 12000);
            sheet.setColumnWidth(12, 5000);
            HSSFFont fEnc = wb.createFont(); fEnc.setFontHeightInPoints((short) 10); fEnc.setFontName(fEnc.FONT_ARIAL);
            HSSFCellStyle sEnc = wb.createCellStyle(); sEnc.setFont(fEnc);
            sEnc.setAlignment(HorizontalAlignment.CENTER); sEnc.setVerticalAlignment(VerticalAlignment.CENTER);
            HSSFFont fCab = wb.createFont(); fCab.setFontHeightInPoints((short) 8); fCab.setFontName(fCab.FONT_ARIAL);
            HSSFCellStyle sCab = wb.createCellStyle(); sCab.setFont(fCab);
            HSSFFont fDet = wb.createFont(); fDet.setFontHeightInPoints((short) 7); fDet.setFontName(fDet.FONT_ARIAL);
            HSSFCellStyle sDet = wb.createCellStyle(); sDet.setFont(fDet);
            sDet.setVerticalAlignment(VerticalAlignment.CENTER);
            HSSFCellStyle sDetC = wb.createCellStyle(); sDetC.setFont(fDet);
            sDetC.setVerticalAlignment(VerticalAlignment.CENTER); sDetC.setAlignment(HorizontalAlignment.CENTER);
            CellStyle wrapStyle = wb.createCellStyle(); wrapStyle.setFont(fDet);
            wrapStyle.setAlignment(HorizontalAlignment.CENTER); wrapStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            wrapStyle.setWrapText(true);
            HSSFRow enc1 = sheet.createRow(0);
            HSSFCell textoEnc = enc1.createCell((short) 0); textoEnc.setCellStyle(sEnc); textoEnc.setCellValue("ASUNTOS MÓDULO SIA");
            sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$M$1"));
            int linea=2;
            HSSFRow row = null;
            HSSFCell cell = null;
            row = sheet.createRow(linea);
            cell = row.createCell((short) 0); cell.setCellStyle(sEnc); cell.setCellValue("Asunto");
            cell = row.createCell((short) 1); cell.setCellStyle(sEnc); cell.setCellValue("Clasificación");
            cell = row.createCell((short) 2); cell.setCellStyle(sEnc); cell.setCellValue("Descripción");
            cell = row.createCell((short) 3); cell.setCellStyle(sEnc); cell.setCellValue("Instrucción");
            cell = row.createCell((short) 4); cell.setCellStyle(sEnc); cell.setCellValue("Fecha envío");
            cell = row.createCell((short) 5); cell.setCellStyle(sEnc); cell.setCellValue("Fecha vencimiento");
            cell = row.createCell((short) 6); cell.setCellStyle(sEnc); cell.setCellValue("Destinatarios");
            cell = row.createCell((short) 7); cell.setCellStyle(sEnc); cell.setCellValue("Estatus responsable");
            cell = row.createCell((short) 8); cell.setCellStyle(sEnc); cell.setCellValue("Avance");
            cell = row.createCell((short) 9); cell.setCellStyle(sEnc); cell.setCellValue("Fecha de atención");
            cell = row.createCell((short) 10); cell.setCellStyle(sEnc); cell.setCellValue("Días proceso\r\nproceso/\r\natención");
            cell = row.createCell((short) 11); cell.setCellStyle(sEnc); cell.setCellValue("Días retraso");
            cell = row.createCell((short) 12); cell.setCellStyle(sEnc); cell.setCellValue("Observaciones");
            linea++;
            linea++;
            for(AsuntoBean as : asuntos) {
                rowspan = as.getNoResponsables();
                merge1=linea+1;
                merge2=linea+rowspan;
                if(as.getEstatus()!= null) {
                    if(as.getEstatus().equals("A")) strEstatus="ATENDIDO";
                    if(as.getEstatus().equals("P")) strEstatus="PENDIENTE";
                } else strEstatus="";
                if(as.getPresidencia()!= null) {
                    if(as.getPresidencia().equals("P")) strPres="Presidencia\r\nC/Resp.";
                    if(as.getPresidencia().equals("N")) strPres="Presidencia\r\nS/Resp.";
                } else strPres="";
                if(as.getFuente().equals("I")) strIntExt="INTERNO";
                if(as.getFuente().equals("E")) strIntExt="EXTERNO";
                if(as.getUrgente()!= null) {
                    if(as.getUrgente().equals("S")) strPriori="URGENTE";
                    if(as.getUrgente().equals("1")) strPriori="PRIORIDAD 1";
                    if(as.getUrgente().equals("2")) strPriori="ATENCIÓN NORMAL";
                    if(as.getUrgente().equals("A")) strPriori="ADMINISTRATIVO";
                } else strPriori="";
                row = sheet.createRow(linea);
                if(rowspan > 1) {
                    cell = row.createCell((short) 0); cell.setCellStyle(wrapStyle); cell.setCellValue(new HSSFRichTextString(as.getIdconsecutivo()+"\r\n"+strEstatus));
                    sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+merge1+":$A$"+merge2));
                    cell = row.createCell((short) 1); cell.setCellStyle(wrapStyle); cell.setCellValue(new HSSFRichTextString(strPres+"\r\n"+as.getNocontrol()+"\r\n"+strIntExt));
                    sheet.addMergedRegion(CellRangeAddress.valueOf("$B$"+merge1+":$B$"+merge2));
                    cell = row.createCell((short) 2); cell.setCellStyle(sDet); cell.setCellValue(transBR(as.getDescripcionFormatoHTML()));
                    sheet.addMergedRegion(CellRangeAddress.valueOf("$C$"+merge1+":$C$"+merge2));
                    cell = row.createCell((short) 3); cell.setCellStyle(sDet); cell.setCellValue(as.getEstatustexto());
                    sheet.addMergedRegion(CellRangeAddress.valueOf("$d$"+merge1+":$d$"+merge2));
                    cell = row.createCell((short) 4); cell.setCellStyle(sDet); cell.setCellValue(as.getFechaingresoFormatoTexto());
                    sheet.addMergedRegion(CellRangeAddress.valueOf("$E$"+merge1+":$E$"+merge2));
                    cell = row.createCell((short) 5); cell.setCellStyle(wrapStyle); cell.setCellValue(new HSSFRichTextString(strPriori+"\r\n"+as.getFechaoriginalFormatoTexto()));
                    sheet.addMergedRegion(CellRangeAddress.valueOf("$F$"+merge1+":$F$"+merge2));
                    cell = row.createCell((short) 12); cell.setCellStyle(sDet); cell.setCellValue(as.getObservacionesFormatoHTML());
                    sheet.addMergedRegion(CellRangeAddress.valueOf("$M$"+merge1+":$M$"+merge2));
                } else {
                    cell = row.createCell((short) 0); cell.setCellStyle(wrapStyle); cell.setCellValue(new HSSFRichTextString(as.getIdconsecutivo()+"\r\n"+strEstatus));
                    cell = row.createCell((short) 1); cell.setCellStyle(wrapStyle); cell.setCellValue(new HSSFRichTextString(strPres+"\r\n"+as.getNocontrol()+"\r\n"+strIntExt));
                    cell = row.createCell((short) 2); cell.setCellStyle(sDet); cell.setCellValue(transBR(as.getDescripcionFormatoHTML()));
                    cell = row.createCell((short) 3); cell.setCellStyle(sDet); cell.setCellValue(as.getEstatustexto());
                    cell = row.createCell((short) 4); cell.setCellStyle(sDet); cell.setCellValue(as.getFechaingresoFormatoTexto());
                    cell = row.createCell((short) 5); cell.setCellStyle(wrapStyle); cell.setCellValue(new HSSFRichTextString(strPriori+"\r\n"+as.getFechaoriginalFormatoTexto()));
                    cell = row.createCell((short) 12); cell.setCellStyle(sDet); cell.setCellValue(as.getObservacionesFormatoHTML());
                }
                for(ResponsableBean resp : as.getResponsables()) {
                    if(resp.getDatos().getEstatus().equals("A")) strEstatusR="ATENDIDO";
                    if(resp.getDatos().getEstatus().equals("P")) strEstatusR="PENDIENTE";
                    if(resp.getDatos().getDiasatencion()>0) strDiasAt=String.valueOf(resp.getDatos().getDiasatencion());
                    if(resp.getDatos().getDiasretraso()>0) strDiasRet=String.valueOf(resp.getDatos().getDiasretraso());
                    cell = row.createCell((short) 6); cell.setCellStyle(sDet); cell.setCellValue(resp.getArea().getSiglas());
                    cell = row.createCell((short) 7); cell.setCellStyle(sDetC); cell.setCellValue(strEstatusR);
                    cell = row.createCell((short) 8); cell.setCellStyle(sDetC); cell.setCellValue(resp.getDatos().getAvance()+" %");
                    cell = row.createCell((short) 9); cell.setCellStyle(sDetC); cell.setCellValue(resp.getFechaatencionFormatoTexto());
                    cell = row.createCell((short) 10); cell.setCellStyle(sDetC); cell.setCellValue(strDiasAt);
                    cell = row.createCell((short) 11); cell.setCellStyle(sDetC); cell.setCellValue(strDiasRet);
                    linea++;
                    row = sheet.createRow(linea);
                    strDiasAt="";
                    strDiasRet="";
                }
            }
            wb.write(out);
            out.flush();
            out.close();
            
            /*String archivo =  ruta+"Sia.xls";
            FileOutputStream arch = new FileOutputStream(archivo);
            wb.write(arch);
            arch.flush();
            arch.close();
            */
        } catch (Exception e) {
            e.toString();
        }
    }

    public void XlsCorreos(OutputStream out, List<AsuntoBean> asuntos) {
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("Hoja1");
            sheet.setColumnWidth(1, 3000);
            sheet.setColumnWidth(2, 5000);
            sheet.setColumnWidth(3, 15000);
            sheet.setColumnWidth(5, 4000);
            sheet.setColumnWidth(12, 5000);
            HSSFFont fEnc = wb.createFont(); fEnc.setFontHeightInPoints((short) 10); fEnc.setFontName(fEnc.FONT_ARIAL);
            HSSFCellStyle sEnc = wb.createCellStyle(); sEnc.setFont(fEnc);
            sEnc.setAlignment(HorizontalAlignment.CENTER); sEnc.setVerticalAlignment(VerticalAlignment.CENTER);
            HSSFFont fCab = wb.createFont(); fCab.setFontHeightInPoints((short) 8); fCab.setFontName(fCab.FONT_ARIAL);
            HSSFCellStyle sCab = wb.createCellStyle(); sCab.setFont(fCab);
            HSSFFont fDet = wb.createFont(); fDet.setFontHeightInPoints((short) 7); fDet.setFontName(fDet.FONT_ARIAL);
            HSSFCellStyle sDet = wb.createCellStyle(); sDet.setFont(fDet);
            sDet.setVerticalAlignment(VerticalAlignment.CENTER);
            HSSFCellStyle sDetC = wb.createCellStyle(); sDetC.setFont(fDet);
            sDetC.setVerticalAlignment(VerticalAlignment.CENTER); sDetC.setAlignment(HorizontalAlignment.CENTER);
            CellStyle wrapStyle = wb.createCellStyle(); wrapStyle.setFont(fDet);
            wrapStyle.setAlignment(HorizontalAlignment.CENTER); wrapStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            wrapStyle.setWrapText(true);
            HSSFRow enc1 = sheet.createRow(0);
            HSSFCell textoEnc = enc1.createCell((short) 0); textoEnc.setCellStyle(sEnc); textoEnc.setCellValue("ASUNTOS MÓDULO CORREOS");
            sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$M$1"));
            int linea=2;
            HSSFRow row = null;
            HSSFCell cell = null;
            row = sheet.createRow(linea);
            cell = row.createCell((short) 0); cell.setCellStyle(sEnc); cell.setCellValue("Asunto");
            cell = row.createCell((short) 1); cell.setCellStyle(sEnc); cell.setCellValue("Clasificación");
            cell = row.createCell((short) 2); cell.setCellStyle(sEnc); cell.setCellValue("Remitente");
            cell = row.createCell((short) 3); cell.setCellStyle(sEnc); cell.setCellValue("Descripción");
            cell = row.createCell((short) 4); cell.setCellStyle(sEnc); cell.setCellValue("Fecha envío");
            cell = row.createCell((short) 5); cell.setCellStyle(sEnc); cell.setCellValue("Fecha vencimiento");
            cell = row.createCell((short) 6); cell.setCellStyle(sEnc); cell.setCellValue("Destinatarios");
            cell = row.createCell((short) 7); cell.setCellStyle(sEnc); cell.setCellValue("Estatus responsable");
            cell = row.createCell((short) 8); cell.setCellStyle(sEnc); cell.setCellValue("Avance");
            cell = row.createCell((short) 9); cell.setCellStyle(sEnc); cell.setCellValue("Fecha de atención");
            cell = row.createCell((short) 10); cell.setCellStyle(sEnc); cell.setCellValue("Días proceso\r\nproceso/\r\natención");
            cell = row.createCell((short) 11); cell.setCellStyle(sEnc); cell.setCellValue("Días retraso");
            cell = row.createCell((short) 12); cell.setCellStyle(sEnc); cell.setCellValue("Observaciones");
            linea++;
            linea++;
            for(AsuntoBean as : asuntos) {
                rowspan = as.getNoResponsables();
                merge1=linea+1;
                merge2=linea+rowspan;
                if(as.getEstatus() != null) {
                    if(as.getEstatus().equals("A")) strEstatus="ATENDIDO";
                    if(as.getEstatus().equals("P")) strEstatus="PENDIENTE";
                } else strEstatus="";
                if(as.getPresidencia() != null) if(as.getPresidencia().equals("P")) strPres="Presidencia";
                else strPres="";
                if(as.getFuente().equals("I")) strIntExt="INTERNO";
                if(as.getFuente().equals("E")) strIntExt="EXTERNO";
                if(as.getUrgente() != null) {
                    if(as.getUrgente().equals("S")) strPriori="URGENTE";
                    if(as.getUrgente().equals("1")) strPriori="PRIORIDAD 1";
                    if(as.getUrgente().equals("2")) strPriori="ATENCIÓN NORMAL";
                    if(as.getUrgente().equals("A")) strPriori="ADMINISTRATIVO";
                } else strPriori="";
                row = sheet.createRow(linea);
                if(rowspan > 1) {
                    cell = row.createCell((short) 0); cell.setCellStyle(wrapStyle); cell.setCellValue(new HSSFRichTextString(as.getIdconsecutivo()+"\r\n"+strEstatus));
                    sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+merge1+":$A$"+merge2));
                    cell = row.createCell((short) 1); cell.setCellStyle(wrapStyle); cell.setCellValue(new HSSFRichTextString(strPres+"\r\n"+strIntExt));
                    sheet.addMergedRegion(CellRangeAddress.valueOf("$B$"+merge1+":$B$"+merge2));
                    cell = row.createCell((short) 2); cell.setCellStyle(sDet); cell.setCellValue(as.getRemitente().getDatos().getNombre());
                    sheet.addMergedRegion(CellRangeAddress.valueOf("$C$"+merge1+":$C$"+merge2));
                    cell = row.createCell((short) 3); cell.setCellStyle(sDet); cell.setCellValue(transBR(as.getDescripcionFormatoHTML()));
                    sheet.addMergedRegion(CellRangeAddress.valueOf("$d$"+merge1+":$d$"+merge2));
                    cell = row.createCell((short) 4); cell.setCellStyle(sDet); cell.setCellValue(as.getFechaingresoFormatoTexto());
                    sheet.addMergedRegion(CellRangeAddress.valueOf("$E$"+merge1+":$E$"+merge2));
                    cell = row.createCell((short) 5); cell.setCellStyle(wrapStyle); cell.setCellValue(new HSSFRichTextString(strPriori+"\r\n"+as.getFechaoriginalFormatoTexto()));
                    sheet.addMergedRegion(CellRangeAddress.valueOf("$F$"+merge1+":$F$"+merge2));
                    cell = row.createCell((short) 12); cell.setCellStyle(sDet); cell.setCellValue(as.getObservacionesFormatoHTML());
                    sheet.addMergedRegion(CellRangeAddress.valueOf("$M$"+merge1+":$M$"+merge2));
                } else {
                    cell = row.createCell((short) 0); cell.setCellStyle(wrapStyle); cell.setCellValue(new HSSFRichTextString(as.getIdconsecutivo()+"\r\n"+strEstatus));
                    cell = row.createCell((short) 1); cell.setCellStyle(wrapStyle); cell.setCellValue(new HSSFRichTextString(strPres+"\r\n"+strIntExt));
                    cell = row.createCell((short) 2); cell.setCellStyle(sDet); cell.setCellValue(as.getRemitente().getDatos().getNombre());
                    cell = row.createCell((short) 3); cell.setCellStyle(sDet); cell.setCellValue(transBR(as.getDescripcionFormatoHTML()));
                    cell = row.createCell((short) 4); cell.setCellStyle(sDet); cell.setCellValue(as.getFechaingresoFormatoTexto());
                    cell = row.createCell((short) 5); cell.setCellStyle(wrapStyle); cell.setCellValue(new HSSFRichTextString(strPriori+"\r\n"+as.getFechaoriginalFormatoTexto()));
                    cell = row.createCell((short) 12); cell.setCellStyle(sDet); cell.setCellValue(as.getObservacionesFormatoHTML());
                }
                for(ResponsableBean resp : as.getResponsables()) {
                    if(resp.getDatos().getEstatus().equals("A")) strEstatusR="ATENDIDO";
                    if(resp.getDatos().getEstatus().equals("P")) strEstatusR="PENDIENTE";
                    if(resp.getDatos().getDiasatencion()>0) strDiasAt=String.valueOf(resp.getDatos().getDiasatencion());
                    if(resp.getDatos().getDiasretraso()>0) strDiasRet=String.valueOf(resp.getDatos().getDiasretraso());
                    cell = row.createCell((short) 6); cell.setCellStyle(sDet); cell.setCellValue(resp.getArea().getSiglas());
                    cell = row.createCell((short) 7); cell.setCellStyle(sDetC); cell.setCellValue(strEstatusR);
                    cell = row.createCell((short) 8); cell.setCellStyle(sDetC); cell.setCellValue(resp.getDatos().getAvance()+" %");
                    cell = row.createCell((short) 9); cell.setCellStyle(sDetC); cell.setCellValue(resp.getFechaatencionFormatoTexto());
                    cell = row.createCell((short) 10); cell.setCellStyle(sDetC); cell.setCellValue(strDiasAt);
                    cell = row.createCell((short) 11); cell.setCellStyle(sDetC); cell.setCellValue(strDiasRet);
                    linea++;
                    row = sheet.createRow(linea);
                    strDiasAt="";
                    strDiasRet="";
                }
            }
            wb.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.toString();
        }
    }
    
    public void XlsReuniones(OutputStream out, List<AsuntoBean> asuntos) {
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("Hoja1");
            sheet.setColumnWidth(1, 3000);
            sheet.setColumnWidth(3, 12000);
            sheet.setColumnWidth(4, 12000);
            sheet.setColumnWidth(9, 8000);
            HSSFFont fEnc = wb.createFont(); fEnc.setFontHeightInPoints((short) 10); fEnc.setFontName(fEnc.FONT_ARIAL);
            
            HSSFCellStyle sEnc = wb.createCellStyle(); sEnc.setFont(fEnc);
            sEnc.setAlignment(HorizontalAlignment.CENTER); sEnc.setVerticalAlignment(VerticalAlignment.CENTER);
            HSSFFont fCab = wb.createFont(); fCab.setFontHeightInPoints((short) 8); fCab.setFontName(fCab.FONT_ARIAL);
            
            HSSFCellStyle sCab = wb.createCellStyle(); sCab.setFont(fCab);
            HSSFFont fDet = wb.createFont(); fDet.setFontHeightInPoints((short) 7); fDet.setFontName(fDet.FONT_ARIAL);
            
            HSSFCellStyle sDet = wb.createCellStyle(); sDet.setFont(fDet);
            sDet.setAlignment(HorizontalAlignment.CENTER); sDet.setVerticalAlignment(VerticalAlignment.CENTER);
            HSSFCellStyle sDetIzq = wb.createCellStyle(); sDetIzq.setFont(fDet); sDetIzq.setVerticalAlignment(VerticalAlignment.CENTER);
            
            CellStyle wrapStyle = wb.createCellStyle(); wrapStyle.setFont(fDet);
            wrapStyle.setAlignment(HorizontalAlignment.CENTER); wrapStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            wrapStyle.setWrapText(true);
            CellStyle wrapIzq = wb.createCellStyle(); wrapIzq.setFont(fDet); wrapIzq.setVerticalAlignment(VerticalAlignment.CENTER);
            wrapIzq.setWrapText(true);
            
            HSSFRow enc1 = sheet.createRow(0);
            HSSFCell textoEnc = enc1.createCell((short) 0); textoEnc.setCellStyle(sEnc); textoEnc.setCellValue("ASUNTOS MÓDULO REUNIONES");
            sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$J$1"));
            int linea=2;
            HSSFRow row = null;
            HSSFCell cell = null;
            row = sheet.createRow(linea);
            cell = row.createCell((short) 0); cell.setCellStyle(sEnc); cell.setCellValue("Id");
            cell = row.createCell((short) 1); cell.setCellStyle(sEnc); cell.setCellValue("Total acuerdos");
            //cell = row.createCell((short) 2); cell.setCellStyle(sEnc); cell.setCellValue("Atendido sin acuerdos");
            cell = row.createCell((short) 2); cell.setCellStyle(sEnc); cell.setCellValue("Fecha");
            cell = row.createCell((short) 3); cell.setCellStyle(sEnc); cell.setCellValue("Tema");
            cell = row.createCell((short) 4); cell.setCellStyle(sEnc); cell.setCellValue("Objetivo");
            cell = row.createCell((short) 5); cell.setCellStyle(sEnc); cell.setCellValue("Lugar");
            cell = row.createCell((short) 6); cell.setCellStyle(sEnc); cell.setCellValue("Horario");
            cell = row.createCell((short) 7); cell.setCellStyle(sEnc); cell.setCellValue("Responsable");
            cell = row.createCell((short) 8); cell.setCellStyle(sEnc); cell.setCellValue("Corresponsables");
            cell = row.createCell((short) 9); cell.setCellStyle(sEnc); cell.setCellValue("Asistentes");
            linea++;
            linea++;
            for(AsuntoBean as : asuntos) {
                row = sheet.createRow(linea);
                cell = row.createCell((short) 0); cell.setCellStyle(sDet); cell.setCellValue(as.getIdconsecutivo());
                cell = row.createCell((short) 1); cell.setCellStyle(sDet); cell.setCellValue(as.getAccionesRealizadas());
                cell = row.createCell((short) 2); cell.setCellStyle(sDet); cell.setCellValue(as.getFechaingresoFormatoTexto());
                cell = row.createCell((short) 3); cell.setCellStyle(wrapIzq); cell.setCellValue(transBR(as.getDescripcionFormatoHTML()));
                cell = row.createCell((short) 4); cell.setCellStyle(sDetIzq); cell.setCellValue(as.getEstatustexto());
                cell = row.createCell((short) 5); cell.setCellStyle(sDetIzq); cell.setCellValue(as.getLugar());
                cell = row.createCell((short) 6); cell.setCellStyle(sDet); cell.setCellValue(as.getInformesunidad());
                cell = row.createCell((short) 7); cell.setCellStyle(sDetIzq); cell.setCellValue(as.getResponsable().getDatos().getSiglas());
                for(AreaBean resp : as.getCorresponsables()) {
                    cell = row.createCell((short) 8); cell.setCellStyle(sDetIzq); cell.setCellValue(new HSSFRichTextString(resp.getDatos().getSiglas()+"\r\n"));
                }
                cell = row.createCell((short) 9); cell.setCellStyle(wrapIzq); cell.setCellValue(transBR(as.getAsistentesFormatoHTML()));
                linea++;
            }
            wb.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.toString();
        }
    }
    
    public void XlsAcuerdos(OutputStream out, List<AccionBean> accion) {
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("Hoja1");
            sheet.setColumnWidth(1, 3000);
            sheet.setColumnWidth(2, 15000);
            sheet.setColumnWidth(4, 3000);
            sheet.setColumnWidth(5, 3000);
            HSSFFont fEnc = wb.createFont(); fEnc.setFontHeightInPoints((short) 10); fEnc.setFontName(fEnc.FONT_ARIAL);
            HSSFCellStyle sEnc = wb.createCellStyle(); sEnc.setFont(fEnc);
            sEnc.setAlignment(HorizontalAlignment.CENTER); sEnc.setVerticalAlignment(VerticalAlignment.CENTER);
            HSSFFont fCab = wb.createFont(); fCab.setFontHeightInPoints((short) 8); fCab.setFontName(fCab.FONT_ARIAL);
            HSSFCellStyle sCab = wb.createCellStyle(); sCab.setFont(fCab);
            HSSFFont fDet = wb.createFont(); fDet.setFontHeightInPoints((short) 7); fDet.setFontName(fDet.FONT_ARIAL);
            HSSFCellStyle sDet = wb.createCellStyle(); sDet.setFont(fDet);
            sDet.setVerticalAlignment(VerticalAlignment.CENTER);
            HSSFCellStyle sDetC = wb.createCellStyle(); sDetC.setFont(fDet);
            sDetC.setVerticalAlignment(VerticalAlignment.CENTER); sDetC.setAlignment(HorizontalAlignment.CENTER);
            CellStyle wrapStyle = wb.createCellStyle(); wrapStyle.setFont(fDet);
            wrapStyle.setAlignment(HorizontalAlignment.CENTER); wrapStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            wrapStyle.setWrapText(true);
            HSSFRow enc1 = sheet.createRow(0);
            HSSFCell textoEnc = enc1.createCell((short) 0); textoEnc.setCellStyle(sEnc); textoEnc.setCellValue("ASUNTOS MÓDULO ACUERDOS");
            sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$K$1"));
            int linea=2;
            HSSFRow row = null;
            HSSFCell cell = null;
            row = sheet.createRow(linea);
            cell = row.createCell((short) 0); cell.setCellStyle(sEnc); cell.setCellValue("Reunión");
            cell = row.createCell((short) 1); cell.setCellStyle(sEnc); cell.setCellValue("Acuerdo");
            cell = row.createCell((short) 2); cell.setCellStyle(sEnc); cell.setCellValue("Descripción");
            cell = row.createCell((short) 3); cell.setCellStyle(sEnc); cell.setCellValue("Fecha envío");
            cell = row.createCell((short) 4); cell.setCellStyle(sEnc); cell.setCellValue("Fecha vencimiento");
            cell = row.createCell((short) 5); cell.setCellStyle(sEnc); cell.setCellValue("Responsables");
            cell = row.createCell((short) 6); cell.setCellStyle(sEnc); cell.setCellValue("Estatus responsable");
            cell = row.createCell((short) 7); cell.setCellStyle(sEnc); cell.setCellValue("Avance");
            cell = row.createCell((short) 8); cell.setCellStyle(sEnc); cell.setCellValue("Fecha de atención");
            cell = row.createCell((short) 9); cell.setCellStyle(sEnc); cell.setCellValue("Días proceso\r\nproceso/\r\natención");
            cell = row.createCell((short) 10); cell.setCellStyle(sEnc); cell.setCellValue("Días retraso");
            linea++;
            linea++;
            for(AccionBean ac : accion) {
                rowspan = ac.getNoResponsables();
                merge1=linea+1;
                merge2=linea+rowspan;
                if(ac.getEstatus().equals("A")) strEstatus="ATENDIDO";
                if(ac.getEstatus().equals("P")) strEstatus="PENDIENTE";
                if(ac.getPrioridad() != null) {
                    if(ac.getPrioridad().equals("S")) strPriori="URGENTE";
                    if(ac.getPrioridad().equals("1")) strPriori="PRIORIDAD 1";
                    if(ac.getPrioridad().equals("2")) strPriori="ATENCIÓN NORMAL";
                    if(ac.getPrioridad().equals("A")) strPriori="ADMINISTRATIVO";
                } else strPriori="";
                row = sheet.createRow(linea);
                if(rowspan > 1) {
                    cell = row.createCell((short) 0); cell.setCellStyle(wrapStyle); cell.setCellValue(ac.getIdAsuntoConsecutivo());
                    sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+merge1+":$A$"+merge2));
                    cell = row.createCell((short) 1); cell.setCellStyle(wrapStyle); cell.setCellValue(new HSSFRichTextString(ac.getIdconsecutivo()+"\r\n"+strEstatus));
                    sheet.addMergedRegion(CellRangeAddress.valueOf("$B$"+merge1+":$B$"+merge2));
                    cell = row.createCell((short) 2); cell.setCellStyle(sDet); cell.setCellValue(transBR(ac.getDescripcionFormatoHTML()));
                    sheet.addMergedRegion(CellRangeAddress.valueOf("$C$"+merge1+":$C$"+merge2));
                    cell = row.createCell((short) 3); cell.setCellStyle(sDet); cell.setCellValue(ac.getFechaenvioFormatoTexto());
                    sheet.addMergedRegion(CellRangeAddress.valueOf("$d$"+merge1+":$d$"+merge2));
                    cell = row.createCell((short) 4); cell.setCellStyle(wrapStyle); cell.setCellValue(new HSSFRichTextString(strPriori+"\r\n"+ac.getFechaoriginalFormatoTexto()));
                    sheet.addMergedRegion(CellRangeAddress.valueOf("$e$"+merge1+":$e$"+merge2));
                } else {
                    cell = row.createCell((short) 0); cell.setCellStyle(wrapStyle); cell.setCellValue(ac.getIdAsuntoConsecutivo());
                    cell = row.createCell((short) 1); cell.setCellStyle(wrapStyle); cell.setCellValue(new HSSFRichTextString(ac.getIdconsecutivo()+"\r\n"+strEstatus));
                    cell = row.createCell((short) 2); cell.setCellStyle(sDet); cell.setCellValue(transBR(ac.getDescripcionFormatoHTML()));
                    cell = row.createCell((short) 3); cell.setCellStyle(sDet); cell.setCellValue(ac.getFechaenvioFormatoTexto());
                    cell = row.createCell((short) 4); cell.setCellStyle(wrapStyle); cell.setCellValue(new HSSFRichTextString(strPriori+"\r\n"+ac.getFechaoriginalFormatoTexto()));
                }
                for(ResponsableBean resp : ac.getResponsables()) {
                    if(resp.getDatos().getEstatus().equals("A")) strEstatusR="ATENDIDO";
                    if(resp.getDatos().getEstatus().equals("P")) strEstatusR="PENDIENTE";
                    if(resp.getDatos().getDiasatencion()>0) strDiasAt=String.valueOf(resp.getDatos().getDiasatencion());
                    if(resp.getDatos().getDiasretraso()>0) strDiasRet=String.valueOf(resp.getDatos().getDiasretraso());
                    cell = row.createCell((short) 5); cell.setCellStyle(sDet); cell.setCellValue(resp.getArea().getSiglas());
                    cell = row.createCell((short) 6); cell.setCellStyle(sDetC); cell.setCellValue(strEstatusR);
                    cell = row.createCell((short) 7); cell.setCellStyle(sDetC); cell.setCellValue(resp.getDatos().getAvance()+" %");
                    cell = row.createCell((short) 8); cell.setCellStyle(sDetC); cell.setCellValue(resp.getFechaatencionFormatoTexto());
                    cell = row.createCell((short) 9); cell.setCellStyle(sDetC); cell.setCellValue(strDiasAt);
                    cell = row.createCell((short) 10); cell.setCellStyle(sDetC); cell.setCellValue(strDiasRet);
                    linea++;
                    row = sheet.createRow(linea);
                    strDiasAt="";
                    strDiasRet="";
                }
            }
            wb.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.toString();
        }
    }
    
    public static String transBR(String texto){
      if (texto == null) return "";
        String[] parrafos=texto.split("<br>");
        String resultado="";
        String saltoLinea="";
        for(int i=0;i<parrafos.length;i++){
            resultado=resultado+saltoLinea+parrafos[i];
            saltoLinea="\r\n";
        }
        return resultado;
    }
}
