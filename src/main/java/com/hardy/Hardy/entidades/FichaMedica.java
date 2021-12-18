package com.hardy.Hardy.entidades;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class FichaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private Integer edad;
    @Column(nullable = false)
    private String grupoSanguineo;
    @Column(nullable = false)
    private Double peso;
    @Column(nullable = false)
    private Integer altura;
    @Column(nullable = false)
    private String enfermedades;
    private LocalDate ultimoChequeo;
    @Column(nullable = false)
    private Boolean alta;
    @OneToOne
    @JoinColumn(nullable = false)
    private Cliente cliente;

    public FichaMedica() {
    }

    public FichaMedica(Integer id, Integer edad, String grupoSanguineo, Double peso, Integer altura, 
            String enfermedades, LocalDate ultimoChequeo, Boolean alta, Cliente cliente) {
        this.id = id;
        this.edad = edad;
        this.grupoSanguineo = grupoSanguineo;
        this.peso = peso;
        this.altura = altura;
        this.enfermedades = enfermedades;
        this.ultimoChequeo = ultimoChequeo;
        this.alta = alta;
        this.cliente = cliente;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getGrupoSanguineo() {
        return grupoSanguineo;
    }

    public void setGrupoSanguineo(String grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Integer getAltura() {
        return altura;
    }

    public void setAltura(Integer altura) {
        this.altura = altura;
    }

    public String getEnfermedades() {
        return enfermedades;
    }

    public void setEnfermedades(String enfermedades) {
        this.enfermedades = enfermedades;
    }

    public LocalDate getUltimoChequeo() {
        return ultimoChequeo;
    }

    public void setUltimoChequeo(LocalDate ultimoChequeo) {
        this.ultimoChequeo = ultimoChequeo;
    }

    public Boolean getAlta() {
        return alta;
    }

    public void setAlta(Boolean alta) {
        this.alta = alta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

}
