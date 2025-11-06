/*
 * DelegadoNegocio.java
 *
 * Created on 29 de octubre de 2006, 12:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.negocio;



import java.io.File;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.AreaDAO;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.BitacoraDAO;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.ProgramacionDAO;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.ResponsableDAO;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.UsuarioDAO;
import mx.org.inegi.dggma.sistemas.asuntos.datosglobales.DatosGlobales;
import mx.org.inegi.dggma.sistemas.asuntos.dto.*;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaDAO;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaUsuarioArea;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AsuntoBean;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.*;
import mx.org.inegi.dggma.sistemas.asuntos.vista.VistaListado;
import mx.org.inegi.dggma.sistemas.asuntos.vista.PermisosAreasUsuario;
import org.apache.commons.fileupload.FileItem;

/**
 *
 * @author Jos Luis Mondragn
 */
public class DelegadoNegocio {
    private FachadaDAO fachada = null;
    private String[][] tasuntos = {{"K","SIA"},
                             {"C","CORREO ELECTRONICO"},
                             {"A","ACUERDOS EN REUNIONES"},
                             {"M","INFORMES DE COMISIONES"},
                             {"R","REGISTRO DE ACUERDOS EN REUNIONES"}};

       /** Creates a new instance of DelegadoNegocio */
    public DelegadoNegocio(AreasConsulta areasC) {
        fachada = new FachadaDAO(areasC); 
    }

   public PermisosAreasUsuario asignaRolPermisos(UsuarioBean usuario, Integer idpermiso) throws Exception{
        PermisosAreasUsuario vistaUsuario = null;
       
        if (usuario.isPermisoValido(idpermiso)) {
            usuario.asignaPermisoActual(idpermiso);
            vistaUsuario = new PermisosAreasUsuario();
            asignaPermisosHerramientas(vistaUsuario, usuario);
           
            
        }
        return vistaUsuario;
    }
   
   public AreasConsulta obtenAreasConsultas(UsuarioBean usuario) throws Exception{
       AreasConsulta areas = new AreasConsulta();
       String rolAcceso = usuario.getPermisoActual().getDatos().getRol();
       FachadaUsuarioArea fua = new FachadaUsuarioArea();
       AdministraUsuariosAreas admonAreas = new AdministraUsuariosAreas();
       
       areas.setAreasResponsables(admonAreas.listaAreasResponsablesmas1NivelS(usuario));
            
       if (PermisoBean.getADMINISTRADOR().equals(rolAcceso) || PermisoBean.getCONSULTA().equals(rolAcceso) || PermisoBean.getEJECUTIVO().equals(rolAcceso) || PermisoBean.getCONVENIOS().equals(rolAcceso)) {
           areas.setAreasConsulta(areas.getAreasResponsables());
           areas.setAreasComparteResponsabilidad(areas.getAreasResponsables());
           
       } else if (PermisoBean.getRESPONSABLE_ADMINISTRADOR().equals(rolAcceso)) {
           List<AreaDTO> areasConsulta = new ArrayList<AreaDTO>(); 
           areasConsulta.add(usuario.getPermisoActual().getAreaBean().getDatos());
           areas.setAreasConsulta(areasConsulta);

           int nivelUsuario = usuario.getPermisoActual().getAreaBean().getDatos().getNivel();
           AreaBean areasuper = fua.buscaAreaSuperior(usuario.getPermisoActual().getAreaBean());
           if (areasuper !=  null) {
             areas.setAreasComparteResponsabilidad(fua.listaAreasResponsablesxNivelxAreaSuperS(nivelUsuario, areasuper.getDatos().getIdarea()));
           } else {
            areas.setAreasConsulta(areas.getAreasResponsables());
            areas.setAreasComparteResponsabilidad(areas.getAreasResponsables());
           }

       }
       
       return areas;
   }
   public AreasConsulta obtenAreasCaptura(UsuarioBean usuario) throws Exception{
       AreasConsulta areas = new AreasConsulta();
       AdministraUsuariosAreas admonAreas = new AdministraUsuariosAreas();
       areas.setAreasResponsables(admonAreas.listaAreasResponsablesmas1NivelSCaptura(usuario)); // es la que cambia
       return areas;
   }   
   
      
   public AreasConsulta obtenAreasCaptura(int idarea) throws Exception{
       AreasConsulta areas = new AreasConsulta();
       AdministraUsuariosAreas admonAreas = new AdministraUsuariosAreas();
       areas.setAreasResponsables(admonAreas.listaAreasResponsablesmas1NivelSCaptura(idarea)); // es la que cambia
       return areas;
   }   
   
   public List<AreaDTO> obtenAreasSASensibles(UsuarioBean usuario) throws Exception{
       List<AreaDTO> areasnew = new ArrayList<AreaDTO>();
       AreasConsulta areas = obtenAreasConsultas(usuario);
       for (AreaDTO a:areas.getAreasResponsables()){
          if (a.getIdarea() != 184 && a.getIdarea() != 179 && a.getIdarea() != 152) {
            areasnew.add(a);
          }
       }
       return areasnew;
   }
   
    
   private void asignaPermisosHerramientas(PermisosAreasUsuario vistaUsuario, UsuarioBean usuario){
        vistaUsuario.setCapturaAsuntos(PermisoBean.getADMINISTRADOR().equals(usuario.getPermisoActual().getDatos().getRol()));
        vistaUsuario.setCapturarAcuerdos(PermisoBean.getRESPONSABLE_ADMINISTRADOR().equals(usuario.getPermisoActual().getDatos().getRol()) || PermisoBean.getADMINISTRADOR().equals(usuario.getPermisoActual().getDatos().getRol()));
        vistaUsuario.setDelegaAsuntos(PermisoBean.getRESPONSABLE_ADMINISTRADOR().equals(usuario.getPermisoActual().getDatos().getRol()) && (usuario.getPermisoActual().getAreaBean().getDatos().getNivel() < 5));
        vistaUsuario.setConsultaAcuerdosReuniones(PermisoBean.getRESPONSABLE_ADMINISTRADOR().equals(usuario.getPermisoActual().getDatos().getRol()));
        vistaUsuario.setConsultarTodo(PermisoBean.getEJECUTIVO().equals(usuario.getPermisoActual().getDatos().getRol()) || PermisoBean.getADMINISTRADOR().equals(usuario.getPermisoActual().getDatos().getRol()) );
        vistaUsuario.setConsultaSoloReuniones(PermisoBean.getCONSULTA().equals(usuario.getPermisoActual().getDatos().getRol()));
        
        //vistaUsuario.setGenerarReportes(PermisoBean.getRESPONSABLE_ADMINISTRADOR().equals(usuario.getPermisoActual().getDatos().getRol()));
    }
    
    public boolean asignaRol(UsuarioBean usuario, Integer idpermiso, VistaListado vistaListado) throws Exception {
        Validador validador = new Validador();
        return validador.asignaRol(usuario, idpermiso, vistaListado);
    }

    public List<RecordatorioDTO> obtenRecordatorios(UsuarioBean usuario) throws Exception {
        return GeneradorRecordatorios.obtenRecordatorios(usuario);
    }

    public ResumenAsuntosClase obtenResumenAsuntos(UsuarioBean usuario,FechaReferencia fechaPivote) throws Exception {
        return new ResumenAsuntosClase(usuario, fechaPivote);
    }

   
    public String getIdAccionCompleto(int idAccion){
        String idAccionStr = String.valueOf(idAccion);
        if(idAccionStr.length() == 1){
            idAccionStr = "00"+ idAccionStr;
        } else if (idAccionStr.length() == 2) {
            idAccionStr = "0" + idAccionStr;
        }
        return idAccionStr;
    }

    public void actualizaResumenAsuntos(AsuntoBean asunto, ResumenAsuntosClase resumenAsuntos, String estatusLast) throws SQLException, Exception{
        FachadaUsuarioArea fua = new FachadaUsuarioArea();
        List<AreaBean> areasAModificar = fua.buscaAreasSuperiores(asunto.getResponsable());
        for(AreaResponsableResumen ab:resumenAsuntos.getResponsables()){
           for (AreaBean area:areasAModificar){
              if ( ab.getAreaBean().getDatos().getIdarea() == area.getDatos().getIdarea()) {
                    if (estatusLast.equals(EstatusBean.getEN_PROCESO()))
                        ab.setAsuntosEnProceso(ab.getAsuntosEnProceso()-1);
                    if (estatusLast.equals(EstatusBean.getCONCLUIDO()))
                        ab.setAsuntosAtendidos(ab.getAsuntosAtendidos()-1);
                    if (estatusLast.equals(EstatusBean.getCANCELADO()))
                        ab.setAsuntosCancelados(ab.getAsuntosCancelados()-1);
                    if (estatusLast.equals(EstatusBean.getDETENIDO()))
                        ab.setAsuntosSuspendidos(ab.getAsuntosSuspendidos()-1);

                    if (asunto.getEstatuscorto().equals(EstatusBean.getEN_PROCESO()))
                        ab.setAsuntosEnProceso(ab.getAsuntosEnProceso()+1);
                    if (asunto.getEstatuscorto().equals(EstatusBean.getCONCLUIDO()))
                        ab.setAsuntosAtendidos(ab.getAsuntosAtendidos()+1);
                    if (asunto.getEstatuscorto().equals(EstatusBean.getCANCELADO()))
                        ab.setAsuntosCancelados(ab.getAsuntosCancelados()+1);
                    if (asunto.getEstatuscorto().equals(EstatusBean.getDETENIDO()))
                        ab.setAsuntosSuspendidos(ab.getAsuntosSuspendidos()+1);

              }
           }
        }
    }


