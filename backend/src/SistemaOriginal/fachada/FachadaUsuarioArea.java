/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.inegi.dggma.sistemas.asuntos.fachada;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.AreaDAO;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.PermisoDAO;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.ResponsableDAO;
import mx.org.inegi.dggma.sistemas.asuntos.baseDatos.UsuarioDAO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.AreaDTO;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.AsuntoBean;
import mx.org.inegi.dggma.sistemas.asuntos.dto.PermisoDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.ResponsableDTO;
import mx.org.inegi.dggma.sistemas.asuntos.dto.UsuarioDTO;


import mx.org.inegi.dggma.sistemas.asuntos.modelo.AreaBean;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.DatosLogin;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.PermisoBean;
import mx.org.inegi.dggma.sistemas.asuntos.modelo.UsuarioBean;

/**
 *
 * @author joseluis
 */
public class FachadaUsuarioArea {

    public UsuarioBean buscaUsuario(DatosLogin datosLogin) throws Exception{
         UsuarioDAO udao = new UsuarioDAO();
         UsuarioBean ub = null;
         UsuarioDTO usuario = udao.buscaUsuario(datosLogin);
         if (usuario != null) {
           ub = new UsuarioBean();
           ub.setDatos(usuario);
         }
         
         return ub;
    }

    public UsuarioBean buscaUsuario(Integer idusuario) throws Exception {
         UsuarioDAO udao = new UsuarioDAO();
         UsuarioBean u = new UsuarioBean();
         u.setDatos(udao.buscaUsuario(idusuario));
         return u;

    }
    
    public UsuarioBean buscaUsuarioVigenteoNo(Integer idusuario) throws Exception {
         UsuarioDAO udao = new UsuarioDAO();
         UsuarioBean u = new UsuarioBean();
         u.setDatos(udao.buscaUsuarioVigenteoNo(idusuario));
         return u;

    }
    

    public void cargaPermisosUsuarios(UsuarioBean usuarioBean) throws Exception {
        PermisoDAO pd = new PermisoDAO();

        List<PermisoDTO> permisos = pd.permisosUsuario(usuarioBean.getDatos().getIdusuario());
        List<PermisoBean> listaPermisos = new ArrayList<PermisoBean>();

        for (PermisoDTO permiso:permisos) {
            PermisoBean pb = new PermisoBean();
            pb.setDatos(permiso);
            if (permiso.getIdarea() > 0) {
                AreaBean ab = buscaArea(permiso.getIdarea());
                pb.setAreaBean(ab);
            }
            listaPermisos.add(pb);
            usuarioBean.setPermisoActual(pb);
        }
        usuarioBean.setPermisos(listaPermisos);
    }


    public List<UsuarioBean> usuariosResponsablesxAsunto(int idArea) throws Exception{
        PermisoDAO pd = new PermisoDAO();
        List<PermisoDTO> permisos = pd.permisosArea(idArea);
        
        List<UsuarioBean> usuarios = new ArrayList<UsuarioBean>();
        for(PermisoDTO permiso:permisos) {
            UsuarioBean usuario = buscaUsuario(permiso.getIdusuario());
            if ((usuario != null) && usuario.getDatos().getActivoestatus().equals("S")){
               usuarios.add(usuario);
            }
        }

      return usuarios;
    }
    
    public List<UsuarioBean> usuariosCPermisosxArea(int idArea) throws Exception{
        PermisoDAO pd = new PermisoDAO();
        List<PermisoDTO> permisos = pd.permisosArea(idArea);
        
        List<UsuarioBean> usuarios = new ArrayList<UsuarioBean>();
        for(PermisoDTO permiso:permisos) {
            UsuarioBean usuario = buscaUsuario(permiso.getIdusuario());
            if ((usuario != null) && usuario.getDatos().getActivoestatus().equals("S")){
               usuarios.add(usuario);
            }
        }

      return usuarios;
    }
    
