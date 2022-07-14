package com.example.eatery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class register extends AppCompatActivity {
    EditText userName, password;
    TextView AccountExists;
    Button register;
    FirebaseUser currentUser;
    private FirebaseAuth mAuth;//Used for firebase authentication
    private ProgressDialog loadingBar;//Used to show the progress of the registration process

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        userName = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.passwd);
        register = (Button) findViewById(R.id.btnregister);
        AccountExists = (TextView) findViewById(R.id.Already_link);
        loadingBar = new ProgressDialog(this);

        currentUser = mAuth.getCurrentUser();

        //When user has  an account already he should be sent to login activity.
        AccountExists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToLoginActivity();
            }
        });
        //When user clicks on register create a new account for user
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAccount();
            }
        });
    }

        private void createNewAccount() {
        String email = userName.getText().toString().trim();
        String pwd = password.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            Toast toast=new Toast(getApplicationContext());
            View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_toast,null);
            TextView textView=view.findViewById(R.id.textviewtoast);
            textView.setText("Please enter email id");
            toast.setView(view);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();

        }
        if(TextUtils.isEmpty(pwd))
        {
            Toast toast=new Toast(getApplicationContext());
            View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_toast,null);
            TextView textView=view.findViewById(R.id.textviewtoast);
            textView.setText("Please enter password");
            toast.setView(view);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }
        else
        {
            //When both email and password are available create a new account
            //Show the progress on Progress Dialog
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait, we are creating new Account");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(email,pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())//If account creation successful print message and send user to Login Activity
                            {
                                sendUserToLoginActivity();
                                Toast toast=new Toast(getApplicationContext());
                                View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_toast,null);
                                TextView textView=view.findViewById(R.id.textviewtoast);
                                textView.setText("Account created successfully");
                                toast.setView(view);
                                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.show();
                                loadingBar.dismiss();
                            }
                            else//Print the error message incase of failure
                            {
                                Toast toast=new Toast(getApplicationContext());
                                View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_toast,null);
                                TextView textView=view.findViewById(R.id.textviewtoast);
                                textView.setText("User Already Found");
                                toast.setView(view);
                                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.show();
                                loadingBar.dismiss();
                            }
                        }
                    });

        }
    }

        private void sendUserToLoginActivity() {
        //This is to send user to Login Activity.
        Intent loginIntent = new Intent(register.this,login.class);
        startActivity(loginIntent);
    }
}