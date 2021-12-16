package com.hardy.Hardy.servicios;

import com.hardy.Hardy.entidades.Especialidad;
import com.hardy.Hardy.excepciones.MiExcepcion;
import com.hardy.Hardy.repositorios.EspecialidadRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EspecialidadServicio {

    @Autowired
    private EspecialidadRepositorio especialidadRepositorio;

    private String mensaje = "No existe ninguna especialidad asociada con el ID %s";

    @Transactional
    public void crearEspecialidad(String nombre, Integer idUsuario) throws Exception, MiExcepcion {
        try {

            validacionNombre(nombre, "Nombre");

            Especialidad especialidad = new Especialidad();

            especialidad.setNombre(nombre);
            especialidad.setIdUsuario(idUsuario);
            especialidad.setAlta(true);

            especialidadRepositorio.save(especialidad);

        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void modificarEspecialidad(Especialidad esp) throws Exception, MiExcepcion {
        try {

            validacionNombre(esp.getNombre(), "Nombre");

            Especialidad especialidad = especialidadRepositorio.findById(esp.getId()).orElseThrow(() -> new MiExcepcion(String.format(mensaje, esp.getId())));
            especialidad.setNombre(esp.getNombre());
            especialidadRepositorio.save(especialidad);
        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }

    }

    @Transactional(readOnly = true)
    public List<Especialidad> obtenerEspeciaidades() throws Exception {

        try {
            return especialidadRepositorio.findAll();
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Transactional(readOnly = true)
    public List<Especialidad> buscarPorUsuario(Integer id) throws Exception {

        try {
            return especialidadRepositorio.BuscarPorUsuario(id);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Especialidad obtenerEspecialidadId(Integer id) throws Exception {
        try {

            Optional<Especialidad> especialidadOptional = especialidadRepositorio.findById(id);
            return especialidadOptional.orElse(null);
            
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void eliminarEspecialidad(Integer id) {
        especialidadRepositorio.deleteById(id);
    }

    @Transactional
    public void baja(Integer id) {
        especialidadRepositorio.baja(id, false);
    }

    @Transactional
    public void alta(Integer id) {
        especialidadRepositorio.baja(id, true);
    }

    public void validacionNombre(String nombre, String tipo) throws Exception, MiExcepcion {
        try {
            if (nombre == null) {
                throw new MiExcepcion(tipo + " no fue cargado");
            } else if (nombre.trim().isEmpty()) {
                throw new MiExcepcion(tipo + " invalido, no puede estar vacio");
            }
        } catch (MiExcepcion es) {
            throw es;
        } catch (Exception e) {
            throw e;
        }
    }

}
