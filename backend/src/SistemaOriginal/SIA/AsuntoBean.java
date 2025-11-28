/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AnexoAsuntoDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AsuntoDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.TareaDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.UsuarioDTO;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AreaBean;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.ResponsableBean;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.UsuarioBean;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.Utiles;

/**
 *
 * @author jacqueline
 */
public class AsuntoBean extends AsuntoDTO implements Serializable {
    
    //private boolean asuntoDebeMostrarse;
    //private boolean responsableEdicion;    
    private AreaBean responsable;
    private UsuarioBean usuarioModificacion;
    private List<AreaBean> corresponsables;
    private List<AnexoAsuntoDTO> anexos;
    private List<AccionBean> acciones;
    private List<TareaDTO> accionesInstrucciones;
    private int noInstrucciones;
    private int accionesRealizadas;
    private int accionesPorRealizar;
    //Nuevo
    private List<ResponsableBean> responsables;
    private boolean descripcionLarga;
    private AnexoAsuntoDTO archivoCierre;
    private String fechaUltimaReprogramacion;
    private String ultimoavance;
    private String penultavance;
    private int rowSpanReporte;
    private List<Integer> posDelete;
    private UsuarioBean remitente;
    
    public AsuntoBean(){
        this.posDelete = new ArrayList<Integer>();
    }

     /**
     * @return the responsable
     */
    public AreaBean getResponsable() {
        return responsable;
    }

    /**
     * @param responsable the responsable to set
     */
    public void setResponsable(AreaBean responsable) {
        this.responsable = responsable;
    }

    /**
     * @return the corresponsables
     */
    public List<AreaBean> getCorresponsables() {
        return corresponsables;
    }

    /**
     * @param corresponsables the corresponsables to set
     */
    public void setCorresponsables(List<AreaBean> corresponsables) {
        this.corresponsables = corresponsables;
    }

    /**
     * @return the anexos
     */
    public List<AnexoAsuntoDTO> getAnexos() {
        return anexos;
    }

    /**
     * @param anexos the anexos to set
     */
    public void setAnexos(List<AnexoAsuntoDTO> anexos) {
        this.anexos = anexos;
    }

    /**
     * @return the acciones
     */
    public List<AccionBean> getAcciones() {
        return acciones;
    }

    /**
     * @param acciones the acciones to set
     */
    public void setAcciones(List<AccionBean> acciones) {
        this.acciones = acciones;
    }

    /**
     * @return the accionesRealizadas
     */
    public int getAccionesRealizadas() {
        return accionesRealizadas;
    }

    /**
     * @param accionesRealizadas the accionesRealizadas to set
     */
    public void setAccionesRealizadas(int accionesRealizadas) {
        this.accionesRealizadas = accionesRealizadas;
    }

    /**
     * @return the accionesPorRealizar
     */
    public int getAccionesPorRealizar() {
        return accionesPorRealizar;
    }

    /**
     * @param accionesPorRealizar the accionesPorRealizar to set
     */
    public void setAccionesPorRealizar(int accionesPorRealizar) {
        this.accionesPorRealizar = accionesPorRealizar;
    }

    /**
     * @return the usuarioModificacion
     */
    public UsuarioBean getUsuarioModificacion() {
        return usuarioModificacion;
    }

    /**
     * @param usuarioModificacion the usuarioModificacion to set
     */
    public void setUsuarioModificacion(UsuarioBean usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }
    


    //////////////////////////////////////////////////////////////////////////
    //ESPECIALES
    ////////////////////////////////////////////////////////////////////////
    
    public String getDescripcionFormatoHTML() {
        return Utiles.parrafeaHTML(super.getDescripcion().trim());
    }
    
    public String getObservacionesFormatoHTML() {
        if(super.getObservaciones() != null && super.getObservaciones().trim() != null) return Utiles.parrafeaHTML(super.getObservaciones().trim()); 
        else return super.getObservaciones();
    }

    public boolean isPublicoEstatus() {
        return super.getPublicoestatus().equals("S");
    }
    
