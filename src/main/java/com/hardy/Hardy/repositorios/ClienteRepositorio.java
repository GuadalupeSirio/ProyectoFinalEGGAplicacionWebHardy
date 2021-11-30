
package com.hardy.Hardy.repositorios;

import com.hardy.Hardy.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Integer>{
    
}
