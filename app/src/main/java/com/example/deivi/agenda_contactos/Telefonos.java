package com.example.deivi.agenda_contactos;

/**
 * Created by Deivi on 08/12/2016.
 */

public class Telefonos {
    public int idTelefonos, contactos_idContacto;
    public String telefono;

    public Telefonos(int idTel, String telefono, int idC){
        idTelefonos=idTel;
        this.telefono=telefono;
        contactos_idContacto=idC;
    }

    public void setIdTelefonos(int idTel){ idTelefonos=idTel; }
    public void setTelefono(String telef){ telefono=telef; }
    public void setContactos_idContacto(int idC){ contactos_idContacto=idC; }

    public int getIdTelefonos(){ return idTelefonos; }
    public String getTelefono(){ return telefono; }
    public int getContactos_idContacto(){ return contactos_idContacto; }

}
