package com.alfaco_1.testno1;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alfaco_1.testno1.GoMainActivity.setSignUpFragment;
import static com.alfaco_1.testno1.GoMainActivity.showCart;

public class ProductDetailsActivity extends AppCompatActivity {

    public static boolean running_wishlist_query = false;
    public static boolean running_rating_query = false;
    public static boolean running_cart_query = false;

    private ViewPager productImagesViewPager;
    private TextView productTitle;
    private TextView averageRatingMiniView;
    private TextView totalRatingMiniView;
    private TextView productPrize;
    private TextView cuttedPrice;
    private ImageView codIndicator;
    private TextView tvCodIndicator;
    private TabLayout viewpagerIndicator;


    private LinearLayout coupenRedemptionLayout;
    private Button coupenRedeemBtn;
    private TextView rewardTitle;
    private TextView rewardBody;

    public static boolean ALREADY_ADDED_TO_WISHLIST = false;
    public static boolean ALREADY_ADDED_TO_CART = false;

    public static FloatingActionButton addToWhislistBtn;

    ////////// Rating layout
    public static int initialRating;
    private TextView averageRating;
    public static LinearLayout rateNowContainer;
    private TextView totalRatings;
    private LinearLayout ratingsNoContainer;
    private TextView totalRatingFigure;
    private LinearLayout ratingsProgressBarContainer;
    ////////// Rating layout

    private Button buyNowBtn;
    private LinearLayout addToCartBtn;
    public static MenuItem cartItem;
    private TextView badgeCount;

    ////Product description
    private ConstraintLayout productDetailsOnlyContainer;
    private ConstraintLayout productDetailsTabsContainer;
    private TextView productOnlyDescriptionBody;
    private ViewPager productDetailsViewPager;
    private TabLayout productDetailsTablayout;
    private String productDescription;
    private String productOtherDetails;
    private List<ProductSpecificationModel> productSpecificationModelList= new ArrayList<>();
    //    public static int tabPosition = -1;
    /////Product description

    private FirebaseFirestore firebaseFirestore;

    ///////////coupenDialog

    public static TextView coupenTitle;
    public static TextView coupenExpireData;
    public static TextView coupenBody;
    private static  RecyclerView coupensRecyclerView;
    private static LinearLayout selectedCoupen;
    ///////////coupenDialog
    private DocumentSnapshot documentSnapshot;
    private Dialog loadingDialog;

    public static String productID;

    private FirebaseUser firebaseUser;

    private Dialog signInDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coupenRedeemBtn = findViewById(R.id.coupen_redemption_btn);
        buyNowBtn = findViewById(R.id.buy_now_btn);
        productImagesViewPager = findViewById(R.id.product_images_viewpager);
        viewpagerIndicator = findViewById(R.id.viewpager_indicator);
        addToWhislistBtn = findViewById(R.id.add_to_wishlist_btn);
        productDetailsViewPager = findViewById(R.id.product_details_viewpager);
        productDetailsTablayout = findViewById(R.id.product_details_tab_layout);
        productTitle = findViewById(R.id.product_title);
        averageRatingMiniView = findViewById(R.id.tv_product_rating_miniview);
        totalRatingMiniView = findViewById(R.id.total_rating_miniview);
        productPrize = findViewById(R.id.product_prize);
        cuttedPrice = findViewById(R.id.cutted_price);
        tvCodIndicator = findViewById(R.id.tv_cod_indicator);
        codIndicator = findViewById(R.id.cod_indicator_imageview);
        rewardTitle = findViewById(R.id.reward_title);
        rewardBody = findViewById(R.id.reward_body);
        productDetailsTabsContainer = findViewById(R.id.product_details_tabs_container);
        productDetailsOnlyContainer = findViewById(R.id.product_details_container);
        productOnlyDescriptionBody = findViewById(R.id.product_detail_body);
        totalRatings = findViewById(R.id.total_ratings);
        ratingsNoContainer = findViewById(R.id.ratings_numbers_container);
        totalRatingFigure = findViewById(R.id.total_ratings_figure);
        ratingsProgressBarContainer = findViewById(R.id.ratings_progressbar_container);
        averageRating = findViewById(R.id.average_rating);
        addToCartBtn = findViewById(R.id.ad_to_cart_btn);
        coupenRedemptionLayout = findViewById(R.id.coupen_redemption_layout);
        firebaseFirestore = FirebaseFirestore.getInstance();

