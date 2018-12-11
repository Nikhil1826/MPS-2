package com.auribisesmyplayschool.myplayschool.bean;

import java.io.Serializable;

public class AdmissionBean implements Serializable {
    int admissionId;
    int branchId;
    int branCourId;
    int batchId;
    int studentId;
    int admStatus;
    int approved;
    int type;
    int feeCategorySelected;
    int totalSellingCost;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    String courseName;


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

    int totalCreditAmount;
    String courseTime,date,joinDate,description,startingDate,finishDate,remarks;

    public int getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(int admissionId) {
        this.admissionId = admissionId;
    }



    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getBranCourId() {
        return branCourId;
    }

    public void setBranCourId(int branCourId) {
        this.branCourId = branCourId;
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getAdmStatus() {
        return admStatus;
    }

    public void setAdmStatus(int admStatus) {
        this.admStatus = admStatus;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFeeCategorySelected() {
        return feeCategorySelected;
    }

    public void setFeeCategorySelected(int feeCategorySelected) {
        this.feeCategorySelected = feeCategorySelected;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
