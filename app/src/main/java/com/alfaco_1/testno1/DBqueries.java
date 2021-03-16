package com.alfaco_1.testno1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DBqueries {

    public static boolean addressesSelected = false;

    public static FirebaseFirestore   firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<CategoryModel> categoryModelList = new ArrayList<>();

    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public static List<String> loadedCategoriesNames = new ArrayList<>();

    public static List<String> wishList = new ArrayList<>();
    public static List<WishlistModel> wishlistModelList = new ArrayList<>();

    public static List<String> myRatedIds = new ArrayList<>();
    public static List<Long> myRating = new ArrayList<>();

    public static List<String> cartList = new ArrayList<>();
    public static List<CartItemModel> cartItemModelList = new ArrayList<>();

    public static int selectedAddress = -1;
    public static List<AdddressesModel> adddressesModelList = new ArrayList<>();

    public static void loadCategories(final RecyclerView categoryRecyclerView, final Context context){
        categoryModelList.clear();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(),documentSnapshot.get("categoryName").toString()));
                            }
                            CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList);
                            categoryRecyclerView.setAdapter(categoryAdapter);
                            categoryAdapter.notifyDataSetChanged();
                        }else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public static void loadFragmentData(final RecyclerView homePageRecyclerView, final Context context, final int index, String categoryName){
        firebaseFirestore.collection("CATEGORIES")
                .document(categoryName.toUpperCase())
                .collection("TOP_DEALS")
                .orderBy("index")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()){

                                if((long)documentSnapshot.get("view_type") == 0){
                                    List<SliderModel> sliderModelList = new ArrayList<>();
                                    long no_of_banners = (long) documentSnapshot.get("no_of_banners");
                                    for(long x =1;x < no_of_banners + 1 ;x++){
                                        sliderModelList.add(new SliderModel(documentSnapshot.get("banner_"+x).toString(),documentSnapshot.get("banner_"+x+"_background").toString()));
                                    }
                                    lists.get(index).add(new HomePageModel(0,sliderModelList));

                                }else if((long)documentSnapshot.get("view_type") == 1){
                                    lists.get(index).add(new HomePageModel(documentSnapshot.get("strip_ad_banner").toString(),documentSnapshot.get("background").toString(),1));
                                }else if((long)documentSnapshot.get("view_type") == 2){
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                    List<WishlistModel> viewAllProductList = new ArrayList<>();
                                    List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
                                    long no_of_products = (long) documentSnapshot.get("no_of_products");
                                    for(long x =1;x < no_of_products + 1 ;x++){
                                        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(
                                                documentSnapshot.get("product_ID_"+x).toString(),
                                                documentSnapshot.get("product_image_"+x).toString(),
                                                documentSnapshot.get("product_title_"+x).toString(),
                                                documentSnapshot.get("product_subtitle_"+x).toString(),
                                                documentSnapshot.get("product_price_"+x).toString()));

                                        viewAllProductList.add(new WishlistModel(
                                                documentSnapshot.get("product_ID_"+x).toString()
                                                ,documentSnapshot.get("product_image_"+x).toString()
                                                ,documentSnapshot.get("product_full_title_"+x).toString()
                                                ,(long)documentSnapshot.get("free_coupens_"+x)
                                                ,documentSnapshot.get("average_rating_"+x).toString()
                                                ,(long)documentSnapshot.get("total_ratings_"+x)
                                                ,documentSnapshot.get("product_price_"+x).toString()
                                                ,documentSnapshot.get("cutted_price_"+x).toString()
                                                ,(boolean)documentSnapshot.get("COD_"+x)));
                                    }
                                    lists.get(index).add(new HomePageModel(2,documentSnapshot.get("layout_title").toString(),documentSnapshot.get("layout_background").toString(),horizontalProductScrollModelList,viewAllProductList));
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                }else if((long)documentSnapshot.get("view_type") == 3){

                                    List<HorizontalProductScrollModel> GridLayoutModelList = new ArrayList<>();
                                    long no_of_products = (long) documentSnapshot.get("no_of_products");
                                    for(long x =1;x < no_of_products + 1 ;x++){
                                        GridLayoutModelList.add(new HorizontalProductScrollModel(
                                                documentSnapshot.get("product_ID_"+x).toString(),
                                                documentSnapshot.get("product_image_"+x).toString(),
                                                documentSnapshot.get("product_title_"+x).toString(),
                                                documentSnapshot.get("product_subtitle_"+x).toString(),
                                                documentSnapshot.get("product_price_"+x).toString()));
                                    }
                                    lists.get(index).add(new HomePageModel(3,documentSnapshot.get("layout_title").toString(),documentSnapshot.get("layout_background").toString(),GridLayoutModelList));

                                }
                            }
                            HomePageAdapter homePageAdapter = new HomePageAdapter(lists.get(index));
                            homePageRecyclerView.setAdapter(homePageAdapter);
                            homePageAdapter.notifyDataSetChanged();
                            HomeFragment.swipeRefreshLayout.setRefreshing(false);
                        }else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void loadWishlist(final Context context, final Dialog dialog, final boolean loadProductData){
       wishList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA")
                .document("MY_WISHLIST")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if(task.isSuccessful()){
                for(long x = 0; x < (long) task.getResult().get("list_size"); x++){
                    wishList.add(task.getResult().get("product_ID_"+x).toString());
                    if(DBqueries.wishList.contains(ProductDetailsActivity.productID)){
                        ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = true;
                        if(ProductDetailsActivity.addToWhislistBtn != null) {
                            ProductDetailsActivity.addToWhislistBtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.red1));
                        }
                    }else {
                        if(ProductDetailsActivity.addToWhislistBtn != null) {
                            ProductDetailsActivity.addToWhislistBtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.red1));
                        }
                        ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                    }
                    if(loadProductData) {
                        wishlistModelList.clear();
                        final String productId = task.getResult().get("product_ID_" + x).toString();
                        firebaseFirestore.collection("PRODUCTS").document(productId)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            wishlistModelList.add(new WishlistModel(
                                                     productId
                                                    ,task.getResult().get("product_image_1").toString()
                                                    , task.getResult().get("product_title").toString()
                                                    , (long) task.getResult().get("free_coupens")
                                                    , task.getResult().get("average_rating").toString()
                                                    , (long) task.getResult().get("total_ratings")
                                                    , task.getResult().get("product_price").toString()
                                                    , task.getResult().get("cutted_price").toString()
                                                    , (boolean) task.getResult().get("COD")));
                                            MyWishlistFragment.wishlistApdapter.notifyDataSetChanged();
                                        } else {
                                            String error = task.getException().getMessage();
                                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }else {
                String error = task.getException().getMessage();
                Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            }
        });
}
    public static void removeFromWishList(final int index, final Context context){
       final String removedProductId = wishList.get(index);

        wishList.remove(index);
        Map<String,Object> updateWishlist = new HashMap<>();

        for(int x = 0; x < wishList.size(); x++){
            updateWishlist.put("product_ID_"+x,wishList.get(x));
        }
        updateWishlist.put("list_size",(long)wishList.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA")
                .document("MY_WISHLIST")
                .set(updateWishlist)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                           if(wishlistModelList.size() != 0){
                             wishlistModelList.remove(index);
                             MyWishlistFragment.wishlistApdapter.notifyDataSetChanged();
                           }
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                            Toast.makeText(context, "Product Removed", Toast.LENGTH_SHORT).show();
                        }else {
                            if(ProductDetailsActivity.addToWhislistBtn != null) {
                                ProductDetailsActivity.addToWhislistBtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.red1));
                            }
                            wishList.add(index,removedProductId);
                            String error = task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }
