package com.example.deivi.agenda_contactos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Deivi on 02/12/2016.
 */

public class BDContactos extends SQLiteOpenHelper implements Serializable{

    private int tamanioTablac, tamanioTablaf;
    private static Elemento elemento;
    private static final int VERSION_BASEDATOS = 1;
    private static final String NOMBRE_BASEDATOS = "BDContactos.bd";
    private static final String TABLA_CONTACTOS = "CREATE TABLE Contactos(\n" +
            "  idContacto int PRIMARY KEY,\n" +
            "  nombre VARCHAR(50),\n" +
            "  direccion VARCHAR(50),\n" +
            "  webBlog VARCHAR(100),\n" +
            "  email VARCHAR(100)\n" +
            ")";
    private static final String TABLA_TELEFONOS = "CREATE TABLE Telefonos(\n" +
            "  idTelefonos int PRIMARY KEY,\n" +
            "  telefono VARCHAR(45),\n" +
            "  contactos_idContacto int\n" +
            ")";
    private static final String TABLA_FOTOS = "CREATE TABLE Fotos(\n" +
            "  idFoto int PRIMARY KEY,\n" +
            "  nomFichero VARCHAR(100),\n" +
            "  observFoto VARCHAR(255),\n" +
            "  contactos_idContacto int\n" +
            ")";
    private static final String NOMBRE_TABLAC = "Contactos";
    private static final String NOMBRE_TABLAT = "Telefonos";
    private static final String NOMBRE_TABLAF = "Fotos";

