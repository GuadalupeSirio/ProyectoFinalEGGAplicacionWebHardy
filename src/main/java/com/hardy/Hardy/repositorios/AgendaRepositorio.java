package com.hardy.Hardy.repositorios;

import com.hardy.Hardy.entidades.Agenda;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepositorio extends JpaRepository<Agenda, Integer> {

    @Modifying
    @Query("UPDATE Agenda a SET a.alta = :alta WHERE a.id = :id")
    void baja(@Param("id") Integer id, @Param("alta") Boolean alta);

    @Query("SELECT a FROM Agenda a WHERE a.id = :id")
    Agenda buscarPorId(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE Agenda a SET a.fecha = :fecha, a.hora = :hora, a.medico = :medico, a.lugar = :lugar WHERE a.id = :id")
    void modificar(@Param("id") Integer id, @Param("fecha") LocalDate fecha, @Param("hora") LocalTime hora, @Param("medico") String medico, @Param("lugar") String lugar);

    @Query("SELECT a FROM Agenda a WHERE a.cliente.id = :clienteId")
    public List<Agenda> obtenerAgendaCliente(@Param("clienteId") Integer clienteId);
    
    @Query("SELECT a FROM Agenda a WHERE a.cliente.id = :clienteId AND MONTH(a.fecha)=MONTH(:fechadeHoy) AND YEAR(a.fecha) =YEAR(:fechadeHoy) AND a.alta = true")
    public List<Agenda> obtenerAgendaMes(@Param("clienteId") Integer clienteId, @Param("fechadeHoy") LocalDate fechadeHoy);
    
    @Query("SELECT a FROM Agenda a WHERE a.cliente.id = :clienteId AND (MONTH(a.fecha)!=MONTH(:fechadeHoy) AND YEAR(a.fecha)!=YEAR(:fechadeHoy)) AND a.fecha>:fechadeHoy AND a.alta = true")
    public List<Agenda> obtenerAgendaFuturo(@Param("clienteId") Integer clienteId, @Param("fechadeHoy") LocalDate fechadeHoy);
}
