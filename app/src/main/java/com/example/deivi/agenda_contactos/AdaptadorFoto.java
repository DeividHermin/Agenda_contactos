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
public class AdaptadorFoto extends BaseAdapter {
    private ArrayList<Fotos> lista;
    private final Activity actividad;
    public AdaptadorFoto(Activity a, ArrayList<Fotos> v){
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
        return lista.get(arg0).getIdFoto();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater ly = actividad.getLayoutInflater();
        View view = ly.inflate(R.layout.itemfoto, null, true);

        TextView tvTelefono= (TextView) view.findViewById(R.id.etNombreFoto);
        tvTelefono.setText(""+ lista.get(position).getObservFoto());


        File imgFile = new File(lista.get(position).getRutaFichero());
        if(imgFile.exists()){
            ImageView imagenFoto = (ImageView)view.findViewById(R.id.ivItemFoto);
            imagenFoto.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
            imagenFoto.setAdjustViewBounds(true);
        }

        return view;
    }
}
