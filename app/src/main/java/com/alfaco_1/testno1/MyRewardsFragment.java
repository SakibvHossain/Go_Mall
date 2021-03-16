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
 * Use the {@link MyRewardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRewardsFragment extends Fragment {

//    implements View.OnClickListener

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyRewardsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyRewardsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyRewardsFragment newInstance(String param1, String param2) {
        MyRewardsFragment fragment = new MyRewardsFragment();
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

    private RecyclerView rewardsRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_rewards, container, false);
     rewardsRecyclerView = view.findViewById(R.id.my_rewards_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
       rewardsRecyclerView.setLayoutManager(layoutManager);

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

        MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(rewardModelList,false);
        rewardsRecyclerView.setAdapter(myRewardsAdapter);
        myRewardsAdapter.notifyDataSetChanged();

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
//                R.id.back_from_rewards);//todo
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