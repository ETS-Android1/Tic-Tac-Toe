package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

public class levelHard extends AppCompatActivity
{
    int activePlayer = 1;
    int playCounter = 0;
    Integer[] gameState = {2, 3, 4, 5, 6, 7, 8, 9, 10};     //Initial Board
    boolean gameActive = true;
    int[][] winState = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};    //Possible Win Positions
    MediaPlayer playMedia;
    MediaPlayer playResult;
    boolean isVolumeMuted = false;
    
    public void inputTap (View view)
    {
        try
        {
            ImageView imageView = (ImageView) view;
            int imageCode = Integer.parseInt(imageView.getTag().toString());
            
            if ( gameState[imageCode] >= 2 && gameActive )
            {
                playCounter++;
                
                if ( activePlayer == 1 )    // Player X
                {
                    imageView.setImageResource(R.drawable.xfinal);
                    gameState[imageCode] = 1;
                    activePlayer = 0;
                    
                    TextView textView = findViewById(R.id.status);
                    textView.setText("O's Turn");
                }
                
                gameState = winCheck(gameState);
                gameActive = !Arrays.asList(gameState).contains(-2);
                if ( !gameActive )
                {
                    startAnimation(gameState);
                    gameActiveIsFalse();
                }
                else if ( playCounter == 9 && gameActive )
                {
                    gameIsDraw();
                }
                else if ( activePlayer == 0 )
                {
                    //delay();
                    playCounter++;
                    int bestMove = getBestMove();
                    
                    LinearLayout linearLayout = findViewById(R.id.linearLayout);
                    ImageView moveView = linearLayout.findViewWithTag(Integer.toString(bestMove));
                    moveView.setImageResource(R.drawable.oblue);
                    moveView.setPadding(0, 50, 10, 20);
                    gameState[bestMove] = 0;
                    activePlayer = 1;
                    TextView textView = findViewById(R.id.status);
                    textView.setText("X's TURN");
                    
                    gameState = winCheck(gameState);
                    gameActive = !Arrays.asList(gameState).contains(-2);
                    if ( !gameActive )
                    {
                        startAnimation(gameState);
                        gameActiveIsFalse();
                    }
                    else if ( playCounter == 9 && gameActive )
                    {
                        gameIsDraw();
                    }
                }
                
            }
        } catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    
    private int getBestMove ()
    {
        Integer[] cornerElements = {gameState[0], gameState[2], gameState[6], gameState[8]};        //Corner Elements
        Integer[] sideElements = {gameState[1], gameState[3], gameState[5], gameState[7]};          // Edge Elements
        Random random = new Random();
        
        if ( playCounter == 2)                 // Initial middle placement
        {
            //If corner elements contain X or centre is vacant
            if ( Arrays.asList(cornerElements).contains(1) || gameState[4]>=2)
                return 4;
            
            // If centre contains X
            else if ( gameState[4] == 1 )
            {
                int move;
                do
                {
                    move = random.nextInt(4);
                } while ( cornerElements[move] < 2 );
                
                if ( gameState[cornerElements[move] - 2] >= 2 )
                    return cornerElements[move] - 2;
            }
        }
        
        if ( playCounter == 4 )
        {
            int move;
            
            if ( gameState[4] == 0 )    // If centre is 0
            {                                   //To counter opposite diagonal effect
                if ( (cornerElements[0] == 1 && cornerElements[3] == 1) || (cornerElements[1] == 1 && cornerElements[2] == 1) )
                {
                    do
                    {
                        move = random.nextInt(4);
                    } while ( sideElements[move] < 2 );
                    
                    if ( gameState[sideElements[move] - 2] >= 2 )
                        return sideElements[move] - 2;
                }
            }
            else if ( gameState[4] == 1 && Arrays.asList(cornerElements).contains(1) )
            {
                int threat = checkThreat();
                if ( threat != -1 )
                    return threat;
    
                do
                {
                    move = random.nextInt(4);
                } while ( cornerElements[move] < 2 );
                
                if ( gameState[cornerElements[move] - 2] >= 2 )
                    return cornerElements[move] - 2;
            }
        }
        
        
        for ( int[] checkWin : winState )           //To check opportunity
        {
            int oCount = 0, vacancyPos = -1;
            for ( int i = 0; i < 3; i++ )
            {
                if ( gameState[checkWin[i]] == 0 )
                    ++oCount;
                else if ( gameState[checkWin[i]] >= 2 )
                    vacancyPos = checkWin[i];
            }
            if ( oCount == 2 && vacancyPos>=0 )
            {
                if(gameState[vacancyPos] >= 2)
                {
                    return vacancyPos;
                }
            }
        }
        int threat = checkThreat();
        if ( threat != -1 )
            return threat;
        
        if(gameState[4]==0)
        {
            if(gameState[3]>=2&&gameState[5]>=2)
                return 3;
            else if(gameState[1]>=2&&gameState[7]>=2)
                return 1;
        }
        
        int move;
    
        do
        {
            move = random.nextInt(9);
        } while ( gameState[move] < 2 );
        
        return move;
    }
    
    private int checkThreat ()
    {
        for ( int[] checkWin : winState )           //To check threat
        {
            int xCount = 0, vacancyPos = -1;
            for ( int i = 0; i < 3; i++ )
            {
                if ( gameState[checkWin[i]] == 1 )
                    ++xCount;
                else if ( gameState[checkWin[i]] >= 2 )
                    vacancyPos = checkWin[i];
            }
            
            if ( xCount == 2 && vacancyPos>=0 )
            {
                if(gameState[vacancyPos] >= 2)
                {
                    return vacancyPos;
                }
            }
        }
        return -1;      //No threat Code
    }
    
    
    private void startAnimation (Integer[] gameClone)
    {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.blink);
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        
        for ( int i = 0; i < 9; i++ )
        {
            if ( gameClone[i] == -2 )
            {
                ImageView animView = linearLayout.findViewWithTag(Integer.toString(i));
                animView.startAnimation(animation);
            }
        }
    }
    
    private Integer[] winCheck (Integer[] gameClone)
    {
        for ( int[] winposition : winState )
        {
            if ( gameClone[winposition[0]].equals(gameClone[winposition[1]]) &&
                    gameClone[winposition[1]].equals(gameClone[winposition[2]]) )
            {
                if ( winposition[0] == 0 && winposition[1] == 1 && winposition[2] == 2 )
                    gameClone[0] = gameClone[1] = gameClone[2] = -2;
                
                else if ( winposition[0] == 3 && winposition[1] == 4 && winposition[2] == 5 )
                    gameClone[3] = gameClone[4] = gameClone[5] = -2;
                
                else if ( winposition[0] == 6 && winposition[1] == 7 && winposition[2] == 8 )
                    gameClone[6] = gameClone[7] = gameClone[8] = -2;
                
                else if ( winposition[0] == 0 && winposition[1] == 3 && winposition[2] == 6 )
                    gameClone[0] = gameClone[3] = gameClone[6] = -2;
                
                else if ( winposition[0] == 1 && winposition[1] == 4 && winposition[2] == 7 )
                    gameClone[1] = gameClone[4] = gameClone[7] = -2;
                
                else if ( winposition[0] == 2 && winposition[1] == 5 && winposition[2] == 8 )
                    gameClone[2] = gameClone[5] = gameClone[8] = -2;
                
                else if ( winposition[0] == 0 && winposition[1] == 4 && winposition[2] == 8 )
                    gameClone[0] = gameClone[4] = gameClone[8] = -2;
                
                else if ( winposition[0] == 2 && winposition[1] == 4 && winposition[2] == 6 )
                    gameClone[2] = gameClone[4] = gameClone[6] = -2;
            }
        }
        return gameClone;
    }
    
    public void gameActiveIsFalse ()
    {
        String winner;
        if ( activePlayer == 0 )
        {
            playResult = MediaPlayer.create(this, R.raw.wintrack);
            if ( isVolumeMuted )
                playResult.setVolume(0, 0);
            playResult.start();
            
            winner = "X WINS!!";
        }
        else
        {
            playResult = MediaPlayer.create(this, R.raw.losetrack);
            if ( isVolumeMuted )
                playResult.setVolume(0, 0);
            playResult.start();
            winner = "O WINS!!";
        }
        
        TextView textView = findViewById(R.id.status);
        textView.setText(winner);
        
        TextView resetView = findViewById(R.id.resetButton);
        resetView.setText("NEW GAME");
    }
    
    private void gameIsDraw ()
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
    
    public void onGameReset (View view)
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
        
        init_imageResource();
        init_animation();
    }
    
    private void init_imageResource ()
    {
        ((ImageView) findViewById(R.id.imageView2)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView3)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView4)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView5)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView6)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView7)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView8)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView9)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView10)).setImageResource(0);
    }
    
    private void init_animation ()
    {
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
    
    public void returnToHome (View view)
    {
        playMedia.stop();
        finish();
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
    
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_hard);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        
        Intent intent = getIntent();
        isVolumeMuted = intent.getBooleanExtra("volumeStatus", false);
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
}