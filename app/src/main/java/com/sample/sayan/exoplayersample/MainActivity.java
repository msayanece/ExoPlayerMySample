package com.sample.sayan.exoplayersample;
import android.app.Dialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.sample.sayan.exoplayersample.exoplayer.GenericExoPlayer;

public class MainActivity extends AppCompatActivity {

    private static final String URL_MP4 = "http://underconstruction.co.in/eve2/upload/post/15_17_26_tomjerry.mp4";
    private static final String URL_MP3 = "http://underconstruction.co.in/eve2/upload/drive/FileImg1532430621.mp3";
    private static final String URL_HLS = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8";
    private GenericExoPlayer exoPlayer;
    private ImageView mFullScreenIcon;
    private FrameLayout mFullScreenButton;
    private Dialog mFullScreenDialog;
    private SimpleExoPlayerView mExoPlayerView;
    private boolean mExoPlayerFullscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mExoPlayerView = findViewById(R.id.simpleExoPlayerView);
        exoPlayer = GenericExoPlayer.createVideoPlayerDefault(this, URL_MP4, GenericExoPlayer.PLAYER_TYPE_DEFAULT, mExoPlayerView, null);
        initFullscreenButton();
    }

    public void onClickButton(View view) {
        GenericExoPlayer.showMediaDefault(this, URL_HLS, GenericExoPlayer.PLAYER_TYPE_HLS);
    }

    private void initFullscreenButton() {
        initFullscreenDialog();
        PlaybackControlView controlView = mExoPlayerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen)
                    openFullscreenDialog();
                else
                    closeFullscreenDialog();
            }
        });
    }

    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    private void openFullscreenDialog() {

//        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
//        mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_skrink));
//        mExoPlayerFullscreen = true;
//        mFullScreenDialog.show();
        GenericExoPlayer.showMediaDefault(this, URL_MP4, GenericExoPlayer.PLAYER_TYPE_DEFAULT, true);
    }

    private void closeFullscreenDialog() {

        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(mExoPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_expand));
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
