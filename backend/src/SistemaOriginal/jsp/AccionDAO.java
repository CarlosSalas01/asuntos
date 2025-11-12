/*
 * AsuntoDAO.java
 *
 * Created on 4 de marzo de 2006, 04:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.baseDatos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import javax.sql.DataSource;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AccionDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AreaDTO;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AccionBean;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AsuntoBean;
import mx.org.inegi.dggma.sistemas.asuntos.dto.Cantidad;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaDAO;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.Utiles;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaUsuarioArea;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author Jos Luis Mondragn
 */
public class AccionDAO {
    private List<AreaDTO> areasConsulta;
    private AreasConsulta areasConsultaCompleto;
    /** Creates a new instance of AsuntoDAO */
    
    public AccionDAO(AreasConsulta areas) {
        if (areas != null) {
            this.areasConsulta = areas.getAreasConsulta();
            this.areasConsultaCompleto = areas;
        }   
    }
    
    public int getMaxIdAccion () throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select max(idaccion) as cantidad from controlasuntospendientesnew.accion";
        
        ResultSetHandler rsh = new BeanHandler(Cantidad.class);
        Cantidad cantidad = (Cantidad) qr.query(sql, rsh);
        return cantidad.getCantidad();
    }
    
    public int getMaxIdAccionxReunion (int idreunion) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select max(idconsecutivo) as cantidad from controlasuntospendientesnew.accion where idasunto = ?";
        Object[] params = {idreunion};
    
        ResultSetHandler rsh = new BeanHandler(Cantidad.class);
        Cantidad cantidad = (Cantidad) qr.query(sql, rsh, params);
        return cantidad.getCantidad();
    }
    
     public AccionBean findByPrimaryKeySingle(int pk) throws SQLException,  NamingException, Exception{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select * from controlasuntospendientesnew.accion " +
                "where idaccion=? ";
        Object[] params = {pk};
        ResultSetHandler rsh = new BeanHandler(AccionBean.class);
        AccionBean dato = (AccionBean) qr.query(sql, rsh, params);
        return dato;
    
    }
    
    
    public AccionBean findByPrimaryKey(int pk) throws SQLException,  NamingException, Exception{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select *,substring(fecha,7,2)||'/'||substring(fecha,5,2)||'/'||substring(fecha,1,4) as fechaFormat,substring(acuerdo_fecha,7,2)||'/'||substring(acuerdo_fecha,5,2)||'/'||substring(acuerdo_fecha,1,4) as acuerdo_fecha_formato from controlasuntospendientesnew.accion " +
                "where idaccion=? ";
        
        Object[] params = {pk};
        
        ResultSetHandler rsh = new BeanHandler(AccionBean.class);
        AccionBean dato = (AccionBean) qr.query(sql, rsh, params);
        complementaDatos(dato,0);
        return dato;
    }

    public void insert(AccionBean accion) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        String sql = "insert into controlasuntospendientesnew.accion " +
                "(descripcion,fecha,realizada,activoestatus,idasunto,fechacaptura,estatus,acuerdo_fecha,idconsecutivo,idusuariomodificacion,fechaoriginal, prioridad) " +
                "values (?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] params = {accion.getDescripcion(),
                           accion.getFecha(),
                           accion.getRealizada(),
                           accion.getActivoestatus(),
                           accion.getIdAsunto(),
                           accion.getFechacaptura(),
                           accion.getEstatus(),
                           accion.getAcuerdo_fecha(),
                           accion.getIdconsecutivo(),
                           accion.getIdusuariomodificacion(),
                           accion.getAcuerdo_fecha(),
                           accion.getPrioridad()
        };
        qr.update(sql,params);
    }

    public void update(AccionBean accion) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
       String modifica="acuerdo_fecha=?, ";
       if(accion.getFechaUltimaReprogramacion() != null) modifica="";
            String sql = "update controlasuntospendientesnew.accion set " +
                    "descripcion=?, fecha=?, realizada=?, activoestatus=?, idasunto=?, " +
                    "fechacaptura=?, estatus=?, "+modifica+" idusuariomodificacion = ?, reprograma = ?, fechaoriginal = ?, prioridad = ? "+
                     "where idaccion=?";
            if(accion.getFechaUltimaReprogramacion() != null){
                Object[] params = {accion.getDescripcion(),
                                  accion.getFecha(),
                                  accion.getRealizada(),
                                  accion.getActivoestatus(),
                                  accion.getIdAsunto(),
                                  accion.getFechacaptura(),
                                  accion.getEstatus(),
                                  //accion.getAcuerdo_fecha(), no se modifica esta fecha si tiene reprogramaci�n
                                  //accion.getIdconsecutivo(),
                                  accion.getIdusuariomodificacion(),
                                  accion.getReprograma(),
                                  accion.getAcuerdo_fecha(),
                                  accion.getPrioridad(),
                                  accion.getIdAccion()
                                  };
                qr.update(sql,params);
            } else {
                Object[] params = {accion.getDescripcion(),
                                  accion.getFecha(),
                                  accion.getRealizada(),
                                  accion.getActivoestatus(),
                                  accion.getIdAsunto(),
                                  accion.getFechacaptura(),
                                  accion.getEstatus(),
                                  accion.getAcuerdo_fecha(),
                                  accion.getIdusuariomodificacion(),
                                  accion.getReprograma(),
                                  accion.getAcuerdo_fecha(),
                                  accion.getPrioridad(),
                                  accion.getIdAccion()
                                  };
                qr.update(sql,params);
            }

    }

    public void updateReprogramacion(AccionBean accion) throws NamingException, SQLException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
            String sql = "update controlasuntospendientesnew.accion set " +
                    " acuerdo_fecha= ?, reprograma = ?"+
                    " where idaccion=? ";
         Object[] params = {accion.getAcuerdo_fecha(),
                           accion.getReprograma(),
                           accion.getIdAccion()
                           };
         qr.update(sql,params);
    }
  

    public void delete(int pk) throws Exception {
        //Se elimina directamente en bd pero se 
        //Agregar a bitacora tmb la eliminaci�n
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "delete from controlasuntospendientesnew.accion " +
                     "where idaccion=?";
        Object[] params = {pk};
                
        qr.update(sql,params);
        
    }

 public List<AccionBean> buscarAccionesAll(FiltroAsunto filtro) throws Exception {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
       SQLFiltro filtrosql = new SQLFiltro();
       //se incorpor� la condici�n final para no considerar a los compromisos de convenios
       filtrosql.setSql("select * from controlasuntospendientesnew.accion where (activoestatus <> 'CNV' or activoestatus is null ) "); // and substring(fecha,1,7)='2018091'
       filtrosql.setParametros(new ArrayList());
       construyeFiltroSQL(filtro,filtrosql);


       String sql = filtrosql.getSql() + " and idaccion in ("
           + "select idaccion from controlasuntospendientesnew.responsable where  ";

       if (filtro.getIdarea() > 0){
            if (filtro.getIdareaDelegada() > 0) {
                sql = sql + " idarea = ?  ";
                filtrosql.getParametros().add(filtro.getIdareaDelegada());
                if (filtro.getEstatusResp() != null && !filtro.getEstatusResp().equals("T")) {
                    sql = sql + " and estatus = ?  ";
                    filtrosql.getParametros().add(filtro.getEstatusResp());
                }
                sql+= " and asignadopor = ? and estatus <> 'C' ";
                filtrosql.getParametros().add(filtro.getIdarea());
           } else {
                sql = sql + " idarea = ?  ";
                filtrosql.getParametros().add(filtro.getIdarea());
                if (filtro.getEstatusResp() != null && !filtro.getEstatusResp().equals("T")) {
                    sql = sql + " and estatus = ?  ";
                    filtrosql.getParametros().add(filtro.getEstatusResp());
                }
            }    
       }  else {
            if (filtro.getEstatusResp() != null && !filtro.getEstatusResp().equals("T")) {
                 sql+="  (";
                 for(AreaDTO area:areasConsulta){
                     sql = sql + "( idarea = ? and estatus = ? ) or ";
                     filtrosql.getParametros().add(area.getIdarea());
                     filtrosql.getParametros().add(filtro.getEstatusResp());
                 }
                 sql = sql.substring(0, sql.length()-3) + ") ";
            } else {    
                 sql+=" (";
                 for(AreaDTO area:areasConsulta){
                     sql = sql + " idarea = ? or ";
                     filtrosql.getParametros().add(area.getIdarea());
                 }
                 sql = sql.substring(0, sql.length()-3) + ") ";
            }     
       }
       
       if (filtro.getTipoFecha() != null && filtro.getTipoFecha().equals("atencion")) {
            sql+= " and ( substring(fechaatencion,1,8) >= ? and substring(fechaatencion,1,8) <= ?) ";
            filtrosql.getParametros().add(filtro.getFechaInicio());
            filtrosql.getParametros().add(filtro.getFechaFinal());
       }
       
       if (filtro.getTipoFecha() != null && filtro.getTipoFecha().equals("asignado")) {
            sql+= " and ( substring(fechaasignado,1,8) >= ? and substring(fechaasignado,1,8) <= ?) ";
            filtrosql.getParametros().add(filtro.getFechaInicio());
            filtrosql.getParametros().add(filtro.getFechaFinal());
       }
       
       if (filtro.getPorcentajeAvance() != null && !filtro.getPorcentajeAvance().equals("")){
            sql+= " and ( avance = ?) ";
            filtrosql.getParametros().add(Integer.parseInt(filtro.getPorcentajeAvance()));
       }
       
       sql += ") order by idasunto desc, idconsecutivo asc ";
       if (!filtro.isLimitAll()) {
           sql+="LIMIT 50 OFFSET ? ";
           filtrosql.getParametros().add(filtro.getOffset());
       }
                
        ResultSetHandler rsh = new BeanListHandler(AccionBean.class);
        List<AccionBean> datos = (List<AccionBean>) qr.query(sql, rsh, filtrosql.getParametros().toArray());
        
        complementaDatos(datos,0);
        return datos;
    }
    
     

    public int cantidadAcciones(AsuntoBean asunto) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select count(*) as cantidad from controlasuntospendientesnew.accion " +
                     "where idasunto=? ";
        
        Object[] params = {asunto.getIdasunto()};
              
        ResultSetHandler rsh = new BeanHandler(Cantidad.class);
        Cantidad dato = (Cantidad) qr.query(sql, rsh, params);
        
        return dato.getCantidad();

    }      
      
      
    public Cantidad cantidadAccionesFiltro(FiltroAsunto filtro) throws Exception {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
       SQLFiltro filtrosql = new SQLFiltro();
       filtrosql.setSql("select count(*) as cantidad from controlasuntospendientesnew.accion where (activoestatus <> 'CNV' or activoestatus is null ) ");
       filtrosql.setParametros(new ArrayList());
       construyeFiltroSQL(filtro,filtrosql);
                
       String sql = filtrosql.getSql() + " and idaccion in ("
           + "select idaccion from controlasuntospendientesnew.responsable where ";

             
       if (filtro.getIdarea() > 0){
            if (filtro.getIdareaDelegada() > 0) {
                sql = sql + " idarea = ?  ";
                filtrosql.getParametros().add(filtro.getIdareaDelegada());
                if (filtro.getEstatusResp() != null && !filtro.getEstatusResp().equals("T")) {
                    sql = sql + " and estatus = ?  ";
                    filtrosql.getParametros().add(filtro.getEstatusResp());
                }
                sql+= " and asignadopor = ? and estatus <> 'C' ";
                filtrosql.getParametros().add(filtro.getIdarea());
           } else {
                sql = sql + " idarea = ?  ";
                filtrosql.getParametros().add(filtro.getIdarea());
                if (filtro.getEstatusResp() != null && !filtro.getEstatusResp().equals("T")) {
                    sql = sql + " and estatus = ?  ";
                    filtrosql.getParametros().add(filtro.getEstatusResp());
                }
            }
       }  else {
            if (filtro.getEstatusResp() != null && !filtro.getEstatusResp().equals("T")) {
                 sql+="  (";
                 for(AreaDTO area:areasConsulta){
                     sql = sql + "( idarea = ? and estatus = ? ) or ";
                     filtrosql.getParametros().add(area.getIdarea());
                     filtrosql.getParametros().add(filtro.getEstatusResp());
                 }
                 sql = sql.substring(0, sql.length()-3) + ") ";
            } else {    
                 sql+=" (";
                 for(AreaDTO area:areasConsulta){
                     sql = sql + " idarea = ? or ";
                     filtrosql.getParametros().add(area.getIdarea());
                 }
                 sql = sql.substring(0, sql.length()-3) + ") ";
            }     
       }
       
       if (filtro.getTipoFecha() != null && filtro.getTipoFecha().equals("atencion")) {
            sql+= " and ( substring(fechaatencion,1,8) >= ? and substring(fechaatencion,1,8) <= ?) ";
            filtrosql.getParametros().add(filtro.getFechaInicio());
            filtrosql.getParametros().add(filtro.getFechaManana());
       }
       
       if (filtro.getTipoFecha() != null && filtro.getTipoFecha().equals("asignado")) {
            sql+= " and ( substring(fechaasignado,1,8) >= ? and substring(fechaasignado,1,8) <= ?) ";
            filtrosql.getParametros().add(filtro.getFechaInicio());
            filtrosql.getParametros().add(filtro.getFechaManana());
       }

       
       sql += ") ";
        
                
        ResultSetHandler rsh = new BeanHandler(Cantidad.class);
        Cantidad dato = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
        
        return dato;
    }
    
    
       public Cantidad cantidadAccionesDelegadasFiltro(FiltroAsunto filtro) throws Exception {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
       SQLFiltro filtrosql = new SQLFiltro();
       filtrosql.setSql("select count(*) as cantidad from controlasuntospendientesnew.accion where 1 = 1 ");
       filtrosql.setParametros(new ArrayList());
       construyeFiltroSQL(filtro,filtrosql);
                
       String sql = filtrosql.getSql() + " and idaccion in ("
           + "select idaccion from controlasuntospendientesnew.responsable where ";

             
       if (filtro.getIdarea() > 0){
           sql = sql + " asignadopor = ?  ";
           filtrosql.getParametros().add(filtro.getIdarea());
           if (filtro.getEstatusResp() != null && !filtro.getEstatusResp().equals("T")) {
               sql = sql + " and estatus = ?  ";
               filtrosql.getParametros().add(filtro.getEstatusResp());
           }
       } 
       
       if (filtro.getTipoFecha() != null && filtro.getTipoFecha().equals("atencion")) {
            sql+= " and ( substring(fechaatencion,1,8) >= ? and substring(fechaatencion,1,8) <= ?) ";
            filtrosql.getParametros().add(filtro.getFechaInicio());
            filtrosql.getParametros().add(filtro.getFechaFinal());
       }
       
       if (filtro.getTipoFecha() != null && filtro.getTipoFecha().equals("asignado")) {
            sql+= " and ( substring(fechaasignado,1,8) >= ? and substring(fechaasignado,1,8) <= ?) ";
            filtrosql.getParametros().add(filtro.getFechaInicio());
            filtrosql.getParametros().add(filtro.getFechaFinal());
       }

       
       sql += ") ";
        
                
        ResultSetHandler rsh = new BeanHandler(Cantidad.class);
        Cantidad dato = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
        
        return dato;
    }
    
    
   
    public void construyeFiltroSQL(FiltroAsunto filtro,SQLFiltro filtrosql){
       String campoFecha = "";
      
       String sql = filtrosql.getSql();
           
       if (filtro.getEstatusAsunto() != null && !filtro.getEstatusAsunto().equals("T")){
           sql += " and estatus = ?  ";
           filtrosql.getParametros().add(filtro.getEstatusAsunto());
       }
       
       if (filtro.getClasifica() != null && !filtro.getClasifica().equals("T")){
           sql+= " and realizada = ? ";
           filtrosql.getParametros().add(filtro.getClasifica());
       }
       
       if (filtro.getTexto() != null && !filtro.getTexto().trim().equals("")) {
         sql += " and ( upper(descripcion) like ? )";
         filtrosql.getParametros().add("%"+filtro.getTexto().toUpperCase().trim()+"%");
                  
       }
       
       if (filtro.getId() !=  null){
          try {
            int val = Integer.parseInt(filtro.getId().trim());
            sql +=  " and idconsecutivo = ?  ";
            filtrosql.getParametros().add(val);                  
          } catch (Exception ex){
            //Hubo un error por lo que no se agrega como num�rico  
          }
       }
    

       if (filtro.getId2() !=  null){
          try {
            int val = Integer.parseInt(filtro.getId2().trim());  
            AsuntoDAO adao = new AsuntoDAO(areasConsultaCompleto);
            AsuntoBean asunto = adao.asuntoxTipoxIdConsecutivo("R", val);
            sql +=  " and  idasunto = ?  ";
            if (asunto != null) {
               filtrosql.getParametros().add(asunto.getIdasunto());                  
            } else {
               filtrosql.getParametros().add(0);                  
            }
            
          } catch (Exception ex){
            //Hubo un error por lo que no se agrega como num�rico  
          }
       }
    

       if (filtro.getTipoFecha() != null && (filtro.getTipoFecha().equals("vencimiento") || filtro.getTipoFecha().equals("captura")  || filtro.getTipoFecha().equals("envio"))){
            if (filtro.getTipoFecha().equals("envio")) {
                campoFecha = "fecha";
            }
            if (filtro.getTipoFecha().equals("vencimiento")) {
                campoFecha = "acuerdo_fecha";
            }
            if (filtro.getTipoFecha().equals("captura")) {
                campoFecha = "fechacaptura";
            }
            sql+= " and ( substring("+campoFecha +",1,8) >= ? and substring("+campoFecha+",1,8) <= ?)  ";
            filtrosql.getParametros().add(filtro.getFechaInicio());
            filtrosql.getParametros().add(filtro.getFechaFinal());
       }
       
        //////////////////////////////////////
       ////Condici�n agregada para un reporte de Osvaldo, es algo excepcional
       /*sql+= " and  (  fecha <= ?) ";
       filtrosql.getParametros().add("201412319999");*/
       ///////////////////////
       
       //Obten los VENCIDOS
       String hoy=Utiles.getFechaHoy();
       String maniana=Utiles.getFechaManianaLunes();
       String pasadomaniana=Utiles.getFechamasXDias(Utiles.getFechaHoraJava(), 2);
       String pasadomanianaMas1=Utiles.getFechamasXDias(Utiles.getFechaHoraJava(), 3);
       if (filtro.getEstatusFechaAtencion() != null) {
        if (filtro.getEstatusFechaAtencion().equals("VE") ){
            sql+= " and ( substring(acuerdo_fecha,1,8) <= ?) and estatus = 'P' ";
            filtrosql.getParametros().add(hoy);
        }
        if (filtro.getEstatusFechaAtencion().equals("AV") ){
            sql+= " and ( substring(acuerdo_fecha,1,8) = ? or substring(acuerdo_fecha,1,8) = ?) and estatus = 'P' ";
            filtrosql.getParametros().add(maniana);
            filtrosql.getParametros().add(pasadomaniana);
        }
        if (filtro.getEstatusFechaAtencion().equals("AC") ){
            sql+= " and ( substring(acuerdo_fecha,1,8) >= ?) and estatus = 'P' ";
            filtrosql.getParametros().add(pasadomanianaMas1);
        }
       }
       
       filtrosql.setSql(sql);

    }
    
    public Cantidad cantidadAcuerdosVencidos (int idarea, FiltroAsunto filtro) throws SQLException,  NamingException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
      
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.accion where acuerdo_fecha <  ? and estatus = 'P' ";
       
       SQLFiltro filtrosql = new SQLFiltro();
       filtrosql.setSql(sql);
       filtrosql.setParametros(new ArrayList());
       filtrosql.getParametros().add(Utiles.getFechaHoy());
       construyeFiltroSQL(filtro,filtrosql);

       sql = filtrosql.getSql();
       sql+= " and  idaccion in ("
                        + "select idaccion from controlasuntospendientesnew.responsable where idarea = ? ";
       filtrosql.getParametros().add(idarea);
       
       if (filtro.getTipoFecha() != null && filtro.getTipoFecha().equals("atencion")) {
          
            sql+= " and ( substring(acuerdo_fecha,1,8) >= ? and substring(acuerdo_fecha,1,8) <= ?) ";
            filtrosql.getParametros().add(filtro.getFechaInicio());
            filtrosql.getParametros().add(filtro.getFechaFinal());
       }
       
       sql += ") ";
       
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad dato = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
       return dato; 
    }
    
    public Cantidad cantidadAcuerdosVencidosCom (int idarea, FiltroAsunto filtro) throws SQLException,  NamingException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
      
       //String sql = "select count(*) as cantidad from controlasuntospendientesnew.accion where acuerdo_fecha <  ? and estatus = 'P' and (activoestatus <> 'CNV' or activoestatus is null ) ";
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.accion where (acuerdo_fecha < ? or acuerdo_fecha <=  ? ) and estatus = 'P' and (activoestatus <> 'CNV' or activoestatus is null)";
       SQLFiltro filtrosql = new SQLFiltro();
       filtrosql.setSql(sql);
       filtrosql.setParametros(new ArrayList());
       filtrosql.getParametros().add(Utiles.getFechaHoy());
       filtrosql.getParametros().add(Utiles.getFechaManiana());
       
       construyeFiltroSQL(filtro,filtrosql);

       sql = filtrosql.getSql();
       sql+= " and  idaccion in ("
                        + "select idaccion from controlasuntospendientesnew.responsable where idarea = ?  and estatus = 'A'";
       filtrosql.getParametros().add(idarea);
       sql += ") ";
       
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad dato = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
       return dato; 
    }
    
    
    public Cantidad cantidadAcuerdosVencidosDirectos (int idarea, FiltroAsunto filtro) throws SQLException,  NamingException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
      
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.accion where  estatus = 'P' and (activoestatus <> 'CNV' or activoestatus is null ) ";
       
       SQLFiltro filtrosql = new SQLFiltro();
       filtro.setEstatusFechaAtencion("VE");
       filtrosql.setSql(sql);
       filtrosql.setParametros(new ArrayList());
       construyeFiltroSQL(filtro,filtrosql);

       sql = filtrosql.getSql();
       sql+= " and  idaccion in ("
                        + "select idaccion from controlasuntospendientesnew.responsable where idarea = ? and estatus = 'P' ";
       filtrosql.getParametros().add(idarea);
       
       if (filtro.getTipoFecha() != null && filtro.getTipoFecha().equals("atencion")) {
          
            sql+= " and ( substring(acuerdo_fecha,1,8) >= ? and substring(acuerdo_fecha,1,8) <= ?) ";
            filtrosql.getParametros().add(filtro.getFechaInicio());
            filtrosql.getParametros().add(filtro.getFechaFinal());
       }
       
       sql += ") ";
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad dato = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
       filtro.setEstatusFechaAtencion("AC");
       return dato; 
    }
    
    public Cantidad cantidadAcuerdosPorVencer(int idarea, FiltroAsunto filtro) throws SQLException,  NamingException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
      
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.accion where (acuerdo_fecha = ? or acuerdo_fecha = ?) and estatus = 'P' ";
       
       SQLFiltro filtrosql = new SQLFiltro();
       filtrosql.setSql(sql);
       filtrosql.setParametros(new ArrayList());
       filtrosql.getParametros().add(Utiles.getFechaHoy());
       filtrosql.getParametros().add(Utiles.getFechaManiana());
       construyeFiltroSQL(filtro,filtrosql);

       sql = filtrosql.getSql();
       sql+= " and  idaccion in ("
                        + "select idaccion from controlasuntospendientesnew.responsable where idarea = ? ";
       filtrosql.getParametros().add(idarea);
       
       if (filtro.getTipoFecha() != null && filtro.getTipoFecha().equals("atencion")) {
          
            sql+= " and ( substring(acuerdo_fecha,1,8) >= ? and substring(acuerdo_fecha,1,8) <= ?) ";
            filtrosql.getParametros().add(filtro.getFechaInicio());
            filtrosql.getParametros().add(filtro.getFechaFinal());
       }
       
       sql += ") ";
       
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad dato = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
       return dato; 
    }
    
    public Cantidad cantidadAcuerdosPorVencerCom(int idarea, FiltroAsunto filtro) throws SQLException,  NamingException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
      
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.accion where (acuerdo_fecha = ? or acuerdo_fecha = ?) and estatus = 'P' and (activoestatus <> 'CNV' or activoestatus is null ) ";
       
       SQLFiltro filtrosql = new SQLFiltro();
       filtrosql.setSql(sql);
       filtrosql.setParametros(new ArrayList());
       filtrosql.getParametros().add(Utiles.getFechaHoy());
       filtrosql.getParametros().add(Utiles.getFechaManiana());
       construyeFiltroSQL(filtro,filtrosql);

       sql = filtrosql.getSql();
       sql+= " and  idaccion in ("
                        + "select idaccion from controlasuntospendientesnew.responsable where idarea = ? and estatus = 'A' ";
       filtrosql.getParametros().add(idarea);
       
       
       sql += ") ";
       
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad dato = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
       return dato; 
    }
    
    public Cantidad cantidadAcuerdosPendActivosComp(int idarea, FiltroAsunto filtro) throws SQLException,  NamingException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
      
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.accion where acuerdo_fecha > ? and estatus = 'P' and (activoestatus <> 'CNV' or activoestatus is null ) ";
       
       SQLFiltro filtrosql = new SQLFiltro();
       filtrosql.setSql(sql);
       filtrosql.setParametros(new ArrayList());
       filtrosql.getParametros().add(Utiles.getFechaManiana());
       construyeFiltroSQL(filtro,filtrosql);

       sql = filtrosql.getSql();
       sql+= " and  idaccion in ("
                        + "select idaccion from controlasuntospendientesnew.responsable where idarea = ? and estatus = 'A' ";
       filtrosql.getParametros().add(idarea);
       
       
       sql += ") ";
       
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad dato = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
       return dato; 
    }
    
    public Cantidad cantidadAcuerdosPendActivosDirectos(int idarea, FiltroAsunto filtro) throws SQLException,  NamingException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
      
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.accion where estatus = 'P' and (activoestatus <> 'CNV' or activoestatus is null ) ";
       
       filtro.setEstatusFechaAtencion("AC");
       SQLFiltro filtrosql = new SQLFiltro();
       filtrosql.setSql(sql);
       filtrosql.setParametros(new ArrayList());
       construyeFiltroSQL(filtro,filtrosql);

       sql = filtrosql.getSql();
       sql+= " and  idaccion in ("
                        + "select idaccion from controlasuntospendientesnew.responsable where idarea = ? and estatus = 'P' ";
       filtrosql.getParametros().add(idarea);
       
       if (filtro.getTipoFecha() != null && filtro.getTipoFecha().equals("atencin")) {
          
           sql+= " and ( substring(acuerdo_fecha,1,8) >= ? and substring(acuerdo_fecha,1,8) <= ?) ";
            filtrosql.getParametros().add(filtro.getFechaInicio());
            filtrosql.getParametros().add(filtro.getFechaFinal());
       }
       
       sql += ") ";
       
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad dato = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
       return dato; 
    }
    public Cantidad cantidadAcuerdosAtendidos(int idarea, FiltroAsunto filtro) throws SQLException,  NamingException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       String sql = "select count(1) as cantidad from controlasuntospendientesnew.accion where (substring(acuerdo_fecha,1,8) >= ? and substring(acuerdo_fecha,1,8) <= ? ) " +
            "and (activoestatus <> 'CNV' or activoestatus is null ) " +
            "and idaccion in (select idaccion from controlasuntospendientesnew.responsable where idarea = ? and estatus = 'A')  ";
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad dato = (Cantidad) qr.query(sql, rsh, filtro.getFechaIniAnioAct(), filtro.getFechaFinal(), idarea);
       return dato; 
    }
    
    public Cantidad cantidadAcuerdosPorVencerDirectos(int idarea, FiltroAsunto filtro) throws SQLException,  NamingException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
      
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.accion where estatus = 'P' and (activoestatus <> 'CNV' or activoestatus is null ) ";
       
       SQLFiltro filtrosql = new SQLFiltro();
       filtro.setEstatusFechaAtencion("AV");
       filtrosql.setSql(sql);
       filtrosql.setParametros(new ArrayList());
       construyeFiltroSQL(filtro,filtrosql);

       sql = filtrosql.getSql();
       sql+= " and  idaccion in ("
                        + "select idaccion from controlasuntospendientesnew.responsable where idarea = ? and estatus = 'P' ";
       filtrosql.getParametros().add(idarea);
       
       if (filtro.getTipoFecha() != null && filtro.getTipoFecha().equals("atencion")) {
          
           sql+= " and ( substring(acuerdo_fecha,1,8) >= ? and substring(acuerdo_fecha,1,8) <= ?) ";
            filtrosql.getParametros().add(filtro.getFechaInicio());
            filtrosql.getParametros().add(filtro.getFechaFinal());
       }
       
       sql += ") ";
       
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad dato = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
       filtro.setEstatusFechaAtencion("AC");
       return dato; 
    }

   
    
    public List<AccionBean> buscarAccionesPorAsunto(AsuntoBean asunto) throws Exception {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select * from controlasuntospendientesnew.accion " +
                "where idasunto=? " +
                "order by idconsecutivo asc ";
        
        Object[] params = {asunto.getIdasunto()};
        
        ResultSetHandler rsh = new BeanListHandler(AccionBean.class);
        List<AccionBean> datos = (List<AccionBean>) qr.query(sql, rsh, params);
        
        complementaDatos(datos,0);
        return datos;
    }

    
  public List<AccionBean> buscarAccionesPorAsunto(int idasunto) throws Exception {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select * from controlasuntospendientesnew.accion " +
                "where idasunto=? " +
                "order by fecha asc ";
        
        Object[] params = {idasunto};
        
        ResultSetHandler rsh = new BeanListHandler(AccionBean.class);
        List<AccionBean> datos = (List<AccionBean>) qr.query(sql, rsh, params);
        
        complementaDatos(datos,0);
        return datos;
    }
  public List<AccionBean> buscarAccionesPorAsuntoConv(int idasunto) throws Exception {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select *, CASE WHEN descripcion is null THEN '' ELSE descripcion END from controlasuntospendientesnew.accion " +
                "where idasunto=? order by idaccion asc ";
        
        Object[] params = {idasunto};
        
        ResultSetHandler rsh = new BeanListHandler(AccionBean.class);
        List<AccionBean> datos = (List<AccionBean>) qr.query(sql, rsh, params);
        
        complementaDatos(datos,0);
        return datos;
    }
 public String ultimoAvanceConvenio(int idaccion) throws Exception {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select descripcion as ultimoavance from controlasuntospendientesnew.avance where idaccion=? order by idavance desc limit 2 ";
        Object[] params = {idaccion};
        ResultSetHandler rsh = new BeanHandler(AccionBean.class);
        AccionBean dato = (AccionBean) qr.query(sql, rsh, params);
        String ultimoAvance="Sin avances";
        if(dato != null) ultimoAvance = dato.getUltimoavance();
        return ultimoAvance;
    }
 public List<AccionBean> ultimosAvancesConvenio(int idaccion) throws Exception {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select fecha,descripcion from controlasuntospendientesnew.avance where idaccion=? order by idavance desc limit 2 ";
        //String sql = "SELECT descripcion FROM controlasuntospendientesnew.avance where idaccion=? and fecha >= ? and fecha <= ? order by fecha";
        Object[] params = {idaccion};
        ResultSetHandler rsh = new BeanListHandler(AccionBean.class);
        List<AccionBean> avances = (List<AccionBean>) qr.query(sql, rsh, params);
        return avances;
    }  
