package com.hardy.Hardy.servicios;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImagenServicio {
    
    @Value("${my.property}")
    private String directory;

    public String copiar(MultipartFile archivo) throws Exception {
        try {
            String imagen = archivo.getOriginalFilename();
            Path ruta = Paths.get(directory, imagen).toAbsolutePath();
            Files.copy(archivo.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);
            return imagen;
        } catch (IOException e) {
            throw e;
       }
    }
    
        public String borrarImagen(String imagen) throws Exception { // Metodo para poder eliminar la imagen anterior si el Cliente la cambia
        try {
            Path ruta = Paths.get(directory, imagen).toAbsolutePath();
            Files.delete(ruta);
            return imagen;
        } catch (IOException e) {
            throw e;
        }
    }
}