    public List<UsuarioDTO> usuariosDTOCPermisosxArea(int idArea) throws Exception{
        PermisoDAO pd = new PermisoDAO();
        List<PermisoDTO> permisos = pd.permisosArea(idArea);
        
        List<UsuarioDTO> usuarios = new ArrayList<UsuarioDTO>();
        for(PermisoDTO permiso:permisos) {
            UsuarioBean usuario = buscaUsuario(permiso.getIdusuario());
            
            if ((usuario != null) && (usuario.getDatos() != null) && usuario.getDatos().getActivoestatus().equals("S")){
               usuarios.add(usuario.getDatos());
            } else {
              System.out.println("No se encontró el usuario "+permiso.getIdusuario()+" para envio de correo recursivo");
            }
        }

      return usuarios;
    }

    private List<AreaBean> getAreasRecursivo(int dependeD, List<AreaBean> lAreas) throws  Exception {
        AreaDAO adao = new AreaDAO(); 
        List<AreaDTO> areas = adao.areasDependientes(dependeD,"nivel");
        
        if (areas.isEmpty()) {
            return lAreas;
        }

        for (AreaDTO area:areas) {
            AreaBean ab = new AreaBean();
            ab.setDatos(area);
            UsuarioBean ub = buscaPersonaResponsable(area.getIdresponsable());
            ab.setResponsable(ub);
            lAreas.add(ab);
            getAreasRecursivo(area.getIdarea(), lAreas);
        }
        return lAreas;
    }

    public List<AreaBean> obtenTodasAreas() throws Exception {

        AreaDAO ad = new AreaDAO(); 

        List<AreaBean> listaAreas = new ArrayList<AreaBean>();

        int nivel = 0;
        List<AreaDTO> areas = null;
        while (areas == null || areas.isEmpty()) {
            nivel++;
            areas = ad.areasxNivel(nivel);
        }


        AreaBean ab = new AreaBean();
        ab.setDatos(areas.get(0));
        UsuarioBean ub = buscaPersonaResponsable(areas.get(0).getIdresponsable());
        ab.setResponsable(ub);
        listaAreas.add(ab);

        getAreasRecursivo(nivel, listaAreas);

        return listaAreas;
    }

   
    
    
   public List<AreaBean> listaAreasResponsablesmas1Nivel(UsuarioBean usuario) throws Exception {
       AreaDAO ad = new AreaDAO();
       int dependede = 1;
       
       AreaBean areaActual = usuario.getPermisoActual().getAreaBean();
       List<AreaDTO> areas = new ArrayList<AreaDTO>();

       if (areaActual != null){
          dependede = areaActual.getDatos().getIdarea();
          areas.add(areaActual.getDatos());
       }   
      
       List<AreaBean> listaAreas = new ArrayList<AreaBean>();
       areas.addAll(ad.areasSubNivel(dependede));
      
       
       for(AreaDTO areadto:areas){
          AreaBean ab = new AreaBean();
          ab.setDatos(areadto);
          UsuarioBean ub = buscaPersonaResponsable(areadto.getIdresponsable());
          ab.setResponsable(ub);
          listaAreas.add(ab);
       }
       
       return listaAreas;      
   }
   
   
  public List<AreaDTO> listaAreasResponsablesmas1NivelSencilla(UsuarioBean usuario) throws Exception {
       AreaDAO ad = new AreaDAO();
       int dependede = 1;
       
       AreaBean areaActual = usuario.getPermisoActual().getAreaBean();
       List<AreaDTO> areas = new ArrayList<AreaDTO>();

       if (areaActual != null){
          dependede = areaActual.getDatos().getIdarea();
          //areas.add(areaActual.getDatos());
       }   
      
       areas.addAll(ad.areasSubNivel(dependede));
            
       return areas;      
   } 
  public List<AreaDTO> listaAreasResponsablesmas1NivelSencillaCaptura(UsuarioBean usuario) throws Exception {
       AreaDAO ad = new AreaDAO();
       int dependede = 1;
       AreaBean areaActual = usuario.getPermisoActual().getAreaBean();
       List<AreaDTO> areas = new ArrayList<AreaDTO>();
       if (areaActual != null){
          dependede = areaActual.getDatos().getIdarea();
       }   
       areas.addAll(ad.areasSubNivelCaptura(dependede));
       return areas;      
   }    
  
