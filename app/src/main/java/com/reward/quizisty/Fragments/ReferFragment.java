package com.reward.quizisty.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.reward.quizisty.Adapters.ReferViewpagerAdapter;
import com.reward.quizisty.Modals.LeaderBoardModal;
import com.reward.quizisty.databinding.FragmentReferBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;


public class ReferFragment extends Fragment {

    FragmentReferBinding binding;
    ArrayList<LeaderBoardModal> leaderBoardList;

    public ReferFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReferBinding.inflate(inflater, container, false);

        binding.referVp.setAdapter(new ReferViewpagerAdapter(this));
        new TabLayoutMediator(binding.referTabs,binding.referVp,(tab, position) -> {
            if (position == 0){
                tab.setText("Refer");
            }else {
                tab.setText("Milestones");
            }
        }).attach();

        return binding.getRoot();
    }


}