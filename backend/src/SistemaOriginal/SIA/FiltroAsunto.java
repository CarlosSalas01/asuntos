/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.Utiles;
import mx.org.inegi.dggma.sistemas.asuntos.tags.ElementoSuperSelect;

/**
 *
 * @author jacqueline
 */
public class FiltroAsunto implements Serializable {

    private String tipoFecha;
    private String fechaInicio;
    private String fechaFinal;
    private String estatusAsunto;
    private String estatusFechaAtencion;
    private int idarea;
    private String fechaInicioFormat;
    private String fechaFinalFormat;
    private String clasifica;
    private String presidencia;
    private String texto;
    private String id;
    private String id2;
    private String urgente;
    private String estatusResp;
    private String estatusReunion;
    private int offset;
    private String tiporeunion;
    private boolean limitAll;
    private int idareaDelegada;
    private String modalidadReunion;
    
    private String mesPenultimo;
    private String anioPenultimo;
    private String mesUltimo;
    private String anioUltimo;
    private String fechaManana;
    private String porcentajeAvance;
    private String fechaIniAnioAct;
    private String rol;
    private int idAreaSel;

    public FiltroAsunto(){
        this.setEstatusFechaAtencion("TO");
        this.setEstatusReunion("TO");
        this.setEstatusAsunto("T");
        this.setClasifica("T");
        this.setPresidencia("T");
        this.setTiporeunion("T");
        this.setFechaInicioFormat("01/01/2010");
        this.setFechaFinalFormat(Utiles.getFechaDMA());
        this.setFechaInicio(Utiles.getSwapFecha(this.fechaInicioFormat));
        this.setFechaFinal(Utiles.getSwapFecha(this.fechaFinalFormat));
        this.setFechaManana(Utiles.getFechaManiana());
    }
    /** 
     * @return the tipoFecha
     */
    public String getTipoFecha() {
        return tipoFecha;
    }

    /**
     * @param tipoFecha the tipoFecha to set
     */
    public void setTipoFecha(String tipoFecha) {
        this.tipoFecha = tipoFecha;
    }

    /**
     * @return the fechaInicio
     */
    public String getFechaInicio() {
        return fechaInicio;
    }

    /**
     * @param fechaInicio the fechaInicio to set
     */
    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * @return the fechaFinal
     */
    public String getFechaFinal() {
        return fechaFinal;
    }

    /**
     * @param fechaFinal the fechaFinal to set
     */
    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    /**
     * @return the estatusAsunto
     */
    public String getEstatusAsunto() {
        return estatusAsunto;
    }

    /**
     * @param estatusAsunto the estatusAsunto to set
     */
    public void setEstatusAsunto(String estatusAsunto) {
        this.estatusAsunto = estatusAsunto;
    }

    /**
     * @return the estatusFechaAtencion
     */
    public String getEstatusFechaAtencion() {
        return estatusFechaAtencion;
    }

    /**
     * @param estatusFechaAtencion the estatusFechaAtencion to set
     */
    public void setEstatusFechaAtencion(String estatusFechaAtencion) {
        this.estatusFechaAtencion = estatusFechaAtencion;
    }

    /**
     * @return the idarea
     */
    public int getIdarea() {
        return idarea;
    }

    /**
     * @param idarea the idarea to set
     */
    public void setIdarea(int idarea) {
        this.idarea = idarea;
    }

    /**
     * @return the fechaInicioFormat
     */
    public String getFechaInicioFormat() {
        return fechaInicioFormat;
    }

    /**
     * @param fechaInicioFormat the fechaInicioFormat to set
     */
    public void setFechaInicioFormat(String fechaInicioFormat) {
        this.fechaInicioFormat = fechaInicioFormat;
    }

    /**
     * @return the fechaFinalFormat
     */
    public String getFechaFinalFormat() {
        return fechaFinalFormat;
    }

    /**
     * @param fechaFinalFormat the fechaFinalFormat to set
     */
    public void setFechaFinalFormat(String fechaFinalFormat) {
        this.fechaFinalFormat = fechaFinalFormat;
    }

    /**
     * @return the clasifica
     */
    public String getClasifica() {
        return clasifica;
    }

    /**
     * @param clasifica the clasifica to set
     */
    public void setClasifica(String clasifica) {
        this.clasifica = clasifica;
    }

    /**
     * @return the presidencia
     */
    public String getPresidencia() {
        return presidencia;
    }

