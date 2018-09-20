package com.mobitechs.chopadi.model;

public class Customer_Items {

    public String custName;
    public String custNo;
    public String custId;


    public Customer_Items() {
    }

    public Customer_Items(String custName, String custId, String custNo) {

        this.custId = custId;
        this.custName = custName;
        this.custNo = custNo;


    }
    

    public String getcustNo() {
        return custNo;
    }

    public void setcustNo(String custNo) {
        this.custNo = custNo;
    }

    public String getcustName() {
        return custName;
    }

    public void setcustName(String custName) {
        this.custName = custName;
    }

    public String getcustId() {
        return custId;
    }

    public void setcustId(String custId) {
        this.custId = custId;
    }


}