package com.pkg.tintincustomer;

import com.google.firebase.firestore.DocumentReference;

public class SearchDataListModel {
    private Object menu;
    private Object name;

    public SearchDataListModel(Object menu, Object name) {
        this.menu = menu;
        this.name = name;
    }

    public Object getMenu() {
        return menu;
    }

    public void setMenu(Object menu) {
        this.menu = menu;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }
}