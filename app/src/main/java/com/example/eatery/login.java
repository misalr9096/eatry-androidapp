package com.example.eatery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    EditText userName,password;
    Button login;
    TextView register,forgotPassword;
    FirebaseUser currentUser;//used to store current user of account
    FirebaseAuth mAuth;//Used for firebase authentication
    ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = (EditText) findViewById(R.id.emailid);
        password = (EditText) findViewById(R.id.passward);
        login = (Button) findViewById(R.id.login_login_xml);
        register = (TextView) findViewById(R.id.registerLink);
        forgotPassword = (TextView) findViewById(R.id.ForgetPassword);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        loadingBar = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllowUserToLogin();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToRegister();
            }
        });
        //if user forgets the password then to reset it
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPasswordUser();
            }
        });
    }

    private void resetPasswordUser() {
        String email = userName.getText().toString().trim();
        if(TextUtils.isEmpty(email))
        {
            Toast toast=new Toast(getApplicationContext());
            View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_toast,null);
            TextView textView=view.findViewById(R.id.textviewtoast);
            textView.setText("Please enter your email id");
            toast.setView(view);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }
        else
        {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast toast=new Toast(getApplicationContext());
                                View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_toast,null);
                                TextView textView=view.findViewById(R.id.textviewtoast);
                                textView.setText("Reset Email sent");
                                toast.setView(view);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.BOTTOM,32,32);
                                toast.show();
                            }
                        }
                    });
        }
    }

    private void sendUserToRegister() {
        //When user wants to create a new account send user to Register Activity
        Intent registerIntent = new Intent(login.this,register.class);
        startActivity(registerIntent);
    }

    private void AllowUserToLogin() {
        String email = userName.getText().toString().trim();
        String pwd = password.getText().toString();

        Bundle bundle = new Bundle();
        String myMessage = "Stackoverflow is cool!";
        bundle.putString("message", myMessage );
        if(TextUtils.isEmpty(email)&& TextUtils.isEmpty(pwd))
        {
            Toast toast=new Toast(getApplicationContext());
            View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_toast,null);
            TextView textView=view.findViewById(R.id.textviewtoast);
            textView.setText("Please fill all credential Carefully");
            toast.setView(view);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.show();
        }
        else {
            //When both email and password are available log in to the account
             try{
                 //Show the progress on Progress Dialog
                 loadingBar.setTitle("Log In");
                 loadingBar.setMessage("Please wait ,Because Good things always take time");
                 loadingBar.setCanceledOnTouchOutside(true);
                 loadingBar.show();
                 mAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())//If account login successful print message and send user to main Activity
                                {
                                    sendToHomeActivity();
                                    loadingBar.dismiss();
                                } else//Print the error message incase of failure
                                {       loadingBar.dismiss();

                                    Toast toast=new Toast(getApplicationContext());
                                    View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_toast,null);
                                    TextView textView=view.findViewById(R.id.textviewtoast);
                                    textView.setText("Please Check Internet Connection");
                                    toast.setView(view);
                                    toast.setDuration(Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.BOTTOM,0,0);
                                    toast.show();

                                }
                            }
                        });

            }
             catch (Exception e){
                 loadingBar.dismiss();
                 Toast toast=new Toast(getApplicationContext());
                 View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_toast,null);
                 TextView textView=view.findViewById(R.id.textviewtoast);
                 textView.setText("Please fill all credential Carefully");
                 toast.setView(view);
                 toast.setDuration(Toast.LENGTH_SHORT);
                 toast.show();
             }
        }
    }

    // somewhere

    //    protected void onStart() {
//        //Check if user has already signed in if yes send to mainActivity
//        //This to avoid signing in everytime you open the app.
//        super.onStart();
//        if(currentUser==null)
//        {
//            Toast.makeText(this, "User not Found", Toast.LENGTH_SHORT).show();
//
//        }
//    }

    private void sendToHomeActivity() {
        //This is to send user to MainActivity
        Intent  HomeIntent = new Intent(login.this,Home.class);
        startActivity(HomeIntent);

    }
}