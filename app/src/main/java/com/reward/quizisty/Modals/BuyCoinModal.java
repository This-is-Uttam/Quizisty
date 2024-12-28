package com.reward.quizisty.Modals;

public class BuyCoinModal {
    int buyCoinPrice, noOfCoins;
    int id;

    public BuyCoinModal(int buyCoinPrice, int noOfCoins, int id) {
        this.buyCoinPrice = buyCoinPrice;
        this.noOfCoins = noOfCoins;
        this.id = id;
    }

    public int getBuyCoinPrice() {
        return buyCoinPrice;
    }

    public void setBuyCoinPrice(int buyCoinPrice) {
        this.buyCoinPrice = buyCoinPrice;
    }

    public int getNoOfCoins() {
        return noOfCoins;
    }

    public void setNoOfCoins(int noOfCoins) {
        this.noOfCoins = noOfCoins;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
