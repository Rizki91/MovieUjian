package com.fahrul.movieujian;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.fahrul.movieujian.model.Movie;
import com.fahrul.movieujian.model.TitleMovie;
import com.fahrul.movieujian.service.APIClient;
import com.fahrul.movieujian.service.APIInterfaceRest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahMovie extends AppCompatActivity {

    EditText txtJudul,txtDirectby,txtWritenby,txtStudio;
    Spinner spnRating,spnGenre;
    CalendarView cbIntheater;
    ImageButton img_btn1,img_btn2,img_btn3;
    Button btnSend;
    String image= "";
    ProgressBar progressBarTambahData;
    String tanggal = "";
    private AppDatabase mDb;
    private StorageReference refence;
    private int REQUEST_GALLERY = 100;
    private int REQUEST_GALLERY2 = 200;
    private int REQUEST_GALLERY3 = 300;
    private DatabaseReference mDatabase;



    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_movie);
        getSupportActionBar().setTitle("Tambah Data");
        cbIntheater = findViewById(R.id.cbIntheater);
        txtJudul = findViewById(R.id.txtJudul);
        txtDirectby = findViewById(R.id.txtDirectby);
        txtWritenby = findViewById(R.id.txtWritenby);
        txtStudio = findViewById(R.id.txtStudio);
        spnRating = findViewById(R.id.spnRating);
        spnGenre = findViewById(R.id.spnGenre);
//        img_btn2  = findViewById(R.id.img_btn2);
//        img_btn3 = findViewById(R.id.img_btn3);
        img_btn1 = findViewById(R.id.img_btn1);
        btnSend = findViewById(R.id.buttonSend);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        progressBarTambahData = findViewById(R.id.progressBarTambahData);
        progressBarTambahData.setVisibility(View.GONE);
        refence = FirebaseStorage.getInstance().getReference();
        mDb = AppDatabase.getInstance(getApplicationContext());
        txtJudul.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                callTitleMovie(charSequence.toString());


            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });




        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
        tanggal = ft.format(dNow);

        cbIntheater.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date date = new Date(year-1900, month, dayOfMonth);
                tanggal = sdf.format(date);
                Toast.makeText(TambahMovie.this, tanggal, Toast.LENGTH_SHORT).show();
            }
        });




        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkMandatory()){



                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            List<TitleMovie> movie = null;
//                            movie = mDb.movieDAO().findByTitel(txtJudul.getText().toString());


                            if (movie != null){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showErrorDialogDifferentContent();
                                    }
                                });
                            } else {
                                mDb.MovieDao().insertAll(generateObjectData());
                                mDatabase.child("Movie").child("Data").child(generateObjectData().getTitle()).setValue(generateObjectData());

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showDialogInfo();
                                    }
                                });
                            }
                        }
                    }).start();
                } else {
                    showErrorDialog();
                }

            }
        });

        img_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String  namaFile = UUID.randomUUID()+".jpg";
//                String pathImage = "gambar/"+namaFile;
//                refence.child(pathImage).putBytes(byteArray);
                openFolder1();

            }
        });

