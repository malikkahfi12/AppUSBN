package com.example.asus.qunasta;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.widget.ListViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.asus.qunasta.Adapter.CategoryAdapter;
import com.example.asus.qunasta.Common.SpaceDecoration;
import com.example.asus.qunasta.DBHelper.DBHelper;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recycler_category;
    private CategoryAdapter adapter;
    private MediaPlayer audio;
    private Button btn_essay;

    MaterialSearchBar materialSearchBar;
    List<String> suggestList =  new ArrayList<>();

    DBHelper database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ujian Nasional SD");

        //Init View
        materialSearchBar = (MaterialSearchBar)findViewById(R.id.search_bar);
        recycler_category = (RecyclerView)findViewById(R.id.recycler_category);
        recycler_category.setHasFixedSize(true);
        recycler_category.setLayoutManager(new GridLayoutManager(this, 1));

        // GET Category From Adapter
        final CategoryAdapter adapter = new CategoryAdapter(HomeActivity.this, DBHelper.getInstance(this).getAllCategories());
        int spaceInPixel = 4;
        recycler_category.addItemDecoration(new SpaceDecoration(spaceInPixel));
        recycler_category.setAdapter(adapter);

        //Init DB
        database = new DBHelper(this);
        audio = MediaPlayer.create(this,R.raw.pilih_pelajaran);
        audio.setVolume(1,1);
        audio.start();

        materialSearchBar.setHint("Cari Ujian");
        materialSearchBar.setCardViewElevation(10);
        loadSuggestList();
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest = new ArrayList<>();
                for(String search:suggestList)
                {
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled)
                    recycler_category.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        btn_essay = (Button)findViewById(R.id.btn_Essay);
        btn_essay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,EssayActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startSearch(String text) {
        adapter = new CategoryAdapter(this,database.getCategoryByName(text));
        recycler_category.setAdapter(adapter);
    }

    private void loadSuggestList() {
        suggestList = database.getNames();
        materialSearchBar.setLastSuggestions(suggestList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feedback_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_feedback){
            Intent intent = new Intent(HomeActivity.this,FeedbackActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
