package com.example.glpi.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "status")
public class Status {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "couleur", nullable = false)
    private String couleur;

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<LangueStatut> langueStatuts;

    public Status() {
    }

    public Status(Integer id, String couleur) {
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

    public List<LangueStatut> getLangueStatuts() {
        return langueStatuts;
    }

    public void setLangueStatuts(List<LangueStatut> langueStatuts) {
        this.langueStatuts = langueStatuts;
    }
}
