package com.example.makeupstudioadmin.model;

public class Brand {
    String brand_id;
    String brand_name;
    String brand_image;

    public Brand() {
    }

    public Brand(String brand_id, String brand_name, String brand_image) {
        this.brand_id = brand_id;
        this.brand_name = brand_name;
        this.brand_image = brand_image;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getBrand_image() {
        return brand_image;
    }

    public void setBrand_image(String brand_image) {
        this.brand_image = brand_image;
    }
}
