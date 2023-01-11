package com.demo.storage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.demo.R;

public class NewWordActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";
    public static final String IS_BATCH_SAVE = "IS_BATCH_SAVE";

    private EditText mEditWordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);

        mEditWordView = findViewById(R.id.edit_word);

        final Button batchSaveButton = findViewById(R.id.button_batch_save);
        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(this);
        batchSaveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent replyIntent = new Intent();
        if (TextUtils.isEmpty(mEditWordView.getText())) {
            setResult(RESULT_CANCELED, replyIntent);
            finish();
            return;
        }

        if (v.getId() == R.id.button_batch_save) {
            replyIntent.putExtra(IS_BATCH_SAVE, true);
        }
        String word = mEditWordView.getText().toString();
        replyIntent.putExtra(EXTRA_REPLY, word);
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}