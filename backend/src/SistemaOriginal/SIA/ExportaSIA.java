/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.controladorNew;

import com.opencsv.CSVWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mx.org.inegi.dggma.sistemas.asuntos.dto.ProgramacionDTO;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaDAO;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaUsuarioArea;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.*;

/**
 *
 * @author jacqueline
 */
public class ExportaSIA extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        PrintWriter out = response.getWriter();
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition","attachment;filename=sia.csv");
        
        HttpSession sesion=request.getSession();
        AreasConsulta areasConsulta = (AreasConsulta) sesion.getAttribute("areasConsulta");
        FiltroAsunto filtro = (FiltroAsunto) sesion.getAttribute("filtroConsulta");
            
         try {
            FachadaDAO fachada = new FachadaDAO(areasConsulta);
            List<AsuntoBean> asuntos = null;
            String param = request.getParameter("param");
            if (param.equals("T")) {
                filtro.setLimitAll(true); //Se consultan todos los registros
                asuntos = fachada.buscarAsuntosPorAreaKeet(filtro);
            } else if (param.equals("A")) {
                int pag = Integer.parseInt(request.getParameter("pagina"));
                filtro.setOffset((pag-1)*50); //se obtiene la sig pag
                asuntos = fachada.buscarAsuntosPorAreaKeet(filtro);
            } 

            CSVWriter wr1 = new CSVWriter(out);
            
            String[] arr = {"ID","Estatus","Pres.","Turno","Clasif.","Asunto","Instrucci�n","Urgente","Fecha env�o","Fecha vencimiento","�ltima Reprogramaci�n",
                            "Destinatarios","Estatus Resp.","Avance","�ltimo Avance","Fecha atenci�n","D�as Proceso/Atenci�n","D�as retraso","Observaciones"};

            wr1.writeNext(arr);

            for (AsuntoBean asunto: asuntos) {
                List<ProgramacionDTO> reprograma = fachada.obtenReprogramacionesAsunto(asunto.getIdasunto());
                arr[0]=String.valueOf(asunto.getIdconsecutivo());
                arr[1]=asunto.getEstatus().equals("P") ? "Pendiente" : "Atendido"; 
                arr[2]=asunto.getPresidencia() == null ? "" : asunto.getPresidencia().equals("P") ? "C/Resp.": (asunto.getPresidencia().equals("N") ? "S/Resp.":"");
                arr[3]=asunto.getNocontrol();
                arr[4]=asunto.getFuente().equals("I") ? "Interno" : "Externo";
                arr[5]=asunto.getDescripcion();
                arr[6]=asunto.getEstatustexto();
               
                arr[7]=asunto.getUrgente() != null && asunto.getUrgente().equals("S") ? "Urgente":"";
                arr[8]=asunto.getFechaingresoFormatoTexto();
                arr[9]=asunto.getFechaatenderFormatoTexto();
                arr[10]= reprograma != null && reprograma.size() > 0 ? reprograma.get(0).getFechareprogramaFormato():"";
                String destinatarios="",estatus="",avances="",fechas="",diasp="",diasr="",repavances="";
                for(ResponsableBean r:asunto.getResponsables()) {
                  if (r.getArea().getNivel() == 2) 
                    destinatarios = destinatarios+ r.getArea().getSiglas() + "\n\r";
                  else
                    destinatarios = destinatarios+ r.getArea().getNombre() + "\n\r";
                  
                  estatus = estatus + (r.getDatos().getEstatus().equals("A") ? "Atendido" : "Pendiente") + "\n\r";
                  avances = avances + r.getDatos().getAvance() + "%\n\r";
                  repavances = repavances + r.getUltimoAvance()+ "%\n\r";
                  //System.out.println(asunto.getIdconsecutivo()+"("+repavances.trim().length()+") -- "+repavances);
                  
//                  if(repavances.length() > 1) {
//                      int fail=asunto.getIdconsecutivo();
//                      System.out.println("**error" +fail+" error**");
//                  }
                  
                  fechas  = fechas  + r.getFechaatencionFormatoTexto() + "\n\r";
                  diasp = diasp + (r.getDatos().getDiasatencion() == 0 ? "": r.getDatos().getDiasatencion()) +"\n\r";
                  diasr = diasr + (r.getDatos().getDiasretraso() == 0 ? "": r.getDatos().getDiasretraso()) +"\n\r";
                }
                
                if(destinatarios.trim().length()>2) arr[11]=destinatarios.substring(0, destinatarios.length()-2);
                if(estatus.trim().length()>2) arr[12]=estatus.substring(0, estatus.length()-2);
                if(avances.trim().length()>2) arr[13]=avances.substring(0, avances.length()-2);
                if(repavances.trim().length() > 2) arr[14]=repavances.substring(0, repavances.length()-2);
                if(fechas.trim().length()>3) arr[15]=fechas.substring(0, fechas.length()-2);
                if(diasp.trim().length()>3) arr[16]=diasp.substring(0, diasp.length()-2);
                if(diasr.trim().length()>3) arr[17]=diasr.substring(0, diasr.length()-2);
                arr[18]=asunto.getObservaciones();
               
                wr1.writeNext(arr);
             }
             wr1.close();   

        } catch (Exception ex) {
            String toString = ex.toString();
            throw new RuntimeException(ex);
        } finally {
          out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
