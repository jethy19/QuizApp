package com.example.quizapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class prefss {
    public static final String HIGHEST_SCORE = "highest_score";
    private SharedPreferences preferences;

    public prefss(Activity context)
    {
        this.preferences = context.getPreferences(Context.MODE_PRIVATE);
    }

    public void saveHighestScore(int Score)
    {
        int currentscore = Score;
        int lastscore = preferences.getInt(HIGHEST_SCORE,0);
        if(currentscore > lastscore)
        {
            preferences.edit().putInt(HIGHEST_SCORE,currentscore).apply();
        }
    }

    public int getHighestScore()
    {
        return preferences.getInt(HIGHEST_SCORE,0);
    }

    public void setstate(int index)
    {
        preferences.edit().putInt("trivia_state",index).apply();
    }

    public int getstate()
    {
        return preferences.getInt("trivia_state",0);
    }
}
