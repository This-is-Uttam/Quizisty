package com.reward.quizisty.Modals;

public class VoucherWinModal {
    String userName, userImage, voucherName, voucherImage, mrp;
    int voucherWinnerCount;
    String winnMonth;

    public VoucherWinModal(String userName, String userImage, String voucherName, String voucherImage, String mrp) {
        this.userName = userName;
        this.userImage = userImage;
        this.voucherName = voucherName;
        this.voucherImage = voucherImage;
        this.mrp = mrp;
    }

    public String getWinnMonth() {
        return winnMonth;
    }

    public void setWinnMonth(String winnMonth) {
        this.winnMonth = winnMonth;
    }

    public int getVoucherWinnerCount() {
        return voucherWinnerCount;
    }

    public void setVoucherWinnerCount(int voucherWinnerCount) {
        this.voucherWinnerCount = voucherWinnerCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public String getVoucherImage() {
        return voucherImage;
    }

    public void setVoucherImage(String voucherImage) {
        this.voucherImage = voucherImage;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }
}
