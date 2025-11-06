/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.negocio;

import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.AccionDAO;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.AreaDAO;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.AsuntoDAO;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.AvanceDAO;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.ResponsableDAO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AreaDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.UsuarioDTO;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaDAO;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaUsuarioArea;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AreaBean;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AreasConsulta;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AsuntosResumenxArea;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.FiltroAsunto;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.ReporteArea;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.ReporteAreaSM;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.ReporteDelegado;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.ResumenArea;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.ResumenAreaSM;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.ResumenAsuntoArea;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.ResumenDelegado;

/**
 *
 * @author jacqueline
 */
public class AdministradorReportes {
    AreasConsulta areas = null;
            
    public AdministradorReportes() {
        
    }
    
    public AdministradorReportes(AreasConsulta areas) {
        this.areas = areas;
        this.areas.setAreasConsulta(areas.getAreasResponsables());
    }

        
            
    private FachadaDAO fachada = new FachadaDAO(null);
   
    
    public List<ResumenAsuntoArea> obtenReporteAsuntosxTipo(List<AreaDTO> subareas, String tipoAsuntos,FiltroAsunto filtro) throws Exception{ 
        
       List<ResumenAsuntoArea> reporte = new ArrayList<ResumenAsuntoArea>();
       if (filtro.getIdarea() != 0) {
          ResumenAsuntoArea resumen = fachada.obtenResumenAsuntosArea(filtro.getIdarea(), tipoAsuntos, filtro);
          FachadaUsuarioArea fua = new FachadaUsuarioArea();
          resumen.setSubarea(fua.buscaArea(filtro.getIdarea()).getDatos());
          reporte.add(resumen);
       } else
            for (AreaDTO area : subareas){
                ResumenAsuntoArea resumen = fachada.obtenResumenAsuntosArea(area.getIdarea(), tipoAsuntos, filtro);
                resumen.setSubarea(area);
                reporte.add(resumen);
            }
       
       return reporte;
   }
   
   public List<ResumenAsuntoArea> obtenReporteAcuerdos(List<AreaDTO> subareas, FiltroAsunto filtro) throws Exception{ 
       List<ResumenAsuntoArea> reporte = new ArrayList<ResumenAsuntoArea>();
       if (filtro.getIdarea() != 0) {
          ResumenAsuntoArea resumen = fachada.obtenResumenAcuerdosArea(filtro.getIdarea(), filtro);
          FachadaUsuarioArea fua = new FachadaUsuarioArea();
          resumen.setSubarea(fua.buscaArea(filtro.getIdarea()).getDatos());
          reporte.add(resumen);  
       } else
        for (AreaDTO area : subareas){
            ResumenAsuntoArea resumen = fachada.obtenResumenAcuerdosArea(area.getIdarea(), filtro);
            resumen.setSubarea(area);
            reporte.add(resumen);
        }
       
      
       return reporte;
   }
   
