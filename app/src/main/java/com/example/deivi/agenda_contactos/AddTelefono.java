package com.example.deivi.agenda_contactos;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.deivi.agenda_contactos.MainActivity.arrayList;

/**
 * Created by Deivi on 08/12/2016.
 */

public class AddTelefono extends AppCompatActivity {

    ListView lista;
    static AdaptadorTel a;
    static ArrayList<Telefonos> arrayListTel = new ArrayList();
    BDContactos bd;
    int idContacto;
    //Elemento contacto;

    Button btAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtelefono);
        bd = new BDContactos(this);

        lista = (ListView)findViewById(R.id.listaTel);
        btAdd = (Button)findViewById(R.id.btAñadirTel);
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
        a = new AdaptadorTel(this,arrayListTel);
        a.notifyDataSetChanged();
        lista.setAdapter(a);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapter, View view, int position, long arg)
            {
                Telefonos tel = (Telefonos) lista.getAdapter().getItem(position);
                diaPulsado(tel);
            }
        });
    }

    @Override
    protected void onRestart () {
        super.onRestart();
        //Toast.makeText(this, "Lista recargada", Toast.LENGTH_SHORT).show();

        actualizaLista();
        a = null;
        a = new AdaptadorTel(this,arrayListTel);
        a.notifyDataSetChanged();
        lista.setAdapter(a);
    }

    public void actualizaLista() {
        arrayListTel.clear();
        if (bd.countTelefonosContacto(idContacto) > 0)
            for (int i = 0; i < bd.countTelefonosContacto(idContacto); i++) {
                List<Telefonos> lista = bd.returnTelefonos(idContacto);
                arrayListTel.add(lista.get(i));
                System.out.println("actualizaLista ID: "+lista.get(i).getIdTelefonos());
            }
        else
            System.out.println("No hay telefonos");
    }

    public void añadirTelefono(String telefono){
        if(bd.guardaTelefono(telefono, idContacto))
            Toast.makeText(getApplicationContext(), "Telefono "+telefono+" guardado", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Error guardando el telefono", Toast.LENGTH_SHORT).show();
    }

    public void borrarTelefono(int idTelefono){
        bd.borrarYordenarTelefono(idTelefono);
    }

    public void llamarTelefono(String telefono){
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:"+telefono));
        startActivity(i);
    }

    public void diaPulsado(final Telefonos tel) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("");
        alert.setTitle("¿Que deseas hacer?");

        alert.setPositiveButton("Llamar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                llamarTelefono(tel.getTelefono());
            }
        });

        alert.setNeutralButton("Borrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                borrarTelefono(tel.getIdTelefonos());
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
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(getApplicationContext());
        alert.setMessage("");
        alert.setTitle("Nuevo telefono");
        alert.setView(edittext);

        alert.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String telefono = edittext.getText().toString();
                añadirTelefono(telefono);
                //actualizaTelContacto();
                onRestart();
                //Toast.makeText(getApplicationContext(), "TELEFONO: "+telefono, Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }
    /*
    public void actualizaTelContacto(){
        contacto.setTelefono(bd.primerTelefono((int)contacto.getId()));
    }*/
}