    /**
     * @param presidencia the presidencia to set
     */
    public void setPresidencia(String presidencia) {
        this.presidencia = presidencia;
    }

    /**
     * @return the texto
     */
    public String getTexto() {
        return texto;
    }

    /**
     * @param texto the texto to set
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

   
    /**
     * @return the urgente
     */
    public String getUrgente() {
        return urgente;
    }

    /**
     * @param urgente the urgente to set
     */
    public void setUrgente(String urgente) {
        this.urgente = urgente;
    }

    public List<ElementoSuperSelect> getLestatusAsunto(){
      List<ElementoSuperSelect> lista = new ArrayList<ElementoSuperSelect>();
      
      String[][] datos = {{"T","Todos"},{"P","Pendientes"},{"A","Atendidos"}};
      for (int i=0; i< datos.length;i++){
          ElementoSuperSelect element = new ElementoSuperSelect();
          element.setIndice(datos[i][0]);
          element.setContenido(datos[i][1]);
          lista.add(element);
      }
      
      return lista;
      
    }
    
    public List<ElementoSuperSelect> getLestatusConvenios(){
      List<ElementoSuperSelect> lista = new ArrayList<ElementoSuperSelect>();
      
      String[][] datos = {{"T","Todos"},{"R","Trámite"},{"O","Concluído"},{"V","Vigente"},{"N","Cancelado"}};
      for (int i=0; i< datos.length;i++){
          ElementoSuperSelect element = new ElementoSuperSelect();
          element.setIndice(datos[i][0]);
          element.setContenido(datos[i][1]);
          lista.add(element);
      }
      
      return lista;
      
    }
    
     public List<ElementoSuperSelect> getLestatusResponsable(){
      List<ElementoSuperSelect> lista = new ArrayList<ElementoSuperSelect>();
      
      String[][] datos = {{"T","Todos"},{"P","Pendientes"},{"A","Atendidos"}};
      for (int i=0; i< datos.length;i++){
          ElementoSuperSelect element = new ElementoSuperSelect();
          element.setIndice(datos[i][0]);
          element.setContenido(datos[i][1]);
          lista.add(element);
      }
      
      return lista;
      
    }
     
    public List<ElementoSuperSelect> getLtipoConvenio(){
      List<ElementoSuperSelect> lista = new ArrayList<ElementoSuperSelect>();
      
      String[][] datos = {{"T","Todos"},{"1","Específico"},{"2","General"},{"3","Marco"},{"4","Internacional"}};
      for (int i=0; i< datos.length;i++){
          ElementoSuperSelect element = new ElementoSuperSelect();
          element.setIndice(datos[i][0]);
          element.setContenido(datos[i][1]);
          lista.add(element);
      }
      
      return lista;
      
    }
    
    public List<ElementoSuperSelect> getLclasificacion(){
      List<ElementoSuperSelect> lista = new ArrayList<ElementoSuperSelect>();
      
      String[][] datos = {{"T","Todos"},{"I","Interno"},{"E","Externo"}};
      for (int i=0; i< datos.length;i++){
          ElementoSuperSelect element = new ElementoSuperSelect();
          element.setIndice(datos[i][0]);
          element.setContenido(datos[i][1]);
          lista.add(element);
      }
      
      return lista;
      
    }
    
    
    public List<ElementoSuperSelect> getLpresidencia(){
      List<ElementoSuperSelect> lista = new ArrayList<ElementoSuperSelect>();
      
      String[][] datos = {{"T","Todos"},{"P","Presidencia C/Resp."},{"N","Presidencia S/Resp."}};
      for (int i=0; i< datos.length;i++){
          ElementoSuperSelect element = new ElementoSuperSelect();
          element.setIndice(datos[i][0]);
          element.setContenido(datos[i][1]);
          lista.add(element);
      }
      
      return lista;
      
    }
    
     public List<ElementoSuperSelect> getLurgentes(){
      List<ElementoSuperSelect> lista = new ArrayList<ElementoSuperSelect>();
      
      String[][] datos = {{"T","Todos"},{"1","Prioridad 1"},{"2","Atención normal"},{"A","Administrativo"},{"S","Urgente"},{"N","Normal"}};
      for (int i=0; i< datos.length;i++){
          ElementoSuperSelect element = new ElementoSuperSelect();
          element.setIndice(datos[i][0]);
          element.setContenido(datos[i][1]);
          lista.add(element);
      }
      
      return lista;
     }
     