   public List<AreaDTO> listaAreasResponsablesmas1NivelSencillaCaptura(int idarea) throws Exception {
       AreaDAO ad = new AreaDAO();
       int dependede = 1;
       AreaDTO areaActual = ad.getArea(idarea);
       List<AreaDTO> areas = new ArrayList<AreaDTO>();
       if (areaActual != null){
          dependede = areaActual.getIdarea();
       }   
       areas.addAll(ad.areasSubNivelCaptura(dependede));
       return areas;      
   }    
  
  
  public List<AreaBean> listaSubAreasResponsables1Nivel(int idarea) throws Exception {
       AreaDAO ad = new AreaDAO();
       List<AreaDTO> areas = new ArrayList<AreaDTO>();
       int dependede = idarea;         
       List<AreaBean> listaAreas = new ArrayList<AreaBean>();
       areas.addAll(ad.areasSubNivel(dependede));
      
       for(AreaDTO areadto:areas){
          AreaBean ab = new AreaBean();
          ab.setDatos(areadto);
          UsuarioBean ub = buscaPersonaResponsable(areadto.getIdresponsable());
          ab.setResponsable(ub);
          listaAreas.add(ab);
       }
       
       return listaAreas;      
   }
  
  public List<AreaDTO> listaSubAreasResponsables1NivelDTO(int idarea) throws Exception {
       AreaDAO ad = new AreaDAO();
       List<AreaDTO> areas = new ArrayList<AreaDTO>();
       int dependede = idarea;         
       areas.addAll(ad.areasSubNivel(dependede));
      
       return areas;
   }
  
  
   public List<AreaDTO> listaDTOSubAreasResponsables1Nivel(int idarea) throws Exception {
       AreaDAO ad = new AreaDAO();
       int dependede = idarea;         
       return ad.areasSubNivel(dependede);      
   }
  
      
   public List<AreaBean> listaSubAreasResponsables1Nivel(UsuarioBean usuario) throws Exception {
       AreaDAO ad = new AreaDAO();
       int dependede = 1;
       
       AreaBean areaActual = usuario.getPermisoActual().getAreaBean();
       List<AreaDTO> areas = new ArrayList<AreaDTO>();

       if (areaActual != null){
          dependede = areaActual.getDatos().getIdarea();
          //areas.add(areaActual.getDatos()); //no incluye a la base
       }   
      
       List<AreaBean> listaAreas = new ArrayList<AreaBean>();
       areas.addAll(ad.areasSubNivel(dependede));
      
       
       for(AreaDTO areadto:areas){
          AreaBean ab = new AreaBean();
          ab.setDatos(areadto);
          UsuarioBean ub = buscaPersonaResponsable(areadto.getIdresponsable());
          ab.setResponsable(ub);
          listaAreas.add(ab);
       }
       
       return listaAreas;      
   } 
     
    public List<AreaBean> listaAreasResponsablesxNivel(int nivel) throws Exception {
       AreaDAO ad = new AreaDAO();
       
       List<AreaBean> listaAreas = new ArrayList<AreaBean>();
       List<AreaDTO> areas = ad.areasxNivel(nivel);
       
       for(AreaDTO areadto:areas){
          AreaBean ab = new AreaBean();
          ab.setDatos(areadto);
          UsuarioBean ub = buscaPersonaResponsable(areadto.getIdresponsable());
          ab.setResponsable(ub);
          listaAreas.add(ab);
       }
       
       return listaAreas; 
        
   }    
    
