package com.example.plantreapp.search;

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
import com.example.plantreapp.entities.Plant;
import com.example.plantreapp.entities.PlantInfo;
import com.example.plantreapp.myPlants.PlantListAdapter;

public class DBPlantInfoListAdapter extends ListAdapter<PlantInfo, DBPlantInfoListAdapter.DBPlantInfoViewHolder> {

    DBPlantInfoListAdapter.PlantInfoClickInterface plantClickInterface;

    public DBPlantInfoListAdapter(@NonNull DiffUtil.ItemCallback<PlantInfo> diffCallback, DBPlantInfoListAdapter.PlantInfoClickInterface plantClickInterface) {
        super(diffCallback);
        this.plantClickInterface = plantClickInterface;
    }

    @NonNull
    @Override
    public DBPlantInfoListAdapter.DBPlantInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DBPlantInfoListAdapter.DBPlantInfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_plant, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DBPlantInfoListAdapter.DBPlantInfoViewHolder holder, int position) {
        PlantInfo plantInfo = getItem(position);
        holder.bind(plantInfo);
    }

    class DBPlantInfoViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, descriptionTextView;
        ImageButton deleteButton;
        RelativeLayout plantItem;

        public DBPlantInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextview);
            descriptionTextView = itemView.findViewById(R.id.DescriptionTextView);
            deleteButton = itemView.findViewById(R.id.deletePlantButton);
            plantItem = itemView.findViewById(R.id.plantItem);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Changed to get item at the position - conforms to db call to delete an item
                    plantClickInterface.onDelete(getItem(getAdapterPosition()));
                }
            });

            plantItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    plantClickInterface.onSelect(getItem(getAdapterPosition()));
                }
            });
        }

        public void bind(PlantInfo plantInfo) {
            nameTextView.setText(plantInfo.getName());
            descriptionTextView.setText(plantInfo.getScientific_name());
        }
    }

    public interface PlantInfoClickInterface {
        public void onDelete(PlantInfo plantInfo);
        public void onSelect(PlantInfo plantInfo);
    }
}