package com.ocean.whatsappclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.ocean.whatsappclone.databinding.SampleReceiverChatBubbleBinding;
import com.ocean.whatsappclone.databinding.SampleSenderChatBubbleBinding;
import com.ocean.whatsappclone.model.MessagesModel;

import java.util.ArrayList;

public class ChatRoomAdaptor extends RecyclerView.Adapter{

    ArrayList<MessagesModel> msgList;
    Context context;

    int SENDER_VIEW_TYPE = 1;
    int RECEVIER_VIEW_TYPE = 2;

    public ChatRoomAdaptor(ArrayList<MessagesModel> msgList, Context context) {
        this.msgList = msgList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == SENDER_VIEW_TYPE) {

            SampleSenderChatBubbleBinding senderBinding = SampleSenderChatBubbleBinding.inflate(LayoutInflater.from(context),parent,false);
            return new SenderViewHolder(senderBinding);
        }else {

            SampleReceiverChatBubbleBinding receiverBinding = SampleReceiverChatBubbleBinding.inflate(LayoutInflater.from(context),parent,false);
            return  new ReceiverViewHolder(receiverBinding);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (msgList.get(position).getUserId().equals(FirebaseAuth.getInstance().getUid())){

            return SENDER_VIEW_TYPE; //TODO: This way since it is easy to recognise sender at first instead of receiver
        }else {

            return RECEVIER_VIEW_TYPE;
        }
        //return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessagesModel msgModel = msgList.get(position);

        if (holder.getClass() == SenderViewHolder.class){
            ((SenderViewHolder) holder).senderBinding.tvSenderMsg.setText(msgModel.getMessage());
            //TODO: sender's timestamp set
        }else {
            ((ReceiverViewHolder) holder).receiverBinding.tvReceiverMsg.setText(msgModel.getMessage());
            //TODO: receiver's timestamp set
        }

    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        SampleReceiverChatBubbleBinding receiverBinding;

        public ReceiverViewHolder(SampleReceiverChatBubbleBinding itemView) {
            super(itemView.getRoot());
            receiverBinding = itemView;
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {

        SampleSenderChatBubbleBinding senderBinding;

        public SenderViewHolder(SampleSenderChatBubbleBinding itemView) {
            super(itemView.getRoot());
            senderBinding = itemView;
        }
    }
}
