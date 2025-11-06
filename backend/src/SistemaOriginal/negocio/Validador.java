
/*
 * Validador.java
 *
 * Created on 10 de marzo de 2006, 04:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.negocio;

import mx.org.inegi.dggma.sistemas.asuntos.fachada.FachadaDAO;
import java.util.*;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AccionBean;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AnexoDTO;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AsuntoBean;
import mx.org.inegi.dggma.sistemas.asuntos.dto.InstruccionDTO;
import mx.org.inegi.dggma.sistemas.asuntos.negocio.Utiles.Actividad;
import mx.org.inegi.dggma.sistemas.asuntos.vista.VistaListado;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AreaBean;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.UsuarioBean;
import mx.org.inegi.dggma.sistemas.asuntos.vista.PermisosAreasUsuario;

/**
 *
 * @author Joseluis.Mondragon
 */
public class Validador {
    private FachadaDAO fachada = new FachadaDAO(null);

    /** Creates a new instance of Validador */
    public Validador() {
    }

    public List<String> getErroresAsunto(AsuntoBean asunto, boolean ejecutivo) throws Exception {
        List<String> resultado = new LinkedList<String>();
        boolean correcto = true;
        if (asunto.getDescripcion().trim().length() == 0) {
            correcto = false;
            resultado.add("El nombre del asunto no puede quedar vacío");
        }
        if (asunto.getEstatustexto().trim().length() == 0) {
            correcto = false;
            resultado.add("La descripción del asunto no puede quedar vacía");
        }

        if (asunto.getResponsable() == null) {
            correcto = false;
            resultado.add("Se debe elegir al responsable del asunto");
        }

        if (correcto) {
            if (asunto.getIdasunto() != -1) {
                if (fachada.yaEsCorresponsable(asunto.getResponsable(), asunto.getIdasunto())) {
                    resultado.add("Esta área ya es co-responsable del asunto");
                }
            }
        }
        if (!ejecutivo) {
            if (asunto.getFechaingreso().trim().length() == 0) {
                correcto = false;
                resultado.add("La fecha de ingreso no puede quedar vacía");
            }
        }
        if (correcto) {
            String fecha = asunto.getFechaingreso().trim();
            if (fecha.length() != 6 && fecha.length() != 8 && fecha.length() != 10 && fecha.length() != 12) {
               correcto = false;
               resultado.add("La fecha de ingreso no tiene el tamaño adecuado (6, 8, 10 o 12)");
            }

            for (int i = 0; i < fecha.length(); i++) {
                if (fecha.charAt(i) < '0' || fecha.charAt(i) > '9') {
                    correcto = false;
                    resultado.add("El caracter en la posición " + (i + 1) + " no es un número");
                }
            }

            if (correcto) {
                int anio = Integer.parseInt(fecha.substring(0, 4));
                int mes = 0;
                int dia = 0;
                int hora = 0;
                int minuto = 0;
                if (fecha.length() >= 6) {
                    mes = Integer.parseInt(fecha.substring(4, 6));
                }
                if (fecha.length() >= 8) {
                    dia = Integer.parseInt(fecha.substring(6, 8));
                }
                if (fecha.length() >= 10) {
                    hora = Integer.parseInt(fecha.substring(8, 10));
                }
                if (fecha.length() == 12) {
                    minuto = Integer.parseInt(fecha.substring(10, 12));
                }

                if (fecha.length() == 4) {
                    Calendar calendario = new GregorianCalendar(anio, 0, 1);
                    if (calendario.get(Calendar.YEAR) != anio) {
                        correcto = false;
                        resultado.add("El año es incorrecto");
                    }
                }
                if (fecha.length() == 6) {
                    Calendar calendario = new GregorianCalendar(anio, mes - 1, 1);
                    if (calendario.get(Calendar.YEAR) != anio || calendario.get(Calendar.MONTH) != (mes - 1)) {
                        correcto = false;
                        resultado.add("El año o el mes son incorrectos");
                    }
                }
                if (fecha.length() == 8) {
                    Calendar calendario = new GregorianCalendar(anio, mes - 1, dia);
                    if (calendario.get(Calendar.YEAR) != anio || calendario.get(Calendar.MONTH) != (mes - 1) || calendario.get(Calendar.DAY_OF_MONTH) != dia) {
                        correcto = false;
                        resultado.add("El año, el mes o el día son incorrectos");
                    }
                }
                if (fecha.length() == 10) {
                    Calendar calendario = new GregorianCalendar(anio, mes - 1, dia, hora, 0);
                    if (calendario.get(Calendar.YEAR) != anio || calendario.get(Calendar.MONTH) != (mes - 1) || calendario.get(Calendar.DAY_OF_MONTH) != dia || calendario.get(Calendar.HOUR_OF_DAY) != hora) {
                        correcto = false;
                        resultado.add("El año, el mes, el día o la hora son incorrectos");
                    }
                }
                if (fecha.length() == 12) {
                    Calendar calendario = new GregorianCalendar(anio, mes - 1, dia, hora, minuto);
                    if (calendario.get(Calendar.YEAR) != anio || calendario.get(Calendar.MONTH) != (mes - 1) || calendario.get(Calendar.DAY_OF_MONTH) != dia || calendario.get(Calendar.HOUR_OF_DAY) != hora || calendario.get(Calendar.MINUTE) != minuto) {
                        correcto = false;
                        resultado.add("El año, el mes, el día, la hora o los minutos son incorrectos");
                    }
                }
            }
        }

        
        if (asunto.getFechaatender().trim().length() == 0) {
           correcto = false;
           resultado.add("La fecha límite para atención no puede quedar vacía");
        }
        
        if (correcto) {
            String fecha = asunto.getFechaatender().trim();
            String fechacompleta = "";
            if (fecha.length() != 4 && fecha.length() != 6 && fecha.length() != 8 && fecha.length() != 10 && fecha.length() != 12) {
                correcto = false;
                resultado.add("La fecha límite para atención no tiene el tamaño adecuado (4,6,8,10 ó 12)");
            }
            for (int i = 0; i < fecha.length(); i++) {
                if (fecha.charAt(i) < '0' || fecha.charAt(i) > '9') {
                    correcto = false;
                    resultado.add("El caracter en la posición " + (i + 1) + " no es un número");
                }
            }
            if (correcto) {
                int anio = 0;
                int mes = 0;
                int dia = 0;
                int hora = 0;
                int minuto = 0;
                if (fecha.length() >= 4){
                   anio = Integer.parseInt(fecha.substring(0, 4));
                }
                if (fecha.length() >= 6) {
                    mes = Integer.parseInt(fecha.substring(4, 6));
                }
                if (fecha.length() >= 8) {
                    dia = Integer.parseInt(fecha.substring(6, 8));
                }
                if (fecha.length() >= 10) {
                    hora = Integer.parseInt(fecha.substring(8, 10));
                }
                if (fecha.length() == 12) {
                    minuto = Integer.parseInt(fecha.substring(10, 12));
                    fechacompleta = fecha + "9999";
                }

                if (fecha.length() == 4) {
                    fechacompleta = fecha + "99999999";
                    Calendar calendario = new GregorianCalendar(anio, 0, 1);
                    if (calendario.get(Calendar.YEAR) != anio) {
                        correcto = false;
                        resultado.add("El año es incorrecto");
                    }
                }
                if (fecha.length() == 6) {
                    fechacompleta = fecha + "999999";
                    Calendar calendario = new GregorianCalendar(anio, mes - 1, 1);
                    if (calendario.get(Calendar.YEAR) != anio || calendario.get(Calendar.MONTH) != (mes - 1)) {
                        correcto = false;
                        resultado.add("El año o el mes son incorrectos");
                    }
                }
                if (fecha.length() == 8) {
                    fechacompleta = fecha + "9999";
                    Calendar calendario = new GregorianCalendar(anio, mes - 1, dia);
                    if (calendario.get(Calendar.YEAR) != anio || calendario.get(Calendar.MONTH) != (mes - 1) || calendario.get(Calendar.DAY_OF_MONTH) != dia) {
                        correcto = false;
                        resultado.add("El año, el mes o el día son incorrectos");
                    }
                }
                if (fecha.length() == 10) {
                    fechacompleta = fecha + "99";
                    Calendar calendario = new GregorianCalendar(anio, mes - 1, dia, hora, 0);
                    if (calendario.get(Calendar.YEAR) != anio || calendario.get(Calendar.MONTH) != (mes - 1) || calendario.get(Calendar.DAY_OF_MONTH) != dia || calendario.get(Calendar.HOUR_OF_DAY) != hora) {
                        correcto = false;
                        resultado.add("El año, el mes, el día o la hora son incorrectos");
                    }
                }
                if (fecha.length() == 12) {
                    fechacompleta = fecha;
                    Calendar calendario = new GregorianCalendar(anio, mes - 1, dia, hora, minuto);
                    if (calendario.get(Calendar.YEAR) != anio || calendario.get(Calendar.MONTH) != (mes - 1) || calendario.get(Calendar.DAY_OF_MONTH) != dia || calendario.get(Calendar.HOUR_OF_DAY) != hora || calendario.get(Calendar.MINUTE) != minuto) {
                        correcto = false;
                        resultado.add("El año, el mes, el día, la hora o los minutos son incorrectos");
                    }
                }
            }

            if (asunto.getFechaingreso().compareTo(fechacompleta) > 0) {
             correcto = false;
             resultado.add("La fecha límite para atención debe ser mayor o igual a la fecha de Alta");
            }

        }

       /* if (asunto.isUrgente()) {
            if (asunto.getHorasAtencionUrgente() < 1) {
                correcto = false;
                resultado.add("Se debe especificar cual es el tiempo en horas de atenci�n de un asunto urgente");
            }
        }*/

       /* if (!ejecutivo) {
            if (asunto.getInformesPeriodicidad() < 1 || asunto.getInformesUnidad().trim().length() == 0) {
                correcto = false;
                resultado.add("Se debe especificar los datos de periodicidad de informes de avance");
            }
        }*/

        if ((asunto.getEstatuscorto().equals("")) || (asunto.getEstatuscorto() == null)){
            resultado.add("Se debe especificar el estatus corto");
        }


        return resultado;
    }

