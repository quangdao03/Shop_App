package com.example.shop_app.model;

import java.util.Date;

public class ChatMessage {
    public String senid,receivedID,mess,datetime,url;
    public Date dateObj;
    public ChatMessage() {
    }

    public String getSenid() {
        return senid;
    }

    public void setSenid(String senid) {
        this.senid = senid;
    }

    public String getReceivedID() {
        return receivedID;
    }

    public void setReceivedID(String receivedID) {
        this.receivedID = receivedID;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Date getDateObj() {
        return dateObj;
    }

    public void setDateObj(Date dateObj) {
        this.dateObj = dateObj;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "senid='" + senid + '\'' +
                ", receivedID='" + receivedID + '\'' +
                ", mess='" + mess + '\'' +
                ", datetime='" + datetime + '\'' +
                ", dateObj=" + dateObj +
                '}';
    }
}
