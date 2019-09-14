package com.pkg.tintincustomer;
/**  Its supplier details and used in Homerecycler view adapter**/
public class HomeDataModel {
    private String Name;
    private String HouseFlatNo;
    private String Landmark;
    private String City;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getHouseFlatNo() {
        return HouseFlatNo;
    }

    public void setHouseFlatNo(String houseFlatNo) {
        HouseFlatNo = houseFlatNo;
    }

    public String getLandmark() {
        return Landmark;
    }

    public void setLandmark(String landmark) {
        Landmark = landmark;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public HomeDataModel() {
    }

    public HomeDataModel(String name, String houseFlatNo, String landmark, String city) {
        Name = name;
        HouseFlatNo = houseFlatNo;
        Landmark = landmark;
        City = city;
    }

    }