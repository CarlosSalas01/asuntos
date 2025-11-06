/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.negocio;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.AreaResponsableResumen;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaDAO;


import mx.org.inegi.dggma.sistemas.asuntos.datosglobales.DatosGlobales;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AreaDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.ResponsableDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.UsuarioDTO;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaUsuarioArea;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.*;

/**
 *
 * @author joseluis
 */
public class AdministraUsuariosAreas implements Serializable {


   public List<AreaResponsableResumen> obtenResumenAsuntos(UsuarioBean usuario, FechaReferencia fechaPivote) throws Exception {
        List<AreaResponsableResumen> listaResponsables = new ArrayList<AreaResponsableResumen>();

        boolean verTodos = false;
        String rolActual = usuario.getPermisoActual().getDatos().getRol();
        /*AreaBean areasimple;
        if (PermisoBean.getADMINISTRADOR().equals(rolActual) || PermisoBean.getEJECUTIVO().equals(rolActual) || PermisoBean.getCONSULTA().equals(rolActual)) {
            areasimple = buscaArea(1);
        }else{
            areasimple = usuario.getPermisoActual().getAreaBean();
        }*/

        List<AreaBean> lista = this.listaAreas(usuario);
        //String fechaNew = FachadaDAO.fechaUltimaCreacionAsuntoPorResponsable(lista, conn);
        //Se muestran solo los asuntos desde la entrada del geo Carlos Guerrero
        String fechaNew = "20100316";

        if (fechaNew != null && fechaPivote.getFecha_inicial().equals("")){
            fechaPivote.setFecha_inicial(fechaNew);
        }

        //int areamax = lista.get(0).getDatos().getIdarea();
        FachadaUsuarioArea fua = new FachadaUsuarioArea();
        //FachadaDAO fachada = new FachadaDAO();
        
        boolean first = true;
        for (AreaBean ab : lista) {
            AreaResponsableResumen rr = new AreaResponsableResumen();
            rr.setAreaBean(ab);

            List<AreaBean> subareas = new ArrayList<AreaBean>();
            
            subareas = fua.listaAreasRecursivo(ab, subareas,true);

            //rr.setMostrar((ab.getDatos().getIdarea() == areamax) || (ab.getDatos().getDependede() == areamax) ? true:false);
            rr.setMostrar(false);

            rr.setSubareas((subareas.size() <= 1) || first ? false:true);

            /*rr.setAsuntosEnProceso(fachada.cantidadAsuntosEnProcesoPorResponsable(subareas,fechaPivote));
            rr.setAsuntosVencidos(fachada.cantidadAsuntosEnProcesoVencidosPorResponsable(subareas,fechaPivote));
            rr.setAsuntosAtendidos(fachada.cantidadAsuntosAtendidosPorResponsable(subareas,fechaPivote));
            rr.setAsuntosCancelados(fachada.cantidadAsuntosCanceladosPorResponsable(subareas,fechaPivote));
            rr.setAsuntosSuspendidos(fachada.cantidadAsuntosDetenidosPorResponsable(subareas,fechaPivote));

            rr.setAsuntosUrgentes(fachada.cantidadAsuntosUrgentesPorResponsable(subareas,fechaPivote));
            rr.setAsuntosImportantes(fachada.cantidadAsuntosImportantesPorResponsable(subareas,fechaPivote));
            rr.setAsuntosNormales(fachada.cantidadAsuntosNormalesPorResponsable(subareas,fechaPivote));

            rr.setAsuntosUrgenesVencidos(fachada.cantidadAsuntosUrgentesVencidosPorResponsable(subareas,fechaPivote));
            rr.setAsuntosImportantesVencidos(fachada.cantidadAsuntosImportantesVencidosPorResponsable(subareas,fechaPivote));
            rr.setAsuntosNormalesVencidos(fachada.cantidadAsuntosNormalesVencidosPorResponsable(subareas,fechaPivote));

            rr.setAsuntosNoPublicados(fachada.cantidadAsuntosNoPublicados(subareas,fechaPivote));
            rr.setAsuntosSinAcciones(fachada.cantidadAsuntosSinAcciones(subareas,fechaPivote));*/

            listaResponsables.add(rr);
            first = false;
        }
   
        return listaResponsables;
    }


