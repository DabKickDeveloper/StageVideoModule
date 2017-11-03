package com.dabkick.videosdk.livesession.chat;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dabkick.videosdk.R;

import java.util.ArrayList;

public class ChatAdapter extends ArrayAdapter<ChatMessage> {

    private Context context;
    private ArrayList<ChatMessage> messageList;

    public ChatAdapter(Context context, ArrayList<ChatMessage> messageList) {
        super(context, R.layout.chat_message, messageList);
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.chat_message, parent, false);
        }

        ChatMessage item = messageList.get(position);

        TextView authorTv = convertView.findViewById(R.id.chat_message_author);
        TextView messageTv = convertView.findViewById(R.id.chat_message_message);

        authorTv.setText(item.getSenderUserId());
        messageTv.setText(item.getMessage());

        return convertView;
    }
}
