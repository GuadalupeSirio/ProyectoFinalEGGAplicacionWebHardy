package com.hardy.Hardy.controladores;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PrincipalControlador {

    @GetMapping
    public ModelAndView inicio(HttpSession session) {
        return new ModelAndView("index");
    }
}
