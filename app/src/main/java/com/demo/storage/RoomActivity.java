package com.demo.storage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.demo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.walker.storage.room.model.User;
import com.walker.storage.room.model.Word;
import com.walker.storage.room.relation.UserAndLibrary;
import com.walker.storage.room.relation.UserWithMusicLists;
import com.walker.storage.room.repository.WordRepository;
import com.walker.storage.room.viewmodel.UserViewModel;
import com.walker.storage.room.viewmodel.WordViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class RoomActivity extends AppCompatActivity {

    private static final String TAG = "RoomActivity";

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    private WordViewModel mWordViewModel;
    private UserViewModel mUserViewModel;

    private final Function<User, String> user2StrFun = user -> String.format("%s %s", user.firstName, user.lastName);
    private final Function<Word, String> word2StrFun = word -> {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return String.format(Locale.getDefault(), "uer(%s) time(%s)", word.getContent(), ft.format(new Date(word.getCreateTime())));
    };


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

        WordRepository mRepository = new WordRepository(this);
        mRepository.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                Log.d(TAG, "words: " + words);
                adapter.submitList(words);
            }
        });

//        mWordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
//            @Override
//            public void onChanged(List<Word> words) {
//                Log.d(TAG, "words: " + words);
//                adapter.submitList(words);
//            }
//        });

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

        LiveData<Word> wordLiveData = mWordViewModel.getLastWord();
        LiveData<String> stringLiveData = Transformations.map(wordLiveData, word2StrFun);

        findViewById(R.id.get_user_fab).setOnClickListener(view -> stringLiveData.observe(RoomActivity.this, s -> {
            Log.d(TAG, s);
            Toast.makeText(RoomActivity.this, s, Toast.LENGTH_SHORT).show();
        }));
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