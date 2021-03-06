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

    EditText etNombre, etTelefono, etDireccion, etEmail, etPagina, etFoto;
    Button btTelefono, btFoto, btAlta;
    ImageView imagen;
    BDContactos bd;

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
        etFoto = (EditText)findViewById(R.id.etFotoA);
        btTelefono = (Button)findViewById(R.id.btAddTelefonoA);
        btTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaAddTelefono();
            }
        });
        btFoto = (Button)findViewById(R.id.btAddFotoA);
        btFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gestionFotos();
            }
        });
        btAlta = (Button)findViewById(R.id.btAlta);
        btAlta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alta();
            }
        });
        imagen = (ImageView)findViewById(R.id.imagenAdd);
    }

    @Override
    public void onBackPressed(){
        bd.borrarTelContacto((int)(bd.returnId()));
        bd.borrarFotosContacto((int)bd.returnId());
        //System.out.println("BORRANDO TELEFONOS DEL CONTACTO "+(bd.returnId()));
        super.onBackPressed();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        etTelefono.setText(bd.primerTelefono((int)bd.returnId()));
        etFoto.setText(bd.comentarioPrimeraFoto((int)bd.returnId()));

    }

    public void alta(){
        String nombre =     etNombre.getText().toString();
        String telefono =   etTelefono.getText().toString();
        String direccion =  etDireccion.getText().toString();
        String email =      etEmail.getText().toString();
        String pagina =     etPagina.getText().toString();

        boolean error1=true, error2=true;
        if(bd.primerTelefono((int)bd.returnId()).equals(""))
            error1=false;
        if(bd.primeraFoto((int)bd.returnId()).equals(""))
            error2=false;

        if(error1 && error2){
            bd.insertarContacto(new Elemento(bd.returnId(), nombre, telefono, "", direccion, email, pagina));
            Toast.makeText(getApplicationContext(), R.string.contactoAdd, Toast.LENGTH_SHORT).show();

            etNombre.setText("");
            etTelefono.setText("");
            etDireccion.setText("");
            etEmail.setText("");
            etPagina.setText("");

            finish();
        }else{
            if(!error1)
                Toast.makeText(getApplicationContext(), R.string.faltaTelefono, Toast.LENGTH_SHORT).show();
            if(!error2)
                Toast.makeText(getApplicationContext(), R.string.faltaFoto, Toast.LENGTH_SHORT).show();
        }

    }

    public void diaAddTelefono(){
        Intent i = new Intent(getApplicationContext(), AddTelefono.class);
        i.putExtra("idContacto", bd.returnId());
        startActivity(i);
    }

    public void gestionFotos(){
        Intent i = new Intent(getApplicationContext(), AddFoto.class);
        i.putExtra("idContacto", bd.returnId());
        startActivity(i);
    }
}