//                        if(ProductDetailsActivity.addToWhislistBtn != null) {
//                            ProductDetailsActivity.addToWhislistBtn.setEnabled(true);
//                        }
                        ProductDetailsActivity.running_wishlist_query = false;
                    }
                });
    }

    public static void loadRatingList(final Context context){
        if(!ProductDetailsActivity.running_rating_query) {
            ProductDetailsActivity.running_rating_query = true;
            myRatedIds.clear();
            myRating.clear();
            firebaseFirestore.collection("USERS")
                    .document(FirebaseAuth.getInstance().getUid())
                    .collection("USER_DATA")
                    .document("MY_RATINGS")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {

                            myRatedIds.add(task.getResult().get("product_ID_" + x).toString());
                            myRating.add((long) task.getResult().get("rating_" + x));

                            if (task.getResult().get("product_ID_" + x).toString().equals(ProductDetailsActivity.productID)) {
                                ProductDetailsActivity.initialRating = Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1;
                                if(ProductDetailsActivity.rateNowContainer != null){
                                    ProductDetailsActivity.setRating(ProductDetailsActivity.initialRating);
                                }
                            }

                        }

                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                    ProductDetailsActivity.running_rating_query = false;
                }
            });
        }
    }

    public static void loadCartList(final Context context, final Dialog dialog, final boolean loadProductData, final TextView badgeCount,final TextView cartTotalAmount){
        cartList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA")
                .document("MY_CART")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    for(long x = 0; x < (long) task.getResult().get("list_size"); x++){
                        cartList.add(task.getResult().get("product_ID_"+x).toString());
                        if(DBqueries.cartList.contains(ProductDetailsActivity.productID)){
                            ProductDetailsActivity.ALREADY_ADDED_TO_CART = true;
                        }else {
                            ProductDetailsActivity.ALREADY_ADDED_TO_CART = false;
                        }
                        if(loadProductData) {
                            cartItemModelList.clear();
                            final String productId = task.getResult().get("product_ID_" + x).toString();
                            firebaseFirestore.collection("PRODUCTS").document(productId)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {

                                                int index = 0;
                                                if(cartList.size() >= 2){
                                                    index = cartList.size() - 2;
                                                }

                                                cartItemModelList.add(index,new CartItemModel(CartItemModel.CART_ITEM,
                                                        productId
                                                        ,task.getResult().get("product_image_1").toString()
                                                        , task.getResult().get("product_title").toString()
                                                        , (long) task.getResult().get("free_coupens")
                                                        , task.getResult().get("product_price").toString()
                                                        , task.getResult().get("cutted_price").toString()
                                                        , (long) 1
                                                        ,(long) 0
                                                        ,(long) 0
                                                        ,(boolean)task.getResult().get("in_stock")));

                                                if(cartList.size() == 1){
                                                    cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                                                    LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                                                    parent.setVisibility(View.VISIBLE);
                                                }

                                                if(cartList.size() == 0){
                                                    cartItemModelList.clear();
                                                }
                                                MyCartFragment.cartAdapter.notifyDataSetChanged();
                                            } else {
                                                String error = task.getException().getMessage();
                                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
//                    if(cartList.size() != 0){
//                        badgeCount.setVisibility(View.VISIBLE);
//                    }else {
//                        badgeCount.setVisibility(View.INVISIBLE);
//                    }

                    if(DBqueries.cartList.size() < 99){
                        badgeCount.setText(String.valueOf(DBqueries.cartList.size()));
                    }else{
                        badgeCount.setText("99");
                    }
                }else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }
    public static void removedFromCart(final int index, final Context context,final TextView cartTotalAmount){
        final String removedProductId = cartList.get(index);

        cartList.remove(index);
        Map<String,Object> updateCartlist = new HashMap<>();

        for(int x = 0; x < cartList.size(); x++){
            updateCartlist.put("product_ID_"+x,cartList.get(x));
        }
        updateCartlist.put("list_size",(long)cartList.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA")
                .document("MY_CART")
                .set(updateCartlist)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            if(cartItemModelList.size() != 0){
                                cartItemModelList.remove(index);
                                MyCartFragment.cartAdapter.notifyDataSetChanged();
                            }

                            if(cartList.size() == 0){
                                LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                                parent.setVisibility(View.GONE);
                                cartItemModelList.clear();
                            }
                            if(ProductDetailsActivity.cartItem != null) {
                                ProductDetailsActivity.cartItem.setActionView(null);
                            }
                            Toast.makeText(context, "Product Removed", Toast.LENGTH_SHORT).show();
                        }else {
                            cartList.add(index,removedProductId);
                            String error = task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }
                        ProductDetailsActivity.running_cart_query = false;
                    }
                });
    }

    public static void loadAddresses(final Context context, final Dialog dialog){
        adddressesModelList.clear();
        firebaseFirestore.collection("USERS").document(
                FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA")
                .document("MY_ADDRESSES")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    Intent delivery;

                    if((long)task.getResult().get("list_size") == 0){
                        delivery = new Intent(context,AddAddressActivity.class);
                        delivery.putExtra("INTENT","deliveryIntent");
                    }else {
                        for(long x = 1; x<(long)task.getResult().get("list_size") +1;x++){
                            adddressesModelList.add(new AdddressesModel(
                                    task.getResult().get("fullname_"+x).toString(),
                                    task.getResult().get("address_"+x).toString(),
                                    task.getResult().get("pincode_"+x).toString(),
                                    (boolean)task.getResult().get("selected_"+x)));
                            if((boolean)task.getResult().get("selected_"+x)){
                                selectedAddress = Integer.parseInt(String.valueOf(x - 1));
                            }
                        }
                        delivery = new Intent(context,DeliveryActivity.class);

                    }
                    context.startActivity(delivery);
                }else{
                    String error = task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    public static void clearData(){
      categoryModelList.clear();
      lists.clear();
      loadedCategoriesNames.clear();
      wishList.clear();
      wishlistModelList.clear();
    }
}
