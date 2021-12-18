package com.hardy.Hardy.repositorios;

import com.hardy.Hardy.entidades.Cliente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Integer> {

    long countByDni(Integer dni);

    @Query("SELECT c FROM Cliente c WHERE c.usuario.id = :id")
    public Optional<Cliente> obtenerPerfil(@Param("id") Integer id);

}
