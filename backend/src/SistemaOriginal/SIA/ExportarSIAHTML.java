/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.org.inegi.dggma.sistemas.asuntos.controladorNew;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mx.org.inegi.dggma.sistemas.asuntos.dto.ProgramacionDTO;
import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaDAO;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AreasConsulta;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AsuntoBean;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.FiltroAsunto;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.Utiles;

/**
 *
 * @author jacqueline.nino
 */
public class ExportarSIAHTML extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sesion=request.getSession();
        AreasConsulta areasConsulta = (AreasConsulta) sesion.getAttribute("areasConsulta");
        FiltroAsunto filtro = (FiltroAsunto) sesion.getAttribute("filtroConsulta");
        
         try {
            FachadaDAO fachada = new FachadaDAO(areasConsulta);
            filtro.setLimitAll(true); //Se consultan todos los registros
            List<AsuntoBean> asuntos = fachada.buscarAsuntosPorAreaKeet(filtro);
            for(AsuntoBean asunto:asuntos){
                asunto.setDescripcion(Utiles.quitaSaltos(asunto.getDescripcion()));
                if (asunto.getReprogramado()){
                  List<ProgramacionDTO> reprogramaciones = fachada.obtenReprogramacionesAsunto(asunto.getIdasunto());
                  ProgramacionDTO ultimaReprograma = reprogramaciones != null && reprogramaciones.size() > 0 ? reprogramaciones.get(reprogramaciones.size()-1):null;
                  String dato = ultimaReprograma != null? ultimaReprograma.getFechareprogramaFormato()+" " +ultimaReprograma.getJustificacion():"";
                  asunto.setFechaUltimaReprogramacion(dato);
                }
            }
            request.setAttribute("asuntosConsulta", asuntos);
            RequestDispatcher salta = request.getRequestDispatcher("exportaSIA.jsp");
            salta.forward(request, response);
            
         } catch (Exception ex) {
           throw new RuntimeException(ex);
         }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
