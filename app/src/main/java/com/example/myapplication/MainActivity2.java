package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {

    ///// Instancia de base de datos
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseReference = database.getReference ();


    /// buto
    private Button guardar,actualizar,eliminar;
    /// variable globales
    String serie_gloval,keyglobal,des_global,tam_global,so_global,cam_global,ram_global;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        serie_gloval = getIntent().getExtras().getString("serie");

        if(!serie_gloval.equals("0")){
            guardar = findViewById(R.id.button2);
            guardar.setVisibility(View.GONE);
            keyglobal = getIntent().getExtras().getString("key");
            des_global = getIntent().getExtras().getString("des");
            tam_global = getIntent().getExtras().getString("tam");
            so_global = getIntent().getExtras().getString("so");
            cam_global = getIntent().getExtras().getString("cam");
            ram_global = getIntent().getExtras().getString("ram");
            Log.e("",keyglobal);
            this.llenar_datos();
        }else{
            actualizar = findViewById(R.id.button3);
            eliminar = findViewById(R.id.button4);
            actualizar.setVisibility(View.GONE);
            eliminar.setVisibility(View.GONE);

        }

    }

    public void llenar_datos(){
        final EditText serie = findViewById(R.id.serie);
        final EditText descrip = findViewById(R.id.descripcion);
        final EditText tama = findViewById(R.id.tamano);
        final EditText so = findViewById(R.id.So);
        final EditText camara = findViewById(R.id.camara);
        final TextView ram = findViewById(R.id.ram);
        serie.setText(serie_gloval);
        descrip.setText(des_global);
        tama.setText(tam_global);
        so.setText(so_global);
        camara.setText(cam_global);
        ram.setText(ram_global);
    }
    // Registrar
    public void  registrar(View view){
        try{
            final EditText serie = findViewById(R.id.serie);
            final EditText descrip = findViewById(R.id.descripcion);
            final EditText tama = findViewById(R.id.tamano);
            final EditText so = findViewById(R.id.So);
            final EditText camara = findViewById(R.id.camara);
            final TextView ram = findViewById(R.id.ram);

            if(TextUtils.isEmpty(serie.getText().toString()) || TextUtils.isEmpty(camara.getText().toString()) ||  TextUtils.isEmpty(ram.getText().toString()) ||  TextUtils.isEmpty(descrip.getText().toString())  || TextUtils.isEmpty(tama.getText().toString())  || TextUtils.isEmpty(so.getText().toString())  ){
                Toast toast = Toast.makeText(this, "Campos Incompletos", Toast.LENGTH_SHORT);
                toast.show();

            }else{

                DatabaseReference usersRef = mDatabaseReference.child("Dispositivos");
                HashMap<String,String> student=new HashMap<>();
                student.put("serie",serie.getText().toString());
                student.put("descripcion",descrip.getText().toString());
                student.put("tmaño",tama.getText().toString().replace(".", "punto"));
                student.put("So",so.getText().toString().replace(".", "punto"));
                student.put("camara",camara.getText().toString());
                student.put("ram",ram.getText().toString().replace(".", "punto"));

                usersRef.push().setValue(student);

                Toast toast = Toast.makeText(this, "Guardado Correctamente", Toast.LENGTH_SHORT);
                toast.show();


                Intent intent = new Intent (this,MainActivity.class );
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 0);
            }


        }catch (Exception e) {
            Log.e("Error",e.toString());
            Toast toast = Toast.makeText(this, "Error al Guardar  los Datos", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // actualizar
    public void  actualizar(View view){
        try{
            final EditText serie = findViewById(R.id.serie);
            final EditText descrip = findViewById(R.id.descripcion);
            final EditText tama = findViewById(R.id.tamano);
            final EditText so = findViewById(R.id.So);
            final EditText camara = findViewById(R.id.camara);
            final TextView ram = findViewById(R.id.ram);

            if(TextUtils.isEmpty(serie.getText().toString()) || TextUtils.isEmpty(camara.getText().toString()) ||  TextUtils.isEmpty(ram.getText().toString()) ||  TextUtils.isEmpty(descrip.getText().toString())  || TextUtils.isEmpty(tama.getText().toString())  || TextUtils.isEmpty(so.getText().toString())  ){
                Toast toast = Toast.makeText(this, "Campos Incompletos", Toast.LENGTH_SHORT);
                toast.show();

            }else{

                DatabaseReference usersRef = mDatabaseReference.child("Dispositivos").child(keyglobal);
                HashMap<String,String> student=new HashMap<>();
                student.put("serie",serie.getText().toString());
                student.put("descripcion",descrip.getText().toString());
                student.put("tmaño",tama.getText().toString());
                student.put("So",so.getText().toString());
                student.put("camara",camara.getText().toString());
                student.put("ram",ram.getText().toString());
                usersRef.setValue(student);

               // usersRef.push().setValue(student);
                //usersRef.updateChildren(student);

                Toast toast = Toast.makeText(this, "Actualizado Correctamente", Toast.LENGTH_SHORT);
                toast.show();


                Intent intent = new Intent (this,MainActivity.class );
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 0);
            }


        }catch (Exception e) {
            Log.e("Error",e.toString());
            Toast toast = Toast.makeText(this, "Error al Actualiar  los Datos", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void eliminar (View view){
        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        //Query applesQuery = ref.child("firebase-test").orderByChild("title").equalTo("Apple");
        DatabaseReference mDatabaseReference = database.getReference("Dispositivos").child(keyglobal);

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    Toast.makeText(getApplicationContext(), "Eliminado Correctamente", Toast.LENGTH_LONG).show();


                }
                Intent intent = new Intent (getApplication(),MainActivity.class );
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error",error.toString());
                Toast.makeText(getApplicationContext(), "Error al Iniciar Sesion ,Intente mas tarde", Toast.LENGTH_LONG).show();

            }
        });
    }
}