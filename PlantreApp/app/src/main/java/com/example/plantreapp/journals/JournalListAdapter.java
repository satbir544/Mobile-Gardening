package com.example.plantreapp.journals;

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
import com.example.plantreapp.entities.Journal;
import com.example.plantreapp.entities.Plant;
import com.example.plantreapp.myPlants.PlantListAdapter;

public class JournalListAdapter extends ListAdapter<Journal, JournalListAdapter.JournalViewHolder> {

    JournalClickInterface journalClickInterface;

    protected JournalListAdapter(@NonNull DiffUtil.ItemCallback<Journal> diffCallback, JournalListAdapter.JournalClickInterface journalClickInterface) {
        super(diffCallback);
        this.journalClickInterface = journalClickInterface;
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JournalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_journal, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
        Journal journal = getItem(position);
        holder.bind(journal);
    }

    class JournalViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "JournalViewHolder";
        TextView nameTextView, descriptionTextView;
        ImageButton deleteButton;
        RelativeLayout journalItem;

        public JournalViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextview);
            descriptionTextView = itemView.findViewById(R.id.DescriptionTextView);
            deleteButton = itemView.findViewById(R.id.delJournalBtn);
            journalItem = itemView.findViewById(R.id.journalItem);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    journalClickInterface.onDelete(getItem(getAdapterPosition()));
                }
            });

            journalItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    journalClickInterface.onSelect(getItem(getAdapterPosition()));
                }
            });
        }

        public void bind(Journal journal) {
            nameTextView.setText(journal.getName());
            descriptionTextView.setText(journal.getDescription());
        }
    }

    interface JournalClickInterface {
        public void onDelete(Journal journal);
        public void onSelect(Journal journal);
    }
}
