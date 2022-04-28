
package com.example.plantreapp.myPlants;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantreapp.R;
/*import com.example.plantreapp.TimerService;*/
import com.example.plantreapp.connection.ConnBtnActivity;
import com.example.plantreapp.connection.ConnectionActivity;
import com.example.plantreapp.entities.Plant;
import com.example.plantreapp.entities.Timer;
import com.example.plantreapp.journals.JournalsActivity;
import com.example.plantreapp.repository.PlantRepository;
import com.example.plantreapp.search.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;

/*My Plants Screen*/

public class MyPlantsActivity extends AppCompatActivity
        implements PlantListAdapter.PlantClickInterface {

    private PlantListAdapter plantListAdapter;
    private PlantsViewModel plantsViewModel;
    private List<Plant> tmpPlantList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plants);

        Intent i = getIntent();

        // set actionbar title to "my plants"
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("MY PLANTS");

        // nav bar
        setNavigation();

        // Toast.makeText(Home.this, String.valueOf(id), Toast.LENGTH_SHORT).show();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        plantListAdapter = new PlantListAdapter( Plant.Companion.getItemCallback(), this);
        recyclerView.setAdapter(plantListAdapter);

        plantsViewModel = new ViewModelProvider(this).get(PlantsViewModel.class);
        applyTexts(i);
        plantsViewModel.getPlantList().observe(this, new Observer<List<Plant>>() {
            @Override
            public void onChanged(List<Plant> plants) {
                plantListAdapter.submitList(plants);
                tmpPlantList = plantListAdapter.getCurrentList();
            }
        });
    }

    public void setNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.my_plants_item);

        // nav click handler
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_item:
                        startActivity(new Intent(getApplicationContext(), ConnBtnActivity.class));
                        return true;
                    case R.id.my_plants_item:
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

    public void addItem(View view) {
        OpenAddPlantActivity();
    }

    public void OpenAddPlantActivity() {
        Intent intent = new Intent(MyPlantsActivity.this, AddPlantActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDelete(Plant plant) {
        plantsViewModel.deletePlant(plant);
        /*Intent deletePlant = new Intent(this, TimerService.class);
        deletePlant.putExtra("deletedPlant", plant.getName());
        startForegroundService(deletePlant);*/
    }

    @Override
    public void onSelect(Plant plant) {
        PlantInfo info = new PlantInfo(
                plant.getName(),
                plant.getScientific_name(),
                null,
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
                plant.getWater_running_time()
        );

        Intent intent = new Intent(MyPlantsActivity.this, PlantInfoActivity.class);
        intent.putExtra("plantInfo", info);
        intent.putExtra("plantUid", plant.getUid());
        startActivity(intent);
    }

    // updating plants in plants view model
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void applyTexts(Intent i) {
        PlantInfo plantInfo = i.getParcelableExtra("plantInfo");
        if (plantInfo != null) {
            Plant plant = new Plant(null,
                    null,
                    null,
                    plantInfo.getName(),
                    plantInfo.getScifiName(),
                    plantInfo.getUri(),
                    plantInfo.getDescription(),
                    plantInfo.getStage(),
                    Math.round(plantInfo.getSeedWaterRate()),
                    Math.round(plantInfo.getSeedlingWaterRate()),
                    Math.round(plantInfo.getMatureWaterRate()),
                    plantInfo.getMinSeedMoisture(),
                    plantInfo.getMaxSeedMoisture(),
                    plantInfo.getMinSeedlingMoisture(),
                    plantInfo.getMaxSeedlingMoisture(),
                    plantInfo.getMinMatureMoisture(),
                    plantInfo.getMaxMatureMoisture(),
                    null,
                    plantInfo.getWaterTime() // Update
            );

            plantsViewModel.addPlant(plant);
            
            /*Intent newPlantIntent = new Intent(this, TimerService.class);
            startForegroundService(newPlantIntent);*/
        }
    }

    // search plants
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    plantListAdapter.submitList(tmpPlantList);
                }
                else {
                    List<Plant> plants = plantListAdapter.getCurrentList();
                    List<Plant> filtered = new ArrayList<Plant>();

                    for (Plant plant : plants) {
                        if (plant.getName().toLowerCase().contains(newText.toLowerCase())){
                            filtered.add(plant);
                        }
                    }

                    plantListAdapter.submitList(filtered);
                }

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