     public List<ElementoSuperSelect> getLfechas(){
        List<ElementoSuperSelect> lista = new ArrayList<ElementoSuperSelect>();

        String[][] datos = {{"envio","Envío"},{"vencimiento","Vencimiento"},{"atencion","Atención"},{"captura","Captura"},{"asignado","Asignado"}};
        for (int i=0; i< datos.length;i++){
            ElementoSuperSelect element = new ElementoSuperSelect();
            element.setIndice(datos[i][0]);
            element.setContenido(datos[i][1]);
            lista.add(element);
        }

        return lista;
     }
             
     public List<ElementoSuperSelect> getLfechasConvenio(){
        List<ElementoSuperSelect> lista = new ArrayList<ElementoSuperSelect>();

        String[][] datos = {{"captura","Alta"},{"envio","Firma"},{"vencimiento","Vigencia"}};
        for (int i=0; i< datos.length;i++){
            ElementoSuperSelect element = new ElementoSuperSelect();
            element.setIndice(datos[i][0]);
            element.setContenido(datos[i][1]);
            lista.add(element);
        }

        return lista;
     }
     
     public List<ElementoSuperSelect> getLmeses(){
        List<ElementoSuperSelect> lista = new ArrayList<ElementoSuperSelect>();

        String[][] datos = {{"0","Enero"},{"1","Febrero"},{"2","Marzo"},{"3","Abril"},{"4","Mayo"},{"5","Junio"},{"6","Julio"},{"7","Agosto"},{"8","Septiembre"},{"9","Octubre"},{"10","Noviembre"},{"11","Diciembre"}};
        for (int i=0; i< datos.length;i++){
            ElementoSuperSelect element = new ElementoSuperSelect();
            element.setIndice(datos[i][0]);
            element.setContenido(datos[i][1]);
            lista.add(element);
        }

        return lista;
     }        
             
    public List<ElementoSuperSelect> getLanios(){
        List<ElementoSuperSelect> lista = new ArrayList<ElementoSuperSelect>();

        String[][] datos = {{"2015","2015"},{"2016","2016"},{"2017","2017"}};
        for (int i=0; i< datos.length;i++){
            ElementoSuperSelect element = new ElementoSuperSelect();
            element.setIndice(datos[i][0]);
            element.setContenido(datos[i][1]);
            lista.add(element);
        }

        return lista;
     }         

    /**
     * @return the estatusResp
     */
    public String getEstatusResp() {
        return estatusResp;
    }

