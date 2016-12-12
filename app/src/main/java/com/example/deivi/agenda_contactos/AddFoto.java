package com.example.deivi.agenda_contactos;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deivi on 08/12/2016.
 */

public class AddFoto extends AppCompatActivity {

    ListView lista;
    static AdaptadorFoto a;
    static ArrayList<Fotos> arrayListFoto = new ArrayList();
    BDContactos bd;
    int idContacto;
    Bitmap imagen;
    boolean continuar;
    //Elemento contacto;

    Button btAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfoto);
        bd = new BDContactos(this);

        lista = (ListView)findViewById(R.id.listaFoto);
        btAdd = (Button)findViewById(R.id.btAñadirFoto);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaAñadir();
            }
        });
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            idContacto = (int)(long)extra.get("idContacto");
        }

        actualizaLista();
        a = null;
        a = new AdaptadorFoto(this,arrayListFoto);
        a.notifyDataSetChanged();
        lista.setAdapter(a);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapter, View view, int position, long arg)
            {
                Fotos foto = (Fotos) lista.getAdapter().getItem(position);
                diaPulsado(foto);
            }
        });
    }

    @Override
    protected void onRestart () {
        super.onRestart();
        //Toast.makeText(this, "Lista recargada", Toast.LENGTH_SHORT).show();

        actualizaLista();
        a = null;
        a = new AdaptadorFoto(this,arrayListFoto);
        a.notifyDataSetChanged();
        lista.setAdapter(a);
    }

    public void actualizaLista() {
        arrayListFoto.clear();
        if (bd.countFotosContacto(idContacto) > 0)
            for (int i = 0; i < bd.countFotosContacto(idContacto); i++) {
                List<Fotos> lista = bd.returnFotos(idContacto);
                arrayListFoto.add(lista.get(i));
                System.out.println("actualizaLista ID: "+lista.get(i).getIdFoto());
            }
        else
            System.out.println("No hay telefonos");
    }



    public void borrarFoto(int idFoto){
        bd.borrarYordenarFoto(idFoto);
    }

    public void llamarTelefono(String telefono){

    }

    public void diaPulsado(final Fotos foto) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("");
        alert.setTitle("¿Deseas borrar la foto?");

        alert.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                borrarFoto(foto.getIdFoto());
                //actualizaTelContacto();
                onRestart();
                Toast.makeText(getApplicationContext(), "Borrado", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }
    public void diaAñadir () {
        imagen = null;
        obtenerFoto();

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("");
        alert.setTitle("Nueva foto");
        TextView tv = new TextView(getApplicationContext());
        tv.setText("Observacion");
        final EditText edittext = new EditText(getApplicationContext());
        alert.setView(tv);
        alert.setView(edittext);

        alert.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String observacion= edittext.getText().toString();
                String nombre;

                if(bd.existeContacto(idContacto))
                    nombre=bd.getContacto(idContacto).getNombre();
                else
                    nombre="newC";
                guardaFotoMovil(nombre, observacion, imagen);
                onRestart();
                //Toast.makeText(getApplicationContext(), "Foto: "+foto, Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    public void obtenerFoto() {
        //mirar las preferencias para abrir galeria o camara
        Intent i;

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean opcion = sp.getBoolean("cbCamara", true);
        Toast.makeText(getApplicationContext(), ""+opcion, Toast.LENGTH_SHORT).show();

        if(opcion) {
            i = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(i, 1);
        }else {
            i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, 2);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //imagen = (ImageView) findViewById(R.id.imagenEdita);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imagen = (Bitmap) data.getExtras().get("data");
        }

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imagen = (Bitmap) data.getExtras().get("data");
        }

        if(resultCode==RESULT_OK)
            continuar=true;
        else
            continuar=false;

    }

    public String guardaFotoMovil(String nombre, String observacion, Bitmap bitmapImage){
        //if (sdDisponible && sdAccesoEscritura) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        String nombreF = nombre+ idContacto+bd.countFotosContacto(idContacto);
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
        añadirFotoBD(f.getAbsolutePath(), observacion);
        return f.getAbsolutePath();
        //}

    }

    public void añadirFotoBD(String foto, String observacion){
        if(bd.guardaFoto(foto, observacion, idContacto))
            Toast.makeText(getApplicationContext(), "Foto "+foto+" guardado", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Error guardando el telefono", Toast.LENGTH_SHORT).show();
    }
}
