/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.negocio;

import java.util.ArrayList;
import java.util.List;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AreaDTO;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaDAO;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaUsuarioArea;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.FiltroAsunto;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.ReporteArea;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.ResumenArea;

/**
 *
 * @author RICARDO.SERNA
 */
public class AdministradorReportes2 {
    
    private FachadaDAO fachada = new FachadaDAO(null);
    
    public List<ReporteArea> obtenReporteAreaTipo(List<AreaDTO> subareas_, FiltroAsunto filtro, String tipo) throws Exception{
       List<ReporteArea> reporteFinal = new ArrayList<ReporteArea>();
       //String[][] tasuntos = {{"K","SIA"},{"M","COMISIONES"},{"C","CORREOS"}, {"A","ACUERDOS"}};
       int idAreaSel = filtro.getIdAreaSel();
       if(idAreaSel > 1) { // cuando se selecciona solo 1 área
           AreaDTO arSel = new AreaDTO();
            ReporteArea reparea = new ReporteArea();
            reparea.setArea(arSel);
            List<ResumenArea> reporte = new ArrayList<ResumenArea>();
            //for (String[] t:tasuntos) {
                ResumenArea resumen = fachada.obtenResumenArea(idAreaSel, tipo, filtro);
                resumen.setTipoasunto(tipo);
                resumen.setTipoAbreviado(tipo);
                reporte.add(resumen);        
            //}
            ResumenArea ra = new ResumenArea();
            ra.setTipoasunto("REUNIONES PENDIENTES DE REGISTRAR ACUERDOS");
            ra.setTipoAbreviado("R");
            ra.setVencidos_d(fachada.cantidadReunionesSinAcuerdosDirectos(idAreaSel));
            ra.setVencidos(fachada.cantidadReunionesSinAcuerdosCompartidos(filtro,idAreaSel));
            reporte.add(ra);
            reparea.setResumen(reporte);
            reporteFinal.add(reparea);
       } else {
            List<AreaDTO> subareas = new ArrayList<AreaDTO>();
            if (filtro.getIdarea() > 0) {
               subareas.add(new FachadaUsuarioArea().buscaArea(filtro.getIdarea()).getDatos());
            } else {
               subareas.addAll(subareas_);
            }
            for (AreaDTO a:subareas){
                ReporteArea reparea = new ReporteArea();
                reparea.setArea(a);
                List<ResumenArea> reporte = new ArrayList<ResumenArea>();
                //for (String[] t:tasuntos) {
                    ResumenArea resumen = fachada.obtenResumenArea(a.getIdarea(), tipo, filtro);
                    resumen.setTipoasunto(tipo);
                    resumen.setTipoAbreviado(tipo);
                    reporte.add(resumen);        
                //}
                ResumenArea ra = new ResumenArea();
                ra.setTipoasunto("REUNIONES PENDIENTES DE REGISTRAR ACUERDOS");
                ra.setTipoAbreviado("R");
                ra.setVencidos_d(fachada.cantidadReunionesSinAcuerdosDirectos(a.getIdarea()));
                ra.setVencidos(fachada.cantidadReunionesSinAcuerdosCompartidos(filtro,a.getIdarea()));
                reporte.add(ra);
                reparea.setResumen(reporte);
                reporteFinal.add(reparea);
            }
       }
       return reporteFinal;
    }
}
