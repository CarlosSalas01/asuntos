/*
 * GeneradorAdvertencias.java
 *
 * Created on 22 de febrero de 2007, 09:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.negocio;

import java.util.List;
import mx.org.inegi.dggma.sistemas.asuntos.dto.RecordatorioDTO;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.UsuarioBean;

/**
 *
 * @author Jos Luis Mondragn
 */
public class GeneradorRecordatorios {

    /** Creates a new instance of GeneradorAdvertencias */
    public GeneradorRecordatorios() {
    }

    public static List<RecordatorioDTO> obtenRecordatorios(UsuarioBean usuario) throws Exception {
        List<RecordatorioDTO> listado = null;
        
        //listado = FachadaDAO.obtenerRecordatorios(usuario);

        return listado;
    }
}
