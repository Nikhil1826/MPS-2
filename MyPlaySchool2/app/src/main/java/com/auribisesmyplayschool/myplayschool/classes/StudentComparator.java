package com.auribisesmyplayschool.myplayschool.classes;

import com.auribisesmyplayschool.myplayschool.bean.StudentBean;

import java.util.Comparator;

/**
 * Created by Aman on 7/24/2017.
 */

public class StudentComparator implements Comparator<StudentBean> {
    int sortMethod = 0;

    public StudentComparator() {

    }

    public StudentComparator(int sortMethod) {
        this.sortMethod = sortMethod;
    }

    @Override
    public int compare(StudentBean studentBean, StudentBean t1) {
        if (sortMethod == 1)
            return studentBean.getJoinDate().compareTo(t1.getStartingDate());
        else if (sortMethod == 2)
            return studentBean.getDate().compareTo(t1.getFinishDate());
        else if (sortMethod == 3)
            return studentBean.getDate().compareTo(t1.getJoinDate());
        else
            return studentBean.getStuName().compareTo(t1.getStuName());

    }
}


