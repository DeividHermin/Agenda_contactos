package com.example.deivi.agenda_contactos;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
    BDContactos bd;

    public Adaptador(Activity a, ArrayList<Elemento> v, BDContactos bd) {
        super();
        this.lista = v;
        this.actividad = a;
        this.bd = bd;
    }

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater ly = actividad.getLayoutInflater();
        View view = ly.inflate(R.layout.item, null, true);

        TextView tvNombre = (TextView) view.findViewById(R.id.tvNombre);
        tvNombre.setText((CharSequence) lista.get(position).getNombre());

        TextView tvTelefono = (TextView) view.findViewById(R.id.tvTelefono);
        tvTelefono.setText("" + lista.get(position).getTelefono(bd));

        TextView tvDireccion = (TextView) view.findViewById(R.id.tvDireccion);
        tvDireccion.setText("" + lista.get(position).getDireccion());

        TextView tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        tvEmail.setText("" + lista.get(position).getEmail());

        TextView tvPagina = (TextView) view.findViewById(R.id.tvPagina);
        tvPagina.setText("" + lista.get(position).getPagina());

        File imgFile = new File(lista.get(position).getFoto(bd));
        if (imgFile.exists()) {
            ImageView imagenFoto = (ImageView) view.findViewById(R.id.imagen);
            System.out.println(imgFile.getAbsolutePath());
            imagenFoto.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
            imagenFoto.setAdjustViewBounds(true);
        }

        Button btLlamada = (Button) view.findViewById(R.id.btItemLlamar);
        btLlamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:" + lista.get(position).getTelefono(bd)));
                if (ActivityCompat.checkSelfPermission(actividad, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                actividad.startActivity(i);
            }
        });

        Button btCamara = (Button) view.findViewById(R.id.btItemCamera);
        btCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(actividad, AddFoto.class);
                i.putExtra("idContacto", lista.get(position).getId());
                actividad.startActivity(i);
            }
        });
        return view;
    }
}
