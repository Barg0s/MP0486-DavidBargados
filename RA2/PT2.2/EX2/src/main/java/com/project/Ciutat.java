package com.project;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ciutat")

public class Ciutat implements Serializable{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ciutatId", unique=true, nullable=false)
    private long ciutatId;

    private String nom;
    private String pais;
    private int poblacio;

    @OneToMany(mappedBy = "ciutat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)

    private Set<Ciutada> ciutadans = new HashSet<>();
    @Column(name = "uuid", nullable = false, updatable = false, unique = true)
    private String uuid = UUID.randomUUID().toString();

    public Ciutat(){}

    public Ciutat(String nom,String pais,int poblacio){
        this.nom = nom;
        this.pais = pais;
        this.poblacio = poblacio;
    }

    public long getCiutatId() {
        return ciutatId;
    }

    public void setCiutatId(long ciutatId) {
        this.ciutatId = ciutatId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public int getPoblacio() {
        return poblacio;
    }

    public void setPoblacio(int poblacio) {
        this.poblacio = poblacio;
    }

    public Set<Ciutada> getCiutadans() {
        return ciutadans;
    }

    public void setCiutadans(Set<Ciutada> ciutadans) {
        this.ciutadans.clear();;
        if (this.ciutadans != null){
            ciutadans.forEach(this::addCiutada);
        }
    }
    
    public void addCiutada(Ciutada ciutada){
        if (ciutadans.add(ciutada)){
            ciutada.setCiutat(this);
        }
    }


    public void deleteCiutada(Ciutada ciutada){
        if (ciutadans.remove(ciutada)){
            ciutada.setCiutat(null);
        }
    }

    @Override
    public String toString() {
        String llistaItems = "Buit";
        if (ciutadans != null && !ciutadans.isEmpty()) {
            llistaItems = ciutadans.stream()
                .map(Ciutada::getNom)
                .collect(Collectors.joining(", "));
        }
        return String.format("Ciutat [ID=%d, Nom=%s,Pais=%s, Poblacio=%d, Items: %s]", ciutatId, nom, pais,poblacio, llistaItems);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ciutat)) return false;
        Ciutat cart = (Ciutat) o;
        return Objects.equals(uuid, cart.uuid);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    } 
}
