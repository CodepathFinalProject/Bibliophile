package com.codepath.bibliophile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.bibliophile.R;

/**
 * Created by supriya on 8/22/16.
 */
public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static class BookViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivBookCover;
        private TextView tvBookTitle;
        private TextView tvBookAuthor;
        private TextView tvRating;
        private TextView tvBookDescription;
        private TextView tvPrice;

        public BookViewHolder(View itemView) {
            super(itemView);

            ivBookCover = (ImageView)itemView.findViewById(R.id.ivBookCover);
            tvBookTitle = (TextView)itemView.findViewById(R.id.tvBookTitle);
            tvBookAuthor = (TextView)itemView.findViewById(R.id.tvAuthorName);
//            tvRating = (TextView)itemView.findViewById(R.id.rating);
            tvBookDescription = (TextView)itemView.findViewById(R.id.tvDescription);
            tvPrice = (TextView)itemView.findViewById(R.id.tvPrice);
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

        public TextView getTvRating() {
            return tvRating;
        }

        public void setTvRating(TextView tvRating) {
            this.tvRating = tvRating;
        }

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
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View v1 = inflater.inflate(R.layout.home_recycler_view_item, parent, false);
        viewHolder = new BookViewHolder(v1);
        return viewHolder;    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BookViewHolder vh1 = (BookViewHolder) holder;


    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
