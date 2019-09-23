package com.pkg.tintincustomer;

public class MenuDataModel {
    private String Type;
    private String Menu;
    private String Cost;
    private String CookName;

    public String getCookName() {
        return CookName;
    }

    public void setCookName(String cookName) {
        CookName = cookName;
    }

    public MenuDataModel() {

    }

    public MenuDataModel(String type, String menu, String cost) {
        Type = type;
        Menu = menu;
        Cost = cost;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getMenu() {
        return Menu;
    }

    public void setMenu(String menu) {
        Menu = menu;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }
}
