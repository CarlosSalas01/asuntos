/*
 * Utiles.java
 *
 * Created on 6 de abril de 2006, 08:57 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mx.org.inegi.dggma.sistemas.asuntos.negocio;

import java.io.Serializable;
import java.util.*;

/**
 *
 * @author Jos? Luis Mondrag?n
 */
public class Utiles implements Serializable {

    public enum Actividad { REPORTES, ADMONASUNTOS, ADMONACCIONES, EDITARESTATUS, ADMONINSTRUCCIONES, VERNOPUBLICADOS, VERPUBLICADOS, CONSULTA, CAPTURAAVANCE};
    /*ADMONASUNTOS comprende:
     * Insertar, Eliminar, Editar y Publicar Asuntos e Insertar, Eliminar, Editar anexos a los asuntos
     *EDITARESTATUS:
     *  Editar Estatus y la Condici�n actual de un asunto
     *ADMONACCIONES:
     *  Insertar, Eliminar, Editar, Publicar Acciones e Insertar, Eliminar, Editar anexos a las acciones
     *ADMONINSTRUCCIONES:
     *  Insertar, Eliminar y editar instrucciones
     *VERNOPUBLICADOS:
     *  Ver asuntos no publicados
     *VERPUBLICADOS:
     *  Ver asunto publicados
     */

    private Random random=null;
    public static int maxCaracteres = 200;

    
    /** Creates a new instance of Utiles */
    public Utiles() {
        random=new Random();
    }
    
    public long getIdUnico(){
        return Math.abs(random.nextLong());
    }
    
    public String getFondo(){
        Calendar calendario=new GregorianCalendar();
        int dia=calendario.get(Calendar.DAY_OF_WEEK);
        return getNombreArchivoFondo(dia);
    }
    
    private String getNombreArchivoFondo(int numeroDia){
        String resultado=null;
        switch(numeroDia){
            case Calendar.SUNDAY:resultado="fondo04.jpg"; break;
            case Calendar.MONDAY:resultado="fondo01.jpg"; break;
            case Calendar.TUESDAY:resultado="fondo04.jpg"; break;
            case Calendar.WEDNESDAY:resultado="fondo03.jpg"; break;
            case Calendar.THURSDAY:resultado="fondo02.jpg"; break;
            case Calendar.FRIDAY:resultado="fondo05.jpg"; break;
            case Calendar.SATURDAY:resultado="fondo02.jpg"; break;
        }
        return resultado;
    }
    
    public static String getFecha(){
        Calendar calendario=new GregorianCalendar();
        return getDia(calendario.get(Calendar.DAY_OF_WEEK))+" "+calendario.get(Calendar.DAY_OF_MONTH)+" de "+getMes(calendario.get(Calendar.MONTH)+1)+" de "+calendario.get(Calendar.YEAR);
    }

    public static String getFechaCorta(String fecha){
        String resultado="";
        String anio="";
        String mes="";
        String dia="";

        if(fecha!=null){
            if(fecha.length()>=4){
                anio=fecha.substring(0,4);
            }
            if(fecha.length()>=6){
                mes=fecha.substring(4,6);
            }
            if(fecha.length()>=8){
                dia=fecha.substring(6,8);
            }
            resultado = dia + "/"+mes+"/"+anio;
        }
        

        return resultado;
    }


    public static String getSwapFecha(String fecha){ 
        //Cambia formato ddmmaa  aammdd
        String resultado="";
        String anio="";
        String mes="";
        String dia="";

        if(fecha!=null){
            if(fecha.length()==8){
                dia =fecha.substring(0,2);
                mes =fecha.substring(3,5);
                anio=fecha.substring(6,8);

            }
            if(fecha.length()==10){
                dia =fecha.substring(0,2);
                mes =fecha.substring(3,5);
                anio=fecha.substring(6,10);
            }
            
            resultado = anio+mes+dia;
        }
        

        return resultado;
    }

    public static String getFechaFlexible(String fecha){
        String resultado="";
        if(fecha!=null && !fecha.equals("")){
            int anio=0;
            int mes=0;
            int dia=0;
            int hora=0;
            int minuto=0;
            if(fecha.length()>=4){
                anio=Integer.parseInt(fecha.substring(0,4));
            }
            if(fecha.length()>=6){
                mes=Integer.parseInt(fecha.substring(4,6));
            }
            if(fecha.length()>=8){
                dia=Integer.parseInt(fecha.substring(6,8));
            }
            if(fecha.length()>=10){
                hora=Integer.parseInt(fecha.substring(8,10));
            }
            if(fecha.length()==12){
                minuto=Integer.parseInt(fecha.substring(10,12));
            }
            
            if(fecha.length()==4){
                if(anio==9999){
                    resultado="-";
                }else{
                    resultado="año "+anio;
                }
            }
            if(fecha.length()==6){
                resultado=getMes(mes)+" de "+anio;
            }
            if(fecha.length()==8){
                Calendar calendario=new GregorianCalendar(anio,mes-1,dia);
                resultado=getDia(calendario.get(Calendar.DAY_OF_WEEK))+" "+calendario.get(Calendar.DAY_OF_MONTH)+" de "+getMes(calendario.get(Calendar.MONTH)+1)+" de "+calendario.get(Calendar.YEAR);
            }
            if(fecha.length()==10){
                Calendar calendario=new GregorianCalendar(anio,mes-1,dia);
                resultado=getDia(calendario.get(Calendar.DAY_OF_WEEK))+" "+calendario.get(Calendar.DAY_OF_MONTH)+" de "+getMes(calendario.get(Calendar.MONTH)+1)+" de "+calendario.get(Calendar.YEAR);
                resultado=resultado+", "+hora+" horas";
            }
            if(fecha.length()==12){
                Calendar calendario=new GregorianCalendar(anio,mes-1,dia);
                resultado=getDia(calendario.get(Calendar.DAY_OF_WEEK))+" "+calendario.get(Calendar.DAY_OF_MONTH)+" de "+getMes(calendario.get(Calendar.MONTH)+1)+" de "+calendario.get(Calendar.YEAR);
                resultado=resultado+", "+hora+":"+dosDigitos(minuto)+" horas";
            }
        }
        return resultado;
    }

