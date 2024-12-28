package com.reward.quizisty.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.reward.quizisty.Fragments.MilestoneFragment;
import com.reward.quizisty.Fragments.ReferChildFragment;

public class ReferViewpagerAdapter extends FragmentStateAdapter {


    public ReferViewpagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0){
            return new ReferChildFragment();
        }else if (position == 1){
            return new MilestoneFragment();
        }else {
            return new ReferChildFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }


}
