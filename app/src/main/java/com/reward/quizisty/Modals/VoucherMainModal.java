package com.reward.quizisty.Modals;

public class VoucherMainModal {
    String daysLeftText, spotLeftText, totalSpotText, vouMainItemName,  shortDescription, details, vouMainItemId;
    String vouMainItemImg,mrp;
    boolean isSpotFull;
    String winningCode;
    int vouPricePerSpot;

    public VoucherMainModal(String daysLeftText, String spotLeftText, String totalSpotText, String vouMainItemName,
                            int vouPricePerSpot, String shortDescription, String details, String vouMainItemId,
                            String vouMainItemImg, boolean isSpotFull, String winningCode) {
        this.daysLeftText = daysLeftText;
        this.spotLeftText = spotLeftText;
        this.totalSpotText = totalSpotText;
        this.vouMainItemName = vouMainItemName;
        this.vouPricePerSpot = vouPricePerSpot;
        this.shortDescription = shortDescription;
        this.details = details;
        this.vouMainItemId = vouMainItemId;
        this.vouMainItemImg = vouMainItemImg;
        this.isSpotFull = isSpotFull;
        this.winningCode = winningCode;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getWinningCode() {
        return winningCode;
    }

    public void setWinningCode(String winningCode) {
        this.winningCode = winningCode;
    }

    public String getDaysLeftText() {
        return daysLeftText;
    }

    public void setDaysLeftText(String daysLeftText) {
        this.daysLeftText = daysLeftText;
    }

    public String getSpotLeftText() {
        return spotLeftText;
    }

    public void setSpotLeftText(String spotLeftText) {
        this.spotLeftText = spotLeftText;
    }

    public String getTotalSpotText() {
        return totalSpotText;
    }

    public void setTotalSpotText(String totalSpotText) {
        this.totalSpotText = totalSpotText;
    }

    public String getVouMainItemName() {
        return vouMainItemName;
    }

    public void setVouMainItemName(String vouMainItemName) {
        this.vouMainItemName = vouMainItemName;
    }

    public int getVouPricePerSpot() {
        return vouPricePerSpot;
    }

    public void setVouPricePerSpot(int vouPricePerSpot) {
        this.vouPricePerSpot = vouPricePerSpot;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getVouMainItemId() {
        return vouMainItemId;
    }

    public void setVouMainItemId(String vouMainItemId) {
        this.vouMainItemId = vouMainItemId;
    }

    public String getVouMainItemImg() {
        return vouMainItemImg;
    }

    public void setVouMainItemImg(String vouMainItemImg) {
        this.vouMainItemImg = vouMainItemImg;
    }

    public boolean isSpotFull() {
        return isSpotFull;
    }

    public void setSpotFull(boolean spotFull) {
        isSpotFull = spotFull;
    }
}
