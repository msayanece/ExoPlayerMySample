package com.sample.sayan.exoplayersample;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.sample.sayan.exoplayersample.exoplayer.GenericExoPlayer;

public class MainActivity extends AppCompatActivity {

    private static final String URL_MP4 = "http://underconstruction.co.in/eve2/upload/post/15_17_26_tomjerry.mp4";
    private static final String URL_MP3 = "http://underconstruction.co.in/eve2/upload/drive/FileImg1532430621.mp3";
    private static final String URL_HLS = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8";
    private GenericExoPlayer exoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SimpleExoPlayerView simpleExoPlayerView = findViewById(R.id.simpleExoPlayerView);
        exoPlayer = GenericExoPlayer.createVideoPlayerDefault(this, URL_MP4, GenericExoPlayer.PLAYER_TYPE_DEFAULT, simpleExoPlayerView, null);
    }

    public void onClickButton(View view) {
        GenericExoPlayer.showMediaDefault(this, URL_HLS, GenericExoPlayer.PLAYER_TYPE_HLS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        exoPlayer.releasePlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        exoPlayer.reconnectPlayer();
    }
}
