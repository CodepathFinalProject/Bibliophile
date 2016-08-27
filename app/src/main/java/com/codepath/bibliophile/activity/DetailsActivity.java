package com.codepath.bibliophile.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.bibliophile.R;

public class DetailsActivity extends AppCompatActivity {

    ImageView ivCover;
    TextView tvDTitle;
    TextView tvDAuthor;
    TextView tvDDescription;
    TextView tvDPrice;
    TextView tvCondition;
    TextView tvISBN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        tvDTitle = (TextView) findViewById(R.id.tvDTitle);
        tvDAuthor = (TextView) findViewById(R.id.tvDAuthor);
        tvDDescription = (TextView) findViewById(R.id.tvDDescription);
        tvDPrice = (TextView) findViewById(R.id.tvDPrice);
        ivCover = (ImageView) findViewById(R.id.ivDCover);
        tvCondition = (TextView) findViewById(R.id.tvCondition);
        tvISBN = (TextView) findViewById(R.id.tvISBN);
        Bundle args = getIntent().getExtras();
        args.getString("title");
        Toast.makeText(this,"inside details " + args.getString("title"),Toast.LENGTH_SHORT).show();

        tvDTitle.setText(args.getString("title"));
        tvDAuthor.setText(args.getString("author"));
        tvDDescription.setText(args.getString("description"));
        tvDPrice.setText(args.getString("price"));
        tvCondition.setText(args.getString("condition"));
        tvISBN.setText(args.getString("isbn"));
        ivCover.setImageResource(0);
        Glide.with(this).load(args.getString("cover")).into(ivCover);

    }
}
