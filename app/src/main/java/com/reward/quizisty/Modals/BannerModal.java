package com.reward.quizisty.Modals;

public class BannerModal {
    String bannerImg;
    String bannerImgLink;

    public BannerModal(String bannerImg, String bannerImgLink) {
        this.bannerImg = bannerImg;
        this.bannerImgLink = bannerImgLink;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }

    public String getBannerImgLink() {
        return bannerImgLink;
    }

    public void setBannerImgLink(String bannerImgLink) {
        this.bannerImgLink = bannerImgLink;
    }

    public String getBannerImg() {
        return bannerImg;
    }
}
