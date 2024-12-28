package com.reward.quizisty.Coins;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.reward.quizisty.Modals.AdNetModal;
import com.reward.quizisty.Modals.BannerModal;
import com.reward.quizisty.Modals.BuyCoinModal;
import com.reward.quizisty.Modals.UserModal;

import java.util.ArrayList;

public class CoinViewModal extends ViewModel {
    CoinsRepository coinsRepository;
    public CoinViewModal() {
        coinsRepository = new CoinsRepository();

    }

    void fetchBannersList(Context context) {
        coinsRepository.fetchBannerDataFromApi(context);
    }

    LiveData<ArrayList<BannerModal>> getBannersList() {
        return coinsRepository.getBannersList();
    }

    void fetchBuyCoinList(Context context) {
        coinsRepository.fetchBuyCoinListFromApi(context);
    }

    LiveData<ArrayList<BuyCoinModal>> getBuyCoinList() {
        return coinsRepository.getBuyCoinsList();
    }

    void fetchWatchVideoList(Context context) {
        coinsRepository.fetchWatchVideoListFromApi(context);
    }

    LiveData<ArrayList<AdNetModal>> getWatchVideoList() {
        return coinsRepository.getWatchVidList();
    }

    LiveData<UserModal> getUserData() {return coinsRepository.getUserData();};
    void  fetchUserData(Context context) {
        coinsRepository.fetchUserDataFromApi(context);
    }
}