   /* public static List<AreaBean> buscarAreasCorresponsablesPorAsunto(AsuntoDTO asunto) throws SQLException, Exception{
        FachadaDAO fdao = new FachadaDAO();
        return fdao.buscarAreasCorresponsablesPorAsunto(asunto);
    }*/
    
    public void grabaAsunto(AsuntoBean asunto,List<FileItem> archivos) throws Exception{
       /*AdministraUsuariosAreas admonUsuArea = new AdministraUsuariosAreas();
       AdministradorCorreo admonCorreo = new AdministradorCorreo();*/
        
       asunto.setIdconsecutivo(fachada.getNextMaxIdAsuntoxTipo(asunto.getTipoasunto()));
       fachada.grabaAsunto(asunto);
       
       if (asunto.getResponsables() != null){
         for(ResponsableBean responsable:asunto.getResponsables()){
            //responsable.getDatos().setAsignadopor(1); //indica que el asunto fue asignado desde la DGGMA
            responsable.getDatos().setIdasunto(asunto.getIdasunto());
            responsable.getDatos().setFechaasignado(Utiles.getFechaHora());
            fachada.grabaResponsableAsunto(responsable);
/*
            if((asunto.getUrgente().equals("S") )&& DatosGlobales.ESTADO_PRODUCCION) {
                List<UsuarioDTO> destinatarios = admonUsuArea.obtenDestinosCorreoBean(responsable.getArea().getIdarea());
                admonCorreo.enviaCorreoUrgenteAsuntoReasigna(asunto, destinatarios);
            }
            if (!asunto.getUrgente().equals("S") && (asunto.getPresidencia().equals("P") || asunto.getPresidencia().equals("S"))) {
                List<UsuarioDTO> destinatarios = admonUsuArea.obtenDestinosCorreoBean(responsable.getArea().getIdarea());
                //Modificaci�n solicitida por OSvaldo de no enviar correo de asuntos tipo correo cuando sean de presidencia
                if (!asunto.getTipoasunto().equals("C")) {
                  admonCorreo.enviaCorreoAsunto(asunto, destinatarios, "Asunto Presidencia");
                }
            }
*/
         }
       }
       
       if (asunto.getCorresponsables() != null){
        for(AreaBean corresponsable:asunto.getCorresponsables()){
            fachada.grabaCorresponsable(corresponsable, asunto);
        }
       }

       if (asunto.getAnexos() != null) {
           for(int i=0;i<asunto.getAnexos().size();i++) {
              AnexoAsuntoDTO anexo=asunto.getAnexos().get(i);
              anexo.setIdAsunto(asunto.getIdasunto());
              fachada.grabaAnexoAsunto(anexo, archivos.get(i));
           }   
       }    
       
       
    }
    
    
    public void grabaAcuerdo(AccionBean acuerdo,List<FileItem> archivos) throws Exception{
       acuerdo.setIdconsecutivo(fachada.getNextMaxIdAcuerdoxReunion(acuerdo.getIdAsunto()));
       acuerdo.setFechaoriginal(acuerdo.getAcuerdo_fecha());     
       fachada.grabaAccion(acuerdo);
       
       if (acuerdo.getResponsables() != null){
        for(ResponsableBean responsable:acuerdo.getResponsables()){
            responsable.getDatos().setIdaccion(acuerdo.getIdAccion());
            fachada.grabaResponsableAccion(responsable);
        }
       }
       
       if (acuerdo.getAnexos() != null) {
           for(int i=0;i<acuerdo.getAnexos().size();i++) {
              AnexoAccionDTO anexo=acuerdo.getAnexos().get(i);
              anexo.setIdaccion(acuerdo.getIdAccion());
              fachada.grabaAnexoAccion(anexo, archivos.get(i));
           }   
       }    
    }

   public ResponsableBean generaResponsable(int idarea) throws Exception{
       ResponsableBean rb = new ResponsableBean();
       AdministraUsuariosAreas adminua = new AdministraUsuariosAreas();
       AreaBean ab = adminua.buscaArea(idarea);
       
       rb.setArea(ab.getDatos());
       rb.setDatos(new ResponsableDTO());
       rb.getDatos().setEstatus("P");
       rb.setUsuario(ab.getResponsable().getDatos());
       return rb;
    }
    
   
   public List<String> obtenPorcentajeNivel(AreaDTO area, int ultimoPorcentajeR, ResponsableDTO responsable, boolean p100){
      int ultimoPorcentaje = (ultimoPorcentajeR / 5) *5;
      List<String> porcentajes=new ArrayList<String>();
      if (area.getNivel() == 2) {
         if (responsable.getDelegado() != null && responsable.getDelegado().equals("S")) {
            String[] valores = {"75","80","85","90","95","99"};
            for(String dato:valores){
                int val = Integer.parseInt(dato);
                if (val > ultimoPorcentaje){
                    porcentajes.add(dato);
                }   
            }
            if(p100) porcentajes.add("100");
         } else {
              for(int i=ultimoPorcentaje+5;i<99;i+=5){
                 porcentajes.add(String.valueOf(i));
              }
              porcentajes.add(p100 ? "100":"99");
         }
      }
      
      if (area.getNivel() == 3) {
       if (responsable.getDelegado() != null && responsable.getDelegado().equals("S")) {  
         
          String[] valores = {"55","60","65","70"};
          for(String dato:valores){
            int val = Integer.parseInt(dato);
            if (val > ultimoPorcentaje){
                porcentajes.add(dato);
            }   
          }
         
       } else {
             for(int i=ultimoPorcentaje+5;i<75;i+=5){
                 porcentajes.add(String.valueOf(i));
              }
         }
      } 
      
      if (area.getNivel() == 4) {
         if (responsable.getDelegado() != null && responsable.getDelegado().equals("S")) {
           
            String[] valores = {"35","40","45","50"};
            for(String dato:valores){
                int val = Integer.parseInt(dato);
                if (val > ultimoPorcentaje){
                    porcentajes.add(dato);
                }   
            }
            
         } else {
             for(int i=ultimoPorcentaje+5;i<55;i+=5){
                 porcentajes.add(String.valueOf(i));
              }
         }
          
      }

      if (area.getNivel() == 5) {
          String[] valores = {"5","10","15","20","25","30"};
          for(String dato:valores){
            int val = Integer.parseInt(dato);
            if (val > ultimoPorcentaje){
                porcentajes.add(dato);
            }   
          }
        
      }

      
      return porcentajes;
   }
   

   
   public int obtenNivelMaxPorcentaje(AreaDTO area,boolean max100){
      int nivelP = 0;
      if (area.getNivel() == 2) {
          nivelP = max100 ? 100:99;
      }
      if (area.getNivel() == 3) {
          nivelP = 70;
      }
      
      if (area.getNivel() == 4) {
          nivelP = 50;
      }

      if (area.getNivel() == 5) {
          nivelP = 30;
      }
      return nivelP;
   }

   
   public void grabaAvance(AvanceBean avance,List<FileItem> archivos, AreaDTO areadto, String tipo) throws Exception{
       
       avance.setIdavance(-1);
       fachada.grabaAvance(avance,tipo);
       
       if (avance.getAnexos() != null) {
           for(int i=0;i<avance.getAnexos().size();i++) {
              AnexoAvanceDTO anexo=avance.getAnexos().get(i);
              anexo.setIdavance(avance.getIdavance());
              fachada.grabaAnexoAvance(anexo, archivos.get(i));
           }   
       }    
       
       ResponsableBean responsable = null; 
       
       //ASUNTO
       if (tipo.equals("asunto")) {
         responsable = fachada.obtenResponsablexAsuntoNoCancelado(avance.getIdarea(),avance.getIdasunto());
         responsable.getDatos().setAvance(avance.getPorcentaje());
         AsuntoBean asunto = fachada.buscaAsuntoPorLlavePrimaria(responsable.getDatos().getIdasunto());
         boolean max100 = (asunto != null && asunto.getTipoasunto().equals("M"));   
         if (this.obtenNivelMaxPorcentaje(areadto,max100) == avance.getPorcentaje() && (areadto.getNivel() > 2 || (areadto.getNivel() == 2 && max100))) {
            responsable.getDatos().setEstatus("A");
            responsable.getDatos().setFechaatencion(Utiles.getFechaHora());
         } 
         //Modificado por jacky 23/07/2015 para que las comisiones se ponga atendido hasta que el avance sea 100%
         //Modificado jacky 17/01/2019 para que en comisiones no ponga atendido a nivel de subdirecci�n nivel 3
         if (this.obtenNivelMaxPorcentaje(areadto,max100) == avance.getPorcentaje() && !asunto.getTipoasunto().equals("K") && !asunto.getTipoasunto().equals("C") && areadto.getNivel()==2 ){
            List<ResponsableBean> responsables = fachada.buscarResponsablesPorAsunto(asunto);
            String atendido = "A";
            for (ResponsableBean r:responsables){
                if (r.getDatos().getEstatus().equals("P")) {
                    atendido = "P";
                    break;
                }
            }
            asunto.setEstatus(atendido);
            fachada.actualizaEstatusAsunto(asunto);
         }
         fachada.actualizaAvanceResponsable(responsable.getDatos());
         actualizaAvanceSuperiorAsunto(avance.getIdasunto(),avance.getIdarea());
         
       } else {
       //ACUERDOS    
         responsable = fachada.obtenResponsablexAcuerdoNoCancelado(avance.getIdarea(),avance.getIdaccion());       
         responsable.getDatos().setAvance(avance.getPorcentaje());
         boolean max100 = true; //pq son acuerdos
         if (this.obtenNivelMaxPorcentaje(areadto,max100) == avance.getPorcentaje()) {
            responsable.getDatos().setEstatus("A");
            responsable.getDatos().setFechaatencion(Utiles.getFechaHora());  
            fachada.actualizaAvanceResponsable(responsable.getDatos());
            
            //if (areadto.getNivel() > 2 || (areadto.getNivel() == 2 && max100)){
            if (areadto.getNivel() == 2 && max100){
                  AccionBean acuerdo = fachada.buscaAccionPorLlavePrimariaFull(avance.getIdaccion());
                  List<ResponsableBean> responsables = fachada.buscarResponsablesPorAcuerdo(acuerdo);
                  boolean atendido = true;
                  for (ResponsableBean r:responsables){
                      if (r.getDatos().getEstatus().equals("P")) {
                          atendido = false;
                          break;
                      }
                   }
                   acuerdo.setEstatus(atendido ? "A":"P");
                   fachada.actualizaEstatusAcuerdo(acuerdo);
            }
         } else {
            //Actualiza unicamente el avance 
            fachada.actualizaAvanceResponsable(responsable.getDatos());
         }
         actualizaAvanceSuperiorAcuerdo(avance.getIdaccion(),avance.getIdarea());
       }
       
    }
   
