/*
 * Login.java
 *
 * Created on 16 de abril de 2006, 10:32 PM
 */
package mx.org.inegi.dggma.sistemas.asuntos.controlAcceso;

import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.*;
//import mx.org.inegi.dggma.Autenticar;

import mx.org.inegi.dggma.sistemas.asuntos.calendario.ListadoAnios;
import mx.org.inegi.dggma.sistemas.asuntos.datosglobales.DatosGlobales;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.DatosLogin;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.UsuarioBean;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.AdministraUsuariosAreas;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.Utiles;
import mx.org.inegi.dggma.sistemas.asuntos.datosglobales.Autenticar;

/**
 *
 * @author Jos Luis Mondragn
 * @version
 */
public class Login extends HttpServlet {

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession sesion = request.getSession();
            sesion.setAttribute("nomSistema", "DGG_JAVA_Sistema_Asuntos_UGMA");
            response.setContentType("application/x-json;charset=UTF-8");
            
            String username = request.getParameter("usuario");
            if (username == null) {
                username = "";
            }
            
            String contrasenia = request.getParameter("contrasenia");
            if (contrasenia == null) {
                contrasenia = "";
            }
            contrasenia = new String(contrasenia.getBytes("ISO-8859-1"),"UTF-8");

            DatosLogin datosLogin = new DatosLogin();
            datosLogin.setUsuario(username);
            datosLogin.setContrasenia(contrasenia);

           
            AdministraUsuariosAreas administraUsuarios = new AdministraUsuariosAreas();
            UsuarioBean usuario = administraUsuarios.buscaUsuario(datosLogin);
            String urlSalto = "";
            boolean isInegiUser = true;
            if(usuario != null) {
                //boolean usuarioValido = true;
                if ((datosLogin.getUsuario() != null || !datosLogin.getUsuario().equals("")) && (datosLogin.getContrasenia() != null || !datosLogin.getContrasenia().equals(""))) {

                  if(DatosGlobales.getAutenticaLDAP()) {
                      try {
                          isInegiUser = Autenticar.validaUsuario(datosLogin.getUsuario(), datosLogin.getContrasenia(), "DGG_JAVA_ASUNTOSPENDIENTES");
                          //Autenticar autenticar = new Autenticar(datosLogin.getUsuario(), datosLogin.getContrasenia(), "DGG_JAVA_ASUNTOSPENDIENTES");
                          if (!isInegiUser) {
                                isInegiUser = false;
                                sesion.setAttribute("usuario", null);
                                request.setAttribute("mensajes", "No cuenta con permisos para acceder al sistema, contacte al administrador");
                                urlSalto = "/login.jsp";
                          }
                      } catch (Exception e) {
                        request.getRequestDispatcher("/errorAutenticacion.jsp").forward(request, response);
                        e.printStackTrace();
                        return;
                      }

                  }

                   if (isInegiUser && usuario != null) {
                        sesion.setAttribute("usuario", usuario);
                        urlSalto = "/inicio.do";
                    } 

                } else {
                    sesion.setAttribute("usuario", null);
                    request.setAttribute("mensajes", "La cuenta no se encuentra dada de alta en el sistema o la contraseña es nula, revise o contacte al administrador");
                    urlSalto = "/login.jsp";
                }
            } else {
                sesion.setAttribute("usuario", null);
                request.setAttribute("mensajes", "La cuenta no se encuentra dada de alta en el sistema o la contraseña es nula, revise o contacte al administrador");
                urlSalto = "/login.jsp";
            }
            RequestDispatcher salto = request.getRequestDispatcher(urlSalto);
            salto.forward(request, response);
            
        } catch (Exception ex) {
                //throw new RuntimeException(ex);
            request.getRequestDispatcher("/errorBD.jsp").forward(request, response);
            ex.printStackTrace();
            return;
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
