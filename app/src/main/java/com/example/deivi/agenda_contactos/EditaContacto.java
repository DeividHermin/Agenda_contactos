package com.example.deivi.agenda_contactos;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Deivi on 01/12/2016.
 */

public class EditaContacto extends AppCompatActivity {

    EditText etNombre, etTelefono, etDireccion, etEmail, etPagina;
    Button btTelefono, btFoto, btBaja, btModificar, btAcciones;
    ImageView imagen;
    BDContactos bd;
    Elemento el;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editacontacto);

        bd = new BDContactos(this);
        etNombre = (EditText)findViewById(R.id.etNombreE);
        etTelefono = (EditText)findViewById(R.id.etTelefonoE);
        etDireccion = (EditText)findViewById(R.id.etDireccionE);
        etEmail = (EditText)findViewById(R.id.etEmailE);
        etPagina = (EditText)findViewById(R.id.etPaginaE);
        btTelefono = (Button)findViewById(R.id.btAddTelefonoE);
        btFoto = (Button)findViewById(R.id.btAddFotoE);
        btBaja = (Button)findViewById(R.id.btBaja);
        btBaja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bajaContacto();
            }
        });
        btModificar = (Button)findViewById(R.id.btModificar);
        btModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarContacto();
            }
        });
        btAcciones = (Button)findViewById(R.id.btAcciones);
        btAcciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               accionesContacto();
            }
        });
        imagen = (ImageView)findViewById(R.id.imagenEdita);

        Bundle extra = getIntent().getExtras();
        if(extra != null){
            Elemento contacto = (Elemento)extra.get("contacto");
            cargaDatos(contacto);
        }
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

    public void bajaContacto(){

        //showConfirmationDialog();
        //if(delete)
            long borrado = bd.borrarYordenar((int)el.getId());
            if (borrado>0)
                Toast.makeText(this, "Contacto borrado", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "No se ha borrado el contacto", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void modificarContacto(){
        boolean modificado = bd.modificarContacto(new Elemento(el.getId(), etNombre.getText().toString(), etDireccion.getText().toString(), etPagina.getText().toString(), etEmail.getText().toString()));
        if (modificado)
            Toast.makeText(this, "Se ha modificado el contacto", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "No se ha modificado el contacto", Toast.LENGTH_SHORT).show();
    }

    public void accionesContacto(){
        /*
            Intent it = new Intent(this, AccionesContacto.class);
            it.putExtra("contacto", contacto);
            startActivity(it);
         */
    }

    public void cargaDatos(Elemento contacto){
        el=contacto;
        etNombre.setText(contacto.getNombre());
        etTelefono.setText(contacto.getTelefono());
        etDireccion.setText(contacto.getDireccion());
        etEmail.setText(contacto.getEmail());
        etPagina.setText(contacto.getPagina());

        /*File imgFile = new File(contacto.getFoto());
        if(imgFile.exists()){
            imagen.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
            imagen.setAdjustViewBounds(true);
        }*/
    }
}
