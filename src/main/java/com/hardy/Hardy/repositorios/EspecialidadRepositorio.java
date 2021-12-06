package com.hardy.Hardy.repositorios;

import com.hardy.Hardy.entidades.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialidadRepositorio extends JpaRepository<Especialidad, Integer>{
    
    @Modifying
    @Query("UPDATE Especialidad a SET a.alta = :alta WHERE a.id = :id")
    void baja(@Param("id") Integer id, @Param("alta") Boolean alta);

}
