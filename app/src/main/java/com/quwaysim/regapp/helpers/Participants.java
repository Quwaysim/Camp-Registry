package com.quwaysim.regapp.helpers;

public class Participants {
    private String name;
    private String email;
    private String phone;
    private String school;
    private String dept;
    private String level;
    private String house;
    private String gender;
    private String status;
    private String bankAmount;
    private String cashAmount;
    private String reg_by;

    public Participants(String name, String email, String phone, String school, String dept, String level, String house, String gender, String status, String bankAmount, String cashAmount, String reg_by) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.school = school;
        this.dept = dept;
        this.level = level;
        this.house = house;
        this.gender = gender;
        this.status = status;
        this.bankAmount = bankAmount;
        this.cashAmount = cashAmount;
        this.reg_by = reg_by;
    }

    public Participants() {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReg_by() {
        return reg_by;
    }

    public void setReg_by(String reg_by) {
        this.reg_by = reg_by;
    }

    public String getBankAmount() {
        return bankAmount;
    }

    public void setBankAmount(String bankAmount) {
        this.bankAmount = bankAmount;
    }

    public String getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(String cashAmount) {
        this.cashAmount = cashAmount;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    @Override
    public String toString() {
        return "Participants{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", school='" + school + '\'' +
                ", dept='" + dept + '\'' +
                ", level='" + level + '\'' +
                ", house='" + house + '\'' +
                ", gender='" + gender + '\'' +
                ", status='" + status + '\'' +
                ", bankAmount='" + bankAmount + '\'' +
                ", cashAmount='" + cashAmount + '\'' +
                ", reg_by='" + reg_by + '\'' +
                '}';
    }
}
