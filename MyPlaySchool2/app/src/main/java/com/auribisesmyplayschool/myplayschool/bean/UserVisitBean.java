package com.auribisesmyplayschool.myplayschool.bean;

import java.io.Serializable;

public class UserVisitBean implements Serializable {
    int enqVisitId, studentId, userId,userType;
    String visitDateTime,userName;

    public UserVisitBean(int enqVisitId, int studentId, int userId, String visitDateTime, int userType, String userName) {
        this.enqVisitId = enqVisitId;
        this.studentId = studentId;
        this.userId = userId;
        this.visitDateTime = visitDateTime;
        this.userType = userType;
        this.userName = userName;
    }

    public UserVisitBean() {
    }

    public int getEnqVisitId() {
        return enqVisitId;
    }

    public void setEnqVisitId(int enqVisitId) {
        this.enqVisitId = enqVisitId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getVisitDateTime() {
        return visitDateTime;
    }

    public void setVisitDateTime(String visitDateTime) {
        this.visitDateTime = visitDateTime;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "UserVisitBean{" +
                "enqVisitId=" + enqVisitId +
                ", studentId=" + studentId +
                ", userId=" + userId +
                ", userType=" + userType +
                ", visitDateTime='" + visitDateTime + '\'' +
                '}';
    }
}
