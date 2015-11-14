package com.iskcon.pb.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iskcon.pb.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class MediaDetailActivity extends AppCompatActivity implements Runnable{

    MediaPlayer mediaPlayer;
    ImageButton btnPlay;
    ImageButton btnPause;
    SeekBar seekBar;
    TextView tvTotalTime;
    TextView tvCurrentTime;
    Thread currentThread;
    boolean continueRun = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_detail);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.red))));

        Intent intent = getIntent();
        mediaPlayer = new MediaPlayer();
        ImageView ivMediaImage = (ImageView) findViewById(R.id.ivMediaImage);
        Picasso.with(this).load(Uri.parse(getIntent().getStringExtra("image_url"))).into(ivMediaImage);
        TextView tvMediaName = (TextView) findViewById(R.id.tvMediaName);
        tvMediaName.setText(intent.getStringExtra("name"));

        getSupportActionBar().setTitle("Loading...");
		setUpViews();
		streamKirtan(intent.getStringExtra("url"));
        currentThread = new Thread(this);
        currentThread.start();
    }

	private void setUpViews() {
		btnPlay = (ImageButton) findViewById(R.id.media_play);
		btnPause = (ImageButton) findViewById(R.id.media_pause);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        tvCurrentTime= (TextView) findViewById(R.id.tvCurrentTime);
        tvTotalTime= (TextView) findViewById(R.id.tvTotalTime);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b) {
                    mediaPlayer.seekTo(i);
                    seekBar.setProgress(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_media_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void streamKirtan(String url) {
        // Set type to streaming
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // Listen for if the audio file can't be prepared
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // ... react appropriately ...
                // The MediaPlayer has moved to the Error state, must be reset!
                Toast.makeText(MediaDetailActivity.this, "Error loading the media", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        // Attach to when audio file is prepared for playing
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                getSupportActionBar().setTitle("Playing...");
                int duration = mp.getDuration();
                seekBar.setMax(duration);
                tvTotalTime.setText(timePresenter(duration));
                mp.start();
                btnPlay.setBackgroundColor(getResources().getColor(R.color.red));
            }
        });
        // Set the data source to the remote URL
        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int i) {
                if(mp.isPlaying() && seekBar != null) {
                    int currentPosition = mp.getCurrentPosition();
                    seekBar.setProgress(currentPosition);
                }
            }
        });

        // Trigger an async preparation which will file listener when completed
        mediaPlayer.prepareAsync();
    }

	public void play(View view) {
        if (!mediaPlayer.isPlaying()) {
            int currentPosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.seekTo(currentPosition);
            mediaPlayer.start();
            btnPlay.setBackgroundColor(getResources().getColor(R.color.red));
            btnPause.setBackgroundColor(getResources().getColor(R.color.grey));
            getSupportActionBar().setTitle("Playing...");
        }
	}

	public void pause(View view) {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlay.setBackgroundColor(getResources().getColor(R.color.grey));
            btnPause.setBackgroundColor(getResources().getColor(R.color.red));
            getSupportActionBar().setTitle("Paused...");
        }
	}

    @Override
    protected void onStop() {
        super.onStop();
        continueRun = false;
    }

    @Override
    public void run() {
        try{
            while(mediaPlayer != null){
                if(!continueRun){
                    mediaPlayer.release();
                    mediaPlayer = null;
                    return;
                }
                int currentPosition = mediaPlayer.getCurrentPosition();
                Message msg = new Message();
                msg.what = currentPosition;
                threadHandler.sendMessage(msg);
                Thread.sleep(1000);
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private Handler threadHandler = new Handler(){
        public void handleMessage(Message msg){
            seekBar.setProgress(msg.what);
            tvCurrentTime.setText(timePresenter(msg.what));
        }
    };

    public String timePresenter(int timeInMilliSeconds) {
        int hours = timeInMilliSeconds/(1000*60*60);
        int totalMinutes = timeInMilliSeconds/(1000*60);
        int minutes = totalMinutes%60;
        int totalSeconds = timeInMilliSeconds/1000;
        int seconds = totalSeconds%60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        continueRun = false;
    }

}
