
package com.hardy.Hardy.repositorios;

import com.hardy.Hardy.entidades.FichaMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FichaMedicaRepositorio extends JpaRepository<FichaMedica, Integer>{
    
}
