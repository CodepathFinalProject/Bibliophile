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

import java.util.List;


public class BookShelfRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<BookModel> mBook;
    private Context mContext;

    public Context getmContext() {
        return mContext;
    }

    public BookShelfRecyclerViewAdapter(Context context, List<BookModel> model) {
        this.mBook = model;
        this.mContext = context;
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivBookCover;
        private TextView tvBookTitle;
        private TextView tvBookAuthor;
        private RatingBar tvRating;
        private TextView tvPrice;
        private TextView tvRatingsCount;
        private TextView tvListingStatus;
        private ImageView ivListingIcon;

        public ImageView getIvListingIcon() {
            return ivListingIcon;
        }

        public void setIvListingIcon(ImageView ivListingIcon) {
            this.ivListingIcon = ivListingIcon;
        }

        public TextView getTvListingStatus() {
            return tvListingStatus;
        }

        public void setTvListingStatus(TextView tvListingStatus) {
            this.tvListingStatus = tvListingStatus;
        }

        public TextView getTvRatingsCount() {
            return tvRatingsCount;
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
            tvRatingsCount = (TextView) itemView.findViewById(R.id.ratings_count);
            tvListingStatus = (TextView) itemView.findViewById(R.id.tv_listing_status);
            ivListingIcon = (ImageView) itemView.findViewById(R.id.listing_icon);

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

        View v1 = inflater.inflate(R.layout.bookshelf_recycler_view_item, parent, false);
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

            int ratingsCount = book.getRatingsCount();
            String ratingsCountFormattedString = "(" + String.valueOf(ratingsCount) + ")";
            vh1.getTvRatingsCount().setText(ratingsCountFormattedString);

            if (book.getBookCover() != null) {
                vh1.getIvBookCover().setVisibility(View.VISIBLE);
                Glide.with(mContext).
                        load(book.getBookCover()).override(128, 72).into(vh1.ivBookCover);
            } else {
                vh1.ivBookCover.setVisibility(View.GONE);
            }

            if (book.getIsListed()){
                vh1.getTvListingStatus().setText("Unlist");
                vh1.getIvListingIcon().setImageResource(R.drawable.ic_unlist);
            } else {
                vh1.getTvListingStatus().setText("List");
                vh1.getIvListingIcon().setImageResource(R.drawable.ic_unlist);
            }
        }
    }


    @Override
    public int getItemCount() {
        return mBook.size();
    }
}
