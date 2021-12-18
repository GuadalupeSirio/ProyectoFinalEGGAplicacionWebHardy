package com.hardy.Hardy.repositorios;

import com.hardy.Hardy.entidades.Registro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroRepositorio extends JpaRepository<Registro, Integer> {

    @Query("SELECT r FROM Registro r WHERE r.cliente.id = :clienteId")
    public List<Registro> obtenerRegistroCliente(@Param("clienteId") Integer clienteId);
    
    @Query("SELECT r FROM Registro r WHERE r.cliente.id = :clienteId and r.especialidad.id= :especialidadId")
    public List<Registro> obtenerRegistroEspecialidad(@Param("clienteId") Integer clienteId,@Param("especialidadId") Integer especialidadId);

}
