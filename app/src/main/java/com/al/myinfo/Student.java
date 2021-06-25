package com.al.myinfo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Student implements Serializable {


    @PrimaryKey(autoGenerate = true)
    int stuId;
    String id,name,contact,gender,experience,chineseLevel,interest,dateOfBirth,imageUri;

    public Student( String id, String name, String contact, String gender, String experience, String chineseLevel, String interest, String dateOfBirth, String imageUri) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.gender = gender;
        this.experience = experience;
        this.chineseLevel = chineseLevel;
        this.interest = interest;
        this.dateOfBirth = dateOfBirth;
        this.imageUri = imageUri;
    }


    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getStuId() {
        return stuId;
    }

    public void setStuId(int stuId) {
        this.stuId = stuId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getChineseLevel() {
        return chineseLevel;
    }

    public void setChineseLevel(String chineseLevel) {
        this.chineseLevel = chineseLevel;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
}
