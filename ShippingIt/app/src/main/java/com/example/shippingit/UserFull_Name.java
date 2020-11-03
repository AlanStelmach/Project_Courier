package com.example.shippingit;

public class UserFull_Name {
    private String Name;
    private String Surname;
    private String Workplace;

    public UserFull_Name(String name, String surname, String workplace) {
        Name = name;
        Surname = surname;
        Workplace = workplace;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getWorkplace() {
        return Workplace;
    }

    public void setWorkplace(String workplace) {
        Workplace = workplace;
    }
}
