package com.hardy.Hardy.controladores;

import com.hardy.Hardy.excepciones.MiExcepcion;
import com.hardy.Hardy.servicios.ClienteServicio;
import com.hardy.Hardy.servicios.UsuarioServicio;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/perfil")
class PerfilControlador {

    @Autowired
    private ClienteServicio clienteServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    //metodos GET
    @GetMapping
    public ModelAndView mostrarPerfil(HttpSession sesion, HttpServletRequest request) throws MiExcepcion, Exception {

        ModelAndView mav = new ModelAndView("perfil");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
        }

        mav.addObject("perfil", clienteServicio.obtenerPerfil((Integer) sesion.getAttribute("idUsuario")));
        return mav;
    }

    // metodos POST
    @PostMapping("/guardar-usuario")
    public RedirectView guardar( RedirectAttributes attributes,HttpServletRequest request,
           Principal principal, @RequestParam String correo, @RequestParam String claveUno,
            @RequestParam String claveDos,
            @RequestParam String nombre,
            @RequestParam String apellido, @RequestParam Integer dni,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaNacimiento,
            @RequestParam MultipartFile imagen) throws Exception {
        try {
            if (principal != null) {
                return new RedirectView("/inicio");
            }
            usuarioServicio.crearUsuario(nombre, apellido, dni, fechaNacimiento, correo, claveUno, claveDos, imagen);
            request.login(correo, claveUno);

            attributes.addFlashAttribute("exito-name", "Usuario registrado exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/login");
    }

}
