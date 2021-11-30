
package com.hardy.Hardy.repositorios;

import com.hardy.Hardy.entidades.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepositorio extends JpaRepository<Agenda, Integer>{
   
}
