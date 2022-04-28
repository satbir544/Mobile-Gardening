package com.example.plantreapp.myPlants;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.plantreapp.R;
import com.example.plantreapp.connection.ConnBtnActivity;
import com.example.plantreapp.connection.ConnectionActivity;
import com.example.plantreapp.journals.JournalsActivity;
import com.example.plantreapp.search.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PlantInfoActivity extends AppCompatActivity {

    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_info);

        Intent i = getIntent();
        PlantInfo info = i.getParcelableExtra("plantInfo");

        TextView plantTxtName = findViewById(R.id.textViewPlantNamesAndStage);
        TextView plantTxtDesc = findViewById(R.id.textViewPlantDesc);
        TextView plantTxtRate = findViewById(R.id.textViewPlantRates);
        TextView plantTxtMoisture = findViewById(R.id.textViewPlantMoisture);

        String tempNameTxt = ("<b><u>Name:</u></b> <br>" + info.getName() +"<br><b><u>Scientific Name:</u></b> <br>" + info.getScifiName() + "<br><b><u>Stage:</u></b> <br>" + info.getStage());
        String tempDescTxt = ("<b><u>Description:</b></u> <br>" + info.getDescription());
        String tempRateTxt = ("<b><u>Seed Rate:</b></u> <br>" + (int)info.getSeedWaterRate() + " HR<br><b><u>Seedling Rate:</b></u> <br>" + (int)info.getSeedlingWaterRate() + " HR<br><b><u>Mature Rate:</b></u> <br>" + (int)info.getMatureWaterRate() + " HR");
        String tempMoistureTxt = ("<b><u>Min Seed Moisture:</b></u> <br>" + (int)info.getMinSeedMoisture() + "%<br><b><u>Max Seed Moisture:</b></u> <br>" + (int)info.getMaxSeedMoisture() + "%<br><b><u>Min Seedling Moisture:</b></u> <br>" + (int)info.getMinSeedlingMoisture() + "%<br><b><u>Max Seedling Moisture:</b></u> <br>" + (int)info.getMaxSeedlingMoisture() + "%<br><b><u>Min Mature Moisture:</b></u> <br>" + (int)info.getMinMatureMoisture() + "%<br><b><u>Max Mature Moisture:</b></u> <br>" + (int)info.getMaxMatureMoisture() + "%");

        plantTxtName.setText(Html.fromHtml(tempNameTxt));
        plantTxtDesc.setText(Html.fromHtml(tempDescTxt));
        plantTxtRate.setText(Html.fromHtml(tempRateTxt));
        plantTxtMoisture.setText(Html.fromHtml(tempMoistureTxt));

        // set actionbar title to "my plants"
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(info.getName());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.my_plants_item);

        // nav click handler
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_item:
                    startActivity(new Intent(getApplicationContext(), ConnBtnActivity.class));
                    return true;
                case R.id.my_plants_item:
                    startActivity(new Intent(getApplicationContext(), MyPlantsActivity.class));
                    return true;
                case R.id.search_item:
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                    return true;
                case R.id.connection_item:
                    startActivity(new Intent(getApplicationContext(), ConnectionActivity.class));
                    return true;
            }
            return false;
        });
    }

    public void launchJournals(View view) {
        Intent i = getIntent();
        PlantInfo info = i.getParcelableExtra("plantInfo");
        int uid = i.getIntExtra("plantUid", 0);

        Intent intent = new Intent(PlantInfoActivity.this, JournalsActivity.class);
        intent.putExtra("plantName", info.getName());
        intent.putExtra("plantUid", uid);
        startActivity(intent);
    }
}