package com.example.lab9.entity;

import javax.persistence.*;

@Entity
@Table(name="areas")
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idarea;

    @Column(name="nombrearea")
    private String nombrearea;

    public Integer getIdarea() {
        return idarea;
    }

    public void setIdarea(Integer idarea) {
        this.idarea = idarea;
    }

    public String getNombrearea() {
        return nombrearea;
    }

    public void setNombrearea(String nombrearea) {
        this.nombrearea = nombrearea;
    }
}
