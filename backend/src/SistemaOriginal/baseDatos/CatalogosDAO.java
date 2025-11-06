/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.baseDatos;

import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;
import mx.org.inegi.dggma.sistemas.asuntos.dto.CategoriaDTO;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author RICARDO.SERNA
 */
public class CatalogosDAO {
    public List<CategoriaDTO> listadoCategoria() throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "SELECT * FROM controlasuntospendientesnew.cat_categoria order by descripcion ";
        ResultSetHandler rsh = new BeanListHandler(CategoriaDTO.class);
        List<CategoriaDTO> lista = (List<CategoriaDTO>) qr.query(sql, rsh);        
        return lista;
    }
    public List<CategoriaDTO> listadoInstituciones() throws SQLException,  NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "SELECT * FROM controlasuntospendientesnew.cat_institucion order by descripcion ";
        ResultSetHandler rsh = new BeanListHandler(CategoriaDTO.class);
        List<CategoriaDTO> lista = (List<CategoriaDTO>) qr.query(sql, rsh);        
        return lista;
    }
}
