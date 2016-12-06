package com.example.deivi.agenda_contactos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deivi on 02/12/2016.
 */

public class BDContactos extends SQLiteOpenHelper implements Serializable{

    private int tamanioTablac;
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
            "  nomFichero VARCHAR(50),\n" +
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

    public long borrarContacto(int index){
        tamanioTablac=(int)returnId()-1;
        long nreg_afectados = -1;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            nreg_afectados = db.delete(NOMBRE_TABLAC, "idContacto=" + index, null);
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
            valores.put("direccion", e.getDireccion());
            valores.put("email", e.getEmail());
            valores.put("webBlog", e.getPagina());
            nreg_afectados = db.insert(NOMBRE_TABLAC, null, valores);
            if(nreg_afectados!=-1)
                tamanioTablac++;

            /*
            valores.clear();

            valores.put("idTelefonos", e.getGeneraIdTelefono());
            valores.put("telefono", e.getTelefono());
            valores.put("contactos_idContacto", e.getId());
            db.insert(NOMBRE_TABLAT, null, valores);

            valores.clear();

            valores.put("idFoto", e.getGeneraIdTelefono());
            valores.put("nomFichero", e.getFoto());
            valores.put("observFoto", e.getFoto());
            valores.put("contactos_idContacto", e.getId());
            db.insert(NOMBRE_TABLAF, null, valores);
            */
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

    public long insertarTelefono(Elemento e) {
        long nreg_afectados = -1;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();

            valores.put("idTelefonos", e.getGeneraIdTelefono());
            valores.put("telefono", e.getTelefono());
            valores.put("contactos_idContacto", e.getId());
            nreg_afectados = db.insert("Telefonos", null, valores);
        }
        db.close();
        return nreg_afectados;
    }

    public long insertarFoto(Elemento e) {
        long nreg_afectados = -1;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();

            valores.put("idFoto", e.getGeneraIdFoto());
            valores.put("nomFichero", e.getTelefono());
            valores.put("observFoto", e.getFoto());
            valores.put("contactos_idContacto", e.getId());
            nreg_afectados = db.insert("Fotos", null, valores);



        }
        db.close();
        return nreg_afectados;
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
