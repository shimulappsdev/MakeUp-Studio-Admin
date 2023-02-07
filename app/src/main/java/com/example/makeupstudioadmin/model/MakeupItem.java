package com.example.makeupstudioadmin.model;

public class MakeupItem {

    String makeupItem_id;
    String category_id;
    String makeupItem_name;
    String makeupItem_about;
    String makeupItem_procedure;
    String makeupItem_remove;
    String makeupItem_image;

    public MakeupItem() {
    }

    public MakeupItem(String makeupItem_id, String category_id, String makeupItem_name, String makeupItem_about, String makeupItem_procedure, String makeupItem_remove, String makeupItem_image) {
        this.makeupItem_id = makeupItem_id;
        this.category_id = category_id;
        this.makeupItem_name = makeupItem_name;
        this.makeupItem_about = makeupItem_about;
        this.makeupItem_procedure = makeupItem_procedure;
        this.makeupItem_remove = makeupItem_remove;
        this.makeupItem_image = makeupItem_image;
    }

    public String getMakeupItem_id() {
        return makeupItem_id;
    }

    public void setMakeupItem_id(String makeupItem_id) {
        this.makeupItem_id = makeupItem_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getMakeupItem_name() {
        return makeupItem_name;
    }

    public void setMakeupItem_name(String makeupItem_name) {
        this.makeupItem_name = makeupItem_name;
    }

    public String getMakeupItem_about() {
        return makeupItem_about;
    }

    public void setMakeupItem_about(String makeupItem_about) {
        this.makeupItem_about = makeupItem_about;
    }

    public String getMakeupItem_procedure() {
        return makeupItem_procedure;
    }

    public void setMakeupItem_procedure(String makeupItem_procedure) {
        this.makeupItem_procedure = makeupItem_procedure;
    }

    public String getMakeupItem_remove() {
        return makeupItem_remove;
    }

    public void setMakeupItem_remove(String makeupItem_remove) {
        this.makeupItem_remove = makeupItem_remove;
    }

    public String getMakeupItem_image() {
        return makeupItem_image;
    }

    public void setMakeupItem_image(String makeupItem_image) {
        this.makeupItem_image = makeupItem_image;
    }
}
