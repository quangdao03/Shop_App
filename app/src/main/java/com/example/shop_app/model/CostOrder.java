package com.example.shop_app.model;

public class CostOrder {

    String orderCost ;
    CostOrder(){

    }

    public CostOrder(String orderCost) {
        this.orderCost = orderCost;
    }

    public String getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(String orderCost) {
        this.orderCost = orderCost;
    }

    @Override
    public String toString() {
        return "CostOrder{" +
                "orderCost='" + orderCost + '\'' +
                '}';
    }
}
