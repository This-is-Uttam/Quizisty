package com.reward.quizisty.Fragments;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.reward.quizisty.Modals.MileStonesModal;

import java.util.ArrayList;

public class MilestoneViewModel extends ViewModel {
    MileStoneRepository mileStoneRepository;
    public MilestoneViewModel() {
        mileStoneRepository = new MileStoneRepository();
    }

    public LiveData<ArrayList<MileStonesModal>> getMileStoneModals() {
        return mileStoneRepository.getMileStoneModals();
    }

    public void fetchMileStoneModal(Context context) {
        mileStoneRepository.fetchMileStoneData(context);
    }
}