   public boolean isAccionValida(AccionBean accion) throws Exception {
        boolean resultado = true;
        if (accion.getDescripcion().trim().length() == 0) {
            resultado = false;
        }


        if (resultado) {
            String fecha = accion.getFecha().trim();
            if (fecha.length() != 4 && fecha.length() != 6 && fecha.length() != 8 && fecha.length() != 10 && fecha.length() != 12) {
                resultado = false;
            }
            for (int i = 0; i < fecha.length(); i++) {
                if (fecha.charAt(i) < '0' || fecha.charAt(i) > '9') {
                    resultado = false;
                }
            }
            if (resultado) {
                int anio = Integer.parseInt(fecha.substring(0, 4));
                int mes = 0;
                int dia = 0;
                int hora = 0;
                int minuto = 0;
                if (fecha.length() >= 6) {
                    mes = Integer.parseInt(fecha.substring(4, 6));
                }
                if (fecha.length() >= 8) {
                    dia = Integer.parseInt(fecha.substring(6, 8));
                }
                if (fecha.length() >= 10) {
                    hora = Integer.parseInt(fecha.substring(8, 10));
                }
                if (fecha.length() == 12) {
                    minuto = Integer.parseInt(fecha.substring(10, 12));
                }

                if (fecha.length() == 4) {
                    Calendar calendario = new GregorianCalendar(anio, 0, 1);
                    if (calendario.get(Calendar.YEAR) != anio) {
                        resultado = false;
                    }
                }
                if (fecha.length() == 6) {
                    Calendar calendario = new GregorianCalendar(anio, mes - 1, 1);
                    if (calendario.get(Calendar.YEAR) != anio || calendario.get(Calendar.MONTH) != (mes - 1)) {
                        resultado = false;
                    }
                }
                if (fecha.length() == 8) {
                    Calendar calendario = new GregorianCalendar(anio, mes - 1, dia);
                    if (calendario.get(Calendar.YEAR) != anio || calendario.get(Calendar.MONTH) != (mes - 1) || calendario.get(Calendar.DAY_OF_MONTH) != dia) {
                        resultado = false;
                    }
                }
                if (fecha.length() == 10) {
                    Calendar calendario = new GregorianCalendar(anio, mes - 1, dia, hora, 0);
                    if (calendario.get(Calendar.YEAR) != anio || calendario.get(Calendar.MONTH) != (mes - 1) || calendario.get(Calendar.DAY_OF_MONTH) != dia || calendario.get(Calendar.HOUR_OF_DAY) != hora) {
                        resultado = false;
                    }
                }
                if (fecha.length() == 12) {
                    Calendar calendario = new GregorianCalendar(anio, mes - 1, dia, hora, minuto);
                    if (calendario.get(Calendar.YEAR) != anio || calendario.get(Calendar.MONTH) != (mes - 1) || calendario.get(Calendar.DAY_OF_MONTH) != dia || calendario.get(Calendar.HOUR_OF_DAY) != hora || calendario.get(Calendar.MINUTE) != minuto) {
                        resultado = false;
                    }
                }
            }

        }


        return resultado;
    }

