package com.hardy.Hardy.servicios;

import com.hardy.Hardy.entidades.Rol;
import com.hardy.Hardy.entidades.Usuario;
import com.hardy.Hardy.excepciones.MiExcepcion;
import com.hardy.Hardy.repositorios.RolRepositorio;
import com.hardy.Hardy.repositorios.UsuarioRepositorio;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    private UsuarioRepositorio ur;

    @Autowired
    private ClienteServicio cs;

    @Autowired
    private RolRepositorio rr;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private EmailServicio es;

    @Transactional
    public void crearUsuario(String nombre, String apellido, Integer dni, LocalDate fechaNacimiento, String correo, String clave, String clave2, Integer idRol, MultipartFile imagen) throws Exception {

        validarCorreo(correo);
        cs.validacionDni(dni);
        validarClave(clave, clave2);

        Usuario u = new Usuario();
        u.setCorreo(correo);
        u.setClave(encoder.encode(clave));
        if (ur.findAll().isEmpty()) {  //Si la lista esta vacia, se crean y guardan los dos roles y se le asigna el rol de ADMIN al primer usuario. 
            Rol r1 = new Rol("ADMIN");
            Rol r2 = new Rol("CLIENTE");
            rr.save(r1);
            rr.save(r2);
            u.setRol(r1);
        }else{
            u.setRol(rr.buscarRol("CLIENTE")); //Luego todos los usuarios se setean con el rol de CLIENTE pero el admin puede modificarlo 
        }
        
        u.setAlta(true);
        //Despues de validar el correo, la clave y el DNI, pasamos a crear la entidad de cliente para que se hagan las validaciones de los campos 
        //antes de guardar el usuario en la base de datos, si salta excepcion en cliente usuario no se persiste.

        cs.guardarCliente(nombre, apellido, dni, fechaNacimiento, imagen, u);
        ur.save(u);
        // es.enviarThread(correo); --> para enviar el correo de bienvenida
    }

    @Transactional
    public void modificar(Integer id, String correo, Integer idRol) throws Exception {

        Usuario u = buscarPorId(id);

        if (!u.getCorreo().equals(correo)) {
            validarCorreo(correo);
            u.setCorreo(correo);
        }
        if (idRol != 0 && idRol != u.getRol().getId()) {
            u.setRol(rr.findById(idRol).orElse(null));
        }
        ur.save(u);
    }

    @Transactional
    public void clave(Integer id, String clave, String clave2) throws MiExcepcion {  
        Usuario u = buscarPorId(id);

        validarClave(clave, clave2);
        u.setClave(encoder.encode(clave));

        ur.save(u);
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = ur.findByCorreo(correo).orElseThrow(() -> new UsernameNotFoundException("No existe un usuario asociado al correo ingresado"));
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attributes.getRequest().getSession(true);

        session.setAttribute("idUsuario", usuario.getId());
        session.setAttribute("correo", usuario.getCorreo());
        session.setAttribute("rol", usuario.getRol().getNombre());

        return new User(usuario.getCorreo(), usuario.getClave(), Collections.singletonList(authority));
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return ur.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Integer id) {
        Optional<Usuario> uOptional = ur.findById(id);
        return uOptional.orElse(null);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorCorreo(String correo) {
        Optional<Usuario> uOptional = ur.findByCorreo(correo);
        return uOptional.orElse(null);
    }

    @Transactional
    public void baja(Integer id) { //Habria que agregar la baja del CLIENTE cuando se da de baja el usuario. 
        ur.baja(id, false);
    }

    @Transactional
    public void alta(Integer id) {
        ur.baja(id, true);
    }

    public void validarCorreo(String correo) throws MiExcepcion {
        if (correo == null || correo.trim().isEmpty()) {
            throw new MiExcepcion("El correo no puede estar vacio.");
        }
        if (ur.existsUsuarioByCorreo(correo)) {
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
