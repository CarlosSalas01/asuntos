/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.baseDatos;

import java.sql.SQLException;
import javax.naming.NamingException;
import javax.sql.DataSource;
import mx.org.inegi.dggma.sistemas.asuntos.dto.BitacoraDTO;
import org.apache.commons.dbutils.QueryRunner;

/**
 *
 * @author jacqueline
 */
public class BitacoraDAO {
    
    public void grabaBitacora(BitacoraDTO bit) throws SQLException,  NamingException {
       DataSource ds = AdministradorDataSource.getDataSource();
       QueryRunner qr = new QueryRunner(ds); 
       String sql = "INSERT INTO controlasuntospendientesnew.bitacora(id, tipo, fecha, observaciones, idusuariomodificacion, accion, idarea, detalle ) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";
         Object[] params = {bit.getId(),
                            bit.getTipo(),
                            bit.getFecha(),
                            bit.getObservaciones(),
                            bit.getIdusuariomodificacion(),
                            bit.getAccion(),
                            bit.getIdarea(),
                            bit.getDetalle()};
         qr.update(sql,params);
         
    }
    
}
