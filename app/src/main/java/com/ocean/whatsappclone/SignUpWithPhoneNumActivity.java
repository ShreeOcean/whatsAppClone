package com.ocean.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.ocean.whatsappclone.databinding.ActivitySignUpPhoneNumBinding;

public class SignUpWithPhoneNumActivity extends AppCompatActivity {

    ActivitySignUpPhoneNumBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpPhoneNumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}