package com.mashkovska.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import static com.mashkovska.authentication.MoviesFragment.EXTRA_DESCRIPTION;
import static com.mashkovska.authentication.MoviesFragment.EXTRA_TITTLE;
import static com.mashkovska.authentication.MoviesFragment.EXTRA_URL;
import static com.mashkovska.authentication.MoviesFragment.EXTRA_YEAR;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String title = intent.getStringExtra(EXTRA_TITTLE);
        Integer year = intent.getIntExtra(EXTRA_YEAR, 0);
        String description = intent.getStringExtra(EXTRA_DESCRIPTION);

        ImageView posterImageView = findViewById(R.id.detail_imageView);
        TextView titleTextView = findViewById(R.id.detail_tittle);
        TextView yearTextView = findViewById(R.id.detail_year);
        TextView descriptionTextView = findViewById(R.id.detail_description);

        Picasso.get().load(imageUrl).fit().centerInside().into(posterImageView);
        titleTextView.setText(title);
        yearTextView.setText("Year: " + year.toString());
        descriptionTextView.setText(description);
    }
}