package com.pkg.tintincustomer;

public class SearchDataModel {
    private String cookname;
    private String menu;

    public SearchDataModel(String cookname, String menu) {
        this.cookname = cookname;
        this.menu = menu;
    }

    public String getCookname() {
        return cookname;
    }

    public void setCookname(String cookname) {
        this.cookname = cookname;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }
}
