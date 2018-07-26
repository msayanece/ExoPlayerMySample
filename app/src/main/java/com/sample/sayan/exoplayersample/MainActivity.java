package com.sample.sayan.exoplayersample;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sample.sayan.exoplayersample.exoplayer.GenericExoPlayer;

public class MainActivity extends AppCompatActivity {

    private static final String URL_MP4 = "http://underconstruction.co.in/eve2/upload/post/15_17_26_tomjerry.mp4";
    private static final String URL_MP3 = "http://underconstruction.co.in/eve2/upload/drive/FileImg1532430621.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickButton(View view) {
        GenericExoPlayer.showMediaDefault(this, URL_MP3, GenericExoPlayer.PLAYER_TYPE_DEFAULT);
    }
}