    public List<ReporteArea> obtenReporteArea(List<AreaDTO> subareas_, FiltroAsunto filtro) throws Exception{ 
       List<ReporteArea> reporteFinal = new ArrayList<ReporteArea>();
       String[][] tasuntos = {{"K","SIA"},{"M","COMISIONES"},{"C","CORREOS"}, {"A","ACUERDOS"}};
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
            for (String[] t:tasuntos) {
                ResumenArea resumen = fachada.obtenResumenArea(a.getIdarea(), t[0], filtro);
                resumen.setTipoasunto(t[1]);        
                resumen.setTipoAbreviado(t[0]);
                reporte.add(resumen);        
            }
            ResumenArea ra = new ResumenArea();
            ra.setTipoasunto("REUNIONES PENDIENTES DE REGISTRAR ACUERDOS");
            ra.setTipoAbreviado("R");
            ra.setVencidos_d(fachada.cantidadReunionesSinAcuerdosDirectos(a.getIdarea()));
            ra.setVencidos(fachada.cantidadReunionesSinAcuerdosCompartidos(filtro,a.getIdarea()));
            reporte.add(ra);
            reparea.setResumen(reporte);
            reporteFinal.add(reparea);
                    
        }
       return reporteFinal;       
    }
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
    public List<ReporteArea> obtenReportePorArea(int idarea, FiltroAsunto filtro) throws Exception{ 
        List<ReporteArea> reporteFinal = new ArrayList();
        String[][] tasuntos = {{"K","SIA"},{"M","COMISIONES"},{"C","CORREOS"}, {"A","ACUERDOS"}};
        ReporteArea reparea = new ReporteArea();
        List<ResumenArea> reporte = new ArrayList();
        for (String[] t:tasuntos) {
            ResumenArea resumen = fachada.obtenResumenArea(idarea, t[0], filtro);
            resumen.setTipoasunto(t[1]);        
            resumen.setTipoAbreviado(t[0]);
            reporte.add(resumen);
        }
        ResumenArea ra = new ResumenArea();
        ra.setTipoasunto("REUNIONES PENDIENTES DE REGISTRAR ACUERDOS");
        ra.setTipoAbreviado("R");
        ra.setVencidos_d(fachada.cantidadReunionesSinAcuerdosDirectos(idarea));
        ra.setVencidos(fachada.cantidadReunionesSinAcuerdosCompartidos(filtro,idarea));
        reporte.add(ra);
        reparea.setResumen(reporte);
        reporteFinal.add(reparea);
        return reporteFinal;
   }   
   
    public List<ReporteArea> obtenReporteAreaBean(List<AreaBean> subareas_, FiltroAsunto filtro) throws Exception{ 
       List<ReporteArea> reporteFinal = new ArrayList();
       String[][] tasuntos = {{"K","SIA"},{"M","COMISIONES"},{"C","CORREOS"}, {"A","ACUERDOS"}};
       List<AreaBean> subareas = new ArrayList();
       if (filtro.getIdarea() > 0) {
          subareas.add(new FachadaUsuarioArea().buscaArea(filtro.getIdarea()));
       } else {
          subareas.addAll(subareas_); 
       }
       
       for (AreaBean a:subareas){
            ReporteArea reparea = new ReporteArea();
            reparea.setArea(a.getDatos());
            List<ResumenArea> reporte = new ArrayList<ResumenArea>();
            for (String[] t:tasuntos) {
                ResumenArea resumen = fachada.obtenResumenArea(a.getDatos().getIdarea(), t[0], filtro);
                resumen.setTipoasunto(t[1]);        
                resumen.setTipoAbreviado(t[0]);
                reporte.add(resumen);        
            }
            ResumenArea ra = new ResumenArea();
            ra.setTipoasunto("REUNIONES PENDIENTES DE REGISTRAR ACUERDOS");
            ra.setTipoAbreviado("R");
            ra.setVencidos_d(fachada.cantidadReunionesSinAcuerdosDirectos(a.getDatos().getIdarea()));
            ra.setVencidos(fachada.cantidadReunionesSinAcuerdosCompartidos(filtro,a.getDatos().getIdarea()));
            reporte.add(ra);
            reparea.setResumen(reporte);
            reporteFinal.add(reparea);
                    
        }
       return reporteFinal;       
   }
   
    
     public List<ReporteArea> obtenReporteAreaDTO(List<AreaDTO> subareas_, FiltroAsunto filtro) throws Exception{ 
       List<ReporteArea> reporteFinal = new ArrayList<ReporteArea>();
       String[][] tasuntos = {{"K","SIA"},{"M","COMISIONES"},{"C","CORREOS"}, {"A","ACUERDOS"}};
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
            for (String[] t:tasuntos) {
                ResumenArea resumen = fachada.obtenResumenArea(a.getIdarea(), t[0], filtro);
                resumen.setTipoasunto(t[1]);        
                resumen.setTipoAbreviado(t[0]);
                reporte.add(resumen);        
            }
            ResumenArea ra = new ResumenArea();
            ra.setTipoasunto("REUNIONES PENDIENTES DE REGISTRAR ACUERDOS");
            ra.setTipoAbreviado("R");
            ra.setVencidos_d(fachada.cantidadReunionesSinAcuerdosDirectos(a.getIdarea()));
            ra.setVencidos(fachada.cantidadReunionesSinAcuerdosCompartidos(filtro,a.getIdarea()));
            reporte.add(ra);
            reparea.setResumen(reporte);
            reporteFinal.add(reparea);
                    
        }
       return reporteFinal;       
   }
   
     
    public List<ResumenDelegado> obtenReporteDelegados(List<AreaDTO> subareas, FiltroAsunto filtro, String tipo) throws Exception{ 
       List<ResumenDelegado> reporte = new ArrayList<ResumenDelegado>();
       for (AreaDTO a:subareas){
          ResumenDelegado resumen = fachada.obtenResumenDelegado(a.getIdarea(), tipo, filtro);
          resumen.setArea(a);
          reporte.add(resumen);        
        }
       return reporte;       
   }
   
       
   ////////////////////////////////////////////////////////////////////////////
   //  GENERA TOTALES PARA REPORTE DIARIO
   //////////////////////////////////////////////////////////////////////////
   public ReporteArea generaTotalesReporteDiario(FiltroAsunto filtro) throws NamingException, Exception{
       
       String[][] tasuntos = {{"K","SIA"},{"M","COMISIONES"},{"C","CORREOS"}, {"A","ACUERDOS"},{"R","REUNIONES PENDIENTES DE REGISTRAR ACUERDOS"}};  
       
       AreaDTO areaT = new AreaDTO();  
       areaT.setNombre("TOTALES");
       ReporteArea repAreaTotal = new ReporteArea();
       repAreaTotal.setArea(areaT);
       
       AsuntoDAO adao = new AsuntoDAO(areas);
       AccionDAO acdao = new AccionDAO(areas);
   
       filtro.setFechaInicio(Utiles.getSwapFecha(filtro.getFechaInicio()));
      
       filtro.setClasifica("T");
       filtro.setPresidencia("T");
       filtro.setEstatusResp("T");
       filtro.setEstatusReunion("TO");
       filtro.setUrgente("T");
       filtro.setTipoFecha("envio");
       
       
       List<ResumenArea> lResumenTotal = new ArrayList<ResumenArea>();
       for (String[] t:tasuntos) {
          ResumenArea resumenT = new ResumenArea();       
          if (t[0].equals("R")) {
            filtro.setEstatusReunion("RS");
            filtro.setEstatusAsunto("T");
            filtro.setEstatusFechaAtencion("TO");
            resumenT.setTipoasunto("REUNIONES PENDIENTES DE REGISTRAR ACUERDOS");
            resumenT.setTipoAbreviado(t[0]);
            resumenT.setVencidos_d(adao.cantidadAsuntosReunion(filtro).getCantidad());
          } else if (t[0].equals("A")){
            resumenT.setTipoasunto(t[1]);        
            resumenT.setTipoAbreviado(t[0]);
            filtro.setEstatusFechaAtencion("VE");
            resumenT.setVencidos_d(acdao.cantidadAccionesFiltro(filtro).getCantidad());  
            filtro.setEstatusFechaAtencion("AV");
            resumenT.setPorvencer_d(acdao.cantidadAccionesFiltro(filtro).getCantidad());
            filtro.setEstatusFechaAtencion("AC");
            resumenT.setPendactivos_d(acdao.cantidadAccionesFiltro(filtro).getCantidad());
          } else {
             filtro.setEstatusAsunto("P");  
            resumenT.setTipoasunto(t[1]);        
            resumenT.setTipoAbreviado(t[0]);
            filtro.setEstatusFechaAtencion("VE");
            resumenT.setVencidos_d(adao.cantidadAsuntosxAreaxTipo(filtro, t[0]).getCantidad());  
            filtro.setEstatusFechaAtencion("AV");
            resumenT.setPorvencer_d(adao.cantidadAsuntosxAreaxTipo(filtro, t[0]).getCantidad());
            filtro.setEstatusFechaAtencion("AC");
            resumenT.setPendactivos_d(adao.cantidadAsuntosxAreaxTipo(filtro, t[0]).getCantidad());
          }  
          lResumenTotal.add(resumenT);
          
       }     
       repAreaTotal.setResumen(lResumenTotal);
       
       return repAreaTotal;
   }
   
   
   
   //////////////////////////////////
    public List<ReporteAreaSM> obtenReporteAreaSM(List<AreaDTO> subareas_, FiltroAsunto filtro) throws Exception{ 
       List<ReporteAreaSM> reporteFinal = new ArrayList<ReporteAreaSM>();
       String[][] tasuntos = {{"K","SIA"},{"M","COMISIONES"},{"C","CORREOS"}, {"A","ACUERDOS"}};
       List<AreaDTO> subareas = new ArrayList<AreaDTO>();
       if (filtro.getIdarea() > 0) {
          subareas.add(new FachadaUsuarioArea().buscaArea(filtro.getIdarea()).getDatos());
       } else {
          subareas.addAll(subareas_); 
       }
       
       for (AreaDTO a:subareas){
            ReporteAreaSM reparea = new ReporteAreaSM();
            reparea.setArea(a);
            filtro.setIdarea(a.getIdarea());
            List<ResumenAreaSM> reporte = new ArrayList<ResumenAreaSM>();
            for (String[] t:tasuntos) {
                filtro.setIdarea(a.getIdarea());
                ResumenAreaSM resumen = fachada.obtenResumenAreaSM(t[0], filtro);
                resumen.setTipoasunto(t[1]);        
                resumen.setTipoAbreviado(t[0]);
                reporte.add(resumen);        
            }
            ResumenAreaSM ra = new ResumenAreaSM();
            ra.setTipoasunto("REUNIONES PENDIENTES DE REGISTRAR ACUERDOS");
            ra.setTipoAbreviado("R");
            int pend = fachada.cantidadReunionesSinAcuerdosDirectos(filtro);
            ra.setPendientes(pend);
            int enviados = fachada.cantidadReunionesEnviados(filtro);
            ra.setEnviados(enviados);
            ra.setAtendidos(enviados - pend);
            //ra.setPendientes(fachada.cantidadReunionesSinAcuerdosDirectos(filtro));
            reporte.add(ra);
            reparea.setResumen(reporte);
            reporteFinal.add(reparea);
                    
        }
       
       
       return reporteFinal;       
   }
    
    public List<ReporteAreaSM> obtenReporteAreaBeanSM(List<AreaBean> subareas_, FiltroAsunto filtro) throws Exception{ 
       List<ReporteAreaSM> reporteFinal = new ArrayList<ReporteAreaSM>();
       String[][] tasuntos = {{"K","SIA"},{"M","COMISIONES"},{"C","CORREOS"}, {"A","ACUERDOS"}};
       List<AreaBean> subareas = new ArrayList<AreaBean>();
       if (filtro.getIdarea() > 0) {
          subareas.add(new FachadaUsuarioArea().buscaArea(filtro.getIdarea()));
       } else {
          subareas.addAll(subareas_); 
       }
       
       for (AreaBean a:subareas){
            ReporteAreaSM reparea = new ReporteAreaSM();
            reparea.setArea(a.getDatos());
            filtro.setIdarea(a.getDatos().getIdarea());
            List<ResumenAreaSM> reporte = new ArrayList<ResumenAreaSM>();
            for (String[] t:tasuntos) {
                filtro.setIdarea(a.getDatos().getIdarea());
                ResumenAreaSM resumen = fachada.obtenResumenAreaSM(t[0], filtro);
                resumen.setTipoasunto(t[1]);        
                resumen.setTipoAbreviado(t[0]);
                reporte.add(resumen);        
            }
            ResumenAreaSM ra = new ResumenAreaSM();
            ra.setTipoasunto("REUNIONES PENDIENTES DE REGISTRAR ACUERDOS");
            ra.setTipoAbreviado("R");
            int pend = fachada.cantidadReunionesSinAcuerdosDirectos(filtro);
            ra.setPendientes(pend);
            int enviados = fachada.cantidadReunionesEnviados(filtro);
            ra.setEnviados(enviados);
            ra.setAtendidos(enviados - pend);
            //ra.setPendientes(fachada.cantidadReunionesSinAcuerdosDirectos(filtro));
            reporte.add(ra);
            reparea.setResumen(reporte);
            reporteFinal.add(reparea);
                    
        }
       
       
       return reporteFinal;       
   }
    
    
    
   ////////////////////////////////////////////////////////////////////////////
   //  GENERA TOTALES PARA REPORTE SM
   //////////////////////////////////////////////////////////////////////////
    public ReporteAreaSM generaTotalesReporteAreaSM(List<ReporteAreaSM> reportesAreasSM){
        
        String[][] tasuntos = {{"K","SIA"},{"M","COMISIONES"},{"C","CORREOS"}, {"A","ACUERDOS"},{"R","REUNIONES PENDIENTES DE REGISTRAR ACUERDOS"}};
        
        AreaDTO areaT = new AreaDTO();  
        areaT.setNombre("TOTALES");
        
        ReporteAreaSM reporteAreaSMTotal = new ReporteAreaSM();
        reporteAreaSMTotal.setArea(areaT);
                
        List<ResumenAreaSM> lResumenTotal = new ArrayList<ResumenAreaSM>();
        for (String[] t:tasuntos) {
          ResumenAreaSM resumenT = new ResumenAreaSM();
          resumenT.setTipoasunto(t[1]);        
          resumenT.setTipoAbreviado(t[0]);
          lResumenTotal.add(resumenT);
       }
        
       for (ReporteAreaSM reporteAreaSM:reportesAreasSM){
         for(ResumenAreaSM resumen : reporteAreaSM.getResumen()){
           ResumenAreaSM resumenT = null;
           if (resumen.getTipoAbreviado().equals("K"))
               resumenT = lResumenTotal.get(0);
           else if (resumen.getTipoAbreviado().equals("C"))
               resumenT = lResumenTotal.get(1);
           else if (resumen.getTipoAbreviado().equals("A"))
               resumenT = lResumenTotal.get(2);
           else if (resumen.getTipoAbreviado().equals("M"))
               resumenT = lResumenTotal.get(3);
           else if (resumen.getTipoAbreviado().equals("R"))
               resumenT = lResumenTotal.get(4);
               
           resumenT.setAtendidos(resumen.getAtendidos()+resumenT.getAtendidos());
           resumenT.setPendientes(resumen.getPendientes()+resumenT.getPendientes());
           resumenT.setEnviados(resumen.getEnviados()+resumenT.getEnviados());
           
           
         }
       }        
       
       reporteAreaSMTotal.setResumen(lResumenTotal);
       
       return reporteAreaSMTotal;
    }
    
    
    public ReporteAreaSM obtenReporteAreaSM(FiltroAsunto filtro) throws Exception{ 
       
        String[][] tasuntos = {{"K","SIA"},{"M","COMISIONES"},{"C","CORREOS"}, {"A","ACUERDOS"}};

        AreaDTO area = new FachadaUsuarioArea().buscaArea(filtro.getIdarea()).getDatos();
        
        ReporteAreaSM reparea = new ReporteAreaSM();
        reparea.setArea(area);
        filtro.setIdarea(area.getIdarea());
        List<ResumenAreaSM> reporte = new ArrayList<ResumenAreaSM>();
        for (String[] t:tasuntos) {
            filtro.setIdarea(area.getIdarea());
            ResumenAreaSM resumen = fachada.obtenResumenAreaSM(t[0], filtro);
            resumen.setTipoasunto(t[1]);        
            resumen.setTipoAbreviado(t[0]);
            reporte.add(resumen);        
        }
        ResumenAreaSM ra = new ResumenAreaSM();
        ra.setTipoasunto("REUNIONES PENDIENTES DE REGISTRAR ACUERDOS");
        ra.setTipoAbreviado("R");
        int pend = fachada.cantidadReunionesSinAcuerdosDirectos(filtro);
        ra.setPendientes(pend);
        int enviados = fachada.cantidadReunionesEnviados(filtro);
        ra.setEnviados(enviados);
        ra.setAtendidos(enviados - pend);
        reporte.add(ra);
        reparea.setResumen(reporte);

       return reparea;       
   }
     
   public List<ResumenAreaSM> resumenSemanalMens(String fecha1, String fechas2, List<AreaDTO> areas) throws Exception{
       String[][] tasuntos = {{"K","SIA"},{"M","COMISIONES"},{"C","CORREOS"}};
       ArrayList<ResumenAreaSM> rep = new ArrayList<ResumenAreaSM>();
       for(AreaDTO ar : areas){
          for(String[] t:tasuntos) {
              ResumenAreaSM uno = fachada.resumenSemanalMens(t[0], fecha1, fechas2, ar.getIdarea());
              if(uno != null) rep.add(uno);
              else {
                  AreaDAO sig = new AreaDAO();
                  AreaDTO siglas = sig.getArea(ar.getIdarea());
                  ResumenAreaSM solo = new ResumenAreaSM();
                  solo.setIdarea(ar.getIdarea());
                  solo.setSiglas(siglas.getSiglas().trim());
                  solo.setNombre(siglas.getNombre());
                  solo.setNivel(siglas.getNivel());
                  solo.setTipoasunto(t[1]);
                  solo.setTipoAbreviado(t[0]);
                  solo.setPendientes(0);
                  rep.add(solo);
              }
           }
          ResumenAreaSM ac = fachada.resumenSemanalMensAcuerdos(fecha1, fechas2, ar.getIdarea());
          if(ac != null ) rep.add(ac);
          else {
              AreaDAO sig = new AreaDAO();
              AreaDTO siglas = sig.getArea(ar.getIdarea());
              ResumenAreaSM solo = new ResumenAreaSM();
              solo.setIdarea(ar.getIdarea());
              solo.setSiglas(siglas.getSiglas().trim());
              solo.setNombre(siglas.getNombre());
              solo.setNivel(siglas.getNivel());
              solo.setTipoasunto("ACUERDOS");
              solo.setTipoAbreviado("A");
              solo.setPendientes(0);
              rep.add(solo);
          }
          ResumenAreaSM reu = fachada.resumenSemanalMensReuniones(fechas2, ar.getIdarea());
          if(reu != null) rep.add(reu);
          else {
              AreaDAO sig = new AreaDAO();
              AreaDTO siglas = sig.getArea(ar.getIdarea());
              ResumenAreaSM solo = new ResumenAreaSM();
              solo.setIdarea(ar.getIdarea());
              solo.setSiglas(siglas.getSiglas().trim());
              solo.setNombre(siglas.getNombre());
              solo.setNivel(siglas.getNivel());
              solo.setTipoasunto("REUNIONES");
              solo.setTipoAbreviado("R");
              solo.setPendientes(0);
              rep.add(solo);
          }
       }
       return rep;
       }
      public List<ResumenAreaSM> resumenSemanalMensTots(String fecha1, String fechas2) throws Exception{
       String[][] tasuntos = {{"K","SIA"},{"M","COMISIONES"},{"C","CORREOS"}};
       ArrayList<ResumenAreaSM> rep = new ArrayList<ResumenAreaSM>();
       for(String[] t:tasuntos) {
            ResumenAreaSM uno = fachada.resumenSemanalMensTots(t[0], fecha1, fechas2);
            if(uno != null) rep.add(uno);
            else {
                ResumenAreaSM solo = new ResumenAreaSM();
                solo.setTipoasunto(t[1]);
                solo.setTipoAbreviado(t[0]);
                solo.setPendientes(0);
                rep.add(solo);
            }
         }
        ResumenAreaSM ac = fachada.resumenSemanalMensAcuerdosTots(fecha1, fechas2);
        if(ac != null ) rep.add(ac);
        else {
            ResumenAreaSM solo = new ResumenAreaSM();
            solo.setTipoasunto("ACUERDOS");
            solo.setTipoAbreviado("A");
            solo.setPendientes(0);
            rep.add(solo);
        }
        ResumenAreaSM reu = fachada.resumenSemanalMensReunionesTots(fechas2);
        if(reu != null) rep.add(reu);
        else {
            ResumenAreaSM solo = new ResumenAreaSM();
            solo.setTipoasunto("REUNIONES");
            solo.setTipoAbreviado("R");
            solo.setPendientes(0);
            rep.add(solo);
        }
       return rep;
       }
   
   public void reporteDiarioRecursivo(List<AreaDTO> areasResponsables,FiltroAsunto filtro,List<UsuarioDTO> destinatariosfinales,  String fecha1, String fecha2) throws Exception{
        AdministraUsuariosAreas admonAreas = new AdministraUsuariosAreas();
        FachadaUsuarioArea fua = new FachadaUsuarioArea();
        AdministradorCorreo envioCorreo = new AdministradorCorreo();
        AdministradorReportes admonReporte = new AdministradorReportes();        
        
        //For por cada adjunta
        for (AreaDTO area:areasResponsables){
            List<AreaDTO> subareas = fua.listaSubAreasResponsables1NivelDTO(area.getIdarea());
            //Obtenemos a los destinatarios del �rea
            List<UsuarioDTO> destinatarios = admonAreas.obtenDestinosCorreoBeanSinA(area.getIdarea());

            List<AreaDTO> subareasInclusive = new ArrayList<AreaDTO>();
            subareasInclusive.add(admonAreas.buscaArea(area.getIdarea()).getDatos());
            subareasInclusive.addAll(subareas);
            List<ReporteArea> reporte = admonReporte.obtenReporteAreaDTO(subareasInclusive, filtro);
            boolean banderaEnvio = false;
            for(ReporteArea reparea:reporte){
               for(ResumenArea fila:reparea.getResumen()){
                  if (fila.getPendactivos_d() > 0) {banderaEnvio = true;break;}
                  if (fila.getVencidos_d() > 0) {banderaEnvio = true;break;}
                  if (fila.getPorvencer_d() > 0) {banderaEnvio = true;break;}
               }
               if (banderaEnvio) break;
            }

            if (banderaEnvio) {
               if (!destinatarios.isEmpty()) {
                    //Este es para saber de que �reas son
                    UsuarioDTO usuario = new UsuarioDTO();
                    usuario.setNombre(area.getSiglas());
                    destinatariosfinales.add(usuario);  

                    /*List<UsuarioDTO> destinatariosenvio = new ArrayList<UsuarioDTO>();
                    destinatariosenvio.add(fua.buscaUsuario(181).getDatos()); //Jacqueline/
                    destinatariosenvio.add(fua.buscaUsuario(235).getDatos()); //Osvaldo 
                    envioCorreo.enviaCorreoRepoteDiario(reporte, destinatariosenvio, fecha1, fecha2);*/

                    envioCorreo.enviaCorreoRepoteDiario(reporte, destinatarios, fecha1, fecha2);
                    destinatariosfinales.addAll(destinatarios);
               }
            } 

            if (subareas.size()> 0) {
                 reporteDiarioRecursivo(subareas,filtro,destinatariosfinales,fecha1,fecha2);
            } 
                
        }

   }
    public List<ResumenAsuntoArea> resumenGeneral(String fecha1, String fechas2) throws Exception{
        List<ResumenAsuntoArea> resumen = new ArrayList();
        AsuntoDAO reporteResumen = new AsuntoDAO(null);
        String[] tipo = {"K","C","M"};
        String tipoAs="" ;
        String fechaManiana = Utiles.getFechaManiana();
        ResumenAsuntoArea dato = new ResumenAsuntoArea();
        for (int i = 0; i < tipo.length; i++) {
            char t = tipo[i].charAt(0);
            switch(t)
            {
                case 'K' : tipoAs="SIA"; break;
                case 'C' : tipoAs="CORREOS"; break;
                case 'M' : tipoAs="COMISIONES"; break;
                //case 'R' : tipoAs="REUNIONES"; break;
                //case 'A' : tipoAs="ACUERDOS"; break;
            }
            dato = reporteResumen.resumenMensAll(tipoAs, tipo[i], fecha1+"0000", fechas2+"9999", fechaManiana);
            resumen.add(dato);
            
        }
        resumen.add(reporteResumen.resumenMensAcuerdosAll(fecha1+"0000", fechas2+"9999", fechaManiana));
        resumen.add(reporteResumen.resumenMensReunionAll(fecha1+"0000", fechas2+"9999"));
        return resumen;
    }
     
}