     public List<AreaDTO> listaAreasResponsablesxNivelS(int nivel) throws Exception {
       AreaDAO ad = new AreaDAO();
       List<AreaDTO> areas = ad.areasxNivel(nivel);
       return areas; 
        
   }    
    
    public List<AreaBean> listaAreasResponsablesxNivelxAreaSuper(int nivel, int idRSuperior) throws Exception, SQLException, SQLException {
       AreaDAO ad = new AreaDAO();
       
       List<AreaBean> listaAreas = new ArrayList<AreaBean>();
       List<AreaDTO> areas = ad.areasxNivelxAreaSuper(nivel,idRSuperior);
       
       for(AreaDTO areadto:areas){
          AreaBean ab = new AreaBean();
          ab.setDatos(areadto);
          UsuarioBean ub = buscaPersonaResponsable(areadto.getIdresponsable());
          ab.setResponsable(ub);
          listaAreas.add(ab);
       }
       
       return listaAreas; 
   }     
    
   
    public List<AreaDTO> listaAreasResponsablesxNivelxAreaSuperS(int nivel, int idRSuperior) throws SQLException, NamingException  {
       AreaDAO ad = new AreaDAO();
       List<AreaDTO> areas = ad.areasxNivelxAreaSuper(nivel,idRSuperior);
       
       return areas; 
   }     
    
   public List<AreaBean> listaAreasResponsables(UsuarioBean usuario) throws Exception {
        AreaDAO ad = new AreaDAO();

        String rol = usuario.getPermisoActual().getDatos().getRol();
      

        List<AreaBean> listaAreas = new ArrayList<AreaBean>();

        boolean consultaArea = usuario.getPermisoActual().getAreaBean() != null;

        // Lo comente pq no importa el usuario siempre se va a generar todas las areas
        if (PermisoBean.getADMINISTRADOR().equals(rol) || (PermisoBean.getCONSULTA().equals(rol) && !consultaArea) || (PermisoBean.getEJECUTIVO().equals(rol) && !consultaArea )) {
            
            //Para agregar a la direcci�n general
            List<AreaDTO> areas = ad.areasxNivel(1);
            
            AreaBean ab = new AreaBean();
            ab.setDatos(areas.get(0));
            UsuarioBean ub = buscaPersonaResponsable(areas.get(0).getIdresponsable());
            ab.setResponsable(ub);
            listaAreas.add(ab);
            
            getAreasRecursivo(1, listaAreas);
            

        } else if (PermisoBean.getRESPONSABLE().equals(rol) || PermisoBean.getRESPONSABLE_ADMINISTRADOR().equals(rol) || (PermisoBean.getCONSULTA().equals(rol) && consultaArea)
                   || (PermisoBean.getEJECUTIVO().equals(rol) && consultaArea)) {
            int idArea = usuario.getPermisoActual().getAreaBean().getDatos().getIdarea();
            //sql = "idarea=? order by nombre"; //Jacky lo coment�

            List<AreaDTO> areas = ad.areasxNivel(idArea);
            for (AreaDTO area:areas) {
                AreaBean ab = new AreaBean();
                ab.setDatos(area);
                UsuarioBean ub = buscaPersonaResponsable(area.getIdresponsable());
                ab.setResponsable(ub);
                listaAreas.add(ab);
                getAreasRecursivo(idArea, listaAreas);
            }


        } 

        return listaAreas;
    }



    public List<AreaBean> listaAreasRecursivo(AreaBean areaPpal, List<AreaBean> listaAreas, boolean base) throws  Exception {
        AreaDAO ad = new AreaDAO();
        List<AreaDTO> areas = null;
        if (base) {
            areas = ad.getAreasxId(areaPpal.getDatos().getIdarea());
        } else {
            areas = ad.areasDependientes(areaPpal.getDatos().getIdarea(), "idarea");
        }

        for (AreaDTO area:areas) {
            AreaBean ab = new AreaBean();
            ab.setDatos(area);
            UsuarioBean ub = buscaPersonaResponsable(area.getIdresponsable());
            ab.setResponsable(ub);
            listaAreas.add(ab);
            listaAreasRecursivo(ab, listaAreas,false);
        }
        return listaAreas;
    }

    
    
