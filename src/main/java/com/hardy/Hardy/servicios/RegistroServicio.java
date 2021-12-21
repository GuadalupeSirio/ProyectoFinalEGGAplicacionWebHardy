package com.hardy.Hardy.servicios;

import com.hardy.Hardy.entidades.Cliente;
import com.hardy.Hardy.entidades.Especialidad;
import com.hardy.Hardy.entidades.FichaMedica;
import com.hardy.Hardy.entidades.Registro;
import com.hardy.Hardy.excepciones.MiExcepcion;
import com.hardy.Hardy.repositorios.FichaMedicaRepositorio;
import com.hardy.Hardy.repositorios.RegistroRepositorio;
import java.time.Instant;
import java.time.LocalDate;
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

    @Autowired
    private FichaMedicaServicio fichaMedicaServicio;

    @Autowired
    private FichaMedicaRepositorio fichaMedicaRepositorio;

    @Transactional
    public void crearRegistro(LocalDate fecha, String medico, String cobertura, String lugar, String resultados, Integer especialidad, Cliente cliente) throws Exception, MiExcepcion {

        try {

            /*if (fichaMedicaServicio.obtenerFichamedicaIdCliente(cliente.getId()) == null) {
                throw new Exception("Tiene que cargar la ficha medica primero");
            }*/
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
            registro.setAlta(true);

            /*FichaMedica fichamedica = fichaMedicaServicio.obtenerFichamedicaIdCliente(cliente.getId());
            if (fichamedica.getUltimoChequeo().isBefore(fecha) || fichamedica.getUltimoChequeo() == null) {
                fichamedica.setUltimoChequeo(fecha);
                fichaMedicaRepositorio.save(fichamedica);
            }*/
            registroRepositorio.save(registro);

        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void modificarRegistro(Integer id, LocalDate fecha, String medico, String cobertura,
            String lugar, String resultados, Especialidad especialidad) throws Exception, MiExcepcion {

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
            registro.setEspecialidad(especialidad);

            registroRepositorio.save(registro);

        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void registroBaja(Integer registroId) throws Exception, MiExcepcion {
        try {
            Registro registro = registroRepositorio.findById(registroId).orElseThrow(() -> new MiExcepcion("Error al obtener registro"));
            registro.setAlta(false);
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
    public List<Registro> obtenerRegistroEspecialidad(Integer clienteId, Integer especialidadId) throws Exception {

        try {
            return registroRepositorio.obtenerRegistroEspecialidad(clienteId, especialidadId);
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
    
        @Transactional(readOnly = true)
    public Boolean obtenerRegistroAlta(Integer id) throws Exception {
        try {
            Registro registro = registroRepositorio.findById(id).orElseThrow(() -> new MiExcepcion("Error al obtener registro"));
            return registro.getAlta();
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<Registro> obtenerRegistroCliente(Integer clienteId) throws Exception {
        try {
            List<Registro> registro = registroRepositorio.obtenerRegistroCliente(clienteId);
            return registro;
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

    public void validacionFecha(LocalDate fecha) throws Exception, MiExcepcion {
        try {
            LocalDate actual = LocalDate.now();

            if (fecha == null) {
                throw new MiExcepcion("La fecha no fue cargada");
            }
            if (fecha.isAfter(actual)) {
                throw new MiExcepcion("La fecha no puede ser posterior a la actual");
            }
        } catch (MiExcepcion es) {
            throw es;
        } catch (Exception e) {
            throw e;
        }
    }
}
