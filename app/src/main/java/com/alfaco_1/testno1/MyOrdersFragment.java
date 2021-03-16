package com.alfaco_1.testno1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOrdersFragment extends Fragment  {

//    implements View.OnClickListener

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyOrdersFragment() {
        // Required empty public constructor
    }


    private RecyclerView myOrdersRecylerView;

    // TODO: Rename and change types and number of parameters
    public static MyOrdersFragment newInstance(String param1, String param2) {
        MyOrdersFragment fragment = new MyOrdersFragment();
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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        myOrdersRecylerView = view.findViewById(R.id.my_ordersrecyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        myOrdersRecylerView.setLayoutManager(layoutManager);

        List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();
        myOrderItemModelList.add(new MyOrderItemModel(R.mipmap.phone2,2,"Huawei P30","Delivered on Mon, 13th FEB 2020"));
        myOrderItemModelList.add(new MyOrderItemModel(R.mipmap.phone2,4,"Huawei P30","Delivered on Mon, 15th JAN 2020"));
        myOrderItemModelList.add(new MyOrderItemModel(R.mipmap.phone2,0,"Huawei P30","Cancelled"));
        myOrderItemModelList.add(new MyOrderItemModel(R.mipmap.phone2,2,"Huawei P30","Delivered on Mon, 15th JUN 2020"));

        MyOrderAdapter myOrderAdapter = new MyOrderAdapter(myOrderItemModelList);
        myOrdersRecylerView.setAdapter(myOrderAdapter);
        myOrderAdapter.notifyDataSetChanged();
        return view;
    }
//    @Override
//    public void onClick(View v) {
//        Intent intent = new Intent(getActivity(), GoMainActivity.class);
//        startActivity(intent);
//        ((Activity) getActivity()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//    }
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        ImageView newBlockButton = (ImageView) getActivity().findViewById(
//                R.id.back_from_order);//todo
//        newBlockButton.setOnClickListener((View.OnClickListener) this);
//    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
//    }
}