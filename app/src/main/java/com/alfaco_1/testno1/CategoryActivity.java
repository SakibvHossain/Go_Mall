package com.alfaco_1.testno1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.alfaco_1.testno1.DBqueries.lists;
import static com.alfaco_1.testno1.DBqueries.loadFragmentData;
import static com.alfaco_1.testno1.DBqueries.loadedCategoriesNames;

public class CategoryActivity extends AppCompatActivity {


    private RecyclerView testing;
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();
    private HomePageAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ////////bannerSlider

        /////home page fake list
        List<SliderModel> sliderModelFakeList = new ArrayList<>();
        sliderModelFakeList.add(new SliderModel("null","#FFFFFF"));
        sliderModelFakeList.add(new SliderModel("null","#FFFFFF"));
        sliderModelFakeList.add(new SliderModel("null","#FFFFFF"));
        sliderModelFakeList.add(new SliderModel("null","#FFFFFF"));
        sliderModelFakeList.add(new SliderModel("null","#FFFFFF"));

        List<HorizontalProductScrollModel> horizontalProductScrollModelFakeList = new ArrayList<>();
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));

        homePageModelFakeList.add(new HomePageModel(0,sliderModelFakeList));
        homePageModelFakeList.add(new HomePageModel("","#FFFFFF",1));
        homePageModelFakeList.add(new HomePageModel(2,"","#FFFFFF",horizontalProductScrollModelFakeList,new ArrayList<WishlistModel>()));//from here
        homePageModelFakeList.add(new HomePageModel(3,"","#FFFFFF",horizontalProductScrollModelFakeList));//changed

        /////home page fake list

        // 2.24,3.23,3.46,2.75,3.06

        ////////bannerSlider

        /////////////
        testing = (RecyclerView) findViewById(R.id.category_recyclerview);
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(CategoryActivity.this);
        testingLayoutManager.setOrientation(RecyclerView.VERTICAL);
        testing.setLayoutManager(testingLayoutManager);
        adapter = new HomePageAdapter(homePageModelFakeList);

        int listPosition = 0;
        for (int x=0; x<loadedCategoriesNames.size();x++){
            if(loadedCategoriesNames.get(x).equals(title.toUpperCase())){
                listPosition = x;
            }
        }

        if(listPosition == 0){
            loadedCategoriesNames.add(title.toUpperCase());
            lists.add(new ArrayList<HomePageModel>());
//            adapter = new HomePageAdapter(lists.get(loadedCategoriesNames.size() - 1));
            loadFragmentData(testing,this,loadedCategoriesNames.size() - 1,title);
        }else{
         adapter = new HomePageAdapter(lists.get(listPosition));
        }
        testing.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.search1)
        {
            Toast.makeText(CategoryActivity.this,"Yes I'm Search",Toast.LENGTH_LONG).show();
            return true;
        }else if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}