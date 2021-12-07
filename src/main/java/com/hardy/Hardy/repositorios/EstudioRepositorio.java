
package com.hardy.Hardy.repositorios;

import com.hardy.Hardy.entidades.Estudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudioRepositorio extends JpaRepository<Estudio, Integer>{
    
}
