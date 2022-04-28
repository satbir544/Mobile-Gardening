package com.example.plantreapp.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

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
import com.example.plantreapp.entities.PlantInfo;
import com.example.plantreapp.myPlants.MyPlantsActivity;
import com.example.plantreapp.myPlants.PlantInfoActivity;
import com.example.plantreapp.myPlants.PlantListAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements DBPlantInfoListAdapter.PlantInfoClickInterface {
    private SearchViewModel _viewModel;
    private DBPlantInfoListAdapter _listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup view
        setContentView(R.layout.activity_search);

        // set actionbar title to "Search"
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Search");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.search_item);

        // nav click handler
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_item:
                        startActivity(new Intent(getApplicationContext(), ConnBtnActivity.class));
                        return true;
                    case R.id.my_plants_item:
                        startActivity(new Intent(getApplicationContext(), MyPlantsActivity.class));
                        return true;
                    case R.id.search_item:
                        //startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                        return true;
                    case R.id.connection_item:
                        startActivity(new Intent(getApplicationContext(), ConnectionActivity.class));
                        return true;
                }
                return false;
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        _listAdapter = new DBPlantInfoListAdapter(PlantInfo.Companion.getItemCallback(), (DBPlantInfoListAdapter.PlantInfoClickInterface) this);

        recyclerView.setAdapter(_listAdapter);

        _viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        _viewModel.getPlantList().observe(this, new Observer<List<PlantInfo>>() {
            @Override
            public void onChanged(List<PlantInfo> plants) {
                _listAdapter.submitList(plants);
            }
        });
    }

    public void refreshList(View view) {
        _viewModel.refreshList();
    }

    @Override
    public void onDelete(PlantInfo plantInfo) {

    }

    @Override
    public void onSelect(PlantInfo plantInfo) {
        Intent intent = new Intent(this, DBPlantInfoActivity.class);
        intent.putExtra("PLANTINFO", plantInfo);
        startActivity(intent);
    }
}
