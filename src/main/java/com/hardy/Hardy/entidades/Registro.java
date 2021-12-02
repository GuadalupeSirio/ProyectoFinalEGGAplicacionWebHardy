package com.hardy.Hardy.entidades;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;

@Entity
//@Table(name = "autor")
public class Registro {
    
    @Id
    @GeneratedValue
    Integer id;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    Date fecha;
    
    //List<String> estudio;
    
    @Column(nullable = false)
    String medico;
    
    @Column(nullable = false)
    String cobertura;
    
    @Column(nullable = false)
    String lugar;
    
    @Column(nullable = false)
    String resultados;
    
    @OneToOne
    @JoinColumn(nullable = false)
    Especialidad especialidad;
    
    @OneToOne
    @JoinColumn(nullable = false)
    Cliente cliente;

    public Registro(Integer id, Date fecha, List<String> estudio, String medico, String cobertura, String lugar, String resultados, Especialidad especialidad, Cliente cliente) {
        this.id = id;
        this.fecha = fecha;
        //this.estudio = estudio;
        this.medico = medico;
        this.cobertura = cobertura;
        this.lugar = lugar;
        this.resultados = resultados;
        this.especialidad = especialidad;
        this.cliente = cliente;
    }
    

    public Registro() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

   /* public List<String> getEstudio() {
        return estudio;
    }*/

   /* public void setEstudio(List<String> estudio) {
        this.estudio = estudio;
    }*/

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public String getCobertura() {
        return cobertura;
    }

    public void setCobertura(String cobertura) {
        this.cobertura = cobertura;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getResultados() {
        return resultados;
    }

    public void setResultados(String resultados) {
        this.resultados = resultados;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    
    
}

