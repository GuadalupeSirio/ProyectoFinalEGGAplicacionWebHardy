package com.hardy.Hardy.servicios;

import com.hardy.Hardy.entidades.Especialidad;
import com.hardy.Hardy.entidades.Rol;
import com.hardy.Hardy.entidades.Usuario;
import com.hardy.Hardy.excepciones.MiExcepcion;
import com.hardy.Hardy.repositorios.EspecialidadRepositorio;
import com.hardy.Hardy.repositorios.RolRepositorio;
import com.hardy.Hardy.repositorios.UsuarioRepositorio;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ClienteServicio clienteServicio;

    @Autowired
    private RolRepositorio rolRepositorio;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private EmailServicio emailServicio;

    @Autowired
    private EspecialidadRepositorio especialidadRepositorio;

    @Transactional
    public void crearUsuario(String nombre, String apellido, Integer dni, LocalDate fechaNacimiento, String correo, String clave, String clave2, MultipartFile imagen) throws Exception {
        try {
            validarCorreo(correo);
            clienteServicio.validacionDni(dni);
            validarClave(clave, clave2);
            clienteServicio.validacionFechaNacimiento(fechaNacimiento);
            clienteServicio.validacionNombre(nombre, "Nombre");
            clienteServicio.validacionNombre(nombre, "Apellido");

            Usuario usuario = new Usuario();
            usuario.setCorreo(correo);
            usuario.setClave(encoder.encode(clave));
            if (usuarioRepositorio.findAll().isEmpty()) {  //Si la lista esta vacia, se crean y guardan los dos roles y se le asigna el rol de ADMIN al primer usuario. 
                Rol r1 = new Rol("ADMIN");
                Rol r2 = new Rol("CLIENTE");
                rolRepositorio.save(r1);
                rolRepositorio.save(r2);
                usuario.setRol(r1);
                cargarEspecialidades();
                
            } else {
                usuario.setRol(rolRepositorio.buscarRol("CLIENTE")); //Luego todos los usuarios se setean con el rol de CLIENTE pero el admin puede modificarlo 
            }
            usuario.setAlta(true);

            usuarioRepositorio.save(usuario);
            
            clienteServicio.guardarCliente(nombre, apellido, dni, fechaNacimiento, imagen, usuario);

           // emailServicio.enviarThread(correo); //--> para enviar el correo de bienvenida   
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void cargarEspecialidades() throws MiExcepcion {
        try {
            Especialidad especialidad = new Especialidad("Cardiología", "Cardiología.png", true, 0);
            especialidadRepositorio.save(especialidad);
            especialidad = new Especialidad("Cirugía general", "Cirugía general.png", true, 0);
            especialidadRepositorio.save(especialidad);
            especialidad = new Especialidad("Clinica médica", "Clinica médica.png", true, 0);
            especialidadRepositorio.save(especialidad);
            especialidad = new Especialidad("Dermatología", "Dermatología.png", true, 0);
            especialidadRepositorio.save(especialidad);
            especialidad = new Especialidad("Gastroenterología", "Gastroenterología.png", true, 0);
            especialidadRepositorio.save(especialidad);
            especialidad = new Especialidad("Ginecología", "Ginecología.png", true, 0);
            especialidadRepositorio.save(especialidad);
            especialidad = new Especialidad("Neurología", "Neurología.png", true, 0);
            especialidadRepositorio.save(especialidad);
            especialidad = new Especialidad("Nutrición", "Nutrición.png", true, 0);
            especialidadRepositorio.save(especialidad);
            especialidad = new Especialidad("Obstetricia", "Obstetricia.png", true, 0);
            especialidadRepositorio.save(especialidad);
            especialidad = new Especialidad("Oftalmología", "Oftalmología.png", true, 0);
            especialidadRepositorio.save(especialidad);
            especialidad = new Especialidad("Pediatría", "Pediatría.png", true, 0);
            especialidadRepositorio.save(especialidad);
            especialidad = new Especialidad("Psiquiatría", "Psiquiatría.png", true, 0);
            especialidadRepositorio.save(especialidad);

        } catch (Exception e) {
            throw e;
        }

    }

    @Transactional
    public void crearAdmin(String correo, String clave, String clave2) throws MiExcepcion {
        try {
            validarCorreo(correo);
            validarClave(clave, clave2);
            Usuario usuario = new Usuario();
            usuario.setCorreo(correo);
            usuario.setClave(encoder.encode(clave));
            usuario.setRol(rolRepositorio.buscarRol("ADMIN"));
            usuario.setAlta(true);
            usuarioRepositorio.save(usuario);

            // emailServicio.enviarThread(correo);           
        } catch (Exception e) {
            throw e;
        }

    }

    @Transactional
    public void modificarCorreo(Integer id, String correo) throws Exception {
        try {
            Usuario usuario = buscarPorId(id);

            if (!usuario.getCorreo().equals(correo)) {
                validarCorreo(correo);
                usuario.setCorreo(correo);
            }
            usuarioRepositorio.save(usuario);
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Transactional
    public void modificarConRol(Integer id, Integer idRol, String correo) throws Exception {
        try {
            Usuario usuario = buscarPorId(id);

            if (!usuario.getCorreo().equals(correo)) {
                validarCorreo(correo);
                usuario.setCorreo(correo);
            }
            usuario.setRol(rolRepositorio.findById(idRol).orElse(usuario.getRol()));
            usuarioRepositorio.save(usuario);
        } catch (Exception e) {
            throw e;
       }
    }

    @Transactional
    public void modificarClave(Integer id, String clave, String clave2) throws MiExcepcion {
        try {
            Usuario usuario = buscarPorId(id);

            validarClave(clave, clave2);
            usuario.setClave(encoder.encode(clave));

            usuarioRepositorio.save(usuario);
        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
          Usuario usuario = usuarioRepositorio.findByCorreo(correo).orElseThrow(() -> new UsernameNotFoundException("No existe un usuario asociado al correo ingresado"));
        if (!usuario.getAlta()) {
            throw new UsernameNotFoundException("El usuario esta dado de baja");
        }
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attributes.getRequest().getSession(true);

        session.setAttribute("idUsuario", usuario.getId());
        session.setAttribute("correo", usuario.getCorreo());
        session.setAttribute("rol", usuario.getRol().getNombre());
        try {
            session.setAttribute("imagen", clienteServicio.obtenerPerfil(usuario.getId()).getImagen());
        } catch (Exception ex) {
            Logger.getLogger(UsuarioServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new User(usuario.getCorreo(), usuario.getClave(), Collections.singletonList(authority));          
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        try {
            return usuarioRepositorio.findAll();
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Integer id) {
        try {
            Optional<Usuario> uOptional = usuarioRepositorio.findById(id);
            return uOptional.orElse(null);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorCorreo(String correo) {
        try {
            Optional<Usuario> uOptional = usuarioRepositorio.findByCorreo(correo);
            return uOptional.orElse(null);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void baja(Integer id) { //Habria que agregar la baja del CLIENTE cuando se da de baja el usuario. 
        try {
            usuarioRepositorio.baja(id, false);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void alta(Integer id) {
        try {
            usuarioRepositorio.baja(id, true);
        } catch (Exception e) {
            throw e;
        }
    }

    public void validarCorreo(String correo) throws MiExcepcion {
        if (correo == null || correo.trim().isEmpty()) {
            throw new MiExcepcion("El correo no puede estar vacio.");
        }
        if (usuarioRepositorio.existsUsuarioByCorreo(correo)) {
            throw new MiExcepcion("Ya existe un usuario asociado al correo ingresado");
        }
        if (!(correo.contains("@") && correo.contains(".com"))) {
            throw new MiExcepcion("Debe ingresar un formato de correo valido.");
        }
    }

    public void validarClave(String clave, String clave2) throws MiExcepcion {
        if (clave.length() < 6) {
            throw new MiExcepcion("La contraseña debe tener al menos 6 caracteres");
        }
        if (!clave.equals(clave2)) {
            throw new MiExcepcion("Las contraseñas no coinciden");
        }
    }
}
