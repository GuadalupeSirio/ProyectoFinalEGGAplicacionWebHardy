package com.hardy.Hardy.controladores;

import com.hardy.Hardy.entidades.Cliente;
import com.hardy.Hardy.excepciones.MiExcepcion;
import com.hardy.Hardy.servicios.ClienteServicio;
import com.hardy.Hardy.servicios.DocumentoServicio;
import com.hardy.Hardy.servicios.EspecialidadServicio;
import com.hardy.Hardy.servicios.EstudioServicio;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/documento")
public class DocumentoControlador {
    
@Autowired
private ClienteServicio clienteServicio;

@Autowired
private EspecialidadServicio especialidadServicio;

@Autowired
private EstudioServicio estudioServicio;

@Autowired
private DocumentoServicio dcoumentoServicio;

    @GetMapping
    public ModelAndView mostrarRegistro(HttpSession sesion, HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView("documentos");
        
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
            
        }
        
        Cliente cliente = clienteServicio.obtenerPerfil((Integer) sesion.getAttribute("idUsuario"));
        mav.addObject("especialidades", especialidadServicio.buscarPorUsuario((Integer) sesion.getAttribute("idUsuario")));
        mav.addObject("documentos", dcoumentoServicio.obtenerDocumentoCliente(cliente.getId()));
        mav.addObject("estudios", estudioServicio.buscarTodosxCliente(cliente.getId()));
        
        return mav;
    }
    
    @PostMapping("/guardar")
    public RedirectView guardarEstudios(HttpSession sesion,@RequestParam MultipartFile adjunto, @RequestParam Integer especialidad, 
            @RequestParam String nombre,RedirectAttributes attributes) throws MiExcepcion, Exception {
        try {
           Cliente cliente = clienteServicio.obtenerPerfil((Integer) sesion.getAttribute("idUsuario"));
           dcoumentoServicio.crearDocumento(cliente, especialidad, adjunto, nombre);
           attributes.addFlashAttribute("exito-name", "El archivo ha sido guardado exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/documento");
    }
}
