package com.example.glpi.dto;

public class UpdateStatutValeurRequest {
    private Integer id;
    private String langue;
    private String valeur;

    public UpdateStatutValeurRequest() {
    }

    public UpdateStatutValeurRequest(Integer id, String langue, String valeur) {
        this.id = id;
        this.langue = langue;
        this.valeur = valeur;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }
}
