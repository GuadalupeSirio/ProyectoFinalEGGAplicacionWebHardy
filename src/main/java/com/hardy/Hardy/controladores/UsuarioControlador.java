package com.hardy.Hardy.controladores;

import com.hardy.Hardy.excepciones.MiExcepcion;
import com.hardy.Hardy.servicios.UsuarioServicio;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@PreAuthorize("hasRole('ADMIN')") //Esta ruta que tiene todos los usuarios solo la podrian ver los ADMIN - tenemos que ver si lo dejamos asi o lo juntamos con CLIENTE
@RequestMapping("/usuarios")
public class UsuarioControlador {
    
    @Autowired
    private UsuarioServicio us;
    
    @GetMapping
    public ModelAndView mostrarUsuarios() {
        ModelAndView mav = new ModelAndView("usuarios");
        mav.addObject("usuarios", us.buscarTodos());
        return mav;
    }
    
    @PostMapping("/crear-admin")
    public RedirectView admin(HttpServletRequest request, Principal principal, @RequestParam String correo, @RequestParam String claveUno, @RequestParam String claveDos, RedirectAttributes a) throws MiExcepcion {
        try {
          us.crearAdmin(correo, claveDos, claveDos); 
          a.addFlashAttribute("exito", "El admin se registro correctamente!");
        } catch (Exception e) {
            a.addFlashAttribute("error", e.getMessage());
        }        
        return new RedirectView("/usuarios");      
    }
    
    @PostMapping("/baja/{id}")  //Al darse de baja deberia darse de baja el CLIENTE -
    public RedirectView baja(@PathVariable Integer id) {
        us.baja(id);
        return new RedirectView("/usuarios");
    }
    
    @PostMapping("/alta/{id}")
    public RedirectView alta(@PathVariable Integer id) {
        us.baja(id);
        return new RedirectView("/usuarios");
    }
}
