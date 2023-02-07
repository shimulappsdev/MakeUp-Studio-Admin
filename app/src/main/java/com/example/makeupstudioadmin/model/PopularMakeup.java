package com.example.makeupstudioadmin.model;

public class PopularMakeup {

    String popularMakeUp_id;
    String popularMakeUp_name;
    String popularMakeUp_image;
    String popularMakeUp_description;

    public PopularMakeup() {
    }

    public PopularMakeup(String popularMakeUp_id, String popularMakeUp_name, String popularMakeUp_image, String popularMakeUp_description) {
        this.popularMakeUp_id = popularMakeUp_id;
        this.popularMakeUp_name = popularMakeUp_name;
        this.popularMakeUp_image = popularMakeUp_image;
        this.popularMakeUp_description = popularMakeUp_description;
    }

    public String getPopularMakeUp_id() {
        return popularMakeUp_id;
    }

    public void setPopularMakeUp_id(String popularMakeUp_id) {
        this.popularMakeUp_id = popularMakeUp_id;
    }

    public String getPopularMakeUp_name() {
        return popularMakeUp_name;
    }

    public void setPopularMakeUp_name(String popularMakeUp_name) {
        this.popularMakeUp_name = popularMakeUp_name;
    }

    public String getPopularMakeUp_image() {
        return popularMakeUp_image;
    }

    public void setPopularMakeUp_image(String popularMakeUp_image) {
        this.popularMakeUp_image = popularMakeUp_image;
    }

    public String getPopularMakeUp_description() {
        return popularMakeUp_description;
    }

    public void setPopularMakeUp_description(String popularMakeUp_description) {
        this.popularMakeUp_description = popularMakeUp_description;
    }
}
