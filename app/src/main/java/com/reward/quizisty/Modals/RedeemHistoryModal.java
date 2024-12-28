package com.reward.quizisty.Modals;

public class RedeemHistoryModal {
    String redHisName, redHisPrice, redHisCoin, redHisDate , redHisResponseTime, redHisGivenDetail;
    String redHisImg;
    int redHisStatus;
    String redeemCode;

    public RedeemHistoryModal(String redHisName, String redHisPrice, String redHisCoin, String redHisDate, String redHisResponseTime, String redHisImg, int redHisStatus) {
        this.redHisName = redHisName;
        this.redHisPrice = redHisPrice;
        this.redHisCoin = redHisCoin;
        this.redHisDate = redHisDate;
        this.redHisResponseTime = redHisResponseTime;
        this.redHisImg = redHisImg;
        this.redHisStatus = redHisStatus;
    }

    public String getRedeemCode() {
        return redeemCode;
    }

    public void setRedeemCode(String redeemCode) {
        this.redeemCode = redeemCode;
    }

    public String getRedHisGivenDetail() {
        return redHisGivenDetail;
    }

    public void setRedHisGivenDetail(String redHisGivenDetail) {
        this.redHisGivenDetail = redHisGivenDetail;
    }

    public String getRedHisName() {
        return redHisName;
    }

    public void setRedHisName(String redHisName) {
        this.redHisName = redHisName;
    }

    public String getRedHisPrice() {
        return redHisPrice;
    }

    public void setRedHisPrice(String redHisPrice) {
        this.redHisPrice = redHisPrice;
    }

    public String getRedHisCoin() {
        return redHisCoin;
    }

    public void setRedHisCoin(String redHisCoin) {
        this.redHisCoin = redHisCoin;
    }

    public String getRedHisDate() {
        return redHisDate;
    }

    public void setRedHisDate(String redHisDate) {
        this.redHisDate = redHisDate;
    }

    public int getRedHisStatus() {
        return redHisStatus;
    }

    public void setRedHisStatus(int redHisStatus) {
        this.redHisStatus = redHisStatus;
    }

    public String getRedHisResponseTime() {
        return redHisResponseTime;
    }

    public void setRedHisResponseTime(String redHisResponseTime) {
        this.redHisResponseTime = redHisResponseTime;
    }

    public String getRedHisImg() {
        return redHisImg;
    }

    public void setRedHisImg(String redHisImg) {
        this.redHisImg = redHisImg;
    }
}
