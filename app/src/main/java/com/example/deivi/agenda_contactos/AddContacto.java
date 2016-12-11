package com.example.deivi.agenda_contactos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Deivi on 30/11/2016.
 */

public class AddContacto extends AppCompatActivity {

    EditText etNombre, etTelefono, etDireccion, etEmail, etPagina;
    Button btTelefono, btFoto, btAlta;
    ImageView imagen;
    BDContactos bd;
    //boolean creado;
    //int idContacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcontacto);

        bd=new BDContactos(this);
        etNombre = (EditText)findViewById(R.id.etNombreA);
        etTelefono = (EditText)findViewById(R.id.etTelefonoA);
        etDireccion = (EditText)findViewById(R.id.etDireccionA);
        etEmail = (EditText)findViewById(R.id.etEmailA);
        etPagina = (EditText)findViewById(R.id.etPaginaA);
        btTelefono = (Button)findViewById(R.id.btAddTelefonoA);
        btTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaAddTelefono();
            }
        });
        btFoto = (Button)findViewById(R.id.btAddFotoA);
        btAlta = (Button)findViewById(R.id.btAlta);
        btAlta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alta();
            }
        });
        imagen = (ImageView)findViewById(R.id.imagenAdd);
        //idContacto=(int)bd.returnId();
        /*if(!creado) {
            bd.insertarContacto(new Elemento(idContacto, "", "", "", "", "", ""));
            creado=true;
            Toast.makeText(getApplicationContext(), "insertado", Toast.LENGTH_SHORT).show();
        }*/

    }

    @Override
    public void onBackPressed(){
        bd.borrarTelContacto((int)(bd.returnId()));
        System.out.println("BORRANDO TELEFONOS DEL CONTACTO "+(bd.returnId()));
        super.onBackPressed();
    }
/*
    @Override
    protected void onDestroy(){
        super.onDestroy();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean("orientacion", creado);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        creado = savedInstanceState.getBoolean("orientacion");
    }*/

    public void alta(){
        String nombre =     etNombre.getText().toString();
        String telefono =   etTelefono.getText().toString();
        String direccion =  etDireccion.getText().toString();
        String email =      etEmail.getText().toString();
        String pagina =     etPagina.getText().toString();

        //bd.modificarContacto(new Elemento(idContacto, nombre, telefono, "", direccion, email, pagina));
        bd.insertarContacto(new Elemento(bd.returnId(), nombre, telefono, "", direccion, email, pagina));
        Toast.makeText(getApplicationContext(), "Contacto añadido", Toast.LENGTH_SHORT).show();

        etNombre.setText("");
        etTelefono.setText("");
        etDireccion.setText("");
        etEmail.setText("");
        etPagina.setText("");

        finish();
    }

    public void diaAddTelefono(){
        Intent i = new Intent(getApplicationContext(), AddTelefono.class);
        i.putExtra("idContacto", bd.returnId());
        startActivity(i);
    }
}
