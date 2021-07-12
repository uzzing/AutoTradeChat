package com.project.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<MessageItem> messagesList;
    private String currentGroupName;

    // get chat data
    private FirebaseAuth auth;
    private DatabaseReference MessageRef;

    public MessageAdapter(List<MessageItem> messagesList, String currentGroupName) {
        this.messagesList = messagesList;
        this.currentGroupName = currentGroupName;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        // my message
        public TextView myName, myMessage, myMessageTime;
        // other message
        public TextView otherName, otherMessage, otherMessageTime;
        public ImageView otherProfileImage;
        public MessageViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            // my message
            myMessage = (TextView) itemView.findViewById(R.id.my_message);
            myMessageTime = (TextView) itemView.findViewById(R.id.my_message_time);
            // other message
            otherName = (TextView) itemView.findViewById(R.id.other_name);
            otherMessage = (TextView) itemView.findViewById(R.id.other_message);
            otherMessageTime = (TextView) itemView.findViewById(R.id.other_message_time);
            otherProfileImage = (ImageView) itemView.findViewById(R.id.other_profile);
        }
    }

    // extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>
    @NonNull
    @NotNull
    @Override
    // connect with xml.file
    public MessageViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {

        View view = null;

        // whether sender is me or other
        if (viewType == 1)
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.chat_my_message, viewGroup, false);
        else
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.chat_other_message, viewGroup, false);

        auth = FirebaseAuth.getInstance();

        return new MessageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageAdapter.MessageViewHolder messageViewHolder, int position) {

        MessageItem messages = messagesList.get(position);
        String name = messages.getName();
        String messageTime = messages.getTime();

        // if sender is me
        if (name.equals(MyData.name)) {
            messageViewHolder.myMessage.setText(messages.getMessage());
            messageViewHolder.myMessageTime.setText(messageTime);
        }
        else { // if sender is other
            messageViewHolder.otherName.setText(name);
            messageViewHolder.otherMessage.setText(messages.getMessage());
            messageViewHolder.otherMessageTime.setText(messageTime);
            messageViewHolder.otherProfileImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messagesList.get(position).getName().equals(MyData.name))
            return 1;
        else
            return 2;
    }

    @Override
    public int getItemCount() {
        // how many messages
        return messagesList.size();
    }
}


// String messageType = messages.getType();

// if type is text
//        if (messageType.equals("text")) {
