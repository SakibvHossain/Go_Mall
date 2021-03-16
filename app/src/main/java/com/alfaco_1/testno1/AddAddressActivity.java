package com.alfaco_1.testno1;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {

    private Button saveBtn;

    private TextInputLayout city;
    private TextInputLayout locality;
    private TextInputLayout flat_no;
    private TextInputLayout pinCode;
    private TextInputLayout landmark;
    private TextInputLayout name;
    private TextInputLayout mobileNo;
    private TextInputLayout alternateMobileNo;

    private Spinner stateSpinner;
    private Dialog loadingDialog;

    private String seletedState;
    private String [] stateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add Your Info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /////loading dialog

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        ////loading dialog

        stateList = getResources().getStringArray(R.array.Bangladesh_states);
        city = findViewById(R.id.city);
        locality = findViewById(R.id.locality);
        flat_no = findViewById(R.id.flat_no);
        pinCode = findViewById(R.id.pincode);
        landmark = findViewById(R.id.landmark);
        name = findViewById(R.id.name);
        mobileNo = findViewById(R.id.mobile_no);
        alternateMobileNo = findViewById(R.id.alternate_mobile_no);

        stateSpinner = findViewById(R.id.state_spinner);

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,stateList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(spinnerAdapter);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             seletedState = stateList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(city.getEditText().getText())){
                    if(!TextUtils.isEmpty(locality.getEditText().getText())){
                        if(!TextUtils.isEmpty(flat_no.getEditText().getText())){
                            if(!TextUtils.isEmpty(pinCode.getEditText().getText()) && pinCode.getEditText().getText().length() == 6){
                                if(!TextUtils.isEmpty(name.getEditText().getText())){
                                    if(!TextUtils.isEmpty(mobileNo.getEditText().getText()) && mobileNo.getEditText().getText().length() == 11){
                                        loadingDialog.show();
                                        final String fullAddress = flat_no.getEditText().getText().toString()+" "+locality.getEditText().getText().toString()+" "+landmark.getEditText().getText().toString()+" "+ city.getEditText().getText().toString()+seletedState;

                                        Map<String,Object> addAddress = new HashMap<>();
                                        addAddress.put("list_size",(long)DBqueries.adddressesModelList.size() +1);
                                        if(TextUtils.isEmpty(alternateMobileNo.getEditText().getText())){
                                            addAddress.put("fullname_"+String.valueOf((long)DBqueries.adddressesModelList.size()+1),name.getEditText().getText().toString() + " - " + mobileNo.getEditText().getText().toString());
                                        }else{
                                            addAddress.put("fullname_"+String.valueOf((long)DBqueries.adddressesModelList.size()+1),name.getEditText().getText().toString() + " - " + mobileNo.getEditText().getText().toString()+" or "+alternateMobileNo.getEditText().getText().toString());
                                        }
                                        addAddress.put("address_"+String.valueOf((long)DBqueries.adddressesModelList.size()+1),fullAddress);
                                        addAddress.put("pincode_"+String.valueOf((long)DBqueries.adddressesModelList.size()+1),pinCode.getEditText().getText().toString());
                                        addAddress.put("selected_"+String.valueOf((long)DBqueries.adddressesModelList.size()+1),true);
                                        if(DBqueries.adddressesModelList.size() > 0 ) {
                                            addAddress.put("selected_" + (DBqueries.selectedAddress + 1), false);
                                        }

                                        FirebaseFirestore.getInstance().collection("USERS")
                                                .document(FirebaseAuth.getInstance().getUid())
                                                .collection("USER_DATA")
                                                .document("MY_ADDRESSES")
                                                .update(addAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful()){

                                                    if(DBqueries.adddressesModelList.size() > 0 ){
                                                        DBqueries.adddressesModelList.get(DBqueries.selectedAddress).setSelected(false);
                                                    }
                                                    if(TextUtils.isEmpty(alternateMobileNo.getEditText().getText())){
                                                        DBqueries.adddressesModelList.add(new AdddressesModel(name.getEditText().getText().toString() + " - " + mobileNo.getEditText().getText().toString(),fullAddress,pinCode.getEditText().getText().toString(),true));
                                                    }else{
                                                        DBqueries.adddressesModelList.add(new AdddressesModel(name.getEditText().getText().toString() + " - " + mobileNo.getEditText().getText().toString()+" or "+ alternateMobileNo.getEditText().getText().toString(),fullAddress,pinCode.getEditText().getText().toString(),true));
                                                    }
                                                    if(getIntent().getStringExtra("INTENT").equals("deliveryIntent")) {
                                                        Intent deliveryIntent = new Intent(AddAddressActivity.this, DeliveryActivity.class);
                                                        startActivity(deliveryIntent);
                                                    }else{
                                                        MyAdressesActivity.refreshItem(DBqueries.selectedAddress,DBqueries.adddressesModelList.size() -1);
                                                    }
                                                    DBqueries.selectedAddress = DBqueries.adddressesModelList.size() - 1;
                                                    finish();
                                                }else{
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(AddAddressActivity.this,error,Toast.LENGTH_SHORT).show();
                                                }
                                                loadingDialog.dismiss();
                                            }
                                        });
                                    }else {
                                        mobileNo.requestFocus();
                                        Toast.makeText(AddAddressActivity.this, "Please valid number", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    name.requestFocus();
                                }
                            }else {
                                pinCode.requestFocus();
                                Toast.makeText(AddAddressActivity.this, "Code does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            flat_no.requestFocus();
                        }
                    }else {
                        locality.requestFocus();
                    }
                }else {
                    city.requestFocus();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }
    private Boolean validatePhoneNo() {
        String val = mobileNo.getEditText().getText().toString();
        String phoneVal = "(^([+]{1}[8]{2}|0088)?(01){1}[3-9]{1}\\d{8})$"
                ;
        if (val.isEmpty()) {
            Toast.makeText(this,"Field can not be empty",Toast.LENGTH_SHORT).show();
            return false;
        }else if(!val.matches(phoneVal))
        {
            Toast.makeText(this,"Please enter valid number",Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }
}