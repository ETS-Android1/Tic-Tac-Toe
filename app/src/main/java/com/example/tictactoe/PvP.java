package com.example.tictactoe;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PvP extends AppCompatActivity
{
    /*  X=1
        O=0
        -=2
     */
    int activePlayer = 1;
    int playCounter = 0;
    int[] gameState = {2, 3, 4, 5, 6, 7, 8, 9, 10};
    int[][] winState = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    boolean gameActive = true;
    MediaPlayer playMedia;
    MediaPlayer playResult;
    boolean isVolumeMuted;
    
    public void gameReset (View view)
    {
        if ( playResult.isPlaying() )
            playResult.stop();
        
        activePlayer = 1;
        for ( int i = 2; i <= 10; i++ )
            gameState[i - 2] = i;
        
        playCounter = 0;
        gameActive = true;
        TextView textView = findViewById(R.id.status);
        textView.setText("X's TURN");
        
        TextView resetView = findViewById(R.id.resetButton);
        resetView.setText("RESTART GAME");
        
        ((ImageView) findViewById(R.id.imageView2)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView3)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView4)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView5)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView6)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView7)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView8)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView9)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView10)).setImageResource(0);
        
        findViewById(R.id.imageView4).clearAnimation();
        findViewById(R.id.imageView3).clearAnimation();
        findViewById(R.id.imageView2).clearAnimation();
        findViewById(R.id.imageView5).clearAnimation();
        findViewById(R.id.imageView6).clearAnimation();
        findViewById(R.id.imageView7).clearAnimation();
        findViewById(R.id.imageView8).clearAnimation();
        findViewById(R.id.imageView9).clearAnimation();
        findViewById(R.id.imageView10).clearAnimation();
        
    }
    
    public void inputTap (View view)
    {
        try
        {
            ImageView imageView = (ImageView) view;
            int imageCode = Integer.parseInt(imageView.getTag().toString());
            if ( gameState[imageCode] >= 2 && gameActive )
            {
                playCounter++;
                
                if ( activePlayer == 1 )
                {
                    imageView.setImageResource(R.drawable.xfinal);
                    gameState[imageCode] = 1;
                    activePlayer = 0;
                    TextView textView = findViewById(R.id.status);
                    textView.setText("O's TURN");
                }
                else
                {
                    imageView.setImageResource(R.drawable.oblue);
                    imageView.setPadding(0, 50, 10, 20);
                    gameState[imageCode] = 0;
                    activePlayer = 1;
                    TextView textView = findViewById(R.id.status);
                    textView.setText("X's TURN");
                }
                String winner;
                gameActive = winCheck(gameState);
                if ( gameActive == false )
                {
                    playResult = MediaPlayer.create(this, R.raw.wintrack);
                    if ( isVolumeMuted )
                        playResult.setVolume(0, 0);
                    playResult.start();
                    if ( activePlayer == 0 )
                        winner = "X WINS!!";
                    else
                        winner = "O WINS!!";
                    
                    TextView textView = findViewById(R.id.status);
                    textView.setText(winner);
                    
                    TextView resetView = findViewById(R.id.resetButton);
                    resetView.setText("NEW GAME");
                }
                if ( playCounter == 9 && gameActive )
                {
                    playResult = MediaPlayer.create(this, R.raw.tietrack);
                    if ( isVolumeMuted )
                        playResult.setVolume(0, 0);
                    
                    playResult.start();
                    TextView textView = findViewById(R.id.status);
                    textView.setText("GAME DRAW!");
                    
                    TextView resetView = findViewById(R.id.resetButton);
                    resetView.setText("NEW GAME");
                    gameActive = false;
                }
            }
        } catch ( Exception E )
        {
            E.printStackTrace();
        }
    }
    
    private boolean winCheck (int[] gameStateClone)
    {
        for ( int[] winposition : winState )
        {
            
            if ( gameStateClone[winposition[0]] == gameStateClone[winposition[1]] &&
                    gameStateClone[winposition[1]] == gameStateClone[winposition[2]] )
            {
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.blink);
                if ( winposition[0] == 0 && winposition[1] == 1 && winposition[2] == 2 )
                {
                    findViewById(R.id.imageView4).startAnimation(animation);
                    findViewById(R.id.imageView3).startAnimation(animation);
                    findViewById(R.id.imageView2).startAnimation(animation);
                }
                else if ( winposition[0] == 3 && winposition[1] == 4 && winposition[2] == 5 )
                {
                    findViewById(R.id.imageView5).startAnimation(animation);
                    findViewById(R.id.imageView6).startAnimation(animation);
                    findViewById(R.id.imageView7).startAnimation(animation);
                }
                else if ( winposition[0] == 6 && winposition[1] == 7 && winposition[2] == 8 )
                {
                    findViewById(R.id.imageView8).startAnimation(animation);
                    findViewById(R.id.imageView9).startAnimation(animation);
                    findViewById(R.id.imageView10).startAnimation(animation);
                }
                else if ( winposition[0] == 0 && winposition[1] == 3 && winposition[2] == 6 )
                {
                    findViewById(R.id.imageView4).startAnimation(animation);
                    findViewById(R.id.imageView7).startAnimation(animation);
                    findViewById(R.id.imageView10).startAnimation(animation);
                }
                else if ( winposition[0] == 1 && winposition[1] == 4 && winposition[2] == 7 )
                {
                    findViewById(R.id.imageView3).startAnimation(animation);
                    findViewById(R.id.imageView5).startAnimation(animation);
                    findViewById(R.id.imageView9).startAnimation(animation);
                }
                else if ( winposition[0] == 2 && winposition[1] == 5 && winposition[2] == 8 )
                {
                    findViewById(R.id.imageView2).startAnimation(animation);
                    findViewById(R.id.imageView6).startAnimation(animation);
                    findViewById(R.id.imageView8).startAnimation(animation);
                }
                else if ( winposition[0] == 0 && winposition[1] == 4 && winposition[2] == 8 )
                {
                    findViewById(R.id.imageView4).startAnimation(animation);
                    findViewById(R.id.imageView5).startAnimation(animation);
                    findViewById(R.id.imageView8).startAnimation(animation);
                    
                }
                else if ( winposition[0] == 2 && winposition[1] == 4 && winposition[2] == 6 )
                {
                    findViewById(R.id.imageView2).startAnimation(animation);
                    findViewById(R.id.imageView5).startAnimation(animation);
                    findViewById(R.id.imageView10).startAnimation(animation);
                }
                return false;
            }
        }
        return true;
    }
    
    public void returnToHome (View view)
    {
        playMedia.stop();
        finish();
    }
    
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvp);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        isVolumeMuted = intent.getBooleanExtra("volumeStatus", false);
        System.out.println(isVolumeMuted);
        
        playResult = MediaPlayer.create(this, R.raw.tietrack);
        playMedia = MediaPlayer.create(this, R.raw.playtrack);
        
        if ( !isVolumeMuted )
        {
            ((ImageView) findViewById(R.id.volumeIcon)).setImageResource(R.drawable.volumehighiconfinal);
            playMedia.setVolume(1.0f, 1.0f);
            isVolumeMuted = false;
        }
        else
        {
            ((ImageView) findViewById(R.id.volumeIcon)).setImageResource(R.drawable.volumemuteiconfinal);
            playMedia.setVolume(0.0f, 0.0f);
            isVolumeMuted = true;
        }
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
            playMedia.setVolume(1.0f, 1.0f);
            isVolumeMuted = false;
        }
        else
        {
            ((ImageView) findViewById(R.id.volumeIcon)).setImageResource(R.drawable.volumemuteiconfinal);
            playMedia.setVolume(0.0f, 0.0f);
            isVolumeMuted = true;
        }
    }
    
    @Override
    protected void onStart ()
    {
        if ( !playMedia.isPlaying() )
        {
            playMedia.start();
            playMedia.setLooping(true);
        }
        super.onStart();
    }
    
    @Override
    public void onBackPressed ()
    {
        playMedia.stop();
        finish();
        super.onBackPressed();
    }
    
    @Override
    protected void onUserLeaveHint ()
    {
        playMedia.seekTo(0);
        playMedia.pause();
        super.onUserLeaveHint();
    }
    
    @Override
    protected void onRestart ()
    {
        if ( !playMedia.isPlaying() )
        {
            playMedia.start();
            playMedia.setLooping(true);
        }
        super.onRestart();
    }
}