   public void grabaAvanceConvenio(AvanceBean avance,List<FileItem> archivos, String tipo) throws Exception{
       avance.setIdavance(-1);
       fachada.grabaAvance(avance,tipo);
       
       if (avance.getAnexos() != null) {
           for(int i=0;i<avance.getAnexos().size();i++) {
              AnexoAvanceDTO anexo=avance.getAnexos().get(i);
              anexo.setIdavance(avance.getIdavance());
              fachada.grabaAnexoAvance(anexo, archivos.get(i));
           }   
       }    
       
    }
   
   
   public void actualizaAvanceSuperiorAsunto(int idasunto, int idarea) throws SQLException, Exception{
       FachadaUsuarioArea fua = new FachadaUsuarioArea();
       AreaBean areaSuper = fua.buscaAreaSuperior(idarea);
       if (areaSuper.getDatos().getNivel() == 1) {
           return;      
       } else {
          List<ResponsableBean> responsables  = fachada.buscarResponsablesxAsuntoAsignado(areaSuper.getDatos().getIdarea(), idasunto);
          AreaBean areaR = fua.buscaArea(idarea);
          AsuntoBean asunto = fachada.buscaAsuntoPorLlavePrimaria(idasunto);
          int maxNivelP = obtenNivelMaxPorcentaje(areaR.getDatos(),!asunto.getTipoasunto().equals("C") && !asunto.getTipoasunto().equals("K"));
          int noresponsables = responsables.size();
          if (noresponsables == 0){
              noresponsables = 1;
          }
          float porcentajeCompartido = (float) maxNivelP /noresponsables;
          float sumPorcentaje = 0;
          for(ResponsableBean r:responsables){
             sumPorcentaje += r.getDatos().getAvance()  * (porcentajeCompartido/ maxNivelP) ;
          } 
          //ResponsableBean responsableSuperior = fachada.buscarResponsablePorAsunto(areaSuper.getDatos().getIdarea(),idasunto);
          ResponsableBean responsableSuperior = fachada.buscarResponsableNCanceladoPorAsunto(areaSuper.getDatos().getIdarea(),idasunto);
          responsableSuperior.getDatos().setAvance((int)sumPorcentaje);
          fachada.actualizaAvanceResponsable(responsableSuperior.getDatos());
          
          actualizaAvanceSuperiorAsunto(idasunto, areaSuper.getDatos().getIdarea());           
       }
   }
   
   
   public void actualizaAvanceSuperiorAcuerdo(int idacuerdo, int idarea) throws SQLException, Exception{
       
       FachadaUsuarioArea fua = new FachadaUsuarioArea();
       AreaBean areaSuper = fua.buscaAreaSuperior(idarea);
       if (areaSuper.getDatos().getNivel() == 1) {
           return;      
       } else {
          List<ResponsableBean> responsables  = fachada.buscarResponsablesPorAcuerdoAsignado(areaSuper.getDatos().getIdarea(), idacuerdo);
          AreaBean areaR = fua.buscaArea(idarea);
          int maxNivelP = obtenNivelMaxPorcentaje(areaR.getDatos(),true); //Pq son acuerdos llega a 100
          int noresponsables = responsables.size();
          if (noresponsables == 0){
              noresponsables = 1;
          }
          float porcentajeCompartido = maxNivelP /noresponsables;
          float sumPorcentaje = 0;
          for(ResponsableBean r:responsables){
             sumPorcentaje += r.getDatos().getAvance()  * (porcentajeCompartido/ maxNivelP) ;
          } 
          //ResponsableBean responsableSuperior = fachada.buscarResponsablePorAcuerdo(areaSuper.getDatos().getIdarea(),idacuerdo);
          ResponsableBean responsableSuperior = fachada.obtenResponsablexAcuerdoNoCancelado(areaSuper.getDatos().getIdarea(),idacuerdo);
          
          responsableSuperior.getDatos().setAvance((int)sumPorcentaje);
          fachada.actualizaAvanceResponsable(responsableSuperior.getDatos());
          
          actualizaAvanceSuperiorAcuerdo(idacuerdo, areaSuper.getDatos().getIdarea());           
       }
   }
   
    
    
   public List<AvancesResponsable> avancesResponsableRecursivo(List<ResponsableBean> responsables) throws SQLException, Exception{
     List<AvancesResponsable> avancesResponsable = new ArrayList<AvancesResponsable>();
   
     for (ResponsableBean r:responsables) {
        int idasunto = r.getDatos().getIdasunto();
        int idarea = r.getDatos().getIdarea();

        AvancesResponsable avResp = new AvancesResponsable();
        avResp.setAvances(fachada.obtenAvancesResponsableAsunto(idasunto,idarea ));
        avResp.setResponsable(r);
        
        if (r.getDatos().getDelegado().equals("S")){
            //avResp.setSubareas(fua.listaSubAreasResponsables1Nivel(idarea));
            //List<ResponsableBean> responsablesDelagados  = fachada.buscarResponsablesPorAsuntoxUsuario(fachada.buscaAsuntoPorLlavePrimaria(idasunto), );         
            //avResp.setSubavances(avancesResponsableRecursivo(responsablesDelagados));    
        }     
        avancesResponsable.add(avResp);
     }
     
     return avancesResponsable;
   }

   
   public void cancelaResponsableAsunto(int idasunto, int idarea, int idRTabla) throws Exception{
        eliminaAvancesRecursivo(idasunto, idarea, idRTabla);
        actualizaAvanceSuperiorAsunto(idasunto, idarea);
        
        //Verifica si ya no existe ning�n responsable asigna estatus NO Delegado
        //sin embargo se comento para que independiente si se encuentra cancelados se�ale que se deleg�
        /*
        //Obten Superior
        ResponsableBean responsable = fachada.buscarResponsablePorAsunto(idarea, idasunto);
        ResponsableBean RSuperior = fachada.buscarResponsablePorAsunto(responsable.getDatos().getAsignadopor(), idasunto);
        List<ResponsableBean> responsables = fachada.buscarResponsablesxAsuntoAsignado(RSuperior.getDatos().getIdarea(), idasunto);
        if (responsables.isEmpty()){
            RSuperior.getDatos().setDelegado("N");
            fachada.actualizaResponsable(RSuperior.getDatos());
        }*/
   }
   
   private void eliminaAvancesRecursivo(int idasunto, int idarea, int idRTabla) throws NamingException, Exception{
        List<AvanceBean> avancesR = fachada.obtenAvancesResponsableAsunto(idasunto, idarea);
        fachada.mueveAvancesCancelados(avancesR, idRTabla);
        fachada.eliminaAvancesxResponsableAsunto(idarea, idasunto);
        fachada.actualizaEstatusResponsable("C", idRTabla);
        
        //Se elimina tmb los avances de las subareas
        List<ResponsableBean> subresponsables  = fachada.buscarResponsablesxAsuntoAsignado(idarea, idasunto);
        if (!subresponsables.isEmpty()){
            for (ResponsableBean r:subresponsables){
               eliminaAvancesRecursivo(idasunto, r.getArea().getIdarea(),r.getDatos().getIdresponsable());
            }   
        }
   }
   
   public void cancelaResponsableAcuerdo(int idacuerdo, int idarea, int idRTabla) throws Exception{
        eliminaAvancesAcuerdoRecursivo(idacuerdo,idarea, idRTabla);
        actualizaAvanceSuperiorAcuerdo(idacuerdo, idarea);
        
        //Verifica si ya no existe ning�n responsable asigna estatus NO Delegado
        /*List<ResponsableBean> responsables = fachada.buscarResponsablesPorAcuerdoAsignado(idarea,idacuerdo);
        if (!responsables.isEmpty()){
            ResponsableBean responsable = fachada.buscarResponsablePorAcuerdo(idarea, idacuerdo);
            responsable.getDatos().setDelegado("N");
            fachada.actualizaResponsable(responsable.getDatos());
        }*/
   }
   
