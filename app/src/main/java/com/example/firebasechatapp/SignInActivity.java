package com.example.firebasechatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    TextInputEditText  emailInputEditText;
    TextInputEditText  passwordInputEditText;
    TextInputEditText  nameInputEditText;
    boolean loginModeActive = false;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userDatabaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailInputEditText = findViewById(R.id.emailTextInput);
        passwordInputEditText = findViewById(R.id.password_TextInput);
        nameInputEditText = findViewById(R.id.nameTextInput);

        Button signInButton  = findViewById(R.id.signInButtton);
        TextView logInTextView  = findViewById(R.id.logInTextView);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userDatabaseReference = firebaseDatabase.getReference().child("users");

        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(SignInActivity.this, UserListActivity.class));
        }

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginModeActive){
                    login();
                }
                else {
                    signIn();
                }
            }
        });

        logInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginModeActive =!loginModeActive;
                if (loginModeActive){
                    logInTextView.setText("or Sign In");
                    signInButton.setText("Log in");
                    nameInputEditText.setVisibility(View.GONE);
                }
                else {
                    logInTextView.setText("or Log In");
                    signInButton.setText("Sing in");
                    nameInputEditText.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void login() {
        if (!isValidEmail()){
            Toast.makeText(SignInActivity.this,"Email is not correct",Toast.LENGTH_LONG).show();
            return;
        }
        if (!isValidPassword()){
            Toast.makeText(SignInActivity.this,"Password is not correct",Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(emailInputEditText.getText().toString(),passwordInputEditText.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(new Intent(SignInActivity.this, UserListActivity.class));
                            intent.putExtra("userName",nameInputEditText.getText().toString());
                            Toast.makeText(SignInActivity.this,"Sign in is successful",Toast.LENGTH_LONG).show();
                            startActivity(intent);                        }
                        else {
                            Toast.makeText(SignInActivity.this,"Sign in is not successful",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void signIn() {
        if (!isValidEmail()){
            Toast.makeText(SignInActivity.this,"Email is not correct",Toast.LENGTH_LONG).show();
            return;
        }
        if (!isValidPassword()){
            Toast.makeText(SignInActivity.this,"Password is not correct",Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(emailInputEditText.getText().toString().trim(), passwordInputEditText.getText().toString().trim())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()){
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        createUSer(firebaseUser);
                        Intent intent = new Intent(new Intent(SignInActivity.this, UserListActivity.class));
                        intent.putExtra("userName",nameInputEditText.getText().toString());
                        startActivity(intent);
                        Toast.makeText(SignInActivity.this,"Sign in is successful",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(SignInActivity.this,"Sign in is not successful",Toast.LENGTH_LONG).show();

                    }
                });

    }

    private void createUSer(FirebaseUser firebaseUser) {
        User user = new User(firebaseUser.getUid(),nameInputEditText.getText().toString().trim(),firebaseUser.getEmail());
        userDatabaseReference.push().setValue(user);


    }

    private boolean isValidPassword(){
        Matcher matcher = Pattern.compile("((?=.*[a-z])(?=.*\\d).{7,20})").matcher(passwordInputEditText.getText().toString().trim());
        return matcher.matches();
    }

    public boolean isValidEmail( ) {
        return (!TextUtils.isEmpty(emailInputEditText.getText().toString().trim()) && Patterns.EMAIL_ADDRESS.matcher(emailInputEditText.getText().toString().trim()).matches());
    }



}