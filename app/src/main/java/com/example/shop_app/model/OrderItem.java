package com.example.shop_app.model;

public class OrderItem {
    String productID, name, price, priceEach, quantity, variant, image;

    public OrderItem() {
    }

    public OrderItem(String productID, String name, String price, String priceEach, String quantity, String variant, String image) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.priceEach = priceEach;
        this.quantity = quantity;
        this.variant = variant;
        this.image = image;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
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

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "productID='" + productID + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", priceEach='" + priceEach + '\'' +
                ", quantity='" + quantity + '\'' +
                ", variant='" + variant + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
