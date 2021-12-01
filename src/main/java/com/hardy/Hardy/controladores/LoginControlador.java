package com.hardy.Hardy.controladores;

import com.hardy.Hardy.entidades.Usuario;
import com.hardy.Hardy.servicios.RolServicio;
import com.hardy.Hardy.servicios.UsuarioServicio;
import java.security.Principal;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class LoginControlador {

    @Autowired
    private UsuarioServicio us;

    @Autowired
    private RolServicio rs;

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, Principal principal) {

        ModelAndView mv = new ModelAndView("login");
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

    @GetMapping("/modificar-correo/{id}") 
    public ModelAndView editarUsuario(@PathVariable Integer id, HttpSession session) throws Exception {
        if (!session.getAttribute("id").equals(id)) {
            return new ModelAndView(new RedirectView("/"));
        }
        ModelAndView mav = new ModelAndView(""); //completar
        mav.addObject("usuario", us.buscarPorId(id));
        mav.addObject("title", "Modificar correo");
        mav.addObject("roles", rs.buscarTodos());
        mav.addObject("action", "modificar-correo");
        return mav;
    }

    @PostMapping("/modificar-correo")
    public RedirectView modificar(@RequestParam Integer id, @RequestParam String correo, @RequestParam(defaultValue = "0") Integer idRol, HttpSession session, RedirectAttributes a) {
        try {
            us.modificar(id, correo, idRol);  //El valor por defecto del idRol es 0. Si no es administrador, se oculta el campo y al pasar 0 no se modifica, si es admin lo puede modificar.
            a.addFlashAttribute("exito", "El usuario se modificó correctamente!");
            Usuario u = us.buscarPorId(id);
            session.setAttribute("correo", u.getCorreo());
        } catch (Exception e) {
            a.addFlashAttribute("error", e.getMessage());
            if (session.getAttribute("rol").equals("ADMIN")) {
                return new RedirectView("/usuarios/editar/" + id);
            }
            return new RedirectView("/modificar/" + id);
        }
        if (session.getAttribute("rol").equals("ADMIN")) {
            return new RedirectView("/usuarios");
        }
        return new RedirectView("/");
    }

    //Tengo separados los metodos para modificar correo y clave porque la clave no se completa con la info en los form, y sino hay que cambiarla si o si o volver a ingresarla    

    @GetMapping("/clave/{id}")
    public ModelAndView modificarClave(@PathVariable Integer id, HttpSession session) throws Exception {
        if (!session.getAttribute("id").equals(id)) { //Para que no pueda modificar la clave de nadie mas
            return new ModelAndView(new RedirectView("/"));
        }
        ModelAndView mav = new ModelAndView("");  //completar
        mav.addObject("usuario", us.buscarPorId(id));
        mav.addObject("title", "Modificar contraseña");
        mav.addObject("action", "clave");
        return mav;
    }

    @PostMapping("/clave")
    public RedirectView clave(@RequestParam Integer id, @RequestParam String clave, @RequestParam String clave2, HttpSession session, RedirectAttributes a) {
        try {
            us.clave(id, clave, clave2);
            a.addFlashAttribute("exito", "La clave se modificó correctamente!");
        } catch (Exception e) {
            a.addFlashAttribute("error", e.getMessage());
            return new RedirectView("/clave/" + id);
        }
        return new RedirectView("/");
    }
}
