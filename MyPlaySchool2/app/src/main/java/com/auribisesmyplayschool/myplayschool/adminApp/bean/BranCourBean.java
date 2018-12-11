package com.auribisesmyplayschool.myplayschool.adminApp.bean;

import java.io.Serializable;

public class BranCourBean implements Serializable {
    int branCourId, branchId, courseId, courseDiscount, courseStatus, adminId;
    String courseFees,courseDuration,courseHours,courseDesc,courseName;

    public BranCourBean() {
    }

    public BranCourBean(int branCourId, int branchId, int courseId, int courseDiscount,
                        int courseStatus, int adminId, String courseFees, String courseDuration,
                        String courseHours, String courseDesc,String courseName) {
        this.branCourId = branCourId;
        this.branchId = branchId;
        this.courseId = courseId;
        this.courseDiscount = courseDiscount;
        this.courseStatus = courseStatus;
        this.adminId = adminId;
        this.courseFees = courseFees;
        this.courseDuration = courseDuration;
        this.courseHours = courseHours;
        this.courseDesc = courseDesc;
        this.courseName = courseName;
    }

    public int getBranCourId() {
        return branCourId;
    }

    public void setBranCourId(int branCourId) {
        this.branCourId = branCourId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getCourseDiscount() {
        return courseDiscount;
    }

    public void setCourseDiscount(int courseDiscount) {
        this.courseDiscount = courseDiscount;
    }

    public int getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(int courseStatus) {
        this.courseStatus = courseStatus;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getCourseFees() {
        return courseFees;
    }

    public void setCourseFees(String courseFees) {
        this.courseFees = courseFees;
    }

    public String getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(String courseDuration) {
        this.courseDuration = courseDuration;
    }

    public String getCourseHours() {
        return courseHours;
    }

    public void setCourseHours(String courseHours) {
        this.courseHours = courseHours;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public String toString() {
        return "BranCourBean{" +
                "branCourId=" + branCourId +
                ", branchId=" + branchId +
                ", courseId=" + courseId +
                ", courseDiscount=" + courseDiscount +
                ", courseStatus=" + courseStatus +
                ", adminId=" + adminId +
                ", courseFees='" + courseFees + '\'' +
                ", courseDuration='" + courseDuration + '\'' +
                ", courseHours='" + courseHours + '\'' +
                ", courseDesc='" + courseDesc + '\'' +
                '}';
    }
}
