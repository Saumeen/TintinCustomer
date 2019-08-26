package com.pkg.tintincustomer;

public class HomeDataModel {
    private String suppliername;
    private String supplierflatno;
    private String supplierlandmark;
    private String suppliercity;

    public HomeDataModel(String suppliername, String supplierflatno, String supplierlandmark, String suppliercity) {
        this.suppliername = suppliername;
        this.supplierflatno = supplierflatno;
        this.supplierlandmark = supplierlandmark;
        this.suppliercity = suppliercity;
    }

    public String getSuppliername() {
        return suppliername;
    }

    public void setSuppliername(String suppliername) {
        this.suppliername = suppliername;
    }

    public String getSupplierflatno() {
        return supplierflatno;
    }

    public void setSupplierflatno(String supplierflatno) {
        this.supplierflatno = supplierflatno;
    }

    public String getSupplierlandmark() {
        return supplierlandmark;
    }

    public void setSupplierlandmark(String supplierlandmark) {
        this.supplierlandmark = supplierlandmark;
    }

    public String getSuppliercity() {
        return suppliercity;
    }

    public void setSuppliercity(String suppliercity) {
        this.suppliercity = suppliercity;
    }
}