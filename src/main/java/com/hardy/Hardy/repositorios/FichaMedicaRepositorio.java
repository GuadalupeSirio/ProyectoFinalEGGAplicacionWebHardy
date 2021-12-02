package com.hardy.Hardy.repositorios;

import com.hardy.Hardy.entidades.FichaMedica;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FichaMedicaRepositorio extends JpaRepository<FichaMedica, Integer> {

    @Query("SELECT f FROM FichaMedica f WHERE f.cliente.id = :id")
    public Optional<FichaMedica> obtenerFichaMedica(@Param("id") Integer id);
}
