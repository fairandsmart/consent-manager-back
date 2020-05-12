package com.fairandsmart.consent.manager.receipt;

public class Controller {

    private boolean isActingBehalfCompany;
    private String company;
    private String name;
    private String address;
    private String email;
    private String phoneNumber;

    public Controller() {
    }

    public boolean isActingBehalfCompany() {
        return isActingBehalfCompany;
    }

    public void setActingBehalfCompany(boolean actingBehalfCompany) {
        isActingBehalfCompany = actingBehalfCompany;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
