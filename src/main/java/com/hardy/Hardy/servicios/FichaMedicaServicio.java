package com.hardy.Hardy.servicios;

import com.hardy.Hardy.entidades.Cliente;
import com.hardy.Hardy.entidades.FichaMedica;
import com.hardy.Hardy.excepciones.MiExcepcion;
import com.hardy.Hardy.repositorios.FichaMedicaRepositorio;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FichaMedicaServicio {
    
    @Autowired
    private FichaMedicaRepositorio fichaMedicaRepositorio;

    //Metodos CRUD
    @Transactional
    public void guardarFichaMedica(String grupoSanguineo, Double peso, Integer altura,
            String enfermedades, LocalDate ultimoChequeo, Cliente cliente) throws Exception, MiExcepcion {
        try {
            validacionGrupoSanguineo(grupoSanguineo);
            validacionPeso(peso);
            validacionAltura(altura);
            validacionEnfermedades(enfermedades);
            //metodo ficha
            validacionCliente(cliente);
            
            FichaMedica fichaMedica = new FichaMedica();
            
            Integer edad = Period.between(cliente.getFechaNacimiento(), LocalDate.now()).getYears();
            
            fichaMedica.setAlta(true);
            fichaMedica.setGrupoSanguineo(grupoSanguineo);
            fichaMedica.setPeso(peso);
            fichaMedica.setAltura(altura);
            fichaMedica.setEnfermedades(enfermedades);
            fichaMedica.setUltimoChequeo(ultimoChequeo);
            fichaMedica.setCliente(cliente);
            fichaMedica.setEdad(edad);
            
            fichaMedicaRepositorio.save(fichaMedica);
            
        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Transactional
    public void modificarFichaMedica(String grupoSanguineo, Double peso, Integer altura,
            String enfermedades, LocalDate ultimoChequeo, Cliente cliente) throws Exception, MiExcepcion {
        try {
            validacionGrupoSanguineo(grupoSanguineo);
            validacionPeso(peso);
            validacionAltura(altura);
            //validacionEnfermedades(enfermedades);
            validacionUltimoChequeo(ultimoChequeo);
            validacionCliente(cliente);
            
            FichaMedica fichaMedica = obtenerFichamedicaId(cliente.getId());
            
            fichaMedica.setGrupoSanguineo(grupoSanguineo);
            fichaMedica.setPeso(peso);
            fichaMedica.setAltura(altura);
            //fichaMedica.setEnfermedades(enfermedades);
            fichaMedica.setUltimoChequeo(ultimoChequeo);
            
            fichaMedicaRepositorio.save(fichaMedica);
            
        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Transactional
    public void modificarEdad(Integer edad, Cliente cliente) throws Exception, MiExcepcion {
        try {
            validacionCliente(cliente);
            FichaMedica fichaMedica = obtenerFichamedicaId(cliente.getId());
            fichaMedica.setEdad(edad);
            fichaMedicaRepositorio.save(fichaMedica);
        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Transactional
    public void modificarPeso(Double peso, Cliente cliente) throws Exception, MiExcepcion {
        try {
            validacionPeso(peso);
            validacionCliente(cliente);
            FichaMedica fichaMedica = obtenerFichamedicaId(cliente.getId());
            fichaMedica.setPeso(peso);
            fichaMedicaRepositorio.save(fichaMedica);
        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Transactional
    public void modificarAltura(Integer altura, Cliente cliente) throws Exception, MiExcepcion {
        try {
            validacionAltura(altura);
            validacionCliente(cliente);
            FichaMedica fichaMedica = obtenerFichamedicaId(cliente.getId());
            fichaMedica.setAltura(altura);
            fichaMedicaRepositorio.save(fichaMedica);
        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Transactional
    public void modificarGrupoSanguineo(String grupoSanguineo, Cliente cliente) throws Exception, MiExcepcion {
        try {
            validacionGrupoSanguineo(grupoSanguineo);
            validacionCliente(cliente);
            FichaMedica fichaMedica = obtenerFichamedicaId(cliente.getId());
            fichaMedica.setGrupoSanguineo(grupoSanguineo);
            fichaMedicaRepositorio.save(fichaMedica);
        } catch (MiExcepcion ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    //Metodos de validacion
    public void validacionGrupoSanguineo(String grupoSanguineo) throws Exception, MiExcepcion {
        //falta expresion regular
        try {
            if (grupoSanguineo == null) {
                throw new MiExcepcion("Grupo sanguineo no fue cargado");
            } else if (grupoSanguineo.trim().isEmpty()) {
                throw new MiExcepcion("Grupo sanguineo invalido, no puede estar en blanco");
            } else if (grupoSanguineo.length() < 1) {
                throw new MiExcepcion("Grupo sanguineo invalido, debe tener mas de una letra");
            }
        } catch (MiExcepcion es) {
            throw es;
        } catch (Exception e) {
            throw e;
        }
    }
    
    public void validacionPeso(Double peso) throws Exception, MiExcepcion {
        //opcional : control de peso min y max 
        try {
            if (peso == null) {
                throw new MiExcepcion("Grupo sanguineo no fue cargado");
            } else if (peso <= 0) {
                throw new MiExcepcion("Peso invalido");
            }
        } catch (MiExcepcion es) {
            throw es;
        } catch (Exception e) {
            throw e;
        }
    }
    
    public void validacionAltura(Integer altura) throws Exception, MiExcepcion {
        //falta detalle de sistema de unidades
        try {
            if (altura == null) {
                throw new MiExcepcion("Altura no fue cargada");
            } else if (altura <= 0) {
                throw new MiExcepcion("Altura invalida");
            }
        } catch (MiExcepcion es) {
            throw es;
        } catch (Exception e) {
            throw e;
        }
    }
    
    public void validacionEnfermedades(String enfermedad) throws Exception, MiExcepcion {
        try {
            if (enfermedad == null) {
                throw new MiExcepcion("Enfermedad no fue cargada");
            } else if (enfermedad.trim().isEmpty()) {
                throw new MiExcepcion("Enfermedad invalida, no puede estar en blanco");
            } else if (enfermedad.length() < 1) {
                throw new MiExcepcion("Enfermedad invalida, debe tener mas de una letra");
            }
        } catch (MiExcepcion es) {
            throw es;
        } catch (Exception e) {
            throw e;
        }
    }
    
    public void validacionUltimoChequeo(LocalDate ultimoChequeo) throws Exception, MiExcepcion {
        try {
            LocalDate actual = LocalDate.now();
            if (ultimoChequeo == null) {
                throw new MiExcepcion("La fecha del ultimo chequeo no fue cargada");
            } else if (ultimoChequeo.isAfter(actual)) {
                throw new MiExcepcion("Ultimo chequeo no puede ser una fecha superior a la actual");
            }
        } catch (MiExcepcion es) {
            throw es;
        } catch (Exception e) {
            throw e;
        }
    }
    
    public void validacionCliente(Cliente cliente) throws Exception, MiExcepcion {
        try {
            if (cliente == null) {
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
    public List<FichaMedica> obtenerFichasMedicas() throws Exception {
        try {
            return fichaMedicaRepositorio.findAll();
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Transactional(readOnly = true)
    public FichaMedica obtenerFichamedicaId(Integer idCliente) throws Exception, MiExcepcion {
        try {
            FichaMedica fichaMedica = fichaMedicaRepositorio.obtenerFichaMedica(idCliente).orElseThrow(() -> new MiExcepcion("Error al obtener ficha medica"));
            return fichaMedica;
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Transactional(readOnly = true)
    public FichaMedica obtenerFichamedicaIdCliente(Integer idCliente) throws Exception, MiExcepcion {
        try {
            FichaMedica fichaMedica = fichaMedicaRepositorio.obtenerFichaMedica(idCliente).orElse(null);
            return fichaMedica;
        } catch (Exception e) {
            throw e;
        }
    }
}
