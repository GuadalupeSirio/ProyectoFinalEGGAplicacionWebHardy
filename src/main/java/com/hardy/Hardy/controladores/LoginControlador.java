package com.hardy.Hardy.controladores;

import com.hardy.Hardy.servicios.EmailServicio;
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
    private UsuarioServicio usuarioServicio;
    
    @Autowired
    private EmailServicio emailServicio;

    @GetMapping("/login")
    public ModelAndView login(HttpServletRequest request, @RequestParam(required = false) String error, @RequestParam(required = false) String logout, Principal principal) {

        ModelAndView mv = new ModelAndView("login");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mv.addObject("exito", flashMap.get("exito-name"));
            mv.addObject("error", flashMap.get("error-name"));
            return mv;
        }
        if (error != null) {
            mv.addObject("errorLog", "Correo o contraseña incorrecta");
            return mv;
        }
        if (logout != null) {
            mv.addObject("logout", "Salió correctamente de la plataforma");
            return mv;
        }
        if (principal != null) {
            mv.setViewName("redirect:/");
        }
        return mv;
    }

    @PostMapping("/modificar-correo")
    public RedirectView modificarCorreo(@RequestParam String correo, HttpSession session, RedirectAttributes attributes) {
        try {
            usuarioServicio.modificarCorreo((Integer) session.getAttribute("idUsuario"), correo);
            session.setAttribute("correo", correo);
            attributes.addFlashAttribute("exito", "El usuario se modificó correctamente!");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }
        return new RedirectView("/");
    }

    @PostMapping("/clave")
    public RedirectView modificarClave(@RequestParam String clave, @RequestParam String clave2, HttpSession session, RedirectAttributes attributes) {
        try {
            usuarioServicio.modificarClave((Integer) session.getAttribute("idUsuario"), clave, clave2);
            attributes.addFlashAttribute("exito", "La clave se modificó correctamente!");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }
        return new RedirectView("/");
    }

    @PostMapping("/emailContacto")
    public RedirectView emailDeContacto(RedirectAttributes attributes, @RequestParam String celular, @RequestParam String mensaje, 
            @RequestParam String nombre, @RequestParam String email) {
        try {
            emailServicio.contacto(celular, mensaje, nombre, email);
            attributes.addFlashAttribute("exito-name", "El correo se envio exitosamente");
        } catch (Exception e) {
            throw e;
        }
        return new RedirectView("/login");
    }
}
