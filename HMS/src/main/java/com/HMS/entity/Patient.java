package com.HMS.entity;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
public class Patient {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String name;
    private int age;
    private String gender;
    private String phoneNumber;
    private String email;
    @Column(columnDefinition = "boolean default true")
    private boolean active = true;  // Added field for soft delete

    public Patient() {
        // Default constructor needed by JPA
    }

//    public Patient(int id, String name, int age, String gender, String phoneNumber, String email) {
//        this.id = id;
//        this.name = name;
//        this.age = age;
//        this.gender = gender;
//        this.phoneNumber = phoneNumber;
//        this.email=email;
//    }

    public int getId() {
        return this.id;
    }

    public void setId(int ID) {
        this.id = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String toString() {
        return "PatientId: " + id + ", Name: " + name + ", Age: " + age + ", Gender: " + gender + ", Phone Number: " + phoneNumber + ", Email: " + email;
    }

}
