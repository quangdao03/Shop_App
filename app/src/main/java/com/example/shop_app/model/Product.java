package com.example.shop_app.model;

public class Product {
    private int id;
    private int image;
    private String name;
    private String price;
    private String number;

    public Product(){

    }

    public Product(int id, int image, String name, String price, String number) {
        this.id = id;
        this.image = image;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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
                ", image=" + image +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