    public static String getFechaFlexibleSDia(String fecha){
        String resultado="";
        if(fecha!=null){
            int anio=0;
            int mes=0;
            int dia=0;
            int hora=0;
            int minuto=0;
            if(fecha.length()>=4){
                anio=Integer.parseInt(fecha.substring(0,4));
            }
            if(fecha.length()>=6){
                mes=Integer.parseInt(fecha.substring(4,6));
            }
            if(fecha.length()>=8){
                dia=Integer.parseInt(fecha.substring(6,8));
            }
            if(fecha.length()>=10){
                hora=Integer.parseInt(fecha.substring(8,10));
            }
            if(fecha.length()==12){
                minuto=Integer.parseInt(fecha.substring(10,12));
            }

            if(fecha.length()==4){
                if(anio==9999){
                    resultado="-";
                }else{
                    resultado="año "+anio;
                }
            }
            if(fecha.length()==6){
                resultado=getMes(mes)+"."+anio;
            }
            if(fecha.length()==8){
                Calendar calendario=new GregorianCalendar(anio,mes-1,dia);
                resultado=calendario.get(Calendar.DAY_OF_MONTH)+"."+getMes(calendario.get(Calendar.MONTH)+1)+"."+calendario.get(Calendar.YEAR);
            }
            if(fecha.length()==10){
                Calendar calendario=new GregorianCalendar(anio,mes-1,dia);
                resultado=calendario.get(Calendar.DAY_OF_MONTH)+"."+getMes(calendario.get(Calendar.MONTH)+1)+"."+calendario.get(Calendar.YEAR);
                resultado=resultado+", "+hora+" horas";
            }
            if(fecha.length()==12){
                Calendar calendario=new GregorianCalendar(anio,mes-1,dia);
                resultado=calendario.get(Calendar.DAY_OF_MONTH)+"."+getMes(calendario.get(Calendar.MONTH)+1)+"."+calendario.get(Calendar.YEAR);
                resultado=resultado+", "+hora+":"+dosDigitos(minuto)+" horas";
            }
        }
        return resultado;
    }

    private static String getMes(int numeroMes){
        String resultado=null;
        switch(numeroMes){
            case 1:resultado="enero"; break;
            case 2:resultado="febrero"; break;
            case 3:resultado="marzo"; break;
            case 4:resultado="abril"; break;
            case 5:resultado="mayo"; break;
            case 6:resultado="junio"; break;
            case 7:resultado="julio"; break;
            case 8:resultado="agosto"; break;
            case 9:resultado="septiembre"; break;
            case 10:resultado="octubre"; break;
            case 11:resultado="noviembre"; break;
            case 12:resultado="diciembre"; break;
        }
        return resultado;
    }
    
    public static String getDia(int numeroDia){
        String resultado=null;
        switch(numeroDia){
            case Calendar.SUNDAY:resultado="domingo"; break;
            case Calendar.MONDAY:resultado="lunes"; break;
            case Calendar.TUESDAY:resultado="martes"; break;
            case Calendar.WEDNESDAY:resultado="miércoles"; break;
            case Calendar.THURSDAY:resultado="jueves"; break;
            case Calendar.FRIDAY:resultado="viernes"; break;
            case Calendar.SATURDAY:resultado="sábado"; break;
        }
        return resultado;
    }
    
    public static String nombreArchivoValido(String nombreArchivo){
        String resultado=null;
        int i=nombreArchivo.length()-1;
        while(nombreArchivo.charAt(i)!='/'&&nombreArchivo.charAt(i)!='\\'&&i>0){
            i--;
        }
        String soloNombre=nombreArchivo.substring(i);
        String nuevoNombre="";
        for(int j=0;j<soloNombre.length();j++){
            char c=soloNombre.charAt(j);
            switch (c){
                case 'á':case 'à':c='a';break;
                case 'é':case 'è':c='e';break;
                case 'í':case 'ì':c='i';break;
                case 'ó':case 'ò':c='o';break;
                case 'ú':case 'ù':case 'ü':c='u';break;
                
                case 'Á':case 'À':c='A';break;
                case 'É':case 'È':c='E';break;
                case 'Í':case 'Ì':c='I';break;
                case 'Ó':case 'Ò':c='O';break;
                case 'Ú':case 'Ù':case 'Ü':c='U';break;
                
                case 'ñ':c='n';break;
                case 'Ñ':c='N';break;
            }
            
            if((c>='a'&&c<='z')||(c>='A'&&c<='Z')||(c>='0'&&c<='9')||(c=='.')||(c=='_')||(c=='-')){
                nuevoNombre=nuevoNombre+c;
            }
        }
        
        if(nuevoNombre.length()==0){
            nuevoNombre="NombreIvalido";
        }
        resultado=nuevoNombre;
        return resultado;
    }
    
