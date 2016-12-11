package com.example.deivi.agenda_contactos;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by Deivi on 01/12/2016.
 */

public class EditaContacto extends AppCompatActivity {

    EditText etNombre, etTelefono, etDireccion, etEmail, etPagina;
    Button btTelefono, btFoto, btBaja, btModificar, btAcciones;
    ImageView imagenV;
    BDContactos bd;
    long idContacto;
    Elemento el;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editacontacto);

        bd = new BDContactos(this);
        etNombre = (EditText) findViewById(R.id.etNombreE);
        etTelefono = (EditText) findViewById(R.id.etTelefonoE);
        etDireccion = (EditText) findViewById(R.id.etDireccionE);
        etEmail = (EditText) findViewById(R.id.etEmailE);
        etPagina = (EditText) findViewById(R.id.etPaginaE);
        btTelefono = (Button) findViewById(R.id.btAddTelefonoE);
        btTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gestionTelefonos();
            }
        });
        btFoto = (Button) findViewById(R.id.btAddFotoE);
        btFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gestionFotos();
            }
        });
        btBaja = (Button) findViewById(R.id.btBaja);
        btBaja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmacionBorrar();
            }
        });
        btModificar = (Button) findViewById(R.id.btModificar);
        btModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarContacto();
            }
        });
        btAcciones = (Button) findViewById(R.id.btAcciones);
        btAcciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionesContacto();
            }
        });
        imagenV = (ImageView) findViewById(R.id.imagenEdita);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            Elemento contacto = (Elemento) extra.get("contacto");
            el = contacto;
            idContacto = el.getId();
            cargaDatos();
        }

        //comprobarSD();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        el = bd.getContacto((int)idContacto);
        cargaDatos();
        Toast.makeText(getApplicationContext(), "onRestart", Toast.LENGTH_SHORT).show();
    }

    /*
    @Override
    public void onBackPressed(){
        Intent data = new Intent();
        data.putExtra("contacto", contacto);
        setResult(RESULT_OK, data);
        finish();
    }
    */

    public void bajaContacto() {

        long borrado = bd.borrarYordenar((int) el.getId());
        if (borrado > 0)
            Toast.makeText(this, "Contacto borrado", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Error al borrar el contacto", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void modificarContacto() {
        boolean modificado = bd.modificarContacto(new Elemento(el.getId(), etNombre.getText().toString(), etDireccion.getText().toString(), etPagina.getText().toString(), etEmail.getText().toString()));
        if (modificado)
            Toast.makeText(this, "Se ha modificado el contacto", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "No se ha modificado el contacto", Toast.LENGTH_SHORT).show();
    }

    public void cargaDatos() {

        etNombre.setText(el.getNombre());
        etTelefono.setText(el.getTelefono(bd));
        etDireccion.setText(el.getDireccion());
        etEmail.setText(el.getEmail());
        etPagina.setText(el.getPagina());
        imagenV.setImageBitmap(cargaFoto(el.getFoto(bd)));
        /*File imgFile = new File(contacto.getFoto());
        if(imgFile.exists()){
            imagen.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
            imagen.setAdjustViewBounds(true);
        }*/
    }

    public void confirmacionBorrar() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("¿Borrar contacto?");
        dialogo.setMessage("¿Deseas borrar el contacto de la lista? ");
        //alertDialogBu.setIcon(R.drawable.ic_launcher);

        dialogo.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                bajaContacto();
            }
        });
        dialogo.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "No se ha borrado", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = dialogo.create();
        alertDialog.show();
    }

    public void accionesContacto() {
        AlertDialog.Builder alertDialogBu = new AlertDialog.Builder(this);
        alertDialogBu.setTitle("¿Que deseas hacer?");
// alertDialogBu.setMessage("Mensaje del AlertDialog ");
// NO PUEDE HABER AL MISMO TIEMPO MENSAJE Y OPCIONES
        //alertDialogBu.setIcon(R.drawable.ic_launcher);
        CharSequence opciones[] = {"Llamar por telefono", "Mandar un email", "Visitar pagina web", "Tomar una foto"};
        alertDialogBu.setItems(opciones, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        llamadaCall();
                        break;
                    case 1:
                        email();
                        break;
                    case 2:
                        paginaWeb();
                        break;
                    case 3:
                        gestionFotos();
                        break;
                }
            }
        });
        AlertDialog alertDialog = alertDialogBu.create();
        alertDialog.show();
    }

    public void llamadaCall() {
        String telefono = "tel:"+el.getTelefono(bd);
        if(telefono.equals("")){
            Toast.makeText(getApplicationContext(), "No se ha encontrado un telefono", Toast.LENGTH_SHORT).show();
        }else{
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse(telefono));
            startActivity(i);
        }
    }

    public void email() {
        String email = el.getEmail();
        if(email.equals("")){
            Toast.makeText(getApplicationContext(), "No se ha encontrado un email", Toast.LENGTH_SHORT).show();
        }else {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Asunto del Correo");
            i.putExtra(Intent.EXTRA_TEXT, "Texto por Defecto del Correo");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            startActivity(i);
        }
    }

    public void paginaWeb() {
        String pagina = el.getPagina();
        pagina="http://www.google.com";
        if(pagina.equals("")){
            Toast.makeText(getApplicationContext(), "No se ha encontrado una pagina", Toast.LENGTH_SHORT).show();
        }else {
            Intent i = new Intent(Intent.ACTION_WEB_SEARCH);
            i.setData(Uri.parse(pagina));
            startActivity(i);
        }
    }
/*
    private static final int CAMARA = 1;
    public void foto() {
        Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(i, CAMARA);
    }*/

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //imagen = (ImageView) findViewById(R.id.imagenEdita);
        if (requestCode == CAMARA && resultCode == RESULT_OK && data != null) {
            Bitmap imagen = (Bitmap) data.getExtras().get("data");
            String ruta = guardaFoto(el, imagen);
            imagenV.setImageBitmap(cargaFoto(ruta));
            //guardaFoto(el, imagen);
        }
    }*/

    /*public boolean guardaTelefono(String telefono){
        return bd.guardaTelefono(telefono, (int)el.getId());
    }*/

    public void gestionTelefonos(){
        Intent i = new Intent(getApplicationContext(), AddTelefono.class);
        i.putExtra("idContacto", el.getId());
        startActivity(i);
    }

    public void gestionFotos(){
        Intent i = new Intent(getApplicationContext(), AddFoto.class);
        i.putExtra("idContacto", el.getId());
        startActivity(i);
    }

    /*public String guardaFoto(Elemento el, Bitmap bitmapImage){
        //if (sdDisponible && sdAccesoEscritura) {
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            String nombreF = el.getNombre()+ el.getId()+bd.countFotosContacto((int)el.getId());
            File f = new File(directory, nombreF);

            FileOutputStream fos = null;
            try {

                fos = new FileOutputStream(f);
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String string = "Directory: "+directory+" file: "+nombreF+" f.absolutePath:"+f.getAbsolutePath();
            Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
            System.out.println(string);
            return f.getAbsolutePath();
        //}

    }*/

    public static Bitmap cargaFoto(String path){
        try {
            File f=new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        } catch (FileNotFoundException e){e.printStackTrace();}

        return null;
    }
}