   private void eliminaAvancesAcuerdoRecursivo(int idacuerdo, int idarea, int idRTabla) throws NamingException, Exception{
        List<AvanceBean> avancesR = fachada.obtenAvancesResponsableAcuerdo(idacuerdo, idarea);
        fachada.mueveAvancesCancelados(avancesR, idRTabla);
        fachada.eliminaAvancesxResponsableAcuerdo(idarea, idacuerdo);
        fachada.actualizaEstatusResponsable("C", idRTabla);
        
        //Se elimina tmb los avances de las subareas
        List<ResponsableBean> subresponsables  = fachada.buscarResponsablesPorAcuerdoAsignado(idarea, idacuerdo);
        if (!subresponsables.isEmpty()){
            for (ResponsableBean r:subresponsables){
                //Se corrigi�  no eliminaba los avances de los subniveles  11/08/2016
               eliminaAvancesAcuerdoRecursivo(idacuerdo, r.getArea().getIdarea(),r.getDatos().getIdresponsable());
            }   
        }
   }
   
   public AvancesResponsable avancesxResponsable(int idasunto, int idarea) throws SQLException, Exception{
        ResponsableBean responsable = fachada.obtenResponsablexAsuntoNoCancelado(idarea, idasunto);
        AvancesResponsable avResp = new AvancesResponsable();
        avResp.setAvances(fachada.obtenAvancesResponsableAsunto(idasunto,idarea ));
        avResp.setResponsable(responsable);
        
        if (responsable.getDatos().getDelegado() != null && responsable.getDatos().getDelegado().equals("S")){
           
            List<ResponsableBean> responsablesDelagados  = fachada.buscarResponsablesxAsuntoAsignado(idarea, idasunto);
            avResp.setSubResponsablesAsignados(responsablesDelagados);
        }     
     
     return avResp;
   }
    
    public AvancesResponsable avancesxResponsableAcuerdo(int idacuerdo, int idarea) throws SQLException, Exception{
        ResponsableBean responsable = fachada.obtenResponsablexAcuerdoNoCancelado(idarea,idacuerdo);
        AvancesResponsable avResp = new AvancesResponsable();
        avResp.setAvances(fachada.obtenAvancesResponsableAcuerdo(idacuerdo,idarea ));
        avResp.setResponsable(responsable);
        
        if (responsable.getDatos() != null &&  responsable.getDatos().getDelegado() != null && responsable.getDatos().getDelegado().equals("S")){
            List<ResponsableBean> responsablesDelagados  = fachada.buscarResponsablesPorAcuerdoAsignado(idarea, idacuerdo);
            avResp.setSubResponsablesAsignados(responsablesDelagados);
        }     
     
     return avResp;
   } 
    
    
   public boolean porcentajeAlacanzoxSubAreas(int idRSuperior,int idasunto) throws SQLException, Exception{
       boolean sw = true;
             
       List<ResponsableBean> responsablesDelagados = fachada.buscarResponsablesxAsuntoAsignado(idRSuperior, idasunto);
       for(ResponsableBean r:responsablesDelagados){
           if (r.getDatos().getAvance() < obtenNivelMaxPorcentaje(r.getArea(),true)) {  //en las subareas esta definido el max
                sw = false;
               break;
           }
       }
       return sw;
   } 
   
    public boolean porcentajeAlacanzoxSubAreasAcuerdo(int idRSuperior,int idacuerdo) throws SQLException, Exception{
       boolean sw = true;
             
       List<ResponsableBean> responsablesDelagados = fachada.buscarResponsablesPorAcuerdoAsignado(idRSuperior, idacuerdo);
       for(ResponsableBean r:responsablesDelagados){
           if (r.getDatos().getAvance() < obtenNivelMaxPorcentaje(r.getArea(),true)) {
               sw = false;
               break;
           }
       }
       return sw;
   } 
   
   public boolean porcentajeAlcanzado(AvancesResponsable avancesResp,boolean max100){
      boolean bandera = false;
      if (avancesResp.getAvances() != null && avancesResp.getAvances().size()>0) {
          //obtener el ultimo avance como el orden es descendente obtenemos el primero
          AvanceBean avance = avancesResp.getAvances().get(0);
          bandera = avance.getPorcentaje() == obtenNivelMaxPorcentaje(avancesResp.getResponsable().getArea(),max100) ? true:false;
       }
      return bandera;
   }
   
   
 
    ///////////////////////////////
    
   public List<ElementoBusqueda> obtenerDatosBusqueda(FiltroAsunto filtro) throws Exception{
       
        List<ElementoBusqueda> datos = new ArrayList<ElementoBusqueda>();

        ElementoBusqueda elem = new ElementoBusqueda();
        elem.setCantidad(fachada.cantidadAsuntosPorAreaxTipo(filtro, "K"));
        elem.setTipoAsunto("K");
        elem.setDescripcion("SIA");
        datos.add(elem);
       
        elem = new ElementoBusqueda();
        elem.setCantidad(fachada.cantidadAsuntosPorAreaxTipo(filtro, "C"));
        elem.setTipoAsunto("C");
        elem.setDescripcion("CORREOS");
        datos.add(elem);
        
        elem = new ElementoBusqueda();
        elem.setCantidad(fachada.cantidadAsuntosPorAreaxTipo(filtro,  "M"));
        elem.setTipoAsunto("M");
        elem.setDescripcion("COMISIONES");
        datos.add(elem);

        elem = new ElementoBusqueda();
        elem.setCantidad(fachada.cantidadAsuntosReuniones(filtro));
        elem.setTipoAsunto("R");
        elem.setDescripcion("REUNIONES");
        datos.add(elem);
      
        elem = new ElementoBusqueda();
        elem.setCantidad(fachada.cantidadAcuerdosFiltro(filtro));
        elem.setTipoAsunto("A");
        elem.setDescripcion("ACUERDOS");
        datos.add(elem);
        
        return datos;   
   }
   
   
public void modificaAsunto(AsuntoBean asunto,List<FileItem> archivos,List<AnexoAsuntoDTO> nuevosAnexos) throws Exception{
      
       FachadaUsuarioArea fua = new FachadaUsuarioArea();
       AsuntoBean asuntoOriginal = fachada.buscaAsuntoPorLlavePrimaria(asunto.getIdasunto());

/*       if(asunto.getDescargaestatus().equals("s")) { // con este atributo se pregunta si se envia correo
            AdministradorCorreo admonCorreo = new AdministradorCorreo();
            AdministraUsuariosAreas admonUsuArea = new AdministraUsuariosAreas();            
            if (!asunto.getTipoasunto().equals("C")) {
                for(ResponsableBean responsable:asunto.getResponsables()){
                    List<UsuarioDTO> destinatarios = admonUsuArea.obtenDestinosCorreoBean(responsable.getArea().getIdarea());
                    admonCorreo.enviaCorreoAsunto(asunto, destinatarios, "Asunto Presidencia");
                }
            } else {
                for(ResponsableBean responsable:asunto.getResponsables()){
                    List<UsuarioDTO> destinatarios = admonUsuArea.obtenDestinosCorreoBean(responsable.getArea().getIdarea());
                    admonCorreo.enviaCorreoUrgenteAsuntoReasigna(asunto, destinatarios);
                }
            }
        }*/
       
       /*
        if (!asuntoOriginal.getUrgente().equals("S") && asunto.getUrgente().equals("S") ) {
              AdministradorCorreo admonCorreo = new AdministradorCorreo();
              AdministraUsuariosAreas admonUsuArea = new AdministraUsuariosAreas();
              for(ResponsableBean responsable:asunto.getResponsables()){
                List<UsuarioDTO> destinatarios = admonUsuArea.obtenDestinosCorreoBean(responsable.getArea().getIdarea());
                admonCorreo.enviaCorreoUrgenteAsuntoReasigna(asunto, destinatarios);
              }  
         }
         //Se quito el envio de correo en correos de presidencia por solicitud de Osvaldo Luevno el 14/06/2018
         if (!asunto.getTipoasunto().equals("C")) {
              //if (!asunto.getUrgente().equals("S") && asuntoOriginal.getPresidencia() != null && !asuntoOriginal.getPresidencia().equals("P") && !asuntoOriginal.getPresidencia().equals("S") && (asunto.getPresidencia().equals("P") || asunto.getPresidencia().equals("S"))) {
              if (!asunto.getUrgente().equals("S") && asunto.getPresidencia() != null && !asunto.getPresidencia().equals("P") && !asunto.getPresidencia().equals("S") && (asunto.getPresidencia().equals("P") || asunto.getPresidencia().equals("S"))) {
                  AdministradorCorreo admonCorreo = new AdministradorCorreo();
                   AdministraUsuariosAreas admonUsuArea = new AdministraUsuariosAreas();
                   for(ResponsableBean responsable:asunto.getResponsables()){
                     List<UsuarioDTO> destinatarios = admonUsuArea.obtenDestinosCorreoBean(responsable.getArea().getIdarea());
                     admonCorreo.enviaCorreoAsunto(asunto, destinatarios, "Asunto Presidencia");
                   }  
              }
          }
        */
       fachada.grabaAsunto(asunto);
       
       //////////////////////////////////////////////////////////////////////////////////////
       if (asunto.getResponsables() != null){
         //AreasDelegadas lo que esta actualemnte en BD  
         List<AreaBean> areasDelegadas = fua.listaAreasDelegadasxAsunto(0, asunto);
         //AgregaResponsable
         for(ResponsableBean responsable:asunto.getResponsables()){
             boolean existe=false;
             for(AreaBean area:areasDelegadas) {
                 if (responsable.getArea().getIdarea() == area.getDatos().getIdarea()){
                     existe = true;
                     break;
                 }
              }
             if (!existe){ 
                responsable.getDatos().setIdasunto(asunto.getIdasunto());
                responsable.getDatos().setFechaasignado(Utiles.getFechaHora());
                fachada.grabaResponsableAsunto(responsable);
                if(!asunto.getTipoasunto().equals("V")) fachada.cambiaEstatusAsunto(asunto.getIdasunto()); // cambia el estatus del asunto = P cuando se agregan mas responsables
             }
          }
         
         //EliminaResponsable
        for(AreaBean area:areasDelegadas) {
             boolean existe=false;
             for(ResponsableBean responsable:asunto.getResponsables()){
                 if (responsable.getArea().getIdarea() == area.getDatos().getIdarea()){
                     existe = true;
                     break;
                 }
              }
             if (!existe){
                 
                 List<ResponsableBean> responsablesAsunto = new ArrayList<ResponsableBean>();
                 responsablesAsunto.add(fachada.buscarResponsablePorAsunto(area.getDatos().getIdarea(), asunto.getIdasunto()));
                 fachada.obtenResponsablesAsuntoRecursivo(area.getDatos().getIdarea(), asunto.getIdasunto(), responsablesAsunto);
                 for (ResponsableBean r:responsablesAsunto){
                     fachada.eliminaRespxAsuntoResp(asunto.getIdasunto(), r.getDatos().getIdarea());
                 }
                 
                
             }
          }
        int idarea=0;
        int idasunto=0;
        int asignadoPor = 0;
        int idresponsable = 0;
        ResponsableBean resp = null;
        ArrayList<Integer> areas = new ArrayList<Integer>();
        
       }
        
           /*for (int i=0;i < asunto.getPosDelete().size();i++){  
              fachada.eliminaResponsableAsuntoAsignado(0, areasDelegadas.get(asunto.getPosDelete().get(i)).getDatos().getIdarea(), asunto.getIdasunto());
            }}*/  
//           int idasunto = asunto.getIdasunto();
//           int idarea = areasDelegadas.get(asunto.getPosDelete().get(0)).getDatos().getIdarea();

       
       ///////////////////////////////////////////////////////////////////////////////////////
       if (asunto.getCorresponsables() != null && !asunto.getCorresponsables().isEmpty()){
          actualizaCorresponsables(asunto.getCorresponsables(),asunto.getIdasunto());
       }
       
       /////////////////////////////////////////////////////////////////////////////////////////

        List<AnexoAsuntoDTO> anexosActuales = fachada.buscarAnexosPorAsunto(asunto);
        for (AnexoAsuntoDTO anexo_ant:anexosActuales){
            boolean existe=false;
            for (AnexoAsuntoDTO anexo_new:asunto.getAnexos()){
                if (anexo_ant.getIdanexo() == anexo_new.getIdanexo()){
                existe = true;
                break;
                }
            }
            if (!existe) {
                eliminaAnexo(anexo_ant.getIdanexo());
            }

        }
        
        if (nuevosAnexos != null) {
           for(int i=0;i<nuevosAnexos.size();i++) {
              AnexoAsuntoDTO anexo= nuevosAnexos.get(i);
              anexo.setIdAsunto(asunto.getIdasunto());
              fachada.grabaAnexoAsunto(anexo, archivos.get(i));
           }   
         }
  }    


