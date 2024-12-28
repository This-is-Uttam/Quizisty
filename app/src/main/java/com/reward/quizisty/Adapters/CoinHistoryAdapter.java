package com.reward.quizisty.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.reward.quizisty.Modals.HistoryModal;
import com.reward.quizisty.R;
import com.reward.quizisty.databinding.CoinHistoryRvItemBinding;

import java.util.ArrayList;

public class CoinHistoryAdapter extends RecyclerView.Adapter<CoinHistoryAdapter.Viewholder> {
    ArrayList<HistoryModal> coinHistoryList;
    Context context;

    public CoinHistoryAdapter(ArrayList<HistoryModal> coinHistoryList, Context context) {
        Log.d("CoinHistoryAdapter", "constructor: ");
        this.coinHistoryList = coinHistoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.coin_history_rv_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        HistoryModal modal = coinHistoryList.get(position);
        Log.d("CoinHistoryAdapter", "onBindViewHolder: ");

        if (!modal.isCredited()){
            holder.binding.historyCoin.setTextColor(Color.GRAY);
            holder.binding.arrowImg.setImageDrawable(context.getDrawable(R.drawable.debit_arrow));
        }else {
            holder.binding.historyCoin.setTextColor(context.getResources().getColor(R.color.orangeDark, context.getTheme()));
            holder.binding.arrowImg.setImageDrawable(context.getDrawable(R.drawable.credit_arrow));
        }
        float coin = Float.parseFloat(modal.getHistoryCoinDiamond());
        String strCoin;
        if (coin>999){
            coin = coin/1000;
            Log.d("uttam", "onBindViewHolder: coin float: "+ coin+" coin long: "+ (long)coin);
            boolean isEqual = (coin == (long)coin);
            Log.d("uttam", "onBindViewHolder: coin float: "+ isEqual);
            if (coin == (long) coin)
                strCoin = (long) coin+"K";
            else
                strCoin = coin+"K";


        }else {
            strCoin = (long) coin+"";
        }
        holder.binding.historyCoin.setText(strCoin);

        holder.binding.historyMsg.setText(modal.getHistoryMsg());
//        date and time
        String date = modal.getHistoryDate();


        holder.binding.historyDate.setText(date);
        holder.binding.historyTime.setText(modal.getHistoryTime());




    }

    @Override
    public int getItemCount() {
        return coinHistoryList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        CoinHistoryRvItemBinding binding;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = CoinHistoryRvItemBinding.bind(itemView);
        }
    }
}
