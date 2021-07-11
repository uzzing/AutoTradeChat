package com.project.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        // show groupName on alert
        currentGroupName = getIntent().getExtras().get("groupName").toString();
        Toast.makeText(this, currentGroupName, Toast.LENGTH_SHORT).show();

        // get user, group
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
                if (snapshot.exists()) {
                    DisplayMessages(snapshot);
                }
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

        getSupportActionBar().setTitle(currentGroupName); // toolbar

        sendMessageButton = (ImageButton) findViewById(R.id.group_chat_send);
        messageInput = (EditText) findViewById(R.id.input_message);
        displayMessages = (TextView) findViewById(R.id.group_chat_text_display);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
    }

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

    private void saveMessageInfoToDatabase() {

        String message = messageInput.getText().toString();
        String messageKey = GroupNameRef.push().getKey();

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "Please write message first", Toast.LENGTH_SHORT).show();
        }
        else {

            // get date and time
            Calendar calendar1 = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MM dd");
            currentDate = simpleDateFormat.format(calendar1.getTime());

            Calendar calendar2 = Calendar.getInstance();
            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = simpleTimeFormat.format(calendar2.getTime());

            // update group db
            HashMap<String, Object> groupMessageKey = new HashMap<>();
            GroupNameRef.updateChildren(groupMessageKey);
            GroupMessageKeyRef = GroupNameRef.child(messageKey);

            HashMap<String, Object> messageInfoMap = new HashMap<>();
            messageInfoMap.put("name", currentUserID);
            messageInfoMap.put("message", message);
            messageInfoMap.put("date", currentDate);
            messageInfoMap.put("time", currentTime);

            GroupMessageKeyRef.updateChildren(messageInfoMap);
        }
    }

    private void DisplayMessages(DataSnapshot snapshot) {
        Iterator iterator = snapshot.getChildren().iterator();

        while (iterator.hasNext()) {
            String chatDate = (String) ( (DataSnapshot) iterator.next() ).getValue();
            String chatMessage = (String) ( (DataSnapshot) iterator.next()).getValue();
            String chatName = (String) ( (DataSnapshot) iterator.next()).getValue();
            String chatTime = (String) ( (DataSnapshot) iterator.next()).getValue();

            //displayMessages.append(chatName + "\n" + chatMessage + "\n" + chatTime + "   " + chatDate);

            TextView name = displayMessages.findViewById(R.id.other_name);
            TextView message = displayMessages.findViewById(R.id.other_message);
            TextView time = displayMessages.findViewById(R.id.other_message_time);

        }
    }
}