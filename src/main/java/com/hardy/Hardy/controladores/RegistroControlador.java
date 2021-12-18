package com.hardy.Hardy.controladores;

import com.hardy.Hardy.entidades.Cliente;
import com.hardy.Hardy.entidades.Especialidad;
import com.hardy.Hardy.entidades.Registro;
import com.hardy.Hardy.servicios.ClienteServicio;
import com.hardy.Hardy.servicios.EspecialidadServicio;
import com.hardy.Hardy.servicios.EstudioServicio;
import com.hardy.Hardy.servicios.FichaMedicaServicio;
import com.hardy.Hardy.servicios.RegistroServicio;
import java.time.LocalDate;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/registro")
public class RegistroControlador {

    @Autowired
    private RegistroServicio registroServicio;

    @Autowired
    private ClienteServicio clienteServicio;

    @Autowired
    private EspecialidadServicio especialidadServicio;

    @Autowired
    private FichaMedicaServicio fichaMedicaServicio;

    @Autowired
    private EstudioServicio estudioServicio;

    @GetMapping
    public ModelAndView mostrarRegistro(HttpSession sesion, HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView("registro-vista");
        Cliente cliente = clienteServicio.obtenerPerfil((Integer) sesion.getAttribute("idUsuario"));
        mav.addObject("fichaMedica", fichaMedicaServicio.obtenerFichamedicaIdCliente(cliente.getId()));
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
        }
        mav.addObject("ruta", "/registro");
        mav.addObject("especialidades", especialidadServicio.buscarPorUsuario((Integer) sesion.getAttribute("idUsuario")));
        return mav;
    }

    @GetMapping("/ver-registros")
    public ModelAndView mostrarRegistros(HttpSession sesion, HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView("registros-consulta");

        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
        }
        Cliente cliente = clienteServicio.obtenerPerfil((Integer) sesion.getAttribute("idUsuario"));
        mav.addObject("fichaMedica", fichaMedicaServicio.obtenerFichamedicaIdCliente(cliente.getId()));

        mav.addObject("ruta", "/registro/ver-registros");
        mav.addObject("titulo", "Historial médico");
        mav.addObject("especialidad", new Especialidad());
        mav.addObject("estudios", estudioServicio.buscarTodosxCliente(cliente.getId()));
        mav.addObject("registros", registroServicio.obtenerRegistroCliente(cliente.getId()));
        return mav;
    }

    @GetMapping("/ver-registros/{especialidadId}")
    public ModelAndView mostrarRegistrosEspecialidad(@PathVariable Integer especialidadId, HttpSession sesion, HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView("registros-consulta");

        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
        }
        Cliente cliente = clienteServicio.obtenerPerfil((Integer) sesion.getAttribute("idUsuario"));
        Especialidad especialidad = especialidadServicio.obtenerEspecialidadId(especialidadId);
        mav.addObject("ruta", "/registro/ver-registros/"+especialidadId);
        mav.addObject("fichaMedica", fichaMedicaServicio.obtenerFichamedicaIdCliente(cliente.getId()));
        mav.addObject("titulo", "Historial de " + especialidad.getNombre());
        mav.addObject("especialidad", especialidad);
        mav.addObject("estudios", estudioServicio.buscarTodosxCliente(cliente.getId()));
        mav.addObject("registros", registroServicio.obtenerRegistroEspecialidad(cliente.getId(), especialidadId));
        return mav;
    }

    @GetMapping("/crear-registro")
    public ModelAndView crearRegistro(HttpSession sesion, RedirectAttributes attributes, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("registro-formulario");

        try {
            Cliente cliente = clienteServicio.obtenerPerfil((Integer) sesion.getAttribute("idUsuario"));
            mav.addObject("fichaMedica", fichaMedicaServicio.obtenerFichamedicaIdCliente(cliente.getId()));

            Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
            if (flashMap != null) {
                mav.addObject("exito", flashMap.get("exito-name"));
                mav.addObject("error", flashMap.get("error-name"));
            }
            mav.addObject("registro", new Registro());
            mav.addObject("especialidades", especialidadServicio.obtenerEspeciaidades());
            mav.addObject("title", "Crear Registro");
            mav.addObject("action", "guardar");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }

        return mav;
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarRegistro(RedirectAttributes attributes, @PathVariable Integer id) throws Exception {
        ModelAndView mav = new ModelAndView("registro-formulario");
        try {
            mav.addObject("registro", registroServicio.obtenerRegistroId(id));
            mav.addObject("especialidad", especialidadServicio.obtenerEspeciaidades());
            mav.addObject("title", "Crear Registro");
            mav.addObject("action", "modificar");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardar(HttpSession sesion, HttpServletRequest request, RedirectAttributes attributes,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha, @RequestParam String medico,
            @RequestParam String cobertura, @RequestParam String lugar, @RequestParam String resultados,
            @RequestParam Integer especialidad, @RequestParam String ruta) throws Exception {

        try {
            Cliente cliente = clienteServicio.obtenerPerfil((Integer) sesion.getAttribute("idUsuario"));
            registroServicio.crearRegistro(fecha, medico, cobertura, lugar, resultados, especialidad, cliente);
            attributes.addFlashAttribute("exito-name", "El registro se cargo exitosamente");

        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
            return new RedirectView(ruta);
        }

        return new RedirectView(ruta);

    }

    @PostMapping("/modificar")
    public RedirectView modificar(HttpServletRequest request, RedirectAttributes attributes, @RequestParam Integer id, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha, @RequestParam String medico, @RequestParam String cobertura, @RequestParam String lugar, @RequestParam String resultados, @RequestParam Especialidad especialidad) throws Exception {

        try {

            registroServicio.modificarRegistro(id, fecha, medico, cobertura, lugar, resultados);
            attributes.addFlashAttribute("exito-name", "El registro se cargo exitosamente");

        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }

        return new RedirectView("/registro");
    }
}
