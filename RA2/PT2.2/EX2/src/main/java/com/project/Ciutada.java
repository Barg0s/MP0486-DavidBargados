package com.project;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name = "ciutada")

public class Ciutada implements Serializable{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ciutadaId", unique=true, nullable=false)    

    private Long ciutadaId;



    private String nom;
    private String cognom;
    private int edat;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ciutatId")
    private Ciutat ciutat;
    @Column(name = "uuid", nullable = false, updatable = false, unique = true)
    private String uuid = UUID.randomUUID().toString();

    public Ciutada(){}


    public Ciutada(String nom,String cognom,int edat){
        this.nom = nom;
        this.cognom = cognom;
        this.edat = edat;

    }
    public Long getCiutadaId() {
        return ciutadaId;
    }
    public void setCiutadaId(Long ciutadaId) {
        this.ciutadaId = ciutadaId;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getCognom() {
        return cognom;
    }

    public void setCognom(String cognom) {
        this.cognom = cognom;
    }
    
    public int getEdat() {
        return edat;
    }
    
    public void setEdat(int edat) {
        this.edat = edat;
    }
    
    public Ciutat getCiutat(){
        return ciutat;
    }
    public void setCiutat(Ciutat ciutat){
        this.ciutat = ciutat;
    }


    @Override
    public String toString() {
        return ciutadaId + ": " + nom + " " + cognom + " (" + edat + " anys)";
    }    
     @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ciutada)) return false; 
        Ciutada item = (Ciutada) o;
        return Objects.equals(uuid, item.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }   
}
