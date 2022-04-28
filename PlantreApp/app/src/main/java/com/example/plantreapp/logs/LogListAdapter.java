package com.example.plantreapp.logs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantreapp.R;
import com.example.plantreapp.entities.Log;

public class LogListAdapter extends ListAdapter<Log, LogListAdapter.LogViewHolder> {

    LogClickInterface logClickInterface;

    protected LogListAdapter(@NonNull DiffUtil.ItemCallback<Log> diffCallback, LogListAdapter.LogClickInterface logClickInterface) {
        super(diffCallback);
        this.logClickInterface = logClickInterface;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LogViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_log, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        Log log = getItem(position);
        holder.bind(log);
    }

    class LogViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "LogViewHolder";
        TextView nameTextView, descriptionTextView;
        ImageButton deleteButton;
        RelativeLayout logItem;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextview);
            descriptionTextView = itemView.findViewById(R.id.DescriptionTextView);
            deleteButton = itemView.findViewById(R.id.delLogBtn);
            logItem = itemView.findViewById(R.id.logItem);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logClickInterface.onDelete(getItem(getAdapterPosition()));
                }
            });

            logItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logClickInterface.onSelect(getItem(getAdapterPosition()));
                }
            });
        }

        public void bind(Log log) {
            nameTextView.setText(log.getName());
            descriptionTextView.setText(log.getDescription());
        }
    }

    interface LogClickInterface {
        public void onDelete(Log log);
        public void onSelect(Log log);
    }
}
