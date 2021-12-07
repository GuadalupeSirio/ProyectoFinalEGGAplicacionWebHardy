package com.hardy.Hardy.servicios;

import com.hardy.Hardy.entidades.Cliente;
import com.hardy.Hardy.entidades.Especialidad;
import com.hardy.Hardy.entidades.Registro;
import com.hardy.Hardy.excepciones.MiExcepcion;
import com.hardy.Hardy.repositorios.RegistroRepositorio;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistroServicio {

    @Autowired
    private RegistroRepositorio registroRepositorio;

    @Autowired
    private EspecialidadServicio especialidadServicio;

    @Transactional
    public void crearRegistro(Date fecha, String medico, String cobertura, String lugar, String resultados, Integer especialidad, Cliente cliente) throws Exception, MiExcepcion {

        try {
            validacionFecha(fecha);
            validacionMedico(medico, "Medico");
            validacionCobertura(cobertura, "Cobertura");
            validacionLugar(lugar, "Lugar");
            

            Registro registro = new Registro();

            registro.setFecha(fecha);
            registro.setMedico(medico);
            registro.setCobertura(cobertura);
            registro.setLugar(lugar);
            registro.setResultados(resultados);
            registro.setEspecialidad(especialidadServicio.obtenerEspecialidadId(especialidad));
            registro.setCliente(cliente);

            registroRepositorio.save(registro);
        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void modificarRegistro(Integer id, Date fecha, String medico, String cobertura, String lugar, String resultados) throws Exception, MiExcepcion {

        try {

            validacionFecha(fecha);
            validacionMedico(medico, "Medico");
            validacionCobertura(cobertura, "Cobertura");
            validacionLugar(lugar, "Lugar");

            Registro registro = registroRepositorio.findById(id).orElse(null);

            registro.setFecha(fecha);
            registro.setMedico(medico);
            registro.setCobertura(cobertura);
            registro.setLugar(lugar);
            registro.setResultados(resultados);

            registroRepositorio.save(registro);

        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<Registro> obtenerRegistros() throws Exception {

        try {
            return registroRepositorio.findAll();
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Registro obtenerRegistroId(Integer id) throws Exception {
        try {
            //Registro registro = registroRepositorio.findById(id).orElseThrow(() -> new MiExcepcion("Error al obtener registro"));
            Optional<Registro> registroOptional = registroRepositorio.findById(id);
            return registroOptional.orElse(null);
        } catch (Exception e) {
            throw e;
        }
    }

    
    public void validacionMedico(String medico, String tipo) throws Exception, MiExcepcion {
        try {
            if (medico == null) {
                throw new MiExcepcion(tipo + " no fue cargado");
            } else if (medico.trim().isEmpty()) {
                throw new MiExcepcion(tipo + " invalido, no puede estar vacio");
            }
        } catch (MiExcepcion es) {
            throw es;
        } catch (Exception e) {
            throw e;
        }
    }

    public void validacionCobertura(String cobertura, String tipo) throws Exception, MiExcepcion {
        try {
            if (cobertura == null) {
                throw new MiExcepcion(tipo + " no fue cargado");
            } else if (cobertura.trim().isEmpty()) {
                throw new MiExcepcion(tipo + " invalido, no puede estar vacio");
            }
        } catch (MiExcepcion es) {
            throw es;
        } catch (Exception e) {
            throw e;
        }
    }

    public void validacionLugar(String lugar, String tipo) throws Exception, MiExcepcion {
        try {
            if (lugar == null) {
                throw new MiExcepcion(tipo + " no fue cargado");
            } else if (lugar.trim().isEmpty()) {
                throw new MiExcepcion(tipo + " invalido, no puede estar vacio");
            }
        } catch (MiExcepcion es) {
            throw es;
        } catch (Exception e) {
            throw e;
        }
    }

    public void validacionFecha(Date fecha) throws Exception, MiExcepcion {
        try {
            Date actual = new Date();
 
            if (fecha == null) {
                throw new MiExcepcion("La fecha no fue cargada");
            }
            if (fecha.after(actual)) {
                throw new MiExcepcion("La fecha no puede ser posterior a la actual");
            }
        } catch (MiExcepcion es) {
            throw es;
        } catch (Exception e) {
            throw e;
        }
    }
}
