package com.alfaco_1.testno1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class InOutScreen extends AppCompatActivity {
  private ImageView login3,create3,img3;
  private  Animation pop,left,left_out,right,right_out;
  TextView txt,txt3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        left = AnimationUtils.loadAnimation(this,R.anim.slide_from_left);
        right = AnimationUtils.loadAnimation(this,R.anim.slide_from_right);
        left_out = AnimationUtils.loadAnimation(this,R.anim.slideout_from_left);
        right_out = AnimationUtils.loadAnimation(this,R.anim.slideout_from_right);
        pop = AnimationUtils.loadAnimation(this,R.anim.pop_up);
        setContentView(R.layout.activity_in_out_screen);
        login3 = (ImageView)findViewById(R.id.imageView4);
        create3 = (ImageView)findViewById(R.id.imageView5);
        img3 = (ImageView)findViewById(R.id.inout_img1);
        txt = (TextView)findViewById(R.id.textView3);
        txt3 = (TextView)findViewById(R.id.textView4);

        login3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginactivity = new Intent(InOutScreen.this,SignIn.class);
                startActivity(loginactivity);
                finish();
                overridePendingTransition(R.anim.slide_from_right,R.anim.slideout_from_left);

            }
        });
        create3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrationactivity = new Intent(InOutScreen.this,SignUpActivity.class);
                startActivity(registrationactivity);
                finish();
                overridePendingTransition(R.anim.slide_from_right,R.anim.slideout_from_left);
            }
        });
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InOutScreen.super.onBackPressed();
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