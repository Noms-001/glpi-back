package com.example.glpi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "langue_statut")
public class LangueStatut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_status", nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "id_langue", nullable = false)
    private Langue langue;

    @Column(name = "value", nullable = false)
    private String value;

    public LangueStatut() {
    }

    public LangueStatut(Status status, Langue langue, String value) {
        this.status = status;
        this.langue = langue;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Langue getLangue() {
        return langue;
    }

    public void setLangue(Langue langue) {
        this.langue = langue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
