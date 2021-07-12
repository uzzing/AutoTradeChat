package com.project.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class GroupChatActivity extends AppCompatActivity {

    // ui
    private ImageButton sendMessageButton;
    private EditText messageInput;
    private ScrollView scrollView;
    private View displayMessages;

    // get user info
    private FirebaseAuth auth;
    private DatabaseReference UserRef, GroupNameRef, GroupMessageKeyRef;
    private String currentGroupName;
    private String currentUserID;

    // get date and time
    private String currentDate;
    private String currentTime;

    // show messages
    private final List<MessageItem> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private RecyclerView messageListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        // show groupName on alert
        currentGroupName = getIntent().getExtras().get("groupName").toString();
        Toast.makeText(this, currentGroupName, Toast.LENGTH_SHORT).show();

        // set user, group database
        auth = FirebaseAuth.getInstance();
        currentUserID = Arrays.stream(auth.getCurrentUser().getEmail().split("@")).findFirst().get();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        GroupNameRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName);

        initializeFields();

        getUserInfo();

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMessageInfoToDatabase();

                messageInput.setText("");
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        GroupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                messageAdapter = new MessageAdapter(messagesList, currentGroupName);
                MessageItem messages = snapshot.getValue(MessageItem.class);
                messagesList.add(messages);
                messageAdapter.notifyDataSetChanged();
                messageListView.setAdapter(messageAdapter); // declared twice
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    private void initializeFields() {

        // toolbar name
        getSupportActionBar().setTitle(currentGroupName);

        // show message list
        messageListView = (RecyclerView) findViewById(R.id.all_message_display);
        linearLayoutManager = new LinearLayoutManager(this);
        messageListView.setLayoutManager(linearLayoutManager);
        messageListView.setAdapter(messageAdapter);

        // the below
        sendMessageButton = (ImageButton) findViewById(R.id.group_chat_send);
        messageInput = (EditText) findViewById(R.id.input_message);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);

    }

    // for saveMessageInfoToDatabase()
    private void getUserInfo() {

        UserRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // didn't program
                if (snapshot.exists()) {
                    currentUserID = snapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // save new message data
    private void saveMessageInfoToDatabase() {

        String message = messageInput.getText().toString();
        String messageKey = GroupNameRef.push().getKey();

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "Please write message first", Toast.LENGTH_SHORT).show();
        } else {

            // get date and time
            Calendar calendar1 = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MM dd");
            currentDate = simpleDateFormat.format(calendar1.getTime());

            Calendar calendar2 = Calendar.getInstance();
            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = simpleTimeFormat.format(calendar2.getTime());


            // add new group info to firebase
            HashMap<String, Object> groupMessageKey = new HashMap<>();
            GroupNameRef.updateChildren(groupMessageKey); // add new message
            GroupMessageKeyRef = GroupNameRef.child(messageKey);


            // add new message info to new group info
            HashMap<String, Object> messageInfoMap = new HashMap<>();
            messageInfoMap.put("name", currentUserID); // getUserInfo()
            messageInfoMap.put("message", message);
            messageInfoMap.put("date", currentDate);
            messageInfoMap.put("time", currentTime);
            GroupMessageKeyRef.updateChildren(messageInfoMap);

        }
    }
}