        initialRating = -1;

        /////loading dialog

        loadingDialog = new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        ////loading dialog

        productID = getIntent().getStringExtra("PRODUCT_ID");

        final List<String> productImages = new ArrayList<>();

        firebaseFirestore.collection("PRODUCTS").document(productID)//getIntent().getStringExtra("PRODUCT_ID  ")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                 documentSnapshot = task.getResult();

                 for(long x=1; x < (long)documentSnapshot.get("no_of_product_images") +1 ;x++){
                   productImages.add(documentSnapshot.get("product_image_"+x).toString());
                 }

                    ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                    productImagesViewPager.setAdapter(productImagesAdapter);
                    productTitle.setText(documentSnapshot.get("product_title").toString());
                    averageRatingMiniView.setText(documentSnapshot.get("average_rating").toString());
                    totalRatingMiniView.setText("("+(long)documentSnapshot.get("total_ratings")+")ratings");
                    cuttedPrice.setText("Tk."+documentSnapshot.get("cutted_price").toString()+"/-");
                    productPrize.setText("Tk."+documentSnapshot.get("product_price").toString()+"/-");

                    if((boolean) documentSnapshot.get("COD")){
                        codIndicator.setVisibility(View.VISIBLE);
                        tvCodIndicator.setVisibility(View.VISIBLE);
                    }else {
                        codIndicator.setVisibility(View.INVISIBLE);
                        tvCodIndicator.setVisibility(View.INVISIBLE);
                    }

                    rewardTitle.setText((long)documentSnapshot.get("free_coupens") + documentSnapshot.get("free_coupen_title").toString());
                    rewardBody.setText(documentSnapshot.get("free_coupen_body").toString());

                    if((boolean)documentSnapshot.get("use_tab_layout")){
                         productDetailsTabsContainer.setVisibility(View.VISIBLE);
                         productDetailsOnlyContainer.setVisibility(View.GONE);
                         productDescription = documentSnapshot.get("product_description").toString();
                         //ProductSpecificationFragment.productSpecificationModelList = new ArrayList<>();
                         productOtherDetails = documentSnapshot.get("product_other_details").toString();
                         for(long x = 1; x < (long) documentSnapshot.get("total_spec_titles")+1; x++){

                             productSpecificationModelList.add(new ProductSpecificationModel(0,documentSnapshot.get("spec_title_"+x).toString()));
                             for(long y = 1; y < (long) documentSnapshot.get("spec_title_"+x+"_total_fields")+1; y++){
                                 productSpecificationModelList.add(new ProductSpecificationModel(1,documentSnapshot.get("spec_title_"+x+"_field_"+y+"_name").toString(),documentSnapshot.get("spec_title_"+x+"_field_"+y+"_value").toString()));
                             }
                         }

                    }else{
                        productDetailsTabsContainer.setVisibility(View.GONE);
                        productDetailsOnlyContainer.setVisibility(View.VISIBLE);
                        productOnlyDescriptionBody.setText(documentSnapshot.get("product_description").toString());
                    }

                    totalRatings.setText((long)documentSnapshot.get("total_ratings")+" ratings");

