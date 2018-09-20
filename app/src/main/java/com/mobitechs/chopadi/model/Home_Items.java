package com.mobitechs.chopadi.model;

public class Home_Items {

    public String title;
    public int gridId;
    public int firstImagePath;


    public Home_Items() {
    }

    public Home_Items(String title, int firstImagePath, int gridId) {

        this.gridId = gridId;
        this.title = title;
        this.firstImagePath = firstImagePath;

    }

    public int getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(Integer firstImagePath) {
        this.firstImagePath = firstImagePath;
    }


    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public int getgridId() {
        return gridId;
    }

    public void setgridId(int gridId) {
        this.gridId = gridId;
    }


}