    public static String claveUnica(){
        Random random=new Random();
        return getFechaHora()+"_"+random.nextInt();
    }
    
    public static String getFechaHora(){
        Calendar calendar=new GregorianCalendar();
        String fechaHora=""+calendar.get(Calendar.YEAR)+
                dosDigitos(calendar.get(Calendar.MONTH)+1)+
                dosDigitos(calendar.get(Calendar.DAY_OF_MONTH))+
                dosDigitos(calendar.get(Calendar.HOUR_OF_DAY))+
                dosDigitos(calendar.get(Calendar.MINUTE));
        return fechaHora;
    }
    public static String getFechaHoraJava(){
        Calendar calendar=new GregorianCalendar();
        String fechaHora=""+calendar.get(Calendar.YEAR)+
                dosDigitos(calendar.get(Calendar.MONTH))+
                dosDigitos(calendar.get(Calendar.DAY_OF_MONTH))+
                dosDigitos(calendar.get(Calendar.HOUR_OF_DAY))+
                dosDigitos(calendar.get(Calendar.MINUTE));
        return fechaHora;
    }
   public static String getHora(){
        Calendar calendar=new GregorianCalendar();
        String Hora=""+ dosDigitos(calendar.get(Calendar.HOUR_OF_DAY))+":"+
                dosDigitos(calendar.get(Calendar.MINUTE));
        return Hora;
    }


    public static String getFechaHoy(){
        Calendar calendar=new GregorianCalendar();
        String fecha=""+calendar.get(Calendar.YEAR)+
                dosDigitos(calendar.get(Calendar.MONTH)+1)+
                dosDigitos(calendar.get(Calendar.DAY_OF_MONTH));
        return fecha;
    }
    
    public static String getFechaDMA(){
        Calendar calendar=new GregorianCalendar();
        String fecha = dosDigitos(calendar.get(Calendar.DAY_OF_MONTH))+"/"+
                   dosDigitos(calendar.get(Calendar.MONTH)+1)+"/"+
                   calendar.get(Calendar.YEAR);
        return fecha;
    }

    public static String getFechaHoyAnio(){
       Calendar calendar=new GregorianCalendar();
       return ""+calendar.get(Calendar.YEAR);
    }
      
    public static String getFechaHoyAnioMes(){
       Calendar calendar=new GregorianCalendar();
       String fechaHora=""+calendar.get(Calendar.YEAR)+
                dosDigitos(calendar.get(Calendar.MONTH)+1);
       return fechaHora;
    }


    public static String getFechaManiana(){
        Calendar calendar=new GregorianCalendar();
        calendar.add(Calendar.DATE,1);
        String fechaHora=""+calendar.get(Calendar.YEAR)+
                dosDigitos(calendar.get(Calendar.MONTH)+1)+
                dosDigitos(calendar.get(Calendar.DAY_OF_MONTH));
        return fechaHora;
    }
    public static String getFechaManianaLunes(){
        Calendar hoy=new GregorianCalendar();
        int year = hoy.get(Calendar.YEAR);
        int month = hoy.get(Calendar.MONTH);
        int day = hoy.get(Calendar.DAY_OF_MONTH);
        GregorianCalendar fechaHoy = new GregorianCalendar(year,month,day);
        
        if(fechaHoy.get(Calendar.DAY_OF_WEEK) == 6) {
            fechaHoy.add(Calendar.DATE,3);
        } else {
            fechaHoy.add(Calendar.DATE,1);
        }
        String fechaHora=""+fechaHoy.get(Calendar.YEAR)+
                dosDigitos(fechaHoy.get(Calendar.MONTH)+1)+
                dosDigitos(fechaHoy.get(Calendar.DAY_OF_MONTH));
        return fechaHora;
    }
    public static String getFechamasXDias(String fechapivote,int xdias) {
        int anio=Integer.parseInt(fechapivote.substring(0,4));
        int mes=Integer.parseInt(fechapivote.substring(4,6));
        int dia=Integer.parseInt(fechapivote.substring(6,8));
        GregorianCalendar calendar = new GregorianCalendar(anio,mes,dia);
        if(calendar.get(Calendar.DAY_OF_WEEK) == 6) {
            xdias = xdias + 2;
        }
        calendar.add(Calendar.DATE,xdias);
        String fecha=""+ calendar.get(Calendar.YEAR)+
              dosDigitos(calendar.get(Calendar.MONTH)+1)+
              dosDigitos(calendar.get(Calendar.DAY_OF_MONTH));
        return fecha;
    }
    public static String getFecha7Dias(){
        Calendar calendar=new GregorianCalendar();
        calendar.add(Calendar.DATE,7);
        String fechaHora=""+calendar.get(Calendar.YEAR)+
                dosDigitos(calendar.get(Calendar.MONTH)+1)+
                dosDigitos(calendar.get(Calendar.DAY_OF_MONTH));
        return fechaHora;
    }

    //Jacky
    public static String getFechaAntes(int dias){
        Calendar calendar=new GregorianCalendar();
        calendar.add(Calendar.DATE, - dias);
        String fechaHora=""+calendar.get(Calendar.YEAR)+
              dosDigitos(calendar.get(Calendar.MONTH)+1)+
              dosDigitos(calendar.get(Calendar.DAY_OF_MONTH))+
              dosDigitos(calendar.get(Calendar.HOUR_OF_DAY))+
              dosDigitos(calendar.get(Calendar.MINUTE));

    return fechaHora;
   }

