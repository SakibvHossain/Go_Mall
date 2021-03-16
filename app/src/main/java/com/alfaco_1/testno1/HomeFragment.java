package com.alfaco_1.testno1;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.widget.Toast.LENGTH_SHORT;
import static com.alfaco_1.testno1.DBqueries.categoryModelList;
//import static com.alfaco_1.testno1.DBqueries.homePageModelList;
import static com.alfaco_1.testno1.DBqueries.lists;
import static com.alfaco_1.testno1.DBqueries.loadCategories;
import static com.alfaco_1.testno1.DBqueries.loadFragmentData;
import static com.alfaco_1.testno1.DBqueries.loadedCategoriesNames;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    private  ConnectivityManager manager;
    private NetworkInfo activeNetwork;
    public static SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private RecyclerView homepageRecyclerView;
    private HomePageAdapter adapter;
    private ImageView noInternetConnection;
    private Button retryBtn;
//    private FirebaseFirestore firebaseFirestore;
    private List<CategoryModel> categoryModelfakeList = new ArrayList<>();
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();
    //////////////////////Garbage code////////////////////////////////
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    //////////////Main code started from here/////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home2, container, false); // this layout provided after creating the fragment by default and  it is initialized
      // Also note that you have to crate view because by default fragment just return the layout you have to return view as your requirement
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        noInternetConnection = view.findViewById(R.id.no_internet_connection); //here we initialize our layout for internet connection
        homepageRecyclerView = view.findViewById(R.id.home_page_recyclerview);
        categoryRecyclerView = view.findViewById(R.id.categories_recyclerview);//recyclerView scrolling list for list items means that view can take list of view and you can easily handle this
        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorAccent),getContext().getResources().getColor(R.color.colorPrimary),getContext().getResources().getColor(R.color.red1));

        retryBtn = view.findViewById(R.id.retry_button);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()); //handles the organization of UI components in a view
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); //orientation of that layout
        categoryRecyclerView.setLayoutManager(layoutManager); //set it on that layout


        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(RecyclerView.VERTICAL);
        homepageRecyclerView.setLayoutManager(testingLayoutManager);

        /////categories fake list
        categoryModelfakeList.add(new CategoryModel("null",""));
        categoryModelfakeList.add(new CategoryModel("",""));
        categoryModelfakeList.add(new CategoryModel("",""));
        categoryModelfakeList.add(new CategoryModel("",""));
        categoryModelfakeList.add(new CategoryModel("",""));
        categoryModelfakeList.add(new CategoryModel("",""));
        categoryModelfakeList.add(new CategoryModel("",""));
        categoryModelfakeList.add(new CategoryModel("",""));
        categoryModelfakeList.add(new CategoryModel("",""));
        categoryModelfakeList.add(new CategoryModel("",""));
        ////categories fake list

        /////home page fake list
        List<SliderModel> sliderModelFakeList = new ArrayList<>();
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));

        List<HorizontalProductScrollModel> horizontalProductScrollModelFakeList = new ArrayList<>();
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));

        homePageModelFakeList.add(new HomePageModel(0,sliderModelFakeList));
        homePageModelFakeList.add(new HomePageModel("","#dfdfdf",1));
        homePageModelFakeList.add(new HomePageModel(2,"","#dfdfdf",horizontalProductScrollModelFakeList,new ArrayList<WishlistModel>()));
        homePageModelFakeList.add(new HomePageModel(3,"","#dfdfdf",horizontalProductScrollModelFakeList));



        /////home page fake list


        categoryAdapter = new CategoryAdapter(categoryModelfakeList); //here we create adapter
        adapter = new HomePageAdapter(homePageModelFakeList);


        manager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE); //You can use the ConnectivityManager to check that you're connected to the internet, and if you are, to determine what type of connection is in place.
         activeNetwork = manager.getActiveNetworkInfo();

        if(null!= activeNetwork && activeNetwork.isConnected() == true) {
            GoMainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            noInternetConnection.setVisibility(View.GONE); //our layout to make understand user that internet is connected or not
            retryBtn.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homepageRecyclerView.setVisibility(View.VISIBLE);

            /*
            * 1.RecyclerView scrolling list for list item
            * 2.Layout for one item of data
            * 3.layoutManager handles the organization of UI components in a view
            * 4.Adapter that connects data to the RecyclerView
            * 5.ViewHolder has information for displaying one item.
            *
            * 6.Each view group has a layout manager
            * 7.Built-in layout manager(What is LayoutManager)
            *     i.LinearLayoutManager
            *     ii.GridLayoutManager
            *     iii.StaggeredGridLayoutManager
            *     iv.Extend RecyclerView.LayoutManager
            * 8.What is an adapter?
            *     i.Helps incompatible interfaces work together
            *     ii.Intermediary between data and view
            *     iii.Manages creating, updating, adding, deleting view items as underlying data changes RecyclerView.Adapter
            * i.Adapter has 3 required methods--- onCreateViewHolder(), isBindViewHolder(), getItemCount()
            * //todo: prottak method different different vhaba likta hoy activity ata akrokom fragment a arak rokom
            * 9.What is a ViewHolder?
            *     i.Used by the adapter to prepare one view with data for one list item
            *     ii.Layout specified in an XML resource file
            *     iii.Can have clickable elements
            *     iv.Is placed by the layout manager
            *     v.RecyclerView. ViewHolder
            * 10.Summary
            *     i.Add RecyclerView dependency to build.gradle if needed
            *     ii.Add RecyclerView to layout
            *     iii.Create XML layout for item
            *     iv.Extend RecyclerView.Adapter
            *     v.Extend RecyclerView.ViewHolder
            *     vi.In Activity onCreate(), create RecyclerView with adapter and layout manager
            * 11.Dependency to app/build.gradle
            *   compile 'com.android.support:recyclerView-v7:26.1.0' //todo: akhana compile hobe na implemences hobe
            * */

            if(categoryModelList.size() == 0){
                loadCategories(categoryRecyclerView,getContext());
            }else {
                categoryAdapter = new CategoryAdapter(categoryModelList);
                categoryAdapter.notifyDataSetChanged();
            }
            categoryRecyclerView.setAdapter(categoryAdapter); // set that adapter here

            if(lists.size() == 0){
                loadedCategoriesNames.add("HOME");
                lists.add(new ArrayList<HomePageModel>());
                loadFragmentData(homepageRecyclerView,getContext(),0,"Home");
            }else {
                adapter = new HomePageAdapter(lists.get(0));
                adapter.notifyDataSetChanged(); // this is used to refresh the data
            }
            homepageRecyclerView.setAdapter(adapter);

        }else {
            GoMainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            retryBtn.setVisibility(View.VISIBLE);
            categoryRecyclerView.setVisibility(View.GONE);
            homepageRecyclerView.setVisibility(View.GONE);
            Glide.with(this).load(R.mipmap.connection_error).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
        }

        ////refresh layout

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                reloadPage();
            }
        });

        /////refresh layout
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadPage();
            }
        });
        return view;
    }

    private void reloadPage(){
        activeNetwork = manager.getActiveNetworkInfo();
        categoryModelList.clear();
        lists.clear();
        loadedCategoriesNames.clear();

        if(null!= activeNetwork && activeNetwork.isConnected() == true) {
            GoMainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            noInternetConnection.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homepageRecyclerView.setVisibility(View.VISIBLE);
            categoryAdapter = new CategoryAdapter(categoryModelfakeList);
            adapter = new HomePageAdapter(homePageModelFakeList);
            categoryRecyclerView.setAdapter(categoryAdapter);
            homepageRecyclerView.setAdapter(adapter);

            loadCategories(categoryRecyclerView,getContext());
            loadedCategoriesNames.add("HOME");
            lists.add(new ArrayList<HomePageModel>());
            loadFragmentData(homepageRecyclerView,getContext(),0,"Home");

        }else {
            GoMainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            Toast.makeText(getContext(), "Connection not found", LENGTH_SHORT).show();
            categoryRecyclerView.setVisibility(View.GONE);
            homepageRecyclerView.setVisibility(View.GONE);
            Glide.with(getContext()).load(R.mipmap.connection_error).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
            retryBtn.setVisibility(View.VISIBLE);
        }
    }
}