package com.hardy.Hardy.repositorios;

import com.hardy.Hardy.entidades.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroRepositorio extends JpaRepository<Registro, Integer> {
    
}
