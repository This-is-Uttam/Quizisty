package com.reward.quizisty.Modals;

public class AdNetModal {
    int adNetImg;
    String adNetName;
    int id;

    public AdNetModal(int adNetImg, String adNetName, int id) {
        this.adNetImg = adNetImg;
        this.adNetName = adNetName;
        this.id = id;
    }

    public int getAdNetImg() {
        return adNetImg;
    }

    public String getAdNetName() {
        return adNetName;
    }

    public int getId() {
        return id;
    }
}
