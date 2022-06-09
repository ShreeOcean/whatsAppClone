package com.ocean.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ocean.whatsappclone.adapter.ChatRoomAdaptor;
import com.ocean.whatsappclone.databinding.ActivityChatRoomBinding;
import com.ocean.whatsappclone.model.MessagesModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityChatRoomBinding binding;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    private String senderID;
    private String senderRoom, receiverRoom;
    private String receiverID;
    ShareActionProvider shareActionProvider;
    String TAG = "ChatRoomActivity : --";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        final ArrayList<MessagesModel> msgModel = new ArrayList<>();
        final ChatRoomAdaptor chatAdaptor = new ChatRoomAdaptor(msgModel, this);
        binding.recyclerViewChatroom.setAdapter(chatAdaptor);//setting recycler view adaptor

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewChatroom.setLayoutManager(layoutManager);//setting linear layout manager

        senderID = firebaseAuth.getUid();//getting id from firebase auth
        receiverID = getIntent().getStringExtra("userID");//getting id from recycler adapter
        String receiverUserName = getIntent().getStringExtra("userName");
        String receiverDP = getIntent().getStringExtra("profileDP");

        Log.d(TAG, "onCreate: " + receiverID + receiverDP + receiverRoom);
        Log.d(TAG, "onCreate: " + senderID + senderRoom);

        senderRoom = senderID + receiverID;
        receiverRoom = receiverID + senderID;

//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Please wait...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();

        binding.tvReceiverNameToolbar.setText(receiverUserName);
        Glide.with(this).load(receiverDP).placeholder(R.drawable.ic_person_white).into(binding.receiverDpToolBar);

        binding.imageViewGoBack.setOnClickListener(view -> {
            startActivity(new Intent(ChatRoomActivity.this, MainActivity.class));
        });

        binding.ivSendBtn.setOnClickListener(this);
        binding.ivCallBtn.setOnClickListener(this);
        binding.ivVideoCallBtn.setOnClickListener(this);

        //TODO: updating recycler view of chat room
        firebaseDatabase.getReference().child("Chats").child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        msgModel.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            MessagesModel model = dataSnapshot.getValue(MessagesModel.class);
                            msgModel.add(model);
                        }
                        chatAdaptor.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_send_btn:

                    String msg = binding.etMsgToSendChatRoom.getText().toString();
                    final MessagesModel msgModel = new MessagesModel(senderID, msg);
                    msgModel.setTimestamp(new Date().getTime());
                    binding.etMsgToSendChatRoom.setText("");

                    firebaseDatabase.getReference().child("Chats")
                            .child(senderRoom)
                            .push()
                            .setValue(msgModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    firebaseDatabase.getReference().child("Chats")
                                            .child(receiverRoom)
                                            .push()
                                            .setValue(msgModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });
                                }
                            });

                break;
            case R.id.iv_call_btn:

                Toast.makeText(this, "Is to be code...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_video_call_btn:

                Toast.makeText(this, "This feature is to be code...", Toast.LENGTH_SHORT).show();
                break;
        }

    }

}