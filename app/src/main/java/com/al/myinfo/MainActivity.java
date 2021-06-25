package com.al.myinfo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener {

    EditText id,name,contact,experience,interest;
    RatingBar rating;
    RadioButton male,female;
    MyDataBase myDatabase;
    Button save,details,push;
    CircleImageView imageView;
    TextView birth;
    DatePickerDialog datePickerDialog;
    private static final int IMAGE_PICK_GALLERY_CROPLESS_CODE = 105;
    Uri imageUri;
    //int requestCode = 1;
    private static final int STORAGE_REQUEST_CODE = 101;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id=findViewById(R.id.st_id);
        name=findViewById(R.id.st_name);
        contact=findViewById(R.id.st_contact);
        experience=findViewById(R.id.st_experience);
        rating=findViewById(R.id.st_rating);
        male=findViewById(R.id.male);
        female=findViewById(R.id.female);
        save=findViewById(R.id.btn_save);
        details=findViewById(R.id.btn_details);
        interest=findViewById(R.id.st_interest);
        birth=findViewById(R.id.st_birth);
        imageView=findViewById(R.id.st_image);
        push=findViewById(R.id.btn_push);

        databaseReference= FirebaseDatabase.getInstance("https://person-info-c6e9c-default-rtdb.firebaseio.com/").getReference("Users");

        setUpDB();

 imageView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (!checkStoragePermission()) {
            requestStoragePermission();
        } else {
            pickGalCropLess();
        }
    }
});


     push.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {

         List<Student> stAll=myDatabase.dao().getAllStudent();
         Toast.makeText(getApplicationContext(),"Pushed To Remote Server",Toast.LENGTH_LONG).show();

         for(Student student:stAll){

             databaseReference.child(databaseReference.push().getKey()).setValue(student);

         }


     }
 });

        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog=new SpinnerDatePickerDialogBuilder()
                        .context(MainActivity.this)
                        .callback(MainActivity.this)
                        .spinnerTheme(R.style.NumberPickerStyle)
                        .showTitle(true)
                        .showDaySpinner(true)
                        .defaultDate(2018, 5,10)
                        .maxDate(2018,7,1)
                        .minDate(1850, 0, 1)
                        .build();

                datePickerDialog.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String st_ratings=String.valueOf(rating.getRating());
                String gender;

                if(male.isChecked()){
                    gender="male";
                }else {
                    gender="female";
                }


                Student student=new Student(id.getText().toString(),name.getText().toString(),contact.getText().toString(),
                        gender,experience.getText().toString(),st_ratings,interest.getText().toString(),birth.getText().toString(),String.valueOf(imageUri));


                myDatabase.dao().studentInsertion(student);
                Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();

            }
        });

      details.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

             startActivity(new Intent(getApplication(),StudentDetails.class));

          }
      });

    }

    private void setUpDB(){

        myDatabase = Room.databaseBuilder(MainActivity.this , MyDataBase.class , "StudentDB")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }




    @Override
    public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear+=1;
        birth.setText(""+dayOfMonth+"/"+monthOfYear+"/"+year);
        if(datePickerDialog.isShowing()){
            datePickerDialog.hide();
        }
    }

    private void pickGalCropLess() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CROPLESS_CODE );
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==IMAGE_PICK_GALLERY_CROPLESS_CODE && resultCode==RESULT_OK){

            imageUri = data.getData();

            //imageView.setImageURI(imageUri);

            if(!imageUri.equals("")){
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    Bitmap resized = Bitmap.createScaledBitmap(bitmap, 2000, 2000, true);
                    imageView.setImageBitmap(resized);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String picturePath = getPath( getApplicationContext( ), imageUri );
                imageUri=Uri.parse(picturePath);
            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }




}