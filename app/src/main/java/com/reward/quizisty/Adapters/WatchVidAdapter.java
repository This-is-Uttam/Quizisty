package com.reward.quizisty.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reward.quizisty.Modals.AdNetModal;
import com.reward.quizisty.R;
import com.reward.quizisty.databinding.WatchVidRvItemBinding;

import java.util.ArrayList;


public class WatchVidAdapter extends RecyclerView.Adapter<WatchVidAdapter.Viewholder> {
    ArrayList<AdNetModal> watchVidList;
    Context context;

//    ButtonClickListener buttonClickListener;

    public WatchVidAdapter(ArrayList<AdNetModal> watchVidList, Context context/*, ButtonClickListener buttonClickListener*/) {
        this.watchVidList = watchVidList;
        this.context = context;
//        this.buttonClickListener = buttonClickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.watch_vid_rv_item, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        AdNetModal modal = watchVidList.get(position);
        holder.binding.watVidName.setText(modal.getAdNetName());
        holder.binding.watVidImg.setImageResource(modal.getAdNetImg());
//        holder.binding.watchBtn.setText(modal.getBtnTitle());
//        Log.d("WatchVidAdapter", "onBindViewHolder: button enable: "+modal.isButtonEnable());
       /* if (modal.isButtonEnable()){
            holder.binding.watchBtn.setEnabled(true);
            holder.binding.watchBtn.setBackgroundTintList(context.getColorStateList(R.color.TertiaryColor));
            holder.binding.watchBtn.setTextColor(context.getResources().getColor(R.color.white, context.getTheme()));
        }else {
            holder.binding.watchBtn.setEnabled(false);
            holder.binding.watchBtn.setBackgroundTintList(context.getColorStateList(R.color.dis));
            holder.binding.watchBtn.setTextColor(context.getResources().getColor(R.color.whiteOnly, context.getTheme()));
        }*/

        holder.binding.watchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                buttonClickListener.onButtonClick(modal.getId());
                Toast.makeText(context, modal.getAdNetName(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return watchVidList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        WatchVidRvItemBinding binding;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = WatchVidRvItemBinding.bind(itemView);
        }
    }
}
