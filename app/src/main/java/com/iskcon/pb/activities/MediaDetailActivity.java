package com.iskcon.pb.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iskcon.pb.R;
import com.iskcon.pb.models.Media;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class MediaDetailActivity extends AppCompatActivity implements Runnable{

    MediaPlayer mediaPlayer;
    ImageButton btnPlay;
    ImageButton btnPause;
    SeekBar seekBar;
    TextView tvTotalTime;
    TextView tvCurrentTime;
    Thread currentThread;
    boolean continueRun = true;
    Media media;
    NotificationManager notifyManager;
    NotificationCompat.Builder mBuilder;
    int id = 1;
    private PowerManager.WakeLock wakeLock;
    private static final String TAG = "com.iskcon.maygupta.MediaDetailActivity.WAKE_LOCK_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_detail);

        initServices();

        Intent intent = getIntent();

        media = new Media(intent.getStringExtra("name"),
                intent.getStringExtra("author"),
                intent.getStringExtra("url"),
                intent.getStringExtra("type"),
                intent.getStringExtra("author_image_url"));

        ImageView ivMediaImage = (ImageView) findViewById(R.id.ivMediaImage);
        TextView tvMediaName = (TextView) findViewById(R.id.tvMediaName);

        Picasso.with(this).load(Uri.parse(media.authorImageUrl)).into(ivMediaImage);
        tvMediaName.setText(media.name);

		setUpViews();

        if(fileExistsOnCard()) {
            playKirtan(getFilepath());
        } else {
            streamKirtan(media.url);
        }

        notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(getFilename())
            .setContentText("Download in progress")
            .setSmallIcon(R.drawable.download);
        currentThread = new Thread(this);
        currentThread.start();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnPlay.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);
            }
        });
    }

    private void initServices() {
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, TAG);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.red))));
        getSupportActionBar().setTitle("Loading...");

        mediaPlayer = new MediaPlayer();
    }

    boolean fileExistsOnCard() {
        File file = new File(getFilepath());
        return file.exists();
    }

    public String getFilename() {
        return media.name.replaceAll(" ", "_").toLowerCase() + ".mp3";
    }

    public String getFilepath() {
        File mFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/iskcon");
        if (!mFolder.exists()) {
            mFolder.mkdir();
        }
        return mFolder.getAbsolutePath() + "/" + getFilename();
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
                if (b) {
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
        } else if (id == R.id.action_download) {
            download();
        }
        return super.onOptionsItemSelected(item);
    }

    private void download() {
        File mFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/iskcon");
        if (!mFolder.exists()) {
            mFolder.mkdir();
        }
        String filename = media.name.replaceAll(" ", "_").toLowerCase() + ".mp3";
        String absoluteFilename = mFolder.getAbsolutePath() + "/" + filename;

        File imgFile = new File(absoluteFilename);

        if (!imgFile.exists()) {
            try {
                imgFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Already downloaded!!", Toast.LENGTH_LONG).show();
            return;
        }
        new DownloadMedia().execute(media.url, absoluteFilename);
    }

    void playKirtan(String filepath) {
        try {
            mediaPlayer.setDataSource(filepath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                getSupportActionBar().setTitle(R.string.playing_audio);
                int duration = mp.getDuration();
                seekBar.setMax(duration);
                tvTotalTime.setText(timePresenter(duration));
                mp.start();
            }
        });
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
                getSupportActionBar().setTitle(R.string.playing_audio);
                int duration = mp.getDuration();
                seekBar.setMax(duration);
                tvTotalTime.setText(timePresenter(duration));
                mp.start();
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
            btnPlay.setVisibility(View.GONE);
            btnPause.setVisibility(View.VISIBLE);
            getSupportActionBar().setTitle("Playing...");
        }
	}

	public void pause(View view) {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPause.setVisibility(View.GONE);
            btnPlay.setVisibility(View.VISIBLE);
            getSupportActionBar().setTitle("Paused");
        }
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

    public class DownloadMedia extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... mediaData) {
            int count;
            try {
                URL mediaUrl = new URL(mediaData[0]);
                URLConnection conn = mediaUrl.openConnection();
                conn.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conn.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(mediaUrl.openStream());

                OutputStream output = new FileOutputStream(mediaData[1]);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    int percent = (int) (total * 100 / lenghtOfFile);
                    if(percent%10 == 0) {
                        publishProgress(percent);
                    }
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mBuilder.setProgress(100, values[0], false);
            notifyManager.notify(id, mBuilder.build());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mBuilder.setContentText("Download complete")
                .setProgress(0, 0, false);
            notifyManager.notify(id, mBuilder.build());
            media.pinInBackground();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        wakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wakeLock.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        continueRun = false;
    }
}