 public void modificaAcuerdo(AccionBean acuerdo,List<FileItem> archivos,List<AnexoAccionDTO> nuevosAnexos) throws Exception{
       
       FachadaUsuarioArea fua = new FachadaUsuarioArea();
       fachada.grabaAccion(acuerdo);
       
       ResponsableDAO respDAO = new ResponsableDAO(null);
       
       if (acuerdo.getResponsables() != null){
            List<AreaBean> areasDelegadas = fua.listaAreasDelegadasxAcuerdo(0, acuerdo.getIdAccion());
            
            for(ResponsableBean responsable:acuerdo.getResponsables()){
                boolean existe=false;
                for(AreaBean area:areasDelegadas) {
                    if (responsable.getArea().getIdarea() == area.getDatos().getIdarea()){
                        existe = true;
                        break;
                    }
                }
                if (!existe){
                    responsable.getDatos().setIdaccion(acuerdo.getIdAccion());
                    responsable.getDatos().setFechaasignado(Utiles.getFechaHora());
                    fachada.grabaResponsableAccion(responsable);
                }
            }
            
         //EliminaResponsable
        for(AreaBean area:areasDelegadas) {
             boolean existe=false;
             for(ResponsableBean responsable:acuerdo.getResponsables()){
                 if (responsable.getArea().getIdarea() == area.getDatos().getIdarea()){
                     existe = true;
                     break;
                 }
              }
             if (!existe){
                 List<ResponsableBean> responsablesAsunto = new ArrayList<ResponsableBean>();
                 responsablesAsunto.add(fachada.buscarResponsablePorAcuerdo(area.getDatos().getIdarea(), acuerdo.getIdAccion()));
                 fachada.obtenResponsablesAcuerdoRecursivo(area.getDatos().getIdarea(), acuerdo.getIdAccion(), responsablesAsunto);
                 for (ResponsableBean r:responsablesAsunto){
                     respDAO.eliminaRespxAsuntoRespAcuerdo(acuerdo.getIdAccion(), r.getArea().getIdarea());
                 }
             }
          }
            
            //EliminaResponsable
            /*for(AreaBean area:areasDelegadas) {
             boolean existe=false;
             for(ResponsableBean responsable:acuerdo.getResponsables()){
                 if ((responsable.getArea().getIdarea() == area.getDatos().getIdarea() )){
                     existe = true;
                     break;
                 }
              }
             if (!existe){
                fachada.eliminaResponsbleAcuerdoAsignado(0, area.getDatos().getIdarea(), acuerdo.getIdAccion());
             }
            }*/
            
         
//            for (int i=0;i < acuerdo.getPosDelete().size();i++){  
//              fachada.eliminaResponsbleAcuerdoAsignado(0, areasDelegadas.get(acuerdo.getPosDelete().get(i)).getDatos().getIdarea(), acuerdo.getIdAccion());
//            }  
           
       } 
      
       if (acuerdo.getAnexos() != null) {
           List<AnexoAccionDTO> anexosActuales = fachada.buscarAnexosPorAcuerdo(acuerdo);
           
           for (AnexoAccionDTO anexo_ant:anexosActuales){
                boolean existe=false;
                for (AnexoAccionDTO anexo_new:acuerdo.getAnexos()){
                    if (anexo_ant.getIdanexo() == anexo_new.getIdanexo()){
                    existe = true;
                    break;
                    }
                }
                if (!existe) {
                    eliminaAnexo(anexo_ant.getIdanexo());
                }

            }

            if (nuevosAnexos != null) {
                for(int i=0;i<nuevosAnexos.size();i++) {
                    AnexoAccionDTO anexo= nuevosAnexos.get(i);
                    anexo.setIdaccion(acuerdo.getIdAccion());
                    fachada.grabaAnexoAccion(anexo, archivos.get(i));
                }   
            }
       }
   }    


   public void eliminaAnexo(int idAnexo) throws Exception{
     AnexoDTO anexo = fachada.buscaAnexoPorLlavePrimaria(idAnexo);
     fachada.eliminaAnexo(idAnexo);
     File f = new File(DatosGlobales.getRuta() + anexo.getNombreenservidor());
     if (f.exists())  f.delete();
   }
   
   public void eliminaAvanceAsunto(int idAvance,int id, int idResponsable) throws Exception {
//        List<AnexoAvanceDTO> anexos = fachada.buscarAnexosPorAvance(idAvance);
//        for(AnexoAvanceDTO anexo:anexos){
//          fachada.eliminaAnexo(anexo.getIdanexo());
//          File f = new File(DatosGlobales.getRuta() + anexo.getNombreenservidor());
//          if (f.exists())  f.delete();
//        }

        fachada.eliminaAvance(idAvance);
        //Falta guardar en bitacora     

       int ultimoPorcentaje = this.ultimoAvanceAsunto(id, idResponsable);
       
       ResponsableBean respB = fachada.obtenResponsablexAsuntoNoCancelado(idResponsable,id);
       ResponsableDTO resp = respB.getDatos();

       resp.setAvance(ultimoPorcentaje);
       resp.setEstatus("P");
       resp.setFechaatencion("");
       resp.setDiasretraso(0);
       resp.setDiasatencion(0);

       fachada.actualizaAvanceResponsable(resp);
       actualizaAvanceSuperiorAsunto(id,idResponsable);
   }
   
   public void eliminaAvanceAcuerdo(int idAvance,int id, int idResponsable) throws Exception {
//        List<AnexoAvanceDTO> anexos = fachada.buscarAnexosPorAvance(idAvance);
//        for(AnexoAvanceDTO anexo:anexos){
//          fachada.eliminaAnexo(anexo.getIdanexo());
//          File f = new File(DatosGlobales.getRuta() + anexo.getNombreenservidor());
//          if (f.exists())  f.delete();
//        }

        fachada.eliminaAvance(idAvance);
        //Falta guardar en bitacora     

       int ultimoPorcentaje = this.ultimoAvanceAcuerdo(id, idResponsable);
       
       ResponsableBean respB =fachada.obtenResponsablexAcuerdoNoCancelado(idResponsable,id );
       ResponsableDTO resp = respB.getDatos();


       resp.setAvance(ultimoPorcentaje);
       resp.setEstatus("P");
       resp.setFechaatencion("");
       resp.setDiasretraso(0);
       resp.setDiasatencion(0);

       fachada.actualizaAvanceResponsable(resp);
       actualizaAvanceSuperiorAcuerdo(id,idResponsable);
   }
   
   
   
