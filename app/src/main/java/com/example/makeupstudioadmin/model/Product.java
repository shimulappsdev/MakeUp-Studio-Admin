package com.example.makeupstudioadmin.model;

public class Product {
    String product_id;
    String product_name;
    String product_image;
    String product_description;

    public Product() {
    }

    public Product(String product_id, String product_name, String product_image, String product_description) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_image = product_image;
        this.product_description = product_description;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }
}
