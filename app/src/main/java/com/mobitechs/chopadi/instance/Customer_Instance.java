package com.mobitechs.chopadi.instance;

public class Customer_Instance {

    private static final Customer_Instance ourInstance = new Customer_Instance();

    private static String customerId ="";
    private static String selectedDate ="";

    public static Customer_Instance getInstance() {
        return ourInstance;
    }

    public Customer_Instance() {
    }


    public static String getCustomerId() {
        return customerId;
    }

    public static void setCustomerId(String cId) {
        customerId = cId;
    }
 public static String getSelectedDate() {
        return selectedDate;
    }

    public static void setSelectedDate(String date) {
        selectedDate = date;
    }

}
