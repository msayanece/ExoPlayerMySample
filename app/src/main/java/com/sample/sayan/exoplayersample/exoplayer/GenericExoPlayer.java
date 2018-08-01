package com.sample.sayan.exoplayersample.exoplayer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
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
    private SimpleExoPlayer player;
    private Activity activity;
    private String mediaUrl;
    private int playerType;
    private SimpleExoPlayerView simpleExoPlayerView;
    private ProgressBar progressBar;
    private long contentPosition;

    private GenericExoPlayer(Activity activity, String mediaUrl, int playerType, SimpleExoPlayerView simpleExoPlayerView, ProgressBar progressBar) {
        this.activity = activity;
        this.mediaUrl = mediaUrl;
        this.playerType = playerType;
        this.simpleExoPlayerView = simpleExoPlayerView;
        this.progressBar = progressBar;
        initializePlayer();
    }

    public static void showMediaDefault(Activity activity, String mediaUrl, int playerType) {
        Intent intent = new Intent(activity, GenericExoPlayerActivity.class);
        intent.putExtra("type", playerType);
        intent.putExtra("mediaUrl", mediaUrl);
        activity.startActivity(intent);
    }

    public static GenericExoPlayer createVideoPlayerDefault(Activity activity, String mediaUrl, int playerType, SimpleExoPlayerView simpleExoPlayerView, ProgressBar progressBar) {
        return new GenericExoPlayer(activity, mediaUrl, playerType, simpleExoPlayerView, progressBar);
    }

    private void initializePlayer() {
        switch (playerType) {
            case PLAYER_TYPE_DEFAULT:
                showDefaultPlayer(activity, mediaUrl);
                break;
            case PLAYER_TYPE_HLS:
                showHLSPlayer(activity, mediaUrl);
                break;
            default:
                showDefaultPlayer(activity, mediaUrl);
                break;
        }
        addListenerToExoplayer();
    }

    private void addListenerToExoplayer() {
        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case ExoPlayer.STATE_IDLE:
                        if (progressBar != null)
                            progressBar.setVisibility(View.INVISIBLE);
                        break;
                    case ExoPlayer.STATE_BUFFERING:
                        if (progressBar != null)
                            progressBar.setVisibility(View.VISIBLE);
                        break;
                    case ExoPlayer.STATE_READY:
                        if (progressBar != null)
                            progressBar.setVisibility(View.INVISIBLE);
                        break;
                    case ExoPlayer.STATE_ENDED:
                        if (progressBar != null)
                            progressBar.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
    }

    private void showDefaultPlayer(Activity activity, String mediaUrl) {
        player = ExoPlayerFactory.newSimpleInstance(activity, new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter())));
        simpleExoPlayerView.requestFocus();
        simpleExoPlayerView.setPlayer(player);

        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DefaultDataSourceFactory mediaDataSourceFactory = new DefaultDataSourceFactory(activity, Util.getUserAgent(activity, "exoPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mediaUrl),
                mediaDataSourceFactory, extractorsFactory, null, null);

        player.prepare(mediaSource);
        if (contentPosition != 0) {
            player.seekTo(contentPosition);
        }
    }

    private void showHLSPlayer(Activity activity, String mediaUrl) {
        player = ExoPlayerFactory.newSimpleInstance(activity, new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter())));
        simpleExoPlayerView.requestFocus();
        simpleExoPlayerView.setPlayer(player);

        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DefaultDataSourceFactory mediaDataSourceFactory = new DefaultDataSourceFactory(activity, Util.getUserAgent(activity, "exoPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new HlsMediaSource(Uri.parse(mediaUrl),
                mediaDataSourceFactory, null, null);

        player.prepare(mediaSource);
        if (contentPosition != 0) {
            player.seekTo(contentPosition);
        }
    }

    public void releasePlayer(){
        contentPosition = player.getContentPosition();
        player.release();
    }

    public void reconnectPlayer(){
        initializePlayer();
    }

    public static class GenericExoPlayerActivity extends AppCompatActivity {

        private SimpleExoPlayer player;
        private String mediaUrl;
        private int type;
        private SimpleExoPlayerView playerView;
        private long contentPosition;
        private ProgressBar progressBar;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_generic_exo_player);
            playerView = findViewById(R.id.playerView);
            progressBar = findViewById(R.id.progressBar);
            type = getIntent().getIntExtra("type", 1);
            mediaUrl = getIntent().getStringExtra("mediaUrl");
            initializePlayer();
//        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"),
//                mediaDataSourceFactory, extractorsFactory, null, null);
//
        }

        private void initializePlayer() {
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
            addListenerToExoplayer();
        }

        private void addListenerToExoplayer() {
            player.addListener(new Player.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    switch (playbackState) {
                        case ExoPlayer.STATE_IDLE:
                            progressBar.setVisibility(View.INVISIBLE);
                            break;
                        case ExoPlayer.STATE_BUFFERING:
                            progressBar.setVisibility(View.VISIBLE);
                            break;
                        case ExoPlayer.STATE_READY:
                            progressBar.setVisibility(View.INVISIBLE);
                            break;
                        case ExoPlayer.STATE_ENDED:
                            progressBar.setVisibility(View.INVISIBLE);
                            break;
                    }
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {

                }

                @Override
                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }

                @Override
                public void onPositionDiscontinuity(int reason) {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }

                @Override
                public void onSeekProcessed() {

                }
            });
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
            if (contentPosition != 0) {
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
            if (contentPosition != 0) {
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

//        @Override
//        public void onConfigurationChanged(Configuration newConfig) {
//            super.onConfigurationChanged(newConfig);
//            contentPosition = player.getContentPosition();
//            // Checking the orientation of the screen
//            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                //First Hide other objects (listview or recyclerview), better hide them using Gone.
//                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
//                params.width=params.MATCH_PARENT;
//                params.height=params.MATCH_PARENT;
//                playerView.setLayoutParams(params);
//            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//                //unhide your objects here.
//                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
//                params.width=params.MATCH_PARENT;
//                params.height=params.WRAP_CONTENT;
//                playerView.setLayoutParams(params);
//            }
//        }
    }
}
