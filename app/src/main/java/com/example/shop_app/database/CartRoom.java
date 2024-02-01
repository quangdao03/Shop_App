package com.example.shop_app.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "cart")
public class CartRoom implements Serializable {
    @PrimaryKey(autoGenerate = true)
    int Cart_ID;
    @ColumnInfo(name = "productID")
    private String productID;
    @ColumnInfo(name = "image")
    private String image;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "creator")
    private String creator;
    @ColumnInfo(name = "variant")
    private String variant;
    @ColumnInfo(name = "price")
    private String price;
    @ColumnInfo(name = "priceEach")
    private String priceEach;
    @ColumnInfo(name = "quantity")
    private String quantity;
    @ColumnInfo(name = "shop_id")
    private String shop_id;
    public CartRoom() {

    }

    public CartRoom(int cart_ID, String productID, String image, String name, String creator, String variant, String price, String priceEach, String quantity, String shop_id) {
        Cart_ID = cart_ID;
        this.productID = productID;
        this.image = image;
        this.name = name;
        this.creator = creator;
        this.variant = variant;
        this.price = price;
        this.priceEach = priceEach;
        this.quantity = quantity;
        this.shop_id = shop_id;
    }

    public int getCart_ID() {
        return Cart_ID;
    }

    public void setCart_ID(int cart_ID) {
        Cart_ID = cart_ID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceEach() {
        return priceEach;
    }

    public void setPriceEach(String priceEach) {
        this.priceEach = priceEach;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    @Override
    public String toString() {
        return "CartRoom{" +
                "Cart_ID=" + Cart_ID +
                ", productID='" + productID + '\'' +
                ", image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", creator='" + creator + '\'' +
                ", variant='" + variant + '\'' +
                ", price='" + price + '\'' +
                ", priceEach='" + priceEach + '\'' +
                ", quantity='" + quantity + '\'' +
                ", shop_id='" + shop_id + '\'' +
                '}';
    }
}