   public void eliminaAsunto(int idasunto) throws Exception{
/*     List<AnexoDTO> anexo = fachada.buscaAnexosxAsunto(idasunto);
     String nombre="";
     File f = null;
     for(int i = 0;i<anexo.size();i++){
        nombre = anexo.get(i).getNombreenservidor();
        f = new File(DatosGlobales.getRuta() + nombre);
        f.delete();
     }
  */   
     fachada.eliminaAnexosxAsunto(idasunto);
     fachada.eliminaAvancesAsunto(idasunto);
     fachada.eliminaRespxAsunto(idasunto);
     fachada.eliminaCorespTodos(idasunto);
     //Aqui obtiene los acuerso si tiene en caso de reuni�n y los elimina tanto responsables, anexos, etc
     List<AccionBean> acuerdos = fachada.buscaAcuerdosxReunion(idasunto);
     if (acuerdos != null && !acuerdos.isEmpty()){
       for(AccionBean acuerdo:acuerdos){
          this.eliminaAcuerdo(acuerdo.getIdAccion());
       }  
     }
     //////////////
     
     fachada.eliminaAsunto(idasunto);
     
    
   }
   
   public void eliminaAcuerdo(int idacuerdo) throws Exception{
     List<AnexoDTO> anexos = fachada.buscaAnexosxAcuerdo(idacuerdo);
     String nombre="";
     File f = null;
     for(int i = 0;i<anexos.size();i++){
        nombre = anexos.get(i).getNombreenservidor();
        f = new File(DatosGlobales.getRuta() + nombre);
        f.delete();
     }
     
     fachada.eliminaAnexosxAcuerdo(idacuerdo);
     fachada.eliminaAvancesAcuerdo(idacuerdo);
     fachada.eliminaRespxAcuerdo(idacuerdo);
     fachada.eliminaAcuerdo(idacuerdo);
    
   }
   
   public void actualizaCorresponsables(List<AreaBean> corresponsables, Integer idAsuntoCapturado) throws Exception {
        //Actualizar Corresponsables
         AsuntoBean asuntoAnterior = fachada.buscaAsuntoPorLlavePrimariaFull(idAsuntoCapturado);
         //Agregar los no existentes en la lista anterior
         for(AreaBean ab_nueva : corresponsables){
             boolean encontrado = false;
             for(AreaBean ab_ant : asuntoAnterior.getCorresponsables()){
                 if (ab_nueva.getDatos().getIdarea() == ab_ant.getDatos().getIdarea()) {
                     encontrado = true;
                     break;
                 }
             }
             if(!encontrado){
               fachada.grabaCorresponsable(ab_nueva , asuntoAnterior);
             }
         }


         //Eliminar los no existentes en la nueva lista
         for(AreaBean ab_ant : asuntoAnterior.getCorresponsables()){
             boolean encontrado = false;
                 for(AreaBean ab_nueva : corresponsables){
                 if (ab_nueva.getDatos().getIdarea() == ab_ant.getDatos().getIdarea()){
                     encontrado= true;
                     break;
                 }
             }
             if(!encontrado){
                 //Eliminamos al corresponsable
                fachada.eliminaCorresponsable(ab_ant, asuntoAnterior);
             }
         }

   }
   
   
    
   public ResponsableBean asignaAtendidoAsunto(DatosAtencion da) throws SQLException, Exception{
       //
      ResponsableBean responsable = fachada.obtenResponsablexAsuntoNoCancelado(da.getIdresponsable(), da.getIdasunto());
      responsable.getDatos().setComentario(da.getComentario()); 
      responsable.getDatos().setEstatus("A");
      responsable.getDatos().setAvance(100);
      responsable.getDatos().setFechaatencion(Utiles.getSwapFecha(da.getFechaatencion()));
      //Esto es para que lo tome el ajax
      responsable.setFechaatencionFormatoTexto(responsable.getFechaatencionFormatoTexto());
      
      responsable.setUltimoAvance(responsable.getUltimoAvance());
      //
      String fechaRA = responsable.getDatos().getFechaatencion().substring(0, 8);
      AsuntoBean asunto = fachada.buscaAsuntoPorLlavePrimariaFull(da.getIdasunto());
      responsable.getDatos().setDiasatencion(Utiles.restaFechasEnDias(asunto.getFechaingreso(),fechaRA));
      
      //responsable.getDatos().setDiasretraso(Utiles.restaFechasEnDias(asunto.getFechaatender(),fechaRA));
      fachada.actualizaResponsable(responsable.getDatos());
      
      return responsable;
   }
   
   public ResponsableBean asignaAtendidoAcuerdo(DatosAtencion da) throws SQLException, Exception{
       //
      ResponsableBean responsable = fachada.obtenResponsablexAcuerdoNoCancelado(da.getIdresponsable(), da.getIdaccion());
      responsable.getDatos().setComentario(da.getComentario()); 
      responsable.getDatos().setEstatus("A");
      responsable.getDatos().setAvance(100);
      responsable.getDatos().setFechaatencion(Utiles.getSwapFecha(da.getFechaatencion()));
      responsable.setFechaatencionFormatoTexto(responsable.getFechaatencionFormatoTexto());
      responsable.setUltimoAvance(responsable.getUltimoAvance());
      
      String fechaRA = responsable.getDatos().getFechaatencion().substring(0, 8);
      AccionBean asuntoAC = fachada.buscaAccionPorLlavePrimaria(da.getIdaccion());
      
      responsable.getDatos().setDiasatencion(Utiles.restaFechasEnDias(asuntoAC.getFecha(),fechaRA));
      
      //responsable.getDatos().setDiasretraso(Utiles.restaFechasEnDias(asunto.getFechaatender(),fechaRA));
      fachada.actualizaResponsableAcuerdo(responsable.getDatos());
      
      return responsable;
   }
   public ResponsableBean quitaAtendidoAsunto(int idasunto, int idresponsable, int idusuario) throws SQLException, Exception{
      ResponsableBean responsable = fachada.obtenResponsablexAsuntoNoCancelado(idresponsable,idasunto);
      AsuntoBean asunto = fachada.buscaAsuntoPorLlavePrimaria(idasunto);

      BitacoraDTO bit = new BitacoraDTO();
      bit.setId(idasunto);
      bit.setObservaciones("Se regreso a pendiente un asunto "+ (asunto.getTipoasunto().equals("K") ? "SIA" : "CORREO" )+ " " +asunto.getIdconsecutivo()  +" del �rea "+idresponsable + " ten�a el comentario "+ responsable.getDatos().getComentario());
      bit.setFecha(Utiles.getFechaHora());
      bit.setIdusuariomodificacion(idusuario);
      bit.setAccion("Quita atendido");

      responsable.getDatos().setComentario(""); 
      responsable.getDatos().setEstatus("P");
      responsable.getDatos().setAvance(99);
      responsable.getDatos().setFechaatencion("");
      responsable.getDatos().setDiasatencion(0);
      
      
      bit.setTipo(asunto.getTipoasunto());
      
      responsable.getDatos().setDiasretraso(Utiles.restaFechasEnDias(asunto.getFechaatender(),Utiles.getFechaHora()));
      fachada.actualizaResponsable(responsable.getDatos());
      
      BitacoraDAO bDao = new BitacoraDAO();
      bDao.grabaBitacora(bit);
      
      return responsable;
   }

   //
   public int ultimoAvanceAsunto(int idasunto, int idresponsable) throws SQLException, Exception{
      int ultimoAv = 0; 
      AvanceBean avance = fachada.obtenUltimoAvanceAsunto(idasunto, idresponsable);
      if (avance == null){
          List<ResponsableBean> responsables  = fachada.buscarResponsablesxAsuntoAsignado(idresponsable, idasunto);
          float avances = 0;
          AsuntoBean asunto = fachada.buscaAsuntoPorLlavePrimaria(idasunto);
          for(ResponsableBean r:responsables){
             float maxNivelP = obtenNivelMaxPorcentaje(r.getArea(),asunto.getTipoasunto().equals("C")); 
             float porcentajeCompartido = maxNivelP /responsables.size();
             avances += ( ((float)r.getDatos().getAvance() / maxNivelP) * porcentajeCompartido );
          } 
          ultimoAv = (int) avances;
      } else {
        ultimoAv = avance.getPorcentaje();
      }
      return ultimoAv;
   }
   //

   public int ultimoAvanceAcuerdo(int idacuerdo, int idresponsable) throws SQLException, Exception{
      int ultimoAv = 0; 
      AvanceBean avance = fachada.obtenUltimoAvanceAcuerdo(idacuerdo, idresponsable);
      if (avance == null){
          List<ResponsableBean> responsables  = fachada.buscarResponsablesPorAcuerdoAsignado(idresponsable, idacuerdo);
          float avances = 0;
          for(ResponsableBean r:responsables){
             float maxNivelP = obtenNivelMaxPorcentaje(r.getArea(),true); 
             float porcentajeCompartido = maxNivelP /responsables.size();
             avances += ( ((float)r.getDatos().getAvance() / maxNivelP) * porcentajeCompartido );
          } 
          ultimoAv = (int) avances;
      } else {
        ultimoAv = avance.getPorcentaje();
      }
      return ultimoAv;
   }

   
    public List<ReunionSAcuerdo> IdreunionesSinAcuerdos() throws SQLException, Exception{
      List<AsuntoBean> asuntos = fachada.buscarReunionesSinAcuerdos(null);
      List<ReunionSAcuerdo> ids = new ArrayList<ReunionSAcuerdo>();
      
      for (AsuntoBean asunto:asuntos){
         ReunionSAcuerdo rsa = new ReunionSAcuerdo();
         rsa.setIdasunto(asunto.getIdasunto());
         rsa.setIdconsecutivo(asunto.getIdconsecutivo());
         ids.add(rsa);
      }
      return ids;
   }
   