   public List<String> getErroresAccion(AccionBean accion) throws Exception {
        List<String> resultado = new LinkedList<String>();
        if (accion.getDescripcion().trim().length() == 0) {
            resultado.add("La accióon no puede quedar vacía");
        }
        boolean correcto = true;

        if (accion.isTiene_acuerdos()){
            if (accion.getAcuerdo().trim().length() == 0){
                correcto = false;
                resultado.add("La fecha del acuerdo no puede quedar vacía");
            }

            if (accion.getAcuerdo_responsable().trim().length() == 0) {
                correcto = false;
                resultado.add("El responsable del acuerdo no puede quedar vacío");
            }

            if (accion.getFecha().trim().length() == 0) {
                correcto = false;
                resultado.add("El acuerdo no puede quedar vacío");
            } else {
               String fecha = accion.getAcuerdo_fecha().trim();
               correcto = validaFormatoFecha(fecha,resultado);
            }

        }
       
        return resultado;
    }

    private static boolean validaFormatoFecha(String fecha, List<String> errores){
        boolean correcto = true;
        if (fecha.length() != 4 && fecha.length() != 6 && fecha.length() != 8 && fecha.length() != 10 && fecha.length() != 12) {
            correcto = false;
            errores.add("La fecha no tiene el tamaño adecuado (4,6,8,10 ó 12)");
        }
        for (int i = 0; i < fecha.length(); i++) {
            if (fecha.charAt(i) < '0' || fecha.charAt(i) > '9') {
                correcto = false;
                errores.add("El caracter en la posición " + (i + 1) + " no es un número");
            }
        }
        if (correcto) {
            int anio = Integer.parseInt(fecha.substring(0, 4));
            int mes = 0;
            int dia = 0;
            int hora = 0;
            int minuto = 0;
            if (fecha.length() >= 6) {
                mes = Integer.parseInt(fecha.substring(4, 6));
            }
            if (fecha.length() >= 8) {
                dia = Integer.parseInt(fecha.substring(6, 8));
            }
            if (fecha.length() >= 10) {
                hora = Integer.parseInt(fecha.substring(8, 10));
            }
            if (fecha.length() == 12) {
                minuto = Integer.parseInt(fecha.substring(10, 12));
            }

            if (fecha.length() == 4) {
                Calendar calendario = new GregorianCalendar(anio, 0, 1);
                if (calendario.get(Calendar.YEAR) != anio) {
                    correcto = false;
                    errores.add("El año es incorrecto");
                }
            }
            if (fecha.length() == 6) {
                Calendar calendario = new GregorianCalendar(anio, mes - 1, 1);
                if (calendario.get(Calendar.YEAR) != anio || calendario.get(Calendar.MONTH) != (mes - 1)) {
                    correcto = false;
                    errores.add("El año o el mes son incorrectos");
                }
            }
            if (fecha.length() == 8) {
                Calendar calendario = new GregorianCalendar(anio, mes - 1, dia);
                if (calendario.get(Calendar.YEAR) != anio || calendario.get(Calendar.MONTH) != (mes - 1) || calendario.get(Calendar.DAY_OF_MONTH) != dia) {
                    correcto = false;
                    errores.add("El año, el mes o el día son incorrectos");
                }
            }
            if (fecha.length() == 10) {
                Calendar calendario = new GregorianCalendar(anio, mes - 1, dia, hora, 0);
                if (calendario.get(Calendar.YEAR) != anio || calendario.get(Calendar.MONTH) != (mes - 1) || calendario.get(Calendar.DAY_OF_MONTH) != dia || calendario.get(Calendar.HOUR_OF_DAY) != hora) {
                    correcto = false;
                    errores.add("El año, el mes, el día o la hora son incorrectos");
                }
            }
            if (fecha.length() == 12) {
                Calendar calendario = new GregorianCalendar(anio, mes - 1, dia, hora, minuto);
                if (calendario.get(Calendar.YEAR) != anio || calendario.get(Calendar.MONTH) != (mes - 1) || calendario.get(Calendar.DAY_OF_MONTH) != dia || calendario.get(Calendar.HOUR_OF_DAY) != hora || calendario.get(Calendar.MINUTE) != minuto) {
                    correcto = false;
                    errores.add("El año, el mes, el día, la hora o los minutos son incorrectos");
                }
            }
        }
       return correcto;

    }

