package com.hardy.Hardy.controladores;

import com.hardy.Hardy.excepciones.MiExcepcion;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

@Controller
@RequestMapping("/inicio")
public class PrincipalControlador {

    @GetMapping
    public ModelAndView inicio(HttpSession sesion, HttpServletRequest request) throws MiExcepcion, Exception {

        ModelAndView mav = new ModelAndView("index");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
        }

        mav.addObject("id", (Integer) sesion.getAttribute("idUsuario"));
        
        return mav;
    }
}
