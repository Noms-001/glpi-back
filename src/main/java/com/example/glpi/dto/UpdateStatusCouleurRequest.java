package com.example.glpi.dto;

public class UpdateStatusCouleurRequest {
    private Integer id;
    private String couleur;

    public UpdateStatusCouleurRequest() {
    }

    public UpdateStatusCouleurRequest(Integer id, String couleur) {
        this.id = id;
        this.couleur = couleur;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }
}
