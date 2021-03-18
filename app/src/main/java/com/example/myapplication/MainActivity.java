package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    myadapter adapter2;
    List<String> lis_key= new ArrayList<String>();
    List<String> lis_serie= new ArrayList<String>();
    List<String> lis_des = new ArrayList<String>();
    List<String> lis_tama = new ArrayList<String>();
    List<String> lis_so = new ArrayList<String>();
    List<String> lis_cama = new ArrayList<String>();
    List<String> list_ram = new ArrayList<String>();

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        llenar_lista();

        /// evento click para lista
        listView=(ListView)findViewById(R.id.lista);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent (getApplicationContext(),MainActivity2.class );
                intent.putExtra("key", lis_key.get(position));
                intent.putExtra("serie", lis_serie.get(position));
                intent.putExtra("des", lis_des.get(position));
                intent.putExtra("tam", lis_tama.get(position));
                intent.putExtra("so", lis_so.get(position));
                intent.putExtra("cam", lis_cama.get(position));
                intent.putExtra("ram", list_ram.get(position));
                startActivityForResult(intent, 0);
            }
        });
    }

    public void agregar(View view){
        Intent intent = new Intent (this,MainActivity2.class );
        intent.putExtra("serie", "0");
        startActivityForResult(intent, 0);
    }
 /// Codigo para econsultar la base de datos , y llenar el adaptador(lista)
    public void llenar_lista(){
        listView=(ListView)findViewById(R.id.lista);
        DatabaseReference mDatabaseReference = database.getReference("Dispositivos");

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapShot: dataSnapshot.getChildren()){
                        Log.e("a",snapShot.toString());
                        lis_key.add(snapShot.getKey());
                        lis_serie.add(snapShot.child("serie").getValue().toString());
                        lis_des.add(snapShot.child("descripcion").getValue().toString());
                        lis_tama.add(snapShot.child("tmaño").getValue().toString());
                        lis_so.add(snapShot.child("So").getValue().toString());
                        lis_cama.add(snapShot.child("camara").getValue().toString());
                        list_ram.add(snapShot.child("ram").getValue().toString());
                    }

                    String[] string9 = new String[lis_serie.size()];
                    string9 = lis_serie.toArray(string9);
                    String[] strings = new String[lis_des.size()];
                    strings = lis_des.toArray(strings);
                    String[] strings2 = new String[lis_tama.size()];
                    strings2 = lis_tama.toArray(strings2);
                    String[] strings3 = new String[lis_so.size()];
                    strings3 = lis_so.toArray(strings3);
                    String[] strings4 = new String[lis_cama.size()];
                    strings4 = lis_cama.toArray(strings4);
                    String[] strings5 = new String[list_ram.size()];
                    strings5 = list_ram.toArray(strings5);
                    final myadapter adapter= new myadapter(getApplication(),string9,strings,strings2,strings3,strings4,strings5);
                    adapter2=adapter;
                    listView.setAdapter(adapter);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Sin Datos de Dispositivos", Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error",databaseError.toString());
                Toast.makeText(getApplicationContext(), "Error al Iniciar Sesion ,Intente mas tarde", Toast.LENGTH_LONG).show();
            }
        });

    }
    class myadapter extends ArrayAdapter<String> {
        Context context;
        String serie [];
        String descrip [];
        String tama [];
        String so [];
        String camara [];
        String ram [];
        myadapter(Context c,String serie [],String descrip [],String  tama [], String so [],String camara[],String ram[]){
            super(c,R.layout.dise_lista,R.id.serie,serie);
            this.context=c;
            this.serie = serie;
            this.descrip= descrip ;
            this.tama= tama;
            this.so= so;
            this.camara= camara;
            this.ram= ram;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row= layoutInflater.inflate(R.layout.dise_lista,parent,false);
            final TextView serie_edt = row.findViewById(R.id.serie_lista);
            final TextView ram_edi = row.findViewById(R.id.ram_list);
            final TextView descrp_edi = row.findViewById(R.id.descrip_lista);
            final TextView tama_edi = row.findViewById(R.id.tama_list);
            final TextView so_edi = row.findViewById(R.id.so_list);
            final TextView camra_edi = row.findViewById(R.id.camara_list);
            serie_edt.setText("Serie: "+serie[position]);
            ram_edi.setText("Ram: "+ram[position]);
            descrp_edi.setText("Descripcion: "+descrip[position]);
            tama_edi.setText("Tamaño: "+tama[position]);
            so_edi.setText("SO: "+so[position]);
            camra_edi.setText("Camara: "+camara[position]);
            return row;
        }
    }

}