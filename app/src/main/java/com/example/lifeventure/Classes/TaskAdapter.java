package com.example.lifeventure.Classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lifeventure.R;

import java.util.ArrayList;



public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.viewHolder> {
    private ArrayList<Task> mList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onCheck(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        public TextView taskAdapterTitle;
        public TextView taskAdapterDesc;
        public TextView taskAdapterExp;
        public TextView taskAdapterDate;
        public CheckBox taskAdapterCheck;

        public viewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            taskAdapterTitle = itemView.findViewById(R.id.taskCardTitle);
            taskAdapterDesc = itemView.findViewById(R.id.taskCardDesc);
            taskAdapterExp = itemView.findViewById(R.id.taskCardExp);
            taskAdapterDate = itemView.findViewById(R.id.taskCardSche);
            taskAdapterCheck = itemView.findViewById(R.id.taskCardComplete);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            taskAdapterCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onCheck(position);
                        }
                    }
                }
            });
        }
    }

    public TaskAdapter(ArrayList<Task> list) {
        mList = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_taskcreate, parent, false);
        viewHolder vh = new viewHolder(v, mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Task current = mList.get(position);

        holder.taskAdapterTitle.setText(current.getTaskClassTitle());
        holder.taskAdapterDesc.setText(current.getTaskClassDesc());
        holder.taskAdapterExp.setText((int) current.getTaskClassExp()+" EXP");
        holder.taskAdapterDate.setText((CharSequence) current.getTaskClassDate());
        holder.taskAdapterCheck.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
