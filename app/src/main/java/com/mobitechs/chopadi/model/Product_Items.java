package com.mobitechs.chopadi.model;

public class Product_Items {

    public String productName;
    public String productId;
    public String price;


    public Product_Items() {
    }

    public Product_Items(String productName, String productId, String price) {

        this.productId = productId;
        this.productName = productName;
        this.price = price;


    }
    public String getprice() {
        return price;
    }

    public void setprice(String price) {
        this.price = price;
    }

    public String getproductName() {
        return productName;
    }

    public void setproductName(String productName) {
        this.productName = productName;
    }

    public String getproductId() {
        return productId;
    }

    public void setproductId(String productId) {
        this.productId = productId;
    }


}