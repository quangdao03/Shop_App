package com.example.shop_app.model;

public class Category {
    private  int id;

    private String url;
    private String name;

    public  Category(){

    }

    public Category(int id, String image, String name) {
        this.id = id;
        this.url = image;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return url;
    }

    public void setImage(String image) {
        this.url = image;
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
                "id='" + id + '\'' +
                ", image='" + url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
