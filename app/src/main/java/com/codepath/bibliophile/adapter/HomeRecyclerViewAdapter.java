package com.codepath.bibliophile.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.bibliophile.R;
import com.codepath.bibliophile.activity.DetailsActivity;
import com.codepath.bibliophile.model.BookModel;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BookModel> mBook;
    private Context mContext;

    public Context getmContext() {
        return mContext;
    }

    public HomeRecyclerViewAdapter(Context context, List<BookModel> model) {
        this.mBook = model;
        this.mContext = context;
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private ImageView ivBookCover;
        private TextView tvBookTitle;
        private TextView tvBookAuthor;
        private RatingBar tvRating;
        private TextView tvBookDescription;
        private TextView tvPrice;
        private TextView tvBookOwner;
        private TextView tvDistance;
        private Button buttonBuy;

        public BookViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ivBookCover = (ImageView) itemView.findViewById(R.id.ivBookCover);
            tvBookTitle = (TextView) itemView.findViewById(R.id.tvBookTitle);
            tvBookAuthor = (TextView) itemView.findViewById(R.id.tvAuthorName);
            tvRating = (RatingBar) itemView.findViewById(R.id.rating);
            tvBookDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvBookOwner = (TextView) itemView.findViewById(R.id.tvBookOwner);
            tvDistance = (TextView) itemView.findViewById(R.id.tvDistance);

            buttonBuy = (Button) itemView.findViewById(R.id.buyButton);
        }

        public View getView() {
            return view;
        }

        public Button getButtonBuy() {
            return buttonBuy;
        }

        public void setButtonBuy(Button buttonBuy) {
            this.buttonBuy = buttonBuy;
        }

        public TextView getTvBookOwner() {
            return tvBookOwner;
        }

        public void setTvBookOwner(TextView tvBookOwner) {
            this.tvBookOwner = tvBookOwner;
        }

        public void setTvRating(RatingBar tvRating) {
            this.tvRating = tvRating;
        }

        public ImageView getIvBookCover() {
            return ivBookCover;
        }

        public void setIvBookCover(ImageView ivBookCover) {
            this.ivBookCover = ivBookCover;
        }

        public TextView getTvBookTitle() {
            return tvBookTitle;
        }

        public void setTvBookTitle(TextView tvBookTitle) {
            this.tvBookTitle = tvBookTitle;
        }

        public TextView getTvBookAuthor() {
            return tvBookAuthor;
        }

        public void setTvBookAuthor(TextView tvBookAuthor) {
            this.tvBookAuthor = tvBookAuthor;
        }

        public RatingBar getTvRating() {
            return tvRating;
        }

//        public void setTvRating(String tvRating) {
//            this.tvRating = tvRating;
//        }

        public TextView getTvBookDescription() {
            return tvBookDescription;
        }

        public void setTvBookDescription(TextView tvBookDescription) {
            this.tvBookDescription = tvBookDescription;
        }

        public TextView getTvPrice() {
            return tvPrice;
        }

        public void setTvPrice(TextView tvPrice) {
            this.tvPrice = tvPrice;
        }

        public TextView getTvDistance() {
            return tvDistance;
        }

        public void setTvDistance(TextView tvDistance) {
            this.tvDistance = tvDistance;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View v1 = inflater.inflate(R.layout.home_recycler_view_item, parent, false);
        viewHolder = new BookViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BookViewHolder vh1 = (BookViewHolder) holder;
        final BookModel book = (BookModel) mBook.get(position);
        String ownerName = null;
        String ownerEmail = null;

        if (book != null) {
            // Populate view items
            vh1.getTvBookTitle().setText(book.getTitle());
            vh1.getTvBookAuthor().setText(book.getAuthor());
            vh1.getTvBookDescription().setText(book.getDescription());
            vh1.getTvPrice().setText("$" + book.getPrice().toString());
            vh1.getTvRating().setRating((float) book.getAverageRating().doubleValue());

            // Calculate book's distance from current user
            ParseUser bookOwner = null;
            ParseGeoPoint bookLocation = null;
            ParseGeoPoint currentUserLocation = null;

            // Fetch the ParseGeoPoint of the book's owner using fetchIfNeeded
            try {
                bookOwner = book.getUser().fetchIfNeeded();
                bookLocation = (ParseGeoPoint) bookOwner.get("coordinates");
                ownerName = (String) bookOwner.get("username");
                ownerEmail = (String) bookOwner.get("email");

                currentUserLocation = (ParseGeoPoint) ParseUser.getCurrentUser().get("coordinates");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Double distance = round(bookLocation.distanceInMilesTo(currentUserLocation), 2);
            final String distanceLabel = Double.toString(distance) + " miles away";
            vh1.getTvDistance().setText(distanceLabel);
            vh1.getTvBookOwner().setText(ownerName);

            vh1.getButtonBuy().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setBuyer(book);
                    Toast.makeText(getmContext(), "Book moved to Transactions", Toast.LENGTH_SHORT).show();
                }
            });

            if (book.getBookCover() != null) {
                vh1.getIvBookCover().setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(book.getBookCover()).into(vh1.ivBookCover);
            } else {
                vh1.ivBookCover.setVisibility(View.GONE);
            }

            final String finalOwnerEmail = ownerEmail;
            final String finalOwnerName = ownerName;

            vh1.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getmContext(), "details", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getmContext(), DetailsActivity.class);
                    intent.putExtra("title", book.getTitle());
                    intent.putExtra("author", book.getAuthor());
                    intent.putExtra("description", book.getDescription());
                    intent.putExtra("price", book.getPrice().toString());
                    intent.putExtra("cover", book.getBookCover());
                    intent.putExtra("isbn", String.valueOf(book.getISBN()));
                    intent.putExtra("condition", book.getCondition());
                    intent.putExtra("ownerName", finalOwnerName);
                    intent.putExtra("ownerEmail", finalOwnerEmail);
                    intent.putExtra("distance", distanceLabel);
                    getmContext().startActivity(intent);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mBook.size();
    }

    private void setBuyer(BookModel book) {

//        ParseQuery<ParseObject> query = ParseQuery.getQuery("BookModel");
//        query.whereEqualTo("_id", book.getObjectId());

        final ParseUser user = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("BookModel");
        query.whereEqualTo("objectId", book.getObjectId());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                object.put("isListed", false);
                object.put("buyer", user);
                object.saveEventually();

            }
        });


//
//        book.setIsListed(false);
//        book.setIsTransactionComplete(false);
//        book.setBuyer(user);
//        final String currentUser = ParseUser.getCurrentUser().getEmail();
//        user.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                Log.d("Seller: ", currentUser);
//            }
//        });

//        Log.d("Buyer:", user.toString());
//        Log.d("User:", user.getEmail());
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
