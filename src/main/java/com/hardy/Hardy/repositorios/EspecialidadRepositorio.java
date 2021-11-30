package com.hardy.Hardy.repositorios;

import com.hardy.Hardy.entidades.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialidadRepositorio extends JpaRepository<Especialidad, Integer>{
    
}

