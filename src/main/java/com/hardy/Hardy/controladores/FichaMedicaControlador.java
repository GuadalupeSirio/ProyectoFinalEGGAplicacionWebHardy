package com.hardy.Hardy.controladores;

import com.hardy.Hardy.entidades.Cliente;
import com.hardy.Hardy.entidades.FichaMedica;
import com.hardy.Hardy.excepciones.MiExcepcion;
import com.hardy.Hardy.servicios.ClienteServicio;
import com.hardy.Hardy.servicios.FichaMedicaServicio;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/ficha-medica")
public class FichaMedicaControlador {

    @Autowired
    private FichaMedicaServicio fichaMedicaServicio;

    @Autowired
    private ClienteServicio clienteServicio;

    // metodos GET
    @GetMapping
    public ModelAndView mostrarFichaMedica(HttpSession sesion, HttpServletRequest request) throws MiExcepcion, Exception {

        ModelAndView mav = new ModelAndView("");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
        }

        Cliente cliente = clienteServicio.obtenerPerfil((Integer) sesion.getAttribute("idUsuario"));
        FichaMedica ficha = fichaMedicaServicio.obtenerFichamedicaId(cliente.getId());

        mav.addObject("perfil", cliente);
        mav.addObject("ficha", ficha);
        return mav;
    }

    // metodos POST
    @PostMapping("/guardar-ficha")
    public RedirectView guardarFicha(@RequestParam String grupoSanguineo, @RequestParam Double peso,
            @RequestParam Integer altura, @RequestParam String enfermedades, 
            HttpSession sesion, RedirectAttributes attributes) throws Exception {
        try {

            Cliente cliente = clienteServicio.obtenerPerfil((Integer) sesion.getAttribute("idUsuario"));

            fichaMedicaServicio.guardarFichaMedica(grupoSanguineo, peso, altura, enfermedades, cliente);

            attributes.addFlashAttribute("exito-name", "Ficha medica registrada exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("");
    }

    @PostMapping("/modificar-ficha")
    public RedirectView modificarFicha(@RequestParam String grupoSanguineo, @RequestParam Double peso,
            @RequestParam Integer altura, @RequestParam String enfermedades, LocalDate ultimoChequeo,
            HttpSession sesion, RedirectAttributes attributes) throws Exception {
        try {

            Cliente cliente = clienteServicio.obtenerPerfil((Integer) sesion.getAttribute("idUsuario"));

            fichaMedicaServicio.modificarFichaMedica(grupoSanguineo, peso, altura, enfermedades, ultimoChequeo, cliente);

            attributes.addFlashAttribute("exito-name", "Ficha medica registrada exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("");
    }
}
