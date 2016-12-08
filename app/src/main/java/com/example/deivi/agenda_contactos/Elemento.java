package com.example.deivi.agenda_contactos;

import java.io.Serializable;

public class Elemento implements Serializable {
    //private static final long serialVersionUID = 1L;
    private long id;
    private String nombre, telefono, rFoto, direccion, email, pagina;

    public Elemento(long id, String nombre){
        this.nombre=nombre;
        this.id=id;
    }

    public Elemento(long idContacto, String nombre, String direccion, String pagina, String email){
        this.nombre = nombre;
        this.id = idContacto;
        this.direccion=direccion;
        this.pagina=pagina;
        this.email=email;
    }

    public Elemento(long id, String nombre, String telefono, String rFoto, String direccion, String email, String pagina){
        this.nombre = nombre;
        this.id = id;
        this.telefono = telefono;
        this.rFoto = rFoto;
        this.direccion=direccion;
        this.pagina=pagina;
        this.email=email;
    }
    public String getNombre(){return nombre;}
    public long getId(){return id;}
    public String getTelefono(){return telefono; }
    public String getFoto(){return rFoto;}
    public String getDireccion(){return direccion; }
    public String getEmail(){return email; }
    public String getPagina(){return pagina; }
    public void setId(long id){this.id = id;}
    public void setNombre(String nombre){this.nombre = nombre;}
    public void setTelefono(String telefono){this.telefono = telefono;}
    public void setImagen(String url){this.rFoto = url; }
    public void setDireccion(String direccion){this.direccion=direccion;}
    public void setEmail(String email){this.email=email;}
    public void setPagina(String pagina){this.pagina=pagina;}
}
