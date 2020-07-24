package com.example.splitmanager;

import android.widget.EditText;

import java.util.Date;

public class UserHelper {

    String pname , bname ,phone, status;
    Float carat , prize , percentage , amount;

    int invoice;
    Date today;
    String  due_date;

    public UserHelper(int invoice, Float amount) {
        this.invoice=invoice;
        this.amount = amount;
    }

    public UserHelper() {
    }

    public UserHelper(String pname, String bname, String phone, String status, Float carat, Float prize, Float percentage, Float amount, int invoice, String due_date, Date today) {
        this.pname = pname;
        this.bname = bname;
        this.phone = phone;
        this.status = status;
        this.carat = carat;
        this.prize = prize;
        this.percentage = percentage;
        this.amount = amount;
        this.invoice = invoice;
        this.due_date = due_date;
        this.today = today;
    }
    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }
    public int getInvoice() {
        return invoice;
    }

    public void setInvoice(int invoice) {
        this.invoice = invoice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Float getCarat() {
        return carat;
    }

    public void setCarat(Float carat) {
        this.carat = carat;
    }

    public Float getPrize() {
        return prize;
    }

    public void setPrize(Float prize) {
        this.prize = prize;
    }

    public Float getPercentage() {
        return percentage;
    }

    public void setPercentage(Float percentage) {
        this.percentage = percentage;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}
