package com.material.tecgurus.activity.player;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.material.tecgurus.R;

public class PlayerMusicGenreImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_music_genre_image);
    }

    public void genreClick(View view) {
        if (view instanceof Button) {
            Button b = (Button) view;
            b.setSelected(!b.isSelected());
        }
    }
}
