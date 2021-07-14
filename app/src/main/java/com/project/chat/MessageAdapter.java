package com.project.chat;

import android.app.AlertDialog;
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
import com.squareup.picasso.Picasso;


import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.zip.Inflater;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<MessageItem> messagesList;
    private String currentGroupName;

    // get chat data
    private FirebaseAuth auth;

    public MessageAdapter(List<MessageItem> messagesList, String currentGroupName) {
        this.messagesList = messagesList;
        this.currentGroupName = currentGroupName;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        // my message
        public TextView myMessage, myMessageTime;
        public ImageView myMessageImage, myMessageTail;

        // other message
        public TextView otherName, otherMessage, otherMessageTime;
        public ImageView otherProfileImage, otherMessageTail;
        public ImageView otherMessageImage;

        final ImageView expandedImageView;

        public MessageViewHolder(@NonNull @NotNull View itemView) {

            super(itemView);

            // my message
            myMessage = (TextView) itemView.findViewById(R.id.my_message);
            myMessageTail = (ImageView) itemView.findViewById(R.id.my_message_tail);
            myMessageTime = (TextView) itemView.findViewById(R.id.my_message_time);
            myMessageImage = (ImageView) itemView.findViewById(R.id.my_message_image);

            // other message
            otherName = (TextView) itemView.findViewById(R.id.other_name);
            otherMessage = (TextView) itemView.findViewById(R.id.other_message);
            otherMessageTail = (ImageView) itemView.findViewById(R.id.other_message_tail);
            otherMessageTime = (TextView) itemView.findViewById(R.id.other_message_time);
            otherProfileImage = (ImageView) itemView.findViewById(R.id.other_profile);
            otherMessageImage = (ImageView) itemView.findViewById(R.id.other_message_image);

            expandedImageView = (ImageView) itemView.findViewById(
                    R.id.my_message_image_expanded);
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
        String type = messages.getType();

        if (type.equals("text")) {
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
        else if (type.equals("image")) {

            // if sender is me
            if (name.equals(MyData.name)) {
                messageViewHolder.myMessage.setVisibility(View.INVISIBLE);
                messageViewHolder.myMessageTail.setVisibility(View.INVISIBLE);
                messageViewHolder.myMessageTime.setText(messageTime);
                messageViewHolder.myMessageImage.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getMessage()).resize(600, 0).into(messageViewHolder.myMessageImage);
            }
            else { // if sender is other
                messageViewHolder.otherName.setText(name);
                messageViewHolder.otherMessage.setVisibility(View.INVISIBLE);
                messageViewHolder.otherMessageTail.setVisibility(View.INVISIBLE);
                messageViewHolder.otherMessageTime.setText(messageTime);
                messageViewHolder.otherProfileImage.setVisibility(View.VISIBLE);
                messageViewHolder.otherMessageImage.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getMessage()).resize(600, 0).into(messageViewHolder.otherMessageImage);
            }
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