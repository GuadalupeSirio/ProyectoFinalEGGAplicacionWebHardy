package com.hardy.Hardy.controladores;

import com.hardy.Hardy.entidades.Agenda;
import com.hardy.Hardy.entidades.Cliente;
import com.hardy.Hardy.entidades.Especialidad;
import com.hardy.Hardy.excepciones.MiExcepcion;
import com.hardy.Hardy.servicios.AgendaServicio;
import com.hardy.Hardy.servicios.ClienteServicio;
import com.hardy.Hardy.servicios.EspecialidadServicio;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
@RequestMapping("/agenda")
public class AgendaControlador {

    @Autowired
    private AgendaServicio agendaServicio;

    @Autowired
    private EspecialidadServicio especialidadServicio;

    @Autowired
    private ClienteServicio clienteServicio;


    @GetMapping
    public ModelAndView mostrarTodos(HttpSession sesion, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("turnos");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
        }
        Cliente cliente = clienteServicio.obtenerPerfil((Integer) sesion.getAttribute("idUsuario"));
        mav.addObject("especialidades", especialidadServicio.buscarPorUsuario((Integer) sesion.getAttribute("idUsuario")));
        mav.addObject("turnosMes", agendaServicio.buscarMes(cliente.getId()));
        mav.addObject("turnosFuturo", agendaServicio.buscarFuturos(cliente.getId()));
        return mav;
    }

    @GetMapping("/crear")
    public ModelAndView crearAgendas(HttpServletRequest request) throws Exception {
        try {
            ModelAndView mav = new ModelAndView("agendas-formulario");
            Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
            if (flashMap != null) {
                mav.addObject("exito", flashMap.get("exito-name"));
                mav.addObject("error", flashMap.get("error-name"));
            }
            mav.addObject("agenda", new Agenda());
            mav.addObject("especialidades", especialidadServicio.obtenerEspeciaidades());
            mav.addObject("title", "Crear Agenda");
            mav.addObject("action", "guardar");
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarAgendas(@PathVariable Integer id, HttpServletRequest request) throws Exception, MiExcepcion {
        try {
            ModelAndView mav = new ModelAndView("agendas-formulario");
            Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
            if (flashMap != null) {
                mav.addObject("exito", flashMap.get("exito-name"));
                mav.addObject("error", flashMap.get("error-name"));
            }
            mav.addObject("agenda", agendaServicio.buscarPorId(id));
            mav.addObject("especialidades", especialidadServicio.obtenerEspeciaidades());
            mav.addObject("title", "Editar Agenda");
            mav.addObject("action", "modificar");
            return mav;
        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    @PostMapping("/guardar")
    public RedirectView guardarAgendas(@RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime hora,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha, @RequestParam String medico,
            @RequestParam String lugar, @RequestParam Integer idEspecialidad,
            HttpSession sesion, RedirectAttributes attributes) throws Exception, MiExcepcion {
        try {
            Cliente cliente = clienteServicio.obtenerPerfil((Integer) sesion.getAttribute("idUsuario"));
            Especialidad especialidad = especialidadServicio.obtenerEspecialidadIdCliente(idEspecialidad, cliente.getId());
            agendaServicio.crearAgenda(fecha, hora, medico, lugar, especialidad, cliente);
            attributes.addFlashAttribute("exito-name", "El turno ha sido guardado exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/agenda");
    }

    @PostMapping("/modificar")
    public RedirectView modificarAgendas(@RequestParam Integer idAgenda, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha, @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime hora, @RequestParam String medico, @RequestParam String lugar, @RequestParam("especialidad") Integer idEspecialidad, RedirectAttributes attributes) throws Exception, MiExcepcion {
        try {
            agendaServicio.modificar(idAgenda, fecha, hora, medico, lugar, idEspecialidad);
            attributes.addFlashAttribute("exito-name", "La agenda ha sido modificada exitosamente");

        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/agendas");
    }

    @PostMapping("/baja/{id}")
    public RedirectView bajaAgenda(@PathVariable Integer idAgenda, RedirectAttributes attributes) throws Exception, MiExcepcion {
        try {
            agendaServicio.baja(idAgenda);
            Agenda agenda = agendaServicio.buscarPorId(idAgenda);
            attributes.addFlashAttribute("exito-name", "La agenda ha sido " + ((agenda.getAlta()) ? "habilitada" : "deshabilitada") + "  exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/agendas");
    }

}
