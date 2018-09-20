package com.mobitechs.chopadi.model;

public class Bill_History_Items {


    public String billId,fromDate;
    public String customerId, customerName, toDate, entryDate;
    public String billAmount, paidAmount, balanceAmount;


    public Bill_History_Items() {
    }


    public Bill_History_Items(String billId,String fromDate, String customerId, String customerName, String entryDate, String toDate, String billAmount, String paidAmount, String balanceAmount) {

        this.billId = billId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.customerId = customerId;
        this.customerName = customerName;

        this.entryDate = entryDate;
        this.billAmount = billAmount;
        this.paidAmount = paidAmount;
        this.balanceAmount = balanceAmount;

    }


    public String getbillId() {
        return billId;
    }

    public void setbillId(String billId) {
        this.billId = billId;
    }

    public String getfromDate() {
        return fromDate;
    }

    public void setfromDate(String fromDate) {
        this.fromDate = fromDate;
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

    public String gettoDate() {
        return toDate;
    }

    public void settoDate(String toDate) {
        this.toDate = toDate;
    }

    public String getbillAmount() {
        return billAmount;
    }

    public void setbillAmount(String billAmount) {
        this.billAmount = billAmount;
    }

    public String getpaidAmount() {
        return paidAmount;
    }

    public void setpaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getbalanceAmount() {
        return balanceAmount;
    }

    public void setbalanceAmount(String balanceAmount) {
        this.balanceAmount = balanceAmount;
    }


}