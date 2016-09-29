package com.example.examplemediacontroller;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.IOException;

public class VideoPlayerActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl, MediaPlayer.OnBufferingUpdateListener {

    private TextView mCompleteTv;
    private SurfaceView mVideoSurface;
    private MediaPlayer mPlayer;
    private VideoControllerView mController;
    private int bufferPercentage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        mCompleteTv = (TextView) findViewById(R.id.tv_complete);
        mVideoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        SurfaceHolder videoHolder = mVideoSurface.getHolder();
        videoHolder.addCallback(this);

        mPlayer = new MediaPlayer();
        mController = new VideoControllerView(this);
        
        try {
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(this, Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
            mPlayer.setOnPreparedListener(this);
            mPlayer.setOnBufferingUpdateListener(this);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCompleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mController.show();
        return false;
    }

    // Implement SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	mPlayer.setDisplay(holder);
        mPlayer.prepareAsync();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        
    }
    // End SurfaceHolder.Callback

    // Implement MediaPlayer.OnPreparedListener
    @Override
    public void onPrepared(MediaPlayer mp) {
        mController.setMediaPlayer(this);
        mController.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        mPlayer.start();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        bufferPercentage = percent;
        Log.i(getLocalClassName(), "percent" + " ------ " + percent);
//        if (percent == 100) {
//            mPlayer.setOnBufferingUpdateListener(null);
//        }
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public int getBufferPercentage() {
        return bufferPercentage;
    }

    @Override
    public int getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return mPlayer.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public void pause() {
        mPlayer.pause();
    }

    @Override
    public void seekTo(int i) {
        mPlayer.seekTo(i);
    }

    @Override
    public void start() {
        mPlayer.start();
    }

    @Override
    public void toggleFullScreen() {
        
    }
}
