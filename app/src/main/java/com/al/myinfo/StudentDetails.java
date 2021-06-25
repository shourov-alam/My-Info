package com.al.myinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

public class StudentDetails extends AppCompatActivity {

    MyDataBase myDatabase;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    StudentAdapter studentAdapter;
    List<Student> stuData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);
        recyclerView=findViewById(R.id.reCycleview);
        setUpDB();

        if(getSupportActionBar()!=null){

            getSupportActionBar().setTitle("Persons List");

        }


        stuData = myDatabase.dao().getAllStudent();

        studentAdapter=new StudentAdapter(stuData,this);
        recyclerView.setAdapter(studentAdapter);

        linearLayoutManager = new LinearLayoutManager(StudentDetails.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    private void setUpDB(){

        myDatabase = Room.databaseBuilder(StudentDetails.this , MyDataBase.class , "StudentDB")
                .allowMainThreadQueries().build();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) myActionMenuItem.getActionView();

        searchView.setQueryHint("Enter Name or ID");

        searchView.setIconifiedByDefault(true);
        int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
        ImageView v =  searchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.ic_baseline_search_24);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                try {
                  //  studentAdapter.filter(s);
                    // fab.setVisibility(View.GONE);
                }catch (Exception e){

                }

                return false;
            }


            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)){

                    try {
                       // fab.setVisibility(View.VISIBLE);
                        studentAdapter.filter("");

                    }catch (Exception e){

                    }

                }
                else {

                    try {
                        studentAdapter.filter(s);
                       // fab.setVisibility(View.GONE);
                    }catch (Exception e){

                    }

                }
                return true;
            }

        });


        return true;
    }
}