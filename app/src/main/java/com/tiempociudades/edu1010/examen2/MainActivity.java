package com.tiempociudades.edu1010.examen2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    BitmapFactory bitmapFactory;
    Bitmap bitmapImagen;
    ImageView imagen;
    UUID uuid;
    FirebaseStorage storage;
    Button cambiar;
    ImageView subir;
    Uri uri;

    EditText aula;
    EditText descripcion;
    EditText uno;
    EditText dos;

    //ArrayList<incidencia> incidencias = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aula=findViewById(R.id.aula);
        descripcion=findViewById(R.id.descripcion);

        // mediaPlayer = MediaPlayer.create(this, R.raw.cancion1);
         //mediaPlayer.start();
        //mediaPlayer.setLooping(true);
        storage= FirebaseStorage.getInstance();
        cambiar=findViewById(R.id.cambiar);
        cambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subir_imagen_galeria();
                Intent intent = new Intent(getApplicationContext(), Incidencias.class);
                startActivity(intent);
            }
        });
        subir=findViewById(R.id.subirImagen);
        subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargar_imagen_galeria();
            }
        });
    }


    private void cargar_imagen_galeria(){

        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,10);


    }

    private void subir_imagen_galeria(){
        uuid = UUID.randomUUID();
        //Subir un fichero a FirebaseStorage, referencia
        final StorageReference storageRef = storage.getReference()
                .child("incidencias").child("incidencia").child(uuid+".jpg");


        UploadTask uploadTask;
        uploadTask = storageRef.putFile(uri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {

                    //codigo
                    Uri downloadUri = task.getResult();

                    incidencia objeto = new incidencia(downloadUri.toString(), descripcion.getText().toString(), aula.getText().toString() );
                    FirebaseDatabase.getInstance().getReference().child("incidencias").child("incidencia").push().setValue(objeto);


                }else {
                    // Handle failures
                    // ...
                }   }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 10) {
            Log.i("ENTRA", "entra");
            uri = data.getData();
            try {

                System.out.print("entrado");
                bitmapImagen = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                subir.setImageBitmap(bitmapImagen);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