                    for(int x = 0; x < 5; x ++){
                     TextView rating = (TextView) ratingsNoContainer.getChildAt(x);
                     rating.setText(String.valueOf((long)documentSnapshot.get((5-x)+"_star")));

                        ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                        int maxProgress = Integer.parseInt(String.valueOf((long)documentSnapshot.get("total_ratings")));
                        progressBar.setMax(maxProgress);
                        progressBar.setProgress(Integer.parseInt(String.valueOf((long)documentSnapshot.get((5-x)+"_star"))));
                    }
                    totalRatingFigure.setText(String.valueOf((long)documentSnapshot.get("total_ratings")));
                    averageRating.setText(documentSnapshot.get("average_rating").toString());
                    productDetailsViewPager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(),productDetailsTablayout.getTabCount(),productDescription,productOtherDetails,productSpecificationModelList));

                    if(firebaseUser != null) {

                        if(DBqueries.myRating.size() == 0){
                            DBqueries.loadRatingList(ProductDetailsActivity.this);
                        }
                        if (DBqueries.cartList.size() == 0) {
                            DBqueries.loadCartList(ProductDetailsActivity.this, loadingDialog,false,badgeCount,new TextView(ProductDetailsActivity.this));
                        }
                        if (DBqueries.wishList.size() == 0) {
                            DBqueries.loadWishlist(ProductDetailsActivity.this, loadingDialog,false);
                        } else {
                            loadingDialog.dismiss();
                        }
                    }else{
                        loadingDialog.dismiss();
                    }

                    if(DBqueries.myRatedIds.contains(productID)){
                        int index = DBqueries.myRatedIds.indexOf(productID);
                        initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index))) -1;
                        setRating(initialRating);
                    }

                    if(DBqueries.cartList.contains(productID)){
                        ALREADY_ADDED_TO_CART = true;
                    }else {
                        ALREADY_ADDED_TO_CART = false;
                    }

                    if(DBqueries.wishList.contains(productID)){
                        ALREADY_ADDED_TO_WISHLIST = true;
                        addToWhislistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.red1));

                    }else {
                        addToWhislistBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#F3F3F3")));
                        ALREADY_ADDED_TO_WISHLIST = false;
                    }

                    if((boolean)documentSnapshot.get("in_stock")){
                        addToCartBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(firebaseUser == null){
                                    signInDialog.show();
                                }else {
                                    if(!running_cart_query) {
                                        running_cart_query = true;
                                        if (ALREADY_ADDED_TO_CART) {
                                            running_cart_query = false;
                                            Toast.makeText(ProductDetailsActivity.this, "Already added to cart!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Map<String, Object> addProduct = new HashMap<>();
                                            addProduct.put("product_ID_" + String.valueOf(DBqueries.cartList.size()), productID);
                                            addProduct.put("list_size", (long) (DBqueries.cartList.size() + 1));

                                            firebaseFirestore.collection("USERS")
                                                    .document(firebaseUser.getUid()).collection("USER_DATA")
                                                    .document("MY_CART")
                                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        if (DBqueries.cartItemModelList.size() != 0) {
                                                            DBqueries.cartItemModelList.add(0,new CartItemModel(CartItemModel.CART_ITEM,
                                                                    productID
                                                                    ,documentSnapshot.get("product_image_1").toString()
                                                                    , documentSnapshot.get("product_title").toString()
                                                                    , (long) documentSnapshot.get("free_coupens")
                                                                    , documentSnapshot.get("product_price").toString()
                                                                    , documentSnapshot.get("cutted_price").toString()
                                                                    , (long) 1
                                                                    ,(long) 0
                                                                    ,(long) 0
                                                                    ,(boolean) documentSnapshot.get("in_stock")));
                                                        }

                                                        ALREADY_ADDED_TO_CART = true;
                                                        DBqueries.cartList.add(productID);
                                                        Toast.makeText(ProductDetailsActivity.this, "Product has been added to Cart", Toast.LENGTH_SHORT).show();
                                                        invalidateOptionsMenu();
                                                        running_cart_query = false;
                                                    }
                                                    else {
                                                        running_cart_query = false;
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                        }
                                    }
                                }
                            }
                        });
                    }else{
                        buyNowBtn.setVisibility(View.GONE);
                        TextView outOfStock = (TextView) addToCartBtn.getChildAt(0);
                        outOfStock.setText("Out of Stock");
                        outOfStock.setTextColor(getResources().getColor(R.color.red1));
                        outOfStock.setCompoundDrawables(null,null,null,null);
                    }

                }else{
                    loadingDialog.dismiss();
                    String error = task.getException().getMessage();
                    Toast.makeText(ProductDetailsActivity.this,error,Toast.LENGTH_SHORT).show();
                }
            }
        });

