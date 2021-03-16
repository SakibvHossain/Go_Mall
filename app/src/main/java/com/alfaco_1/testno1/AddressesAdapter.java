package com.alfaco_1.testno1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.alfaco_1.testno1.DeliveryActivity.SELECT_ADDRESS;
import static com.alfaco_1.testno1.MyAccountFragment.MANAGE_ADDRESS;
import static com.alfaco_1.testno1.MyAdressesActivity.refreshItem;

class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.Viewholder> {

    private List<AdddressesModel> adddressesModelList;
    private int MODE;
    private int preSelectedPosition;

    public AddressesAdapter(List<AdddressesModel> adddressesModelList,int MODE) {
        this.adddressesModelList = adddressesModelList;
        this.MODE = MODE;
        preSelectedPosition = DBqueries.selectedAddress;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.addresses_item_layout,viewGroup,false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int position) {
    String name = adddressesModelList.get(position).getFullname();
    String address = adddressesModelList.get(position).getAddress();
    String pincode = adddressesModelList.get(position).getPincode();
    Boolean selected = adddressesModelList.get(position).getSelected();

    viewholder.setData(name,address,pincode,selected,position);
    }

    @Override
    public int getItemCount() {
        return adddressesModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView fullname;
        private TextView address;
        private TextView pincode;
        private ImageView icon;
        private LinearLayout optionContainer;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            fullname = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            pincode = itemView.findViewById(R.id.pincode);
            icon = itemView.findViewById(R.id.icon_view);
            optionContainer = itemView.findViewById(R.id.option_container);
        }

        private  void setData(String username, String userAddress, String userPincode, Boolean selected, final int position){
            fullname.setText(username);
            address.setText(userAddress);
            pincode.setText(userPincode);

            if(MODE == SELECT_ADDRESS){

             icon.setImageResource(R.mipmap.check);
             if(selected){
                 icon.setVisibility(View.VISIBLE);
                 preSelectedPosition = position;
             }else {
                 icon.setVisibility(View.GONE);
             }

             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if(preSelectedPosition!=position) {
                         adddressesModelList.get(position).setSelected(true);
                         adddressesModelList.get(preSelectedPosition).setSelected(false);
                         refreshItem(preSelectedPosition, position);
                         preSelectedPosition = position;
                         DBqueries.selectedAddress = position;
                     }
                 }
             });

            }else if(MODE == MANAGE_ADDRESS){
              optionContainer.setVisibility(View.GONE);
              icon.setImageResource(R.mipmap.menu_dot);
              icon.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      optionContainer.setVisibility(View.VISIBLE);
                      refreshItem(preSelectedPosition,preSelectedPosition);
                      preSelectedPosition = position;
                  }
              });
              itemView.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      refreshItem(preSelectedPosition,preSelectedPosition);
                      preSelectedPosition = -1;
                  }
              });
            }
        }
    }
}
