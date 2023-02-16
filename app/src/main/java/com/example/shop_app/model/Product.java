package com.example.shop_app.model;

public class Product {
    private int id;
    private String url ;
    private String name;
    private String price;
    private String number;

    public Product(){}

    public Product(int id, String url, String name, String price, String number) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.price = price;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String geturl() {
        return url;
    }

    public void seturl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", url=" + url +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
