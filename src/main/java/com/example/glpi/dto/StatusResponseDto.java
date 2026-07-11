package com.example.glpi.dto;

import java.util.List;

public class StatusResponseDto {
    private Integer id;
    private String couleur;
    private List<LangueValeurDto> langues;

    public StatusResponseDto(Integer id, String couleur, List<LangueValeurDto> langues) {
        this.id = id;
        this.couleur = couleur;
        this.langues = langues;
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

    public List<LangueValeurDto> getLanguues() {
        return langues;
    }

    public void setLanguues(List<LangueValeurDto> langues) {
        this.langues = langues;
    }
}