    public boolean isActivoEstatus() {
        return super.getPublicoestatus().equals("S");
    }
    
//    public boolean isAdministrador() {
//        return super.getAdministrador().equals("S");
//    }

    
    public String getCondicionActualFormatoHTML() {
        /*if (this.condicionActual != null) {
            return Utiles.parrafeaHTML(this.condicionActual.trim());
        } else {
            return "";
        }*/
        return "";
    }
    
    public boolean isTieneInstrucciones() {
        boolean resultado = false;
        if (this.noInstrucciones > 0) {
            resultado = true;
        }
        return resultado;
    }

    public boolean isTieneTareas() {
        boolean resultado = false;
        if (this.getAccionesInstrucciones().size() > 0) {
            resultado = true;
        }
        return resultado;
    }

    
    public boolean isResumenLargo(){
        String finEstatusTexto = "";
        if (super.getEstatustexto().length() > 3 ){
           finEstatusTexto = super.getEstatustexto().substring(super.getEstatustexto().length()-3);
        }

        return finEstatusTexto.equals("...")  ? true:false;

    }

   

    /*private boolean asuntoDebeMostrarse;
    private String resumenAcciones;
    private boolean isResumenLargo*/

    
    /**
     * @return the asuntoDebeMostrarse
     */
    /*public boolean isAsuntoDebeMostrarse() {
        return asuntoDebeMostrarse;
    }*/

    /**
     * @param asuntoDebeMostrarse the asuntoDebeMostrarse to set
     */
    /*public void setAsuntoDebeMostrarse(boolean asuntoDebeMostrarse) {
        this.asuntoDebeMostrarse = asuntoDebeMostrarse;
    }*/

    /**
     * @return the accionesInstrucciones
     */
    public List<TareaDTO> getAccionesInstrucciones() {
        return accionesInstrucciones;
    }

    /**
     * @param accionesInstrucciones the accionesInstrucciones to set
     */
    public void setAccionesInstrucciones(List<TareaDTO> accionesInstrucciones) {
        this.accionesInstrucciones = accionesInstrucciones;
    }
    
    public int getNoAcuerdos(){
        int noacc=0;
        if (this.getAcciones() != null){
            noacc = this.getAcciones().size();
        }
        return noacc;
    }
    


    public String getFechaingresoFormatoTexto() {
        String result = "";
        if (super.getFechaingreso() != null && !super.getFechaingreso().trim().equals("")) {
           result =  Utiles.getFechaCorta(super.getFechaingreso().trim());
        }   
        return result;   
    }
    
    public String getFechaatenderFormatoTexto() {
        String result = "";
        if (super.getFechaatender() != null && !super.getFechaatender().trim().equals("")) {
           result = Utiles.getFechaCorta(super.getFechaatender().trim());
        }   
        return result;   
    }
    public String getFechaatendertexto() {
        String result = "";
        if (super.getFechaatendertexto()!= null && !super.getFechaatendertexto().trim().equals("")) {
           result = Utiles.getFechaCorta(super.getFechaatendertexto().trim());
        }   
        return result;   
    }    
    public String getFechaoriginalFormatoTexto() {
        String result = "";
        if (super.getFechaoriginal() != null && !super.getFechaoriginal().trim().equals("")) {
           result = Utiles.getFechaCorta(super.getFechaoriginal().trim());
        }   
        return result;   
    }
    
    public String getFechadescargaFormatoTexto() {
        String result = "";
        if (super.getFechadescarga() != null && !super.getFechaoriginal().trim().equals("")) {
          result = Utiles.getFechaCorta(super.getFechadescarga().trim());
        }  
        return result;
    }
    
    public String getFechacierreFormatoTexto() {
        String result = "";
        if (super.getFechadescarga() != null) {
          result = Utiles.getFechaCorta(this.getArchivoCierre().getFechacarga().trim());
        }  
        return result;
    }
    
    
    public boolean yaExisteCorresponsable(int idACorresponsable){
      boolean bandera = false;
     
      if (this.corresponsables != null){
          for (AreaBean ab:this.corresponsables){
             if (ab.getDatos().getIdarea() == idACorresponsable){
                bandera = true;
                break;
             }
          }
      } 
      return bandera;

    }
    