    public boolean isAnexoValido(AnexoDTO anexo) {
        boolean resultado = true;
        if (anexo.getTitulo().trim().length() == 0) {
            resultado = false;
        }
        if (anexo.getNombrearchivo() == null) {
            resultado = false;
        }
        if (anexo.getTamanio() == 0) {
            resultado = false;
        }
        return resultado;
    }

    public List getErroresAnexo(AnexoDTO anexo) {
        List<String> resultado = new LinkedList<String>();
        if (anexo.getTitulo().trim().length() == 0) {
            resultado.add("El título no puede quedar vacío");
        }
        if (anexo.getNombrearchivo() == null) {
            resultado.add("No se ha elegido un archivo");
        }
        if (anexo.getTamanio() == 0) {
            resultado.add("El archivo no contiene información");
        }
        return resultado;
    }

    public boolean isInstruccionValida(InstruccionDTO instruccion) {
        boolean resultado = true;
        if (instruccion.getTextoInstruccion().trim().length() == 0) {
            resultado = false;
        }
        return resultado;
    }

    public List getErroresInstruccion(InstruccionDTO instruccion) {
        List<String> resultado = new LinkedList<String>();
        if (instruccion.getTextoInstruccion().trim().length() == 0) {
            resultado.add("Las instrucciones no pueden quedar vacías");
        }
        return resultado;
    }

