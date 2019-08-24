package com.pkg.tintincustomer;

public class MenuDataModel {
    private String suppliername;
    private String menu;
    private String cost;

    public MenuDataModel(String suppliername, String menu, String cost) {
        this.suppliername = suppliername;
        this.menu = menu;
        this.cost = cost;
    }

    public String getSuppliername() {
        return suppliername;
    }

    public void setSuppliername(String suppliername) {
        this.suppliername = suppliername;
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
