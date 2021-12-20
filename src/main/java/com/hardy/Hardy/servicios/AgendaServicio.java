package com.hardy.Hardy.servicios;

import com.hardy.Hardy.entidades.Agenda;
import com.hardy.Hardy.entidades.Cliente;
import com.hardy.Hardy.entidades.Especialidad;
import com.hardy.Hardy.excepciones.MiExcepcion;
import com.hardy.Hardy.repositorios.AgendaRepositorio;
import com.hardy.Hardy.repositorios.ClienteRepositorio;
import com.hardy.Hardy.repositorios.EspecialidadRepositorio;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgendaServicio {

    @Autowired
    private AgendaRepositorio agendaRepositorio;

    @Autowired
    private EspecialidadRepositorio especialidadRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Transactional
    public void crearAgenda(LocalDate fecha, LocalTime hora, String medico, String lugar, Especialidad especialidad, Cliente cliente) throws MiExcepcion, Exception {
        try {

            validarNombre(medico, "medico");
            validarNombre(lugar, "lugar");
            validarFecha(fecha);
            validarHora(hora);
            validarCliente(cliente);

            Agenda agenda = new Agenda();

            agenda.setFecha(fecha);
            agenda.setHora(hora);
            agenda.setMedico(medico);
            agenda.setLugar(lugar);
            agenda.setAlta(true);
            agenda.setCliente(cliente);
            agenda.setEspecialidad(especialidad);

            agendaRepositorio.save(agenda);

        } catch (MiExcepcion ex) {
            throw ex;

        } catch (Exception e) {
            throw e;
        }
    }

    public void modificar(Integer idAgenda, LocalDate fecha, LocalTime hora, String medico, String lugar, Integer idEspecialidad) throws MiExcepcion, Exception {
        try {
            validarNombre(medico, "medico");
            validarNombre(lugar, "lugar");
            validarFecha(fecha);
            validarHora(hora);

            Agenda agenda = agendaRepositorio.findById(idAgenda).orElseThrow(() -> new MiExcepcion("No se encontró el Id"));

            //agenda.setFecha(fecha);
            agenda.setHora(hora);
            agenda.setMedico(medico);
            agenda.setLugar(lugar);

            Especialidad especialidad = especialidadRepositorio.findById(idEspecialidad).orElseThrow(() -> new MiExcepcion("No se encontró el Id"));
            agenda.setEspecialidad(especialidad);
            agendaRepositorio.save(agenda);

        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<Agenda> buscarTodos() throws Exception {
        try {
            return agendaRepositorio.findAll();
        } catch (Exception e) {
            throw new Exception("Error al obtener turnos");
        }
    }
    
    @Transactional(readOnly = true)
    public List<Agenda> buscarMes(Integer clienteId) throws Exception {
        try {
            LocalDate fechadeHoy = LocalDate.now();
            return agendaRepositorio.obtenerAgendaMes(clienteId, fechadeHoy);
        } catch (Exception e) {
            throw new Exception("Error al obtener turnos");
        }
    }
    
    @Transactional(readOnly = true)
    public List<Agenda> buscarFuturos(Integer clienteId) throws Exception {
        try {
            LocalDate fechadeHoy = LocalDate.now();
            return agendaRepositorio.obtenerAgendaFuturo(clienteId, fechadeHoy);
        } catch (Exception e) {
            throw new Exception("Error al obtener turnos");
        }
    }

    @Transactional(readOnly = true)
    public List<Agenda> buscarPorUsuario(Integer clienteId) throws Exception {
        try {

            return agendaRepositorio.obtenerAgendaCliente(clienteId);
        } catch (Exception e) {
            throw new Exception("Error al buscar por Id");
        }
    }

    @Transactional(readOnly = true)
    public Agenda buscarPorId(Integer id) throws Exception {
        try {
            Optional<Agenda> agendaOptional = agendaRepositorio.findById(id);
            return agendaOptional.orElse(null);
        } catch (Exception e) {
            throw new Exception("Error al buscar por Id");
        }
    }

    @Transactional
    public void baja(Integer id) throws MiExcepcion {
        try {
            Optional<Agenda> agendaOptional = agendaRepositorio.findById(id);
            if (agendaOptional.isPresent()) {
                Agenda agenda = agendaOptional.get();
                agenda.setAlta(agenda.getAlta() ? false : true);
                agendaRepositorio.save(agenda);
            } else {
                throw new MiExcepcion("No se encontró el Id");
            }
        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void alta(Integer id) throws MiExcepcion {
        try {
            Optional<Agenda> agendaOptional = agendaRepositorio.findById(id);
            if (agendaOptional.isPresent()) {
                Agenda agenda = agendaOptional.get();
                agenda.setAlta(agenda.getAlta() ? true : false);
                agendaRepositorio.save(agenda);
            } else {
                throw new MiExcepcion("No se encontró el Id");
            }
        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    public void validarNombre(String nombre, String tipo) throws MiExcepcion, Exception {

        try {
            if (nombre == null) {
                throw new MiExcepcion("Nombre del " + tipo + " no fue cargado");
            } else if (nombre.trim().isEmpty()) {
                throw new MiExcepcion("Nombre del " + tipo + " no puede estar vacío");
            } else if (nombre.length() < 2) {
                throw new MiExcepcion("Nombre del " + tipo + " no puede tener menos de una letra");
            } else if (tipo.equalsIgnoreCase("medico")) {
                if (nombre.matches(".*\\d.*")) {
                    throw new MiExcepcion("Nombre del " + tipo + " no puede contener números");
                }
            }
        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    public void validarFecha(LocalDate fecha) throws MiExcepcion {
        try {
            if (fecha == null) {
                throw new MiExcepcion("Ingrese una fecha");
            } else if (fecha.toString().trim().isEmpty()) {
                throw new MiExcepcion("El campo fecha no puede estar vacìo");
            }

            LocalDate fechaActual = LocalDate.now();

            if (fecha.isBefore(fechaActual)) {
                throw new MiExcepcion("La fecha ingresada no es válida");
            }
        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    public void validarHora(LocalTime hora) throws MiExcepcion {
        try {
            if (hora == null) {
                throw new MiExcepcion("Ingrese un horario");
            } else if (hora.toString().trim().isEmpty()) {
                throw new MiExcepcion("El campo hora no puede estar vacìo"); //VER
            }
//
//            LocalTime horaActual = LocalTime.now();
//
//            if (hora.equals(horaActual) || hora.isBefore(horaActual)) {
//                throw new MiExcepcion("La hora ingresada no es válida");
//            }
        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    public void validarCliente(Cliente cliente) throws MiExcepcion, Exception {
        try {
            if (cliente == null) {
                throw new MiExcepcion("Error al obtener perfil");
            }
        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

}
