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
import android.widget.EditText;
import android.widget.Toast;

import com.demo.R;
import com.demo.customview.activity.OtherProcessActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tencent.wink.storage.winkdb.model.User;
import com.tencent.wink.storage.winkdb.model.Word;
import com.tencent.wink.storage.winkdb.relation.UserAndLibrary;
import com.tencent.wink.storage.winkdb.relation.UserWithMusicLists;
import com.tencent.wink.storage.winkdb.viewmodel.UserViewModel;
import com.tencent.wink.storage.winkdb.viewmodel.WordViewModel;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class RoomActivity extends AppCompatActivity {

    private static final String TAG = "RoomActivity";

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    private WordViewModel mWordViewModel;
    private UserViewModel mUserViewModel;
    private WordListAdapter mAdapter;

    private final Function<User, String> user2StrFun = user -> String.format("%s %s", user.firstName, user.lastName);
    private final Function<Word, String> word2StrFun = this::word2StrFun;


    private String word2StrFun(Word word) {
        if (word == null) {
            return "";
        }
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        return String.format(Locale.getDefault(),
                "user(%s) time(%s)",
                word.getContent(), ft.format(new Date(word.getCreateTime())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        initViewModel();
        mAdapter = new WordListAdapter(new WordListAdapter.WordDiff(), mWordViewModel);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initFab();
    }

    private final Observer<List<Word>> observer = new Observer<List<Word>>() {
        @Override
        public void onChanged(List<Word> words) {
            Log.d(TAG, "words: " + words);
            if (mAdapter != null) {
                mAdapter.submitList(words);
            }
        }
    };

    private void initViewModel() {
        mWordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        mWordViewModel.getAllWords().observe(this, words -> {
            Log.d(TAG, "words: " + words);
            if (mAdapter != null) {
                mAdapter.submitList(words);
            }
        });

        mWordViewModel.getAllWordsWrapper().observe(this, wordWrappers -> {
            Log.d(TAG, "wordWrappers: " + wordWrappers);
        });

        mWordViewModel.getAllWordWrapperStr().observe(this, list -> Log.d(TAG, "[getAllWordWrapperStr] list: " + list));

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
    }

    private void initFab() {
        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(RoomActivity.this, NewWordActivity.class);
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
        });

        LiveData<Word> wordLiveData = mWordViewModel.getLastWord();
        LiveData<String> stringLiveData = Transformations.map(wordLiveData, word2StrFun);
        findViewById(R.id.fab_get_last_user).setOnClickListener(view -> {
            stringLiveData.observe(RoomActivity.this, s -> {
                Log.d(TAG, s);
                Toast.makeText(RoomActivity.this, s, Toast.LENGTH_SHORT).show();
            });
        });

        findViewById(R.id.fab_delete_user).setOnClickListener(view -> {

            EditText editText = findViewById(R.id.delete_words);
            String text = editText.getText().toString();
            Log.d(TAG, "editText text: " + text);
            String[] words = text.split(" ");
            mWordViewModel.delete(Arrays.asList(words));
        });

        findViewById(R.id.fab_jump_other_process).setOnClickListener(view -> {
            Intent intent = new Intent(this, OtherProcessActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "[onResume]");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
            Log.i(TAG, "[onActivityResult] word: " + word);
            mWordViewModel.insert(word);
        } else {
            Toast.makeText(getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_SHORT).show();
        }
    }
}