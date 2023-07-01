package com.example.firebasechatapp;

import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private ArrayList<User> usersArrayList;
    private onUserClickListener onUserClickListener;


    public interface onUserClickListener{
        void onUserClick(int position);
    }

    public UserAdapter(ArrayList<User> users){
        this.usersArrayList = users;
    }

    public void setonUserClickListener(onUserClickListener listener){
        this.onUserClickListener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
        UserViewHolder  userViewHolder = new UserViewHolder(view, onUserClickListener);

        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        User currentUser =  usersArrayList.get(position);
        holder.userNameTextView.setText(currentUser.getName());
        holder.avatarImageView.setImageResource(currentUser.getAvatarResource());
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }


    public class UserViewHolder extends RecyclerView.ViewHolder {
        public ImageView avatarImageView;
        public TextView userNameTextView;

        public UserViewHolder(@NonNull View itemView,onUserClickListener listener) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.userAvatarImageView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null){
                        Log.d("task exeption failed", "test2");
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onUserClick(position);
                        }
                    }
                }
            });
        }
    }
}
