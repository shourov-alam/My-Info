package com.al.myinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentAllInfo extends AppCompatActivity {
    TextView id,name,contact,experience,interest,gender,chineseLevel,dob;
    CircleImageView circleImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_all_info);

        if(getSupportActionBar()!=null){

            getSupportActionBar().setTitle("Person Details");

        }

        if(getIntent()!=null && getIntent().getExtras() != null && getIntent().hasExtra(StudentAdapter.STUDENT_KEY)){

            Student student=(Student) getIntent().getSerializableExtra(StudentAdapter.STUDENT_KEY);

            circleImageView=findViewById(R.id.st_image);
            name=findViewById(R.id.name);
            id=findViewById(R.id.id);
            gender=findViewById(R.id.gender);
            chineseLevel=findViewById(R.id.chinese);
            dob=findViewById(R.id.dob);
            contact=findViewById(R.id.contact);
            experience=findViewById(R.id.experience);
            interest=findViewById(R.id.interest);

            Glide.with(getApplicationContext()).load(student.getImageUri()).into(circleImageView);

            name.setText(student.getName());
            id.setText(student.getId());
            gender.setText(student.getGender());
            chineseLevel.setText(student.getChineseLevel());
            dob.setText(student.getDateOfBirth());
            contact.setText(student.getContact());
            experience.setText(student.getExperience());
            interest.setText(student.getInterest());





        }
    }
}