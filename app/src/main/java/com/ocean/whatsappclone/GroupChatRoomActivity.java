package com.ocean.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ocean.whatsappclone.adapter.ChatRoomAdaptor;
import com.ocean.whatsappclone.databinding.ActivityGroupChatRoomBinding;
import com.ocean.whatsappclone.model.MessagesModel;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatRoomActivity extends AppCompatActivity {

    private static final String TAG = "GroupChatRoomActivity";
    ActivityGroupChatRoomBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.imageViewGoBack.setOnClickListener(view -> {
            startActivity(new Intent(GroupChatRoomActivity.this, MainActivity.class));
        });

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final ArrayList<MessagesModel> list = new ArrayList<>();
        final String senderId = FirebaseAuth.getInstance().getUid();
        final String senderName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        final ChatRoomAdaptor chatRoomAdaptor = new ChatRoomAdaptor(list, this);
        binding.recyclerViewChatroom.setAdapter(chatRoomAdaptor);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewChatroom.setLayoutManager(layoutManager);

        //TODO: getting msg event that is onDataChange method
        firebaseDatabase.getReference().child("GroupChat")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            MessagesModel mModel = dataSnapshot.getValue(MessagesModel.class);
                            list.add(mModel);
                        }
                        chatRoomAdaptor.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Log.d(TAG, "onCancelled: ----" + error);
                    }
                });

        //TODO: send msg btn event
        binding.ivSendBtn.setOnClickListener(view -> {

            final String msg = binding.etMsgToSendChatRoom.getText().toString();
            final MessagesModel msgModel = new MessagesModel(senderId, senderName+" :  "+ '\n' + msg);
            msgModel.setTimestamp(new Date().getTime());

            binding.etMsgToSendChatRoom.setText("");
            firebaseDatabase.getReference().child("GroupChat")
                    .push()
                    .setValue(msgModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
        });
    }
}