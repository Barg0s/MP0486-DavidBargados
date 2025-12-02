package com.project;

import java.io.Serializable;

public class Ciutada implements Serializable{

    private long ciutatId;
    private String nom;
    private String cognom;
    private int edat;

    public Ciutada(){}


    public Ciutada(String nom){
        this.nom = nom;

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
    

    
}