public List<AccionBean> ultimosAvancesConvenioSinCompromiso(int idasunto, String periodo1, String periodo2) throws Exception {
    DataSource ds = AdministradorDataSource.getDataSource();
    QueryRunner qr = new QueryRunner(ds); 
    String sql = "SELECT * FROM controlasuntospendientesnew.asunto as a " +
            "inner join controlasuntospendientesnew.avance as b on a.idasunto=b.idasunto " +
            "where a.tipoasunto='V' and a.idasunto=? and fecha >= ? and fecha <= ? order by b.fecha";
    Object[] params = {idasunto, periodo1, periodo2};
    ResultSetHandler rsh = new BeanListHandler(AccionBean.class);
    List<AccionBean> avances = (List<AccionBean>) qr.query(sql, rsh, params);
    return avances;
}
 public AccionBean buscarAcuerdoAsignado(int idRSuperior,int idacuerdo) throws Exception {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select * from controlasuntospendientesnew.accion where idaccion in (" 
                    + "select idaccion from controlasuntospendientesnew.responsable where "
                    + "asignadopor = ? and idaccion = ?)";
        
        Object[] params = {idRSuperior,idacuerdo};
        
        ResultSetHandler rsh = new BeanHandler(AccionBean.class);
        AccionBean dato = (AccionBean) qr.query(sql, rsh, params);
        
        complementaDatos(dato,idRSuperior);
        return dato;
    }
 
    private void complementaDatos(AccionBean accion, int idRSuperior) throws SQLException,  NamingException, Exception{
       if (accion == null) return;
       AnexoAccionDAO anexAcDao = new AnexoAccionDAO(); 
       FachadaDAO fachada = new FachadaDAO(areasConsultaCompleto);
        accion.setAnexos(anexAcDao.buscarAnexosPorAccion(accion));
        AsuntoBean asunto = fachada.buscaAsuntoPorLlavePrimaria(accion.getIdAsunto());
        accion.setIdAsuntoConsecutivo(asunto.getIdconsecutivo());
        if (idRSuperior == 0) {
            accion.setResponsables(fachada.buscarResponsablesPorAcuerdoxAreas(accion));
        } else {
            accion.setResponsables(fachada.buscarResponsablesxAcuerdoAsignadoM(idRSuperior, accion));
        } 
    }
   
    private void complementaDatos(List<AccionBean> acciones, int idRSuperior) throws SQLException,  NamingException, Exception{
       AnexoAccionDAO anexAcDao = new AnexoAccionDAO(); 
       FachadaDAO fachada = new FachadaDAO(areasConsultaCompleto);
       for(AccionBean accion:acciones) {
            accion.setAnexos(anexAcDao.buscarAnexosPorAccion(accion));
            AsuntoBean asunto = fachada.buscaAsuntoPorLlavePrimaria(accion.getIdAsunto());
            accion.setIdAsuntoConsecutivo(asunto.getIdconsecutivo());
            if (idRSuperior == 0) {
                accion.setResponsables(fachada.buscarResponsablesPorAcuerdoxAreas(accion));
            } else {
                accion.setResponsables(fachada.buscarResponsablesxAcuerdoAsignadoM(idRSuperior, accion));
            } 
       }  
    }
    
    public void eliminaAcuerdo(int idacuerdo) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "delete from controlasuntospendientesnew.accion where idaccion=?";
        Object[] params = {idacuerdo};
        qr.update(sql,params);
    }
    
    public void eliminaAcuerdosxAsunto(int idasunto) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "delete from controlasuntospendientesnew.accion where idasunto=?";
        Object[] params = {idasunto};
        qr.update(sql,params);
    }

    
    public void updateFechaAtencion(int idacuerdo, String fechanew) throws SQLException,  NamingException {
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       String sql = "update controlasuntospendientesnew.accion " +
                    "set acuerdo_fecha=? WHERE idaccion = ?";
       Object[] params = {fechanew,idacuerdo};
       qr.update(sql,params);
   } 
    
   public void updateEstatus(int idacuerdo, String estatus) throws NamingException, SQLException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       String sql = "update controlasuntospendientesnew.accion " +
                    "set estatus=? WHERE idaccion = ?";
       Object[] params = {estatus,idacuerdo};
       qr.update(sql,params);
   }
    public String buscaEstatusAccion(int idaccion) throws SQLException,  NamingException, Exception{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select estatus from controlasuntospendientesnew.accion where idaccion=? ";
        Object[] params = {idaccion};
        ResultSetHandler rsh = new BeanHandler(AccionBean.class);
        AccionDTO dato = (AccionDTO) qr.query(sql, rsh, params);
        return dato.getEstatus();
    }  
    public void cambiaEstatus(int idacuerdo, int idresponsable) throws NamingException, SQLException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       String sql = "update controlasuntospendientesnew.accion set estatus='P' WHERE idaccion = ? ";
       qr.update(sql,idacuerdo);
       sql = "update controlasuntospendientesnew.responsable set estatus='P', avance=99 WHERE idaccion = ? and idarea = ? ";
       Object[] params = {idacuerdo, idresponsable};
       qr.update(sql,params);
       sql = "update controlasuntospendientesnew.avance set porcentaje=99 WHERE idaccion = ? and idarea = ? ";
       qr.update(sql,params);
   }
}
