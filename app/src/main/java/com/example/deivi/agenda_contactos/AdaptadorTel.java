package com.example.deivi.agenda_contactos;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Deivi on 30/11/2016.
 */
public class AdaptadorTel extends BaseAdapter {
    private ArrayList<Telefonos> lista;
    private final Activity actividad;
    public AdaptadorTel(Activity a, ArrayList<Telefonos> v){
        super();
        this.lista = v;
        this.actividad = a;
    }
// En el constructor de la clase se indica la actividad donde se ejecutará
// y la lista de datos a visualizar.
    @Override
    public int getCount() {
        return lista.size();
    }
    @Override
    public Object getItem(int arg0) {
        return lista.get(arg0);
    }
    @Override
    public long getItemId(int arg0) {
        return lista.get(arg0).getIdTelefonos();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater ly = actividad.getLayoutInflater();
        View view = ly.inflate(R.layout.itemtel, null, true);

        TextView tvTelefono= (TextView) view.findViewById(R.id.tvEditTel);
        tvTelefono.setText(""+ lista.get(position).getTelefono());

        return view;
    }
}
