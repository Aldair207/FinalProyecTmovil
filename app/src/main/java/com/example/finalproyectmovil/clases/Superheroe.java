package com.example.finalproyectmovil.clases;

public class Superheroe {
    private String nombre;
    private String descripcion;
    private String imagen;
    private String comics;
    private String poderes;

    public Superheroe(String nombre, String descripcion, String imagen, String comics, String poderes) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.comics = comics;
        this.poderes = poderes;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getComics() {
        return comics;
    }

    public void setComics(String comics) {
        this.comics = comics;
    }

    public String getPoderes() {
        return poderes;
    }

    public void setPoderes(String poderes) {
        this.poderes = poderes;
    }
} 