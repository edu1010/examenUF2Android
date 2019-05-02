package com.tiempociudades.edu1010.examen2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.util.ArrayList;

public class Incidencias extends AppCompatActivity {

    RecyclerView recyclerView;
    IncidenciasAdapter incidenciasAdapter ;
    private FirebaseStorage firebaseStorage; // Storage
    private FirebaseDatabase firebaseDatabase; // Realtime Database
    ArrayList<incidencia> incidencias = new ArrayList<>(); //lista de incidencias sacadas de firebase db
    Bitmap bitmap;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //musica
        String urlmusica = "https://firebasestorage.googleapis.com/v0/b/examen2-eab49.appspot.com/o/cancion1.mp3?alt=media&token=f3196c31-723e-4cf8-96a5-34ae26569396"; // your URL here
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(urlmusica);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        mediaPlayer.isLooping();


        setContentView(R.layout.activity_incidencias);
        //Apartado Recycler
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        incidenciasAdapter= new IncidenciasAdapter();
        recyclerView.setAdapter(incidenciasAdapter);

        //Apartado firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("incidencias").child("incidencia").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                incidencia incidenciaObjeto;
                incidenciaObjeto = dataSnapshot.getValue(incidencia.class);
                incidencias.add(incidenciaObjeto);
                incidenciasAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        firebaseStorage = FirebaseStorage.getInstance();


    }







    //ADAPTER
    public class IncidenciasAdapter extends RecyclerView.Adapter<IncidenciasAdapter.IncidenciasViewHolder>{


        @NonNull
        @Override
        public IncidenciasViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = getLayoutInflater().inflate(R.layout.recycler_view, viewGroup, false);

            return new IncidenciasViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final IncidenciasViewHolder incidenciasViewHolder, int i) {


            incidenciasViewHolder.descripcionMostrar.setText(incidencias.get(i).getDescripcion());
            incidenciasViewHolder.aulamostrar.setText("Aula: "+incidencias.get(i).getAula());

            firebaseStorage.getReferenceFromUrl(incidencias.get(i).getImagenURL()) // Pillamos el String con la URL de la imagen metida
                    .getBytes(1024*1024*5).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length); // Transforma el Array de bytes con la foto para setearlo al ImageView
                    incidenciasViewHolder.imagen.setImageBitmap(bitmap);
                }
            });

        }

        @Override
        public int getItemCount() {
            return incidencias.size();
        }

        //VIEWHOLDER
        public class IncidenciasViewHolder extends RecyclerView.ViewHolder{
            ImageView imagen;
            ImageView x;
            CheckBox tachar;
            //Button subir;
            TextView descripcionMostrar;
            TextView aulamostrar;
            int contador=0;
            public IncidenciasViewHolder(@NonNull View itemView) {
                super(itemView);
                descripcionMostrar=itemView.findViewById(R.id.descripcionmostrar);
                aulamostrar=itemView.findViewById(R.id.aulaMostrar);
              tachar=itemView.findViewById(R.id.tachar);

            imagen=itemView.findViewById(R.id.foto);
            x=itemView.findViewById(R.id.x);
            x.animate().translationX(1000);

                tachar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        contador=contador+1;
                        if((contador % 2) != 0){
                        x.animate().translationX(0).setDuration(1000);}else{
                            x.animate().translationX(1000).setDuration(1000);
                        }

                    }
                });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //tachar.animate().translationX(-1000).setDuration(1000);
                }
            });


            }
        }

    }

    //MENU TOOLBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Escojo que hacer en función del botón pulsado
        switch (item.getItemId()){
            case R.id.silenciar:
                mediaPlayer.pause();
                break;



            //Este botón de momento no tiene utilidad ;-)
            case R.id.anterior:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
