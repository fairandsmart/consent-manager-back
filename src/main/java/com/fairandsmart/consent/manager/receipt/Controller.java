package com.fairandsmart.consent.manager.receipt;

public class Controller {

    public boolean actingBehalfCompany;
    public String company;
    public String name;
    public String address;
    public String email;
    public String phoneNumber;

    public boolean isActingBehalfCompany() {
        return actingBehalfCompany;
    }

    public void setActingBehalfCompany(boolean actingBehalfCompany) {
        this.actingBehalfCompany = actingBehalfCompany;
    }

    public Controller withActingBehalfCompany(boolean actingBehalfCompany) {
        this.actingBehalfCompany = actingBehalfCompany;
        return this;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Controller withCompany(String company) {
        this.company = company;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Controller withName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Controller withAddress(String address) {
        this.address = address;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Controller withEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Controller withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
}
