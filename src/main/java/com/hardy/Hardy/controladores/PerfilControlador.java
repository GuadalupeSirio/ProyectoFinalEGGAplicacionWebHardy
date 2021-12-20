package com.hardy.Hardy.controladores;

import com.hardy.Hardy.entidades.Cliente;
import com.hardy.Hardy.excepciones.MiExcepcion;
import com.hardy.Hardy.servicios.ClienteServicio;
import com.hardy.Hardy.servicios.FichaMedicaServicio;
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

    @Autowired
    private FichaMedicaServicio fichaMedicaServicio;

    //metodos GET
    @GetMapping
    public ModelAndView mostrarPerfil(HttpSession sesion, HttpServletRequest request) throws MiExcepcion, Exception {

        ModelAndView mav = new ModelAndView("perfil-vista");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
        }
        Cliente cliente = clienteServicio.obtenerPerfil((Integer) sesion.getAttribute("idUsuario"));
        mav.addObject("fichaMedica", fichaMedicaServicio.obtenerFichamedicaIdCliente(cliente.getId()));
        mav.addObject("cliente", cliente);
        return mav;
    }

    // metodos POST
    @PostMapping("/guardar-usuario")
    public RedirectView guardar(RedirectAttributes attributes, HttpServletRequest request,
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
            
            //attributes.addFlashAttribute("exito-name", "Usuario registrado exitosamente");
            request.login(correo, claveUno);
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/login");
    }

    @PostMapping("/editar-foto")
    public RedirectView modificarImagen(RedirectAttributes attributes, @RequestParam MultipartFile imagen, HttpSession sesion) throws Exception {
        try {    
            Cliente cliente = clienteServicio.obtenerPerfil((Integer) sesion.getAttribute("idUsuario"));
            clienteServicio.modificarImagen(cliente, imagen);
            sesion.setAttribute("imagen", cliente.getImagen());
            attributes.addFlashAttribute("exito-name", "La imagen se modifico correctamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/perfil");
    }

    @PostMapping("/editar-nombre")
    public RedirectView editarNombre(RedirectAttributes attributes, HttpServletRequest request, @RequestParam String nombre, HttpSession sesion) throws Exception {
        try {
            Cliente cliente = clienteServicio.obtenerPerfil((Integer) sesion.getAttribute("idUsuario"));
            clienteServicio.editarNombre(nombre, cliente);
            attributes.addFlashAttribute("exito-name", "El nombre se modifico exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/perfil");
    }

    @PostMapping("/editar-apellido")
    public RedirectView editarApellido(RedirectAttributes attributes, HttpServletRequest request,
            @RequestParam String apellido, HttpSession sesion) throws Exception {
        try {

            Cliente cliente = clienteServicio.obtenerPerfil((Integer) sesion.getAttribute("idUsuario"));
            clienteServicio.editarApellido(apellido, cliente);
            attributes.addFlashAttribute("exito-name", "El apellido se modifico exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/perfil");
    }

    @PostMapping("/editar-documento")
    public RedirectView editarDocumento(RedirectAttributes attributes, HttpServletRequest request,
            @RequestParam Integer documento, HttpSession sesion) throws Exception {
        try {
            Cliente cliente = clienteServicio.obtenerPerfil((Integer) sesion.getAttribute("idUsuario"));
            clienteServicio.editarDocumento(documento, cliente);
            attributes.addFlashAttribute("exito-name", "El documento se modifico exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/perfil");
    }

    @PostMapping("/editar-fecha-nacimiento")
    public RedirectView editarFecha(RedirectAttributes attributes, HttpServletRequest request,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaNacimiento, HttpSession sesion) throws Exception {
        try {

            Cliente cliente = clienteServicio.obtenerPerfil((Integer) sesion.getAttribute("idUsuario"));
            clienteServicio.editarFechaNacimiento(fechaNacimiento, cliente);
            attributes.addFlashAttribute("exito-name", "La fecha de nacimiento se modifico exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/perfil");
    }

}
