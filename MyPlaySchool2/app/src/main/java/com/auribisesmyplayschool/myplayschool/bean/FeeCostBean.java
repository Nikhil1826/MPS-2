package com.auribisesmyplayschool.myplayschool.bean;

import java.io.Serializable;

/**
 * Created by kshitij on 31/05/17.
 */

public class FeeCostBean implements Serializable {

    int feeCostId;
    int headId;
    int categoryId;
    int cost;
    int branchId;
    int feeType;
    int sellingAmount;
    int discount;
    int creditAmount;
    int creditAmountTemp;
    int feeStuId;
    int sellingCost;
    int admissionId;
    int studentId;
    int feePaidId;
    int totalSellingCost;
    int totalCreditAmount;
    int routeId;

    public int getNoOfMonths() {
        return noOfMonths;
    }

    public void setNoOfMonths(int noOfMonths) {
        this.noOfMonths = noOfMonths;
    }

    int noOfMonths;
    String categoryName;
    String headName;
    String invoiceDate;
    String startingDate;
    String endingDate;
    String cycleNumber;
    String otStartingDate;
    String otEndingDate;
    String aStartingDate;
    String aEndingDate;
    String mStartingDate;
    String mEndingDate;
    String tStartingDate;
    String tEndingDate;

    public String gettStartingDate() {
        return tStartingDate;
    }

    public void settStartingDate(String tStartingDate) {
        this.tStartingDate = tStartingDate;
    }

    public String gettEndingDate() {
        return tEndingDate;
    }

    public void settEndingDate(String tEndingDate) {
        this.tEndingDate = tEndingDate;
    }


    boolean isChecked=false,isPayFee=false;

    public FeeCostBean() {

    }

    public FeeCostBean(int feeCostId, int headId, int categoryId, int cost, int branchId, int feeType, int sellingAmount, int discount, int creditAmount, int creditAmountTemp, int feeStuId, int sellingCost, int admissionId, int studentId, int feePaidId, int totalSellingCost, int totalCreditAmount, String categoryName, String headName, String invoiceDate, String startingDate, String endingDate, String cycleNumber, String otStartingDate, String otEndingDate, String aStartingDate, String aEndingDate, String mStartingDate, String mEndingDate, boolean isChecked, boolean isPayFee, int routeId) {
        this.feeCostId = feeCostId;
        this.headId = headId;
        this.categoryId = categoryId;
        this.cost = cost;
        this.branchId = branchId;
        this.feeType = feeType;
        this.sellingAmount = sellingAmount;
        this.discount = discount;
        this.creditAmount = creditAmount;
        this.creditAmountTemp = 0;
        this.feeStuId = feeStuId;
        this.sellingCost = sellingCost;
        this.admissionId = admissionId;
        this.studentId = studentId;
        this.feePaidId = feePaidId;
        this.totalSellingCost = totalSellingCost;
        this.totalCreditAmount = totalCreditAmount;
        this.categoryName = categoryName;
        this.headName = headName;
        this.invoiceDate = invoiceDate;
        this.startingDate = "0000-00-00";
        this.endingDate = "0000-00-00";
// this.startingDate = startingDate;
//        this.endingDate = endingDate;
        this.cycleNumber = cycleNumber;
        this.otStartingDate = otStartingDate;
        this.otEndingDate = otEndingDate;
        this.aStartingDate = aStartingDate;
        this.aEndingDate = aEndingDate;
        this.mStartingDate = mStartingDate;
        this.mEndingDate = mEndingDate;
        this.isChecked = isChecked;
        this.isPayFee = isPayFee;
        this.routeId =routeId;
    }

    public int getCreditAmountTemp() {
        return creditAmountTemp;
    }

    public void setCreditAmountTemp(int creditAmountTemp) {
        this.creditAmountTemp = creditAmountTemp;
    }

    public String getOtStartingDate() {
        return otStartingDate;
    }

    public void setOtStartingDate(String otStartingDate) {
        this.otStartingDate = otStartingDate;
    }

    public String getOtEndingDate() {
        return otEndingDate;
    }

