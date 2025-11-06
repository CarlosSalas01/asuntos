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
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AccionBean;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AnexoAccionDTO;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author Jos Luis Mondragn
 */
public class AnexoAccionDAO {

    /** Creates a new instance of AsuntoDAO */
    public AnexoAccionDAO() {
    }

    public AnexoAccionDTO findByPrimaryKey(int pk) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        
        String sql = "select * from controlasuntospendientesnew.anexo " +
                "where idaccion=?" ;
        
        Object[] params = {pk};
        
        ResultSetHandler rsh = new BeanHandler(AnexoAccionDTO.class);
        AnexoAccionDTO dato = (AnexoAccionDTO) qr.query(sql, rsh, params);
        
        return dato;
    }
    
    public void insert(AnexoAccionDTO anexo) throws SQLException,  NamingException, NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        
         String sql = "insert into controlasuntospendientesnew.anexo " +
                    "(activoestatus,titulo,nombrearchivo,nombreenservidor," +
                    "contenttype,fechacarga,tamanio,idaccion) " +
                    "values (?,?,?,?,?,?,?,?)";

         Object[] params = {anexo.getActivoestatus(),
                            anexo.getTitulo(),
                            anexo.getNombrearchivo(),
                            anexo.getNombreenservidor(),

                            anexo.getContenttype(),
                            anexo.getFechacarga(),
                            anexo.getTamanio(),
                            anexo.getIdaccion()};
                           
          qr.update(sql,params);                  

    }

    public void update(AnexoAccionDTO anexo) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        
            String sql = "update controlasuntospendientesnew.anexo " +
                    "set activoestatus=?,titulo=?,nombrearchivo=?,nombreenservidor=?," +
                    "contenttype=?,fechacarga=?,tamanio=?,idaccion=? " +
                    "where idanexo=?";
            
            Object[] params = {anexo.getActivoestatus(),
                            anexo.getTitulo(),
                            anexo.getNombrearchivo(),
                            anexo.getNombreenservidor(),
                            anexo.getContenttype(),
                            anexo.getFechacarga(),
                            anexo.getTamanio(),
                            anexo.getIdaccion(),

                            anexo.getIdanexo()};
                           
            qr.update(sql,params);        
            
    }


    public void delete(int idanexo) throws SQLException,  NamingException {
        //Se elimina directamente en bd pero se 
        //Agregar a bitacora tmb la eliminaci�n
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "DELETE FROM controlasuntospendientesnew.anexo "+
                     "where idanexo =?";
        Object[] params = {idanexo};
                
        qr.update(sql,params);
        
    }


    public  List<AnexoAccionDTO> buscarAnexosPorAccion(AccionBean accion) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        
        String sql = "select * from controlasuntospendientesnew.anexo " +
                "where idaccion=? " +
                "order by fechacarga";
        
        Object[] params = {accion.getIdAccion()};
        
        ResultSetHandler rsh = new BeanListHandler(AnexoAccionDTO.class);
        List<AnexoAccionDTO> datos = (List<AnexoAccionDTO>) qr.query(sql, rsh, params);
        
        return datos;
    }

   

   
}
