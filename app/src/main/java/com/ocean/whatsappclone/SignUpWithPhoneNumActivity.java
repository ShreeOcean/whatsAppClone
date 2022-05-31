package com.ocean.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ocean.whatsappclone.databinding.ActivitySignUpPhoneNumBinding;

public class SignUpWithPhoneNumActivity extends AppCompatActivity {

    ActivitySignUpPhoneNumBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpPhoneNumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}