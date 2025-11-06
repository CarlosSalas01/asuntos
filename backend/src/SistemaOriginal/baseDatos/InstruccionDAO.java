/*
 * AsuntoDAO.java
 *
 * Created on 4 de marzo de 2006, 04:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.baseDatos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import javax.sql.DataSource;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AreaDTO;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AsuntoBean;
import mx.org.inegi.dggma.sistemas.asuntos.dto.Cantidad;
import mx.org.inegi.dggma.sistemas.asuntos.dto.InstruccionDTO;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaUsuarioArea;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author Jos Luis Mondragn
 */
public class InstruccionDAO {

    /** Creates a new instance of AsuntoDAO */
    public InstruccionDAO() {
    }

    public  void insert(InstruccionDTO instruccion) throws Exception {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        
        String sql = "insert into controlasuntospendientesnew.instruccion "
                + "(textoinstruccion,fecha,textorespuesta,fecharespuesta,"
                + "idusuarioimparte,idresponsableatiende,idasunto,activoestatus,"
                + "atendida) "
                + "values (?,?,?,?,?,?,?,?,?)";
        
        
        Object[] params = {instruccion.getTextoInstruccion(),
                           instruccion.getFecha(),
                           instruccion.getTextoRespuesta(),
                           instruccion.getFechaRespuesta(),
                           instruccion.getUsuarioImparte().getDatos().getIdusuario(),
                           instruccion.getResponsableAtiende().getDatos().getIdusuario(),
                           instruccion.getIdAsunto(),
                           instruccion.getActivoestatus(),
                           instruccion.getAtendida()};
        
        qr.update(sql,params);
        
    }

    public  void update(InstruccionDTO instruccion) throws Exception {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        
        String sql = "update controlasuntospendientesnew.instruccion "
                    + "set textoinstruccion=?,fecha=?,textorespuesta=?,fecharespuesta=?,"
                    + "idusuarioimparte=?,idresponsableatiende=?,idasunto=?,activoestatus=?,"
                    + "atendida=? "
                    + "where idinstruccion=?";

        Object[] params = {instruccion.getTextoInstruccion(),
                           instruccion.getFecha(),
                           instruccion.getTextoRespuesta(),
                           instruccion.getFechaRespuesta(),
                           instruccion.getUsuarioImparte().getDatos().getIdusuario(),
                           instruccion.getResponsableAtiende().getDatos().getIdusuario(),
                           instruccion.getIdAsunto(),
                           instruccion.getActivoestatus(),
                           instruccion.getAtendida(),
                           instruccion.getIdInstruccion()};
        
        qr.update(sql,params);   
    }

    
   public  void delete(int pk) throws Exception {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "DELETE FROM controlasuntospendientesnew.instruccion WHERE idinstruccion = ?;";
        Object[] params = {pk};
        
        qr.update(sql,params);   
    }

    public  InstruccionDTO findByPrimaryKey(int pk) throws Exception {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "select * from controlasuntospendientesnew.instruccion where idinstruccion=?";
  
        Object[] params = {pk};
     
        ResultSetHandler rsh = new BeanHandler(InstruccionDTO.class);
        InstruccionDTO dato = (InstruccionDTO) qr.query(sql, rsh, params);    

        complementaDatos(dato);
        
        return dato;
        
    }

    public  List<InstruccionDTO> buscarInstruccionesPorAsunto(int idAsunto) throws Exception {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        
        String sql = "select * from controlasuntospendientesnew.instruccion "
                + "where activoestatus='S' and idasunto=? "
                + "order by fecha";
     
        Object[] params = {idAsunto};
     
        ResultSetHandler rsh = new BeanListHandler(InstruccionDTO.class);
        List<InstruccionDTO> datos = (List<InstruccionDTO>) qr.query(sql, rsh, params);    

        complementaDatos(datos);
        
        return datos;
    }


    public  List<InstruccionDTO> buscarInstruccionesPorAsunto(AsuntoBean asunto) throws Exception {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        
        String sql = "select * from controlasuntospendientesnew.instruccion "
                + "where activoestatus='S' and idasunto=? "
                + "order by fecha";
     
        Object[] params = {asunto.getIdasunto()};
     
        ResultSetHandler rsh = new BeanListHandler(InstruccionDTO.class);
        List<InstruccionDTO> datos = (List<InstruccionDTO>) qr.query(sql, rsh, params);    

        complementaDatos(datos);
        
        return datos;
    }

    public int cantidadInstruccionesPorAsunto(AsuntoBean asunto) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
 
        String sql = "select count(*) as cantidad from controlasuntospendientesnew.instruccion "
                + "where activoestatus='S' and idasunto= ? ";
      
        Object[] params = {asunto.getIdasunto()};
        ResultSetHandler rsh = new BeanHandler(Cantidad.class);
        Cantidad dato = (Cantidad) qr.query(sql, rsh, params);
            
        return dato.getCantidad();
    }


    private void complementaDatos(InstruccionDTO instruccion) throws Exception {
        FachadaUsuarioArea fua = new FachadaUsuarioArea();
    
        instruccion.setUsuarioImparte(fua.buscaUsuario(instruccion.getIdusuarioimparte()));
        instruccion.setResponsableAtiende(fua.buscaUsuario(instruccion.getIdresponsableatiende()));
    }
    
    private void complementaDatos(List<InstruccionDTO> instrucciones) throws Exception {
        FachadaUsuarioArea fua = new FachadaUsuarioArea();
    
        for(InstruccionDTO instruccion:instrucciones) {
            instruccion.setUsuarioImparte(fua.buscaUsuario(instruccion.getIdusuarioimparte()));
            instruccion.setResponsableAtiende(fua.buscaUsuario(instruccion.getIdresponsableatiende()));
        }
 
    }
}
