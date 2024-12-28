package com.reward.quizisty.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reward.quizisty.Modals.BuyCoinModal;
import com.reward.quizisty.databinding.BuyCoinItemBinding;

import java.util.ArrayList;

public class BuyCoinAdapter extends RecyclerView.Adapter<BuyCoinAdapter.Viewholder> {
    ArrayList<BuyCoinModal> buyCoinList;
    Context context;
    private String TAG = "BuyCoinAdapter";

    public BuyCoinAdapter(ArrayList<BuyCoinModal> buyCoinList, Context context) {
        this.buyCoinList = buyCoinList;
        this.context = context;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(com.reward.quizisty.R.layout.buy_coin_item,parent,false);
        Log.d(TAG, "onCreateViewHolder: called here..");
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        BuyCoinModal modal = buyCoinList.get(position);
        float coins = modal.getNoOfCoins();
//        float coins = 5230;

//    change 1000 int K
        if (coins > 999){
            float coinsF = (coins/1000);
            if ((long)coinsF == coinsF){
                holder.binding.noOfCoins.setText((long)coinsF+"K ");
            }else {
                holder.binding.noOfCoins.setText(coinsF+"K ");
            }
            Log.d("qqq", "onBindViewHolder: coins: "+coins +"coinsF: "+coinsF +"coinsF Long: "+(long) coinsF);
        }else {
            holder.binding.noOfCoins.setText((int) coins+" ");
        }

        holder.binding.buyCoinPrice.setText("₹"+modal.getBuyCoinPrice());

        holder.binding.buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, UpiAppsActivity.class);
//                intent.putExtra("COINS", coins);
//                intent.putExtra("PRICE", modal.getBuyCoinPrice());
//                intent.putExtra("BUY_COIN_ID", modal.getId());
//
//                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return buyCoinList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        BuyCoinItemBinding binding;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = BuyCoinItemBinding.bind(itemView);
        }
    }
}
