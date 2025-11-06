/*
 * AsuntoDAO.java
 *
 * Created on 4 de marzo de 2006, 04:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.baseDatos;


import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaDAO;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AsuntoBean;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.DatoString;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import javax.naming.NamingException;
import javax.sql.DataSource;
import mx.org.inegi.dggma.sistemas.asuntos.dto.*;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.Utiles;
import mx.org.inegi.dggma.sistemas.asuntos.vista.VistaListado;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaUsuarioArea;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.*;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.AdministraUsuariosAreas;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author Jos Luis Mondragn
 */
public class AsuntoDAO {
    private List<AreaDTO> areasConsulta;
    private AreasConsulta areasConsultaCompleto;
    
    /** Creates a new instance of AsuntoDAO */
    public AsuntoDAO(AreasConsulta areasC) {
       if (areasC != null) {
        this.areasConsulta = areasC.getAreasConsulta();
        this.areasConsultaCompleto = areasC;
       }
    }


    public int getMaxIdAsunto () throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select max(idasunto) as cantidad from controlasuntospendientesnew.asunto";
        
        ResultSetHandler rsh = new BeanHandler(Cantidad.class);
        Cantidad cantidad = (Cantidad) qr.query(sql, rsh);
        return cantidad.getCantidad();
        
    }

    public int getMaxIdAsuntoxTipo (String tipo) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select max(idconsecutivo) as cantidad from controlasuntospendientesnew.asunto where tipoasunto=?";
        
        Object[] params = {tipo};
        
        ResultSetHandler rsh = new BeanHandler(Cantidad.class);
        Cantidad cantidad = (Cantidad) qr.query(sql, rsh, params);
        return cantidad.getCantidad();
        
    }

    public void insert(AsuntoBean asunto) throws SQLException,  NamingException {
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       String sql = "insert into controlasuntospendientesnew.asunto(" +
                    "idconsecutivo,"+
                    "descripcion,"+
                    "fechaingreso,"+
                    "fechaatender,"+
                    "lugar,"+
                    "idarea,"+
                    "estatus,"+
                    "estatuscorto,"+
                    "observaciones,"+
                    "fuente,"+
                    "urgente,"+
                    "fechadescarga,"+
                    "activoestatus,"+
                    "fechaalta,"+
                    "tipoasunto,"+
                    //"administrador,"+
                    "presidencia,"+
                    "nocontrol,"+
                    "asistentes,"+
                    "estatustexto,"+
                    "informesunidad,"+
                    "reprograma," +
                    "modalidadreunion, " +
                    "fechaoriginal, categoria, transversal) "+
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
         Object[] params = {asunto.getIdconsecutivo(),
                            asunto.getDescripcion(),
                            asunto.getFechaingreso(),
                            asunto.getFechaatender(),
                            asunto.getLugar(),
                            asunto.getIdarea(),
                            asunto.getEstatus(),
                            asunto.getEstatuscorto(),
                            asunto.getObservaciones(),
                            asunto.getFuente(),
                            asunto.getUrgente(),
                            asunto.getFechadescarga(),
                            asunto.getActivoestatus(),
                            asunto.getFechaalta(),
                            asunto.getTipoasunto(),
//                            asunto.getAdministrador(),
                            asunto.getPresidencia(),
                            asunto.getNocontrol(),
                            asunto.getAsistentes(),
                            asunto.getEstatustexto(),
                            asunto.getInformesunidad(),
                            asunto.getReprograma(),
                            asunto.getModalidadreunion(),
                            asunto.getFechaatender(), 
                            asunto.getCategoria(), asunto.isTransversal()}; 
         qr.update(sql,params);
    }
    
   public void update(AsuntoBean asunto) throws SQLException,  NamingException {
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       String modifica="fechaatender=?, ";
       if(asunto.getFechaUltimaReprogramacion() != null) modifica="";
       String sql = "update controlasuntospendientesnew.asunto set " +
                    "descripcion = ?, "+
                    "fechaingreso=?, "+modifica+
                    //"fechaatender=?, "+ // si tiene reprog se queda como esta
                    "lugar=?, "+
                    "idarea=?, "+
                    "estatus=?, "+
                    "estatuscorto=?, "+
                    "observaciones=?, "+
                    "fuente=?," +
                    "urgente=?, " +
                    "fechadescarga=?, " +
                    "activoestatus=?, "+
                    "fechamodificacion=?, "+
                    "tipoasunto=?, "+
                    "presidencia=?, " +
                    "nocontrol=?, " +
                    "asistentes=?, "+
                    "estatustexto=?, " +
                    "informesunidad=?, "+
                    "modalidadreunion=?, "+
                    "reprograma = ?, "+
                    "fechaoriginal=?, " +
                    "categoria=?, "+
                    "transversal=? "+
                    "where idasunto=? ";
       if(asunto.getFechaUltimaReprogramacion() != null){
            Object[] params = {
                 asunto.getDescripcion(),
                 asunto.getFechaingreso(),
                 asunto.getLugar(),
                 asunto.getIdarea(),
                 asunto.getEstatus(),
                 asunto.getEstatuscorto(),
                 asunto.getObservaciones(),
                 asunto.getFuente(),
                 asunto.getUrgente(),
                 asunto.getFechadescarga(),
                 asunto.getActivoestatus(),
                 asunto.getFechamodificacion(),
                 asunto.getTipoasunto(),
                 asunto.getPresidencia(),
                 asunto.getNocontrol(),
                 asunto.getAsistentes(),
                 asunto.getEstatustexto(),
                 asunto.getInformesunidad(),
                 asunto.getModalidadreunion(),
                 asunto.getReprograma(),
                 asunto.getFechaatender(),
                 asunto.getCategoria(),
                 asunto.isTransversal(),
                 asunto.getIdasunto()
                 };
                qr.update(sql,params);
       } else {
            Object[] params = {
                 asunto.getDescripcion(),
                 asunto.getFechaingreso(),
                 asunto.getFechaatender(),
                 asunto.getLugar(),
                 asunto.getIdarea(),
                 asunto.getEstatus(),
                 asunto.getEstatuscorto(),
                 asunto.getObservaciones(),
                 asunto.getFuente(),
                 asunto.getUrgente(),
                 asunto.getFechadescarga(),
                 asunto.getActivoestatus(),
                 asunto.getFechamodificacion(),
                 asunto.getTipoasunto(),
                 asunto.getPresidencia(),
                 asunto.getNocontrol(),
                 asunto.getAsistentes(),
                 asunto.getEstatustexto(),
                 asunto.getInformesunidad(),
                 asunto.getModalidadreunion(),
                 asunto.getReprograma(),
                 asunto.getFechaatender(),
                 asunto.getCategoria(),
                 asunto.isTransversal(),
                 asunto.getIdasunto()
                 };
            qr.update(sql,params);
       }
    }

   public void updateFechaReprograma(AsuntoBean asunto) throws NamingException, SQLException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       String sql = "update controlasuntospendientesnew.asunto " +
                    "set  fechaatender = ?, reprograma = ? "+
                    "where idasunto=? ";
       Object[] params = {asunto.getFechaatender(), asunto.getReprograma(),asunto.getIdasunto()};
       qr.update(sql,params);
   }
     
   public void updateFechaAtencion(int idasunto, String fechanew) throws SQLException,  NamingException {
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       String sql = "update controlasuntospendientesnew.asunto " +
                    "set fechaatender=? WHERE idasunto = ?";
       Object[] params = {fechanew,idasunto};
       qr.update(sql,params);
   }           
   
   public void updateIdArchivoCierre(int idasunto, int valor) throws SQLException,  NamingException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       String sql = "update controlasuntospendientesnew.asunto " +
                    "set idarchivocierre=? WHERE idasunto = ?";
       Object[] params = {valor,idasunto};
       qr.update(sql,params);
   }
   
    
   public void actualizaInfo(AsuntoBean asunto) throws SQLException,  NamingException {
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       String sql = "update controlasuntospendientesnew.asunto " +
                    "set descripcion=? where idasunto=? and tipoasunto=?";
       Object[] params = {asunto.getDescripcion(), asunto.getIdasunto(), asunto.getTipoasunto()};
       qr.update(sql,params);
    }
    
    public void updateEstatus(AsuntoBean asunto) throws SQLException,  NamingException {
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       
       String sql = "update controlasuntospendientesnew.asunto " +
                    "set estatus=?, fechaatendertexto=? where idasunto=?";
   
       Object[] params = {asunto.getEstatus(), asunto.getFechaatender(), asunto.getIdasunto()};
       qr.update(sql,params);
    }
    public void updateEstatusAcuerdo(AccionBean acuerdo) throws SQLException,  NamingException {
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       //String sql = "update controlasuntospendientesnew.asunto set estatus=?, fechaatendertexto=? where idasunto=?";
       String sql = "update controlasuntospendientesnew.accion set estatus=? where idaccion=?";
       Object[] params = {acuerdo.getEstatus(), acuerdo.getIdAccion()};
       qr.update(sql,params);
    }
    public void delete(int idasunto) throws Exception {
            
        //Tal vez validar que no existan acciones direccionadas al asunto, en caso de no permitir eliminar
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
        
       String sql = "delete from controlasuntospendientesnew.asunto " +
                    "where idasunto=?";
       
       Object[] params = {idasunto};
               
       qr.update(sql,params);
        
    }

       
    public AsuntoBean findByPrimaryKey(int pk) throws Exception {
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds);
       
       String sql = "select * from controlasuntospendientesnew.asunto where idasunto=? ";

       Object[] params = {pk};
       
       ResultSetHandler rsh = new BeanHandler(AsuntoBean.class);
       AsuntoBean dato = (AsuntoBean) qr.query(sql, rsh, params);
              
       return dato;
    }
    
       

     public AsuntoBean findByPrimaryKeyFull(int pk) throws Exception {
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
      
       String sql = "select * from controlasuntospendientesnew.asunto where idasunto=?";
        
       Object[] params = {pk};
       
       ResultSetHandler rsh = new BeanHandler(AsuntoBean.class);
       AsuntoBean dato = (AsuntoBean) qr.query(sql, rsh, params);
       
       complementaDatosAsunto(dato,0);
       
       return dato;
    }
     
    public AsuntoBean asuntoxTipoxIdConsecutivo(String tipo, int idconsecutivo) throws SQLException,  NamingException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
      
       String sql = "select * from controlasuntospendientesnew.asunto where tipoasunto=? and idconsecutivo=?";
        
       Object[] params = {tipo,idconsecutivo};
       
       ResultSetHandler rsh = new BeanHandler(AsuntoBean.class);
       AsuntoBean dato = (AsuntoBean) qr.query(sql, rsh, params);
             
       return dato;
         
     }

     public Cantidad cantidadAsuntosPorAreaxTipo(FiltroAsunto filtro, String tipo) throws SQLException,  NamingException, Exception{
       DataSource ds = AdministradorDataSource.getDataSource();
       
        
       QueryRunner qr = new QueryRunner(ds); 
       SQLFiltro filtrosql = new SQLFiltro();
       filtrosql.setSql("select count(*) as cantidad from controlasuntospendientesnew.asunto as a where tipoasunto = ?  ");
       filtrosql.setParametros(new ArrayList());
       filtrosql.getParametros().add(tipo);
       construyeFiltroSQL(filtro,filtrosql);
              
       String sql = filtrosql.getSql() + " and idasunto in ("
           + "select idasunto from controlasuntospendientesnew.responsable where ";

      
       if (filtro.getIdarea() > 0){
           sql = sql + " idarea = ? and ";
           filtrosql.getParametros().add(filtro.getIdarea());
       } else {
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
    
       if (filtro.getTipoFecha() != null && filtro.getTipoFecha().equals("atención")) {
            sql+= " and ( fechaatencion >= ? and fechaatencion <= ?) ";
            filtrosql.getParametros().add(filtro.getFechaInicio());
            filtrosql.getParametros().add(filtro.getFechaFinal());
       }
       
       if (filtro.getTipoFecha() != null && filtro.getTipoFecha().equals("envio")) {
            sql+= " and ( fechaasignado >= ? and fechaasignado <= ?) ";
            filtrosql.getParametros().add(filtro.getFechaInicio());
            filtrosql.getParametros().add(filtro.getFechaFinal());
       }
       
       if (filtro.getPorcentajeAvance() != null && !filtro.getPorcentajeAvance().equals("")){
            sql+= " and ( avance = ?) ";
            filtrosql.getParametros().add(Integer.parseInt(filtro.getPorcentajeAvance()));
       }
       
       sql+= " order by idarea ) order by idconsecutivo desc";
       
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad dato = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
       
       return dato; 
    } 
     
    public List<AsuntoBean> buscarTotalAsuntosxAreaxTipo() throws SQLException,  NamingException, Exception{
       DataSource ds = AdministradorDataSource.getDataSource();
            
       QueryRunner qr = new QueryRunner(ds); 
       String sql = "select * from controlasuntospendientesnew.asunto where idasunto in (" +
                        "select idasunto from controlasuntospendientesnew.responsable where ";
       List param = new ArrayList();
       for (AreaDTO area:areasConsulta){
          sql = sql + " idarea = ? or ";
          param.add(area.getIdarea());
       }
       sql = sql.substring(0, sql.length()-3) + ")";
       
       ResultSetHandler rsh = new BeanListHandler(AsuntoBean.class);
       List<AsuntoBean> datos = (List<AsuntoBean>) qr.query(sql, rsh, param.toArray());
       
       complementaDatosAsunto(datos,0);
       
       return datos; 
    }
     
  
    public List<AsuntoBean> buscarAsuntosPorAreaxTipo(FiltroAsunto filtro, String tipo) throws SQLException,  NamingException, Exception{
       DataSource ds = AdministradorDataSource.getDataSource();
       
       QueryRunner qr = new QueryRunner(ds); 
       SQLFiltro filtrosql = new SQLFiltro();
       filtrosql.setSql("select * from controlasuntospendientesnew.asunto as a where tipoasunto = ? "); //and substring(fechaingreso,1,6)='202207' 2017 convenios
       filtrosql.setParametros(new ArrayList());
       filtrosql.getParametros().add(tipo);
       construyeFiltroSQL(filtro,filtrosql);
              
       String sql = filtrosql.getSql() + " and idasunto in ("
           + "select idasunto from controlasuntospendientesnew.responsable where ";

      
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
       
       sql+= " order by idarea )  order by idconsecutivo desc ";
       if (!filtro.isLimitAll()) {
           sql+="LIMIT 50 OFFSET ? ";
           filtrosql.getParametros().add(filtro.getOffset());
       }
       
       ResultSetHandler rsh = new BeanListHandler(AsuntoBean.class);
       List<AsuntoBean> datos = (List<AsuntoBean>) qr.query(sql, rsh, filtrosql.getParametros().toArray());
       
       complementaDatosAsunto(datos,0);
       return datos; 
    } 
    

    public Cantidad cantidadReunionesSinAcuerdos() throws SQLException,  NamingException, Exception{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
     
             
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.asunto where (";
       Object[] params = new Object[areasConsulta.size()];
       
       for(int i = 0;i<areasConsulta.size();i++){
           sql = sql + "idarea = ? or ";
           params[i] = areasConsulta.get(i).getIdarea();
       }                       
       
       sql = sql.substring(0, sql.length()-3) + ") and tipoasunto = 'R' and not (idasunto in ("+
             "select idasunto from controlasuntospendientesnew.accion group by idasunto))";
       
      ResultSetHandler rsh = new BeanHandler(Cantidad.class);
      Cantidad cnt = (Cantidad) qr.query(sql, rsh, params);
       
       return cnt; 
    } 
    
    
    public Cantidad cantidadReunionesSinAcuerdos(FiltroAsunto filtro) throws SQLException,  NamingException, Exception{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
     
       SQLFiltro filtrosql = new SQLFiltro();
       filtrosql.setSql("select count(*) as cantidad from controlasuntospendientesnew.asunto where (");
       filtrosql.setParametros(new ArrayList());
       construyeFiltroSQL(filtro,filtrosql);
       
       String sql = filtrosql.getSql();
       
       if (filtro.getIdarea() > 0){
           sql = sql + " idarea = ? ";
           filtrosql.getParametros().add(filtro.getIdarea());
       } else {
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
            sql+= " and ( fechaatencion >= ? and fechaatencion <= ?) ";
            filtrosql.getParametros().add(filtro.getFechaInicio());
            filtrosql.getParametros().add(filtro.getFechaFinal());
       }
       
       if (filtro.getTipoFecha() != null && filtro.getTipoFecha().equals("envio")) {
            sql+= " and ( fechaasignado >= ? and fechaasignado <= ?) ";
            filtrosql.getParametros().add(filtro.getFechaInicio());
            filtrosql.getParametros().add(filtro.getFechaFinal());
       }
       
       
       if (filtro.getPorcentajeAvance() != null && !filtro.getPorcentajeAvance().equals("")){
            sql+= " and ( avance = ?) ";
            filtrosql.getParametros().add(Integer.parseInt(filtro.getPorcentajeAvance()));
       }
       
       sql = sql.substring(0, sql.length()-3) + ") and tipoasunto = 'R' and not (idasunto in ("+
             "select idasunto from controlasuntospendientesnew.accion group by idasunto))";
       
      ResultSetHandler rsh = new BeanHandler(Cantidad.class);
      Cantidad cnt = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
       
       return cnt; 
    } 
    
    public Cantidad cantidadReunionesSinAcuerdosDirectos(int idarea) throws SQLException,  NamingException, Exception{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.asunto where idarea = ? and tipoasunto = 'R' and not (idasunto in ("+
             "select idasunto from controlasuntospendientesnew.accion group by idasunto))";
      Object[] params = {idarea};
      ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad cnt = (Cantidad) qr.query(sql, rsh, params);
       return cnt; 
    } 
    
    public Cantidad cantidadReunionesSinAcuerdosDirectos(FiltroAsunto filtro) throws SQLException,  NamingException, Exception{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       
      
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.asunto where idarea = ? " +
                    " and  ( fechaingreso >= ? and fechaingreso <= ?) and "+
                    " tipoasunto = 'R' and not (idasunto in ("+
                    "select idasunto from controlasuntospendientesnew.accion group by idasunto))";
      Object[] params = {filtro.getIdarea(),filtro.getFechaInicio()+"0000",filtro.getFechaFinal()+"9999"};
      ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad cnt = (Cantidad) qr.query(sql, rsh, params);
       
       return cnt; 
    } 
    public Cantidad cantidadReunionesEnviados(FiltroAsunto filtro) throws SQLException,  NamingException, Exception{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.asunto where idarea = ? " +
                    " and  ( fechaingreso >= ? and fechaingreso <= ?) and "+
                    " tipoasunto = 'R' and (idasunto in ("+
                    "select idasunto from controlasuntospendientesnew.accion group by idasunto))";
      Object[] params = {filtro.getIdarea(),filtro.getFechaInicio()+"0000",filtro.getFechaFinal()+"9999"};
      ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad cnt = (Cantidad) qr.query(sql, rsh, params);
       return cnt;
    }
    public Cantidad cantidadReunionesConAcuerdosDirectos(int idarea) throws SQLException,  NamingException, Exception{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
      
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.asunto where idarea = ? and tipoasunto = 'R' and (idasunto in ("+
             "select idasunto from controlasuntospendientesnew.accion group by idasunto))";
      Object[] params = {idarea};
      ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad cnt = (Cantidad) qr.query(sql, rsh, params);
       
       return cnt; 
    } 
    
    public Cantidad cantidadReunionesConAcuerdosDirectos(FiltroAsunto filtro) throws SQLException,  NamingException, Exception{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
      
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.asunto where idarea = ? "+
                    " and  ( fechaingreso >= ? and fechaingreso <= ?) and "+
                    " tipoasunto = 'R' and (idasunto in ("+
                    "select idasunto from controlasuntospendientesnew.accion group by idasunto))";
      Object[] params = {filtro.getIdarea(),filtro.getFechaInicio()+"0000",filtro.getFechaFinal()+"9999"};
      ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad cnt = (Cantidad) qr.query(sql, rsh, params);
       
       return cnt; 
    } 
    
    
    public Cantidad cantidadReunionesSinAcuerdosCompartidos(int idarea) throws SQLException,  NamingException, Exception{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       
      
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.asunto where tipoasunto = 'R' and "+
                    "idasunto in (select idasunto from controlasuntospendientesnew.corresponsable where idarea = ? ) " +
                    "and not (idasunto in (select idasunto from controlasuntospendientesnew.accion group by idasunto))";
       
      Object[] params = {idarea};
      ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad cnt = (Cantidad) qr.query(sql, rsh, params);
       
       return cnt; 
    }
    
    
    public List<AsuntoBean> buscarAsuntosSAccionesxArea() throws SQLException,  NamingException, Exception{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       String sql = "select * from controlasuntospendientesnew.asunto where (";
       Object[] params = new Object[areasConsulta.size()];
       
       for(int i = 0;i<areasConsulta.size();i++){
           sql = sql + "idarea = ? or ";
           params[i] = areasConsulta.get(i).getIdarea();
       }                       
       sql = sql.substring(0, sql.length()-3) + ") and tipoasunto = 'R' and estatus='P' and not (idasunto in ("+
             "select idasunto from controlasuntospendientesnew.accion group by idasunto)) order by idconsecutivo";
       
       ResultSetHandler rsh = new BeanListHandler(AsuntoBean.class);
       List<AsuntoBean> datos = (List<AsuntoBean>) qr.query(sql, rsh, params);
       //complementaDatosAsunto(datos,areas); No se necesita solo es consulta
       return datos; 
    } 
    public int reunionesSinAcuerdo(String anio) throws SQLException,  NamingException, Exception{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       String sql = "select count(1) as cantidad from controlasuntospendientesnew.asunto \n" +
            "where tipoasunto = 'R' and substring(fechaingreso,1,4)=? \n" +
            "and not (idasunto in (select idasunto from controlasuntospendientesnew.accion group by idasunto)) ";
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad dato = (Cantidad) qr.query(sql, rsh, anio);
       return dato.getCantidad();
    }
    
    public AsuntoBean buscarAsuntoAsignado(int idRSuperior, int idasunto) throws SQLException,  NamingException, Exception{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
      
       String sql = "select * from controlasuntospendientesnew.asunto where idasunto in ("
                  + "select idasunto from controlasuntospendientesnew.responsable where "
                  + " idasunto = ? and asignadopor = ? )";
       
       Object[] params = {idasunto,idRSuperior};
       ResultSetHandler rsh = new BeanHandler(AsuntoBean.class);
       AsuntoBean dato = (AsuntoBean) qr.query(sql, rsh, params);
       if (dato != null) complementaDatosAsunto(dato, idRSuperior);
          
       return dato; 
       
    } 
     
    
    public List<AsuntoBean> buscarAsuntosReunion(FiltroAsunto filtro) throws SQLException,  NamingException, Exception{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
      
       SQLFiltro filtrosql = new SQLFiltro();
       filtrosql.setSql("select a.*,cat.descripcion as categoriaTxt from controlasuntospendientesnew.asunto as a left join controlasuntospendientesnew.cat_categoria as cat on a.categoria=cat.id "
       //filtrosql.setSql("select * from controlasuntospendientesnew.asunto, " 
               + " where tipoasunto = 'R' "); //and substring(fechaingreso,1,4)='2022'
       filtrosql.setParametros(new ArrayList());
       construyeFiltroSQL(filtro,filtrosql);
       
       String sql = filtrosql.getSql();
     
       
       if (filtro.getIdarea() > 0) {
         if (filtro.getTiporeunion()!= null && !filtro.getTiporeunion().equals("")){
            sql = sql + "and  ( ";
            String areas="";
            if (filtro.getTiporeunion().equals("D") || filtro.getTiporeunion().equals("T")) {   
                areas = areas + " idarea = ? ";
                filtrosql.getParametros().add(filtro.getIdarea());
            }
           
            if (filtro.getTiporeunion().equals("C") || filtro.getTiporeunion().equals("T")) {   
                areas = areas.equals("") ? "" : areas + " or ";
                areas = areas + " idasunto in ( "
                    + " select idasunto from controlasuntospendientesnew.corresponsable where idarea = ? )";
                filtrosql.getParametros().add(filtro.getIdarea());
            }
            
             /*Se coment� a solicutd de Osvaldo 19/06/2015*/
            //Tiene acuerdos asignados aunque no es responsable de la reuni�n.
            /*if (filtro.getTiporeunion().equals("A") || filtro.getTiporeunion().equals("T")) {   
                areas = areas.equals("") ? "" : areas + " or ";
                areas = areas + " idasunto in ( "
                    + " select idasunto from controlasuntospendientesnew.accion where idaccion in ( "
                    + " select idaccion from controlasuntospendientesnew.responsable where idarea = ? )) ";
                filtrosql.getParametros().add(filtro.getIdarea());
            }*/
            
            sql = sql+areas+") ";
            
            
         } else {
            sql = sql + " and  ( idarea = ?  or idasunto in ("
                      + "select idasunto from controlasuntospendientesnew.corresponsable where idarea = ? ))";
                filtrosql.getParametros().add(filtro.getIdarea());
                filtrosql.getParametros().add(filtro.getIdarea());
         }
           
       } else {
         if (filtro.getTiporeunion()!= null  && !filtro.getTiporeunion().equals("")){
            sql+=" and (";
            String areas="";
            if (filtro.getTiporeunion().equals("D") || filtro.getTiporeunion().equals("T")) {
                for (AreaDTO area:areasConsulta) {
                    areas = areas + " idarea = ? or ";
                    filtrosql.getParametros().add(area.getIdarea());
                }                   
                areas = areas.substring(0, areas.length()-3);      
            }
           
            //corresponsables        
            if (filtro.getTiporeunion().equals("C") || filtro.getTiporeunion().equals("T")) {
                areas = areas.equals("") ? "": areas+" or ";
                areas = areas + " idasunto in ("
                            + "select idasunto from controlasuntospendientesnew.corresponsable where ";

                for (AreaDTO area:areasConsulta) {
                    areas = areas + " idarea = ? or ";
                    filtrosql.getParametros().add(area.getIdarea());
                }                   

               areas = areas.substring(0, areas.length()-3)+")";
            }
            sql = sql + areas+ " )";
         
         }
         
       }
           
       if (filtro.getEstatusReunion() != null && !filtro.getEstatusReunion().equals("TO")) {
         if (filtro.getEstatusReunion().equals("RA")) {
           sql = sql + " and (idasunto in (select idasunto from controlasuntospendientesnew.accion group by idasunto))";          
         } else if (filtro.getEstatusReunion().equals("RS")) {
           sql = sql + " and not (idasunto in (select idasunto from controlasuntospendientesnew.accion group by idasunto)) and a.estatus='P' ";        
         } else if (filtro.getEstatusReunion().equals("RR")) {
           //sql = sql + " and (idasunto in (select idasunto from controlasuntospendientesnew.accion WHERE descripcion='Sin acuerdos' group by idasunto))";        
           sql = sql + " and a.estatus = 'A' ";
         }
       }
       
       sql = sql + " order by idconsecutivo desc LIMIT 50 OFFSET ?";
       filtrosql.getParametros().add(filtro.getOffset());
           
       ResultSetHandler rsh = new BeanListHandler(AsuntoBean.class);
       List<AsuntoBean> datos = (List<AsuntoBean>) qr.query(sql, rsh, filtrosql.getParametros().toArray());
       
       complementaDatosAsunto(datos,0);  //No existen varias areas responsables
       
       return datos; 
       
    } 
    
    public  Cantidad  cantidadAsuntosReunion(FiltroAsunto filtro) throws SQLException,  NamingException, Exception{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
      
       SQLFiltro filtrosql = new SQLFiltro();
       filtrosql.setSql("select count(*) as cantidad from controlasuntospendientesnew.asunto as a where tipoasunto = 'R' ");
       filtrosql.setParametros(new ArrayList());
       construyeFiltroSQL(filtro,filtrosql);
       
       String sql = filtrosql.getSql();
             
       if (filtro.getIdarea() > 0) {
         if (filtro.getTiporeunion()!= null && !filtro.getTiporeunion().equals("")){
            sql = sql + "and  ( ";
            String areas="";
            if (filtro.getTiporeunion().equals("D") || filtro.getTiporeunion().equals("T")) {   
                areas = areas + " idarea = ? ";
                filtrosql.getParametros().add(filtro.getIdarea());
            }
           
            if (filtro.getTiporeunion().equals("C") || filtro.getTiporeunion().equals("T")) {   
                areas = areas.equals("") ? "" : areas + " or ";
                areas = areas + " idasunto in ( "
                    + " select idasunto from controlasuntospendientesnew.corresponsable where idarea = ? )";
                filtrosql.getParametros().add(filtro.getIdarea());
            }
            
            //Tiene acuerdos asignados aunque no es responsable de la reuni�n.
             /*Se coment� a solicutd de Osvaldo 19/06/2015*/
            /*if (filtro.getTiporeunion().equals("A") || filtro.getTiporeunion().equals("T")) {   
                areas = areas.equals("") ? "" : areas + " or ";
                areas = areas + " idasunto in ( "
                    + " select idasunto from controlasuntospendientesnew.accion where idaccion in ( "
                    + " select idaccion from controlasuntospendientesnew.responsable where idarea = ? )) ";
                filtrosql.getParametros().add(filtro.getIdarea());
            }*/
            
            sql = sql+areas+") ";
            
         } else {
            sql = sql + " and  ( idarea = ?  or idasunto in ("
                      + "select idasunto from controlasuntospendientesnew.corresponsable where idarea = ? ))";
                filtrosql.getParametros().add(filtro.getIdarea());
                filtrosql.getParametros().add(filtro.getIdarea());
         }
           
       } else {
         if (filtro.getTiporeunion()!= null  && !filtro.getTiporeunion().equals("")){
            sql+=" and (";
            String areas="";
            if (filtro.getTiporeunion().equals("D") || filtro.getTiporeunion().equals("T")) {
                for (AreaDTO area:areasConsulta) {
                    areas = areas + " idarea = ? or ";
                    filtrosql.getParametros().add(area.getIdarea());
                }                   
                areas = areas.substring(0, areas.length()-3);      
            }
           
            //corresponsables        
            if (filtro.getTiporeunion().equals("C") || filtro.getTiporeunion().equals("T")) {
                areas = areas.equals("") ? "": areas+" or ";
                areas = areas + " idasunto in ("
                            + "select idasunto from controlasuntospendientesnew.corresponsable where ";

                for (AreaDTO area:areasConsulta) {
                    areas = areas + " idarea = ? or ";
                    filtrosql.getParametros().add(area.getIdarea());
                }                   

               areas = areas.substring(0, areas.length()-3)+")";
            }
            
            
            //Tiene acuerdos asignados aunque no es responsable o corresponsable de la reuni�n.
            /*se coment� por solicitud de Osvaldo 19/06/2015 para que no aparezcan reuniones que no pertenezcan
              al �rea filtrada
            */
            
            /*if (filtro.getTiporeunion().equals("A") || filtro.getTiporeunion().equals("T")) {   
                areas = areas.equals("") ? "" : areas + " or ";
                areas = areas + " idasunto in ( "
                    + " select idasunto from controlasuntospendientesnew.accion where idaccion in ( "
                    + " select idaccion from controlasuntospendientesnew.responsable where ";
                
                for (AreaDTO area:areasConsulta) {
                    areas = areas + " idarea = ? or ";
                    filtrosql.getParametros().add(area.getIdarea());
                }               
                areas = areas.substring(0, areas.length()-3)+"))";
            }*/
            
            sql = sql + areas+ " ) ";
         
         }
         
       }
       
/*       if (filtro.getEstatusReunion() != null && !filtro.getEstatusReunion().equals("TO")) {
         if (filtro.getEstatusReunion().equals("RA")) {
           sql = sql + " and (idasunto in (select idasunto from controlasuntospendientesnew.accion group by idasunto))";          
         } else {
           sql = sql + " and not (idasunto in (select idasunto from controlasuntospendientesnew.accion group by idasunto))";        
         }
       }*/
       
       if (filtro.getEstatusReunion() != null && !filtro.getEstatusReunion().equals("TO")) {
         if (filtro.getEstatusReunion().equals("RA")) {
           sql = sql + " and (idasunto in (select idasunto from controlasuntospendientesnew.accion group by idasunto))";          
         } else if (filtro.getEstatusReunion().equals("RS")) {
           sql = sql + " and not (idasunto in (select idasunto from controlasuntospendientesnew.accion group by idasunto)) and a.estatus='P' ";
         } else if (filtro.getEstatusReunion().equals("RR")) {
           sql = sql + " and a.estatus = 'A' ";
         }
       }
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad cnt = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
       return cnt;
    } 

    
    
    public void construyeFiltroSQL(FiltroAsunto filtro,SQLFiltro filtrosql){
       String campoFecha = "";
      
       String sql = filtrosql.getSql();
       
       if (filtro.getUrgente() != null && !filtro.getUrgente().equals("T")) {
           sql+= " and urgente = ? ";
           filtrosql.getParametros().add(filtro.getUrgente().trim());
       }
       
       if (filtro.getEstatusAsunto() != null && !filtro.getEstatusAsunto().equals("T")){
           sql += " and estatus = ? ";
           filtrosql.getParametros().add(filtro.getEstatusAsunto());
       }
       
       if (filtro.getClasifica() != null && !filtro.getClasifica().equals("T")){
           sql+= " and fuente = ? ";
           filtrosql.getParametros().add(filtro.getClasifica());
       }
       
       if (filtro.getPresidencia() != null && !filtro.getPresidencia().equals("T")){
           sql += " and presidencia = ?  ";
           filtrosql.getParametros().add(filtro.getPresidencia().trim());
       }
       
       if (filtro.getModalidadReunion() != null && !filtro.getModalidadReunion().equals("T")){
           sql += " and modalidadreunion = ?  ";
           filtrosql.getParametros().add(filtro.getModalidadReunion().trim());
       }

       if (filtro.getTexto() != null && !filtro.getTexto().trim().equals("")) {
         sql += " and ( nocontrol like ? or upper(a.descripcion) like ? or upper(estatustexto) like ? or upper(lugar) like ? or upper(asistentes) like ? or estatuscorto like ? ) "; 
         filtrosql.getParametros().add("%"+filtro.getTexto().toUpperCase().trim()+"%");
         filtrosql.getParametros().add("%"+filtro.getTexto().toUpperCase().trim()+"%");
         filtrosql.getParametros().add("%"+filtro.getTexto().toUpperCase().trim()+"%");
         filtrosql.getParametros().add("%"+filtro.getTexto().toUpperCase().trim()+"%");
         filtrosql.getParametros().add("%"+filtro.getTexto().toUpperCase().trim()+"%");
         filtrosql.getParametros().add("%"+filtro.getTexto().toUpperCase().trim()+"%");
       }
       
       if (filtro.getId() !=  null){
          try {
            int val = Integer.parseInt(filtro.getId().trim());
            sql +=  " and idconsecutivo = ? ";
            filtrosql.getParametros().add(val);                  
          } catch (Exception ex){
            //Hubo un error por lo que no se agrega como num�rico  
          }
       }
    
       
       if (filtro.getTipoFecha() != null && (filtro.getTipoFecha().equals("vencimiento") || filtro.getTipoFecha().equals("captura")  || filtro.getTipoFecha().equals("envio"))){
            if (filtro.getTipoFecha().equals("envio")) {
                campoFecha = "fechaingreso";
            }
            if (filtro.getTipoFecha().equals("vencimiento")) {
                campoFecha = "fechaatender";
            }
            
            if (filtro.getTipoFecha().equals("captura")) {
                campoFecha = "fechaalta";
            }
            
            sql+= " and  ( substring( "+campoFecha +",1,8) >= ? and substring("+campoFecha+",1,8) <= ?) ";
            filtrosql.getParametros().add(filtro.getFechaInicio());
            filtrosql.getParametros().add(filtro.getFechaFinal());
       }
       
       //////////////////////////////////////
       ////Condici�n agregada para un reporte de Osvaldo, es algo excepcional
       /*sql+= " and  (  fechaingreso <= ?) ";
       filtrosql.getParametros().add("201412319999");*/
       ///////////////////////

       
       
       //Obten los VENCIDOS
       String hoy=Utiles.getFechaHoy();
       String maniana=Utiles.getFechaManianaLunes();
       String pasadomaniana=Utiles.getFechamasXDias(Utiles.getFechaHoraJava(), 2);
       String pasadomanianaMas1=Utiles.getFechamasXDias(Utiles.getFechaHoraJava(), 3);
       if (filtro.getEstatusFechaAtencion() != null) {
        //Vencidos   
        if (filtro.getEstatusFechaAtencion().equals("VE") ){
            sql+= " and ( substring(fechaatender,1,8) <= ? and estatus = 'P')  ";
            filtrosql.getParametros().add(hoy);
        }
        //Por Vencer
        if (filtro.getEstatusFechaAtencion().equals("AV") ){
            sql+= " and  ( substring(fechaatender,1,8) = ? or substring(fechaatender,1,8) = ?) and estatus = 'P' ";
            filtrosql.getParametros().add(maniana);
            filtrosql.getParametros().add(pasadomaniana);
        }
        //Activos
        if (filtro.getEstatusFechaAtencion().equals("AC") ){
            sql+= " and ( substring(fechaatender,1,8) >= ?) and estatus = 'P' ";
            filtrosql.getParametros().add(pasadomanianaMas1);
        }
       }
       
       filtrosql.setSql(sql);

    }
    
          
    
    public Cantidad cantidadAsuntosxEstatusxAreaxTipo(String tipo, String estatus, FiltroAsunto filtro, int idarea) throws SQLException,  NamingException, Exception{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.asunto as a where tipoasunto = ? and estatus = ?  ";
       
       SQLFiltro filtrosql = new SQLFiltro();
       filtrosql.setSql(sql);
       filtrosql.setParametros(new ArrayList());
       filtrosql.getParametros().add(tipo);
       filtrosql.getParametros().add(estatus);
       construyeFiltroSQL(filtro,filtrosql);

       sql = filtrosql.getSql();
       sql+= " and idasunto in ("
                        + "select idasunto from controlasuntospendientesnew.responsable where idarea = ? ";
       filtrosql.getParametros().add(idarea);
       
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
       
       sql+=" ) ";
       
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad dato = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
                    
       return dato; 
    }
    
    ////////////******************************************************************************************************************
    ////////////******************************************************************************************************************
    ////////////******************************************************************************************************************
    
    public Cantidad cantidadAsuntosVencidos(String tipo, int idarea, FiltroAsunto filtro) throws SQLException,  NamingException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
      
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.asunto as a where tipoasunto = ? and fechaatender <  ? and estatus= 'P' ";
       
       SQLFiltro filtrosql = new SQLFiltro();
       filtrosql.setSql(sql);
       filtrosql.setParametros(new ArrayList());
       filtrosql.getParametros().add(tipo);
       filtrosql.getParametros().add(Utiles.getFechaHoy());
       construyeFiltroSQL(filtro,filtrosql);

       sql = filtrosql.getSql();
       sql+= " and idasunto in ("
                        + "select idasunto from controlasuntospendientesnew.responsable where idarea = ? ";
       filtrosql.getParametros().add(idarea);
       
       if (filtro.getTipoFecha() != null && filtro.getTipoFecha().equals("atencion")) {
            sql+= " and ( substring(fechaatencion,1,8) >= ? and substring(fechaatencion,1,8) <= ?) ";
            filtrosql.getParametros().add(filtro.getFechaInicio());
            filtrosql.getParametros().add(filtro.getFechaFinal());
       }
       
       if (filtro.getTipoFecha() != null && filtro.getTipoFecha().equals("asignado")) {
            sql+= " and (substring(fechaasignado,1,8) >= ? and substring(fechaasignado,1,8) <= ?) ";
            filtrosql.getParametros().add(filtro.getFechaInicio());
            filtrosql.getParametros().add(filtro.getFechaFinal());
       }
              
       if (filtro.getPorcentajeAvance() != null && !filtro.getPorcentajeAvance().equals("")){
            sql+= " and ( avance = ?) ";
            filtrosql.getParametros().add(Integer.parseInt(filtro.getPorcentajeAvance()));
       }
       
       sql+=" ) ";
       
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad dato = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
       return dato; 
    }
    
    public Cantidad cantidadAsuntosVencidosCom(String tipo, int idarea, FiltroAsunto filtro) throws SQLException,  NamingException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
      
       //String sql = "select count(*) as cantidad from controlasuntospendientesnew.asunto where tipoasunto = ? and fechaatender <  ? and estatus= 'P' ";
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.asunto as a where tipoasunto = ? and fechaatender <= ? and estatus= 'P' ";

       SQLFiltro filtrosql = new SQLFiltro();
       filtrosql.setSql(sql);
       filtrosql.setParametros(new ArrayList());
       filtrosql.getParametros().add(tipo);
       filtrosql.getParametros().add(Utiles.getFechaHoy());
       construyeFiltroSQL(filtro,filtrosql);

       sql = filtrosql.getSql();
       sql+= " and idasunto in ("
                        + "select idasunto from controlasuntospendientesnew.responsable where idarea = ? and estatus = 'A' ";
       filtrosql.getParametros().add(idarea);
       
       //AGREGADO EL 21/08/14
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
       
       sql+=" ) ";
       
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad dato = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
       return dato; 
    }
    
    
    public Cantidad cantidadAsuntosVencidosDirectos(String tipo, int idarea, FiltroAsunto filtro) throws SQLException,  NamingException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       //filtro.setEstatusFechaAtencion("VE");
      // String sql = "select count(*) as cantidad from controlasuntospendientesnew.asunto where tipoasunto = ? and (substring(fechaatender,1,8) <= ?) and estatus= 'P' ";
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.asunto as a where tipoasunto = ? and estatus= 'P' ";
       SQLFiltro filtrosql = new SQLFiltro();
       filtrosql.setSql(sql);
       filtrosql.setParametros(new ArrayList());
       filtrosql.getParametros().add(tipo);
       //EstatusFechaAtencion VE   TO
       filtro.setEstatusFechaAtencion("VE");
       //filtrosql.getParametros().add(Utiles.getFechaHoy());
       construyeFiltroSQL(filtro,filtrosql);
       sql = filtrosql.getSql(); //and substring(fechaatender,1,8) <= '20221124'
       sql+= " and idasunto in ("
                        + "select idasunto from controlasuntospendientesnew.responsable where idarea = ?  and estatus = 'P' ";
       filtrosql.getParametros().add(idarea);
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
       sql+=" ) ";
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad dato = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
       filtro.setEstatusFechaAtencion("TO");
       return dato; 
    }
    
     public Cantidad cantidadAsuntosPorVencer(String tipo, int idarea, FiltroAsunto filtro) throws SQLException,  NamingException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.asunto as a where tipoasunto = ? and (fechaatender = ? or fechaatender = ?) and estatus = 'P' ";
           
       SQLFiltro filtrosql = new SQLFiltro();
       filtrosql.setSql(sql);
       filtrosql.setParametros(new ArrayList());
       filtrosql.getParametros().add(tipo);
       filtrosql.getParametros().add(Utiles.getFechaHoy());
       filtrosql.getParametros().add(Utiles.getFechaManiana());
       construyeFiltroSQL(filtro,filtrosql);

       sql = filtrosql.getSql();
       sql+= " and idasunto in ("
                        + "select idasunto from controlasuntospendientesnew.responsable where idarea = ? ";
       filtrosql.getParametros().add(idarea);
       
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
       
       sql+=" ) ";
       
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad dato = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
                    
       return dato; 
    
    }
   
     public Cantidad cantidadAsuntosPorVencerCom(String tipo, int idarea, FiltroAsunto filtro) throws SQLException,  NamingException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.asunto as a where tipoasunto = ? and (fechaatender = ? or fechaatender = ?) and estatus = 'P' ";
           
       SQLFiltro filtrosql = new SQLFiltro();
       filtrosql.setSql(sql);
       filtrosql.setParametros(new ArrayList());
       filtrosql.getParametros().add(tipo);
       filtrosql.getParametros().add(Utiles.getFechaHoy());
       filtrosql.getParametros().add(Utiles.getFechaManiana());
       construyeFiltroSQL(filtro,filtrosql);

       sql = filtrosql.getSql();
       sql+= " and idasunto in ("
                        + "select idasunto from controlasuntospendientesnew.responsable where idarea = ?  and estatus = 'A' ";
       filtrosql.getParametros().add(idarea);
      
       //AGREGADO EL 21/08/14
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
       
       sql+=" ) ";
       
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad dato = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
                    
       return dato; 
    
    }
     
     public Cantidad cantidadAsuntosPendActivosCom(String tipo, int idarea, FiltroAsunto filtro) throws SQLException,  NamingException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.asunto as a where tipoasunto = ? and fechaatender > ? and estatus = 'P' ";           
       SQLFiltro filtrosql = new SQLFiltro();
       filtrosql.setSql(sql);
       filtrosql.setParametros(new ArrayList());
       filtrosql.getParametros().add(tipo);
       filtrosql.getParametros().add(Utiles.getFechaManiana());
       construyeFiltroSQL(filtro,filtrosql);

       sql = filtrosql.getSql();
       sql+= " and idasunto in ("
                        + "select idasunto from controlasuntospendientesnew.responsable where idarea = ?  and estatus = 'A' ";
       filtrosql.getParametros().add(idarea);
      
       //AGREGADO EL 21/08/14
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
       
       sql+=" ) ";
       
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad dato = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
                    
       return dato; 
    
    }
     
     
     
     public Cantidad cantidadAsuntosPorVencerDirectos(String tipo, int idarea, FiltroAsunto filtro) throws SQLException,  NamingException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       
       //filtro.setEstatusFechaAtencion("AV");
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.asunto as a where tipoasunto = ? and estatus = 'P'  ";
           
       SQLFiltro filtrosql = new SQLFiltro();
       filtrosql.setSql(sql);
       filtrosql.setParametros(new ArrayList());
       filtrosql.getParametros().add(tipo);
       filtro.setEstatusFechaAtencion("AV");
       construyeFiltroSQL(filtro,filtrosql);

       sql = filtrosql.getSql();
       sql+= " and  idasunto in ("
                        + "select idasunto from controlasuntospendientesnew.responsable where idarea = ? and estatus = 'P' ";
       filtrosql.getParametros().add(idarea);
       
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
       
       sql+=" ) ";
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad dato = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
       filtro.setEstatusFechaAtencion("TO");
       return dato; 
    
    } 
     
    public Cantidad cantidadAsuntosPendActivosDirectos(String tipo, int idarea, FiltroAsunto filtro) throws SQLException,  NamingException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       
       filtro.setEstatusFechaAtencion("AC");
       String sql = "select count(*) as cantidad from controlasuntospendientesnew.asunto as a where tipoasunto = ? and estatus = 'P'  ";
           
       SQLFiltro filtrosql = new SQLFiltro();
       filtrosql.setSql(sql);
       filtrosql.setParametros(new ArrayList());
       filtrosql.getParametros().add(tipo);
       construyeFiltroSQL(filtro,filtrosql);

       sql = filtrosql.getSql();
       sql+= " and  idasunto in ("
                        + "select idasunto from controlasuntospendientesnew.responsable where idarea = ? and estatus = 'P' ";
       filtrosql.getParametros().add(idarea);
       
       if (filtro.getTipoFecha() != null && filtro.getTipoFecha().equals("atencion")) {
            sql+= " and ( substring(fechaatencion >= ? and substring(fechaatencion,1,8) <= ?) ";
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
       
       sql+=" ) ";
       
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad dato = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
       return dato; 
    
    }
    
    public Cantidad cantidadAsuntosAtendidos(String tipo, int idarea, FiltroAsunto filtro) throws SQLException,  NamingException{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       String sql = "select count(1) as cantidad from controlasuntospendientesnew.asunto where tipoasunto = ?  " +
            "and idasunto in (select idasunto from controlasuntospendientesnew.responsable where idarea = ? and estatus = 'A' " +
            "and (substring(fechaatencion,1,8) >= ? and substring(fechaatencion,1,8) <= ?) )  ";
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad dato = (Cantidad) qr.query(sql, rsh, tipo, idarea, filtro.getFechaIniAnioAct(), filtro.getFechaFinal());
       return dato;
    }
    public Cantidad cantidadAsuntosxAreaxTipo(FiltroAsunto filtro,String tipo) throws SQLException,  NamingException, Exception{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       
       SQLFiltro filtrosql = new SQLFiltro();
       
       filtrosql.setSql("select count(*) as Cantidad from controlasuntospendientesnew.asunto as a where tipoasunto = ? ");
       filtrosql.setParametros(new ArrayList());
       filtrosql.getParametros().add(tipo);
       construyeFiltroSQL(filtro,filtrosql);
              
       String sql = filtrosql.getSql() + " and idasunto in ("
           + "select idasunto from controlasuntospendientesnew.responsable where ";

      
       if (filtro.getIdarea() > 0){
           if (filtro.getIdareaDelegada() > 0) {
                sql = sql + " idarea = ?  ";
                filtrosql.getParametros().add(filtro.getIdareaDelegada());
                if (filtro.getEstatusResp() != null && !filtro.getEstatusResp().equals("T")) {
                    sql = sql + " and estatus = ?  ";
                    filtrosql.getParametros().add(filtro.getEstatusResp());
                }
                sql+= " and asignadopor = ?  and estatus <> 'C' ";
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
              
       sql = sql + ")";
       
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad dato = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
       
       return dato; 
    } 

    public Cantidad cantidadAsuntosDelegadosxAreaxTipo(FiltroAsunto filtro,String tipo) throws SQLException,  NamingException, Exception{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       
       SQLFiltro filtrosql = new SQLFiltro();
       
       filtrosql.setSql("select count(*) as Cantidad from controlasuntospendientesnew.asunto as a where tipoasunto = ? ");
       filtrosql.setParametros(new ArrayList());
       filtrosql.getParametros().add(tipo);
       construyeFiltroSQL(filtro,filtrosql);
              
       String sql = filtrosql.getSql() + " and idasunto in ("
           + "select idasunto from controlasuntospendientesnew.responsable where ";

      
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
       
       
       if (filtro.getPorcentajeAvance() != null && !filtro.getPorcentajeAvance().equals("")){
            sql+= " and ( avance = ?) ";
            filtrosql.getParametros().add(Integer.parseInt(filtro.getPorcentajeAvance()));
       }
              
       sql = sql + ")";
       
       ResultSetHandler rsh = new BeanHandler(Cantidad.class);
       Cantidad dato = (Cantidad) qr.query(sql, rsh, filtrosql.getParametros().toArray());
       
       return dato; 
    } 
    

    ///////La ultima versi�n
    private void complementaDatosAsunto(List<AsuntoBean> asuntos, int idRSuperior) throws SQLException,  NamingException, Exception{
        FachadaUsuarioArea fu = new FachadaUsuarioArea();
        AnexoAsuntoDAO anexoADAO = new AnexoAsuntoDAO();
        FachadaDAO fachada = new FachadaDAO(areasConsultaCompleto);
        AccionDAO adao = new AccionDAO(areasConsultaCompleto);
        
        for(AsuntoBean asunto:asuntos){
            //Checar si esta condicion se utiliza
            if (idRSuperior == 0) {
               //asunto.setResponsables(fachada.buscarResponsablesPorAsuntoxAreas(asunto.getIdasunto(),asunto.getFechaoriginal()));
                asunto.setResponsables(fachada.buscarResponsablesPorAsuntoxAreasM(asunto));
            } else {
               asunto.setResponsables(fachada.buscarResponsablesxAsuntoAsignadoM(idRSuperior, asunto));
            }   
            
            if (asunto.getIdarea() > 0){
              if (asunto.getTipoasunto().equals("C")) {
                asunto.setRemitente(fu.buscaUsuarioVigenteoNo(asunto.getIdarea())); //se esta sustituyendo el idarea por idusuario para asignar directo a correo
              } else {
                asunto.setResponsable(fu.buscaArea(asunto.getIdarea()));
              }  
            }
            asunto.setCorresponsables(fu.buscarAreasCorresponsablesPorAsunto(asunto));
            asunto.setAnexos(anexoADAO.buscarAnexosPorAsunto(asunto));

            asunto.setArchivoCierre(anexoADAO.buscaxID(asunto.getIdarchivocierre()));

            //Las acciones no son necesarias ya que se consultan indpendiente solo se agrega el numero de acciones
            //asunto.setAcciones(adao.buscarAccionesPorAsunto(asunto));
            asunto.setAccionesRealizadas(adao.cantidadAcciones(asunto));
    
        }
    }    
    
    
    
    
    private void complementaDatosAsunto(AsuntoBean asunto, int idRSuperior) throws SQLException,  NamingException, Exception{
        FachadaUsuarioArea fu = new FachadaUsuarioArea();
        AnexoAsuntoDAO anexoADAO = new AnexoAsuntoDAO();
        FachadaDAO fachada = new FachadaDAO(areasConsultaCompleto);
        AccionDAO adao = new AccionDAO(areasConsultaCompleto);
        
        //Checar si esta condicion se utiliza
        if (idRSuperior == 0) {
           asunto.setResponsables(fachada.buscarResponsablesPorAsuntoxAreasM(asunto));
        } else {
           asunto.setResponsables(fachada.buscarResponsablesxAsuntoAsignadoM(idRSuperior, asunto));
        }   

        if (asunto.getIdarea() > 0){
            asunto.setResponsable(fu.buscaArea(asunto.getIdarea()));
        } else if (asunto.getTipoasunto().equals("M")){
              asunto.setResponsable(fu.buscaArea(asunto.getResponsables().get(0).getArea().getIdarea()));
        }
        asunto.setCorresponsables(fu.buscarAreasCorresponsablesPorAsunto(asunto));
        asunto.setAnexos(anexoADAO.buscarAnexosPorAsunto(asunto));

        if (asunto.getIdarchivocierre() > 0 ){
          asunto.setArchivoCierre(anexoADAO.buscaxID(asunto.getIdarchivocierre()));
        }  

        asunto.setAccionesRealizadas(adao.cantidadAcciones(asunto));
        
    }   
    
    
    public void actualizaInfo(AsuntoBean asunto, String campo) throws SQLException,  NamingException {
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       String sql="";
       Object[] params = new Object[3];
       if(campo.equals("Descripcion")) {
           sql = "update controlasuntospendientesnew.asunto " +
                    "set descripcion=? where idasunto=? and tipoasunto=?";
           params[0]=asunto.getDescripcion();
       }
       if(campo.equals("Instruccion") || campo.equals("Tema")) {
           sql = "update controlasuntospendientesnew.asunto " +
                    "set estatustexto=? where idasunto=? and tipoasunto=?";
           params[0]=asunto.getEstatustexto();
       }
       if(campo.equals("FechaEnvio")) {
           sql = "update controlasuntospendientesnew.asunto " +
                    "set fechaingreso=? where idasunto=? and tipoasunto=?";
           params[0]=asunto.getFechaingreso();
       }
       if(campo.equals("FechaVencimiento")) {
           sql = "update controlasuntospendientesnew.asunto " +
                    "set fechaatender=? where idasunto=? and tipoasunto=?";
           params[0]=asunto.getFechaatender();
       }
       if(campo.equals("Comisionados")) {
           sql = "update controlasuntospendientesnew.asunto " +
                    "set asistentes=? where idasunto=? and tipoasunto=?";
           params[0]=asunto.getAsistentes();
       }
       
       params[1]=asunto.getIdasunto();
       params[2]=asunto.getTipoasunto();
       qr.update(sql,params);
    }
    public void cambiaEstatus(int idasunto) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "update controlasuntospendientesnew.asunto set estatus='P', fechaatendertexto = '' where idasunto = ? ";
        qr.update(sql, idasunto);
    }
    
    public ResumenPromediosDTO resumenPromedios(int idarea, String tipoasunto, String estatus, String fec_ini, String fec_fin) throws SQLException,  NamingException, Exception{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds);
       String[] alias = new String[3];
       String campo="";
       if(tipoasunto.equals("K")) {
           if(estatus.equals("A")) {
               alias[0]="sia_asuntos_atend";
               alias[1]="sia_dias_atend";
               alias[2]="sia_prom_atend";
               campo="b.diasatencion";
           } else {
               alias[0]="sia_asuntos_pend";
               alias[1]="sia_dias_pend";
               alias[2]="sia_prom_pend";
               campo="b.diasretraso";
           }
       }
       if(tipoasunto.equals("C")) {
           if(estatus.equals("A")) {
               alias[0]="correo_asuntos_atend";
               alias[1]="correo_dias_atend";
               alias[2]="correo_prom_atend";
               campo="b.diasatencion";
           } else {
               alias[0]="correo_asuntos_pend";
               alias[1]="correo_dias_pend";
               alias[2]="correo_prom_pend";
               campo="b.diasretraso";
           }
       }
       if(tipoasunto.equals("M")) {
           if(estatus.equals("A")) {
               alias[0]="comision_asuntos_atend";
               alias[1]="comision_dias_atend";
               alias[2]="comision_prom_atend";
               campo="b.diasatencion";
           } else {
               alias[0]="comision_asuntos_pend";
               alias[1]="comision_dias_pend";
               alias[2]="comision_prom_pend";
               campo="b.diasretraso";
           }
       }
//       String sql ="select a.tipoasunto,count(1) as "+alias[0]+", sum(diasatencion) as "+alias[1]+", avg(r.diasatencion) as "+alias[2]+" "
//               + "from controlasuntospendientesnew.asunto a "
//               + "inner join controlasuntospendientesnew.responsable as r on a.idasunto=r.idasunto "
//               + "where r.idarea=? and a.tipoasunto=? and a.estatus=? and substring(a.fechaingreso,1,4)=? group by a.tipoasunto";
       
       String sql="select a.tipoasunto,count(1) as "+alias[0]+", sum("+campo+") as "+alias[1]+", avg("+campo+") "+alias[2]+" "
               + "from controlasuntospendientesnew.asunto a "
               + "inner join controlasuntospendientesnew.responsable as b on a.idasunto=b.idasunto "
               + "where a.idasunto in (select idasunto from controlasuntospendientesnew.responsable where idarea=?)  "
               + "and a.tipoasunto=? and a.estatus=? and a.fechaingreso >= ? and a.fechaingreso <= ? group by a.tipoasunto";
       ResultSetHandler rsh = new BeanHandler(ResumenPromediosDTO.class);
       Object[] params = {idarea, tipoasunto, estatus, fec_ini, fec_fin};
       ResumenPromediosDTO dato = (ResumenPromediosDTO) qr.query(sql, rsh, params);
       if(dato == null) {
           ResumenPromediosDTO d = new ResumenPromediosDTO();
           d.setSia_asuntos_atend(0);
           d.setSia_asuntos_pend(0);
           d.setSia_dias_atend(0);
           d.setSia_dias_pend(0);
           d.setSia_prom_atend(0);
           d.setSia_prom_pend(0);
           d.setCorreo_asuntos_atend(0);
           d.setCorreo_asuntos_pend(0);
           d.setCorreo_dias_atend(0);
           d.setCorreo_dias_pend(0);
           d.setCorreo_prom_atend(0);
           d.setCorreo_prom_pend(0);
           d.setComision_asuntos_atend(0);
           d.setComision_asuntos_pend(0);
           d.setComision_dias_atend(0);
           d.setComision_dias_pend(0);
           d.setComision_prom_atend(0);
           d.setComision_prom_pend(0);
           return d;
       } else return dato;
    }
    public ResumenPromediosDTO resumenPromediosAcuerdos(int idarea, String fec_ini, String fec_fin) throws SQLException,  NamingException, Exception{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       String sql ="select 'Acuerdo' as asunto, "
               + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where estatus='A' and fecha >=? and fecha <=? and activoestatus <> 'CNV')) as acuerdo_asuntos_atend, "
               + "(select sum(diasatencion) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where estatus='A' and fecha >=? and fecha <=? and activoestatus <> 'CNV')) as acuerdo_dias_atend, "
               + "(select avg(diasatencion) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where estatus='A' and fecha >=? and fecha <=? and activoestatus <> 'CNV')) as acuerdo_prom_atend, "
               + "(select count(1) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where estatus='P' and fecha >=? and fecha <=? and activoestatus <> 'CNV')) as acuerdo_asuntos_pend, "
               + "(select sum(diasretraso) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where estatus='P' and fecha >=? and fecha <=? and activoestatus <> 'CNV')) as acuerdo_dias_atend, "
               + "(select avg(diasretraso) from controlasuntospendientesnew.responsable where idarea=? and idaccion in (select idaccion from controlasuntospendientesnew.accion where estatus='P' and fecha >=? and fecha <=? and activoestatus <> 'CNV')) as acuerdo_prom_pend "
               + "from controlasuntospendientesnew.responsable as a where a.idarea=? group by a.idarea";
        ResultSetHandler rsh = new BeanHandler(ResumenPromediosDTO.class);
        Object[] params = {idarea, fec_ini, fec_fin, idarea, fec_ini, fec_fin, idarea, fec_ini, fec_fin, idarea, fec_ini, fec_fin, idarea, fec_ini, fec_fin, idarea, fec_ini, fec_fin, idarea};
        ResumenPromediosDTO dato = (ResumenPromediosDTO) qr.query(sql, rsh, params);
        if(dato == null) {
            ResumenPromediosDTO d = new ResumenPromediosDTO();
            d.setAcuerdo_asuntos_atend(0);
            d.setAcuerdo_asuntos_pend(0);
            d.setAcuerdo_dias_atend(0);
            d.setAcuerdo_dias_pend(0);
            d.setAcuerdo_prom_atend(0);
            d.setAcuerdo_prom_pend(0);
            return d;
        } else return dato;
    }
    public List<AsuntoBean> listaActualizaDiasRetraso() throws SQLException,  NamingException, Exception{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds);
       String sql = "select a.idasunto,a.fechaingreso from controlasuntospendientesnew.asunto as a "
               + "inner join controlasuntospendientesnew.responsable as b on a.idasunto=b.idasunto where b.estatus='P' ";
       ResultSetHandler rsh = new BeanListHandler(AsuntoBean.class);
       List<AsuntoBean> datos = (List<AsuntoBean>) qr.query(sql, rsh);
       return datos; 
    }
    public void actualizaRetrasos(int diasretraso, int idasunto) throws SQLException,  NamingException {
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       String sql="update controlasuntospendientesnew.responsable " +
                    "set diasretraso=? where idasunto=? and estatus='P'";
       qr.update(sql,diasretraso,idasunto);
    }

    public List<AccionDTO> listaActualizaDiasRetrasoAcuerdos() throws SQLException,  NamingException, Exception{
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds);
       String sql = "select a.idaccion,a.acuerdo_fecha from controlasuntospendientesnew.accion as a "
               + "inner join controlasuntospendientesnew.responsable as b on a.idaccion=b.idaccion where a.estatus='P' and activoestatus <> 'CNV' ";
       ResultSetHandler rsh = new BeanListHandler(AccionDTO.class);
       List<AccionDTO> datos = (List<AccionDTO>) qr.query(sql, rsh);
       return datos; 
    }
    public void actualizaRetrasosAcuerdos(int diasretraso, int idasunto) throws SQLException,  NamingException {
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       String sql="update controlasuntospendientesnew.responsable " +
                    "set diasretraso=? where idaccion=? and estatus='P'";
       qr.update(sql,diasretraso,idasunto);
    }
    // segundo cuadro resumen del correo
    public ResumenAreaSM resumenSemanalMens(String tipo, String fecha1, String fecha2, int idarea) throws SQLException,  NamingException, Exception{
        char t = tipo.charAt(0);
        String tipoAs="";
        switch(t)
        {
            case 'K' : tipoAs="SIA"; break;
            case 'C' : tipoAs="CORREOS"; break;
            case 'M' : tipoAs="COMISIONES"; break;
            case 'R' : tipoAs="REUNIONES"; break;
            case 'A' : tipoAs="ACUERDOS"; break;
        }        
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        String sql="select '"+tipo+"' as tipoAbreviado, b.idarea,b.siglas,b.nombre,b.nivel, '"+tipoAs+"' as tipoasunto, "
                + "(SELECT count(1) FROM controlasuntospendientesnew.responsable as resp where (substring(fechaasignado,1,8) >= ? and substring(fechaasignado,1,8) <= ?) and idarea=? and idasunto in (select idasunto FROM controlasuntospendientesnew.asunto where tipoasunto=? )) as enviados, "
                + "(SELECT count(1) FROM controlasuntospendientesnew.responsable as resp where estatus='A' and (substring(fechaatencion,1,8) >= ? and substring(fechaatencion,1,8) <= ?) and idarea=? and idasunto in (select idasunto FROM controlasuntospendientesnew.asunto where tipoasunto=? )) as atendidos, "
                + "(SELECT count(1) FROM controlasuntospendientesnew.responsable as resp where estatus='P' and idarea=? and resp.idasunto in (select idasunto FROM controlasuntospendientesnew.asunto where tipoasunto=? )) as pendientes "
                + "from controlasuntospendientesnew.responsable as a "
                + "inner join controlasuntospendientesnew.area as b on a.idarea = b.idarea "
                + "where a.idarea=? group by a.idarea,b.siglas,b.idarea";
        Object[] params = {fecha1+"0000",fecha2+"9999",idarea,tipo,fecha1,fecha2,idarea,tipo,idarea,tipo,idarea};
        ResultSetHandler rsh = new BeanHandler(ResumenAreaSM.class);
        ResumenAreaSM datos = (ResumenAreaSM) qr.query(sql, rsh, params);
        return datos;
    }
    // segundo cuadro resumen del correo
    public ResumenAreaSM resumenSemanalMensTots(String tipo, String fecha1, String fecha2) throws SQLException,  NamingException, Exception{
        char t = tipo.charAt(0);
        String tipoAs="";
        switch(t)
        {
            case 'K' : tipoAs="SIA"; break;
            case 'C' : tipoAs="CORREOS"; break;
            case 'M' : tipoAs="COMISIONES"; break;
            case 'R' : tipoAs="REUNIONES"; break;
            case 'A' : tipoAs="ACUERDOS"; break;
        }        
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        String sql="select '"+tipo+"' as tipoAbreviado, '"+tipoAs+"' as tipoasunto, "
                + "(SELECT count(1) FROM controlasuntospendientesnew.asunto where (substring(fechaingreso,1,8) >= ? and substring(fechaingreso,1,8) <= ?) and tipoasunto=? ) as enviados, "
                + "(SELECT count(1) FROM controlasuntospendientesnew.asunto where estatus='A' and (substring(fechaatendertexto,1,8) >= ? and substring(fechaatendertexto,1,8) <= ?) and tipoasunto=? ) as atendidos, "
                + "(SELECT count(1) FROM controlasuntospendientesnew.asunto where estatus='P' and tipoasunto=? and idasunto in (select idasunto from controlasuntospendientesnew.responsable) ) as pendientes ";
        Object[] params = {fecha1+"0000",fecha2+"9999",tipo,fecha1,fecha2,tipo,tipo};
        ResultSetHandler rsh = new BeanHandler(ResumenAreaSM.class);
        ResumenAreaSM datos = (ResumenAreaSM) qr.query(sql, rsh, params);
        return datos;
    }
    public ResumenAreaSM resumenSemanalMensAcuerdos(String fecha1, String fecha2, int idarea) throws SQLException,  NamingException, Exception{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        String sql="select 'A' as tipoAbreviado,b.idarea,b.siglas,b.nombre,b.nivel, 'ACUERDOS' as tipoasunto, "
                + "(SELECT count(1) FROM controlasuntospendientesnew.responsable as resp where (substring(fechaasignado,1,8) >= ? and substring(fechaasignado,1,8) <= ?) and idarea=? and idaccion in (select idaccion FROM controlasuntospendientesnew.accion where (activoestatus <> 'CNV' or activoestatus is null) )) as enviados, "
                + "(SELECT count(1) FROM controlasuntospendientesnew.responsable as resp where estatus='A' and (substring(fechaatencion,1,8) >= ? and substring(fechaatencion,1,8) <= ?) and idarea=? and idaccion in (select idaccion FROM controlasuntospendientesnew.accion where estatus='A' and (activoestatus <> 'CNV' or activoestatus is null))) as atendidos, "
                + "(SELECT count(1) FROM controlasuntospendientesnew.responsable as resp where estatus='P' and idarea=? and resp.idaccion in (select idaccion FROM controlasuntospendientesnew.accion where estatus='P' and (activoestatus <> 'CNV' or activoestatus is null) )) as pendientes "
                + "from controlasuntospendientesnew.responsable as a  "
                + "inner join controlasuntospendientesnew.area as b on a.idarea = b.idarea "
                + "where a.idarea=? group by a.idarea,b.siglas,b.idarea";
        Object[] params = {fecha1,fecha2,idarea,fecha1,fecha2,idarea,idarea,idarea};
        ResultSetHandler rsh = new BeanHandler(ResumenAreaSM.class);
        ResumenAreaSM datos = (ResumenAreaSM) qr.query(sql, rsh, params);
        return datos;
    }
    public ResumenAreaSM resumenSemanalMensAcuerdosTots(String fecha1, String fecha2) throws SQLException,  NamingException, Exception{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        String sql="select 'A' as tipoAbreviado, 'ACUERDOS' as tipoasunto, "
                + "(SELECT count(1) FROM controlasuntospendientesnew.accion where (substring(fechaoriginal,1,8) >= ? and substring(fechaoriginal,1,8) <= ?) and (activoestatus <> 'CNV' or activoestatus is null) ) as enviados, "
                + "(SELECT count(1) FROM controlasuntospendientesnew.accion where estatus='A' and (substring(acuerdo_fecha,1,8) >= ? and substring(acuerdo_fecha,1,8) <= ?) and (activoestatus <> 'CNV' or activoestatus is null)) as atendidos, "
                + "(SELECT count(1) FROM controlasuntospendientesnew.accion where estatus='P' and (activoestatus <> 'CNV' or activoestatus is null)) as pendientes ";
        Object[] params = {fecha1,fecha2,fecha1,fecha2};
        ResultSetHandler rsh = new BeanHandler(ResumenAreaSM.class);
        ResumenAreaSM datos = (ResumenAreaSM) qr.query(sql, rsh, params);
        return datos;
    }
    public ResumenAreaSM resumenSemanalMensReuniones(String fecha2, int idarea) throws SQLException,  NamingException, Exception{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        String sql="select 'R' as tipoAbreviado, b.idarea,b.siglas,b.nombre,b.nivel, 'REUNIONES' as tipoasunto, count(1) as pendientes "
                + "from controlasuntospendientesnew.asunto as a "
                + "inner join controlasuntospendientesnew.area as b on a.idarea = b.idarea  "
                + "where a.idarea=? and idasunto not in (select idasunto from controlasuntospendientesnew.accion) "
                + "and a.tipoasunto ='R' and (substring(fechaingreso,1,8) >= '20100101' and substring(fechaingreso,1,8) <= ?) group by b.siglas,b.idarea";
        Object[] params = {idarea,fecha2};
        ResultSetHandler rsh = new BeanHandler(ResumenAreaSM.class);
        ResumenAreaSM datos = (ResumenAreaSM) qr.query(sql, rsh, params);
        return datos;
    }
    public ResumenAreaSM resumenSemanalMensReunionesTots(String fecha2) throws SQLException,  NamingException, Exception{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        String sql="select 'R' as tipoAbreviado, 'REUNIONES PENDIENTES DE REGISTRAR ACUERDOS' as tipoasunto, count(1) as pendientes \n" +
"                from controlasuntospendientesnew.asunto as a \n" +
"                inner join controlasuntospendientesnew.area as b on a.idarea = b.idarea  \n" +
"                where idasunto not in (select idasunto from controlasuntospendientesnew.accion) \n" +
"                and a.tipoasunto ='R' and (substring(fechaingreso,1,8) >= '20100101' and substring(fechaingreso,1,8) <= ?) ";
        
        Object[] params = {fecha2};
        ResultSetHandler rsh = new BeanHandler(ResumenAreaSM.class);
        ResumenAreaSM datos = (ResumenAreaSM) qr.query(sql, rsh, params);
        return datos;
    }
    public ResumenAsuntoArea resumenMensAll(String tipoAs, String tipo, String fecha1, String fecha2, String fechaManiana) throws SQLException,  NamingException, Exception{
        String sql = "SELECT '"+tipoAs+"' as tipoasunto, "
        +"(select count(1) FROM controlasuntospendientesnew.asunto where tipoasunto='"+tipo+"' and (fechaatender < '"+fecha2+"' OR fechaatender <= '"+fechaManiana+"' ) and estatus='P' ) as vencidos, "
        +"(select count(1) FROM controlasuntospendientesnew.asunto where tipoasunto='"+tipo+"' and (fechaatender >= '"+fecha1+"' AND fechaatender <= '"+fecha2+"') and estatus= 'P' ) as porvencer, "
        +"(select count(1) FROM controlasuntospendientesnew.asunto where tipoasunto='"+tipo+"' and fechaatender > '"+fecha1+"' and estatus = 'P') as activos";
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        //Object[] params = {fecha1+"0000",fecha2+"9999",idarea,tipo,fecha1,fecha2,idarea,tipo,idarea,tipo,idarea};
        ResultSetHandler rsh = new BeanHandler(ResumenAsuntoArea.class);
        ResumenAsuntoArea datos = (ResumenAsuntoArea) qr.query(sql, rsh);
        return datos;
    }
    public ResumenAsuntoArea resumenMensReunionAll(String fecha1, String fecha2) throws SQLException,  NamingException, Exception{
        String sql = "select 'R' as tipoAbreviado, 'REUNIONES PENDIENTES DE REGISTRAR ACUERDOS' as tipoasunto, count(1) as vencidos "
                +"from controlasuntospendientesnew.asunto as a "
                +"where estatus='P' and idasunto not in (select idasunto from controlasuntospendientesnew.accion group by idasunto) "
                +"and a.tipoasunto ='R' and (substring(fechaingreso,1,8) >= '"+fecha1+"' and substring(fechaingreso,1,8) <= '"+fecha2+"' )";
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        ResultSetHandler rsh = new BeanHandler(ResumenAsuntoArea.class);
        ResumenAsuntoArea datoR = (ResumenAsuntoArea) qr.query(sql, rsh);
        return datoR;
    }
    public ResumenAsuntoArea resumenMensAcuerdosAll(String fecha1, String fecha2, String fechaManiana) throws SQLException,  NamingException, Exception{
        String sql = "SELECT 'A' as tipoAbreviado, 'ACUERDOS' as tipoasunto, \n" +
            "  (select count(1) FROM controlasuntospendientesnew.accion where estatus='P' and (activoestatus <> 'CNV' or activoestatus is null) and (acuerdo_fecha < '"+fecha2+"' OR acuerdo_fecha <= '"+fechaManiana+"' ) ) as vencidos,\n" +
            "  (select count(1) FROM controlasuntospendientesnew.accion where estatus='P' and (activoestatus <> 'CNV' or activoestatus is null) and (acuerdo_fecha >= '"+fecha1+"' and acuerdo_fecha <= '"+fecha2+"')) as porvencer,\n" +
            "  (select count(1) FROM controlasuntospendientesnew.accion where estatus='P' and (activoestatus <> 'CNV' or activoestatus is null) and acuerdo_fecha > '"+fecha1+"' ) as activos";
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        ResultSetHandler rsh = new BeanHandler(ResumenAsuntoArea.class);
        ResumenAsuntoArea datoA = (ResumenAsuntoArea) qr.query(sql, rsh);
        return datoA;
    }
    ////////////// tmp
    public List<AsuntoDTO> idsAsunto() throws SQLException,  NamingException, Exception{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select idasunto FROM controlasuntospendientesnew.asunto where estatus = 'A'";
        ResultSetHandler rsh = new BeanListHandler(AsuntoDTO.class);
        List<AsuntoDTO> datos = (List<AsuntoDTO>) qr.query(sql, rsh);
        return datos;
    }
    public void fechaMax(int idasunto) throws SQLException,  NamingException, Exception{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select coalesce(fechaatencion, '') as fechaatendertexto FROM controlasuntospendientesnew.responsable where estatus='A' and idasunto = ? order by fechaatencion desc limit 1";
        ResultSetHandler rsh = new BeanHandler(AsuntoDTO.class);
        AsuntoDTO dato = (AsuntoDTO) qr.query(sql, rsh, idasunto);
        if(dato != null) {
            sql = "update controlasuntospendientesnew.asunto set fechaatendertexto = '"+dato.getFechaatendertexto()+"' where idasunto = ?";
            qr.update(sql,idasunto);
        }
    }
}
