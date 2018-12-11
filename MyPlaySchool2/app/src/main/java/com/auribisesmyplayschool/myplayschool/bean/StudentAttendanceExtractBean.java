package com.auribisesmyplayschool.myplayschool.bean;

import java.io.Serializable;

/**
 * Created by amandeep on 31/01/18.
 */

public class StudentAttendanceExtractBean implements Serializable {
    String stuName,studentId,status;

    public StudentAttendanceExtractBean() {
    }

    public StudentAttendanceExtractBean(String stuName, String studentId, String status) {
        this.stuName = stuName;
        this.studentId = studentId;
        this.status = status;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StudentAttendanceExtractBean{" +
                "stuName='" + stuName + '\'' +
                ", studentId='" + studentId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
