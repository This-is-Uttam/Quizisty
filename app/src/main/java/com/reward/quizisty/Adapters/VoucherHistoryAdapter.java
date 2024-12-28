package com.reward.quizisty.Adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reward.quizisty.Modals.BiddingHistoryModal;
import com.reward.quizisty.R;
import com.reward.quizisty.Utils.Constants;
import com.reward.quizisty.databinding.VoucherHistoryRvItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VoucherHistoryAdapter extends RecyclerView.Adapter<VoucherHistoryAdapter.Viewholder> {
    ArrayList<BiddingHistoryModal> biddingHistoryList;
    Context context;

    public VoucherHistoryAdapter(ArrayList<BiddingHistoryModal> biddingHistoryList, Context context) {
        this.biddingHistoryList = biddingHistoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public VoucherHistoryAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.voucher_history_rv_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherHistoryAdapter.Viewholder holder, int position) {
        BiddingHistoryModal modal = biddingHistoryList.get(position);

        holder.binding.bidProductName.setText(modal.getBidProductName());
        holder.binding.bidCoin.setText(modal.getBidCoin());
        holder.binding.bidDiamond.setText(modal.getBidDiamond());
        holder.binding.bidDate.setText(modal.getBidDate());
        holder.binding.bidTime.setText(modal.getBidTime());
        Log.d("hell", "onBindViewHolder: winner: "+ modal.getWinner()+" code : "+ modal.getVoucherCode());
//        holder.binding.bidWinningStatus.setBackground(ResourcesCompat.getDrawable(context.getResources(),R.drawable.circular_solid_bg_36dp, context.getTheme()));

        if (modal.getWinner()==1 && modal.getVoucherCode()!="null"){
            holder.binding.bidWinningStatus.setTextColor(context.getResources().getColor(R.color.white, context.getTheme()));
            holder.binding.bidWinningStatus.setBackgroundTintList(context.getColorStateList(R.color.green));
            holder.binding.bidWinningStatus.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            holder.binding.bidWinningStatus.setTypeface(Typeface.DEFAULT_BOLD);
//            holder.binding.constraintLayout6.setPadding(0,0,0,0);

//            holder.binding.bidWinningStatus.setTextColor(context.getResources().getColor(R.color.winnerGreen, context.getTheme()));
            holder.binding.voucherCode.setVisibility(View.VISIBLE);
            holder.binding.voucherCode.setText(modal.getVoucherCode());
            holder.binding.vCodeTitle.setVisibility(View.VISIBLE);
            holder.binding.copyCode.setVisibility(View.VISIBLE);

        }else if (modal.getWinner()==0 && modal.getVoucherCode()=="null"){
            holder.binding.bidWinningStatus.setTextColor(context.getResources().getColor(R.color.green, context.getTheme()));
            holder.binding.bidWinningStatus.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.bid_his_item_color, context.getTheme())));
            holder.binding.bidWinningStatus.setTypeface(Typeface.DEFAULT_BOLD);

//            holder.binding.bidWinningStatus.setTextColor(context.getResources().getColor(R.color.black, context.getTheme()));
//            holder.binding.voucherCode.setText(modal.getVoucherCode());
            holder.binding.voucherCode.setVisibility(View.GONE);
            holder.binding.vCodeTitle.setVisibility(View.GONE);
            holder.binding.copyCode.setVisibility(View.GONE);
        }else {
            holder.binding.bidWinningStatus.setTextColor(context.getResources().getColor(R.color.black, context.getTheme()));
            holder.binding.bidWinningStatus.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.bid_his_item_color, context.getTheme())));
//            holder.binding.bidWinningStatus.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
            holder.binding.bidWinningStatus.setTypeface(Typeface.DEFAULT_BOLD);
            holder.binding.voucherCode.setVisibility(View.GONE);
            holder.binding.vCodeTitle.setVisibility(View.GONE);
            holder.binding.copyCode.setVisibility(View.GONE);
        }
        holder.binding.bidWinningStatus.setText(modal.getBidWinningStatus());

        String prodImg = Constants.VOUCHER_IMG_URL + modal.getBidProductImage();
        Picasso.get()
                .load(prodImg)
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.bidProductImage);

        holder.binding.copyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Voucher code", holder.binding.voucherCode.getText().toString());
                manager.setPrimaryClip(clipData);
                Toast.makeText(context, "Voucher Code Copied", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return biddingHistoryList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        VoucherHistoryRvItemBinding binding;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = VoucherHistoryRvItemBinding.bind(itemView);
        }
    }
}
