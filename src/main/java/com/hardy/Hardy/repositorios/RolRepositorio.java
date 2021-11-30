package com.hardy.Hardy.repositorios;

import com.hardy.Hardy.entidades.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepositorio extends JpaRepository<Rol, Integer> {

    @Query("SELECT a FROM Rol a WHERE a.nombre = :nombre")
    Rol buscarRol(@Param("nombre") String nombre);
}
