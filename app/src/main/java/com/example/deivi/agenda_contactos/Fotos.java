package com.example.deivi.agenda_contactos;

/**
 * Created by Deivi on 08/12/2016.
 */

public class Fotos {
    private int idFoto, contactos_idContacto;
    private String nomFichero, observFoto;

    public Fotos(int idFoto, String nomFichero, String observFoto, int idC){
        this.idFoto=idFoto;
        this.nomFichero=nomFichero;
        this.observFoto=observFoto;
        contactos_idContacto=idC;
    }

    public void setIdFoto(int idFoto){ this.idFoto=idFoto; }
    public void setNomFichero(String nomFichero){ this.nomFichero=nomFichero; }
    public void setObservFoto(String observFoto){ this.observFoto=observFoto; }
    //public void setRuta(String ruta){ this.ruta=ruta; }
    public void setContactos_idContacto(int idC){ contactos_idContacto=idC; }

    public int getIdFoto(){ return idFoto; }
    public String getNomFichero(){ return nomFichero; }
    public String getRutaFichero(){ return nomFichero; }
    public String getObservFoto(){ return observFoto; }
    //public String getRuta(){ return ruta; }
    public int getContactos_idContacto(){ return contactos_idContacto; }

}
