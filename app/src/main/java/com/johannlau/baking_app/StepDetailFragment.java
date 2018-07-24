package com.johannlau.baking_app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.johannlau.baking_app.utilities.Steps;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment implements Player.EventListener {

    @BindView(R.id.exo_player)
    PlayerView exoPlayerView;
    @BindView(R.id.step_long_description)
    TextView detailLongDescriptionTextView;


    private static final String TAG = StepDetailFragment.class.getSimpleName();
    private final String POS_KEY = "EXO_PLAYER_POSITION";

    private SimpleExoPlayer exoPlayer;
    private Steps recipe_step;
    private String step_detail_video_url;
    private String step_detail_long_description;
    private static MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;
    private NotificationManager notificationManager;


    public StepDetailFragment( ) {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.step_detail_fragment,container,false);
        ButterKnife.bind(this,rootView);

        recipe_step = getArguments().getParcelable(getString(R.string.fragmentstepdetail));

        getVideoURLfromSteps(recipe_step);
        getLongDescriptionfromSteps(recipe_step);

        detailLongDescriptionTextView.setText(step_detail_long_description);

        initializeMediaSession();
        initializePlayer();

        return rootView;
    }

    private void getVideoURLfromSteps(Steps step) {

        step_detail_video_url = step.getVideoUrl();
        if(step_detail_video_url.isEmpty() && !(step.getThumbnailURL().isEmpty())) {
            step_detail_video_url = step.getThumbnailURL();
        }
    }

    private void getLongDescriptionfromSteps(Steps steps) {

        step_detail_long_description = steps.getLongDescription();
        if(step_detail_long_description.isEmpty()) {
            step_detail_long_description = "No Step Details Found";
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(POS_KEY, exoPlayer.getCurrentPosition());
        outState.putParcelable(getString(R.string.restoreStep),recipe_step);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {
            Long savedTime = savedInstanceState.getLong(POS_KEY);
            recipe_step = savedInstanceState.getParcelable(getString(R.string.restoreStep));
            Log.v(TAG,recipe_step.getShortDescription());
            getVideoURLfromSteps(recipe_step);
            exoPlayer.seekTo(savedTime);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(exoPlayer == null) {
            initializePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    // Create Media Session for playback controls from other devices (e.g. Bluetooth, Notification)
    private void initializeMediaSession() {
        
        mediaSession = new MediaSessionCompat(getContext(),TAG);

        mediaSession.setMediaButtonReceiver(null);

        stateBuilder = new PlaybackStateCompat.Builder().setActions(PlaybackState.ACTION_PLAY |
                PlaybackState.ACTION_PLAY_PAUSE |
                PlaybackState.ACTION_PAUSE |
                PlaybackState.ACTION_SKIP_TO_PREVIOUS);
        mediaSession.setPlaybackState(stateBuilder.build());

        mediaSession.setCallback(new SessionCallback());

        mediaSession.setActive(true);
    }

    private void initializePlayer() {
        if(exoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(),trackSelector);


            if(!step_detail_video_url.isEmpty()) {
                exoPlayerView.setPlayer(exoPlayer);
                exoPlayer.setVideoSurfaceView((SurfaceView)exoPlayerView.getVideoSurfaceView());
                exoPlayer.addListener(this);

                String userAgent = Util.getUserAgent(getContext(),"BakingApp");
                ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory( new DefaultHttpDataSourceFactory(userAgent)).createMediaSource(Uri.parse(step_detail_video_url));
                exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(true);
            }
           else {
                exoPlayerView.setPlayer(null);
                exoPlayerView.setVisibility(View.GONE);
                Toast.makeText(getContext(),"No Video Available",Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Release the player to stop music playback
    private void releasePlayer() {
        if(exoPlayer != null){
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

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
        if((playbackState == Player.STATE_READY) && playWhenReady) {
            stateBuilder.setState(PlaybackState.STATE_PLAYING, exoPlayer.getCurrentPosition(),1f);
        }
        else if ((playbackState == Player.STATE_READY)) {
            stateBuilder.setState(PlaybackState.STATE_PAUSED, exoPlayer.getCurrentPosition(),1f);
        }
        mediaSession.setPlaybackState(stateBuilder.build());
        showNotification(stateBuilder.build());
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

    // Creating the Media Notification
    private void showNotification(PlaybackStateCompat playbackState) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),TAG);

        int icon;
        String play_pause;
        if(playbackState.getState() == PlaybackState.STATE_PLAYING) {
            icon = R.drawable.exo_controls_pause;
            play_pause = getString(R.string.pause);
        }
        else {
            icon = R.drawable.exo_controls_play;
            play_pause = getString(R.string.play);
        }

        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(icon,play_pause,
                MediaButtonReceiver.buildMediaButtonPendingIntent(getContext(),PlaybackStateCompat.ACTION_PLAY_PAUSE));
        NotificationCompat.Action restartAction = new NotificationCompat.Action(R.drawable.exo_controls_previous, getString(R.string.restart),
                MediaButtonReceiver.buildMediaButtonPendingIntent(getContext(),PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        PendingIntent contentIntent = PendingIntent.getActivity(getContext(),0,new Intent(getContext(),StepDetailActivity.class),0);
        builder.setContentTitle("Baking App")
                .setContentText(recipe_step.getShortDescription())
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.exo_controls_play)
                .addAction(restartAction)
                .addAction(playPauseAction)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(mediaSession.getSessionToken())
                .setShowActionsInCompactView(0,1));

        notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = TAG;
            String NAME = "channel";
            String DESCRIPTION = "my_channel";
            int importance = NotificationManager.IMPORTANCE_LOW;

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID,NAME,importance);
            mChannel.setDescription(DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            mChannel.setShowBadge(false);
            mChannel.setSound(null,null);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(0,builder.build());
    }

    // Media Controls Callback functions
    private class SessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            exoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            exoPlayer.seekTo(0);
        }
    }

    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {};

        @Override
        public void onReceive(Context context, Intent intent) {

            MediaButtonReceiver.handleIntent(mediaSession,intent);
        }
    }
}
