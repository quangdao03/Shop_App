package com.example.shop_app.model;

public class Brand {
    private int id;
    private String image;

    public Brand(){

    }

    public Brand(int id, String image) {
        this.id = id;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id='" + id + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
