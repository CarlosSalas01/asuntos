/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.baseDatos;

import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;
import javax.sql.DataSource;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AreaDTO;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AsuntoBean;
import mx.org.inegi.dggma.sistemas.asuntos.dto.Cantidad;
import mx.org.inegi.dggma.sistemas.asuntos.dto.ResponsableDTO;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author jacqueline
 */
public class ResponsableDAO {
    private String clausulaOrder = "order by fechaasignado,idresponsable";
    private List<AreaDTO> areasConsultaR;
    
    public ResponsableDAO(AreasConsulta areasC) {
       if (areasC != null) { 
         this.areasConsultaR = areasC.getAreasComparteResponsabilidad();
       }  
    }
    
    public void insertarResponsableAsunto(ResponsableBean responsable) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "insert into controlasuntospendientesnew.responsable " +
                     "(idasunto, idarea, avance, fechaatencion, diasatencion, diasretraso,estatus,"+
                     "delegado,asignadopor,fechaasignado, instruccion) "+
                     " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
                    
        
        Object[] params = {responsable.getDatos().getIdasunto(),responsable.getArea().getIdarea(),responsable.getDatos().getAvance(),
                           responsable.getDatos().getFechaatencion(),responsable.getDatos().getDiasatencion(),
                           responsable.getDatos().getDiasretraso(),responsable.getDatos().getEstatus(),
                           responsable.getDatos().getDelegado(), responsable.getDatos().getAsignadopor(),
                           responsable.getDatos().getFechaasignado(),responsable.getDatos().getInstruccion()}; 
        
