package com.hardy.Hardy.repositorios;

import com.hardy.Hardy.entidades.Estudio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudioRepositorio extends JpaRepository<Estudio, Integer> {

    @Modifying
    @Query("SELECT e.adjunto FROM Estudio e INNER JOIN Registro r WHERE e.registro.id = :registroId")
    List<Estudio> estudioxRegistro(@Param("registroId") Integer id);

    @Query("SELECT e FROM Estudio e WHERE e.registro.cliente.id = :clienteId")
    public List<Estudio> buscarTodosxCliente(@Param("clienteId") Integer clienteId);

}
