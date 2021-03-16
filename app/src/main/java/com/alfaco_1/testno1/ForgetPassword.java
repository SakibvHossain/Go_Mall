package com.alfaco_1.testno1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    private TextInputLayout reset_email;
    private TextView go_back,error_txt1,error_txt2,txt3;
    private ProgressBar pro_bar;
    private ImageView reset_img1,error_img1,error_img2;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password);
        reset_email = (TextInputLayout)findViewById(R.id.reset_textInputLayout);
        go_back = (TextView)findViewById(R.id.reset_textView10);
        mAuth = FirebaseAuth.getInstance();
        pro_bar = (ProgressBar)findViewById(R.id.reset_progressBar);
        reset_img1 = (ImageView)findViewById(R.id.reset_imageView14);
        error_txt1 =(TextView)findViewById(R.id.reset_textView9);
        error_img1 = (ImageView)findViewById(R.id.imageView15);
        txt3 = (TextView)findViewById(R.id.textView20);
        error_img2 = (ImageView)findViewById(R.id.imageView16); //red
        error_txt2 = (TextView)findViewById(R.id.textView21); //red
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(ForgetPassword.this,SignIn.class);
                startActivity(login);
                finish();
                overridePendingTransition(R.anim.slide_from_left,R.anim.slideout_from_right);
            }
        });



        reset_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_img2.setVisibility(View.INVISIBLE);
                error_txt2.setVisibility(View.INVISIBLE);
                error_img1.setVisibility(View.INVISIBLE);
                txt3.setVisibility(View.INVISIBLE);
                String email1 = reset_email.getEditText().getText().toString().trim();
                if(!validateEmail())
                {
                    return;
                }
                pro_bar.setVisibility(View.VISIBLE);
                mAuth.sendPasswordResetEmail(email1)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    error_img1.setVisibility(View.VISIBLE);
                                    txt3.setVisibility(View.VISIBLE);
                                    pro_bar.setVisibility(View.INVISIBLE);

                                }else{
                                    ConnectivityManager manager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                                    NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
                                    if(null!= activeNetwork){
                                        if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                                            error_img2.setVisibility(View.VISIBLE);
                                            error_txt2.setVisibility(View.VISIBLE);
                                            pro_bar.setVisibility(View.INVISIBLE);
                                        }
                                        if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                                            error_img2.setVisibility(View.VISIBLE);
                                            error_txt2.setVisibility(View.VISIBLE);
                                            pro_bar.setVisibility(View.INVISIBLE);                                        }
                                    }
                                    else{
                                        Toast.makeText(ForgetPassword.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                                        pro_bar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }
                        });

            }
        });

    }

    private Boolean validateEmail() {
        String email1 = reset_email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email1.isEmpty()) {
            reset_email.setError(".");
            error_txt1.setVisibility(View.VISIBLE);
            return false;
        } else if (!email1.matches(emailPattern)) {
            reset_email.setError(".");
            Toast.makeText(this,"Invalid email address",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            reset_email.setError(null);
            reset_email.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to leave this page?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ForgetPassword.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}







