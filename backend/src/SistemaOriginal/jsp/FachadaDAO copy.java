/*
 * FachadaDAO.java
 *
 * Created on 29 de octubre de 2006, 11:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.fachada;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.naming.NamingException;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.*;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.AccionDAO;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.AnexoAccionDAO;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.AnexoAsuntoDAO;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.AnexoDAO;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.AsuntoDAO;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.CorresponsableDAO;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.InstruccionDAO;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.ResponsableDAO;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AccionBean;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AnexoAccionDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AnexoAsuntoDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AnexoDTO;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AsuntoBean;
import mx.org.inegi.dggma.sistemas.asuntos.dto.InstruccionDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.TareaDTO;
import mx.org.inegi.dggma.sistemas.asuntos.datosglobales.DatosGlobales;
import mx.org.inegi.dggma.sistemas.asuntos.dto.*;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.*;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.Utiles;


import org.apache.commons.fileupload.FileItem;

/**
 *
 * @author Jos Luis Mondragn
 */
public class FachadaDAO {
    private AsuntoDAO adao= null;
    private AccionDAO accdao = null;
    private AvanceDAO avdao = null;
    private ResponsableDAO resDao = null;
    
    /** Creates a new instance of FachadaDAO */
    public FachadaDAO(AreasConsulta areas) {
        adao = new AsuntoDAO(areas);
        accdao = new AccionDAO(areas);
        avdao = new AvanceDAO(areas);
        resDao = new ResponsableDAO(areas);
    }


    /////////////////////////////////////////////////////////////////////////////////////
    // Nuevas columas  //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////
    public int ultimoidAsunto()throws SQLException, NamingException {
      int id = -1;
      
      id = adao.getMaxIdAsunto();

      return id;
    }

  
    
    //////////////////////////////////////////////////////////////////////

       
   public AsuntoBean buscaAsuntoAsignado(int idRSuperior, int idasunto)throws SQLException, NamingException, Exception{
      
       return adao.buscarAsuntoAsignado(idRSuperior, idasunto);
       
   }
   
   public AccionBean buscaAcuerdoAsignado(int idRSuperior,int idacuerdo) throws Exception {
       return accdao.buscarAcuerdoAsignado(idRSuperior, idacuerdo);
   
   }
    
  
   
    public List<AsuntoBean> buscarAsuntosPorAreaKeet(FiltroAsunto filtro)throws SQLException, NamingException, Exception{
        
        List<AsuntoBean> lista = adao.buscarAsuntosPorAreaxTipo(filtro,"K");   
        return lista;
    }
     
    
    public List<AsuntoBean> buscarAsuntosPorAreaCorreo(FiltroAsunto filtro)throws SQLException, NamingException, Exception{
        
        List<AsuntoBean> lista = adao.buscarAsuntosPorAreaxTipo(filtro,"C");   
        return lista;
    }
    
    public List<AsuntoBean> buscarAsuntosPorAreaComisiones(FiltroAsunto filtro)throws SQLException, NamingException, Exception{
       
        List<AsuntoBean> lista = adao.buscarAsuntosPorAreaxTipo(filtro,"M");   
        return lista;
    }
    public List<AsuntoBean> buscarAsuntosPorAreaConvenio(FiltroAsunto filtro)throws SQLException, NamingException, Exception{
        
        List<AsuntoBean> lista = adao.buscarAsuntosPorAreaxTipo(filtro,"V");
        return lista;
    }
     
    public List<AsuntoBean> buscarAsuntosPorAreaReuniones( FiltroAsunto filtro )throws SQLException, NamingException, Exception{
        
        List<AsuntoBean> lista = adao.buscarAsuntosReunion(filtro);   
        return lista;
    }
    
    public AvanceBean buscaAvancexLlavePrimaria(int idavance)throws SQLException, NamingException, Exception{
       
       return avdao.findByPrimaryKey(idavance);
    }
    //////////////////////////////////////////////
    
   public int cantidadAsuntosPorAreaxTipo(FiltroAsunto filtro, String tipo) throws Exception{
       
       Cantidad dato = adao.cantidadAsuntosxAreaxTipo(filtro, tipo);  
       int result = dato != null ? dato.getCantidad():0; 
       return result;
   }
   
   public int cantidadAsuntosReuniones(FiltroAsunto filtro) throws Exception{
      
       Cantidad dato = adao.cantidadAsuntosReunion(filtro);  
       int result = dato != null ? dato.getCantidad():0; 
       return result;
   }
    
   public int cantidadAcuerdosFiltro(FiltroAsunto filtro) throws Exception{
      
      Cantidad dato = accdao.cantidadAccionesFiltro(filtro);
      int result = dato != null ? dato.getCantidad():0; 
      return result;
   }
   
   public int cantidadReunionesSinAcuerdosDirectos(int idarea)throws SQLException, NamingException, Exception{
      
      Cantidad dato = adao.cantidadReunionesSinAcuerdosDirectos(idarea); 
      return dato.getCantidad();
   }
   
   public int cantidadReunionesSinAcuerdosDirectos(FiltroAsunto filtro)throws SQLException, NamingException, Exception{
      
      Cantidad dato = adao.cantidadReunionesSinAcuerdosDirectos(filtro); 
      return dato.getCantidad();
   }
   public int cantidadReunionesEnviados(FiltroAsunto filtro)throws SQLException, NamingException, Exception{
      Cantidad dato = adao.cantidadReunionesEnviados(filtro); 
      return dato.getCantidad();
   }   
   public int cantidadReunionesConAcuerdosDirectos(int idarea)throws SQLException, NamingException, Exception{
      
      Cantidad dato = adao.cantidadReunionesSinAcuerdosDirectos(idarea); 
      return dato.getCantidad();
   }

   public int cantidadReunionesConAcuerdosDirectos(FiltroAsunto filtro)throws SQLException, NamingException, Exception{
      
      Cantidad dato = adao.cantidadReunionesSinAcuerdosDirectos(filtro); 
      return dato.getCantidad();
   }
   
   public int cantidadReunionesSinAcuerdosCompartidos(FiltroAsunto filtro, int idarea)throws SQLException, NamingException, Exception{
      
      Cantidad dato = adao.cantidadReunionesSinAcuerdosCompartidos(idarea); 
      return dato.getCantidad();
   }
   
   public int cantidadReunionesSinAcuerdos(FiltroAsunto filtro)throws SQLException, NamingException, Exception{
      Cantidad dato = adao.cantidadReunionesSinAcuerdos(filtro); 
      return dato.getCantidad();
   }
   
    //////////////////////////////
   
   
    public List<AsuntoBean> buscarReunionesSinAcuerdos(FiltroAsunto filtro)throws SQLException, NamingException, Exception{
      
      List<AsuntoBean> lista = adao.buscarAsuntosSAccionesxArea();
      return lista;  
    }
    
    public int reunionesSinAcuerdo(String anio) throws NamingException, Exception{
        return adao.reunionesSinAcuerdo(anio);
    }
  
    public AsuntoBean buscaAsuntoPorLlavePrimaria(int pk) throws Exception {
        
        AsuntoBean asunto = adao.findByPrimaryKey(pk);
        return asunto;
    }

     public AsuntoBean buscaAsuntoPorLlavePrimariaFull(int pk) throws Exception {
        
        AsuntoBean asunto = adao.findByPrimaryKeyFull(pk);
        return asunto;
     }

    public AccionBean buscaAccionPorLlavePrimaria(int pk) throws Exception {
        
        AccionBean accion = accdao.findByPrimaryKeySingle(pk);
        return accion;
    }
     
     
    public AccionBean buscaAccionPorLlavePrimariaFull(int pk) throws Exception {
        
        AccionBean accion = accdao.findByPrimaryKey(pk);
        return accion;
    }
    
   public AnexoDTO buscaAnexoPorLlavePrimaria(int pk) throws Exception {
        AnexoDAO anexDao = new AnexoDAO();
        AnexoDTO anexo =  anexDao.findByPrimaryKey(pk);
        return anexo;
    }
   
   public AnexoDTO buscaAnexoActSeguim(int idanexo) throws Exception {
        AnexoDAO anexDao = new AnexoDAO();
        AnexoDTO anexo = anexDao.findByPrimaryKeySeguimiento(idanexo);
        return anexo;
    }
    public AnexoDTO buscaAnexoAccionPorLlavePrimaria(int pk) throws Exception {
        AnexoAccionDAO anexAccDao = new AnexoAccionDAO();
        AnexoDTO anexo =  anexAccDao.findByPrimaryKey(pk);
        return anexo;
    }

    public InstruccionDTO buscaInstruccionPorLlavePrimaria(int pk) throws Exception {
        InstruccionDAO idao = new InstruccionDAO();
        InstruccionDTO instruccion = idao.findByPrimaryKey(pk);
        return instruccion;
    }

    public void eliminaAccion(int idAccion) throws Exception {
        accdao.delete(idAccion);
    }

    public void eliminaAnexo(int idAnexo) throws Exception {
        AnexoDAO anexDao = new AnexoDAO();
        anexDao.delete(idAnexo);
        
    }
    public void eliminaAnexosxAsunto(int idasunto) throws Exception {
        AnexoDAO anexDao = new AnexoDAO();
        anexDao.eliminaAnexosxAsunto(idasunto);
        
    }
    public void eliminaAnexosxAcuerdo(int idacuerdo) throws Exception {
        AnexoDAO anexDao = new AnexoDAO();
        anexDao.eliminaAnexosxAcuerdo(idacuerdo);
    }

