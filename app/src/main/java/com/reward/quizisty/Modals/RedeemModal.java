package com.reward.quizisty.Modals;


public class RedeemModal {
    int redeemId;
    String redeemName;
    String redeemCoins;
    String redeemAmt;
    String redeemHint;
    String redeemInputType;
    String redeemStatus;
    String redeemImg;
    String voucherSymbol;


    public RedeemModal(String redeemName, String redeemCoins, String redeemAmt, String redeemHint,
                       String redeemInputType, String redeemStatus, String redeemImg) {
        this.redeemName = redeemName;
        this.redeemCoins = redeemCoins;
        this.redeemAmt = redeemAmt;
        this.redeemHint = redeemHint;
        this.redeemInputType = redeemInputType;
        this.redeemStatus = redeemStatus;
        this.redeemImg = redeemImg;
    }

    public int getRedeemId() {
        return redeemId;
    }

    public void setRedeemId(int redeemId) {
        this.redeemId = redeemId;
    }

    public String getRedeemName() {
        return redeemName;
    }

    public void setRedeemName(String redeemName) {
        this.redeemName = redeemName;
    }

    public String getRedeemCoins() {
        return redeemCoins;
    }

    public void setRedeemCoins(String redeemCoins) {
        this.redeemCoins = redeemCoins;
    }

    public String getRedeemAmt() {
        return redeemAmt;
    }

    public void setRedeemAmt(String redeemAmt) {
        this.redeemAmt = redeemAmt;
    }

    public String getRedeemHint() {
        return redeemHint;
    }

    public void setRedeemHint(String redeemHint) {
        this.redeemHint = redeemHint;
    }

    public String getRedeemInputType() {
        return redeemInputType;
    }

    public void setRedeemInputType(String redeemInputType) {
        this.redeemInputType = redeemInputType;
    }

    public String getRedeemStatus() {
        return redeemStatus;
    }

    public void setRedeemStatus(String redeemStatus) {
        this.redeemStatus = redeemStatus;
    }

    public String getRedeemImg() {
        return redeemImg;
    }

    public void setRedeemImg(String redeemImg) {
        this.redeemImg = redeemImg;
    }

    public String getVoucherSymbol() {
        return voucherSymbol;
    }

    public void setVoucherSymbol(String voucherSymbol) {
        this.voucherSymbol = voucherSymbol;
    }
}
