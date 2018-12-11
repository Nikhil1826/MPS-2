package com.auribisesmyplayschool.myplayschool.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


//batchGroup

public class TeacherBean implements Serializable {

    int userId,userType,userStatus,adminId,branchId,userSalary,branchStatus,batchCount;
    String userName,userEmail,userContact,userPassword,branchName,branchAddress,branchContact,img_url,msgCode;
    boolean enabled;
    List<String> batchGroup;
    List<Integer> branCourId;

    public List<Integer> getBranCourId() {
        return branCourId;
    }

    public void setBranCourId(List<Integer> branCourId) {
        this.branCourId = branCourId;
    }



    public List<String> getBatchGroup() {
        return batchGroup;
    }

    public void setBatchGroup(List<String> batchGroup) {
        this.batchGroup = batchGroup;
    }


    //String[] batchGroup;


    public TeacherBean() {
    }


   /* public ArrayList<String> getBatchGroup() {
        return batchGroup;
    }

    public void setBatchGroup(ArrayList<String> batchGroup) {
        this.batchGroup = batchGroup;
    }*/

    public int getBatchCount() {
        return batchCount;
    }

    public void setBatchCount(int batchCount) {
        this.batchCount = batchCount;
    }

   /* public String getBatchGroup() {
        return batchGroup;
    }

    public void setBatchGroup(String batchGroup) {
        this.batchGroup = batchGroup;
    }*/

    /*public String[] getBatchGroup() {
        return batchGroup;
    }

    public void setBatchGroup(String[] batchGroup) {
        this.batchGroup = batchGroup;
    }*/

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getUserSalary() {
        return userSalary;
    }

    public void setUserSalary(int userSalary) {
        this.userSalary = userSalary;
    }

    public int getBranchStatus() {
        return branchStatus;
    }

    public void setBranchStatus(int branchStatus) {
        this.branchStatus = branchStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserContact() {
        return userContact;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getBranchContact() {
        return branchContact;
    }

    public void setBranchContact(String branchContact) {
        this.branchContact = branchContact;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "TeacherBean{" +
                "userId=" + userId +
                ", userType=" + userType +
                ", userStatus=" + userStatus +
                ", adminId=" + adminId +
                ", branchId=" + branchId +
                ", userSalary=" + userSalary +
                ", branchStatus=" + branchStatus +
                ", batchCount=" + batchCount +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userContact='" + userContact + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", branchName='" + branchName + '\'' +
                ", branchAddress='" + branchAddress + '\'' +
                ", branchContact='" + branchContact + '\'' +
                ", img_url='" + img_url + '\'' +
                ", msgCode='" + msgCode + '\'' +
                ", batchGroup='" + batchGroup + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
