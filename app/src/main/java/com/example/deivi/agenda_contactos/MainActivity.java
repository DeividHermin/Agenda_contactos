package com.example.deivi.agenda_contactos;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends ListActivity {

    static Adaptador a;
    static ArrayList<Elemento> arrayList = new ArrayList();
    public static BDContactos bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //PARA PODER USAR TOOLBAR EN UN LISTACTIVITY
        AppCompatCallback callback = new AppCompatCallback() {
            @Override
            public void onSupportActionModeStarted(ActionMode actionMode) {
            }

            @Override
            public void onSupportActionModeFinished(ActionMode actionMode) {
            }

            @Nullable
            @Override
            public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
                return null;
            }
        };

        AppCompatDelegate delegate = AppCompatDelegate.create(this, callback);

        delegate.onCreate(savedInstanceState);
        delegate.setContentView(R.layout.activity_main);

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        delegate.setSupportActionBar(toolbar);
        delegate.getSupportActionBar().setDisplayShowHomeEnabled(true);
        //PARA PODER USAR TOOLBAR EN UN LISTACTIVITY

        bd = new BDContactos(this);
        actualizaLista();

        a = new Adaptador(this,arrayList);
        a.notifyDataSetChanged();
        setListAdapter(a);

        for(int i=0; i<bd.returnId()-1; i++)
            System.out.println("Id: "+bd.listado().get(i).getId());

        final ListView listview = (ListView) findViewById(android.R.id.list);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapter, View view, int position, long arg)
            {
                //Toast.makeText(getApplication(), "Pulsado", Toast.LENGTH_SHORT).show();
                Elemento el = (Elemento) listview.getAdapter().getItem(position);
                Intent it = new Intent(getApplicationContext(), EditaContacto.class);
                it.putExtra("contacto", el);
                startActivity(it);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.btPreferences:   abrirPreferencias();break;
            case R.id.btAddContacto:   abrirAddContacto();break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart () {
        super.onRestart();
        Toast.makeText(this, "Lista recargada", Toast.LENGTH_SHORT).show();
        actualizaLista();

        for(int i=0; i<bd.returnId()-1; i++)
            System.out.println("Id: "+bd.listado().get(i).getId());

        a = null;
        a = new Adaptador(this,arrayList);
        a.notifyDataSetChanged();
        setListAdapter(a);
    }

    public void abrirPreferencias(){
        Intent i = new Intent(this, Preferencias.class);
        startActivity(i);
    }

    public void abrirAddContacto(){
        Intent it = new Intent(getApplicationContext(), AddContacto.class);
        startActivity(it);
    }

    public void actualizaLista(){
        arrayList.clear();
        if(bd.listado().size()>0)
            for(int i=0; i<bd.listado().size(); i++){
                arrayList.add(bd.listado().get(i));
            }
        else
            Toast.makeText(getApplicationContext(), "La lista está vacia", Toast.LENGTH_SHORT).show();
    }
}
