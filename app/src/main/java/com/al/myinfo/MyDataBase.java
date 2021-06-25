package com.al.myinfo;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities =  {Student.class} , version = 2 , exportSchema = false)
public abstract class MyDataBase extends RoomDatabase {
    public abstract DAO dao();
}
