package com.example.makeupstudioadmin.model;

public class Slider {
    String slider_id;
    String slider_image;

    public Slider() {
    }

    public Slider(String slider_id, String slider_image) {
        this.slider_id = slider_id;
        this.slider_image = slider_image;
    }

    public String getSlider_id() {
        return slider_id;
    }

    public void setSlider_id(String slider_id) {
        this.slider_id = slider_id;
    }

    public String getSlider_image() {
        return slider_image;
    }

    public void setSlider_image(String slider_image) {
        this.slider_image = slider_image;
    }
}
