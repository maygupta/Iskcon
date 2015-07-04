package com.example.maygupta.iskcon;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class Download {
    // Media Player Object
    private MediaPlayer mPlayer;

    private Context mContext;

    public void setCurrentView(View currentView) {
        this.currentView = currentView;
    }

    public View getCurrentView() {
        return currentView;
    }

    private View currentView;

    Download(Context context) {
        mContext = context;
    }

    protected boolean isPlaying() {
        boolean ret = false;
        if ( mPlayer != null ) {
            ret = mPlayer.isPlaying();
        }
        return ret;
    }

    protected void stopCurrentSong() {
        if (mPlayer != null && currentView != null) {
            (currentView.findViewById(R.id.play_icon)).setVisibility(View.VISIBLE);
            (currentView.findViewById(R.id.pause_icon)).setVisibility(View.GONE);
            mPlayer.stop();
        }
    }

    protected void pause() {
        if ( mPlayer != null ) {
            (currentView.findViewById(R.id.play_icon)).setVisibility(View.VISIBLE);
            (currentView.findViewById(R.id.pause_icon)).setVisibility(View.GONE);
            mPlayer.pause();
        }
    }

    protected void resume() {
        if ( mPlayer != null ) {
            (currentView.findViewById(R.id.play_icon)).setVisibility(View.GONE);
            (currentView.findViewById(R.id.pause_icon)).setVisibility(View.VISIBLE);
            mPlayer.seekTo(mPlayer.getCurrentPosition());
            mPlayer.start();
        }
    }

    // Play Music
    protected void playMusic(File file){
        // Read Mp3 file present under SD card
        Uri myUri1 = Uri.fromFile(file);
        mPlayer  = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        (currentView.findViewById(R.id.play_icon)).setVisibility(View.GONE);
        (currentView.findViewById(R.id.pause_icon)).setVisibility(View.VISIBLE);
        try {
            mPlayer.setDataSource(mContext, myUri1);
            mPlayer.prepare();
            // Start playing the Music file
            mPlayer.start();

            mPlayer.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    Toast.makeText(mContext, "Music completed playing",Toast.LENGTH_LONG).show();
                }
            });
        } catch (IllegalArgumentException e) {
            Toast.makeText(mContext, "You might not set the URI correctly!",	Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(mContext,	"URI cannot be accessed, permissed needed",	Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(mContext,	"Media Player is not in correct state",	Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(mContext,	"IO Error occured",	Toast.LENGTH_LONG).show();
        }
    }
}
