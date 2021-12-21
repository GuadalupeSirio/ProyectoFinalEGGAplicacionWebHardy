package com.hardy.Hardy.controladores;

import com.hardy.Hardy.excepciones.MiExcepcion;
import com.hardy.Hardy.servicios.RolServicio;
import com.hardy.Hardy.servicios.UsuarioServicio;
import java.util.Map;
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
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@PreAuthorize("hasRole('ADMIN')") //Todas las rutas de este service son solo para ADMIN
@RequestMapping("/usuarios")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private RolServicio rolServicio;

    @GetMapping
    public ModelAndView mostrarUsuarios(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("usuarios");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
        }
        mav.addObject("usuarios", usuarioServicio.buscarTodos());
        mav.addObject("roles", rolServicio.buscarTodos());
        return mav;
    }

    @PostMapping("/crear-admin")
    public RedirectView admin(@RequestParam String correo, @RequestParam String claveUno, @RequestParam String claveDos, RedirectAttributes attributes) throws MiExcepcion {
        try {
            usuarioServicio.crearAdmin(correo, claveDos, claveDos);
            attributes.addFlashAttribute("exito", "El admin se registro correctamente!");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/usuarios");
    }

    @PostMapping("/editar")
    public RedirectView editar(@RequestParam Integer idUsuario, @RequestParam String correo, @RequestParam Integer idRol, RedirectAttributes attributes) throws MiExcepcion {
        try {
            usuarioServicio.modificarConRol(idUsuario, idRol, correo);
            attributes.addFlashAttribute("exito", "El usuario se modificó correctamente!");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/usuarios");
    }

    @PostMapping("/baja/{id}")
    public RedirectView baja(@PathVariable Integer id, RedirectAttributes attributes) {
        try {
            usuarioServicio.baja(id);
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }

        return new RedirectView("/usuarios");
    }

    @PostMapping("/alta/{id}")
    public RedirectView alta(@PathVariable Integer id, RedirectAttributes attributes) {
        try {
            usuarioServicio.alta(id);
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/usuarios");
    }
}
