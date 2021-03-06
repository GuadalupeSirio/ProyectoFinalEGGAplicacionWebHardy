package com.hardy.Hardy.servicios;

import com.hardy.Hardy.entidades.Rol;
import com.hardy.Hardy.repositorios.RolRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RolServicio {

    @Autowired
    private RolRepositorio rolRepositorio;

    @Transactional(readOnly = true)
    public List<Rol> buscarTodos() {
        try {
         return rolRepositorio.findAll();   
        } catch (Exception e) {
            throw e;
        }
    }
}
