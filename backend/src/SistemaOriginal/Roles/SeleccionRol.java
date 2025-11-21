/*
 * SeleccionRol.java
 *
 * Created on 15 de octubre de 2006, 08:46 PM
 */
package mx.org.inegi.dggma.sistemas.asuntos.controladorNew;


import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import mx.org.inegi.dggma.sistemas.asuntos.dto.PermisoDTO;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.DelegadoNegocio;
import mx.org.inegi.dggma.sistemas.asuntos.vista.VistaListado;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AreaBean;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.PermisoBean;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.UsuarioBean;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.AdministraUsuariosAreas;
import mx.org.inegi.dggma.sistemas.asuntos.vista.PermisosAreasUsuario;

/**
 *
 * @author Jos Luis Mondragn
 * @version
 */
public class SeleccionRol extends HttpServlet {

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idPermisoStr = request.getParameter("idPermiso");
            int idPermiso = Integer.parseInt(idPermisoStr);

            HttpSession sesion = request.getSession();
            UsuarioBean usuario = (UsuarioBean) sesion.getAttribute("usuario");
            String urlSalto = "";
            boolean rolValido = usuario.isPermisoValido(idPermiso);
            if (rolValido) {
                urlSalto = "/iniciaSesion.do?idPermiso="+idPermisoStr;
            } else {
                urlSalto = "/seleccionRol.jsp";
                request.setAttribute("mensajes", "Seleccione un rol válido para su clave");
            }

            RequestDispatcher salta = request.getRequestDispatcher(urlSalto);
            salta.forward(request, response);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