    public void eliminaAsunto(int idAsunto) throws Exception {
        adao.delete(idAsunto);
    }

    public void eliminaInstruccion(int idInstruccion) throws Exception {
        InstruccionDAO idao = new InstruccionDAO();
        idao.delete(idInstruccion);
        
    }
    
    public void eliminaAvance(int idavance) throws Exception{
       
       avdao.delete(idavance);
    }

    public List<AccionBean> getAccionesAsunto(int idAsunto)throws SQLException, NamingException, Exception {
        AsuntoBean  asunto = new AsuntoBean();
       
        asunto.setIdasunto(idAsunto);
        List<AccionBean> lista = accdao.buscarAccionesPorAsunto(asunto);
        return lista;
    }
    
   

    public boolean grabaAsunto(AsuntoBean asunto) throws Exception {
       
        boolean nuevo = true;
        if (asunto.getIdasunto() <= 0) {
            adao.insert(asunto);
            asunto.setIdasunto(adao.getMaxIdAsunto());
        } else {
            nuevo = false;
            adao.update(asunto);
        }
        return nuevo;
    }

    public void actualizaFechaReprograma(AsuntoBean asunto) throws NamingException, SQLException{
      adao.updateFechaReprograma(asunto);
   }
    
    
    public void grabaAnexoAsunto(AnexoAsuntoDTO anexo,  FileItem item) throws Exception {
        AnexoAsuntoDAO anexADao = new AnexoAsuntoDAO();
        if (anexo.getIdanexo() == -1) {
            anexADao.insert(anexo);
            File uploadedFile = new File(DatosGlobales.getRuta() + anexo.getNombreenservidor());
            try {
                item.write(uploadedFile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            anexADao.update(anexo);
        }
        
    }
      
    public void grabaAnexoAccion(AnexoAccionDTO anexo, FileItem item) throws Exception {
        AnexoAccionDAO anexCDao = new AnexoAccionDAO();
        if (anexo.getIdanexo() == -1) {
            anexCDao.insert(anexo);
            
            File uploadedFile = new File(DatosGlobales.getRuta() + anexo.getNombreenservidor());
            try {
                item.write(uploadedFile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            anexCDao.update(anexo);
        }
    }
    
    public void grabaAnexoAvance(AnexoAvanceDTO anexo, FileItem item) throws Exception {
        AnexoAvanceDAO anexCDao = new AnexoAvanceDAO();
        if (anexo.getIdanexo() == -1) {
            anexCDao.insert(anexo);
            //File uploadedFile = new File(ap.getDirectorioAnexos() + File.separatorChar + anexo.getNombreEnServidor());
            File uploadedFile = new File(DatosGlobales.getRuta() + anexo.getNombreenservidor());
            try {
                item.write(uploadedFile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            anexCDao.update(anexo);
        }
    }

    public void grabaAnexo(AnexoDTO anexo) throws Exception {
        AnexoDAO anexDao = new AnexoDAO();
        if (anexo.getIdanexo() == -1) {
            anexDao.insert(anexo);
        } else {
            anexDao.update(anexo);
        }
    }

    public void grabaAnexo(AnexoDTO anexo, FileItem item) throws Exception {
        AnexoDAO anexDao = new AnexoDAO();
        if (anexo.getIdanexo() == -1) {
            anexDao.insert(anexo);
            File uploadedFile = new File(DatosGlobales.getRuta() + anexo.getNombreenservidor());
            try {
                item.write(uploadedFile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            
        } else {
            anexDao.update(anexo);
        }
    }
    

    public boolean grabaInstruccion(InstruccionDTO instruccion) throws Exception {
        InstruccionDAO idao = new InstruccionDAO();
        boolean nuevo = false;
        if (instruccion.getIdInstruccion() < 0) {
            idao.insert(instruccion);
            nuevo = true;
        } else {
            idao.update(instruccion);
        }
        return nuevo;
    }

    public void marcarInstruccionAtendida(int idInstruccion) throws Exception {
        InstruccionDAO idao = new InstruccionDAO();
        InstruccionDTO instruccion = idao.findByPrimaryKey(idInstruccion);
        instruccion.setAtendida("S");
        idao.update(instruccion);
    }

    public void noDescargarAsunto(int idAsunto) throws Exception {
       
        AsuntoBean asunto = adao.findByPrimaryKey(idAsunto);
        asunto.setDescargaestatus("N");
        adao.update(asunto);
    }



    public void publicarAsunto(int idAsunto) throws Exception {
       /* AsuntoDAO asDao = new AsuntoDAO();
        AsuntoBean asunto = asDao.findByPrimaryKey(idAsunto);
        asunto.setPublicoestatus("S");
        asunto.setAdministrador("N");
        asDao.update(asunto);*/
    }

    public void publicarAccion(int idAccion) throws Exception {
        /*AccionDAO acDao = new AccionDAO();
        AccionBean accion = acDao.findByPrimaryKey(idAccion);
        accion.setPublicoEstatus("S");
        acDao.update(accion);*/
    }


    public boolean grabaAccion(AccionBean accion) throws Exception {
        
        boolean nuevo = false;
        if (accion.getIdAccion() < 0) {
            accdao.insert(accion);
            accion.setIdAccion(accdao.getMaxIdAccion());
            nuevo = true;
        } else {
            accdao.update(accion);
        }
        
        return nuevo;
        
    }

    public void actualizaFechaReprograma(AccionBean accion) throws NamingException, SQLException{
       accdao.updateReprogramacion(accion);
    }

    public List<TareaDTO> getTareasAsunto(int idAsunto)throws SQLException, NamingException, Exception {
        /*AccionDAO acDao = new AccionDAO();
        InstruccionDAO idao = new InstruccionDAO();
        List<AccionBean> acciones= acDao.buscarAccionesPorAsunto(idAsunto);
        List<InstruccionDTO> instrucciones= idao.buscarInstruccionesPorAsunto(idAsunto);

        List<TareaDTO> tareas=new ArrayList<TareaDTO>();
        for (AccionBean a : acciones) {
            if (a.isPublicoEstatus()){
              tareas.add(a.getTarea());
            }
        }
        for (InstruccionDTO i : instrucciones) {
            tareas.add(i.getTarea());
        }
        
        Collections.sort(tareas);

        return tareas;*/
        return null;
    }
    

    public void grabaCorresponsable(AreaBean corresponsable, AsuntoBean asuntoCapturado) throws Exception {
        CorresponsableDAO corrDao = new CorresponsableDAO();
        corrDao.insertarCorresponsable(asuntoCapturado, corresponsable);
    }
    
    public void grabaResponsableAsunto(ResponsableBean responsable) throws Exception {
       
        resDao.insertarResponsableAsunto(responsable);
    }
    
    public void grabaResponsableAccion(ResponsableBean responsable) throws Exception {
        
        resDao.insertarResponsableAccion(responsable);
        
    }

   public void grabaCorresponsable(AreaBean corresponsable, Integer idAsuntoCapturado) throws Exception {
        
        AsuntoBean asunto = adao.findByPrimaryKey(idAsuntoCapturado);
        //Esta validaci�n se pasa a la captura del asunto
        CorresponsableDAO corrDao = new CorresponsableDAO();
        corrDao.insertarCorresponsable(asunto, corresponsable);
    }

   
   
   public void eliminaCorresponsable(AreaBean corresponsable, AsuntoBean asunto) throws Exception {
        CorresponsableDAO corrDao = new CorresponsableDAO();
        corrDao.eliminarCorresponsable(asunto, corresponsable);
    }

   
   
    public boolean esResponsablePrincipal(AreaBean corresponsable, Integer idAsuntoCapturado) throws Exception {
        boolean esPrincipal = false;
        
        AsuntoBean asunto = adao.findByPrimaryKey(idAsuntoCapturado);
        if (asunto.getResponsable().getDatos().getIdarea() == corresponsable.getDatos().getIdarea()) {
            esPrincipal = true;
        }
        return esPrincipal;
    }

    public boolean yaEsCorresponsable(AreaBean corresponsable, Integer idAsuntoCapturado) throws Exception {
        boolean yaEsCorresponsable;
        
        CorresponsableDAO corrDao = new CorresponsableDAO();
        
        AsuntoBean asunto = adao.findByPrimaryKey(idAsuntoCapturado);
        yaEsCorresponsable = corrDao.existeCorresponsableEnAsunto(asunto, corresponsable);
        
        return yaEsCorresponsable;
    }


    public List<UsuarioBean> usuariosResponsablesxArea(int idArea)throws SQLException, NamingException, Exception{
        FachadaUsuarioArea fua = new FachadaUsuarioArea();
        List<UsuarioBean>  usuarios = fua.usuariosResponsablesxAsunto(idArea);
        
        return usuarios;
    }

   

    public List<AnexoAsuntoDTO> buscarAnexosPorAsunto(AsuntoBean asunto)throws SQLException, NamingException {
        AnexoAsuntoDAO anexAsDao = new AnexoAsuntoDAO();
        List<AnexoAsuntoDTO> anexos = anexAsDao.buscarAnexosPorAsunto(asunto);

       return anexos;

    }
    
    
    public List<AnexoAccionDTO> buscarAnexosPorAcuerdo(AccionBean acuerdo)throws SQLException, NamingException {
        AnexoAccionDAO anexAcDao = new AnexoAccionDAO();
        List<AnexoAccionDTO> anexos = anexAcDao.buscarAnexosPorAccion(acuerdo);

       return anexos;

    }


    public List<AnexoAvanceDTO> buscarAnexosPorAvance(AvanceBean avance)throws SQLException, NamingException {
        AnexoAvanceDAO anexAsDao = new AnexoAvanceDAO();
        List<AnexoAvanceDTO> anexos = anexAsDao.buscarAnexosPorAvance(avance);

       return anexos;

    }
   
      public List<AnexoAvanceDTO> buscarAnexosPorAvance(int idavance)throws SQLException, NamingException {
        AnexoAvanceDAO anexAsDao = new AnexoAvanceDAO();
        List<AnexoAvanceDTO> anexos = anexAsDao.buscarAnexosPorAvance(idavance);

       return anexos;

    }

   public List<AreaBean> buscarAreasCorresponsablesPorAsunto(AsuntoBean asunto)throws SQLException, NamingException, Exception{
       FachadaUsuarioArea fu = new FachadaUsuarioArea();
       List<AreaBean> areas = fu.buscarAreasCorresponsablesPorAsunto(asunto);

       return areas;

   }
   
   
   public List<ResponsableBean> buscarResponsablesPorAsunto(AsuntoBean asunto)throws SQLException, NamingException, Exception{
       
       FachadaUsuarioArea fu = new FachadaUsuarioArea();
              
       List<ResponsableBean> responsables = new ArrayList<ResponsableBean>();
       
       List<ResponsableDTO> responsablesDto = resDao.obtenResponsablesxAsunto(asunto);
       
       for(ResponsableDTO r:responsablesDto){
           ResponsableBean rb = new ResponsableBean();
           rb.setDatos(r);
           AreaBean area = fu.buscaArea(r.getIdarea());
           rb.setArea(area.getDatos());
           rb.setUsuario(area.getResponsable().getDatos());
           responsables.add(rb);
       }

       return responsables;

   }

   public List<ResponsableBean> buscarResponsablesPorAcuerdo(AccionBean accion)throws SQLException, NamingException, Exception{
       
       FachadaUsuarioArea fu = new FachadaUsuarioArea();
              
       List<ResponsableBean> responsables = new ArrayList<ResponsableBean>();
       
       List<ResponsableDTO> responsablesDto = resDao.obtenResponsablesxAcuerdo(accion);
       
       for(ResponsableDTO r:responsablesDto){
           ResponsableBean rb = new ResponsableBean();
           rb.setDatos(r);
           AreaBean area = fu.buscaArea(r.getIdarea());
           rb.setArea(area.getDatos());
           rb.setUsuario(area.getResponsable().getDatos());
           responsables.add(rb);
       }

       return responsables;

   }

   
   public List<ResponsableBean> buscarResponsablesPorAcuerdoxAreas(AccionBean acuerdo)throws SQLException, NamingException, Exception{
       
      FachadaUsuarioArea fu = new FachadaUsuarioArea();
      List<ResponsableBean> responsables = new ArrayList<ResponsableBean>();
       
      List<ResponsableDTO> responsablesDto = resDao.obtenResponsablesxAcuerdoxAreas(acuerdo);
       boolean atendido = true;
       for(ResponsableDTO r:responsablesDto){
           ResponsableBean rb = new ResponsableBean();
           if (r.getEstatus().equals("P") && acuerdo.getActivoestatus() == null && acuerdo.getAcuerdo_fecha() != null &&  !acuerdo.getAcuerdo_fecha().equals("")) { //Se agreg� la condici�n de que no calcule fechas de termino de compromisos
             int retraso = Utiles.restaFechasEnDias(acuerdo.getAcuerdo_fecha(),Utiles.getFechaHora()); //es de acuerdo a la ultima fecha reprogramada
             r.setDiasretraso(retraso > 0 ? retraso : 0);
             int atencion = Utiles.restaFechasEnDias(acuerdo.getFecha(),Utiles.getFechaHora());
             r.setDiasatencion(atencion > 0 ? atencion : 0);  
             atendido = false;
           }  
           
           rb.setDatos(r);
           AreaBean area = fu.buscaArea(r.getIdarea());
           rb.setArea(area.getDatos());
           rb.setUsuario(area.getResponsable().getDatos());
           responsables.add(rb);
       }

      // if (atendido) acuerdo.setEstatus("A");
       return responsables;
   }

   public List<ResponsableBean> buscarResponsablesPorAcuerdoAsignado(int idRSuperior,AccionBean acuerdo)throws SQLException, NamingException, Exception{
       
       FachadaUsuarioArea fu = new FachadaUsuarioArea();
              
       List<ResponsableBean> responsables = new ArrayList<ResponsableBean>();
       
      List<ResponsableDTO> responsablesDto = resDao.obtenResponsablesxAcuerdoxAreaAsignada(acuerdo.getIdAccion(), idRSuperior);
       
       for(ResponsableDTO r:responsablesDto){
           ResponsableBean rb = new ResponsableBean();
            if (r.getEstatus().equals("P") && acuerdo.getActivoestatus() != null && !acuerdo.getActivoestatus().equals("CNV")) { //Se agreg� la condici�n de que no calcule fechas de termino de compromisos) {
             int retraso = Utiles.restaFechasEnDias(acuerdo.getAcuerdo_fecha(),Utiles.getFechaHora()); //es de acuerdo a la ultima fecha reprogramada
             r.setDiasretraso(retraso > 0 ? retraso : 0);
             int atencion = Utiles.restaFechasEnDias(acuerdo.getFecha(),Utiles.getFechaHora());
             r.setDiasatencion(atencion > 0 ? atencion : 0);  
           }  
           rb.setDatos(r);
           AreaBean area = fu.buscaArea(r.getIdarea());
           rb.setArea(area.getDatos());
           rb.setUsuario(area.getResponsable().getDatos());
           responsables.add(rb);
       }

       return responsables;
   }

   
    public List<ResponsableBean> buscarResponsablesPorAcuerdoAsignado(int idRSuperior,int idacuerdo)throws SQLException, NamingException, Exception{
       
      FachadaUsuarioArea fu = new FachadaUsuarioArea();
              
      List<ResponsableBean> responsables = new ArrayList<ResponsableBean>();
       
       //23/08/14 Se cambi� para eliminar a los cancelados y no se consideren
      //List<ResponsableDTO> responsablesDto = resDao.obtenResponsablesxAcuerdoxAreaAsignada(idacuerdo, idRSuperior);
      List<ResponsableDTO> responsablesDto = resDao.obtenResponsablesxAcuerdoxAreaAsignadaSCancelados(idacuerdo, idRSuperior);
       
       for(ResponsableDTO r:responsablesDto){
           ResponsableBean rb = new ResponsableBean();
           rb.setDatos(r);
           AreaBean area = fu.buscaArea(r.getIdarea());
           rb.setArea(area.getDatos());
           rb.setUsuario(area.getResponsable().getDatos());
           responsables.add(rb);
       }

       return responsables;
   }

   
   public List<ResponsableBean> buscarResponsablesPorAsuntoxAreasM(AsuntoBean asunto)throws SQLException, NamingException, Exception{
       
       FachadaUsuarioArea fu = new FachadaUsuarioArea();
              
       List<ResponsableBean> responsables = new ArrayList<ResponsableBean>();
       
       List<ResponsableDTO> responsablesDto = resDao.obtenResponsablesxAsuntoxAreas(asunto.getIdasunto());
       //ojo revisar este pocedimiento
       boolean atendido = true;
       for(ResponsableDTO r:responsablesDto){
           ResponsableBean rb = new ResponsableBean();
           if (r.getEstatus().equals("P") && !asunto.getTipoasunto().equals("V")) { //Se agreg� la condici�n de que en convenios no se calcule las dias de retraso
               int retraso = 0;
               if(asunto.getFechaatender() != null && !asunto.getFechaatender().trim().equals("")  ) {
                   retraso = Utiles.restaFechasEnDias(asunto.getFechaatender(),Utiles.getFechaHora()); //es de acuerdo a la ultima fecha reprogramada
               }
             r.setDiasretraso(retraso > 0 ? retraso : 0);
             int atencion = Utiles.restaFechasEnDias(asunto.getFechaingreso(),Utiles.getFechaHora());
             r.setDiasatencion(atencion > 0 ? atencion : 0);  
             atendido = false;
           }  
           //Para que funcione el visualizar como se atendi� un asunto
           r.setComentario(Utiles.parrafeaHTML(r.getComentario()));
           //
           rb.setDatos(r);
           
           AreaBean area = fu.buscaArea(r.getIdarea());
           rb.setArea(area.getDatos());
           rb.setUsuario(area.getResponsable().getDatos());
           responsables.add(rb);
       }
       
       //Si todos los responsables atendieron se atiende el asunto
       //if (atendido)  asunto.setEstatus("A");
       
       return responsables;
   }
   
    public List<ResponsableBean> buscarResponsablesPorAsuntoxAreas(int idasunto)throws SQLException, NamingException, Exception{
       
       FachadaUsuarioArea fu = new FachadaUsuarioArea();
              
       List<ResponsableBean> responsables = new ArrayList<ResponsableBean>();
       
       List<ResponsableDTO> responsablesDto = resDao.obtenResponsablesxAsuntoxAreas(idasunto);
       //ojo revisar este pocedimiento
       for(ResponsableDTO r:responsablesDto){
           ResponsableBean rb = new ResponsableBean();
           rb.setDatos(r);
           
           AreaBean area = fu.buscaArea(r.getIdarea());
           rb.setArea(area.getDatos());
           rb.setUsuario(area.getResponsable().getDatos());
           responsables.add(rb);
       }

       return responsables;
   }
     
    public List<ResponsableBean> buscarResponsablesxAsuntoAsignadoM(int idRSuperior, AsuntoBean asunto)throws SQLException, NamingException, Exception{
        
        FachadaUsuarioArea fu = new FachadaUsuarioArea();
        List<ResponsableDTO> responsablesDto = resDao.obtenResponsablesxAsuntoxAreaAsignada(asunto.getIdasunto(), idRSuperior);
        
        List<ResponsableBean> responsables = new ArrayList<ResponsableBean>();
        boolean pendiente= false;
        
        for(ResponsableDTO r:responsablesDto){
           ResponsableBean rb = new ResponsableBean();
           if (r.getEstatus().equals("P")  && !asunto.getTipoasunto().equals("V")) { //Se agreg� la condici�n de que en convenios no se calcule las dias de retraso
             int retraso = Utiles.restaFechasEnDias(asunto.getFechaatender(),Utiles.getFechaHora()); //es con respecto a la ultima fecha reprogramada
             r.setDiasretraso(retraso > 0?retraso:0);
             int atencion = Utiles.restaFechasEnDias(asunto.getFechaingreso(),Utiles.getFechaHora());
             r.setDiasatencion(atencion > 0 ? atencion : 0);  
             pendiente = true;
           }  
           //Para la visualizaci�n de como se atendi�
           r.setComentario(Utiles.parrafeaHTML(r.getComentario()));
           //
           rb.setDatos(r);
           AreaBean area = fu.buscaArea(r.getIdarea());
           rb.setArea(area.getDatos());
           rb.setUsuario(area.getResponsable().getDatos());
           responsables.add(rb);
       }
        
      //260914 se quito para que si no tiene responsbles activos no lo pongo atendido  
       //if (pendiente)  asunto.setEstatus("A");

       return responsables;
        
    }
    
     public List<ResponsableBean> buscarResponsablesxAsuntoAsignado(int idRSuperior, int idasunto)throws SQLException, NamingException, Exception{
        
        FachadaUsuarioArea fu = new FachadaUsuarioArea();
        
        //List<ResponsableDTO> responsablesDto = resDao.obtenResponsablesxAsuntoxAreaAsignada(idasunto, idRSuperior);
        //23/08/14 Se cambi� para eliminar a los cancelados y no se consideren
        List<ResponsableDTO> responsablesDto = resDao.obtenResponsablesxAsuntoxAreaAsignadaSCancelados(idasunto, idRSuperior);
        
        
        List<ResponsableBean> responsables = new ArrayList<ResponsableBean>();
        for(ResponsableDTO r:responsablesDto){
           ResponsableBean rb = new ResponsableBean();
           rb.setDatos(r);
           AreaBean area = fu.buscaArea(r.getIdarea());
           rb.setArea(area.getDatos());
           rb.setUsuario(area.getResponsable().getDatos());
           responsables.add(rb);
       }

       return responsables;
        
    }
     
     
    /////RecursivoResponsbles Asunto
    public void obtenResponsablesAsuntoRecursivo(int idareaSuperior, int idasunto, List<ResponsableBean> responsablesAsunto) throws NamingException, Exception {
        List<ResponsableBean> subresponsables = this.buscarResponsablesxAsuntoAsignado(idareaSuperior, idasunto);
        if (subresponsables != null && !subresponsables.isEmpty()) {
          for(ResponsableBean r:subresponsables){
              responsablesAsunto.add(r);
              this.obtenResponsablesAsuntoRecursivo(r.getArea().getIdarea(),idasunto,responsablesAsunto);
          }
        }
    }
     
    /////RecursivoResponsbles Acuerdo
    public void obtenResponsablesAcuerdoRecursivo(int idareaSuperior, int idacuerdo, List<ResponsableBean> responsablesAcuerdo) throws NamingException, Exception {
       List<ResponsableBean> subresponsables = this.buscarResponsablesPorAcuerdoAsignado(idareaSuperior, idacuerdo);
        if (subresponsables != null && !subresponsables.isEmpty()) {
          for(ResponsableBean r:subresponsables){
              responsablesAcuerdo.add(r);
              this.obtenResponsablesAsuntoRecursivo(r.getArea().getIdarea(),idacuerdo,responsablesAcuerdo);
          }
        }
    }
    
     
     public List<ResponsableBean> buscarResponsablesxAcuerdoAsignadoM(int idRSuperior,AccionBean acuerdo)throws SQLException, NamingException, Exception{
        
        FachadaUsuarioArea fu = new FachadaUsuarioArea();
        List<ResponsableDTO> responsablesDto = resDao.obtenResponsablesxAcuerdoxAreaAsignada(acuerdo.getIdAccion(), idRSuperior);
        
        List<ResponsableBean> responsables = new ArrayList<ResponsableBean>();
        boolean atendido = true;
        for(ResponsableDTO r:responsablesDto){
           ResponsableBean rb = new ResponsableBean();
          
           if (r.getEstatus().equals("P") && acuerdo.getActivoestatus() != null && !acuerdo.getActivoestatus().equals("CNV")) { //Se agreg� la condici�n de que no calcule fechas de termino de compromisos) {
             int retraso = Utiles.restaFechasEnDias(acuerdo.getAcuerdo_fecha(),Utiles.getFechaHora()); //es con respecto a la ultima fecha reprogramada
             r.setDiasretraso(retraso > 0?retraso:0);
             int atencion = Utiles.restaFechasEnDias(acuerdo.getFecha(),Utiles.getFechaHora());
             r.setDiasatencion(atencion > 0 ? atencion : 0);  
             atendido = false;
           }  
           
           rb.setDatos(r);
           AreaBean area = fu.buscaArea(r.getIdarea());
           rb.setArea(area.getDatos());
           rb.setUsuario(area.getResponsable().getDatos());
           responsables.add(rb);
       }

         //260914 se quito para que si no tiene responsbles activos no lo pongo atendido  
       //if (atendido)  acuerdo.setEstatus("A");
  
       return responsables;
        
    }

    
   public ResponsableBean buscarResponsablePorAsunto(int idResponsable,int idasunto)throws SQLException, NamingException, Exception{
      
       ResponsableDTO r = resDao.obtenResponsablexAsunto(idResponsable, idasunto);
       FachadaUsuarioArea fu = new FachadaUsuarioArea();
       
       ResponsableBean rb = null;
       
       if (r != null) {
           rb = new ResponsableBean();
           rb.setDatos(r);
           AreaBean area = fu.buscaArea(r.getIdarea());
           rb.setArea(area.getDatos());
           rb.setUsuario(area.getResponsable().getDatos());
       }
   
       return rb;
   }
   
   public ResponsableBean buscarResponsableNCanceladoPorAsunto(int idResponsable,int idasunto)throws SQLException, NamingException, Exception{
      
       ResponsableDTO r = resDao.obtenResponsablexAsuntoNoCancelado(idResponsable, idasunto);
       FachadaUsuarioArea fu = new FachadaUsuarioArea();
       
       ResponsableBean rb = null;
       
       if (r != null) {
           rb = new ResponsableBean();
           rb.setDatos(r);
           AreaBean area = fu.buscaArea(r.getIdarea());
           rb.setArea(area.getDatos());
           rb.setUsuario(area.getResponsable().getDatos());
       }
   
       return rb;
   }
   
    //Obtiene el responsable sin cancelados
   public ResponsableBean obtenResponsablexAsuntoNoCancelado(int idarea,int idasunto )throws SQLException, NamingException, Exception{
       ResponsableBean respB = new ResponsableBean();
       //Se hizo la modificaci�n para que solo trajera a los responsables 
       respB.setDatos(resDao.obtenResponsablexAsuntoNoCancelado(idarea, idasunto)); 
       
       respB.setArea(new AreaDAO().getArea(idarea));
       respB.setUsuario(new UsuarioDAO().buscaUsuario(respB.getArea().getIdresponsable()));
       
       return respB;
   }
   public ResponsableBean obtenResponsablexAsuntoNoCanceladoAcuerdo(int idarea,int idacuerdo )throws SQLException, NamingException, Exception{
       ResponsableBean respB = new ResponsableBean();
       //Se hizo la modificaci�n para que solo trajera a los responsables 
       respB.setDatos(resDao.obtenResponsablexAcuerdoNoCancelado(idarea, idacuerdo)); 
       respB.setArea(new AreaDAO().getArea(idarea));
       respB.setUsuario(new UsuarioDAO().buscaUsuario(respB.getArea().getIdresponsable()));
       
       return respB;
   }
   
    public ResponsableBean buscarResponsablePorAcuerdo(int idResponsable,int idacuerdo)throws SQLException, NamingException, Exception{
       
       ResponsableDTO r = resDao.obtenResponsablexAcuerdo(idResponsable, idacuerdo);
       FachadaUsuarioArea fu = new FachadaUsuarioArea();
       
       ResponsableBean rb = null;
       
       if (r != null) {
           rb = new ResponsableBean();
           rb.setDatos(r);
           AreaBean area = fu.buscaArea(r.getIdarea());
           rb.setArea(area.getDatos());
           rb.setUsuario(area.getResponsable().getDatos());
       }
   
       return rb;
   }
    
  
   
   
   public void defineDelegadoAsuntoResponsable(int idasunto, int responsable, String valor)throws SQLException, NamingException{
       
       resDao.updateDelegaAsunto(idasunto, responsable, valor);
   }   
   
    public void defineDelegadoAcuerdoResponsable(int idacuerdo, int responsable, String valor)throws SQLException, NamingException{
       
       resDao.updateDelegaAcuerdo(idacuerdo, responsable, valor);
   }  
   
   public void eliminaResponsableAsuntoAsignado(int idareaSuperior,int idarea, int idasunto)throws SQLException, NamingException{
        
        ResponsableDTO resp = resDao.obtenResponsablexAsuntoAsignado(idareaSuperior,idarea, idasunto);
        resDao.eliminarResponsable(resp.getIdresponsable());
        //System.out.println("");
   }
   
   public void eliminaResponsbleAcuerdoAsignado(int idRSuperior, int idarea, int idacuerdo)throws SQLException, NamingException{
       
        ResponsableDTO resp = resDao.obtenResponsablexAcuerdoAsignado(idRSuperior,idarea, idacuerdo);
        resDao.eliminarResponsable(resp.getIdresponsable());
   }
   
   
    public ResponsableBean obtenResponsablexAcuerdoNoCancelado(int idarea,int idaccion)throws SQLException, NamingException, Exception{
       ResponsableBean respB = new ResponsableBean();
       respB.setDatos(resDao.obtenResponsablexAcuerdoNoCancelado(idarea,idaccion));
       respB.setArea(new AreaDAO().getArea(idarea));
       respB.setUsuario(new UsuarioDAO().buscaUsuario(respB.getArea().getIdresponsable()));
       
       return respB;
    }   
    
    
   public ResponsableBean obtenResponsablexId(int idresponsablet, int idarea)throws SQLException, NamingException, Exception{
       ResponsableBean respB = new ResponsableBean();
       //Se hizo la modificaci�n para que solo trajera a los responsables 
       respB.setDatos(resDao.obtenResponsablexId(idresponsablet)); 
       
       respB.setArea(new AreaDAO().getArea(idarea));
       respB.setUsuario(new UsuarioDAO().buscaUsuario(respB.getArea().getIdresponsable()));
       
       return respB;
   }
   
   
   
   

   public List<AvanceBean> obtenAvancesResponsableAsunto(int idasunto, int idarea)throws SQLException, NamingException, Exception{
       return avdao.obtenAvancesxResponsablexAsunto(idasunto, idarea);
   }
   
   public void mueveAvancesCancelados(List<AvanceBean> avances, int idRTabla) throws SQLException, NamingException{
       AvanceCanceladoDTO  avanceC = new AvanceCanceladoDTO();
        for(AvanceBean avance:avances){
            avanceC.setIdasunto(avance.getIdasunto());
            avanceC.setIdaccion(avance.getIdaccion());
            avanceC.setIdarea(avance.getIdarea());
            avanceC.setIdavance(avance.getIdavance());
            avanceC.setIdresponsable(idRTabla);
            avanceC.setDescripcion(avance.getDescripcion());
            avanceC.setFecha(avance.getFecha());
            avanceC.setPorcentaje(avance.getPorcentaje());
            //Se guard� en bitacora el movimiento
            //avanceC.setIdusuariomodificacion(idusuario);
            avdao.insertAvanceCancelado(avanceC);
        } 
       
   }
   
   
   public List<AvanceCanceladoDTO> avancesCanceladosxResponsableAsunto(int idRTabla, int idasunto, int idresponsable) throws NamingException, Exception{
      return avdao.obtenAvancesCanceladosxResponsableAsunto(idRTabla, idasunto, idresponsable);
   } 
   
   
   public List<AvanceCanceladoDTO> avancesCanceladosxResponsableAcuerdo(int idacuerdo, int idresponsable) throws NamingException, Exception{
      return avdao.obtenAvancesCanceladosxResponsableAcuerdo(idacuerdo, idresponsable);
   } 
   
   
   public List<AvanceBean> obtenAvancesResponsableAcuerdo(int idacuerdo, int idarea)throws SQLException, NamingException, Exception{
       return avdao.obtenAvancesxResponsablexAcuerdo(idacuerdo, idarea);
   }

   
   public void obtenAvancesxAsunto(int idasunto, int idarea, List<AvanceBean> avances)throws SQLException, NamingException, Exception{
       List<ResponsableBean> subresponsables = this.buscarResponsablesxAsuntoAsignado(idarea, idasunto);
       if (subresponsables != null && !subresponsables.isEmpty()) {
          for (ResponsableBean r:subresponsables){
            obtenAvancesxAsunto (idasunto,r.getArea().getIdarea(),avances);
          }
          // Se agregan los avances del �rea base
          avances.addAll(0, avdao.obtenAvancesxAsunto(idasunto,idarea));
       } else {
          avances.addAll(0, avdao.obtenAvancesxAsunto(idasunto,idarea));
       }            
       
   }
   public List<AvanceBean> avancesTodos(int idasunto) throws NamingException, Exception{
      return avdao.obtenAvancesTodos(idasunto);
   }
   
   public List<AvanceBean> avancesTodosHistorico(int idasunto) throws NamingException, Exception{
      return avdao.obtenAvancesHistorico(idasunto);
   }
   
   public void obtenAvancesxAcuerdo(int idacuerdo, int idarea, List<AvanceBean> avances)throws SQLException, NamingException, Exception{
       List<ResponsableBean> subresponsables = this.buscarResponsablesPorAcuerdoAsignado(idarea, idacuerdo);
       if (subresponsables != null && !subresponsables.isEmpty()) {
          for (ResponsableBean r:subresponsables){
            obtenAvancesxAcuerdo (idacuerdo,r.getArea().getIdarea(),avances);
          }
          // Se agregan los avances del �rea base
          avances.addAll(0, avdao.obtenAvancesxAcuerdo(idacuerdo,idarea));
       } else {
          avances.addAll(0, avdao.obtenAvancesxAcuerdo(idacuerdo,idarea));
       }            
       
   }
   public List<AvanceBean> obtenAvancesxAcuerdoTodos(int idacuerdo) throws NamingException, Exception{
      return avdao.obtenAvancesxAcuerdoTodos(idacuerdo);
   } 
   public boolean grabaAvance(AvanceBean avance,String tipo) throws Exception {
        
        boolean nuevo = false;
        if (avance.getIdavance() < 0) {
            if (tipo.equals("asunto") || tipo.equals("convenio"))  avdao.insertAsunto(avance);
            else avdao.insertAcuerdo(avance);
            avance.setIdavance(avdao.getMaxId());
            nuevo = true;
        } else {
            avdao.update(avance);
        }
        
        
        return nuevo;
    }

   public void insertAsuntoHistorico(AvanceBean avance) throws SQLException, NamingException {
       avdao.insertAsuntoHistorico(avance);
   }
   public List<AccionBean> buscaAcuerdosxReunion(int idasunto) throws Exception{
         
       List<AccionBean> compromiso = accdao.buscarAccionesPorAsunto(idasunto);
       for(AccionBean ac:compromiso){
           ac.setUltimoavance(accdao.ultimoAvanceConvenio(ac.getIdAccion()));
       } 
      return compromiso;
   }
   public List<AccionBean> buscaAcuerdosxConvenio(int idasunto) throws Exception{
       List<AccionBean> compromiso = accdao.buscarAccionesPorAsuntoConv(idasunto);
       for(AccionBean ac:compromiso){
           String avanterior="",avactual="";
           List<AccionBean> avancesanteriores = null;
           List<AccionBean> avancesactuales = accdao.ultimosAvancesConvenio(ac.getIdAccion());
           if(avancesanteriores == null || avancesanteriores.size() == 0) {
               ac.setPenultavance(""); //No se reportaron avances por parte del �rea
           } else {
               for (AccionBean aa:avancesanteriores) {
                   avanterior += aa.getFecha()+" - "+aa.getDescripcion()+"<br>";
               }
               
               ac.setPenultavance(avanterior);
           }
           if(avancesactuales == null || avancesactuales.size() == 0) {
               ac.setUltimoavance("");
           } else {
               for (AccionBean aac:avancesactuales) {
                   avactual += Utiles.getFechaCorta(aac.getFecha())+" - "+aac.getDescripcion()+"<br><br>";
               }
               avactual = avactual.substring(0,avactual.length()-8); // quitar los 2 saltos <br> al final de la cadena.
               ac.setUltimoavance(avactual);
           }           
       } 
      return compromiso;
   }
   
   public List<AccionBean> obtenerAcuerdosAll(FiltroAsunto filtro) throws Exception{
      
      return accdao.buscarAccionesAll(filtro);
   }
   
   public void actualizaAvanceResponsable(ResponsableDTO responsable)throws SQLException, NamingException {
     
     resDao.updateAvanceResponsable(responsable);
   }
   
   public void actualizaEstatusAsunto(AsuntoBean asunto)throws SQLException, NamingException{
      
      adao.updateEstatus(asunto);
   }
   
   public void actualizaEstatusAcuerdo(AccionBean acuerdo)throws SQLException, NamingException{
      accdao.updateEstatus(acuerdo.getIdAccion(), acuerdo.getEstatus());
   }
   
   public int getNextMaxIdAsuntoxTipo(String tipo)throws SQLException, NamingException{
    
     return adao.getMaxIdAsuntoxTipo(tipo)+1;
   }
   
   public int getNextMaxIdAcuerdoxReunion(int idreunion)throws SQLException, NamingException{
     return accdao.getMaxIdAccionxReunion(idreunion)+1;
   }
   
   
   public ResumenAsuntoArea obtenResumenAsuntosArea(int idarea, String tipo, FiltroAsunto filtro) throws Exception {
     
     ResumenAsuntoArea resumen = new ResumenAsuntoArea();
     resumen.setAtendidos(adao.cantidadAsuntosxEstatusxAreaxTipo(tipo, "A", filtro, idarea).getCantidad());
     resumen.setPendientes(adao.cantidadAsuntosxEstatusxAreaxTipo(tipo, "P", filtro, idarea).getCantidad());
     
     resumen.setVencidos(adao.cantidadAsuntosVencidos(tipo, idarea,filtro).getCantidad());
     resumen.setPorvencer(adao.cantidadAsuntosPorVencer(tipo, idarea, filtro).getCantidad());
     return resumen;
   }
   
   public ResumenAsuntoArea obtenResumenAcuerdosArea(int idarea, FiltroAsunto filtro) throws Exception {
    
     ResumenAsuntoArea resumen = new ResumenAsuntoArea();
     filtro.setEstatusAsunto("A");
     resumen.setAtendidos(accdao.cantidadAccionesFiltro(filtro).getCantidad());
     filtro.setEstatusAsunto("P");
     resumen.setPendientes(accdao.cantidadAccionesFiltro(filtro).getCantidad());
     
     resumen.setVencidos(accdao.cantidadAcuerdosVencidosDirectos(idarea, filtro).getCantidad());
     resumen.setPorvencer(accdao.cantidadAcuerdosPorVencerDirectos(idarea, filtro).getCantidad());
     return resumen;
   }
   
   public ResumenArea obtenResumenArea(int idarea, String tipo, FiltroAsunto filtro) throws Exception {
     //filtro.setIdarea(idarea);
     ResumenArea resumen = new ResumenArea();
     //int vencidos=0, porVencer=0,pendAct=0;
     //int vencidosA=0, porVencerA=0,pendActA=0;
     if (!tipo.equals("A")) {
        resumen.setVencidos_d(adao.cantidadAsuntosVencidosDirectos(tipo, idarea, filtro).getCantidad());
        resumen.setPorvencer_d(adao.cantidadAsuntosPorVencerDirectos(tipo, idarea, filtro).getCantidad());
        resumen.setPendactivos_d(adao.cantidadAsuntosPendActivosDirectos(tipo, idarea, filtro).getCantidad());
        resumen.setAtendidos(adao.cantidadAsuntosAtendidos(tipo, idarea, filtro).getCantidad());
     } else {
        resumen.setVencidos_d(accdao.cantidadAcuerdosVencidosDirectos(idarea, filtro).getCantidad());
        resumen.setPorvencer_d(accdao.cantidadAcuerdosPorVencerDirectos(idarea, filtro).getCantidad());
        resumen.setPendactivos_d(accdao.cantidadAcuerdosPendActivosDirectos(idarea, filtro).getCantidad());
        resumen.setAtendidos(accdao.cantidadAcuerdosAtendidos(idarea, filtro).getCantidad());
     }
     //Total por tipo asunto
     resumen.setPendientes(resumen.getVencidos_d()+resumen.getPorvencer_d()+resumen.getPendactivos_d());
     //filtro.setIdarea(0);
     filtro.setEstatusAsunto(null);
     //filtro.setEstatusFechaAtencion("TO");
     return resumen;
   }
   
   
   public ResumenDelegado obtenResumenDelegado(int idarea, String tipo, FiltroAsunto filtro) throws Exception {
     filtro.setIdarea(idarea);
     ResumenDelegado resumen = new ResumenDelegado();
     if (!tipo.equals("A")) {
        resumen.setTotalasuntos(adao.cantidadAsuntosxAreaxTipo(filtro, tipo).getCantidad());
        resumen.setDelegados(adao.cantidadAsuntosDelegadosxAreaxTipo(filtro, tipo).getCantidad());
        
     } else {
        //Acuerdos
         resumen.setTotalasuntos(accdao.cantidadAccionesFiltro(filtro).getCantidad());
         resumen.setDelegados(accdao.cantidadAccionesDelegadasFiltro(filtro).getCantidad());
     }
     filtro.setIdarea(0);
     filtro.setEstatusAsunto(null);
     return resumen;
   }
   
   
    public ResumenAreaSM obtenResumenAreaSM(String tipo, FiltroAsunto filtro) throws Exception {
     ResumenAreaSM resumen = new ResumenAreaSM();
     if (!tipo.equals("A")) {
        filtro.setTipoFecha("envio"); 
        resumen.setEnviados(adao.cantidadAsuntosxAreaxTipo(filtro, tipo).getCantidad());
        filtro.setTipoFecha("atencion"); 
        filtro.setEstatusAsunto("A"); 
        resumen.setAtendidos(adao.cantidadAsuntosxAreaxTipo(filtro, tipo).getCantidad());
        filtro.setEstatusAsunto("P");
        resumen.setPendientes(adao.cantidadAsuntosxAreaxTipo(filtro, tipo).getCantidad());
        
     } else {
        filtro.setTipoFecha("envio"); 
        resumen.setEnviados(accdao.cantidadAccionesFiltro(filtro).getCantidad()); 
        filtro.setTipoFecha("atencion");
        filtro.setEstatusAsunto("A");
        resumen.setAtendidos(accdao.cantidadAccionesFiltro(filtro).getCantidad()); 
        filtro.setEstatusAsunto("P");
        resumen.setPendientes(accdao.cantidadAccionesFiltro(filtro).getCantidad());
     }
     filtro.setEstatusAsunto(null);
     
     return resumen;
   }
   
    
   public boolean existePeriodoCierre(String periodo) throws SQLException, NamingException{
       RegistroConveniosDAO rcdao = new RegistroConveniosDAO();
       RegistroConveniosDTO convenio = rcdao.totalesxPeriodo(periodo);
       return convenio!= null;
   } 
    
   public void cierreConvenios(AreasConsulta areas, String periodo) throws Exception{
       FiltroAsunto filtro = new FiltroAsunto();
       RegistroConveniosDAO rcdao = new RegistroConveniosDAO();
       RegistroConveniosDTO cierreConvenio = new RegistroConveniosDTO();
       
       filtro.setTipoFecha("captura");
       for(AreaDTO area:areas.getAreasResponsables() ){
            cierreConvenio.setIdarea(area.getIdarea());
            cierreConvenio.setFecha(periodo);

            filtro.setIdarea(area.getIdarea());
            filtro.setEstatusAsunto("R"); //tr�mite
            int totalReg = this.cantidadAsuntosPorAreaxTipo(filtro, "V"); 
            cierreConvenio.setTramite(totalReg);
            
            filtro.setEstatusAsunto("O"); //concluido
            totalReg = this.cantidadAsuntosPorAreaxTipo(filtro, "V"); 
            cierreConvenio.setConcluidos(totalReg);
            
            filtro.setEstatusAsunto("V"); //vigente
            totalReg = this.cantidadAsuntosPorAreaxTipo(filtro, "V"); 
            cierreConvenio.setVigentes(totalReg);
            
            filtro.setEstatusAsunto("N"); //cancelados
            totalReg = this.cantidadAsuntosPorAreaxTipo(filtro, "V"); 
            cierreConvenio.setCancelados(totalReg);
            
            rcdao.insertaCierre(cierreConvenio);
        }
   }
   
   public void actualizaInfo(AsuntoBean asunto, String campo)throws SQLException, NamingException {
    
     adao.actualizaInfo(asunto, campo);
   }
   
  
   
   public void grabaBitacora(BitacoraDTO bit) throws Exception{
       BitacoraDAO bDao = new BitacoraDAO();
       bDao.grabaBitacora(bit);
   }

   public void eliminaAvancesxResponsableAsunto(int idarea, int idasunto) throws SQLException, NamingException{
      avdao.eliminaAvancesxResponsableAsunto(idarea, idasunto);
   }
   
   public void eliminaAvancesxResponsableAcuerdo(int idarea, int idacuerdo) throws SQLException, NamingException{
      avdao.eliminaAvancesxResponsableAcuerdo(idarea, idacuerdo);
   }
   
   public void eliminaAvancesAsunto(int idasunto)throws SQLException, NamingException{
        avdao.eliminaAvancesAsunto(idasunto);
   }       
   
   public void eliminaAvancesAcuerdo(int idacuerdo)throws SQLException, NamingException{
        avdao.eliminaAvancesAcuerdo(idacuerdo);
   }       
   
   public void eliminaRespxAsunto(int idasunto)throws SQLException, NamingException{
        resDao.eliminaRespxAsunto(idasunto);
   }
   
   public void eliminaRespxAcuerdo(int idacuerdo)throws SQLException, NamingException{
        resDao.eliminaRespxAcuerdo(idacuerdo);
   }
   
   public void eliminaCorespTodos(int idasunto)throws SQLException, NamingException{
        CorresponsableDAO crdao = new CorresponsableDAO();
        crdao.eliminaCorrespTodos(idasunto);
   }
   public void eliminaAcuerdo(int idacuerdo)throws SQLException, NamingException{
        accdao.eliminaAcuerdo(idacuerdo);
   }
   
   public void eliminaAcuerdosxReunion(int idacuerdo)throws SQLException, NamingException{
        accdao.eliminaAcuerdosxAsunto(idacuerdo);
   }
   
   public List<AnexoDTO> buscaAnexosxAsunto(int idasunto) throws Exception {
       AnexoDAO anexDao = new AnexoDAO();
       List<AnexoDTO> anexo =  anexDao.buscaAnexosxAsunto(idasunto);
       return anexo;
   }
   
   public List<AnexoDTO> buscaAnexosxAcuerdo(int idacuerdo) throws Exception {
       AnexoDAO anexDao = new AnexoDAO();
       List<AnexoDTO> anexo =  anexDao.buscaAnexosxAcuerdo(idacuerdo);
       return anexo;
   }
   
   
   public AvanceBean obtenUltimoAvanceAsunto(int idasunto, int idResponsable)throws SQLException, NamingException, Exception{
       List<AvanceBean> avances = obtenAvancesResponsableAsunto(idasunto, idResponsable);
       AvanceBean ultimoA = null;
       if (!avances.isEmpty() && avances != null){
           ultimoA = avances.get(0);
       }  
       return ultimoA;
   
   }

   public AvanceBean obtenUltimoAvanceAcuerdo(int idacuerdo, int idResponsable)throws SQLException, NamingException, Exception{
       List<AvanceBean> avances = obtenAvancesResponsableAcuerdo(idacuerdo, idResponsable);
       AvanceBean ultimoA = null;
       if (!avances.isEmpty() && avances != null){
           ultimoA = avances.get(0);
       }  
       return ultimoA;
   
   }
   
   public void actualizaEstatusResponsable(String estatus,int idresponsable) throws NamingException, SQLException{
      resDao.actualizaEstatusResponsable(estatus, idresponsable);
   }

   
   public void actualizaResponsable(ResponsableDTO responsable)throws SQLException, NamingException {
      resDao.updateResponsable(responsable);
   }
  
   public void actualizaResponsableAcuerdo(ResponsableDTO responsable)throws SQLException, NamingException {
      resDao.updateResponsableAcuerdo(responsable);
   }
   public List<ProgramacionDTO> obtenReprogramacionesAsunto(int idasunto)throws SQLException, NamingException{
      ProgramacionDAO pdao = new ProgramacionDAO();
      return pdao.obtenProgramacionesxAsunto(idasunto);
   }
   public ProgramacionDTO obtenReprogramacionxId(int idreprograma)throws SQLException, NamingException{
      ProgramacionDAO pdao = new ProgramacionDAO();
      return pdao.obtenProgramacionesxId(idreprograma);
   }   
    public List<ProgramacionDTO> obtenReprogramacionesAcuerdo(int idacuerdo)throws SQLException, NamingException{
      ProgramacionDAO pdao = new ProgramacionDAO();
      return pdao.obtenProgramacionesxAcuerdo(idacuerdo);
   }
    
   public void insertaReprogramacionAsunto(ProgramacionDTO pdto)throws SQLException, NamingException{
       ProgramacionDAO pdao = new ProgramacionDAO();
       pdao.insertaReprogramacionAsunto(pdto);
       adao.updateFechaAtencion(pdto.getIdasunto(), pdto.getFechareprograma());
   }     
   
   public void insertaReprogramacionAcuerdo(ProgramacionDTO pdto)throws SQLException, NamingException{
       ProgramacionDAO pdao = new ProgramacionDAO();
       pdao.insertaReprogramacionAcuerdo(pdto);
       accdao.updateFechaAtencion(pdto.getIdacuerdo(),pdto.getFechareprograma());
   } 
   
   public void cierraAsunto(AnexoAsuntoDTO anexo, FileItem archivo) throws Exception{
      AnexoDAO anexoDAO = new AnexoDAO();
      grabaAnexo(anexo, archivo);
      adao.updateIdArchivoCierre(anexo.getIdAsunto(),anexoDAO.getMaxId());
   }
    public List<GraficaDTO> xasuntoarea(List cvesareas, String fecha1, String fecha2)throws SQLException, NamingException{
      GraficasDAO gdao = new GraficasDAO();
      return gdao.xasuntoarea(cvesareas, fecha1, fecha2);
   }
    public List<GraficaDTO> xasuntoareareunion(List areas, String fecha1, String fecha2)throws SQLException, NamingException{
      GraficasDAO gdao = new GraficasDAO();
      return gdao.xasuntoareareunion(areas, fecha1, fecha2);
   }
    public List<GraficaDTO> xasuntoareaacuerdo(String fecha1, String fecha2)throws SQLException, NamingException{
      GraficasDAO gdao = new GraficasDAO();
      return gdao.xasuntoareaacuerdo(fecha1, fecha2);
   }
    public List<GraficaDTO> xarea(List areas, String fecha1, String fecha2)throws SQLException, NamingException{
      GraficasDAO gdao = new GraficasDAO();
      return gdao.xarea(areas, fecha1, fecha2);
   }
    public List<GraficaDTO> xareaacuerdo(String stat, List areas, String fecha1, String fecha2)throws SQLException, NamingException{
      GraficasDAO gdao = new GraficasDAO();
      return gdao.xareaacuerdo(stat, areas, fecha1, fecha2);
   }
    public List<GraficaDTO> xasuntoareaArea(int idarea, String anio)throws SQLException, NamingException{
      GraficasDAO gdao = new GraficasDAO();
      return gdao.xasuntoareaUna(idarea, anio);
   }
    public List<GraficaDTO> xasuntoareareunionArea(int idarea, String anio)throws SQLException, NamingException{
      GraficasDAO gdao = new GraficasDAO();
      return gdao.xasuntoareareunionUna(idarea, anio);
   }
    public List<GraficaDTO> xasuntoareaacuerdoArea(int idarea, String anio)throws SQLException, NamingException{
      GraficasDAO gdao = new GraficasDAO();
      return gdao.xasuntoareaacuerdoUna(idarea, anio);
   }
    public AreaDTO nombreArea(int idarea)throws SQLException, NamingException{
      AreaDAO gdao = new AreaDAO();
      return gdao.getArea(idarea);
   }
    public List<ReporteXMesDTO> areaMes1(int idarea, String anioMes)throws SQLException, NamingException{
      AreaDAO adao = new AreaDAO();
      return adao.areaAsuntoMes1(idarea, anioMes);
   }
    public List<ReporteXMesDTO> areaMes2(int idarea, String anioMes)throws SQLException, NamingException{
      AreaDAO adao = new AreaDAO();
      return adao.areaAsuntoMes2(idarea, anioMes);
   }
    public List<ReporteXMesDTO> areaMes3(int idarea, String anioMes)throws SQLException, NamingException{
      AreaDAO adao = new AreaDAO();
      return adao.areaAsuntoMes3(idarea, anioMes);
   }
    public ResumenPromediosDTO resumenPromedios(int idarea, String tipoasunto, String estatus, String fec_ini, String fec_fin)throws SQLException, NamingException, Exception{
        AsuntoDAO asunto = new AsuntoDAO(null);
        return asunto.resumenPromedios(idarea, tipoasunto, estatus, fec_ini, fec_fin);
    }
    public ResumenPromediosDTO resumenPromediosAcuerdos(int idarea, String fec_ini, String fec_fin)throws SQLException, NamingException, Exception{
        AsuntoDAO asunto = new AsuntoDAO(null);
        return asunto.resumenPromediosAcuerdos(idarea, fec_ini, fec_fin);
    }
    public List<AsuntoBean> listaActualizaDiasRetraso()throws SQLException, NamingException, Exception{
      AsuntoDAO adao = new AsuntoDAO(null);
      return adao.listaActualizaDiasRetraso();
    }
    public void actualizaRetrasos(int dias, int idasunto) throws Exception{
        AsuntoDAO asunto = new AsuntoDAO(null);
        asunto.actualizaRetrasos(dias, idasunto);
    }
    public List<AccionDTO> listaActualizaDiasRetrasoAcuerdos()throws SQLException, NamingException, Exception{
      AsuntoDAO adao = new AsuntoDAO(null);
      return adao.listaActualizaDiasRetrasoAcuerdos();
    }
    public void actualizaRetrasosAcuerdos(int dias, int idasunto) throws Exception{
        AsuntoDAO asunto = new AsuntoDAO(null);
        asunto.actualizaRetrasosAcuerdos(dias, idasunto);
    }
    
    public ResumenAreaSM resumenSemanalMens(String tipo, String fecha1, String fecha2, int idarea) throws Exception{
        AsuntoDAO asunto = new AsuntoDAO(null);
        return asunto.resumenSemanalMens(tipo, fecha1, fecha2, idarea);
    }
    public ResumenAreaSM resumenSemanalMensAcuerdos(String fecha1, String fecha2, int idarea) throws Exception{
        AsuntoDAO asunto = new AsuntoDAO(null);
        return asunto.resumenSemanalMensAcuerdos(fecha1, fecha2, idarea);
    }
    public ResumenAreaSM resumenSemanalMensReuniones(String fecha2, int idarea) throws Exception{
        AsuntoDAO asunto = new AsuntoDAO(null);
        return asunto.resumenSemanalMensReuniones(fecha2, idarea);
    }
    public ResumenAreaSM resumenSemanalMensTots(String tipo, String fecha1, String fecha2) throws Exception{
        AsuntoDAO asunto = new AsuntoDAO(null);
        return asunto.resumenSemanalMensTots(tipo, fecha1, fecha2);
    }
    public ResumenAreaSM resumenSemanalMensAcuerdosTots(String fecha1, String fecha2) throws Exception{
        AsuntoDAO asunto = new AsuntoDAO(null);
        return asunto.resumenSemanalMensAcuerdosTots(fecha1, fecha2);
    }
    public ResumenAreaSM resumenSemanalMensReunionesTots(String fecha2) throws Exception{
        AsuntoDAO asunto = new AsuntoDAO(null);
        return asunto.resumenSemanalMensReunionesTots(fecha2);
    }
    

    public ResponsableDTO comentarioAtendido(int idasunto, int idareaSup) throws Exception{
        ResponsableDAO comm = new ResponsableDAO(null);
        return comm.buscaComentarioAtendido(idasunto, idareaSup);
    }

    public int obtenAreaSuperior(int idarea, int nivel) throws Exception{
        AreaDAO sup = new AreaDAO();
        return sup.obtenAreaSuperior(idarea, nivel);
    }
    public ResponsableDTO comentarioAtendidoAcuerdos(int idaccion, int idareaSup) throws Exception{
        ResponsableDAO comm = new ResponsableDAO(null);
        return comm.buscaComentarioAtendidoAcuerdos(idaccion, idareaSup);
    }
   
    public void actualizaFechaReprogram(int idasunto) throws Exception{
        ProgramacionDAO repro = new ProgramacionDAO();
        repro.actualizaFechaReprogram(idasunto);
    }
    public void actualizaFechaReprogramAcuerdo(int idacuerdo) throws Exception{
        ProgramacionDAO repro = new ProgramacionDAO();
        repro.actualizaFechaReprogramAcuerdos(idacuerdo);
    }
    public List<AnexoDTO> obtenAnexosAtendido(int idasunto) throws Exception{
        AnexoDAO an = new AnexoDAO();
        return an.obtenAnexosAtendido(idasunto);
    }
    public List<AnexoDTO> obtenAnexosAtendidoAcuerdos(int idaccion) throws Exception{
        AnexoDAO an = new AnexoDAO();
        return an.obtenAnexosAtendidoAcuerdos(idaccion);
    }
    public List<AreaDTO> obtenDlgs(int idasunto, int nivel) throws Exception{
        ResponsableDAO resp = new ResponsableDAO(null);
        return resp.obtenDlgs(idasunto, nivel);
    }
    public List<AreaDTO> obtenDlgsAcuerdo(int idasunto, int nivel) throws Exception{
        ResponsableDAO resp = new ResponsableDAO(null);
        return resp.obtenDlgsAcuerdo(idasunto, nivel);
    }    
    public int obtenNivelNextAcuerdo(int idaccion, int idarea) throws Exception{
        AreaDAO sup = new AreaDAO();
        return sup.obtenNivelNextAcuerdo(idaccion, idarea);
    }
    public int obtenNivelNext(int idasunto, int idarea) throws Exception{
        AreaDAO sup = new AreaDAO();
        return sup.obtenNivelNext(idasunto, idarea);
    }
    public int obtenIdAccion(int idasunto, int idarea) throws Exception{
        AreaDAO sup = new AreaDAO();
        return sup.obtenNivelNext(idasunto, idarea);
    }
    public List<CategoriaDTO> listadoCategoria() throws Exception{
        CatalogosDAO cat = new CatalogosDAO();
        return cat.listadoCategoria();
    }
    public int obtenPctjXasuntoArea(int idasunto, int idarea) throws Exception{
        AvanceDAO pct = new AvanceDAO(null);
        return pct.pctjesRegistradosXAsuntoArea(idasunto, idarea);
    }
//////////////
    public void updateResponsableConvenio(ResponsableBean resp) throws Exception{
        resDao.updateResponsableConvenio(resp);
    }
    public List<CategoriaDTO> listadoInstitucion() throws Exception{
        CatalogosDAO cat = new CatalogosDAO();
        return cat.listadoInstituciones();
    }
    public boolean existeRespCompromiso(int idarea, int idaccion) throws Exception{
        ResponsableDAO resp = new ResponsableDAO(null);
        return resp.existeRespCompromiso(idarea, idaccion);
    }
    public String buscaEstatusAccion(int idaccion) throws Exception{
        AccionDAO estat = new AccionDAO(null);
        return estat.buscaEstatusAccion(idaccion);
    }
    public List<UsuarioDTO> usuariosNivel2() throws Exception{
        UsuarioDAO nivel2 = new UsuarioDAO();
        return nivel2.obtenUsuariosNivel2();
    }
    public List<AvanceBean> obtenAvancesConvenio(int idasunto, int idarea) throws Exception{
        AvanceDAO av = new AvanceDAO(null);
        return av.obtenAvancesxAsunto(idasunto, idarea);
    }
    public void cambiaEstatusAcuerdo(int idacuerdo, int idresponsable) throws Exception{
        accdao.cambiaEstatus(idacuerdo, idresponsable);
    }
    public void eliminaRespxAsuntoResp(int idasunto, int idarea) throws Exception{
        ResponsableDAO resp = new ResponsableDAO(null);
        resp.eliminaRespxAsuntoResp(idasunto, idarea);
    }
   
    public ResponsableDTO obtenDlgEliminar(int idareaSuperior, int idasunto) throws SQLException, NamingException{
        ResponsableDTO resp = resDao.obtenDlgEliminar(idareaSuperior, idasunto);
        return resp;
    }

    public List<ActividadesSeguimientoDTO> listaActividadesSeguim(int idasunto, int idarea) throws Exception{
        ActividadesSeguimientoDAO actividades = new ActividadesSeguimientoDAO();
        return actividades.listaAct(idasunto, idarea);
    }
    public List<ActividadesSeguimientoDTO> listaActividadesSeguimAcuerdos(int idaccion, int idarea) throws Exception{
        ActividadesSeguimientoDAO actividades = new ActividadesSeguimientoDAO();
        return actividades.listaActAcuerdos(idaccion, idarea);
    }
    public List<AnexoDTO> listaAnexosActividadesSeguim(int idactividad) throws Exception{
        ActividadesSeguimientoDAO actividades = new ActividadesSeguimientoDAO();
        return actividades.listaAnexos(idactividad);
    }
    public void insertaActividadesSeguim(ActividadesSeguimientoDTO registro) throws Exception{
        ActividadesSeguimientoDAO actividades = new ActividadesSeguimientoDAO();
        actividades.insertActividades(registro);
    }
    public void updateActividadesSeguim(ActividadesSeguimientoDTO registro) throws Exception{
        ActividadesSeguimientoDAO actividades = new ActividadesSeguimientoDAO();
        actividades.updateActividades(registro);
    }
    public void insertaActividadesSeguimAcuerdo(ActividadesSeguimientoDTO registro) throws Exception{
        ActividadesSeguimientoDAO actividades = new ActividadesSeguimientoDAO();
        actividades.insertActividadesAcuerdo(registro);
    }
    public void insertaAnexosActividadesSeguim(AnexoAsuntoDTO anexo) throws Exception{
        ActividadesSeguimientoDAO actividades = new ActividadesSeguimientoDAO();
        actividades.insertAnexoActividades(anexo);
    }
    public int nextIdactividad() throws Exception{
        ActividadesSeguimientoDAO actividades = new ActividadesSeguimientoDAO();
        return actividades.nextIdactividad();
    }
    public void eliminaActividadSeguim(int idactividad) throws Exception{
        ActividadesSeguimientoDAO actividades = new ActividadesSeguimientoDAO();
        actividades.eliminaActividades(idactividad);
    }
    public void eliminaAnexoActividadSeguim(int idanexo) throws Exception{
        ActividadesSeguimientoDAO actividades = new ActividadesSeguimientoDAO();
        actividades.eliminaAnexo(idanexo);
    }
    public ActividadesSeguimientoDTO actividadById(int idactividad) throws Exception{
        ActividadesSeguimientoDAO actividades = new ActividadesSeguimientoDAO();
        return actividades.actById(idactividad);
    }
    public List<AreaDTO> responsablesNivel2(int idasunto, String tipoAs) throws Exception{
        AreaDAO area = new AreaDAO();
        return area.areasResponsablesNivel2(idasunto, tipoAs);
    }
    public List<ResumenAreaSM> obtenResumenInicio(String fecha1, String fecha2, List areas) throws SQLException, NamingException{
        ResponsableDAO resp = new ResponsableDAO(null);
        return resp.obtenResumenInicio(fecha1, fecha2, areas);
    }
    public List<ResumenAreaSM> obtenResumenAcInicio(String fecha1, String fecha2, List areas) throws SQLException, NamingException{
        ResponsableDAO resp = new ResponsableDAO(null);
        return resp.obtenResumenAcInicio(fecha1, fecha2, areas);
    }
    public ResumenAreaSM obtenResumenIni(String fecha1, String fecha2) throws SQLException, NamingException{
        ResponsableDAO resp = new ResponsableDAO(null);
        return resp.obtenResumenIni(fecha1, fecha2);
    }
    public void cambiaEstatusAsunto(int idasunto) throws Exception{
        adao.cambiaEstatus(idasunto);
    }
    public int idAreaByPermiso(int idpermiso) throws Exception{
        PermisoDAO permisos = new PermisoDAO();
        return permisos.idAreaByPermiso(idpermiso);
    }    
}
