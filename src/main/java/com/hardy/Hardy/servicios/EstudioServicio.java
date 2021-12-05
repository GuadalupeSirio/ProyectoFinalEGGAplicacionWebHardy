package com.hardy.Hardy.servicios;

import com.hardy.Hardy.entidades.Estudio;
import com.hardy.Hardy.entidades.Registro;
import com.hardy.Hardy.excepciones.MiExcepcion;
import com.hardy.Hardy.repositorios.EstudioRepositorio;
import com.hardy.Hardy.repositorios.RegistroRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EstudioServicio {

    @Autowired
    private EstudioRepositorio estudioRepositorio;

    @Autowired
    private RegistroRepositorio registroRepositorio;
    
    @Autowired
    private ImagenServicio imagenServicio;
    
    @Transactional
    public void crearEstudio(Integer idRegistro, MultipartFile adjunto) throws MiExcepcion {

        Estudio estudio = new Estudio();

        estudio.setAlta(true);
        if(!adjunto.isEmpty()){
            estudio.setAdjunto(imagenServicio.copiar(adjunto));
        }

        Optional<Registro> respuesta1 = registroRepositorio.findById(idRegistro);
        if (respuesta1.isPresent()) {
            Registro registro = respuesta1.get();
            estudio.setRegistro(registro);
        } else {
            throw new MiExcepcion("No se encontró el Id");
        }
        
        estudioRepositorio.save(estudio);
    }
    
    @Transactional(readOnly = true)
    public List<Estudio> buscarTodos() throws Exception {
        try {
            return estudioRepositorio.findAll();
        } catch (Exception e) {
            throw new Exception("Error al obtener estudios");
        }
    }
    
    @Transactional(readOnly = true)
    public Estudio buscarPorId(Integer id) throws Exception {
        try {
            Optional<Estudio> estudioOptional = estudioRepositorio.findById(id);
            return estudioOptional.orElse(null);
        } catch (Exception e) {
            throw new Exception("Error al buscar por Id");
        }
    }
    
    @Transactional
    public void baja(Integer id) throws MiExcepcion {
        try {
            Optional<Estudio> estudioOptional = estudioRepositorio.findById(id);
            if (estudioOptional.isPresent()) {
                Estudio estudio = estudioOptional.get();
                estudio.setAlta(estudio.getAlta() ? false : true);
                estudioRepositorio.save(estudio);
            } else {
                throw new MiExcepcion("No se encontró el Id");
            }
        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

}