    public boolean yaExisteResponsable(int idResponsable){
      boolean bandera = false;
      
      if (this.responsables != null){
          for (ResponsableBean rb:this.responsables){
             if (rb.getArea().getIdarea() == idResponsable){
                bandera = true;
                break;
             }
          }
      }
      return bandera;
    }
 
    public void agregaResponsable(ResponsableBean responsable){
       if (this.getResponsables() == null ){
          this.responsables = new ArrayList<ResponsableBean>();
       }
       this.responsables.add(responsable);        
    }
    
    public void agregacorresponsable(AreaBean corresponsable){
       if (this.getCorresponsables() == null ){
          this.corresponsables = new ArrayList<AreaBean>();
       }
       this.corresponsables.add(corresponsable);
    }
    
    
    public boolean eliminaResponsable(int idResponsable, int posicion){
       boolean bandera = false;
       int pos = 0;
       if (this.responsables != null){
          for (ResponsableBean rb:this.responsables){
             if ((rb.getArea().getIdarea() == idResponsable) && (posicion == pos)){
                bandera = true;
                this.responsables.remove(rb);
                break;
             }
             pos++;
          }
       }
       
       return bandera; 
       
       
    }
    
    public boolean eliminaCorresponsable(int idCorresponsable){
       boolean bandera = false;
       
       if (this.corresponsables != null){
          for (AreaBean corr:this.corresponsables){
             if (corr.getDatos().getIdarea() == idCorresponsable){
                bandera = true;
                this.corresponsables.remove(corr);
                break;
             }
          }
       }
       
       return bandera; 
    }

   public boolean eliminaAnexo(int idanexo){
       boolean bandera = false;
       
       if (this.anexos != null){
          for (AnexoAsuntoDTO anexo:this.getAnexos()){
             if (anexo.getIdanexo() == idanexo){
                bandera = true;
                this.anexos.remove(anexo);
                break;
             }
          }
       }
       
       return bandera; 
    }

    
    public String getEstatusTextoFormatoHTML() {
        if (super.getEstatustexto() != null) {
            return Utiles.parrafeaHTML(super.getEstatustexto().trim());
        } else {
            return "";
        }
    }

   
    public String getAsistentesFormatoHTML() {
        if (super.getAsistentes() != null) {
           return Utiles.parrafeaHTML(super.getAsistentes().trim());  
        } else {
           return "";
        }
        
        /*String asisteHtml = "";
        if (super.getAsistentes() != null) {
          String[] Vasistentes = super.getAsistentes().split(",");
          if (Vasistentes.length > 0) {
            for (String a:Vasistentes){
                asisteHtml =  asisteHtml+a+"<br>";
            }
          } else {
             return Utiles.parrafeaHTML(super.getAsistentes().trim());
          }  
        }*/
        
        
    }

    /**
     * @return the responsables
     */
    public List<ResponsableBean> getResponsables() {
        return responsables;
    }

    /**
     * @param responsables the responsables to set
     */
    public void setResponsables(List<ResponsableBean> responsables) {
        this.responsables = responsables;
    }

       
    /**
     * @return the noResponsables
     */
    public int getNoResponsables() {
        return this.responsables.size();
    }

    /**
     * @return the noCorresponsables
     */
    public int getNoCorresponsables() {
        return this.corresponsables.size();
    }

       
    public int getEstatusAsunto(){
        int estatus = 0;
        String hoy = Utiles.getFechaHoy();
        //String fecha2antes = Utiles.getFechamenosXDias(this.getFechaatender(),2);
        //String fecha1antes = Utiles.getFechamenosXDias(this.getFechaatender(),1);
        String maniana=Utiles.getFechaManiana();
        //String pasadomaniana=Utiles.getFechamasXDias(hoy, 2);
        String pasadomaniana=Utiles.getFechamasXDias(Utiles.getFechaHoraJava(), 2);
        //if (hoy.equals(maniana) || hoy.equals(pasadomaniana)){
//        if(this.getIdconsecutivo() == 8150) {
//            System.out.println("this");
//        }
        if ( Integer.parseInt(maniana) >= Integer.parseInt(this.getFechaatender().trim()) || Integer.parseInt(pasadomaniana) >= Integer.parseInt(this.getFechaatender().trim())){
           estatus = 1; //En amarillo Por Vencer
        } 
        if (hoy.equals(this.getFechaatender().trim()) || hoy.compareTo(this.getFechaatender().trim()) > 0 ){
           estatus = 2; // en Rojo Vencidos
        }
        return estatus;
    }
    
