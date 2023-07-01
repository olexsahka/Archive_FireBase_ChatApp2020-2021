package com.example.firebasechatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private static final int RC_IMAGE_PICKER =  125;
    private ListView listView;
    private Button sendMessageButton;
    private ImageView addPhotoImageButton;
    private TextInputEditText editMessageText;
    private MessageAdapter messageAdapter;
    private ArrayList<Message> messageArrayList = new ArrayList<>();
    private String userName = "User";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference messageDatabaseReference;
    private ChildEventListener messageChildEventListener;
    private DatabaseReference userDatabaseReference;
    private ChildEventListener userChildEventListener;
    private  FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private StorageReference imageReference;
    private String recipientUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        firebaseDatabase = FirebaseDatabase.getInstance();
        messageDatabaseReference = firebaseDatabase.getReference().child("messages");
        userDatabaseReference = firebaseDatabase.getReference().child("users");
        editMessageText = findViewById(R.id.inputText);
        addPhotoImageButton = findViewById(R.id.addPhotoImageButton);
        sendMessageButton = findViewById(R.id.messageSendButton);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        imageReference = storageReference.child("images");
        listView = findViewById(R.id.messageListView);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        messageAdapter = new MessageAdapter(this,R.layout.message_item,messageArrayList);
        listView.setAdapter(messageAdapter);

        recipientUid = getIntent().getStringExtra("recipientUid");
        userName =  getIntent().getStringExtra("recipientUsername");

        setTitle("Chat with "+ userName);

        editMessageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length()>0){
                    sendMessageButton.setEnabled(true);
                }
                else {
                    sendMessageButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editMessageText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500)});

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Message message = new Message();
                message.setText(editMessageText.getText().toString());
                message.setName(userName);
                message.setImageUrl(null);
                message.setSender(FirebaseAuth.getInstance().getCurrentUser().getUid());
                message.setRecipient(recipientUid);
                messageDatabaseReference.push().setValue(message);
                editMessageText.setText("");

            }
        });

        addPhotoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(intent,"Choose image"),RC_IMAGE_PICKER);

            }
        });

       messageChildEventListener = new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               Message message = snapshot.getValue(Message.class);
               if (message.getRecipient().equals(recipientUid) && message.getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                   message.setMyMessage(true);
                   messageAdapter.add(message);

               }
               else  if (message.getRecipient().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && message.getSender().equals(recipientUid)){
                   message.setMyMessage(false);
                   messageAdapter.add(message);

               }
           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot snapshot) {

           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       };

       userChildEventListener = new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               User user =  snapshot.getValue(User.class);
               if (user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                   userName = user.getName();
               }
           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot snapshot) {

           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       };

       messageDatabaseReference.addChildEventListener(messageChildEventListener);
       userDatabaseReference.addChildEventListener(userChildEventListener);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_dialog_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ChatActivity.this,SignInActivity.class));
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RC_IMAGE_PICKER:
                    Uri imageUrl =  data.getData();
                    StorageReference storageReference = imageReference.child(imageUrl.getLastPathSegment());

                    UploadTask uploadTask = storageReference.putFile(imageUrl);
                    uploadTask = storageReference.putFile(imageUrl);


                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                Log.d("task exeption failed",task.getException().toString());
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return storageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                Message message = new Message(downloadUri.toString(),userName);
                                message.setSender(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                message.setRecipient(recipientUid);
                                messageDatabaseReference.push().setValue(message);
                                messageAdapter.add(message);
                            } else {
                                Toast.makeText(ChatActivity.this," Не удалось загрузить фото",Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                break;
        }
    }
}