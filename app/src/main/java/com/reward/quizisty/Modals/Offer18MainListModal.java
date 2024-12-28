package com.reward.quizisty.Modals;

public class Offer18MainListModal {
    String adTitle;
    String adDesc;
    String adTermsNcon;
    String adRewardCoin;
    String adClaimBtn;
    String adPosterImg, adIcon;
    int totalTasks;
    String adId;
    String clickUrl;
    String countryCode;
    String helpUrl;
//    String cityCode;

    public Offer18MainListModal(String adPosterImg, String adIcon, String adTitle, String adDesc, String adRewardCoin, String adClaimBtn) {
        this.adPosterImg = adPosterImg;
        this.adIcon = adIcon;
        this.adTitle = adTitle;
        this.adDesc = adDesc;
        this.adRewardCoin = adRewardCoin;
        this.adClaimBtn = adClaimBtn;
    }

    public String getAdTermsNcon() {
        return adTermsNcon;
    }

    public void setAdTermsNcon(String adTermsNcon) {
        this.adTermsNcon = adTermsNcon;
    }

    public String getHelpUrl() {
        return helpUrl;
    }

    public void setHelpUrl(String helpUrl) {
        this.helpUrl = helpUrl;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public String getAdPosterImg() {
        return adPosterImg;
    }

    public String getAdIcon() {
        return adIcon;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public void setAdDesc(String adDesc) {
        this.adDesc = adDesc;
    }

    public void setAdRewardCoin(String adRewardCoin) {
        this.adRewardCoin = adRewardCoin;
    }

    public void setAdClaimBtn(String adClaimBtn) {
        this.adClaimBtn = adClaimBtn;
    }

    public void setAdPosterImg(String adPosterImg) {
        this.adPosterImg = adPosterImg;
    }
    public String getAdTitle() {
        return adTitle;
    }

    public String getAdDesc() {
        return adDesc;
    }

    public String getAdRewardCoin() {
        return adRewardCoin;
    }

    public String getAdClaimBtn() {
        return adClaimBtn;
    }

    public void setAdIcon(String adIcon) {
        this.adIcon = adIcon;
    }
}
