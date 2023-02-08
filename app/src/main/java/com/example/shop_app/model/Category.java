package com.example.shop_app.model;

public class Category {
    private int image;
    private String name;


    public Category(int image, String name) {
        this.image = image;
        this.name = name;
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

    @Override
    public String toString() {
        return "Category{" +
                "image=" + image +
                ", name='" + name + '\'' +
                '}';
    }
}
