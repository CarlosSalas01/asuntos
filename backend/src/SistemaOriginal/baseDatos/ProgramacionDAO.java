/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.baseDatos;

import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;
import javax.sql.DataSource;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AsuntoDTO;

import mx.org.inegi.dggma.sistemas.asuntos.dto.ProgramacionDTO;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AccionBean;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;


/**
 *
 * @author jacqueline
 */
public class ProgramacionDAO {
    
      public List<ProgramacionDTO> obtenProgramacionesxAsunto(int idasunto) throws SQLException,  NamingException {
         DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "SELECT * FROM controlasuntospendientesnew.programacion "
                     +"WHERE idasunto = ? order by fecharealizada, fechareprograma";
        

        Object[] params = {idasunto};
     
        ResultSetHandler rsh = new BeanListHandler(ProgramacionDTO.class);
        List<ProgramacionDTO> datos = (List<ProgramacionDTO>) qr.query(sql, rsh, params);       
        
        return datos;
        
        
    }
      
      public String obtenMaxReprograma(int idasunto) throws SQLException, NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "SELECT max(fechareprograma) as fechareprograma FROM controlasuntospendientesnew.programacion WHERE idasunto = ? ";
        Object[] params = {idasunto};
        ResultSetHandler rsh = new BeanHandler(ProgramacionDTO.class);
        ProgramacionDTO datos = (ProgramacionDTO) qr.query(sql, rsh, params);
        String f = datos.getFechareprograma();
        if(f != null) return f.substring(6,8)+"/"+f.substring(4,6)+"/"+f.substring(0,4);
        else return null;
      }
      public String obtenMaxReprogramaAcuerdo(int idacuerdo) throws SQLException, NamingException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "SELECT max(fechareprograma) as fechareprograma FROM controlasuntospendientesnew.programacion WHERE idacuerdo = ? ";
        Object[] params = {idacuerdo};
        ResultSetHandler rsh = new BeanHandler(ProgramacionDTO.class);
        ProgramacionDTO datos = (ProgramacionDTO) qr.query(sql, rsh, params);
        String f = datos.getFechareprograma();
        if(f != null) return f.substring(6,8)+"/"+f.substring(4,6)+"/"+f.substring(0,4);
        else return null;
      }
    public ProgramacionDTO obtenProgramacionesxId(int idreprograma) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        String sql = "SELECT * FROM controlasuntospendientesnew.programacion WHERE idreprogramacion = ? ";
        Object[] params = {idreprograma};
        ResultSetHandler rsh = new BeanHandler(ProgramacionDTO.class);
        ProgramacionDTO dato = (ProgramacionDTO) qr.query(sql, rsh, params);
        return dato;
    }
      
    public List<ProgramacionDTO> obtenProgramacionesxAcuerdo(int idacuerdo) throws SQLException,  NamingException {
         DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "SELECT * FROM controlasuntospendientesnew.programacion "
                     +"WHERE idacuerdo = ? order by fecharealizada";
        

        Object[] params = {idacuerdo};
     
        ResultSetHandler rsh = new BeanListHandler(ProgramacionDTO.class);
        List<ProgramacionDTO> datos = (List<ProgramacionDTO>) qr.query(sql, rsh, params);       
        
        return datos;
    }
    

    public void insertaReprogramacionAsunto(ProgramacionDTO prog) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        
        String sql = "INSERT INTO controlasuntospendientesnew.programacion( " +
                     "idasunto, fecharealizada, fechareprograma, justificacion) " +
                     "VALUES (?, ?, ?, ?);";

        Object[] params = {prog.getIdasunto(),
                           prog.getFecharealizada(),
                           prog.getFechareprograma(),
                           prog.getJustificacion()};
        qr.update(sql,params);
       
    }

    
    public void insertaReprogramacionAcuerdo(ProgramacionDTO prog) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        
        String sql = "INSERT INTO controlasuntospendientesnew.programacion( " +
                     "idacuerdo, fecharealizada, fechareprograma, justificacion) " +
                     "VALUES (?, ?, ?, ?);";

        Object[] params = {prog.getIdacuerdo(),
                           prog.getFecharealizada(),
                           prog.getFechareprograma(),
                           prog.getJustificacion()};
        qr.update(sql,params);
       
    }
    
    public void eliminaReprogramacion(int idreprograma) throws NamingException, SQLException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        
        String sql = "DELETE FROM controlasuntospendientesnew.programacion WHERE idreprogramacion = ?";

        Object[] params = {idreprograma};
        
        qr.update(sql,params);
    }
    
     public List<ProgramacionDTO> obtenProgramaciones() throws SQLException,  NamingException {
         DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds); 
        
        String sql = "SELECT * FROM controlasuntospendientesnew.programacion "
                     +"order by fecharealizada";
     
        ResultSetHandler rsh = new BeanListHandler(ProgramacionDTO.class);
        List<ProgramacionDTO> datos = (List<ProgramacionDTO>) qr.query(sql, rsh);       
        
        return datos;
    }
    
    public void actualizaReprogramacionID(ProgramacionDTO prog) throws SQLException,  NamingException {
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
    
        String sql = "";
        if (prog.getIdasunto() != 0) {
           sql = "UPDATE controlasuntospendientesnew.programacion " +
                     "SET idreprogramacion=? " +
                     "WHERE idasunto=? and fecharealizada=? and fechareprograma=? and justificacion=? and idreprogramacion is null";
           
           Object[] params = {prog.getIdreprogramacion(),
                           prog.getIdasunto(),
                           prog.getFecharealizada(),
                           prog.getFechareprograma(),
                           prog.getJustificacion()};
                   
            qr.update(sql,params);
            
        } else if (prog.getIdacuerdo() != 0) {
           sql = "UPDATE controlasuntospendientesnew.programacion " +
                     "SET idreprogramacion=? " +
                     "WHERE idacuerdo=? and fecharealizada=? and fechareprograma=? and justificacion=? and idreprogramacion is null";
            Object[] params = {prog.getIdreprogramacion(),
                           prog.getIdacuerdo(),
                           prog.getFecharealizada(),
                           prog.getFechareprograma(),
                           prog.getJustificacion()};

            qr.update(sql,params);
        }   
    }
    public void actualizaFechaReprogram(int idasunto) throws NamingException, SQLException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        String sql = "SELECT * FROM controlasuntospendientesnew.programacion where idasunto = ?";
        ResultSetHandler rsh = new BeanListHandler(ProgramacionDTO.class);
        List<ProgramacionDTO> datos = (List<ProgramacionDTO>) qr.query(sql, rsh, idasunto);
        String fecha="",reprograma="S";
        if(datos.size() == 0) {
            sql="SELECT fechaoriginal FROM controlasuntospendientesnew.asunto where idasunto = ?";
            rsh = new BeanHandler(AsuntoDTO.class);
            AsuntoDTO fec = (AsuntoDTO) qr.query(sql, rsh, idasunto);
            fecha = fec.getFechaoriginal();
            reprograma="";
        }
        if(datos.size() > 1) {
            sql="SELECT max(fechareprograma) as fechaoriginal FROM controlasuntospendientesnew.programacion where idasunto = ? ";
            rsh = new BeanHandler(AsuntoDTO.class);
            AsuntoDTO fec = (AsuntoDTO) qr.query(sql, rsh, idasunto);
            fecha = fec.getFechaoriginal();
        }
        if(datos.size() == 1) {
            sql="SELECT fechareprograma as fechaoriginal FROM controlasuntospendientesnew.programacion where idasunto = ? ";
            rsh = new BeanHandler(AsuntoDTO.class);
            AsuntoDTO fec = (AsuntoDTO) qr.query(sql, rsh, idasunto);
            fecha = fec.getFechaoriginal();
        }
        String sql2="UPDATE controlasuntospendientesnew.asunto SET fechaatender='"+fecha+"', reprograma='"+reprograma+"' WHERE idasunto = ? ";
        qr.update(sql2,idasunto);
    }
    public void actualizaFechaReprogramAcuerdos(int idacuerdo) throws NamingException, SQLException{
        DataSource ds = AdministradorDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        String sql = "SELECT * FROM controlasuntospendientesnew.programacion where idacuerdo = ?";
        ResultSetHandler rsh = new BeanListHandler(ProgramacionDTO.class);
        List<ProgramacionDTO> datos = (List<ProgramacionDTO>) qr.query(sql, rsh, idacuerdo);
        String fecha="";
        if(datos.size() == 0) {
            sql="SELECT fechaoriginal FROM controlasuntospendientesnew.accion where idaccion = ?";
            rsh = new BeanHandler(AsuntoDTO.class);
            AsuntoDTO fec = (AsuntoDTO) qr.query(sql, rsh, idacuerdo);
            fecha = fec.getFechaoriginal();
        }
        if(datos.size() > 1) {
            sql="SELECT max(fechareprograma) as fechaoriginal FROM controlasuntospendientesnew.programacion where idacuerdo = ? ";
            rsh = new BeanHandler(AsuntoDTO.class);
            AsuntoDTO fec = (AsuntoDTO) qr.query(sql, rsh, idacuerdo);
            fecha = fec.getFechaoriginal();
        }
        if(datos.size() == 1) {
            sql="SELECT fechareprograma as fechaoriginal FROM controlasuntospendientesnew.programacion where idacuerdo = ? ";
            rsh = new BeanHandler(AsuntoDTO.class);
            AsuntoDTO fec = (AsuntoDTO) qr.query(sql, rsh, idacuerdo);
            fecha = fec.getFechaoriginal();
        }
        String sql2="UPDATE controlasuntospendientesnew.accion SET acuerdo_fecha='"+fecha+"' WHERE idaccion = ? ";
        qr.update(sql2,idacuerdo);
    }

}
