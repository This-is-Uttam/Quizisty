package com.reward.quizisty.Modals;

public class MileStonesModal {
    int id, rewardType;
    int claimStatus = 0;
    String prize, refers, imageUrl, rejectReason;
//    0 = disable
//    1 = enable
//    2 = pending
//    3 = claimed
//    4 = rejected


    public MileStonesModal(int id, int rewardType, int claimStatus, String prize,
                           String refers, String imageUrl) {
        this.id = id;
        this.rewardType = rewardType;
        this.claimStatus = claimStatus;
        this.prize = prize;
        this.refers = refers;
        this.imageUrl = imageUrl;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public int getId() {
        return id;
    }

    public int getRewardType() {
        return rewardType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrize() {
        return prize;
    }

    public String getRefers() {
        return refers;
    }

    public int getClaimStatus() {
        return claimStatus;
    }
}
