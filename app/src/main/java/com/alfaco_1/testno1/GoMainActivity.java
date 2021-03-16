package com.alfaco_1.testno1;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class GoMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

//    private AppBarConfiguration mAppBarConfiguration;
    private Dialog signInDialog;
    private static final int HOME_FRAGMENT = 0;
    private static final int CART_FRAGMENT = 1;
    private static final int ORDERS_FRAGMENT = 2;
    private static final int WISHLIST_FRAGMENT = 3;
    private static final int REWARDS_FRAGMENT = 4;
    private static final int ACCOUNT_FRAGMENT = 5;
    public static Boolean showCart = false;
    public static boolean onResetPasswordFragment = false;
    public static boolean setSignUpFragment = false;
    private TextView badgeCount;

    private FrameLayout frameLayout;
    private ImageView actionbar_logo;
    private  int currentFragment = -1;
    private NavigationView navigationView;
    private Window window;
    private int scrollFlags;
    private AppBarLayout.LayoutParams params;

    private FirebaseUser firebaseUser;

    public static DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        actionbar_logo = findViewById(R.id.actionber_logo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //        window = getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

         params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
         scrollFlags = params.getScrollFlags();

        drawer = findViewById(R.id.drawer_layout);
        //todo: toggol button

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        frameLayout = findViewById(R.id.main_framelayout);


        if (showCart) {
            // drawer.setDrawerLockMode(1);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            gotoFragment("My Cart", new MyCartFragment(), -2);
        } else {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            setFragment(new HomeFragment(), HOME_FRAGMENT);
        }

//        setFragment(new HomeFragment(),HOME_FRAGMENT);

//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                 R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_sign_in,R.id.nav_main_home,R.id.nav_main_cart,R.id.nav_main_order,R.id.nav_mywishlist,R.id.nav_myrewards,R.id.nav_my_account)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

        signInDialog = new Dialog(GoMainActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.sign_up_btn);
        final Intent registerIntent = new Intent(GoMainActivity.this,SignUpActivity.class);
        final Intent signInIntent = new Intent(GoMainActivity.this,SignIn.class);


//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn.disableCloseBtn=true;
               // SignUpActivity.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(signInIntent);
            }
        });

        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn.disableCloseBtn=true;
                //SignUpActivity.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser == null){
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(false);
        }else{
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(true);
        }

        invalidateOptionsMenu();
    }

//back pressed
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            if(currentFragment == HOME_FRAGMENT){
                currentFragment = -1;
                super.onBackPressed();
            }else {
                if(showCart){
                    showCart = false;
                    finish();
                }else {
                    actionbar_logo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new HomeFragment(), HOME_FRAGMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        }
    }
//back pressed
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(currentFragment == HOME_FRAGMENT){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.go_main, menu);

            MenuItem cartItem = menu.findItem(R.id.main_cart_icon);

               cartItem.setActionView(R.layout.badge_layout);
               ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
               badgeIcon.setImageResource(R.mipmap.shopping_cart_white);
               badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);

               if(firebaseUser != null){
                   if (DBqueries.cartList.size() == 0) {
                       DBqueries.loadCartList(GoMainActivity.this, new Dialog(GoMainActivity.this),false,badgeCount,new TextView(GoMainActivity.this));
                   }else{
                           badgeCount.setVisibility(View.VISIBLE);
                       if(DBqueries.cartList.size() < 99){
                           badgeCount.setText(String.valueOf(DBqueries.cartList.size()));
                       }else{
                           badgeCount.setText("99");
                       }
                   }
               }
               cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if(firebaseUser == null){
                           signInDialog.show();
                       }else {
                           gotoFragment("My Cart",new MyCartFragment(),CART_FRAGMENT);
                       }
                   }
               });

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.main_search_icon){

            return true;
        }else if(id == R.id.main_notification_icon){

        }else if(id == R.id.main_cart_icon){

            if(firebaseUser == null){
                signInDialog.show();
            }else {
                gotoFragment("My Cart",new MyCartFragment(),CART_FRAGMENT);
            }
            return true;
        }
        else if(id == android.R.id.home){
            if(showCart){
                showCart = false;
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void gotoFragment(String title, Fragment fragment, int fragmentNo) {
      actionbar_logo.setVisibility(View.GONE);
      getSupportActionBar().setDisplayShowTitleEnabled(true);
      getSupportActionBar().setTitle(title);
      invalidateOptionsMenu();
      setFragment(fragment,fragmentNo);
      if(fragmentNo == CART_FRAGMENT || showCart){
          navigationView.getMenu().getItem(3).setChecked(true);
          params.setScrollFlags(0);
      }else {
          params.setScrollFlags(scrollFlags);
      }
    }

    private void myCart() {

    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

    MenuItem menuItem;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
       menuItem = item;

        if(firebaseUser != null) {

            drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    int id = menuItem.getItemId();
                    if (id == R.id.nav_my_home) {
                        actionbar_logo.setVisibility(View.VISIBLE);
                        invalidateOptionsMenu();
                        setFragment(new HomeFragment(), HOME_FRAGMENT);
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            drawer.closeDrawer(GravityCompat.START);
//            return true;
                    } else if (id == R.id.nav_my_order) {
                        gotoFragment("My Orders", new MyOrdersFragment(), ORDERS_FRAGMENT);
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            drawer.closeDrawer(GravityCompat.START);
//            return true;
                    } else if (id == R.id.nav_my_rewards) {
                        gotoFragment("My Rewards", new MyRewardsFragment(), REWARDS_FRAGMENT);
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            drawer.closeDrawer(GravityCompat.START);
//            return true;
                    } else if (id == R.id.nav_my_cart) {
                        gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            drawer.closeDrawer(GravityCompat.START);
//            return true;

                    } else if (id == R.id.nav_my_wishlist) {
                        gotoFragment("My Wishlist", new MyWishlistFragment(), WISHLIST_FRAGMENT);
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            drawer.closeDrawer(GravityCompat.START);
//            return true;

                    } else if (id == R.id.nav_my_account) {
                        gotoFragment("My Account", new MyAccountFragment(), ACCOUNT_FRAGMENT);
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            drawer.closeDrawer(GravityCompat.START);
//            return true;
                    } else if (id == R.id.nav_sign_out) {
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            drawer.closeDrawer(GravityCompat.START);
                        FirebaseAuth.getInstance().signOut();
                        DBqueries.clearData();
                        Intent signUp = new Intent(GoMainActivity.this,SignUpActivity.class);
                        startActivity(signUp);
                        finish();
                    }
                }
            });

            return true;
        }else{
            signInDialog.show();
            return false;
        }
    }
    private void setFragment(Fragment fragment, int fragmentNo){
           if(fragmentNo != currentFragment) {
               currentFragment = fragmentNo;
               FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
               fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
               fragmentTransaction.replace(frameLayout.getId(), fragment);
               fragmentTransaction.commit();
           }
    }
}