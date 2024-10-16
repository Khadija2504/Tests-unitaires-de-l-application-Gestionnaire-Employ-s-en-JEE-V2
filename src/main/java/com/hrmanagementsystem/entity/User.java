package com.hrmanagementsystem.entity;

import com.hrmanagementsystem.enums.Role;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private int salary;
    private Date birthday;
    private Date hireDate;
    private String position;
    private int kidsNum;
    private int totalSalary;
    private String situation;
    private String department;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(unique = true)
    private String nssu;
    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {}

    public User(String firstName, String lastName, String phoneNumber, int salary, Date birthday, Date hireDate, String position, int kidsNum, int totalSalary, String situation, String department, String email, String password, String nssu, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.salary = salary;
        this.birthday = birthday;
        this.hireDate = hireDate;
        this.position = position;
        this.kidsNum = kidsNum;
        this.totalSalary = totalSalary;
        this.situation = situation;
        this.department = department;
        this.email = email;
        this.password = password;
        this.nssu = nssu;
        this.role = role;
    }

    public boolean authenticate(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getKidsNum() {
        return kidsNum;
    }

    public void setKidsNum(int kidsNum) {
        this.kidsNum = kidsNum;
    }

    public int getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(int totalSalary) {
        this.totalSalary = totalSalary;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getNssu() {
        return nssu;
    }

    public void setNssu(String nssu) {
        this.nssu = nssu;
    }
}

