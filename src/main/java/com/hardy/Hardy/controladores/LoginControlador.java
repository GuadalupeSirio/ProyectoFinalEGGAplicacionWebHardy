package com.hardy.Hardy.controladores;

import com.hardy.Hardy.servicios.RolServicio;
import com.hardy.Hardy.servicios.UsuarioServicio;
import java.security.Principal;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class LoginControlador {

    @Autowired
    private UsuarioServicio us;

    @Autowired
    private RolServicio rs;

    @GetMapping("/login")
    public ModelAndView login(HttpServletRequest request, @RequestParam(required = false) String error, @RequestParam(required = false) String logout, Principal principal) {

        ModelAndView mv = new ModelAndView("login");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mv.addObject("exito", flashMap.get("exito-name"));
            mv.addObject("error", flashMap.get("error-name"));
        }
        if (error != null) {
            mv.addObject("error", "Correo o contraseña incorrecta");
        }
        if (logout != null) {
            mv.addObject("logout", "Salió correctamente de la plataforma");
        }
        if (principal != null) {
            mv.setViewName("redirect:/");
        }

        return mv;
    }
    
    @PostMapping("/modificar-correo")
    public RedirectView modificarCorreo(@RequestParam Integer id, @RequestParam String correo, HttpSession session, RedirectAttributes a) {
        try {
            us.modificarCorreo((Integer)session.getAttribute("idUsuario"), correo);  
            a.addFlashAttribute("exito", "El usuario se modificó correctamente!");
        } catch (Exception e) {
            a.addFlashAttribute("error", e.getMessage());
        }
        return new RedirectView("/");
    }

    @PostMapping("/clave")
    public RedirectView modificarClave(@RequestParam String clave, @RequestParam String clave2, HttpSession session, RedirectAttributes a) {
        try {
            us.modificarClave((Integer)session.getAttribute("idUsuario"), clave, clave2);
            a.addFlashAttribute("exito", "La clave se modificó correctamente!");
        } catch (Exception e) {
            a.addFlashAttribute("error", e.getMessage());
        }
        return new RedirectView("/");
    }
}

//Me parece que no vamos a necesitar estos GET ya que estamos trabajando el front con modales, lo dejo por las dudas
//No se si esta bien como obtengo el ID para que lo haga conn el que esta loggeado



//    @GetMapping("/modificar-correo/{id}")
//    public ModelAndView editarUsuario(@PathVariable Integer id, HttpSession session) throws Exception {
//        if (!session.getAttribute("id").equals(id)) {
//            return new ModelAndView(new RedirectView("/"));
//        }
//        ModelAndView mav = new ModelAndView(""); //completar
//        mav.addObject("usuario", us.buscarPorId(id));
//        mav.addObject("title", "Modificar correo");
//        mav.addObject("roles", rs.buscarTodos());
//        mav.addObject("action", "modificar-correo");
//        return mav;
//    }

//    @GetMapping("/clave/{id}")
//    public ModelAndView modificarClave(@PathVariable Integer id, HttpSession session) throws Exception {
//        if (!session.getAttribute("id").equals(id)) { //Para que no pueda modificar la clave de nadie mas
//            return new ModelAndView(new RedirectView("/"));
//        }
//        ModelAndView mav = new ModelAndView("");  //completar
//        mav.addObject("usuario", us.buscarPorId(id));
//        mav.addObject("title", "Modificar contraseña");
//        mav.addObject("action", "clave");
//        return mav;
//    }