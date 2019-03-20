package com.shid.chat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shid.chat.MessageActivity;
import com.shid.chat.Model.Chat;
import com.shid.chat.Model.User;
import com.shid.chat.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context mContext;
    private List<Chat> mChat;
    private String imageUrl;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context mContext, List<Chat> mChat,String imageUrl) {
        this.mContext = mContext;
        this.mChat = mChat;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,viewGroup,false);
            return new MessageAdapter.ViewHolder(view);
        } else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,viewGroup,false);
            return new MessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int position) {
      Chat chat = mChat.get(position);

      viewHolder.show_message.setText(chat.getMessage());

      if (imageUrl.equals("default")){
          viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher_round);
      } else {
          Glide.with(mContext).load(imageUrl).into(viewHolder.profile_image);
      }

      if (position == mChat.size()-1){
          if (chat.isIsseen()){
              viewHolder.txt_seen.setText("Seen");
          } else{
              viewHolder.txt_seen.setText("Delivered");
          }
      } else{
          viewHolder.txt_seen.setVisibility(View.GONE);
      }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image;
        public TextView txt_seen;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}

