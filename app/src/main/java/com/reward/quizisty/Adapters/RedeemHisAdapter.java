package com.reward.quizisty.Adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reward.quizisty.Modals.RedeemHistoryModal;
import com.reward.quizisty.R;
import com.reward.quizisty.Utils.Constants;
import com.reward.quizisty.databinding.RedeemHisRvItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RedeemHisAdapter extends RecyclerView.Adapter<RedeemHisAdapter.Viewholder> {
    ArrayList<RedeemHistoryModal> redeemHistoryList;
    Context context;

    public RedeemHisAdapter(ArrayList<RedeemHistoryModal> redeemHistoryList, Context context) {
        this.redeemHistoryList = redeemHistoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.redeem_his_rv_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        RedeemHistoryModal modal = redeemHistoryList.get(position);

        holder.binding.redHisName.setText(modal.getRedHisName());
        holder.binding.redHisCoin.setText(modal.getRedHisCoin());
        holder.binding.redHisPrice.setText(modal.getRedHisPrice());
        holder.binding.redHisDate.setText(modal.getRedHisDate());
        holder.binding.redHisGivenDetail.setText(modal.getRedHisGivenDetail());
//        check status
        int status =modal.getRedHisStatus();
        String statusMsg;
        if (status==0){
            holder.binding.redHisStatus.setText("Pending");
            holder.binding.redHisStatus.setTextColor(context.getResources().getColor(R.color.orangeDark, context.getTheme()));
        } else if (status==1) {
            holder.binding.redHisStatus.setText("Approved (wait for completion)");
            holder.binding.redHisStatus.setTextColor(context.getResources().getColor(R.color.green, context.getTheme()));

        }else if (status==3){
            holder.binding.redHisStatus.setText("Completed");
            holder.binding.redHisStatus.setTextColor(context.getResources().getColor(R.color.completedColor, context.getTheme()));
            //Redeem Code show
            if (!modal.getRedeemCode().equals("null")){
                holder.binding.redeemCode.setText(modal.getRedeemCode());
                holder.binding.rCodeTitle.setVisibility(View.VISIBLE);
                holder.binding.redeemCode.setVisibility(View.VISIBLE);
                holder.binding.copyRCode.setVisibility(View.VISIBLE);
            }else {
                holder.binding.rCodeTitle.setVisibility(View.GONE);
                holder.binding.redeemCode.setVisibility(View.GONE);
                holder.binding.copyRCode.setVisibility(View.GONE);
            }


        }else {
            holder.binding.redHisStatus.setText("Rejected");
            holder.binding.redHisStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_light, context.getTheme()));
        }

        holder.binding.redHisResponseTime.setText(modal.getRedHisResponseTime());

        String img = Constants.REDEEM_IMG_URL + modal.getRedHisImg();
        Picasso.get()
                .load(img)
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.redHisImg);

        holder.binding.copyRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Code", modal.getRedeemCode());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(context, "Code Copied", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return redeemHistoryList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        RedeemHisRvItemBinding binding;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = RedeemHisRvItemBinding.bind(itemView);
        }
    }
}
