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
import mx.org.inegi.dggma.sistemas.asuntos.dto.AvanceCanceladoDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AvanceDTO;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AsuntoBean;
import mx.org.inegi.dggma.sistemas.asuntos.dto.Cantidad;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaDAO;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaUsuarioArea;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AreaBean;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AreasConsulta;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AvanceBean;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.Utiles;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *e
 * @author jacqueline
 */
public class AvanceDAO {
    private AreasConsulta areasConsulta;
    
    public AvanceDAO(AreasConsulta areasC) {
       this.areasConsulta = areasC;
    }
    
     public int getMaxId () throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select max(idavance) as cantidad from controlasuntospendientesnew.avance";
        
        ResultSetHandler rsh = new BeanHandler(Cantidad.class);
        Cantidad cantidad = (Cantidad) qr.query(sql, rsh);
        return cantidad.getCantidad();
        
    }
    
    public AvanceBean findByPrimaryKey(int pk) throws SQLException,  NamingException, Exception{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select * from controlasuntospendientesnew.avance " +
                "where idavance=? ";
        
        Object[] params = {pk};
        
        ResultSetHandler rsh = new BeanHandler(AvanceBean.class);
        AvanceBean dato = (AvanceBean) qr.query(sql, rsh, params);
        complementaDato(dato);
        return dato;
    
    }

    public void insertAsunto(AvanceBean avance) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        
        String sql = "insert into controlasuntospendientesnew.avance " +
                "(descripcion,fecha,idasunto,idarea,porcentaje) " +
                "values (?,?,?,?,?)";

        Object[] params = {avance.getDescripcion(),
                           avance.getFecha(),
                           avance.getIdasunto(),
                           avance.getIdarea(),
                           avance.getPorcentaje()};
        qr.update(sql,params);
       
    }
    public void insertAsuntoHistorico(AvanceBean avance) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        String sql = "insert into controlasuntospendientesnew.avance_historico " +
                "(descripcion,fecha,idasunto,idarea,porcentaje) " +
                "values (?,?,?,?,?)";
        Object[] params = {avance.getDescripcion(), avance.getFecha(), avance.getIdasunto(), avance.getIdarea(), avance.getPorcentaje()};
        qr.update(sql,params);
    }
    public void insertAcuerdo(AvanceBean avance) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        
        String sql = "insert into controlasuntospendientesnew.avance " +
                "(descripcion,fecha,idaccion,idarea,porcentaje) " +
                "values (?,?,?,?,?)";

        Object[] params = {avance.getDescripcion(),
                           avance.getFecha(),
                           avance.getIdaccion(),
                           avance.getIdarea(),
                           avance.getPorcentaje()};
        qr.update(sql,params);
    }
    
    
    

    public void update(AvanceBean avance) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
            String sql = "update controlasuntospendientesnew.avance " +
                    "set descripcion=?,fecha=?,idasunto=?,idarea=?," +
                    "porcentaje=? "+
                    "where idavance=?";
            
        Object[] params = {avance.getDescripcion(),
                           avance.getFecha(),
                           avance.getIdasunto(),
                           avance.getIdarea(),
                           avance.getPorcentaje(),
                           avance.getIdavance()};
         qr.update(sql,params);
    }
  
    public void delete(int pk) throws Exception {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "delete from controlasuntospendientesnew.avance " +
                     "where idavance=?";
        Object[] params = {pk};
        qr.update(sql,params);
    }
    
    public List<AvanceBean> obtenAvancesxResponsablexAsunto(AsuntoBean asunto, AreaBean responsable) throws SQLException,  NamingException, Exception{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select * from controlasuntospendientesnew.avance " +
                    " where idasunto=? and idarea = ? order by fecha desc, porcentaje desc";
         Object[] params = {asunto.getIdasunto(), responsable.getDatos().getIdarea()};
         ResultSetHandler rsh = new BeanListHandler(AvanceBean.class);
         List<AvanceBean> datos = (List<AvanceBean>) qr.query(sql, rsh, params);
         complementaDatos(datos);
         return datos;
    }
    
    public List<AvanceBean> obtenAvancesxResponsablexAsunto(int idasunto, int idresponsable) throws SQLException,  NamingException, Exception{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select * from controlasuntospendientesnew.avance " +
                    " where idasunto=? and idarea = ? order by fecha desc, porcentaje desc";
         Object[] params = {idasunto,idresponsable};
         ResultSetHandler rsh = new BeanListHandler(AvanceBean.class);
         List<AvanceBean> datos = (List<AvanceBean>) qr.query(sql, rsh, params);
         complementaDatos(datos);
         return datos;
    }
    
    public List<AvanceBean> obtenAvancesxResponsablexAcuerdo(int idacuerdo, int idresponsable) throws SQLException,  NamingException, Exception{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select * from controlasuntospendientesnew.avance " +
                    "where idaccion=? and idarea = ? order by fecha desc, porcentaje desc";
         Object[] params = {idacuerdo,idresponsable};
         ResultSetHandler rsh = new BeanListHandler(AvanceBean.class);
         List<AvanceBean> datos = (List<AvanceBean>) qr.query(sql, rsh, params);
         complementaDatos(datos);
         return datos;
    }

    public List<AvanceBean> obtenAvancesxAsunto(int idasunto, int idarea) throws SQLException,  NamingException, Exception{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select * from controlasuntospendientesnew.avance " +
                    " where idasunto=? and idarea = ? order by fecha desc, porcentaje desc";
         Object[] params = {idasunto, idarea};
         ResultSetHandler rsh = new BeanListHandler(AvanceBean.class);
         List<AvanceBean> datos = (List<AvanceBean>) qr.query(sql, rsh, params);
         complementaDatos(datos);
         return datos;
    }
    public List<AvanceBean> obtenAvancesTodos(int idasunto) throws SQLException,  NamingException, Exception{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql="SELECT idavance, descripcion, fecha, porcentaje, b.nombre as nombre " +
            "FROM controlasuntospendientesnew.avance as a " +
            "inner join controlasuntospendientesnew.area as b on a.idarea=b.idarea " +
            "where idasunto=? order by fecha desc, porcentaje desc";
         Object[] params = {idasunto};
         ResultSetHandler rsh = new BeanListHandler(AvanceBean.class);
         List<AvanceBean> datos = (List<AvanceBean>) qr.query(sql, rsh, params);
         complementaDatos(datos);
         return datos;
    }
    public List<AvanceBean> obtenAvancesHistorico(int idasunto) throws SQLException,  NamingException, Exception{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql="SELECT idavance, descripcion, fecha, porcentaje, b.nombre as nombre, b.siglas as siglas " +
            "FROM controlasuntospendientesnew.avance_historico as a " +
            "inner join controlasuntospendientesnew.area as b on a.idarea=b.idarea " +
            "where idasunto=? order by fecha desc, porcentaje desc";
         Object[] params = {idasunto};
         ResultSetHandler rsh = new BeanListHandler(AvanceBean.class);
         List<AvanceBean> datos = (List<AvanceBean>) qr.query(sql, rsh, params);
         complementaDatos(datos);
         return datos;
    }
    public List<AvanceBean> obtenAvancesxAcuerdo(int idacuerdo, int idarea) throws SQLException,  NamingException, Exception{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select * from controlasuntospendientesnew.avance " +
                    " where idaccion=? and idarea = ? order by fecha desc, porcentaje desc";
        
         Object[] params = {idacuerdo, idarea};

         ResultSetHandler rsh = new BeanListHandler(AvanceBean.class);
         List<AvanceBean> datos = (List<AvanceBean>) qr.query(sql, rsh, params);
         
         complementaDatos(datos);
         return datos;
    }
    public List<AvanceBean> obtenAvancesxAcuerdoTodos(int idacuerdo) throws SQLException,  NamingException, Exception{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "SELECT idavance, descripcion, fecha, porcentaje, b.nombre as nombre " +
            "FROM controlasuntospendientesnew.avance as a " +
            "inner join controlasuntospendientesnew.area as b on a.idarea=b.idarea " +
            "where idaccion=? order by b.nivel, fecha desc, porcentaje desc";
         Object[] params = {idacuerdo};
         ResultSetHandler rsh = new BeanListHandler(AvanceBean.class);
         List<AvanceBean> datos = (List<AvanceBean>) qr.query(sql, rsh, params);
         complementaDatos(datos);
         return datos;
    }
    
    
    private void complementaDatos(List<AvanceBean> datos) throws Exception {
       FachadaUsuarioArea fua = new FachadaUsuarioArea();
       FachadaDAO fachada = new FachadaDAO(areasConsulta);
       for (AvanceBean avance:datos){
           avance.setArea(fua.buscaArea(avance.getIdarea()));
           avance.setAnexos(fachada.buscarAnexosPorAvance(avance));
           avance.setFechaFormatoTexto(Utiles.getFechaCorta(avance.getFecha()));
       }
    }
    
    private void complementaDato(AvanceBean dato) {
       
       
    }
    
    public void eliminaAvancesxResponsableAsunto(int idarea, int idasunto) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "delete from controlasuntospendientesnew.avance where idasunto=? and idarea = ?";
        Object[] params = {idasunto,idarea};
        qr.update(sql,params);
    }
    
    public void eliminaAvancesxResponsableAcuerdo(int idarea, int idacuerdo) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "delete from controlasuntospendientesnew.avance where idaccion=? and idarea = ?";
        Object[] params = {idacuerdo,idarea};
        qr.update(sql,params);
    }
    
    
    public void eliminaAvancesAsunto(int idasunto) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "delete from controlasuntospendientesnew.avance where idasunto=?";
        Object[] params = {idasunto};
        qr.update(sql,params);
    }
    
    public void eliminaAvancesAcuerdo(int idacuerdo) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "delete from controlasuntospendientesnew.avance where idaccion=?";
        Object[] params = {idacuerdo};
        qr.update(sql,params);
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
     public void insertAvanceCancelado(AvanceCanceladoDTO avanceC) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        
        String sql = "insert into controlasuntospendientesnew.avancescancelados " +
                "(idasunto, idaccion, idarea,idresponsable, descripcion,fecha,porcentaje, idusuariomodificacion) " +
                "values (?,?,?,?,?,?,?,?)";

        Object[] params = {avanceC.getIdasunto(),
                           avanceC.getIdaccion(),
                           avanceC.getIdarea(),
                           avanceC.getIdresponsable(),
                           avanceC.getDescripcion(),
                           avanceC.getFecha(),
                           avanceC.getPorcentaje(),
                           avanceC.getIdusuariomodificacion()};
        
        qr.update(sql,params);
    }
    
    public List<AvanceCanceladoDTO> obtenAvancesCanceladosxResponsableAcuerdo(int idacuerdo,int idresponsable) throws SQLException,  NamingException, Exception{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "SELECT * FROM controlasuntospendientesnew.avancescancelados "+
                     "WHERE idaccion = ? and idarea = ? "; /*and idresponsable = ?";*/
        
         Object[] params = {idacuerdo, idresponsable/*, idRTabla*/};

         ResultSetHandler rsh = new BeanListHandler(AvanceCanceladoDTO.class);
         List<AvanceCanceladoDTO> datos = (List<AvanceCanceladoDTO>) qr.query(sql, rsh, params);
         
         complementaDatosCancelados(datos);
         
         return datos;
    }
     
     
    public List<AvanceCanceladoDTO> obtenAvancesCanceladosxResponsableAsunto(int idRTabla, int idasunto, int idresponsable) throws SQLException,  NamingException, Exception{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "SELECT * FROM controlasuntospendientesnew.avancescancelados "+
                     "WHERE idasunto = ? and idarea = ? and idresponsable = ?";
        
         Object[] params = {idasunto, idresponsable, idRTabla};

         ResultSetHandler rsh = new BeanListHandler(AvanceCanceladoDTO.class);
         List<AvanceCanceladoDTO> datos = (List<AvanceCanceladoDTO>) qr.query(sql, rsh, params);
         
         complementaDatosCancelados(datos);
         
         return datos;
    }
    
    private void complementaDatosCancelados(List<AvanceCanceladoDTO> avancesC) throws SQLException, NamingException{
       FachadaDAO fachada = new FachadaDAO(areasConsulta); 
       for (AvanceCanceladoDTO avanceC:avancesC)
            avanceC.setAnexos(fachada.buscarAnexosPorAvance(avanceC.getIdavance()));
    }

    public int pctjesRegistradosXAsuntoArea(int idasunto, int idarea) throws SQLException,  NamingException, Exception{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "SELECT max(porcentaje) as porcentaje FROM controlasuntospendientesnew.avance where idasunto=? and idarea=? ";
        Object[] params = {idasunto, idarea};
        ResultSetHandler rsh = new BeanHandler(AvanceBean.class);
        AvanceBean dato = (AvanceBean) qr.query(sql, rsh, params);
        return dato.getPorcentaje();
    }
    public void cambiaPctjeResp(AvanceDTO av, String atendido) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        String sql="update controlasuntospendientesnew.responsable set estatus=?, avance=? WHERE idasunto = ? and idarea = ?";
        qr.update(sql, atendido, av.getPorcentaje(), av.getIdasunto(), av.getIdarea());
    }
    public void cambiaPctjeAvance(AvanceDTO av) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        String sql="update controlasuntospendientesnew.avance set porcentaje=? where idavance=?";
        qr.update(sql,av.getPorcentaje(),av.getIdavance());
    }
}