        qr.update(sql,params);
        
    }
    
     public void insertarResponsableAccion(ResponsableBean responsable) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "insert into controlasuntospendientesnew.responsable " +
                     "(idaccion, idarea, avance, fechaatencion, diasatencion, diasretraso,estatus,"+
                     "delegado, asignadopor,fechaasignado, instruccion) "+
                     " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?) ";
                    
        
        Object[] params = {responsable.getDatos().getIdaccion(),responsable.getArea().getIdarea(),responsable.getDatos().getAvance(),
                           responsable.getDatos().getFechaatencion(),responsable.getDatos().getDiasatencion(),
                           responsable.getDatos().getDiasretraso(),responsable.getDatos().getEstatus(),
                           responsable.getDatos().getDelegado(), responsable.getDatos().getAsignadopor(),
                           responsable.getDatos().getFechaasignado(),responsable.getDatos().getInstruccion()}; 
        
        qr.update(sql,params);
    }
     
     public void insertarResponsableAccion(ResponsableDTO responsable) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "insert into controlasuntospendientesnew.responsable " +
                     "(idaccion, idarea, avance, fechaatencion, diasatencion, diasretraso,estatus,"+
                     "delegado, asignadopor,fechaasignado, instruccion) "+
                     " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?) ";
                    
        
        Object[] params = {responsable.getIdaccion(),responsable.getIdarea(),responsable.getAvance(),
                           responsable.getFechaatencion(),responsable.getDiasatencion(),
                           responsable.getDiasretraso(),responsable.getEstatus(),
                           responsable.getDelegado(), responsable.getAsignadopor(),
                           responsable.getFechaasignado(),responsable.getInstruccion()}; 
        
        qr.update(sql,params);
        
    }

     public void updateResponsable(ResponsableBean responsable) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "UPDATE controlasuntospendientesnew.responsable " +
                     "SET avance=?, fechaatencion=?, diasatencion=?, diasretraso=?, estatus=?, " +
                     "delegado = ? , idarea = ?, idasunto=?, idaccion =?, asignadopor= ?, "+
                     "fechaasignado =?, instruccion=? , comentario = ?"+ 
                     "WHERE idresponsable = ?";
        
        Object[] params = {responsable.getDatos().getAvance(), 
                           responsable.getDatos().getFechaatencion(),
                           responsable.getDatos().getDiasatencion(), 
                           responsable.getDatos().getDiasretraso(),
                           responsable.getDatos().getEstatus(), 
                           responsable.getDatos().getDelegado(),
                           responsable.getArea().getIdarea(), 
                           responsable.getDatos().getIdasunto(),
                           responsable.getDatos().getAsignadopor(),
                           responsable.getDatos().getInstruccion(),
                           responsable.getDatos().getComentario(),
                           responsable.getDatos().getIdresponsable(),
                           };
        
        qr.update(sql,params);
        
    }
     
     public void actualizaEstatusResponsable(String estatus,int idresponsable) throws NamingException, SQLException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "UPDATE controlasuntospendientesnew.responsable " +
                     "SET estatus=? WHERE idresponsable = ?";
        
        Object[] params = {estatus,idresponsable};
        
        qr.update(sql,params);
     }
     
     public void updateResponsable(ResponsableDTO responsableDto) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "UPDATE controlasuntospendientesnew.responsable " +
                     "SET avance=?, fechaatencion=?, diasatencion=?, diasretraso=?, estatus=?, " +
                     "delegado = ? , idarea = ?, idasunto=?, asignadopor= ?, "+
                     "fechaasignado =?, instruccion=? , comentario = ? "+ 
                     "WHERE idresponsable = ?";
        
        Object[] params = {responsableDto.getAvance(), responsableDto.getFechaatencion(),
                           responsableDto.getDiasatencion(), responsableDto.getDiasretraso(),
                           responsableDto.getEstatus(), responsableDto.getDelegado(),
                           responsableDto.getIdarea(), responsableDto.getIdasunto(),
                           responsableDto.getAsignadopor(),
                           responsableDto.getFechaasignado(),
                           responsableDto.getInstruccion(),
                           responsableDto.getComentario(),
                           responsableDto.getIdresponsable()
                           };
        
        qr.update(sql,params);
        
    }
     
     public void updateResponsableAcuerdo(ResponsableDTO responsableDto) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "UPDATE controlasuntospendientesnew.responsable " +
                     "SET avance=?, fechaatencion=?, diasatencion=?, diasretraso=?, estatus=?, " +
                     "delegado = ? , idarea = ?, asignadopor= ?, "+
                     "fechaasignado =?, instruccion=? , comentario = ? "+ 
                     "WHERE idresponsable = ?";
        
        Object[] params = {responsableDto.getAvance(), responsableDto.getFechaatencion(),
                           responsableDto.getDiasatencion(), responsableDto.getDiasretraso(),
                           responsableDto.getEstatus(), responsableDto.getDelegado(),
                           responsableDto.getIdarea(), 
                           responsableDto.getAsignadopor(),
                           responsableDto.getFechaasignado(),
                           responsableDto.getInstruccion(),
                           responsableDto.getComentario(),
                           responsableDto.getIdresponsable()
                           };
        
        qr.update(sql,params);
        
    }
     
     public void updateAvanceResponsable(ResponsableDTO responsable) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "UPDATE controlasuntospendientesnew.responsable " +
                     "SET avance=?, fechaatencion=?, diasatencion=?, diasretraso=?, estatus=? "+
                     "WHERE idresponsable = ?";
        
        Object[] params = {responsable.getAvance(), responsable.getFechaatencion(),
                           responsable.getDiasatencion(), responsable.getDiasretraso(),
                           responsable.getEstatus(),
                           responsable.getIdresponsable()};
        
        qr.update(sql,params);
        
    }
     
    public void updateDelegaAsunto(int idasunto, int idresponsable, String valor) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "UPDATE controlasuntospendientesnew.responsable " +
                     "SET delegado = ? "+ 
                     "WHERE idasunto=? and idarea = ?";
        
        Object[] params = {valor, idasunto, idresponsable};
        
        qr.update(sql,params);
        
    } 
    
    public void updateDelegaAcuerdo(int idacuerdo, int idResponsable, String valor) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "UPDATE controlasuntospendientesnew.responsable " +
                     "SET delegado = ? "+ 
                     "WHERE idaccion=? and idarea = ?";
        
        Object[] params = {valor, idacuerdo,idResponsable};
        
        qr.update(sql,params);
        
    } 
     
    public void eliminarResponsable(ResponsableBean responsable) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 

        String sql = "delete from controlasuntospendientesnew.responsable " +
                     "where idresponsable=?";
        
        Object[] params = {responsable.getDatos().getIdresponsable()};
        
        qr.update(sql,params);
    }
    
     public void eliminarResponsable(int idresponsable) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 

        String sql = "delete from controlasuntospendientesnew.responsable " +
                     "where idresponsable=?";
        
        Object[] params = {idresponsable};
        
        qr.update(sql,params);
    }
    

    public boolean existeResponsableEnAsunto(AsuntoBean asunto, ResponsableBean responsable) throws SQLException,  NamingException {
    
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select count(*) as cantidad from controlasuntospendientesnew.responsable" +
                    " where idasunto=? and idarea=?";
        
         Object[] params = {asunto.getIdasunto(),responsable.getArea().getIdarea()};

         ResultSetHandler rsh = new BeanHandler(Cantidad.class);
         Cantidad cantidad = (Cantidad) qr.query(sql, rsh, params);
         
         return cantidad.getCantidad() > 0;
         
    }

    public ResponsableDTO obtenResponsablexId(int idresponsable) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select * from controlasuntospendientesnew.responsable " +
                    " where idresponsable=? ";
        
         Object[] params = {idresponsable};

         ResultSetHandler rsh = new BeanHandler(ResponsableDTO.class);
         ResponsableDTO datos = (ResponsableDTO) qr.query(sql, rsh, params);

         return datos;
    }
    
    public ResponsableDTO obtenResponsablexAsunto(int idarea, int idasunto) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select * from controlasuntospendientesnew.responsable" +
                    " where idarea = ? and idasunto=? ";
                    
        
         Object[] params = {idarea, idasunto};

         ResultSetHandler rsh = new BeanHandler(ResponsableDTO.class);
         ResponsableDTO datos = (ResponsableDTO) qr.query(sql, rsh, params);

         return datos;
    }
    
    public ResponsableDTO obtenResponsablexAsuntoNoCancelado(int idarea, int idasunto) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select * from controlasuntospendientesnew.responsable" +
                    " where idarea = ? and idasunto=? "+
                    " and estatus <> 'C'"; // se agreg� esta condici�n para no traer al responsable con cancelaciones
        
         Object[] params = {idarea, idasunto};

         ResultSetHandler rsh = new BeanHandler(ResponsableDTO.class);
         ResponsableDTO datos = (ResponsableDTO) qr.query(sql, rsh, params);

         return datos;
    }
    public ResponsableDTO obtenResponsablexAcuerdo(int idarea, int idacuerdo) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select * from controlasuntospendientesnew.responsable" +
                    " where idarea = ? and idaccion=?";
        
         Object[] params = {idarea, idacuerdo};

         ResultSetHandler rsh = new BeanHandler(ResponsableDTO.class);
         ResponsableDTO datos = (ResponsableDTO) qr.query(sql, rsh, params);

         return datos;
    }
    
    public ResponsableDTO obtenResponsablexAcuerdoNoCancelado(int idarea, int idaccion) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select * from controlasuntospendientesnew.responsable" +
                    " where idarea = ? and idaccion= ? " +
                    " and estatus <> 'C'"; // se agreg� esta condici�n para no traer al responsable con cancelaciones
        
         Object[] params = {idarea, idaccion};

         ResultSetHandler rsh = new BeanHandler(ResponsableDTO.class);
         ResponsableDTO datos = (ResponsableDTO) qr.query(sql, rsh, params);

         return datos;
    }
    
    public ResponsableDTO obtenResponsablexAsuntoAsignado(int idareaSuperior,int idarea, int idasunto) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select * from controlasuntospendientesnew.responsable" +
                    " where idarea = ? and idasunto=? and asignadopor = ?";
        
         Object[] params = {idarea, idasunto, idareaSuperior};

         ResultSetHandler rsh = new BeanHandler(ResponsableDTO.class);
         ResponsableDTO datos = (ResponsableDTO) qr.query(sql, rsh, params);

         return datos;
    }
    public ResponsableDTO obtenDlgEliminar(int idareaSuperior, int idasunto) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select * from controlasuntospendientesnew.responsable" +
                    " where idasunto=? and asignadopor = ?";
        Object[] params = {idasunto, idareaSuperior};
        ResultSetHandler rsh = new BeanHandler(ResponsableDTO.class);
        ResponsableDTO datos = (ResponsableDTO) qr.query(sql, rsh, params);
        return datos;
    }
    
     public ResponsableDTO obtenResponsablexAcuerdoAsignado(int idareaSuperior,int idarea, int idacuerdo) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select * from controlasuntospendientesnew.responsable" +
                    " where idarea = ? and idaccion=? and asignadopor = ?";
        
         Object[] params = {idarea, idacuerdo, idareaSuperior};

         ResultSetHandler rsh = new BeanHandler(ResponsableDTO.class);
         ResponsableDTO datos = (ResponsableDTO) qr.query(sql, rsh, params);

         return datos;
    }
    
        
    public List<ResponsableDTO> obtenResponsablesxAsunto(AsuntoBean asunto) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select * from controlasuntospendientesnew.responsable" +
                    " where idasunto=? " + clausulaOrder;
        
         Object[] params = {asunto.getIdasunto()};

         ResultSetHandler rsh = new BeanListHandler(ResponsableDTO.class);
         List<ResponsableDTO> datos = (List<ResponsableDTO>) qr.query(sql, rsh, params);
         
         return datos;
    }
    
    public List<ResponsableDTO> obtenResponsablesxAsuntoxAreas(AsuntoBean asunto) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select * from controlasuntospendientesnew.responsable" +
                    " where (";
        
       
        Object[] params = new Object[areasConsultaR.size()+1];

        for(int i = 0;i<areasConsultaR.size();i++){
            sql = sql + "idarea = ? or ";
            params[i] = areasConsultaR.get(i).getIdarea();
        }                       
        sql = sql.substring(0, sql.length()-3) + ") and idasunto=? "+ clausulaOrder;
        params[areasConsultaR.size()] = asunto.getIdasunto();

        ResultSetHandler rsh = new BeanListHandler(ResponsableDTO.class);
        List<ResponsableDTO> datos = (List<ResponsableDTO>) qr.query(sql, rsh, params);
         
        return datos;
    }
    
    public List<ResponsableDTO> obtenResponsablesxAsuntoxAreas(int idasunto) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select * from controlasuntospendientesnew.responsable" +
                    " where (";
        
       
        Object[] params = new Object[areasConsultaR.size()+1];

        for(int i = 0;i<areasConsultaR.size();i++){
            sql = sql + "idarea = ? or ";
            params[i] = areasConsultaR.get(i).getIdarea();
        }                       
        sql = sql.substring(0, sql.length()-3) + ") and idasunto=? "+ clausulaOrder;;
        params[areasConsultaR.size()] = idasunto;

        ResultSetHandler rsh = new BeanListHandler(ResponsableDTO.class);
        List<ResponsableDTO> datos = (List<ResponsableDTO>) qr.query(sql, rsh, params);
         
        return datos;
    }
    
    
    
    
    public List<ResponsableDTO> obtenResponsablesxAcuerdoxAreas(AccionBean acuerdo) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select * from controlasuntospendientesnew.responsable" +
                    " where (";
        
        Object[] params = new Object[areasConsultaR.size()+1];

        for(int i = 0;i<areasConsultaR.size();i++){
            sql = sql + "idarea = ? or ";
            params[i] = areasConsultaR.get(i).getIdarea();
        }                       
        sql = sql.substring(0, sql.length()-3) + ") and idaccion=?" + clausulaOrder;
        params[areasConsultaR.size()] = acuerdo.getIdAccion();

        ResultSetHandler rsh = new BeanListHandler(ResponsableDTO.class);
        List<ResponsableDTO> datos = (List<ResponsableDTO>) qr.query(sql, rsh, params);
         
        return datos;
    }
    
    public List<ResponsableDTO> obtenResponsablesxAsuntoxAreaAsignada(int idasunto, int idareaSuperior) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select * from controlasuntospendientesnew.responsable" +
                    " where asignadopor = ? and idasunto = ? " + clausulaOrder;
        
        Object[] params = {idareaSuperior, idasunto};

        ResultSetHandler rsh = new BeanListHandler(ResponsableDTO.class);
        List<ResponsableDTO> datos = (List<ResponsableDTO>) qr.query(sql, rsh, params);
         
        return datos;
    }
    
    
    public List<ResponsableDTO> obtenResponsablesxAsuntoxAreaAsignadaSCancelados(int idasunto, int idareaSuperior) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select * from controlasuntospendientesnew.responsable" +
                    " where asignadopor = ? and idasunto = ? and estatus <> 'C' " + clausulaOrder;
        
        Object[] params = {idareaSuperior, idasunto};

        ResultSetHandler rsh = new BeanListHandler(ResponsableDTO.class);
        List<ResponsableDTO> datos = (List<ResponsableDTO>) qr.query(sql, rsh, params);
         
        return datos;
    }
    
    
    
     public List<ResponsableDTO> obtenResponsablesxAcuerdoxAreaAsignada(int idacuerdo, int idareaSuperior) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select * from controlasuntospendientesnew.responsable" +
                    " where asignadopor = ? and idaccion = ? "+ clausulaOrder;
        
        Object[] params = {idareaSuperior, idacuerdo};

        ResultSetHandler rsh = new BeanListHandler(ResponsableDTO.class);
        List<ResponsableDTO> datos = (List<ResponsableDTO>) qr.query(sql, rsh, params);
         
        return datos;
    }
     
     
    
    public List<ResponsableDTO> obtenResponsablesxAcuerdoxAreaAsignadaSCancelados(int idacuerdo, int idareaSuperior) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select * from controlasuntospendientesnew.responsable" +
                    " where asignadopor = ? and idaccion = ? and estatus <> 'C' "+ clausulaOrder;
        
        Object[] params = {idareaSuperior, idacuerdo};

        ResultSetHandler rsh = new BeanListHandler(ResponsableDTO.class);
        List<ResponsableDTO> datos = (List<ResponsableDTO>) qr.query(sql, rsh, params);
         
        return datos;
    }
    
    
    public List<ResponsableDTO> obtenResponsablesxAcuerdo(AccionBean acuerdo) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select * from controlasuntospendientesnew.responsable" +
                    " where idaccion=? "+ clausulaOrder;
        
         Object[] params = {acuerdo.getIdAccion()};

         ResultSetHandler rsh = new BeanListHandler(ResponsableDTO.class);
         List<ResponsableDTO> datos = (List<ResponsableDTO>) qr.query(sql, rsh, params);
         
         return datos;
    }
    
    public void eliminaRespxAsunto(int idasunto) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "delete from controlasuntospendientesnew.responsable where idasunto=?";
        Object[] params = {idasunto};
        qr.update(sql,params);
    }
    
    public void eliminaRespxAcuerdo(int idacuerdo) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "delete from controlasuntospendientesnew.responsable where idaccion=?";
        Object[] params = {idacuerdo};
        qr.update(sql,params);
    }
    
    ////////// Richardo
    
     public ResponsableDTO buscaComentarioAtendido(int idasunto, int idareaSuperior) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "SELECT substring(fechaatencion,7,2)||'/'||substring(fechaatencion,5,2)||'/'||substring(fechaatencion,1,4) as fechaatencion,comentario "
                + "FROM controlasuntospendientesnew.responsable where idasunto=? and idarea=? ";
         Object[] params = {idasunto, idareaSuperior};
         ResultSetHandler rsh = new BeanHandler(ResponsableDTO.class);
         ResponsableDTO datos = (ResponsableDTO) qr.query(sql, rsh, params);
         return datos;
     }
     
     public ResponsableDTO buscaComentarioAtendidoAcuerdos(int idaccion, int idareaSuperior) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select descripcion as comentario,substring(fecha,7,2)||'/'||substring(fecha,5,2)||'/'||substring(fecha,1,4) as fechaatencion from controlasuntospendientesnew.avance where idarea=? and idaccion=? and porcentaje=100 ";
        Object[] params = {idareaSuperior, idaccion};
        ResultSetHandler rsh = new BeanHandler(ResponsableDTO.class);
        ResponsableDTO datos = (ResponsableDTO) qr.query(sql, rsh, params);
        return datos;
    }
    public List<AreaDTO> obtenDlgs(int idasunto, int nivel) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "SELECT b.idarea,c.siglas,c.nombre from controlasuntospendientesnew.responsable as b  "
                + "inner join controlasuntospendientesnew.area as c on b.idarea=c.idarea WHERE b.idasunto = ? and c.nivel=? ";
         Object[] params = {idasunto, nivel};
         ResultSetHandler rsh = new BeanListHandler(AreaDTO.class);
         List<AreaDTO> datos = (List<AreaDTO>) qr.query(sql, rsh, params);
         return datos;
    }
    public List<AreaDTO> obtenDlgsAcuerdo(int idaccion, int nivel) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "SELECT b.idarea,c.siglas,c.nombre from controlasuntospendientesnew.responsable as b  "
                + "inner join controlasuntospendientesnew.area as c on b.idarea=c.idarea WHERE b.idaccion = ? and c.nivel=? ";
         Object[] params = {idaccion, nivel};
         ResultSetHandler rsh = new BeanListHandler(AreaDTO.class);
         List<AreaDTO> datos = (List<AreaDTO>) qr.query(sql, rsh, params);
         return datos;
    }
