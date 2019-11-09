package com.team.szkielet.login;

import android.net.Uri;

public class Person {
    private String name;
    private String email;
    private String ID;
    private Uri photo;

    public Person(String name, String email, String ID, Uri photo) {
        this.name = name;
        this.email = email;
        this.ID = ID;
        this.photo = photo;
    }

    public Uri getPhoto() {
        return photo;
    }

    public void setPhoto(Uri photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