    public List<AreaBean> listaAreasCorresponsables() throws Exception {
        AreaDAO adao = new AreaDAO();
        List<AreaBean> listaAreas = new ArrayList<AreaBean>();

        List<AreaDTO> areas = adao.areasCorresponsablesxNivelRango(2,4);

        for (AreaDTO area:areas) {
            AreaBean ab = new AreaBean();
            ab.setDatos(area);
            UsuarioBean ub = buscaPersonaResponsable(area.getIdresponsable());
            ab.setResponsable(ub);
            listaAreas.add(ab);
        }

        return listaAreas;
    }


    public List<AreaBean> buscarAreasCorresponsablesPorAsunto(AsuntoBean asunto) throws Exception {
        AreaDAO adao = new AreaDAO();
        
        List<AreaDTO> areas = adao.areasCorresponsablesxAsunto(asunto);
        
        List<AreaBean> listaAreas = new ArrayList<AreaBean>();
        for (AreaDTO area:areas) {
            AreaBean ab = new AreaBean();
            ab.setDatos(area);
            UsuarioBean ub = buscaPersonaResponsable(area.getIdresponsable());
            ab.setResponsable(ub);
            listaAreas.add(ab);
        }
        return listaAreas;

    }
    
    public List<AreaBean> buscarAreasResponsablesPorAsunto(AsuntoBean asunto) throws Exception {
        AreaDAO adao = new AreaDAO();
        
        List<AreaDTO> areas = adao.areasResponsablesxAsunto(asunto);
        List<AreaBean> listaAreas = new ArrayList<AreaBean>();
        for (AreaDTO area:areas) {
            AreaBean ab = new AreaBean();
            ab.setDatos(area);
            UsuarioBean ub = buscaPersonaResponsable(area.getIdresponsable());
            ab.setResponsable(ub);
            listaAreas.add(ab);
        }
        return listaAreas;
    }
    
    public List<AreaBean> buscarAreasResponsablesPorAsunto(int idasunto) throws Exception {
        AreaDAO adao = new AreaDAO();
        
        List<AreaDTO> areas = adao.areasResponsablesxAsunto(idasunto);
        List<AreaBean> listaAreas = new ArrayList<AreaBean>();
        for (AreaDTO area:areas) {
            AreaBean ab = new AreaBean();
            ab.setDatos(area);
            UsuarioBean ub = buscaPersonaResponsable(area.getIdresponsable());
            ab.setResponsable(ub);
            listaAreas.add(ab);
        }
        return listaAreas;
    }
    

    public AreaBean buscaArea(Integer idArea) throws Exception {
        AreaDAO adao = new AreaDAO();
        AreaDTO area = adao.getArea(idArea);
        
        AreaBean ab = null;
        if (area != null) {
           ab = new AreaBean();
           ab.setDatos(area);
           UsuarioBean ub = buscaPersonaResponsable(area.getIdresponsable());
           ab.setResponsable(ub);
        }
        return ab;
    }

   /* public AreaBean buscaAreadeResponsable(int idResponsable){
        AreaDAO adao = new AreaDAO();
        AreaDTO area = adao.getArea(idArea);
        
        AreaBean ab = null;
        if (area != null) {
           ab = new AreaBean();
           ab.setDatos(area);
           UsuarioBean ub = buscaPersonaResponsable(area.getIdresponsable());
           ab.setResponsable(ub);
        }
        return ab;
    }*/
    

    public AreaBean buscaAreaSuperior(AreaBean areabase) throws Exception{
        AreaDAO adao = new AreaDAO();
        
        AreaDTO area = adao.getArea(areabase.getDatos().getDependede());

        if (area == null) return null;

        AreaBean ab = new AreaBean();
        ab.setDatos(area);
        UsuarioBean ub = buscaPersonaResponsable(area.getIdresponsable());
        ab.setResponsable(ub);
        
        return ab;
    }
    
    
       