    public UsuarioBean buscaUsuario(DatosLogin datosLogin) throws Exception {
        FachadaUsuarioArea fu = new FachadaUsuarioArea();
        UsuarioBean usuarioBean = fu.buscaUsuario(datosLogin);
        if (usuarioBean != null){
           fu.cargaPermisosUsuarios(usuarioBean);
        }
        return usuarioBean;
    }


    public List<AreaBean> listaAreas(UsuarioBean usuario) throws Exception {
        FachadaUsuarioArea fu = new FachadaUsuarioArea();
        return fu.obtenTodasAreas();
    }

     public List<AreaBean> listaAreasResponsables(UsuarioBean usuario) throws Exception {
        FachadaUsuarioArea fu = new FachadaUsuarioArea();
        return fu.listaAreasResponsables(usuario);
    }

     public List<AreaBean> listaAreasResponsablesmas1Nivel(UsuarioBean usuario) throws Exception {
        FachadaUsuarioArea fu = new FachadaUsuarioArea();
        return fu.listaAreasResponsablesmas1Nivel(usuario);
     }
     
     public List<AreaDTO> listaAreasResponsablesmas1NivelS(UsuarioBean usuario) throws Exception {
        FachadaUsuarioArea fu = new FachadaUsuarioArea();
        return fu.listaAreasResponsablesmas1NivelSencilla(usuario);
     } 
     public List<AreaDTO> listaAreasResponsablesmas1NivelSCaptura(UsuarioBean usuario) throws Exception {
        FachadaUsuarioArea fu = new FachadaUsuarioArea();
        return fu.listaAreasResponsablesmas1NivelSencillaCaptura(usuario);
     }      
     public List<AreaDTO> listaAreasResponsablesmas1NivelSCaptura(int idarea) throws Exception {
        FachadaUsuarioArea fu = new FachadaUsuarioArea();
        return fu.listaAreasResponsablesmas1NivelSencillaCaptura(idarea);
     }  
     public List<AreaBean> listaAreasResponsablesXNivel(int nivel) throws Exception {
        FachadaUsuarioArea fu = new FachadaUsuarioArea();
        return fu.listaAreasResponsablesxNivel(nivel);
     }

     public List<AreaBean> listaAreasResponsablesxnivelxareasuperior(int nivel, int idRSuperior) throws Exception {
        FachadaUsuarioArea fu = new FachadaUsuarioArea();
        return fu.listaAreasResponsablesxNivelxAreaSuper(nivel, idRSuperior);
     }

     
    public List<AreaBean> getSubAreas(AreaBean area) throws SQLException, Exception{
        List<AreaBean> subareas = new ArrayList<AreaBean>();
        FachadaUsuarioArea fua = new FachadaUsuarioArea();
        List<AreaBean> areas = fua.listaAreasRecursivo(area, subareas, true);
        return areas;
    }

    public String getAreastoString (List<AreaBean> subareas) {
       String whereAreas=" (";
       for (AreaBean ab : subareas) {
            whereAreas = whereAreas +" idarea  = " + ab.getDatos().getIdarea() + " or ";
        }
       whereAreas = whereAreas.substring(1,whereAreas.length()-4) + " )";
       return whereAreas;

    }
    
   
    public boolean isResponsableSubareas(AreaBean area, AreaBean areaBuscar) throws SQLException, Exception{
        boolean bandera = false;
        List<AreaBean> subareas = getSubAreas(area);
        for (AreaBean ab : subareas) {
          if (ab.getDatos().getIdarea() == areaBuscar.getDatos().getIdarea()){
              bandera = true;
              break;
          }
        }
        return bandera;
    }

    //Se encuentra directo en FachadaUsuarioArea
    public AreaBean buscaArea(Integer idArea) throws Exception {
        FachadaUsuarioArea fu = new FachadaUsuarioArea();
        return fu.buscaArea(idArea);
    }
    