//        img_btn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                String  namaFile1 = UUID.randomUUID()+".jpg";
////                String pathImage1 = "gambar/"+namaFile1;
////                refence.child(pathImage1).putBytes(byteArray2);
//                openFolder2();
//            }
//        });
//
//        img_btn3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                String  namaFile2 = UUID.randomUUID()+".jpg";
////                String pathImage2 = "gambar/"+namaFile2;
////                refence.child(pathImage2).putBytes(byteArray2);
//                openFolder3();
//            }
//        });
    }
    APIInterfaceRest apiInterface;
    public void callTitleMovie(String judul){
        apiInterface = APIClient.getClient().create(APIInterfaceRest.class);
        progressBarTambahData.setVisibility(View.VISIBLE);
        Call<Movie> call3 = apiInterface.searchByOMDbId(judul,"763c4ddf");
        call3.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Movie model = response.body();
                if (model !=null) {
                    final ArrayList<String>ratingList = new ArrayList<String>();
                    ratingList.clear();
                    if (model.getResponse().equals("False")) {
                        ratingList.add("");
                    } else {
                        for (int i = 0; i < model.getRatings().size(); i++) {
                            ratingList.add(model.getRatings().get(i).getValue() + " : " + model.getRatings().get(i).getSource());
                        }
                    }
                    ArrayAdapter<String> arrayRating = new ArrayAdapter<String>(TambahMovie.this, android.R.layout.simple_list_item_1, ratingList);
                    arrayRating.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnRating.setAdapter(arrayRating);
                    String genre = (model.getResponse().equals("False")) ? "" : model.getGenre();
                    List<String> genreList = Arrays.asList(genre.split("\\s*,\\s*"));
                    ArrayAdapter<String> arrayGenre = new ArrayAdapter<String>(TambahMovie.this, android.R.layout.simple_list_item_1, genreList);
                    arrayGenre.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnGenre.setAdapter(arrayGenre);
                    txtDirectby.setText(model.getDirector());
                    txtWritenby.setText(model.getWriter());
                    image = model.getPoster();
                    Picasso.get().load(image).into(img_btn1);
                    progressBarTambahData.setVisibility(View.GONE);
                } else{

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(TambahMovie.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(TambahMovie.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                progressBarTambahData.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Maaf koneksi bermasalah",Toast.LENGTH_LONG).show();
                call.cancel();
            }


        });
    }





    public boolean checkMandatory(){
        boolean pass = true;
        if (TextUtils.isEmpty(txtJudul.getText().toString())){
            pass = false;
            txtJudul.setError("Masukkan Judul, mandatory");
        }

        return pass;
    }



    public void openFolder1() {


        Intent folderIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(folderIntent, REQUEST_GALLERY);



    }

//    public void openFolder2() {
//
//        Intent folderIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(folderIntent, REQUEST_GALLERY2);
//
//
//
//    }
//
//    public void openFolder3() {
//
//
//        Intent folderIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(folderIntent, REQUEST_GALLERY3);
//
//
//    }

    Bitmap bitmap;
    byte[] byteArray,byteArray2,byteArray3;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();

            img_btn1.setImageURI(selectedImage);
            Bitmap bitmap = ((BitmapDrawable) img_btn1.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byteArray = baos.toByteArray();
            // img_btn1.setImageBitmap(bitmap);



//        }else if (requestCode == REQUEST_GALLERY2 && resultCode == Activity.RESULT_OK) {
//            Uri selectedImage = data.getData();
//
//            img_btn2.setImageURI(selectedImage);
//            Bitmap bitmap = ((BitmapDrawable) img_btn2.getDrawable()).getBitmap();
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
//            byteArray2 = baos.toByteArray();
//            // img_btn2.setImageBitmap(bitmap);
//
//        }else  if (requestCode == REQUEST_GALLERY3 && resultCode == Activity.RESULT_OK) {
//            Uri selectedImage = data.getData();
//
//            img_btn3.setImageURI(selectedImage);
//            Bitmap bitmap = ((BitmapDrawable) img_btn3.getDrawable()).getBitmap();
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
//            byteArray3 = baos.toByteArray();
//            // img_btn3.setImageBitmap(bitmap);

        }
    }
    public TitleMovie generateObjectData(){
        TitleMovie movie = new TitleMovie();
        movie.setTitle(txtJudul.getText().toString());
        movie.setRating(spnRating.getSelectedItem().toString());
        movie.setGenre(spnGenre.getSelectedItem().toString());
        movie.setDirectedBy(txtDirectby.getText().toString());
        movie.setWrittenBy(txtWritenby.getText().toString());
        movie.setInTheater(tanggal);
        movie.setStudio(txtStudio.getText().toString());
        movie.setImgPoster(image);
        return movie;
    }

    public void showErrorDialogDifferentContent(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(TambahMovie.this);
        alertDialog.setTitle("Peringatan");
        alertDialog.setMessage("Mohon masukkan Judul yang berbeda")
                .setIcon(R.drawable.ic_close)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(TambahMovie.this, "Cancel ditekan", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void showDialogInfo(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(TambahMovie.this);
        alertDialog.setTitle("Tambah Data");
        alertDialog.setMessage("Berhasil tambah data")
                .setIcon(R.drawable.ic_tick)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void showErrorDialog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(TambahMovie.this);
        alertDialog.setTitle("Peringatan");
        alertDialog.setMessage("Mohon isi field yang mandatory")
                .setIcon(R.drawable.ic_close)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(TambahMovie.this, "Cancel ditekan", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }


}