    public static String getFechaHoraDosDiasAntes(){
        Calendar calendar=new GregorianCalendar();
        calendar.add(Calendar.DATE,-2);
        String fechaHora=""+calendar.get(Calendar.YEAR)+
                dosDigitos(calendar.get(Calendar.MONTH)+1)+
                dosDigitos(calendar.get(Calendar.DAY_OF_MONTH))+
                dosDigitos(calendar.get(Calendar.HOUR_OF_DAY))+
                dosDigitos(calendar.get(Calendar.MINUTE));
        return fechaHora;
    }
    
    
    public static String getFechaHoraSegundos(){
        Calendar calendar=new GregorianCalendar();
        String fechaHora=""+calendar.get(Calendar.YEAR)+
                dosDigitos(calendar.get(Calendar.MONTH)+1)+
                dosDigitos(calendar.get(Calendar.DAY_OF_MONTH))+
                dosDigitos(calendar.get(Calendar.HOUR_OF_DAY))+
                dosDigitos(calendar.get(Calendar.MINUTE))+
                dosDigitos(calendar.get(Calendar.SECOND));
        return fechaHora;
    }
    
    public static String dosDigitos(int numero){
        String numeroStr=null;
        if(numero<10){numeroStr="0"+numero;}else{numeroStr=""+numero;}
        return numeroStr;
    }
    
    public static String trima(String texto){
        return texto!=null?texto.trim():null;
    }
    
    public static String parrafeaHTML(String texto){
      if (texto == null) return "";
      
        String[] parrafos=texto.split("\n");
        String resultado="";
        String saltoLinea="";
        for(int i=0;i<parrafos.length;i++){
            resultado=resultado+saltoLinea+parrafos[i];
            saltoLinea="<br>";
        }
        return resultado;
    }
    public static String quitaSaltos(String texto){
        if (texto == null) return "";
        String resultado = texto.replace("\r\n", " ");
        return resultado;
    }

    
    public static String getFechamenosXDias(String fechapivote,int xdias) {
        //Calendar fec = new GregorianCalendar();
        int anio=Integer.parseInt(fechapivote.substring(0,4));
        int mes=Integer.parseInt(fechapivote.substring(4,6));
        int dia=Integer.parseInt(fechapivote.substring(6,8));
        GregorianCalendar calendar = new GregorianCalendar(anio,mes,dia);
        calendar.add(Calendar.DATE,-xdias);
        String fecha=""+ calendar.get(Calendar.YEAR)+
              dosDigitos(calendar.get(Calendar.MONTH))+
              dosDigitos(calendar.get(Calendar.DAY_OF_MONTH));
        return fecha;
    }
     
    public static String getFechamenos3Dias(String fechapivote) {
    
        //Calendar fec = new GregorianCalendar();
        int anio=Integer.parseInt(fechapivote.substring(0,4));
        int mes=Integer.parseInt(fechapivote.substring(4,6));
        int dia=Integer.parseInt(fechapivote.substring(6,8));
        GregorianCalendar calendar = new GregorianCalendar(anio,mes,dia);
        calendar.add(Calendar.DATE,-3);
        
        
        String fecha=""+ calendar.get(Calendar.YEAR)+
              dosDigitos(calendar.get(Calendar.MONTH)+1)+
              dosDigitos(calendar.get(Calendar.DAY_OF_MONTH));
        
        /*String diaStr = String.valueOf(fec.get(Calendar.DAY_OF_MONTH));
        String anioStr = String.valueOf(fec.get(Calendar.YEAR));
        int mesStr = fec.get(Calendar.MONTH) + 1;
        String mes2 = String.valueOf(mes);

        if (dia.length()==1) dia="0"+dia;
        if (mes2.length()==1) mes2="0"+mes2;
        fecha=anio+mes2+dia;*/

        return fecha;

    }

