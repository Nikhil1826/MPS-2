package com.auribisesmyplayschool.myplayschool.adminApp.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kshitij on 31/05/17.
 */

public class FeeHeadBean implements Serializable {

    int headId,feeType,branchId,adminId;
    String headName;
    ArrayList<FeeCostBean> feeCostBeanArrayList;


    public int getHeadId() {
        return headId;
    }

    public void setHeadId(int headId) {
        this.headId = headId;
    }

    public int getFeeType() {
        return feeType;
    }

    public void setFeeType(int feeType) {
        this.feeType = feeType;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public ArrayList<FeeCostBean> getFeeCostBeanArrayList() {
        return feeCostBeanArrayList;
    }

    public void setFeeCostBeanArrayList(ArrayList<FeeCostBean> feeCostBeanArrayList) {
        this.feeCostBeanArrayList = feeCostBeanArrayList;
    }

    @Override
    public String toString() {
        return "FeeHeadBean{" +
                "headId=" + headId +
                ", feeType=" + feeType +
                ", branchId=" + branchId +
                ", adminId=" + adminId +
                ", headName='" + headName + '\'' +
                ", feeCostBeanArrayList=" + feeCostBeanArrayList +
                '}';
    }
}
