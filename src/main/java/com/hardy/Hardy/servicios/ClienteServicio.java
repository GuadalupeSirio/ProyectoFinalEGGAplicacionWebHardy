package com.hardy.Hardy.servicios;

import com.hardy.Hardy.entidades.Cliente;
import com.hardy.Hardy.entidades.Usuario;
import com.hardy.Hardy.excepciones.MiExcepcion;
import com.hardy.Hardy.repositorios.ClienteRepositorio;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServicio {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    //Metodos CRUD
    @Transactional
    public void guardarCliente(String nombre, String apellido, Integer dni, LocalDate fechaNacimiento,
            String imagen, Usuario usuario) throws Exception, MiExcepcion {
        //Falta set de imagen
        try {
            validacionNombre(nombre, "Nombre");
            validacionNombre(nombre, "Apellido");
            validacionDni(dni);
            validacionFechaNacimiento(fechaNacimiento);

            Cliente cliente = new Cliente();
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setDni(dni);
            cliente.setFechaNacimiento(fechaNacimiento);
            cliente.setUsuario(usuario);

            clienteRepositorio.save(cliente);
        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void modificarCliente(String nombre, String apellido, Integer dni, LocalDate fechaNacimiento,
            String imagen) throws Exception, MiExcepcion {
        //Falta set de imagen
        try {
            validacionNombre(nombre, "Nombre");
            validacionNombre(nombre, "Apellido");
            validacionDni(dni);
            validacionFechaNacimiento(fechaNacimiento);

            Cliente cliente = new Cliente();
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setDni(dni);
            cliente.setFechaNacimiento(fechaNacimiento);

            clienteRepositorio.save(cliente);
        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    //Metodos de validacion
    
    public void validacionNombre(String nombre, String tipo) throws Exception, MiExcepcion {
        try {
            if (nombre == null) {
                throw new MiExcepcion(tipo + " no fue cargado");
            } else if (nombre.trim().isEmpty()) {
                throw new MiExcepcion(tipo + " invalido, no puede estar en blanco");
            } else if (nombre.length() < 1) {
                throw new MiExcepcion(tipo + " invalido, debe tener mas de una letra");
            }
        } catch (MiExcepcion es) {
            throw es;
        } catch (Exception e) {
            throw e;
        }
    }

    public void validacionDni(Integer documento) throws Exception, MiExcepcion {
        //chequear
        try {
            if (documento == null) {
                throw new MiExcepcion("Documento no fue cargado");
            } else if (documento < 0) {
                throw new MiExcepcion("Documento invalido, no puede ser un numero negativo");
            } else if (Long.toString(documento).matches("^[0-9][^a-zA-Z]{6,9}$") == false) {
                throw new MiExcepcion("Documento invalido");
            }
        } catch (MiExcepcion es) {
            throw es;
        } catch (Exception e) {
            throw e;
        }
    }

    public void validacionFechaNacimiento(LocalDate fechaNacimiento) throws Exception, MiExcepcion {
        try {
            LocalDate actual = LocalDate.now();
            if (fechaNacimiento == null) {
                throw new MiExcepcion("La fecha de nacimiento no fue cargada");
            } else if (Math.abs(actual.getYear() - fechaNacimiento.getYear()) < 15) {
                throw new MiExcepcion("La edad minima para crear una cuenta es de 14 años");
            }
        } catch (MiExcepcion es) {
            throw es;
        } catch (Exception e) {
            throw e;
        }
    }

    public void validacionUsuario(Usuario usuario) throws Exception, MiExcepcion {
        try {
            if (usuario == null) {
                throw new MiExcepcion("Los datos del usuario no fueron cargados");
            }
        } catch (MiExcepcion es) {
            throw es;
        } catch (Exception e) {
            throw e;
        }
    }

    //Metodos de consulta
    
    @Transactional(readOnly = true)
    public List<Cliente> obtenerClientes() throws Exception {
        try {
            return clienteRepositorio.findAll();
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Cliente obtenerClienteId(Integer id) throws Exception, MiExcepcion {
        try {
            Cliente cliente = clienteRepositorio.findById(id).orElseThrow(() -> new MiExcepcion("Error al obtener cliente"));
            return cliente;
        } catch (Exception e) {
            throw e;
        }
    }
}
