package com.fairandsmart.consent.manager.model;

import java.util.Objects;

public class Controller {

    private boolean actingBehalfCompany = false;
    private String company;
    private String name;
    private String address;
    private String email;
    private String phoneNumber;

    public Controller() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Controller that = (Controller) o;
        return actingBehalfCompany == that.actingBehalfCompany &&
                Objects.equals(company, that.company) &&
                Objects.equals(name, that.name) &&
                Objects.equals(address, that.address) &&
                Objects.equals(email, that.email) &&
                Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actingBehalfCompany, company, name, address, email, phoneNumber);
    }

    @Override
    public String toString() {
        return "Controller{" +
                "actingBehalfCompany=" + actingBehalfCompany +
                ", company='" + company + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