   public List<ResponsableDTO> realizaReprogramacion(ProgramacionDTO prog, int idusuario) throws SQLException, Exception{
       List<ResponsableDTO> idresponsables = null;

       AsuntoBean asunto = fachada.buscaAsuntoPorLlavePrimaria(prog.getIdasunto());
       boolean fechaCorrecta = false;
          
       if (asunto.getFechaatender().compareTo(prog.getFechareprograma()) < 0){
            fechaCorrecta = true;
       }
           
       if (fechaCorrecta) {
            fachada.insertaReprogramacionAsunto(prog);
            idresponsables = new ArrayList<ResponsableDTO>();
            List<ResponsableBean> responsables = fachada.buscarResponsablesxAsuntoAsignado(0, prog.getIdasunto());
            for (ResponsableBean r:responsables){
               if (r.getDatos().getEstatus().equals("P")) {
                  r.getDatos().setDiasretraso(Utiles.restaFechasEnDias(prog.getFechareprograma(),Utiles.getFechaHora()));
                  idresponsables.add(r.getDatos());
               }
            }
            asunto.setReprograma("S");
            asunto.setFechaatender(prog.getFechareprograma());
            fachada.actualizaFechaReprograma(asunto);
            
            BitacoraDTO bit = new BitacoraDTO();
            bit.setId(asunto.getIdasunto());
            bit.setObservaciones("Alta reprogramaci�n asunto "+asunto.getIdconsecutivo()+" | Fecha: "+prog.getFechareprograma());
            bit.setFecha(Utiles.getFechaHora());
            bit.setIdusuariomodificacion(idusuario);
            bit.setAccion("Alta reprogramaci�n");
            bit.setTipo(asunto.getTipoasunto());
            //bit.setIdarea(idarea); //No lleva el �rea pq solo administradores pueden reprogramar 

            BitacoraDAO bDao = new BitacoraDAO();
            bDao.grabaBitacora(bit);
            
       }    
       
       return idresponsables;
   }
   
   public List<ResponsableDTO> realizaReprogramacionAcuerdo( ProgramacionDTO prog, int idusuario) throws SQLException, Exception{
       List<ResponsableDTO> idresponsables = null;

       AccionBean acuerdo = fachada.buscaAccionPorLlavePrimaria(prog.getIdacuerdo());
       
       boolean fechaCorrecta = false;
       
        if (acuerdo.getAcuerdo_fecha().compareTo(prog.getFechareprograma()) < 0){
            fechaCorrecta = true;
       }
       
       if (fechaCorrecta) {
            fachada.insertaReprogramacionAcuerdo(prog);
            idresponsables = new ArrayList<ResponsableDTO>();
            List<ResponsableBean> responsables = fachada.buscarResponsablesPorAcuerdoAsignado(0, prog.getIdacuerdo());
            for (ResponsableBean r:responsables){
               if (r.getDatos().getEstatus().equals("P")) {
                  r.getDatos().setDiasretraso(Utiles.restaFechasEnDias(prog.getFechareprograma(),Utiles.getFechaHora()));
                  idresponsables.add(r.getDatos());
               }
            }
            acuerdo.setReprograma("S");
            acuerdo.setAcuerdo_fecha(prog.getFechareprograma());
            fachada.actualizaFechaReprograma(acuerdo);
            
            BitacoraDTO bit = new BitacoraDTO();
            bit.setId(acuerdo.getIdAccion());
            bit.setObservaciones("Alta reprogramación acuerdo "+acuerdo.getIdconsecutivo()+" de la reunión "+prog.getReunion()+ " | Fecha: "+prog.getFechareprogramaFormato());
            bit.setFecha(Utiles.getFechaHora());
            bit.setIdusuariomodificacion(idusuario);
            bit.setAccion("Alta reprogramación");
            bit.setTipo("A");
            //bit.setIdarea(idarea); //No lleva el �rea pq solo administradores pueden reprogramar 

            BitacoraDAO bDao = new BitacoraDAO();
            bDao.grabaBitacora(bit);
           
       }    
       
       return idresponsables;
   }
   
   
   public List<ResponsableDTO> ajustaDiasRetrasoResponsableAcuerdo(ProgramacionDTO prog) throws SQLException, Exception{
      
       fachada.insertaReprogramacionAcuerdo(prog);
       
       List<ResponsableDTO> idresponsables = new ArrayList<ResponsableDTO>();
       List<ResponsableBean> responsables = fachada.buscarResponsablesPorAcuerdoAsignado(0,prog.getIdacuerdo());
       for (ResponsableBean r:responsables){
          if (r.getDatos().getEstatus().equals("P")) {
             r.getDatos().setDiasretraso( Utiles.restaFechasEnDias(prog.getFechareprograma(),Utiles.getFechaHora()));
             idresponsables.add(r.getDatos());
          }
       }
       
       return idresponsables;
   }
   
   
   
   public void cierraAsunto(AnexoAsuntoDTO anexo, FileItem archivo) throws Exception{
     
      
      
   }
    
   
    public List<DetalleAtn> obtenDetalleReporte(FiltroAsunto filtro, int[] porcAtencion) throws Exception {
         DecimalFormat formato=new DecimalFormat("###,###.0");
        List<DetalleAtn> detalle = new ArrayList<DetalleAtn>();
        int p=0;
        DetalleAtn detallefinal = new DetalleAtn(); 
        detallefinal.setAsunto("TOTAL DE LA EVALUACIÓN");
        detallefinal.setPonderacion("100%");
        /////////////////////////////////////////
        for (String[] tipo:tasuntos) {
           DetalleAtn detalleatencion = new DetalleAtn(); 
           detalleatencion.setAsunto(tipo[1]);
           detalleatencion.setPonderacion(String.valueOf( porcAtencion[p])+"%");
           if (tipo[0].equals("A")) {
                filtro.setEstatusAsunto("T");
                detalleatencion.setTurnados(fachada.cantidadAcuerdosFiltro(filtro));
                filtro.setEstatusAsunto("A");
                detalleatencion.setAtendidos(fachada.cantidadAcuerdosFiltro(filtro));
           }else if (tipo[0].equals("R")) {
                detalleatencion.setTurnados(fachada.cantidadReunionesSinAcuerdosDirectos(filtro.getIdarea()));
                detalleatencion.setAtendidos(fachada.cantidadReunionesConAcuerdosDirectos(filtro.getIdarea()));
           } else {    
                filtro.setEstatusAsunto("T");
                detalleatencion.setTurnados(fachada.cantidadAsuntosPorAreaxTipo(filtro, tipo[0]));
                filtro.setEstatusAsunto("A");
                detalleatencion.setAtendidos(fachada.cantidadAsuntosPorAreaxTipo(filtro, tipo[0]));
           } 
           
           
           if (detalleatencion.getTurnados() == 0) {
             detalleatencion.setPorcentajeatn(0);  
             detalleatencion.setPorcentajeFormat("0.00");  
             
           } else {
             detalleatencion.setPendientes(detalleatencion.getTurnados()-detalleatencion.getAtendidos());
             float pa= (float) (detalleatencion.getAtendidos() * porcAtencion[p]) / (float) detalleatencion.getTurnados();
             detalleatencion.setPorcentajeatn(pa);
             detalleatencion.setPorcentajeFormat(formato.format(pa));
           } 
             
             
           p++;
           detalle.add(detalleatencion);
           detallefinal.setTurnados(detallefinal.getTurnados()+detalleatencion.getTurnados());
           detallefinal.setAtendidos(detallefinal.getAtendidos()+detalleatencion.getAtendidos());
           detallefinal.setPendientes(detallefinal.getPendientes()+detalleatencion.getPendientes());
           detallefinal.setPorcentajeatn(detallefinal.getPorcentajeatn()+detalleatencion.getPorcentajeatn());
           detallefinal.setPorcentajeFormat(formato.format(detallefinal.getPorcentajeatn()));
           
           
        }
        detalle.add(detallefinal);
        return detalle;
   }

    
   public ReporteGral obtenReporteGeneralArea(int idarea,String anio, int[] porcAtencion) throws Exception{
      DecimalFormat formato=new DecimalFormat("###,##0.0");
      FachadaUsuarioArea fua = new FachadaUsuarioArea();
      
      AreaDTO area = null;
      if (idarea > 0 ) area =  fua.buscaArea(idarea).getDatos();
      
      FiltroAsunto filtro = new FiltroAsunto();
           
      String[][] diameses = {{"0101","0131"},{"0201","0228"},{"0301","0331"},{"0401","0430"},{"0501","0531"},{"0601","0630"},
                             {"0701","0731"},{"0801","0831"},{"0901","0930"},{"1001","1031"},{"1101","1130"},{"1201","1231"}};
      

         ReporteGral rg = new ReporteGral();
         rg.setArea(area);
         float sumaProm=0;
         List<DatoMes> datomes = new ArrayList<DatoMes>();
         filtro.setIdarea(idarea);
         filtro.setTipoFecha("envio");
         for (String[] dm:diameses){ 
            DatoMes dato = new DatoMes(); 
            dato.setDiamesIni(anio+dm[0]);
            dato.setDiamesFin(anio+dm[1]);
            float sumaMes = 0;
            int p=0;
            /////////////////////////////////////////
            for (String[] tipo:tasuntos) {
               DetalleAtn detalleatencion = new DetalleAtn(); 
               filtro.setFechaInicio(anio+dm[0]);            
               filtro.setFechaFinal(anio+dm[1]);
               detalleatencion.setAsunto(tipo[1]);
               if (tipo[0].equals("A")) {
                    filtro.setEstatusAsunto("T");
                    detalleatencion.setTurnados(fachada.cantidadAcuerdosFiltro(filtro));
                    filtro.setEstatusAsunto("A");
                    detalleatencion.setAtendidos(fachada.cantidadAcuerdosFiltro(filtro));
               }else if (tipo[0].equals("R")) {
                    detalleatencion.setTurnados(fachada.cantidadReunionesSinAcuerdosDirectos(filtro));
                    detalleatencion.setAtendidos(fachada.cantidadReunionesConAcuerdosDirectos(filtro));
               } else {    
                    filtro.setEstatusAsunto("T");
                    detalleatencion.setTurnados(fachada.cantidadAsuntosPorAreaxTipo(filtro, tipo[0]));
                    filtro.setEstatusAsunto("A");
                    detalleatencion.setAtendidos(fachada.cantidadAsuntosPorAreaxTipo(filtro, tipo[0]));
               } 
               
               detalleatencion.setPendientes(detalleatencion.getTurnados()-detalleatencion.getAtendidos());
               
               
                if (detalleatencion.getTurnados() == 0) {
                  detalleatencion.setPorcentajeatn(0);
                } else {
                    float   pa = (float) (detalleatencion.getAtendidos() * porcAtencion[p]) / (float) detalleatencion.getTurnados();
                    detalleatencion.setPorcentajeatn(pa);
                    pa=0;
                }   

                sumaMes+=detalleatencion.getPorcentajeatn();
                p++;    

            }
            dato.setPorcentajeatn(sumaMes);
            dato.setPorcentajeFormat(formato.format(dato.getPorcentajeatn()));
            
            datomes.add(dato);
            
            sumaProm+=sumaMes;
            ////////////////
         }   
         rg.setDatomes(datomes);
         float nomeses = 12;
         if (Utiles.getFechaHoyAnio().equals(anio)) {
           String mes = Utiles.getFechaDMA().substring(3,5);
           nomeses = Float.parseFloat(mes);
         } 
         rg.setPromedio(sumaProm/nomeses);
         rg.setPromedioFormat(formato.format(rg.getPromedio()));

      return  rg;
   }

   
   
   
  public ReporteGralAM obtenReporteGeneralAreaMes(int idarea,String anio, int[] porcAtencion) throws Exception{   
      
      FiltroAsunto filtro = new FiltroAsunto();
           
      String[][] diameses = {{"0101","0131"},{"0201","0228"},{"0301","0331"},{"0401","0430"},{"0501","0531"},{"0601","0630"},
                             {"0701","0731"},{"0801","0831"},{"0901","0930"},{"1001","1031"},{"1101","1130"},{"1201","1231"}};
      

        ReporteGralAM rgam = new ReporteGralAM();
 
        filtro.setIdarea(idarea);
        filtro.setTipoFecha("envio");
        int p=0;
        for (String[] tipo:tasuntos) {
            rgam.setAsunto(tipo[1]);
            rgam.setPonderacion(String.valueOf(porcAtencion[p]));
            List<DetalleGralArea> meses = new ArrayList<DetalleGralArea>();
            for (String[] dm:diameses){ 
                DetalleGralArea detallegral = new DetalleGralArea(); 
                detallegral.setDiamesIni(anio+dm[0]);
                detallegral.setDiamesFin(anio+dm[1]);
                
                if (tipo[0].equals("A")) {
                     filtro.setEstatusAsunto("A");
                     detallegral.setAtendidos(fachada.cantidadAcuerdosFiltro(filtro));
                     filtro.setEstatusAsunto("A");
                     detallegral.setPendientes(fachada.cantidadAcuerdosFiltro(filtro));
                     
                }else if (tipo[0].equals("R")) {
                     detallegral.setAtendidos(fachada.cantidadReunionesConAcuerdosDirectos(filtro));
                     detallegral.setPendientes(fachada.cantidadReunionesSinAcuerdosDirectos(filtro));
                } else {    
                     filtro.setEstatusAsunto("A");
                     detallegral.setAtendidos(fachada.cantidadAsuntosPorAreaxTipo(filtro, tipo[0]));
                     filtro.setEstatusAsunto("P");
                     detallegral.setAtendidos(fachada.cantidadAsuntosPorAreaxTipo(filtro, tipo[0]));
                } 
               detallegral.setTotal(detallegral.getAtendidos()+detallegral.getPendientes());

            }
           p++;

       }   

       return  rgam;
         
   }
   

   
   
