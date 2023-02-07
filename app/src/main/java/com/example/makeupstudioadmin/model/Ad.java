package com.example.makeupstudioadmin.model;

public class Ad {
    String banner;
    String interstitial;

    public Ad() {
    }

    public Ad(String banner, String interstitial) {
        this.banner = banner;
        this.interstitial = interstitial;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getInterstitial() {
        return interstitial;
    }

    public void setInterstitial(String interstitial) {
        this.interstitial = interstitial;
    }
}
