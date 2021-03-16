package com.alfaco_1.testno1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {
    private FirebaseAuth auth;

    private ImageView field_image, go_backbtn, signin_btn, closeBtn;
    private ProgressBar progressBar_signin;
    private TextInputLayout email_signin, password_signin;
    private TextView forgetpassword, createaccount, warningtxte, warningtxtp, txt5;
    public static boolean disableCloseBtn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in);
        //Image field
        field_image = (ImageView) findViewById(R.id.signin_field);
        go_backbtn = (ImageView) findViewById(R.id.signin_back);
        signin_btn = (ImageView) findViewById(R.id.signin_btn_img);
        closeBtn = (ImageView) findViewById(R.id.imageView6_mainview);
        //progress field
        progressBar_signin = (ProgressBar) findViewById(R.id.progressBar_signin);
        //TextInputLayout field
        email_signin = (TextInputLayout) findViewById(R.id.quantity_number);
        password_signin = (TextInputLayout) findViewById(R.id.sign_in_user_field_2);
        //TextView field
        warningtxte = (TextView) findViewById(R.id.signin_textView8);
        warningtxtp = (TextView) findViewById(R.id.signin_textView9);
        forgetpassword = (TextView) findViewById(R.id.signin_forget);
        createaccount = (TextView) findViewById(R.id.signin_create);
        txt5 = (TextView) findViewById(R.id.signin_textView5);
        //must create huck of firebase auth otherwise it won't work be careful
        auth = FirebaseAuth.getInstance();
        //forget password
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent user = new Intent(SignIn.this, GoMainActivity.class);
                startActivity(user);
                finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slideout_from_left);
            }
        });
        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forget = new Intent(SignIn.this, ForgetPassword.class);
                startActivity(forget);
                finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slideout_from_left);
            }
        });
        //create account section
        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(SignIn.this, SignUpActivity.class);
                startActivity(signup);
                finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slideout_from_left);

            }
        });
        //Go back btn
        go_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(SignIn.this, InOutScreen.class);
                startActivity(signup);
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slideout_from_right);

            }
        });
        //sign in section
        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email1 = email_signin.getEditText().getText().toString().trim();
                String pass1 = password_signin.getEditText().getText().toString().trim();
                if (!validateEmail1() | !validatePassword1()) {
                    return;
                }
                //main section

                progressBar_signin.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(email1, pass1).addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar_signin.setVisibility(View.INVISIBLE);
                            Intent signup = new Intent(SignIn.this, ConnectionFailed.class);
                            startActivity(signup);
                            finish();
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slideout_from_left);
                        } else {

                            ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

                            if (null != activeNetwork) {
                                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                                    progressBar_signin.setVisibility(View.INVISIBLE);
                                    Toast.makeText(SignIn.this, "User not found!", Toast.LENGTH_LONG).show();
                                }
                                if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                                    System.out.println("Data Network Enabled");
                                    progressBar_signin.setVisibility(View.INVISIBLE);
                                    Toast.makeText(SignIn.this, "User not found!", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(SignIn.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                progressBar_signin.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });
//            if(disableCloseBtn){
//              closeBtn.setVisibility(View.GONE);
//            }else {
//                closeBtn.setVisibility(View.VISIBLE);
//            }

            }
        });
    }

    private Boolean validateEmail1() {
        String email1 = email_signin.getEditText().getText().toString();

        if (email1.isEmpty()) {
            email_signin.setError(".");
            warningtxte.setVisibility(View.VISIBLE);
            return false;
        } else {
            warningtxte.setVisibility(View.INVISIBLE);
            email_signin.setError(null);
            email_signin.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword1() {
        String pass1 = password_signin.getEditText().getText().toString();
        if (pass1.isEmpty()) {
            password_signin.setError(".");
            warningtxtp.setVisibility(View.VISIBLE);
            return false;
        } else {
            warningtxtp.setVisibility(View.INVISIBLE);
            password_signin.setError(null);
            password_signin.setErrorEnabled(false);
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
                        SignIn.super.onBackPressed();
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