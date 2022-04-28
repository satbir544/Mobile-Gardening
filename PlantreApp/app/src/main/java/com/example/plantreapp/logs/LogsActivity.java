
package com.example.plantreapp.logs;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantreapp.R;
import com.example.plantreapp.connection.ConnBtnActivity;
import com.example.plantreapp.connection.ConnectionActivity;
import com.example.plantreapp.entities.Log;
import com.example.plantreapp.myPlants.MyPlantsActivity;
import com.example.plantreapp.search.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

/*Logs Screen*/

public class LogsActivity extends AppCompatActivity
        implements LogListAdapter.LogClickInterface {

    private LogListAdapter logListAdapter;
    private LogsViewModel logsViewModel;

    private String journalName;
    private static int journalUid;

    private List<Log> tmpLogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        Intent i = getIntent();

        String title = i.getStringExtra("journalName");
        int uid = i.getIntExtra("journalUid", 0);
        if (uid != 0) {
            journalUid = uid;
        }

        journalName = title;
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title.toUpperCase() + " LOGS");


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


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        logListAdapter = new LogListAdapter( Log.Companion.getItemCallback() , this);
        recyclerView.setAdapter(logListAdapter);

        logsViewModel = new ViewModelProvider(this, new LogViewModelFactory(this.getApplication(), journalUid)).get(LogsViewModel.class);
        logsViewModel.getLogList().observe(this, new Observer<List<Log>>() {
            @Override
            public void onChanged(List<Log> logs) {
                logListAdapter.submitList(logs);
                tmpLogList = logListAdapter.getCurrentList();
            }
        });

        if (i.getStringExtra("newNoteName") != null) {
            String name = i.getStringExtra("newNoteName");
            String info = i.getStringExtra("newNoteInfo");
            Log log = new Log(null, name, journalUid, "date...", info, "");
            logsViewModel.addLog(log);
        }
    }

    public void addItem(View view) {
        Intent intent = new Intent(LogsActivity.this, AddLogActivity.class);
        intent.putExtra("journalName", journalName);
        startActivity(intent);
    }

    @Override
    public void onDelete(Log log) {
        logsViewModel.deleteLog(log);
    }

    @Override
    public void onSelect(Log log) {
        Intent intent = new Intent(LogsActivity.this, NoteActivity.class);

        intent.putExtra("logName", log.getName());
        intent.putExtra("logDescription", log.getDescription());

        // todo: send entire log object

        startActivity(intent);
    }


    // search logs
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
                    logListAdapter.submitList(tmpLogList);
                }
                else {
                    List<Log> logs = logListAdapter.getCurrentList();
                    List<Log> filtered = new ArrayList<Log>();

                    for (Log log : logs) {
                        if (log.getName().toLowerCase().contains(newText.toLowerCase())){
                            filtered.add(log);
                        }
                    }

                    logListAdapter.submitList(filtered);
                }

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
