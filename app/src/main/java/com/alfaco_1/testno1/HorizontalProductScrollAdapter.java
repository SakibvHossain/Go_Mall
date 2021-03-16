package com.alfaco_1.testno1;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

class HorizontalProductScrollAdapter extends RecyclerView.Adapter<HorizontalProductScrollAdapter.ViewHolder> {

    private List<HorizontalProductScrollModel> horizontalProductScrollModels;

    public HorizontalProductScrollAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModels) {
        this.horizontalProductScrollModels = horizontalProductScrollModels;
    }

    @NonNull
    @Override
    public HorizontalProductScrollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_item_layout,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalProductScrollAdapter.ViewHolder viewHolder, int position) {
    String resource = horizontalProductScrollModels.get(position).getProductImage();
    String title = horizontalProductScrollModels.get(position).getProductTitle();
    String description = horizontalProductScrollModels.get(position).getProductDescription();
    String price = horizontalProductScrollModels.get(position).getProductPrice();
   String productID = horizontalProductScrollModels.get(position).getProductID();
    viewHolder.setData(productID,resource,title,description,price);
//  viewHolder.setProductImage(resource,title,description,price);
////  viewHolder.setProductTile(title);
////  viewHolder.setProductDescription(description);
////  viewHolder.setProductPrice(price);

    }

    @Override
    public int getItemCount() {
        if(horizontalProductScrollModels.size() > 8)
        {
            return 8;
        }else {
            return horizontalProductScrollModels.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productTitle;
        private TextView productDescription;
        private TextView productPrice;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.h_s_product_image);
            productTitle = itemView.findViewById(R.id.h_s_product_title);
            productDescription = itemView.findViewById(R.id.h_s_product_description);
            productPrice = itemView.findViewById(R.id.h_s_product_price);

        }
        private void setData(final String productID, String resource, String title, String description, String price){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.mipmap.placeholder_small)).into(productImage);
            productTitle.setText(title);
            productDescription.setText(description);
            productPrice.setText("Tk."+price+"/-");

            if(!title.equals(""))
            {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                        productDetailsIntent.putExtra("PRODUCT_ID",productID);
                        itemView.getContext().startActivity(productDetailsIntent);
                    }
                });
            }
        }
//        private void setProductTile(){
//        }
//        private void setProductDescription(){
//        }
//        private void setProductPrice(){
//        }

    }
}
