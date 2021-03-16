package com.alfaco_1.testno1;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

class HomePageAdapter extends RecyclerView.Adapter {
private List<HomePageModel> homePageModelList;
private RecyclerView.RecycledViewPool recycledViewPool;
private int lastPosition = -1;
public HomePageAdapter(List<HomePageModel>homePageModelList){
    this.homePageModelList = homePageModelList;
    recycledViewPool = new RecyclerView.RecycledViewPool();
}

    @Override
    public int getItemViewType(int position) {
     switch (homePageModelList.get(position).getType()){
         case  0:
             return HomePageModel.BANNER_SLIDER;
         case  1:
             return HomePageModel.STRIP_AD_BANNER;
         case  2:
             return HomePageModel.HORIZONTAL_PRODUCT_VIEW;
         case  3:
             return HomePageModel.GRID_PRODUCT_VIEW;
         default:
             return -1;
     }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
         switch (viewType){
             case HomePageModel.BANNER_SLIDER:
                 View bannerSliderView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sliding_ad_layout,viewGroup,false);
                 return new BannerSliderViewHolder(bannerSliderView);
             case HomePageModel.STRIP_AD_BANNER:
                 View stripAdView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.strip_ad_layout,viewGroup,false);
                 return new StripAdBannerViewHolder(stripAdView);
             case HomePageModel.GRID_PRODUCT_VIEW://from here
                 View gridProuctView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_product_layout,viewGroup,false);
                 return new gridProductViewHolder(gridProuctView);
             case HomePageModel.HORIZONTAL_PRODUCT_VIEW://changed
                 View horizontalProuctView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_layout,viewGroup,false);
                 return new HorizontalProductViewholder(horizontalProuctView);
             default:
                 return null;
         }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewholder, int position) {
    switch (homePageModelList.get(position).getType()){
        case HomePageModel.BANNER_SLIDER:
           List<SliderModel> sliderModelList = homePageModelList.get(position).getSliderModelList();
            ((BannerSliderViewHolder)viewholder).setBannerSliderViewPage(sliderModelList);
            break;
         case HomePageModel.STRIP_AD_BANNER:
             String resource = homePageModelList.get(position).getResource();
             String color = homePageModelList.get(position).getBackgroundColor();
             ((StripAdBannerViewHolder)viewholder).setStripAd(resource,color);
             break;
        case HomePageModel.HORIZONTAL_PRODUCT_VIEW:
            String layoutColor = homePageModelList.get(position).getBackgroundColor();
            String horizontalLayoutTitle = homePageModelList.get(position).getTitle();
            List<WishlistModel> viewAllProductList = homePageModelList.get(position).getViewAllProductList();
            List<HorizontalProductScrollModel> horizontalProductScrollModelList = homePageModelList.get(position).getHorizontalProductScrollModelList();
            ((HorizontalProductViewholder)viewholder).setHorizontalProductLayout(horizontalProductScrollModelList,horizontalLayoutTitle,layoutColor,viewAllProductList);
            break;
        case HomePageModel.GRID_PRODUCT_VIEW:
            String gridLayoutcolor = homePageModelList.get(position).getBackgroundColor();
            String gridLayoutTitle = homePageModelList.get(position).getTitle();
            List<HorizontalProductScrollModel> gridProductScrollModelList = homePageModelList.get(position).getHorizontalProductScrollModelList();
            ((gridProductViewHolder)viewholder).setGridProductLayout(gridProductScrollModelList,gridLayoutTitle,gridLayoutcolor);
            break;
        default:
            return;
    }
        if(lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(viewholder.itemView.getContext(), R.anim.fade_in);
            viewholder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }

    public class BannerSliderViewHolder extends RecyclerView.ViewHolder{
        private ViewPager bannerSliderViewPage;
        private int currentPage ;
        private Timer timer;
        final private long DELAY_TIME = 3000;
        final private long PERIOD_TIME = 3000;
        private List<SliderModel> arrangedList;
    public BannerSliderViewHolder(@NonNull View itemView) {
        super(itemView);
        bannerSliderViewPage = itemView.findViewById(R.id.banner_slider_view_pager);

    }
        private void setBannerSliderViewPage(final List<SliderModel> sliderModelList){
            currentPage = 2;
            if(timer != null){
            timer.cancel();
        }
           arrangedList = new ArrayList<>();

            for(int x=0;x<sliderModelList.size();x++){
             arrangedList.add(x,sliderModelList.get(x));
            }

            arrangedList.add(0,sliderModelList.get(sliderModelList.size()-2));
            arrangedList.add(1,sliderModelList.get(sliderModelList.size()-1));
            arrangedList.add(sliderModelList.get(0));
            arrangedList.add(sliderModelList.get(1));

            SliderAdapter sliderAdapter = new SliderAdapter(arrangedList);
            bannerSliderViewPage.setAdapter(sliderAdapter);
            bannerSliderViewPage.setClipToPadding(false);
            bannerSliderViewPage.setPageMargin(20);
            bannerSliderViewPage.setCurrentItem(currentPage);
            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if(state == ViewPager.SCROLL_STATE_IDLE){
                        pageLopper(arrangedList);
                    }
                }
            };
            bannerSliderViewPage.addOnPageChangeListener(onPageChangeListener);
            startbannerSliderShow(arrangedList);
            bannerSliderViewPage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    pageLopper(arrangedList);
                    stopBannerSliderShow();
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        startbannerSliderShow(arrangedList);
                    }
                    return false;
                }
            });
        }
        private void pageLopper(List<SliderModel> sliderModelList){
            if(currentPage == sliderModelList.size() - 2){
                currentPage = 2;
                bannerSliderViewPage.setCurrentItem(currentPage,false);
            }
            if(currentPage == 1){
                currentPage = sliderModelList.size() - 3;
                bannerSliderViewPage.setCurrentItem(currentPage,false);
            }
        }
        private void startbannerSliderShow(final List<SliderModel> sliderModelList){
            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    if(currentPage >= sliderModelList.size()){
                        currentPage = 1;
                    }
                    bannerSliderViewPage.setCurrentItem(currentPage++,true);
                }
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            },DELAY_TIME,PERIOD_TIME);
        }
        private void stopBannerSliderShow(){
            timer.cancel();
        }
}
    public class StripAdBannerViewHolder extends RecyclerView.ViewHolder{
        private ImageView stripAdImage;
        private ConstraintLayout stripAdContainer;
        public StripAdBannerViewHolder(@NonNull View itemView) {
            super(itemView);
            stripAdImage = itemView.findViewById(R.id.strip_ad_image);
            stripAdContainer = itemView.findViewById(R.id.strip_ad_container);
        }
        private void setStripAd(String resource,String color){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.mipmap.placeholder_big)).into(stripAdImage);

            stripAdContainer.setBackgroundColor(Color.parseColor(color));
        }
    }
    public class HorizontalProductViewholder extends RecyclerView.ViewHolder{

        private ConstraintLayout contain;
        private TextView horizontalLayoutTitle;
        private Button horizontalLayoutViewAllbtn;
        private RecyclerView horizontalRecyclerView;

        public HorizontalProductViewholder(@NonNull View itemView) {
            super(itemView);
            contain = itemView.findViewById(R.id.container);
            horizontalLayoutTitle = itemView.findViewById(R.id.horizontal_scroll_layout_title);
            horizontalLayoutViewAllbtn = itemView.findViewById(R.id.horizontal_scroll_view_all);
            horizontalRecyclerView = itemView.findViewById(R.id.horizontal_scroll_layout_recyclerview);
            horizontalRecyclerView.setRecycledViewPool(recycledViewPool);
        }
        private void setHorizontalProductLayout(List<HorizontalProductScrollModel>horizontalProductScrollModelList, final String title, String color, final List<WishlistModel> viewAllProductList){
            contain.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            horizontalLayoutTitle.setText(title);

            if(horizontalProductScrollModelList.size()>8){
                horizontalLayoutViewAllbtn.setVisibility(View.VISIBLE);
                horizontalLayoutViewAllbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewAllActivity.wishlistModelList = viewAllProductList;
                        Intent viewAllIntent = new Intent(itemView.getContext(),ViewAllActivity.class);
                        viewAllIntent.putExtra("layout_code",0);
                        viewAllIntent.putExtra("title",title);
                        itemView.getContext().startActivity(viewAllIntent);
                    }
                });
            }else{
                horizontalLayoutViewAllbtn.setVisibility(View.INVISIBLE);
            }

            HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModelList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            horizontalRecyclerView.setLayoutManager(linearLayoutManager);
            horizontalRecyclerView.setAdapter(horizontalProductScrollAdapter);
            horizontalProductScrollAdapter.notifyDataSetChanged();
        }
    }
    public class gridProductViewHolder extends  RecyclerView.ViewHolder{

       private ConstraintLayout container;
       private TextView gridLayoutTitle;
       private Button gridLayoutViewAllBtn;
       private GridLayout gridProductLayout;

        public gridProductViewHolder(@NonNull View itemView) {
            super(itemView);
             container = itemView.findViewById(R.id.container);
             gridLayoutTitle = itemView.findViewById(R.id.grid_product_layout_title);
             gridLayoutViewAllBtn = itemView.findViewById(R.id.grid_product_layout_viewall_btn);
             gridProductLayout = itemView.findViewById(R.id.grid_layout);
        }
        private void setGridProductLayout(final List<HorizontalProductScrollModel> horizontalProductScrollModelList, final String title, String color){
            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            gridLayoutTitle.setText(title);


            for(int x=0; x<4;x++){
                ImageView productImage = gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_image);
                TextView productTitle = gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_title);
                TextView productDescription = gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_description);
                TextView productPrice = gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_price);

                Glide.with(itemView.getContext()).load(horizontalProductScrollModelList.get(x).getProductImage()).apply(new RequestOptions().placeholder(R.mipmap.placeholder_small)).into(productImage);
                productTitle.setText(horizontalProductScrollModelList.get(x).getProductTitle());
                productDescription.setText(horizontalProductScrollModelList.get(x).getProductDescription());
                productPrice.setText("Tk."+horizontalProductScrollModelList.get(x).getProductPrice()+"/-");
//                productPrice.setText(horizontalProductScrollModelList.get(x).getProductPrice());


                gridProductLayout.getChildAt(x).setBackgroundColor(Color.parseColor("#FFFFFF"));

                if(!title.equals(""))
                {
                    final int finalX = x;
                    gridProductLayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                            productDetailsIntent.putExtra("PRODUCT_ID",horizontalProductScrollModelList.get(finalX).getProductID());
                            itemView.getContext().startActivity(productDetailsIntent);
                        }
                    });
                }
            }

            if(!title.equals("")) {
                gridLayoutViewAllBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewAllActivity.horizontalProductScrollModelList = horizontalProductScrollModelList;
                        Intent viewAllIntent = new Intent(itemView.getContext(), ViewAllActivity.class);
                        viewAllIntent.putExtra("layout_code", 1);
                        viewAllIntent.putExtra("title", title);
                        itemView.getContext().startActivity(viewAllIntent);
                    }
                });
            }

        }
    }
}
