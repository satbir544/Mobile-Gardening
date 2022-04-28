
package com.example.plantreapp.myPlants;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantreapp.R;
import com.example.plantreapp.connection.ConnBtnActivity;
import com.example.plantreapp.connection.ConnectionActivity;
import com.example.plantreapp.entities.Plant;
import com.example.plantreapp.entities.PlantIdentity;
import com.example.plantreapp.search.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

/*My Plants Screen*/

public class SelectPlantActivity extends AppCompatActivity
        implements PlantListAdapter.PlantClickInterface {

    private PlantListAdapter plantListAdapter;
    private PlantsViewModel plantsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_plant);

        // set actionbar title to "select plant"
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("SELECT PLANT");

        // nav bar
        setNavigation();

        // Toast.makeText(Home.this, String.valueOf(id), Toast.LENGTH_SHORT).show();

        RecyclerView recyclerView = findViewById(R.id.selectPlantRecyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        plantListAdapter = new PlantListAdapter( Plant.Companion.getItemCallback(), this);
        recyclerView.setAdapter(plantListAdapter);

        plantsViewModel = new ViewModelProvider(this).get(PlantsViewModel.class);
        plantsViewModel.getPlantList().observe(this, new Observer<List<Plant>>() {
            @Override
            public void onChanged(List<Plant> plants) {
                plantListAdapter.submitList(plants);
            }
        });
    }

    public void setNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.home_item);

        // nav click handler
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_item:
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
            }
        });
    }

    @Override
    public void onDelete(Plant plant) {}

    @Override
    public void onSelect(Plant plant) {

        String plantName = plant.getName();
        Bundle extras = getIntent().getExtras();
        int i = -1;
        if(extras != null)
        {
            i = extras.getInt("position");
        }
        Plant tempPlant = new Plant(plant.getUid(),
                null,
                null,
                plant.getName(),
                plant.getScientific_name(),
                plant.getPicture(),
                plant.getDescription(),
                plant.getStage(),
                plant.getSeed_water_rate(),
                plant.getSeedling_water_rate(),
                plant.getMature_water_rate(),
                plant.getMin_seed_moisture(),
                plant.getMax_seed_moisture(),
                plant.getMin_seedling_moisture(),
                plant.getMax_seedling_moisture(),
                plant.getMin_mature_moisture(),
                plant.getMax_mature_moisture(),
                i == -1 ? null : i,
                plant.getWater_running_time()
        );
/*        Intent intent = new Intent(SelectPlantActivity.this, ConnBtnActivity.class);
        intent.putExtra("plantInfo", info);
        intent.putExtra("position", getIntent().getIntExtra("position", -1));
        intent.putExtra("plantUid", plant.getUid());*/
        Intent intent = new Intent(SelectPlantActivity.this, ConnBtnActivity.class);

        // Update or add items in repository
        plantsViewModel.updatePlantPosition(tempPlant, i);

/*        Bundle returnExtras = new Bundle();
        returnExtras.putString("plantName", plantName);
        returnExtras.putInt("position", i);
        intent.putExtras(returnExtras);*/
        startActivity(intent);
    }
}
