
package com.hardy.Hardy.repositorios;

import com.hardy.Hardy.entidades.Documento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoRepositorio extends JpaRepository<Documento, Integer>{
    
    @Query("SELECT d FROM Documento d WHERE d.cliente.id = :clienteId")
    public List<Documento> obtenerDocumentoCliente(@Param("clienteId") Integer clienteId);
}
