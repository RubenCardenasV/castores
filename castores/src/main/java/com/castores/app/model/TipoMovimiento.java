package com.castores.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipos_movimiento")
public class TipoMovimiento {

    @Id
    private int idTipo;

    private String nombre;


    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
