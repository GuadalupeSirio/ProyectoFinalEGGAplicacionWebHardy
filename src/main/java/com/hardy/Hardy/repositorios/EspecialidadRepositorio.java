package com.hardy.Hardy.repositorios;

import com.hardy.Hardy.entidades.Especialidad;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialidadRepositorio extends JpaRepository<Especialidad, Integer> {

    @Modifying
    @Query("UPDATE Especialidad a SET a.alta = :alta WHERE a.id = :id")
    void baja(@Param("id") Integer id, @Param("alta") Boolean alta);

    @Query("SELECT e FROM Especialidad e WHERE e.idUsuario = :id OR e.idUsuario = 0 AND e.alta = true")
    List<Especialidad> BuscarPorUsuario(@Param("id") Integer id);

    //@Query("SELECT e FROM Especialidad e WHERE e.idUsuario = :idUsuario OR (e.idUsuario = 0 AND e.idUsuario != :idUsuario) AND e.alta = true AND e.id= :especialidadId")
    @Query("SELECT e FROM Especialidad e WHERE e.id= :especialidadId")
    Optional<Especialidad> BuscarPorId(@Param("especialidadId") Integer especialidadId);
}
