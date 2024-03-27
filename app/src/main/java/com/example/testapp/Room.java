package com.example.testapp;

public class Room {
    protected int id;
    protected String type;
    protected String nom;
    protected float temperature;
    protected boolean lumiere;

    public Room(int id, String type, String nom, float temperature, boolean lumiere) {
        this.id = id;
        this.type = type;
        this.nom = nom;
        this.temperature = temperature;
        this.lumiere = lumiere;
    }
    public Room() {}
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
