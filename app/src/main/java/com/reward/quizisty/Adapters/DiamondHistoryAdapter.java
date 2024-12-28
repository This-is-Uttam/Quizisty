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
import com.reward.quizisty.databinding.DiamondHistoryRvItemBinding;

import java.util.ArrayList;

public class DiamondHistoryAdapter extends RecyclerView.Adapter<DiamondHistoryAdapter.Viewholder> {
    ArrayList<HistoryModal> diamondHisList;
    Context context;

    public DiamondHistoryAdapter(ArrayList<HistoryModal> diamondHisList, Context context) {
        this.diamondHisList = diamondHisList;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.diamond_history_rv_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        HistoryModal modal = diamondHisList.get(position);

        if (!modal.isCredited()){
            holder.binding.historyDiamond.setTextColor(Color.GRAY);
            holder.binding.arrowDimdImg.setImageDrawable(context.getDrawable(R.drawable.debit_arrow));
        }else {
            holder.binding.historyDiamond.setTextColor(context.getResources().getColor(R.color.blue, context.getTheme()));
            holder.binding.arrowDimdImg.setImageDrawable(context.getDrawable(R.drawable.credit_arrow));
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
        holder.binding.historyDiamond.setText(strCoin);
        holder.binding.historyDimdDate.setText(modal.getHistoryDate());

        holder.binding.historyDimdMsg.setText(modal.getHistoryMsg());
        holder.binding.historyDimdTime.setText(modal.getHistoryTime());


    }

    @Override
    public int getItemCount() {
        return diamondHisList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        DiamondHistoryRvItemBinding binding;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = DiamondHistoryRvItemBinding.bind(itemView);
        }
    }
}