    public void setOtEndingDate(String otEndingDate) {
        this.otEndingDate = otEndingDate;
    }

    public String getaStartingDate() {
        return aStartingDate;
    }

    public void setaStartingDate(String aStartingDate) {
        this.aStartingDate = aStartingDate;
    }

    public String getaEndingDate() {
        return aEndingDate;
    }

    public void setaEndingDate(String aEndingDate) {
        this.aEndingDate = aEndingDate;
    }

    public String getmStartingDate() {
        return mStartingDate;
    }

    public void setmStartingDate(String mStartingDate) {
        this.mStartingDate = mStartingDate;
    }

    public String getmEndingDate() {
        return mEndingDate;
    }

    public void setmEndingDate(String mEndingDate) {
        this.mEndingDate = mEndingDate;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getFeePaidId() {
        return feePaidId;
    }

    public void setFeePaidId(int feePaidId) {
        this.feePaidId = feePaidId;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
    }

    public String getCycleNumber() {
        return cycleNumber;
    }

    public void setCycleNumber(String cycleNumber) {
        this.cycleNumber = cycleNumber;
    }

    public int getFeeStuId() {
        return feeStuId;
    }

    public void setFeeStuId(int feeStuId) {
        this.feeStuId = feeStuId;
    }

    public int getSellingCost() {
        return sellingCost;
    }

    public void setSellingCost(int sellingCost) {
        this.sellingCost = sellingCost;
    }

    public int getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(int admissionId) {
        this.admissionId = admissionId;
    }

    public int getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(int creditAmount) {
        this.creditAmount = creditAmount;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getFeeType() {
        return feeType;
    }

    public void setFeeType(int feeType) {
        this.feeType = feeType;
    }

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
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

    public int getSellingAmount() {
        return sellingAmount;
    }

    public void setSellingAmount(int sellingAmount) {
        this.sellingAmount = sellingAmount;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public boolean isPayFee() {
        return isPayFee;
    }

    public void setPayFee(boolean payFee) {
        isPayFee = payFee;
    }

    public int getTotalSellingCost() {
        return totalSellingCost;
    }

    public void setTotalSellingCost(int totalSellingCost) {
        this.totalSellingCost = totalSellingCost;
    }

    public int getTotalCreditAmount() {
        return totalCreditAmount;
    }

    public void setTotalCreditAmount(int totalCreditAmount) {
        this.totalCreditAmount = totalCreditAmount;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    @Override
    public String toString() {
        return "FeeCostBean{" +
                "feeCostId=" + feeCostId +
                ", headId=" + headId +
                ", categoryId=" + categoryId +
                ", cost=" + cost +
                ", branchId=" + branchId +
                ", feeType=" + feeType +
                ", sellingAmount=" + sellingAmount +
                ", discount=" + discount +
                ", creditAmount=" + creditAmount +
                ", creditAmountTemp=" + creditAmountTemp +
                ", feeStuId=" + feeStuId +
                ", sellingCost=" + sellingCost +
                ", admissionId=" + admissionId +
                ", studentId=" + studentId +
                ", feePaidId=" + feePaidId +
                ", totalSellingCost=" + totalSellingCost +
                ", totalCreditAmount=" + totalCreditAmount +
                ", categoryName='" + categoryName + '\'' +
                ", headName='" + headName + '\'' +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", startingDate='" + startingDate + '\'' +
                ", endingDate='" + endingDate + '\'' +
                ", cycleNumber='" + cycleNumber + '\'' +
                ", otStartingDate='" + otStartingDate + '\'' +
                ", otEndingDate='" + otEndingDate + '\'' +
                ", aStartingDate='" + aStartingDate + '\'' +
                ", aEndingDate='" + aEndingDate + '\'' +
                ", mStartingDate='" + mStartingDate + '\'' +
                ", mEndingDate='" + mEndingDate + '\'' +
                ", isChecked=" + isChecked +
                ", isPayFee=" + isPayFee +
                ", routeId=" + routeId +
                '}';
    }
}