    public static void restaFechasEnAniosDias(String fecha1, String fecha2){
        if (fecha1.length() == 8 && fecha2.length() == 8){
            int anio1=Integer.parseInt(fecha1.substring(0,4));
            int mes1=Integer.parseInt(fecha1.substring(4,6));
            int dia1=Integer.parseInt(fecha1.substring(6,8));

            int anio2=Integer.parseInt(fecha1.substring(0,4));
            int mes2=Integer.parseInt(fecha1.substring(4,6));
            int dia2=Integer.parseInt(fecha1.substring(6,8));

            /*CREAMOS EL GREGORIAN CALENDAR DE LAS DOS FECHAS*/
            GregorianCalendar date1 = new GregorianCalendar(anio1,mes1,dia1);
            GregorianCalendar date2 = new GregorianCalendar(anio2,mes2,dia2);

            /* COMPROBAMOS SI ESTAMOS EN EL MISMO ANYO */
            if (date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)) {
                System.out.println( "Valor de Resta simple: " +String.valueOf(date2.get(Calendar.DAY_OF_YEAR) - date1.get(Calendar.DAY_OF_YEAR)));
            } else {
                /* SI ESTAMOS EN DISTINTO ANYO COMPROBAMOS QUE EL ANYO DEL DATEINI NO SEA BISIESTO
                * SI ES BISIESTO SON 366 DIAS EL ANYO
                * SINO SON 365
                */
                int diasAnyo = date1.isLeapYear(date1.get(Calendar.YEAR)) ? 366 : 365;

                /* CALCULAMOS EL RANGO DE ANYOS */
                int rangoAnyos = date2.get(Calendar.YEAR) - date1.get(Calendar.YEAR);

                /* CALCULAMOS EL RANGO DE DIAS QUE HAY */
                int rango = (rangoAnyos * diasAnyo) + (date2.get(Calendar.DAY_OF_YEAR) - date1.get(Calendar.DAY_OF_YEAR));

//                System.out.println("Valor de rangoDias:" + (date2.get(Calendar.DAY_OF_YEAR) - date1.get(Calendar.DAY_OF_YEAR)));
//                System.out.println("Valor de rangoAnyos: " + rangoAnyos);
//                System.out.println("Valor de rango: " + rango);


            }
        }
    }
    
    public static int restaFechasEnDias(String _fecha1, String _fecha2){
          int nodias=0;
          if (_fecha1.length() >= 8 && _fecha2.length() >=8){
            String fecha1 = _fecha1.substring(0,8);
            String fecha2 = _fecha2.substring(0,8);
            if (fecha1.compareTo(fecha2) < 0) {
                int anio1=Integer.parseInt(fecha1.substring(0,4));
                int mes1=Integer.parseInt(fecha1.substring(4,6));
                int dia1=Integer.parseInt(fecha1.substring(6,8));

                int anio2=Integer.parseInt(fecha2.substring(0,4));
                int mes2=Integer.parseInt(fecha2.substring(4,6));
                int dia2=Integer.parseInt(fecha2.substring(6,8));

                /*Calendar c = Calendar.getInstance();

                //fecha inicio
                Calendar fechaInicio = new GregorianCalendar();
                fechaInicio.set(anio1,mes1,dia1);
                //fecha fin
                Calendar fechaFin = new GregorianCalendar();
                fechaFin.set(anio2,mes2,dia2);

                c.setTimeInMillis(

                    fechaFin.getTime().getTime() - fechaInicio.getTime().getTime());

                nodias = c.get(Calendar.DAY_OF_YEAR);*/
                
                GregorianCalendar f1 = new GregorianCalendar(anio1,mes1,dia1);
                GregorianCalendar f2 = new GregorianCalendar(anio2,mes2,dia2);

                long time = f2.getTimeInMillis() - f1.getTimeInMillis();
                double ddias = (double)time/(60.0d*24.0d);

                nodias = (int) ddias / 60000;
                
            }
            
        }    
        return  nodias;         
    }
    
    public static String get5diasHabiles(int day, int month, int year){
        Calendar fecha = new GregorianCalendar(year,month,day);
        String dia5="";
        /* Cambio de �ltima hra. del Sr. Osvaldo, ahora quiere 10 dias naturales
        for(int i = 1 ;i <= 5 ; i++){
            if(fecha.get(Calendar.DAY_OF_WEEK)==6) fecha.add(Calendar.DATE, 3); //7=sabado,0=dom.
            else fecha.add(Calendar.DATE, 1);
        }*/
        fecha.add(Calendar.DATE, 10);
        String dia = String.valueOf(fecha.get(Calendar.DAY_OF_MONTH));
        String anio = String.valueOf(fecha.get(Calendar.YEAR));
        int mes = fecha.get(Calendar.MONTH) + 1;
        String mes2 = String.valueOf(mes);
        if (dia.length()==1) dia="0"+dia;
        if (mes2.length()==1) mes2="0"+mes2;
        dia5=dia+"/"+mes2+"/"+anio;
        return dia5;
    }
    public static String get5diasHabilesOld(int day, int month, int year){
        Calendar fecha = new GregorianCalendar(year,month,day);
        String dia5="";
        for(int i = 1 ;i < 5 ; i++){
            if(fecha.get(Calendar.DAY_OF_WEEK)==3) fecha.add(Calendar.DATE, 3); //3=sabado,4=dom.
            else fecha.add(Calendar.DATE, 1);
        }
        String dia = String.valueOf(fecha.get(Calendar.DAY_OF_MONTH));
        String anio = String.valueOf(fecha.get(Calendar.YEAR));
        int mes = fecha.get(Calendar.MONTH) + 1;
        String mes2 = String.valueOf(mes);
        if (dia.length()==1) dia="0"+dia;
        if (mes2.length()==1) mes2="0"+mes2;
        dia5=dia+"/"+mes2+"/"+anio;
        return dia5;
    }

    
    public static String getMes2(int numeroMes){
        String resultado=null;
        switch(numeroMes){
            case 1:resultado="Enero"; break;
            case 2:resultado="Febrero"; break;
            case 3:resultado="Marzo"; break;
            case 4:resultado="Abril"; break;
            case 5:resultado="Mayo"; break;
            case 6:resultado="Junio"; break;
            case 7:resultado="Julio"; break;
            case 8:resultado="Agosto"; break;
            case 9:resultado="Septiembre"; break;
            case 10:resultado="Octubre"; break;
            case 11:resultado="Noviembre"; break;
            case 12:resultado="Diciembre"; break;
        }
        return resultado;
    }
    public static String unaSemana(int year, int month, int day){
        Calendar fecha = new GregorianCalendar(year, month, day);
        fecha.add(Calendar.DATE, -3);
        String fecha1=dosDigitos(fecha.get(Calendar.DAY_OF_MONTH))+"/"+
                dosDigitos(fecha.get(Calendar.MONTH)+1)+"/"+
                fecha.get(Calendar.YEAR);
        fecha.add(Calendar.DATE, -4);
        String fecha2=dosDigitos(fecha.get(Calendar.DAY_OF_MONTH))+"/"+
                dosDigitos(fecha.get(Calendar.MONTH)+1)+"/"+
                fecha.get(Calendar.YEAR);
        return fecha2+"-"+fecha1;
    }
    public static String unMes(int year, int month, int day){
        int cuantos = cuantosDias(month);
        // determinar a�o bisiesto
        if ((year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0))) {
            cuantos = 29;
        }
        String fecha1="01/"+dosDigitos(month)+"/"+year;
        String fecha2=cuantos+"/"+dosDigitos(month)+"/"+year;
        return fecha1+"-"+fecha2;
    }
    public static int cuantosDias(int month){
        int cuantos=0;
        switch(month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12: cuantos=31; break;
            case 2: cuantos=28; break;
            case 4: case 6: case 9: case 11: cuantos=30; break;
//            case 0: case 2: case 4: case 6: case 7: case 9: case 11: cuantos=31; break;
//            case 1: cuantos=28; break;
//            case 3: case 5: case 8: case 10: cuantos=30; break;
        }
        return cuantos;
    }
    
    public static String[] permiteCapturaAvance(int anio0, int mes0, int dia0){
        //Calendar fechaHoy = new GregorianCalendar();
        Calendar fechaHoy = new GregorianCalendar(anio0,mes0,dia0);
        int dia = fechaHoy.get(Calendar.DAY_OF_MONTH);
        int mes = fechaHoy.get(Calendar.MONTH);
        int anio = fechaHoy.get(Calendar.YEAR);
        int penultimo=0, ultimo=0, mesnext1=0,mesnext2=0;
        boolean permite = false;
        if(dia >= 1 && dia <= 5) { // primeros 5 dias de cada mes
            penultimo = mes - 2;
            ultimo = mes - 1;
            mesnext1 = penultimo + 1;
            mesnext2 = ultimo + 1;
        }
        if(dia >= 1 && dia <= 5 && mes == 0) {
            penultimo = 10;
            ultimo = 11;
            mesnext1 = penultimo + 1;
            mesnext2 = 1;
        }
        if(dia >= 1 && dia <= 5 && mes == 1) {
            penultimo = 11;
            ultimo = 0;
            mesnext1 = 0;
            mesnext2 = 2;
        }
        if(dia >= 6 && dia <= 23 && mes == 11) {
            penultimo = mes -2;
            ultimo = mes -1;
            mesnext1 = 10;
            mesnext2 = 1;
        }
        if(dia >= 6 && dia <= 23 && mes !=11){ // este rango de dias es igual p todos los meses
            if(mes == 0) {
                penultimo = 10;
                ultimo = 11;
                mesnext1 = 11;
                mesnext2 = 1;
            } else if(mes == 1) {
                penultimo = 11;
                ultimo = 0;
                mesnext1 = 0;
                mesnext2 = 2;
            } else {
                penultimo = mes - 2;
                ultimo = mes - 1;
                mesnext1 = penultimo + 1;
                mesnext2 = ultimo + 2;
            }
        }
        if(mes == 0 || mes == 2 || mes == 4 || mes == 6 || mes == 7 || mes == 9 || mes == 11) {
            if((dia >= 1 && dia <= 5) ||  (dia >= 27 && dia <= 31) ) {
                permite = true;
            }
            if(dia >= 24 && dia <= 26){ // rango de dias en particular de estos meses
                penultimo = mes - 2;
                ultimo = mes - 1;
                mesnext1 = penultimo + 1;
                mesnext2 = ultimo + 2;
            }
            if(dia >= 24 && dia <= 26 && mes == 0){
                penultimo = 10;
                ultimo = 11;
                mesnext1 = 11;
                mesnext2 = 1;
            }
            if(dia >= 27 && dia <= 31){ // ultimos 5 dias del mes
                if(mes == 11) {
                    penultimo = mes -1;
                    ultimo = mes;
                    mesnext1 = 11;
                    mesnext2 = 1;
                }
                else if(mes == 0){
                    penultimo = 11;
                    ultimo = 0;
                    mesnext1 = 0;
                    mesnext2 = 2;
                }
                else {
                    penultimo = mes -1;
                    ultimo = mes;
                    mesnext1 = penultimo + 1;
                    mesnext2 = ultimo + 2;
                }
            }
        }
        if(mes == 3 || mes == 5 || mes == 8 || mes == 10 ) {
            if((dia >= 1 && dia <= 5) ||  (dia >= 26 && dia <= 30) ) {
                permite = true;
            }
            if(dia >= 24 && dia <= 25){
                penultimo = mes - 2;
                ultimo = mes - 1;
            }
            if(dia >= 26 && dia <= 30){ 
                penultimo = mes - 1;
                ultimo = mes;
            }
            if(mes == 10){
                mesnext1 = 11;
                mesnext2 = 0;
            } else {
//                mesnext1 = penultimo + 1;
//                mesnext2 = ultimo + 2;
            }
        }
        if(mes == 1){
            if(dia == 24) {
                penultimo = 11;
                ultimo = 0;
            }
            if((anio % 4 == 0) && ((anio % 100 != 0) || (anio % 400 == 0))) {
                if((dia >= 1 && dia <= 5) ||  (dia >= 25 && dia <= 29) ) { //a�o bisiesto
                    permite = true;
                    if(dia >= 25 && dia <= 29){ 
                        penultimo = 0;
                        ultimo = 1;
                    }
                }
            }
            else {
                if((dia >= 1 && dia <= 5) ||  (dia >= 24 && dia <= 28) ) {
                    permite = true;
                    if(dia >= 24 && dia <= 28){ 
                        penultimo = mes;
                        ultimo = mes + 1;
                    }
                }
            }
            mesnext1 = penultimo + 1;
            mesnext2 = ultimo + 2;
        }
        String periodoanterior1 = String.valueOf(anio)+dosDigitos(penultimo+1)+"25";
        String periodoanterior2 = String.valueOf(anio)+dosDigitos(mesnext1+1)+"06";
        String periodoultimo1 = String.valueOf(anio)+dosDigitos(ultimo+1)+"25";
        String periodoultimo2 = String.valueOf(anio)+dosDigitos(mesnext2+1)+"10";
        
        String[] info = {String.valueOf(permite),String.valueOf(penultimo), String.valueOf(ultimo), periodoanterior1, periodoanterior2, periodoultimo1, periodoultimo2};
        return info;
    }
    
    public static String[] permiteCapturaAvanceMes1(int anio0, int mes0, int dia0){
        //Calendar fechaHoy = new GregorianCalendar();
        Calendar fechaHoy = new GregorianCalendar(anio0,mes0,dia0);
        int dia = fechaHoy.get(Calendar.DAY_OF_MONTH);
        int mes = fechaHoy.get(Calendar.MONTH);
        int anio = fechaHoy.get(Calendar.YEAR);
        int penultimo=0, mesnext1=0;
        boolean permite = false;
        if(dia >= 1 && dia <= 5) { // primeros 5 dias de cada mes
            penultimo = mes - 2;
            mesnext1 = penultimo + 1;
        }
        if(dia >= 1 && dia <= 5 && mes == 0) {
            penultimo = 10;
            mesnext1 = penultimo + 1;
        }
        if(dia >= 1 && dia <= 5 && mes == 1) {
            penultimo = 11;
            mesnext1 = 0;
        }
        if(dia >= 6 && dia <= 23 && mes == 11) {
            penultimo = mes -2;
            mesnext1 = 10;
        }
        if(dia >= 6 && dia <= 23 && mes !=11){ // este rango de dias es igual p todos los meses
            if(mes == 0) {
                penultimo = 10;
                mesnext1 = 11;
            } else if(mes == 1) {
                penultimo = 11;
                mesnext1 = 0;
            } else {
                penultimo = mes - 2;
                mesnext1 = penultimo + 1;
            }
        }
        if(mes == 0 || mes == 2 || mes == 4 || mes == 6 || mes == 7 || mes == 9 || mes == 11) {
            if((dia >= 1 && dia <= 5) ||  (dia >= 27 && dia <= 31) ) {
                permite = true;
            }
            if(dia >= 24 && dia <= 26){ // rango de dias en particular de estos meses
                penultimo = mes - 2;
                mesnext1 = penultimo + 1;
            }
            if(dia >= 24 && dia <= 26 && mes == 0){
                penultimo = 10;
                mesnext1 = 11;
            }
            if(dia >= 27 && dia <= 31){ // ultimos 5 dias del mes
                if(mes == 11) {
                    penultimo = mes -1;
                    mesnext1 = 11;
                }
                else if(mes == 0){
                    penultimo = 11;
                    mesnext1 = 0;
                }
                else {
                    penultimo = mes -1;
                    mesnext1 = penultimo + 1;
                }
            }
        }
        if(mes == 3 || mes == 5 || mes == 8 || mes == 10 ) {
            if((dia >= 1 && dia <= 5) ||  (dia >= 26 && dia <= 30) ) {
                permite = true;
            }
            if(dia >= 24 && dia <= 25){
                penultimo = mes - 2;
            }
            if(dia >= 26 && dia <= 30){ 
                penultimo = mes - 1;
            }
            if(mes == 10){
                mesnext1 = 11;
            } else {
//                mesnext1 = penultimo + 1;
//                mesnext2 = ultimo + 2;
            }
        }
        if(mes == 1){
            if(dia == 24) {
                penultimo = 11;
            }
            if((anio % 4 == 0) && ((anio % 100 != 0) || (anio % 400 == 0))) {
                if((dia >= 1 && dia <= 5) ||  (dia >= 25 && dia <= 29) ) { //a�o bisiesto
                    permite = true;
                    if(dia >= 25 && dia <= 29){ 
                        penultimo = 0;
                    }
                }
            }
            else {
                if((dia >= 1 && dia <= 5) ||  (dia >= 24 && dia <= 28) ) {
                    permite = true;
                    if(dia >= 24 && dia <= 28){ 
                        penultimo = mes;
                    }
                }
            }
            mesnext1 = penultimo + 1;
        }
        String periodoanterior1 = String.valueOf(anio)+dosDigitos(penultimo+1)+"25";
        String periodoanterior2 = String.valueOf(anio)+dosDigitos(mesnext1+1)+"06";
        
        String[] info = {String.valueOf(permite), regresaMes(penultimo+1), periodoanterior1, periodoanterior2};
        return info;
    }
    
    public static String[] permiteCapturaAvanceMes2(int anio0, int mes0, int dia0){
        Calendar fechaHoy = new GregorianCalendar(anio0,mes0-1,dia0);
        int dia = fechaHoy.get(Calendar.DAY_OF_MONTH);
        int mes = fechaHoy.get(Calendar.MONTH);
        int anio = fechaHoy.get(Calendar.YEAR);
        int ultimo=0, mesnext2=0;
        boolean permite = false;
        boolean dia1 = false;
        if(dia >= 1 && dia <= 5) { // primeros 5 dias de cada mes
            ultimo = mes - 1;
            mesnext2 = ultimo + 1;
        }
        if(dia >= 1 && dia <= 5 && mes == 0) {
            ultimo = 11;
            mesnext2 = 1;
        }
        if(dia >= 1 && dia <= 5 && mes == 1) {
            ultimo = 0;
            mesnext2 = 2;
        }
        if(dia >= 6 && dia <= 23 && mes == 11) {
            ultimo = mes -1;
            mesnext2 = 1;
        }
        if(dia >= 6 && dia <= 23 && mes !=11){ // este rango de dias es igual p todos los meses
            if(mes == 0) {
                ultimo = 11;
                mesnext2 = 1;
            } else if(mes == 1) {
                ultimo = 0;
                mesnext2 = 2;
            } else {
                ultimo = mes - 1;
                mesnext2 = ultimo + 2;
            }
        }
        if(mes == 0 || mes == 2 || mes == 4 || mes == 6 || mes == 7 || mes == 9 || mes == 11) {
            if((dia >= 1 && dia <= 5) ||  (dia >= 27 && dia <= 31) ) {
                permite = true;
                if(dia == 27) dia1 = true;
            }
            if(dia >= 24 && dia <= 26){ // rango de dias en particular de estos meses
                ultimo = mes - 1;
                mesnext2 = ultimo + 2;
            }
            if(dia >= 24 && dia <= 26 && mes == 0){
                ultimo = 11;
                mesnext2 = 1;
            }
            if(dia >= 27 && dia <= 31){ // ultimos 5 dias del mes
                if(mes == 11) {
                    ultimo = mes;
                    mesnext2 = 1;
                }
                else if(mes == 0){
                    ultimo = 0;
                    mesnext2 = 2;
                }
                else {
                    ultimo = mes;
                    mesnext2 = ultimo + 2;
                }
            }
        }
        if(mes == 3 || mes == 5 || mes == 8 || mes == 10 ) {
            if((dia >= 1 && dia <= 5) ||  (dia >= 26 && dia <= 30) ) {
                permite = true;
                if(dia == 26) dia1 = true;
            }
            if(dia >= 24 && dia <= 25){
                ultimo = mes - 1;
            }
            if(dia >= 26 && dia <= 30){ 
                ultimo = mes;
            }
            if(mes == 10){
                mesnext2 = 0;
            } else {
//                mesnext1 = penultimo + 1;
//                mesnext2 = ultimo + 2;
            }
        }
        if(mes == 1){
            if(dia == 24) {
                ultimo = 0;
            }
            if((anio % 4 == 0) && ((anio % 100 != 0) || (anio % 400 == 0))) {
                if((dia >= 1 && dia <= 5) ||  (dia >= 25 && dia <= 29) ) { //a�o bisiesto
                    permite = true;
                    if(dia >= 25 && dia <= 29){ 
                        ultimo = 1;
                    }
                }
                if(dia == 25) dia1 = true;
            }
            else {
                if((dia >= 1 && dia <= 5) ||  (dia >= 24 && dia <= 28) ) {
                    permite = true;
                    if(dia >= 24 && dia <= 28){ 
                        ultimo = mes + 1;
                    }
                }
                if(dia == 24) dia1 = true;
            }
            mesnext2 = ultimo + 2;
        }
        String periodoultimo1 = String.valueOf(anio)+dosDigitos(ultimo+1)+"25";
        String periodoultimo2 = String.valueOf(anio)+dosDigitos(mesnext2+1)+"06";
        
        String[] info = {String.valueOf(permite), regresaMes(ultimo), periodoultimo1, periodoultimo2, String.valueOf(dia1)};
        return info;
    }    
    
    public static String regresaMes(int numeroMes){
        String resultado=null;
        switch(numeroMes){
            case 0:resultado="Enero"; break;
            case 1:resultado="Febrero"; break;
            case 2:resultado="Marzo"; break;
            case 3:resultado="Abril"; break;
            case 4:resultado="Mayo"; break;
            case 5:resultado="Junio"; break;
            case 6:resultado="Julio"; break;
            case 7:resultado="Agosto"; break;
            case 8:resultado="Septiembre"; break;
            case 9:resultado="Octubre"; break;
            case 10:resultado="Noviembre"; break;
            case 11:resultado="Diciembre"; break;
        }
        return resultado;
    }    
    public static String unaSemanaCompleta(int year, int month, int day){
        Calendar fecha = new GregorianCalendar(year, month - 1, day);
        String fecha1=fecha.get(Calendar.YEAR)+
                dosDigitos(fecha.get(Calendar.MONTH)+1)+
                dosDigitos(fecha.get(Calendar.DAY_OF_MONTH));
        fecha.add(Calendar.DATE, -7);
        String fecha2=fecha.get(Calendar.YEAR)+
                dosDigitos(fecha.get(Calendar.MONTH)+1)+
                dosDigitos(fecha.get(Calendar.DAY_OF_MONTH));
        return fecha2+"-"+fecha1;
    }
    public static String unMesCompleto(int year, int month, int day){
        int cuantos = cuantosDias(month);
        // determinar a�o bisiesto
        if ((year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0))) {
            cuantos = 29;
        }
        Calendar fecha = new GregorianCalendar(year, month - 1, day);
        String fecha1=fecha.get(Calendar.YEAR)+
                dosDigitos(fecha.get(Calendar.MONTH)+1)+
                dosDigitos(fecha.get(Calendar.DAY_OF_MONTH));
        fecha.add(Calendar.DATE, -cuantos);
        String fecha2=fecha.get(Calendar.YEAR)+
                dosDigitos(fecha.get(Calendar.MONTH)+1)+
                dosDigitos(fecha.get(Calendar.DAY_OF_MONTH));
        return fecha2+"-"+fecha1;
    }
}
