package com.example.deivi.agenda_contactos;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Deivi on 01/12/2016.
 */

public class EditaContacto extends AppCompatActivity {

    EditText etNombre, etTelefono, etDireccion, etEmail, etPagina, etFoto;
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
        etFoto = (EditText) findViewById(R.id.etFotoE);
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
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        el = bd.getContacto((int)idContacto);
        cargaDatos();
    }


    public void bajaContacto() {
        long borrado = bd.borrarYordenar((int) el.getId());
        if (borrado > 0)
            Toast.makeText(this, R.string.contactoBorrado, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
        finish();
    }

    public void modificarContacto() {
        boolean error1=true, error2=true;

        if(bd.primerTelefono((int)idContacto).equals(""))
            error1=false;
        if(bd.primeraFoto((int)idContacto).equals(""))
            error2=false;

        if(error1 && error2){
            boolean modificado = bd.modificarContacto(new Elemento(el.getId(), etNombre.getText().toString(), etDireccion.getText().toString(), etPagina.getText().toString(), etEmail.getText().toString()));
            if (modificado)
                Toast.makeText(this, R.string.contactoModificado, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
        }else{
            if(!error1)
                Toast.makeText(getApplicationContext(), R.string.faltaTelefono, Toast.LENGTH_SHORT).show();
            if(!error2)
                Toast.makeText(getApplicationContext(), R.string.faltaFoto, Toast.LENGTH_SHORT).show();
        }
    }

    public void cargaDatos() {

        etNombre.setText(el.getNombre());
        etTelefono.setText(el.getTelefono(bd));
        etDireccion.setText(el.getDireccion());
        etEmail.setText(el.getEmail());
        etPagina.setText(el.getPagina());
        etFoto.setText(el.getObservacionFoto(bd));
        imagenV.setImageBitmap(cargaFoto(el.getFoto(bd)));
    }

    public void confirmacionBorrar() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle(R.string.borrarContacto);
        //dialogo.setMessage("");

        dialogo.setPositiveButton(R.string.btBorrar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                bajaContacto();
            }
        });
        dialogo.setNeutralButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), R.string.noSeHaBorrado, Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = dialogo.create();
        alertDialog.show();
    }

    public void accionesContacto() {
        AlertDialog.Builder alertDialogBu = new AlertDialog.Builder(this);
        alertDialogBu.setTitle(R.string.queDeseasHacer);
        CharSequence opciones[] = {getResources().getString(R.string.llamarPorTelefono), getResources().getString(R.string.mandarUnEmail), getResources().getString(R.string.visitarPagina), getResources().getString(R.string.tomarUnaFoto)};
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
            Toast.makeText(getApplicationContext(), R.string.noHayTelefono, Toast.LENGTH_SHORT).show();
        }else{
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse(telefono));
            startActivity(i);
        }
    }

    public void email() {
        String email = el.getEmail();
        if(email.equals("")){
            Toast.makeText(getApplicationContext(), R.string.noHayEmail, Toast.LENGTH_SHORT).show();
        }else {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, R.string.asuntoDelCorreo);
            i.putExtra(Intent.EXTRA_TEXT, R.string.textoCorreo);
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            startActivity(i);
        }
    }

    public void paginaWeb() {
        String pagina = el.getPagina();
        //pagina="http://www.google.com";
        if(pagina.equals("")){
            Toast.makeText(getApplicationContext(), R.string.noHayPagina, Toast.LENGTH_SHORT).show();
        }else {
            Intent i = new Intent(Intent.ACTION_WEB_SEARCH);
            i.setData(Uri.parse(pagina));
            try{
                startActivity(i);
            }catch(ActivityNotFoundException e){
                Toast.makeText(getApplicationContext(), R.string.noHayPagina, Toast.LENGTH_SHORT).show();
            }

        }
    }

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

    public static Bitmap cargaFoto(String path){
        try {
            File f=new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        } catch (FileNotFoundException e){e.printStackTrace();}

        return null;
    }
}