    /**
     * @param estatusResp the estatusResp to set
     */
    public void setEstatusResp(String estatusResp) {
        this.estatusResp = estatusResp;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * @param offset the offset to set
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * @return the estatusReunion
     */
    public String getEstatusReunion() {
        return estatusReunion;
    }

    /**
     * @param estatusReunion the estatusReunion to set
     */
    public void setEstatusReunion(String estatusReunion) {
        this.estatusReunion = estatusReunion;
    }

    /**
     * @return the id2
     */
    public String getId2() {
        return id2;
    }

    /**
     * @param id2 the id2 to set
     */
    public void setId2(String id2) {
        this.id2 = id2;
    }

    /**
     * @return the tiporeunion
     */
    public String getTiporeunion() {
        return tiporeunion;
    }

    /**
     * @param tiporeunion the tiporeunion to set
     */
    public void setTiporeunion(String tiporeunion) {
        this.tiporeunion = tiporeunion;
    }

    /**
     * @return the limitAll
     */
    public boolean isLimitAll() {
        return limitAll;
    }

    /**
     * @param limitAll the limitAll to set
     */
    public void setLimitAll(boolean limitAll) {
        this.limitAll = limitAll;
    }

    /**
     * @return the idareaDelegada
     */
    public int getIdareaDelegada() {
        return idareaDelegada;
    }

    /**
     * @param idareaDelegada the idareaDelegada to set
     */
    public void setIdareaDelegada(int idareaDelegada) {
        this.idareaDelegada = idareaDelegada;
    }

    /**
     * @return the modalidadReunion
     */
    public String getModalidadReunion() {
        return modalidadReunion;
    }

    /**
     * @param modalidadReunion the modalidadReunion to set
     */
    public void setModalidadReunion(String modalidadReunion) {
        this.modalidadReunion = modalidadReunion;
    }

    /**
     * @return the fechaAvanceInicio
     */
    public String getFechaAvancePenultimoIni() {
        String fechaini = "01/"+ Utiles.dosDigitos(Integer.parseInt(this.getMesPenultimo())+1)+"/"+this.getAnioPenultimo();
        return Utiles.getSwapFecha(fechaini);
    }


    /**
     * @return the fechaAvanceFinal
     */
    public String getFechaAvancePenultimoFin() {
        String fechafin = "31/"+ Utiles.dosDigitos(Integer.parseInt(this.getMesPenultimo())+1)+"/"+this.getAnioPenultimo();
        return Utiles.getSwapFecha(fechafin);

    }

    /**
     * @return the fechaAvanceInicio
     */
    public String getFechaAvanceUltimoIni() {
        String fechaini = "01/"+ Utiles.dosDigitos(Integer.parseInt(this.getMesUltimo())+1)+"/"+this.getAnioUltimo();
        return Utiles.getSwapFecha(fechaini);
    }


    /**
     * @return the fechaAvanceFinal
     */
    public String getFechaAvanceUltimoFin() {
        String fechafin = "31/"+ Utiles.dosDigitos(Integer.parseInt(this.getMesUltimo())+1)+"/"+this.getAnioUltimo();
        return Utiles.getSwapFecha(fechafin);

    }
    
    /**
     * @return the avanceEncabezadoPenultimo
     */
    public String getAvanceEncabezadoPenultimo() {
        return Utiles.regresaMes(Integer.parseInt(this.mesPenultimo))+" / "+(Integer.parseInt(this.anioPenultimo));
    }

        /**
     * @return the avanceEncabezadoUltimo
     */
    public String getAvanceEncabezadoUltimo() {
        return Utiles.regresaMes(Integer.parseInt(this.mesUltimo))+" / "+(Integer.parseInt(this.anioUltimo));
    }

        
    /**
     * @return the mesPenultimo
     */
    public String getMesPenultimo() {
        return mesPenultimo;
    }

    /**
     * @param mesPenultimo the mesPenultimo to set
     */
    public void setMesPenultimo(String mesPenultimo) {
        this.mesPenultimo = mesPenultimo;
    }

    /**
     * @return the anioPenultimo
     */
    public String getAnioPenultimo() {
        return anioPenultimo;
    }

    /**
     * @param anioPenultimo the anioPenultimo to set
     */
    public void setAnioPenultimo(String anioPenultimo) {
        this.anioPenultimo = anioPenultimo;
    }

    /**
     * @return the mesUltimo
     */
    public String getMesUltimo() {
        return mesUltimo;
    }

    /**
     * @param mesUltimo the mesUltimo to set
     */
    public void setMesUltimo(String mesUltimo) {
        this.mesUltimo = mesUltimo;
    }

    /**
     * @return the anioUltimo
     */
    public String getAnioUltimo() {
        return anioUltimo;
    }

    /**
     * @param anioUltimo the anioUltimo to set
     */
    public void setAnioUltimo(String anioUltimo) {
        this.anioUltimo = anioUltimo;
    }

    /**
     * @return the fechaManana
     */
    public String getFechaManana() {
        return fechaManana;
    }

    /**
     * @param fechaManana the fechaManana to set
     */
    public void setFechaManana(String fechaManana) {
        this.fechaManana = fechaManana;
    }

    /**
     * @return the porcentajeAvance
     */
    public String getPorcentajeAvance() {
        return porcentajeAvance;
    }

    /**
     * @param porcentajeAvance the porcentajeAvance to set
     */
    public void setPorcentajeAvance(String porcentajeAvance) {
        this.porcentajeAvance = porcentajeAvance;
    }
    /**
     * @return the rol
     */
    public String getRol() {
        return rol;
    }

    /**
     * @param rol the rol to set
     */
    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * @return the fechaIniAnioAct
     */
    public String getFechaIniAnioAct() {
        return fechaIniAnioAct;
    }

    /**
     * @param fechaIniAnioAct the fechaIniAnioAct to set
     */
    public void setFechaIniAnioAct(String fechaIniAnioAct) {
        this.fechaIniAnioAct = fechaIniAnioAct;
    }
    /**
     * @return the idAreaSel
     */
    public int getIdAreaSel() {
        return idAreaSel;
    }

    /**
     * @param idAreaSel the idAreaSel to set
     */
    public void setIdAreaSel(int idAreaSel) {
        this.idAreaSel = idAreaSel;
    }

}
