package com.auribisesmyplayschool.myplayschool.bean;

import java.io.Serializable;

/**
 * Created by amandeep on 23/08/17.
 */

public class AccountBean implements Serializable {
    int accountId,amount,inOutSignal,studentId,inOutType,admissionId,cycleNumber,signal,receiptId;
    String date;
    String time;
    String neftTransaction;
    String neftDate;
    String chequeNo;
    String chequeDate;

    public AccountBean() {
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getInOutSignal() {
        return inOutSignal;
    }

    public void setInOutSignal(int inOutSignal) {
        this.inOutSignal = inOutSignal;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getInOutType() {
        return inOutType;
    }

    public void setInOutType(int inOutType) {
        this.inOutType = inOutType;
    }

    public int getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(int admissionId) {
        this.admissionId = admissionId;
    }

    public int getCycleNumber() {
        return cycleNumber;
    }

    public void setCycleNumber(int cycleNumber) {
        this.cycleNumber = cycleNumber;
    }

    public int getSignal() {
        return signal;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
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

    public String getNeftTransaction() {
        return neftTransaction;
    }

    public void setNeftTransaction(String neftTransaction) {
        this.neftTransaction = neftTransaction;
    }

    public String getNeftDate() {
        return neftDate;
    }

    public void setNeftDate(String neftDate) {
        this.neftDate = neftDate;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }
}
