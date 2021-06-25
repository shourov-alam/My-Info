package com.al.myinfo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tsongkha.spinnerdatepicker.DatePicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder>{

    public static final String STUDENT_KEY = "STUDENT";
    List<Student> list;
    Context context;
    MyDataBase myDatabase;
    List<Student> list1;
    private static final int PERMISSION_SEND_SMS = 123;


    public StudentAdapter(List<Student> list, Context context) {
        this.list = list;
        this.context = context;
        this.list1 =new ArrayList<>(list);
       // this.list1.addAll(list);

        myDatabase= Room.databaseBuilder(context , MyDataBase.class , "StudentDB")
                .allowMainThreadQueries().build();
    }


    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.student_details_row,parent,false);

        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.StudentViewHolder holder, int position) {


                 Glide
                .with(context)
                .load(list.get(position).getImageUri())
               // .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(holder.imageView);
        holder.name.setText(list.get(position).getName());
        holder.id.setText(list.get(position).getId());
        //holder.gender.setText(list.get(position).getGender());
        //holder.contact.setText(list.get(position).getContact());
        holder.dob.setText(list.get(position).getDateOfBirth());
       // holder.chinese.setText(list.get(position).getChineseLevel());
       // holder.experience.setText("Experience: "+list.get(position).getExperience());
        //holder.interest.setText("Interest: "+list.get(position).getInterest());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student student=new Student(list.get(position).getId(),list.get(position).getName(),
                        list.get(position).getContact(),list.get(position).getGender(),list.get(position).getExperience()
                ,list.get(position).getChineseLevel(),list.get(position).getInterest(),list.get(position).getDateOfBirth()
                ,list.get(position).getImageUri());
                Intent intent=new Intent(context,StudentAllInfo.class);
                intent.putExtra(STUDENT_KEY,student);
                context.startActivity(intent);

            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Student student=list.get(position);
                myDatabase.dao().delete(student);

             //   list=myDatabase.dao().getAllStudent();

                list.remove(position);
                //not to repeat while search
                list1.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,list.size());
                notifyDataSetChanged();


               // notifyItemRangeRemoved(position,list.size());
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                View view=LayoutInflater.from(context).inflate(R.layout.edit_student_row,null,false);

                TextView id=view.findViewById(R.id.st_id);
                TextView name=view.findViewById(R.id.st_name);
                TextView contact=view.findViewById(R.id.st_contact);
                TextView experience=view.findViewById(R.id.st_experience);
                RatingBar rating=view.findViewById(R.id.st_rating);
                RadioButton male=view.findViewById(R.id.male);
                RadioButton female=view.findViewById(R.id.female);
                TextView interest=view.findViewById(R.id.st_interest);
                TextView birth=view.findViewById(R.id.st_birth);
                CircleImageView imageView=view.findViewById(R.id.st_image);


                if(list!=null){

                    id.setText(list.get(position).getId());
                    name.setText(list.get(position).getName());
                    contact.setText(list.get(position).getContact());
                    experience.setText(list.get(position).getExperience());
                    interest.setText(list.get(position).getInterest());
                    birth.setText(list.get(position).getDateOfBirth());
                    rating.setRating(Float.parseFloat(list.get(position).getChineseLevel()));
                    if(list.get(position).getGender().equals("male")){
                        male.setChecked(true);
                    }else {
                        female.setChecked(true);
                    }

                    Glide.with(context).load(list.get(position).getImageUri()).into(imageView);


                }

/////////////////
                new AlertDialog.Builder(context)
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (list != null) {

                                    String st_ratings=String.valueOf(rating.getRating());
                                    String gender;

                                    if(male.isChecked()){
                                        gender="male";
                                    }else {
                                        gender="female";
                                    }



                                    Student student=new Student(id.getText().toString(),name.getText().toString(),contact.getText().toString(),
                                            gender,experience.getText().toString(),st_ratings,interest.getText().toString(),birth.getText().toString(),String.valueOf(list.get(position).getImageUri()));

                                    student.setStuId(list.get(position).getStuId());
                                    myDatabase.dao().update(student);
                                    list.clear();
                                    list=myDatabase.dao().getAllStudent();
                                    notifyDataSetChanged();

                                }
                            }
                        })

                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.dismiss();
                            }
                        })
                        .setCancelable(false)
                        .setView(view)
                        .show();


///////////////////////////
            }
        });

        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

                    requestSmsPermission();

                }else {
                    View view=LayoutInflater.from(context).inflate(R.layout.send_sms_row,null,false);

                    EditText num=view.findViewById(R.id.edt_number);
                    new AlertDialog.Builder(context).setTitle("Enter Number to send SMS")
                            .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                   // Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_LONG).show();
                                    SmsManager sms = SmsManager.getDefault();
                                    sms.sendTextMessage(num.getText().toString(), null, "Name: "+list.get(position).getName() +"\n"+ "ID: "+ list.get(position).getId(), null, null);

                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    }).setCancelable(false).setView(view).show();
                }

            }



        });

    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length()==0){

           // list=myDatabase.dao().getAllStudent();
            list.addAll(list1);
        }
        else {
            list1.clear();
            list1=myDatabase.dao().getAllStudent();
            for (Student model : list1){
                if (model.getName().toLowerCase(Locale.getDefault())
                        .contains(charText) || model.getId().toLowerCase(Locale.getDefault())
                        .contains(charText) ){
                    list.add(model);
                }

            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class StudentViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name,id,gender,contact,dob,chinese,experience,interest;
        ImageView delete,edit,message;
        LinearLayout linearLayout;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.st_image);
            name=itemView.findViewById(R.id.name);
            id=itemView.findViewById(R.id.ids);
           // gender=itemView.findViewById(R.id.gender);
            //contact=itemView.findViewById(R.id.contact);
            dob=itemView.findViewById(R.id.dob);
            /*chinese=itemView.findViewById(R.id.chinese);
            experience=itemView.findViewById(R.id.exper);
            interest=itemView.findViewById(R.id.interest); */
            delete=itemView.findViewById(R.id.delete);
            edit=itemView.findViewById(R.id.edit);
            message=itemView.findViewById(R.id.message);
            linearLayout=itemView.findViewById(R.id.lin);





        }
    }
    private void requestSmsPermission() {

        // check permission is given
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // request permission (see result in onRequestPermissionsResult() method)
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSION_SEND_SMS);
        } else {
            // permission already granted run sms send
            // sendSms(phone, message);
        }
    }

 /*   @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_SEND_SMS: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    // sendSms(phone, message);
                } else {
                    // permission denied
                }
                return;
            }
        }
    }

    private void sendSms(String phoneNumber, String message){
        Toast.makeText(context,"Done",Toast.LENGTH_LONG).show();
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    } */


}
