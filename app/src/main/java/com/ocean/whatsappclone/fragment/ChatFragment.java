package com.ocean.whatsappclone.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ocean.whatsappclone.R;
import com.ocean.whatsappclone.adapter.ChatFragRecyclerAdapter;
import com.ocean.whatsappclone.databinding.FragmentChatBinding;
import com.ocean.whatsappclone.model.UserModel;

import java.util.ArrayList;

public class ChatFragment extends Fragment {


    public ChatFragment(){}//required empty constructor

    FragmentChatBinding chatBinding;
    ArrayList<UserModel> list = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        chatBinding = FragmentChatBinding.inflate(inflater, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ChatFragRecyclerAdapter adapter = new ChatFragRecyclerAdapter(list, getContext());
        chatBinding.recyclerViewFragmentChat.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        chatBinding.recyclerViewFragmentChat.setLayoutManager(layoutManager);

        firebaseDatabase.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    progressDialog.dismiss();
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    userModel.getUserID(dataSnapshot.getKey());
                    list.add(userModel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return chatBinding.getRoot();
    }
}