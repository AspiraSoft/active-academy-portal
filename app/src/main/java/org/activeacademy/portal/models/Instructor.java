package org.activeacademy.portal.models;


public class Instructor extends RemoteObject {

    private String name;
    private String phone;
    private String email;
    private String position;
    private String joinDate;
    private Integer salary;
    private Integer shareInFee;

    public Instructor() {

    }

    public String getEmail() {
        return email;
    }

    public Instructor setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Instructor setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public Instructor setJoinDate(String joinDate) {
        this.joinDate = joinDate;
        return this;
    }

    public String getName() {
        return name;
    }

    public Instructor setName(String name) {
        this.name = name;
        return this;
    }

    public String getPosition() {
        return position;
    }

    public Instructor setPosition(String position) {
        this.position = position;
        return this;
    }

    public Integer getShareInFee() {
        return shareInFee;
    }

    public Instructor setShareInFee(Integer shareInFee) {
        this.shareInFee = shareInFee;
        return this;
    }

    public Integer getSalary() {
        return salary;
    }

    public Instructor setSalary(Integer salary) {
        this.salary = salary;
        return this;
    }
}