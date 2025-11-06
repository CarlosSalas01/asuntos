/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.negocio;

import java.sql.SQLException;
import java.util.List;
import mx.org.inegi.dggma.sistemas.asuntos.datosglobales.DatosGlobales;
import mx.org.inegi.dggma.sistemas.asuntos.dto.UsuarioDTO;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaDAO;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaUsuarioArea;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.*;

/**
 *
 * @author jacqueline
 */
public class AdmonAsignacion {
   private FachadaDAO fachada = null;
   
   private DelegadoNegocio delegado = null;
   
   public AdmonAsignacion(AreasConsulta areasC){
     fachada = new FachadaDAO(areasC);
     delegado = new DelegadoNegocio(areasC);
   }
        
   public boolean asignaResponsableAsunto(DatosDelega dd) throws Exception{
         boolean yaexiste = false;
        
         FachadaUsuarioArea fua = new FachadaUsuarioArea();
         int idasunto = dd.getIdasuntoacuerdo();
         //List<AreaBean> areasDelegadas = fua.listaAreasDelegadasxAsunto(dd.getIdRSuperior(),dd.getIdasuntoacuerdo());
         List<AreaBean> areasDelegadas = fua.listaAreasDelegadasxAsuntoSCancelados(dd.getIdRSuperior(),dd.getIdasuntoacuerdo());
               
         for(AreaBean area:areasDelegadas) {
            if (area.getDatos().getIdarea() == dd.getIdresponsable()) {
                //Ya existe el �rea delegada
                yaexiste = true;
                break;
            }
         }
         ResponsableBean rb = null;
         if (!yaexiste) {
            rb = delegado.generaResponsable(dd.getIdresponsable());
            rb.getDatos().setAsignadopor(dd.getIdRSuperior());
            rb.getDatos().setIdasunto(dd.getIdasuntoacuerdo());
            rb.getDatos().setFechaasignado(Utiles.getFechaHora());
            rb.getDatos().setInstruccion(dd.getInstruccion());
            fachada.grabaResponsableAsunto(rb);

            /////Rich
//            AsuntoBean asunto = fachada.buscaAsuntoPorLlavePrimariaFull(idasunto);
//            if(asunto.getUrgente().equals("S") && DatosGlobales.ESTADO_PRODUCCION) {
//                AdministraUsuariosAreas usuarios = new AdministraUsuariosAreas();
//                //List<UsuarioDTO> destinatarios = usuarios.obtenDestinosCorreoBean(dd.getIdresponsable());
//                List<UsuarioDTO> destinatarios = usuarios.obtenDestinosCorreoBeanSinA(dd.getIdresponsable());
//                AdministradorCorreo adminCorreo = new AdministradorCorreo();
//                adminCorreo.enviaCorreoUrgenteAsuntoReasigna(asunto, destinatarios);
//            }
            ///////////
            
            if (areasDelegadas.isEmpty()) {
              dd.setValor("S");
              fachada.defineDelegadoAsuntoResponsable(dd.getIdasuntoacuerdo(), dd.getIdRSuperior() , "S"); //Asigna al asunto que se encuentra delegado           
            }  
         }

         return !yaexiste;
    } 

    public boolean asignaResponsableAcuerdo(DatosDelega dd) throws Exception{
         boolean yaexiste = false;
        
         FachadaUsuarioArea fua = new FachadaUsuarioArea();
        
         List<AreaBean> areasDelegadas = fua.listaAreasDelegadasxAcuerdoSCancelados(dd.getIdRSuperior(),dd.getIdasuntoacuerdo());
               
         for(AreaBean area:areasDelegadas) {
            if (area.getDatos().getIdarea() == dd.getIdresponsable()) {
                //Ya existe el �rea delegada
                yaexiste = true;
                break;
            }
         }

         ResponsableBean rb = null;
         if (!yaexiste) {
            
            rb = delegado.generaResponsable(dd.getIdresponsable());
            rb.getDatos().setAsignadopor(dd.getIdRSuperior());
            rb.getDatos().setIdaccion(dd.getIdasuntoacuerdo());
            rb.getDatos().setFechaasignado(Utiles.getFechaHora());
            rb.getDatos().setInstruccion(dd.getInstruccion());
            fachada.grabaResponsableAccion(rb);
            
            if (areasDelegadas.isEmpty()) {
              dd.setValor("S");
              fachada.defineDelegadoAcuerdoResponsable(dd.getIdasuntoacuerdo(), dd.getIdRSuperior() , "S"); //Asigna al acuerdo que se encuentra delegado           
            }  
         }


         return !yaexiste;
    }    
   
   
   public boolean eliminaResponsableAsuntoAsignado(int idareaSuperior,int idarea, int idasunto) throws SQLException, Exception {
         FachadaUsuarioArea fua = new FachadaUsuarioArea();
         
         List<AreaBean> areasDelegadas =  fua.listaAreasDelegadasxAsunto(idareaSuperior,idasunto);
         fachada.eliminaResponsableAsuntoAsignado(idareaSuperior,idarea, idasunto);
          if (areasDelegadas.size() == 1) {
            fachada.defineDelegadoAsuntoResponsable(idasunto, idareaSuperior,"");
            }
         return true;
   }
   
    public boolean eliminaResponsableAcuerdoAsignado(int idareaSuperior,int idarea, int idacuerdo) throws SQLException, Exception {
         FachadaUsuarioArea fua = new FachadaUsuarioArea();
         
         List<AreaBean> areasDelegadas =  fua.listaAreasDelegadasxAcuerdo(idareaSuperior,idacuerdo);
         fachada.eliminaResponsableAsuntoAsignado(idareaSuperior,idarea, idacuerdo);
          if (areasDelegadas.size() == 1) {
            fachada.defineDelegadoAcuerdoResponsable(idacuerdo, idareaSuperior,"");
            }
         return true;
   }

   public AsuntoBean consultaAsuntoAsignado(int idResponsable, int idasunto) throws SQLException, Exception{
     
       AsuntoBean asuntoAsignado = fachada.buscaAsuntoAsignado(idResponsable,idasunto);
       return asuntoAsignado;
   }
    
   public AccionBean consultaAcuerdoAsignado(int idResponsable, int idaccion) throws SQLException, Exception{
       
       AccionBean acuerdoAsignado = fachada.buscaAcuerdoAsignado(idResponsable,idaccion);
       return acuerdoAsignado;
   }

}
