package com.hardy.Hardy.servicios;

import com.hardy.Hardy.entidades.Cliente;
import com.hardy.Hardy.entidades.Usuario;
import com.hardy.Hardy.excepciones.MiExcepcion;
import com.hardy.Hardy.repositorios.ClienteRepositorio;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ClienteServicio {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Autowired
    private FichaMedicaServicio fichaMedicaServicio;

    //Metodos CRUD
    @Transactional
    public void guardarCliente(String nombre, String apellido, Integer dni, LocalDate fechaNacimiento,
            MultipartFile imagen, Usuario usuario) throws Exception, MiExcepcion {

        try {

            Cliente cliente = new Cliente();
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setDni(dni);
            cliente.setFechaNacimiento(fechaNacimiento);
            cliente.setUsuario(usuario);
            if (!imagen.isEmpty()) {
                cliente.setImagen(imagenServicio.copiar(imagen));
            }
            clienteRepositorio.save(cliente);
        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void editarNombre(String nombre, Cliente cliente) throws Exception, MiExcepcion {
        try {
            validacionNombre(nombre, "Nombre");
            cliente.setNombre(nombre);
            clienteRepositorio.save(cliente);
        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void editarApellido(String apellido, Cliente cliente) throws Exception, MiExcepcion {
        try {
            validacionNombre(apellido, "Apellido");
            cliente.setApellido(apellido);
            clienteRepositorio.save(cliente);
        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void editarDocumento(Integer documento, Cliente cliente) throws Exception, MiExcepcion {
        try {
            validacionDni(documento);
            cliente.setDni(documento);
            clienteRepositorio.save(cliente);
        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void editarFechaNacimiento(LocalDate fechaNacimiento, Cliente cliente) throws Exception, MiExcepcion {
        try {
            validacionFechaNacimiento(fechaNacimiento);
            cliente.setFechaNacimiento(fechaNacimiento);
            fichaMedicaServicio.modificarEdad(Period.between(cliente.getFechaNacimiento(), LocalDate.now()).getYears(), cliente);
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
            } else if (nombre.matches(".*\\d.*")) {
                throw new MiExcepcion(tipo + " invalido, no puede contener numeros");
            }
        } catch (MiExcepcion es) {
            throw es;
        } catch (Exception e) {
            throw e;
        }
    }

    public void validacionDni(Integer documento) throws Exception, MiExcepcion {
        try {
            if (documento == null) {
                throw new MiExcepcion("Documento no fue cargado");
            } else if (documento < 0) {
                throw new MiExcepcion("Documento invalido, no puede ser un numero negativo");
            } else if (Long.toString(documento).matches("^[0-9][^a-zA-Z]{6,9}$") == false) {
                throw new MiExcepcion("Documento invalido");
            } else if (clienteRepositorio.countByDni(documento) > 0) {
                throw new MiExcepcion("Documento ingresado se encuentra asociado a una cuenta");
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
            Period edad = Period.between(fechaNacimiento, actual);
            if (fechaNacimiento == null) {
                throw new MiExcepcion("La fecha de nacimiento no fue cargada");
            } else if (edad.getYears() < 15) {
                throw new MiExcepcion("La edad minima para crear una cuenta es de 15 años");
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

    @Transactional
    public void modificarImagen(Cliente cliente, MultipartFile imagen) throws Exception {
        try {
            if (!imagen.isEmpty()) {
                if (cliente.getImagen()!=null) {
                    imagenServicio.borrarImagen(cliente.getImagen());
                }
                cliente.setImagen(imagenServicio.copiar(imagen));
                clienteRepositorio.save(cliente);
            }
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
    public Cliente obtenerPerfil(Integer id) throws Exception, MiExcepcion {
        try {
            Cliente cliente = clienteRepositorio.obtenerPerfil(id).orElseThrow(() -> new MiExcepcion("Error al obtener datos del perfil"));
            return cliente;
        } catch (Exception e) {
            throw e;
        }
    }
    
}
