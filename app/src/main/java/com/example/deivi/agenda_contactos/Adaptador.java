package com.example.deivi.agenda_contactos;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Deivi on 30/11/2016.
 */
public class Adaptador extends BaseAdapter {
    private ArrayList<Elemento> lista;
    private final Activity actividad;
    public Adaptador(Activity a, ArrayList<Elemento> v){
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
        return lista.get(arg0).getId();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater ly = actividad.getLayoutInflater();
        View view = ly.inflate(R.layout.item, null, true);

        TextView tvNombre= (TextView) view.findViewById(R.id.tvNombre);
        tvNombre.setText((CharSequence) lista.get(position).getNombre());

        TextView tvTelefono= (TextView) view.findViewById(R.id.tvTelefono);
        tvTelefono.setText(""+ lista.get(position).getTelefono());

        TextView tvDireccion= (TextView) view.findViewById(R.id.tvDireccion);
        tvDireccion.setText(""+ lista.get(position).getDireccion());

        TextView tvEmail= (TextView) view.findViewById(R.id.tvEmail);
        tvEmail.setText(""+ lista.get(position).getEmail());

        TextView tvPagina= (TextView) view.findViewById(R.id.tvPagina);
        tvPagina.setText(""+ lista.get(position).getPagina());

        /*File imgFile = new File(lista.get(position).getFoto());
        if(imgFile.exists()){
            ImageView im = (ImageView) view.findViewById(R.id.imagen);
            im.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
            im.setAdjustViewBounds(true);
        }*/
        return view;
    }
}
