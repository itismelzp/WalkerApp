package com.demo.storage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.demo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.walker.storage.room.model.Address;
import com.walker.storage.room.model.User;
import com.walker.storage.room.model.Word;
import com.walker.storage.room.relation.UserAndLibrary;
import com.walker.storage.room.relation.UserWithMusicLists;
import com.walker.storage.room.viewmodel.UserViewModel;
import com.walker.storage.room.viewmodel.WordViewModel;

import java.util.List;


public class RoomActivity extends AppCompatActivity {

    private static final String TAG = "RoomActivity";

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    private WordViewModel mWordViewModel;
    private UserViewModel mUserViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        mWordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        final WordListAdapter adapter = new WordListAdapter(new WordListAdapter.WordDiff(), mWordViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Update the cached copy of the words in the adapter.

        mWordViewModel.getAllWords().observe(this, adapter::submitList);

        mUserViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                Log.d(TAG, "users: " + users);
            }
        });

        mUserViewModel.getAllUsersWithLibrary().observe(this, new Observer<List<UserAndLibrary>>() {
            @Override
            public void onChanged(List<UserAndLibrary> userAndLibraries) {
                Log.d(TAG, "userAndLibraries: " + userAndLibraries);
            }
        });

        mUserViewModel.getAllUsersWithMusicLists().observe(this, new Observer<List<UserWithMusicLists>>() {
            @Override
            public void onChanged(List<UserWithMusicLists> userWithMusicLists) {
                Log.d(TAG, "userWithMusicLists: " + userWithMusicLists);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(RoomActivity.this, NewWordActivity.class);
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
            word.setCreateTime(System.currentTimeMillis());
            mWordViewModel.insert(word);
        } else {
            Toast.makeText(getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_SHORT).show();
        }
    }
}