/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.baseDatos;

import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;
import javax.sql.DataSource;
import mx.org.inegi.dggma.sistemas.asuntos.dto.ActividadesSeguimientoDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AnexoAsuntoDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AnexoDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AreaDTO;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author RICARDO.SERNA
 */
public class ActividadesSeguimientoDAO {
    
    public List<ActividadesSeguimientoDTO> listaAct(int idasunto, int idarea) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        String condicion = "";
        if(idarea != 0) {
            condicion = " and a.idarea="+idarea+" ";
        }
        String sql = "SELECT idactividad,substring(fechaactividad,7,2)||'/'||substring(fechaactividad,5,2)||'/'||substring(fechaactividad,1,4) as fechaactividadstr,a.descripcion,b.nombre as usuario, a.idarea, COALESCE(c.siglas, '') as siglas \n" +
                "FROM controlasuntospendientesnew.actividades_seguimiento as a\n" +
                "inner join controlasuntospendientesnew.usuario as b on a.idusuario=b.idusuario\n" +
                "left join controlasuntospendientesnew.area as c on a.idarea=c.idarea "+
                "where idasunto=? "+condicion+" order by fechaactividad desc";
        ResultSetHandler rsh = new BeanListHandler(ActividadesSeguimientoDTO.class);
        List<ActividadesSeguimientoDTO> datos = (List<ActividadesSeguimientoDTO>) qr.query(sql, rsh, idasunto);
        return datos;
    }
    public List<AnexoDTO> listaAnexos(int idactividad) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select * from controlasuntospendientesnew.anexos_actseg where idactividad = ?";
        ResultSetHandler rsh = new BeanListHandler(AnexoDTO.class);
        List<AnexoDTO> datos = (List<AnexoDTO>) qr.query(sql, rsh, idactividad);
        return datos;
    }
    public void insertActividades(ActividadesSeguimientoDTO acts) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "INSERT INTO controlasuntospendientesnew.actividades_seguimiento(\n" +
                "idasunto, fechaactividad, descripcion, idusuario, idarea)\n" +
                "VALUES (?, ?, ?, ?, ?);";
        Object[] params = {acts.getIdasunto(), acts.getFechaactividad(), acts.getDescripcion(), acts.getIdusuario(), acts.getIdarea()};
        qr.update(sql,params);
    }
    public void updateActividades(ActividadesSeguimientoDTO acts) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "UPDATE controlasuntospendientesnew.actividades_seguimiento \n" +
                "set fechaactividad=?, descripcion=?, idusuario=?, idarea=? where idactividad = ?";
        Object[] params = {acts.getFechaactividad(), acts.getDescripcion(), acts.getIdusuario(), acts.getIdarea(), acts.getIdactividad()};
        qr.update(sql,params);
    }
    public void insertAnexoActividades(AnexoAsuntoDTO anexo) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "insert into controlasuntospendientesnew.anexos_actseg " +
                    "(nombrearchivo,nombreenservidor,contenttype,fechacarga,tamanio, idasunto, idaccion, idactividad) values (?,?,?,?,?,?,?,?)";
         Object[] params = {anexo.getNombrearchivo(),
                            anexo.getNombreenservidor(),
                            anexo.getContenttype(),
                            anexo.getFechacarga(),
                            anexo.getTamanio(),
                            anexo.getIdAsunto(),
                            anexo.getIdAccion(),
                            anexo.getIdactividad()
         };
          qr.update(sql,params);
    }
    public int nextIdactividad() throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select max(idactividad) as idactividad from controlasuntospendientesnew.actividades_seguimiento";
        ResultSetHandler rsh = new BeanHandler(AnexoAsuntoDTO.class);
        AnexoAsuntoDTO dato = (AnexoAsuntoDTO) qr.query(sql, rsh);
        return dato.getIdactividad();
    }
    public void eliminaActividades(int idactividad) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "delete from controlasuntospendientesnew.actividades_seguimiento where idactividad=?";
        qr.update(sql,idactividad);
        sql = "delete from controlasuntospendientesnew.anexos_actseg where idactividad=?";
        qr.update(sql,idactividad);
    }
    public void eliminaAnexo(int idanexo) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "delete from controlasuntospendientesnew.anexos_actseg where idanexo=?";
        qr.update(sql,idanexo);
    }    
    // acuerdos
    public List<ActividadesSeguimientoDTO> listaActAcuerdos(int idaccion, int idarea) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String condicion = "";
        if(idarea != 0) {
            condicion = " and a.idarea="+idarea+" ";
        }
        String sql = "SELECT idactividad,substring(fechaactividad,7,2)||'/'||substring(fechaactividad,5,2)||'/'||substring(fechaactividad,1,4) as fechaactividadstr,a.descripcion, b.nombre as usuario, a.idarea, COALESCE(c.siglas, '') as siglas \n" +
                "FROM controlasuntospendientesnew.actividades_seguimiento as a\n" +
                "inner join controlasuntospendientesnew.usuario as b on a.idusuario=b.idusuario\n" +
                "left join controlasuntospendientesnew.area as c on a.idarea=c.idarea " +
                "where idaccion=? "+condicion+" order by fechaactividad desc";
        ResultSetHandler rsh = new BeanListHandler(ActividadesSeguimientoDTO.class);
        List<ActividadesSeguimientoDTO> datos = (List<ActividadesSeguimientoDTO>) qr.query(sql, rsh, idaccion);
        return datos;
    }
    public void insertActividadesAcuerdo(ActividadesSeguimientoDTO acts) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "INSERT INTO controlasuntospendientesnew.actividades_seguimiento(\n" +
                "idaccion, fechaactividad, descripcion, idusuario, idarea)\n" +
                "VALUES (?, ?, ?, ?, ?);";
        Object[] params = {acts.getIdaccion(), acts.getFechaactividad(), acts.getDescripcion(), acts.getIdusuario(), acts.getIdarea()};
        qr.update(sql,params);
    }
    public void insertAnexoActividadesAcuerdoBorrar(AnexoAsuntoDTO anexo) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "insert into controlasuntospendientesnew.anexos_actseg " +
                    "(nombrearchivo,nombreenservidor,contenttype,fechacarga,tamanio, idaccion, idactividad) values (?,?,?,?,?,?,?)";
         Object[] params = {anexo.getNombrearchivo(),
                            anexo.getNombreenservidor(),
                            anexo.getContenttype(),
                            anexo.getFechacarga(),
                            anexo.getTamanio(),
                            anexo.getIdAccion(),
                            anexo.getIdactividad()
         };
          qr.update(sql,params);
    }
    public ActividadesSeguimientoDTO actById(int idactividad) throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "SELECT * FROM controlasuntospendientesnew.actividades_seguimiento where idactividad=?";
        ResultSetHandler rsh = new BeanHandler(ActividadesSeguimientoDTO.class);
        ActividadesSeguimientoDTO dato = (ActividadesSeguimientoDTO) qr.query(sql, rsh, idactividad);
        return dato;
    }
}
