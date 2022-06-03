package com.ocean.whatsappclone.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ocean.whatsappclone.ChatRoomActivity;
import com.ocean.whatsappclone.R;
import com.ocean.whatsappclone.databinding.CustomRecyclerviewChatLayoutBinding;
import com.ocean.whatsappclone.model.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatFragRecyclerAdapter extends RecyclerView.Adapter<ChatFragRecyclerAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<UserModel> list; //= new ArrayList<>()

    public ChatFragRecyclerAdapter(ArrayList<UserModel> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ChatFragRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CustomRecyclerviewChatLayoutBinding binding = CustomRecyclerviewChatLayoutBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatFragRecyclerAdapter.MyViewHolder holder, int position) {

        UserModel userModel = list.get(position);

        Picasso.with(context).load(userModel.getProfilePic()).placeholder(R.drawable.ic_baseline_person_24).into(holder.binding.profileImage);

        holder.binding.tvUserName.setText(userModel.getUserName());

        //TODO: last message code to be done after chat room code

        holder.binding.usersRecyclerItem.setOnClickListener(view -> {
            Intent intent = new Intent(context, ChatRoomActivity.class);
            intent.putExtra("userID", userModel.getUserID());
            intent.putExtra("profileDP", userModel.getProfilePic());
            intent.putExtra("userName", userModel.getUserName());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {

            return list.size();

//        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CustomRecyclerviewChatLayoutBinding binding;

        public MyViewHolder(CustomRecyclerviewChatLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
