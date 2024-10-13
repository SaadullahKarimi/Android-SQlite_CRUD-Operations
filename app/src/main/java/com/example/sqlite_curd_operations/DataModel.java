package com.example.sqlite_curd_operations;

import androidx.annotation.NonNull;

public class DataModel {
    private int id;
    private String Name;
    private int Age;

    // Constructor
    public DataModel(int id, String name, int age) {
        this.id = id;
        Name = name;
        this.Age = age;
    }

    @NonNull
    @Override
    public String toString() {
        return "DataModel{" +
                "id=" + id +
                ", Name='" + Name + '\'' +
                ", age=" + Age +
                '}';
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }
}

