package com.mobitechs.chopadi.instance;

public class Bill_Amount_instance {
    
    public static final Bill_Amount_instance ourInstance = new Bill_Amount_instance();

    public static Bill_Amount_instance getInstance() {
        return ourInstance;
    }

    public static int grandTotal =0;
    public static String fromDate ="";
    public static String toDate ="";
    public static String lastBillRecordId ="";

    public Bill_Amount_instance() {
    }

    public static int getGrandTotal() {
        return grandTotal;
    }

    public static void setGrandTotal(int gt) {
        grandTotal = gt;
    }


    public static String getFromDate() {
        return fromDate;
    }

    public static void setFromDate(String fDate) {
        fromDate = fDate;
    }

    public static String getToDate() {
        return toDate;
    }

    public static void setToDate(String tDate) {
        toDate = tDate;
    }

    public static String getLastBillRecordId() {
        return lastBillRecordId;
    }

    public static void setLastBillRecordId(String billId) {
        lastBillRecordId = billId;
    }
    
}
