package com.demo.rxjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.demo.R;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RxJavaActivity extends AppCompatActivity {

    private static final String TAG = "RxJavaActivity";

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);

        textView = findViewById(R.id.textview);

        // (1) 原始方式
//        // 1)创建一个上游 Observable
//        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                emitter.onNext(1);
//                emitter.onNext(2);
//                emitter.onNext(3);
//                emitter.onComplete();
//            }
//        });
//
//        // 2)创建一个下游 Observer
//        Observer<Integer> observer = new Observer<Integer>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                Log.d(TAG, "subscribe");
//                textView.setText("subscribe");
//            }
//
//            @Override
//            public void onNext(Integer value) {
//                Log.d(TAG, "" + value);
//                textView.setText("" + value);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d(TAG, "error");
//                textView.setText("error");
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "complete");
//                textView.setText("complete");
//            }
//        };
//
//        //3)建立连接
//        observable.subscribe(observer);

        // (2) 链式方式
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "subscribe");
                textView.setText("subscribe");
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "" + value);
                textView.setText("" + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "error");
                textView.setText("error");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "complete");
                textView.setText("complete");
            }
        });


        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            Log.d(TAG, "emit 1");
            emitter.onNext(1);
            Log.d(TAG, "emit 2");
            emitter.onNext(2);
            Log.d(TAG, "emit 3");
            emitter.onNext(3);
            Log.d(TAG, "emit complete");
            emitter.onComplete();
            Log.d(TAG, "emit 4");
            emitter.onNext(4);
        }).subscribe(integer -> {
            Log.d(TAG, "" + integer);
            textView.setText("" + integer);
        }, throwable -> {
            Log.d(TAG, "error");
            textView.setText("error");
        }, () -> {
            Log.d(TAG, "complete");
            textView.setText("complete");
        }, disposable -> {
            Log.d(TAG, "subscribe");
            textView.setText("subscribe");
        });

    }
}