    public BDContactos(Context context){
        super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLA_CONTACTOS);
        db.execSQL(TABLA_TELEFONOS);
        db.execSQL(TABLA_FOTOS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLAC);
        db.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLAT);
        db.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLAF);
        onCreate(db);
    }


    //---------------------------------------------------TELEFONOS----------------------------------------------------------------------

    public long returnIdTelefono(){
        long count=0;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery("SELECT COUNT(*) FROM "+NOMBRE_TABLAT, null);
            c.moveToFirst();
            count=Long.parseLong(c.getString(0));
        }
        db.close();
        return count+1;
    }

    public long countTelefonosContacto(int idContacto){
        long count=0;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery("SELECT COUNT(*) FROM "+NOMBRE_TABLAT+" WHERE contactos_idContacto="+idContacto, null);
            c.moveToFirst();
            count=Long.parseLong(c.getString(0));
        }
        db.close();
        return count;
    }

    public boolean guardaTelefono(String telefono, int idContacto){
        long idTelefono = returnIdTelefono();
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            db.execSQL("INSERT INTO "+NOMBRE_TABLAT+" VALUES ("+idTelefono+", '"+telefono+"', "+idContacto+")");
            System.out.println("Se guarda ID:"+idTelefono);
            return true;
        }
        db.close();
        return false;
    }

    public String primerTelefono(int idContacto){
        String telefono = "";
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery("SELECT telefono FROM "+NOMBRE_TABLAT+" WHERE contactos_idContacto="+idContacto, null);
            if(c.moveToNext())
                telefono=c.getString(0);
        }
        db.close();
        return telefono;
    }

    public long borrarYordenarTelefono(int index){
        tamanioTablaf=(int)returnIdTelefono()-1;

        long nreg_afectados = -1;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            nreg_afectados = db.delete(NOMBRE_TABLAT, "idTelefonos=" + index, null);
            //System.out.println("borrado idTelefonos=" + index);
            for(int i=index; i<tamanioTablaf; i++) {
                db.execSQL("UPDATE " + NOMBRE_TABLAT + " SET idTelefonos=" + i + " WHERE idTelefonos=" + (i + 1));
                System.out.println("UPDATE " + NOMBRE_TABLAT + " SET idTelefonos=" + i + " WHERE idTelefonos=" + (i + 1));
            }
        }

        if(nreg_afectados>0)
            tamanioTablaf=-(int)nreg_afectados;
        db.close();
        return nreg_afectados;
    }

    public long borrarTelContacto(int idContacto){
        long nreg_afectados = -1;
        int borrar[]=null;
        int contador=0;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            nreg_afectados=0;
            Cursor c = db.rawQuery("SELECT idTelefonos FROM "+NOMBRE_TABLAT+" WHERE contactos_idContacto="+idContacto, null);
            System.out.println("SELECT idTelefonos FROM "+NOMBRE_TABLAT+" WHERE contactos_idContacto="+idContacto);
            borrar = new int[c.getColumnCount()+1];
            for(int i=0; c.moveToNext(); i++){
                borrar[i]=c.getInt(0);
                contador++;
                System.out.println("Se borrara ID:"+c.getInt(0));
                nreg_afectados++;
            }
        }
        db.close();

        if(contador>0)
            for (int i=0; i<contador; i++) {
                System.out.println("borrarYordenarTelefono(["+borrar[i]+"])");
                borrarYordenarTelefono(borrar[i]);
            }

        return nreg_afectados;
    }

    public List<Telefonos> returnTelefonos(int idContacto){
        List<Telefonos> telefonos = new ArrayList<Telefonos>();
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery("SELECT idTelefonos, telefono FROM "+NOMBRE_TABLAT+" WHERE contactos_idContacto="+idContacto+" ORDER BY idTelefonos ASC", null);
            //c.moveToFirst();
            //if(!c.isNull(c.getPosition())){
           //     telefonos.add(new Telefonos(c.getInt(0), c.getString(1), idContacto));
                while(c.moveToNext()){
                    telefonos.add(new Telefonos(c.getInt(0), c.getString(1), idContacto));
                }
          //  }
        }
        db.close();
        return telefonos;
    }

    //---------------------------------------------------FOTOS----------------------------------------------------------------------

    public long returnIdFoto(){
        long count=0;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery("SELECT COUNT(*) FROM "+NOMBRE_TABLAF, null);
            c.moveToFirst();
            count=Long.parseLong(c.getString(0));
        }
        db.close();
        return count+1;
    }

    public long countFotosContacto(int idContacto){
        long count=0;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery("SELECT COUNT(*) FROM "+NOMBRE_TABLAF+" WHERE contactos_idContacto="+idContacto, null);
            c.moveToFirst();
            count=Long.parseLong(c.getString(0));
        }
        db.close();
        return count;
    }

    public boolean guardaFoto(String nombre, String observacion, int idContacto){
        long idFoto = returnIdFoto();
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            db.execSQL("INSERT INTO "+NOMBRE_TABLAF+" VALUES ("+idFoto+", '"+nombre+"', '"+observacion+"', "+idContacto+")");
            System.out.println("Se guarda ID:"+idFoto);
            return true;
        }
        db.close();
        return false;
    }

    public String primeraFoto(int idContacto){
        String foto = "";
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery("SELECT nomFichero FROM "+NOMBRE_TABLAF+" WHERE contactos_idContacto="+idContacto, null);
            if(c.moveToNext())
                foto=c.getString(0);
        }
        db.close();
        return foto;
    }

    public long borrarYordenarFoto(int index){
        tamanioTablaf=(int)returnIdFoto()-1;

        long nreg_afectados = -1;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            nreg_afectados = db.delete(NOMBRE_TABLAF, "idFoto=" + index, null);
            //System.out.println("borrado idTelefonos=" + index);
            for(int i=index; i<tamanioTablaf; i++) {
                db.execSQL("UPDATE " + NOMBRE_TABLAF + " SET idFoto=" + i + " WHERE idFoto=" + (i + 1));
                System.out.println("UPDATE " + NOMBRE_TABLAF + " SET idFoto=" + i + " WHERE idFoto=" + (i + 1));
            }
        }

        if(nreg_afectados>0)
            tamanioTablaf=-(int)nreg_afectados;
        db.close();
        return nreg_afectados;
    }

    public long borrarFotosContacto(int idContacto){
        long nreg_afectados = -1;
        int borrar[]=null;
        int contador=0;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            nreg_afectados=0;
            Cursor c = db.rawQuery("SELECT idFoto FROM "+NOMBRE_TABLAF+" WHERE contactos_idContacto="+idContacto, null);
            System.out.println("SELECT idFoto FROM "+NOMBRE_TABLAF+" WHERE contactos_idContacto="+idContacto);
            borrar = new int[c.getColumnCount()+1];
            for(int i=0; c.moveToNext(); i++){
                borrar[i]=c.getInt(0);
                contador++;
                System.out.println("Se borrara ID:"+c.getInt(0));
                nreg_afectados++;
            }
        }
        db.close();

        if(contador>0)
            for (int i=0; i<contador; i++) {
                System.out.println("borrarYordenarFoto(["+borrar[i]+"])");
                borrarYordenarFoto(borrar[i]);
            }

        return nreg_afectados;
    }

    public List<Fotos> returnFotos(int idContacto){
        List<Fotos> fotos = new ArrayList<Fotos>();
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery("SELECT idFoto, nomFichero, observFoto FROM "+NOMBRE_TABLAF+" WHERE contactos_idContacto="+idContacto+" ORDER BY idFoto ASC", null);
            while(c.moveToNext()){
                fotos.add(new Fotos(c.getInt(0), c.getString(1), c.getString(2), idContacto));
            }
        }
        db.close();
        return fotos;
    }


    //-------------------------------------------------CONTACTOS------------------------------------------------------------------------
    public long returnId(){
        long count=0;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery("SELECT COUNT(*) FROM "+NOMBRE_TABLAC, null);
            c.moveToFirst();
            count=Long.parseLong(c.getString(0));
        }
        db.close();
        return count+1;
    }

    public boolean existeContacto(int index){
        boolean existe=false;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery("SELECT * FROM "+NOMBRE_TABLAC+" WHERE idContacto="+index, null);
            if(c.moveToNext())
                existe=true;
        }
        db.close();

        return existe;
    }
    public long borrarContacto(int index){
        tamanioTablac=(int)returnId()-1;
        long nreg_afectados = -1;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            nreg_afectados = db.delete(NOMBRE_TABLAC, "idContacto=" + index, null);
            borrarTelContacto(index);
            borrarFotosContacto(index);
            //db.delete(NOMBRE_TABLAT, "contactos_idContacto=" + index, null);
            //db.delete(NOMBRE_TABLAF, "contactos_idContacto=" + index, null);
        }

        if(nreg_afectados>0)
            tamanioTablac=tamanioTablac-(int)nreg_afectados;
        db.close();
        return nreg_afectados;
    }

    public long borrarYordenar(int index){
        tamanioTablac=(int)returnId()-1;
        //returnId devuelve el count de la tabla contactos + 1, asi que aprovecho el metodo que ya esta hecho
        //no uses el returnId despues de abrir la db=getWritableDatabase, porque te dara error de que
        //esta intentando abrirla cuando ya esta abierta
        //tamanioTablac es un int que uso para no tener que usar el returnId en la condicion del for y que salte que la db ya esta abierta
        //tamanioTablac++ cuando añadas contactos, tamanioTablac-- cuando borres contactos
        //aun asi no deberia haber problema si lo actualias con returnId-1 antes de ordenar

        long nreg_afectados = -1;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            nreg_afectados = db.delete(NOMBRE_TABLAC, "idContacto=" + index, null);
            for(int i=index; i<tamanioTablac; i++) {
                db.execSQL("UPDATE " + NOMBRE_TABLAC + " SET idContacto=" + i + " WHERE idContacto=" + (i + 1));
            }
            //db.delete(NOMBRE_TABLAT, "contactos_idContacto=" + index, null);
            //db.delete(NOMBRE_TABLAF, "contactos_idContacto=" + index, null);
        }

        if(nreg_afectados>0)
            tamanioTablac=-(int)nreg_afectados;
        db.close();
        return nreg_afectados;
    }

    public long insertarContacto(Elemento e) {
        long nreg_afectados = -1;
        /* Abrimos la BD de Escritura */
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
        /* en este metodo utilizaremos ContentValues */
            ContentValues valores = new ContentValues();
            valores.put("nombre", e.getNombre());
            valores.put("idContacto", e.getId());
            System.out.println("INSERTADO CONTACTO "+e.getId());
            valores.put("direccion", e.getDireccion());
            valores.put("email", e.getEmail());
            valores.put("webBlog", e.getPagina());
            nreg_afectados = db.insert(NOMBRE_TABLAC, null, valores);
            if(nreg_afectados!=-1)
                tamanioTablac++;
        }
        db.close();
        return nreg_afectados;
    }

    public boolean modificarContacto(Elemento c){
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            db.execSQL("UPDATE " + NOMBRE_TABLAC + " SET nombre='" + c.getNombre() + "' WHERE idContacto=" + c.getId());
            db.execSQL("UPDATE " + NOMBRE_TABLAC + " SET direccion='" + c.getDireccion() + "' WHERE idContacto=" + c.getId());
            db.execSQL("UPDATE " + NOMBRE_TABLAC + " SET webBlog='" + c.getPagina() + "' WHERE idContacto=" + c.getId());
            db.execSQL("UPDATE " + NOMBRE_TABLAC + " SET email='" + c.getEmail() + "' WHERE idContacto=" + c.getId());
            return true;
        }
        return false;
    }

    public Elemento getContacto(int id){
        Elemento el = null;
        SQLiteDatabase db = getReadableDatabase();
        if(db!=null){
            Cursor c = db.rawQuery("SELECT * FROM "+NOMBRE_TABLAC+" WHERE idContacto="+id, null);
            if(c.moveToNext())
                el = new Elemento(id, c.getString(1), primerTelefono(id), "", c.getString(2), c.getString(4), c.getString(3));
        }
        return el;
    }
    public List<Elemento> listado() {
        SQLiteDatabase db = getReadableDatabase();
        List<Elemento> lista_contactos = new ArrayList<Elemento>();
        if (db != null) {
            String[] campos = {"idContacto", "nombre", "direccion", "webBlog", "email"};
        /* Como queremos devolver todos los registros el tercer parámetro del query ( String selection ) es null */

            Cursor c = db.query(NOMBRE_TABLAC, campos, null, null, null, null, "nombre ASC", null);
            if (c.moveToFirst()) {
                do {
                    elemento = new Elemento(c.getLong(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4));
                    lista_contactos.add(elemento);
                } while (c.moveToNext());
            }
            c.close();
        }
        db.close();
        return lista_contactos;
    }
}