    public List<AreaBean> getAreasCorresponsables() throws SQLException, Exception{
        FachadaUsuarioArea fua = new FachadaUsuarioArea();
        return fua.listaAreasCorresponsables();
    }
    
     public List<AreaBean> listaSubAreas1nivel(UsuarioBean usuario) throws SQLException, Exception{
        FachadaUsuarioArea fua = new FachadaUsuarioArea();
        return fua.listaSubAreasResponsables1Nivel(usuario);
    }
    
     public List<AreaBean> listaSubAreas1Nivel(int idarea) throws Exception {
        FachadaUsuarioArea fua = new FachadaUsuarioArea();
        return fua.listaSubAreasResponsables1Nivel(idarea);
     }
     
     public List<UsuarioDTO> obtenDestinosCorreo(List<AreaDTO> areasConsulta) throws Exception{
        FachadaUsuarioArea fua = new FachadaUsuarioArea(); 
        List<UsuarioDTO> destinatarios = new ArrayList<UsuarioDTO>();
        for (AreaDTO area: areasConsulta){
           List<UsuarioDTO> usuarios = fua.usuariosDTOCPermisosxArea(area.getIdarea());
           for (UsuarioDTO u:usuarios) {
             boolean existe = false;
             for (UsuarioDTO usuarioDestino:destinatarios){
                 if (usuarioDestino.getIdusuario() == u.getIdusuario()) {
                     existe = true;
                     break;
                 }
              }   
             if (!existe) destinatarios.add(u);
           }

        }
        return destinatarios;
     }    
     
      public List<UsuarioDTO> obtenDestinosCorreoBean(int idarea) throws Exception{
        FachadaUsuarioArea fua = new FachadaUsuarioArea(); 
        List<UsuarioDTO> destinatarios = new ArrayList<UsuarioDTO>();
     
           List<UsuarioDTO> usuarios = fua.usuariosDTOCPermisosxArea(idarea);
           for (UsuarioDTO u:usuarios) {
             if (u.getEnviocorreoauto().equals("N")) continue;
             boolean existe = false;
             for (UsuarioDTO usuarioDestino:destinatarios){
                 if (usuarioDestino.getIdusuario() == u.getIdusuario()) {
                     existe = true;
                     break;
                 }
              }   
             if (!existe) destinatarios.add(u);
           }

        
        return destinatarios;
     }    
      
     public List<UsuarioDTO> obtenDestinosCorreoBeanSinA(int idarea) throws Exception{
        FachadaUsuarioArea fua = new FachadaUsuarioArea(); 
        List<UsuarioDTO> destinatarios = new ArrayList<UsuarioDTO>();
     
           List<UsuarioDTO> usuarios = fua.usuariosDTOCPermisosxArea(idarea);
           for (UsuarioDTO u:usuarios) {
             if (u.getEnviocorreoauto().equals("N") || u.getEnviocorreoauto().equals("A")) continue;
             boolean existe = false;
             for (UsuarioDTO usuarioDestino:destinatarios){
                 if (usuarioDestino.getIdusuario() == u.getIdusuario()) {
                     existe = true;
                     break;
                 }
              }   
             if (!existe) destinatarios.add(u);
           }

        
        return destinatarios;
     }    
     
     public List<UsuarioDTO> obtenDestinosCorreo(String[] areasConsulta) throws Exception{
        FachadaUsuarioArea fua = new FachadaUsuarioArea(); 
        List<UsuarioDTO> destinatarios = new ArrayList<UsuarioDTO>();
        for (String areaStr: areasConsulta){
           int  idarea = Integer.parseInt(areaStr); 
           List<UsuarioDTO> usuarios = fua.usuariosDTOCPermisosxArea(idarea);
           for (UsuarioDTO u:usuarios) {
             boolean existe = false;
             for (UsuarioDTO usuarioDestino:destinatarios){
                 if (usuarioDestino.getIdusuario() == u.getIdusuario()) {
                     existe = true;
                     break;
                 }
              }   
             if (!existe) destinatarios.add(u);
           }

        }
        return destinatarios;
     }    
     
     
}
