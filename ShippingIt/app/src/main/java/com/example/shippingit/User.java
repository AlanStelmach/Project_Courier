package com.example.shippingit;

public class User {
    private String Name;
    private String Surname;
    private String Email;
    private String PNumber;
    private String Id;
    private String Sex;
    private String Workplace;
    private String YOB;

    public User()
    {

    }

    public User(String name, String surname, String email, String PNumber, String id, String sex, String workplace, String YOB) {
        Name = name;
        Surname = surname;
        Email = email;
        this.PNumber = PNumber;
        Id = id;
        Sex = sex;
        Workplace = workplace;
        this.YOB = YOB;
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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPNumber() {
        return PNumber;
    }

    public void setPNumber(String PNumber) {
        this.PNumber = PNumber;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getWorkplace() {
        return Workplace;
    }

    public void setWorkplace(String workplace) {
        Workplace = workplace;
    }

    public String getYOB() {
        return YOB;
    }

    public void setYOB(String YOB) {
        this.YOB = YOB;
    }
}
