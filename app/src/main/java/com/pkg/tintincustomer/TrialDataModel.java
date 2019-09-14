package com.pkg.tintincustomer;

public class TrialDataModel {
    private String Name;
    private String PhoneNo;
    private String City;

    public TrialDataModel() {
    }

    public TrialDataModel(String name, String phoneNo, String city) {
        Name = name;
        PhoneNo = phoneNo;
        City = city;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}
