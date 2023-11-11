package com.example.quizapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.quizapp.data.AnswerListAsyncResponse;
import com.example.quizapp.data.Repository;
import com.example.quizapp.databinding.ActivityMainBinding;
import com.example.quizapp.model.Question;
import com.example.quizapp.model.Score;
import com.example.quizapp.util.prefss;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int index = 0;
    List<Question> questionList;
    private int scorecounter = 0;
    private Score score;
    private prefss Prefss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        score = new Score();
        Prefss = new prefss(MainActivity.this);
        index = Prefss.getstate();
        binding.highesttext.setText("Highest: "+ String.valueOf(Prefss.getHighestScore()));
        binding.scoretext.setText("Score: "+ String.valueOf(score.getScore()));
        questionList =  new Repository().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                binding.questiontext.setText((CharSequence) questionArrayList.get(index).getAnswer().toString());
                updatecounter(questionArrayList);
            }
        });

        binding.Nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getnextquestion();
                //Prefss.saveHighestScore(scorecounter);
            }
        });

        binding.truebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkanswer(true);
                updatequestion();
            }
        });

        binding.falsebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkanswer(false);
                updatequestion();
            }
        });

    }

    private void getnextquestion() {
        index = (index+1) % questionList.size();
        updatequestion();
    }

    private void checkanswer(boolean b)
    {
        boolean answer = questionList.get(index).isAnswertrue();
        int snackmsg = 0;
        if(b == answer)
        {
            snackmsg = R.string.correct_answer;
            fadeanimation();
            addpoints();
        }
        else
        {
            snackmsg = R.string.incorrect;
            shakeAnimation();
            deductpoints();
        }
        Snackbar.make(binding.cardView,snackmsg,Snackbar.LENGTH_SHORT).show();
    }

    private void updatecounter(ArrayList<Question> questionArrayList) {
        binding.textViewOutOf.setText("Question: "+(index+1)+"/"+ questionArrayList.size());
    }

    private void updatequestion() {
        String question = questionList.get(index).getAnswer();
        binding.questiontext.setText(question);
        updatecounter((ArrayList<Question>)questionList);
    }

    private void fadeanimation()
    {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        binding.cardView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questiontext.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questiontext.setTextColor(Color.WHITE);
                getnextquestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void shakeAnimation()
    {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);
        binding.cardView.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questiontext.setTextColor(Color.RED);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questiontext.setTextColor(Color.WHITE);
                getnextquestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void addpoints()
    {
        scorecounter += 2;
        score.setScore(scorecounter);
        binding.scoretext.setText("Score: " + String.valueOf(score.getScore()));
    }

    private void deductpoints()
    {
        scorecounter -= 1;
        score.setScore(scorecounter);
        binding.scoretext.setText("Score: q" + String.valueOf(score.getScore()));
    }

    @Override
    protected void onPause() {
        Prefss.saveHighestScore(score.getScore());
        Prefss.setstate(index);
        super.onPause();
    }
}