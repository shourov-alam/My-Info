package com.al.myinfo;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.ArrayList;
import java.util.List;
@Dao
public interface DAO {
    @Insert
    public void studentInsertion(Student student);
    @Update
    void update(Student student);
    @Delete
    void delete(Student student);
    @Query("DELETE FROM Student")
    void deleteAllStudent();
    @Query("SELECT * FROM Student")
    List<Student> getAllStudent();
}

