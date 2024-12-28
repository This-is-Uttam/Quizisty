package com.reward.quizisty.Modals;

public class LeaderBoardModal {
    boolean crown;
    int winnerCount2;
    String winnerName2;
    String winnerImage;

    public LeaderBoardModal(boolean crown, int winnerCount2, String winnerName2, String winnerImage, int referCount2) {
        this.crown = crown;
        this.winnerCount2 = winnerCount2;
        this.winnerName2 = winnerName2;
        this.winnerImage = winnerImage;
        this.referCount2 = referCount2;
    }

    public String getWinnerImage() {
        return winnerImage;
    }

    public void setWinnerImage(String winnerImage) {
        this.winnerImage = winnerImage;
    }

    int referCount2;

    public LeaderBoardModal(int winnerCount2, String winnerName2, int referCount2, boolean crown) {
        this.crown = crown;
        this.winnerCount2 = winnerCount2;
        this.winnerName2 = winnerName2;
        this.referCount2 = referCount2;
    }

    public LeaderBoardModal(String winnerName2, int referCount2) {

        this.winnerName2 = winnerName2;
        this.referCount2 = referCount2;
    }

    public int getWinnerCount2() {
        return winnerCount2;
    }

    public void setWinnerCount2(int winnerCount2) {
        this.winnerCount2 = winnerCount2;
    }

    public String getWinnerName2() {
        return winnerName2;
    }

    public void setWinnerName2(String winnerName2) {
        this.winnerName2 = winnerName2;
    }

    public int getReferCount2() {
        return referCount2;
    }

    public void setReferCount2(int referCount2) {
        this.referCount2 = referCount2;
    }

    public boolean isCrown() {
        return crown;
    }

    public void setCrown(boolean crown) {
        this.crown = crown;
    }
}
