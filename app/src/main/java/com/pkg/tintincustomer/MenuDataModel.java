package com.pkg.tintincustomer;

public class MenuDataModel {
    private String type;
    private String menu;
    private String cost;

    public MenuDataModel(String type, String menu, String cost) {
        this.type = type;
        this.menu = menu;
        this.cost = cost;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
