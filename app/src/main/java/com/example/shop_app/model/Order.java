package com.example.shop_app.model;

public class Order {
    String orderID, orderTime, orderStatus, orderCost, orderBy, orderTo,orderNameProduct;

    public Order() {
    }

    public Order(String orderID, String orderTime, String orderStatus, String orderCost, String orderBy, String orderTo, String orderNameProduct) {
        this.orderID = orderID;
        this.orderTime = orderTime;
        this.orderStatus = orderStatus;
        this.orderCost = orderCost;
        this.orderBy = orderBy;
        this.orderTo = orderTo;
        this.orderNameProduct = orderNameProduct;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(String orderCost) {
        this.orderCost = orderCost;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderTo() {
        return orderTo;
    }

    public void setOrderTo(String orderTo) {
        this.orderTo = orderTo;
    }

    public String getOrderNameProduct() {
        return orderNameProduct;
    }

    public void setOrderNameProduct(String orderNameProduct) {
        this.orderNameProduct = orderNameProduct;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID='" + orderID + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderCost='" + orderCost + '\'' +
                ", orderBy='" + orderBy + '\'' +
                ", orderTo='" + orderTo + '\'' +
                ", orderNameProduct='" + orderNameProduct + '\'' +
                '}';
    }
}
