package com.example.glpi.dto;

public class LangueValeurDto {
    private String langue;
    private String valeur;

    public LangueValeurDto(String langue, String valeur) {
        this.langue = langue;
        this.valeur = valeur;
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
