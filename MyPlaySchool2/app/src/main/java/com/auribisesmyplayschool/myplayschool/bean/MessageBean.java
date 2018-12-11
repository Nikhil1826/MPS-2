package com.auribisesmyplayschool.myplayschool.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Eshaan on 08-Dec-16.
 */
public class MessageBean implements Serializable{
    int id;
    int type;
    int userId;
    int batchId;

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    int branchId;
    String message;
    String date;
    String time;
    String batch_title;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    String userName;
    List<Integer> audience;

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public String getBatch_title() {
        return batch_title;
    }

    public void setBatch_title(String batch_title) {
        this.batch_title = batch_title;
    }

    public List<Integer> getAudience() {
        return audience;
    }

    public void setAudience(List<Integer> audience) {
        this.audience = audience;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
}