    public AreaBean buscaAreaSuperior(int idareabase) throws SQLException, Exception{
        AreaBean areabase = buscaArea(idareabase);
        AreaBean areaSuperior = buscaAreaSuperior(areabase);

        return areaSuperior;
    }
    
     public List<AreaBean> buscaAreasSuperiores(AreaBean areabase) throws SQLException,  Exception {
        List<AreaBean> areasSuperiores = new ArrayList<AreaBean>();
        AreaBean areaSuperior = new AreaBean();
        areaSuperior = areabase;
        int idArea = areaSuperior.getDatos().getDependede();
        areasSuperiores.add(areaSuperior);
        while (idArea != 0){
            FachadaUsuarioArea fua = new FachadaUsuarioArea();
            areaSuperior = fua.buscaAreaSuperior(areaSuperior);
            areasSuperiores.add(areaSuperior);
            idArea = areaSuperior.getDatos().getDependede();
        }

        return areasSuperiores;
    }
    


    private UsuarioBean buscaPersonaResponsable(Integer idUsuario) throws Exception {
         UsuarioDAO udao = new UsuarioDAO();
         UsuarioBean u = new UsuarioBean();
         u.setDatos(udao.buscaUsuario(idUsuario));
         return u; 
    }
    

    public List<AreaBean> listaAreasDelegadasxAsunto(int idRSuperior,AsuntoBean asunto) throws SQLException, Exception{
        ResponsableDAO rdao = new ResponsableDAO(null); //No se requieres areas de Consulta
        AreaDAO adao = new AreaDAO();
        List<ResponsableDTO> areasR = rdao.obtenResponsablesxAsuntoxAreaAsignada(asunto.getIdasunto(), idRSuperior);
        
        List<AreaBean> listaAreas = new ArrayList<AreaBean>();
        for (ResponsableDTO responsable:areasR) {
            AreaBean ab = new AreaBean();
            AreaDTO area = adao.getArea(responsable.getIdarea());
            ab.setDatos(area);
            UsuarioBean ub = buscaPersonaResponsable(area.getIdresponsable());
            ab.setResponsable(ub);
            listaAreas.add(ab);
        }
        return listaAreas;
    }
    
    public List<AreaBean> listaAreasDelegadasxAsunto(int idRSuperior,int idasunto) throws SQLException, Exception{
        ResponsableDAO rdao = new ResponsableDAO(null);
        AreaDAO adao = new AreaDAO();
        List<ResponsableDTO> areasR = rdao.obtenResponsablesxAsuntoxAreaAsignada(idasunto, idRSuperior);
        
        List<AreaBean> listaAreas = new ArrayList<AreaBean>();
        for (ResponsableDTO responsable:areasR) {
            AreaBean ab = new AreaBean();
            AreaDTO area = adao.getArea(responsable.getIdarea());
            ab.setDatos(area);
            UsuarioBean ub = buscaPersonaResponsable(area.getIdresponsable());
            ab.setResponsable(ub);
            listaAreas.add(ab);
        }
        return listaAreas;
    }
    
