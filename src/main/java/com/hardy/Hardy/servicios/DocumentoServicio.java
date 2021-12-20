
package com.hardy.Hardy.servicios;

import com.hardy.Hardy.entidades.Cliente;
import com.hardy.Hardy.entidades.Documento;
import com.hardy.Hardy.excepciones.MiExcepcion;
import com.hardy.Hardy.repositorios.DocumentoRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentoServicio {
    
    @Autowired 
    private DocumentoRepositorio documentoRepositorio;
    
    @Autowired
    private ImagenServicio imagenServicio;
    
    @Autowired
    private EspecialidadServicio especialidadServicio;
    
    @Transactional
    public void crearDocumento(Cliente cliente , Integer especialidad, MultipartFile adjunto, String nombre) throws MiExcepcion, Exception {

        try {
            validacionNombre(nombre);
            Documento documento = new Documento();

            documento.setAlta(true);
            if (!adjunto.isEmpty()) {
                documento.setAdjunto(imagenServicio.copiar(adjunto));
            }
            
            documento.setNombre(nombre);
            documento.setCliente(cliente);
            documento.setEspecialidad(especialidadServicio.obtenerEspecialidadId(especialidad));
            documentoRepositorio.save(documento);
        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }
    
        public void validacionNombre(String nombre) throws Exception, MiExcepcion{
        try {
            if (nombre == null) {
                throw new MiExcepcion("Nombre del archivo no fue cargado");
            } 
            if (nombre.trim().isEmpty()) {
                throw new MiExcepcion("Nombre del archivo invalido, no puede estar en blanco");
            } 
            if (nombre.length() < 3) {
                throw new MiExcepcion("Nombre del archivo invalido, debe tener mas de tres carácteres");
            } 
        } catch (MiExcepcion es) {
            throw es;
        } catch (Exception e) {
            throw e;
        }
    }
        
            @Transactional(readOnly = true)
    public List<Documento> obtenerDocumentoCliente(Integer clienteId) throws Exception {
        try {
            List<Documento> registro = documentoRepositorio.obtenerDocumentoCliente(clienteId);
            return registro;
        } catch (Exception e) {
            throw e;
        }
    }
}
