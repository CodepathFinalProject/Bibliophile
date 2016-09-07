package com.codepath.bibliophile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.bibliophile.R;
import com.codepath.bibliophile.model.BookModel;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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

        public ImageView ivBookCover;
        private TextView tvBookTitle;
        private TextView tvBookAuthor;
        private RatingBar tvRating;
        private TextView tvPrice;
        private TextView tvRatingsCount;
        private TextView tvDistance;

        public TextView getTvDistance() {
            return tvDistance;
        }

        public TextView getTvRatingsCount() {
            return tvRatingsCount;
        }

        private CircleImageView cvSeller;

        public TextView getSellerName() {
            return sellerName;
        }


        private TextView sellerName;

        public CircleImageView getCvSeller() {
            return cvSeller;
        }

        private View view;

        public BookViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ivBookCover = (ImageView) itemView.findViewById(R.id.ivBookCover);
            tvBookTitle = (TextView) itemView.findViewById(R.id.tvBookTitle);
            tvBookAuthor = (TextView) itemView.findViewById(R.id.tvAuthorName);
            tvRating = (RatingBar) itemView.findViewById(R.id.rating);

            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            cvSeller = (CircleImageView) itemView.findViewById(R.id.seller_image);
            sellerName = (TextView) itemView.findViewById(R.id.seller_name);
            tvRatingsCount = (TextView) itemView.findViewById(R.id.ratings_count);
            tvDistance = (TextView) itemView.findViewById(R.id.tv_location);

        }

        public View getView() {
            return view;
        }

        public ImageView getIvBookCover() {
            return ivBookCover;
        }


        public TextView getTvBookTitle() {
            return tvBookTitle;
        }

        public TextView getTvBookAuthor() {
            return tvBookAuthor;
        }

        public RatingBar getTvRating() {
            return tvRating;
        }

        public TextView getTvPrice() {
            return tvPrice;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        Context context = getmContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View v1 = inflater.inflate(R.layout.home_recycler_view_item, parent, false);
        viewHolder = new BookViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final BookViewHolder vh1 = (BookViewHolder) holder;
        final BookModel book = mBook.get(position);
        if (book != null) {
            vh1.getTvBookTitle().setText(book.getTitle());
            String author = "by " + book.getAuthor();
            vh1.getTvBookAuthor().setText(author);
            String price = "$" + book.getPrice().toString();
            vh1.getTvPrice().setText(price);
            vh1.getTvRating().setRating((float) book.getAverageRating().doubleValue());
            try {
                ParseGeoPoint bookLocation = book.getSeller().fetchIfNeeded().getParseGeoPoint("coordinates");
                ParseGeoPoint myLocation = ParseUser.getCurrentUser().getParseGeoPoint("coordinates");
                String bookDistance = calculateDistanceBetween(myLocation, bookLocation);
                vh1.getTvDistance().setText(bookDistance);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            int ratingsCount = book.getRatingsCount();
            String ratingsCountFormattedString = "(" + String.valueOf(ratingsCount) + ")";
            vh1.getTvRatingsCount().setText(ratingsCountFormattedString);
            try {
                vh1.getSellerName().setText(book.getSeller().fetchIfNeeded().getUsername());
                Glide.with(mContext)
                        .load(book.getSeller().fetchIfNeeded().getString("profilePic"))
                        .into(vh1.getCvSeller());
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if (book.getBookCover() != null) {
                vh1.getIvBookCover().setVisibility(View.VISIBLE);
                Glide.with(mContext).
                        load(book.getBookCover()).override(128, 72).into(vh1.ivBookCover);
            } else {
                vh1.ivBookCover.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return mBook.size();
    }


    private String calculateDistanceBetween(ParseGeoPoint myLocation, ParseGeoPoint bookLocation) {
        Double distance = myLocation.distanceInMilesTo(bookLocation);
        return String.format("%.2f", distance) + " miles away";

    }

}