    public String getNoControl_No(){
      String[] nocontrolArray = this.getNocontrol().split("/");
      String no = "";
      if (nocontrolArray.length > 0 ){
          no = nocontrolArray[0];
      } 
      return no;
    }
    
    public String getNoControl_Anio(){
      String[] nocontrolArray = this.getNocontrol().split("/");
      String anio = "";
      if (nocontrolArray.length > 1 ){
          anio = nocontrolArray[1];
      } 
      return anio;
    }
    
    public boolean getReprogramado(){
      boolean bandera = false;
      if (super.getReprograma()!= null && super.getReprograma().equals("S")) bandera = true;
      return bandera;
    }
    
    public String getDescripcionCorta(){
      String corta = this.getDescripcion();
      if (this.getDescripcion().length() > 50) {
         corta = this.getDescripcion().substring(0, 50)+"...";
      }
      return corta;
    }

    /**
     * @return the descripcionLarga
     */
    public boolean isDescripcionLarga() {
        return this.getDescripcion().length() > 50;
    }

    /**
     * @return the archivoCierre
     */
    public AnexoAsuntoDTO getArchivoCierre() {
        return archivoCierre;
    }

    /**
     * @param archivoCierre the archivoCierre to set
     */
    public void setArchivoCierre(AnexoAsuntoDTO archivoCierre) {
        this.archivoCierre = archivoCierre;
    }

    /**
     * @return the fechaUltimaReprogramacion
     */
    public String getFechaUltimaReprogramacion() {
        return fechaUltimaReprogramacion;
    }

    /**
     * @param fechaUltimaReprogramacion the fechaUltimaReprogramacion to set
     */
    public void setFechaUltimaReprogramacion(String fechaUltimaReprogramacion) {
        this.fechaUltimaReprogramacion = fechaUltimaReprogramacion;
    }
   
    
   /*public boolean isUrgente(){
      return this.getUrgente().equals("S");
    }*/

    /**
     * @param rowSpanReporte the rowSpanReporte to set
     */
    public void setRowSpanReporte(int rowSpanReporte) {
        this.rowSpanReporte = rowSpanReporte;
    }

    /**
     * @return the rowSpanReporte
     */
    public int getRowSpanReporte() {
        return rowSpanReporte;
    }
   
    public int obtenRowSpan(){
        int rowSpan=0;       
        if (this.getAcciones() != null){
           for (AccionBean acuerdo:this.getAcciones()){
              rowSpan+=acuerdo.getNoResponsables();
           }
        }
        return rowSpan;
    }        

    

    /**
     * @return the ultimoavance
     */
    public String getUltimoavance() {
        return ultimoavance;
    }

    /**
     * @param ultimoavance the ultimoavance to set
     */
    public void setUltimoavance(String ultimoavance) {
        this.ultimoavance = ultimoavance;
    }

    /**
     * @return the penultavance
     */
    public String getPenultavance() {
        return penultavance;
    }

    /**
     * @param penultavance the penultavance to set
     */
    public void setPenultavance(String penultavance) {
        this.penultavance = penultavance;
    }

    /**
     * @return the posDelete
     */
    public List<Integer> getPosDelete() {
        return posDelete;
    }

    /**
     * @param posDelete the posDelete to set
     */
    public void setPosDelete(List<Integer> posDelete) {
        this.posDelete = posDelete;
    }

    /**
     * @return the remitente
     */
    public UsuarioBean getRemitente() {
        return remitente;
    }

    /**
     * @param remitente the remitente to set
     */
    public void setRemitente(UsuarioBean remitente) {
        this.remitente = remitente;
    }
    
}
