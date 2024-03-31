package com.example.testapp;

//toute la classe sert à rien je crois
public class Room {
    protected String type;
    protected String nom;
    protected float temperature;
    protected boolean lumiere;

    private Room() {};

    public Room(String type, String nom, float temperature, boolean lumiere) {
        this.type = type;
        this.nom = nom;
        this.temperature = temperature;
        this.lumiere = lumiere;
    }
    public String  getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public float getTemperature() {
        return temperature;
    }
    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }
    public boolean isLumiere() {
        return lumiere;
    }
    public void setLumiere(boolean lumiere) {
        this.lumiere = lumiere;
    }
}
