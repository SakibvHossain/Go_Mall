package com.alfaco_1.testno1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 4000;
private ImageView logo;
private TextView txt;
private Animation top,bottom;
    Animation pop,left,left_out,right,right_out;
private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        top = AnimationUtils.loadAnimation(this,R.anim.top_anim);
        bottom = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);
        logo = (ImageView)findViewById(R.id.imageView2);
        txt = (TextView)findViewById(R.id.textView);
        auth = FirebaseAuth.getInstance();
                pop = AnimationUtils.loadAnimation(this,R.anim.pop_up);
         logo.startAnimation(pop);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = auth.getCurrentUser();
                if(user == null)
                {
                    Intent login = new Intent(SplashScreen.this,InOutScreen.class);
//                    Pair[] pairs = new Pair[1];
//                    pairs[0] = new Pair<View,String>(txt,"txt_transition");
//                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this,pairs);
//                    startActivity(login,options.toBundle());
                    startActivity(login);
                    finish();
                    overridePendingTransition(R.anim.slide_from_right,R.anim.slideout_from_left);
                }else {
                    Intent main = new Intent(SplashScreen.this,GoMainActivity.class);
                    startActivity(main);
                    finish();
                    overridePendingTransition(R.anim.slide_from_right,R.anim.slideout_from_left);
                }
            }
        },SPLASH_TIME_OUT);
    }
}