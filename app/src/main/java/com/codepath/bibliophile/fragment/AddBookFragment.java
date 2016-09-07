
package com.codepath.bibliophile.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.bibliophile.R;
import com.codepath.bibliophile.model.GoogleBookModel;

import org.parceler.Parcels;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddBookFragment extends Fragment {

    private GoogleBookModel googleBookModel;

    private ImageView ivBookThumbnail;
    private TextView tvBookTitle;
    private TextView tvBookSubtitle;
    private TextView tvBookDescription;
    private TextView tvBookAuthor;
    private TextView tvBookRatingsCount;
    private RatingBar rbBookRating;
    private Spinner spnBookCondition;
    private EditText etBookPrice;

    private Button btnPostBook;
    private Button btnCancelPost;

    private OnPostBookListener listener;

    public AddBookFragment() {
        // Required empty public constructor
    }

    // Tell the parent activity to dynamically embed a new fragment
    public interface OnPostBookListener {
        public void onPostClicked(GoogleBookModel listing);

        public void onCancelClicked();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPostBookListener) {
            listener = (OnPostBookListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement AddBookFragment.OnPostBookListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googleBookModel = Parcels.unwrap(getArguments().getParcelable("book"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_book, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivBookThumbnail = (ImageView) view.findViewById(R.id.ivBookThumbnail);
        tvBookTitle = (TextView) view.findViewById(R.id.tvBookTitle);
        tvBookSubtitle = (TextView) view.findViewById(R.id.tvBookSubtitle);
        tvBookAuthor = (TextView) view.findViewById(R.id.tvBookAuthor);
        tvBookRatingsCount = (TextView) view.findViewById(R.id.tvBookRatingsCount);
        tvBookDescription = (TextView) view.findViewById(R.id.tvBookDescription);
        spnBookCondition = (Spinner) view.findViewById(R.id.spnBookCondition);
        etBookPrice = (EditText) view.findViewById(R.id.etBookPrice);
        rbBookRating = (RatingBar) view.findViewById(R.id.rbBookRating);

        btnCancelPost = (Button) view.findViewById(R.id.btnCancelPost);
        btnPostBook = (Button) view.findViewById(R.id.btnPostBook);
        setButtonListeners();

        Glide.with(getContext()).load(googleBookModel.getThumbnailUrl()).into(ivBookThumbnail);
        tvBookTitle.setText(googleBookModel.getTitle());
        tvBookSubtitle.setText(googleBookModel.getSubtitle());
        tvBookAuthor.setText(googleBookModel.getPrimaryAuthor());

        tvBookDescription.setText(googleBookModel.getDescription());
        tvBookRatingsCount.setText(Integer.toString(googleBookModel.getRatingsCount()) + " ratings"); // TOOO use String.format
        rbBookRating.setRating((float) googleBookModel.getAverageRating());
    }

    private void setButtonListeners() {
        btnPostBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                googleBookModel.setCondition(spnBookCondition.getSelectedItem().toString());

                String price = etBookPrice.getText().toString();
                if (price.equals("")) {
                    Toast.makeText(getContext(), R.string.book_add_price_prompt, Toast.LENGTH_SHORT).show();
                } else {
                    googleBookModel.setPrice(Double.parseDouble(price));
                    listener.onPostClicked(googleBookModel);
                }

            }
        });

        btnCancelPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCancelClicked();
            }
        });
    }
}