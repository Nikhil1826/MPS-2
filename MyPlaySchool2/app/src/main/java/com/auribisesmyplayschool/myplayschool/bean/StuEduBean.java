package com.auribisesmyplayschool.myplayschool.bean;

import java.io.Serializable;

public class StuEduBean implements Serializable {
    int stueduid,year;
    String examin,university,subjects,percent;

    public StuEduBean() {
        year =0;
        percent = "0";
    }

    public StuEduBean(int stueduid, int year, String examin, String university, String subjects, String percent) {
        this.stueduid = stueduid;
        this.year = year;
        this.examin = examin;
        this.university = university;
        this.subjects = subjects;
        this.percent = percent;
    }

    public int getStueduid() {
        return stueduid;
    }

    public void setStueduid(int stueduid) {
        this.stueduid = stueduid;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getExamin() {
        return examin;
    }

    public void setExamin(String examin) {
        this.examin = examin;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "StuEduBean{" +
                "stueduid=" + stueduid +
                ", year=" + year +
                ", examin='" + examin + '\'' +
                ", university='" + university + '\'' +
                ", subjects='" + subjects + '\'' +
                ", percent=" + percent +
                '}';
    }
}
