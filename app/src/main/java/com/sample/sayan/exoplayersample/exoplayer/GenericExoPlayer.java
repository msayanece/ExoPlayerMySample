package com.sample.sayan.exoplayersample.exoplayer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.sample.sayan.exoplayersample.R;

public class GenericExoPlayer {

    public static final int PLAYER_TYPE_DEFAULT = 1;
    public static final int PLAYER_TYPE_HLS = 2;

    public static void showMediaDefault(Activity activity, String mediaUrl, int playerType) {
        Intent intent = new Intent(activity, GenericExoPlayerActivity.class);
        intent.putExtra("type", playerType);
        intent.putExtra("mediaUrl", mediaUrl);
        activity.startActivity(intent);
    }

    public static class GenericExoPlayerActivity extends AppCompatActivity {

        private SimpleExoPlayer player;
        private String mediaUrl;
        private int type;
        private SimpleExoPlayerView playerView;
        private long contentPosition;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_generic_exo_player);
            playerView = findViewById(R.id.playerView);
            type = getIntent().getIntExtra("type", 1);
            mediaUrl = getIntent().getStringExtra("mediaUrl");
            initializePlayer();
//        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"),
//                mediaDataSourceFactory, extractorsFactory, null, null);
//
        }

        private void initializePlayer(){
            switch (type) {
                case PLAYER_TYPE_DEFAULT:
                    showDefaultPlayer(playerView, mediaUrl);
                    break;
                case PLAYER_TYPE_HLS:
                    showHLSPlayer(playerView, mediaUrl);
                    break;
                default:
                    showDefaultPlayer(playerView, mediaUrl);
                    break;
            }
        }

        private void showDefaultPlayer(SimpleExoPlayerView playerView, String mediaUrl) {
            player = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter())));
            playerView.requestFocus();
            playerView.setPlayer(player);

            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            DefaultDataSourceFactory mediaDataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
            DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mediaUrl),
                    mediaDataSourceFactory, extractorsFactory, null, null);

            player.prepare(mediaSource);
            if (contentPosition != 0){
                player.seekTo(contentPosition);
            }
        }

        private void showHLSPlayer(SimpleExoPlayerView playerView, String mediaUrl) {
            player = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter())));
            playerView.requestFocus();
            playerView.setPlayer(player);

            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            DefaultDataSourceFactory mediaDataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
            DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            MediaSource mediaSource = new HlsMediaSource(Uri.parse(mediaUrl),
                    mediaDataSourceFactory, null, null);

            player.prepare(mediaSource);
            if (contentPosition != 0){
                player.seekTo(contentPosition);
            }
        }

        @Override
        protected void onPause() {
            super.onPause();
            contentPosition = player.getContentPosition();
            player.release();
        }

        @Override
        protected void onResume() {
            initializePlayer();
            super.onResume();
        }
    }
}
