package com.auribisesmyplayschool.myplayschool.adminApp.bean;

import java.io.Serializable;

/**
 * Created by kshitij on 31/05/17.
 */

public class FeeCostBean implements Serializable {

    int feeCostId,headId,categoryId,cost,branchId;
    String categoryName;

    public FeeCostBean() {
    }

    public int getFeeCostId() {
        return feeCostId;
    }

    public void setFeeCostId(int feeCostId) {
        this.feeCostId = feeCostId;
    }

    public int getHeadId() {
        return headId;
    }

    public void setHeadId(int headId) {
        this.headId = headId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
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
        return "FeeCostBean{" +
                "feeCostId=" + feeCostId +
                ", headId=" + headId +
                ", categoryId=" + categoryId +
                ", cost=" + cost +
                ", branchId=" + branchId +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
