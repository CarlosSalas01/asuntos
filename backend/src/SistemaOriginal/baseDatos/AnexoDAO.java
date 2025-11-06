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
import java.util.List;
import javax.naming.NamingException;
import javax.sql.DataSource;

import mx.org.inegi.dggma.sistemas.asuntos.dto.AnexoDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.Cantidad;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author Jos Luis Mondragn
 */
public class AnexoDAO {

    /** Creates a new instance of AsuntoDAO */
    public AnexoDAO() {
    }

    public  void insert(AnexoDTO anexo) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "insert into controlasuntospendientesnew.anexo " +
                    "(activoestatus,titulo,nombrearchivo,nombreenservidor," +
                    "contenttype,fechacarga,tamanio) " +
                    "values (?,?,?,?,?,?,?)";

         Object[] params = {anexo.getActivoestatus(),
                            anexo.getTitulo(),
                            anexo.getNombrearchivo(),
                            anexo.getNombreenservidor(),

                            anexo.getContenttype(),
                            anexo.getFechacarga(),
                            anexo.getTamanio()};
                           
          qr.update(sql,params);          
    }

    
    public  void update(AnexoDTO anexo) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "update controlasuntospendientesnew.anexo " +
                    "set activoestatus=?,titulo=?,nombrearchivo=?,nombreenservidor=?," +
                    "contenttype=?,fechacarga=?,tamanio=? " +
                    "where idanexo=?";
         Object[] params = {anexo.getActivoestatus(),
                            anexo.getTitulo(),
                            anexo.getNombrearchivo(),
                            anexo.getNombreenservidor(),

                            anexo.getContenttype(),
                            anexo.getFechacarga(),
                            anexo.getTamanio(),
                            anexo.getIdanexo()};
                           
          qr.update(sql,params);          
    }

    public int getMaxId () throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select max(idanexo) as cantidad from controlasuntospendientesnew.anexo ";
        
        ResultSetHandler rsh = new BeanHandler(Cantidad.class);
        Cantidad cantidad = (Cantidad) qr.query(sql, rsh);
        return cantidad.getCantidad();
        
    }

    public void delete(int pk) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        //anexo.setActivoEstatus(false);
        String sql = "DELETE FROM controlasuntospendientesnew.anexo WHERE idanexo = ?";
        Object[] params = {pk};
                           
        qr.update(sql,params);          
    }

    public AnexoDTO findByPrimaryKey(int pk) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        
        String sql = "select * from controlasuntospendientesnew.anexo " +
                     "where idanexo=?" ;
        
        Object[] params = {pk};
        
        ResultSetHandler rsh = new BeanHandler(AnexoDTO.class);
        AnexoDTO dato = (AnexoDTO) qr.query(sql, rsh, params);
        
        return dato;
    }
    public AnexoDTO findByPrimaryKeySeguimiento(int idanexo) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select * from controlasuntospendientesnew.anexos_actseg where idanexo=?" ;
        Object[] params = {idanexo};
        ResultSetHandler rsh = new BeanHandler(AnexoDTO.class);
        AnexoDTO dato = (AnexoDTO) qr.query(sql, rsh, params);
        return dato;
    }
    
    
    public void eliminaAnexosxAsunto(int idasunto) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "DELETE FROM controlasuntospendientesnew.anexo WHERE idasunto = ?";
        Object[] params = {idasunto};
        qr.update(sql,params);
    }

    
    public void eliminaAnexosxAcuerdo(int idacuerdo) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "DELETE FROM controlasuntospendientesnew.anexo WHERE idaccion = ?";
        Object[] params = {idacuerdo};
        qr.update(sql,params);
    }

    
    public List<AnexoDTO> buscaAnexosxAsunto(int idasunto) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select * from controlasuntospendientesnew.anexo where idasunto=?" ;
        Object[] params = {idasunto};
        ResultSetHandler rsh = new BeanListHandler(AnexoDTO.class);
        List<AnexoDTO> dato = (List<AnexoDTO>) qr.query(sql, rsh, params);
        return dato;
    }
    
    
    public List<AnexoDTO> buscaAnexosxAcuerdo(int idacuerdo) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "select * from controlasuntospendientesnew.anexo where idaccion=?" ;
        Object[] params = {idacuerdo};
        ResultSetHandler rsh = new BeanListHandler(AnexoDTO.class);
        List<AnexoDTO> dato = (List<AnexoDTO>) qr.query(sql, rsh, params);
        return dato;
    }
    public List<AnexoDTO> obtenAnexosAtendido(int idasunto) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
/*        String sql = "SELECT b.idanexo,b.contenttype,nombrearchivo FROM controlasuntospendientesnew.asunto as a "
                + "inner join controlasuntospendientesnew.anexo as b on a.idasunto=b.idasunto where a.tipoasunto=? and a.idconsecutivo=? " ;*/
        String sql="SELECT b.idanexo,b.contenttype,nombrearchivo FROM controlasuntospendientesnew.avance as a "
                + "inner join controlasuntospendientesnew.anexo as b on a.idasunto=b.idasunto "
                + "where a.idasunto=? order by a.fecha,b.idanexo desc LIMIT 1";
        Object[] params = {idasunto};
        ResultSetHandler rsh = new BeanListHandler(AnexoDTO.class);
        List<AnexoDTO> dato = (List<AnexoDTO>) qr.query(sql, rsh, params);
        return dato;
    }
    public List<AnexoDTO> obtenAnexosAtendidoAcuerdos(int idaccion) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql="SELECT a.idasunto,a.idaccion,b.idavance,c.nombreenservidor,c.idanexo "
                + "FROM controlasuntospendientesnew.accion as a "
                + "left join controlasuntospendientesnew.avance as b on a.idaccion=b.idaccion "
                + "left join controlasuntospendientesnew.anexo as c on b.idavance=c.idavance "
                + "where a.idaccion=? order by a.idasunto,c.idavance desc limit 1";
        Object[] params = {idaccion};
        ResultSetHandler rsh = new BeanListHandler(AnexoDTO.class);
        List<AnexoDTO> dato = (List<AnexoDTO>) qr.query(sql, rsh, params);
        return dato;
    }
}
