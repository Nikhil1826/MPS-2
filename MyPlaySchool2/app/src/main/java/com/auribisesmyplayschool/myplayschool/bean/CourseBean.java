package com.auribisesmyplayschool.myplayschool.bean;

import java.io.Serializable;

/**
 * Created by Amandeep on 12-May-16.
 */
public class CourseBean implements Serializable {
    String courseName,courseFees,courseDuration,courseHours, courseDesc,courseDiscount;
    int courseId,branchId,courseStatus,branCourId;

    public CourseBean() {
    }

    public CourseBean(String courseName, String courseFees, String courseDuration,
                      String courseHours, String courseDesc, String courseDiscount, int courseId
    , int branchId, int courseStatus, int branCourId) {
        this.courseName = courseName;
        this.courseFees = courseFees;
        this.courseDuration = courseDuration;
        this.courseHours = courseHours;
        this.courseDesc = courseDesc;
        this.courseDiscount = courseDiscount;
        this.courseId = courseId;
        this.courseStatus = courseStatus;
        this.branCourId=branCourId;
    }

    public int getBranCourId() {
        return branCourId;
    }

    public void setBranCourId(int branCourId) {
        this.branCourId = branCourId;
    }

    public int getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(int courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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

    public String getCourseDiscount() {
        return courseDiscount;
    }

    public void setCourseDiscount(String courseDiscount) {
        this.courseDiscount = courseDiscount;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    @Override
    public String toString() {
        return "CourseBean{" +
                "courseName='" + courseName + '\'' +
                ", courseFees='" + courseFees + '\'' +
                ", courseDuration='" + courseDuration + '\'' +
                ", courseHours='" + courseHours + '\'' +
                ", courseDesc='" + courseDesc + '\'' +
                ", courseDiscount='" + courseDiscount + '\'' +
                ", courseId=" + courseId +
                ", branchId=" + branchId +
                ", courseStatus=" + courseStatus +
                ", branCourId=" + branCourId +
                '}';
    }
}
