package com.codepath.bibliophile.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    TextView tvOwnerName;
    TextView tvOwnerEmail;
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
        tvOwnerName = (TextView) findViewById(R.id.tvDOwnerName);
        tvOwnerEmail = (TextView) findViewById(R.id.tvDOwnerEmail);
        Bundle args = getIntent().getExtras();
        tvDTitle.setText(args.getString("title"));
        tvDAuthor.setText(args.getString("author"));
        tvDDescription.setText(args.getString("description"));
        tvDPrice.setText(args.getString("price"));
        tvCondition.setText(args.getString("condition"));
        tvISBN.setText(args.getString("isbn"));
        tvOwnerName.setText(args.getString("bookOwner"));
        tvOwnerEmail.setText(args.getString("ownerEmail"));
        ivCover.setImageResource(0);
        Glide.with(this).load(args.getString("cover")).into(ivCover);

        tvOwnerEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { tvOwnerEmail.getText().toString() });
                sendIntent.setData(Uri.parse("mailto:"));
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Interested in buying the book \"" + tvDTitle.getText().toString() + "\"");
                sendIntent.setType("plain/text");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(sendIntent);
            }
        });

    }
}