///////////////////////
    public void updateResponsableConvenio(ResponsableBean responsable) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "UPDATE controlasuntospendientesnew.responsable " +
                     "SET fechaatencion=?, idaccion =?, fechaasignado =? "+ 
                     "WHERE idresponsable = ?";
        Object[] params = {responsable.getDatos().getFechaatencion(),
                           responsable.getDatos().getIdaccion(), 
                           responsable.getDatos().getFechaasignado(),
                           responsable.getDatos().getIdresponsable(),
                           };
        qr.update(sql,params);
    }
     public boolean existeRespCompromiso(int idarea, int idaccion) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "SELECT * FROM controlasuntospendientesnew.responsable where idarea=? and idaccion=? ";
        Object[] params = {idarea, idaccion};
        ResultSetHandler rsh = new BeanHandler(ResponsableDTO.class);
        ResponsableDTO resultado = (ResponsableDTO) qr.query(sql, rsh, params);
        if(resultado!=null) return true;
        else return false;
    }
    public void eliminaRespxAsuntoResp(int idasunto, int idresponsable) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "delete from controlasuntospendientesnew.responsable where idasunto=? and idarea=?";
        Object[] params = {idasunto, idresponsable};
        qr.update(sql,params);
    }
    public void eliminaRespxAsuntoRespAcuerdo(int idaccion, int idresponsable) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "delete from controlasuntospendientesnew.responsable where idaccion=? and idarea=?";
        Object[] params = {idaccion, idresponsable};
        qr.update(sql,params);
    }
    
    public List<ResumenAreaSM> obtenResumenInicio(String fecha1, String fecha2, List areas) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "SELECT resp.idarea,trim(ar.siglas) as siglas, " +
            "(select count(1) from controlasuntospendientesnew.asunto a, controlasuntospendientesnew.responsable b where a.idasunto=b.idasunto and b.idarea=resp.idarea and a.estatus='A' and a.fechaatendertexto >= '"+fecha1+"' and a.fechaatendertexto <= '"+fecha2+"') as atendidos, " +
            "(select count(1) from controlasuntospendientesnew.asunto a, controlasuntospendientesnew.responsable b where a.idasunto=b.idasunto and b.idarea=resp.idarea and a.estatus='P' and a.fechaingreso >= '"+fecha1+"' and a.fechaingreso <= '"+fecha2+"') as pendientes " +
            "FROM controlasuntospendientesnew.responsable as resp inner join controlasuntospendientesnew.area as ar on resp.idarea=ar.idarea " +
            //"where resp.idarea in (2, 47, 97, 129, 152, 164, 179, 184) group by resp.idarea, ar.nombre order by nombre";
            "where resp.idarea in (";
        for (int i = 0; i < areas.size(); i++) {
            AreaBean ar = (AreaBean) areas.get(i);
            sql+=ar.getDatos().getIdarea()+", ";
        }
        sql = sql.substring(0, sql.length()-2) + ") ";
        sql+=" group by resp.idarea, ar.siglas order by siglas";
        ResultSetHandler rsh = new BeanListHandler(ResumenAreaSM.class);
        List<ResumenAreaSM> datos = (List<ResumenAreaSM>) qr.query(sql, rsh);
        return datos;
    }
    public List<ResumenAreaSM> obtenResumenAcInicio(String fecha1, String fecha2, List areas) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "SELECT resp.idarea,trim(ar.siglas) as siglas, " +
            "(select count(1) from controlasuntospendientesnew.accion a, controlasuntospendientesnew.responsable b where a.idaccion=b.idaccion and b.idarea=resp.idarea and a.estatus='A' and a.fecha >= '"+fecha1+"' and a.fecha <= '"+fecha2+"') as atendidos, " +
            "(select count(1) from controlasuntospendientesnew.accion a, controlasuntospendientesnew.responsable b where a.idaccion=b.idaccion and b.idarea=resp.idarea and a.estatus='P' and a.fecha >= '"+fecha1+"' and a.fecha <= '"+fecha2+"') as pendientes " +
            "FROM controlasuntospendientesnew.responsable as resp inner join controlasuntospendientesnew.area as ar on resp.idarea=ar.idarea " +
            "where resp.idarea in (";
        for (int i = 0; i < areas.size(); i++) {
            AreaBean ar = (AreaBean) areas.get(i);
            sql+=ar.getDatos().getIdarea()+", ";
        }
        sql = sql.substring(0, sql.length()-2) + ") ";
        sql+=" group by resp.idarea, ar.siglas order by siglas";
        ResultSetHandler rsh = new BeanListHandler(ResumenAreaSM.class);
        List<ResumenAreaSM> datos = (List<ResumenAreaSM>) qr.query(sql, rsh);
        return datos;
    }
    public ResumenAreaSM obtenResumenIni(String fecha1, String fecha2) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select " +
            "(SELECT count(*) FROM controlasuntospendientesnew.asunto where estatus='A' AND fechaatendertexto >= '"+fecha1+"' and fechaatendertexto <= '"+fecha2+"') as atendidos, " +
            "(SELECT count(*) FROM controlasuntospendientesnew.asunto where estatus='P' and tipoasunto<>'R' AND fechaingreso >= '"+fecha1+"' and fechaingreso <= '"+fecha2+"') as pendientes";
        ResultSetHandler rsh = new BeanHandler(ResumenAreaSM.class);
        ResumenAreaSM dato = (ResumenAreaSM) qr.query(sql, rsh);
        return dato;
    }
}
