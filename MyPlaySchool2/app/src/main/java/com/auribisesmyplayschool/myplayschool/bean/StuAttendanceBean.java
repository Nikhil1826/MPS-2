package com.auribisesmyplayschool.myplayschool.bean;

import java.io.Serializable;

/**
 * Created by amandeep on 31/01/18.
 */

public class StuAttendanceBean implements Serializable{
    private int stuAttId,userId,branchId,batchId,status;
    private String date;
    private String time;
    private String attendance;
    private String userName;

    public String getBatch_title() {
        return batch_title;
    }

    public void setBatch_title(String batch_title) {
        this.batch_title = batch_title;
    }

    private String batch_title;
    private boolean isMarked=true;

    public StuAttendanceBean() {
    }

    public StuAttendanceBean(int stuAttId, int userId, int branchId, String date, String time,
                             String attendance, int batchId, boolean isMarked, String userName,
                             int status) {
        this.stuAttId = stuAttId;
        this.userId = userId;
        this.branchId = branchId;
        this.date = date;
        this.time = time;
        this.attendance = attendance;
        this.batchId=batchId;
        this.isMarked = isMarked;
        this.userName = userName;
        this.status = status;
    }

    public int getStuAttId() {
        return stuAttId;
    }

    public void setStuAttId(int stuAttId) {
        this.stuAttId = stuAttId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StuAttendanceBean{" +
                "stuAttId=" + stuAttId +
                ", userId=" + userId +
                ", branchId=" + branchId +
                ", batchId=" + batchId +
                ", status=" + status +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", attendance='" + attendance + '\'' +
                ", userName='" + userName + '\'' +
                ", isMarked=" + isMarked +
                '}';
    }
}