    public void eliminaPramacionAsunto(int idreprograma, int idasunto, int idusuario) throws NamingException, SQLException, Exception{
        ProgramacionDAO pdao = new ProgramacionDAO();
        pdao.eliminaReprogramacion(idreprograma);
        
        FachadaDAO fachada = new FachadaDAO(null);
        List<ProgramacionDTO> programaciones = fachada.obtenReprogramacionesAsunto(idasunto);
        AsuntoBean asunto = fachada.buscaAsuntoPorLlavePrimaria(idasunto);
        if (programaciones.size() <= 0) {
           asunto.setReprograma("");
           fachada.grabaAsunto(asunto);
        }
        

        BitacoraDTO bit = new BitacoraDTO();
        bit.setFecha(Utiles.getFechaHora());
        bit.setIdusuariomodificacion(idusuario);
        bit.setAccion("Elimina reprogramación");
        bit.setObservaciones("Elimina reprogramación del asunto "+asunto.getIdconsecutivo());
        bit.setTipo(asunto.getTipoasunto());
        //bit.setIdarea(idarea); //No lleva el �rea pq solo administradores pueden eliminar reprogramaci�n
        
        BitacoraDAO bDao = new BitacoraDAO();
        bDao.grabaBitacora(bit);
   }
    public void eliminaProgramacionAsunto(int idreprograma, int idasunto, int idusuario) throws NamingException, SQLException, Exception{
        ProgramacionDAO pdao = new ProgramacionDAO();
        FachadaDAO fachada = new FachadaDAO(null);
        ProgramacionDTO repr = fachada.obtenReprogramacionxId(idreprograma);
        pdao.eliminaReprogramacion(idreprograma);
        
        List<ProgramacionDTO> programaciones = fachada.obtenReprogramacionesAsunto(idasunto);
        AsuntoBean asunto = fachada.buscaAsuntoPorLlavePrimaria(idasunto);
//        if (programaciones.size() <= 0) {
//           asunto.setReprograma("");
//           fachada.grabaAsunto(asunto);
//        }
        BitacoraDTO bit = new BitacoraDTO();
        bit.setId(idasunto);
        bit.setFecha(Utiles.getFechaHora());
        bit.setIdusuariomodificacion(idusuario);
        bit.setAccion("Elimina reprogramación");
        bit.setObservaciones("Elimina reprogramación del asunto "+asunto.getIdconsecutivo()+ " | Fecha: "+repr.getFecharealizadaFormato()+" | Area: "+repr.getJustificacion());
        bit.setTipo(asunto.getTipoasunto());
        
        BitacoraDAO bDao = new BitacoraDAO();
        bDao.grabaBitacora(bit);
   }    
    
   public void eliminaPramacionAcuerdo(int idreprograma, int idacuerdo, int idusuario) throws NamingException, SQLException, Exception{
        ProgramacionDAO pdao = new ProgramacionDAO();
        pdao.eliminaReprogramacion(idreprograma);
        List<ProgramacionDTO> programaciones = fachada.obtenReprogramacionesAcuerdo(idacuerdo);
        
        FachadaDAO fachada = new FachadaDAO(null);
        AccionBean acuerdo = fachada.buscaAccionPorLlavePrimaria(idacuerdo);
        if (programaciones.size() <= 0){
           acuerdo.setReprograma("");
           fachada.grabaAccion(acuerdo);
        }
        
        BitacoraDTO bit = new BitacoraDTO();
        bit.setFecha(Utiles.getFechaHora());
        bit.setIdusuariomodificacion(idusuario);
        bit.setAccion("Elimina reprogramación");
        //bit.setIdarea(idarea); //No lleva el �rea pq solo administradores pueden eliminar reprogramaci�n
        bit.setObservaciones("Elimina reprogramación del acuerdo "+acuerdo.getIdconsecutivo());
        bit.setTipo("A");

        BitacoraDAO bDao = new BitacoraDAO();
        bDao.grabaBitacora(bit);
   } 
   public void eliminaProgramacionAcuerdo(int idreprograma, int idacuerdo, int idusuario) throws NamingException, SQLException, Exception{
        ProgramacionDAO pdao = new ProgramacionDAO();
        FachadaDAO fachada = new FachadaDAO(null);
        ProgramacionDTO repr = fachada.obtenReprogramacionxId(idreprograma);
        pdao.eliminaReprogramacion(idreprograma);
        List<ProgramacionDTO> programaciones = fachada.obtenReprogramacionesAcuerdo(idacuerdo);
        
        AccionBean acuerdo = fachada.buscaAccionPorLlavePrimaria(idacuerdo);
//        if (programaciones.size() <= 0){
//           acuerdo.setReprograma("");
//           fachada.grabaAccion(acuerdo);
//        }
        
        BitacoraDTO bit = new BitacoraDTO();
        bit.setFecha(Utiles.getFechaHora());
        bit.setIdusuariomodificacion(idusuario);
        bit.setAccion("Elimina reprogramación");
        //bit.setIdarea(idarea); //No lleva el �rea pq solo administradores pueden eliminar reprogramaci�n
        bit.setObservaciones("Elimina reprogramación del cons. acuerdo "+acuerdo.getIdconsecutivo()+ " | id. acuerdo: "+repr.getIdacuerdo()+" | Fecha: "+repr.getFecharealizadaFormato()+" | Area: "+repr.getJustificacion());
        bit.setTipo("A");

        BitacoraDAO bDao = new BitacoraDAO();
        bDao.grabaBitacora(bit);
   } 
}
