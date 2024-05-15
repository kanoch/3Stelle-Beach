package com.example.app3stelle.ui.login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.app3stelle.MainActivity;
import com.example.app3stelle.ui.MySharedData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginManager {
    private static final String TAG = "EmailPassword";
    private MySharedData sharedData = MySharedData.getInstance();
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private static LoginManager instance;
    private LoginManager() {
    }

    public static LoginManager getInstance() {
        if (instance == null) {
            instance = new LoginManager();
        }
        return instance;
    }

    public void onStart(Context cont) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            sharedData.setUserId(currentUser.getUid());
            sharedData.setUserMail(currentUser.getEmail());
            Toast.makeText(cont, "Login Effettuato",
                    Toast.LENGTH_SHORT).show();
        } else {
            gotoLogin(cont);
        }
    }

    public void createAccount(String email, String password, Context cont) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sharedData.setUserMail(email);
                            reload(cont);
                        } else {
                            Toast.makeText(cont, "L' Email è già registrata.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signIn(String email, String password,Context cont) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(cont, "Authentication successed",
                                    Toast.LENGTH_SHORT).show();
                            sharedData.setUserMail(email);
                            FirebaseUser user = mAuth.getCurrentUser();
                            sharedData.setUserId(user.getUid());
                            reload(cont);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(cont, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendEmailVerification() {
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Email sent
                    }
                });
    }

    private void reload(Context cont) {
        Intent intent = new Intent(cont, MainActivity.class);
        cont.startActivity(intent);
    }
    public void logOut(Context cont) {
        mAuth.signOut();
        sharedData.setUserId(null);

    }

    public void gotoLogin(Context cont) {
        Intent intent = new Intent(cont, LoginActivity.class);
        cont.startActivity(intent);
    }

}
