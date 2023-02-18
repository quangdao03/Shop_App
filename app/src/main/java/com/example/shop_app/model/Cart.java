package com.example.shop_app.model;

public class Cart {
    String Cart_ID, productID, image, name,creator,variant, price, priceEach, quantity;
    public Cart (){

    }

    public Cart(String cart_ID, String productID, String image, String name, String creator, String variant, String price, String priceEach, String quantity) {
        Cart_ID = cart_ID;
        this.productID = productID;
        this.image = image;
        this.name = name;
        this.creator = creator;
        this.variant = variant;
        this.price = price;
        this.priceEach = priceEach;
        this.quantity = quantity;
    }

    public String getCart_ID() {
        return Cart_ID;
    }

    public void setCart_ID(String cart_ID) {
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

    @Override
    public String toString() {
        return "Cart{" +
                "Cart_ID='" + Cart_ID + '\'' +
                ", productID='" + productID + '\'' +
                ", image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", creator='" + creator + '\'' +
                ", variant='" + variant + '\'' +
                ", price='" + price + '\'' +
                ", priceEach='" + priceEach + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }
}
