
package com.hardy.Hardy.controladores;

import com.hardy.Hardy.entidades.Cliente;
import com.hardy.Hardy.entidades.Especialidad;
import com.hardy.Hardy.entidades.Registro;
import com.hardy.Hardy.servicios.EspecialidadServicio;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("/especialidad")
public class EspecialidadControlador {
    
    @Autowired
    private EspecialidadServicio especialidadServicio;
    
    @GetMapping
    public ModelAndView mostrarEspecialidades() throws Exception {
        ModelAndView mav = new ModelAndView("especialidades");
        mav.addObject("especialidades", especialidadServicio.obtenerEspeciaidades());
        return mav;
    }
    
    /*@GetMapping("/guardar-especialidad")
    public ModelAndView crearEspecialidad(HttpServletRequest request) {

        ModelAndView mav = new ModelAndView("especialidad-formulario");

        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
        }
        mav.addObject("especialidad", new Registro());
        mav.addObject("title", "Crear Especialidad");
        mav.addObject("action", "guardar");

        return mav;
    }*/
    
     @GetMapping("/editar/{id}")
    public ModelAndView editarEspecialidad(HttpServletRequest request, @PathVariable Integer id) throws Exception {
        
        ModelAndView mav = new ModelAndView("especialidad-formulario");
        
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
        }
        mav.addObject("registro", especialidadServicio.obtenerEspecialidadId(id));
        mav.addObject("title", "Modificar Especialidad");
        mav.addObject("action", "modificar");
        return mav;
    }
    
    @PostMapping("/guardar")
    public RedirectView guardar(HttpServletRequest request, RedirectAttributes attributes, @RequestParam String nombre) throws Exception {

        try {

            especialidadServicio.crearEspecialidad(nombre);
            attributes.addFlashAttribute("exito-name", "La especialidad se cargo exitosamente");

        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }

        return new RedirectView("/registro");

    }
    
    @PostMapping("/modificar")
    public RedirectView modificar (HttpServletRequest request, RedirectAttributes attributes, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha, @RequestParam String medico, @RequestParam String cobertura, @RequestParam String lugar, @RequestParam String resultados, @RequestParam Especialidad especialidad, @RequestParam Cliente cliente) throws Exception {
        
        try {

            especialidadServicio.modificarEspecialidad(especialidad);
            attributes.addFlashAttribute("exito-name", "El registro se cargo exitosamente");

        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }

        return new RedirectView("/registros");
    }
}
