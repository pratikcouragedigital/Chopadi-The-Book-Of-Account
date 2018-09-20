package com.mobitechs.chopadi.model;

public class Bill_Items {


    public String billId,billDate;
    public String customerId, customerName, date, entryDate;
    public String productName, productPrice, productQty, totalPrice, day, month, year;


    public Bill_Items() {
    }


    public Bill_Items(String billId,String billDate, String customerId, String customerName, String entryDate, String selectedDate, String productName, String productPrice, String productQty, String pTotal, String day, String month, String year) {

        this.billId = billId;
        this.billDate = billDate;
        this.customerId = customerId;
        this.customerName = customerName;
        this.date = date;
        this.entryDate = entryDate;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQty = productQty;
        this.totalPrice = totalPrice;
        this.day = day;
        this.month = month;
        this.year = year;

    }


    public String getbillId() {
        return billId;
    }

    public void setbillId(String billId) {
        this.billId = billId;
    }

    public String getbillDate() {
        return billDate;
    }

    public void setbillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getcustomerId() {
        return customerId;
    }

    public void setcustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getcustomerName() {
        return customerName;
    }

    public void setcustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getentryDate() {
        return entryDate;
    }

    public void setentryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
    }

    public String getproductName() {
        return productName;
    }

    public void setproductName(String productName) {
        this.productName = productName;
    }

    public String getproductPrice() {
        return productPrice;
    }

    public void setproductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getproductQty() {
        return productQty;
    }

    public void setproductQty(String productQty) {
        this.productQty = productQty;
    }


    public String gettotalPrice() {
        return totalPrice;
    }

    public void settotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getday() {
        return day;
    }

    public void setday(String day) {
        this.day = day;
    }

    public String getmonth() {
        return month;
    }

    public void setmonth(String month) {
        this.month = month;
    }

    public String getyear() {
        return year;
    }

    public void setyear(String year) {
        this.year = year;
    }

}