//        List<Integer> productImages = new ArrayList<>();
//        productImages.add(R.mipmap.applogo1);
//        productImages.add(R.mipmap.phone1);
//        productImages.add(R.mipmap.person_logo);
//        productImages.add(R.mipmap.add_photo);


        viewpagerIndicator.setupWithViewPager(productImagesViewPager,true);

        addToWhislistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firebaseUser == null){
                    signInDialog.show();
                }else {
//                    addToWhislistBtn.setEnabled(false);

                    if(!running_wishlist_query) {

                        running_wishlist_query = true;

                        if (ALREADY_ADDED_TO_WISHLIST) {
                            int index = DBqueries.wishList.indexOf(productID);
                            DBqueries.removeFromWishList(index, ProductDetailsActivity.this);

//                        ALREADY_ADDED_TO_WISHLIST = false;
                            addToWhislistBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#F3F3F3")));
                        } else {
                            addToWhislistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.red1));
                            Map<String, Object> addProduct = new HashMap<>();
                            addProduct.put("product_ID_" + String.valueOf(DBqueries.wishList.size()), productID);
                            addProduct.put("list_size", (long) (DBqueries.wishList.size() + 1));
                            firebaseFirestore.collection("USERS")
                                    .document(firebaseUser.getUid()).collection("USER_DATA")
                                    .document("MY_WISHLIST")
                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                                    if (DBqueries.wishlistModelList.size() != 0) {
                                                        DBqueries.wishlistModelList.add(new WishlistModel(
                                                                productID
                                                                , documentSnapshot.get("product_image_1").toString()
                                                                , documentSnapshot.get("product_title").toString()
                                                                , (long) documentSnapshot.get("free_coupens")
                                                                , documentSnapshot.get("average_rating").toString()
                                                                , (long) documentSnapshot.get("total_ratings")
                                                                , documentSnapshot.get("product_price").toString()
                                                                , documentSnapshot.get("cutted_price").toString()
                                                                , (boolean) documentSnapshot.get("COD")));
                                                    }

                                                    ALREADY_ADDED_TO_WISHLIST = true;
                                                    addToWhislistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.red1));
                                                    DBqueries.wishList.add(productID);
                                                    Toast.makeText(ProductDetailsActivity.this, "Product has been added to wishlist", Toast.LENGTH_SHORT).show();
                                    } else {
//                                        addToWhislistBtn.setEnabled(true);
                                        addToWhislistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.red1));
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                    running_wishlist_query = false;
                                }
                            });

                        }
                    }
                }
            }
        });

      productDetailsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTablayout));
      productDetailsTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
          @Override
          public void onTabSelected(TabLayout.Tab tab) {
              productDetailsViewPager.setCurrentItem(tab.getPosition());
          }

          @Override
          public void onTabUnselected(TabLayout.Tab tab) {

          }

          @Override
          public void onTabReselected(TabLayout.Tab tab) {

          }
      });
