package com.auribisesmyplayschool.myplayschool.bean;

public class GridItemUserHomeBean {
    String title;
    int imgId;

    public GridItemUserHomeBean(String title, int imgId) {
        this.title = title;
        this.imgId = imgId;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
