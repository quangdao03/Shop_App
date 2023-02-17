package com.example.shop_app.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Product {

    private int id;
    private String url ;
    private String name;
    private String price;
    private String quantity;
    private int rate;
    private String variant;
    private String desc;
    private String creator;
    private Boolean favourite;

    private String uid;

    public Product(){}

    public Product(int id, String url, String name, String price, String quantity, int rate, String variant, String desc, String creator, Boolean favourite,String uid) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.rate = rate;
        this.variant = variant;
        this.desc = desc;
        this.creator = creator;
        this.favourite = favourite;
        this.uid = uid;
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", quantity='" + quantity + '\'' +
                ", rate=" + rate +
                ", variant='" + variant + '\'' +
                ", desc='" + desc + '\'' +
                ", creator='" + creator + '\'' +
                ", favourite=" + favourite +
                ", uid='" + uid + '\'' +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("favourite", true);


        return result;
    }
}
