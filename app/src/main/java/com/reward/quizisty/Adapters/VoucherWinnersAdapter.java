package com.reward.quizisty.Adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reward.quizisty.Modals.VoucherWinModal;
import com.reward.quizisty.R;
import com.reward.quizisty.Utils.Constants;
import com.reward.quizisty.databinding.WinnerRvItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VoucherWinnersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    ArrayList<VoucherWinModal> voucherWinList;
    Context context;
    boolean showAll;

    public VoucherWinnersAdapter(ArrayList<VoucherWinModal> voucherWinList, Context context, boolean showAll) {
        this.voucherWinList = voucherWinList;
        this.context = context;
        this.showAll = showAll;
    }

    @Override
    public int getItemViewType(int position) {
        return voucherWinList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.winner_rv_item, parent, false);
            return new ItemViewholder(itemView);
        }else {
            View loadingView = LayoutInflater.from(context).inflate(R.layout.loading_view, parent, false);
            return new LoadingViewholder(loadingView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewholder) {
//            instance of ItemViewHolder
            setItemData((ItemViewholder) holder, position);
        }else {
//            loading view will show.
        }

    }

    private void setItemData(VoucherWinnersAdapter.ItemViewholder holder, int position) {
        VoucherWinModal modal = voucherWinList.get(position);

        holder.binding.winnerCount.setText("#" + modal.getVoucherWinnerCount());
        holder.binding.prodWinnerName.setText(modal.getUserName());
        holder.binding.prodWinningPrice.setText(modal.getVoucherName());
        holder.binding.prodWinPriceValue.setText("₹"+modal.getMrp());
        holder.binding.winMonth.setText(modal.getWinnMonth());

        String prodImg = Constants.VOUCHER_IMG_URL + modal.getVoucherImage();

        Log.d("VoucherWinnersAdapter", "onBindViewHolder: prodimg: "+prodImg+"name: "+ modal.getUserName());
        Picasso.get()
                .load(prodImg)
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.winnerProductImg);
        Picasso.get()
                .load(modal.getUserImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.winnerImg);
    }

    @Override
    public int getItemCount() {
        return voucherWinList.size();
    }

    public static class ItemViewholder extends RecyclerView.ViewHolder {
        WinnerRvItemBinding binding;

        public ItemViewholder(@NonNull View itemView) {
            super(itemView);
            binding = WinnerRvItemBinding.bind(itemView);
        }
    }

    public static class LoadingViewholder extends RecyclerView.ViewHolder {

        public LoadingViewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