    public boolean isRespuestaInstruccionValida(InstruccionDTO instruccion) {
        boolean resultado = true;
        if (instruccion.getTextoRespuesta().trim().length() == 0) {
            resultado = false;
        }
        return resultado;
    }

    public List getErroresRespuestaInstruccion(InstruccionDTO instruccion) {
        List<String> resultado = new LinkedList<String>();
        if (instruccion.getTextoRespuesta().trim().length() == 0) {
            resultado.add("La respuesta no puede quedar vacía");
        }
        return resultado;
    }

    public boolean isCorresponsableValido(AreaBean corresponsable, Integer idAsuntoCapturado) throws Exception {
        boolean resultado = true;
        if (fachada.esResponsablePrincipal(corresponsable, idAsuntoCapturado)) {
            resultado = false;
        }
        if (fachada.yaEsCorresponsable(corresponsable, idAsuntoCapturado)) {
            resultado = false;
        }
        return resultado;
    }

    public List getErroresCorresponsable(AreaBean corresponsable, Integer idAsuntoCapturado) throws Exception {
        List<String> resultado = new LinkedList<String>();
        if (fachada.esResponsablePrincipal(corresponsable, idAsuntoCapturado)) {
            resultado.add("Esta área ya es la responsable principal");
        }
        if (fachada.yaEsCorresponsable(corresponsable, idAsuntoCapturado)) {
            resultado.add("Esta área ya es co-responsable del asunto");
        }
        return resultado;
    }

       
    public boolean asignaRol(UsuarioBean usuario, Integer idpermiso, VistaListado vistaListado) throws Exception {
        boolean rolValido = false;
        if (usuario.isPermisoValido(idpermiso)) {
            usuario.asignaPermisoActual(idpermiso);
            rolValido = true;
        }
        asignaPermisosHerramientas(usuario, vistaListado);
        return rolValido;
    }
   
    private void asignaPermisosHerramientas(UsuarioBean usuario, VistaListado vistaListado) {
          vistaListado.setMostrarAsuntosNoPublicados(usuario.isTienePermisosActividad(Actividad.VERNOPUBLICADOS));
          vistaListado.setHerramientaVerReportes(usuario.isTienePermisosActividad(Actividad.REPORTES));
          vistaListado.setHerramientaAdmonInstrucciones(usuario.isTienePermisosActividad(Actividad.ADMONINSTRUCCIONES));
          vistaListado.setHerramientaAdmonAsuntos(usuario.isTienePermisosActividad(Actividad.ADMONASUNTOS));
          vistaListado.setHerramientaEdicionEstatus(usuario.isTienePermisosActividad(Actividad.EDITARESTATUS));
          vistaListado.setHerramientaAdmonAcciones(usuario.isTienePermisosActividad(Actividad.ADMONACCIONES));
          vistaListado.setHerramientaFiltroResponsable(true);
    }
}
