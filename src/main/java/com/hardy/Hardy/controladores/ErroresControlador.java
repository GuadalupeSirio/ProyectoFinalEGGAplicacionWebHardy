package com.hardy.Hardy.controladores;

import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErroresControlador implements ErrorController {

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView errores(HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("error");
        String mensaje = "";
        int codigo = response.getStatus();

        switch (codigo) {
            case 403:
                mensaje = "No tiene permisos suficientes para acceder al recurso solicitado";
                break;
            case 404:
                mensaje = "El recurso solicitado no existe";
                break;
            case 500:
                mensaje = "Error interno en el servidor";
                break;
            default:
                mensaje = "Error inesperado";
        }
        mav.addObject("mensaje", mensaje);
        mav.addObject("codigo", codigo);

        return mav;
    }
}