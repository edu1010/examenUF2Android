package com.tiempociudades.edu1010.examen2;

import android.net.Uri;

public class incidencia {
    private String imagenURL;
    private String descripcion;

    public incidencia(String imagenURL, String descripcion, String aula) {
        this.imagenURL = imagenURL;
        this.descripcion = descripcion;
        Aula = aula;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAula() {
        return Aula;
    }

    public void setAula(String aula) {
        Aula = aula;
    }

    private String Aula;

    public incidencia() {
    }

    public incidencia(String imagenURL) {
        this.imagenURL = imagenURL;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }
}
