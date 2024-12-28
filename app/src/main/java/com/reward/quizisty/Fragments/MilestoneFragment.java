package com.reward.quizisty.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reward.quizisty.Adapters.MileStoneAdapter;
import com.reward.quizisty.databinding.FragmentMilestoneBinding;

public class MilestoneFragment extends Fragment {
    FragmentMilestoneBinding binding;
    private MilestoneViewModel mViewModel;
    MileStoneAdapter mileStoneAdapter;


    public static MilestoneFragment newInstance() {
        return new MilestoneFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMilestoneBinding.inflate(inflater, container, false);




        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MilestoneViewModel.class);

        mViewModel.getMileStoneModals().observe(getViewLifecycleOwner(), mileStonesModals -> {
            binding.mileStoneSwipeRefresh.setRefreshing(false);
            if ((mileStonesModals.isEmpty())){
                binding.emptyTxtMileStones.setVisibility(View.VISIBLE);
            }else {
                binding.mileStoneRv.setLayoutManager(new LinearLayoutManager(getContext()));
                mileStoneAdapter = new MileStoneAdapter(mileStonesModals, getContext());
                binding.mileStoneRv.setAdapter(mileStoneAdapter);
            }
        });

        if (mViewModel.getMileStoneModals().getValue() == null) {
            binding.mileStoneSwipeRefresh.setRefreshing(true);
            mViewModel.fetchMileStoneModal(getContext());
        }

        binding.mileStoneSwipeRefresh.setOnRefreshListener(() -> {

            mViewModel.fetchMileStoneModal(getContext());
        });
    }

}