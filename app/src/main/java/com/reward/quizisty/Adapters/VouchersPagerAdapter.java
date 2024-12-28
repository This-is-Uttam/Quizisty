package com.reward.quizisty.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.reward.quizisty.Fragments.VoucherChildFragment;
import com.reward.quizisty.Fragments.VoucherWinnerFragment;

public class VouchersPagerAdapter extends FragmentStateAdapter {
    public VouchersPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new VoucherChildFragment();
        } else if (position == 1) {
            return new VoucherWinnerFragment();
        } else return new VoucherChildFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
