package com.reward.quizisty.Modals;

public class BiddingHistoryModal {
    String bidProductImage, bidProductName, bidCoin, bidDiamond, bidWinningStatus, bidDate, bidTime;
    int winner;
    int bidId;
    String winnerAddress;
    String winningDiamond, voucherCode;

    public BiddingHistoryModal() {
    }

    public BiddingHistoryModal(String bidProductImage, String bidProductName, String bidCoin, String bidDiamond, String bidWinningStatus, String bidDate, String bidTime) {
        this.bidProductImage = bidProductImage;
        this.bidProductName = bidProductName;
        this.bidCoin = bidCoin;
        this.bidDiamond = bidDiamond;
        this.bidWinningStatus = bidWinningStatus;
        this.bidDate = bidDate;
        this.bidTime = bidTime;
    }

    public String getWinnerAddress() {
        return winnerAddress;
    }

    public void setWinnerAddress(String winnerAddress) {
        this.winnerAddress = winnerAddress;
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getWinningDiamond() {
        return winningDiamond;
    }

    public void setWinningDiamond(String winningDiamond) {
        this.winningDiamond = winningDiamond;
    }

    public String getBidProductImage() {
        return bidProductImage;
    }

    public void setBidProductImage(String bidProductImage) {
        this.bidProductImage = bidProductImage;
    }

    public String getBidProductName() {
        return bidProductName;
    }

    public void setBidProductName(String bidProductName) {
        this.bidProductName = bidProductName;
    }

    public String getBidCoin() {
        return bidCoin;
    }

    public void setBidCoin(String bidCoin) {
        this.bidCoin = bidCoin;
    }

    public String getBidDiamond() {
        return bidDiamond;
    }

    public void setBidDiamond(String bidDiamond) {
        this.bidDiamond = bidDiamond;
    }

    public String getBidWinningStatus() {
        return bidWinningStatus;
    }

    public void setBidWinningStatus(String bidWinningStatus) {
        this.bidWinningStatus = bidWinningStatus;
    }

    public String getBidDate() {
        return bidDate;
    }

    public void setBidDate(String bidDate) {
        this.bidDate = bidDate;
    }

    public String getBidTime() {
        return bidTime;
    }

    public void setBidTime(String bidTime) {
        this.bidTime = bidTime;
    }


}
