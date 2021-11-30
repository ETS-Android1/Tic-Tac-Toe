package com.example.tictactoe;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    MediaPlayer mediaPlayer;
    boolean isVolumeMuted = false;
    
    public void invokePvP (View view)
    {
        Intent intent = new Intent(this, PvP.class);
        intent.putExtra("volumeStatus", isVolumeMuted);
        startActivity(intent);
    }
    
    public void invokeCredits (View view)
    {
        Intent intent = new Intent(this, credits.class);
        startActivity(intent);
    }
    
    public void invokeComingSoonScr(View view)
    {
        Intent intent=new Intent(this,comingSoonScreen.class);
        startActivity(intent);
    }
    
    public void invokeLevelHard(View view)
    {
        Intent intent=new Intent(this,levelHard.class);
        intent.putExtra("volumeStatus", isVolumeMuted);     // Volume status
        startActivity(intent);
    }
    
    public void invokeEasy (View view)
    {
        Intent intent = new Intent(this, levelEasy.class);
        intent.putExtra("volumeStatus", isVolumeMuted);     // Volume status
        startActivity(intent);
    }
    
    public void invokeVolumeSet (View view)
    {
        setVolume();
    }
    
    private void setVolume ()
    {
        if ( isVolumeMuted )
        {
            ((ImageView) findViewById(R.id.volumeIcon)).setImageResource(R.drawable.volumehighiconfinal);
            mediaPlayer.setVolume(1.0f, 1.0f);
            isVolumeMuted = false;
        }
        else
        {
            ((ImageView) findViewById(R.id.volumeIcon)).setImageResource(R.drawable.volumemuteiconfinal);
            mediaPlayer.setVolume(0.0f, 0.0f);
            isVolumeMuted = true;
        }
    }
    
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        mediaPlayer = MediaPlayer.create(this, R.raw.mainscreentrack);
        
    }
    
    @Override
    public void onBackPressed ()
    {
        mediaPlayer.stop();
        finish();
    }
    
    @Override
    protected void onUserLeaveHint ()
    {
        mediaPlayer.seekTo(0);
        mediaPlayer.pause();
        super.onUserLeaveHint();
    }
    
    @Override
    protected void onRestart ()
    {
        if(!mediaPlayer.isPlaying())
        {
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }
        super.onRestart();
    }
    
    @Override
    protected void onStart ()
    {
        if ( !mediaPlayer.isPlaying() )
        {
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }
        super.onStart();
    }
}