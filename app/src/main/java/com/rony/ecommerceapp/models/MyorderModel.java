package com.rony.ecommerceapp.models;

public class MyorderModel {

    String productName;
    String productPrice;
    int totalPrice;
    String totalQuantity;
    String setDocumentID;
    public MyorderModel() {
    }

    public MyorderModel(String productName, String productPrice, int totalPrice, String totalQuantity) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
    }

    public MyorderModel(String setDocumentID) {
        this.setDocumentID = setDocumentID;
    }

    public String getSetDocumentID() {
        return setDocumentID;
    }

    public void setSetDocumentID(String setDocumentID) {
        this.setDocumentID = setDocumentID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

}
