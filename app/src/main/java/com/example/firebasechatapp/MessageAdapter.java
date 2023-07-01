package com.example.firebasechatapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    private List<Message> messageList;
    private Activity activity;


    public MessageAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<Message> objects) {
        super(context, resource, objects);
        this.messageList = objects;
        this.activity = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater layoutInflater =  (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message message = getItem(position);
        int layoutResource = 0;
        int viewType = getItemViewType(position);
        if (viewType == 0){
            layoutResource = R.layout.my_message;
        }
        else {
            layoutResource = R.layout.your_message;
        }
        if (convertView!= null){
            viewHolder = (ViewHolder) convertView.getTag();
        }
        else {
            convertView = layoutInflater.inflate(layoutResource,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        boolean isText = message.getImageUrl() == null;
        if (isText){
            viewHolder.messageTextView.setVisibility(View.VISIBLE);
            viewHolder.photoImageView.setVisibility(View.GONE);
            viewHolder.messageTextView.setText(message.getText());
        }
        else {
            viewHolder.messageTextView.setVisibility(View.GONE);
            viewHolder.photoImageView.setVisibility(View.VISIBLE);
            viewHolder.messageTextView.setText(message.getText());
            Glide.with(viewHolder.photoImageView.getContext()).load(message.getImageUrl()).into(viewHolder.photoImageView);
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        if (message.isMyMessage()) return 0;
        else return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    private class ViewHolder {
        private TextView messageTextView;
        private ImageView photoImageView;

        public  ViewHolder(View view){
            photoImageView = view.findViewById(R.id.photoImageView);
            messageTextView = view.findViewById(R.id.bubbleTextView);
        }
    }
}