/////////////rating layout

        rateNowContainer = findViewById(R.id.rate_now_container);
        for(int x = 0; x < rateNowContainer.getChildCount();x++){
            final int starPosition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(firebaseUser == null){
                        signInDialog.show();
                    }else {
                        if(starPosition != initialRating) {
                            if (!running_rating_query) {
                                running_rating_query = true;
                                setRating(starPosition);
                                Map<String, Object> updateRating = new HashMap<>();
                                if (DBqueries.myRatedIds.contains(productID)) {
                                    TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                    TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);

                                    updateRating.put(initialRating + 1 + "_star", Long.parseLong(oldRating.getText().toString()) - 1);
                                    updateRating.put(starPosition + 1 + "_star", Long.parseLong(finalRating.getText().toString()) + 1);
                                    updateRating.put("average_rating", calculateAverageRating((long) starPosition - initialRating, true));
                                } else {
                                    updateRating.put(starPosition + 1 + "_star", (long) documentSnapshot.get(starPosition + 1 + "_star") + 1);
                                    updateRating.put("average_rating", calculateAverageRating((long) starPosition + 1, false));
                                    updateRating.put("total_ratings", (long) documentSnapshot.get("total_ratings") + 1);
                                }
                                firebaseFirestore.collection("PRODUCTS")
                                        .document(productID)
                                        .update(updateRating)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Map<String, Object> myRating = new HashMap<>();
                                                    if (DBqueries.myRatedIds.contains(productID)) {
                                                        myRating.put("rating_" + DBqueries.myRatedIds.indexOf(productID), (long) starPosition + 1);
                                                    } else {
                                                        myRating.put("list_size", (long) DBqueries.myRatedIds.size() + 1);
                                                        myRating.put("product_ID_" + DBqueries.myRatedIds.size(), productID);
                                                        myRating.put("rating_" + DBqueries.myRatedIds.size(), (long) starPosition + 1);
                                                    }
                                                    firebaseFirestore.collection("USERS")
                                                            .document(firebaseUser.getUid())
                                                            .collection("USER_DATA")
                                                            .document("MY_RATINGS")
                                                            .update(myRating)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {

                                                                        if (DBqueries.myRatedIds.contains(productID)) {
                                                                            DBqueries.myRating.set(DBqueries.myRatedIds.indexOf(productID), (long) starPosition + 1);

                                                                            TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                                                            TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                                            oldRating.setText(String.valueOf(Integer.parseInt(oldRating.getText().toString()) - 1));
                                                                            finalRating.setText(String.valueOf(Integer.parseInt(finalRating.getText().toString()) + 1));

                                                                        } else {
                                                                            DBqueries.myRatedIds.add(productID);
                                                                            DBqueries.myRating.add((long) starPosition + 1);

                                                                            TextView rating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                                            rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));

                                                                            totalRatingMiniView.setText("(" + ((long) documentSnapshot.get("total_ratings") + 1) + ")ratings");
                                                                            totalRatings.setText((long) documentSnapshot.get("total_ratings") + 1 + " ratings");
                                                                            totalRatingFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings") + 1));

                                                                            Toast.makeText(ProductDetailsActivity.this, "Thanks for your rating", Toast.LENGTH_SHORT).show();
                                                                        }

                                                                        for (int x = 0; x < 5; x++) {
                                                                            TextView ratingfigures = (TextView) ratingsNoContainer.getChildAt(x);

                                                                            ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                                                                            int maxProgress = Integer.parseInt(String.valueOf(totalRatingFigure.getText().toString()));
                                                                            progressBar.setMax(maxProgress);

                                                                            progressBar.setProgress(Integer.parseInt(ratingfigures.getText().toString()));
                                                                        }

                                                                        initialRating = starPosition;
                                                                        averageRating.setText(calculateAverageRating(0, true));
                                                                        averageRatingMiniView.setText(calculateAverageRating(0, true));

                                                                        if (DBqueries.wishList.contains(productID) && DBqueries.wishlistModelList.size() != 0) {
                                                                            int index = DBqueries.wishList.indexOf(productID);
                                                                            DBqueries.wishlistModelList.get(index).setRating(averageRating.getText().toString());
                                                                            DBqueries.wishlistModelList.get(index).setTotalRating(Long.parseLong(totalRatingFigure.getText().toString()));
                                                                        }

                                                                    } else {
                                                                        setRating(initialRating);
                                                                        String error = task.getException().getMessage();
                                                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    running_rating_query = false;
                                                                }
                                                            });
                                                } else {
                                                    running_rating_query = false;
                                                    setRating(initialRating);
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    }
                }
            });
        }

