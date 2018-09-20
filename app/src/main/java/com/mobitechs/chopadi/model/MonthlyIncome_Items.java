package com.mobitechs.chopadi.model;

public class MonthlyIncome_Items  {


    public String date,monthName;
    public String totalPrice, day, month, year;


    public MonthlyIncome_Items() {

    }

    public MonthlyIncome_Items(String monthName,String date, String totalPrice, String day, String month, String year) {

        this.date = date;
        this.totalPrice = totalPrice;
        this.monthName = monthName;
        this.day = day;
        this.month = month;
        this.year = year;

    }



    public String getmonthName() {
        return monthName;
    }

    public void setmonthName(String monthName) {
        this.monthName = monthName;
    }


    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
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
