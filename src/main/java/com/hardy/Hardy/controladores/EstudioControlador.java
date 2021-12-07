package com.hardy.Hardy.controladores;

import com.hardy.Hardy.entidades.Estudio;
import com.hardy.Hardy.excepciones.MiExcepcion;
import com.hardy.Hardy.servicios.EstudioServicio;
import com.hardy.Hardy.servicios.RegistroServicio;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/estudios")
public class EstudioControlador {

    @Autowired
    private RegistroServicio registroServicio;

    @Autowired
    private EstudioServicio estudioServicio;

    @GetMapping
    public ModelAndView mostrarTodos(HttpServletRequest request) throws Exception, MiExcepcion {

        ModelAndView mav = new ModelAndView("estudios");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
        }
        mav.addObject("estudios", estudioServicio.buscarTodos());
        return mav;
    }

    @GetMapping("/crear")
    public ModelAndView crearEstudios() throws Exception {
        try {
            ModelAndView mav = new ModelAndView("estudios-formulario");
            mav.addObject("estudio", new Estudio());
            mav.addObject("registros", registroServicio.obtenerRegistros());
            mav.addObject("title", "Crear Estudio");
            mav.addObject("action", "guardar");
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/guardar")
    public RedirectView guardarEstudios(@RequestParam MultipartFile adjunto, @RequestParam("registro") Integer idRegistro, RedirectAttributes attributes) throws MiExcepcion, Exception {
        try {
            estudioServicio.crearEstudio(idRegistro, adjunto);
            attributes.addFlashAttribute("exito-name", "El estudio ha sido guardado exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/estudios");
    }

    @PostMapping("/baja/{id}")
    public RedirectView bajaEstudio(@PathVariable Integer idEstudio, RedirectAttributes attributes) throws Exception, MiExcepcion {
        try {
            estudioServicio.baja(idEstudio);
            Estudio estudio = estudioServicio.buscarPorId(idEstudio);
            attributes.addFlashAttribute("exito-name", "El estudio ha sido " + ((estudio.getAlta()) ? "habilitado" : "deshabilitado") + "  exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/estudios");
    }
}
