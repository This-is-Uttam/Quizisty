package com.reward.quizisty.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reward.quizisty.Modals.Offer18TasksModal;
import com.reward.quizisty.R;
import com.reward.quizisty.databinding.Offer18TaskRvItemBinding;

import java.util.ArrayList;

public class Offer18TasksAdapter extends RecyclerView.Adapter<Offer18TasksAdapter.Viewholder> {

    ArrayList<Offer18TasksModal> trakierTasksModalsList;
    Context context;

    public Offer18TasksAdapter(ArrayList<Offer18TasksModal> trakierTasksModalsList, Context context) {
        this.trakierTasksModalsList = trakierTasksModalsList;
        this.context = context;
    }

    @NonNull
    @Override
    public Offer18TasksAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.offer18_task_rv_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Offer18TasksAdapter.Viewholder holder, int position) {

        Offer18TasksModal taskModal = trakierTasksModalsList.get(position);

        holder.binding.taskCount.setText(taskModal.getTaskCount());
        holder.binding.taskName.setText(taskModal.getTaskName());
        if (taskModal.isTaskCompleted()){
            holder.binding.taskCompletion.setAlpha(1.0f);
        }else {
            holder.binding.taskCompletion.setAlpha(0.2f);
        }
    }

    @Override
    public int getItemCount() {
        return trakierTasksModalsList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        Offer18TaskRvItemBinding binding;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = Offer18TaskRvItemBinding.bind(itemView);
        }
    }
}