/////////////rating layout

        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                if(firebaseUser == null){
                  signInDialog.show();
                }else {
                    DeliveryActivity.cartItemModelList = new ArrayList<>();
                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.CART_ITEM,
                            productID
                            ,documentSnapshot.get("product_image_1").toString()
                            , documentSnapshot.get("product_title").toString()
                            , (long) documentSnapshot.get("free_coupens")
                            , documentSnapshot.get("product_price").toString()
                            , documentSnapshot.get("cutted_price").toString()
                            , (long) 1
                            ,(long) 0
                            ,(long) 0
                            ,(boolean) documentSnapshot.get("in_stock")));
                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));

                    if(DBqueries.adddressesModelList.size() == 0){
                        DBqueries.loadAddresses(ProductDetailsActivity.this,loadingDialog);

                    }else{
                        loadingDialog.dismiss();
                        Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                        startActivity(deliveryIntent);
                    }
                }
            }
        });


        ////////coupen dialog

        final Dialog checkCoupenPriceDialog = new Dialog(ProductDetailsActivity.this);
        checkCoupenPriceDialog.setContentView(R.layout.coupen_redeem_dialog);
        checkCoupenPriceDialog.setCancelable(true);
        checkCoupenPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView toggleRecyclerview =  checkCoupenPriceDialog.findViewById(R.id.toggle_recyclerview);
        coupensRecyclerView = checkCoupenPriceDialog.findViewById(R.id.coupens_recyclerview);
        selectedCoupen = checkCoupenPriceDialog.findViewById(R.id.selected_coupen);

        coupenTitle = checkCoupenPriceDialog.findViewById(R.id.coupen_title);
        coupenExpireData = checkCoupenPriceDialog.findViewById(R.id.coupen_validity);
        coupenBody = checkCoupenPriceDialog.findViewById(R.id.coupen_body);

        TextView originalPrice = checkCoupenPriceDialog.findViewById(R.id.orginal_price);
        TextView discountedPrice = checkCoupenPriceDialog.findViewById(R.id.discounted_price);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailsActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        coupensRecyclerView.setLayoutManager(layoutManager);

        List<RewardModel> rewardModelList = new ArrayList<>();
        rewardModelList.add(new RewardModel("Buy 1 get 1 free","till 2nd,June 2020","GET 30% CASHBACK on any product above Tk.1000/- and below Tk.3000/-"));
        rewardModelList.add(new RewardModel("Big deal","till 2nd,June 2020","GET 30% CASHBACK on any product above Tk.1000/- and below Tk.3000/-"));
        rewardModelList.add(new RewardModel("Cashback","till 2nd,June 2020","GET 30% CASHBACK on any product above Tk.1000/- and below Tk.3000/-"));
        rewardModelList.add(new RewardModel("Discount","till 2nd,June 2020","GET 30% CASHBACK on any product above Tk.1000/- and below Tk.3000/-"));
        rewardModelList.add(new RewardModel("Cashback","till 2nd,June 2020","GET 30% CASHBACK on any product above Tk.1000/- and below Tk.3000/-"));
        rewardModelList.add(new RewardModel("Buy 1 get 1 free","till 2nd,June 2020","GET 30% CASHBACK on any product above Tk.1000/- and below Tk.3000/-"));
        rewardModelList.add(new RewardModel("Cashback","till 2nd,June 2020","GET 30% CASHBACK on any product above Tk.1000/- and below Tk.3000/-"));
        rewardModelList.add(new RewardModel("Discount","till 2nd,June 2020","GET 30% CASHBACK on any product above Tk.1000/- and below Tk.3000/-"));
        rewardModelList.add(new RewardModel("Cashback","till 2nd,June 2020","GET 30% CASHBACK on any product above Tk.1000/- and below Tk.3000/-"));

        MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(rewardModelList,true);
        coupensRecyclerView.setAdapter(myRewardsAdapter);
        myRewardsAdapter.notifyDataSetChanged();

        toggleRecyclerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRecyclerView();
            }
        });
        ////////coupen dialog




        coupenRedeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkCoupenPriceDialog.show();
            }
        });

        ////////coupen dialog

        //////signin dialog

        signInDialog = new Dialog(ProductDetailsActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.sign_up_btn);
        final Intent registerIntent = new Intent(ProductDetailsActivity.this,SignUpActivity.class);
        final Intent signInIntent = new Intent(ProductDetailsActivity.this,SignUpActivity.class);

        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn.disableCloseBtn=true;
             //   SignUpActivity.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(signInIntent);
            }
        });

        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn.disableCloseBtn=true;
             //   SignUpActivity.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });

        /////////signin dialog

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser == null){
            coupenRedemptionLayout.setVisibility(View.GONE);
        }else{
            coupenRedemptionLayout.setVisibility(View.VISIBLE);
        }

        if(firebaseUser != null) {

            if(DBqueries.myRating.size() == 0){
                DBqueries.loadRatingList(ProductDetailsActivity.this);
            }
            if (DBqueries.wishList.size() == 0) {
                DBqueries.loadWishlist(ProductDetailsActivity.this, loadingDialog,false);
            } else {
                loadingDialog.dismiss();
            }
        }else{
            loadingDialog.dismiss();
        }

        if(DBqueries.myRatedIds.contains(productID)){
            int index = DBqueries.myRatedIds.indexOf(productID);
            initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index))) -1;
            setRating(initialRating);
        }

        if(DBqueries.cartList.contains(productID)){
            ALREADY_ADDED_TO_CART = true;
        }else {
            ALREADY_ADDED_TO_CART = false;
        }

        if(DBqueries.wishList.contains(productID)){
            ALREADY_ADDED_TO_WISHLIST = true;
            addToWhislistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.red1));

        }else {
            addToWhislistBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#F3F3F3")));
            ALREADY_ADDED_TO_WISHLIST = false;
        }
        invalidateOptionsMenu();
    }

    public  static void showDialogRecyclerView(){
        if(coupensRecyclerView.getVisibility() == View.GONE){
            coupensRecyclerView.setVisibility(View.VISIBLE);
            selectedCoupen.setVisibility(View.GONE);
        }else {
            coupensRecyclerView.setVisibility(View.GONE);
            selectedCoupen.setVisibility(View.VISIBLE);
        }
    }

    public static void setRating(int starPosition) {
            for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
                ImageView starBtn = (ImageView) rateNowContainer.getChildAt(x);
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
                if (x <= starPosition) {
                    starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffff00")));
                }
            }
    }

    private String calculateAverageRating(long currentUserRating, boolean update){
        Double totalStars = Double.valueOf(0);
        for(int x = 1; x < 6; x++){
            TextView ratingNo = (TextView) ratingsNoContainer.getChildAt(5 - x);
            totalStars = totalStars +  (Long.parseLong(ratingNo.getText().toString())*x);
        }
        totalStars = totalStars + currentUserRating;
        if(update){
            return String.valueOf(totalStars/Long.parseLong(totalRatingFigure.getText().toString())).substring(0,3);
        }else{
            return String.valueOf(totalStars/(Long.parseLong(totalRatingFigure.getText().toString()) +1)).substring(0,3);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);

        cartItem = menu.findItem(R.id.main_cart_icon);

            cartItem.setActionView(R.layout.badge_layout);
            ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
            badgeIcon.setImageResource(R.mipmap.shopping_cart_white);
            badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);

        if(firebaseUser != null){
            if (DBqueries.cartList.size() == 0) {
                DBqueries.loadCartList(ProductDetailsActivity.this, loadingDialog,false,badgeCount, new TextView(ProductDetailsActivity.this));
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
                        Intent cartIntent = new Intent(ProductDetailsActivity.this,GoMainActivity.class);
                        showCart = true;
                        startActivity(cartIntent);
                    }
                }
            });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
             finish();
            return true;
        }else if(id == R.id.main_search_icon)
        {
            return true;
        }
        else if(id == R.id.main_cart_icon)
        {
//            Intent cartIntent = new Intent(ProductDetailsActivity.this,GoMainActivity.class);
//            showCart = true;
//            startActivity(cartIntent);
//            return true;
            if(firebaseUser == null){
                signInDialog.show();
            }else {
                Intent cartIntent = new Intent(ProductDetailsActivity.this,GoMainActivity.class);
                showCart = true;
                startActivity(cartIntent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}