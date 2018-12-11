package com.auribisesmyplayschool.myplayschool.bean;

import java.io.Serializable;

/**
 * Created by kshitij on 05/06/17.
 */

public class FeeCategoryBean implements Serializable {

    int feeCategoryId,branchId;
    String categoryName;

    public FeeCategoryBean() {
    }

    public int getFeeCategoryId() {
        return feeCategoryId;
    }

    public void setFeeCategoryId(int feeCategoryId) {
        this.feeCategoryId = feeCategoryId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "FeeCategoryBean{" +
                "feeCategoryId=" + feeCategoryId +
                ", branchId=" + branchId +
                ", categoryBean='" + categoryName + '\'' +
                '}';
    }
}
