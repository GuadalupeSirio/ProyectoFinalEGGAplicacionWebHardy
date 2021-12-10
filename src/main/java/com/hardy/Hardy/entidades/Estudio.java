package com.hardy.Hardy.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Estudio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Boolean alta;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Registro registro;

    @Column(nullable = false)
    private String adjunto;

    public Estudio() {
    }

    public Estudio(Integer id, Boolean alta, String nombre, Registro registro, String adjunto) {
        this.id = id;
        this.alta = alta;
        this.nombre = nombre;
        this.registro = registro;
        this.adjunto = adjunto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getAlta() {
        return alta;
    }

    public void setAlta(Boolean alta) {
        this.alta = alta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Registro getRegistro() {
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public String getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(String adjunto) {
        this.adjunto = adjunto;
    }

   
}
