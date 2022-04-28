package com.example.plantreapp.connection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantreapp.R;

import java.util.ArrayList;

public class WaterInfoAdapter extends RecyclerView.Adapter<WaterInfoAdapter.WaterInfoViewHolder> {
    private Context context;
    private ArrayList<WaterInfo> waterInfoArr;

    WaterInfoInterface waterInfoInterface;

    public WaterInfoAdapter(Context context, ArrayList<WaterInfo> waterInfoArr, WaterInfoInterface waterInfoInterface) {
        this.context = context;
        this.waterInfoArr = waterInfoArr;
        this.waterInfoInterface = waterInfoInterface;
    }



    @NonNull
    @Override
    public WaterInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_row_conn_btn,parent,false);
        return new WaterInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WaterInfoViewHolder holder, int position) {
        WaterInfo waterInfo = waterInfoArr.get(position);
        holder.setDetails(waterInfo);

    }



    @Override
    public int getItemCount() {
        return waterInfoArr.size();
    }

    public class WaterInfoViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar bar;
        Button btn;
        TextView txt, txtPlantName;
        Button selectPlantBtn;

        public WaterInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            bar = itemView.findViewById(R.id.progessbar_circular);
            btn = itemView.findViewById(R.id.btnSendPump);
            txt = itemView.findViewById(R.id.text_status);
            selectPlantBtn = itemView.findViewById(R.id.btnSelectPlant);
            txtPlantName = itemView.findViewById(R.id.text_plant_name);

            btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    waterInfoInterface.onWaterBtnClick(getAdapterPosition(),waterInfoArr);
                }
            });
            selectPlantBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    waterInfoInterface.onSelectPlantClick(getAdapterPosition());
                }
            });
        }


        public void setDetails(WaterInfo waterInfo) {
            bar.setProgress(waterInfo.getPercentage());
            btn.setText(String.format("%s", waterInfo.getBtnName()));
            txt.setText(String.format("%s", waterInfo.getText()));
            txtPlantName.setText(String.format("%s", waterInfo.getPlantText()));
        }
    }

    public interface WaterInfoInterface {
        void onWaterBtnClick(int position, ArrayList<WaterInfo> w);
        void onSelectPlantClick(int position);
    }
}