    public List<AreaBean> listaAreasDelegadasxAsuntoSCancelados(int idRSuperior,int idasunto) throws SQLException, Exception{
        ResponsableDAO rdao = new ResponsableDAO(null);
        AreaDAO adao = new AreaDAO();
        List<ResponsableDTO> areasR = rdao.obtenResponsablesxAsuntoxAreaAsignadaSCancelados(idasunto, idRSuperior);
        
        List<AreaBean> listaAreas = new ArrayList<AreaBean>();
        for (ResponsableDTO responsable:areasR) {
            AreaBean ab = new AreaBean();
            AreaDTO area = adao.getArea(responsable.getIdarea());
            ab.setDatos(area);
            UsuarioBean ub = buscaPersonaResponsable(area.getIdresponsable());
            ab.setResponsable(ub);
            listaAreas.add(ab);
        }
        return listaAreas;
    }
    
      
    public List<AreaBean> listaAreasDelegadasxAcuerdo(int idRSuperior,int idacuerdo) throws SQLException, Exception{
        ResponsableDAO rdao = new ResponsableDAO(null);
        AreaDAO adao = new AreaDAO();
        List<ResponsableDTO> areasR = rdao.obtenResponsablesxAcuerdoxAreaAsignada(idacuerdo, idRSuperior);
        
        List<AreaBean> listaAreas = new ArrayList<AreaBean>();
        for (ResponsableDTO responsable:areasR) {
            AreaBean ab = new AreaBean();
            AreaDTO area = adao.getArea(responsable.getIdarea());
            ab.setDatos(area);
            UsuarioBean ub = buscaPersonaResponsable(area.getIdresponsable());
            ab.setResponsable(ub);
            listaAreas.add(ab);
        }
        return listaAreas;
    }
      
    public List<AreaBean> listaAreasDelegadasxAcuerdoSCancelados(int idRSuperior,int idacuerdo) throws SQLException, Exception{
        ResponsableDAO rdao = new ResponsableDAO(null);
        AreaDAO adao = new AreaDAO();
        List<ResponsableDTO> areasR = rdao.obtenResponsablesxAcuerdoxAreaAsignadaSCancelados(idacuerdo, idRSuperior);
        
        List<AreaBean> listaAreas = new ArrayList<AreaBean>();
        for (ResponsableDTO responsable:areasR) {
            AreaBean ab = new AreaBean();
            AreaDTO area = adao.getArea(responsable.getIdarea());
            ab.setDatos(area);
            UsuarioBean ub = buscaPersonaResponsable(area.getIdresponsable());
            ab.setResponsable(ub);
            listaAreas.add(ab);
        }
        return listaAreas;
    }  
      
     public List<UsuarioDTO> obtenUsuariosAdministradores() throws SQLException, NamingException{
        UsuarioDAO udao = new UsuarioDAO();
        return udao.obtenUsuariosAdministradores();
     }
     
     public List<AreaBean> remitentes() throws Exception{
       List<AreaBean> remite = new ArrayList();
       remite.addAll(this.listaAreasResponsablesxNivel(1));
       remite.add(this.buscaArea(164)); //Dirección Mejora de la gestión
       return remite;
       
     }
     
     public List<UsuarioDTO> remitentesUsuarios() throws Exception{
       List<UsuarioDTO> remite = new ArrayList();
       //Se modificó para que tomara a los responsables de las áreas 
       List<AreaBean> areasremitentes = this.remitentes();
       for (AreaBean area:areasremitentes){
        remite.add(area.getResponsable().getDatos());
       } 
       return remite;
     }

     public List<UsuarioDTO> remitentesUsuariosTodos() throws Exception{
       List<UsuarioDTO> remite = new ArrayList();
       remite.add(this.buscaUsuarioVigenteoNo(241).getDatos()); //241 Luis Gerardo 
       remite.add(this.buscaUsuarioVigenteoNo(401).getDatos()); //401 CARMEN REYES
       remite.add(this.buscaUsuarioVigenteoNo(405).getDatos()); //Cortes Briano
       //remite.add(this.buscaUsuarioVigenteoNo(254).getDatos()); //MARTINEZ TOPETE
       remite.add(this.buscaUsuarioVigenteoNo(226).getDatos()); //carlos.elemen
       remite.add(this.buscaUsuarioVigenteoNo(228).getDatos()); //javier.moreno
       return remite;
     }

     
     
     public List<AreaBean> listaAreasResponsablesNivel0y1() throws Exception{
       List<AreaBean> areas = this.listaAreasResponsablesxNivel(1);
       areas.addAll(this.listaAreasResponsablesxNivel(2));
       return areas;
     }
    
}
