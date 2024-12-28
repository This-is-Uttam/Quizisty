package com.reward.quizisty.Adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reward.quizisty.Activities.Offer18CampaignDetailActivity;
import com.reward.quizisty.R;
import com.reward.quizisty.databinding.Offer18MainRvItemBinding;
import com.squareup.picasso.Picasso;
import com.reward.quizisty.Modals.Offer18MainListModal;

import java.util.ArrayList;

public class Offer18MainListAdapter extends RecyclerView.Adapter<Offer18MainListAdapter.Viewholder> {

    ArrayList<Offer18MainListModal> trakierMainListModals;
    Context context;

    public Offer18MainListAdapter(ArrayList<Offer18MainListModal> trakierMainListModals, Context context) {
        this.trakierMainListModals = trakierMainListModals;
        this.context = context;
    }

    @NonNull
    @Override
    public Offer18MainListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.offer18_main_rv_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Offer18MainListAdapter.Viewholder holder, int position) {
        Offer18MainListModal mainListModal = trakierMainListModals.get(position);

//        mainListModal.setAdId(mainListModal.getAdId());
        holder.binding.adTitle.setText(mainListModal.getAdTitle());
        holder.binding.adDesc.setText(mainListModal.getAdDesc());
        holder.binding.adRewardCoin.setText(mainListModal.getAdRewardCoin());
        Picasso.get()
                .load(mainListModal.getAdPosterImg())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.adPosterImg);

        holder.binding.campaignCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trakierDetailIntent = new Intent(context, Offer18CampaignDetailActivity.class);
                trakierDetailIntent.putExtra("campaignId", mainListModal.getAdId());

                context.startActivity(trakierDetailIntent);
            }
        });

        holder.binding.adClaimBtn.setOnClickListener(v -> {
            /*CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                    .setDefaultColorSchemeParams(new CustomTabColorSchemeParams.Builder()
                            .setToolbarColor(
                                    ResourcesCompat.getColor(context.getResources(),
                                            R.color.md_theme_primary, context.getTheme()))
                            .build())
                    .build();

            customTabsIntent.launchUrl(context, Uri.parse(mainListModal.getClickUrl()));*/

            Intent claimIntent = new Intent(Intent.ACTION_VIEW);
            claimIntent.setData(Uri.parse(mainListModal.getClickUrl()));
            context.startActivity(claimIntent);
        });
    }

    @Override
    public int getItemCount() {
        return trakierMainListModals.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        Offer18MainRvItemBinding binding;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = Offer18MainRvItemBinding.bind(itemView);
        }
    }
}
